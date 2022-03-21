package com.manulife.pension.ps.web.login;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.common.PhoneNumber;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseController;
import com.manulife.pension.platform.web.passcode.EmailAddressMaskFormat;
import com.manulife.pension.platform.web.passcode.MobileMask;
import com.manulife.pension.platform.web.passcode.PasscodeErrorMessage;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.security.SecurityUtils;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.passcode.IllegalPasscodeStateException;
import com.manulife.pension.service.security.passcode.MailServerInactiveException;
import com.manulife.pension.service.security.passcode.PasscodeChannel;
import com.manulife.pension.service.security.passcode.PasscodeDeliveryPreference;
import com.manulife.pension.service.security.passcode.PasscodeGenerationResult;
import com.manulife.pension.service.security.passcode.RequestDetails;
import com.manulife.pension.service.security.passcode.SmsServerInactiveException;
import com.manulife.pension.service.security.passcode.VoiceServerInactiveException;
import com.manulife.pension.service.security.valueobject.MobileNumber;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.EventLog;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.pension.validation.ValidationError;

@Controller
@RequestMapping(value = "/login/forgotPasswordPasscodeTransition")
@SessionAttributes({"passwordResetAuthenticationForm"})
public class PasswordResetPasscodeTransitionController extends BaseController {

	@ModelAttribute("passwordResetAuthenticationForm")
	public PasswordResetAuthenticationForm populateForm() {
		return new PasswordResetAuthenticationForm();
	}

	private static final String DEFAULT = "default";
	private static final String ERROR = "error";
	private static final String STEPUP = "stepup";
	public static final String USERNAME_PARAM = "userName";
	private static final EmailAddressMaskFormat MASK_FORMAT = new EmailAddressMaskFormat(
			"the email address we have on file");

	public static HashMap<String, String> forwards = new HashMap<String, String>();

	static {
		forwards.put(DEFAULT, "/login/forgotPasswordPasscodeTransition.jsp");
		forwards.put(STEPUP, "redirect:/do/login/forgotPasswordPasscodeValidation/");
		forwards.put(ERROR, "/login/login.jsp");
	}

	public PasswordResetPasscodeTransitionController() {
		super(PasswordResetPasscodeTransitionController.class);
	}

