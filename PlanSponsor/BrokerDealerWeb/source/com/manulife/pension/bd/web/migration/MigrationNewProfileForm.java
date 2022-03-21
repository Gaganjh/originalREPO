package com.manulife.pension.bd.web.migration;

import com.manulife.pension.bd.web.registration.RegisterBrokerStep2Form;
import com.manulife.pension.bd.web.registration.util.PasswordChallengeInput;
import com.manulife.pension.bd.web.registration.util.UserCredential;
import com.manulife.pension.platform.web.controller.BaseForm;

public class MigrationNewProfileForm extends BaseForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String FIELD_FIRSTNAME = "firstName";

	public static final String FIELD_LASTNAME = "lastName";

	public static final String FIELD_USER_ID = "userId";

	public static final String FIELD_PASSWORD = "password";

	public static final String FIELD_CONFIRMED_PASSWORD = "confirmedPassword";

	public static final String FIELD_CHALLENGE = "challenge";

	public static final String FIELD_PRODUCER_LICENSE = "producerLicense";

	public static final String FIELD_DEFAULT_SITE_LOCATION = "defaultSiteLocation";

	public static final String FIELD_ACCEPT_DISCLAIMER = "acceptDisclaimer";

	private String firstName;
	
	private String lastName;
	
	private UserCredential userCredential = new UserCredential();

	private PasswordChallengeInput challenge1 = new PasswordChallengeInput();

	private PasswordChallengeInput challenge2 = new PasswordChallengeInput();

	private Boolean producerLicense;

	private String defaultSiteLocation;

	private Boolean acceptDisclaimer;

    private boolean emailNotification = false;

	private boolean userIdNeedChange;
	private boolean passwordNeedChange;

	public UserCredential getUserCredential() {
		return userCredential;
	}

	public void setUserCredential(UserCredential userCredential) {
		this.userCredential = userCredential;
	}

	public PasswordChallengeInput getChallenge1() {
		return challenge1;
	}

	public void setChallenge1(PasswordChallengeInput challenge1) {
		this.challenge1 = challenge1;
	}

	public PasswordChallengeInput getChallenge2() {
		return challenge2;
	}

	public void setChallenge2(PasswordChallengeInput challenge2) {
		this.challenge2 = challenge2;
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

	public Boolean getAcceptDisclaimer() {
		return acceptDisclaimer;
	}

	public void setAcceptDisclaimer(Boolean acceptDisclaimer) {
		this.acceptDisclaimer = acceptDisclaimer;
	}

	public void copyFrom(RegisterBrokerStep2Form src) {
		this.userCredential.copyFrom(src.getUserCredential());
		this.challenge1.copyFrom(src.getChallenge1());
		this.challenge2.copyFrom(src.getChallenge2());
		this.acceptDisclaimer = src.getAcceptDisclaimer();
		this.defaultSiteLocation = src.getDefaultSiteLocation();
		this.producerLicense = src.getProducerLicense();
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isUserIdNeedChange() {
		return userIdNeedChange;
	}

	public void setUserIdNeedChange(boolean userIdNeedChange) {
		this.userIdNeedChange = userIdNeedChange;
	}

	public boolean isPasswordNeedChange() {
		return passwordNeedChange;
	}

	public void setPasswordNeedChange(boolean passwordNeedChange) {
		this.passwordNeedChange = passwordNeedChange;
	}

	public boolean isEmailNotification() {
		return emailNotification;
	}

	public void setEmailNotification(boolean emailNotification) {
		this.emailNotification = emailNotification;
	}

	public void copyFrom(MigrationNewProfileForm src) {
		this.firstName = src.firstName;
		this.lastName = src.lastName;
		this.userCredential.copyFrom(src.getUserCredential());
		this.challenge1.copyFrom(src.getChallenge1());
		this.challenge2.copyFrom(src.getChallenge2());
		this.acceptDisclaimer = src.getAcceptDisclaimer();
		this.defaultSiteLocation = src.getDefaultSiteLocation();
		this.producerLicense = src.getProducerLicense();
		this.emailNotification = src.isEmailNotification();
	}
}
