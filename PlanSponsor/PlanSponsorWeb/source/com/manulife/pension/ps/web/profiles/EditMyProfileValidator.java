package com.manulife.pension.ps.web.profiles;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.platform.web.util.ExtensionRule;
import com.manulife.pension.platform.web.util.FaxRule;
import com.manulife.pension.platform.web.util.PhoneRule;
import com.manulife.pension.platform.web.validation.rules.NameRule;
import com.manulife.pension.platform.web.validation.rules.NewPasswordRule;
import com.manulife.pension.platform.web.validation.rules.PrimaryEmailRule;
import com.manulife.pension.platform.web.validation.rules.SecondaryEmailRule;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.rules.AnswerConfirmRule;
import com.manulife.pension.ps.web.validation.rules.PrimaryEmailNotLocalRule;
import com.manulife.pension.ps.web.validation.rules.QuestionRule;
import com.manulife.pension.ps.web.validation.rules.SecondaryEmailNotLocalRule;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.util.Pair;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;
@Component
public class EditMyProfileValidator implements Validator {

	
	private static Logger logger = Logger.getLogger(EditMyProfileValidator.class);
	/*@Override
	public boolean supports(Class arg0) {
		// TODO Auto-generated method stub
		return false;
	}*/

	@Override
	public boolean supports(Class<?> clazz) {
		return EditMyProfileForm.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {

		BindingResult bindingResult = (BindingResult)errors;
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		EditMyProfileForm actionForm = (EditMyProfileForm) target;
		Collection errorList = new ArrayList();
		
	        if (logger.isDebugEnabled()) {
	            logger.debug("entry <-- doValidate");
	        }
	        Pair pair = null;
	      
	        UserProfile user = SessionHelper.getUserProfile(request);
	        if (actionForm.isSaveAction()) {
	        	
	           

	            if (actionForm.isFirstNameChanged()) {
	                NameRule.getFirstNameInstance().validate(EditMyProfileForm.FIELD_FIRST_NAME, errorList, actionForm.getFirstName());
	            }
	            if (actionForm.isLastNameChanged()) {
	                NameRule.getLastNameInstance().validate(EditMyProfileForm.FIELD_LAST_NAME, errorList, actionForm.getLastName());
	            }
	 
	            if (actionForm.isEmailChanged()) {
	            	PrimaryEmailRule.getInstance().validate(EditMyProfileForm.FIELD_EMAIL, errorList, actionForm.getEmail());
	                if (user.getRole().isTPA()) {
	                    if (actionForm.getEmail().toLowerCase().contains("@jhancock") || actionForm.getEmail().toLowerCase().contains("@manulife")) {
	                        if (!actionForm.getContractAccess(0).getTpaFirmIds().contains(new Integer(52801)) || actionForm.getContractAccess(0).getTpaFirmIds().size() > 1) {
	                            errorList.add(new GenericException(ErrorCodes.PRIMARY_EMAIL_MUST_BE_EXTERNAL));
	                        }
	                    }
	                } else {
	                int currentContractNumber = 0;
	                Contract currentContract = user.getCurrentContract();
	                if (currentContract != null) {
	                	currentContractNumber = currentContract.getContractNumber();
	                }
	                PrimaryEmailNotLocalRule emailNotLocalRule = new PrimaryEmailNotLocalRule(currentContractNumber);
	                emailNotLocalRule.validate(EditMyProfileForm.FIELD_EMAIL, errorList, actionForm.getEmail());
	                }
	            }
	            
	            if (actionForm.isSecondaryEmailChanged() && StringUtils.isNotBlank(actionForm.getSecondaryEmail())) {
	            	SecondaryEmailRule.getInstance().validate(EditMyProfileForm.FIELD_SECONDARY_EMAIL, errorList, actionForm.getSecondaryEmail());
	            	if(actionForm.getSecondaryEmail().equals(actionForm.getEmail())){
	            		errorList.add(new GenericException(ErrorCodes.EMAIL_MATCHES_WITH_PRIMARY_EMAIL));
	            	}
	                if (user.getRole().isTPA()) {
	                    if (actionForm.getSecondaryEmail().toLowerCase().contains("@jhancock") || actionForm.getSecondaryEmail().toLowerCase().contains("@manulife")) {
	                        if (!actionForm.getContractAccess(0).getTpaFirmIds().contains(new Integer(52801)) || actionForm.getContractAccess(0).getTpaFirmIds().size() > 1) {
	                            errorList.add(new GenericException(ErrorCodes.SECONDARY_EMAIL_MUST_BE_EXTERNAL));
	                        }
	                    }
	                } else {
	                int currentContractNumber = 0;
	                Contract currentContract = user.getCurrentContract();
	                if (currentContract != null) {
	                	currentContractNumber = currentContract.getContractNumber();
	                }
	                SecondaryEmailNotLocalRule emailNotLocalRule = new SecondaryEmailNotLocalRule(currentContractNumber);
	                emailNotLocalRule.validate(EditMyProfileForm.FIELD_SECONDARY_EMAIL, errorList, actionForm.getSecondaryEmail());
	                }
	            }
	            
	            if(actionForm.isPhoneNumberChanged()) {
		            PhoneRule.getInstance().validate(EditMyProfileForm.FIELD_TELEPHONE_NUMBER,	errorList, actionForm.getTelephoneNumber().getValue());
					if(StringUtils.isNotEmpty(actionForm.getTelephoneNumber().getValue()))
					{
						if(StringUtils.isEmpty(actionForm.getTelephoneNumber().getAreaCode()) || StringUtils.isEmpty(actionForm.getTelephoneNumber().getPhonePrefix())
									|| StringUtils.isEmpty(actionForm.getTelephoneNumber().getPhoneSuffix()) || actionForm.getTelephoneNumber().getValue().length() < 10)
						{
							errorList.add(new ValidationError(EditMyProfileForm.FIELD_TELEPHONE_NUMBER, ErrorCodes.PHONE_NOT_COMPLETE));
						}
						if(StringUtils.isNotEmpty(actionForm.getTelephoneNumber().getAreaCode()) && StringUtils.isNotEmpty(actionForm.getTelephoneNumber().getPhonePrefix()))
						{
							String areaCode = null,phonePrefix = null;
							areaCode = actionForm.getTelephoneNumber().getAreaCode();
							phonePrefix = actionForm.getTelephoneNumber().getPhonePrefix();
							if( areaCode.charAt(0) == '0' || areaCode.charAt(0) == '1' || phonePrefix.charAt(0) == '0' || phonePrefix.charAt(0) == '1')
							{
								errorList.add(new ValidationError(EditMyProfileForm.FIELD_TELEPHONE_NUMBER, ErrorCodes.PHONE_INVALID));
							}
						}
					}
	            }	
				
	            if(actionForm.isFaxNumberChanged()) {
					FaxRule.getInstance().validate(EditMyProfileForm.FIELD_FAX_NUMBER,	errorList, actionForm.getFaxNumber().getValue());
					if(StringUtils.isNotEmpty(actionForm.getFaxNumber().getValue()))
					{
						if(StringUtils.isEmpty(actionForm.getFaxNumber().getAreaCode()) || StringUtils.isEmpty(actionForm.getFaxNumber().getFaxPrefix())
									|| StringUtils.isEmpty(actionForm.getFaxNumber().getFaxSuffix()) || actionForm.getFaxNumber().getValue().length() < 10)
						{
							errorList.add(new ValidationError(EditMyProfileForm.FIELD_FAX_NUMBER, ErrorCodes.FAX_NOT_COMPLETE));
						}
						if(StringUtils.isNotEmpty(actionForm.getFaxNumber().getAreaCode()) && StringUtils.isNotEmpty(actionForm.getFaxNumber().getFaxPrefix()))
						{
							String areaCode = null,faxPrefix = null;
							areaCode = actionForm.getFaxNumber().getAreaCode();
							faxPrefix = actionForm.getFaxNumber().getFaxPrefix();
							if( areaCode.charAt(0) == '0' || areaCode.charAt(0) == '1' || faxPrefix.charAt(0) == '0' || faxPrefix.charAt(0) == '1')
							{
								errorList.add(new ValidationError(EditMyProfileForm.FIELD_FAX_NUMBER, ErrorCodes.FAX_INVALID));
							}
						}
					}
	            }	
				
	            if(actionForm.getTelephoneExtension() != null) {
					ExtensionRule.getInstance().validate(EditMyProfileForm.FIELD_EXTENSION_NUMBER,	errorList, actionForm.getTelephoneExtension());
					if( StringUtils.isEmpty(actionForm.getTelephoneNumber().getValue()) && StringUtils.isNotEmpty(actionForm.getTelephoneExtension()) )
					{
						errorList.add(new ValidationError(EditMyProfileForm.FIELD_EXTENSION_NUMBER, ErrorCodes.PH_NOTENTERED_EXT_ENTERED));
					}
	            }	
	            if (actionForm.isPasswordChanged() || actionForm.isConfirmNewPasswordChanged()) {
	                pair = new Pair(actionForm.getNewPassword(), actionForm.getConfirmNewPassword());

	                NewPasswordRule.getInstance().validate(new String[] { EditMyProfileForm.FIELD_NEW_PASSWORD, EditMyProfileForm.FIELD_VERIFY_PASSWORD }, errorList, pair);
	            }

	            // this section is for External users only
	            if (actionForm.isChallengeQuestionRequired()) {
	                if (actionForm.isChallengeQuestionChanged()) {
	                    QuestionRule.getInstance().validate(EditMyProfileForm.FIELD_CHALLENGE_QUESTION, errorList, actionForm.getChallengeQuestion());
	                }
	                if (actionForm.isChallengeAnswerChanged() || actionForm.isVerifyAnswerChanged()) {
	                    pair = new Pair(actionForm.getChallengeAnswer(), actionForm.getVerifyChallengeAnswer());
	                    AnswerConfirmRule.getInstance().validate(new String[] { EditMyProfileForm.FIELD_CHALLENGE_ANSWER, EditMyProfileForm.FIELD_VERIFY_ANSWER }, errorList,
	                            pair);
	                }
	            }
	            // end ExternalOnly sesction

	            // System must display an error if current password has not been entered.
	            if (!actionForm.isInformationUpdated()) {
	                errorList.add(new GenericException(ErrorCodes.SAVING_WITH_NO_CHANGES));
	            } else {
	                MandatoryRule rule = new MandatoryRule(ErrorCodes.CURRENT_PASSWORD_MANDATORY);
	                rule.validate(EditMyProfileForm.FIELD_CURRENT_PASSWORD, errorList, actionForm.getCurrentPassword());
	            }
	        }

	        if (logger.isDebugEnabled()) {
	            logger.debug("exit <-- doValidate");
	        }
		
	}

}
