package com.manulife.pension.bd.web.password;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.delegate.BDUserSecurityServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.platform.web.util.PasswordMeterUtility;
import com.manulife.pension.platform.web.validation.rules.NewPasswordRule;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.service.security.bd.BDUserPasswordStatus;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.role.BDInternalUser;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.util.Pair;
import com.manulife.pension.validator.ValidationError;


@Controller
@RequestMapping(value ="/updatePassword")

public class UpdatePasswordController extends BaseAutoController {
	private static final Logger generalLog = Logger.getLogger(UpdatePasswordController.class);
	private final String PASSWORD_UPDATE_START_TIME = "passwordUpdateStartTime";
	@ModelAttribute("updatePasswordForm") 
	public UpdatePasswordForm populateForm() 
	{
		return new UpdatePasswordForm();
		}
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	private static String FORWARD_DEFAULT = "default";
	private static String FORWARD_SUCCESS = "success";
	private static String FORWARD_CONTINUE = "continue"; 
	private static String FORWARD_INPUT = "input"; 
	private static String FORWARD_FAIL = "fail"; 
	private static String IS_UPDATE_PWD_SUCC_IND = "isUpdatePasswordSuccessInd";
	private static String UPDATE_PASSWORD_PAGE = "/password/updatePassword.jsp";
	private static String FALSE = "false";
	private boolean isFRWExternalUser = Boolean.FALSE;
	private static String IS_FRW_EXTERNAL_USER = "isFRWExternalUser";
	
	static{
		forwards.put(FORWARD_INPUT,UPDATE_PASSWORD_PAGE);
		forwards.put(FORWARD_DEFAULT,UPDATE_PASSWORD_PAGE);		
		forwards.put(FORWARD_SUCCESS,UPDATE_PASSWORD_PAGE);
		forwards.put(FORWARD_CONTINUE,"redirect:/do/home/");
		forwards.put(FORWARD_FAIL,UPDATE_PASSWORD_PAGE);
		}

	
		
	private final MandatoryRule pwdMandatoryRule = new MandatoryRule(
			CommonErrorCodes.PASSWORD_MANDATORY);
	private final MandatoryRule confirmedPwdMandatoryRule = new MandatoryRule(
			CommonErrorCodes.CONFIRM_PASSWORD_MANDATORY);


	public UpdatePasswordController() {
		super(UpdatePasswordController.class);
	}

