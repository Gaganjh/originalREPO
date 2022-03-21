package com.manulife.pension.ireports.report.viewbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.manulife.pension.ireports.report.util.AssetHouseMainGroup;

public class AssetHouseMainGroupViewBean {

	private final List subGroups = new ArrayList();
	private AssetHouseMainGroup mainGroup;

	public AssetHouseMainGroupViewBean(AssetHouseMainGroup mainGroup) {
		this.mainGroup = mainGroup;
	}

	public String getName() {
		return mainGroup.getName();
	}
	
	public int getOrder() {
		return mainGroup.getOrder();
	}

	public List getSubGroups() {
		return Collections.unmodifiableList(subGroups);
	}

	public void addSubGroup(AssetHouseSubGroupViewBean subGroup) {
		subGroups.add(subGroup);
	}
	
	public boolean hasSubGroups() {
		return subGroups != null && !subGroups.isEmpty();
	}

	public int getNumberOfSubgroups() {
		if (subGroups != null) {
			return subGroups.size();
		}
		return 0;
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof AssetHouseMainGroupViewBean)) {
			return false;
		}
		AssetHouseMainGroupViewBean otherMainGroup = (AssetHouseMainGroupViewBean)obj;
		if (this.mainGroup == null || otherMainGroup.mainGroup == null) {
			return false;
		}
		return this.mainGroup.equals(otherMainGroup.mainGroup);
	}
	
	public int hashCode() {
		if (mainGroup == null) {
			return -1; 
		}
		return mainGroup.hashCode();
	}

}
