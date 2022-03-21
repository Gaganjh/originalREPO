package com.manulife.pension.bd.web.navigation.generation;

import java.util.List;

import com.manulife.pension.bd.web.controller.AuthorizationSubject;
import com.manulife.pension.bd.web.navigation.UserMenuItem;
import com.manulife.pension.platform.web.navigation.BaseMenuItem;

public class SystemMenuItem extends BaseMenuItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<SystemMenuItem> subMenuItems;

	private ParentMenuItemUrlGenerator urlGenerator = new ParentMenuItemUrlGenerator(){
		public String getUrl(List<UserMenuItem> children,
				AuthorizationSubject subject) {
			return getActionURL();
		}		
	};
	
	public ParentMenuItemUrlGenerator getUrlGenerator() {
		return urlGenerator;
	}

	public void setUrlGenerator(ParentMenuItemUrlGenerator urlGenerator) {
		this.urlGenerator = urlGenerator;
	}

	public SystemMenuItem() {
		super();
	}

	public List<SystemMenuItem> getSubMenuItems() {
		return subMenuItems;
	}

	public void setSubMenuItems(List<SystemMenuItem> subMenuItems) {
		this.subMenuItems = subMenuItems;
	}
	
	public boolean hasChildren() {
		return subMenuItems != null && subMenuItems.size() > 0;
	}
}
