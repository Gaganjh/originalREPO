package com.manulife.pension.service.distribution.valueobject;

import java.io.Serializable;
import java.util.Date;

/**
 * Class used to set / get the Password reset activity performed by the participant.
 * 
 * @author Vasanth Balaji
 *
 */
public class AtRiskPasswordResetVO implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	private Date emailPasswordResetDate = null;
	private String emailPasswordResetEmailAddress = null;
	private Integer emailPasswordResetInitiatedProfileId = null;

	private Integer emailAddressLastUpdatedProfileId = null;
	private String emailAddressLastUpdatedUserIdType = null;
	private String emailPasswordResetInitiatedUserFirstName = null;
	private String emailPasswordResetInitiatedUserLastName = null;
	private String emailAddressLastUpdatedUserFirstName = null;
	private String emailAddressLastUpdatedUserLastName = null;
	private String activityTypeCode;
	private boolean isAtRiskPeriod;
	
	
	/**
	 * @return the isAtRiskPeriod
	 */
	public final boolean isAtRiskPeriod() {
		return isAtRiskPeriod;
	}
	/**
	 * @param isAtRiskPeriod the isAtRiskPeriod to set
	 */
	public final void setAtRiskPeriod(boolean isAtRiskPeriod) {
		this.isAtRiskPeriod = isAtRiskPeriod;
	}
	
	
	public String getEmailPasswordResetInitiatedUserFirstName() {
		return emailPasswordResetInitiatedUserFirstName;
	}
	public void setEmailPasswordResetInitiatedUserFirstName(
			String emailPasswordResetInitiatedUserFirstName) {
		this.emailPasswordResetInitiatedUserFirstName = emailPasswordResetInitiatedUserFirstName;
	}
	public String getEmailPasswordResetInitiatedUserLastName() {
		return emailPasswordResetInitiatedUserLastName;
	}
	public void setEmailPasswordResetInitiatedUserLastName(
			String emailPasswordResetInitiatedUserLastName) {
		this.emailPasswordResetInitiatedUserLastName = emailPasswordResetInitiatedUserLastName;
	}
	public String getEmailAddressLastUpdatedUserFirstName() {
		return emailAddressLastUpdatedUserFirstName;
	}
	public void setEmailAddressLastUpdatedUserFirstName(
			String emailAddressLastUpdatedUserFirstName) {
		this.emailAddressLastUpdatedUserFirstName = emailAddressLastUpdatedUserFirstName;
	}
	public String getEmailAddressLastUpdatedUserLastName() {
		return emailAddressLastUpdatedUserLastName;
	}
	public void setEmailAddressLastUpdatedUserLastName(
			String emailAddressLastUpdatedUserLastName) {
		this.emailAddressLastUpdatedUserLastName = emailAddressLastUpdatedUserLastName;
	}
	
	
	
	public Date getEmailPasswordResetDate() {
		return emailPasswordResetDate;
	}
	public void setEmailPasswordResetDate(Date emailPasswordResetDate) {
		this.emailPasswordResetDate = emailPasswordResetDate;
	}
	public String getEmailPasswordResetEmailAddress() {
		return emailPasswordResetEmailAddress;
	}
	public void setEmailPasswordResetEmailAddress(
			String emailPasswordResetEmailAddress) {
		this.emailPasswordResetEmailAddress = emailPasswordResetEmailAddress;
	}
	
	
	public String getEmailAddressLastUpdatedUserIdType() {
		return emailAddressLastUpdatedUserIdType;
	}
	public void setEmailAddressLastUpdatedUserIdType(
			String emailAddressLastUpdatedUserIdType) {
		this.emailAddressLastUpdatedUserIdType = emailAddressLastUpdatedUserIdType;
	}

	
	public Integer getEmailPasswordResetInitiatedProfileId() {
		return emailPasswordResetInitiatedProfileId;
	}
	public void setEmailPasswordResetInitiatedProfileId(
			Integer emailPasswordResetInitiatedProfileId) {
		this.emailPasswordResetInitiatedProfileId = emailPasswordResetInitiatedProfileId;
	}
	public Integer getEmailAddressLastUpdatedProfileId() {
		return emailAddressLastUpdatedProfileId;
	}
	public void setEmailAddressLastUpdatedProfileId(
			Integer emailAddressLastUpdatedProfileId) {
		this.emailAddressLastUpdatedProfileId = emailAddressLastUpdatedProfileId;
	}
	
	/**
	 * @return the activityTypeCode
	 */
	public String getActivityTypeCode() {
		return activityTypeCode;
	}
	/**
	 * @param activityTypeCode the activityTypeCode to set
	 */
	public void setActivityTypeCode(String activityTypeCode) {
		this.activityTypeCode = activityTypeCode;
	}
	
}
