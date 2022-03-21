package com.manulife.pension.bd.web.password;

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
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.service.security.bd.valueobject.BDUserSecurityProfile;
import com.manulife.pension.service.security.bd.valueobject.PasswordChallenge;
import com.manulife.pension.service.security.exception.FailedNTimesForgotPasswordException;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.passcode.PasscodeSecurity;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.validator.ValidationError;

/**
 * Validate the user's challenge question/answers
 * 
 * @author guweigu
 * 
 */
@Controller
@RequestMapping(value ="/forgetPassword")

public class ForgetPasswordStep2Controller extends BDPublicWizardProcessController {
	@ModelAttribute("forgetPasswordForm") 
	public ForgetPasswordForm populateForm() 
	{
		return new ForgetPasswordForm();
		}
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	private static ControllerRedirect HomeRedirect1 = new ControllerRedirect(URLConstants.HomeURL);
	private static final String HomeRedirect=HomeRedirect1.getPath();
	static {
		forwards.put("input", "/password/forgetPasswordStep2.jsp");
		forwards.put("fail", "/password/forgetPasswordStep2.jsp");
	}

	private final MandatoryRule rule = new MandatoryRule(
			BDErrorCodes.FORGET_PWD_BLANK_ANSWER);
	
	public ForgetPasswordStep2Controller() {
		super(ForgetPasswordStep2Controller.class, ForgetPasswordContext.class,
				ForgetPasswordContext.ProcessName, ForgetPasswordContext.Step2);
	}

	private PasswordChallenge[] constructChallengeAnswers(
			ForgetPasswordForm form, BDUserSecurityProfile profile) {
		PasswordChallenge[] challenges = new PasswordChallenge[2];
		challenges[0] = new PasswordChallenge(
				profile.getChallengeQuestions()[0], form.getAnswer1());
		challenges[1] = new PasswordChallenge(
				profile.getChallengeQuestions()[1], form.getAnswer2());
		return challenges;
	}

	@Override
	protected ProcessState doContinue(
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {

		ForgetPasswordContext context = (ForgetPasswordContext) getProcessContext(request);
		ForgetPasswordForm form = (ForgetPasswordForm) actionForm;
		List<ValidationError> errors = new ArrayList<ValidationError>(1);
		String ipAddress = IPAddressUtils.getRemoteIpAddress(request);
		if (rule.validate("", errors, form.getAnswer1())) {
			rule.validate("", errors, form.getAnswer2());
		}
		boolean isExempted = false;
		if (errors.size() == 0) {
			BDUserSecurityProfile profile = null;
			try {
				profile = context.getSecurityProfile();
				BDPublicSecurityServiceDelegate.getInstance()
						.validatePasswordChallenge(profile.getPrincipal(),
								constructChallengeAnswers(form, profile), ipAddress);
			} catch (SecurityServiceException e) {
				logger.debug("Fail the validation of challenge answer", e);
				Integer errorCode = SecurityServiceExceptionHelper
						.getErrorCodeForCause(e);
				errors.add(new ValidationError("", errorCode));
				if (e instanceof FailedNTimesForgotPasswordException) {
					context.setDisabled(true);
				}
			}
			//RPSSS-122001 : Check for exemption and if user is not exempted, update the session to show masked Email address on next page
			if ( isUserExempted(profile, IPAddressUtils.getRemoteIpAddress(request))  ) {
				if (logger.isDebugEnabled()) {
					logger.debug("User with profile id [ " + profile.getPrincipal().getProfileId() + " ] is exempted for OTP.");
				}
				isExempted = true;
			}
		}
		
		ProcessState nextState = getState();
		if (errors.size() > 0) {
			setErrorsInSession(request, errors);
		} else {
			nextState = nextState
					.getNext(BDWizardProcessContext.ACTION_CONTINUE);
		}
		
		//RPSSS-122001 : Skip the next 2 step for OTP if a user is exempted
		return (!isExempted || errors.size() > 0) ? nextState : nextState.getNext(BDWizardProcessContext.ACTION_CONTINUE).getNext(BDWizardProcessContext.ACTION_CONTINUE);
	}
	
	private boolean isUserExempted(final BDUserSecurityProfile userProfile, final String ipAddress) {
		if (Objects.nonNull(userProfile) && Objects.nonNull(userProfile.getPrincipal()))
			return PasscodeSecurity.BD.isExemptUserProfile(userProfile.getPrincipal().getProfileId(), ipAddress);
		return false;
	}
	
	@RequestMapping(value = "/step2", method = { RequestMethod.GET })
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
	@RequestMapping(value = "/step2", params = { "action=continue" }, method = { RequestMethod.POST })
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
	
	@RequestMapping(value = "/step2", params = { "action=cancel" }, method = { RequestMethod.POST })
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
}
