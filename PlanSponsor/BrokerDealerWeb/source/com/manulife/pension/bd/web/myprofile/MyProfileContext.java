package com.manulife.pension.bd.web.myprofile;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.manulife.pension.bd.web.controller.SecurityManager;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.exception.SystemException;

/**
 * This is the context related to my profile pages. It should be created for any
 * my profile web functions
 * 
 * @author guweigu
 * 
 */
public class MyProfileContext implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyProfileNavigation navigation;

	public MyProfileContext(SecurityManager securityManager,
			BDUserProfile profile) throws SystemException {
		navigation = new MyProfileNavigation(profile, securityManager);
	}

	public MyProfileNavigation getNavigation() {
		return navigation;
	}

	public void setNavigation(MyProfileNavigation navigation) {
		this.navigation = navigation;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}
}
