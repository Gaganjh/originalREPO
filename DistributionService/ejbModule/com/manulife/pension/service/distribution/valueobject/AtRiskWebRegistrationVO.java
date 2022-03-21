package com.manulife.pension.service.distribution.valueobject;

import java.io.Serializable;
import java.util.Date;

import com.manulife.pension.service.withdrawal.valueobject.Address;

/**
 * class used to set / get the Partcipant's Web registration information.
 * 
 * @author Vasanth Balaji
 *
 */
public class AtRiskWebRegistrationVO implements Serializable{
 
	private static final long serialVersionUID = 1L;
	private String confirmUpdatedUserIdType = null;
	private Date webRegistrationDate = null;
	private Date webRegConfirmationMailedDate = null;
	private Integer confirmUpdatedProfileId = null;
	private Address address = null;
	private String confirmUpdatedUserFirstName;
	private String confirmUpdatedUserLastName;
	private boolean isWebRegAtRiskPeriod;
	private boolean isWebConfirmationLetterAvailable;
	
	
	
	/**
	 * @return the isWebConfirmationLetterAvailable
	 */
	public boolean isWebConfirmationLetterAvailable() {
		return isWebConfirmationLetterAvailable;
	}


	/**
	 * @param isWebConfirmationLetterAvailable the isWebConfirmationLetterAvailable to set
	 */
	public void setWebConfirmationLetterAvailable(
			boolean isWebConfirmationLetterAvailable) {
		this.isWebConfirmationLetterAvailable = isWebConfirmationLetterAvailable;
	}

	/**
	 * @return the isWebRegAtRiskPeriod
	 */
	public final boolean isWebRegAtRiskPeriod() {
		return isWebRegAtRiskPeriod;
	}
	
	
	/**
	 * @param isWebRegAtRiskPeriod the isWebRegAtRiskPeriod to set
	 */
	public final void setWebRegAtRiskPeriod(boolean isWebRegAtRiskPeriod) {
		this.isWebRegAtRiskPeriod = isWebRegAtRiskPeriod;
	}
	
	
	/**
	 * @return the confirmUpdatedUserFirstName
	 */
	public String getConfirmUpdatedUserFirstName() {
		return confirmUpdatedUserFirstName;
	}
	/**
	 * @param confirmUpdatedUserFirstName the confirmUpdatedUserFirstName to set
	 */
	public void setConfirmUpdatedUserFirstName(String confirmUpdatedUserFirstName) {
		this.confirmUpdatedUserFirstName = confirmUpdatedUserFirstName;
	}
	/**
	 * @return the confirmUpdatedUserLastName
	 */
	public String getConfirmUpdatedUserLastName() {
		return confirmUpdatedUserLastName;
	}
	/**
	 * @param confirmUpdatedUserLastName the confirmUpdatedUserLastName to set
	 */
	public void setConfirmUpdatedUserLastName(String confirmUpdatedUserLastName) {
		this.confirmUpdatedUserLastName = confirmUpdatedUserLastName;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public Integer getConfirmUpdatedProfileId() {
		return confirmUpdatedProfileId;
	}
	public void setConfirmUpdatedProfileId(Integer confirmUpdatedProfileId) {
		this.confirmUpdatedProfileId = confirmUpdatedProfileId;
	}
	public String getConfirmUpdatedUserIdType() {
		return confirmUpdatedUserIdType;
	}
	public void setConfirmUpdatedUserIdType(String confirmUpdatedUserIdType) {
		this.confirmUpdatedUserIdType = confirmUpdatedUserIdType;
	}
	public Date getWebRegistrationDate() {
		return webRegistrationDate;
	}
	public void setWebRegistrationDate(Date webRegistrationDate) {
		this.webRegistrationDate = webRegistrationDate;
	}
	public Date getWebRegConfirmationMailedDate() {
		return webRegConfirmationMailedDate;
	}
	public void setWebRegConfirmationMailedDate(Date webRegConfirmationMailedDate) {
		this.webRegConfirmationMailedDate = webRegConfirmationMailedDate;
	}
	
	
	

}
