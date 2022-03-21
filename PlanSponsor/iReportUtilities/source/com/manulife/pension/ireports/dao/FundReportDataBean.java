/**
 * 
 */
package com.manulife.pension.ireports.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.manulife.pension.content.view.MutableContent;
import com.manulife.pension.ireports.model.FundOffering;
import com.manulife.pension.service.fund.standardreports.valueobject.CurrentAsOfDate;
import com.manulife.pension.service.fund.standardreports.valueobject.Fund;

public class FundReportDataBean {
	protected Map allFunds;
	protected Map currentAsOfDates;
	protected Date earliestAsOfDate;
	protected Map fundMetricsByDate;
	protected Map fundFootnoteSymbols;
	protected List marketIndexes;
	protected Map marketIndexPerformancesByDate;
	protected Map marketIndexIbPerformancesByDate;
	protected Map morningstarCategoryPerformancesByDate;
	protected Map fundStandardDeviationsByDate;
	protected Map fundExpensesByDate;
	protected Map guaranteedAccounts;
	protected Map guaranteedAccountRates;
	protected Map investmentGroups;
	protected Map assetClasses;
	protected Map assetCategoryGroups;
	protected Map assetCategories;
	protected Map footnotesByCompany;
	private MutableContent[] MarketIndexFootnoesMap;
	protected Map<String,String> footnotesByMarketIndex;
	protected Map<String ,CurrentAsOfDate> currentAsOfDatesMap;
	
	public Fund getFund(String investmentid) {
		return (Fund) allFunds.get(investmentid);
	}

	/** 
	 * @param fundOffering
	 * @param lifeportfolio
	 * @return Collection &ltFund&gt;
	 */
	public Collection getAllFunds(FundOffering fundOffering, final String lifeportfolio) {
		Collection allCompanyFunds = getFunds(fundOffering);
		Collection lifecycleFunds = CollectionUtils.select(allCompanyFunds, new Predicate() {
			public boolean evaluate(Object obj) {
				return lifeportfolio.equals( ((Fund) obj).getAssetcls());
			}
		});
		return lifecycleFunds;
	}

	public Collection getFunds(final FundOffering fundOffering) {
		return CollectionUtils.select(allFunds.values(), new Predicate() {
			public boolean evaluate(Object fund) {
				return fund instanceof Fund && fundOffering.isIncluded((Fund) fund);
			}});
	}

	public Map getGuaranteedAccountRates(final String classMenu) {
		return (Map) guaranteedAccountRates.get(classMenu);
	}

	/**
	 * @return the marketIndexFootnoesMap
	 */
	public MutableContent[] getMarketIndexFootnoesMap() {
		return MarketIndexFootnoesMap;
	}

	/**
	 * @param marketIndexFootnoesMap the marketIndexFootnoesMap to set
	 */
	public void setMarketIndexFootnoesMap(MutableContent[] MarketIndexFootnoesMap) {
		this.MarketIndexFootnoesMap = MarketIndexFootnoesMap;
	}


	
	/**
	 * @return the footnotesByCompany
	 */
	public Map getFootnotesByCompany() {
		return footnotesByCompany;
	}


	/**
	 * @param footnotesByCompany the footnotesByCompany to set
	 */
	public void setFootnotesByCompany(Map footnotesByCompany) {
		this.footnotesByCompany = footnotesByCompany;
	}


}