	@RequestMapping(value = "/", method = {RequestMethod.GET,RequestMethod.POST})
	public String doExecute(@Valid @ModelAttribute("passwordResetAuthenticationForm") 
						PasswordResetAuthenticationForm form, BindingResult bindingResult, HttpServletRequest request, 
						HttpServletResponse response) throws ServletException {
		
		if(bindingResult.hasErrors()){
            String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
            if(errDirect!=null){
            request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
            return forwards.get(DEFAULT);
            }
       }
		
		final String action = getSubmitAction(request, form);
		final HttpSession session = request.getSession();
		
		if (session.getAttribute(CommonConstants.PASSCODE_SESSION_KEY) == null || session.getAttribute("USERID") == null) {
			// refresh session in order to protect against session fixation
			// exploit
			SessionHelper.invalidateSession(request, response);
			return forwardTo(ERROR, request);
		}
		
		PsPasscodeSession passcodeSession = (PsPasscodeSession) session.getAttribute(CommonConstants.PASSCODE_SESSION_KEY);
		final String devicePrint = request.getParameter(CommonConstants.DEVICE_PRINT);
		
		try {

			if (passcodeSession.getLoginInformation().isOptionalPassodeChannel() && !"Send".equals(action)) {

				if (passcodeSession.getLoginInformation().getMobileNumber() != null) {
					form.setMaskedMobile(
							MobileMask.maskPhone(passcodeSession.getLoginInformation().getMobileNumber().toString()));
				}

				form.setMaskedPhone(passcodeSession.getLoginInformation().getPhoneNumber() != null
						&& StringUtils.isEmpty(passcodeSession.getLoginInformation().getPhoneExtension())
								? MobileMask.maskPhone(
										passcodeSession.getLoginInformation().getPhoneNumber().toString())
								: null);
				form.setMaskedEmail(MASK_FORMAT.format(passcodeSession.getLoginInformation().getEmail()));

				final SecurityServiceDelegate securityServiceDelegate = SecurityServiceDelegate.getInstance();

				if (StringUtils.isNotEmpty(passcodeSession.getRecipientMobile())) {
					if (securityServiceDelegate.isPasscodeChannelDown(PasscodeChannel.SMS)) {
						form.setSmsSwithOn(true);
					}
					if (securityServiceDelegate.isPasscodeChannelDown(PasscodeChannel.VMOBILE)) {
						form.setVoiceSwithOn(true);
					}
				}

				if (StringUtils.isNotEmpty(passcodeSession.getRecipientPhone())
						&& securityServiceDelegate.isPasscodeChannelDown(PasscodeChannel.VPHONE)) {
					form.setVoiceSwithOn(true);
				}

				if (securityServiceDelegate.isPasscodeChannelDown(PasscodeChannel.EMAIL)) {
					form.setEmailSwithOn(true);
				}
				
				if (form.isSmsSwithOn() && form.isVoiceSwithOn() && form.isEmailSwithOn()) {
					throw new SystemException(
							"No Passcode delivey channel is avilable, due to turning on all passcode delevery channel switches");
				}
				
				form.setPasscodeDeliveryPreference(getPreSelectOption(
						passcodeSession.getLoginInformation().getPasscodeDeliveryPreference(), form));
				
				response.setHeader("Pragma", "no-cache");
				response.setHeader("Expires", "-1");
				response.setHeader("Cache-Control", "no-cache");

				return forwards.get(DEFAULT);

			} else {

				if (passcodeSession.getLoginInformation().isOptionalPassodeChannel() &&  "Send".equals(action)) {

					validatePhoneNumbers(form.getPasscodeDeliveryPreference(),
							passcodeSession.getLoginInformation().getMobileNumber(),
							passcodeSession.getLoginInformation().getPhoneNumber());

				}
				if (session.getAttribute(Constants.IS_TRANSITION) != null) {
					session.removeAttribute(CommonConstants.IS_TRANSITION);
				}
				
				final String errorForward = generatePasscode(request, response, devicePrint,
						form.getPasscodeDeliveryPreference());
				
				response.setHeader("Pragma", "no-cache");
				response.setHeader("Expires", "-1");
				response.setHeader("Cache-Control", "no-cache");

				if (errorForward != null) {
					return errorForward;
				}
				return forwards.get(STEPUP);

			}

		} catch (final SystemException se) {

			LogUtility.logSystemException(Constants.PS_APPLICATION_ID, se);
			return handleErrors(request, response, new PasscodeErrorMessage.PasscodeErrorMap.Builder()
					.add(PasscodeErrorMessage.SYSTEM_ERROR_AT_LOGIN, ErrorCodes.PASSCODE_SYSTEM_ERROR_AT_LOGIN_USA,
							ErrorCodes.PASSCODE_SYSTEM_ERROR_AT_LOGIN_NY)
					.build().withArguments(PasscodeErrorMessage.SYSTEM_ERROR_AT_LOGIN));
		}

	}

	private String generatePasscode(HttpServletRequest request, HttpServletResponse response, final String devicePrint,
			PasscodeDeliveryPreference passcodeDeliveryPreference) throws SystemException {

		final HttpSession session = request.getSession(false);
		final PsPasscodeSession passcodeSession = (PsPasscodeSession) session
				.getAttribute(CommonConstants.PASSCODE_SESSION_KEY);

		if (passcodeSession == null || session.getAttribute("USERID") == null) {
			// refresh session in order to protect against session fixation
			// exploit
			SessionHelper.invalidateSession(request, response);
			return forwardTo(ERROR, request);
		}

		try {
			if (passcodeSession.getLoginInformation().isOptionalPassodeChannel()) {
				validatePhoneNumbers(passcodeDeliveryPreference,
						passcodeSession.getLoginInformation().getMobileNumber(),
						passcodeSession.getLoginInformation().getPhoneNumber());
			}

			PasscodeGenerationResult generationResult = PsPasscodeVerification.INSTANCE.generatePasscodeForUser(
					passcodeSession.getLoginInformation(), session, new RequestDetails(request, devicePrint),
					passcodeSession.getLoginInformation().isOptionalPassodeChannel() ? passcodeDeliveryPreference
							: PasscodeDeliveryPreference.EMAIL);

			switch (generationResult.getResult()) {

			case SUCCESS:
			case EMAIL_WAIT:
				// proceed
				break;
			case COOLING:
				return handleErrors(request, response, new PasscodeErrorMessage.PasscodeErrorMap.Builder()
						.add(PasscodeErrorMessage.COOLING, ErrorCodes.PASSCODE_COOLING_USA,
								ErrorCodes.PASSCODE_COOLING_NY)
						.build()
						.withArguments(PasscodeErrorMessage.COOLING, new SimpleDateFormat("MMM d, yyyy h:mm a z")
								.format(generationResult.getCoolingTimestamp())));
			case PREVIOUSLY_LOCKED:
				return handleErrors(request, response,
						new PasscodeErrorMessage.PasscodeErrorMap.Builder()
								.add(PasscodeErrorMessage.LOCKED, ErrorCodes.PASSCODE_LOCKED_USA,
										ErrorCodes.PASSCODE_LOCKED_NY)
								.build().withArguments(PasscodeErrorMessage.LOCKED));
			default:
				return handleErrors(request, response);

			}

		} catch (final MailServerInactiveException msie) {

			try {
				SecurityUtils.handleSecurityServiceException(new MailServerInactiveException(this.getClass().getName(),
						"login", "PSW Mail server is inactive," + EventLog.REMOTE_ADDRESS + ":"
								+ new RequestDetails(request, devicePrint).getIpAddress()));
			} catch (SecurityServiceException e) {
				return handleErrors(request, response, new GenericException(ErrorCodes.DENY));
			}
		}

		catch (final SmsServerInactiveException ssie) {
			return handleErrors(request, response, new ValidationError("SmsSwitchOn", ErrorCodes.SMS_SWITCH_ON));
		} catch (VoiceServerInactiveException e) {
			return handleErrors(request, response, new ValidationError("VoiceSwitchOn", ErrorCodes.SMS_SWITCH_ON));
		} catch (final IllegalPasscodeStateException ipse) {
			throw new SystemException(ipse, ipse.getMessage());

		}

		return null;

	}

