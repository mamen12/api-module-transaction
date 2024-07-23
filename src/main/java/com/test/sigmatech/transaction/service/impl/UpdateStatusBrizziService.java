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
@EnableAsync
@EnableScheduling
public class UpdateStatusBrizziService {
	private  Logger log = LoggerFactory.getLogger(UpdateStatusBrizziService.class);
	@Autowired
	private RestTemplate restTemplate;

	private ScheduledFuture<?> scheduledFuture;
	
	@Autowired
	private PaymentRepository repo;
	
//	private String urlTrx;
//	private Payment paymentTrx;
//	private Request<WalletRequest> request;

	
	@Async
	public void updateStatusPending(WalletRequest rq, String url, Payment payment) {
		final AtomicInteger executionCount = new AtomicInteger(0);
		final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		final long delay = 3000L;
		final int maxExecution = 3;

		Request<WalletRequest> request = new Request<WalletRequest>();
		request.setRequestPayload(rq);
		
		
		RequsetHeader rqHeader = new RequsetHeader();
		rqHeader.setChanel("API PAYMENT");
		rqHeader.setRequestId(UUID.randomUUID().toString());
		request.setRequestHeader(rqHeader);

		scheduler.initialize();
		
		scheduler.scheduleWithFixedDelay(() -> {
			int currentCount = executionCount.incrementAndGet();
			if (currentCount > maxExecution) {
				log.info("selesai");
				scheduler.shutdown();
			}else {
				log.info("update status job run");
				try {
					request.getRequestPayload().setCountHit(currentCount);
					WalletResponse respWallet = restTemplate.postForObject(url, request, WalletResponse.class);
					log.info(respWallet.toString());
					if (respWallet.getReduced()) {
						payment.setStatusTrx(AppConstants.STATUS_COMPLETED);
						repo.save(payment);
						log.info("success");
						scheduler.shutdown();
						
					}
				} catch (Exception e) {
					log.error("update status exception");
				}
			}
			log.info("update status counter : {}", currentCount);
			}, Instant.now().plusMillis(delay), Duration.ofSeconds(delay));
	}

	private Boolean berhasil(Boolean balikan) {
		balikan = true;
		return balikan;
	}
	
	private void shutdownScheduler(ThreadPoolTaskScheduler scheduler) {
		if (scheduledFuture != null) {
			scheduledFuture.cancel(false);
		}
		scheduler.shutdown();
	}
	
}
