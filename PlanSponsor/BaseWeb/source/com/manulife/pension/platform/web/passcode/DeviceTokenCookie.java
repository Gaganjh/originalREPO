package com.manulife.pension.platform.web.passcode;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.util.CommonEnvironment;

/**
 * DeviceTokenCookie
 * Manages the DeviceToken cookie
 * 
 * This should be refactored (along with the other *Cookie classes to abstract common functionality
 *
 */
public class DeviceTokenCookie  {

	public static final String NAME = "deviceToken";
	public static final String PATH = "/";
	
	private static final String FRW_RSA_SIGNIN_COOKIE_EXPIRED_IN_DAYS= "FRW_RSA_SIGNIN_COOKIE_EXPIRED_IN_DAYS";
	
	
	/**
	 * Initialize the cookie domain.
	 */
	public static final String COOKIE_DOMAIN = CommonEnvironment.getInstance().getCookieDomain();
	
	
	private static final int FRW_RSA_DEVICE_TOKEN_COOKIE_MAX_AGE = getMaxAge(CommonConstants.BD_APPLICATION_ID);
	
	
	/**
	 * private constructor to prevent initialization of utility class accidentally.
	 */
	private DeviceTokenCookie() {
		
	}

	/**
	 * writeToResponse
	 * Create or overwrite the cookie on the browser by sending it with the given
	 * response.  
	 * @param token the cookie value
	 * @param response the http response to be sent to the browser
	 */	
	public static void writeToResponse(String token, HttpServletResponse response, String applicationId) {
		Cookie cookie = new Cookie(NAME, token);
		cookie.setMaxAge(
				CommonConstants.BD_APPLICATION_ID.equals(applicationId) ? FRW_RSA_DEVICE_TOKEN_COOKIE_MAX_AGE : -1);
		cookie.setPath(PATH);
		cookie.setDomain(COOKIE_DOMAIN);
		response.addCookie(cookie);
	}

	
	/**
	 * ReadFromRequest.
	 * 
	 * Returns the cookie as read from the Request object
	 * @param request
	 * @return cookie Value
	 */
	public static String readFromRequest(HttpServletRequest request) {
				
		String cookieValue = null;
		Cookie[] cookies = request.getCookies();
		
		if (cookies != null){
			for (int i = 0; i < cookies.length; i++) 
			{
				if (NAME.equals(cookies[i].getName())) 
				{
					cookieValue = cookies[i].getValue();
					break;
				}
			}
		}
		
		if (cookieValue == null || cookieValue.length() == 0 || cookieValue.endsWith("//") ) {
			return null;
		}
			
		return cookieValue;
	}

	/**
	 * Removes the cookie from response
	 * @param response
	 */
	public static void removeSelf(HttpServletResponse response) {
		writeToResponse("", response, null);
	}
	
	private static int getMaxAge(String applicationId) {
		int maxAge = -1;
		if(CommonConstants.BD_APPLICATION_ID.equals(applicationId)) {
			try {
				Integer parmMaxAge = Integer.parseInt(EnvironmentServiceDelegate.getInstance(applicationId)
						.getBusinessParam(FRW_RSA_SIGNIN_COOKIE_EXPIRED_IN_DAYS));
				if(parmMaxAge > 0 ) {
					maxAge =  parmMaxAge * 24 * 60 * 60; 
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return maxAge;
	}
}
