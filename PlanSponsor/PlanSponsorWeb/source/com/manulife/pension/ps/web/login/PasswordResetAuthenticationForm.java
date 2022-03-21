package com.manulife.pension.ps.web.login;

import javax.servlet.http.HttpServletRequest;


import com.manulife.pension.platform.web.passcode.EmailAddressMaskFormat;
import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.ps.web.controller.PsForm;
import com.manulife.pension.service.security.passcode.PasscodeDeliveryPreference;

/**
 * @author Chris Shin
 */
public class PasswordResetAuthenticationForm extends PsForm {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final EmailAddressMaskFormat MASK_FORMAT = new EmailAddressMaskFormat("the primary email address on your profile");
    
	private static final int maxSteps = 3;

	public static final String FIELD_SSN = "ssn";
	public static final String FIELD_EMAIL = "emailAddress";
	public static final String FIELD_CONTRACT_NUMBER = "contractNumber";
	public static final String FIELD_CHALLENGE_ANSWER = "challengeAnswer";
	public static final String FIELD_NEW_PASSWORD = "newPassword";
	public static final String FIELD_CONFIRM_PASSWORD = "confirmPassword";	

	// forward actions
	public static final String CONTINUE = "continue";
	public static final String DEFAULT = "default";
	public static final String RESTART = "restart";
	public static final String FINISHED = "finished";
	public static final String ERRORS = "errors";
	public static final String LOGIN = "login";
	public static final String CANCEL = "cancel";
	
	// Password reset steps
	public static final int STEP_PASSWORD_RESET_AUTHENTICATION = 0;
	public static final int STEP_PASSWORD_CHALLENGE = 1;
	public static final int STEP_PASSWORD_RESET = 2;
	//public static final int STEP_PASSWORD_PASSCODE = 3;
	public static final int STEP_PASSWORD_CONFIRMATION = 3;
	
	// flags to sequence
	private boolean[] stepsCompleted = new boolean[] {false, false, false, false};

	/*
	 * step 1 fields
	 */
	private String emailAddress;
	private Ssn ssn = new Ssn();
	private String contractNumber;
	private String button;
	private long profileId;

	/*
	 * step 2 fields
	 */
	private String savedChallengeQuestion;
	private String challengeAnswer;


	/*
	 * step 3 fields
	 */
	private String username;
	private String name;
	private String newPassword;
	private String confirmPassword;

	// Add for logging purposes
	private String firstName;
	private String lastName;

	static final String DISPALY_USER_NAME = "dispalyUserName";

	private PasscodeDeliveryPreference passcodeDeliveryPreference;
	private String maskedMobile;
	private String maskedPhone;
	private String maskedEmail;
	private boolean smsSwithOn = false;
	private boolean voiceSwithOn = false;
	private boolean emailSwithOn = false;
	
	private boolean isOptionalPassodeChannel = false;
	private boolean isPhoneNumberCollection = false;
	
	
	public boolean isOptionalPassodeChannel() {
		return isOptionalPassodeChannel;
	}

	public void setOptionalPassodeChannel(boolean isOptionalPassodeChannel) {
		this.isOptionalPassodeChannel = isOptionalPassodeChannel;
	}

	public boolean isPhoneNumberCollection() {
		return isPhoneNumberCollection;
	}

	public void setPhoneNumberCollection(boolean isPhoneNumberCollection) {
		this.isPhoneNumberCollection = isPhoneNumberCollection;
	}

	
	
	public PasscodeDeliveryPreference getPasscodeDeliveryPreference() {
		return passcodeDeliveryPreference;
	}
	public void setPasscodeDeliveryPreference(PasscodeDeliveryPreference passcodeDeliveryPreference) {
		this.passcodeDeliveryPreference = passcodeDeliveryPreference;
	}
	public String getMaskedMobile() {
		return maskedMobile;
	}
	public void setMaskedMobile(String maskedMobile) {
		this.maskedMobile = maskedMobile;
	}
	public String getMaskedPhone() {
		return maskedPhone;
	}
	public void setMaskedPhone(String maskedPhone) {
		this.maskedPhone = maskedPhone;
	}
	public String getMaskedEmail() {
		return maskedEmail;
	}
	public void setMaskedEmail(String maskedEmail) {
		this.maskedEmail = maskedEmail;
	}
	public boolean isSmsSwithOn() {
		return smsSwithOn;
	}
	public void setSmsSwithOn(boolean smsSwithOn) {
		this.smsSwithOn = smsSwithOn;
	}
	public boolean isVoiceSwithOn() {
		return voiceSwithOn;
	}
	public void setVoiceSwithOn(boolean voiceSwithOn) {
		this.voiceSwithOn = voiceSwithOn;
	}
	public boolean isEmailSwithOn() {
		return emailSwithOn;
	}
	public void setEmailSwithOn(boolean emailSwithOn) {
		this.emailSwithOn = emailSwithOn;
	}
	
	private String passcode;

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

