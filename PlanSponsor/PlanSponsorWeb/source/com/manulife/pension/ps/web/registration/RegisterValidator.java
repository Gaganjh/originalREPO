package com.manulife.pension.ps.web.registration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.util.ExtensionRule;
import com.manulife.pension.platform.web.util.FaxRule;
import com.manulife.pension.platform.web.util.MobileRule;
import com.manulife.pension.platform.web.util.PhoneRule;
import com.manulife.pension.platform.web.validation.rules.EmailRule;
import com.manulife.pension.platform.web.validation.rules.NewPasswordRule;
import com.manulife.pension.platform.web.validation.rules.UserNameRule;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.validation.pentest.PsValidator1;
import com.manulife.pension.ps.web.validation.rules.AnswerRule;
import com.manulife.pension.ps.web.validation.rules.QuestionRule;
import com.manulife.pension.util.Pair;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;
@Component
public class RegisterValidator implements Validator {
	protected final static String ACTION_CONTINUE = "continue";

	private static Logger logger = Logger.getLogger(RegisterValidator.class);
	@Override
	public boolean supports(Class<?> arg0) {
		return RegisterForm.class.equals(arg0);
	}

	@Override
	public void validate(Object target, Errors errors) {
		RegisterForm form = (RegisterForm) target;
		Collection error = new ArrayList<Object>();
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		BindingResult bindingResult = (BindingResult)errors;
		String[] errorCodes = new String[10];
		
	    if(logger.isDebugEnabled()) {
		    logger.debug("entry -> doValidate");
	    }
		
	   		
		if (PsValidator1.getInstance().validateSanitizeCatalogedFormFields(form,  error, request) == false) {
			form.setRegisterValid(false);
			if(!error.isEmpty()){
			for (Object e : error) {
				if (e instanceof GenericException) {
					GenericException errorEx=(GenericException) e;
					errorCodes = new String[]{Integer.toString(errorEx.getErrorCode())};
					bindingResult.addError(new ObjectError(errors
							                 .getObjectName(),errorCodes , errorEx.getParams(), errorEx.getMessage()));
					
				}
				
			}
			if(request.getSession().getAttribute(CommonConstants.ERROR_RDRCT) == null){
		    	request.getSession().setAttribute(CommonConstants.ERROR_RDRCT, CommonConstants.ERROR_PAGE);
		    }
			
				
				form.setAuthenticateValid(false);
				request.getSession().removeAttribute(PsBaseUtil.ERROR_KEY);
				request.removeAttribute(PsBaseUtil.ERROR_KEY);
				request.getSession().setAttribute(PsBaseUtil.ERROR_KEY, bindingResult);
				request.setAttribute(PsBaseUtil.ERROR_KEY, bindingResult);
		}
			
			if(logger.isDebugEnabled()) {
			    logger.debug("exit <- doValidate");
		    }
			return;
		}
		
	    if(logger.isDebugEnabled()) {
	    	logger.debug(form.toString());
		    Enumeration enumParams = request.getParameterNames();
		    while(enumParams.hasMoreElements()) {
		    	String key = (String)enumParams.nextElement();
		    	String value = request.getParameter(key);
		    	logger.debug("Request Param " + key + " = " + value);
		    }
	    }

	    
	    // only validate for the continue action
	    if(form.getAction()!=null && form.getAction().equalsIgnoreCase(ACTION_CONTINUE)){
	    	
	    	form.setRegisterValid(false);
			// Email mandatory and standards must be met
			EmailRule.getInstance().validate(
				RegisterForm.FIELD_EMAIL,
				error, 
				form.getEmail()
			);
			
			PhoneRule.getInstance().validate(RegisterForm.FIELD_PHONE_NO, error, form.getPhone().getValue());
			if(StringUtils.isNotEmpty(form.getPhone().getValue()))
			{
				if(StringUtils.isEmpty(form.getPhone().getAreaCode()) || StringUtils.isEmpty(form.getPhone().getPhonePrefix())
							|| StringUtils.isEmpty(form.getPhone().getPhoneSuffix()) || form.getPhone().getValue().length() < 10)
				{
					error.add(new ValidationError(RegisterForm.FIELD_PHONE_NO, ErrorCodes.PHONE_NOT_COMPLETE));
				}
				if(StringUtils.isNotEmpty(form.getPhone().getAreaCode()) && StringUtils.isNotEmpty(form.getPhone().getPhonePrefix()))
				{
					String areaCode = null,phonePrefix = null;
					areaCode = form.getPhone().getAreaCode();
					phonePrefix = form.getPhone().getPhonePrefix();
					if( areaCode.charAt(0) == '0' || areaCode.charAt(0) == '1' || phonePrefix.charAt(0) == '0' || phonePrefix.charAt(0) == '1')
					{
						error.add(new ValidationError(RegisterForm.FIELD_PHONE_NO, ErrorCodes.PHONE_INVALID));
					}
				}
			}
			
			ExtensionRule.getInstance().validate(RegisterForm.FIELD_EXTENSION_NO,	error, form.getExt());
			if( StringUtils.isEmpty(form.getPhone().getValue()) && StringUtils.isNotEmpty(form.getExt()) )
			{
				error.add(new ValidationError(RegisterForm.FIELD_EXTENSION_NO, ErrorCodes.PH_NOTENTERED_EXT_ENTERED));
			}
					
			if(form.getTpa())
			{
				
				FaxRule.getInstance().validate(RegisterForm.FIELD_FAX_NO,	error, form.getFax().getValueTPA());
				if(StringUtils.isNotEmpty(form.getFax().getValueTPA()))
    			{
    				if(StringUtils.isEmpty(form.getFax().getAreaCode()) || StringUtils.isEmpty(form.getFax().getFaxPrefix())
    							|| StringUtils.isEmpty(form.getFax().getFaxSuffix()) || form.getFax().getValue().length() < 10)
    				{
    					error.add(new ValidationError(RegisterForm.FIELD_FAX_NO, ErrorCodes.FAX_NOT_COMPLETE));
    				}
    				if(StringUtils.isNotEmpty(form.getFax().getAreaCode()) && StringUtils.isNotEmpty(form.getFax().getFaxPrefix()))
    				{
    					String areaCode = null,faxPrefix = null;
    					areaCode = form.getFax().getAreaCode();
    					faxPrefix = form.getFax().getFaxPrefix();
    					if( areaCode.charAt(0) == '0' || areaCode.charAt(0) == '1' || faxPrefix.charAt(0) == '0' || faxPrefix.charAt(0) == '1')
    					{
    						error.add(new ValidationError(RegisterForm.FIELD_FAX_NO, ErrorCodes.FAX_INVALID));
    					}
    				}
    			}
				
			}
			
			if(StringUtils.isNotEmpty(form.getMobile().getValue()))
			{
				MobileRule.getInstance().validate(RegisterForm.FIELD_MOBILE_NO, error, form.getMobile().getValue());
				if(StringUtils.isEmpty(form.getMobile().getAreaCode()) || StringUtils.isEmpty(form.getMobile().getPhonePrefix())
							|| StringUtils.isEmpty(form.getMobile().getPhoneSuffix()) || form.getMobile().getValue().length() < 10)
				{
					error.add(new ValidationError(RegisterForm.FIELD_MOBILE_NO, ErrorCodes.MOBILE_NOT_COMPLETE));
				}
				if(StringUtils.isNotEmpty(form.getMobile().getAreaCode()) && StringUtils.isNotEmpty(form.getMobile().getPhonePrefix()))
				{
					String areaCode = null,phonePrefix = null;
					areaCode = form.getMobile().getAreaCode();
					phonePrefix = form.getMobile().getPhonePrefix();
					if( areaCode.charAt(0) == '0' || areaCode.charAt(0) == '1')
					{
						error.add(new ValidationError(RegisterForm.FIELD_MOBILE_NO, ErrorCodes.MOBILE_INVALID));
					}
				}
			}
			
			//Validate Passcode Channel Decision
			error.addAll(validatePasscodeSelection(form));
			
			// User Name mandatory and standards must be met
			UserNameRule.getInstance().validate(
				RegisterForm.FIELD_USER_NAME,
				error, 
				form.getUserName()
			);

			// Password mandatory and standards must be met
			// Confirm Password mandatory and standards must be met
			Pair pair = new Pair(form.getPassword(), form.getConfirmPassword());
			
			// changes for US 44837
			
			NewPasswordRule.getInstance().validate(
				RegisterForm.FIELD_PASSWORD,
				error, 
				pair
			);
			
			// changes for defect 8589, 8590
			
			if(null != form.getPassword() && null!= form.getConfirmPassword()){
				 if(null != error && error.isEmpty()) {
					 SecurityServiceDelegate serviceInstance = null;
			         String responseText = null;
				     serviceInstance = SecurityServiceDelegate.getInstance();
                try{
                	//Passing Boolean value as False for only FRW External Users to validate	
                	responseText = serviceInstance.passwordStrengthValidation(form.getPassword(),
								form.getUserName(),Boolean.FALSE); 
						getPasswordScore(responseText,error,request);
                }catch(Exception e){
                	if (logger.isDebugEnabled()) {
                        logger.debug("exception occured while calling "
                        		+ "passwordStrengthValidation service call" +e.getMessage());
                        logger.info(e);
                    }
                 }
				 }
				
			}
			// end changes for defect 8589 , 8590
			// End changes for US 44837

			if(!form.getTpa()) {
				// Question mandatory and standards must be met
				QuestionRule.getInstance().validate(
					RegisterForm.FIELD_CHALLENGE_QUESTION,
					error, 
					form.getChallengeQuestion()
				);
				
				if(form.getChallengeQuestion() != null && !form.getChallengeQuestion().trim().equals(""))
				// Answer mandatory and standards must be met
				// Confirm Answer mandatory and standards must be met
				pair = new Pair(form.getAnswer().trim().toLowerCase(), form.getConfirmAnswer().trim().toLowerCase());
				AnswerRule.getInstance().validate(
					RegisterForm.FIELD_ANSWER,
					error,
					pair
				);
			}

		    if(error.isEmpty()) 
		    {
		    	form.setRegisterValid(true);
		    }
		    else
		    {
		    	 for (Object e : error) {
						if (e instanceof GenericException) {
							GenericException errorEx=(GenericException) e;
							errorCodes = new String[]{Integer.toString(errorEx.getErrorCode())};
							bindingResult.addError(new ObjectError(errors
									                 .getObjectName(),errorCodes , errorEx.getParams(), errorEx.getMessage()));
						}
					}
				    if(request.getSession().getAttribute(CommonConstants.ERROR_RDRCT) == null){
				    	request.getSession().setAttribute(CommonConstants.ERROR_RDRCT, CommonConstants.ERROR_PAGE);
				    }
				    request.getSession().removeAttribute(PsBaseUtil.ERROR_KEY);
					request.removeAttribute(PsBaseUtil.ERROR_KEY);
					request.getSession().setAttribute(PsBaseUtil.ERROR_KEY, bindingResult);
					request.setAttribute(PsBaseUtil.ERROR_KEY, bindingResult);
					request.getSession(false).setAttribute(PsBaseUtil.ERROR_KEY, error);
		    }
	    }
  
		
		if(logger.isDebugEnabled()) {
		    logger.debug("exit <- doValidate");
	    }
	}
	
