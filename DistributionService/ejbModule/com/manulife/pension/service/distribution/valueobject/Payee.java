package com.manulife.pension.service.distribution.valueobject;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface Payee {

	/**
	 * Payee type code for Beneficiary.
	 */
	String TYPE_CODE_BENEFICIARY = "BE";

	/**
	 * Payee type code for Financial Institution.
	 */
	String TYPE_CODE_FINANCIAL_INSTITUTION = "FI";

	/**
	 * Payee type code for Participant.
	 */
	String TYPE_CODE_PARTICIPANT = "PA";

	/**
	 * Payee type code for Trustee.
	 */
	String TYPE_CODE_TRUSTEE = "TR";

	/**
	 * Payee reason code for payment.
	 */
	String REASON_CODE_PAYMENT = "P";

	/**
	 * Payee reason code for rollover.
	 */
	String REASON_CODE_ROLLOVER = "R";

	/**
	 * Payment method code for Wire Transfer.
	 */
	String WIRE_PAYMENT_METHOD_CODE = "WT";
    String WIRE_PAYMENT_METHOD_CODE_DESC = "Wire";

	/**
	 * Payment method code for Automated Clearing House.
	 */
	String ACH_PAYMENT_METHOD_CODE = "AC";
    String ACH_PAYMENT_METHOD_CODE_DESC = "Direct Deposit";

	/**
	 * Payment method code for Check.
	 */
	String CHECK_PAYMENT_METHOD_CODE = "CH";
    String CHECK_PAYMENT_METHOD_CODE_DESC = "Check";

	/**
	 * Bank account type code for Checking.
	 */
	String CHECKING_ACCOUNT_TYPE_CODE = "C";

	/**
	 * Bank account type code for Saving.
	 */
	String SAVINGS_ACCOUNT_TYPE_CODE = "S";

	/**
	 * IRS Distribution Type Code for Early Distribution Under 59.5.
	 */
	String IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5 = "1  ";

	/**
	 * IRS Distribution Type Code for Early Distribution Under 59.5 with
	 * exception.
	 */
	String IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_WITH_EXCEPTION = "2  ";

	/**
	 * IRS Distribution Type Code for Early Distribution Under 59.5 10 year tax
	 * option.
	 */
	String IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_TEN_YEAR_OPTION = "2A ";

	/**
	 * IRS Distribution Type Code for Disability.
	 */
	String IRS_DISTRIBUTION_CODE_DISABILITY = "3  ";

	/**
	 * IRS Distribution Type Code for Rollover.
	 */
	String IRS_DISTRIBUTION_CODE_DISABILITY_TEN_YEAR_OPTION = "3A ";

	/**
	 * IRS Distribution Type Code for Normal Distribution Over 59.5.
	 */
	String IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5 = "7  ";

	/**
	 * IRS Distribution Type Code for Normal Distribution Over 59.5.
	 */
	String IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5_TEN_YEAR_OPTION = "7A ";

	/**
	 * IRS Distribution Type Code for Rollover.
	 */
	String IRS_DISTRIBUTION_CODE_ROLLOVER = "G  ";

	/**
	 * Default plan name.
	 */
	String DEFAULT_ROLLOVER_PLAN_NAME = "<enter name of plan>";

	/**
	 * ABA Number Length.
	 */
	int BANK_ABA_NUMBER_LENGTH = 9;

	/**
	 * ABA Number Minimum Length.
	 */
	int BANK_ABA_NUMBER_MINIMUM_VALUE = 0;

	/**
	 * ABA Number Maximum Length.
	 */
	int BANK_ABA_NUMBER_MAXIMUM_VALUE = 999999999;
	
	String NON_TAXABLE_AMOUNT ="nonTaxableAmount";
	String TOTAL_ROTH_BALANCE ="totalRothBalance";
	String FOLLOWING_AMOUNT ="followingAmount";
    
	String NONTAXABLE_TRADITIONAL_IRA ="nonTaxableTraditionalIRA";
	String NONTAXABLE_ROTH_IRA ="nonTaxableRothIRA";
	String NONTAXABLE_EMPSPONSORED_QUALIFIEDPLAN ="nonTaxableEmpSponsoredQualifiedPlan";

     String TAXABLE_TRADITIONAL_IRA ="taxableTraditionalIRA";
     String TAXABLE_ROTH_IRA ="taxableRothIRA";
     String TAXABLE_EMPSPONSORED_QUALIFIEDPLAN ="taxableEmpSponsoredQualifiedPlan";

     String ROTH_BALANCE_ROTHIRA ="rothBalanceRothIRA";
     String ROTH_BALANCE_EMPSPONSORED_QUALIFIEDPLAN ="rothBalanceEmpSponsoredQualifiedPlan";

	Integer getCreatedById();

	void setCreatedById(final Integer createdById);

	Timestamp getCreated();

	void setCreated(final Timestamp created);

	Integer getLastUpdatedById();

	void setLastUpdatedById(final Integer lastUpdatedById);

	Timestamp getLastUpdated();

	void setLastUpdated(final Timestamp lastUpdated);

	Integer getRecipientNo();

	void setRecipientNo(Integer recipientNo);

	Integer getPayeeNo();

	void setPayeeNo(Integer payeeNo);

	String getFirstName();

	void setFirstName(String firstName);

	String getLastName();

	void setLastName(String lastName);

	String getOrganizationName();

	void setOrganizationName(String organizationName);

	String getTypeCode();

	void setTypeCode(String typeCode);

	String getReasonCode();

	void setReasonCode(String reasonCode);

	String getPaymentMethodCode();

	void setPaymentMethodCode(String paymentMethodCode);

	String getShareTypeCode();

	void setShareTypeCode(String shareTypeCode);

	BigDecimal getShareValue();

	void setShareValue(BigDecimal shareValue);

	String getRolloverAccountNo();

	void setRolloverAccountNo(String rolloverAccountNo);

	String getRolloverPlanName();

	void setRolloverPlanName(String rolloverPlanName);

	String getIrsDistCode();

	void setIrsDistCode(String irsDistCode);

	Boolean getMailCheckToAddress();

	void setMailCheckToAddress(Boolean mailCheckToAddress);

	Boolean getSendCheckByCourier();

	void setSendCheckByCourier(Boolean sendCheckByCourier);

	String getCourierCompanyCode();

	void setCourierCompanyCode(String courierCompanyCode);

	String getCourierNo();

	void setCourierNo(String courierNo);

	DistributionAddress getAddress();

	void setAddress(DistributionAddress address);

	DistributionAddress getDefaultAddress();

	void setDefaultAddress(DistributionAddress defaultAddress);

	PaymentInstruction getPaymentInstruction();

	void setPaymentInstruction(PaymentInstruction paymentInstruction);

	void setSubmissionId(Integer submissionId);

	Integer getSubmissionId();
	
	String getTaxes();
	void setTaxes(String taxes);
	
	String getParticipant();
	void setParticipant(String participant);

	String getRolloverType();
	void setRolloverType(String rolloverType);

}
