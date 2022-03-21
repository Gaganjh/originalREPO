package com.manulife.pension.ps.administration;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.SecurityService;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.service.security.valueobject.UserSearchCriteria;

/**
 * This class will be used by System Admins to add IUM user only.
 * 
 * @author Ilker Celikyilmaz
 */
public class SearchInternalUsers extends BaseAdministration{
	private static boolean verbose = false;

	/**
	 * Constructor for SearchInternalUsers
	 */
	public SearchInternalUsers() {
		super();
	}

	public static void main(String[] args) {
		args = new String[3];
		args[0] = "mlisezkpiws2:2810";
		args[1] = "systone";
		args[2] = "Only4Us";

		//args[0] = "localhost:2810";
		//args[1] = "1009083";
		//args[2] = "12345";
		     
		try {
		         
			new File("webaccess.csv").createNewFile();
			logWriter = new BufferedWriter(new FileWriter("webaccess.csv", true));
			
			logWriter.write("\n***********************************************************");
			logWriter.write("\n RPS Internal Users" );
			logWriter.write("\n " + new Date());			 
			logWriter.write("\n***********************************************************");
			
			 
			// set ejbServiceProviderURL
			ejbServiceProviderURL = "iiop://" + args[0];
 
			// authenticate the user
			Principal principal = authenticateUser(args[1], args[2]);

			if ( principal != null )
			{
				if ( verbose ) {
					logWriter.write("\n Principal:" + principal);																														
				}
				
				// search Internal users  		 
				Collection internalUsers = searchUsers(principal);
				if ( internalUsers == null )
				{
					logWriter.write("Internal User Manager not found.");
				}
				else 
				{
					Iterator it = internalUsers.iterator();
					logWriter.write("\nEmployee Number,User Name,First Name,Last Name,Email,Ezk Role,PlanSponsor Role");					
					while(it.hasNext())
					{
										
						
						UserInfo intUser = (UserInfo) it.next();
						
						intUser = searchUser(principal, intUser.getUserName());
						
						logWriter.write("\n" + 
									"\"" + intUser.getEmployeeNumber() + "\"," +
									"\"" + intUser.getUserName() + "\"," +
									"\"" + intUser.getFirstName() + "\"," +
									"\"" + intUser.getLastName() + "\"," +
									"\"" + intUser.getEmail() + "\"," +
									"\"" + intUser.getParticipantRole() + "\"," +
									"\"" + intUser.getRole() + "\"");
					}
					
				}
			}
			else
			{
				logWriter.write("searching user failed. Check the log for details.");
			}
		}
		catch(Exception e)
		{
			try {
				StringWriter wr = new StringWriter();
				PrintWriter pw = new PrintWriter(wr);
				e.printStackTrace(pw);
			   logWriter.write(wr.getBuffer().toString());
			} catch (Exception ex) {}
			e.printStackTrace();
		}

		try {
			logWriter.close();	
		} catch (java.io.IOException e) {}
	
		System.exit(0);
	}
	
	
	private static Collection searchUsers(Principal principal) throws Exception
	{
		SecurityService service = getSecurityService();
		UserSearchCriteria searchCriteria = new UserSearchCriteria();
		searchCriteria.setSearchCriteria(UserSearchCriteria.SEARCH_BY_INTERNAL_USER_LASTNAME);
		searchCriteria.setSearchObject("");
		Collection internalUsers = null;
		
		try {
			internalUsers = service.searchUser(principal, searchCriteria);
		}
		catch (SecurityServiceException ex)		
		{
			logWriter.write("\n SecurityService problem:" + ex.getDisplayMessage());																		
		}
			
		return internalUsers;		
	}		

	private static UserInfo searchUser(Principal principal, String userName) throws Exception
	{
		SecurityService service = getSecurityService();
		UserInfo internalUser = null;
		
		try {
			internalUser = service.searchByUserName(principal, userName);
		}
		catch (SecurityServiceException ex)		
		{
			logWriter.write("\n SecurityService problem (searchByUserName):" + ex.getDisplayMessage());																		
		}
			
		return internalUser;		
	}		

	
	
}