	private void validatePhoneNumbers(PasscodeDeliveryPreference passcodeDeliveryPreference, MobileNumber mobile,
			PhoneNumber phone) throws SystemException {

		switch (passcodeDeliveryPreference) {
		case SMS:
		case VOICE_TO_MOBILE:
			if (mobile == null || mobile.toString().length() != 10) {
				throw new SystemException("Invalid mobile number");
			}

			break;
		case VOICE_TO_PHONE:
			if (phone == null || phone.toString().length() != 10) {
				throw new SystemException("Invalid telephone number");
			}
			break;
		default:
			break;
		}
	}
	
	private PasscodeDeliveryPreference getPreSelectOption(PasscodeDeliveryPreference pref,
			PasswordResetAuthenticationForm form) {

		if (pref.equals(PasscodeDeliveryPreference.EMAIL) && form.isEmailSwithOn()) {
			if (StringUtils.isNotEmpty(form.getMaskedMobile()) && !form.isSmsSwithOn()) {
				return PasscodeDeliveryPreference.SMS;
			} else if (StringUtils.isNotEmpty(form.getMaskedMobile()) && !form.isVoiceSwithOn()) {
				return PasscodeDeliveryPreference.VOICE_TO_MOBILE;
			} else if (StringUtils.isNotEmpty(form.getMaskedPhone()) && !form.isVoiceSwithOn()) {
				return PasscodeDeliveryPreference.VOICE_TO_PHONE;
			}
		} else if (pref.equals(PasscodeDeliveryPreference.SMS) && form.isSmsSwithOn()) {
			if (StringUtils.isNotEmpty(form.getMaskedMobile()) && !form.isVoiceSwithOn()) {
				return PasscodeDeliveryPreference.VOICE_TO_MOBILE;
			} else if (StringUtils.isNotEmpty(form.getMaskedPhone()) && !form.isVoiceSwithOn()) {
				return PasscodeDeliveryPreference.VOICE_TO_PHONE;
			} else if (StringUtils.isNotEmpty(form.getMaskedEmail()) && !form.isEmailSwithOn()) {
				return PasscodeDeliveryPreference.EMAIL;
			}
		} else if ((pref.equals(PasscodeDeliveryPreference.VOICE_TO_MOBILE)
				|| pref.equals(PasscodeDeliveryPreference.VOICE_TO_PHONE)) && form.isVoiceSwithOn()) {
			if (StringUtils.isNotEmpty(form.getMaskedMobile()) && !form.isSmsSwithOn()) {
				return PasscodeDeliveryPreference.SMS;
			} else if (StringUtils.isNotEmpty(form.getMaskedEmail()) && !form.isEmailSwithOn()) {
				return PasscodeDeliveryPreference.EMAIL;
			}
		}

		return pref;
	}

	private String handleErrors(HttpServletRequest request, HttpServletResponse response,
			final GenericException... errors) {

		SessionHelper.invalidateSession(request, response);
		request.setAttribute(Constants.ERROR_KEYS, Arrays.asList(errors));

		return forwardTo(ERROR, request);
	}
	
	private static String forwardTo(final String forwardName, final HttpServletRequest request) {
		return forwards.get(forwardName);

	}
	
	@Autowired
	private PSValidatorFWDefault psValidatorFWDefault;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		//binder.bind(request);
		binder.addValidators(psValidatorFWDefault);
	} 

}
