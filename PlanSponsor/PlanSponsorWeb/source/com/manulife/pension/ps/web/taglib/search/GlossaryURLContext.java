package com.manulife.pension.ps.web.taglib.search;

import java.util.Hashtable;
import java.util.Iterator;

import com.manulife.pension.service.searchengine.SearchResult;

public class GlossaryURLContext extends AbstractURLContext {

	/**
	 * @see AbstractURLContext#generateURL(String, Hashtable, Hashtable, SearchResult)
	 */
	public String generateURL(
		String url,
		Hashtable contentIDs,
		Hashtable queryParams,
		SearchResult result) {
		

		StringBuffer returnUrl = new StringBuffer("/do/contentpages/glossary");
		
		String contentId = "";
		Iterator iter = contentIDs.keySet().iterator();
		
		//obtain content id
		if(iter.hasNext()){
			contentId = ((Integer)contentIDs.get((String)iter.next())).toString();
		}

		returnUrl.append("?Glossary_Category="+contentId);
		
		String title = result.getTitle().toUpperCase();
		
		int letterSelected = title.charAt(0) - (int)'A';
		
		returnUrl.append("&letterSelected="+letterSelected);
				
		//set excerpt text
		result.setExcerpt(result.getContent());
		
		
		//return URL
		return returnUrl.toString();


	}

}

