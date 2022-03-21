package com.manulife.pension.ps.web.login;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.utility.util.exception.GenericException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.util.PasswordMeterUtility;
import com.manulife.pension.platform.web.validation.rules.NewPasswordRule;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.registration.RegisterForm;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.util.Pair;
import com.manulife.pension.validator.ValidationError;

/**
 * @author Chris Shin
 * @version CS 1.0
 */
@Controller
@RequestMapping(value ="/login")
@SessionAttributes({"passwordResetAuthenticationForm"})

public class PasswordResetLoginController extends AbstractPasswordResetController {
	@ModelAttribute("passwordResetAuthenticationForm") 
	public PasswordResetAuthenticationForm populateForm()
	{
		return new PasswordResetAuthenticationForm();
		}
	
	public static Map<String,String> forwards = new HashMap<String,String>();
	public static final String FORGOT_PASSWORD_STEP3 = "/login/forgotPasswordStep3.jsp";
	static{ 
		forwards.put("input",FORGOT_PASSWORD_STEP3);
		forwards.put("default",FORGOT_PASSWORD_STEP3);
		forwards.put("errors",FORGOT_PASSWORD_STEP3); 
		forwards.put("continue","redirect:/do/login/passwordResetConfirmation/");
		forwards.put("restart","redirect:/do/login/passwordResetAuthentication/"); 
		forwards.put("login","/login/login.jsp ");
		forwards.put("challenge","redirect:/do/login/forgotPasswordPasscodeTransition/");
		}

	@Autowired
    private PSValidatorFWDefault psValidatorFWDefault;
	/**
	 * Constructor.
	 */
	public PasswordResetLoginController() {
		super(PasswordResetLoginController.class);
	}

	protected int getStep() {
		return PasswordResetAuthenticationForm.STEP_PASSWORD_RESET;
	}
	
	/*
	 */
	protected String processFormValidatedData(PasswordResetAuthenticationForm form, 
			Collection errors,HttpServletRequest request,
			HttpServletResponse response, String defaultTarget) 
			throws SystemException {

		String target = defaultTarget;
		
		UserInfo userInfo = getUserInfo(form);
		userInfo.setIpAddress(IPAddressUtils.getRemoteIpAddress(request));
		try {
			// changes for defect 8541
			if(!delegate.isAllowedToChangePasswordViaForgotPassword(userInfo.getProfileId(), "PS")){
			delegate.changePasswordThroughForgotPassword(userInfo, Environment.getInstance()
					.getSiteLocation());
			}
			// end changes for 8541
			String userflow = "ForgetPassword";
            
			HttpSession session = request.getSession(false);
		    if(null == session){
				session = request.getSession();
		    }
		   delegate.updateDBTransactionPassword(new UserProfile().getPrincipal(),userInfo,userflow,
		        		 IPAddressUtils.getRemoteIpAddress(request),session.getId());
			
		} catch (SecurityServiceException e ) {
			target = handleErrors(e,errors);
			request.setAttribute(Environment.getInstance().getErrorKey(),errors);
		}catch(RemoteException e){
			throw new SystemException(e, "com.manulife.pension.ps.web.login.PasswordResetLoginController", "processFormValidatedData", "Exception not handled: " + e.toString());
		}
		return target;
	}

	protected void processDefault(PasswordResetAuthenticationForm form,
		HttpServletRequest request) {
		
		request.getSession().setAttribute(PasswordResetAuthenticationForm.DISPALY_USER_NAME, form.isStepValid(PasswordResetAuthenticationForm.STEP_PASSWORD_CHALLENGE));
		//is there anything to do here?	Doesn't appear to be.
	}
	
	@RequestMapping(value ="/passwordReset/",method =  {RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute ("passwordResetAuthenticationForm") PasswordResetAuthenticationForm form, BindingResult bindingResult,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SystemException{
		if(bindingResult.hasErrors()){
            String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
            if(errDirect!=null){
            request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
            return forwards.get(errDirect);
            }
       }
		
		HttpSession session = request.getSession(false);
		
		Collection validationErrors = new ArrayList<GenericException>();
		
		
		//if(!businessIndicator){
		   validationErrors = doValidate(form, request);
		//}
		
		// End changes for US 44837 PSW
		
		
		if(!validationErrors.isEmpty()){
			return forwards.get("default");
		}
		String forward = super.doExecute(form, request, response);
		
		if ("default".equalsIgnoreCase(forward)
				&& session != null && session.getAttribute(Constants.CHALLENGE_PASSCODE_IND) != null ) {
			boolean challengeUserInd = (boolean) session.getAttribute(Constants.CHALLENGE_PASSCODE_IND);
			
			if(challengeUserInd) {
	    		return forwards.get("challenge");
	    	}	
		}
		
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
			Pair pair = new Pair(actionForm.getNewPassword(), actionForm.getConfirmPassword());

			// New password mandatory and standards must be met
			NewPasswordRule.getInstance().validate(PasswordResetAuthenticationForm.FIELD_NEW_PASSWORD,
				errors, pair);
			
			
            if(null != errors && errors.isEmpty()) {
            	SecurityServiceDelegate serviceInstance = null;
                String responseText = null;
                serviceInstance = SecurityServiceDelegate.getInstance();
            try{
            	responseText = serviceInstance.passwordStrengthValidation(actionForm.getNewPassword(),
            			actionForm.getUsername(), Boolean.FALSE);
            	getPasswordScore(responseText,errors,request);
            }catch(Exception e){
            	if (logger.isDebugEnabled()) {
                    logger.debug("exception occured while calling "
                    		+ "passwordStrengthValidation service call" +e.getMessage());
                    logger.info(e);
                }
            }
            }
		}
		if (null!=errors && !errors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, errors);
		}
		
		return errors;
	}
	@RequestMapping(value ="/passwordReset/", params={"button=cancel"}, method =  {RequestMethod.POST}) 
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
	@RequestMapping(value ="/passwordReset/", params={"button=continue"} , method =  {RequestMethod.POST}) 
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
		if(!validationErrors.isEmpty() || (null!= request.getSession(false).getAttribute("Deapi") && request.getSession(false).getAttribute("Deapi").equals("down"))){
			return forwards.get("default");
		}
		String forward = super.doExecute(form, request, response);
		return forwards.get(forward);
		
	}
	
	@RequestMapping(value ="/passwordReset/ajaxvalidator/" ,method =  {RequestMethod.POST})
	@ResponseBody
	public String doDeApiAjaxCall(@Valid @ModelAttribute ("passwordResetAuthenticationForm") PasswordResetAuthenticationForm form, BindingResult bindingResult,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SystemException,SecurityServiceException, IOException{
		
      //Password Meter Validation DeApi Call
	  //Passing Boolean value as False for only FRW External Users to validate
      	PasswordMeterUtility.validatePasswordMeter(request,response,form.getUsername().toString(),Boolean.FALSE); 
		return null;
	}


	 @InitBinder
	   protected void initBinder(HttpServletRequest request,
				ServletRequestDataBinder  binder) {
		  binder.addValidators(psValidatorFWDefault);
	 }
	 
	 private void getPasswordScore(String responseText, Collection errors,HttpServletRequest request) {
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
					errors.add(new ValidationError
	    					(RegisterForm.FIELD_PASSWORD ,CommonErrorCodes.PASSWORD_FAILS_STANDARDS));
				}
			}
		}
}