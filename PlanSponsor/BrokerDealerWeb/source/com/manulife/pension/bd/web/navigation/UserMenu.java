package com.manulife.pension.bd.web.navigation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.manulife.pension.exception.SystemException;

/**
 * This class will contain the Menu Tree that will be shown in each page in NBDW as "Navigation Menu".
 * 
 * MenuGenerator creates an object of this type and places it in Application, session context.
 * 
 * MenuRenderer uses this object (created by MenuGenerator), to create the html 
 * content for displaying the "Navigation Menu".
 * 
 * @author HArlomte
 *
 */
public class UserMenu implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// The Collection of "userMenuItem" can be considered as a Collection of Tier-1 level menu.
	ArrayList<UserMenuItem> levelOneuserMenuItems;
	
	// Each UserMenuItem created is placed in the HashMap for easy accessibility.
	HashMap<String, UserMenuItem> menuMap;

	public UserMenu() {
		levelOneuserMenuItems = new ArrayList<UserMenuItem>();
		menuMap = new HashMap<String, UserMenuItem>();
	}
	
	public ArrayList<UserMenuItem> getLevelOneuserMenuItems() {
		return levelOneuserMenuItems;
	}

	public HashMap<String, UserMenuItem> getMenuMap() {
		return menuMap;
	}

	public UserMenuItem getMenuItem(String menuID) {
		return this.menuMap.get(menuID);
	}

	/**
	 * This method is used to add a Top-level (i.e., Level One) Navigation Menu Item. 
	 * It is needed that before calling this method to add a Level-One Navigation Menu Item, all the 
	 * sub-menu items for it should have been added.
	 *  
	 * @param userMenuItem
	 * @throws Exception
	 */
	public void addLevelOneUserMenuItem (UserMenuItem userMenuItem) throws SystemException {
		this.levelOneuserMenuItems.add(userMenuItem);
		if (this.menuMap.get(userMenuItem.getId()) != null) {
			throw new SystemException("menuID " + userMenuItem.getId() + 
					"is already in use. menuID should be unique.");
		}
		this.menuMap.put(userMenuItem.getId(),userMenuItem);
		
		if(userMenuItem.getSubMenuItems() != null && (!userMenuItem.getSubMenuItems().isEmpty())) {
			for (UserMenuItem subMenuItem: userMenuItem.getSubMenuItems()) {
				this.menuMap.put(subMenuItem.getId(), subMenuItem);
			}
		}
	}

	/**
	 * This method returns a String Array of selected menuID's. Given a menuID "X", it gets the 
	 * parentMenuID "Y" of that menu item having menuID "X" , then the parent menuID "Z" of that menu item
	 * having menuID "Y" and so on till it reaches a menu Item that has parent MenuID as null (indicating it
	 * is a level One Menu Item).
	 * @param menuIDSelected
	 * @return
	 */
	public ArrayList<String> getSelectedMenuItems(String menuIDSelected) {
		ArrayList<String> menuItemsSelected = new ArrayList<String>();
		if (menuIDSelected == null) {
			return null;
		}
		menuItemsSelected.add(menuIDSelected);
		UserMenuItem menuItemSelected = getMenuItem(menuIDSelected);
		if (menuItemSelected == null) {
			return null;
		}
		
		String parentID = menuItemSelected.getParentMenuID();
		while (parentID != null ) {
			String menuID = parentID;
			menuItemsSelected.add(menuID);
			menuItemSelected = getMenuItem(menuID);
			if (menuItemSelected == null) {break;}
			parentID = menuItemSelected.getParentMenuID();
		}
		//reverse the order.
		Collections.reverse(menuItemsSelected);

		return menuItemsSelected;
	}
/*	The setter methods have been commented out because we want to have control over how the 
 * UserMenuItems are inserted into UserMenu object.
 * 
 * public void setLevelOneuserMenuItems(
			ArrayList<UserMenuItem> levelOneuserMenuItems) {
		this.levelOneuserMenuItems = levelOneuserMenuItems;
	}

	public void setMenuMap(HashMap<String, UserMenuItem> menuMap) {
		this.menuMap = menuMap;
	}*/

	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}
}
