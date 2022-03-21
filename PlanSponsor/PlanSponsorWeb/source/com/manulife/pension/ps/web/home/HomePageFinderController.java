package com.manulife.pension.ps.web.home;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
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

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseController;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.SecurityManager;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.login.ContractCookie;
import com.manulife.pension.ps.web.login.UserCookie;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWSecureHomePage;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.security.role.ExternalUser;
import com.manulife.pension.service.security.role.InternalUser;
import com.manulife.pension.service.security.role.JhtcOfficer;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.utility.SecurityConstants;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.util.TrueIPCookie;
import com.manulife.pension.util.log.LogUtility;

/**
 * HomePageFinderAction Action class
 *
 * @author Marcos Rogovsky
 */

@Controller
@RequestMapping(value = "/home")
public class HomePageFinderController extends BaseController
{
	public static final String EMPTY_VALUE = "";
	public static final String JSESSIONID = "JSESSIONID";
	
	public HomePageFinderController() {
		super(HomePageFinderController.class);
	}

	@ModelAttribute("psDynForm")
	public DynaForm populateForm() {
	     return new DynaForm(); // populates form for the first time if its null
	}
	
	private static final String SEARCH_CONTRACT_PAGE = "searchContractPage";
	private static final String SELECT_CONTRACT_PAGE = "selectContractPage";
	private static final String CONTRACT_FUNDS_PAGE = "contractFundsPage";
	private static final String TOOLS_PAGE = "toolsPage";
	private static final String SECURE_HOME_PAGE = "secureHomePage";
	private static final String LOGIN_PAGE = "loginPage";
	private static final String CHANGE_PASSWORD_PAGE = "changePasswordPage";
	private static final String UPDATE_PASSWORD_PAGE = "updatePasswordPage";
	private static final String WELCOME_PAGE = "welcomePage";
	
	public static final String REGISTERED_PARAM = "registered";	

	// The Direct URLs that the contract servlet recognizes and
	// allows redirect to it after the successful login
	static private final Set<String> DirectURLs = new HashSet<String>();
	
	static {
		DirectURLs.add("/do/mcdispatch/");
		DirectURLs.add("/do/mcdirect/message");
		DirectURLs.add("/do/messagecenter/personalizeEmail");
		DirectURLs.add("/do/messagecenter/detail");
		DirectURLs.add("/do/messagecenter/actMessage");
		DirectURLs.add("/do/messagecenter/history");
	}
    public static HashMap<String,String> forwards = new HashMap<String,String>();static{
        forwards.put(SEARCH_CONTRACT_PAGE,"/do/home/searchContractDetail/"); 
        forwards.put(SELECT_CONTRACT_PAGE,"/do/home/selectContractDetail/"); 
        forwards.put(CONTRACT_FUNDS_PAGE,"/do/investment/contractFundsReport/");
        forwards.put(TOOLS_PAGE,"/do/tools/toolsMenu/");
        forwards.put(SECURE_HOME_PAGE,"/do/home/homePage/"); 
        forwards.put(LOGIN_PAGE,"/do/tools/toolsMenu/");
        forwards.put(CHANGE_PASSWORD_PAGE,"/do/password/changePassword/");
        forwards.put(UPDATE_PASSWORD_PAGE,"/do/password/updatePassword/");
        forwards.put(WELCOME_PAGE,"/do/home/welcomePage/"); 
    }


	/**
	 * Returns whether the request is an URL that will redirect to it once login
	 */
	protected boolean canRedirect(UserProfile userProfile, String url) {
		/**
		 * Internal user allows redirect on all URLs
		 */
		if (userProfile.getPrincipal().getRole() instanceof InternalUser) {
			return true;
		}
		
		/**
		 * External user can only be redirected to certain URLs
		 */
		for (String directUrl: DirectURLs) {
			if (url.indexOf(directUrl) != -1) {
				return true;
			}
		}
		return false;
	}
	
