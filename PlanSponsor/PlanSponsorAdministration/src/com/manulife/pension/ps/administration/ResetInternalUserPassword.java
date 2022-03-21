package com.manulife.pension.ps.administration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;

import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.SecurityService;
import com.manulife.pension.service.security.exception.DisabledUserException;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.exception.UserNotFoundException;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * This class will be used by System Admins to reset IUM user's password only.
 * 
 * @author Ilker Celikyilmaz
 */
public class ResetInternalUserPassword extends BaseAdministration{
	private static boolean verbose = false;

	/**
	 * Constructor for AddInternalUserManager
	 */
	public ResetInternalUserPassword() {
		super();
	}

	public static void main(String[] args) {
		if (args.length < 4 )
		{
			System.err.println("Usage: ResetInternalUserManagerPassword.bat host:port userName password IUMusername -v");
			System.exit(0);			
		}
			
		 
		if (args.length == 5)
		{
			if ( args[4].equals("-v") || args[4].equals("-verbose") ) verbose = true;
		}
		     
		try {
		         
			new File("log.file").createNewFile();
			logWriter = new BufferedWriter(new FileWriter("log.file", true));
			
			logWriter.write("\n\n\n***********************************************************");
			logWriter.write("\n Reset Internal User Manager's Password" );
			logWriter.write("\n " + new Date());			 
			logWriter.write("\n***********************************************************");
			
			if ( verbose )
			{
				logWriter.write("\n Input parameters are:" );				
				logWriter.write("\n host:port=" + args[0]);								
				logWriter.write("\n userName=" + args[1]);								
				logWriter.write("\n IUMusername=" + args[3]);
			}
			
			// set ejbServiceProviderURL
			ejbServiceProviderURL = "iiop://" + args[0];
 
			// authenticate the user
			Principal principal = authenticateUser(args[1], args[2]);

			if ( principal != null )
			{
				if ( verbose ) {
					logWriter.write("\n Principal:" + principal);																														
				}
				
				// add the IUM user  		 
				boolean isSuccessful = resetPassword(principal, args[3]);
				if ( !isSuccessful )
				{
					System.err.println("resetting password failed. Check the log for details.");
				}
				else 
				{
					logWriter.write("\n User's password reset successfully:" + args[3]);																									
					System.out.println("User's password reset successfully.");					
				}
			}
			else
			{
				System.err.println("resetting password failed. Check the log for details.");
			}
		}
		catch(Exception e)
		{
			System.err.println(e);
			e.printStackTrace();
		}

		try {
			logWriter.close();	
		} catch (java.io.IOException e) {}
	
		System.exit(0);
	}
	
	
	private static boolean resetPassword(Principal principal, String userName) throws Exception
	{
		SecurityService service = getSecurityService();
		UserInfo userInfo = new UserInfo();
		userInfo.setUserName(userName);
		
		boolean isSuccessful = false;
		
		try {
			service.resetPassword(principal, userInfo, "US");
			isSuccessful = true;
		}
		catch (UserNotFoundException ex)
		{
			logWriter.write("\n Username not found:" + userName);																					
		}
		catch (DisabledUserException ex)		
		{
			logWriter.write("\n User is disabled:" + userName);																								
		}
		catch (SecurityServiceException ex)		
		{
			logWriter.write("\n SecurityService problem:" + ex.getDisplayMessage());																		
		}
			
		return isSuccessful;		
	}		

	
}

