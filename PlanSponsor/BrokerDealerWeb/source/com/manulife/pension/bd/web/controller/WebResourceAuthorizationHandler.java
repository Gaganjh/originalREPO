package com.manulife.pension.bd.web.controller;

import com.manulife.pension.exception.SystemException;

/**
 * The web resource authroization handler verify the authroization
 * of a subject for an URL
 * 
 * @author guweigu
 *
 */
public interface WebResourceAuthorizationHandler {
	
	abstract public boolean isUserAuthorized(AuthorizationSubject subject,
			String url) throws SystemException;

	/**
	 * If the authorization needs authentication
	 * @return
	 */
	abstract public boolean needsAuthentication();
	
	abstract public boolean hasPermission();
}
