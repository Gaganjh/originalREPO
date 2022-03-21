package com.manulife.pension.bd.web.navigation;

import java.util.ArrayList;
import java.util.List;

public class MenuItem {
	private String id;
	private String title;
	private String action;

	private List<MenuItem> subMenuItems;

	public boolean isLeaf() {
		return subMenuItems == null || subMenuItems.size() == 0;
	}

	public List<MenuItem> getSubMenuItems() {
		return subMenuItems;
	}

	public void setSubMenuItems(List<MenuItem> subMenuItem) {
		this.subMenuItems = subMenuItem;
	}

	public void addSubMenuItem(MenuItem subMenuItem) {
		if (subMenuItems == null) {
			subMenuItems = new ArrayList<MenuItem>();
		}
		subMenuItems.add(subMenuItem);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
}
