package com.manulife.pension.ps.web.transaction;

import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsFTFReportData;
import com.manulife.pension.ps.web.report.ReportForm;

public class TransactionDetailsFTFForm extends ReportForm {

	private TransactionDetailsFTFReportData report = null;
	private String transactionNumber="";
	private String contractNumber="";
	private String participantId="";
	private String maskedSSN="";
	private String participantName="";
	private String profileId="";
	
	public TransactionDetailsFTFForm() {
		super();
	}
	
	public void setReport(TransactionDetailsFTFReportData report){
		this.report = report;
	}	

	public TransactionDetailsFTFReportData getReport(){
		return this.report;
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
	
	/**
	 * @deprecated use pptId
	 */
	public String getParticipantId() {
		return participantId;
	}
	
	/**
	 * @deprecated use pptId
	 */
	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}
	
	public String getPptId() {
		return participantId;
	}
	
	public void setPptId(String participantId) {
		this.participantId = participantId;
	}
	
	/**
	 * @return Returns the maskedSSN.
	 */
	public String getMaskedSSN() {
		return maskedSSN;
	}
	/**
	 * @param maskedSSN The maskedSSN to set.
	 */
	public void setMaskedSSN(String maskedSSN) {
		this.maskedSSN = maskedSSN;
	}
	/**
	 * @return Returns the participantName.
	 */
	public String getParticipantName() {
		return participantName;
	}
	/**
	 * @param participantName The participantName to set.
	 */
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	/**
	 * @return Returns the profileId.
	 */
	public String getProfileId() {
		return profileId;
	}
	/**
	 * @param profileId The profileId to set.
	 */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}
}


