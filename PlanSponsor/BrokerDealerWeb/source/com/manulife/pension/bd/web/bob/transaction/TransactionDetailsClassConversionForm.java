package com.manulife.pension.bd.web.bob.transaction;

import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsClassConversionReportData;

/**
 * This is the action form for Class Conversion page
 * 
 * @author Syntel
 * 
 */
public class TransactionDetailsClassConversionForm extends BaseReportForm {

    private static final long serialVersionUID = 1L;

    private TransactionDetailsClassConversionReportData report = null;
	private String transactionNumber="";
	private String contractNumber="";
	private String pptId = "";
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
		return pptId;
	}
	
	public void setPptId(String pptId) {
        this.pptId = pptId;
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


