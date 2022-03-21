package com.manulife.pension.ps.administration;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.naming.Context;

import com.manulife.pension.ps.administration.valueobject.TpaUser;
import com.manulife.pension.ps.administration.valueobject.TpaUserKey;
import com.manulife.pension.service.dao.BaseDatabaseDAO.SqlStringBuffer;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.SecurityService;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.service.security.valueobject.UserSearchCriteria;
import com.manulife.util.tools.MessageFormatHelper;

/**
 * This class will be used to convert(update)all 
 * TPA users on Apollo with receive i:loan email
 * permission to PSW or report all TAP users on Apollo
 * with the latet status 
 * 
 * @author Ilker Celikyilmaz
 */
public class ConvertReportTPAUsersWithReceiveILoanEmailPermission extends BaseAdministration{
	private static boolean verbose = false;

	private static final String APOLLO_SCHEMA_NAME = "apollo.schema.name";
	private static Object[] QUERY_SUBSTITUTIONS;	
	private static final String APOLLO_DATA_SOURCE_NAME = "jdbc/apollo";	
	private static final Date conversionDate = new Date();
	private static final Timestamp conversionTimestamp = new Timestamp(conversionDate.getTime());
	/**
	 * Constructor for SearchInternalUsers
	 */
	public ConvertReportTPAUsersWithReceiveILoanEmailPermission() {
		super();
	}

