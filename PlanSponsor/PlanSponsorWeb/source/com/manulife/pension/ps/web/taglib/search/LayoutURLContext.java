package com.manulife.pension.ps.web.taglib.search;

import java.util.Hashtable;
import java.util.Iterator;

import com.manulife.pension.content.bizdelegates.BrowseServiceDelegate;
import com.manulife.pension.service.searchengine.SearchResult;

public class LayoutURLContext extends AbstractURLContext {
	
	public static final String LANDING_PAGE = "/do/contentpages/userguide/landingpage/";
	public static final String SECOND_LEVEL_PAGE = "/do/contentpages/userguide/secondlevel/";

	/**
	 * @see AbstractURLContext#generateURL(String, Hashtable, Hashtable, SearchResult)
	 */
	public String generateURL(
		String url,
		Hashtable contentIDs,
		Hashtable queryParams,
		SearchResult result) {
		
		
		String returnUrl = "";
		
		String contentId = "";
		Iterator iter = contentIDs.keySet().iterator();
		
		//obtain content id
		if(iter.hasNext()){
			contentId = ((Integer)contentIDs.get((String)iter.next())).toString();
		}
		
		String parentId = result.getParentId();
		String category = result.getContentCategory();
		
		if(category.equals("0")){
			returnUrl = LANDING_PAGE + "?parentId=" + parentId;
		}else{
			returnUrl = SECOND_LEVEL_PAGE + "?contentKey=" + contentId;
		}
		
		
		
		//create browse service delegate
        BrowseServiceDelegate browseServiceDelegate= BrowseServiceDelegate.getInstance();

				
		//set excerpt text
		result.setExcerpt(result.getContent());
		
		
		//return URL
		return returnUrl;

	}

}

