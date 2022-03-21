package com.manulife.pension.platform.web.passcode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseController;
import com.manulife.pension.platform.web.passcode.PasscodeErrorMessage.PasscodeErrorMap;
import com.manulife.pension.platform.web.util.BaseSessionHelper;
import com.manulife.pension.service.security.passcode.PasscodeChannel;
import com.manulife.pension.service.security.passcode.PasscodeGenerationResult;
import com.manulife.pension.service.security.passcode.PasscodeVerificationResult;
import com.manulife.pension.service.security.passcode.RequestDetails;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.pension.validator.ValidateCatalogLaunch;

public abstract class ValidatePasscodeController<LoginVo> extends BaseController {
    
    private final String appIdForAppLog;
    private final ValidateCatalogLaunch injectionValidation;
    private final PasscodeVerification<LoginVo> verification;
    private final AuthenticatedSessionInitialization<LoginVo> initialization;
    private final PasscodeErrorMap errorMap;
    private final String PASSWORD_CHANGE_NEW_USER = "passwordChangeNewUser";
    private final String PSW_SHOW_PASSWORD_METER_IND = "PSW_SHOW_PASSWORD_METER_IND";
    private final String FRW_SHOW_PASSWORD_METER_IND = "FRW_SHOW_PASSWORD_METER_IND";
    
    public ValidatePasscodeController(
            final Class<?> clazz,
            final String appIdForAppLog,
            final ValidateCatalogLaunch injectionValidation,
            final PasscodeVerification<LoginVo> verification,
            final AuthenticatedSessionInitialization<LoginVo> initialization,
            final PasscodeErrorMap errorMap) {
        super(clazz);
        this.appIdForAppLog = appIdForAppLog;
        this.injectionValidation = injectionValidation;
        this.verification = verification;
        this.initialization = initialization;
        this.errorMap = errorMap;
    }
    
