package com.manulife.pension.bd.web.registration;

import com.manulife.pension.bd.web.registration.util.PasswordChallengeInput;
import com.manulife.pension.bd.web.registration.util.UserCredential;
import com.manulife.pension.platform.web.controller.BaseForm;

/**
 * This is the form bean for Step 2 action of Broker Registration
 * 
 * @author Ilamparithi
 * 
 */
public class RegisterBrokerStep2Form extends BaseForm {

    private static final long serialVersionUID = 1L;

    public static final String FIELD_USER_ID = "userId";

    public static final String FIELD_PASSWORD = "password";

    public static final String FIELD_CONFIRMED_PASSWORD = "confirmedPassword";

    public static final String FIELD_CHALLENGE = "challenge";

    public static final String FIELD_PRODUCER_LICENSE = "producerLicense";

    public static final String FIELD_DEFAULT_SITE_LOCATION = "defaultSiteLocation";

    public static final String FIELD_ACCEPT_DISCLAIMER = "acceptDisclaimer";

    private UserCredential userCredential = new UserCredential();

    private PasswordChallengeInput challenge1 = new PasswordChallengeInput();

    private PasswordChallengeInput challenge2 = new PasswordChallengeInput();

    private Boolean producerLicense;

    private String defaultSiteLocation;

    private Boolean acceptDisclaimer;

    private boolean emailNotification = false;
    
    /**
     * Returns the userCredential object
     * 
     * @return the userCredential
     */
    public UserCredential getUserCredential() {
        return userCredential;
    }

    /**
     * Sets the userCredential object
     * 
     * @param userCredential the userCredential to set
     */
    public void setUserCredential(UserCredential userCredential) {
        this.userCredential = userCredential;
    }

    /**
     * Returns the first challenge question object
     * 
     * @return the challenge1
     */
    public PasswordChallengeInput getChallenge1() {
        return challenge1;
    }

    /**
     * Sets the first challenge question object
     * 
     * @param challenge1 the challenge1 to set
     */
    public void setChallenge1(PasswordChallengeInput challenge1) {
        this.challenge1 = challenge1;
    }

    /**
     * Returns the second challenge question object
     * 
     * @return the challenge2
     */
    public PasswordChallengeInput getChallenge2() {
        return challenge2;
    }

    /**
     * Sets the second challenge question object
     * 
     * @param challenge2 the challenge2 to set
     */
    public void setChallenge2(PasswordChallengeInput challenge2) {
        this.challenge2 = challenge2;
    }

    /**
     * Returns the producerLicense
     * 
     * @return the producerLicense
     */
    public Boolean getProducerLicense() {
        return producerLicense;
    }

    /**
     * Sets the producerLicense
     * 
     * @param producerLicense the producerLicense to set
     */
    public void setProducerLicense(Boolean producerLicense) {
        this.producerLicense = producerLicense;
    }

    /**
     * Returns the defaultSiteLocation
     * 
     * @return the defaultSiteLocation
     */
    public String getDefaultSiteLocation() {
        return defaultSiteLocation;
    }

    /**
     * Sets the defaultSiteLocation
     * 
     * @param defaultSiteLocation the defaultSiteLocation to set
     */
    public void setDefaultSiteLocation(String defaultSiteLocation) {
        this.defaultSiteLocation = defaultSiteLocation;
    }

    /**
     * Returns the acceptDisclaimer indicator
     * 
     * @return the acceptDisclaimer
     */
    public Boolean getAcceptDisclaimer() {
        return acceptDisclaimer;
    }

    /**
     * Sets the acceptDisclaimer indicator
     * 
     * @param acceptDisclaimer the acceptDisclaimer to set
     */
    public void setAcceptDisclaimer(Boolean acceptDisclaimer) {
        this.acceptDisclaimer = acceptDisclaimer;
    }

    
    public boolean getEmailNotification() {
		return emailNotification;
	}

	public void setEmailNotification(boolean emailNotification) {
		this.emailNotification = emailNotification;
	}

	/**
     * Copy data from one form to another
     * 
     * @param src
     */
    public void copyFrom(RegisterBrokerStep2Form src) {
        this.userCredential.copyFrom(src.getUserCredential());
        this.challenge1.copyFrom(src.getChallenge1());
        this.challenge2.copyFrom(src.getChallenge2());
        this.acceptDisclaimer = src.getAcceptDisclaimer();
        this.defaultSiteLocation = src.getDefaultSiteLocation();
        this.producerLicense = src.getProducerLicense();
        this.emailNotification = src.emailNotification;
    }
}
