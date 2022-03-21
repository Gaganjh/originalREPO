package com.manulife.pension.ps.web.password;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.util.PasswordMeterUtility;
import com.manulife.pension.platform.web.validation.rules.NewPasswordRule;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.utility.SecurityConstants;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.util.Pair;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;
/**
 * @author Chris Shin
 * @version CS 1.0
 */
@Controller
@SessionAttributes({"updatePasswordForm"})
@RequestMapping(value = "/password")
public class UpdatePasswordController extends PsController {
	private static final Logger generalLog = Logger.getLogger(UpdatePasswordController.class);
	private final String PASSWORD_UPDATE_START_TIME = "passwordUpdateStartTime";
	@ModelAttribute("updatePasswordForm")
	public UpdatePasswordForm populateForm()
	{
		return new UpdatePasswordForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	
	private static final String DEFAULT = "default";
    private static final String CONTINUE = "continue";
	private static final String SIGNOUT = "signout";
    private static final String CANCEL = "cancel";
    private static final String LOGIN_FLOW = "loginFlow";
    private static final String UPDATECONTINUE= "updatecontinue";
    // US 44837
    private static final String UPDATEINPUT = "updateInput";
    private static final String UPDATEDEFAULT = "defaultInput";
    private static final String UPDATE_PASSWORD_PAGE = "/password/updatePassword.jsp";
    // End US 44837
    private UserInfo userInfo;
    
    
	static{
		forwards.put(UPDATEINPUT,UPDATE_PASSWORD_PAGE);
		forwards.put(UPDATECONTINUE,"redirect:/do/password/updatePasswordConfirmation/");
		forwards.put(CANCEL,"redirect:/do/home/homePageFinder/");
		forwards.put(DEFAULT,UPDATE_PASSWORD_PAGE);
		forwards.put(SIGNOUT,"redirect:/do/home/Signout/");
		forwards.put(UPDATEDEFAULT,UPDATE_PASSWORD_PAGE);
		
		
		
		}

	
	/**
	 * Constructor.
	 */
	public UpdatePasswordController() {
		super(UpdatePasswordController.class);
	}
	
	
	/*
	 */
	
	
	
	
	private void populateFormUpdate(UserInfo userInfo, UpdatePasswordForm form) {

		StringBuilder buf = new StringBuilder();
		buf.append(userInfo.getFirstName());
		buf.append(" ");
		buf.append(userInfo.getLastName());
		form.setUserFullName(buf.toString());
		form.setEmailAddress(userInfo.getEmail());
		
	}
	/*
	 */
	@RequestMapping(value ="/updatePassword/", method =  {RequestMethod.GET,RequestMethod.POST}) 
	public String doExecuteUpdate(@Valid @ModelAttribute("updatePasswordForm") UpdatePasswordForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(UPDATEINPUT);
			}
		}
		UserProfile userProfileId = getUserProfile(request); 
		userInfo = SecurityServiceDelegate.getInstance().getUserInfo(
				userProfileId.getPrincipal());
		// changes for US44837
		Collection errors = doValidateUpdate(form,request);
		

