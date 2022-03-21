package com.manulife.pension.ps.web.login;

import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.service.security.role.UserRole;

/**
 * This cookie holds the userid
 * 
 * It is set to never expire
 */
public class UserCookie extends Cookie {
	public static final String NAME = "PSUser";
	private static String SITE_PROTOCOL = Environment.getInstance().getSiteProtocol();
	
	public UserCookie(String userid) {
		this(userid, null);
	}
	
	public UserCookie(String userid, UserRole role) {
		super(NAME, null);
		String value = userid;
		if (role != null) {
			value += ":" + role.toString();
		}
		this.setValue(value);
		this.setComment("PlanSponsor User");
		this.setMaxAge(30*24*3600); //set to 30 days
		this.setPath("/");
		// Checks if current site protocol is https then sets Secure flag as true
        if (StringUtils.contains(SITE_PROTOCOL, Constants.HTTPS)) {
        	this.setSecure(true);
        }else{
        	this.setSecure(false);
        }
	}
	

}


