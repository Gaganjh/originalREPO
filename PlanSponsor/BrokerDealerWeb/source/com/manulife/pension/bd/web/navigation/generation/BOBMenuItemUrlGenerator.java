package com.manulife.pension.bd.web.navigation.generation;

import java.util.List;

import com.manulife.pension.bd.web.controller.AuthorizationSubject;
import com.manulife.pension.bd.web.navigation.UserMenuItem;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.userprofile.BDUserProfileHelper;

/**
 * Default strategy is to return the first child item's url
 * 
 * @author gullara
 *
 */
public class BOBMenuItemUrlGenerator implements ParentMenuItemUrlGenerator {
	public String getUrl(List<UserMenuItem> children, AuthorizationSubject subject) {
		if (children == null || children.size() == 0) {
			return null;
		} else {
			BDUserProfile userProfile = (BDUserProfile) subject.getUserProfile();
			if(BDUserProfileHelper.isFirmRep(userProfile)){
				return children.get(1).getActionURL();
			}else{
				return children.get(0).getActionURL();
			}
		}
	}
}
