/*
 * Created on Sep 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.administration;

import java.util.ArrayList;
import java.util.Properties;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import com.manulife.pension.security.PasswordDigest;

/**
 * @author antalro
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DecryptPIN {
	private static Properties jndiProperties = new Properties();
	private static String BASE_DN = "OU=U-2,OU=o-1,OU=organizations,O=Manulife,DC=mlifex01,DC=manulife,DC=com";
	//private static String BASE_DN = "OU=U-2,OU=o-1,OU=organizations,O=Manulife,DC=mlifuat02,DC=manulife,DC=com";
	static
	{
		jndiProperties.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		jndiProperties.put("java.naming.security.authentication", "simple");
		jndiProperties.put("java.naming.security.principal", "dsadmin");
		jndiProperties.put("java.naming.security.credentials", "dsadmin");
	}
	
	private static String getAttributeValue(String attributeName, Attributes attributes) throws Exception
	{
		Attribute attr = attributes.get(attributeName);

		String returnValue = "";
		//ArrayList values = new ArrayList();
		if(attr != null)
		{
			NamingEnumeration innerEnum = attr.getAll();
			if(innerEnum.hasMore())
			{
				returnValue = innerEnum.next().toString();
				//values.add(innerEnum.next().toString());
			}
		}
		//return values;
		return returnValue;
	}
	
	private static String getPIN(String location, String filter, SearchControls controls) throws Exception
	{
		String returnValue = null;
		jndiProperties.put("java.naming.provider.url", "ldap://10.201.40.74:389");
		
		InitialDirContext dirContext = new InitialDirContext(jndiProperties);

		//System.out.println("getPIN filter= "+filter);

    	NamingEnumeration namingEnumeration = dirContext.search(location, filter, controls);

    	while(namingEnumeration.hasMore())
    	{
    		SearchResult searchResult = (SearchResult) namingEnumeration.next();
    		Attributes attributes = searchResult.getAttributes();

    		returnValue = (getAttributeValue("man-ivrPIN", attributes).length() > 0 ? getAttributeValue("man-ivrPIN", attributes):"");
    		
    	}
    	return returnValue;

	}
	private static void decryptPINForProfile(String profileID) throws Exception
	{
		SearchControls controls = new SearchControls();
    	controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
    	StringBuffer filter = new StringBuffer("man-profileID=");
    	filter.append(profileID);
    	String pinFromLDAP = getPIN(BASE_DN,filter.toString(),controls);
    	if(!pinFromLDAP.equals(""))
    	{
	    	// System.out.println("About to decrypt "+pinFromLDAP);
	    	boolean done = false;
	    	int pinValue = 10000;
	    	String encryptedPIN = null;
	    	while(!done && pinValue < 100000)
	    	{
	    		encryptedPIN = PasswordDigest.compute(String.valueOf(pinValue));
	    		if(encryptedPIN.equals(pinFromLDAP))
	    		{
	    			done = true;
	    			System.out.println("Decrypted PIN ==>> "+pinValue);
	    		}
	    		pinValue += 1;
	    	}
	    	if(!done)
	    	{
	    		System.out.println("Could not decrypt PIN for for profileID="+profileID);
	    	}
    	}
    	else
    	{
    		System.out.println("No PIN information for this profile ID could be found in LDAP");
    	}
	}
	
	public static void main(String[] args) throws Exception
	{
		if(args.length == 0 || args.length > 1)
		{
			System.out.println("Usage: java DecrypPIN <profileID>");
		}
		else
		{
			decryptPINForProfile(args[0]);
		}
	}
	
}
