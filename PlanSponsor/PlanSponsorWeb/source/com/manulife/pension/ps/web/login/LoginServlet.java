package com.manulife.pension.ps.web.login;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Objects;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import java.util.Arrays;

import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.passcode.DeviceTokenCookie;
import com.manulife.pension.platform.web.passcode.PasscodeErrorMessage;
import com.manulife.pension.platform.web.validation.rules.UserNameRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.SignonCounter;
import com.manulife.pension.ps.web.pagelayout.LayoutBean;
import com.manulife.pension.ps.web.pagelayout.LayoutBeanRepository;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PsValidator1;
import com.manulife.pension.service.security.exception.MaxNumberOfUsersException;
import com.manulife.pension.service.security.exception.NoContractAssociatedWithAnySiteException;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.exception.WrongNYSiteException;
import com.manulife.pension.service.security.exception.WrongUSSiteException;
import com.manulife.pension.service.security.passcode.PasscodeSecurity;
import com.manulife.pension.service.security.passcode.PasscodeSecurity.StepUpAction;
import com.manulife.pension.service.security.passcode.RequestDetails;
import com.manulife.pension.service.security.valueobject.LoginPSValueObject;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.pension.validation.ValidationError;
import com.manulife.pension.validator.valueobject.ViolationVO;

/**
	This is entry point for PSW application and does both authentication and authorization including the two step.
*/
public class LoginServlet extends HttpServlet {
    
    private static final UserPasswordValidation VALIDATION = UserPasswordValidation.INSTANCE;
    private static final PsAuthenticatedSessionInitialization INITIALIZATION = PsAuthenticatedSessionInitialization.INSTANCE;

	private static final String LOGIN_LAYOUT_PAGE = "/WEB-INF/global/layout/publichomelayout.jsp";
	private static final String LOGIN_PAGEBEAN_ID = "/login/loginPage.jsp";
	public static final String USERNAME_PARAM = "userName";
	public static final String PASSWORD_PARAM = "password";
	private static String errorKey = null;
	private static final String SYSTEM_ERROR_PAGE = "/error.jsp";
	private static Logger logger = Logger.getLogger(LoginServlet.class);
	
	private static int maxNumberOfUsers;
	
	public void init(ServletConfig config) throws ServletException
	{
		super.init ( config );
		errorKey = Environment.getInstance().getErrorKey();
        maxNumberOfUsers = Environment.getInstance().getMaxNumberOfUsers();
        logger.debug("Value for maxNumberOfUsers:" + maxNumberOfUsers);
	}