	public static void main(String[] args) {
		args = new String[6];
		args[0]="usgppresit11:900";
		args[1]="1009075";
		args[2]="12345";
		//args[3]="DB2LPD1B"; // PRESIT1
		args[3]="DB2LPDCB"; // PRESIT11
		args[4]="report";
		args[5]="-v";
		
		if (args.length < 5 )
		{
			System.err.println("Usage: ConvertReportTPAUsers.bat host:port userName password apolloSchema report/convert -v");
			System.exit(0);			
		}
			
		 
		if (args.length == 6)
		{
			if ( args[5].equals("-v") || args[5].equals("-verbose") ) verbose = true;
		}
		     
		try {
		         
			new File("convertReportTPAUserlog.file").createNewFile();
			logWriter = new BufferedWriter(new FileWriter("convertReportTPAUserlog.file", true));
			
			logWriter.write("\n\n\n***********************************************************");
			logWriter.write("\n " + args[4] + " TPA Users" );
			logWriter.write("\n " + conversionDate);			 
			logWriter.write("\n***********************************************************");
			
			if ( verbose )
			{
				logWriter.write("\n Input parameters are:" );				
				logWriter.write("\n host:port=" + args[0]);								
				logWriter.write("\n userName=" + args[1]);								
				logWriter.write("\n password=" + args[2]);
				logWriter.write("\n Apollo Schema="+args[3]);				
				logWriter.write("\n Run Type="+args[4]);						
			}
			
			boolean isConversionMode = args[4].equals("convert");
			
			QUERY_SUBSTITUTIONS = new Object[] {args[3]};			
			
			// set ejbServiceProviderURL
			ejbServiceProviderURL = "iiop://" + args[0];
 
			Context context = getInitialContext();		
			
			Map tpaUsersWithReceiveIloanEmailPerm = getTpaUsersWithReceiveILoanPermission();
			Map tpaUsersWithoutReceiveIloanEmailPerm = getTpaUsersWithoutReceiveILoanPermission();
			Map allApolloTpaUsers = new HashMap();
			allApolloTpaUsers.putAll(tpaUsersWithReceiveIloanEmailPerm);
			allApolloTpaUsers.putAll(tpaUsersWithoutReceiveIloanEmailPerm);
			
			// authenticate the user
			Principal principal = authenticateUser(args[1], args[2]);

			if ( principal != null )
			{
				if ( verbose ) {
					logWriter.write("\n Principal:" + principal);																														
				}
				
				Collection conversionHistory = getConverisonHistory();
				
				for (Iterator it = conversionHistory.iterator(); it.hasNext();) {
					TpaUser convertedTpaUser = (TpaUser) it.next();
					
					Object tempUser = allApolloTpaUsers.get(new TpaUserKey(convertedTpaUser.getFirmId(), convertedTpaUser.getSsn()));
					if ( tempUser == null ) {
						logWriter.write(" User From Conversion History does not exist on Apollo firm id =" + convertedTpaUser.getFirmId() + ", ssn="+convertedTpaUser.getSsn());
					}
					else {
						TpaUser tpaUser = (TpaUser) tempUser; 
						tpaUser.setConversionDate(convertedTpaUser.getConversionDate());
						tpaUser.setMatchingILoansPermissionActionOnPSW(convertedTpaUser.getMatchingILoansPermissionActionOnPSW());
					}
				}				
				
				// get TPA users from PSW
				UserSearchCriteria searchCriteria = new UserSearchCriteria();
				searchCriteria.setSearchCriteria(UserSearchCriteria.SEARCH_BY_TPA_LASTNAME);
				searchCriteria.setSearchObject("");
				SecurityService service = getSecurityService();
				ArrayList pswTpaUsers = service.searchUser(principal, searchCriteria);
				
				for (Iterator it=pswTpaUsers.iterator(); it.hasNext();) {
					UserInfo userInfo = (UserInfo)it.next();
					
					String pswSsn = userInfo.getSsn().trim();
					
					UserInfo userInfoDetailed = service.searchByUserName(principal, userInfo.getUserName());
					
					boolean userInfoUpdated = false;
					for (Iterator itPs = userInfoDetailed.getTpaFirmsAsCollection().iterator();itPs.hasNext();) {
						TPAFirmInfo firmInfo = (TPAFirmInfo) itPs.next();
						
						Object tempUser = allApolloTpaUsers.get(new TpaUserKey(Integer.toString(firmInfo.getId()), pswSsn));
						if ( tempUser == null ) {
							TpaUser tpaUser = new TpaUser(); 
							tpaUser.setFirmId(Integer.toString(firmInfo.getId()));
							tpaUser.setFirmName(firmInfo.getName());
							tpaUser.setSsn(pswSsn);
							tpaUser.setPswFirstName(userInfoDetailed.getFirstName());
							tpaUser.setPswLastName(userInfoDetailed.getLastName());
							if ( firmInfo.getContractPermission() != null ) {
								tpaUser.setPswStaffPlanPermission(firmInfo.getContractPermission().isTpaStaffPlanAccess());
								tpaUser.setPswReceiveIloanEmailPermission(firmInfo.getContractPermission().isReceiveIloansEmail());
							}
							tpaUser.setPswStatus(userInfoDetailed.getProfileStatus());
							tpaUser.setPswEmail(userInfoDetailed.getEmail());
							
							allApolloTpaUsers.put(new TpaUserKey(tpaUser.getFirmId(), pswSsn), tpaUser);
						}
						else {
							TpaUser tpaUser = (TpaUser) tempUser; 
							tpaUser.setPswFirstName(userInfoDetailed.getFirstName());
							tpaUser.setPswLastName(userInfoDetailed.getLastName());
							if ( firmInfo.getContractPermission() != null ) {
								tpaUser.setPswStaffPlanPermission(firmInfo.getContractPermission().isTpaStaffPlanAccess());
								tpaUser.setPswReceiveIloanEmailPermission(firmInfo.getContractPermission().isReceiveIloansEmail());
							}
							tpaUser.setPswStatus(userInfoDetailed.getProfileStatus());
							tpaUser.setPswEmail(userInfoDetailed.getEmail());
							
							// this execution is for conversion 
							if ( isConversionMode ) {
								if ( tpaUser.getConversionDate() == null &&
									 tpaUser.getPswStatus() != null ) {
									if ( tpaUser.isApolloReceiveIloanEmailPermission() && !tpaUser.isPswReceiveIloanEmailPermission() )
									{
										firmInfo.getContractPermission().setReceiveIloansEmail(true);
										userInfoUpdated = true;
										tpaUser.setMatchingILoansPermissionActionOnPSW("Changed to Yes");
									}
									else if ( tpaUser.isApolloReceiveIloanEmailPermission() && tpaUser.isPswReceiveIloanEmailPermission() )
										tpaUser.setMatchingILoansPermissionActionOnPSW("Left it as is (Yes)");
									else if ( !tpaUser.isApolloReceiveIloanEmailPermission() && tpaUser.isPswReceiveIloanEmailPermission() )
										tpaUser.setMatchingILoansPermissionActionOnPSW("Left it as is (Yes)");
									else if ( !tpaUser.isApolloReceiveIloanEmailPermission() && !tpaUser.isPswReceiveIloanEmailPermission() )
										tpaUser.setMatchingILoansPermissionActionOnPSW("Left it as is (No)");
									
									Date conversionDate = updateConversionHistory(tpaUser.getFirmId(), tpaUser.getSsn(), tpaUser.getMatchingILoansPermissionActionOnPSW());
									tpaUser.setConversionDate(conversionDate);
								}
							}
							
						}
					}
					
					if ( userInfoUpdated )
						service.updateUser(principal, userInfoDetailed, "USA");
					
				}
				
				
				new File("convertReportTPAUsers_report_only_Feb2.txt").createNewFile();
				BufferedWriter csvWriter = new BufferedWriter(new FileWriter("convertReportTPAUsers_report_only_Feb2.txt", true));
				
				csvWriter.write("" + conversionDate);
				
				StringBuffer tmp = new StringBuffer();
				tmp.append("\n");
				tmp.append("FirmId");
				tmp.append(";");
				tmp.append("Firm Name");
				tmp.append(";");
				tmp.append("Ssn");
				tmp.append(";");
				tmp.append("Apollo First Name");
				tmp.append(";");
				tmp.append("Apollo Last Name");
				tmp.append(";");
				tmp.append("Apollo Email");
				tmp.append(";");
				tmp.append("Apollo Staff Plan Permission");
				tmp.append(";");
				tmp.append("Apollo Receive Iloan Email Permission");
				tmp.append(";");
				tmp.append("Apollo Primary TPA");
				tmp.append(";");
				tmp.append("Psw First Name");
				tmp.append(";");
				tmp.append("Psw Last Name");
				tmp.append(";");
				tmp.append("Psw Staff Plan Permission");
				tmp.append(";");
				tmp.append("Psw Status");
				tmp.append(";");
				tmp.append("Psw Receive Iloan Email Permission");
				if ( isConversionMode ) 
					tmp.append(" (Prior to this run)");

				tmp.append(";");
				tmp.append("Psw Email");
				tmp.append(";");
				tmp.append("Conversion Date");
				tmp.append(";");
				tmp.append("Action on Receive ILoan Email Permission on PSW");
				if ( isConversionMode ) 
					tmp.append(";Converted in this run");
				
				csvWriter.write(tmp.toString());				
			
				Collection allUsers = allApolloTpaUsers.values();
				for (Iterator it = allUsers.iterator(); it.hasNext();)
				{
					TpaUser user = (TpaUser)it.next();
					csvWriter.write(convertToCSV(user, isConversionMode));
				}				
				
				csvWriter.close();				
			}
			else
			{
				System.err.println("Failed. Check the log for details.");
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
	
	
	private static Map getTpaUsersWithReceiveILoanPermission() throws Exception {
		return getTpaUsersFromApollo(true);
	}

	private static Map getTpaUsersWithoutReceiveILoanPermission() throws Exception {
		return getTpaUsersFromApollo(false);
	}	
	
	private static Map getTpaUsersFromApollo(boolean withReceiveIloansEmail) throws Exception {
		Connection connection = null;
		PreparedStatement stmt = null;
		boolean result = false;

		if (verbose) {
			logWriter.write("\nentry -> getTpaUsersFromApollo");
		}

		Map tpaUsers = new HashMap();		
		
		try {
			// This has been ixed in order to fix compilation problems. This is an obsolote
			// report so there is no need to fxi it properly
			connection = getConnection("APOLLO_DATA_SOURCE_NAME","","");
	
			stmt = connection.prepareStatement(withReceiveIloansEmail ? getTpaUsersFromApolloWithReceiveILoanEmailPermissionSql() :
																		getTpaUsersFromApolloWithoutReceiveILoanEmailPermissionSql());
			ResultSet rs = stmt.executeQuery();
			
			
			while (rs.next()) {
				TpaUser tpaUser = new TpaUser();
				tpaUser.setSsn(rs.getString("SSN").trim());
				tpaUser.setApolloFirstName(rs.getString("FIRST_NAME"));
				tpaUser.setApolloLastName(rs.getString("LAST_NAME"));
				tpaUser.setApolloStaffPlanPermission(rs.getString("STAFF_PLAN_PERM").equals("Y") ? true : false);
				tpaUser.setFirmId(rs.getString("FIRM_ID").trim());
				tpaUser.setFirmName(rs.getString("FIRM_NAME"));
				tpaUser.setApolloReceiveIloanEmailPermission(rs.getBoolean("RCV_ILOAN_EMAIL_PR"));
				tpaUser.setApolloEmail(rs.getString("EMAIL"));
				tpaUser.setPrimaryTpa(rs.getBoolean("PRIMARY_TPA"));				

				
				tpaUsers.put(new TpaUserKey(tpaUser.getFirmId(), tpaUser.getSsn()), tpaUser);
			}
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(stmt, connection);
		}
		
		if (verbose) {
			logWriter.write("\nexit <- getTpaUsersFromApollo");
		}
		
		return tpaUsers;
	}

	


	private static String getTpaUsersFromApolloWithReceiveILoanEmailPermissionSql() {
		SqlStringBuffer sql = new SqlStringBuffer();
		
			        sql.append("SELECT ")
				    .append("BASE.FIRM_ID ")  
				    .append(",	BASE.FIRM_NAME ")  
				    .append("	,	BASE.SSN ")  
				    .append(",	BASE.STAFF_PLAN_PERM ")  
				    .append(",	BASE.FIRST_NAME ")  
				    .append(",	BASE.LAST_NAME ")  
				    .append(",	BASE.EMAIL ")
				    .append(",	BASE.RCV_ILOAN_EMAIL_PR ")
				    .append(",	CASE WHEN LENGTH(RTRIM(TpaumUser.USRSSN)) <> 0 THEN 'True' ELSE 'False' END PRIMARY_TPA ")
				    .append("	FROM ")
				    .append("( ")
				 
					.append("SELECT ")
				    .append("A.FIRM_ID ")
                    .append(",	A.FIRM_NAME ")
				    .append(",	A.SSN ")
				    .append(",	A.STAFF_PLAN_PERM ")
				    .append(",	A.FIRST_NAME ")
				   	.append(",	A.LAST_NAME ")
				   	.append(",	A.EMAIL ")					
				   	.append(",	CASE ")
				   	.append("WHEN LENGTH(RTRIM(Firm.USRSSN)) = 0 OR Firm.USRSSN IS NULL then 'False' ") 
				 	.append("					ELSE 'True' ")
				    .append("					END AS RCV_ILOAN_EMAIL_PR ")
				   	.append("				FROM ( ")
				   	.append("				SELECT  ")
				   	.append("					Firm.XPAID AS FIRM_ID ")
				   	.append(",	Firm.XPANAM AS FIRM_NAME ")
				   	.append(",	TpaUser.USRSSN AS SSN ")
				   	.append(",	TpaUserAccess.TPASTFPL AS STAFF_PLAN_PERM ")
				   	.append(",	TpaUser.USRFSTNM FIRST_NAME ")
				   	.append(",	TpaUser.USRLSTNM LAST_NAME ")
					.append(",	TpaUser.USREMAIL EMAIL ")					
				   	.append("FROM    ")
				   	.append("{0}.VLP1102 Firm ")
				   	.append(",	{0}.VLP1208 TpaUserAccess ")
				   	.append(",	{0}.VLP1206_NOPIN TpaUser ")
                    .append("WHERE   ")
                    .append("TpaUser.USRSSN = TpaUserAccess.USRSSN ")
                    .append("AND	TpaUserAccess.ACSTYP = 'T' ")
                    .append("AND	Firm.XPAID = TpaUserAccess.ACSGRP) AS A ( ")
                    .append("FIRM_ID ")
                    .append(",	FIRM_NAME ")
                    .append(",	SSN ")
                    .append(",	STAFF_PLAN_PERM ")
                    .append(",	FIRST_NAME ")
                    .append(",	LAST_NAME ")
                    .append(",	EMAIL ")					
                    .append(") LEFT OUTER JOIN {0}.VLP1102 Firm ON ( ")
                    .append("Firm.XPAID = A.FIRM_ID ")
                    .append("AND	Firm.USRSSN = A.SSN ")
                    .append(") ")
                    .append("WHERE ") 
                    .append("LENGTH(RTRIM(Firm.USRSSN)) <> 0 AND Firm.USRSSN IS NOT NULL ")

			        
					.append(") AS BASE ( ")
					.append("		FIRM_ID ")  
					.append("	,	FIRM_NAME ")  
					.append("	,	SSN   ")
					.append("	,	STAFF_PLAN_PERM ")  
					.append("	,	FIRST_NAME   ")
					.append("	,	LAST_NAME   ")
					.append("	,	EMAIL ")
					.append("	,	RCV_ILOAN_EMAIL_PR ")
                    .append("	) LEFT OUTER JOIN {0}.VLP1207 TpaumUser ON ( ")
					.append("		BASE.FIRM_ID = TpaumUser.ACSGRP ")
					.append("	AND	BASE.SSN = TpaumUser.USRSSN ")
					.append("	AND	TpaumUser.ACSTYP = 'T' ")
					.append("	)					 ");			        

		String query = MessageFormatHelper.format(sql.toString(),
				QUERY_SUBSTITUTIONS);
System.out.println(query);
		return query;
	}

	private static String getTpaUsersFromApolloWithoutReceiveILoanEmailPermissionSql() {
		SqlStringBuffer sql = new SqlStringBuffer();

	
		         sql.append("SELECT ")
				    .append("BASE.FIRM_ID ")  
				    .append(",	BASE.FIRM_NAME ")  
				    .append("	,	BASE.SSN ")  
				    .append(",	BASE.STAFF_PLAN_PERM ")  
				    .append(",	BASE.FIRST_NAME ")  
				    .append(",	BASE.LAST_NAME ")  
				    .append(",	BASE.EMAIL ")
				    .append(",	BASE.RCV_ILOAN_EMAIL_PR ")
				    .append(",	CASE WHEN LENGTH(RTRIM(TpaumUser.USRSSN)) <> 0 THEN 'True' ELSE 'False' END PRIMARY_TPA ")
				    .append("	FROM ")
				    .append("( ")
					
				 	.append("SELECT ")
				    .append("A.FIRM_ID ")
                    .append(",	A.FIRM_NAME ")
				    .append(",	A.SSN ")
				    .append(",	A.STAFF_PLAN_PERM ")
				    .append(",	A.FIRST_NAME ")
				   	.append(",	A.LAST_NAME ")
				   	.append(",	A.EMAIL ")					
				   	.append(",	CASE ")
				   	.append("WHEN LENGTH(RTRIM(Firm.USRSSN)) = 0 OR Firm.USRSSN IS NULL then 'False' ") 
				 	.append("					ELSE 'True' ")
				    .append("					END AS RCV_ILOAN_EMAIL_PR ")
				   	.append("				FROM ( ")
				   	.append("				SELECT  ")
				   	.append("					Firm.XPAID AS FIRM_ID ")
				   	.append(",	Firm.XPANAM AS FIRM_NAME ")
				   	.append(",	TpaUser.USRSSN AS SSN ")
				   	.append(",	TpaUserAccess.TPASTFPL AS STAFF_PLAN_PERM ")
				   	.append(",	TpaUser.USRFSTNM FIRST_NAME ")
				   	.append(",	TpaUser.USRLSTNM LAST_NAME ")
					.append(",	TpaUser.USREMAIL EMAIL ")					
				   	.append("FROM    ")
				   	.append("{0}.VLP1102 Firm ")
				   	.append(",	{0}.VLP1208 TpaUserAccess ")
				   	.append(",	{0}.VLP1206_NOPIN TpaUser ")
                    .append("WHERE   ")
					.append("TpaUser.USRSSN = TpaUserAccess.USRSSN ")
					.append("AND	TpaUserAccess.ACSTYP = 'T' ")
					.append("AND	Firm.XPAID = TpaUserAccess.ACSGRP) AS A ( ")
					.append("FIRM_ID ")
					.append(",	FIRM_NAME ")
					.append(",	SSN ")
					.append(",	STAFF_PLAN_PERM ")
					.append(",	FIRST_NAME ")
					.append(",	LAST_NAME ")
                    .append(",	EMAIL ")					
					.append(") LEFT OUTER JOIN {0}.VLP1102 Firm ON ( ")
					.append("Firm.XPAID = A.FIRM_ID ")
					.append("AND	Firm.USRSSN = A.SSN ")
					.append(") ")
					.append("WHERE ") 
					.append("LENGTH(RTRIM(Firm.USRSSN)) = 0 OR Firm.USRSSN IS NULL ")
					
					
					.append(") AS BASE ( ")
					.append("		FIRM_ID ")  
					.append("	,	FIRM_NAME ")  
					.append("	,	SSN   ")
					.append("	,	STAFF_PLAN_PERM ")  
					.append("	,	FIRST_NAME   ")
					.append("	,	LAST_NAME   ")
					.append("	,	EMAIL ")
					.append("	,	RCV_ILOAN_EMAIL_PR ")
                    .append("	) LEFT OUTER JOIN {0}.VLP1207 TpaumUser ON ( ")
					.append("		BASE.FIRM_ID = TpaumUser.ACSGRP ")
					.append("	AND	BASE.SSN = TpaumUser.USRSSN ")
					.append("	AND	TpaumUser.ACSTYP = 'T' ")
					.append("	)					 ");
					

		String query = MessageFormatHelper.format(sql.toString(),
				QUERY_SUBSTITUTIONS);
System.out.println(query);
		return query;
	}
	
	private static String convertToCSV(TpaUser user, boolean conversionMode) throws Exception
	{
		StringBuffer tmp = new StringBuffer();
		tmp.append("\n");
		tmp.append(user.getFirmId());
		tmp.append(";");
		tmp.append(user.getFirmName());
		tmp.append(";");
		tmp.append(user.getSsn());
		tmp.append(";");
		tmp.append(user.getApolloFirstName() == null ? "" : user.getApolloFirstName());
		tmp.append(";");
		tmp.append(user.getApolloLastName() == null ? "" : user.getApolloLastName());
		tmp.append(";");
		tmp.append(user.getApolloEmail() == null ? "" : user.getApolloEmail());
		tmp.append(";");
		tmp.append(user.getApolloFirstName() == null ? "" : user.isApolloStaffPlanPermission() == true ? "Yes" : "No");
		tmp.append(";");
		tmp.append(user.getApolloFirstName() == null ? "" : user.isApolloReceiveIloanEmailPermission() == true ? "Yes" : "No");
		tmp.append(";");
		tmp.append(user.getApolloFirstName() == null ? "" : user.isPrimaryTpa() == true ? "Yes" : "No");
		tmp.append(";");
		tmp.append(user.getPswFirstName() == null ? "" : user.getPswFirstName());
		tmp.append(";");
		tmp.append(user.getPswLastName() == null ? "" : user.getPswLastName());
		tmp.append(";");
		tmp.append(user.getPswFirstName() == null ? "" : user.isPswStaffPlanPermission() == true ? "Yes" : "No");
		tmp.append(";");
		tmp.append(user.getPswStatus() == null ? "" : user.getPswStatus() );
		tmp.append(";");
		tmp.append(user.getPswFirstName() == null ? "" : user.isPswReceiveIloanEmailPermission() == true ? "Yes" : "No");
		tmp.append(";");
		tmp.append(user.getPswEmail() == null ? "" : user.getPswEmail());
		tmp.append(";");
		tmp.append(user.getConversionDate() == null ? "" : user.getConversionDate().toString());
		tmp.append(";");
		tmp.append(user.getPswFirstName() == null ? "" : user.getMatchingILoansPermissionActionOnPSW());
		
		if ( conversionMode ) {
			tmp.append(";");

			if ( conversionDate.equals(user.getConversionDate()) && 
					"Changed to Yes".equals(user.getMatchingILoansPermissionActionOnPSW()) )
				tmp.append("True");
		}		
		
		return tmp.toString();
	}
	
	
	private static Collection getConverisonHistory() throws Exception {
		Collection conversionHistory = new ArrayList();
		
		Connection connection = getConnection(); 		
		PreparedStatement stmt = connection.prepareStatement("Select * from CELIKIL.CONVERSION_HISTORY");
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			TpaUser tpaUser = new TpaUser();
			
			tpaUser.setFirmId(rs.getString("FIRM_ID").trim());
			tpaUser.setSsn(rs.getString("SSN").trim());
			tpaUser.setConversionDate(new Date(rs.getTimestamp("CONVERSION_DATE").getTime()));
			tpaUser.setMatchingILoansPermissionActionOnPSW(rs.getString("ILOAN_PERM_PSW_ACTION"));
			
			conversionHistory.add(tpaUser);
		}
		
		rs.close();
		stmt.close();
		connection.close();
			
		return conversionHistory;
	}

	private static Date updateConversionHistory(String firmId, String ssn, String matchingILoansPermissionActionOnPSW) throws Exception
	{
		Connection connection = getConnection(); 		
		PreparedStatement stmt = connection.prepareStatement("Select * from CELIKIL.CONVERSION_HISTORY where FIRM_ID = ? AND " +
				" SSN=?");
		stmt.setString(1, firmId);
		stmt.setString(2, ssn);
		
		ResultSet rs = stmt.executeQuery();
		
		if ( rs.next() )
		{
			PreparedStatement stmt2 = connection.prepareStatement("Update CELIKIL.CONVERSION_HISTORY " +
					"SET CONVERSION_DATE = ? , " +
					"	 ILOAN_PERM_PSW_ACTION = ? " +
					"WHERE FIRM_ID = ? AND " +
					"      SSN=?");
			
			stmt2.setTimestamp(1, conversionTimestamp);
			stmt2.setString(2, matchingILoansPermissionActionOnPSW);
			stmt2.setString(3, firmId);
			stmt2.setString(4, ssn);

			stmt2.execute();
			stmt2.close();
		}
		else 
		{
			PreparedStatement stmt2 = connection.prepareStatement("INSERT INTO CELIKIL.CONVERSION_HISTORY " +
					" VALUES(?,?,?,?)");
			
			stmt2.setString(1, firmId);
			stmt2.setString(2, ssn);
			stmt2.setTimestamp(3, conversionTimestamp);
			stmt2.setString(4, matchingILoansPermissionActionOnPSW);
			
			stmt2.execute();
			stmt2.close();
		}
		
		rs.close();
		stmt.close();
		connection.close();
		
		return conversionDate;
	}
	
	private static Connection getConnection() throws Exception
	{
		Class.forName("com.ibm.db2.jcc.DB2Driver");
		return DriverManager.getConnection("jdbc:db2:tpaconv", "db2admin","db2admin");
	}
	
	
	
}


