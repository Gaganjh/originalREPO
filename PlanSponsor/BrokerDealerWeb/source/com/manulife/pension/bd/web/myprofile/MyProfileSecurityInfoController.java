package com.manulife.pension.bd.web.myprofile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
import com.manulife.pension.bd.web.BdBaseController;
import com.manulife.pension.bd.web.registration.util.PasswordChallengeInput;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.bd.web.validation.rules.PasswordChallengeInputRule;
import com.manulife.pension.delegate.BDUserSecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.platform.web.util.PasswordMeterUtility;
import com.manulife.pension.platform.web.validation.rules.NewPasswordRule;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.service.security.bd.valueobject.PasswordChallenge;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.util.Pair;
import com.manulife.pension.validator.ValidationError;

/**
 * Action for Security tab for my profile
 * 
 * @author guweigu
 *
 */
@Controller
@RequestMapping(value = "/myprofile")

public class MyProfileSecurityInfoController extends BaseAutoController {
	
	@ModelAttribute("myprofileSecurityInfoForm")
	public MyProfileSecurityInfoForm populateForm() {
		return new MyProfileSecurityInfoForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/myprofile/securityInfo.jsp");
		forwards.put("cancel", "redirect:/do/home/");
		forwards.put("fail", "/myprofile/securityInfo.jsp");
		forwards.put("myprofileDispatch", "redirect:/do/myprofileDispatch/");
	}

	private final MandatoryRule currentPwdMandatory = new MandatoryRule(BDErrorCodes.CURRENT_PWD_MANDATORY);

	private final MandatoryRule pwdMandatoryRule = new MandatoryRule(CommonErrorCodes.EMPTY_PASSWORD);
	
	private final MandatoryRule confirmedPwdMandatoryRule = new MandatoryRule(
			CommonErrorCodes.CONFIRM_PASSWORD_MANDATORY);

	public MyProfileSecurityInfoController() {
		super(MyProfileSecurityInfoController.class);
	}

