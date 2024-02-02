package com.test.sigmatech.transaction.service.impl;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.commons.beans.beans.PaymentRequest;
import com.commons.beans.beans.PaymentResponse;
import com.commons.beans.beans.Request;
import com.commons.beans.beans.UserRequest;
import com.commons.beans.beans.UserResponse;
import com.commons.beans.beans.WalletRequest;
import com.commons.beans.beans.WalletResponse;
import com.commons.beans.constant.AppConstants;
import com.commons.beans.constant.TransactionCode;
import com.test.sigmatech.transaction.model.Payment;
import com.test.sigmatech.transaction.repository.PaymentRepository;
import com.test.sigmatech.transaction.service.IPaymentService;

@Service
public class PaymentServiceImpl implements IPaymentService{

	@Autowired
	private PaymentRepository repo;
	
	@Autowired
	private RestTemplate restTemplate;
	
	private static String url_balance = "http://localhost:8082/api/wallet/balance";
	private static String url_reduce_balance = "http://localhost:8082/api/wallet/update_saldo";
	private static String url_account = "http://localhost:8082/api/acct/detail";
	
	@Override
	public PaymentResponse savePayment(PaymentRequest rq) throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		Request<UserRequest> userRequest = new Request<UserRequest>();
		Request<WalletRequest> walletRq = new Request<WalletRequest>();
		
		String uuidPayment = UUID.randomUUID().toString();
		
		PaymentResponse resp = new PaymentResponse();
		if (repo.countPaymentPending(rq.getAccountNo(), rq.getTranCode(), AppConstants.STATUS_PENDING) < 1) {
			Payment payment = new Payment();
			payment.setAccountNo(rq.getAccountNo());
			payment.setPaymentCode(rq.getTranCode());
			payment.setAmount(rq.getAmount());
			payment.setCreatedAt(new Date());
			payment.setCreatedBy(rq.getIdUser());
			payment.setStatusTrx(AppConstants.STATUS_SAVED);
			payment.setVersion(1);
			payment.setIdPayment(uuidPayment);
			
			try {
				Future<UserResponse> f1 = executor.submit(new Callable<UserResponse>() {
					@Override
					public UserResponse call() throws Exception {
						UserRequest userPayload = new UserRequest();
						userPayload.setId(rq.getIdUser());
						
						userRequest.setRequestPayload(userPayload);
						UserResponse ur = restTemplate.postForObject(url_account, userRequest, UserResponse.class);
						return ur;
					}
				});
				
				Future<WalletResponse> f2 = executor.submit(new Callable<WalletResponse>() {

					@Override
					public WalletResponse call() throws Exception {
						WalletRequest walletPayload = new WalletRequest();
						walletPayload.setAccountNo(rq.getAccountNo());
						walletPayload.setBallance(rq.getAmount());
						walletRq.setRequestPayload(walletPayload);
						WalletResponse respBalance = restTemplate.postForObject(url_balance, walletRq, WalletResponse.class);
						return respBalance;
					}
				});
				
				
				UserResponse userResponse = f1.get();
				WalletResponse respWallet = f2.get();
			
				payment.setAccountName(userResponse.getName());
				
				// insert database payment
				repo.save(payment);
				
				if (!respWallet.getIsInsufficient().equals(AppConstants.BALANCE)) {
					payment.setStatusTrx(AppConstants.STATUS_FAILED);
				}else {
					respWallet = restTemplate.postForObject(url_reduce_balance, walletRq, WalletResponse.class);
					if (respWallet.getReduced()) {
						//versioning trx
						payment.setStatusTrx(AppConstants.STATUS_COMPLETED);
					}
				}
				
				payment.setVersion(payment.getVersion() + 1);
				payment.setUpdatedAt(new Date());
				payment.setUpdatedBy(AppConstants.SYSTEM);
				payment.setEffectiveDate(new Date());
				repo.save(payment);
				
				resp.setStatusTransaction(payment.getStatusTrx());
				resp.setPaymentDesc(TransactionCode.getTransactionCodeDesc(rq.getTranCode()));
			} catch (Exception e) {
				throw e;
			}
		}else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gagal Melanjutkan dikarenakan sudah ada transaksi yang berjalan");
		}
		return resp;
	}
	
}
