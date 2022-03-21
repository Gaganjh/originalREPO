package com.manulife.pension.ps.web.login;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.passcode.PasscodeErrorMessage;
import com.manulife.pension.platform.web.passcode.PasscodeForm;
import com.manulife.pension.platform.web.passcode.ValidatePasscodeController;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.SecurityManager;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PsValidator1;
import com.manulife.pension.service.security.valueobject.LoginPSValueObject;

@Controller
@RequestMapping( value ="/passcodeValidation")

public class PsValidatePasscodeController extends ValidatePasscodeController<LoginPSValueObject> {
	private static final String SUCCESS = "success";
	private long employeeProfileId = 0L;
	@ModelAttribute("passcodeForm")
	public PasscodeForm populateForm()
	{
		return new PasscodeForm();
		}
	public static HashMap<String,String> forwards=new HashMap<String,String>();
	static{
		forwards.put("default" ,"/login/passcode.jsp");
		forwards.put(SUCCESS ,"redirect:/do/home/homePageFinder/");
		forwards.put("phoneCollection" ,"redirect:/do/phoneCollection/");
		forwards.put("retry" ,"/login/passcode.jsp");
		forwards.put("failure" ,"/login/login.jsp");
		forwards.put("expired" ,"/login/passcode.jsp");
		forwards.put("locked" ,"/login/login.jsp");
		forwards.put("cooling" ,"/login/passcode.jsp");
		forwards.put("cancel" ,"/login/login.jsp");
		forwards.put("resend" ,"/login/passcode.jsp");
		forwards.put("expired_sms" ,"/login/passcode.jsp");
		forwards.put("retry_sms" ,"/login/passcode.jsp");
		forwards.put("sms_switch_on" ,"/login/login.jsp");
	    forwards.put("updatePassword", "redirect:/do/password/updatePassword/");
	    forwards.put("updatePasswordOnly", "/do/password/updatePassword/");
	  	    
			}
	 public PsValidatePasscodeController() {
	        super(
	                PsValidatePasscodeController.class,
	                Constants.PS_APPLICATION_ID,
	                PsValidator1.getInstance(),
	                PsPasscodeVerification.INSTANCE,
	                PsAuthenticatedSessionInitialization.INSTANCE,
	                new PasscodeErrorMessage.PasscodeErrorMap.Builder()
	                .add(PasscodeErrorMessage.RETRY, 3551)
	                .add(PasscodeErrorMessage.LOCKED, 3552, 3555)
	                .add(PasscodeErrorMessage.COOLING, 3553, 3556)
	                .add(PasscodeErrorMessage.EXPIRED, 3550)
	                .add(PasscodeErrorMessage.BLANK_PASSCODE, 3554)
	                .add(PasscodeErrorMessage.SYSTEM_ERROR_AT_LOGIN, 3557, 3559)
	                .add(PasscodeErrorMessage.SYSTEM_ERROR_AT_RESEND, 3558, 3560)
	                .add(PasscodeErrorMessage.EXPIRED_SMS, 8142)
	                .add(PasscodeErrorMessage.RETRY_SMS, 8143)
	                .add(PasscodeErrorMessage.SMS_SWITCH_ON, 8140)
	                .build());

	    }
/*	public PsValidatePasscodeAction(Class<?> clazz, String appIdForAppLog, ValidateCatalogLaunch injectionValidation,
			PasscodeVerification<LoginPSValueObject> verification,
			AuthenticatedSessionInitialization<LoginPSValueObject> initialization, PasscodeErrorMap errorMap) {
		super(clazz, appIdForAppLog, injectionValidation, verification, initialization, errorMap);
	}*/
	
