package com.manulife.pension.bd.web.registration;

import com.manulife.pension.bd.web.registration.util.AddressVO;
import com.manulife.pension.bd.web.registration.util.PasswordChallengeInput;
import com.manulife.pension.bd.web.registration.util.PhoneNumber;
import com.manulife.pension.bd.web.registration.util.UserCredential;
import com.manulife.pension.platform.web.controller.BaseForm;
import com.manulife.pension.service.security.bd.valueobject.BrokerAssistantCreationValueObject;

/**
 * This is the form bean for RegisterBrokerAssistantStep2Action.
 * 
 * @author Ilamparithi
 * 
 */
public class RegisterBrokerAssistantStep2Form extends BaseForm {

    private static final long serialVersionUID = 1L;

    public static final String FIELD_ADDRESS = "address";

    public static final String FIELD_PHONE_NUMBER = "phoneNumber";

    public static final String FIELD_USER_ID = "userId";

    public static final String FIELD_PASSWORD = "password";

    public static final String FIELD_CONFIRMED_PASSWORD = "confirmedPassword";

    public static final String FIELD_CHALLENGE1 = "challenge1";

    public static final String FIELD_CHALLENGE2 = "challenge2";

    public static final String FIELD_PRODUCER_LICENSE = "producerLicense";

    public static final String FIELD_DEFAULT_SITE_LOCATION = "defaultSiteLocation";

    public static final String FIELD_ACCEPT_DISCLAIMER = "acceptDisclaimer";

    private BrokerAssistantCreationValueObject creationVO;

    private UserCredential userCredential = new UserCredential();

    private PasswordChallengeInput challenge1 = new PasswordChallengeInput();

    private PasswordChallengeInput challenge2 = new PasswordChallengeInput();

    private String defaultSiteLocation;

    private Boolean producerLicense;

    private Boolean acceptDisclaimer;

    private boolean emailNotification = false;
    
    private AddressVO address = new AddressVO();

    private PhoneNumber phoneNumber = new PhoneNumber();

    /**
     * Returns the BrokerAssistantCreationValueObject
     * 
     * @return BrokerAssistantCreationValueObject
     */
    public BrokerAssistantCreationValueObject getCreationVO() {
        return creationVO;
    }

    /**
     * Sets the BrokerAssistantCreationValueObject
     * 
     * @param creationVO
     */
    public void setCreationVO(BrokerAssistantCreationValueObject creationVO) {
        this.creationVO = creationVO;
    }

    /**
     * Returns the UserCredential object
     * 
     * @return UserCredential
     */
    public UserCredential getUserCredential() {
        return userCredential;
    }

    /**
     * Sets the UserCredential object
     * 
     * @param userCredential
     */
    public void setUserCredential(UserCredential userCredential) {
        this.userCredential = userCredential;
    }

    /**
     * Returns the first challenge question object
     * 
     * @return PasswordChallengeInput
     */
    public PasswordChallengeInput getChallenge1() {
        return challenge1;
    }

    /**
     * Sets the second challenge question object
     * 
     * @param challenge1
     */
    public void setChallenge1(PasswordChallengeInput challenge1) {
        this.challenge1 = challenge1;
    }

    /**
     * Returns the second challenge question object
     * 
     * @return PasswordChallengeInput
     */
    public PasswordChallengeInput getChallenge2() {
        return challenge2;
    }

    /**
     * Sets the second challenge question object
     * 
     * @param challenge2
     */
    public void setChallenge2(PasswordChallengeInput challenge2) {
        this.challenge2 = challenge2;
    }

    /**
     * Returns the address value object
     * 
     * @return AddressVO
     */
    public AddressVO getAddress() {
        return address;
    }

    /**
     * Sets the address value object
     * 
     * @param address
     */
    public void setAddress(AddressVO address) {
        this.address = address;
    }

    /**
     * Returns the defaultSiteLocation
     * 
     * @return String
     */
    public String getDefaultSiteLocation() {
        return defaultSiteLocation;
    }

    /**
     * Sets the defaultSiteLocation
     * 
     * @param defaultSiteLocation
     */
    public void setDefaultSiteLocation(String defaultSiteLocation) {
        this.defaultSiteLocation = defaultSiteLocation;
    }

    /**
     * Returns the producerLicense flag
     * 
     * @return Boolean
     */
    public Boolean getProducerLicense() {
        return producerLicense;
    }

    /**
     * Sets the producerLicense flag
     * 
     * @param producerLicense
     */
    public void setProducerLicense(Boolean producerLicense) {
        this.producerLicense = producerLicense;
    }

    /**
     * Returns the acceptDisclaimer indicator
     * 
     * @return Boolean
     */
    public Boolean getAcceptDisclaimer() {
        return acceptDisclaimer;
    }

    /**
     * Sets the acceptDisclaimer indicator
     * 
     * @param acceptDisclaimer
     */
    public void setAcceptDisclaimer(Boolean acceptDisclaimer) {
        this.acceptDisclaimer = acceptDisclaimer;
    }

    /**
     * Returns the PhoneNumber object
     * 
     * @return PhoneNumber
     */
    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the PhoneNumber object
     * 
     * @param phoneNumber
     */
    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
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
    public void copyFrom(RegisterBrokerAssistantStep2Form src) {
        this.userCredential.copyFrom(src.getUserCredential());
        this.challenge1.copyFrom(src.getChallenge1());
        this.challenge2.copyFrom(src.getChallenge2());
        this.defaultSiteLocation = src.getDefaultSiteLocation();
        this.producerLicense = src.getProducerLicense();
        this.acceptDisclaimer = src.getAcceptDisclaimer();
        this.address.copyFrom(src.getAddress());
        this.phoneNumber.copyFrom(src.getPhoneNumber());
        this.emailNotification = src.emailNotification;
    }
}
