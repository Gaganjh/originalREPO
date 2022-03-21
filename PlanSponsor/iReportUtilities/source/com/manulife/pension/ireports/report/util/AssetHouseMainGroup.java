package com.manulife.pension.ireports.report.util;


public class AssetHouseMainGroup {

	private final String name;
	private int order;

	public AssetHouseMainGroup(String name, int order) {
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
		if (!(obj instanceof AssetHouseMainGroup)) {
			return false;
		}
		AssetHouseMainGroup otherMainGroup = (AssetHouseMainGroup)obj;
		if (this.name == null || otherMainGroup.name == null) {
			return false;
		}
		return this.name.equals(otherMainGroup.name) && this.order == otherMainGroup.order;
	}
	
	public int hashCode() {
		if (name == null) {
			return -1; 
		}
		return name.hashCode() + order;
	}

}
