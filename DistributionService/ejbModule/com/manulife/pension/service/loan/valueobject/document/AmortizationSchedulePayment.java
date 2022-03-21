package com.manulife.pension.service.loan.valueobject.document;

import java.math.BigDecimal;
import java.util.Date;

import com.manulife.pension.common.BaseSerializableCloneableObject;

public class AmortizationSchedulePayment extends
		BaseSerializableCloneableObject {

	private static final long serialVersionUID = 1L;
	private BigDecimal paymentAmount = null;
	private BigDecimal paymentAmountPrincipal = null;
	private BigDecimal paymentAmountInterest = null;

	private Date paymentDate = null;

	// all totals are to be post payment
	private BigDecimal totalPaymentAmount = null;
	private BigDecimal totalPaymentAmountInterest = null;
	private BigDecimal loanPrincipalRemaining = null;

	public AmortizationSchedulePayment() {
		super();
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Date getPaymentDate() {
		return this.paymentDate;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public BigDecimal getPaymentAmount() {
		return this.paymentAmount;
	}

	public void setPaymentAmountPrincipal(BigDecimal paymentAmountPrincipal) {
		this.paymentAmountPrincipal = paymentAmountPrincipal;
	}

	public BigDecimal getPaymentAmountPrincipal() {
		return this.paymentAmountPrincipal;
	}

	public void setPaymentAmountInterest(BigDecimal paymentAmountInterest) {
		this.paymentAmountInterest = paymentAmountInterest;
	}

	public BigDecimal getPaymentAmountInterest() {
		return this.paymentAmountInterest;
	}

	public void setTotalPaymentAmount(BigDecimal totalPaymentAmount) {
		this.totalPaymentAmount = totalPaymentAmount;
	}

	public BigDecimal getTotalPaymentAmount() {
		return this.totalPaymentAmount;
	}

	public void setTotalPaymentAmountInterest(
			BigDecimal totalPaymentAmountInterest) {
		this.totalPaymentAmountInterest = totalPaymentAmountInterest;
	}

	public BigDecimal getTotalPaymentAmountInterest() {
		return this.totalPaymentAmountInterest;
	}

	public void setLoanPrincipalRemaining(BigDecimal loanPrincipalRemaining) {
		this.loanPrincipalRemaining = loanPrincipalRemaining;
	}

	public BigDecimal getLoanPrincipalRemaining() {
		return this.loanPrincipalRemaining;
	}
}
