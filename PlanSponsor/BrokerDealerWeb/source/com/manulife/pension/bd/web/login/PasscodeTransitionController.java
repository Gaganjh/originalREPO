package com.manulife.pension.bd.web.login;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseController;
import com.manulife.pension.platform.web.passcode.PasscodeErrorMessage;
import com.manulife.pension.platform.web.passcode.PasscodeErrorMessage.PasscodeErrorMap;
import com.manulife.pension.platform.web.passcode.PasscodeForm;
import com.manulife.pension.platform.web.util.BaseSessionHelper;
import com.manulife.pension.ps.common.util.Constants;
import com.manulife.pension.service.security.passcode.IllegalPasscodeStateException;
import com.manulife.pension.service.security.passcode.MailServerInactiveException;
import com.manulife.pension.service.security.passcode.PasscodeGenerationResult;
import com.manulife.pension.service.security.passcode.PasscodeSecurity;
import com.manulife.pension.service.security.passcode.RequestDetails;
import com.manulife.pension.service.security.passcode.SmsServerInactiveException;
import com.manulife.pension.service.security.passcode.VoiceServerInactiveException;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.pension.validator.ValidationError;

@SuppressWarnings("unused")
@Controller
@RequestMapping(value ="/stepupTransition")


public class PasscodeTransitionController extends BaseController {

	@ModelAttribute("passcodeForm")
	public PasscodeForm populateForm()
	{
		return new PasscodeForm();
		}
	
	
	@SuppressWarnings("unchecked")

	private static final String DEFAULT = "default";
	private static final String ERROR = "error";
	private static final String SESSION_KEY = "PASSCODE_SESSION_KEY";
	private static final String CHANGEPASSWORDFLAG = "changePasswordFlag";
	private static PasscodeErrorMap errorMap;
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{ 
		forwards.put("default","/home/passcodeTransition.jsp");
		forwards.put("stepup","redirect:/do/stepupPasscode/");
		forwards.put("cancel","redirect:/do/home/");
		forwards.put("error","/home/public_home.jsp");
		
      	
	}
	public PasscodeTransitionController() {
		super(PasscodeTransitionController.class);
		this.errorMap = new PasscodeErrorMessage.PasscodeErrorMap.Builder().add(PasscodeErrorMessage.RETRY, 8121)
				.add(PasscodeErrorMessage.LOCKED, 8122).add(PasscodeErrorMessage.COOLING, 8123)
				.add(PasscodeErrorMessage.SYSTEM_ERROR_AT_LOGIN, 8126).build();
	}

