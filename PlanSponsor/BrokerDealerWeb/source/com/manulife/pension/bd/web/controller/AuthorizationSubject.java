package com.manulife.pension.bd.web.controller;

import java.io.Serializable;

import com.manulife.pension.platform.web.controller.BaseUserProfile;

/**
 * The subject to be checked for authorization
 * 
 * @author guweigu
 *
 */
public class AuthorizationSubject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BaseUserProfile userProfile;
	// whether we should ignore mimic check
	private boolean ignoreMimic = false;
	
	public BaseUserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(BaseUserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public boolean isIgnoreMimic() {
		return ignoreMimic;
	}

	public void setIgnoreMimic(boolean ignoreMimic) {
		this.ignoreMimic = ignoreMimic;
	}
	
}
