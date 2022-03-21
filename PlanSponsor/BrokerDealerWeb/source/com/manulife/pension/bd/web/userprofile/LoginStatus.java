package com.manulife.pension.bd.web.userprofile;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Login status
 * 
 * @author guweigu
 *
 */
public enum LoginStatus {
	FullyLogin("fullyLogin"),
	ResetPassword("resetPassword"),
	UpdatePassword("updatePassword");
	
	public String getCode() {
		return code;
	}


	private String code;
	
	LoginStatus(String code) {
		this.code = code;
	}
	
	
	private static final Map<String, LoginStatus> lookupByCode = new HashMap<String, LoginStatus>(
			17);

	public static LoginStatus getByCode(String value) {
		return lookupByCode.get(value);
	}

	static {
		for (LoginStatus t : EnumSet.allOf(LoginStatus.class)) {
			lookupByCode.put(t.getCode(), t);
		}
	}
}
