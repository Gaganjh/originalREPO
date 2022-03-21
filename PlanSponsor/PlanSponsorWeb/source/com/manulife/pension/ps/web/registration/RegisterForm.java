package com.manulife.pension.ps.web.registration;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;



import com.manulife.pension.common.PhoneNumber;
import com.manulife.pension.common.FaxNumber;
import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.ps.web.profiles.ContractAccess;
import com.manulife.pension.ps.web.profiles.TpaFirm;
import com.manulife.pension.ps.web.util.CloneableAutoForm;
import com.manulife.pension.service.security.passcode.PasscodeDeliveryPreference;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.valueobject.MobileNumber;

/** 
 * A simple session form to contain the values
 * from the registration 4 step process wizard.
 * @author Tony Tomasone
 * 
 * TODO: We really should combine all UserForm together.
 * 
 */
public final class RegisterForm extends CloneableAutoForm {
	public static final String FIELD_CONTRACT_NUMBER = "contractNumber";
	public static final String FIELD_FIRM_ID = "firmId";
	public static final String FIELD_SSN = "ssn";
	public static final String FIELD_MOBILE_NO = "mobile";
	public static final String FIELD_PHONE_NO = "phone";
	public static final String FIELD_EXTENSION_NO = "ext";
	public static final String FIELD_FAX_NO = "fax";
	public static final String FIELD_PIN = "pin";
	public static final String FIELD_EMAIL = "emailAddress";
	public static final String FIELD_USER_NAME = "userName";
	public static final String FIELD_PASSWORD = "password";
	public static final String FIELD_CONFIRM_PASSWORD = "confirmPassword";	
	public static final String FIELD_CHALLENGE_QUESTION = "challengeQuestion";
	public static final String FIELD_ANSWER = "answer";
	public static final String FIELD_CONFIRM_ANSWER = "confirmAnswer";	
	public static final String FIELD_PASSCODE_DELIVERY_PREF = "passcodeDeliveryPreference";


	// page one authentication
	private String contractNumber;
	private String firmId;
	private String ssnOne;
	private String ssnTwo;
	private String ssnThree;
	private Ssn ssn;
	private String pin;
	private boolean authenticateValid=false;
	private boolean tpa=false;
	
	// page two terms
	private String acceptedTerms;
	private boolean termsValid=false;
	
	// page three registration
	private String email;
	private String ext;
	private PhoneNumber phone = new PhoneNumber();
	private MobileNumber mobile = new MobileNumber();

	private FaxNumber fax = new FaxNumber();
	private String userName;
	private String firstName;
	private String lastName;
	private String password;
	private String confirmPassword;
	private String challengeQuestion;
	private String answer;
	private String confirmAnswer;
	private boolean registerValid=false;
	private PasscodeDeliveryPreference passcodeDeliveryPreference ;
		
	private UserRole accessLevel;
	
	private ContractAccess[] contractAccess;
	
	private List tpaFirms = new ArrayList();
	
	private String defaultUserName;
	
	private int existingSiteUserFailedCount = 0;
	/**
	 * Gets the contractNumber
	 * @return Returns a String
	 */
	public String getContractNumber() {
		return contractNumber;
	}
	
