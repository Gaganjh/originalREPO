
import com.manulife.pension.service.security.SecurityService;
import com.manulife.pension.service.security.SecurityServiceHome;
import com.manulife.pension.service.security.valueobject.UserInfo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;



/**
 * 
 * 
 * @author Ilker Celikyilmaz
 */
public class RevertPINs {
	private static boolean verbose = false;
	private static final String INITIAL_CONTEXT_FACTORY = "com.ibm.websphere.naming.WsnInitialContextFactory";		
	private static Context initialContext = null;	
	protected static String ejbServiceProviderURL = "";	
	private static BufferedWriter logWriter = null;	

	/**
	 * Constructor for AddInternalUserManager
	 */
	public RevertPINs() {
		super();
	}

	public static void main(String[] args) {
		if (args.length < 2 )
		{
			System.err.println("Usage: RevertPINs.bat host:port -v");
			System.exit(0);			
		}
			
		 
		if (args.length == 3)
		{
			if ( args[2].equals("-v") || args[2].equals("-verbose") ) verbose = true;
		}
		     
		try {
		         
			new File("log.file").createNewFile();
			logWriter = new BufferedWriter(new FileWriter("log.file", true));
			
			logWriter.write("\n\n\n***********************************************************");
			logWriter.write("\n Revert PINs" );
			logWriter.write("\n " + new Date());			 
			logWriter.write("\n***********************************************************");
			
			if ( verbose )
			{
				logWriter.write("\n Input parameters are:" );				
				logWriter.write("\n host:port=" + args[0]);								
			}
			
			// set ejbServiceProviderURL
			ejbServiceProviderURL = "iiop://" + args[0];
 
			SecurityService security = getSecurityService();
			Collection users = readUsers();
			
			UserInfo[] userInfoList = (UserInfo[]) users.toArray(new UserInfo[0]);
			int len = userInfoList.length;
			for (int i=0; i<len; i++)
			{
				security.upsert((new Long(userInfoList[i].getProfileId())).toString(), "I", userInfoList[i].getPassword(), "us");	
				logWriter.write("\n User's PIN reverted successfully:" + userInfoList[i].getProfileId());																									
			}
			
			System.out.println("Revert is successful.");
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
	
	
//	private static boolean upsert(Principal principal, String userName) throws Exception
//	{
//		SecurityService service = getSecurityService();
//		UserInfo userInfo = new UserInfo();
//		userInfo.setUserName(userName);
//		
//		boolean isSuccessful = false;
//		
//		try {
//			service.resetPassword(principal, userInfo, "US");
//			isSuccessful = true;
//		}
//		catch (UserNotFoundException ex)
//		{
//			logWriter.write("\n Username not found:" + userName);																					
//		}
//		catch (DisabledUserException ex)		
//		{
//			logWriter.write("\n User is disabled:" + userName);																								
//		}
//		catch (SecurityServiceException ex)		
//		{
//			logWriter.write("\n SecurityService problem:" + ex.getDisplayMessage());																		
//		}
//			
//		return isSuccessful;		
//	}		


	protected static Context getInitialContext() throws javax.naming.NamingException 
	{
		if ( initialContext == null )
		{
			Properties prop = new Properties();
			  			
			prop.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
			prop.put(Context.PROVIDER_URL, ejbServiceProviderURL);

			try {
				initialContext = new InitialContext(prop);
			}
			catch(NamingException e)
			{
				System.err.println("InitialContext can't be created:"+e.getMessage());
				throw e;
			}
		}		
		
		return initialContext;
	}

	protected static SecurityService getSecurityService() throws NamingException, ClassNotFoundException, RemoteException, CreateException
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
	
	
	private static Collection readUsers() throws Exception
	{
		BufferedReader bufferedReader = null;
		Collection users = new ArrayList();		
		
		 bufferedReader = new BufferedReader(new FileReader("C:\\cvs\\PlanSponsor\\PlanSponsorAdministration\\bin\\XYZ.csv")); //EZkRevertPINContent.file
	
		 String line;
		 while ((line = bufferedReader.readLine()) != null)
		 {
		 	UserInfo userInfo = processLine(line.trim());
		 	if ( userInfo != null )
		 		users.add(userInfo);
		 }
		
		 bufferedReader.close();
		
		return users;

	} 
	
	
   private static UserInfo processLine(String line) throws IOException
   {
      if (line.startsWith("#")) return null;

      UserInfo userInfo = new UserInfo();
      StringTokenizer st = new StringTokenizer(line, ",");

      userInfo.setProfileId((new Long(st.nextToken().trim())).longValue());
      userInfo.setPassword(st.nextToken().trim());

	  if ( verbose )
	      logWriter.write("\nUser read: " + userInfo);
		
	  return userInfo;	
   }	
}

