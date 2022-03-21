package com.manulife.pension.ps.web.transaction;

import com.manulife.pension.ps.web.report.ReportForm;

/**
 * @author Maria Lee
 */
public class LinkToLoanRepaymentDetailsReportForm extends ReportForm {
	
	public static final String DATE_ENTRY_FORMAT = "MM/dd/yyyy";
	public static final String DATE_IN_PATTERN = "yyyy-MM-dd";
	public static final String PARAMETER_KEY_PRTID = "participantId";
	public static final String PARAMETER_KEY_CONTRACT_NUMBER = "contractNumber";
	public static final String PARAMETER_KEY_TRANSACTION_DATE = "transactionDate";
	public static final String PARAMETER_KEY_TRANSACTION_TYPE = "type";
	public static final String PARAMETER_KEY_TRANSACTION_NUMBER = "transactionNumber";
	
	private String transactionDate;
	private String transactionType;
	private String transactionNumber;
	private String profileId;
	private String participantId;
	private String contractNumber;
	private String application = "PS";
	
	/**
	 * 
	 */
	public LinkToLoanRepaymentDetailsReportForm() {
		super();
	}

	/**
	 * Gets the transactionType
	 * @return Returns a String
	 */
	public String getTransactionType() {
		return transactionType;
	}
	/**
	 * Sets the transactionType
	 * @param transactionType The transactionType to set
	 */
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}


}
