package com.manulife.pension.ireports.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.ireports.dao.FundReportDataBean;
import com.manulife.pension.ireports.util.FundSelectionItem;
import com.manulife.pension.ireports.util.InvestmentidTransformer;
import com.manulife.pension.service.fund.standardreports.valueobject.Fund;

public class FundOffering {
	private String companyId;
	private int fundMenu; 
	private boolean includeNML; 

	public boolean isIncludeNML() {
		return includeNML;
	}

	public FundOffering(String companyId, int fundMenu, boolean includeNML) {
		this.companyId = companyId;
		this.fundMenu = fundMenu;
		this.includeNML = includeNML;
	}

	public String getCompanyId() {
		return companyId;
	}

	public int getFundMenu() {
		return fundMenu;
	}

	public String getFundMenuString() {
		return ""+getFundMenu();
	}
	
	/**
	 * Process a list of investmentIds, expanding any placeholders.
	 * The Lifecycle products list a single placeholder investmentId for the UI which
	 * must be expanded back to the actual funds in the reports.
	 * 
	 * @param fundsChosen the Fund.investmentids 
	 * @return the expanded investmentids
	 */
	public String[] buildFundsFromFundSelectionItems(String[] fundsChosen, FundReportDataBean fundReportDataBean) {
		List result = new ArrayList(fundsChosen.length);
		for (int i = 0; i < fundsChosen.length; i++) {
			String investmentid = fundsChosen[i];
			if (FundSelectionItem.LIFECYCLE_FAKE_INVESTMENT_ID.equals(investmentid)) {
				Collection lifecycleFunds = 
					fundReportDataBean.getAllFunds(this, StandardReportsConstants.ASSET_CLASS_LIFECYCLE);
				CollectionUtils.collect(lifecycleFunds, new InvestmentidTransformer(), result);
			} else {
				result.add(investmentid);
			}
		}
		return (String[]) result.toArray(new String[result.size()]);
	}


	public boolean equals(Object that) {
		return EqualsBuilder.reflectionEquals(this, that); 
	}
	
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	public boolean isValid() {
		return StringUtils.isNotEmpty(companyId) && fundMenu > 0;
	}
	
	public String toString() {
		return companyId + "_" + fundMenu;
	}


	/**
	 * Is the fund included in this fund list?
	 * @param fund
	 * @return true if included
	 */
	public boolean isIncluded(Fund fund) {
		boolean companyMatch = companyId.equals(fund.getCompanyId());
		boolean categoryMatch = isFundcategoryMatch(fund.getFundCategory());
		boolean nmlMatch = includeNML || !fund.isNML();
		boolean includeFundType = fund.isPooled();
		boolean match = fund.isGuaranteedAccount() || (companyMatch && categoryMatch && nmlMatch && includeFundType);
		return match;
	}

	protected boolean isFundcategoryMatch(String fundCategory) {
		switch (fundMenu) {
		case StandardReportsConstants.FUND_MENU_RETAIL:
			return !StandardReportsConstants.FUNDCATEGORY_VENTURE.equals(fundCategory);

		case StandardReportsConstants.FUND_MENU_VENTURE:
			return !StandardReportsConstants.FUNDCATEGORY_RETAIL.equals(fundCategory);
			
		case StandardReportsConstants.FUND_MENU_ALL:
			return true;
			
		default:
			throw new IllegalArgumentException("Illegal fundmenu value: " + fundMenu);
		}
	}
	
	public boolean isUSA() {
		return companyId.equals(StandardReportsConstants.COMPANY_ID_USA);
	}
}
