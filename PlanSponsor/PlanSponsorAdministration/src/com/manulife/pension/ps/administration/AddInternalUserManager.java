package com.manulife.pension.ps.administration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;

import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.SecurityService;
import com.manulife.pension.service.security.exception.DuplicateEmployeeNumberException;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.exception.UserNameIsInUseException;
import com.manulife.pension.service.security.role.InternalUserManager;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * This class will be used by System Admins to add IUM user only.
 * 
 * @author Ilker Celikyilmaz
 */
public class AddInternalUserManager extends BaseAdministration{
	private static boolean verbose = false;

	/**
	 * Constructor for AddInternalUserManager
	 */
	public AddInternalUserManager() {
		super();
	}

	public static void main(String[] args) {
		if (args.length < 8 )
		{
			System.err.println("Usage: AddInternalUserManager.bat host:port userName password IUMusername IUMemployeeNumber IUMfirstName IUMlastName IUMemail -v");
			System.exit(0);			
		}
			
		 
		if (args.length == 9)
		{
			if ( args[8].equals("-v") || args[8].equals("-verbose") ) verbose = true;
		}
		     
		try {
		         
			new File("log.file").createNewFile();
			logWriter = new BufferedWriter(new FileWriter("log.file", true));
			
			logWriter.write("\n\n\n***********************************************************");
			logWriter.write("\n Add Internal User Manager" );
			logWriter.write("\n " + new Date());			 
			logWriter.write("\n***********************************************************");
			
			if ( verbose )
			{
				logWriter.write("\n Input parameters are:" );				
				logWriter.write("\n host:port=" + args[0]);								
				logWriter.write("\n userName=" + args[1]);								
				logWriter.write("\n IUMusername=" + args[3]);
				logWriter.write("\n IUMemployeeNumber="+args[4]);						
				logWriter.write("\n IUMfirstName=" + args[5]);												
				logWriter.write("\n IUMlastName=" + args[6]);																				
				logWriter.write("\n IUMemail=" + args[7]);																
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
				UserInfo addedUserInfo = addUser(principal,args[3], args[5], args[6], args[4], args[7]);
				if ( addedUserInfo == null )
				{
					System.err.println("adding user failed. Check the log for details.");
				}
				else 
				{
					logWriter.write("\n User added successfully:" + addedUserInfo);																									
					System.out.println("User added successfully.");					
				}
			}
			else
			{
				System.err.println("adding user failed. Check the log for details.");
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
	
	
	private static UserInfo addUser(Principal principal, String userName, String firstName,
				String lastName, String employeeNumber, String email) throws Exception
	{
		SecurityService service = getSecurityService();
		UserInfo userInfo = new UserInfo();
		userInfo.setFirstName(firstName);
		userInfo.setLastName(lastName);
		userInfo.setUserName(userName);
		userInfo.setEmployeeNumber(employeeNumber);
		userInfo.setEmail(email);
		userInfo.setRole(new InternalUserManager());
		
		
		UserInfo addedUserInfo = null;
		try {
			addedUserInfo = service.addUser(principal, userInfo, "US");
		}
		catch (DuplicateEmployeeNumberException ex)
		{
			logWriter.write("\n Employee Number already exists:" + employeeNumber);																					
		}
		catch (UserNameIsInUseException ex)		
		{
			logWriter.write("\n UserName is in use:" + userName);																								
		}
		catch (SecurityServiceException ex)		
		{
			logWriter.write("\n SecurityService problem:" + ex.getDisplayMessage());																		
		}
			
		return addedUserInfo;		
	}		

	
}

