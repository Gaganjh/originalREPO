/*
 * Created on Aug 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.administration;

/**
 * @author antalro
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;
import java.io.*;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class DeletedUsersReport
{

	private static Properties jndiProperties = new Properties();
	private static String BASE_DN = "OU=U-2,OU=o-1,OU=organizations,O=Manulife,DC=mlifex01,DC=manulife,DC=com";

	static
	{
		jndiProperties.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		jndiProperties.put("java.naming.security.authentication", "simple");
		jndiProperties.put("java.naming.security.principal", "dsadmin");
		jndiProperties.put("java.naming.security.credentials", "dsadmin");
	}
	private static String FILE_NAME = "DeletedUsersReport.csv";
	private static String HEADER = "UserName,FirstName,LastName,ContractNumber,ContractName,Role,EmailAddress,DeletedByFirstName,DeletedByLastName,DeletedTime\n";


	private static Connection getMRLDatabaseConnection() throws Exception
	{
		Class.forName("com.ibm.db2.jcc.DB2Driver");
		Connection conn = java.sql.DriverManager.getConnection("jdbc:db2:MRLDMCSP","antalro","robert6pwd");
		conn.setAutoCommit(true);
		return conn;
	}
	private static Connection getCSDatabaseConnection() throws Exception
	{
		Class.forName("com.ibm.db2.jcc.DB2Driver");
		Connection conn = java.sql.DriverManager.getConnection("jdbc:db2:cs100p","ezcs_ro","ezcs_ro13");
		conn.setAutoCommit(true);
		return conn;
	}
	private static User[] retrieveDeletedUsersFromMRL(Date startDate) throws Exception
	{
		ArrayList temp = new ArrayList();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String sql = new String("select transaction_data from history.el_transaction where application_name='PS' and logging_point like '%delet%' and ts_date >= '"+dateFormatter.format(startDate)+"'");
		System.out.println("sql= "+sql);
		Connection connection = getMRLDatabaseConnection();
		Statement statement = connection.createStatement();

		ResultSet rs = statement.executeQuery(sql);
		User user = null;
		while(rs.next())
		{
			user = parseUserFromString(rs.getString(1));
			updateDeletedUsersInfoFromCSDB(user);
			temp.add(user);
		}
		statement.close();
		connection.close();
		System.out.println("retrieved= "+temp);
		return (User[]) temp.toArray(new User[temp.size()]);
	}

	private static void updateDeletedUsersInfoFromCSDB(User user) throws Exception
	{
		//ArrayList temp = new ArrayList();
		//SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		//String stringDate = parseDate(user.getTimeStamp());
		String profileID = getProfileID(user);
		String sql = new String("select first_Name, last_Name from psw100.user_profile where user_profile_id="+profileID);
		System.out.println("sql2= "+sql);
		Connection connection = getCSDatabaseConnection();
		Statement statement = connection.createStatement();

		ResultSet rs = statement.executeQuery(sql);
		while(rs.next())
		{
			user.setFirstName(rs.getString(1));
			user.setLastName(rs.getString(2));
		}
		statement.close();
		connection.close();
		//System.out.println("retrieved= "+temp);

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

	private static String getProfileID(User user) throws Exception
	{
		SearchControls controls = new SearchControls();
    	controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
    	StringBuffer filter = new StringBuffer("cn=");
    	filter.append(user.getUserName());
    	System.out.println("Filter="+filter);
		String returnValue = null;
		jndiProperties.put("java.naming.provider.url", "ldap://10.201.40.74:389");

		InitialDirContext dirContext = new InitialDirContext(jndiProperties);

		//System.out.println("getPIN filter= "+filter);

    	NamingEnumeration namingEnumeration = dirContext.search(BASE_DN, filter.toString(), controls);

    	while(namingEnumeration.hasMore())
    	{
    		SearchResult searchResult = (SearchResult) namingEnumeration.next();
    		Attributes attributes = searchResult.getAttributes();

    		returnValue = (getAttributeValue("man-profileId", attributes).length() > 0 ? getAttributeValue("man-profileId", attributes):"");

    	}
    	System.out.println("ProfileID retrieved from LDAP="+returnValue);
    	return returnValue;

	}


	public static void main(String[] args) throws Exception
	{
		Calendar calendar = GregorianCalendar.getInstance();
		Date today = calendar.getTime();
		calendar.add(Calendar.DATE,-7);
		Date startDate = calendar.getTime();

		User[] users = retrieveDeletedUsersFromMRL(startDate);
		writeToFile(users);
	}

	private static User parseUserFromString(String userInfo)
	{
		System.out.println(" about to parse "+userInfo);
		User returnValue = new User();
		StringTokenizer tokenizer = new StringTokenizer(userInfo,",");
		int count = tokenizer.countTokens();
		System.out.println("THERE were "+count+" tokens");
		String[] tokens = new String[count];
		for(int i = 0; i < count; i++)
		{
			tokens[i] = tokenizer.nextToken();
			System.out.println(i+"corresponds to"+tokens[i]);
		}
		returnValue.setUserName(tokens[0]);
		returnValue.setTimeStamp(tokens[4]);
		returnValue.setUpdatedByFirstName(tokens[5]);
		if(tokens.length > 9)
		{
			returnValue.setUpdatedByLastName(tokens[6]);
			returnValue.setEmail(tokens[7]);
			returnValue.setContractNumber(tokens[8]);
			returnValue.setContractName(tokens[9]);

			returnValue.setRoleName(tokens[10]);
		}


		return returnValue;
	}




	private static void writeToFile(User[] user) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(FILE_NAME)));
		writer.write(HEADER);
		for(int i = 0; i < user.length; i++)
		{
			writer.write(user[i].toString());
		}
		writer.flush();
		writer.close();
	}

	private static String parseDate(String stringDate)
	{
		StringBuffer returnValue = new StringBuffer();
		StringTokenizer tokenizer = new StringTokenizer(stringDate," ");
		HashMap months = new HashMap();
		if(stringDate != null)
		{
			String year = null;
			String month = null;
			String day = null;
			String hourMinutesSecond = null;
			months.put("Jan","01");
			months.put("Feb","02");
			months.put("Mar","03");
			months.put("Apr","04");
			months.put("May","05");
			months.put("Jun","06");
			months.put("Jul","07");
			months.put("Aug","08");
			months.put("Sep","09");
			months.put("Oct","10");
			months.put("Nov","11");
			months.put("Dec","12");
			// ignore the first token, which is the name of day
			tokenizer.nextToken();
			month = (String) months.get(tokenizer.nextToken());
			day = tokenizer.nextToken();
			StringBuffer hms = new StringBuffer(tokenizer.nextToken());
			for(int i = hms.length(); i > 0; i = i-2)
			{
				if(i != hms.length())
				{
					hms.insert(i,'.');
				}
			}
			// ignore the next token
			tokenizer.nextToken();
			year = tokenizer.nextToken();
			hourMinutesSecond = hms.toString();
			returnValue.append(year).append("-").append(month).append("-").append(day).append("-").append(hourMinutesSecond);
		}
		return returnValue.toString();
	}

}