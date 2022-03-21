package com.manulife.pension.bd.web.usermanagement;

import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.service.security.BDUserRoleType;

/**
 * The action form for the user management dispatch action
 * 
 * @author guweigu
 * 
 */
public class UserManagementDispatchForm extends AutoForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long userProfileId;
	private String userRoleCode;

	public long getUserProfileId() {
		return userProfileId;
	}

	public void setUserProfileId(long userProfileId) {
		this.userProfileId = userProfileId;
	}

	public String getUserRoleCode() {
		return userRoleCode;
	}

	public void setUserRoleCode(String userRoleCode) {
		this.userRoleCode = userRoleCode;
	}
	
	public BDUserRoleType getUserRole() {
		return BDUserRoleType.getByRoleCode(getUserRoleCode());
	}
}
