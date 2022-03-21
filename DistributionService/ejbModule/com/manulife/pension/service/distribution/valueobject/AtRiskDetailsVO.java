package com.manulife.pension.service.distribution.valueobject;

import java.io.Serializable;

/**
 * Class is used to set / get the Participant Risk information
 *  
 * @author Vasanth Balaji
 *
 */
public class AtRiskDetailsVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer profileId = null;
	private String contractId = null;
	private AtRiskWebRegistrationVO webRegistration = null;
	private AtRiskAddressChangeVO addresschange = null;
	private AtRiskPasswordResetVO passwordReset = null;
	private AtRiskForgetUserName forgetUserName = null;
	private Integer submissionId = null;
	
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
	public Integer getProfileId() {
		return profileId;
	}
	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public AtRiskWebRegistrationVO getWebRegistration() {
		return webRegistration;
	}
	public void setWebRegistration(AtRiskWebRegistrationVO webRegistration) {
		this.webRegistration = webRegistration;
	}
	public AtRiskAddressChangeVO getAddresschange() {
		return addresschange;
	}
	public void setAddresschange(AtRiskAddressChangeVO addresschange) {
		this.addresschange = addresschange;
	}
	public AtRiskPasswordResetVO getPasswordReset() {
		return passwordReset;
	}
	public void setPasswordReset(AtRiskPasswordResetVO passwordReset) {
		this.passwordReset = passwordReset;
	}
	public AtRiskForgetUserName getForgetUserName() {
		return forgetUserName;
	}
	public void setForgetUserName(AtRiskForgetUserName forgetUserName) {
		this.forgetUserName = forgetUserName;
	}
	
	
	

}