	@SuppressWarnings({ "rawtypes" })
	public void service(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		String userName = request.getParameter(USERNAME_PARAM);
		String password = request.getParameter(PASSWORD_PARAM);
		final String devicePrint = request.getParameter(CommonConstants.DEVICE_PRINT);
		String deviceToken= DeviceTokenCookie.readFromRequest(request);
		
		try
		{
			Collection penErrors = PsValidator1.doValidatePenTestAction(null, request, CommonConstants.ERROR);
			if (penErrors != null && penErrors.size() > 0) {
				//
				// Report sole generic error only, skip the warnings.
				//
				for (Object p : penErrors) {
					if (p instanceof ViolationVO) {
						logger.debug("Ignoring warning/non-generic violation: " + p);
					}
					else if (p instanceof GenericException) {
						handleErrors(request, response, (GenericException) p);
						return;
					}
				}
			}

			// check if we are at the maxNumberOfUsers
			if (maxNumberOfUsers != 0 && SignonCounter.getCounter() >= maxNumberOfUsers) 
				throw new MaxNumberOfUsersException(this.getClass().getName(), "service", 
					 "Number of Current Users=" + SignonCounter.getCounter());
	
	
			/*
			 * If the username and password is saved as an attribute, the request is
			 * coming from the forgot your password or registration pages.
			 */
			if (userName == null && password == null && request.getSession(false) != null) {
				Object obj = null;
				obj = request.getSession(false).getAttribute(USERNAME_PARAM);
				if (obj != null) {
					userName = (String)obj;
					request.getSession(false).removeAttribute(USERNAME_PARAM);
				}
	
				obj = request.getSession(false).getAttribute(PASSWORD_PARAM);
	
				if (obj != null) {
					password = (String)obj;
					request.getSession(false).removeAttribute(PASSWORD_PARAM);
				}
	
			}
			
	
			if (userName != null) userName = userName.trim();
			if (password != null) password = password.trim();
	
			if (logger.isDebugEnabled())
			{
				logger.debug("userName="+userName);
				logger.debug("password="+password);
			}
	
			if (userName == null || password == null || userName.length() == 0 || password.length() == 0 )
			{
				// Set errors
				if (userName == null || userName.length() == 0) {
				    handleErrors(request, response, new GenericException(ErrorCodes.EMPTY_USERNAME));
				    return;
				}
	
				if (password == null || password.length() == 0) {
				    handleErrors(request, response, new GenericException(ErrorCodes.EMPTY_PASSWORD));
				    return;
				}
	
			} 
			
			RegularExpressionRule userNameRe= new RegularExpressionRule(CommonErrorCodes.USER_DOES_NOT_EXIST, UserNameRule.getInstance().getUserNameRuleRegExp()); 
			
			 if (!userNameRe.validate(userName)) {
				handleErrors(request, response, new GenericException(CommonErrorCodes.USER_DOES_NOT_EXIST));
				logger.error("Login failed at Controller due to Invalid UserName :"+userName);
				return;
			}
			
			//create USER object
			final LoginPSValueObject loginVO =
			        VALIDATION.execute(
			                userName,
			                password,
			                Environment.getInstance().getSiteLocation(),
			                new RequestDetails(request, devicePrint), deviceToken);
			
			            // US 44837 Make Service call to fetch value for Flag
						boolean changePasswordFlag = Boolean.FALSE;
						
						String businessParamIndicator = null;
						
						

				    	EnvironmentServiceDelegate envservice = null;
	
						

						try {
							if (loginVO != null) {
								SecurityServiceDelegate service = SecurityServiceDelegate.getInstance();
								envservice = EnvironmentServiceDelegate.getInstance();
								String profileId = String.valueOf(loginVO.getPrincipal().getProfileId());
								businessParamIndicator = envservice.getBusinessParam(Constants.PSW_SHOW_PASSWORD_METER_IND);
								if(!loginVO.getIsPasswordReset()) {
									changePasswordFlag = service.checkChangePasswordForNewUser(profileId,password);
									changePasswordFlag = changePasswordFlag || checkUsernamePasswordSequence(userName,password);
								}
								
								
								
							}
							
							
						} catch (SecurityServiceException sse) {
							if (logger.isDebugEnabled() ) logger.debug(sse.toString());
							
							throw new SystemException("com.manulife.pension.ps.web.home.LoginServlet.service"
									+ "Failed to get password change flag for new user "
									+ "due to service not found / down or any other ejb errors" + sse.toString());
							
						}
						
			// End changes for US 44837
			boolean challengeUser = Objects.isNull(request.getSession().getAttribute(Constants.CHALLENGE_PASSCODE_IND));
			// refresh session in order to protect against session fixation exploit
			SessionHelper.invalidateSessionKeepCookie(request);
			final HttpSession session = request.getSession();
			if (logger.isDebugEnabled() ) logger.debug(loginVO.getPrincipal().toString());
			
			if (loginVO.getRsaDeviceToken() != null ) {
				DeviceTokenCookie.writeToResponse(loginVO.getRsaDeviceToken(), response, Constants.PS_APPLICATION_ID);
			}
			
			if (loginVO.getStepUpAction() == StepUpAction.ALLOW || !challengeUser) {
			    INITIALIZATION.execute(userName, request, response, loginVO); 
			    // We need below attributes for STA 30, to challenge the user in other pages.
			    if(!isUserExempted(loginVO.getPrincipal().getProfileId(),  IPAddressUtils.getRemoteIpAddress(request))) {
					session.setAttribute(PsAuthenticatedSessionInitialization.USERID_KEY, userName);
					if(challengeUser) { //to avoid overwriting from forgot password flow
						session.setAttribute(Constants.CHALLENGE_PASSCODE_IND, true);
					}else {
						session.setAttribute(Constants.CHALLENGE_PASSCODE_IND, challengeUser);
						session.setAttribute(Constants.IS_CHALLENGED_IN_FORGOT_PASSWORD_FLOW, true);
					}
					PsPasscodeSession psSession = new PsPasscodeSession(loginVO);
					psSession.setPasswordChangeOrUpdateRequired(changePasswordFlag);
					session.setAttribute(CommonConstants.PASSCODE_SESSION_KEY, psSession);
					//end
				}else {
					session.setAttribute(Constants.CHALLENGE_PASSCODE_IND, false);
				}

			    response.setHeader("Pragma","no-cache");
	            response.setHeader("Expires","-1");
	            response.setHeader("Cache-Control","no-cache");
				if (loginVO.isPhoneNumberCollection()) {
					session.setAttribute(Constants.PHONE_COLLECTION, true);
					response.sendRedirect("/do/phoneCollection/");
				} else {
					request.getSession(false).setAttribute(Constants.PASSWORD_CHANGE_NEW_USER, changePasswordFlag);
					request.getSession(false).setAttribute(Constants.PSW_SHOW_PASSWORD_METER_IND, businessParamIndicator);
					response.sendRedirect("/do/home/homePageFinder/");
				}
				
	            
			} else if (loginVO.getStepUpAction() == StepUpAction.DENY) {

				handleErrors(request, response, new ValidationError("DENY", ErrorCodes.DENY));
				return;
			} else {
				if (loginVO.getUserPasscodeDetailedInfo().isPasscodeLocked()) {

					handleErrors(request, response,
							new PasscodeErrorMessage.PasscodeErrorMap.Builder()
							.add(PasscodeErrorMessage.LOCKED, ErrorCodes.PASSCODE_LOCKED_USA,
									ErrorCodes.PASSCODE_LOCKED_NY)
							.build().withArguments(PasscodeErrorMessage.LOCKED));
					return;

				} else if (loginVO.getUserPasscodeDetailedInfo().isPasscodeCooling()) {
					handleErrors(request, response, new PasscodeErrorMessage.PasscodeErrorMap.Builder()
							.add(PasscodeErrorMessage.COOLING, ErrorCodes.PASSCODE_COOLING_USA,
									ErrorCodes.PASSCODE_COOLING_NY)
							.build()
							.withArguments(PasscodeErrorMessage.COOLING, new SimpleDateFormat("MMM d, yyyy h:mm a z")
									.format(loginVO.getUserPasscodeDetailedInfo().getCoolingTime())));
					return;
				}
				
				// set the Direct URL parameter into the attribute
				String directUrl = StringUtils.trimToEmpty(request
						.getParameter(Constants.DIRECT_URL_ATTR));
				session.setAttribute(Constants.DIRECT_URL_ATTR, directUrl);
				session.setAttribute(PsAuthenticatedSessionInitialization.USERID_KEY, userName);
				session.setAttribute(CommonConstants.IS_TRANSITION, true);
				PsPasscodeSession psSession = new PsPasscodeSession(loginVO);
				psSession.setPasswordChangeOrUpdateRequired(changePasswordFlag);
				session.setAttribute(CommonConstants.PASSCODE_SESSION_KEY, psSession);
				session.setAttribute(Constants.PASSWORD_CHANGE_NEW_USER, changePasswordFlag);
				session.setAttribute(Constants.PSW_SHOW_PASSWORD_METER_IND, businessParamIndicator);
				session.setMaxInactiveInterval(
						Environment.getInstance().getIntNamingVariable(
								"passcode.inactiveSessionTimeout",
								null,
								300));
				
				response.setHeader("Pragma","no-cache");
				response.setHeader("Expires","-1");
				response.setHeader("Cache-Control","no-cache");
				response.sendRedirect("/do/passcodeTransition/");

			}
			
		}
		catch (SystemException e)
		{
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID,e);

			request.setAttribute("errorCode", "1099");
			request.setAttribute("uniqueErrorId", e.getUniqueId());
			SessionHelper.invalidateSession(request, response);

			// forward to Error Page
			getServletContext().getRequestDispatcher(SYSTEM_ERROR_PAGE).forward(request, response);
			return;
		}
		catch(WrongUSSiteException e)
		{
			// The following if statement is added in order to accomadate TPA Re-write changes.
			// It is changed accoring to SPR.59 and MPR.598
			String [] wrongSiteErrorMsgArgs = new String[2];
			if ( e.isTpaUser() )
				wrongSiteErrorMsgArgs[0] = Environment.getInstance().getTPAOtherSiteMarketingURL();
			else 
				wrongSiteErrorMsgArgs[0] = Environment.getInstance().getPSOtherSiteMarketingURL();
				
			wrongSiteErrorMsgArgs[1] = wrongSiteErrorMsgArgs[0]; 
			handleErrors(request, response, new GenericException(Integer.parseInt(e.getErrorCode()),wrongSiteErrorMsgArgs));
			return;			
		}
		catch(WrongNYSiteException e)
		{
			// The following if statement is added in order to accomadate TPA Re-write changes.
			// It is changed accoring to SPR.59 and MPR.598
			String [] wrongSiteErrorMsgArgs = new String[2];
			if ( e.isTpaUser() )
				wrongSiteErrorMsgArgs[0] = Environment.getInstance().getTPAOtherSiteMarketingURL();
			else 
				wrongSiteErrorMsgArgs[0] = Environment.getInstance().getPSOtherSiteMarketingURL();
				
			wrongSiteErrorMsgArgs[1] = wrongSiteErrorMsgArgs[0]; 
			handleErrors(request, response, new GenericException(Integer.parseInt(e.getErrorCode()),wrongSiteErrorMsgArgs));
			return;			
		}
		catch(SecurityServiceException e)
		// These exceptions include no contract assigned, user not found, password doesn't match,
		// user is locked .etc.
		{
			// The following if statement is added in order to accomadate TPA Re-write changes.
			// It is changed accoring to SPR.59 and MPR.598
		    if (e instanceof NoContractAssociatedWithAnySiteException)
			{
				String [] noContractMsgArgs = {Environment.getInstance().getGeneralCARPhoneNumber()};
				handleErrors(request, response, new GenericException(Integer.parseInt(e.getErrorCode()),noContractMsgArgs));
			}
		    
			else
				handleErrors(request, response, new GenericException(Integer.parseInt(e.getErrorCode())));
			
			return;
		}

	}
