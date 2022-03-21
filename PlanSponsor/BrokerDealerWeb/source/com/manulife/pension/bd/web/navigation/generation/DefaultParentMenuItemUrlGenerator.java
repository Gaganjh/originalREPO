package com.manulife.pension.bd.web.navigation.generation;

import java.util.List;

import com.manulife.pension.bd.web.controller.AuthorizationSubject;
import com.manulife.pension.bd.web.navigation.UserMenuItem;

/**
 * Default strategy is to return the first child item's url
 * 
 * @author guweigu
 *
 */
public class DefaultParentMenuItemUrlGenerator implements ParentMenuItemUrlGenerator {
	public String getUrl(List<UserMenuItem> children, AuthorizationSubject subject) {
		if (children == null || children.size() == 0) {
			return null;
		} else {
			return children.get(0).getActionURL();
		}
	}
}
