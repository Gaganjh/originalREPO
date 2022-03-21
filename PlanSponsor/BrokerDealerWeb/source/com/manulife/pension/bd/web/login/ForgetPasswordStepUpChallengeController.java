/**
 * 
 */
package com.manulife.pension.bd.web.login;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.manulife.pension.bd.web.password.ForgetPasswordContext;
import com.manulife.pension.bd.web.password.ForgetPasswordForm;
import com.manulife.pension.bd.web.process.BDPublicWizardProcessController;
import com.manulife.pension.bd.web.process.BDWizardProcessContext;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.passcode.PasscodeErrorMessage;
import com.manulife.pension.platform.web.passcode.PasscodeErrorMessage.PasscodeErrorMap;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.ps.common.util.Constants;
import com.manulife.pension.service.security.bd.valueobject.BDLoginValueObject;
import com.manulife.pension.service.security.bd.valueobject.BDUserSecurityProfile;
import com.manulife.pension.service.security.passcode.IllegalPasscodeStateException;
import com.manulife.pension.service.security.passcode.MailServerInactiveException;
import com.manulife.pension.service.security.passcode.PasscodeChannel;
import com.manulife.pension.service.security.passcode.PasscodeGenerationResult;
import com.manulife.pension.service.security.passcode.PasscodeSecurity;
import com.manulife.pension.service.security.passcode.PasscodeSecurity.PasscodeDecisionResult;
import com.manulife.pension.service.security.passcode.PasscodeSecurity.RsaSession;
import com.manulife.pension.service.security.passcode.PasscodeSecurity.StepUpAction;
import com.manulife.pension.service.security.passcode.RequestDetails;
import com.manulife.pension.service.security.passcode.SmsServerInactiveException;
import com.manulife.pension.service.security.passcode.VoiceServerInactiveException;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.LogUtility;

@Controller
@RequestMapping("/forgetPassword/stepUpChallenge")
public class ForgetPasswordStepUpChallengeController extends BDPublicWizardProcessController {

	private static final Logger LOGGER = Logger.getLogger(ForgetPasswordStepUpChallengeController.class);
	private static final PasscodeErrorMap PASSCODE_ERROR_MAP = new PasscodeErrorMessage.PasscodeErrorMap.Builder()
			.add(PasscodeErrorMessage.RETRY, 8121)
			.add(PasscodeErrorMessage.LOCKED, 8122)
			.add(PasscodeErrorMessage.COOLING, 8123)
			.add(PasscodeErrorMessage.SYSTEM_ERROR_AT_LOGIN, 8126).build();
	
	private static final Map<String,String> forwards;
	static {
		Map<String, String> map = new HashMap<>();
		map.put("input", "/password/stepUpChallenge.jsp");
		map.put("fail", "/password/stepUpChallenge.jsp");
		forwards = Collections.unmodifiableMap(map);
	}

	@ModelAttribute("forgetPasswordForm")
	public ForgetPasswordForm populateForm() {
		return new ForgetPasswordForm();
	}

	public ForgetPasswordStepUpChallengeController() {
		super(ForgetPasswordStepUpChallengeController.class, ForgetPasswordContext.class,
				ForgetPasswordContext.ProcessName, ForgetPasswordContext.StepUpChallengeStep);
	}

	@GetMapping
	public String showForm(@Valid @ModelAttribute("forgetPasswordForm") ForgetPasswordForm forgetPasswordForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, IOException, ServletException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("entry -> showForm");
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
		ForgetPasswordContext context = (ForgetPasswordContext) getProcessContext(request);
		if ( Objects.nonNull(context) && Objects.nonNull(context.getSecurityProfile() ) ) {
			updateSessionForViewAndValidation(request, context.getSecurityProfile() );
		}
		
		String forward = super.doExecuteInput(request);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("super.doExecuteInput(request) output is [ " + forward + " ]");
		}

