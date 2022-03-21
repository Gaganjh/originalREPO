package com.manulife.pension.ireports.util;

import com.manulife.pension.service.fund.standardreports.valueobject.Fund;
import com.manulife.pension.service.fund.standardreports.valueobject.FundSortable;

/**
 * View item for fund select list.  Used in place of Fund so we can
 * introduce a placeholder for the Lifecycle funds.
 */
public class FundSelectionItem implements FundSortable {
	/**
	 * Magic fake investment id.  Must be <= 3 chars to fit in config cookie.
	 */
	public static final String LIFECYCLE_FAKE_INVESTMENT_ID = "_LC"; 
	
	private String fundName;
	private String investmentid;
	private String fundnameWithAssetcls;
	private Integer assetclsOrder;
	private int sortnumber;
	private int order;

	public FundSelectionItem(String investmentid, String fundName, String fundnameWithAssetcls, Integer assetclsOrder, int sortnumber, int order) {
		this.investmentid = investmentid;
		this.fundName = fundName;
		this.fundnameWithAssetcls = fundnameWithAssetcls;
		this.assetclsOrder = assetclsOrder;
		this.sortnumber = sortnumber;
		this.order = order;
	}

	public FundSelectionItem(Fund fund) {
		this(fund.getInvestmentid(), 
				fund.getFundname(), 
				fund.getAssetcls() + " - " + fund.getFundname(),
				fund.getAssetclsOrder(),
				fund.getSortnumber(),
				fund.getOrder());
	}

	public String getInvestmentid() {
		return this.investmentid;
	}

	public String getFundname() {
		return fundName;
	}

	public String getFundnameWithAssetcls() {
		return fundnameWithAssetcls;
	}

	public int getSortnumber() {
		return sortnumber;
	}

	public Integer getAssetclsOrder() {
		return assetclsOrder;
	}

	public int getOrder() {
		return this.order;
	}
	
	public String toString() {
		return investmentid;
	}
}
