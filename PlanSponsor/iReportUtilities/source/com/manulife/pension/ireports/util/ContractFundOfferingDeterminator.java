package com.manulife.pension.ireports.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.ireports.model.Contract;
import com.manulife.pension.ireports.model.FundOffering;
import com.manulife.pension.service.fund.standardreports.valueobject.Fund;

public class ContractFundOfferingDeterminator implements StandardReportsConstants {

	public boolean contractMixAndMatchInd;
	public FundOffering fundOffering;
	public String classMenu;
	public Map<String, Fund> contractFundsMap;

	public ContractFundOfferingDeterminator(String company, Contract contract) {
		// iterate funds
		Collection funds = contract.getFunds();
		Set fundCategories = new HashSet();
		boolean isNML = false;
		String contractClass = "";
		boolean isMixAndMatch = false;
		contractFundsMap = new HashMap<String, Fund>();

		for (Iterator fundsIterator = funds.iterator(); fundsIterator.hasNext();) {
			Fund fund = (Fund) fundsIterator.next();

			fundCategories.add(fund.getFundCategory());

			if (!isNML && fund.isNML()) {
				isNML = true;
			}

			if (StringUtils.isEmpty(contractClass)) {
				contractClass = fund.getRatetype();
			} else if (!contractClass.equals(fund.getRatetype())) {
				isMixAndMatch = true;
			}

			contractFundsMap.put(fund.getInvestmentid(), fund);
		}

		boolean multiclassContract = StandardReportsConstants.PACKAGE_SERIES_MULTI_CLASS
				.equals(contract.getFundPackageSeries());

		// contractMixAndMatchInd
		contractMixAndMatchInd = false;
		if (multiclassContract && isMixAndMatch) {
			contractMixAndMatchInd = true;
		}

		// fundOffering
		int fundMenu = 0;

		if (CATEGORY_RETAIL.containsAll(fundCategories)) {
			fundMenu = StandardReportsConstants.FUND_MENU_RETAIL;
		} else if (CATEGORY_VENTURE.containsAll(fundCategories)) {
			fundMenu = StandardReportsConstants.FUND_MENU_VENTURE;
		} else {
			fundMenu = StandardReportsConstants.FUND_MENU_ALL;
		}

		fundOffering = new FundOffering(company, fundMenu, isNML);

		// classMenu
		// Note: Although the classMenu is being determined here, we are not
		// using this classMenu anywhere in the ireports. Instead, we are using
		// the Fund Rate type for each fund and getting its Fund Metrics,
		// Standard Deviation, Expense Ratio values.
		if (multiclassContract) {
			classMenu = contractClass;
		} else {
			if (PRODUCT_IDS_VENTURE_BD.contains(contract.getProductId())) {
				classMenu = StandardReportsConstants.CLASS_2;
			} else {
				classMenu = StandardReportsConstants.CLASS_5;
			}
		}
	}
	
	public ContractFundOfferingDeterminator(String company, com.manulife.pension.service.fund.ireport.Contract contract) {
		// iterate funds
		Collection funds = contract.getFunds();
		Set fundCategories = new HashSet();
		boolean isNML = false;
		String contractClass = "";
		boolean isMixAndMatch = false;
		contractFundsMap = new HashMap<String, Fund>();

		for (Iterator fundsIterator = funds.iterator(); fundsIterator.hasNext();) {
			Fund fund = (Fund) fundsIterator.next();

			fundCategories.add(fund.getFundCategory());

			if (!isNML && fund.isNML()) {
				isNML = true;
			}

			if (StringUtils.isEmpty(contractClass)) {
				contractClass = fund.getRatetype();
			} else if (!contractClass.equals(fund.getRatetype())) {
				isMixAndMatch = true;
			}

			contractFundsMap.put(fund.getInvestmentid(), fund);
		}

		boolean multiclassContract = StandardReportsConstants.PACKAGE_SERIES_MULTI_CLASS
				.equals(contract.getFundPackageSeries());

		// contractMixAndMatchInd
		contractMixAndMatchInd = false;
		if (multiclassContract && isMixAndMatch) {
			contractMixAndMatchInd = true;
		}

		// fundOffering
		int fundMenu = 0;

		if (CATEGORY_RETAIL.containsAll(fundCategories)) {
			fundMenu = StandardReportsConstants.FUND_MENU_RETAIL;
		} else if (CATEGORY_VENTURE.containsAll(fundCategories)) {
			fundMenu = StandardReportsConstants.FUND_MENU_VENTURE;
		} else {
			fundMenu = StandardReportsConstants.FUND_MENU_ALL;
		}

		fundOffering = new FundOffering(company, fundMenu, isNML);

		// classMenu
		// Note: Although the classMenu is being determined here, we are not
		// using this classMenu anywhere in the ireports. Instead, we are using
		// the Fund Rate type for each fund and getting its Fund Metrics,
		// Standard Deviation, Expense Ratio values.
		if (multiclassContract) {
			classMenu = contractClass;
		} else {
			if (PRODUCT_IDS_VENTURE_BD.contains(contract.getProductId())) {
				classMenu = StandardReportsConstants.CLASS_2;
			} else {
				classMenu = StandardReportsConstants.CLASS_5;
			}
		}
	}

	public boolean isContractMixAndMatch() {
		return contractMixAndMatchInd;
	}

	public String getFundMenuCode() {
		return fundOffering.getFundMenuString();
	}

	public String getClassMenuCode() {
		return classMenu;
	}

	public String getIncludeNML() {
		return fundOffering.isIncludeNML() ? "Y" : "N";
	}

	/**
	 * @return the contractFundsMap
	 */
	public Map<String, Fund> getContractFundsMap() {
		return contractFundsMap;
	}

}
