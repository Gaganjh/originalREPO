package com.manulife.pension.ps.web.taglib.search;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import com.manulife.pension.service.searchengine.SearchResult;

public abstract class AbstractURLContext implements URLContext {
	public abstract String generateURL(String url, Hashtable contentIDs, Hashtable queryParams, SearchResult result);
	
	public String addParamsToUrl(String url, Hashtable params){
		StringBuffer buf = new StringBuffer();
		if(!params.isEmpty()){
			Set keys = params.keySet();
			Iterator iter = keys.iterator();
			while(iter.hasNext()){
				String key = (String)iter.next();
				String value = ((Integer)params.get(key)).toString();
				buf.append(getSeparator(url)+key+"="+value);
			}
		}
		return buf.toString();
	}
	
	private String getSeparator(String url){
		if(url.indexOf("?") > 0) {
			return "&";
		}else{
			return "?";	
		}
	}
}