    public String doExecute(
            final ActionForm form,
            final HttpServletRequest request,
            final HttpServletResponse response)
    throws IOException, ServletException, SystemException {
        
        final String action = getSubmitAction(request, form);
        
        if ("Cancel".equals(action)) {
        	HttpSession session = request.getSession(false);
        	if(session != null){
        		PasscodeSession<?> savedPasscodeSession = (PasscodeSession<?>) (session).getAttribute(CommonConstants.PASSCODE_SESSION_KEY);
    			if (savedPasscodeSession != null) {
    				verification.clearPasscodeSession(session);
    				BaseSessionHelper.invalidateSessionKeepCookie(request);
    			}
        	}
			
            return forwardToCancel(request, response);
            
        } else if ("Resend".equals(action)) {
            
            final HttpSession session = request.getSession(false);
            
            final PasscodeGenerationResult result;
            try {
                
                result = verification.resendPasscode(session, new RequestDetails(request,verification.getPasscodeSession(session).getDevicePrint()));
                
            } catch (final Exception e) {
                
                verification.clearPasscodeSession(session);
                
                LogUtility.logSystemException(
                        appIdForAppLog,
                        e instanceof SystemException
                        ? (SystemException) e
                        : new SystemException(e, e.getMessage()));
                
                setErrorInRequest(request, PasscodeErrorMessage.SYSTEM_ERROR_AT_RESEND);        
                return forwardToCancel(request, response);
                
            }
            
            switch (result.getResult()) {
                
                case PREVIOUSLY_LOCKED:
                    setErrorInRequest(request, PasscodeErrorMessage.LOCKED);
                    return forwardTo("locked", request);
                    
                case COOLING:
                    setErrorInRequest(
                            request,
                            PasscodeErrorMessage.COOLING,
                            new SimpleDateFormat("MMM d, yyyy h:mm a 'ET'").format(
                                    result.getCoolingTimestamp()));
                    return forwardTo("cooling", request);
                    
                case SUCCESS:
                case EMAIL_WAIT:
                    return forwardTo("resend", request);
                    
                default:
                    throw new AssertionError("Unhandled result " + result);
                    
            }
            
        } else if ("Verify".equals(action)) {
            
            HttpSession session = request.getSession(false);
            
            if (form == null || !(form instanceof PasscodeForm) || session == null ) {
                
                return forwardToCancel(request, response);
                
            }
            
            final String passcode =
                    StringUtils.substring(
                            StringUtils.trimToNull(((PasscodeForm) form).getPasscode()),
                            0,
                            32);
            
            if (passcode == null) {
                
                setErrorInRequest(request, PasscodeErrorMessage.BLANK_PASSCODE);
                verification.setHelpfulHintPasscodeTS(session, verification.getPasscodeSession(session).getProfileId());
                return forwardTo("default", request);
                
            }
            
            if (! injectionValidation.validateSanitizeCatalogedFormFields(form, null, null)) {
                
                return forwardToCancel(request, response);
                
            }
            
            
            
            try {
                
                final PasscodeVerificationResult result;
                try {
                    
					result = verification.verify(session, null, passcode, new RequestDetails(request, verification.getPasscodeSession(session).getDevicePrint()));

					if (result.getToken() != null) {
						DeviceTokenCookie.writeToResponse(result.getToken().toString(), response,
								getApplicationFacade(request).getApplicationId());
					}
                    
                } catch (final Exception e) {
                    
                    verification.clearPasscodeSession(session);
                    
                    LogUtility.logSystemException(
                            appIdForAppLog,
                            e instanceof SystemException
                            ? (SystemException) e
                            : new SystemException(e, e.getMessage()));
                    
                    setErrorInRequest(request, PasscodeErrorMessage.SYSTEM_ERROR_AT_RESEND);
                    return forwardToCancel(request, response);
                    
                }
                
                switch (result.getResult()) {
                    
                    case RETRY:
                    	final PasscodeChannel channel = verification.getPasscodeSession(session).getPasscodeChannel(); 
                    	if (channel.equals(PasscodeChannel.SMS) || channel.equals(PasscodeChannel.VMOBILE) || channel.equals(PasscodeChannel.VPHONE)) {
                    		setErrorInRequest(
                                    request,
                                    PasscodeErrorMessage.RETRY_SMS,
                                    Integer.toString(Math.max(0, result.getRemainingAttempts())) +
                                    " " +
                                    (result.getRemainingAttempts() == 1
                                    ? "attempt"
                                    : "attempts"));
                    		 return forwardTo("retry_sms", request);
                    	}
                    		setErrorInRequest(
                                    request,
                                    PasscodeErrorMessage.RETRY,
                                    Integer.toString(Math.max(0, result.getRemainingAttempts())) +
                                    " " +
                                    (result.getRemainingAttempts() == 1
                                    ? "attempt"
                                    : "attempts"));
                    		 return forwardTo("retry", request);
                      
                        
                    case EXHAUSTED:
                        setErrorInRequest(request, PasscodeErrorMessage.LOCKED);
                        return forwardTo("failure", request);
                        
                    case PREVIOUSLY_LOCKED:
                        setErrorInRequest(request, PasscodeErrorMessage.LOCKED);
                        return forwardTo("locked", request);
                        
                    case EXPIRED:
                    	switch (verification.getPasscodeSession(session).getPasscodeChannel()) {
                    	case SMS:
                    		setErrorInRequest(request, PasscodeErrorMessage.EXPIRED_SMS);
                    		break;
                    	default:
                    		setErrorInRequest(request, PasscodeErrorMessage.EXPIRED);
                    		break;
                    	}
                    	return forwardTo("expired", request);
                        
                    case MATCH:
                        
                        final LoginVo loginVo = verification.getPasscodeSession(session).getLoginInformation();
                        final String userName = (String) session.getAttribute(AuthenticatedSessionInitialization.USERID_KEY);
                        
                        verification.clearPasscodeSession(session);
                        request.getSession(false).removeAttribute(AuthenticatedSessionInitialization.USERID_KEY);
                        
                        // FRW US
                    	boolean changePasswordFlag = Boolean.FALSE;
                		if(null != request.getSession(false).getAttribute("changePasswordFlag")){
                			changePasswordFlag = (Boolean)request.getSession(false).getAttribute("changePasswordFlag");
                		}
                		
                		String businessParamIndicatorFRW = null;
                		if (null != request.getSession(false).getAttribute(FRW_SHOW_PASSWORD_METER_IND)) {
                			businessParamIndicatorFRW = (String) request.getSession(false).getAttribute(FRW_SHOW_PASSWORD_METER_IND);
                		}
                		
                		boolean businessParamFlagFRW = ("EXT".equals(businessParamIndicatorFRW)|| "ALL".equals(businessParamIndicatorFRW)) 
                				&& changePasswordFlag ;
                		// END of FRW US 
                		
                		// PSW US
                    	boolean changePasswordFlagPSW = Boolean.FALSE;
                		if(null != session.getAttribute(PASSWORD_CHANGE_NEW_USER)){
                			changePasswordFlagPSW = (Boolean)session.getAttribute(PASSWORD_CHANGE_NEW_USER);
                		}
                		
                		
                		String businessParamIndicator = null;
                		if (null != session.getAttribute(PSW_SHOW_PASSWORD_METER_IND)) {
                			businessParamIndicator = (String) session.getAttribute(PSW_SHOW_PASSWORD_METER_IND);
                		}
                		
                		boolean businessParamFlagPSW = ("EXT".equals(businessParamIndicator)|| "ALL".equals(businessParamIndicator)) 
                				&& changePasswordFlagPSW ;
                		
                		// END of PSW US 
                        // refresh session in order to protect against session fixation exploit
                		BaseSessionHelper.invalidateSessionKeepCookie(request);
                		session = request.getSession();
            			
                		initialization.execute(userName, request, response, loginVo);
            			response.setHeader("Pragma", "no-cache");
                        response.setHeader("Expires", "-1");
                        response.setHeader("Cache-Control", "no-cache");
                        if(businessParamFlagFRW || businessParamFlagPSW){
                        	return forwardTo("updatePassword", request);
                        }else{
                            return forwardTo("success", request);
                        }
                        
                    default:
                        throw new AssertionError("Unhandled result " + result);
                        
                }
                
            } catch (final SystemException e) {
                
                verification.clearPasscodeSession(session);
                LogUtility.logSystemException(appIdForAppLog, e);
                request.setAttribute("errorCode", "1099");
                request.setAttribute("uniqueErrorId", e.getUniqueId());
                BaseSessionHelper.invalidateSession(request, response);
                
                // forward to Error Page
                return SYSTEM_ERROR_PAGE;
                
            }
            
        } else {
        	
			HttpSession session = request.getSession(false);
			if (session != null) {
				PasscodeSession savedPasscodeSession = (PasscodeSession) (session).getAttribute(CommonConstants.PASSCODE_SESSION_KEY);
				if (session.getAttribute(CommonConstants.IS_TRANSITION) == null && savedPasscodeSession != null) {
					return forwardTo("default", request);
				} else {
					verification.clearPasscodeSession(session);
					BaseSessionHelper.invalidateSessionKeepCookie(request);
					return forwardToCancel(request, response);
				}
			}
			
			return forwardToCancel(request, response);
            
        }
        
        
    }
    
    private void setErrorInRequest(final HttpServletRequest request, final PasscodeErrorMessage error, final String... args) {
        
        setErrorsInRequest(request, Arrays.asList(new GenericException[] { errorMap.withArguments(error, args) }));
        
    }
    
    private static String forwardTo(final String forwardName, final HttpServletRequest request) {
        
        request.setAttribute("PASSCODE_FORWARD_KEY", forwardName);
        return forwardName;
        
    }
    
    private static String forwardToCancel(final HttpServletRequest request, final HttpServletResponse response) {
        
        BaseSessionHelper.invalidateSession(request, response);
        return "cancel";
        
    }
    
}