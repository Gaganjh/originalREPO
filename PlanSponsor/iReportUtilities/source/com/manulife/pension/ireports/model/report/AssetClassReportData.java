package com.manulife.pension.ireports.model.report;

import java.util.Map;

public class AssetClassReportData {
	private Map assetClasses;
	
	/**
	 * retrieves all asset classes sorted by AssetClass.order property.
	 * 
	 * @return
	 */
	public Map getAssetClasses() {
		return assetClasses;
	}
	public void setAssetClasses(Map assetClasses) {
		this.assetClasses = assetClasses;
	}
	
}
