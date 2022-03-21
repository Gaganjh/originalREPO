package com.manulife.pension.bd.web.ikit;

import java.util.List;

/**
 * This class is used to hold the Asset Class ID ( / Investment Category ID),
 * Asset Class Name ( / Investment Category name), A list of sorted fund id's
 * for that particular asset class( / Investment Category).
 * 
 * @author harlomte
 * 
 */
public class AssetClassOrRiskReturn {
	String id;
	String name;
	List<String> fundIds;

	public AssetClassOrRiskReturn() {

	}

	public AssetClassOrRiskReturn(String id, String name, List<String> fundIds) {
		this.id = id;
		this.name = name;
		this.fundIds = fundIds;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getFundIds() {
		return fundIds;
	}

	public void setFundIds(List<String> fundIds) {
		this.fundIds = fundIds;
	}

}