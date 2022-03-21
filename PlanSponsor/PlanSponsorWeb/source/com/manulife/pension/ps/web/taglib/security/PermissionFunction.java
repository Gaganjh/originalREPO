package com.manulife.pension.ps.web.taglib.security;

import com.manulife.pension.service.security.role.UserRole;

/**
 * Class PermissionFunction is used to have permission related functions that
 * will be used in JSTL
 * 
 * @author Venkatesh Kasiraj
 * 
 */
public class PermissionFunction {
	/**
	 * Method hasPermission is used to check the particular permission is
	 * present in role passed
	 * 
	 * @param role
	 * @param permissionCode
	 * @return
	 */
	public static boolean hasPermission(UserRole role, String permissionCode) {
		boolean hasPermission = false;
		if (role != null) {
			hasPermission = role.hasPermissionCode(permissionCode);
		}
		return hasPermission;
	}
}
