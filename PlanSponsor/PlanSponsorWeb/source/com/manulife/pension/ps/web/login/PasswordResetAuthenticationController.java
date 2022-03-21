package com.manulife.pension.ps.web.login;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.passcode.DeviceTokenCookie;
import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.platform.web.util.SsnRule;
import com.manulife.pension.platform.web.validation.rules.ContractNumberRule;
import com.manulife.pension.platform.web.validation.rules.EmailRule;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.passcode.PasscodeSecurity;
import com.manulife.pension.service.security.passcode.RequestDetails;
import com.manulife.pension.service.security.valueobject.LoginPSValueObject;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.util.content.GenericException;

/**
 * @author Chris Shin
 * @version CS 1.0
 */
@Controller
@RequestMapping(value ="/login")
@SessionAttributes({"passwordResetAuthenticationForm"})

public class PasswordResetAuthenticationController extends AbstractPasswordResetController {
	
	@ModelAttribute("passwordResetAuthenticationForm") 
	public PasswordResetAuthenticationForm populateForm()
	{
		return new PasswordResetAuthenticationForm();
	}

	public static Map<String,String> forwards=new HashMap<String,String>();
	
	public static final String FORGOT_PASSWORD_STEP1 = "/login/forgotPasswordStep1.jsp";
	
	public static final String APP_NAME="PS";
	
	static{
		forwards.put("input",FORGOT_PASSWORD_STEP1);
		forwards.put("default",FORGOT_PASSWORD_STEP1);
		forwards.put("errors",FORGOT_PASSWORD_STEP1);
		forwards.put("continue","redirect:/do/login/passwordResetChallenge/");
		forwards.put("restart",FORGOT_PASSWORD_STEP1);
		forwards.put("login","/login/login.jsp");
	}
	
	@Autowired
    private PSValidatorFWDefault psValidatorFWDefault;

	/**
	 * Constructor.
	 */
	public PasswordResetAuthenticationController() {
		super(PasswordResetAuthenticationController.class);
	}

	protected int getStep() {
		return PasswordResetAuthenticationForm.STEP_PASSWORD_RESET_AUTHENTICATION;
	}
	
	/*
	 */
	protected String processFormValidatedData(PasswordResetAuthenticationForm form, 
			Collection errors, HttpServletRequest request,
			HttpServletResponse response,String defaultTarget) 
			throws SystemException {

		String target = defaultTarget;
		
		try {

			UserInfo userInput = getUserInfo(form);
			UserInfo user = delegate.getChallengeQuestion(userInput);
			if (null != user) {
			    delegate.isAllowedToChangePasswordViaForgotPassword(user.getProfileId(), APP_NAME );
			}
			populateFormInfo(user, form);
			if(!isUserExempted(user.getProfileId(), IPAddressUtils.getRemoteIpAddress(request))) {
				request.getSession().setAttribute(CommonConstants.CHALLENGE_PASSCODE_IND, true);
				createPsSession(user, request, response, target, errors);
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
	
	private void populateFormInfo(UserInfo selectedUser, 
			PasswordResetAuthenticationForm form) {

		form.setSavedChallengeQuestion(selectedUser.getChallengeQuestion());
		form.setUsername(selectedUser.getUserName());
		form.setConfirmPassword("");
		form.setNewPassword("");
		form.setProfileId(selectedUser.getProfileId());
		form.setFirstName(selectedUser.getFirstName());
		form.setLastName(selectedUser.getLastName());
		
		StringBuffer buf = new StringBuffer(selectedUser.getFirstName().trim());
		buf.append(" ");
		buf.append(selectedUser.getLastName().trim());
		form.setName(buf.toString());
	}
	
	protected void processDefault(PasswordResetAuthenticationForm form, HttpServletRequest request) 
	{
	
		if (form.getContractNumber() == null) {
			form.setButton("");
			form.setEmailAddress("");
			form.setSsn(new Ssn());
		}
	}
	
	@RequestMapping(value ="/passwordResetAuthentication/",method =  {RequestMethod.GET}) 
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
	@RequestMapping(value ="/passwordResetAuthentication/", params={"button=cancel"}, method =  {RequestMethod.POST}) 
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
	@RequestMapping(value ="/passwordResetAuthentication/", params={"button=continue"} , method =  {RequestMethod.POST}) 
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
		UserInfo userInput = getUserInfo(form);
		try {
			UserInfo user = delegate.getChallengeQuestion(userInput);
		} catch (SecurityServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			/*
			 * Contract Number is mandatory, numeric
			 */
			ContractNumberRule.getInstance().validate(PasswordResetAuthenticationForm.FIELD_CONTRACT_NUMBER,
				errors, actionForm.getContractNumber());
			
			/*
			 * Personal Identifier is mandatory, numeric
			 */
			SsnRule.getInstance().validate(PasswordResetAuthenticationForm.FIELD_SSN,
				errors, actionForm.getSsn().toString());			
				
			/*
			 * Email address is mandatory
			 */
			EmailRule.getInstance().validate(PasswordResetAuthenticationForm.FIELD_EMAIL,
				errors, actionForm.getEmailAddress());
		}
		
		if (!errors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, errors);
		}
		
		return errors;
	}

	 @InitBinder
	   protected void initBinder(HttpServletRequest request,
				ServletRequestDataBinder  binder) {
		  binder.addValidators(psValidatorFWDefault);
		
	 }
	 
	 private void createPsSession(UserInfo user , HttpServletRequest request
			 ,HttpServletResponse response, String target, Collection errors) throws SystemException {
			try {
				
				
			final String devicePrint = request.getParameter(CommonConstants.DEVICE_PRINT);
			String deviceToken= DeviceTokenCookie.readFromRequest(request);
			
			String location = Environment.getInstance().getSiteLocation();
			
			LoginPSValueObject loginPSValueObject = delegate.forgotPasswordPS(user.getUserName(), Environment.getInstance().getSiteLocation(),
	                new RequestDetails(request, devicePrint), deviceToken, false);
			
			PsPasscodeSession psPasscodeSession = new PsPasscodeSession(loginPSValueObject);
			
			request.getSession().setAttribute(CommonConstants.PASSCODE_SESSION_KEY, psPasscodeSession);
			request.getSession().setAttribute(PsAuthenticatedSessionInitialization.USERID_KEY, user.getUserName());
			
			//this condition is for stopping the user from further navigation when the passcode is locked  
			if (loginPSValueObject.getUserPasscodeDetailedInfo().isPasscodeLocked()) {
				if("usa".equalsIgnoreCase(location)) {
					errors.add(new GenericException(CommonErrorCodes.PASSCODE_LOCKED_USA));
				}else {
					errors.add(new GenericException(CommonErrorCodes.PASSCODE_LOCKED_NY));
				}
			}
			} catch (SecurityServiceException e) {
				target = handleErrors(e,errors);
				request.setAttribute(Environment.getInstance().getErrorKey(),errors);
			}

		}
}
	 