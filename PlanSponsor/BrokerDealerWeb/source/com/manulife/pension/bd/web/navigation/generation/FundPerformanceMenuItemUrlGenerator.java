package com.manulife.pension.bd.web.navigation.generation;

import java.util.List;

import com.manulife.pension.bd.web.controller.AuthorizationSubject;
import com.manulife.pension.bd.web.navigation.UserMenuItem;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.service.security.bd.valueobject.UserSiteInfoValueObject.SiteLocation;

/**
 * Strategy for Fund & Performance is based on user preference
 * 
 * @author guweigu
 * 
 */
public class FundPerformanceMenuItemUrlGenerator implements
		ParentMenuItemUrlGenerator {
	private static final String USFP_ID = "jhUSAFund";
	private static final String NYFP_ID = "jhNYFund";

	public String getUrl(List<UserMenuItem> children, AuthorizationSubject subject) {
		BDUserProfile userProfile = (BDUserProfile) subject.getUserProfile();
		SiteLocation defaultFundListing = userProfile.getDefaultFundListing();
		String menuID = null;
		if (defaultFundListing == null
				|| defaultFundListing.compareTo(SiteLocation.USA) == 0) {
			menuID = USFP_ID;
		} else {
			menuID = NYFP_ID;
		}
		for (UserMenuItem c : children) {
			if (c.getId().equals(menuID)) {
				return c.getActionURL();
			}
		}
		return children.get(0).getActionURL();
	}

}
