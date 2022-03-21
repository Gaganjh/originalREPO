/*
 * Created on Nov 20, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.manulife.pension.ps.service.submission.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.manulife.pension.lp.model.gft.PaymentInstruction;

/**
 * @author tomasto
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class SubmissionPaymentItem extends SubmissionHistoryItem implements Serializable {
	
    private Date requestedPaymentEffectiveDate;
    private BigDecimal totalContributionPayment;
    private BigDecimal totalBillPayment;
    private BigDecimal totalCreditPayment;
    private PaymentInstruction paymentInstructions[];


	/**
	 * 
	 */
	public SubmissionPaymentItem() {
		super();
		// TODO Auto-generated constructor stub
	}


	/**
	 * @return Returns the paymentInstructions.
	 */
	public PaymentInstruction[] getPaymentInstructions() {
		return paymentInstructions;
	}
	/**
	 * @param paymentInstructions The paymentInstructions to set.
	 */
	public void setPaymentInstructions(PaymentInstruction[] paymentInstructions) {
		this.paymentInstructions = paymentInstructions;
	}
	/**
	 * @return Returns the requestedPaymentEffectiveDate.
	 */
	public Date getRequestedPaymentEffectiveDate() {
		return requestedPaymentEffectiveDate;
	}
	/**
	 * @param requestedPaymentEffectiveDate The requestedPaymentEffectiveDate to set.
	 */
	public void setRequestedPaymentEffectiveDate(
			Date requestedPaymentEffectiveDate) {
		this.requestedPaymentEffectiveDate = requestedPaymentEffectiveDate;
	}
	/**
	 * @return Returns the totalBillPayment.
	 */
	public BigDecimal getTotalBillPayment() {
		return totalBillPayment;
	}
	/**
	 * @param totalBillPayment The totalBillPayment to set.
	 */
	public void setTotalBillPayment(BigDecimal totalBillPayment) {
		this.totalBillPayment = totalBillPayment;
	}
	/**
	 * @return Returns the totalContributionPayment.
	 */
	public BigDecimal getTotalContributionPayment() {
		return totalContributionPayment;
	}
	/**
	 * @param totalContributionPayment The totalContributionPayment to set.
	 */
	public void setTotalContributionPayment(BigDecimal totalContributionPayment) {
		this.totalContributionPayment = totalContributionPayment;
	}
	/**
	 * @return Returns the totalCreditPayment.
	 */
	public BigDecimal getTotalCreditPayment() {
		return totalCreditPayment;
	}
	/**
	 * @param totalCreditPayment The totalCreditPayment to set.
	 */
	public void setTotalCreditPayment(BigDecimal totalCreditPayment) {
		this.totalCreditPayment = totalCreditPayment;
	}
}
