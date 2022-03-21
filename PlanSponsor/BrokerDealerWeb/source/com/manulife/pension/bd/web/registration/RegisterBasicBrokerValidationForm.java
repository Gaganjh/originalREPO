package com.manulife.pension.bd.web.registration;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.registration.util.AddressVO;
import com.manulife.pension.bd.web.registration.util.PhoneNumber;
import com.manulife.pension.platform.web.controller.BaseForm;

/**
 * This is the form bean for step 1 of Basic Broker Registration
 * 
 * @author Ilamparithi
 * 
 */
public class RegisterBasicBrokerValidationForm extends BaseForm {

    private static final long serialVersionUID = 1L;

    public static final String FIELD_FIRST_NAME = "firstName";

    public static final String FIELD_LAST_NAME = "lastName";

    public static final String FIELD_EMAIL = "emailAddress";

    public static final String FIELD_ADDRESS = "address";

    public static final String FIELD_PHONE_NUMBER = "phoneNumber";

    public static final String FIELD_PARTY_TYPE = "partyType";

    public static final String FIELD_FIRM_NAME = "selectedFirmName";

    public static final String FIELD_COMPANY_NAME = "orgName";

    private String userHasContract = "No";

    private String firstName;

    private String lastName;

    private String emailAddress;

    private AddressVO address = new AddressVO();

    private PhoneNumber phoneNumber = new PhoneNumber();

    private String partyType;

    private String companyName;

    private String firmId;

    private String firmName;

    private boolean changed = false;

    private boolean disabled = false;
    /**
     * Returns Yes/No to indicate whether the user has contract or not
     * 
     * @return the userHasContract
     */
    public String getUserHasContract() {
        return userHasContract;
    }

    /**
     * Sets the userHasContract with Yes/No value
     * 
     * @param userHasContract the userHasContract to set
     */
    public void setUserHasContract(String userHasContract) {
        this.userHasContract = userHasContract;
    }

    /**
     * Returns the first name
     * 
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the firstName
     * 
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = StringUtils.trimToEmpty(firstName);
    }

    /**
     * Returns the lastName
     * 
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the lastName
     * 
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = StringUtils.trimToEmpty(lastName);
    }

    /**
     * Returns the emailAddress
     * 
     * @return the emailAddress
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the emailAddress
     * 
     * @param emailAddress the emailAddress to set
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = StringUtils.trimToEmpty(emailAddress);
    }

    /**
     * Returns the address value object
     * 
     * @return the address
     */
    public AddressVO getAddress() {
        return address;
    }

    /**
     * Sets the address value object
     * 
     * @param address the address to set
     */
    public void setAddress(AddressVO address) {
        this.address = address;
    }

    /**
     * Returns the phoneNumber
     * 
     * @return the phoneNumber
     */
    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phoneNumber
     * 
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns the partyType
     * 
     * @return the partyType
     */
    public String getPartyType() {
        return partyType;
    }

    /**
     * Sets the partyType
     * 
     * @param partyType the partyType to set
     */
    public void setPartyType(String partyType) {
        this.partyType = partyType;
    }

    /**
     * Returns the companyName
     * 
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Sets the companyName
     * 
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = StringUtils.trimToEmpty(companyName);
    }

    /**
     * Returns the firmId
     * 
     * @return the firmId
     */
    public String getFirmId() {
        return firmId;
    }

    /**
     * Sets the firmId
     * 
     * @param firmId the firmId to set
     */
    public void setFirmId(String firmId) {
        this.firmId = firmId;
    }

    /**
     * Returns the firstName
     * 
     * @return the firmName
     */
    public String getFirmName() {
        return firmName;
    }

    /**
     * Sets the firstName
     * 
     * @param firmName the firmName to set
     */
    public void setFirmName(String firmName) {
        this.firmName = StringUtils.trimToEmpty(firmName);
    }

    /**
     * Returns a flag that indicates whether a form is dirty or not
     * 
     * @return the changed
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * Sets the dirty flag of the form
     * 
     * @param changed the changed to set
     */
    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    /**
     * Copy data from one form to another
     * 
     * @param src
     */
    public void copyFrom(RegisterBasicBrokerValidationForm src) {
        this.firstName = src.getFirstName();
        this.lastName = src.getLastName();
        this.emailAddress = src.getEmailAddress();
        this.address.copyFrom(src.getAddress());
        this.phoneNumber.copyFrom(src.getPhoneNumber());
        this.partyType = src.getPartyType();
        this.companyName = src.getCompanyName();
        this.firmId = src.getFirmId();
        this.firmName = src.getFirmName();
        this.changed = src.isChanged();
    }

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}
