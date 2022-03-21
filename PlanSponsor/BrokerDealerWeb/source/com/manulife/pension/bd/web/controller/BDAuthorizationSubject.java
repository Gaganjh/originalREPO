package com.manulife.pension.bd.web.controller;

import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;

/**
 * The BD authroization Subject, includes the BobContext
 * @author guweigu
 *
 */
public class BDAuthorizationSubject extends AuthorizationSubject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BobContext bobContext;
	// actual logged in/ mimicking user 
	private BDUserProfile loggedinUserProfile;

	public BobContext getBobContext() {
		return bobContext;
	}
	private String userIdKey; 
	public void setBobContext(BobContext bobContext) {
		this.bobContext = bobContext;
	}

	public BDUserProfile getLoggedinUserProfile() {
		return loggedinUserProfile;
	}

	public void setLoggedinUserProfile(BDUserProfile loggedinUserProfile) {
		this.loggedinUserProfile = loggedinUserProfile;
	}
	
	
		public String getUserIdKey() {
				return userIdKey;
			}

			public void setUserIdKey(String userIdKey) {
				this.userIdKey = userIdKey;
			} 
		 

}
