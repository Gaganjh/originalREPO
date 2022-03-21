package com.manulife.pension.bd.web.fundEvaluator.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Contains global data read from FundEvaluator.properties file
 * @author PWakode
 */

public class CoreToolGlobalData implements CoreToolConstants{
	
	//Either Money Market or Stable value funds should be selected by default by the tool.
	
	public static final List<String> defaultSelectedStableValueFunds = new ArrayList<String>(); 
	
	public static final List<String> defaultSelectedMoneyMarketFunds = new ArrayList<String>(); 
	
	 

	public static final List<String> giflV3OptionalLSPSFunds = new ArrayList<String>();
	
	//Below static block is to read and initialize property file information one time
	static {
		//MorganStanleyFunds - //same as stable value funds/select * from PSW100.fund where INVESTMENT_MANAGER_ID='M51' and FUND_STATUS_CD !='CLOS'
//		getDefaultSelectedMoneyMarketFunds();//cannot be retrieved from database, need to be defined in properties file
//        getDefaultSelectedStableValueFunds();//cannot be retrieved from database, need to be defined in properties file
		getGiflV3OptionalLSPSFunds();
	}
	
//    /**
//     * This method reads Money Market funds names from properties file
//     */
//    private static void getDefaultSelectedMoneyMarketFunds() {
//        loadPropertyIntoMap(DEFAULT_MONEY_MARKET_FUNDS, defaultSelectedMoneyMarketFunds);
//    }
//    
//    /**
//     * This method reads Stable value funds names from properties file
//     */
//    private static void getDefaultSelectedStableValueFunds() {
//        loadPropertyIntoMap(DEFAULT_STABLE_VALUE_FUNDS, defaultSelectedStableValueFunds1);
//    }
    

    /**
     * Utility method to read properties from property file
     */
	private static void loadPropertyIntoMap(String property, Map<String, String> propertyMap) {
		try {
			String properties = FundEvaluatorProperties.get(property);
			StringTokenizer tokenizer = new StringTokenizer(properties, COMMA);
			while (tokenizer.hasMoreTokens()) {
				String id = tokenizer.nextToken().trim();
				propertyMap.put(id, "");
			}
		} catch (Exception ce) {
			ce.printStackTrace();
		}
	}
	
    /**
     * This method reads GIFL V3 option LSPS fund names from properties file
     */
    private static void getGiflV3OptionalLSPSFunds() {
		try {
			String properties = FundEvaluatorProperties.get(GIFL_V3_OPTIONAL_LSPS_FUNDS);
			StringTokenizer tokenizer = new StringTokenizer(properties, COMMA);
			while (tokenizer.hasMoreTokens()) {
				String id = tokenizer.nextToken().trim();
				giflV3OptionalLSPSFunds.add(id);
			}
		} catch (Exception ce) {
			ce.printStackTrace();
		}
    }

}