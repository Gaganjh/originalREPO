package com.manulife.pension.bd.web.registration;

import com.manulife.pension.bd.web.registration.util.PasswordChallengeInput;
import com.manulife.pension.bd.web.registration.util.UserCredential;
import com.manulife.pension.platform.web.controller.BaseForm;

/**
 * This is the form bean for step 2 of Basic Broker Registration
 * 
 * @author Ilamparithi
 * 
 */
public class RegisterBasicBrokerStep2Form extends BaseForm {

    private static final long serialVersionUID = 1L;

    public static final String FIELD_USER_ID = "userId";

    public static final String FIELD_PASSWORD = "password";

    public static final String FIELD_CONFIRMED_PASSWORD = "confirmedPassword";

    public static final String FIELD_CHALLENGE = "challenge";

    public static final String FIELD_DEFAULT_SITE_LOCATION = "defaultSiteLocation";

    public static final String FIELD_ACCEPT_DISCLAIMER = "acceptDisclaimer";

    private UserCredential userCredential = new UserCredential();

    private PasswordChallengeInput challenge1 = new PasswordChallengeInput();

    private PasswordChallengeInput challenge2 = new PasswordChallengeInput();

    private String defaultSiteLocation;

    private Boolean acceptDisclaimer = false;

    /**
     * Returns the user credential object
     * 
     * @return the userCredential
     */
    public UserCredential getUserCredential() {
        return userCredential;
    }

    /**
     * Sets the user credential object
     * 
     * @param userCredential the userCredential to set
     */
    public void setUserCredential(UserCredential userCredential) {
        this.userCredential = userCredential;
    }

    /**
     * Returns the first chanllenge question object
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
     * Returns the default site location
     * 
     * @return the defaultSiteLocation
     */
    public String getDefaultSiteLocation() {
        return defaultSiteLocation;
    }

    /**
     * Sets the default site location
     * 
     * @param defaultSiteLocation the defaultSiteLocation to set
     */
    public void setDefaultSiteLocation(String defaultSiteLocation) {
        this.defaultSiteLocation = defaultSiteLocation;
    }

    /**
     * Returns accept disclaimer indicator
     * 
     * @return the acceptDisclaimer
     */
    public Boolean getAcceptDisclaimer() {
        return acceptDisclaimer;
    }

    /**
     * Sets the accept disclaimer indicator
     * 
     * @param acceptDisclaimer the acceptDisclaimer to set
     */
    public void setAcceptDisclaimer(Boolean acceptDisclaimer) {
        this.acceptDisclaimer = acceptDisclaimer;
    }

    /**
     * Copy one form form another
     * 
     * @param src
     */
    public void copyFrom(RegisterBasicBrokerStep2Form src) {
        this.userCredential.copyFrom(src.getUserCredential());
        this.challenge1.copyFrom(src.getChallenge1());
        this.challenge2.copyFrom(src.getChallenge2());
        this.defaultSiteLocation = src.getDefaultSiteLocation();
        this.acceptDisclaimer = src.getAcceptDisclaimer();
    }
}
