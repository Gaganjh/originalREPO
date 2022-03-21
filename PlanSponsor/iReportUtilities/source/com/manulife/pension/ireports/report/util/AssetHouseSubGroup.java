package com.manulife.pension.ireports.report.util;


public class AssetHouseSubGroup implements Cloneable {

	private final String name;
	private int order;

	public AssetHouseSubGroup(String name, int order) {
		this.name = name;
		this.order = order;
	}

	public String getName() {
		return name;
	}
	
	public int getOrder() {
		return order;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof AssetHouseSubGroup)) {
			return false;
		}
		AssetHouseSubGroup otherSubGroup = (AssetHouseSubGroup)obj;
		if (this.name == null || otherSubGroup.name == null) {
			return false;
		}
		return this.name.equals(otherSubGroup.name) && this.order == otherSubGroup.order;
	}
	
	public int hashCode() {
		if (name == null) {
			return -1; 
		}
		return name.hashCode() + order;
	}

}
