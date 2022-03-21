package com.manulife.pension.ps.web.pif;

import com.manulife.pension.platform.web.controller.AutoForm;

/**
 * This Action class handles plan information delete page actions form class
 * 
 * @author Vivek Lingesan
 */

public class DeletePIFDataForm extends AutoForm {
	
	private static final long serialVersionUID = 1L;
	private Integer contractNumber;
	private String contractName;
	private Integer submissionId;
    private String lastUpdatedDate;
    private String userName;
    
	/**
	 * @return the contractNumber
	 */
	public Integer getContractNumber() {
		return contractNumber;
	}
	/**
	 * @param contractNumber the contractNumber to set
	 */
	public void setContractNumber(Integer contractNumber) {
		this.contractNumber = contractNumber;
	}
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
	 * @return the submissionId
	 */
	public Integer getSubmissionId() {
		return submissionId;
	}
	/**
	 * @param submissionId the submissionId to set
	 */
	public void setSubmissionId(Integer submissionId) {
		this.submissionId = submissionId;
	}
	/**
	 * @return the lastUpdatedDate
	 */
	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	/**
	 * @param lastUpdatedDate the lastUpdatedDate to set
	 */
	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
    
}