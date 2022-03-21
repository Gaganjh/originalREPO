package com.manulife.pension.ps.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

/**
 * This is a bean class which is instantiated during the load
 * of securityinfo.xml. It is the representation of URL information in Webresources.
 * 
 * @author Ilker Celikyilmaz
 */
public class URLPattern implements Comparable {
	private String url;
	private Collection<String> allowedProductTypes;
	private Collection<String> allowedContractStatuses;	
	private boolean contractRequired;
	private Collection<String> contractPermissionsRequired;
	private Collection<String> allowedSites;	
	private Collection<String> allowedPermissions;
	private boolean selectedAccessCheckRequired;
	
	public URLPattern(String url, boolean contractRequired, boolean selectedAccessCheckRequired)
	{
		this.url = url;
		this.contractRequired = contractRequired;
		this.selectedAccessCheckRequired = selectedAccessCheckRequired;
		allowedProductTypes = new ArrayList<String>();
		allowedContractStatuses = new ArrayList<String>();
		contractPermissionsRequired = new ArrayList<String>();
		allowedSites = new ArrayList<String>();
		allowedPermissions = new ArrayList<String>();
	}
	
	/**
	 * Gets the url
	 * @return Returns a String
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * This method is used to add allowed product types for this url.
	 * Parameter can be "AL", "UAL" or "AL,UAL".
	 * The method is parse the values appropriately.
	 * 
	 * @param String productTypes
	 */
	public void addAllowedProductTypes(String productTypes)
	{
		StringTokenizer strToken = new StringTokenizer(productTypes, ",");
		while (strToken.hasMoreTokens())
			this.allowedProductTypes.add(strToken.nextToken());
	}

	
	/**
	 * This method is used to add allowed contract statuses for this url.
	 * Parameter will have multiple contract statuses seperated by comma (AC,CF,DI)
	 * The method is parse the values appropriately.
	 * 
	 * @param String contractStatuses
	 */
	public void addAllowedContractStatuses(String contractStatuses)
	{
		StringTokenizer strToken = new StringTokenizer(contractStatuses, ",");
		while (strToken.hasMoreTokens())
			this.allowedContractStatuses.add(strToken.nextToken());
	}
	
	/**
	 * This method is used to add allowed permissions for a specific url.
	 * Parameter will have multiple permission codes separated by comma
	 * The method is parse the values appropriately.
	 * 
	 * @param String contractStatuses
	 */
	public void addAllowedPermissions(String permissionCodes) {
		StringTokenizer strToken = new StringTokenizer(permissionCodes, ",");
		while (strToken.hasMoreTokens()) {
			this.allowedPermissions.add(strToken.nextToken());
		}
	}
		
	/**
	 * This method is used to test if the current contracts product types is
	 * allowed to access this url.
	 * 
	 * @param boolean isContractAllocated true is is productt type is "AL", false
	 * if product type is "UAL".
	 * 
	 * @return true if product type is allowed.
	 */	
	public boolean isProductTypeAllowed(boolean isDefinedBenefit)	
	{
		boolean isAllowed = false;
	
		String productType = "AL";
		
        if ( isDefinedBenefit )
            productType = "DB";
     		
		if ( allowedProductTypes.contains(productType) )
			isAllowed = true;
			
		return isAllowed;
	}
	
	/**
	 * This method is used to verify if the current contracts status is
	 * allowed to access this url.
	 * 
	 * @param String contractStatus
	 * 
	 * @return true if contract status is allowed.
	 */	
	public boolean isContractStatusAllowed(String contractStatus)	
	{
		boolean isAllowed = false;

		if ( allowedContractStatuses.contains(contractStatus) )
			isAllowed = true;
			
		return isAllowed;
	}	
	
