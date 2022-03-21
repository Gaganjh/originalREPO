package com.manulife.pension.ps.web.controller;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This is the implementation of WebServices entity 
 * which is stored in SecurityManager.
 * 
 * @author Ilker Celikyilmaz
 */
public class WebResource {
	private String id;
	private String description;
	private Collection associatedURLs;
	private boolean permissions = false;
	
	public WebResource(String id, String description) {
		this.id = id;
		this.description = description;
		associatedURLs = new ArrayList();
	} 
	
	/**
	 * This methos is used to associate the url
	 * with the webservice 
	 * 
	 * @param url
	 * 		The url that to be associated to the service.
	 */
	public void addURL(URLPattern url) {
		associatedURLs.add(url);
	}
	
	/**
	 * This methos returns the associated urls.
	 * 
	 * @return 
	 * 		Returns the all associated urls.
	 */
	public Collection getAssociatedURLs() {
		return associatedURLs;
	}	

	public String toString() {
		StringBuffer buf = new StringBuffer("WebService object==>");
		buf.append("id=").append(id).append(", ");
		buf.append("description=").append(description).append(", ");
		buf.append("associatedURLs=").append(associatedURLs).append("|");
		
		return buf.toString();
	}
	
	/**
	 * Gets the description
	 * @return Returns a String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the permissions
	 */
	public boolean hasPermissions() {
		return permissions;
	}

	/**
	 * @param permissions the permissions to set
	 */
	public void setPermissions(boolean permissions) {
		this.permissions = permissions;
	}
}
