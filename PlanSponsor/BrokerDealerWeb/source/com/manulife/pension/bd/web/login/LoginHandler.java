package com.manulife.pension.bd.web.login;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.passcode.DeviceTokenCookie;
import com.manulife.pension.platform.web.validation.rules.UserNameRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.service.security.bd.valueobject.BDLoginValueObject;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.passcode.PasscodeSecurity;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.validator.ValidationError;

/**
 * The login handler that can be used by login servlet and other registration pages
 * 
 * @author guweigu
 * 
 */
public class LoginHandler {
    private static final Logger log = Logger.getLogger(LoginHandler.class);

    public static final String STEPUP_PASSCODE_LOOPBACK_OCTET = "127.";
   	public static final String PARTICIPANT_EMAIL_ADDRESS_STEP_UP_AUTH = "participantEmailAddressStepUpAuth";
   	private static final UserPasswordValidation VALIDATION = UserPasswordValidation.INSTANCE;
	private static final String SESSION_KEY = "PASSCODE_SESSION_KEY";



	public static LoginHandler getInstance() {
        return new LoginHandler(true, null);
    }
    public static LoginHandler getInstance(final String devicePrint) {
        return new LoginHandler(false, devicePrint);
    }
    
    private final boolean initiatedFromRegistration;
    private final String devicePrint;
    
    private LoginHandler(final boolean initiatedFromRegistration, final String devicePrint) {
    	this.initiatedFromRegistration = initiatedFromRegistration;
    	this.devicePrint= devicePrint;

    }

	

    /**
     * The login logic. 1. Call the security service to do the login 2. If success it invoke post login logic
     * 
     * @param userName
     * @param password
     * @param request
     * @param response
     * @throws SystemException
     * @throws ServletException
     * @throws IOException
     */
	public List<ValidationError> doLogin(String userName, String password, HttpServletRequest request,
			HttpServletResponse response, Map<String, Integer> localSecurityExceptionMapping)
			throws SystemException, ServletException, IOException {
    
    	List<ValidationError> errors = new ArrayList<ValidationError>();
        String trimedUserName = StringUtils.trimToEmpty(userName);		
        if (StringUtils.isEmpty(trimedUserName)) {
            errors.add(new ValidationError("userId", BDErrorCodes.EMPTY_USERNAME));
        }
        if (StringUtils.isEmpty(password)) {
            errors.add(new ValidationError("password", BDErrorCodes.EMPTY_PASSWORD));
        }
		
        if (errors.size() == 0) {
            RegularExpressionRule userNameRe= new RegularExpressionRule(CommonErrorCodes.USER_DOES_NOT_EXIST, UserNameRule.getInstance().getUserNameRuleRegExp()); 
            
            if(!userNameRe.validate(userName)) {
                errors.add(new ValidationError("userId", CommonErrorCodes.USER_DOES_NOT_EXIST));
                log.error("Login failed at Controller due to Invalid UserName :"+userName); 
            }    
        }
		
        if (userName != null) userName = userName.trim();
		if (password != null) password = password.trim();
						
        if (errors.size() == 0) {
            try {
            	
            	String deviceToken= DeviceTokenCookie.readFromRequest(request);
           	
            	//create USER object
				final BDLoginValueObject loginVO = VALIDATION.execute(userName, password, deviceToken, devicePrint, request, initiatedFromRegistration); 
    			
    			final HttpSession session = request.getSession();
    			// set the device print cookie if we have one
    			if (loginVO.getRsaDeviceToken() != null ) {
    				DeviceTokenCookie.writeToResponse(loginVO.getRsaDeviceToken(), response, BDConstants.BD_APPLICATION_ID);
    			}
    			
				switch (loginVO.getStepUpAction()) {

				case CHALLENGE:

					if (loginVO.getUserPasscodeDetailedInfo().isPasscodeLocked()) {
						
						errors.add(new ValidationError("PREVIOUSLY_LOCKED", BDErrorCodes.PREVIOUSLY_LOCKED));
						return errors;

					} else if (loginVO.getUserPasscodeDetailedInfo().isPasscodeCooling()) {
						errors.add(new ValidationError("COOLING", BDErrorCodes.COOLING,
								new Object[] { new SimpleDateFormat("MMM d, yyyy h:mm a 'ET'").format(loginVO.getUserPasscodeDetailedInfo().getCoolingTime()) }));
						return errors;
					}

					// To store loginVo object and devicePrint.
					BdPasscodeSession passcodeSession = new BdPasscodeSession(loginVO);
					session.setAttribute(SESSION_KEY, passcodeSession);
					session.setAttribute(BDConstants.USERID_KEY, userName);
					session.setAttribute(BDConstants.IS_TRANSITION, true);
					session.setMaxInactiveInterval(Environment.getInstance()
							.getIntNamingVariable("bd.passcode.inactiveSendPasscodeTimeoutInSeconds", null, 300));

					break;

				case ALLOW:

					BdAuthenticatedSessionInitialization.INSTANCE.execute(userName, request, response, loginVO);
					
					//Below attributes needed for challenge the user in other pages as part of STA.30
					if(!isUserExempted(loginVO.getPrincipal().getProfileId(), IPAddressUtils.getRemoteIpAddress(request))) {
					BdPasscodeSession bdPasscodeSession = new BdPasscodeSession(loginVO);
					request.getSession(true).setAttribute(SESSION_KEY, bdPasscodeSession);
					request.getSession().setAttribute(CommonConstants.CHALLENGE_PASSCODE_IND, true);
					}
					//END
					response.setHeader("Pragma", "no-cache");
					response.setHeader("Expires", "-1");
					response.setHeader("Cache-Control", "no-cache");

					break;

				case DENY:

					errors.add(new ValidationError("DENY", BDErrorCodes.DENY));
					return errors;


				default:

					throw new AssertionError("Unhandled stepup action");

				}
    			
			} catch (SecurityServiceException e) {

				errors.add(new ValidationError("",
						SecurityServiceExceptionHelper.getErrorCodeForCause(e, localSecurityExceptionMapping)));

				log.debug("Fail to login", e);
			}
        }
        return errors;
    }
		
	private boolean isUserExempted(final long profileId, final String ipAddress) {
		 return PasscodeSecurity.BD.isExemptUserProfile(profileId, ipAddress);
	 }
}
