package com.manulife.pension.bd.web.controller;


/**
 * This AuthorizationHandler allows all user to access the url (public url).
 * 
 * @author guweigu
 *
 */
public class PublicWebResourceAuthorizationHandler implements
		WebResourceAuthorizationHandler {

	public boolean isUserAuthorized(AuthorizationSubject subject, String url) {
		return true;
	}

	public boolean needsAuthentication() {
		return false;
	}

	public boolean hasPermission() {
		return false;
	}

}