	@RequestMapping(value = "/", method = { RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("updatePasswordForm") UpdatePasswordForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		//while refresh removing the deapi error from the screen
		request.getSession(false).setAttribute("Deapi", "up");
		String forward = preExecute(actionForm, request, response);
		//Check the user is Internal / External
		if(!actionForm.isInternal()){
			isFRWExternalUser =Boolean.TRUE;
		}else {
			isFRWExternalUser =Boolean.FALSE;
		}
		request.getSession(false).setAttribute(IS_FRW_EXTERNAL_USER, isFRWExternalUser);
		
		if(forward!=null && !"".equals(forward)){
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); }
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(FORWARD_FAIL);
			}
		}
		request.getSession(false).setAttribute("isUpdatePasswordBackClick", "true");
		request.getSession(false).setAttribute(IS_UPDATE_PWD_SUCC_IND,FALSE );
		return forwards.get(FORWARD_DEFAULT);
	}

	@RequestMapping(value = "/", params = { "action=change" }, method = { RequestMethod.POST})
	public String doChange(@Valid @ModelAttribute("updatePasswordForm") UpdatePasswordForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(actionForm, request, response);
		
		if(forward!=null && !"".equals(forward)){
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); }
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(FORWARD_FAIL);
			}
		}
		request.getSession(false).setAttribute(IS_UPDATE_PWD_SUCC_IND, FALSE);
		Collection error = doValidate(actionForm, request);
		if(!error.isEmpty() || (null!= request.getSession(false).getAttribute("Deapi") && request.getSession(false).getAttribute("Deapi").equals("down"))){
			setErrorsInRequest(request, error);
			return forwards.get(FORWARD_INPUT);
		}
		
		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);

		try {
			BDUserSecurityServiceDelegate.getInstance().updatePassword(userProfile.getBDPrincipal(),
					actionForm.getPassword(), IPAddressUtils.getRemoteIpAddress(request),
					request.getSession(false).getId());
			
			if (null != request.getSession().getAttribute(PASSWORD_UPDATE_START_TIME)) {
				Date startTime = (Date) request.getSession().getAttribute(PASSWORD_UPDATE_START_TIME);
				Long timeToUpdatePassword = new Date().getTime() - startTime.getTime();
				generalLog.error("Time taken to Update Password for " + "Username:" + userProfile.getBDPrincipal().getUserName()
						+ " is:" + timeToUpdatePassword + "ms");
				request.getSession().setAttribute(PASSWORD_UPDATE_START_TIME, null);
			}
		} catch (SecurityServiceException e) {
			List<ValidationError> errors = new ArrayList<ValidationError>(1);
			errors.add(new ValidationError("", SecurityServiceExceptionHelper.getErrorCode(e)));
			setErrorsInRequest(request, errors);
			logger.debug("Failed to update password", e);
			
				return forwards.get(FORWARD_INPUT);
		}
		request.getSession(false).setAttribute(IS_UPDATE_PWD_SUCC_IND, "true");
		userProfile.setPasswordStatus(BDUserPasswordStatus.Active);
		request.setAttribute("success", Boolean.TRUE);
		
		return forwards.get(FORWARD_SUCCESS);
	}
	
	@RequestMapping(value = "/", params = { "action=continue" }, method = { RequestMethod.POST })
	public String doContinue(@Valid @ModelAttribute("updatePasswordForm") UpdatePasswordForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(actionForm, request, response);
		if(forward!=null && !"".equals(forward)){
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); }
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(FORWARD_FAIL);
			}
		}
		
		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
		request.getSession(false).setAttribute("isUpdatePasswordBackClick", FALSE);
		if (userProfile.getPasswordStatus().compareTo(BDUserPasswordStatus.Active) == 0) {
			return forwards.get(FORWARD_CONTINUE);
		} else {
			return forwards.get(FORWARD_DEFAULT);
		}
	}

	protected Collection doValidate( ActionForm form, HttpServletRequest request) {

		Collection errors = super.doValidate( form, request);
		UpdatePasswordForm actionForm = (UpdatePasswordForm) form;
		if (!StringUtils.equals(UpdatePasswordForm.ACTION_CHANGE,
				actionForm.getAction())) {
			return errors;
		}
		pwdMandatoryRule.validate(
				UpdatePasswordForm.FIELD_NEW_PASSWORD, errors,
				actionForm.getPassword());
		confirmedPwdMandatoryRule.validate(
				UpdatePasswordForm.FIELD_NEW_CONFIRMED_PASSWORD,
				errors, actionForm.getConfirmedPassword());
        if (StringUtils.isNotEmpty(actionForm.getPassword())
                && StringUtils.isNotEmpty(actionForm.getConfirmedPassword())) {
            Pair pair = new Pair(actionForm.getPassword(), actionForm.getConfirmedPassword());

            // New password mandatory and standards must be met
            NewPasswordRule.getInstance().validate(UpdatePasswordForm.FIELD_NEW_PASSWORD,
                    errors, pair);
            
            if (null != errors && !errors.isEmpty()) {
            	// This condition is used to override the error code if logged in user is FRW External User
				if (isFRWExternalUser) {
					for (int index = 0; index < errors.size(); index++) {
						if (((ArrayList<ValidationError>) errors).get(index).getErrorCode() == 2011) {
							errors.remove(((ArrayList<ValidationError>) errors).get(index));
							errors.add(new ValidationError("",
									CommonErrorCodes.FRW_EXTERNAL_USER_PASSWORD_FAILS_STANDARDS));
						}
					}
				}
            	return errors;
            } 

            BDUserProfile userProfileDetails = BDSessionHelper.getUserProfile(request);
			String userName = StringUtils.EMPTY;
			if (null != userProfileDetails && null != userProfileDetails.getBDPrincipal()) {
				userName = userProfileDetails.getBDPrincipal().getUserName();
			}
            
            // Changes for defect 8589, 8590
			if (!isFRWExternalUser) {
				SecurityServiceDelegate serviceInstance = null;
				String responseText = null;
				serviceInstance = SecurityServiceDelegate.getInstance();
				try {
					responseText = serviceInstance.passwordStrengthValidation(actionForm.getPassword(), userName,
							isFRWExternalUser);
					getPasswordScore(responseText, errors, request);
				} catch (Exception e) {
					logger.debug("Failed to update password", e);
				}
			} else {
				/*
				 *  This method is to validate only for FRW External users to check following validations.
				 *  1. Any consecutive and sequential characters present in the password
				 *  2. Check user name and password Sequence
				 */
				PasswordMeterUtility.validateSeqConsCharForExUsers(userName, actionForm.getPassword(), errors);
			}
			// End changes for defect 8589, 8590
        }
		return errors;
	}
	
	
	@Override
	protected String preExecute(
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			SystemException {
		UpdatePasswordForm form = (UpdatePasswordForm) actionForm;
		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);		

		if (userProfile.getBDPrincipal().getBDUserRole() instanceof BDInternalUser) {
			form.setInternal(true);
		} else {
			form.setInternal(false);
		}
		return super.preExecute( form, request, response);
	}
	
	
	@RequestMapping(value ="/ajaxvalidator/" ,method =  {RequestMethod.POST})
	@ResponseBody
	public String doDeApiAjaxCall(@Valid @ModelAttribute("updatePasswordForm") UpdatePasswordForm form, BindingResult bindingResult,
           HttpServletRequest request, HttpServletResponse response) throws ServletException, SystemException,SecurityServiceException, IOException{
		if (null == request.getSession().getAttribute(PASSWORD_UPDATE_START_TIME)) {
			request.getSession().setAttribute(PASSWORD_UPDATE_START_TIME, new Date());
		}
		BDUserProfile userProfileDetails = BDSessionHelper.getUserProfile(request);
        //Password Meter Validation DeApi Call and passing Boolean value as False for only FRW External Users to validate
        PasswordMeterUtility.validatePasswordMeter(request,response,userProfileDetails.getBDPrincipal().getUserName().toString(),isFRWExternalUser);
		return null;
		}
	
	@Autowired
	   private BDValidatorFWFail  bdValidatorFWFail;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWFail);
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
		} else{
		if (null != jsonObject && null != jsonObject.get("score")) {
			score = jsonObject.get("score").getAsInt();
		}
		request.getSession(false).setAttribute("Deapi", "up");
		if (score < passwordScore) {
			errors.add(new ValidationError(UpdatePasswordForm.FIELD_NEW_PASSWORD,CommonErrorCodes.PASSWORD_FAILS_STANDARDS));
		}
		
	}
	
	}
	
	
}
