package com.manulife.pension.bd.web.controller;

/**
 * This AuthorizationHandler allows all user to access the url (public url).
 * 
 * @author guweigu
 *
 */
public class StepupPageAuthorizationHandler implements
		WebResourceAuthorizationHandler {

	public boolean isUserAuthorized(AuthorizationSubject subject, String url) {

		String userIdKey =  ((BDAuthorizationSubject) subject).getUserIdKey();

		if (null != userIdKey) {

			if (userIdKey.isEmpty()) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	public boolean needsAuthentication() {
		return false;
	}

	public boolean hasPermission() {
		return false;
	}

}