	 @RequestMapping(value ="/", method ={RequestMethod.POST,RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("passcodeForm") PasscodeForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException, SystemException, MailServerInactiveException {

		List<ValidationError> errors = new ArrayList<ValidationError>();
		final String action = getSubmitAction(request, form);


		final HttpSession session = request.getSession(false);
		
		boolean changePasswordFlag = Boolean.FALSE;
		// sonarcube comments
		if(session != null && null != session.getAttribute(CHANGEPASSWORDFLAG)){
		changePasswordFlag = (boolean) session.getAttribute(CHANGEPASSWORDFLAG);
		}

		String businessParamValue = null;
		if(session != null && null != session.getAttribute(BDConstants.FRW_SHOW_PASSWORD_METER_IND)){
			businessParamValue = (String)session.getAttribute(BDConstants.FRW_SHOW_PASSWORD_METER_IND);
		}

		boolean challengeInd = false;
		String challengeRequest = (String)request.getSession().getAttribute("challengeRequestFrom"); 
		String currentTab = (String)request.getSession().getAttribute("myProfileCurrentTab");
		if(Objects.nonNull(request.getSession().getAttribute(CommonConstants.CHALLENGE_PASSCODE_IND))) {
			challengeInd = (boolean)request.getSession().getAttribute(CommonConstants.CHALLENGE_PASSCODE_IND);
		}
		
		if (session == null || session.getAttribute(BDConstants.IS_TRANSITION) == null ) {
			// refresh session in order to protect against session fixation exploit
			BaseSessionHelper.invalidateSession(request,response, false);
			return forwardTo(ERROR, request);
		}

		if ("Send".equals(action)) {

			PasscodeGenerationResult generationResult;
			BdPasscodeSession passcodeSession = (BdPasscodeSession) session.getAttribute(SESSION_KEY);
			passcodeSession.setPasswordChangeOrUpdateRequired(changePasswordFlag);
			passcodeSession.setBusinessParamValue(businessParamValue);

			// Defect 8711: userProfile is required if the user is challenged on OTP at Manage my
			// profile.
			BDUserProfile userProfile = null;
			if (null != challengeRequest) {
				userProfile = (BDUserProfile) session.getAttribute(BDConstants.USERPROFILE_KEY);
			}
			// refresh session in order to protect against session fixation exploit
			BaseSessionHelper.invalidateSessionKeepCookie(request);
			
			
			final HttpSession stepUpSession = request.getSession();
			stepUpSession.setAttribute(BDConstants.FRW_SHOW_PASSWORD_METER_IND, businessParamValue);
			stepUpSession.setAttribute("challengeRequestFrom", challengeRequest);
			stepUpSession.setAttribute("myProfileCurrentTab", currentTab);
			stepUpSession.setAttribute(CommonConstants.CHALLENGE_PASSCODE_IND, challengeInd);
			stepUpSession.setAttribute(BDConstants.USERPROFILE_KEY, userProfile);
			
			try {
				generationResult = BdPasscodeVerification.INSTANCE.generatePasscodeForUser(
						passcodeSession.getLoginInformation(), stepUpSession,
						new RequestDetails(request, passcodeSession.getDevicePrint()), null);

				switch (generationResult.getResult()) {

				case SUCCESS:
				case EMAIL_WAIT:
					// proceed
					break;
				case PREVIOUSLY_LOCKED:
					setErrorInRequest(request, PasscodeErrorMessage.LOCKED);
					return forwardTo(ERROR, request);
				case COOLING:
					setErrorInRequest(request, PasscodeErrorMessage.COOLING,
							new SimpleDateFormat("MMM d, yyyy h:mm a 'ET'")
									.format(generationResult.getCoolingTimestamp()));
					return forwardTo(ERROR, request);

				default:
					break;
				}

			} 
			catch (final MailServerInactiveException msie) {
	            
	            PasscodeSecurity.BD.reportLoginFail(String.valueOf(passcodeSession.getLoginInformation().getPrincipal().getProfileId()), new RequestDetails(request, passcodeSession.getDevicePrint()));
	            LogUtility.logApplicationException(msie);
	            return forwardTo(ERROR, request);
	            }
			
			catch (final SmsServerInactiveException ssie) {
    			
				setErrorInRequest(request, PasscodeErrorMessage.SMS_SWITCH_ON);
				return forwardTo(ERROR, request);
				
				}
			
			catch (final VoiceServerInactiveException vmie) {

				setErrorInRequest(request, PasscodeErrorMessage.VOICE_SWITCH_ON);
				return forwardTo(ERROR, request);

			}

			catch (final IllegalPasscodeStateException ipse) {

				throw new SystemException(ipse, ipse.getMessage());

			} catch (final SystemException se) {
				LogUtility.logSystemException(Constants.PS_APPLICATION_ID, se);
				setErrorInRequest(request, PasscodeErrorMessage.SYSTEM_ERROR_AT_LOGIN);
				return forwardTo(ERROR, request);
			}
			
			

			stepUpSession.setMaxInactiveInterval(Environment.getInstance()
					.getIntNamingVariable("bd.passcode.inactiveSessionTimeoutInSeconds", null, 300));

			// set the Direct URL parameter into the attribute
			String directUrl = StringUtils.trimToEmpty(request.getParameter(BDConstants.DIRECT_URL_ATTR));
			stepUpSession.setAttribute(BDConstants.DIRECT_URL_ATTR, directUrl);
			stepUpSession.setAttribute(BdAuthenticatedSessionInitialization.USERID_KEY, passcodeSession.getUserName());
			stepUpSession.setAttribute(CHANGEPASSWORDFLAG, changePasswordFlag);

			response.setHeader("Pragma", "no-cacMar@2118he");
			response.setHeader("Expires", "-1");
			response.setHeader("Cache-Control", "no-cache");
			response.sendRedirect(URLConstants.StepupAuth);
		}

		return forwards.get(DEFAULT);
	}

	private static String forwardTo(final String forwardName, final HttpServletRequest request) {

		request.setAttribute("PASSCODE_FORWARD_KEY", forwardName);
		return forwards.get(forwardName);

	}

	private void setErrorInRequest(final HttpServletRequest request, final PasscodeErrorMessage error,
			final String... args) {

		setErrorsInRequest(request, Arrays.asList(new GenericException[] { errorMap.withArguments(error, args) }));

	}

}
