package com.test.sigmatech.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.test.sigmatech.transaction.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

	@Query("SELECT COUNT(u) FROM Payment u WHERE u.accountNo=?1 AND u.paymentCode=?2 AND u.statusTrx=?3")
	public Integer countPaymentPending(String accountNo, String paymentCode, String statusTrx);
	
}
