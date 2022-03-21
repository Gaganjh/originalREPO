package com.manulife.pension.service.distribution.valueobject;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * Class used to set / get the Forgot User name activity performed by the participant.
 * 
 * @author Vasanth Balaji
 *
 */
public class AtRiskForgetUserName implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	private Date forgotPasswordRequestedDate = null;
	private String forgotPasswordEmailAddress = null;
	private Integer forgotPasswordUpdatedProfileId = null;
	private String forgotPasswordUpdatedUserIdType = null;
	private String forgotPasswordUpdatedUserLastName = null;
	private String forgotPasswordUpdatedUserFirstName = null;
	private boolean isAtRiskPeriod;
	/**
	 * @return the forgotPasswordRequestedDate
	 */
	public final Date getForgotPasswordRequestedDate() {
		return forgotPasswordRequestedDate;
	}
	/**
	 * @param forgotPasswordRequestedDate the forgotPasswordRequestedDate to set
	 */
	public final void setForgotPasswordRequestedDate(
			Date forgotPasswordRequestedDate) {
		this.forgotPasswordRequestedDate = forgotPasswordRequestedDate;
	}
	/**
	 * @return the forgotPasswordEmailAddress
	 */
	public final String getForgotPasswordEmailAddress() {
		return forgotPasswordEmailAddress;
	}
	/**
	 * @param forgotPasswordEmailAddress the forgotPasswordEmailAddress to set
	 */
	public final void setForgotPasswordEmailAddress(
			String forgotPasswordEmailAddress) {
		this.forgotPasswordEmailAddress = forgotPasswordEmailAddress;
	}
	/**
	 * @return the forgotPasswordUpdatedProfileId
	 */
	public final Integer getForgotPasswordUpdatedProfileId() {
		return forgotPasswordUpdatedProfileId;
	}
	/**
	 * @param forgotPasswordUpdatedProfileId the forgotPasswordUpdatedProfileId to set
	 */
	public final void setForgotPasswordUpdatedProfileId(
			Integer forgotPasswordUpdatedProfileId) {
		this.forgotPasswordUpdatedProfileId = forgotPasswordUpdatedProfileId;
	}
	/**
	 * @return the forgotPasswordUpdatedUserIdType
	 */
	public final String getForgotPasswordUpdatedUserIdType() {
		return forgotPasswordUpdatedUserIdType;
	}
	/**
	 * @param forgotPasswordUpdatedUserIdType the forgotPasswordUpdatedUserIdType to set
	 */
	public final void setForgotPasswordUpdatedUserIdType(
			String forgotPasswordUpdatedUserIdType) {
		this.forgotPasswordUpdatedUserIdType = forgotPasswordUpdatedUserIdType;
	}
	/**
	 * @return the forgotPasswordUpdatedUserLastName
	 */
	public final String getForgotPasswordUpdatedUserLastName() {
		return forgotPasswordUpdatedUserLastName;
	}
	/**
	 * @param forgotPasswordUpdatedUserLastName the forgotPasswordUpdatedUserLastName to set
	 */
	public final void setForgotPasswordUpdatedUserLastName(
			String forgotPasswordUpdatedUserLastName) {
		this.forgotPasswordUpdatedUserLastName = forgotPasswordUpdatedUserLastName;
	}
	/**
	 * @return the forgotPasswordUpdatedUserFirstName
	 */
	public final String getForgotPasswordUpdatedUserFirstName() {
		return forgotPasswordUpdatedUserFirstName;
	}
	/**
	 * @param forgotPasswordUpdatedUserFirstName the forgotPasswordUpdatedUserFirstName to set
	 */
	public final void setForgotPasswordUpdatedUserFirstName(
			String forgotPasswordUpdatedUserFirstName) {
		this.forgotPasswordUpdatedUserFirstName = forgotPasswordUpdatedUserFirstName;
	}
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
	
	
	
	

}
