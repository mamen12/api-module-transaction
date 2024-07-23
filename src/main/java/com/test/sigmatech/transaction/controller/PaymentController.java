package com.test.sigmatech.transaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.commons.beans.beans.PaymentRequest;
import com.commons.beans.beans.Request;
import com.commons.beans.beans.Response;
import com.commons.beans.constant.ApiResponse;
import com.test.sigmatech.transaction.service.IPaymentService;

@RestController
@RequestMapping("/api/trx")
public class PaymentController {

	@Autowired
	private IPaymentService trxService;

	@RequestMapping(value = "/payment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<String> trxPayment(@RequestBody Request<PaymentRequest> rq) {
		Response<String> response = new Response<>();

		try {
			trxService.savePayment(rq.getRequestPayload());
			response.setStatusResponse(ApiResponse.SUCCESS);
		} catch (Exception e) {
			response.setStatusResponse(ApiResponse.FAILED);
		}
		return response;
	}

}
