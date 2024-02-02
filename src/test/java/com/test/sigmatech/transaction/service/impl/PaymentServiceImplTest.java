package com.test.sigmatech.transaction.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.commons.beans.beans.PaymentRequest;
import com.commons.beans.beans.PaymentResponse;
import com.commons.beans.constant.AppConstants;
import com.commons.beans.constant.TransactionCode;
import com.test.sigmatech.transaction.service.IPaymentService;

@SpringBootTest
class PaymentServiceImplTest {
	
	@Autowired
	private IPaymentService paymentService;
	
//	@Test
//	void test() throws Exception {
//		PaymentRequest request = new PaymentRequest();
//		
//		request.setIdUser("02a91722-3209-4284-a20d-e852631fefe7");
//		request.setAccountNo("0247492683197");
//		request.setAmount(new BigDecimal(80000));
//		request.setTranCode(TransactionCode.GOJEK.getCode());
//		
//		PaymentResponse resp = paymentService.savePayment(request);
//		assertEquals(resp.getPaymentDesc(), TransactionCode.GOJEK.getDesc());
//	}

	@Test
	void testFailed() throws Exception {
		PaymentRequest request = new PaymentRequest();
		
		request.setIdUser("02a91722-3209-4284-a20d-e852631fefe7");
		request.setAccountNo("0247492683197");
		request.setAmount(new BigDecimal(80000));
		request.setTranCode(TransactionCode.GOJEK.getCode());
		
		PaymentResponse resp = paymentService.savePayment(request);
		assertEquals(resp.getStatusTransaction(), AppConstants.STATUS_FAILED);
	}
}
