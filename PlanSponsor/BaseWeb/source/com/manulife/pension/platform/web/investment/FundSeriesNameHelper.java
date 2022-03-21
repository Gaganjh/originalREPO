package com.manulife.pension.platform.web.investment;

import java.util.Arrays;

import com.manulife.pension.platform.web.CommonConstants;

public class FundSeriesNameHelper {
	
	private static String[] NON_SIGNATURE_PRODUCTS = {"ARA85", "AIP", "10K", "ARA92", "ARADB", "ARA88", "ARANY", "ARABD", "ARABDY"};

	public static String getFundSeriesName (String productId, String location, String fundSeries)
	{
	 	String fundSeriesName ="";
	 	
	 	if(Arrays.asList(NON_SIGNATURE_PRODUCTS).contains(productId)){
	 		// All these products are considered Non-Signature products, even though some of them may have Multi-Class feature.
	 		fundSeriesName = CommonConstants.SITEMODE_NY.equalsIgnoreCase(location)?CommonConstants.FUND_SERIES_DESCRIPTION_HYBRID_NY: CommonConstants.FUND_SERIES_DESCRIPTION_HYBRID;
	 	} else if (CommonConstants.FUND_PACKAGE_MULTICLASS.equalsIgnoreCase(fundSeries))
	 		fundSeriesName = CommonConstants.SITEMODE_NY.equalsIgnoreCase(location)?CommonConstants.FUND_SERIES_DESCRIPTION_MULTICLASS_NY: CommonConstants.FUND_SERIES_DESCRIPTION_MULTICLASS;
	 	if (CommonConstants.FUND_PACKAGE_RETAIL.equalsIgnoreCase(fundSeries))
	 		fundSeriesName = CommonConstants.SITEMODE_NY.equalsIgnoreCase(location)?CommonConstants.FUND_SERIES_DESCRIPTION_RETAIL_NY: CommonConstants.FUND_SERIES_DESCRIPTION_RETAIL;
	 	else if (CommonConstants.FUND_PACKAGE_VENTURE.equalsIgnoreCase(fundSeries)||CommonConstants.FUND_PACKAGE_VRS.equalsIgnoreCase(fundSeries))
	 		fundSeriesName = CommonConstants.FUND_SERIES_DESCRIPTION_VENTURE;
	 	else if(CommonConstants.FUND_PACKAGE_HYBRID.equalsIgnoreCase(fundSeries))
	 	{
	 		if(CommonConstants.BD_PRODUCT_ID.equalsIgnoreCase(productId)||CommonConstants.BD_PRODUCT_NY_ID.equalsIgnoreCase(productId))
	 			fundSeriesName = CommonConstants.SITEMODE_NY.equalsIgnoreCase(location)?CommonConstants.FUND_SERIES_DESCRIPTION_HYBRID_BD_NY: CommonConstants.FUND_SERIES_DESCRIPTION_HYBRID_BD;
	 		else
	 			fundSeriesName = CommonConstants.SITEMODE_NY.equalsIgnoreCase(location)?CommonConstants.FUND_SERIES_DESCRIPTION_HYBRID_NY: CommonConstants.FUND_SERIES_DESCRIPTION_HYBRID;
	 	}			
		return fundSeriesName;
	}
}
