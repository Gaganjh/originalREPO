package com.manulife.pension.bd.web.bob.transaction;

import com.manulife.pension.platform.web.report.BaseReportForm;

/**
 * This is the form bean for LinkToLoanRepaymentDetailsReportAction
 */
public class LinkToLoanRepaymentDetailsReportForm extends BaseReportForm {
	
    private static final long serialVersionUID = 1L;

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

	/**
     * Gets the transactionNumber
     * @return Returns a String
     */
	public String getTransactionNumber() {
		return transactionNumber;
	}

	/**
     * Sets the transactionNumber
     * @param transactionNumber The transactionNumber to set
     */
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	/**
     * Gets the contractNumber
     * @return Returns a String
     */
	public String getContractNumber() {
		return contractNumber;
	}

	/**
     * Sets the contractNumber
     * @param contractNumber The contractNumber to set
     */
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	/**
     * Gets the application
     * @return Returns a String
     */
	public String getApplication() {
		return application;
	}

	/**
     * Sets the application
     * @param application The application to set
     */
	public void setApplication(String application) {
		this.application = application;
	}

	/**
     * Gets the transactionDate
     * @return Returns a String
     */
	public String getTransactionDate() {
		return transactionDate;
	}

	/**
     * Sets the transactionDate
     * @param transactionDate The transactionDate to set
     */
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
     * Gets the profileId
     * @return Returns a String
     */
	public String getProfileId() {
		return profileId;
	}

	/**
     * Sets the profileId
     * @param profileId The profileId to set
     */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	/**
     * Gets the participantId
     * @return Returns a String
     */
	public String getParticipantId() {
		return participantId;
	}

	/**
     * Sets the participantId
     * @param participantId The participantId to set
     */
	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}
}
