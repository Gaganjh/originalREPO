package com.manulife.pension.ps.administration;

import java.io.BufferedWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.ejb.EJBHome;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.SecurityService;
import com.manulife.pension.service.security.SecurityServiceHome;
import com.manulife.pension.service.security.exception.IncorrectPasswordException;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.exception.UserNotFoundException;
import com.manulife.pension.service.security.valueobject.LoginPSValueObject;

/**
 * This is the base class for System Admin realted activity classes.
 * 
 * @author Ilker Celikyilmaz
 */
public class BaseAdministration {
	protected static BufferedWriter logWriter = null;
	private static final String INITIAL_CONTEXT_FACTORY = "com.ibm.websphere.naming.WsnInitialContextFactory";
	private static Context initialContext = null;
	protected static String ejbServiceProviderURL = "";	
	
	protected static Context getInitialContext() throws Exception 
	{
		if ( initialContext == null )
		{
			
			Properties prop = new Properties();
			  			
			prop.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
			prop.put(Context.PROVIDER_URL, ejbServiceProviderURL);

			
			try {
				initialContext = new InitialContext(prop);
			}
			catch(Exception e)
			{
				System.err.println("InitialContext can't be created:"+e.getMessage());
				throw e;
			}
		}		
		
		return initialContext;
	}

	protected static SecurityService getSecurityService() throws Exception
	{	
		EJBHome home=null;
				
		Context context = getInitialContext();

		Object objref = context.lookup(SecurityServiceHome.class.getName());
		
        if (objref != null) 
        {
  	    	home = (EJBHome) PortableRemoteObject.narrow(objref, Class.forName(SecurityServiceHome.class.getName()));
        } 
        else 
        {
            throw new NamingException("Home not found " + SecurityServiceHome.class.getName());		
        }		


		SecurityServiceHome securityHome = (SecurityServiceHome)home;
		
		return securityHome.create();
	}
		
		
	protected static Principal authenticateUser(String userName, String password) throws Exception
	{
		SecurityService service = getSecurityService();
		Principal principal = null;
		
		try {
			LoginPSValueObject lvo = service.loginPS(userName, password, "US", null);
			principal = lvo.getPrincipal();
		}
		catch (UserNotFoundException ex)
		{
			logWriter.write("\n Admin User not Found:" + userName);																					
		}
		catch (IncorrectPasswordException ex)		
		{
			logWriter.write("\n Admin User's Password is incorrect:" + userName);																								
		}
		catch (SecurityServiceException ex)		
		{
			logWriter.write("\n SecurityService problem:" + ex.getDisplayMessage());																		
		}
		
		return principal;
	}	
	
	protected static Connection getConnection(String databaseURL, String userId, String passwd) throws Exception {
		Class.forName("com.ibm.db2.jcc.DB2Driver");
		Connection connection = java.sql.DriverManager.getConnection(databaseURL, userId, passwd);
		return connection;		
	}	
	
	public static void close(Statement stmt, Connection connection) {
		try {
			if (stmt != null) {
                /*
                 * Close all the result sets in the statement. This seems to be
                 * necessary because these ResultSets are not originated from
                 * this statement but as parameters in the stored proc.
                 */
                while (stmt.getMoreResults()) {
                    stmt.getResultSet();
                }
                stmt.close();
            }
		} catch (SQLException e) {
			/*
			 * this section was commented out to allow DAOs catch SQLExceptions and throw
			 * other exceptions if needed such as RetryableConnection
			 * Exceptions raised when closing connections would only overwrite and mask
			 * earlier exceptions
			 */
		}

		/*
		 * Even if we fail to close the statement, we can still try to close the
		 * connection and return the resource to the pool.
		 */
		try {
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			/*
			 * this section was commented out to allow DAOs catch SQLExceptions and throw
			 * other exceptions if needed such as RetryableConnection
			 * Exceptions raised when closing connections would only overwrite and mask
			 * earlier exceptions
			 */
		}
	}	
}