	public String toString()
	{
		StringBuffer buf = new StringBuffer("URLPattern object==>");
		buf.append("url=").append(url).append(", ");
		buf.append("contractRequired=").append(contractRequired).append(", ");
		buf.append("selectedAccessCheckRequired=").append(selectedAccessCheckRequired).append(", ");
		buf.append("allowedContractStatuses=").append(allowedContractStatuses).append(", ");				
		buf.append("allowedProductTypes=").append(allowedProductTypes).append("|");
		buf.append("contractPermissionsRequired=").append(contractPermissionsRequired).append("|");		
		buf.append("allowedSites=").append(allowedSites);		
		
		return buf.toString();
	}	
	
	public boolean equals(Object obj)
	{
		boolean isEqual = false;
		if ( obj instanceof URLPattern )
		{
			URLPattern objCompare = (URLPattern) obj;
			
			if ( this.url.equals(objCompare.getUrl()) && 
                    this.isProductTypeAllowed(false) == objCompare.isProductTypeAllowed(false) &&
                    this.isProductTypeAllowed(false) == objCompare.isProductTypeAllowed(false) &&
                    this.isProductTypeAllowed(true) == objCompare.isProductTypeAllowed(true) &&
                    this.isProductTypeAllowed(true) == objCompare.isProductTypeAllowed(true) )
				isEqual = true;
		}
	
		return isEqual;			
	}
	
	public int compareTo(Object obj)
	{
		int compareResult = -1;
		if ( obj instanceof URLPattern )
		{
			URLPattern objCompare = (URLPattern) obj;
			
			compareResult = this.url.compareTo(objCompare.getUrl());
		}
	
		return compareResult;			
	}
	
	/**
	 * checks if the contract obejcts existince in user profile required
	 * @return Returns a boolean
	 */
	public boolean isContractRequired() {
		return contractRequired;
	}


	public boolean isSelectedAccessCheckRequired() {
		return selectedAccessCheckRequired;
	}

	/**
	 * Returns whether this URL requires a permission
	 * @return
	 */
	public boolean isPermissionRequired() {
		return !this.allowedPermissions.isEmpty();
	}
	
	/**
	 * This method is used to add requird contract permission for this url.
	 * Parameter will have multiple contract permissions seperated by comma ()
	 * The method parses the values appropriately.
	 * 
	 * @param String contractPermissions
	 */
	public void addContractPermissionsRequired(String contractPermissions)
	{
		StringTokenizer strToken = new StringTokenizer(contractPermissions, ",");
		while (strToken.hasMoreTokens())
			this.contractPermissionsRequired.add(strToken.nextToken());
	}
	
	/**
	 * This method returns the required permissions 
	 * 
	 * @return String[] contractPermissionsRequired
	 */
	public String[] getContractPermissionsRequired()
	{
		return contractPermissionsRequired.toArray(new String[0]);
	}

	/**
	 * This method returns the required permissions (not contract related, from role)  
	 * 
	 * @return String[] allowedPermissions
	 */
	public String[] getAllowedPermissions()
	{
		return allowedPermissions.toArray(new String[0]);
	}
	
	
	/**
	 * This method is used to add allowed sites for this url.
	 * Parameter can be "US", "NY" or "US,NY".
	 * The method is parse the values appropriately.
	 * 
	 * @param String allowedTypes
	 */
	public void addAllowedSites(String sites)
	{
		StringTokenizer strToken = new StringTokenizer(sites, ",");
		while (strToken.hasMoreTokens())
			this.allowedSites.add(strToken.nextToken().toUpperCase());
	}
	
	/**
	 * This method is used to verify if the current site is
	 * allowed to access this url.
	 * 
	 * @param String siteCode
	 * 
	 * @return true if site is allowed.
	 */	
	public boolean isSiteAllowed(String siteCode)	
	{
		boolean isAllowed = false;

		if ( allowedSites.contains(siteCode.toUpperCase()) )
			isAllowed = true;
		else if ( allowedSites.isEmpty() )
			isAllowed = true;
		
		return isAllowed;
	}
	
}

