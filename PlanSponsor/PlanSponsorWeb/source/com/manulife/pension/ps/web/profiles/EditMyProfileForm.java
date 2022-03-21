package com.manulife.pension.ps.web.profiles;
 
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.common.FaxNumber;
import com.manulife.pension.common.PhoneNumber;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.service.security.passcode.PasscodeDeliveryPreference;
import com.manulife.pension.service.security.valueobject.MobileNumber;

/**
 * This is Form class for EditMyProfile
 * 
 * @author Ludmila Stern
 */
public class EditMyProfileForm extends AutoForm {
	private static final long serialVersionUID = 5343316042924479520L;

    public final static String YES = "Yes";
    public final static String NO = "No";
	
	private static final String SAVE_ACTION = "save";

    private long profileId;
    private String userName;
	private String firstName;
	private String oldFirstName;
	private String lastName;
	private String oldLastName;

	private String email;
	private String oldEmail;
	
	private String secondaryEmail;
	private String oldSecondaryEmail;
	
	private String oldPassword;
	private String currentPassword;
	private String newPassword;
	private String confirmNewPassword;

	private String challengeQuestion;
	private String oldChallengeQuestion;
	private String challengeAnswer;
	private String oldChallengeAnswer;

	private String verifyChallengeAnswer;

	private String profileLastUpdatedBy;
	private boolean profileLastUpdatedByInternal;
	private Date profileLastUpdatedTS;
	private boolean internalUser;
	private String actionLabel;
	private EditMyProfileForm oldForm;
	private String updatedInformation;
	
	private boolean submitted = false;
    
    private List<MyProfileContractAccess> contractAccesses = new ArrayList<MyProfileContractAccess>(); 
	

	public static final String FIELD_USER_NAME = "userName";
	public static final String FIELD_FIRST_NAME = "firstName";
	public static final String FIELD_LAST_NAME = "lastName";
	public static final String FIELD_EMAIL = "email";
	public static final String FIELD_SECONDARY_EMAIL = "secondaryEmail";
	public static final String FIELD_MOBILE_NO = "mobile";
	public static final String FIELD_PHONE_NO = "phone";
	public static final String FIELD_EXTENSION_NO = "ext";
	public static final String FIELD_FAX_NO = "fax";
	public static final String FIELD_EMAIL_NEWSLETTER = "emailNewsletter";
	public static final String FIELD_OLD_PASSWORD = "oldPassword";
	public static final String FIELD_CURRENT_PASSWORD = "currentPassword";
	public static final String FIELD_VERIFY_PASSWORD = "confirmNewPassword";
	public static final String FIELD_NEW_PASSWORD = "newPassword";
	public static final String FIELD_CHALLENGE_QUESTION = "challengeQuestion";
	public static final String FIELD_CHALLENGE_ANSWER = "challengeAnswer";
	public static final String FIELD_VERIFY_ANSWER = "verifyChallengeAnswer";
	public static final String FIELD_PASSCODE_DELIVERY_PREF = "passcodeDeliveryPreference";
	private static final Map<String, String> ACTION_LABEL_MAP = new HashMap<String, String>();
	private boolean challengeQuestionRequired;
	private MobileNumber mobileNumber = new MobileNumber();
	private PhoneNumber telephoneNumber = new PhoneNumber();
	private String telephoneExtension = "";
	private FaxNumber faxNumber = new FaxNumber();
	private String oldMobileNumber = "";
	private String oldTelephoneNumber = "";
	private String oldTelephoneExtension = "";
	private String oldFaxNumber = "";
	private PasscodeDeliveryPreference passcodeDeliveryPreference ;
	private PasscodeDeliveryPreference oldPasscodeDeliveryPreference ;
	public static final String FIELD_MOBILE_NUMBER = "mobileNumber";
	public static final String FIELD_TELEPHONE_NUMBER = "telephoneNumber";
	public static final String FIELD_TELEPHONE_EXTENSION = "telephoneExtension";
	public static final String FIELD_FAX_NUMBER = "faxNumber";
	public static final String FIELD_EXTENSION_NUMBER = "telephoneExtension";
	public static final String FIELD_CHANNEL = "channel";
	public boolean loginFlow = false;