	private List<ValidationError> validatePasscodeSelection(RegisterForm form){
		List<ValidationError> errorList = new ArrayList<>(10);
		if(form.getPasscodeDeliveryPreference() == null){
			errorList.add(new ValidationError(RegisterForm.FIELD_PASSCODE_DELIVERY_PREF, ErrorCodes.NO_PHONE_NUMBER_OR_MOBILE_NUMBER_HAS_BEEN_PROVIDED));
			return errorList;
		}
		switch (form.getPasscodeDeliveryPreference()) {
		case SMS: //user has no mobile number but has chosen text via mobile
			if(StringUtils.isEmpty(form.getMobile().getValue())){
				errorList.add(new ValidationError(RegisterForm.FIELD_PASSCODE_DELIVERY_PREF, ErrorCodes.MOBILE_MISSING_FOR_TEXT));
			}
			break;
		case VOICE_TO_MOBILE: //user has no mobile number but has choosen voice message via mobile
			if(StringUtils.isEmpty(form.getMobile().getValue())){
				errorList.add(new ValidationError(RegisterForm.FIELD_PASSCODE_DELIVERY_PREF, ErrorCodes.MOBILE_MISSING_FOR_VOICE_MESSAGE));
			}
			break;
		case VOICE_TO_PHONE: //user has selected auto voice message to telephone number and number has extension
			if(StringUtils.isEmpty(form.getPhone().getValue())){
				errorList.add(new ValidationError(RegisterForm.FIELD_PASSCODE_DELIVERY_PREF, ErrorCodes.PHONE_MISSING_FOR_VOICE_MESSAGE));
			}
			if(StringUtils.isNotEmpty(form.getExt())){
				errorList.add(new ValidationError(RegisterForm.FIELD_PASSCODE_DELIVERY_PREF, ErrorCodes.PHONE_NUMBER_HAS_EXTENSION));
			}
			break;

		default:
			break;
		}
		return errorList;
	}
	
	private void getPasswordScore(String responseText, Collection error, HttpServletRequest request) {
		int score = 0;
		String deApiStatus = null;
		JsonElement jsonElement = null;
		final int passwordScore = 2;
		if (null != responseText) {
			jsonElement = new JsonParser().parse(responseText);
		}
		JsonObject jsonObject = null;
		if (null != jsonElement) {
			jsonObject = jsonElement.getAsJsonObject();
		}
		if (null != jsonObject && null != jsonObject.get("Deapi")) {
			deApiStatus = jsonObject.get("Deapi").getAsString();
		}

		if (null != deApiStatus && !deApiStatus.isEmpty() && deApiStatus.equalsIgnoreCase("down")) {
			request.getSession(false).setAttribute("Deapi", "down");
		} else {
			if (null != jsonObject && null != jsonObject.get("score")) {
				score = jsonObject.get("score").getAsInt();
			}
			request.getSession(false).setAttribute("Deapi", "up");
			if (score < passwordScore) {
				error.add(new ValidationError(RegisterForm.FIELD_PASSWORD,
						CommonErrorCodes.PASSWORD_FAILS_STANDARDS));
			}
		}
	}
}
