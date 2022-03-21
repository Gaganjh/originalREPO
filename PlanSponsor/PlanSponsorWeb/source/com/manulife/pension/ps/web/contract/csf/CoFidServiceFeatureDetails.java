package com.manulife.pension.ps.web.contract.csf;

import com.manulife.pension.common.BaseSerializableCloneableObject;

public class CoFidServiceFeatureDetails extends BaseSerializableCloneableObject {
	
	private String coFidServiceProviderDescription;
	private boolean isSelectedServiceProvider;
	
	public CoFidServiceFeatureDetails(){
		
	}
	public String getCoFidServiceProviderDescription() {
		return coFidServiceProviderDescription;
	}
	public void setCoFidServiceProviderDescription(
			String coFidServiceProviderDescription) {
		this.coFidServiceProviderDescription = coFidServiceProviderDescription;
	}
	public boolean isSelectedServiceProvider() {
		return isSelectedServiceProvider;
	}
	public void setSelectedServiceProvider(boolean isSelectedServiceProvider) {
		this.isSelectedServiceProvider = isSelectedServiceProvider;
	}
	
	
	

}