	@RequestMapping(value = "/security", method = { RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("myprofileSecurityInfoForm") MyProfileSecurityInfoForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("input");// if input forward not
												// //available, provided default
			}
		}
		//if user is bookmarked the URL, we still need to challenge.
    	if(Objects.nonNull(request.getSession().getAttribute(CommonConstants.CHALLENGE_PASSCODE_IND))) {
    		if((boolean)request.getSession().getAttribute(CommonConstants.CHALLENGE_PASSCODE_IND)) {
    			request.getSession().setAttribute("myProfileCurrentTab", MyProfileNavigation.SecurityInfoTabId);
    			return forwards.get("myprofileDispatch");
    		}
    	}
		prepareRendering(form, request, response);
		return forwards.get("input");
	}

	@RequestMapping(value = "/security", params = { "action=save" }, method = { RequestMethod.POST })
	public String doSave(@Valid @ModelAttribute("myprofileSecurityInfoForm") MyProfileSecurityInfoForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("input");
			}
		}
		BDUserProfile profile = BDSessionHelper.getUserProfile(request);

		List<ValidationError> errors = new ArrayList<ValidationError>();
		
        boolean change = validate(form, errors, request);

        String userName = StringUtils.EMPTY;
		if (null != profile && null != profile.getBDPrincipal()) {
			userName = profile.getBDPrincipal().getUserName();
		}
		/*
		 *  This method is to validate only for FRW External users to check following validations.
		 *  1. Any consecutive and sequential characters present in the password
		 *  2. Check user name and password Sequence
		*/
		if (errors.isEmpty()) {
			PasswordMeterUtility.validateSeqConsCharForExUsers(userName, form.getNewPassword(), errors);
		} else{
			// This condition is used to override the error code if logged in user is FRW External User
			for (int index = 0; index < errors.size(); index++) {
				if (errors.get(index).getErrorCode() == CommonErrorCodes.PASSWORD_FAILS_STANDARDS){
				errors.remove(errors.get(index));
				errors.add(new ValidationError("", CommonErrorCodes.FRW_EXTERNAL_USER_PASSWORD_FAILS_STANDARDS));
			}
		}
		}
		try {
			if (change && errors.size() == 0) {
				BDUserSecurityServiceDelegate.getInstance().updateSecurityInfo(profile.getBDPrincipal(),
						form.getCurrentPassword(), form.getNewPassword(), getChallenages(form),IPAddressUtils.getRemoteIpAddress(request),request.getSession(false).getId());
				form.setSuccess(true);
				
			}

		} catch (SecurityServiceException e) {
			errors.add(new ValidationError("", SecurityServiceExceptionHelper.getErrorCode(e,
					MyProfileUtil.MyProfileSecurityServiceExceptionMapping)));
		}
		if (!errors.isEmpty()) {
			form.setChanged(true);
			request.getSession().removeAttribute(BdBaseController.ERROR_KEY);
			request.getSession().setAttribute(BdBaseController.ERROR_KEY, errors);
		} else {
			form.clear();
		}

		prepareRendering(form, request, response);
		return forwards.get("input");
	}

	@RequestMapping(value = "/security", params = { "action=cancel" }, method = { RequestMethod.POST })
	public String doCancel(@Valid @ModelAttribute("myprofileSecurityInfoForm") MyProfileSecurityInfoForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("input");
			}
		}
		MyProfileUtil.clearContext(request);
		return forwards.get("cancel");
	}

	private PasswordChallenge[] getChallenages(MyProfileSecurityInfoForm form) {
		PasswordChallenge[] challenges = null;
		challenges = new PasswordChallenge[] {
				new PasswordChallenge(form.getChallenge1().getQuestionText(), form.getChallenge1().getAnswer()),
				new PasswordChallenge(form.getChallenge2().getQuestionText(), form.getChallenge2().getAnswer()), };
		return challenges;
	}

	/**
	 * Populate the form for current challenge questions
	 * 
	 * @param form
	 * @param request
	 * @param response
	 * @throws SystemException
	 */
	private void prepareRendering(ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		BDUserProfile user = BDSessionHelper.getUserProfile(request);
		try {
			String[] questions = BDUserSecurityServiceDelegate.getInstance()
					.getChallengeQuestions(user.getBDPrincipal());
		    MyProfileSecurityInfoForm f = (MyProfileSecurityInfoForm) form;
			f.setCurrentChallengeQuestions(questions);
		} catch (SecurityServiceException e) {
			logger.error("Fail to get the challenge question", e);
			throw new SystemException(e,
					"Could not get challenge questions for user profileId = " + user.getBDPrincipal().getProfileId());
		}
		MyProfileContext context = MyProfileUtil.getContext(request.getServletContext(), request);
		context.getNavigation().setCurrentTabId(MyProfileNavigation.SecurityInfoTabId);
	}

	/**
	 * Validate the
	 * 
	 * @param errors
	 */
	public boolean validate(MyProfileSecurityInfoForm form, List<ValidationError> errors, HttpServletRequest request) {
		boolean change = false;
		// If atleast one of the passwords present
		if (!StringUtils.isEmpty(form.getNewPassword()) || !StringUtils.isEmpty(form.getConfirmedPassword())) {
			change = true;
			// Password mandatory and standards must be met
			pwdMandatoryRule.validate(MyProfileSecurityInfoForm.FIELD_PASSWORD, errors, form.getNewPassword());
			// Confirm Password mandatory and standards must be met
			confirmedPwdMandatoryRule.validate(MyProfileSecurityInfoForm.FIELD_CONFIRMED_PASSWORD, errors,
					form.getConfirmedPassword());
			// If both passwords are present
			// Changes for defects 8589 , 8590
			if (!StringUtils.isEmpty(form.getNewPassword()) && !StringUtils.isEmpty(form.getConfirmedPassword())) {
				
				NewPasswordRule.getInstance().validate("", errors,
						new Pair<String, String>(form.getNewPassword(), form.getConfirmedPassword()));
			}
		}
		// If all challege question values are present then do the validation
		// using the generic rule
		if (form.getChallenge1().allValuesPresent() && form.getChallenge2().allValuesPresent()) {
			change = true;
			PasswordChallengeInputRule.getInstance().validate("", errors,
					new Pair<PasswordChallengeInput, PasswordChallengeInput>(form.getChallenge1(),
							form.getChallenge2()));
			// If only some of the challenge question values present then show
			// the common error message
			// for missing fields.
		} else if (!form.getChallenge1().isEmpty() || !form.getChallenge2().isEmpty()) {
			change = true;
			errors.add(new ValidationError("", BDErrorCodes.MUST_ENTER_ALL_CHALLENGE_QA));
		}
		if (change) {
			currentPwdMandatory.validate("currentPassword", errors, form.getCurrentPassword());
		}
		return change;
	}

	/**
	 * Validate form and request against penetration attack, prior to other
	 * validations.
	 */
	@Autowired
	private BDValidatorFWFail bdValidatorFWFail;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWFail);
	}
	
	
	@RequestMapping(value ="/security/ajaxvalidator/" ,method =  {RequestMethod.POST})
	@ResponseBody
	public String doDeApiAjaxCall(@Valid @ModelAttribute("myprofileSecurityInfoForm") MyProfileSecurityInfoForm form, BindingResult bindingResult,
           HttpServletRequest request, HttpServletResponse response) throws ServletException, SystemException,SecurityServiceException, IOException{
		BDUserProfile userProfileDetails = BDSessionHelper.getUserProfile(request);
		// Passing Boolean value as True for only FRW External Users to validate
		PasswordMeterUtility.validatePasswordMeter(request,response,userProfileDetails.getBDPrincipal().getUserName().toString(),Boolean.TRUE);
		return null;
		}
}
