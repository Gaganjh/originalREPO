package com.manulife.pension.ps.web.home;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.manulife.pension.ps.web.util.Environment;


/**
 * CallingURLCookie
 * Manages the Calling_URL cookie
 *
 * @author 	Marcos Rogovsky
 */

public class CallingURLCookie 
{

	public static final String NAME = "Calling_URL";
	public static final String PATH = "/";
	public static String COOKIE_DOMAIN = Environment.getInstance().getCookieDomain();

	/**
	 * writeToResponse
	 * Create or overwrite the cookie on the browser by sending it with the given
	 * response.  
	 * @param token the cookie value
	 * @param response the http response to be sent to the browser
	 */	
	public static void writeToResponse(String token, HttpServletResponse response) 
	{
		Cookie cookie = new Cookie(NAME, token);
		cookie.setMaxAge(-1);
		cookie.setPath(PATH);
		cookie.setDomain(COOKIE_DOMAIN);
		response.addCookie(cookie);
	}

	
	/**
	 * readFromRequest
	 * Returns the cookie as read from the Request object
	 */	
	public static String readFromRequest(HttpServletRequest request) 
	{
				
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
		if (cookieValue == null || cookieValue.length() < 10 || cookieValue.endsWith("//")) return null;
			
		int i = cookieValue.indexOf("//");
		if (i == -1) return null;
		cookieValue = cookieValue.substring(i+2);
		
		i = cookieValue.indexOf("/");
		if (i == -1) return null;
		cookieValue = cookieValue.substring(i);	
 				
		return cookieValue;
	}

	/**
	 * removeSelf
	 * Removes the cookie from response
	 */	
	public static void removeSelf(HttpServletResponse response) 
	{
		writeToResponse("", response);
	}
}
