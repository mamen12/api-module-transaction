package com.test.sigmatech.transaction.service;

import com.commons.beans.beans.PaymentRequest;
import com.commons.beans.beans.PaymentResponse;

public interface IPaymentService {
	public PaymentResponse savePayment(PaymentRequest request) throws Exception;
}
