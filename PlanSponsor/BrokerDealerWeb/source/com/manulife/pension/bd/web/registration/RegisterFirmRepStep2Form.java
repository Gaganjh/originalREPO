package com.manulife.pension.bd.web.registration;

import java.util.SortedSet;

import com.manulife.pension.ezk.web.ActionForm;

import com.manulife.pension.bd.web.registration.util.AddressVO;
import com.manulife.pension.bd.web.registration.util.PasswordChallengeInput;
import com.manulife.pension.bd.web.registration.util.UserCredential;
import com.manulife.pension.bd.web.usermanagement.UserManagementHelper;
import com.manulife.pension.service.security.bd.valueobject.FirmRepCreationValueObject;

/**
 * This is the form bean for Firm Rep Registration step 2
 * 
 * @author guweigu
 * 
 */
public class RegisterFirmRepStep2Form implements ActionForm {

    private static final long serialVersionUID = 1L;

    public static final String FIELD_ADDRESS = "address";

    public static final String FIELD_USER_ID = "userId";

    public static final String FIELD_PASSWORD = "password";

    public static final String FIELD_CONFIRMED_PASSWORD = "confirmedPassword";

    public static final String FIELD_CHALLENGE = "passwordchallenge";

    public static final String FIELD_PRODUCER_LICENSE = "producerLicense";

    public static final String FIELD_DEFAULT_SITE_LOCATION = "defaultSiteLocation";

    public static final String FIELD_ACCEPT_DISCLAIMER = "acceptDisclaimer";

    private FirmRepCreationValueObject creationVO;

    private UserCredential userCredential = new UserCredential();

    private PasswordChallengeInput challenge1 = new PasswordChallengeInput();

    private PasswordChallengeInput challenge2 = new PasswordChallengeInput();

    private AddressVO address = new AddressVO();

    private String defaultSiteLocation;

    private Boolean producerLicense;

    private Boolean acceptDisclaimer = false;

    /**
     * Returns the FirmRepCreationValueObject
     * 
     * @return the creationVO
     */
    public FirmRepCreationValueObject getCreationVO() {
        return creationVO;
    }

    /**
     * Sets the FirmRepCreationValueObject
     * 
     * @param creationVO the creationVO to set
     */
    public void setCreationVO(FirmRepCreationValueObject creationVO) {
        this.creationVO = creationVO;
    }

    /**
     * Returns the UserCredential object
     * 
     * @return the userCredential
     */
    public UserCredential getUserCredential() {
        return userCredential;
    }

    /**
     * Sets the UserCredential
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
     * Returns the AddressVO object
     * 
     * @return the address
     */
    public AddressVO getAddress() {
        return address;
    }

    /**
     * Sets the AddressVO object
     * 
     * @param address the address to set
     */
    public void setAddress(AddressVO address) {
        this.address = address;
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

    public SortedSet<String> getSortedBDFirmNames() {
    	return UserManagementHelper.getSortedBDFirmNames(creationVO.getFirms());
    }

    /**
     * Copy data from one form to another
     * 
     * @param src
     */
    public void copyFrom(RegisterFirmRepStep2Form src) {
        this.userCredential.copyFrom(src.getUserCredential());
        this.challenge1.copyFrom(src.getChallenge1());
        this.challenge2.copyFrom(src.getChallenge2());
        this.address.copyFrom(src.getAddress());
        this.defaultSiteLocation = src.getDefaultSiteLocation();
        this.producerLicense = src.getProducerLicense();
        this.acceptDisclaimer = src.getAcceptDisclaimer();
    }
}
