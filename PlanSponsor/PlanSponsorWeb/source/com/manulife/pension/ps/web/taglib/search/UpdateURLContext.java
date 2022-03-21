package com.manulife.pension.ps.web.taglib.search;

import java.util.Hashtable;
import java.util.Iterator;

import com.manulife.pension.service.searchengine.SearchResult;

public class UpdateURLContext extends AbstractURLContext {
	
	//url for the action that takes user to the current legislative page
	public static final String CURRENT_LEGISLATIVE_UPDATES_URL = "/do/news/currentLegislativeUpdates/";
	//url for the action that takes user to the archived legislative page
	public static final String ARCHIVED_LEGISLATIVE_UPDATES_URL = "/do/news/archivedLegislativeUpdates/";
	//url for the action that takes user to the current updates page
	public static final String CURRENT_UPDATES_URL = "/do/news/currentUpdates/";
	//url for the action that takes user to the archived updates page
	public static final String ARCHIVED_UPDATES_URL = "/do/news/archivedUpdates/";
	//url for the action that takes user to the current tpa updates page
	public static final String CURRENT_TPA_UPDATES_URL = "/do/news/currentTpaUpdates/";
	//url for the action that takes user to the archived updates page
	public static final String ARCHIVED_TPA_UPDATES_URL = "/do/news/archivedTpaUpdates/";
	//url for the action that takes user to the current tpa industry updates page
	public static final String CURRENT_TPA_INDUSTRY_UPDATES_URL = "/do/news/currentTpaIndustryUpdates/";
	//url for the action that takes user to the archived updates page
	public static final String ARCHIVED_TPA_INDUSTRY_UPDATES_URL = "/do/news/archivedTpaIndustryUpdates/";
	
	
	//groups that an update can possibly belong to 
	public static final int LEGISLATIVE_UPDATES_GROUP = 80;
	public static final int UPDATES_GROUP = 44;
	public static final int TPA_UPDATES_GROUP = 94;
	public static final int TPA_INDUSTRY_UPDATES_GROUP = 95;		

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
		StringBuffer returnURL = new StringBuffer();
		
		switch (parentId) {
			case LEGISLATIVE_UPDATES_GROUP:
				if(status.equalsIgnoreCase("current")){
					returnURL.append(CURRENT_LEGISLATIVE_UPDATES_URL);
				}
				if(status.equalsIgnoreCase("archived")){
					returnURL.append(ARCHIVED_LEGISLATIVE_UPDATES_URL);
				}
				break;
			case UPDATES_GROUP:
				if(status.equalsIgnoreCase("current")){
					returnURL.append(CURRENT_UPDATES_URL);
				}
				if(status.equalsIgnoreCase("archived")){
					returnURL.append(ARCHIVED_UPDATES_URL);
				}
				break;
			case TPA_UPDATES_GROUP:
				if(status.equalsIgnoreCase("current")){
					returnURL.append(CURRENT_TPA_UPDATES_URL);
				}
				if(status.equalsIgnoreCase("archived")){
					returnURL.append(ARCHIVED_TPA_UPDATES_URL);
				}
				break;
			case TPA_INDUSTRY_UPDATES_GROUP:
				if(status.equalsIgnoreCase("current")){
					returnURL.append(CURRENT_TPA_INDUSTRY_UPDATES_URL);
				}
				if(status.equalsIgnoreCase("archived")){
					returnURL.append(ARCHIVED_TPA_INDUSTRY_UPDATES_URL);
				}
				break;
		}
		
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


