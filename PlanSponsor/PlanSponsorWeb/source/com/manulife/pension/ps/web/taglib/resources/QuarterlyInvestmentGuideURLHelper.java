package com.manulife.pension.ps.web.taglib.resources;

import com.manulife.pension.ps.web.Constants;
//build Quarterly Investment Guide URL for multiclass products
public class QuarterlyInvestmentGuideURLHelper {
	static public String getQuarterlyInvestmentGuideURL (String defaultClass, String location, boolean nml)
	{
        
		StringBuffer sbQigUrl = new StringBuffer();
		// build
		if(Constants.NY_LOCATION.equalsIgnoreCase(location))
		  sbQigUrl.append(Constants.QIG_BASE_NY_URL);
		else
		  sbQigUrl.append(Constants.QIG_BASE_URL);	
		sbQigUrl.append(Constants.UNDERSCORE)
		.append(defaultClass.substring(defaultClass.indexOf(Constants.CLASS_PREFIX)+Constants.CLASS_LENGTH));

		if(nml)
		{
		 sbQigUrl.append(Constants.UNDERSCORE);
		 sbQigUrl.append(Constants.NML);
		 
		}
		if(Constants.NY_LOCATION.equalsIgnoreCase(location))
		{
		 sbQigUrl.append(Constants.UNDERSCORE);
		 sbQigUrl.append(Constants.NY_LOCATION);
		 
		}    	
		sbQigUrl.append(Constants.PDF_SUFFIX);
		//String temp =sbQigUrl.toString();
		//System.out.println(temp);
		return sbQigUrl.toString();
	}

}
