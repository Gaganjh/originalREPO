package com.manulife.pension.bd.web.home;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.DynaForm;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.login.CallingURLCookie;
import com.manulife.pension.bd.web.login.LoginCookie;
import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.userprofile.LoginStatus;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.LoginCookieEncryptorDecryptor;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.util.BaseEnvironment;
/**
 * Home page dispatcher to either public or secured home page
 * 
 * @author guweigu
 * 
 */
@Controller


public class HomePageDispatchController extends BDController {
	
	
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	public static String SECURED_HOME= "securedHome";
	public static String PUBLIC_HOME = "publicHome";
	static{
		forwards.put(SECURED_HOME,"/do/secured/");
		forwards.put("carHome","redirect:/do/usermanagement/search");
		forwards.put(PUBLIC_HOME,"/home/public_home.jsp");
		}

	
	private static EnumMap<BDUserRoleType, ControllerRedirect> HomePageMap = new EnumMap<BDUserRoleType, ControllerRedirect>(
			BDUserRoleType.class);
	
	// added for Fundcheck to redirect the user to fundcheck page while accessing the link in FRW email message.
	static private final Set<String> DirectURLs = new HashSet<String>();
	
	static {
		HomePageMap.put(BDUserRoleType.CAR, new ControllerRedirect(
				URLConstants.UserManagement));
		HomePageMap.put(BDUserRoleType.SuperCAR, new ControllerRedirect(
				URLConstants.UserManagement));
		DirectURLs.add("/do/messagecenter/actMessage");
		DirectURLs.add("/do/fundcheck/fundCheckL2");
		DirectURLs.add("/do/fundcheck/fundCheckInternal");
		DirectURLs.add("/do/fundcheck/fundCheckL1");
	}

	public HomePageDispatchController() {
		super(HomePageDispatchController.class);
	}
/* code added for MarketWatch -- Starts */
	
	@RequestMapping(value="/home/", params={"task=getSessionId"},method = {RequestMethod.GET})
		public  String doGetSessionId(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException{
		
		BaseEnvironment baseEnvironment = new BaseEnvironment();
		String marketURL = baseEnvironment.getNamingVariable(BDConstants.MARKET_WATCH_SERVICE_URL, null);
			try{
			URL url = new URL (marketURL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream content = (InputStream)connection.getInputStream();
                      BufferedReader in   = 
                new BufferedReader (new InputStreamReader (content));
            String line;
            StringBuffer responseText = new StringBuffer();
            while ((line = in.readLine()) != null) {
            	responseText.append(line);
                
            }
			
		response.setContentType("text/json");
		PrintWriter out;
		try {
			out = response.getWriter();
		} catch (IOException exception) {
			throw new SystemException(exception,
					"IOException occured while checking the status of this RequestId "
							);
		}

		out.print(responseText);
		out.flush();
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doGetSessionId()in HomePageDispatchController.");
		}
		}catch (Exception e){
			logger.error("unable to parse response from the url"+e);
		}
		return null;
	}
	/* code added for MarketWatch -- Ends */
	 @RequestMapping( value ="/home/" ,method =  {RequestMethod.GET}) 
	 public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException {	
	    BDSessionHelper.removeBOBTabSelectionFromSession(request);
	    // sonarcube comments
	    HttpSession session = request.getSession(false);
	    if(null == session){
	    	session = request.getSession();
	    }
	    String updatePwdBackBtnClick = null;
		if(null!= session && null != session.getAttribute("isUpdatePasswordBackClick")){
		 updatePwdBackBtnClick = (String) session.getAttribute("isUpdatePasswordBackClick");
		}
		
		String updatePwdSuccInd = null;
		if(null!= session && null != session.getAttribute("isUpdatePasswordSuccessInd")){
		updatePwdSuccInd = (String) session.getAttribute("isUpdatePasswordSuccessInd");
		}
		// end sonarcube comments
		if(null != updatePwdBackBtnClick && 
				"true".equals(updatePwdBackBtnClick)){
			BDSessionHelper.invalidateSession(request, response, false);
			return forwards.get(PUBLIC_HOME);
		}
		// set the Direct URL parameter into the attribute
		String directUrl = StringUtils.trimToEmpty((String)request
				.getAttribute(BDConstants.DIRECT_URL_ATTR));
		request.setAttribute(BDConstants.DIRECT_URL_ATTR, directUrl);
		String username = StringUtils.EMPTY;
		String rememberMe = StringUtils.EMPTY;
		if (!StringUtils.isEmpty(directUrl)) {
			session.setAttribute(BDConstants.DIRECT_URL_ATTR, directUrl);
		}
		BDUserProfile profile = BDSessionHelper.getUserProfile(request);
		if (profile == null
				|| !LoginStatus.FullyLogin.equals(profile.getLoginStatus())) {
			
			String encryptedUserName = HomePageHelper.getCookieValue(request.getCookies(), LoginCookie.getLoginCookieName());
			if(StringUtils.isNotEmpty(encryptedUserName)) {
				username = LoginCookie.parseUserName(LoginCookieEncryptorDecryptor.decrypt(encryptedUserName));
				rememberMe = "on";
			}
			request.setAttribute(BDConstants.LOGIN_NAME_FROM_COOKIE_ATTRIBUTE, username);
			request.setAttribute(BDConstants.LOGIN_REMEBER_ME_ATTRIBUTE, rememberMe);
			if(null != profile && "true".equals(updatePwdSuccInd)){
				profile.setLoginStatus(LoginStatus.FullyLogin);
				return forwards.get(SECURED_HOME);
			}
			BDSessionHelper.invalidateSession(request, response, false);
			return forwards.get(PUBLIC_HOME);
		} else {
			BDUserRoleType roleType = profile.getBDPrincipal().getBDUserRole()
					.getRoleType();
			ControllerRedirect forward = HomePageMap.get(roleType);
			// Check if there is any direct url to redirect to
			String directURL = StringUtils.trimToEmpty((String) session.getAttribute(BDConstants.DIRECT_URL_ATTR));
			// remove the Direct URL !!!
			session.removeAttribute(BDConstants.DIRECT_URL_ATTR);
			// If there is a DirectURL, redirect to it.

			if (!StringUtils.isEmpty(directURL) && canRedirect(directURL)) {
				return (directURL);
			}
			if (forward == null) {
				return forwards.get(SECURED_HOME);
			} else {
				return forward.getPath();
			}
		}
	}
	
	/**
	 * Returns whether the request is an URL that will redirect to it once login
	 */
	private boolean canRedirect(String url) {

		for (String directUrl: DirectURLs) {
			if (url.indexOf(directUrl) != -1) {
				return true;
			}
		}
		return false;
	}
	@RequestMapping(value ="/home/managesession",params = {"action=extendSession"}, method = { RequestMethod.GET})
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
    @RequestMapping(value ="/home/managesession",params = {"action=expireSession"}, method = { RequestMethod.GET})
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
          Cookie deadCookie = new Cookie("JSESSIONID", "");
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