		if (forward == null) {
			String forward1 = super.doInput(forgetPasswordForm, request, response);
			return StringUtils.contains(forward1, '/') ? forward1 : forwards.get(forward1);
		}
		return forward;
	}
	
	private void updateSessionForViewAndValidation(HttpServletRequest request, BDUserSecurityProfile bdUserSecurityProfile) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Updating Session with BdPasscodeSession to show masked email address for [ "
					+ bdUserSecurityProfile.getPrincipal().getEmailAddress() + " ]");
		}
		HttpSession session = request.getSession();
		String devicePrint = request.getParameter(CommonConstants.DEVICE_PRINT);
		BDLoginValueObject bdLoginValueObject = new BDLoginValueObject(bdUserSecurityProfile);
		bdLoginValueObject.setPasscodeDecision(
				new PasscodeDecisionResult<RsaSession, String>(null, devicePrint, StepUpAction.CHALLENGE));
		BdPasscodeSession passcodeSession = new BdPasscodeSession(bdLoginValueObject);
		passcodeSession.setPasscodeChannel(PasscodeChannel.EMAIL);
		session.setAttribute(CommonConstants.PASSCODE_SESSION_KEY, passcodeSession);
		//This is for Challenge transition page
		session.setMaxInactiveInterval(Environment.getInstance()
				.getIntNamingVariable("bd.passcode.inactiveSendPasscodeTimeoutInSeconds", null, 300));
	}

	@PostMapping
	public String execute(@Valid @ModelAttribute("forgetPasswordForm") ForgetPasswordForm forgetPasswordForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, IOException, ServletException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("entry -> execute");
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

		String forward = super.doCancelContinue(forgetPasswordForm, request, response);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("super.doCancelContinue(request) output is [ " + forward + " ]");
		}

		return forward;
	}

	@Override
	protected ProcessState doContinue(ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {

		return sendPasscode(request, response);
	}

	protected ProcessState sendPasscode(HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		PasscodeGenerationResult passcodeGenerationResult = null;
		ForgetPasswordContext context = (ForgetPasswordContext) getProcessContext(request);
		final HttpSession session = request.getSession(false);
		BdPasscodeSession passcodeSession = (BdPasscodeSession) session
				.getAttribute(CommonConstants.PASSCODE_SESSION_KEY);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Sending passcode to email address [ " + passcodeSession.getRecipientEmail() + " ]");
		}
		
		try {
			final HttpSession stepUpSession = request.getSession();
			passcodeGenerationResult = BdPasscodeVerification.INSTANCE.generatePasscodeForUser(
					passcodeSession.getLoginInformation(), stepUpSession,
					new RequestDetails(request, passcodeSession.getDevicePrint()), null);

		} catch (final MailServerInactiveException mailException) {

			PasscodeSecurity.BD.reportLoginFail(
					String.valueOf(context.getSecurityProfile().getPrincipal().getProfileId()),
					new RequestDetails(request, request.getParameter(CommonConstants.DEVICE_PRINT)));

			LogUtility.logApplicationException(mailException);
			return getState().getNext(BDWizardProcessContext.ACTION_CANCEL);
		} catch (final VoiceServerInactiveException voiceException) {
			setErrorInRequest(request, PasscodeErrorMessage.SMS_SWITCH_ON);
			return getState().getNext(BDWizardProcessContext.ACTION_CANCEL);
		} catch (final SmsServerInactiveException smsException) {
			setErrorInRequest(request, PasscodeErrorMessage.VOICE_SWITCH_ON);
			return getState().getNext(BDWizardProcessContext.ACTION_CANCEL);
		} catch (final IllegalPasscodeStateException ipse) {
			throw new SystemException(ipse, ipse.getMessage());
		} catch (final SystemException se) {
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID, se);
			setErrorInRequest(request, PasscodeErrorMessage.SYSTEM_ERROR_AT_LOGIN);
			return getState().getNext(BDWizardProcessContext.ACTION_CANCEL);
		}
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Passcode successfully sent and the result is [ " + passcodeGenerationResult.getResult() + " ]");
		}

		switch (passcodeGenerationResult.getResult()) {

			case SUCCESS:
			case EMAIL_WAIT:
				// proceed
				break;
			case PREVIOUSLY_LOCKED:
				setErrorInRequest(request, PasscodeErrorMessage.LOCKED);
				return getState().getNext(BDWizardProcessContext.ACTION_CANCEL);
			case COOLING:
				setErrorInRequest(request, PasscodeErrorMessage.COOLING, new SimpleDateFormat("MMM d, yyyy h:mm a 'ET'")
						.format(passcodeGenerationResult.getCoolingTimestamp()));
				return getState().getNext(BDWizardProcessContext.ACTION_CANCEL);
	
			default:
				break;
			}
		//Session Timeout for validation page
		session.setMaxInactiveInterval(Environment.getInstance()
				.getIntNamingVariable("bd.passcode.inactiveSessionTimeoutInSeconds", null, 300));

		return getState().getNext(BDWizardProcessContext.ACTION_CONTINUE);
	}

	private void setErrorInRequest(final HttpServletRequest request, final PasscodeErrorMessage error,
			final String... args) {

		setErrorsInRequest(request,
				Arrays.asList(new GenericException[] { PASSCODE_ERROR_MAP.withArguments(error, args) }));

	}
	
	@Autowired
	   private BDValidatorFWFail  bdValidatorFWFail;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWFail);
	}

}
