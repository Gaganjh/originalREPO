package com.manulife.pension.bd.web.password;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.process.BDPublicWizardProcessController;
import com.manulife.pension.bd.web.process.BDWizardProcessContext;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.delegate.BDUserSecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.platform.web.util.PasswordMeterUtility;
import com.manulife.pension.platform.web.validation.rules.NewPasswordRule;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.service.security.bd.exception.BDUserNoEmailAddressException;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.util.Pair;
import com.manulife.pension.validator.ValidationError;

/**
 * Last step of forget password. Change the password
 * 
 * @author guweigu
 *
 */
@Controller
@RequestMapping( value = "/forgetPassword")

public class ForgetPasswordStep3Controller extends BDPublicWizardProcessController {
	
	@ModelAttribute("forgetPasswordForm") 
	public ForgetPasswordForm populateForm() 
	{
		return new ForgetPasswordForm();
		}
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	private static ControllerRedirect HomeRedirect1 = new ControllerRedirect(URLConstants.HomeURL);
	private static final String HomeRedirect=HomeRedirect1.getPath();
	static{
		forwards.put("input","/password/forgetPasswordStep3.jsp");
		forwards.put("fail","/password/forgetPasswordStep3.jsp");
		}

	private static Map<String, Integer> localSecurityServiceExceptionMapping = new HashMap<String, Integer>(
			3);
	static {
		localSecurityServiceExceptionMapping.put(
				BDUserNoEmailAddressException.class.getName(),
				BDErrorCodes.FORGET_PWD_PROFILE_NO_EMAIL_ADDRESS);
	}
    
    private final MandatoryRule pwdMandatoryRule = new MandatoryRule(
            CommonErrorCodes.PASSWORD_MANDATORY);

    private final MandatoryRule confirmedPwdMandatoryRule = new MandatoryRule(
            CommonErrorCodes.CONFIRM_PASSWORD_MANDATORY);


	public ForgetPasswordStep3Controller() {
		super(ForgetPasswordStep3Controller.class, ForgetPasswordContext.class,
				ForgetPasswordContext.ProcessName, ForgetPasswordContext.Step3);
	}

	@Override
	protected ProcessState doContinue(
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {

		List<ValidationError> errors = new ArrayList<ValidationError>(1);
		ForgetPasswordForm form = (ForgetPasswordForm) actionForm;
		
        // New password mandatory and standards must be met
        pwdMandatoryRule.validate(ChangeTempPasswordForm.FIELD_NEW_PASSWORD, errors, form
                .getNewPassword());
        // Confirm Password mandatory and standards must be met
       
        
        confirmedPwdMandatoryRule.validate(
                ChangeTempPasswordForm.FIELD_NEW_CONFIRMED_PASSWORD, errors, form
                        .getConfirmedPassword());
        if (StringUtils.isNotEmpty(form.getNewPassword())
                && StringUtils.isNotEmpty(form.getConfirmedPassword())) {
            Pair<String, String> passwordPair = new Pair<String, String>(form.getNewPassword(),
                    form.getConfirmedPassword());
            
            NewPasswordRule.getInstance().validate(ChangeTempPasswordForm.FIELD_NEW_PASSWORD,
                    errors, passwordPair);
            
			if (errors.isEmpty()) {
				String userName = (String) request.getSession(false).getAttribute("userId");
				/*
				 *  This method is to validate only for FRW External users to check following validations.
				 *  1. Any consecutive and sequential characters present in the password
				 *  2. Check user name and password Sequence
				 */
				PasswordMeterUtility.validateSeqConsCharForExUsers(userName, form.getNewPassword(), errors); 
			} else {
				// This condition is used to override the error code if logged in user is FRW External User
				for (int index = 0; index < errors.size(); index++) {
    				if (errors.get(index).getErrorCode() == CommonErrorCodes.PASSWORD_FAILS_STANDARDS){
    				errors.remove(errors.get(index));
    				errors.add(new ValidationError("", CommonErrorCodes.FRW_EXTERNAL_USER_PASSWORD_FAILS_STANDARDS));
    			}
    		}
			}
        }
        String ipAddress = IPAddressUtils.getRemoteIpAddress(request);
		ProcessState nextState = getState();
		 
		if (errors.isEmpty()) {
			// change the password
			try {
				BDUserSecurityServiceDelegate
						.getInstance()
						.changeForgotPassword(
								((ForgetPasswordContext) getProcessContext(request))
										.getSecurityProfile(),
								form.getNewPassword(), ipAddress,  request.getSession(false).getId());
				nextState = nextState
						.getNext(BDWizardProcessContext.ACTION_CONTINUE);
			} catch (SecurityServiceException e) {
				Integer errorCode = SecurityServiceExceptionHelper
						.getErrorCodeForCause(e, localSecurityServiceExceptionMapping);
				errors.add(new ValidationError("", errorCode));
			}
		}
		if (!errors.isEmpty()) {
			setErrorsInSession(request, errors);
		}
		return nextState;
	}
	@RequestMapping(value = "/step3", method = { RequestMethod.GET })
	protected String doInput(@Valid @ModelAttribute("forgetPasswordForm") ForgetPasswordForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, IOException, ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doInput");
		}
		
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("fail");// if input forward not //available, provided default
			}
		}
		
		if (BDSessionHelper.getUserProfile(request) != null) {
			return HomeRedirect;
		}
		
		String forward = super.doExecuteInput(request);

		if (forward == null) {
			String forward1 = super.doInput(form, request, response);
			return StringUtils.contains(forward1, '/') ? forward1 : forwards.get(forward1);
		}
		return forward;
	}
	@RequestMapping(value = "/step3", params = { "action=continue" }, method = { RequestMethod.POST })
	protected String doContinue(@Valid @ModelAttribute("forgetPasswordForm") ForgetPasswordForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, IOException, ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doInput");
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("fail");// if input forward not //available, provided default
			}
		}
		if (BDSessionHelper.getUserProfile(request) != null) {
			return HomeRedirect;
		}
		String forward = super.doCancelContinue(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value = "/step3", params = { "action=cancel" }, method = { RequestMethod.POST })
	protected String doCancel(@Valid @ModelAttribute("forgetPasswordForm") ForgetPasswordForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, IOException, ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCancel");
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("fail");// if input forward not //available, provided default
			}
		}
		if (BDSessionHelper.getUserProfile(request) != null) {
			return HomeRedirect;
		}
		String forward = super.doCancelContinue(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	/**
	 * Validate form and request against penetration attack, prior to other validations.
	 */
	@Autowired
	   private BDValidatorFWFail  bdValidatorFWFail;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWFail);
	}
	
	@RequestMapping(value ="/step3/ajaxvalidator/" ,method =  {RequestMethod.POST})
	@ResponseBody
	public String doDeApiAjaxCall(@Valid @ModelAttribute("forgetPasswordForm") ForgetPasswordForm form, BindingResult bindingResult,
           HttpServletRequest request, HttpServletResponse response) throws ServletException, SystemException,SecurityServiceException, IOException{
		
		String userName = (String) request.getSession(false).getAttribute("userId");
		// Passing Boolean value as True for only FRW External Users to validate
      	PasswordMeterUtility.validatePasswordMeter(request,response,userName,Boolean.TRUE);
		return null;
		}
}
