package com.manulife.pension.bd.web.controller;


/**
 * This handler guards all the inactive urls
 * 
 * @author guweigu
 *
 */
public class InactiveWebResourceAuthorizationHandler implements
		WebResourceAuthorizationHandler {

	public boolean isUserAuthorized(AuthorizationSubject subject, String url) {
		return false;
	}

	public boolean needsAuthentication() {
		return false;
	}

	public boolean hasPermission() {
		return false;
	}
}