	/**
	 * Constructor.
	 */
	public PasswordResetAuthenticationForm() {
		super();
	}

	/**
	 * @return Returns the contractNumber.
	 */
	public String getContractNumber() {
		return contractNumber;
	}

	/**
	 * @param contractNumber
	 *            The contractNumber to set.
	 */
	public void setContractNumber(String contractNumber) {
		
		this.contractNumber = contractNumber;
	}

	/**
	 * @return Returns the email.
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress
	 *            The emailAddress to set.
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public String getMaskedEmailAddress() { return MASK_FORMAT.format(emailAddress); }
	

	public String getMaskedSSN() {
		if (ssn!=null && !ssn.isEmpty()) {
			return "XXX-XX-"+getSsn().toString().substring(5);
		} else {
			return "";
		}
	}
	
	/**
	 * @return Returns the ssn.
	 */
	public Ssn getSsn() {
		return ssn;
	}

	/**
	 * @param ssn
	 *            The ssn to set.
	 */
	public void setSsn(Ssn ssn) {
		this.ssn = ssn;
	}
	

	/**
	 * @return Returns the profileId.
	 */
	public long getProfileId() {
		return profileId;
	}

	/**
	 * @param profileId
	 *            The profileId to set.
	 */
	public void setProfileId(long profileId) {
		
		this.profileId = profileId;
	}



	/**
	 * @return Returns the savedChallengeQuestion.
	 */
	public String getSavedChallengeQuestion() {
		return this.savedChallengeQuestion;
	}
	
	/**
	 * @param savedChallengeQuestion
	 *            The savedChallengeQuestion to set.
	 */	
	public void setSavedChallengeQuestion(String savedChallengeQuestion) {
		this.savedChallengeQuestion = savedChallengeQuestion;
	}

	/**
	 * @return Returns the challengeAnswer.
	 */
	public String getChallengeAnswer() {
		return this.challengeAnswer;
	}
	
	/**
	 * @param challengeAnswer
	 *            The challengeAnswer to set.
	 */	
	public void setChallengeAnswer(String challengeAnswer) {
		this.challengeAnswer = challengeAnswer;
	}

	/**
	 * @return Returns the button.
	 */
	public String getButton() {
		return this.button;
	}

	/**
	 * @param button
	 *            The button to set.
	 */
	public void setButton(String button) {
		this.button = button;
	}
	
	/**
	 * @return Returns the username.
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * @param username
	 *            The username to set.
	 */		
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return Returns the newPassword.
	 */	
	public String getNewPassword() {
		return this.newPassword;
	}
	/**
	 * @param newPassword
	 *            The newPassword to set.
	 */		
	
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	/**
	 * @return Returns the confirmPassword.
	 */
	public String getConfirmPassword() {
		return this.confirmPassword;
	}
	
	/**
	 * @param confirmPassword
	 *            The confirmPassword to set.
	 */		
	
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @param name
	 *            The name to set.
	 */		
	
	public void setName(String name) {
		this.name = name;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.action.Form#reset(org.apache.struts.action.ActionMapping,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	public void clear( HttpServletRequest request) {
		//super.reset(mapping, request);
		ssn = new Ssn();
		emailAddress = null;
		contractNumber = null;
		profileId = 0;
		challengeAnswer = null;
		savedChallengeQuestion = null;
		username = null;
		newPassword = null;
		confirmPassword = null;
		name = null;
		stepsCompleted = new boolean[]{false,false,false,false};
		resetButton();
	}
	
	public void resetButton() {
		this.button = null;
	}

	protected boolean isStepValid(int stepIndex) {
		// check that steps completed in proper order
		
		boolean valid = true;
		
		for (int i=0; i<stepIndex; i++) {
			if (!this.stepsCompleted[i]) {		
				valid = false;
			}
		}		
		
		return valid;
	}
	
	public void setStepCompleted(int stepIndex) {
		this.stepsCompleted[stepIndex] = true;
		
		for (int i=stepIndex+1;i<=maxSteps;i++) {
			this.stepsCompleted[i] = false;
		}
	}	
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("emailAddress [");
		buf.append(emailAddress);
		buf.append("] SSN [");
		buf.append(ssn.getDigits(0));
		buf.append("-");
		buf.append(ssn.getDigits(1));
		buf.append("-");
		buf.append(ssn.getDigits(2));
		buf.append("] contractNumber [");
		buf.append(contractNumber);
		buf.append("] profileId [");
		buf.append(profileId);
		buf.append("] challengeQuestion [");
		buf.append(savedChallengeQuestion);
		buf.append("] challengeAnswer [");
		buf.append(challengeAnswer);
		buf.append("] username [");
		buf.append(username);
		buf.append("] newPassword [");
		buf.append(newPassword);
		buf.append("] confirmPassword [");
		buf.append(confirmPassword);
		buf.append("] name [");
		buf.append(name);
		buf.append("] button [");
		buf.append(button);
		buf.append("] \n");

		return buf.toString();
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

}