	@RequestMapping(value = "/homePageFinder/" ,method = RequestMethod.GET) 
	public String doDefault(@Valid @ModelAttribute("psDynForm") DynaForm psDynForm, BindingResult bindingResult,HttpServletRequest request,
		 			             HttpServletResponse response)
					                throws IOException, ServletException, SystemException
	{
		//LoginPSValueObject loginVO = (LoginPSValueObject)request.getAttribute(SecurityConstants.LOGIN_VO);
		
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(LOGIN_PAGE);//if input forward not //available, provided default
        	}
        }
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		HttpSession session = request.getSession(false);
		
		// start changes for US44837
				boolean changePasswordFlag = Boolean.FALSE;
				if (null != session.getAttribute(Constants.PASSWORD_CHANGE_NEW_USER)) {
					changePasswordFlag = (Boolean) session.getAttribute(Constants.PASSWORD_CHANGE_NEW_USER);
				}
				String businessParamIndicator = null;
				if (null != session.getAttribute(Constants.PSW_SHOW_PASSWORD_METER_IND)) {
					businessParamIndicator = (String) session.getAttribute(Constants.PSW_SHOW_PASSWORD_METER_IND);
				}
				
				
	  // end changes for US44837
		
        

		if(userProfile != null && SecurityConstants.RESET_PASSWORD_STATUS.equals(userProfile.getPasswordStatus()))
		{	if ( logger.isDebugEnabled() )
				logger.debug("forwarding to " + CHANGE_PASSWORD_PAGE);

			return verifyAndFindForward(request, response,  userProfile, forwards.get(CHANGE_PASSWORD_PAGE));			
		}
		
		// start changes for PSW password 
		boolean isExternalUser = userProfile.getRole() instanceof ExternalUser;
		boolean isInternalUser = userProfile.getRole() instanceof InternalUser;
		
		if(userProfile != null){
			  if((changePasswordFlag && "INT".equals(businessParamIndicator) && isInternalUser)
					|| (changePasswordFlag && "EXT".equals(businessParamIndicator) && isExternalUser)
					|| (changePasswordFlag && "ALL".equals(businessParamIndicator))){
			 	if ( logger.isDebugEnabled()){
					logger.debug("forwarding to " + UPDATE_PASSWORD_PAGE);
			 	}
				return verifyAndFindForward(request, response,  userProfile, forwards.get(UPDATE_PASSWORD_PAGE));			
			 }
		}
		
		// end changes for PSW password changes

		// Check if there is any direct url to redirect to
		String directURL = StringUtils.trimToEmpty((String) session.getAttribute(Constants.DIRECT_URL_ATTR));
		// remove the Direct URL !!!
		session.removeAttribute(Constants.DIRECT_URL_ATTR);
		// If there is a DirectURL, redirect to it.

		if (!StringUtils.isEmpty(directURL)
				&& canRedirect(userProfile, directURL)) {
			return "redirect:"+directURL;
		}
		
		// This means that user has not selected the current contract yet
		if ( currentContract == null ) {
			
			// The user should have at least one contract, otherwise we wouldn't have let him login
			if (userProfile.getNumberOfContracts() > 20)
			{
				if ( logger.isDebugEnabled() )
					logger.debug("forwarding to " + SEARCH_CONTRACT_PAGE);

				return verifyAndFindForward(request, response, userProfile, forwards.get(SEARCH_CONTRACT_PAGE));
			}
			// Upated for TLN.2 and TLN.4
			else if (userProfile.getNumberOfContracts() > 1 || userProfile.getRole() instanceof ThirdPartyAdministrator )
			{
				if ( logger.isDebugEnabled() )
					logger.debug("forwarding to " + SELECT_CONTRACT_PAGE);

				return verifyAndFindForward(request, response,userProfile, forwards.get(SELECT_CONTRACT_PAGE));
			}
		}
		else
		{
			List errors = new ArrayList();
			
			if (userProfile != null && userProfile.getRole() != null && JhtcOfficer.ID.equalsIgnoreCase(StringUtils.trimToEmpty(userProfile.getRole().getRoleId()))) {
				boolean isPassiveTrusteeEnabled = ContractServiceDelegate.getInstance().isContractServiceFeatureAvailable(currentContract.getContractNumber(), "PTR", null);
				if(!isPassiveTrusteeEnabled){
					errors.add(new com.manulife.pension.util.content.GenericException(ErrorCodes.JHTC_NON_PASSIVE_TRUSTEE_CONTRACT_ACCESS));
					setErrorsInSession(request, errors);
				}
			}
			
			if (userProfile.getNumberOfContracts() > 20 && !errors.isEmpty())
			{
				if ( logger.isDebugEnabled() )
					logger.debug("forwarding to " + SEARCH_CONTRACT_PAGE);

				return verifyAndFindForward(request, response, userProfile, forwards.get(SEARCH_CONTRACT_PAGE));
			}
			else if ((userProfile.getNumberOfContracts() > 1 || userProfile.getRole() instanceof ThirdPartyAdministrator ) && !errors.isEmpty())
			{
				if ( logger.isDebugEnabled() )
					logger.debug("forwarding to " + SELECT_CONTRACT_PAGE);

				return verifyAndFindForward(request, response, userProfile, forwards.get(SELECT_CONTRACT_PAGE));
			}
			
			// set the contract cookie to the current contract
			response.addCookie(new ContractCookie(currentContract.getContractNumber()));
			response.addCookie(
					new UserCookie(userProfile.getPrincipal().getUserName(), userProfile.getPrincipal().getRole()));

			String ipAddress = IPAddressUtils.getRemoteIpAddress(request);
	    	String SITE_PROTOCOL = Environment.getInstance().getSiteProtocol();
	    	response.addCookie(new TrueIPCookie(ipAddress, SITE_PROTOCOL, Constants.TRUE_COOKIES_SITE_NAME));

			// I'm assuming the contract selected is the first one
			if (!currentContract.isContractAllocated())
			{
				if ( logger.isDebugEnabled() )
					logger.debug("forwarding to " + CONTRACT_FUNDS_PAGE);

				return verifyAndFindForward(request, response, userProfile, forwards.get(CONTRACT_FUNDS_PAGE));
			}
			else
			{
                // Contract status is :  Proposal Signed (PS), Details Complete (DC), 
                // Pending Contract Approval (PC), Contract Approved (CA)
				if (userProfile.isWelcomePageAccessOnly())  
				{
                    if (userProfile.getPrincipal().getRole().hasPermission(PermissionType.SELECTED_ACCESS))
                    {
                        if ( logger.isDebugEnabled() )
                            logger.debug("forwarding to " + TOOLS_PAGE);

                        return verifyAndFindForward(request, response, userProfile, forwards.get(TOOLS_PAGE));
                    } 
                    else 
                    {
                        if ( logger.isDebugEnabled() )
						logger.debug("forwarding to " + WELCOME_PAGE);

					    return verifyAndFindForward(request, response, userProfile, forwards.get(WELCOME_PAGE));
                    }
				}
				else if (currentContract.getStatus().equals(Contract.STATUS_ACTIVE_CONTRACT) ||
					currentContract.getStatus().equals(Contract.STATUS_CONTRACT_DISCONTINUED) ||
					currentContract.getStatus().equals(Contract.STATUS_CONTRACT_FROZEN))
				{
					if (userProfile.getPrincipal().getRole().hasPermission(PermissionType.SELECTED_ACCESS))
					{
						if ( logger.isDebugEnabled() )
							logger.debug("forwarding to " + TOOLS_PAGE);

						return verifyAndFindForward(request, response, userProfile, forwards.get(TOOLS_PAGE));
					}
					else
					{
						// THIS code was used to fix 008312, a change in requirement made this obsolete
						// and to fix 008767 we comment this out.
						Object obj = session.getAttribute(REGISTERED_PARAM);
						
						if ( logger.isDebugEnabled() )
							logger.debug("forwarding to " + SECURE_HOME_PAGE);
						if(obj != null)
						{
							// clean up
							session.removeAttribute(REGISTERED_PARAM);
						}

						return verifyAndFindForward(request, response, userProfile, forwards.get(SECURE_HOME_PAGE));
					}
				}
			}
		}

		// This should never happen
		if ( logger.isDebugEnabled() ) {
			logger.debug("forwarding to " + LOGIN_PAGE + " THIS SHOULD NOT HAPPEN.");
			SystemException se = new SystemException(new Exception("forwarding to " + LOGIN_PAGE + " THIS SHOULD NOT HAPPEN."), this.getClass().getName(), "doExecute", userProfile.toString());
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
		}

		//This should never happen, but if it does let's invalidate the session
		SessionHelper.invalidateSession(request, response);

		return "redirect:"+forwards.get(LOGIN_PAGE);
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
			forward = forwards.get(LOGIN_PAGE);
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
	 

	@Autowired
	private PSValidatorFWSecureHomePage psValidatorFWSecureHomePage;
	
	@InitBinder
	protected void initBinder(HttpServletRequest request,
				ServletRequestDataBinder  binder) {
		binder.addValidators(psValidatorFWSecureHomePage);
	}
	
	@RequestMapping(value ="/managesession",params = {"action=extendSession"}, method = { RequestMethod.GET })
	public String extendUserSession(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String responseText;
		logger.debug("Entry -------> extendUserSession");
		HttpSession session = request.getSession(false);
		if (session != null) {
			int oldMaxInactiveInterval = session.getMaxInactiveInterval();
    	    session.setMaxInactiveInterval(oldMaxInactiveInterval);
			responseText = "{\"result\" : \"Y\"}";
		} else {
			responseText = "{\"result\" : \"N\"}";
		}
		response.setContentType("application/json");
		response.getWriter().print(responseText);
		logger.info("Exit -------> extendUserSession");
		return null;
	}

	/**
	 * Expires user session asynchronously.
	 * 
	 * @param request
	 * @param response
	 * @return null, results are returned via response.
	 * @throws SystemException
	 * @throws IOException
	 */
	@RequestMapping(value ="/managesession",params = {"action=expireSession"}, method = { RequestMethod.GET })
	public String expireUserSession(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		logger.debug("Entry -------> processNextTime");
		HttpSession session = request.getSession(false);
		String responseText = StringUtils.EMPTY;
		try {
			if (session != null) {
				session.invalidate();
				responseText = "{\"result\" : \"Y\"}";
			}

	    	// Erase session ID from JSESSION cookie so it doesn't show on login page.
	    	Cookie deadCookie = new Cookie(JSESSIONID, EMPTY_VALUE);
			deadCookie.setPath("/");
			deadCookie.setMaxAge(0);
			response.setContentType("text/html");
			response.addCookie(deadCookie);
			
			// If the Calling_URL cookie exists set its value to blank
			if (CallingURLCookie.readFromRequest(request) != null) 
				CallingURLCookie.removeSelf(response);
		} catch (Exception e) {
			responseText = "{\"result\" : \"N\"}";
			logger.error("Error in terminating user session.", e);
		}
		response.setContentType("application/json");
		response.getWriter().print(responseText);
		logger.debug("Exit -------> expireUserSession");
		return null;
	}

}