	/*
	 * Maps the button label to the corresponding action.
	 */
	static {
		ACTION_LABEL_MAP.put("save", "save");
		ACTION_LABEL_MAP.put("cancel", "cancel");
        ACTION_LABEL_MAP.put("Continue", "save");
        ACTION_LABEL_MAP.put("finish", "finish");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.action.Form#reset(org.apache.struts.action.ActionMapping,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	public void clear( HttpServletRequest request) {
		super.reset( request);
	}

	protected void resetConfirmFields() {
		setVerifyChallengeAnswer(this.challengeAnswer);
		setConfirmNewPassword("");
		setCurrentPassword("");
	}

	/**
	 * Gets the email
	 * 
	 * @return Returns a String
	 */
	public String getEmail() {
	    if(email != null && email.trim().length()> 0){
            return email.toLowerCase();
        }
        return email;
	}
	/**
	 * Sets the email
	 * 
	 * @param email
	 *            The email to set
	 */
	public void setEmail(String email) {
		this.email = trimString(email);
	}
	
	public String getSecondaryEmail() {
	    if(secondaryEmail != null && secondaryEmail.trim().length()> 0){
            return secondaryEmail.toLowerCase();
        }
		return secondaryEmail;
	}

	public void setSecondaryEmail(String secondaryEmail) {
		this.secondaryEmail = secondaryEmail;
	}

	public String getOldSecondaryEmail() {
		return oldSecondaryEmail;
	}

	public void setOldSecondaryEmail(String oldSecondaryEmail) {
		this.oldSecondaryEmail = oldSecondaryEmail;
	}

