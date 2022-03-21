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
import java.util.Iterator;
import java.util.StringTokenizer;

import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.SecurityService;
import com.manulife.pension.service.security.exception.DuplicateSSNException;
import com.manulife.pension.service.security.exception.InsufficientPrivilegesException;
import com.manulife.pension.service.security.exception.LockedUserException;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.exception.UserNotFoundException;
import com.manulife.pension.service.security.role.TPAUserManager;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.service.security.valueobject.UserSearchCriteria;

/**
 * This class will be used To Convert some of the
 * existing TPA user to TPAUM users
 *
 * @author Ilker Celikyilmaz
 */
public class ConvertTPAUMUsers extends BaseAdministration{
	private static boolean verbose = false;
	private static BufferedWriter reportWriter = null;

	/**
	 * Constructor for ConvertTPAUMUsers
	 */
	public ConvertTPAUMUsers() {
		super();
	}

	public static void main(String[] args) {
		args = new String[5];
		args[0] = "usgppresit11:900";
		args[1] = "c:\\projects\\tpaum\\TPAUMConversionCSVFileCycle3bRound2.txt";
		args[2] = "1009075";
		args[3] = "12345";
		args[4] = "c:\\projects\\tpaum\\report_for_tpa_to_tpaum_conversion_Cycle3bRound2.file";
		//args[5] = "-v";

		if (args.length < 5 )
		{
			System.err.println("Usage: ConvertTPAUMUsers.bat host:port userFileName principalUsername principalPassword reportFileName -v");
			System.exit(0);
		}


		if (args.length == 5)
		{
			if ( args[4].equals("-v") || args[4].equals("-verbose") ) verbose = true;
		}

		try {

			new File("c:\\projects\\tpaum\\convertTPAUM_log.file").createNewFile();
			logWriter = new BufferedWriter(new FileWriter("c:\\projects\\tpaum\\convertTPAUM_log.file", true));

			logWriter.write("\n\n\n***********************************************************");
			logWriter.write("\n Convert TPAUM Users" );
			logWriter.write("\n " + new Date());
			logWriter.write("\n***********************************************************");

			if ( verbose )
			{
				logWriter.write("\nInput parameters are:" );
				logWriter.write("\nhost:port=" + args[0]);
				logWriter.write("\nuserfilename=" + args[1]);
				logWriter.write("\nPrincipal Username=" + args[2]);
				logWriter.write("\nreort file name=" + args[4]);

			}

			// Create the report File
			new File(args[4]).createNewFile();
			reportWriter = new BufferedWriter(new FileWriter(args[4], true));
			reportWriter.write("\n" + new Date());
			reportWriter.write("\nProfile Identifier Type;TPA+PSW Username or SSN;Firm id;User's Name;Firm Name;Message");



			// set ejbServiceProviderURL
			ejbServiceProviderURL = "iiop://" + args[0];

			// convert the TPAUM users
			convertTPAUMUsers(args[1], args[2], args[3]);
		}
		catch(Exception e)
		{
			try {logWriter.write("\nProblem occurred:" + e.getMessage());} catch(IOException ie){}
			System.err.println(e);
			e.printStackTrace();
		}

		try {
			logWriter.write("\nConversion Finished at " + new Date());
			logWriter.close();
			reportWriter.close();
		} catch (java.io.IOException e) {}

		System.exit(0);
	}


