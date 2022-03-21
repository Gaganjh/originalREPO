/**
 * 
 */
package com.manulife.pension.bd.web.controller;

import org.apache.commons.lang3.StringUtils;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.platform.web.controller.BaseUserProfile;

/**
 * @author narintr
 *
 */
public class BDUserRolesAuthorizationHandler implements WebResourceAuthorizationHandler {

	private String restrictedRoles;

	public String getRestrictedRoles() {
		return restrictedRoles;
	}

	public void setRestrictedRoles(String restrictedRoles) {
		this.restrictedRoles = restrictedRoles;
	}

	@Override
	public boolean isUserAuthorized(AuthorizationSubject subject, String url){

		if (subject == null || subject.getUserProfile() == null) {
			return true;
		}
		
		BaseUserProfile user = subject.getUserProfile();
		String userRole = user.getAbstractPrincipal().getAbstractUserRole().getRoleId();
		
		if (!(user instanceof BDUserProfile)) {
			return false;
		}
		
		if (StringUtils.contains(restrictedRoles,userRole)) {
			return false;
		}
		return true;
	}

	@Override
	public boolean needsAuthentication() {
		return false;
	}

	@Override
	public boolean hasPermission() {
		return true;
	}
	
}