	/**
	 * Gets the oldPassword
	 * 
	 * @return Returns a String
	 */
	public String getOldPassword() {
		return oldPassword;
	}
	/**
	 * Sets the oldPassword
	 * 
	 * @param oldPassword
	 *            The oldPassword to set
	 */
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	/**
	 * Gets the newPassword
	 * 
	 * @return Returns a String
	 */
	public String getNewPassword() {
		return newPassword;
	}
	/**
	 * Sets the newPassword
	 * 
	 * @param newPassword
	 *            The newPassword to set
	 */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	/**
	 * Gets the confirmNewPassword
	 * 
	 * @return Returns a String
	 */
	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}
	/**
	 * Sets the confirmNewPassword
	 * 
	 * @param confirmNewPassword
	 *            The confirmNewPassword to set
	 */
	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}

	/**
	 * Gets the challengeQuestion
	 * 
	 * @return Returns a String
	 */
	public String getChallengeQuestion() {
		return challengeQuestion;
	}

	/**
	 * Sets the challengeQuestion
	 * 
	 * @param challengeQuestion
	 *            The challengeQuestion to set
	 */
	public void setChallengeQuestion(String challengeQuestion) {
		this.challengeQuestion = trimString(challengeQuestion);
	}

	/**
	 * Gets the answer
	 * 
	 * @return Returns a String
	 */
	public String getChallengeAnswer() {
		return challengeAnswer;
	}

	/**
	 * Sets the answer
	 * 
	 * @param answer
	 *            The answer to set
	 */
	public void setChallengeAnswer(String challengeAnswer) {
		this.challengeAnswer = challengeAnswer;
	}

	/**
	 * Gets the verifyAnswer
	 * 
	 * @return Returns a String
	 */
	public String getVerifyChallengeAnswer() {
		return verifyChallengeAnswer;
	}

	/**
	 * Sets the verifyAnswer
	 * 
	 * @param verifyAnswer
	 *            The verifyAnswer to set
	 */
	public void setVerifyChallengeAnswer(String verifyChallengeAnswer) {
		this.verifyChallengeAnswer = verifyChallengeAnswer;
	}

	/**
	 * Gets the firstNameChanged
	 * 
	 * @return Returns a boolean
	 */
	public boolean isFirstNameChanged() {
		return firstName!=null && !firstName.equals(oldFirstName);
	}

	/**
	 * Gets the lastNameChanged
	 * 
	 * @return Returns a boolean
	 */
	public boolean isLastNameChanged() {
		return lastName!=null && !lastName.equals(oldLastName);
	}

	/**
	 * Gets the challengeQuestionChanged
	 * 
	 * @return Returns a boolean
	 */
	public boolean isChallengeQuestionChanged() {
		return challengeQuestion!=null && !challengeQuestion.equals(oldChallengeQuestion);
	}

	/**
	 * Gets the challengeAnswerChanged
	 * 
	 * @return Returns a boolean
	 */
	public boolean isChallengeAnswerChanged() {
		return challengeAnswer!=null && !challengeAnswer.equals(oldChallengeAnswer);
	}

	/**
	 * Gets the isVerifyAnswerChanged
	 * 
	 * @return Returns a boolean
	 */
	public boolean isVerifyAnswerChanged() {
		return  verifyChallengeAnswer!=null && !verifyChallengeAnswer.equals("");
	}

	/**
	 * Gets the passwordChanged
	 * 
	 * @return Returns a boolean
	 */
	public boolean isPasswordChanged() {
		
		return newPassword != null && !newPassword.equals("");

	}
	/**
	 * Gets the passwordChanged
	 * 
	 * @return Returns a boolean
	 */
	public boolean isConfirmNewPasswordChanged() {
		boolean changed = false;
		if (confirmNewPassword != null && !confirmNewPassword.equals(""))
			changed = (!newPassword.equals(confirmNewPassword));
		return changed;
	}

	/**
	 * Gets the isCurrentPasswordChanged
	 * 
	 * @return Returns a boolean
	 */
	public boolean isCurrentPasswordChanged() {
		boolean changed = false;
		if (currentPassword != null && !currentPassword.equals(""))
			changed = (!currentPassword.equals(oldPassword));
		return changed;

	}

	/**
	 * Gets the emailChanged
	 * 
	 * @return Returns a boolean
	 */
	public boolean isEmailChanged() {
		return email!=null && !email.equals(oldEmail);
	}

	/**
	 * Gets the secondaryEmail changed
	 * 
	 * @return Returns a boolean
	 */
	public boolean isSecondaryEmailChanged() {
		return secondaryEmail!=null && !secondaryEmail.equals(oldSecondaryEmail);
	}
	
	/**
	 * Gets the userName
	 * 
	 * @return Returns a String
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the userName
	 * 
	 * @param userName
	 *            The userName to set
	 */
	public void setUserName(String userName) {
		this.userName = trimString(userName);
	}

	/**
	 * Gets the firstName
	 * 
	 * @return Returns a String
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the firstName
	 * 
	 * @param firstName
	 *            The firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = trimString(firstName);
	}

	/**
	 * Gets the lastName
	 * 
	 * @return Returns a String
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * Sets the lastName
	 * 
	 * @param lastName
	 *            The lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = trimString(lastName);
	}

	/**
	 * Gets the profileLastUpdatedBy
	 * 
	 * @return Returns a String
	 */
	public String getProfileLastUpdatedBy() {
		return profileLastUpdatedBy;
	}

	/**
	 * Sets the profileLastUpdatedBy
	 * 
	 * @param profileLastUpdatedBy
	 *            The profileLastUpdatedBy to set
	 */
	public void setProfileLastUpdatedBy(String profileLastUpdatedBy) {
		this.profileLastUpdatedBy = trimString(profileLastUpdatedBy);
	}

	/**
	 * Gets the profileLastUpdatedByInternal
	 * 
	 * @return Returns a boolean
	 */
	public boolean isProfileLastUpdatedByInternal() {
		return profileLastUpdatedByInternal;
	}
	/**
	 * Sets the profileLastUpdatedByInternal
	 * 
	 * @param profileLastUpdatedByInternal
	 *            The profileLastUpdatedByInternal to set
	 */
	public void setProfileLastUpdatedByInternal(
			boolean profileLastUpdatedByInternal) {
		this.profileLastUpdatedByInternal = profileLastUpdatedByInternal;
	}

	/**
	 * Gets the profileLastUpdatedTS
	 * 
	 * @return Returns a Date
	 */
	public Date getProfileLastUpdatedTS() {
		return profileLastUpdatedTS;
	}

	/**
	 * Sets the profileLastUpdatedTS
	 * 
	 * @param profileLastUpdatedTS
	 *            The profileLastUpdatedTS to set
	 */
	public void setProfileLastUpdatedTS(Date profileLastUpdatedTS) {
		this.profileLastUpdatedTS = profileLastUpdatedTS;
	}

	/**
	 * Gets the internalUser
	 * 
	 * @return Returns a boolean
	 */
	public boolean isInternalUser() {
		return internalUser;
	}
	/**
	 * Sets the internalUser
	 * 
	 * @param internalUser
	 *            The internalUser to set
	 */
	public void setInternalUser(boolean internalUser) {
		this.internalUser = internalUser;
	}

	/**
	 * @return Returns the actionLabel.
	 */
	public String getActionLabel() {
		return actionLabel;
	}

	/**
	 * @param actionLabel
	 *            The actionLabel to set.
	 */
	public void setActionLabel(String actionLabel) {
		this.actionLabel = trimString(actionLabel);
		setAction((String) ACTION_LABEL_MAP.get(actionLabel));
	}

	/**
	 * Gets the oldFirstName
	 * 
	 * @return Returns a String
	 */
	public String getOldFirstName() {
		return oldFirstName;
	}

	/**
	 * Sets the oldFirstName
	 * 
	 * @param oldFirstName
	 *            The oldFirstName to set
	 */
	public void setOldFirstName(String oldFirstName) {
		this.oldFirstName = trimString(oldFirstName);
	}

	/**
	 * Gets the oldLastName
	 * 
	 * @return Returns a String
	 */
	public String getOldLastName() {
		return oldLastName;
	}

	/**
	 * Sets the oldLastName
	 * 
	 * @param oldLastName
	 *            The oldLastName to set
	 */
	public void setOldLastName(String oldLastName) {
		this.oldLastName = trimString(oldLastName);
	}

	/**
	 * Gets the oldEmail
	 * 
	 * @return Returns a String
	 */
	public String getOldEmail() {
		return oldEmail;
	}

	/**
	 * Sets the oldEmail
	 * 
	 * @param oldEmail
	 *            The oldEmail to set
	 */
	public void setOldEmail(String oldEmail) {
		this.oldEmail = trimString(oldEmail);
	}
	
	/**
	 * Gets the oldChallengeQuestion
	 * 
	 * @return Returns a String
	 */
	public String getOldChallengeQuestion() {
		return oldChallengeQuestion;
	}

	/**
	 * Sets the oldChallengeQuestion
	 * 
	 * @param oldChallengeQuestion
	 *            The oldChallengeQuestion to set
	 */
	public void setOldChallengeQuestion(String oldChallengeQuestion) {
		this.oldChallengeQuestion = trimString(oldChallengeQuestion);
	}

	/**
	 * Gets the oldChallengeAnswer
	 * 
	 * @return Returns a String
	 */
	public String getOldChallengeAnswer() {
		return oldChallengeAnswer;
	}

	/**
	 * Sets the oldChallengeAnswer
	 * 
	 * @param oldChallengeAnswer
	 *            The oldChallengeAnswer to set
	 */
	public void setOldChallengeAnswer(String oldChallengeAnswer) {
		this.oldChallengeAnswer = oldChallengeAnswer;
	}

	public boolean isInformationUpdated() {
		
		boolean changed =isFirstNameChanged() ||isLastNameChanged() ||isEmailChanged() ||isSecondaryEmailChanged()||isPhoneNumberChanged() ||
				isExtensionChanged() ||
				isFaxNumberChanged() || 
				isMobileNumberChanged() || 
				isPasscodeDeliveryPreferenceChanged() ||
				isPasswordChanged() ||
				isConfirmNewPasswordChanged();
        

		if (challengeQuestionRequired){
			changed = changed||
				isChallengeQuestionChanged() || 
				isChallengeAnswerChanged() ||
				isVerifyAnswerChanged();
		}
        
        for (MyProfileContractAccess contractAccess:contractAccesses){
            changed = changed || contractAccess.isChanged();
        }
	
	return changed;
	
	}
	
	public String getUpdatedInformation() {
		if (updatedInformation == null || updatedInformation.length() == 0) {
			StringBuffer changes = new StringBuffer();

			if (isFirstNameChanged()) {
				changes.append(" First Name: " + firstName);
			}
			if (isLastNameChanged()) {
				changes.append(" Last Name: " + lastName);
			}

			if (isEmailChanged()) {
				changes.append(" Email: " + email);
			}
			if (isSecondaryEmailChanged()) {
				changes.append(" Secondary Email: " + secondaryEmail);
			}

			if(isMobileNumberChanged()) {
				changes.append(" Mobile: " + mobileNumber.getValue());
			}
			
			if(isPasscodeDeliveryPreferenceChanged()) {
				changes.append(" PasscodeDeliveryPreferenceChanged: " + passcodeDeliveryPreference.getDescreption());
			}
			
			if(isPhoneNumberChanged()) {
				changes.append(" Telephone: " + telephoneNumber.getValue());
			}
			if(isExtensionChanged()) {
				changes.append(" ext: " + telephoneExtension);
			}
			if(isFaxNumberChanged()) {
				changes.append(" Fax: " + faxNumber.getValue());
			}

			if (challengeQuestionRequired) {
				if (isChallengeQuestionChanged()) {
					changes.append(" Challenge Question:******");

				}

				if (isChallengeAnswerChanged()) {
					changes.append(" Challenge Answer:*******");
				}
			}
			if (isPasswordChanged()) {
				changes.append(" Password:******");
			}

            for (MyProfileContractAccess contractAccess : contractAccesses) {
                if (contractAccess.isChanged()) {
                    if (contractAccess.getContractNumber() != null && contractAccess.getContractNumber().intValue() != 0) {
                        changes.append(" Contract " + contractAccess.getContractNumber());
                    }

                    if (contractAccess.isSpecialAttributeChanged()) {
                    	changes.append(" Special Attributes Changes: {");
                    	if(contractAccess.isPrimaryContactChanged()){
                            changes.append(" Primary contact: " + contractAccess.isPrimaryContact());                    		
                    	}
                    	else if(contractAccess.isMailRecepientChanged()){
                    		changes.append(" Client mail recipient: " + contractAccess.isMailRecepient());
                    	}
                    	else if(contractAccess.isTrusteeMailRecepientChanged()){
                    		changes.append(" Trustee mail recipient: " + contractAccess.isTrusteeMailRecepient());
                    	}
                    	changes.append("}");
                    }
                    changes.append(" Email Preference Changes: {");
                    if (contractAccess.isReceiveILoanEmailChanged()) {
                        changes.append(" ILoan Email: " + contractAccess.getReceiveILoanEmail());
                    }
                    if (contractAccess.isEmailNewsLetterChanged()) {
                        changes.append(" Newsletter Email: " + contractAccess.getEmailNewsletter());
                    }
                    changes.append("}");
                }
            }

			this.updatedInformation = changes.toString();
		}
		return updatedInformation;
	}
	
	
	public void resetUpdatedInformation() {
		updatedInformation = null;
	}

	public Map getFormAsMap() {
		Map formMap = new HashMap();
		return formMap;
	}

	/**
	 * Gets the oldForm
	 * 
	 * @return Returns a EditMyProfileForm
	 */
	public EditMyProfileForm getOldForm() {
		return oldForm;
	}

	/**
	 * Sets the oldForm
	 * 
	 * @param oldForm
	 *            The oldForm to set
	 */
	public void setOldForm(EditMyProfileForm oldForm) {
		this.oldForm = oldForm;
	}

	/**
	 * Gets the currentPassword
	 * 
	 * @return Returns a String
	 */
	public String getCurrentPassword() {
		return currentPassword;
	}

	/**
	 * Sets the currentPassword
	 * 
	 * @param currentPassword
	 *            The currentPassword to set
	 */
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	/**
	 * Gets the isChallengeQuestionRequired
	 * 
	 * @return Returns a boolean
	 */
	public boolean isChallengeQuestionRequired() {
		return challengeQuestionRequired;
	}

	/**
	 * Sets the isChallengeQuestionRequired
	 * 
	 * @param isChallengeQuestionRequired
	 *            The isChallengeQuestionRequired to set
	 */
	public void setChallengeQuestionRequired(boolean challengeQuestionRequired) {
		this.challengeQuestionRequired = challengeQuestionRequired;
	}
	
	public boolean isSaveAction() {
		return getAction().equals(SAVE_ACTION);
	}


    /**
     * @return the profileId
     */
    public long getProfileId() {
        return profileId;
    }

    /**
     * @param profileId the profileId to set
     */
    public void setProfileId(long profileId) {
        this.profileId = profileId;
    }

    public List<MyProfileContractAccess> getContractAccesses() {
        return contractAccesses;
    }

    public void setContractAccesses(List<MyProfileContractAccess> contractAccesses) {
        this.contractAccesses = contractAccesses;
    }
    
    public MyProfileContractAccess getContractAccess(int i){
        return contractAccesses.get(i);
    }

    public boolean isEmailPreferenceShown() {
        boolean emailPreferenceShown = false;
        for (MyProfileContractAccess contractAccess : contractAccesses) {
            if (contractAccess.isShowEmailPreferences()) {
                emailPreferenceShown = true;
                break;
            }
        }
        return emailPreferenceShown;
    }

	public boolean isSubmitted() {
		return submitted;
	}

	public void setSubmitted(boolean submitted) {
		this.submitted = submitted;
	}

	/**
	 * @return the telephoneNumber
	 */
	public PhoneNumber getTelephoneNumber() {
		return telephoneNumber;
	}

	/**
	 * @param telephoneNumber the telephoneNumber to set
	 */
	public void setTelephoneNumber(PhoneNumber telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	/**
	 * @return the telephoneExtension
	 */
	public String getTelephoneExtension() {
		return telephoneExtension;
	}

	/**
	 * @param telephoneExtension the telephoneExtension to set
	 */
	public void setTelephoneExtension(String telephoneExtension) {
		this.telephoneExtension = telephoneExtension;
	}

	/**
	 * @return the faxNumber
	 */
	public FaxNumber getFaxNumber() {
		return faxNumber;
	}

	/**
	 * @param faxNumber the faxNumber to set
	 */
	public void setFaxNumber(FaxNumber faxNumber) {
		this.faxNumber = faxNumber;
	}
	
	public PasscodeDeliveryPreference getPasscodeDeliveryPreference() {
		return passcodeDeliveryPreference;
	}

	public void setPasscodeDeliveryPreference(PasscodeDeliveryPreference passcodeDeliveryPreference) {
		this.passcodeDeliveryPreference = passcodeDeliveryPreference;
	}
	public PasscodeDeliveryPreference getOldPasscodeDeliveryPreference() {
		return oldPasscodeDeliveryPreference;
	}

	public void setOldPasscodeDeliveryPreference(PasscodeDeliveryPreference oldPasscodeDeliveryPreference) {
		this.oldPasscodeDeliveryPreference = oldPasscodeDeliveryPreference;
	}
	/**
	 * @return the oldTelephoneExtension
	 */
	public String getOldTelephoneExtension() {
		return oldTelephoneExtension;
	}

	/**
	 * @param oldTelephoneExtension the oldTelephoneExtension to set
	 */
	public void setOldTelephoneExtension(String oldTelephoneExtension) {
		this.oldTelephoneExtension = oldTelephoneExtension;
	}
	
	
	/**
	 * @return the MobileChanged
	 */
	public boolean isMobileNumberChanged() {
		return (mobileNumber != null && ! StringUtils.equals(mobileNumber.getValue(), oldMobileNumber));
	}
	
	public boolean isPasscodeDeliveryPreferenceChanged() {
		return (passcodeDeliveryPreference != null && ! passcodeDeliveryPreference.equals(oldPasscodeDeliveryPreference));
	}
	
	/**
	 * @return the phoneNumberChanged
	 */
	public boolean isPhoneNumberChanged() {
		return (telephoneNumber != null && ! StringUtils.equals(telephoneNumber.getValue(), oldTelephoneNumber));
	}

	public String getOldMobileNumber() {
		return oldMobileNumber;
	}

	public void setOldMobileNumber(String oldMobileNumber) {
		this.oldMobileNumber = oldMobileNumber;
	}

	public MobileNumber getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(MobileNumber mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * @return the extensionChanged
	 */
	public boolean isExtensionChanged() {
		return (telephoneExtension != null && !StringUtils.equals(telephoneExtension, oldTelephoneExtension));
	}

	/**
	 * @return the faxNumberChanged
	 */
	public boolean isFaxNumberChanged() {
		return (faxNumber != null && !StringUtils.equals(faxNumber.getValue(), oldFaxNumber));
	}


	/**
	 * @return the oldTelephoneNumber
	 */
	public String getOldTelephoneNumber() {
		return oldTelephoneNumber;
	}

	/**
	 * @param oldTelephoneNumber the oldTelephoneNumber to set
	 */
	public void setOldTelephoneNumber(String oldTelephoneNumber) {
		this.oldTelephoneNumber = oldTelephoneNumber;
	}

	/**
	 * @return the oldFaxNumber
	 */
	public String getOldFaxNumber() {
		return oldFaxNumber;
	}

	/**
	 * @param oldFaxNumber the oldFaxNumber to set
	 */
	public void setOldFaxNumber(String oldFaxNumber) {
		this.oldFaxNumber = oldFaxNumber;
	}

	/**
	 * @return the loginFlow
	 */
	public boolean isLoginFlow() {
		return loginFlow;
	}

	/**
	 * @param loginFlow the loginFlow to set
	 */
	public void setLoginFlow(boolean loginFlow) {
		this.loginFlow = loginFlow;
	}

	
}