/*	 @Autowired
	   private PSValidatorFWError  psValidatorFWError;

	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWError);
	}
*/	private void handleErrors(HttpServletRequest request, HttpServletResponse response, final GenericException... errors)
	throws IOException, ServletException {
	    
		SessionHelper.invalidateSession(request, response);
		request.setAttribute(errorKey, Arrays.asList(errors));
		LayoutBean bean = LayoutBeanRepository.getInstance().getPageBean(LOGIN_PAGEBEAN_ID);
		request.setAttribute(Constants.LAYOUT_BEAN, bean);
		getServletContext().getRequestDispatcher(LOGIN_LAYOUT_PAGE).forward(request, response);
	}

	private boolean isUserExempted(final long profileId, final String ipAddress) {
			return PasscodeSecurity.PS.isExemptUserProfile(profileId, ipAddress);
	}

	private Boolean checkUsernamePasswordSequence(String uname, String pwd){
		  Boolean contains=false;
		  
		  for (String seq: uname.split("/[\\@\\-\\.\\_]/g")){ 
		     if(pwd.contains(seq)|| pwd.contains(seq.toUpperCase())|| pwd.contains(seq.toLowerCase())){
		        contains=true;
		        break;
		     }
		  }
		  
		  if(pwd.contains(uname)){
			  contains = true;
		  }
		  return contains;
		}
	
}