		if (!errors.isEmpty()|| (null!= request.getSession(false).getAttribute("Deapi") && request.getSession(false).getAttribute("Deapi").equals("down"))) {
			  SessionHelper.setErrorsInSession(request, errors);
		} 
		return forwards.get(UPDATEINPUT);
    	
	}
	

	@RequestMapping(value ="/updatePassword/",params = { "button=continue" }, method =  {RequestMethod.GET,RequestMethod.POST}) 
	public String doContinue(@Valid @ModelAttribute("updatePasswordForm") UpdatePasswordForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(UPDATEINPUT);
			}
		}

		// changes for US44837
		UserProfile userProfile = getUserProfile(request); 

		userInfo = SecurityServiceDelegate.getInstance().getUserInfo(
				userProfile.getPrincipal());

		Collection errors = doValidateUpdate(form,request);


		if (!errors.isEmpty() || (null!= request.getSession(false).getAttribute("Deapi") && request.getSession(false).getAttribute("Deapi").equals("down"))) {
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(UPDATEINPUT);
		} 


		if(StringUtils.isNotEmpty(request.getParameter(LOGIN_FLOW))){
			form.setLoginFlow(Boolean.TRUE);
		}
		populateFormUpdate(userInfo, form);

		userInfo.setNewPassword(form.getNewPassword());

		SecurityServiceDelegate delegate = SecurityServiceDelegate.getInstance();
		
		String forward = "";
		
		String userflow = "UpdatePassword";

		try {

			if (form.getButton() != null && form.getButton().equals(CONTINUE)) {
				delegate.updatePassword(getUserProfile(request).getPrincipal(), userInfo,
						IPAddressUtils.getRemoteIpAddress(request), Environment.getInstance().getSiteLocation(),
						request.getSession(false).getId());

				if (SessionHelper.getBusinessParamFlag(request, userInfo))
					delegate.updateDBTransactionPassword(getUserProfile(request).getPrincipal(), userInfo, userflow,
							IPAddressUtils.getRemoteIpAddress(request), request.getSession(false).getId());

				if (null != request.getSession().getAttribute(PASSWORD_UPDATE_START_TIME)) {
					Date startTime = (Date) request.getSession().getAttribute(PASSWORD_UPDATE_START_TIME);
					Long timeToUpdatePassword = new Date().getTime() - startTime.getTime();
					generalLog.error("Time taken to Update Password for " + "Username:"
							+ userInfo.getUserName() + " is:" + timeToUpdatePassword + "ms");
					request.getSession().setAttribute(PASSWORD_UPDATE_START_TIME, null);
				}

			}
			UserProfile user = getUserProfile(request);
			user.setPasswordStatus(SecurityConstants.ACTIVE_PASSWORD_STATUS);
			request.getSession(false).setAttribute(Constants.USERPROFILE_KEY, user);

		} catch (SecurityServiceException e) {
			errors.add(new GenericException(Integer.parseInt(e.getErrorCode())));

			forward = forwards.get(SIGNOUT);

		}catch(RemoteException e){
			throw new SystemException(e, "com.manulife.pension.ps.web.password.UpdatePasswordController", "doExecute", "Exception not handled: " + e.toString());
		}

		if (errors == null || errors.size() == 0) {
			forward = forwards.get(UPDATECONTINUE);

		} else {

			SessionHelper.setErrorsInSession(request, errors);
			form.setButton(null);
		}
		return forward;
	}

	
	
		
	 
	  protected Collection doValidateUpdate( ActionForm form,HttpServletRequest request) {
			

			Collection errors = new ArrayList();
			UpdatePasswordForm actionForm = (UpdatePasswordForm) form;


			if (actionForm.getButton() != null
					&& (actionForm.getButton().equals(CONTINUE))) {

			
			// new password mandatory 
			if (actionForm.getNewPassword().length() == 0) {
				errors.add(new ValidationError(UpdatePasswordForm.FIELD_NEW_PASSWORD ,ErrorCodes.PASSWORD_MANDATORY));
			}

				// confirm new password mandatory 
				if (actionForm.getConfirmPassword().length() == 0) {
					errors.add(new ValidationError(UpdatePasswordForm.FIELD_CONFIRM_PASSWORD ,ErrorCodes.CONFIRM_PASSWORD_MANDATORY));
				}

				
				
				Pair pair = new Pair(actionForm.getNewPassword(), actionForm
						.getConfirmPassword());
				
				
				if (actionForm.getNewPassword().length() > 0) {
					
					NewPasswordRule
							.getInstance()
							.validate(
									new String[] {
											UpdatePasswordForm.FIELD_NEW_PASSWORD,
											UpdatePasswordForm.FIELD_CONFIRM_PASSWORD },
									errors, pair);
					// Changes for defect 8589 , 8590
					
					if(null != errors && errors.isEmpty()) {
						SecurityServiceDelegate serviceInstance = null;
			            String responseText = null;

					    serviceInstance = SecurityServiceDelegate.getInstance();
	                try{
	                	responseText = serviceInstance.passwordStrengthValidation(actionForm.getNewPassword(),
	                			userInfo.getUserName(),Boolean.FALSE); //Passing Boolean value as False for only FRW External Users to validate
	                	getPasswordScore(responseText,errors,request);
	                }catch(Exception e){
	                	if (logger.isDebugEnabled()) {
	                        logger.debug("exception occured while calling "
	                        		+ "passwordStrengthValidation service call" +e.getMessage());
	                    }
	                }
					}
	                // end changes for defect 8589 , 8590

					
				}
				
				
			}

			return errors;
		}
	  
		@RequestMapping(value ="/updatePassword/ajaxvalidator/" ,method =  {RequestMethod.POST})
		@ResponseBody
		public String doDeApiAjaxCall(@Valid @ModelAttribute("updatePasswordForm") UpdatePasswordForm form, BindingResult bindingResult,
	           HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SystemException,SecurityServiceException, IOException{
		if(null == request.getSession().getAttribute(PASSWORD_UPDATE_START_TIME)){
		request.getSession().setAttribute(PASSWORD_UPDATE_START_TIME, new Date());
		}
		//Passing Boolean value as False for only FRW External Users to validate
		PasswordMeterUtility.validatePasswordMeter(request, response, userInfo.getUserName().toString(),Boolean.FALSE); 
		return null;
		}
		
		/**
		 * Validate the input action form.
		 *  
		 */
		@Autowired
		private PSValidatorFWDefault psValidatorFWDefault;

		@InitBinder
		public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
			binder.bind(request);
			binder.addValidators(psValidatorFWDefault);

		}
		
	private void getPasswordScore(String responseText, Collection errors, HttpServletRequest request) {
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
				errors.add(new ValidationError(UpdatePasswordForm.FIELD_NEW_PASSWORD,
						CommonErrorCodes.PASSWORD_FAILS_STANDARDS));
			}
		}
	}

}