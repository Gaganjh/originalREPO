/**
 * 
 */
package com.manulife.pension.bd.web.registration;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.registration.util.PhoneNumber;
import com.manulife.pension.platform.web.controller.BaseForm;

/**
 * @author narintr
 * 
 */
public class RegisterRiaUserValidationForm extends BaseForm {

	private static final long serialVersionUID = 1L;

	public static final String FIELD_FIRST_NAME = "firstName";

	public static final String FIELD_LAST_NAME = "lastName";

	public static final String FIELD_EMAIL = "emailAddress";

	public static final String FIELD_PHONE_NUMBER = "phoneNumber";

	public static final String FIELD_IARD_NUMBER = "iardNumber";

	public static final String FIELD_PASSCODE = "passCode";

	private String firstName;

	private String lastName;

	private String emailAddress;

	private PhoneNumber phoneNumber = new PhoneNumber();

	private String iardNumber;

	private String passCode;

	private boolean disabled = false;

	/**
	 * Returns the firstName
	 * 
	 * @return String
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the firstName
	 * 
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = StringUtils.trimToEmpty(firstName);
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
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress
	 *            the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
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

	/**
	 * @return the iardNumber
	 */
	public String getIardNumber() {
		return iardNumber;
	}

	/**
	 * @param iardNumber
	 *            the iardNumber to set
	 */
	public void setIardNumber(String iardNumber) {
		this.iardNumber = StringUtils.trimToEmpty(iardNumber);
	}

	/**
	 * @return the passCode
	 */
	public String getPassCode() {
		return passCode;
	}

	/**
	 * @param passCode
	 *            the passCode to set
	 */
	public void setPassCode(String passCode) {
		this.passCode = passCode;
	}

	/**
	 * Copy data from one form to another
	 * 
	 * @param src
	 */
	public void copyFrom(RegisterRiaUserValidationForm src) {
		if (src != null) {
			firstName = src.getFirstName();
			lastName = src.getLastName();
			emailAddress = src.getEmailAddress();
			phoneNumber.set(src.getPhoneNumber());
			iardNumber = src.getIardNumber();
			passCode = src.getPassCode();
		}
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

}
