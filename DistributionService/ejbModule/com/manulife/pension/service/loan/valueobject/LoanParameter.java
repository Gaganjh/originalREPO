package com.manulife.pension.service.loan.valueobject;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.manulife.pension.common.BaseSerializableCloneableObject;

public class LoanParameter extends BaseSerializableCloneableObject {

	private static final long serialVersionUID = 1L;

	private boolean readyToSave;
	private String statusCode;
	private BigDecimal maximumAvailable;
	private BigDecimal loanAmount;
	private BigDecimal paymentAmount;
	private String paymentFrequency;
	private Integer amortizationMonths;
	private BigDecimal interestRate;
	private Integer createdById;
	private Integer lastUpdatedById;
	private Timestamp created;
	private Timestamp lastUpdated;

	public Integer getAmortizationMonths() {
		return amortizationMonths;
	}

	public void setAmortizationMonths(Integer amortizationMonths) {
		this.amortizationMonths = amortizationMonths;
	}

	/**
	 * Returns the years portion when attribute amortizationMonths is converted
	 * to a years and months representation.
	 * @return
	 */
	public int getAmortizationPeriodYears() {
	    BigDecimal temp = new BigDecimal(getAmortizationMonths());
	    return temp.divide(new BigDecimal(12), BigDecimal.ROUND_DOWN).intValue();
	}

	/**
     * Returns the months portion when attribute amortizationMonths is converted
     * to a years and months representation.
     * @return
     */
    public int getAmortizationPeriodMonths() {
        return getAmortizationMonths() % 12;
    }
	
	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public BigDecimal getMaximumAvailable() {
		return maximumAvailable;
	}

	public void setMaximumAvailable(BigDecimal maximumAvailable) {
		this.maximumAvailable = maximumAvailable;
	}

	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getPaymentFrequency() {
		return paymentFrequency;
	}

	public void setPaymentFrequency(String paymentFrequency) {
		this.paymentFrequency = paymentFrequency;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public Integer getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Integer createdById) {
		this.createdById = createdById;
	}

	public Integer getLastUpdatedById() {
		return lastUpdatedById;
	}

	public void setLastUpdatedById(Integer lastUpdatedById) {
		this.lastUpdatedById = lastUpdatedById;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Timestamp getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Timestamp lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public boolean isReadyToSave() {
		return readyToSave;
	}

	public void setReadyToSave(boolean readyToSave) {
		this.readyToSave = readyToSave;
	}
}
