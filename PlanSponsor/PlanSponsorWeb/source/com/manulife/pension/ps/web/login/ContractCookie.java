package com.manulife.pension.ps.web.login;

import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.util.Environment;

/**
 * sets the cookie PSContract to the value of the current contract
 * that the user is working with
 * 
 * This cookie is set to last only for the browsers's lifetime
 */
public class ContractCookie extends Cookie {
	public static final String NAME = "PSContract";
	private static String SITE_PROTOCOL = Environment.getInstance().getSiteProtocol();

	public ContractCookie(int contractId) {
		super(NAME, String.valueOf(contractId));
		this.setComment("PlanSponsor Contract");
		this.setMaxAge(24*3600); 
		this.setPath("/");
		// Checks if current site protocol is https then sets Secure flag as true
        if (StringUtils.contains(SITE_PROTOCOL, CommonConstants.HTTPS)) {
        	this.setSecure(true);
        }else{
        	this.setSecure(false);
        }
		
	}
	
	

}

