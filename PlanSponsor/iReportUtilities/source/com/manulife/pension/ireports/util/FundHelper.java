package com.manulife.pension.ireports.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.map.LinkedMap;

import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.service.fund.standardreports.valueobject.Fund;
import com.manulife.pension.service.fund.standardreports.valueobject.FundSortable;


public abstract class FundHelper {
	public static final String FUND_SORT_ORDER_ALPHABETICAL = "alpha";
	public static final String FUND_SORT_ORDER_RISKRETURN = "riskreturn";
	public static final String FUND_SORT_ORDER_ASSETCLASS = "assetclass";
        
	static Map fundComparatorMap;
	
	static class AlphabeticalFundComparator implements Comparator {
		public int compare (Object p1, Object p2){
			FundSortable fund1 = (FundSortable)p1;
			FundSortable fund2 = (FundSortable)p2;
			String fundname1 = fund1.getFundname();
			String fundname2 = fund2.getFundname();
			return fundname1.compareToIgnoreCase(fundname2);
		}
	}
		
	static class RiskReturnFundComparator implements Comparator {
		public int compare (Object p1, Object p2){
			FundSortable fund1 = (FundSortable)p1;
			FundSortable fund2 = (FundSortable)p2;
			if (fund1.getOrder() != fund2.getOrder()) {
				return fund1.getOrder() - fund2.getOrder(); 
			}
			return fund1.getSortnumber() - fund2.getSortnumber(); 
		}
	}

	static class AssetClassFundComparator implements Comparator {
		public int compare (Object p1, Object p2){
			FundSortable fund1 = (FundSortable)p1;
			FundSortable fund2 = (FundSortable)p2;

			Integer compare1 = fund1.getAssetclsOrder() == null ? new Integer(0) : fund1.getAssetclsOrder();
			Integer compare2 = fund2.getAssetclsOrder() == null ? new Integer(0) : fund2.getAssetclsOrder();
			
			if (compare1.compareTo(compare2) == 0) {
				// same asset class, sort funds within an asset class
				return fund1.getSortnumber() - fund2.getSortnumber(); 
			}
			
			return compare1.compareTo(compare2); 
		}
	}

	static {
		fundComparatorMap = new HashMap();
		fundComparatorMap.put(FUND_SORT_ORDER_ALPHABETICAL, new AlphabeticalFundComparator());
		fundComparatorMap.put(FUND_SORT_ORDER_RISKRETURN, new RiskReturnFundComparator());
		fundComparatorMap.put(FUND_SORT_ORDER_ASSETCLASS, new AssetClassFundComparator());
	}
	/**
	 * Collect the investmentids
	 * @param funds Collection<Fund>
	 * @return List<String> the investmentids
	 */
	public static List getFundInvestmentids(Collection funds) {
		return new ArrayList(makeFundMap(funds).keySet());
	}

	/**
	 * Create a Map from investmentid to Fund maintaining iteration order (see LinkedMap)
	 * @param funds Collection&lt;Fund&gt;
	 * @return Map&lt;investmentid, Fund&gt;
	 */
	public static Map makeFundMap(Collection funds) {
		Map result = new LinkedMap();
		for (Iterator iter = funds.iterator(); iter.hasNext();) {
			FundSortable fund = (FundSortable) iter.next();
			result.put(fund.getInvestmentid(), fund);
		}
		return result;
	}

	public static Map sortFunds(Map funds, String fundSortOrder) {
	    // sort the Map
	    Comparator comparator = getFundComparator(fundSortOrder);
	            
	    // sort the funds by the retrieved comparator
	    ArrayList list = new ArrayList(funds.values());
	    
		Collections.sort(list, comparator);
	            
	    // store the list of funds as a map
		return makeFundMap(list);
	}

	public static Comparator getFundComparator(String fundSortOrder) {
		Comparator comparator = (Comparator) fundComparatorMap.get(fundSortOrder);
		if (comparator == null) {
			// default to alphabetical sort
			comparator = (Comparator) fundComparatorMap.get(FundHelper.FUND_SORT_ORDER_ALPHABETICAL);
		}
		return comparator;
	}

	/**
	 * Transform a Map<String,Fund> --> Map<String,FundSelectionItem> preserving order and keys.
	 * @param funds
	 * @return Map<String,FundSelectionItem>
	 */
	public static Map transformFunds(Map funds, String companyId) {
		Map result = new LinkedMap();
		boolean foundLifeCycleFund = false;
		for(Iterator iter = funds.entrySet().iterator(); iter.hasNext();) {
			Map.Entry entry = (Entry) iter.next();
			Fund fund = (Fund)entry.getValue();
			if (fund.isLifecycle()) {
				if (!foundLifeCycleFund) {
					foundLifeCycleFund = true;
					// TODO
                    String fundname = StandardReportsUtils.isNewYork(companyId) ? "USA" : "NY";
					String fundnameWithAssetclass = StandardReportsConstants.ASSET_CLASS_LIFECYCLE + " - " + fundname;
					
					FundSelectionItem item = 
						new FundSelectionItem(FundSelectionItem.LIFECYCLE_FAKE_INVESTMENT_ID,
								fundname,
								fundnameWithAssetclass,
								fund.getAssetclsOrder(),
								fund.getSortnumber(),
								fund.getOrder());
					result.put(FundSelectionItem.LIFECYCLE_FAKE_INVESTMENT_ID , item);
				}
			} else {
				FundSelectionItem item = new FundSelectionItem((Fund)entry.getValue());
				result.put(entry.getKey(), item);
			}
		}
		return result;
	}


}
