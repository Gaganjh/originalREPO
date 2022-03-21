package com.manulife.pension.bd.web.password;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.bd.web.validation.rules.AccessCodeRule;
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
import com.manulife.pension.service.security.bd.exception.BDChangeTempPasswordFailedException;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.role.BDInternalUser;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.util.Pair;
import com.manulife.pension.validator.ValidationError;


@Controller
@RequestMapping(value ="/changeTempPassword")

public class ChangeTempPasswordController extends BaseAutoController {
	
	@ModelAttribute("changeTempPasswordForm") 
	public ChangeTempPasswordForm populateForm() 
	{
		return new ChangeTempPasswordForm();
		}
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	private static String FORWARD_DEFAULT = "default";
	private static String FORWARD_CANCEL = "cancel";
	private static String FORWARD_SUCCESS = "success";
	private static String FORWARD_CONTINUE = "continue"; 
	private static String FORWARD_INPUT = "input"; 
	private static String FORWARD_FAIL = "fail"; 
	private boolean isFRWExternalUser = Boolean.FALSE;
	private static String IS_FRW_EXTERNAL_USER = "isFRWExternalUser";
	static{
		forwards.put(FORWARD_INPUT,"/password/changeTempPassword.jsp");
		forwards.put(FORWARD_FAIL,"/password/changeTempPassword.jsp");
		forwards.put(FORWARD_DEFAULT,"/password/changeTempPassword.jsp");		
		forwards.put(FORWARD_CANCEL,"redirect:/do/home/");
		forwards.put(FORWARD_SUCCESS,"/password/changeTempPassword.jsp");
		forwards.put(FORWARD_CONTINUE,"/do/postLogin");
		}

	
		
	private final MandatoryRule pwdMandatoryRule = new MandatoryRule(
			CommonErrorCodes.PASSWORD_MANDATORY);
	private final MandatoryRule confirmedPwdMandatoryRule = new MandatoryRule(
			CommonErrorCodes.CONFIRM_PASSWORD_MANDATORY);

	private final AccessCodeRule accessCodeRule = AccessCodeRule.getInstance();

	public ChangeTempPasswordController() {
		super(ChangeTempPasswordController.class);
	}

