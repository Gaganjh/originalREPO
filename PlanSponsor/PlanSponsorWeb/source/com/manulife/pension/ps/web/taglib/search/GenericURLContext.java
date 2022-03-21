package com.manulife.pension.ps.web.taglib.search;

import java.util.Hashtable;

import com.manulife.pension.service.searchengine.SearchResult;

public class GenericURLContext extends AbstractURLContext {

	public String generateURL(String url, Hashtable contentIDs, Hashtable queryParams, SearchResult result){
		StringBuffer buf = new StringBuffer();
		buf.append(url);
		buf.append(addParamsToUrl(url, contentIDs));
		buf.append(addParamsToUrl(url, queryParams));
		//must set the excerpt text before sending result or the excerpt will be empty.
		result.setExcerpt(result.getContent());
		return buf.toString();
	}	
}
