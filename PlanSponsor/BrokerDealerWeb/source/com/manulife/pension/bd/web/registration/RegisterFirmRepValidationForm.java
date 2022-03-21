package com.manulife.pension.bd.web.registration;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.registration.util.PhoneNumber;
import com.manulife.pension.platform.web.controller.BaseForm;

/**
 * This is the form bean for Firm Rep Registration step 1
 * 
 * @author guweigu
 * 
 */
public class RegisterFirmRepValidationForm extends BaseForm {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public static final String FIELD_LAST_NAME = "lastName";

    public static final String FIELD_PASSCODE = "passCode";

    public static final String FIELD_PHONE_NUMBER = "phoneNumber";

    private PhoneNumber phoneNumber = new PhoneNumber();

    private String lastName;

    private String passCode;

    private boolean disabled = false;
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

    /**
     * Returns the lastName
     * 
     * @return String
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the lastName
     * 
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = StringUtils.trimToEmpty(lastName);
    }

    /**
     * Returns the passCode
     * 
     * @return String
     */
    public String getPassCode() {
        return passCode;
    }

    /**
     * Sets the passCode
     * 
     * @param passCode
     */
    public void setPassCode(String passCode) {
        this.passCode = StringUtils.trimToEmpty(passCode);
    }

    /**
     * Copy data from one form to another
     * 
     * @param src
     */
    public void copyFrom(RegisterFirmRepValidationForm src) {
        if (src != null) {
            lastName = src.getLastName();
            passCode = src.getPassCode();
            phoneNumber.set(src.getPhoneNumber());
        }
    }

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}
