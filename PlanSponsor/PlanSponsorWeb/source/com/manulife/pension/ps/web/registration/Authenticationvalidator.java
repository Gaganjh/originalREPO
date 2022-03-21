package com.manulife.pension.ps.web.registration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.util.SsnRule;
import com.manulife.pension.platform.web.validation.rules.ContractNumberRule;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.validation.pentest.PsValidator1;
import com.manulife.pension.ps.web.validation.rules.PinRule;
import com.manulife.pension.ps.web.validation.rules.TpaFirmRule;
import com.manulife.pension.util.Pair;
import com.manulife.pension.util.content.GenericException;

@Component
public class Authenticationvalidator implements Validator {
	
	protected final static String ACTION_CONTINUE = "continue";
	private static Logger logger = Logger.getLogger(Authenticationvalidator.class);
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
		if (PsValidator1.getInstance().validateSanitizeCatalogedFormFields(form, error, request) == false) {
			//
			// Only Internal filed validation failure on registration page results in a
			// detailed message, in other cases generic error message is shown.
			//
			boolean nonGuiError = false;
			for (Object e : error) {
				if (e instanceof GenericException) {
					//
					// Anonymous penetration error, to be reported later.
					//				
					if (((GenericException) e).getErrorCode() != CommonErrorCodes.ERROR_PSVALIDATION_WITHOUT_GUI_FIELD_NAME) {
						error.clear();
						if (new UrlPathHelper().getPathWithinApplication(request).equals("/registration/tpaauthentication/")) {
							request.getSession().setAttribute("error", String.valueOf(ErrorCodes.TPA_FIRM_ID_INVALID));
						}
						else {
							request.getSession().setAttribute("error", String.valueOf(CommonErrorCodes.USER_DOES_NOT_EXIST));
						}
						form.setAuthenticateValid(false);
						nonGuiError = true;
						break;
					}
				}
			}
			//
			// Error to be reported right after validation.
			//
			if (nonGuiError == false) {
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
	    	
	    	form.setAuthenticateValid(false);
		   	
		   	if(form.getTpa()) {
				// Contract Number mandatory and standards must be met
				TpaFirmRule.getInstance().validate(
					RegisterForm.FIELD_FIRM_ID,
					error, 
					new Pair(null, form.getFirmId())
				);
		   	}
			else {
				// Contract Number mandatory and standards must be met
				ContractNumberRule.getInstance().validate(
					RegisterForm.FIELD_CONTRACT_NUMBER,
					error, 
					form.getContractNumber()
				);
			}

			// SSN Number mandatory and standards must be met
			SsnRule.getInstance().validate(
				RegisterForm.FIELD_SSN,
				error, 
				form.getSsn()
			);

			// PIN mandatory and standards must be met
			PinRule.getInstance().validate(
					form.FIELD_PIN,
				error, 
				form.getPin()
			);

		    if(error.isEmpty())
		    {
		    	form.setAuthenticateValid(true);
		    	 if(isExistingSiteUser(form.getPin()))
		    	 {
		    	 	request.getSession(false).setAttribute(com.manulife.pension.ps.web.Constants.EXISTING_SITE_USER,new Boolean(true));
		    	 }
		    }else{
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
	    }
	    }
		if(logger.isDebugEnabled()) {
		    logger.debug("exit <- doValidate");
	    }
	}
	private boolean isExistingSiteUser(String pin)
	{
		boolean existingUser = false;
		try 
		{
			Integer.parseInt(pin);
			existingUser = true;
		} 
		catch (NumberFormatException e)
		{
			existingUser = false;
		}
		return existingUser;
	}
}