	@RequestMapping(value = "/", method = { RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("changePasswordForm") ChangeTempPasswordForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(actionForm, request, response);
		//Check the user is External / Internal
		if(!actionForm.isInternal()){
			isFRWExternalUser =Boolean.TRUE;
		}else {
			isFRWExternalUser =Boolean.FALSE;
		}
		request.getSession(false).setAttribute(IS_FRW_EXTERNAL_USER, isFRWExternalUser);

		if(forward!=null && !forward.equals("")){
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); }
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(FORWARD_FAIL);
			}
		}
		return forwards.get(FORWARD_DEFAULT);
	}

	@RequestMapping(value = "/", params = { "action=change" }, method = { RequestMethod.POST})
	public String doChange(@Valid @ModelAttribute("changePasswordForm") ChangeTempPasswordForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(actionForm, request, response);
		if(forward!=null && !forward.equals("")){
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); }
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(FORWARD_FAIL);
			}
		}
	
		Collection error = doValidate(actionForm, request);
		if(!error.isEmpty() || (null!= request.getSession(false).getAttribute("Deapi") && request.getSession(false).getAttribute("Deapi").equals("down"))){
			setErrorsInRequest(request, error);
			return forwards.get(FORWARD_INPUT);
		}
		
		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);

		try {
			BDUserSecurityServiceDelegate.getInstance().changeTempPassword(userProfile.getBDPrincipal(),
					actionForm.getPassword(), actionForm.getAccessCode(), IPAddressUtils.getRemoteIpAddress(request),request.getSession(false).getId());
			request.getSession(false).removeAttribute("changePasswordFlag");
		} catch (SecurityServiceException e) {
			if (e instanceof BDChangeTempPasswordFailedException) {
				actionForm.setDisabled(true);
			}
			List<ValidationError> errors = new ArrayList<ValidationError>(1);
			errors.add(new ValidationError("", SecurityServiceExceptionHelper.getErrorCode(e)));
			setErrorsInRequest(request, errors);
			logger.debug("Failed to change temporary password", e);
			return forwards.get(FORWARD_INPUT);
		}
		userProfile.setPasswordStatus(BDUserPasswordStatus.Active);
		request.setAttribute("success", Boolean.TRUE);
		return forwards.get(FORWARD_SUCCESS);
	}
	@RequestMapping(value = "/", params= {"action=cancel"},method =  {RequestMethod.POST}) 
	public String doCancel(@Valid @ModelAttribute("changePasswordForm") ChangeTempPasswordForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(actionForm, request, response);
		if(forward!=null && !forward.equals("")){
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); }
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(FORWARD_FAIL);
			}
		}
			// clear the user profile
		ApplicationHelper.setUserProfile(request, null);
		return forwards.get(FORWARD_CANCEL);
	}

	@RequestMapping(value = "/", params = { "action=continue" }, method = { RequestMethod.POST })
	public String doContinue(@Valid @ModelAttribute("changePasswordForm") ChangeTempPasswordForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(actionForm, request, response);
		if(forward!=null && !forward.equals("")){
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); }
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(FORWARD_FAIL);
			}
		}
		
		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
		if (userProfile.getPasswordStatus().compareTo(BDUserPasswordStatus.Active) == 0) {
			return forwards.get(FORWARD_CONTINUE);
		} else {
			return forwards.get(FORWARD_DEFAULT);
		}
	}

	protected Collection doValidate( ActionForm form, HttpServletRequest request) {

		Collection errors = super.doValidate( form, request);
		ChangeTempPasswordForm actionForm = (ChangeTempPasswordForm) form;
		if (!StringUtils.equals(ChangeTempPasswordForm.ACTION_CHANGE,
				actionForm.getAction())) {
			return errors;
		}
		if (!actionForm.isInternal()) {
			accessCodeRule.validate(
					ChangeTempPasswordForm.FIELD_ACCESS_CODE, errors,
					actionForm.getAccessCode());
		}
		pwdMandatoryRule.validate(
				ChangeTempPasswordForm.FIELD_NEW_PASSWORD, errors,
				actionForm.getPassword());
		confirmedPwdMandatoryRule.validate(
				ChangeTempPasswordForm.FIELD_NEW_CONFIRMED_PASSWORD,
				errors, actionForm.getConfirmedPassword());
		// Changes for defect 8589 , 8590
		
        if (StringUtils.isNotEmpty(actionForm.getPassword())
                && StringUtils.isNotEmpty(actionForm.getConfirmedPassword())) {
            Pair pair = new Pair(actionForm.getPassword(), actionForm.getConfirmedPassword());

            // New password mandatory and standards must be met
            NewPasswordRule.getInstance().validate(ChangeTempPasswordForm.FIELD_NEW_PASSWORD,
                    errors, pair);
            
            if (null != errors && !errors.isEmpty()) {
            	// This condition is used to override the error code if logged in user is FRW External User
            	if(isFRWExternalUser){
					for (int index = 0; index < errors.size(); index++) {
						if (((ArrayList<ValidationError>) errors).get(index).getErrorCode() == CommonErrorCodes.PASSWORD_FAILS_STANDARDS) {
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
            
			if (!isFRWExternalUser) {
				SecurityServiceDelegate serviceInstance = null;
				String responseText = null;
				serviceInstance = SecurityServiceDelegate.getInstance();
				try {
					responseText = serviceInstance.passwordStrengthValidation(actionForm.getPassword(), userName,
							isFRWExternalUser);
					getPasswordScore(responseText, errors, request);
				} catch (Exception e) {
					if (logger.isDebugEnabled()) {
						logger.debug("exception occured while calling " + "passwordStrengthValidation service call"
								+ e.getMessage());
						logger.info(e);
					}
				}
			} else {
				/*
				 *  This method is to validate only for FRW External users to check following validations.
				 *  1. Any consecutive and sequential characters present in the password
				 *  2. Check user name and password Sequence
				 */
				PasswordMeterUtility.validateSeqConsCharForExUsers(userName, actionForm.getPassword(), errors);
			}
            // End changes for defects 8589 , 8590
        }
		return errors;
	}
	
	@Autowired
	   private BDValidatorFWFail  bdValidatorFWFail;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWFail);
	}
	
	@Override
	protected String preExecute(
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			SystemException {
		ChangeTempPasswordForm form = (ChangeTempPasswordForm) actionForm;
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
	public String doDeApiAjaxCall(@Valid @ModelAttribute("changeTempPasswordForm") ChangeTempPasswordForm form, BindingResult bindingResult,
           HttpServletRequest request, HttpServletResponse response) throws ServletException, SystemException,SecurityServiceException, IOException{
		BDUserProfile userProfileDetails = BDSessionHelper.getUserProfile(request);
        //Password Meter Validation DeApi Call and passing Boolean value as True for only FRW External Users to validate
      	PasswordMeterUtility.validatePasswordMeter(request,response,userProfileDetails.getBDPrincipal().getUserName().toString(),isFRWExternalUser);
		return null;
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
			errors.add(new ValidationError
					(ChangeTempPasswordForm.FIELD_NEW_PASSWORD ,CommonErrorCodes.PASSWORD_FAILS_STANDARDS));
		}
		
	}
	
	}
}
