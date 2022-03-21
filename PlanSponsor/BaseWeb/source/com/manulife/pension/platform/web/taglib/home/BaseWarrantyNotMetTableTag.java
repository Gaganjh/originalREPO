package com.manulife.pension.platform.web.taglib.home;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.fund.cache.FundInfoCache;
import com.manulife.pension.service.fund.valueobject.AssetClassVO;
import com.manulife.pension.util.log.LogUtility;

/**
 * This class is the tag that will build the warrantyNotMet table
 * LS Fall 2006 fund launch. Added life asset grouping for warranty validation
 **/


public abstract class BaseWarrantyNotMetTableTag extends TagSupport {

	private static final String TOKEN_DELIMITER = ",";
	private static final String PARAMETER_KEY = "assetClasses";
	private static final String ASSET_CLASS_ID_LIFESTYLE = "LSF";
	private static final String ASSET_CLASS_ID_LIFECYCLE = "LCF";
	private static final String ASSET_CLASS_ID_LIFEASSET_DESC = 
						"Either an entire Target Date suite, or an entire Target Risk suite"; // description for life asset group

/**
 * doStartTag is called by the JSP container when the tag is encountered
 **/
	public int doStartTag()throws JspException {

		try {
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			
			String failedAssetClassString = (String)request.getParameter(PARAMETER_KEY);
			List firstList = new ArrayList();
			List secondList = new ArrayList();
			
			// failedAssetClassString is in the format of [xxx, xxx, xxx]
			// removes the square brackets
			failedAssetClassString = failedAssetClassString.substring(1, failedAssetClassString.length() - 1);
			List failedAssetClasses = (List) parseFailedAssetClassString(failedAssetClassString);
			int listSize = failedAssetClasses.size();
			// if size is > 6, we need two lists side by side
			if (listSize > 6) {
				listSize = failedAssetClasses.size() / 2;
				// add the odd one to the first list
				if (failedAssetClasses.size() % 2 > 0)
					listSize++;
			}
			List assetClasses = FundInfoCache.getAllAssetClasses();			
			for (Iterator iter = assetClasses.iterator(); iter.hasNext();) {
				AssetClassVO vo = (AssetClassVO) iter.next();
				String key = vo.getAssetClass();
				if( ASSET_CLASS_ID_LIFECYCLE.equalsIgnoreCase(key))
					continue; //skipping lifecycle to avoid duplicate display of life asset group
				String assetDesc = vo.getAssetClassDesc();
				if (ASSET_CLASS_ID_LIFESTYLE.equalsIgnoreCase(key))
				{
				    assetDesc =ASSET_CLASS_ID_LIFEASSET_DESC;
				}
				Iterator it = failedAssetClasses.iterator();
				for (int i=0; it.hasNext(); i++) {
					String assetClass = (String) it.next();
					if (assetClass.equalsIgnoreCase(key)) {

						if (i < listSize) {
							firstList.add(assetDesc);
						} else {
							secondList.add(assetDesc);
						}
						break;
					}
				}
			}

			generateTable(firstList, secondList);

		} catch (IOException ex){
			SystemException se = new SystemException(ex,
                    "com.manulife.pension.platform.web.taglib.home.BaseWarrantyNotMetTableTag",
                    "doStartTag", "Exception when building the table: " + ex.toString());
			LogUtility.log(se);
			throw new JspException(se.getMessage());
		}
		return SKIP_BODY;
	}

	private Collection parseFailedAssetClassString(String failedAssetClassString) {
		
		List classes = new ArrayList();
		StringTokenizer st = new StringTokenizer(failedAssetClassString, TOKEN_DELIMITER);
		while (st.hasMoreTokens()) {
			classes.add(st.nextToken().trim());
		}
		return classes;	
	}
	
	protected abstract void generateTable(List firstList, List secondList) throws IOException;
	
	protected String generateList(List list) {
		
		StringBuffer buff = new StringBuffer();

		buff.append("<td valign=\"top\" width=\"50%\"><ul>");
		
		Iterator it = list.iterator();
		while (it.hasNext()) {
			String desc = (String) it.next();
			buff.append("<li>" + desc + "</li>");
		}
		buff.append("</ul>");
		return buff.toString();
	}
}