	private static void convertTPAUMUsers(String fileName, String principalUsername, String principalPassword) throws Exception
	{
		SecurityService service = getSecurityService();


		try {

			Collection users = readUsers(fileName);
			Principal principal = authenticateUser(principalUsername, principalPassword);

			UserInfo[] userInfoList = (UserInfo[]) users.toArray(new UserInfo[0]);
			int len = userInfoList.length;
			for (int i=0; i<len; i++)
			{
				try {
					UserInfo userInfo = null;
					boolean isUserNameSearch = false;
					if ( userInfoList[i].getUserName() != null ) {
						isUserNameSearch = true;
						userInfo = service.searchByUserName(principal, userInfoList[i].getUserName());
						if ( userInfo == null  ) {
							reportWriter.write("\n" + "Username" + ";"+ userInfoList[i].getUserName() + ";" + userInfoList[i].getTpaFirmId() + ";N/A" + ";N/A" +";No user found during UserName search");
							continue;
						}
					}
					else
					{
						UserSearchCriteria searchCriteria = new UserSearchCriteria();
						searchCriteria.setSearchCriteria(UserSearchCriteria.SEARCH_BY_TPA_FIRM_ID);
						searchCriteria.setSearchObject(Integer.toString(userInfoList[i].getTpaFirmId()));
						Collection userList = service.searchUser(principal, searchCriteria);
						if  (userList == null || userList.isEmpty() )
						{
							reportWriter.write("\n" + "SSN" + ";"+ userInfoList[i].getSsn() + ";" + userInfoList[i].getTpaFirmId() + ";N/A" + ";N/A" +";No user found during TPA Firm Id search");
							continue;
						}
						else {
							// loop throug hte result of the search and find the TPA user with matching SSN
							Iterator it = userList.iterator();
							while( it.hasNext() )
							{
								UserInfo tempUser = (UserInfo) it.next();
								if ( userInfoList[i].getSsn().equals(tempUser.getSsn()) ) {
									userInfo = tempUser;
									break;
								}
							}

							if ( userInfo == null ) {
								reportWriter.write("\n" + "SSN" + ";"+ userInfoList[i].getSsn() + ";" + userInfoList[i].getTpaFirmId() + ";N/A" + ";N/A" +";No user found during SSN search");
								continue;
							} else { // To get all the user permissions we need to call searchByUserName

								userInfo = service.searchByUserName(principal, userInfo.getUserName());
								if ( userInfo == null  ) { // This should not HAPPEN
									reportWriter.write("\n" + "Username" + ";"+ userInfoList[i].getUserName() + ";" + userInfoList[i].getTpaFirmId() + ";N/A" + ";N/A" +";User foundduring TPA FIrm id+SSN search but could not be found during UserName search[THIS SHOULD NOT HAPPEN]");
									continue;
								}


							}
						}
					}

					TPAFirmInfo firm = userInfo.getTpaFirm(userInfoList[i].getTpaFirmId());
					if ( firm == null )
					{
						if ( isUserNameSearch )
							reportWriter.write("\n" + "Username" + ";"+ userInfoList[i].getUserName() + ";" + userInfoList[i].getTpaFirmId() + ";N/A" + ";N/A" +";TPA Firm is not associated with this user");
						else
							reportWriter.write("\n" + "SSN" + ";"+ userInfoList[i].getSsn() + ";" + userInfoList[i].getTpaFirmId() + ";N/A" + ";N/A" +";TPA Firm is not associated with this user");
					}
					else if ( firm.getContractPermission().getRole() instanceof TPAUserManager ) {
						if ( isUserNameSearch )
							reportWriter.write("\n" + "Username" + ";"+ userInfoList[i].getUserName() + ";" + userInfoList[i].getTpaFirmId() + ";" + userInfo.getFirstName() + " " + userInfo.getLastName() + ";" + firm.getName()  +";This user is already a TPAUM");
						else
							reportWriter.write("\n" + "SSN" + ";"+ userInfoList[i].getSsn() + ";" + userInfoList[i].getTpaFirmId() + ";" + userInfo.getFirstName() + " " + userInfo.getLastName() + ";" + firm.getName()  +";This user is already a TPAUM");
					}
					else {
						UserRole tpaRole = firm.getContractPermission().getRole();
						UserRole role = new TPAUserManager();
						role.removeAllPermissions();
						userInfo.setRole(role);
						userInfo.removeTpaFirm(firm);

						firm.getContractPermission().setRole(role);

						if ( tpaRole.hasPermission(PermissionType.DIRECT_DEBIT_ACCOUNT) )
							firm.getContractPermission().setDirectDebit(true);
						if ( tpaRole.hasPermission(PermissionType.CASH_ACCOUNT_ACCESS) )
							firm.getContractPermission().setCashAccount(true);
						if ( tpaRole.hasPermission(PermissionType.PARTICIPANT_ADDRESS_ACCESS) )
							firm.getContractPermission().setParticipantAddressDownloadAccessAvailable(true);
						if ( tpaRole.hasPermission(PermissionType.EMPLOYER_STATEMENT_ACCESS) )
							firm.getContractPermission().setStatementsAccessAvailable(true);
						if ( tpaRole.hasPermission(PermissionType.REPORT_DOWNLOAD) )
							firm.getContractPermission().setReportDownload(true);
						if ( tpaRole.hasPermission(PermissionType.UPLOAD_SUBMISSIONS) )
							firm.getContractPermission().setUploadSubmissions(true);
						if ( tpaRole.hasPermission(PermissionType.VIEW_ALL_SUBMISSIONS) )
							firm.getContractPermission().setViewAllSubmissions(true);
						if ( tpaRole.hasPermission(PermissionType.SUBMISSION_ACCESS) )
							firm.getContractPermission().setSubmissionAccess(true);
						if ( tpaRole.hasPermission(PermissionType.TPA_STAFF_PLAN_ACCESS) )
							firm.getContractPermission().setTpaStaffPlanAccess(true);
						if ( tpaRole.hasPermission(PermissionType.RECEIVE_ILOANS_EMAIL) )
							firm.getContractPermission().setReceiveIloansEmail(true);

						firm.getContractPermission().setSubmissionAccess(true);
						firm.getContractPermission().setDirectDebit(true);
						firm.getContractPermission().setViewAllSubmissions(true);
						firm.getContractPermission().setUploadSubmissions(true);
						firm.getContractPermission().setCashAccount(true);
						firm.getContractPermission().setReportDownload(true);

						// default permissions
						firm.getContractPermission().setParticipantAddressDownloadAccessAvailable(true);
						firm.getContractPermission().setStatementsAccessAvailable(true);



						userInfo.addTpaFirm(firm);
						userInfo.setUpdatedInformation(" Additions: [tpaFirms[" + userInfoList[i].getTpaFirmId() + "].planSponsorSiteRole=TUM]" +
								                       " Modifications: [tpaFirms["+ userInfoList[i].getTpaFirmId() + "].cashAccount=true], " +
													   " [tpaFirms["+ userInfoList[i].getTpaFirmId() + "].receiveTPAUMNotification=true], " +
													   " [tpaFirms["+ userInfoList[i].getTpaFirmId() + "].reportDownload=true], " +
													   " [tpaFirms["+ userInfoList[i].getTpaFirmId() + "].submissionAccess=true], " +
													   " [tpaFirms["+ userInfoList[i].getTpaFirmId() + "].uploadSubmissions=true], " +
													   " [tpaFirms[" + userInfoList[i].getTpaFirmId()
                                + "].viewAllSubmissions=true]");
						
						service.updateUser(principal, userInfo, "USA");

						if ( isUserNameSearch )
							reportWriter.write("\n" + "Username" + ";"+ userInfoList[i].getUserName() + ";" + userInfoList[i].getTpaFirmId() + ";" + userInfo.getFirstName() + " " + userInfo.getLastName() + ";" + firm.getName()  +";TPA user succesfully converted to TPAUM");
						else
							reportWriter.write("\n" + "SSN" + ";"+ userInfoList[i].getSsn() + ";" + userInfoList[i].getTpaFirmId() + ";" + userInfo.getFirstName() + " " + userInfo.getLastName() + ";" + firm.getName()  +";TPA user succesfully converted to TPAUM");

					}


				}
				catch (UserNotFoundException ex)
				{
					logWriter.write("\nUser not found:" + userInfoList[i]);
				}
				catch (LockedUserException ex)
				{
					logWriter.write("\nLockedUserException:" + userInfoList[i]);
				}
				catch (DuplicateSSNException ex)
				{
					logWriter.write("\nDuplicateSSNException:" + userInfoList[i]);
				}
				catch (InsufficientPrivilegesException ex)
				{
					logWriter.write("\nInsufficientPrivilegesException:" + userInfoList[i]);
				}
				catch (SecurityServiceException ex)
				{
					logWriter.write("\nSecurityService problem occurred for user:"+ userInfoList[i].getUserName() + ";"+userInfoList[i].getSsn()+ ";" + userInfoList[i].getTpaFirmId() +" -- " + ex.getDisplayMessage());
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
					logWriter.write("\nException occurred for user:"+ userInfoList[i].getUserName() + ";"+userInfoList[i].getSsn()+";"+ userInfoList[i].getTpaFirmId() +" -- " + ex.getMessage());
				}
			}
		}
		catch (Exception ex)
		{
			logWriter.write("\nException Occurred:" + ex.getMessage());
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

      String profileIdentifierType = st.nextToken().trim();
      String profileIdentifier = st.nextToken().trim();
      String firmId = st.nextToken().trim();

      if ( "ssn".equalsIgnoreCase(profileIdentifierType) )
      {
      	 userInfo.setSsn(profileIdentifier);
      	 userInfo.setUserName(null);
      } else if ( "username".equalsIgnoreCase(profileIdentifierType) ) {
     	 userInfo.setSsn(null);
      	 userInfo.setUserName(profileIdentifier);
      } else {
      	 reportWriter.write("\n" + profileIdentifierType + ";"+ profileIdentifier + ";" + firmId + ";N/A;N/A;Wrong profile identifier type");
      	 return null;
      }

      userInfo.setTpaFirmId(Integer.parseInt(firmId));

	  if ( verbose )
	      logWriter.write("\nUser read: " + userInfo);

	  return userInfo;
   }

}

