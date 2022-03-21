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

import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.validation.pentest.PsValidator1;
import com.manulife.pension.util.content.GenericException;
@Component
public class ConfirmValidator implements Validator {

	
	
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

	   
		if (PsValidator1.getInstance().validateSanitizeCatalogedFormFields(form, error, request) == false) {
			form.setAuthenticateValid(false);
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

		if(logger.isDebugEnabled()) {
		    logger.debug("exit <- doValidate");
	    }
	
	
	
	}
}