	@RequestMapping(value ="/",  method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doDefault (@Valid @ModelAttribute("passcodeForm") PasscodeForm passcodeForm,BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException, ServletException, SystemException {
		String challengeRequestFrom = null;
		if (null != request.getSession(false).getAttribute("challengeRequestFrom")) {
			challengeRequestFrom = (String) request.getSession(false).getAttribute("challengeRequestFrom");
		}

		
		if (null != request.getSession(false).getAttribute("employeeProfileId")) {
			employeeProfileId = (long) request.getSession(false).getAttribute("employeeProfileId");
		}

		UserProfile userProfile = null;
		if (null != request.getSession().getAttribute(Constants.USERPROFILE_KEY)) {

			userProfile = (UserProfile) request.getSession().getAttribute(Constants.USERPROFILE_KEY);
		}
		
		if (null != PsPasscodeVerification.INSTANCE.getPasscodeSession(request.getSession(false))) {
		final LoginPSValueObject loginVo = PsPasscodeVerification.INSTANCE.getPasscodeSession(request.getSession(false))
				.getLoginInformation();
		
		final String action = super.doExecute(passcodeForm, request, response);
		
		if("updatePassword".equals(action)){
			request.getSession(false).setAttribute(Constants.PASSCODE_FLOW_NEW_USER,true);
			return verifyAndFindForward(request,response ,getUserProfile(request),forwards.get("updatePasswordOnly"));
		}else{
			request.getSession(false).setAttribute(Constants.PASSCODE_FLOW_NEW_USER,false);
		}
		
		if(SUCCESS.equals(action) 
				&& "editProfilePage".equalsIgnoreCase(StringUtils.trimToEmpty(challengeRequestFrom))) {
			request.getSession().setAttribute(Constants.CHALLENGE_PASSCODE_IND, false);
			if (Objects.nonNull(userProfile)) {

				request.getSession().setAttribute(Constants.USERPROFILE_KEY, userProfile);
			}
			return "redirect:/do/profiles/editMyProfile/?loginFlow=Y";
		}
		 if(SUCCESS.equals(action) 
				&& "editEmployeeSnapshotPage".equalsIgnoreCase(StringUtils.trimToEmpty(challengeRequestFrom))) {
			request.getSession(false).setAttribute(Constants.CHALLENGE_PASSCODE_IND, false);
			request.getSession(false).setAttribute("employeeProfileId", employeeProfileId);
			if (Objects.nonNull(userProfile)) {

				request.getSession().setAttribute(Constants.USERPROFILE_KEY, userProfile);
			}
			return "redirect:/do/census/editEmployeeSnapshot/?profileId="+employeeProfileId+"&source=censusSummary";
		}
		 if(SUCCESS.equals(action) 
				&& "viewEmployeeSnapshotPage".equalsIgnoreCase(StringUtils.trimToEmpty(challengeRequestFrom))) {
			request.getSession(false).setAttribute(Constants.CHALLENGE_PASSCODE_IND, false);
			request.getSession(false).setAttribute("employeeProfileId", employeeProfileId);
			if (Objects.nonNull(userProfile)) {

				request.getSession().setAttribute(Constants.USERPROFILE_KEY, userProfile);
			}
			return "redirect:/do/census/editEmployeeSnapshot/?profileId="+employeeProfileId+"&source=censusSummary";
		}
		 if(SUCCESS.equals(action) 
					&& "editBeneficiaryInformationPage".equalsIgnoreCase(StringUtils.trimToEmpty(challengeRequestFrom))) {
				request.getSession(false).setAttribute(Constants.CHALLENGE_PASSCODE_IND, false);
				request.getSession(false).setAttribute("employeeProfileId", employeeProfileId);
				if (Objects.nonNull(userProfile)) {

					request.getSession().setAttribute(Constants.USERPROFILE_KEY, userProfile);
				}
				return "redirect:/do/census/beneficiary/editBeneficiaryInformation/?profileId="+employeeProfileId;
			}
		if ("failure".equalsIgnoreCase(action)) {
			request.getSession(false).invalidate();
			return forwards.get(action);
		}
		if (action != SUCCESS) {
			return forwards.get(action);
		}
		
		if (loginVo.isPhoneNumberCollection()) {
			request.getSession().setAttribute(Constants.PHONE_COLLECTION, true);
			request.getSession().setAttribute("isChallengedAtLogin", true);
			return forwards.get("phoneCollection");
		}
		return forwards.get(action);
		
		}
		//Defect 8696 & 8697
		//If the user click back button after getting OTP verified
		//then direct the user to same page from where he clicked back button.
		else {
			return redirectUser(userProfile, challengeRequestFrom, request);
		}
	}


	
	/**
	 * This method is implemented to avoid deadlock if the user is not permitted to go to page. 
	 * If we don't do this stach will overflow.
	 * 
	 */
	private String verifyAndFindForward(HttpServletRequest request, HttpServletResponse response, UserProfile userProfile, String url)
	{
		String forward = url;		
		// This should never happen 
		if ( !SecurityManager.getInstance().isUserAuthorized(userProfile, forward) )
		{
			forward = forwards.get("failue");
			SessionHelper.invalidateSession(request, response);
			if ( logger.isDebugEnabled() ) {
				logger.debug("Permissions are not allowed for " + forward+ ". Forwarding to Login page.");
			}
		}
		return "redirect:"+forward;
	}
	
	public static UserProfile getUserProfile(final HttpServletRequest request) {
		return SessionHelper.getUserProfile(request);
	}
	
	public String redirectUser(UserProfile userProfile, String challengeRequestFrom, HttpServletRequest request ) {

		if( "editProfilePage".equalsIgnoreCase(StringUtils.trimToEmpty(challengeRequestFrom))) {
				if (Objects.nonNull(userProfile)) {

					request.getSession().setAttribute(Constants.USERPROFILE_KEY, userProfile);
				}
				return "redirect:/do/profiles/editMyProfile/?loginFlow=Y";
				
			}
		else if("editEmployeeSnapshotPage".equalsIgnoreCase(StringUtils.trimToEmpty(challengeRequestFrom))) {
				if (Objects.nonNull(userProfile)) {

					request.getSession().setAttribute(Constants.USERPROFILE_KEY, userProfile);
				}
				return "redirect:/do/census/editEmployeeSnapshot/?profileId="+employeeProfileId+"&source=censusSummary";
			}
		else if("viewEmployeeSnapshotPage".equalsIgnoreCase(StringUtils.trimToEmpty(challengeRequestFrom))) {
				if (Objects.nonNull(userProfile)) {

					request.getSession().setAttribute(Constants.USERPROFILE_KEY, userProfile);
				}
				return "redirect:/do/census/editEmployeeSnapshot/?profileId="+employeeProfileId+"&source=censusSummary";
			}
		else if("editBeneficiaryInformationPage".equalsIgnoreCase(StringUtils.trimToEmpty(challengeRequestFrom))) {
					if (Objects.nonNull(userProfile)) {

						request.getSession().setAttribute(Constants.USERPROFILE_KEY, userProfile);
					}
					return "redirect:/do/census/beneficiary/editBeneficiaryInformation/?profileId="+employeeProfileId;
				}
		
		return "redirect:/do/home/homePageFinder/";
	}
	
}