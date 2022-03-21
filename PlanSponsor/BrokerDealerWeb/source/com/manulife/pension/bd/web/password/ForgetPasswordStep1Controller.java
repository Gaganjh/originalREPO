package com.manulife.pension.bd.web.password;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.process.BDPublicWizardProcessController;
import com.manulife.pension.bd.web.process.BDWizardProcessContext;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.delegate.BDPublicSecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.service.security.bd.valueobject.BDUserSecurityProfile;
import com.manulife.pension.service.security.exception.DisabledUserException;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.exception.UserNotRegisteredException;
import com.manulife.pension.service.security.passcode.PasscodeSecurity;
import com.manulife.pension.service.security.passcode.UserPasscodeDetailedInfo;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.validator.ValidationError;

/**
 * Step1 action for forget password process. This action validates the userId
 * and profile to see if the user is allowed to do forget password
 * 
 * @author guweigu
 * 
 */
@Controller
@RequestMapping(value ="/forgetPassword")

public class ForgetPasswordStep1Controller extends BDPublicWizardProcessController {
	
	@ModelAttribute("forgetPasswordForm") 
	public ForgetPasswordForm populateForm()
	{
		return new ForgetPasswordForm();
		}
	public static HashMap<String,String> forwards=new HashMap<String,String>();
	private static ControllerRedirect HomeRedirect1 = new ControllerRedirect(URLConstants.HomeURL);
	private static final String HomeRedirect=HomeRedirect1.getPath();
	static{
		forwards.put("input","/password/forgetPasswordStep1.jsp");
		forwards.put("fail","/password/forgetPasswordStep1.jsp");
		}

	private static Map<String, Integer> localSecurityServiceExceptionMapping = new HashMap<String, Integer>(
			3);
	
	static {
		localSecurityServiceExceptionMapping.put(
				UserNotRegisteredException.class.getName(),
				BDErrorCodes.FORGET_PWD_PROFILE_NOT_ACTIVE);
		localSecurityServiceExceptionMapping.put(
				DisabledUserException.class.getName(),
				BDErrorCodes.FORGET_PWD_PROFILE_DELETED);
	}

	public ForgetPasswordStep1Controller() {
		super(ForgetPasswordStep1Controller.class, ForgetPasswordContext.class,
				ForgetPasswordContext.ProcessName, ForgetPasswordContext.Step1);
	}

	/**
	 * Handle when the user clicks continue button
	 */
	@Override
	protected ProcessState doContinue(
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		List<ValidationError> errors = new ArrayList<ValidationError>(1);
		ForgetPasswordForm form = (ForgetPasswordForm) actionForm;
		String ipAddress = IPAddressUtils.getRemoteIpAddress(request);
		BDUserSecurityProfile profile = null ;
		final String APP_NAME = "BD";
		if (StringUtils.isEmpty(form.getUserId())) {
			errors.add(new ValidationError("userId", BDErrorCodes.EMPTY_USERNAME));
		}
		if (errors.isEmpty()) {
			
			try {
				BDPublicSecurityServiceDelegate delegate = BDPublicSecurityServiceDelegate.getInstance() ;
				profile = delegate.validateForForgetPassword(
								form.getUserId(), ipAddress);
				if (profile.getPrincipal().getBDUserRole().getRoleType()
						.isInternal()) {
					errors.add(new ValidationError("userId",
							BDErrorCodes.FORGET_PWD_NOT_ALLOWED_INTERNAL_USER));
				} else if (profile.isBadChallengeAnswerOverLimit()) {
					errors.add(new ValidationError("userId",
							BDErrorCodes.FORGET_PWD_FAILED_CHALLENGE));
				} else if (profile.getChallengeQuestions() == null
						|| profile.getChallengeQuestions().length != 2) {
					errors.add(new ValidationError("userId",
							BDErrorCodes.FORGET_PWD_FAILED_CHALLENGE));
				} else if (StringUtils.isEmpty(profile.getPrincipal()
						.getEmailAddress())) {
					errors.add(new ValidationError("userId",
							BDErrorCodes.FORGET_PWD_PROFILE_NO_EMAIL_ADDRESS));
				} else if (delegate.isAllowedToChangePasswordViaForgotPassword(
						profile.getPrincipal().getProfileId(), APP_NAME, ipAddress)) {
					errors.add(new ValidationError("userId", BDErrorCodes.FORGET_PWD_NOT_ALLOWED_TO_CHANGED));
				} else {
					ForgetPasswordContext context = (ForgetPasswordContext) getProcessContext(request);
					context.setSecurityProfile(profile);
				}
			} catch (SecurityServiceException e) {
				logger.debug("Fail the validation of userName", e);
				errors.add(new ValidationError("userId",
						SecurityServiceExceptionHelper.getErrorCodeForCause(e,
								localSecurityServiceExceptionMapping)));
			}
		}
		//RPSSS-122001 - If no errors have been found, check if the passcode status is Locked or Cooling
		if (errors.isEmpty() && Objects.nonNull(profile)) {
			UserPasscodeDetailedInfo userPasscodeDetailedInfo = PasscodeSecurity.BD
					.getLockedAndCoolingInfo(profile.getPrincipal().getProfileId());
			if (Objects.nonNull(userPasscodeDetailedInfo)){
				if (logger.isDebugEnabled()) {
					logger.debug("User Passcode Detailed Info for user id [" + profile.getPrincipal().getProfileId()
							+ ". Locked status is [" + userPasscodeDetailedInfo.isPasscodeLocked()
							+ "] and Cooling status is [" + userPasscodeDetailedInfo.isPasscodeCooling() + "]");
				}
				if ( userPasscodeDetailedInfo.isPasscodeLocked() ) errors.add(new ValidationError("userId", BDErrorCodes.PREVIOUSLY_LOCKED));
				else if ( userPasscodeDetailedInfo.isPasscodeCooling() ) errors.add( new ValidationError("userId", BDErrorCodes.COOLING, 
						new Object[] { new SimpleDateFormat("MMM d, yyyy h:mm a 'ET'").format(userPasscodeDetailedInfo.getCoolingTime() )}) );
			}	
		}
		
		ProcessState nextState = getState();
		if (!errors.isEmpty()) {
			setErrorsInSession(request, errors);
		} else {
			nextState = nextState
					.getNext(BDWizardProcessContext.ACTION_CONTINUE);
		}
		return nextState;
	} 

	
	@RequestMapping(value = "/step1", method = { RequestMethod.GET })
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
	@RequestMapping(value = "/step1", params = { "action=continue" }, method = { RequestMethod.POST })
	protected String doContinue(@Valid @ModelAttribute("forgetPasswordForm") ForgetPasswordForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, IOException, ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doContinue");
		}
		request.getSession(false).setAttribute("userId",form.getUserId());
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
	@RequestMapping(value = "/step1", params = { "action=cancel" }, method = { RequestMethod.POST })
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
	private BDValidatorFWFail bdValidatorFWFail;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWFail);
	}

	
}
