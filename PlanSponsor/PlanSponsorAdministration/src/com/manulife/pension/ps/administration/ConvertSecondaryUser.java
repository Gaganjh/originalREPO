package com.manulife.pension.ps.administration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.StringTokenizer;

import com.manulife.pension.lp.model.common.PasswordBean;
import com.manulife.pension.lp.model.common.UserIDBean;
import com.manulife.pension.service.security.SecurityService;
import com.manulife.pension.service.security.exception.DisabledContractException;
import com.manulife.pension.service.security.exception.DisabledUserException;
import com.manulife.pension.service.security.exception.DuplicateSSNException;
import com.manulife.pension.service.security.exception.FailedAlmostNTimesException;
import com.manulife.pension.service.security.exception.FailedNTimesException;
import com.manulife.pension.service.security.exception.IncorrectPasswordException;
import com.manulife.pension.service.security.exception.InsufficientPrivilegesException;
import com.manulife.pension.service.security.exception.LockedUserException;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.exception.UserAlreadyRegisteredException;
import com.manulife.pension.service.security.exception.UserNotFoundException;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * This class will be used Existing Secondary Users.
 *
 * @author Ilker Celikyilmaz
 */
public class ConvertSecondaryUser extends BaseAdministration{
	private static boolean verbose = false;

	/**
	 * Constructor for ConvertSecondaryUser
	 */
	public ConvertSecondaryUser() {
		super();
	}

	public static void main(String[] args) {
		if (args.length < 3 )
		{
			System.err.println("Usage: ConvertSecondaryUser.bat host:port fileName -v");
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
			logWriter.write("\n Convert existing Secondary Users" );
			logWriter.write("\n " + new Date());
			logWriter.write("\n***********************************************************");

			if ( verbose )
			{
				logWriter.write("\n Input parameters are:" );
				logWriter.write("\n host:port=" + args[0]);
				logWriter.write("\n filename=" + args[1]);
			}

			// set ejbServiceProviderURL
			ejbServiceProviderURL = "iiop://" + args[0];

			// convert the Secondary user
			convertSecondaryUsers(args[1]);
		}
		catch(Exception e)
		{
			System.err.println(e);
			e.printStackTrace();
		}

		try {
			logWriter.write("\n Conversion Finished at " + new Date());
			logWriter.close();
		} catch (java.io.IOException e) {}

		System.exit(0);
	}


	private static void convertSecondaryUsers(String fileName) throws Exception
	{
		SecurityService service = getSecurityService();


		try {

			Collection users = readUsers(fileName);

			UserInfo[] userInfoList = (UserInfo[]) users.toArray(new UserInfo[0]);
			int len = userInfoList.length;
			for (int i=0; i<len; i++)
			{
				try {
					service.validationForRegistration(userInfoList[i], userInfoList[i].getEmail());
					logWriter.write("\n Secondary User succesfully converted:" + userInfoList[i].getContractNumber() + ","+userInfoList[i].getSsn());
					//System.out.println(i + " Secondary User succesfully converted." + userInfoList[i].getContractNumber() + ","+userInfoList[i].getSsn());
				}
				catch (UserAlreadyRegisteredException ex)
				{
					logWriter.write("\n User already registered:" + userInfoList[i]);
				}
				catch (DisabledUserException ex)
				{
					logWriter.write("\n User is disabled:" + userInfoList[i]);
				}
				catch (UserNotFoundException ex)
				{
					logWriter.write("\n User not found:" + userInfoList[i]);
				}
				catch (IncorrectPasswordException ex)
				{
					logWriter.write("\n IncorrectPasswordException:" + userInfoList[i]);
				}
				catch (FailedAlmostNTimesException ex)
				{
					logWriter.write("\n FailedAlmostNTimesException:" + userInfoList[i]);
				}
				catch (FailedNTimesException ex)
				{
					logWriter.write("\n FailedNTimesException:" + userInfoList[i]);
				}
				catch (DisabledContractException ex)
				{
					logWriter.write("\n DisabledContractException:" + userInfoList[i]);
				}
				catch (LockedUserException ex)
				{
					logWriter.write("\n LockedUserException:" + userInfoList[i]);
				}
				catch (DuplicateSSNException ex)
				{
					logWriter.write("\n DuplicateSSNException:" + userInfoList[i]);
				}
				catch (InsufficientPrivilegesException ex)
				{
					logWriter.write("\n InsufficientPrivilegesException(No Contract Associated):" + userInfoList[i]);
				}
				catch (SecurityServiceException ex)
				{
					logWriter.write("\n SecurityService problem occurred for user:"+ userInfoList[i].getContractNumber() + ","+userInfoList[i].getSsn()+" -- " + ex.getDisplayMessage());
				}
				catch (Exception ex)
				{
					logWriter.write("\n Exception occurred for user:"+ userInfoList[i].getContractNumber() + ","+userInfoList[i].getSsn()+" -- " + ex.getMessage());
				}
			}
		}
		catch (Exception ex)
		{
			logWriter.write("\n Exception Occurred:" + ex.getMessage());
		}

	}


	private static Collection readUsers(String fileName) throws Exception
	{
		BufferedReader bufferedReader = null;
		Collection users = new ArrayList();

		 bufferedReader = new BufferedReader(new FileReader(fileName));

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

  	  userInfo.setContractNumber(new Integer(st.nextToken().trim()).intValue());
	  userInfo.setSsn(st.nextToken().trim());

	  // since password is encrypted we will have to decrypt it first
	  String encryptedPassword = st.nextToken().trim();
	  UserIDBean  userIDBean = new UserIDBean(userInfo.getSsn());
	  PasswordBean passwordBean = new PasswordBean(encryptedPassword);
	  userInfo.setPassword(passwordBean.decrypt(userIDBean).getValue());

	  // Email attribute is used to set the Company id
	  userInfo.setEmail(st.nextToken().trim());


	  if ( verbose )
	      logWriter.write("\nUser read: " + userInfo);

	  return userInfo;
   }

}

