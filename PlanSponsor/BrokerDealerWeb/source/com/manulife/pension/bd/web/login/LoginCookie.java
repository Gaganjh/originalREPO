package com.manulife.pension.bd.web.login;

import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.platform.web.util.CommonEnvironment;

/**
 * Cookie to hold user name of last logged-in user.
 * If user had checked Remember username checkbox, this cookie will be persisted in users hard disk
 * 	else if user had unchecked Remember username checkbox, this cookie will be set to expire immediately.
 * 
 * @author balajna
 *
 */
public class LoginCookie extends Cookie {

	private static final String NAME = "UserNameFrw";
	
	private static String SITE_PROTOCOL = CommonEnvironment.getInstance().getSiteProtocol();
		
	/**
     * creates LoginCookie with username and sets maximum time for the cookie to expire
     * 
     * @param userName - UserId of logged-in user.
     * @param maxAge -lifetime of cookie
     */
	public LoginCookie(String userName, int maxAge) {
		super(NAME, userName);
	    this.setComment("Cookie to remember last logged-in user");
        this.setMaxAge(maxAge);
        this.setPath("/");
        
        // Checks if current site protocol is https then sets Secure flag as true
        if (StringUtils.contains(SITE_PROTOCOL, BDConstants.HTTPS)) {
        	this.setSecure(true);
        }else{
        	this.setSecure(false);
        }
	}
	
	public static String getLoginCookieName () {
		return NAME;
	}
	
	public static String userNameToEncrypt (String username) {
		return BDConstants.PREFIX_LOGIN_COOKIE + username;
	}
	
	public static String parseUserName (String username) {
		return StringUtils.substringAfterLast(username, BDConstants.PREFIX_LOGIN_COOKIE);
	}
	
}
