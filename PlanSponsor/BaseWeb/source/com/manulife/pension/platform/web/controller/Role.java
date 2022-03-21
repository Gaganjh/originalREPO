package com.manulife.pension.platform.web.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * This is the implementation of Role entity 
 * which is stored in SecurityManager.
 * 
 * @author Ilker Celikyilmaz
 */
public class Role {
	
	private String id;
	private String description;
	private List<String> associatedServices;
	private int timeout;
	
	public Role(String id, String description, String timeout)
	{
		this.id = id;
		this.description = description;
		this.timeout = Integer.parseInt(timeout);
		associatedServices = new ArrayList<String>();
	}
	public Role(String id, String description, int timeout)
	{
		this.id = id;
		this.description = description;
		this.timeout = timeout;
		associatedServices = new ArrayList<String>();
	}

	/**
	 * This method used used to add web servives
	 * 
	 * @param service
	 * 		webservice id
	 */	
	public void addService(String service)
	{
		associatedServices.add(service);
	}	

	/**
	 * Gets the id
	 * @return Returns a String
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the description
	 * @return Returns a String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the timeout 
	 * @return Returns int
	 */
	public int getTimeout() {
		return timeout;
	}


	/**
	 * This method returrns the all associated webservice ids
	 * @return Iterator
	 * 		Returns the all associated webservices id.
	 */
	@SuppressWarnings("unchecked")
	public Iterator getAssociatedServices()
	{
		return associatedServices.iterator();
	}

	public String toString()
	{
		StringBuffer buf = new StringBuffer("Role object==>");
		buf.append("id=").append(id).append(", ");
		buf.append("description=").append(description).append(", ");
		buf.append("timeout=").append(timeout).append(", ");		
		buf.append("associatedService=").append(associatedServices).append("|");
		
		return buf.toString();
	}
}
