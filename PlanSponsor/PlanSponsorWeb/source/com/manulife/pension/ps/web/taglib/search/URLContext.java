package com.manulife.pension.ps.web.taglib.search;

import java.util.Hashtable;

import com.manulife.pension.service.searchengine.SearchResult;


public interface URLContext {
	public String generateURL(String url, Hashtable contentIDs, Hashtable queryParams, SearchResult result);
}

