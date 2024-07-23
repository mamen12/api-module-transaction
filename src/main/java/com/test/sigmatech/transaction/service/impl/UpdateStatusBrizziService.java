package com.test.sigmatech.transaction.service.impl;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.commons.beans.beans.Request;
import com.commons.beans.beans.RequsetHeader;
import com.commons.beans.beans.WalletRequest;
import com.commons.beans.beans.WalletResponse;
import com.commons.beans.constant.AppConstants;
import com.test.sigmatech.transaction.model.Payment;
import com.test.sigmatech.transaction.repository.PaymentRepository;

@Service
public class UpdateStatusBrizziService {
	private  Logger log = LoggerFactory.getLogger(UpdateStatusBrizziService.class);
	@Autowired
	private RestTemplate restTemplate;

	private ScheduledFuture<?> scheduledFuture;
	
	@Autowired
	private PaymentRepository repo;
	
	private static String url_balance = "http://localhost:8082/api/wallet/balance";
	private static String url_reduce_balance = "http://localhost:8082/api/wallet/update_saldo";
	private static String url_account = "http://localhost:8082/api/acct/detail";
	
	private Payment paymentTrx;
	private Request<WalletRequest> request;
	private final AtomicInteger executionCount = new AtomicInteger(0);
	private final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
	final long delay = 3000L;
	final int maxExecution = 3;
	
	@Async
	public void updateStatusPending(WalletRequest rq, String url, Payment payment) {
		paymentTrx = payment;
		scheduler.setPoolSize(5);
		scheduler.setThreadNamePrefix("thread update status-");
		

		request = new Request<WalletRequest>();
		request.setRequestPayload(rq);
		
		
		RequsetHeader rqHeader = new RequsetHeader();
		rqHeader.setChanel("API PAYMENT");
		rqHeader.setRequestId(UUID.randomUUID().toString());
		request.setRequestHeader(rqHeader);

		scheduler.initialize();
		
		scheduledFuture = scheduler.scheduleWithFixedDelay(this::berhasil,Instant.now().plusMillis(delay), Duration.ofMillis(delay));
	}

	private void berhasil() {
		int currentCount = executionCount.incrementAndGet();
		request.getRequestPayload().setCountHit(currentCount);
		WalletResponse respWallet = restTemplate.postForObject(url_reduce_balance, request, WalletResponse.class);
		if (currentCount > maxExecution) {
			log.info("selesai");
			shutdownScheduler();
		}else {
			log.info("update status job run");
			try {
				log.info(respWallet.toString());
				if (respWallet.getReduced()) {
					paymentTrx.setStatusTrx(AppConstants.STATUS_COMPLETED);
					repo.save(paymentTrx);
					log.info("success");
					shutdownScheduler();
					
				}
			} catch (Exception e) {
				log.error(e.getMessage());
				log.error("update status exception");
			}
		}
		log.info("update status counter : {}", currentCount);
	}
	
	private void shutdownScheduler() {
		if (scheduledFuture != null) {
			scheduledFuture.cancel(false);
		}
		scheduler.shutdown();
	}
	
}
