package com.manulife.pension.ireports.report.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.manulife.pension.ireports.model.report.ReportFund;
import com.manulife.pension.ireports.report.viewbean.AssetHouseMainGroupViewBean;
import com.manulife.pension.ireports.report.viewbean.AssetHouseSubGroupViewBean;
import com.manulife.pension.ireports.report.viewbean.MarketIndexIbPerformanceViewBean;
import com.manulife.pension.ireports.report.viewbean.MorningstarCategoryPerformanceViewBean;
import com.manulife.pension.platform.utility.fap.constants.FapConstants;
import com.manulife.pension.service.fund.standardreports.valueobject.Fund;
import com.manulife.pension.service.fund.standardreports.valueobject.MorningstarCategoryPerformance;
import com.manulife.pension.service.fund.valueobject.MarketIndexIbPerformance;

public class AssetHouseFundCollator {

	private List funds;
	private AssetHouseFundMapping mapping;
	private Map morningstarCategoryPerformances;
	private Map marketIndexIbPerformances;
	private Map<String, MarketIndexIbPerformance> marketIndexIbPerformancesMisssing = new HashMap<String, MarketIndexIbPerformance>();
	private List<String> morningstarCategorIdList = new ArrayList<String>();
	//changes added as part of ACR REWRITE (iReport changes)
	private String selectedReportName;

	public AssetHouseFundCollator(List funds, AssetHouseFundMapping mapping, Map morningstarCategoryPerformances, Map marketIndexPerformances,String selectedReportName) {
		super();
		this.funds = funds;
		this.mapping = mapping;
		this.morningstarCategoryPerformances = morningstarCategoryPerformances;
		this.marketIndexIbPerformances = marketIndexPerformances;
		this.selectedReportName = selectedReportName;
	}

	public List collate() {
		List result = new ArrayList();
		if (funds == null) {
			return Collections.EMPTY_LIST;
		}
		for (Iterator fundsIterator = funds.iterator(); fundsIterator.hasNext();) {
			ReportFund reportFund = (ReportFund) fundsIterator.next();
			Fund fund = reportFund.getFund();
			if (!fund.isMarketIndex()) {
				final AssetHouseSubGroupViewBean subgroup = new AssetHouseSubGroupViewBean(mapping.getSubGroup(fund.getAssetcls()));
				if (!result.contains(subgroup)) {
					createSubGroupInResult(result, subgroup);
				}
				
				AssetHouseSubGroupViewBean subGroupInResult = 
					(AssetHouseSubGroupViewBean) CollectionUtils.find(result, new Predicate() {
						public boolean evaluate(Object subGroupObj) {
							return subGroupObj.equals(subgroup);
						}
					});
				subGroupInResult.addFund(reportFund);
				if(reportFund.getFund().isGuaranteedAccount()) {
					subGroupInResult.addGICFund(reportFund);
				}
				if (morningstarCategoryPerformances != null) {
					addMorningstarCategoryPerformance(subGroupInResult, fund);
				}
				if (marketIndexIbPerformances != null) {
					addMarketIndexIbPerformance(subGroupInResult, fund,selectedReportName);
				}
			}
		}
		return collateIntoMainGroups(result);
	}

	private void createSubGroupInResult(List result, final AssetHouseSubGroupViewBean subgroup) {
		int order = subgroup.getOrder();
		int i = 0;
		boolean inserted = false;
		for (Iterator iter = result.iterator(); iter.hasNext() && !inserted;) {
			AssetHouseSubGroupViewBean subGroupVBInResult = (AssetHouseSubGroupViewBean) iter.next();
			if (subGroupVBInResult.getOrder() > order) {
				result.add(i, subgroup);
				inserted = true;
			}
			i++;
		}
		if (!inserted) {
			result.add(subgroup);
		}
	}

	private void addMarketIndexIbPerformance(AssetHouseSubGroupViewBean currentGroup, Fund fund,String selectedReportName) {
		String marketIndexId = fund.getMarketIndexId();
		if (marketIndexIbPerformances!=null && marketIndexId != null) {
			MarketIndexIbPerformance performance = 
				(MarketIndexIbPerformance)marketIndexIbPerformances.get(marketIndexId);
			if (performance != null) {
				currentGroup.addMarketIndexPerformance(new MarketIndexIbPerformanceViewBean(performance, fund));
			}
			// changes done as part of ACR REWRITE (iReport changes)
			else 
			{
				if(FapConstants.INVESTEMNT_RETURNS_AND_STANDARD_DEVIATION_REPORT.equals(selectedReportName)
						|| FapConstants.INVESTEMNT_RETURNS_AND_EXPENSE_RATIO_REPORT.equals(selectedReportName)){
					MarketIndexIbPerformance ibPerformance = marketIndexIbPerformancesMisssing.get(marketIndexId);
					if(ibPerformance == null) {
						ibPerformance = new MarketIndexIbPerformance();
						ibPerformance.setMarketIndexId(marketIndexId);
						ibPerformance.setMarketIndexName(fund.getMarketIndexName());
						marketIndexIbPerformancesMisssing.put(marketIndexId, ibPerformance);
					} 
					currentGroup.addMarketIndexPerformance(new MarketIndexIbPerformanceViewBean(ibPerformance, fund));
				}
			}
		}
	}

	private void addMorningstarCategoryPerformance(AssetHouseSubGroupViewBean currentGroup, Fund fund) {
		String fundId = fund.getInvestmentid();
		String morningstarId = fund.getMorningstarCategoryId();
		if (morningstarCategoryPerformances!=null && fundId != null) {
			MorningstarCategoryPerformance performance = 
				(MorningstarCategoryPerformance)morningstarCategoryPerformances.get(fundId);
			if (performance != null && !morningstarCategorIdList.contains(morningstarId)) {
				morningstarCategorIdList.add(morningstarId);
				currentGroup.addMorningstarCategoryPerformance(new MorningstarCategoryPerformanceViewBean(performance));
			}
		}
	}

	private List collateIntoMainGroups(List subGroups) {
		List result = new ArrayList();
		for (Iterator subGroupsIterator = subGroups.iterator(); subGroupsIterator.hasNext();) {
			AssetHouseSubGroupViewBean subGroupViewBean = (AssetHouseSubGroupViewBean) subGroupsIterator.next();
			AssetHouseMainGroup mainGroup = (AssetHouseMainGroup) mapping.lookupMainGroup(subGroupViewBean.getSubGroup());
			final AssetHouseMainGroupViewBean mainGroupViewBean = 
				new AssetHouseMainGroupViewBean(mainGroup);
			AssetHouseMainGroupViewBean resultMainGroup = null;
			if (! result.contains(mainGroupViewBean)) {
				resultMainGroup = (AssetHouseMainGroupViewBean) mainGroupViewBean;
				int order = mainGroupViewBean.getOrder();
				if (order >= result.size()) {
					result.add(resultMainGroup);
				} else {
					result.add(order, resultMainGroup);
				}
			}
			if (resultMainGroup == null) {
				resultMainGroup = 
					(AssetHouseMainGroupViewBean) CollectionUtils.find(result, new Predicate() {
						public boolean evaluate(Object subGroupObj) {
							return subGroupObj.equals(mainGroupViewBean);
						}
					});
			}
			resultMainGroup.addSubGroup(subGroupViewBean);
		}
		return result;
	}

}
