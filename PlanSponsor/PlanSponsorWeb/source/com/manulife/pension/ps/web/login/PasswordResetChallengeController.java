package com.manulife.pension.ps.web.login;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.passcode.PasscodeSecurity;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.validator.ValidationError;

/**
 * @author Chris Shin
 * @version CS 1.0
 */
@Controller
@RequestMapping(value ="/login")
@SessionAttributes({"passwordResetAuthenticationForm"})

public class PasswordResetChallengeController extends AbstractPasswordResetController {

	@ModelAttribute("passwordResetAuthenticationForm") 
	public PasswordResetAuthenticationForm populateForm()
	{
		return new PasswordResetAuthenticationForm();
		}
	
	public static Map<String,String> forwards = new HashMap<String,String>();
	public static final String FORGOT_PASSWORD_STEP2 = "/login/forgotPasswordStep2.jsp";
	static{ 
		forwards.put("input",FORGOT_PASSWORD_STEP2);
		forwards.put("default",FORGOT_PASSWORD_STEP2);
		forwards.put("errors",FORGOT_PASSWORD_STEP2); 
		forwards.put("challenge","redirect:/do/login/forgotPasswordPasscodeTransition/");
		forwards.put("continue","redirect:/do/login/passwordReset/");
		forwards.put("restart","redirect:/do/login/passwordResetAuthentication/"); 
		forwards.put("login","/login/login.jsp ");
		}

	@Autowired
    private PSValidatorFWDefault psValidatorFWDefault;
	
	/**
	 * Constructor.
	 */
	public PasswordResetChallengeController() {
		super(PasswordResetChallengeController.class);
	}

	protected int getStep() {
		return PasswordResetAuthenticationForm.STEP_PASSWORD_CHALLENGE;
	}
	
	/*
	 */
	protected String processFormValidatedData(PasswordResetAuthenticationForm form, 
			Collection errors,  HttpServletRequest request,
			HttpServletResponse response, String defaultTarget) 
			throws SystemException {
		HttpSession session = request.getSession(false);
		String target = defaultTarget;
		
		try {
			UserInfo user = getUserInfo(form);
			user.setIpAddress(IPAddressUtils.getRemoteIpAddress(request));
			delegate.validateChallengeAnswer(user);
			
			 if(isUserExempted(user.getProfileId(), IPAddressUtils.getRemoteIpAddress(request))) {
				 target = defaultTarget;
			 }else {
				 //setting session expiry time for passcode challenge in forgot password page
				 session.setMaxInactiveInterval(
	                        Environment.getInstance().getIntNamingVariable(
	                                      "passcode.inactiveSessionTimeout",
	                                      null,
	                                      300));
				 target = "challenge";
			 }

		} catch (SecurityServiceException e) {
			target = handleErrors(e,errors);
			request.setAttribute(Environment.getInstance().getErrorKey(),errors);
		}

		return target;
	}

	private boolean isUserExempted(final long userProfile, final String ipAddress) {
			return PasscodeSecurity.PS.isExemptUserProfile(userProfile, ipAddress);
	}
	
	protected void processDefault(PasswordResetAuthenticationForm form,
		 HttpServletRequest request) {
	
		//do something		
	}
	
	@RequestMapping(value ="/passwordResetChallenge/",method =  {RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute ("passwordResetAuthenticationForm") PasswordResetAuthenticationForm form, BindingResult bindingResult,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SystemException{
		
		if(bindingResult.hasErrors()){
            String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
            if(errDirect!=null){
            request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
            return forwards.get(errDirect);
            }
       }
		Collection validationErrors = doValidate(form, request);
		if(!validationErrors.isEmpty()){
			return forwards.get("default");
		}
		String forward = super.doExecute(form, request, response);
		return forwards.get(forward);
	}

	/**
	 * Validate the input action form. 
	 * 
	 */
	
	protected Collection doValidate(ActionForm form, HttpServletRequest request) {

		Collection errors = super.doValidate(form, request);
		PasswordResetAuthenticationForm actionForm = (PasswordResetAuthenticationForm) form;

		if (actionForm.getButton() != null && !actionForm.getButton().equals(PasswordResetAuthenticationForm.CANCEL))
		{
			// challenge answer mandatory 
			if (actionForm.getChallengeAnswer().length() == 0) {
				errors.add(new ValidationError(PasswordResetAuthenticationForm.FIELD_CHALLENGE_ANSWER ,ErrorCodes.ANSWER_MANDATORY));
			}
		}

		if (!errors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, errors);
		}
		
		return errors;
	}
	
	@RequestMapping(value ="/passwordResetChallenge/", params={"button=cancel"}, method =  {RequestMethod.POST}) 
	public String doCancel(@Valid @ModelAttribute ("passwordResetAuthenticationForm") PasswordResetAuthenticationForm form, BindingResult bindingResult,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SystemException{
		
		if(bindingResult.hasErrors()){
            String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
            if(errDirect!=null){
            request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
            return forwards.get(errDirect);
            }
       }
		Collection validationErrors = doValidate(form, request);
		if(!validationErrors.isEmpty()){
			return forwards.get("default");
		}
		String forward = super.doExecute(form, request, response);
		return forwards.get(forward);
	}
	@RequestMapping(value ="/passwordResetChallenge/", params={"button=continue"} , method =  {RequestMethod.POST}) 
	public String doSave(@Valid @ModelAttribute ("passwordResetAuthenticationForm") PasswordResetAuthenticationForm form, BindingResult bindingResult,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SystemException{
		
		if(bindingResult.hasErrors()){
            String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
            if(errDirect!=null){
            request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
            return forwards.get(errDirect);
            }
       }
		Collection validationErrors = doValidate(form, request);
		if(!validationErrors.isEmpty()){
			return forwards.get("default");
		}
		String forward = super.doExecute(form, request, response);
		return forwards.get(forward);
	}

	
	
	 @InitBinder
	   protected void initBinder(HttpServletRequest request,
				ServletRequestDataBinder  binder) {
		  binder.addValidators(psValidatorFWDefault);
		
	 }
	
}