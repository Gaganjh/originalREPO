package com.manulife.pension.bd.web.navigation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Miscellaneous;
import com.manulife.pension.platform.web.navigation.BaseMenuItem;
import com.manulife.pension.util.content.manager.ContentCacheManager;

/**
 * UserMenuItem object would contain the information, each Menu Item should
 * have. UserMenuItem can represent a menu item at any level. i.e., it can
 * represent a Level-one Navigation Menu Item (i.e., Tier-1) , or it can
 * represent a Level-Two Navigation Menu Item (i.e., Tier-2), etc.
 * 
 * @author HArlomte
 * 
 */
public class UserMenuItem extends BaseMenuItem {

	UserMenuItem() {
		super();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// A Menu-Item (for example, a Level-one Navigation Menu Item) can have
	// sub-menu Items.
	private List<UserMenuItem> subMenuItems;

	// Each of the sub-menu items have a common Level-one Navigation Menu Item.
	// parentMenuID gives
	// the ID of Level-one Navigation Menu Item.
	private String parentMenuID;
	private int cmaKey = 0;

	public UserMenuItem(int cmaKey) {
		this.cmaKey = cmaKey;
	}

	public UserMenuItem(String id, String title, String actionURL) {
		super(id, title, actionURL);
		subMenuItems = new ArrayList<UserMenuItem>();
	}

	public UserMenuItem(String id, String title, String actionURL,
			Boolean isEnabled) {
		super(id, title, actionURL, isEnabled);
		subMenuItems = new ArrayList<UserMenuItem>();
	}

	public int getCmaKey() {
		return cmaKey;
	}

	public List<UserMenuItem> getSubMenuItems() {
		return subMenuItems;
	}

	public void setSubMenuItems(List<UserMenuItem> subMenuItems) {
		this.subMenuItems = subMenuItems;
	}

	public String getParentMenuID() {
		return parentMenuID;
	}

	public void setParentMenuID(String parentMenuID) {
		this.parentMenuID = parentMenuID;
	}

	/**
	 * This method is used to add a Sub-Menu Item to a UserMenuItem object.
	 * 
	 * @param subMenuItem
	 */
	public void addSubMenuItem(UserMenuItem subMenuItem) {
		// We always expect the parentMenuID to be null.
		if (subMenuItem.getParentMenuID() == null
				|| "".equals(subMenuItem.getParentMenuID().trim())) {
			subMenuItem.setParentMenuID(this.getId());
		}
		this.subMenuItems.add(subMenuItem);
	}

	/**
	 * Returns the html of the menu if the menu is CMA controlled
	 * @return
	 * @throws ContentException
	 */
	public String getHtmlContent() throws ContentException {
		if (cmaKey != 0) {
			Miscellaneous content = (Miscellaneous) ContentCacheManager
					.getInstance().getContentById(cmaKey,
							ContentTypeManager.instance().MISCELLANEOUS);
			return content.getText();
		} else {
			return "";
		}
	}
	
	public boolean hasChildren() {
		return subMenuItems != null && subMenuItems.size() > 0;
	}

	/**
	 * Get the childnren's size
	 * @return
	 */
	public int getChildrenSize() {
		return subMenuItems == null ? 0 : subMenuItems.size();
	}
	
	/**
	 * Remove the child at index
	 * @param index
	 */
	public void removeChild(int index) {
		if (subMenuItems == null || subMenuItems.size() <= index) {
			return;
		} else {
			subMenuItems.remove(index);
		}
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}
}
