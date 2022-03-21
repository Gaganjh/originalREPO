package com.manulife.pension.ps.web.taglib.search;

import java.util.Hashtable;
import java.util.Iterator;

import com.manulife.pension.service.searchengine.SearchResult;

public class TPAFormURLContext extends AbstractURLContext {
	
	//url for the TPA form with Group Annuity content
	public static final String TPA_FORM_GROUP_ANNUITY_URL = "/do/resources/tpaGaForms/";
//	url for the TPA form with Mutual Fund Content
	public static final String TPA_FORM_MUTUAL_FUND_URL = "/do/resources/tpaMfForms/";
	
	//groups that an update can possibly belong to 
	public static final String GROUP_ANNUITY_CONTENT_IND = "G -";
	public static final String MUTUAL_FUND_CONTENT_IND = "M -";

	/**
	 * @see AbstractURLContext#generateURL(String, Hashtable, Hashtable, SearchResult)
	 * 
	 * The implementation of the url generation for updates content type is as described below
	 * The group is evaluated to see if it belongs to the updates group or the legislative updates group
	 * In both situations the status is read & evaluated to be of either current or achived. This gives rise to
	 * four scenarios & correspondingly the url generated is one of the four given in constants in the class.
	 * 
	 * The content id is appended to the end of the url, prefixed with #X, this takes the user directly to 
	 * the anchor where the content id is found.
	 */
	public String generateURL(
		String url,
		Hashtable contentIDs,
		Hashtable queryParams,
		SearchResult result) {

		int parentId = Integer.parseInt(result.getParentId());
		String status = result.getStatus();
		String contentCategory = result.getContentCategory();
		StringBuffer returnURL = new StringBuffer();

		if ( contentCategory.startsWith(GROUP_ANNUITY_CONTENT_IND) )
			returnURL.append(TPA_FORM_GROUP_ANNUITY_URL);
		else if ( contentCategory.startsWith(MUTUAL_FUND_CONTENT_IND) )
			returnURL.append(TPA_FORM_MUTUAL_FUND_URL);
		
		//set the excerpt text
		result.setExcerpt(result.getContent());
		
		
		String contentId = "";
		Iterator iter = contentIDs.keySet().iterator();
		
		if(iter.hasNext()){
			contentId = ((Integer)contentIDs.get((String)iter.next())).toString();
		}

		returnURL.append("#X"+contentId);
		return returnURL.toString();
	}

}



