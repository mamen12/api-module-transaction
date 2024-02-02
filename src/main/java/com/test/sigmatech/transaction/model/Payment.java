package com.test.sigmatech.transaction.model;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name ="tbl_mt_payment")
public class Payment {

	@Id
	@Column(name = "payment_id")
	private String idPayment;
	
	@Column(name = "account_no")
	private String accountNo;
	
	@Column(name="effective_date")
	private Date effectiveDate;
	
	private BigDecimal amount;
	
	@Column(name = "payment_code")
	private String paymentCode;
	
	@Column(name = "transaction_status")
	private String statusTrx;
	
	@Column(name = "created_at")
	private Date createdAt;
	@Column(name = "created_by")
	private String createdBy;
	@Column(name = "updated_at")
	private Date updatedAt;
	@Column(name = "updated_by")
	private String updatedBy;
	@Column(name = "version")
	private Integer version;
	@Column(name = "is_del")
	private String isDel;
	
	@Column(name = "account_name")
	private String accountName;

	public String getIdPayment() {
		return idPayment;
	}

	public void setIdPayment(String idPayment) {
		this.idPayment = idPayment;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getPaymentCode() {
		return paymentCode;
	}

	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}

	public String getStatusTrx() {
		return statusTrx;
	}

	public void setStatusTrx(String statusTrx) {
		this.statusTrx = statusTrx;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getIsDel() {
		return isDel;
	}

	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
}
