package com.manulife.pension.service.loan.valueobject;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.service.distribution.valueobject.PaymentInstruction;

public class LoanPaymentInstruction extends BaseSerializableCloneableObject implements PaymentInstruction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer createdById;
	private Integer lastUpdatedById;
	private Timestamp created;
	private Timestamp lastUpdated;
    private String bankAccountTypeCode;
    private Integer bankTransitNumber;
    private String bankAccountNumber;
    private String bankName;
    private String attentionName;
    private String creditPartyName;
    private Integer payeeNo;
    private Integer recipientNo;	
    private Integer submissionId;
    private BigDecimal paymentAmount;
	
	public Integer getSubmissionId() {
		return submissionId;
	}
	public void setSubmissionId(Integer submissionId) {
		this.submissionId = submissionId;
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
	public String getBankAccountTypeCode() {
		return bankAccountTypeCode;
	}
	public void setBankAccountTypeCode(String bankAccountTypeCode) {
		this.bankAccountTypeCode = bankAccountTypeCode;
	}
	public Integer getBankTransitNumber() {
		return bankTransitNumber;
	}
	public void setBankTransitNumber(Integer bankTransitNumber) {
		this.bankTransitNumber = bankTransitNumber;
	}
	public String getBankAccountNumber() {
		return bankAccountNumber;
	}
	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getAttentionName() {
		return attentionName;
	}
	public void setAttentionName(String attentionName) {
		this.attentionName = attentionName;
	}
	public String getCreditPartyName() {
		return creditPartyName;
	}
	public void setCreditPartyName(String creditPartyName) {
		this.creditPartyName = creditPartyName;
	}
	public Integer getPayeeNo() {
		return payeeNo;
	}
	public void setPayeeNo(Integer payeeNo) {
		this.payeeNo = payeeNo;
	}
	public Integer getRecipientNo() {
		return recipientNo;
	}
	public void setRecipientNo(Integer recipientNo) {
		this.recipientNo = recipientNo;
	}
	public boolean isBlank() {
        return (StringUtils.isBlank(getBankAccountTypeCode())
                && StringUtils.isBlank(getBankAccountNumber())
                && StringUtils.isBlank(getBankName()) && StringUtils.isBlank(getAttentionName())
                && StringUtils.isBlank(getCreditPartyName()) && (getBankTransitNumber() == null)
                && (getRecipientNo() == null) && (getPayeeNo() == null));
	}

	@Override
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}
	@Override
	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}	

}
