package com.manulife.pension.bd.web.myprofile;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.platform.web.controller.AutoForm;

public class MyProfileInternalForm extends AutoForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String firstName;
	private String lastName;
	private String emailAddress;

	private String newPassword;
	private String confirmedPassword;
	private String currentPassword;

	private Boolean producerLicense;

	private String defaultSiteLocation;

	private boolean changed = false;
	
	// indicator if the update succeeded
	private boolean success = false;
	private boolean licenseWarning = false;
	
	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = StringUtils.trimToEmpty(currentPassword);
	}

	public Boolean getProducerLicense() {
		return producerLicense;
	}

	public void setProducerLicense(Boolean producerLicense) {
		this.producerLicense = producerLicense;
	}

	public String getDefaultSiteLocation() {
		return defaultSiteLocation;
	}

	public void setDefaultSiteLocation(String defaultSiteLocation) {
		this.defaultSiteLocation = defaultSiteLocation;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = StringUtils.trimToEmpty(newPassword);
	}

	public String getConfirmedPassword() {
		return confirmedPassword;
	}

	public void setConfirmedPassword(String confirmedPassword) {
		this.confirmedPassword = StringUtils.trimToEmpty(confirmedPassword);
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setLicenseWarning(boolean b) {
		licenseWarning = b;
	}

	public boolean isLicenseWarning() {
		return licenseWarning;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}
}