	/**
	 * Sets the contractNumber
	 * @param contractNumber The contractNumber to set
	 */
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}


	/**
	 * Gets the pin
	 * @return Returns a String
	 */
	public String getPin() {
		return pin;
	}
	/**
	 * Sets the pin
	 * @param pin The pin to set
	 */
	public void setPin(String pin) {
		this.pin = pin;
	}


	/**
	 * Gets the acceptedTerms
	 * @return Returns a String
	 */
	public String getAcceptedTerms() {
		return acceptedTerms;
	}
	/**
	 * Sets the acceptedTerms
	 * @param acceptedTerms The acceptedTerms to set
	 */
	public void setAcceptedTerms(String acceptedTerms) {
		this.acceptedTerms = acceptedTerms;
	}


	/**
	 * Gets the userName
	 * @return Returns a String
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * Sets the userName
	 * @param userName The userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}


	/**
	 * Gets the password
	 * @return Returns a String
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * Sets the password
	 * @param password The password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}


	/**
	 * Gets the confirmPassword
	 * @return Returns a String
	 */
	public String getConfirmPassword() {
		return confirmPassword;
	}
	/**
	 * Sets the confirmPassword
	 * @param confirmPassword The confirmPassword to set
	 */
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}


	/**
	 * Gets the challengeQuestion
	 * @return Returns a String
	 */
	public String getChallengeQuestion() {
		return challengeQuestion;
	}
	/**
	 * Sets the challengeQuestion
	 * @param challengeQuestion The challengeQuestion to set
	 */
	public void setChallengeQuestion(String challengeQuestion) {
		this.challengeQuestion = challengeQuestion;
	}


	/**
	 * Gets the answer
	 * @return Returns a String
	 */
	public String getAnswer() {
		return answer;
	}
	/**
	 * Sets the answer
	 * @param answer The answer to set
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}


	/**
	 * Gets the confirmAnswer
	 * @return Returns a String
	 */
	public String getConfirmAnswer() {
		return confirmAnswer;
	}
	/**
	 * Sets the confirmAnswer
	 * @param confirmAnswer The confirmAnswer to set
	 */
	public void setConfirmAnswer(String confirmAnswer) {
		this.confirmAnswer = confirmAnswer;
	}
	/**
	 * @return Returns the phone.
	 */
	public PhoneNumber getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            The phone to set.
	 */
	public void setPhone(PhoneNumber  phone) {
		this.phone = phone;
	}
	
	public MobileNumber getMobile() {
		return mobile;
	}

	public void setMobile(MobileNumber mobile) {
		this.mobile = mobile;
	}
	
	/**
	 * @return Returns the ext.
	 */
	public String getExt() {
		return ext;
	}

	/**
	 * @param ext
	 *            The ext to set.
	 */
	public void setExt(String  ext) {
		this.ext = ext;
	}
	
	/**
	 * @return Returns the fax.
	 */
	public FaxNumber getFax() {
		return fax;
	}

	/**
	 * @param fax
	 *            The fax to set.
	 */
	public void setFax(FaxNumber  fax) {
		this.fax = fax;
	}

	/**
	 * Gets the ssn
	 * @return Returns a Ssn
	 */
	public Ssn getSsn() {
		Ssn ssnTemp = new Ssn();
		ssnTemp.setDigits(0,ssnOne);
		ssnTemp.setDigits(1,ssnTwo);
		ssnTemp.setDigits(2,ssnThree);
		return ssnTemp;
	}
	/**
	 * Gets the ssnOne
	 * @return Returns a String
	 */
	public String getSsnOne() {
		return ssnOne;
	}
	/**
	 * Sets the ssnOne
	 * @param ssnOne The ssnOne to set
	 */
	public void setSsnOne(String ssnOne) {
		this.ssnOne = ssnOne;
	}


	/**
	 * Gets the ssnTwo
	 * @return Returns a String
	 */
	public String getSsnTwo() {
		return ssnTwo;
	}
	/**
	 * Sets the ssnTwo
	 * @param ssnTwo The ssnTwo to set
	 */
	public void setSsnTwo(String ssnTwo) {
		this.ssnTwo = ssnTwo;
	}


	/**
	 * Gets the ssnThree
	 * @return Returns a String
	 */
	public String getSsnThree() {
		return ssnThree;
	}
	/**
	 * Sets the ssnThree
	 * @param ssnThree The ssnThree to set
	 */
	public void setSsnThree(String ssnThree) {
		this.ssnThree = ssnThree;
	}

	public String toString() {
		StringBuffer buff = new StringBuffer("RegisterForm: ");
	
		buff.append("[action = \""				).append(getAction()		).append("\" ]");
		buff.append("[contractNumber = \""		).append(contractNumber		).append("\" ]");
		buff.append("[firmId = \""				).append(firmId				).append("\" ]");
		buff.append("[ssnOne = \""				).append(ssnOne				).append("\" ]");
		buff.append("[ssnTwo = \""				).append(ssnTwo				).append("\" ]");
		buff.append("[ssnThree = \""			).append(ssnThree			).append("\" ]");
		buff.append("[pin = \""					).append(pin				).append("\" ]");
		buff.append("[authenticateValid = \""	).append(authenticateValid	).append("\" ]");
		buff.append("[tpa = \""					).append(tpa				).append("\" ]");

		
		buff.append("[acceptedTerms = \""		).append(acceptedTerms		).append("\" ]");
		buff.append("[termsValid = "			).append(termsValid			).append("\" ]");

		buff.append("[userName = \""			).append(userName			).append("\" ]");
		buff.append("[password = \""			).append(password			).append("\" ]");
		buff.append("[confirmPassword = \""		).append(confirmPassword	).append("\" ]");
		buff.append("[challengeQuestion = \""	).append(challengeQuestion	).append("\" ]");
		buff.append("[answer = \""				).append(answer				).append("\" ]");
		buff.append("[confirmAnswer = \""		).append(confirmAnswer		).append("\" ]");
		buff.append("[registerValid = \""		).append(registerValid		).append("\" ]");

		buff.append("\n");

		
    	return buff.toString();
	
	}
	
	public void reset( HttpServletRequest request) {
		super.reset( request);
	}	


	/**
	 * Gets the email
	 * @return Returns a String
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * Sets the email
	 * @param email The email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}


	/**
	 * Gets the getPasscodeDeliveryPreference
	 * @return Returns a PasscodeDeliveryPreference
	 */
	public PasscodeDeliveryPreference getPasscodeDeliveryPreference() {
		return passcodeDeliveryPreference;
	}
	/**
	 * Sets the passcodeDeliveryPreference
	 * @param passcodeDeliveryPreference The passcodeDeliveryPreference to set
	 */
	public void setPasscodeDeliveryPreference(PasscodeDeliveryPreference passcodeDeliveryPreference) {
		this.passcodeDeliveryPreference = passcodeDeliveryPreference;
	}
	/**
	 * Gets the registerValid
	 * @return Returns a boolean
	 */
	public boolean getRegisterValid() {
		return registerValid;
	}
	/**
	 * Sets the registerValid
	 * @param registerValid The registerValid to set
	 */
	public void setRegisterValid(boolean registerValid) {
		this.registerValid = registerValid;
	}


	/**
	 * Gets the termsValid
	 * @return Returns a boolean
	 */
	public boolean getTermsValid() {
		return termsValid;
	}
	/**
	 * Sets the termsValid
	 * @param termsValid The termsValid to set
	 */
	public void setTermsValid(boolean termsValid) {
		this.termsValid = termsValid;
	}


	/**
	 * Gets the authenticateValid
	 * @return Returns a boolean
	 */
	public boolean getAuthenticateValid() {
		return authenticateValid;
	}
	/**
	 * Sets the authenticateValid
	 * @param authenticateValid The authenticateValid to set
	 */
	public void setAuthenticateValid(boolean authenticateValid) {
		this.authenticateValid = authenticateValid;
	}


	/**
	 * Gets the tpa
	 * @return Returns a boolean
	 */
	public boolean getTpa() {
		return tpa;
	}
	/**
	 * Sets the tpa
	 * @param tpa The tpa to set
	 */
	public void setTpa(boolean tpa) {
		this.tpa = tpa;
	}


	/**
	 * Gets the firmId
	 * @return Returns a String
	 */
	public String getFirmId() {
		return firmId;
	}
	/**
	 * Sets the firmId
	 * @param firmId The firmId to set
	 */
	public void setFirmId(String firmId) {
		this.firmId = firmId;
	}


	/**
	 * Gets the firstName
	 * @return Returns a String
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * Sets the firstName
	 * @param firstName The firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	/**
	 * Gets the lastName
	 * @return Returns a String
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * Sets the lastName
	 * @param lastName The lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	


	

	/**
	 * Gets the defaultUserName
	 * @return Returns a String
	 */
	public String getDefaultUserName() {
		return defaultUserName;
	}
	/**
	 * Sets the defaultUserName
	 * @param defaultUserName The defaultUserName to set
	 */
	public void setDefaultUserName(String defaultUserName) {
		this.defaultUserName = defaultUserName;
	}


	/**
	 * Gets the accessLevel
	 * @return Returns a Role
	 */
	public UserRole getAccessLevel() {
		return accessLevel;
	}
	


	/**
	 * Sets the accessLevel
	 * @param accessLevel The accessLevel to set
	 */
	public void setAccessLevel(UserRole accessLevel) {
		this.accessLevel = accessLevel;
	}


	/**
	 * Gets the contractAccess
	 * @return Returns a ContractAccess
	 */
	public ContractAccess[] getContractAccess() {
		return contractAccess;
	}
	/**
	 * Sets the contractAccess
	 * @param contractAccess The contractAccess to set
	 */
	public void setContractAccess(ContractAccess[] contractAccess) {
		this.contractAccess = contractAccess;
	}


	/**
	 * Gets the existingSiteUserFailedCount
	 * @return Returns a int
	 */
	public int getExistingSiteUserFailedCount() {
		return existingSiteUserFailedCount;
	}
	/**
	 * Increase the existingSiteUserFailedCount
	 */
	public void increaseExistingSiteUserFailedCount() {
		if ( !tpa )
			this.existingSiteUserFailedCount++;
	}

	public TpaFirm getTpaFirm(int index) {
		while (index > tpaFirms.size() - 1) {
			tpaFirms.add(new TpaFirm());
		}
		return (TpaFirm) tpaFirms.get(index);
	}

	public List getTpaFirms() {
		return tpaFirms;
	}
}


