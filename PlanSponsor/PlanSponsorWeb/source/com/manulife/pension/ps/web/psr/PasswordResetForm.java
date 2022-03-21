/**
 * 
 */
package com.manulife.pension.ps.web.psr;

import java.util.Date;

import com.manulife.pension.ps.web.census.CensusConstants;
import com.manulife.pension.ps.web.util.CloneableAutoForm;

/**
 * Action form for the Password Reset page
 * @author gazulra
 *
 */
public class PasswordResetForm extends CloneableAutoForm {
	
	/**
	 * Default Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Contract Id 
	 * Acts as a primary key
	 */
	private int contractId;
	
	/**
	 * Profile Id of the employee. 
	 * Acts as a primary key
	 */
	private String profileId;
	
	/**
	 * employee's first name
	 */
	private String firstName;
	
	/**
	 * employee's last name
	 */
	private String lastName;
	
	/**
	 * employee's ssn
	 */
	private String ssn;
	
	/**
	 * employee's date of birth
	 */
	private Date birthDate;
	
	/**
	 * Employer provided email address
	 */
	private String employerProvidedEmailAddress;
	
	/**
	 * Holds "disabled" - if there are any validation errors else holds ""
	 * This is used to enable or disable the "reset password" button;
	 */
	private boolean suppressResetPasswordButton;
	
	/**
	 * The source page id from where the user comes to this page
	 */
	private String source = CensusConstants.CENSUS_SUMMARY_PAGE;
	/**
	 *  Holds "Yes" - if the participant is Web Registered 
	 *  "No" - if the participant is not Web Registered
	 */
	private String pptWebRegisStatus;
	
	/**
	 * Holds date and time stamp when password reset request was made
	 */
	private String requestedTs;
	
	/**
	 * Holds the password reset request Id.
	 */
	private int requestId;
	
	/**
	 * @return the contractId
	 */
	public int getContractId() {
		return contractId;
	}
	/**
	 * @param contractId the contractId to set
	 */
	public void setContractId(int contractId) {
		this.contractId = contractId;
	}
	/**
	 * @return the profileId
	 */
	public String getProfileId() {
		return profileId;
	}
	/**
	 * @param profileId the profileId to set
	 */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the ssn
	 */
	public String getSsn() {
		return ssn;
	}
	/**
	 * @param ssn the ssn to set
	 */
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	/**
	 * @return the birthDate
	 */
	public Date getBirthDate() {
		return birthDate;
	}
	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	/**
	 * @return the employerProvidedEmailAddress
	 */
	public String getEmployerProvidedEmailAddress() {
		return employerProvidedEmailAddress;
	}
	/**
	 * @param employerProvidedEmailAddress the employerProvidedEmailAddress to set
	 */
	public void setEmployerProvidedEmailAddress(String employerProvidedEmailAddress) {
		this.employerProvidedEmailAddress = employerProvidedEmailAddress;
	}
	/**
	 * @return the suppressResetPasswordButton
	 */
	public boolean isSuppressResetPasswordButton() {
		return suppressResetPasswordButton;
	}
	/**
	 * @param suppressResetPasswordButton the suppressResetPasswordButton to set
	 */
	public void setSuppressResetPasswordButton(boolean suppressResetPasswordButton) {
		this.suppressResetPasswordButton = suppressResetPasswordButton;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @return the pptWebRegisStatus
	 */
	public String getPptWebRegisStatus() {
		return pptWebRegisStatus;
	}
	/**
	 * @param pptWebRegisStatus the pptWebRegisStatus to set
	 */
	public void setPptWebRegisStatus(String pptWebRegisStatus) {
		this.pptWebRegisStatus = pptWebRegisStatus;
	}
	/**
	 * @return the requestedTs
	 */
	public String getRequestedTs() {
		return requestedTs;
	}
	/**
	 * @param requestedTs the requestedTs to set
	 */
	public void setRequestedTs(String requestedTs) {
		this.requestedTs = requestedTs;
	}
	/**
	 * @return the requestId
	 */
	public int getRequestId() {
		return requestId;
	}
	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

}
