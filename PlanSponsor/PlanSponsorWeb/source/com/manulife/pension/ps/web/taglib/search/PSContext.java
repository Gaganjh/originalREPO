package com.manulife.pension.ps.web.taglib.search;

import java.util.Hashtable;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.searchengine.SearchResult;

/**
 * This class is to replace TemplateContext from vignette. At present it does not have
 * all the methods of the class TemplateContext, however, it has some of the main methods that
 * are relavant to the search functionality.
 * 
 */
public class PSContext {

	/**
	 * This method accepts the name of a class that has the implementation for how to generate the url, if the 
	 * class is not null then it delegates the responsibility of generating the url to this class. 
	 * If the class is null then it passes the url generation responsibility to the generic class named
	 * GenericUrlContext
	 */
	public String makeCURL(String url, Hashtable contentIDs, Hashtable queryParams, String className, SearchResult result) throws SystemException{
		URLContext context = null;
		if(className != null){
			try{
				context =  (URLContext)Class.forName(className).newInstance();
			}
			catch (IllegalAccessException e){
				throw new SystemException(e,this.getClass().getName(), "makeCURL", "class " + className + " cannot be accessed, underlying exception is::"+e.toString());
			}
			catch (ClassNotFoundException e){
				throw new SystemException(e,this.getClass().getName(), "makeCURL", "class " + className + " not found, underlying exception is::"+e.toString());
			}
			catch (InstantiationException e){
				throw new SystemException(e,this.getClass().getName(), "makeCURL", "class " + className + " cannot be instantiated, underlying exception is::"+e.toString());
			}
		}
		if(context == null) context = new GenericURLContext();
	
		return context.generateURL(url, contentIDs, queryParams, result);
	}
	
	public String makeCURL(String url, String contentIDs, String queryParams) throws SystemException{
		return url + contentIDs + queryParams;
	}	
		
}

