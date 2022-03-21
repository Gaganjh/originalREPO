package com.manulife.pension.bd.web.firmsearch;

import java.util.List;

import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;

public class FirmSearchForm extends AutoForm {

	private static final long serialVersionUID = 1L;
	
	private String query;
	private String firmType;
	private List<? extends BrokerDealerFirm> matchingFirms;
	
	public List<? extends BrokerDealerFirm> getMatchingFirms() {
		return matchingFirms;
	}
	public void setMatchingFirms(List<? extends BrokerDealerFirm> matchingFirms) {
		this.matchingFirms = matchingFirms;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getFirmType() {
		return firmType;
	}
	public void setFirmType(String firmType) {
		this.firmType = firmType;
	}
}
