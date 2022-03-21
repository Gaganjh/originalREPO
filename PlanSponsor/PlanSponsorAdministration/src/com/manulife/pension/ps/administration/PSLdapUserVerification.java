/*
 * Created on Apr 26, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.administration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
/**
 * @author antalro
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PSLdapUserVerification {
	


	

	

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

		public static void main(String[] args) throws Exception
		{
			//int delayTime;
			String profileIdToStart;
			String profileIdToEnd;
			long start = System.currentTimeMillis();
			
			
			String[] profileIds = retrieveProfileIdsFromCSDB();
			verifyWithLdapAndWriteErrors(profileIds);
		

			System.out.println("DONE in (ms) = "+(System.currentTimeMillis()-start));
		}

		private static String[] retrieveProfileIdsFromCSDB() throws Exception
		{

			ArrayList temp = new ArrayList();
			System.out.println("Entered retrieveProfileIdsFromCSDB");
			String sql = new String("select distinct user_profile_id from  psw100.user_profile with ur");
			System.out.println("Sql statement ==>>"+sql);
			Connection dbConnection = getDatabaseConnection();
			Statement statement = dbConnection.createStatement();

			ResultSet rs = statement.executeQuery(sql);
			while(rs.next())
			{
				temp.add(rs.getBigDecimal(1).toString());
			}

			return (String[]) temp.toArray(new String[]{});
		}

		private static void verifyWithLdapAndWriteErrors(String[] profileIds) throws Exception
		{
			LdapUser[] usersFromPrimary = getUsersWithinOrgWithProfileIds(BASE_DN,profileIds,true);

			LdapUser[] usersFromSecondary = getUsersWithinOrgWithProfileIds(BASE_DN,profileIds,false);
			HashMap primaryUsersMap = new HashMap();
			HashMap secondaryUsersMap = new HashMap();

			BufferedWriter errorWriter = new BufferedWriter(new java.io.FileWriter("./UserDifferences"+profileIds[0]+"."+profileIds[profileIds.length-1]+".log"));
			// create and populate the hashmap
			for(int i = 0 ; i < usersFromPrimary.length; i++)
			{
				if(primaryUsersMap.containsKey(usersFromPrimary[i].getProfileId()))
				{
					errorWriter.write("Duplicate profileID found in primary LDAP= "+usersFromPrimary[i].getProfileId()+"\n");
				}
				primaryUsersMap.put(usersFromPrimary[i].getProfileId(), usersFromPrimary[i]);
			}

			// create and populate the hashmap
			for(int i = 0 ; i < usersFromSecondary.length; i++)
			{
				if(secondaryUsersMap.containsKey(usersFromSecondary[i].getProfileId()))
				{
					errorWriter.write("Duplicate profileID found in secondary LDAP= "+usersFromSecondary[i].getProfileId()+"\n");
				}
				secondaryUsersMap.put(usersFromSecondary[i].getProfileId(), usersFromSecondary[i]);
			}



			if(profileIds.length != usersFromPrimary.length)
			{
				errorWriter.write("There were differences found between CSDB and primary LDAP. Number of users in CSDB= "+profileIds.length+ " primary LDAP= "+usersFromPrimary.length+"\n");
				errorWriter.flush();
			}

			if(profileIds.length != usersFromSecondary.length)
			{
				errorWriter.write("There were differences found between CSDB and secondary LDAP. Number of users in CSDB= "+profileIds.length+ " secondary LDAP= "+usersFromSecondary.length+"\n");
				errorWriter.flush();
			}

			// check if the database user is in the primary LDAP server.
			for(int i = 0; i < profileIds.length; i++)
			{
				if(!primaryUsersMap.containsKey(profileIds[i]))//!profileExists(profileIds[i],usersFromPrimary))
				{
					errorWriter.write("Database user not found in Primary LDAP= "+profileIds[i]+"\n");
				}

				if(!secondaryUsersMap.containsKey(profileIds[i]))//!profileExists(profileIds[i],usersFromSecondary))
				{
					errorWriter.write("Database user not found in Secondary LDAP= "+profileIds[i]+"\n");
				}
			}



			errorWriter.flush();

			if(usersFromPrimary.length != usersFromSecondary.length)
			{
				errorWriter.write("Number of users in Primary = "+usersFromPrimary.length+ " whereas Secondary = "+usersFromSecondary.length+"\n");
				errorWriter.flush();
			}
			// compare users from primary LDAP with the secondary LDAP
			for(int i = 0; i < usersFromPrimary.length; i++)
			{
				if(!usersFromPrimary[i].equals((LdapUser)secondaryUsersMap.get(usersFromPrimary[i].getProfileId())))
				{
					//errorWriter.write("*****Differences found between primary LDAP and secondary LDAP********\n");
					errorWriter.write("  Ldap differences   ==>> Primary User   = "+usersFromPrimary[i].toString()+"\n");
					errorWriter.write("  Ldap differences   ==>> Secondary User = "+((LdapUser)secondaryUsersMap.get(usersFromPrimary[i].getProfileId())).toString()+"\n");
					errorWriter.flush();
				}
			}
			errorWriter.close();
		}

		private static Connection getDatabaseConnection() throws Exception
		{
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			Connection conn = java.sql.DriverManager.getConnection("jdbc:db2:CS100P","ezcs_ro","ezcs_ro13");
			conn.setAutoCommit(true);
			return conn;
		}

		public static LdapUser[] getUsersWithinOrgWithProfileIds(String location, String[] profileIds, boolean primaryServer) throws Exception
		{
			// initialized Directory Context
			LdapUser[] users = null;
			ArrayList temp = new ArrayList();

	    	SearchControls controls = new SearchControls();
	    	controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
	    	StringBuffer  filter = new StringBuffer("(|");
	    	// we can not process more than 180 profileIds at one time,
	    	// so create batches of 180.
	    	System.out.println("There should be "+profileIds.length+" profileIds");
	    	if(profileIds.length > 180)
	    	{
	    		for (int i = 0; i < profileIds.length; i++)
	    		{
	    			if(Integer.parseInt(profileIds[i])>4)
	    			{
	    				//System.out.println(i+" "+profileIds[i]);
	    				filter.append("(").append("man-profileId").append("=").append(profileIds[i]).append(")");
	    				if(i > 0 && i % 180 == 0 )
	    				{
	    					filter.append(")");
	    					LdapUser[] tmpUsers = executeSearch(location,filter.toString(),controls,primaryServer);
	    					for(int j = 0; j< tmpUsers.length; j++)
	    					{
	    						temp.add(tmpUsers[j]);
	    					}
	    					// re-initialize the filter.
	    					filter = new StringBuffer("(|");
	    				}
	    			}
	    		}

	    		// if there are any left overs, that did not fit in a batch of 180 process them as well.
	    		if( filter.toString().indexOf("man-profileId") > 0)
	    		{
	    			filter.append(")");
	        		LdapUser[] tmpUsers = executeSearch(location,filter.toString(),controls,primaryServer);
					for(int j = 0; j< tmpUsers.length; j++)
					{
						temp.add(tmpUsers[j]);
					}
					// re-initialize the filter.
					filter = new StringBuffer("(|");
	    		}
	    	}
	    	else
	    	{
	    		for(int i = 0; i < profileIds.length; i++)
	    		{
	    			filter.append("(").append("man-profileId").append("=").append(profileIds[i]).append(")");
	    		}
	    		filter.append(")");
	    		LdapUser[] tmpUsers = executeSearch(location,filter.toString(),controls, primaryServer);
				for(int j = 0; j< tmpUsers.length; j++)
				{
					temp.add(tmpUsers[j]);
				}
	    	}

	        users = (LdapUser[]) temp.toArray(new LdapUser[]{});
	        return users;
		}

		private static LdapUser[] executeSearch(String location, String filter, SearchControls controls, boolean primaryServer) throws Exception
		{
			if(primaryServer)
			{
				jndiProperties.put("java.naming.provider.url", "ldap://10.201.40.74:389");
			}
			else
			{
				jndiProperties.put("java.naming.provider.url", "ldap://10.201.40.78:389");
			}
			InitialDirContext dirContext = new InitialDirContext(jndiProperties);
			ArrayList temp = new ArrayList();

			System.out.println("getAllUsersWithingOrgWithProfileIds filter= "+filter);

	    	NamingEnumeration namingEnumeration = dirContext.search(location, filter, controls);

	    	while(namingEnumeration.hasMore())
	    	{
	    		SearchResult searchResult = (SearchResult) namingEnumeration.next();
	    		Attributes attributes = searchResult.getAttributes();

	    		LdapUser ldapUser = new LdapUser();
	    		ldapUser.setUserName(getAttributeValue("sAMAccountName", attributes).size() > 0 ? getAttributeValue("sAMAccountName", attributes).get(0).toString():"");
				ldapUser.setProfileId(getAttributeValue("man-profileId", attributes).size() > 0 ? getAttributeValue("man-profileId", attributes).get(0).toString():"");
	    		ldapUser.setManAnswer(getAttributeValue("man-answers", attributes));
	    		ldapUser.setManChallengeAnswer(getAttributeValue("man-challengeAnswer", attributes).size() > 0 ? getAttributeValue("man-challengeAnswer", attributes).get(0).toString():"");
	    		ldapUser.setManNumericPassword(getAttributeValue("man-numericPassword", attributes).size() > 0 ? getAttributeValue("man-numericPassword", attributes).get(0).toString():"");
	    		ldapUser.setManProfileStatus(getAttributeValue("man-profileStatus", attributes).size() > 0 ? getAttributeValue("man-profileStatus", attributes).get(0).toString():"");
	    		ldapUser.setPin(getAttributeValue("man-ivrPIN", attributes).size() > 0 ? getAttributeValue("man-ivrPIN", attributes).get(0).toString():"");
	    		ldapUser.setPinStatus(getAttributeValue("man-ivrPINStatus", attributes).size() > 0 ? getAttributeValue("man-ivrPINStatus", attributes).get(0).toString():"");
	    		ldapUser.setOntChallenge(getAttributeValue("ont-challenge", attributes).size() > 0 ? getAttributeValue("ont-challenge", attributes).get(0).toString():"");
	    		temp.add(ldapUser);
	    	}
	    	return (LdapUser[]) temp.toArray(new LdapUser[]{});

		}

		private static ArrayList getAttributeValue(String attributeName, Attributes attributes) throws Exception
		{
			Attribute attr = attributes.get(attributeName);

			//String returnValue = "";
			ArrayList values = new ArrayList();
			if(attr != null)
			{
				NamingEnumeration innerEnum = attr.getAll();
				if(innerEnum.hasMore())
				{
					//returnValue = innerEnum.next().toString();
					values.add(innerEnum.next().toString());
				}
			}
			return values;
			//return returnValue;
		}

		private static boolean profileExists(String profileId,LdapUser[] users)
		{
			boolean returnValue = false;
			for(int i = 0; i < users.length; i++)
			{
				if(users[i].getProfileId().equals(profileId))
				{
					returnValue = true;
					break;
				}
			}

			return returnValue;

		}
	



}
