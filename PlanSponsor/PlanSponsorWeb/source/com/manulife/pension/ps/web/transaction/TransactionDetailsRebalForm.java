package com.manulife.pension.ps.web.transaction;

import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsRebalReportData;
import com.manulife.pension.ps.web.report.ReportForm;

public class TransactionDetailsRebalForm extends ReportForm {

	private TransactionDetailsRebalReportData report = null;
	private String transactionNumber="";
	private String contractNumber="";
	private String participantId="";
	private String profileId="";
	private String maskedSSN="";
	private String name="";
	
	public TransactionDetailsRebalForm() {
		super();
	}
	
	public void setReport(TransactionDetailsRebalReportData report){
		this.report = report;
	}	

	public TransactionDetailsRebalReportData getReport(){
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
	
	public String getMaskedSSN() {
		return maskedSSN;
	}
	public void setMaskedSSN(String maskedSSN) {
		this.maskedSSN = maskedSSN;
	}
	public String getProfileId() {
		return profileId;
	}
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}


