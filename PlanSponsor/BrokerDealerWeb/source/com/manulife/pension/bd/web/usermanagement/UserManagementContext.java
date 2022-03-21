package com.manulife.pension.bd.web.usermanagement;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import com.manulife.pension.delegate.BDUserManagementServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBDExtUserProfile;
import com.manulife.pension.service.security.exception.SecurityServiceException;

public class UserManagementContext implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String UserManagementContextAttr = "usermanagementContext";

	private ExtendedBDExtUserProfile managedUserProfile;

	public ExtendedBDExtUserProfile getManagedUserProfile() {
		return managedUserProfile;
	}

	public void setManagedUserProfile(
			ExtendedBDExtUserProfile managedUserProfile) {
		this.managedUserProfile = managedUserProfile;
	}

	public static UserManagementContext getContext(HttpServletRequest request) {
		UserManagementContext instance = (UserManagementContext) request
				.getSession().getAttribute(UserManagementContextAttr);
		if (instance == null) {
			instance = new UserManagementContext();
			request.getSession().setAttribute(UserManagementContextAttr,
					instance);
		}
		return instance;
	}

	/**
	 * Set a user into the UserManagementContext as the current user
	 * 
	 * @param request
	 * @param principal
	 * @param userProfileId
	 * @throws SystemException
	 */
	public static void setCurrentUser(HttpServletRequest request,
			BDPrincipal principal, long userProfileId) throws SystemException,
			SecurityServiceException {
		UserManagementContext context = UserManagementContext
				.getContext(request);
		// clear the previous user
		context.setManagedUserProfile(null);
		ExtendedBDExtUserProfile externalUserProfile = BDUserManagementServiceDelegate
				.getInstance().getExtendedBDExtUserProfile(principal,
						userProfileId);
		context.setManagedUserProfile(externalUserProfile);
	}

	public static void clearContext(HttpServletRequest request) {
		request.getSession().removeAttribute(UserManagementContextAttr);
	}
}
