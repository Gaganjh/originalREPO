package com.manulife.pension.service.distribution.valueobject;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface PaymentInstruction {

	String BANK_ACCOUNT_TYPE_SAVING = "S";
    String BANK_ACCOUNT_TYPE_SAVING_DESC = "Savings";

	String BANK_ACCOUNT_TYPE_CHECKING = "C";
    String BANK_ACCOUNT_TYPE_CHECKING_DESC = "Checking";

	Integer getCreatedById();

	void setCreatedById(final Integer createdById);

	Timestamp getCreated();

	void setCreated(final Timestamp created);

	Integer getLastUpdatedById();

	void setLastUpdatedById(final Integer lastUpdatedById);

	Timestamp getLastUpdated();

	void setLastUpdated(final Timestamp lastUpdated);

	String getBankAccountTypeCode();

	void setBankAccountTypeCode(String bankAccountTypeCode);

	Integer getBankTransitNumber();

	void setBankTransitNumber(Integer bankTransitNumber);

	String getBankAccountNumber();

	void setBankAccountNumber(String bankAccountNumber);

	String getBankName();

	void setBankName(String bankName);

	String getAttentionName();

	void setAttentionName(String attentionName);

	String getCreditPartyName();

	void setCreditPartyName(String creditPartyName);

	Integer getPayeeNo();

	void setPayeeNo(Integer payeeNo);

	Integer getRecipientNo();

	void setRecipientNo(Integer recipientNo);

	Integer getSubmissionId();

	void setSubmissionId(Integer submissionId);

	boolean isBlank();
	
	BigDecimal getPaymentAmount();
	void setPaymentAmount(BigDecimal paymentAmount);

}
