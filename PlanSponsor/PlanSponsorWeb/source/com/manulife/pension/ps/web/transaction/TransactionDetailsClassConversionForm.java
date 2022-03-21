package com.manulife.pension.ps.web.transaction;

import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsClassConversionReportData;
import com.manulife.pension.ps.web.report.ReportForm;

public class TransactionDetailsClassConversionForm extends ReportForm {

	private TransactionDetailsClassConversionReportData report = null;
	private String transactionNumber="";
	private String contractNumber="";
	private String participantId="";
	private String maskedSSN="";
	private String participantName="";
	private String profileId="";
	
	public TransactionDetailsClassConversionForm() {
		super();
	}
	
	public void setReport(TransactionDetailsClassConversionReportData report){
		this.report = report;
	}	

	public TransactionDetailsClassConversionReportData getReport(){
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


