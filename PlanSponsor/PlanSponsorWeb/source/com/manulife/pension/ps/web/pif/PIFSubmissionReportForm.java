package com.manulife.pension.ps.web.pif;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.web.report.ReportForm;

/**
 * Action form class for plan information submission report page related activities
 * 
 * @author Vivek Lingesan
 */

public class PIFSubmissionReportForm extends ReportForm{

	private static final long serialVersionUID = 1L;
	private String contractName;
	private String contractNumber;
	private String submissionStatus;
	private String contractId;
	
	
	/**
	 * @return the contractName
	 */
	public String getContractName() {
		return contractName;
	}
	/**
	 * @param contractName the contractName to set
	 */
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	/**
	 * @return the contractNumber
	 */
	public String getContractNumber() {
		return contractNumber;
	}
	/**
	 * @param contractNumber the contractNumber to set
	 */
	public void setContractNumber(String contractNumber) {
		this.contractNumber = StringUtils.trim(contractNumber);
	}
	/**
	 * @return the submissionStatus
	 */
	public String getSubmissionStatus() {
		return submissionStatus;
	}
	/**
	 * @param submissionStatus the submissionStatus to set
	 */
	public void setSubmissionStatus(String submissionStatus) {
		this.submissionStatus = submissionStatus;
	}
	
	public void resetForm(){
		this.setContractName("");
		this.setContractNumber("");
		this.setSubmissionStatus("");
	}

	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
}
