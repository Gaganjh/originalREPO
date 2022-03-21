package com.manulife.pension.ps.administration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * This report is created during PAM as an automated report to run once a month (as Ctrl+M job).
 * It will run first day of teh each month and it will find the all the particiapnt with multiple addresses
 * for the prior month. 
 * 
 * 
 * @author Ilker Celikyilmaz
 *
 */
public class ParticipantsWithMultipleAddressChangesReport extends BaseAdministration {

	public class Participant {

		public Participant() {
			super();
		}
		
		public String contractNumber = "";
		public String contractName = "";
		public Date   planYearEnd;
		public String lastName = "";
		public String firstName = "";
		public String ssn = "";
		public String status = "";
		public Timestamp dateOfChange;
		public String source = "";
		public String updatedByFirstName= "";
		public String updatedByLastName= "";
		public String addressLine1 = "";
		public String addressLine2 = "";
		public String city = "";
		public String state = "";
		public String zip = "";
		public String country = "";
	}
	
	private static String FILE_NAME = "ParticipantsWithMultipleAddressChangesReport.csv";
	private static String HEADER = "ContractNumber,ContractName,PlanYearEnd,LastName,FirstName,SSN,Status,DateOfChange,Source,ChangedByLastName,ChangedByFirstName,AddressLine1,AdddressLine2,City,State,ZIP,Country\n";

	private static String SQL_SELECT_PARTICIPANTS_WITH_MULTIPLE_ADDRESS_CHANGE =
	"WITH ParticipantsWithMultipleAddressUpdates (PROFILE_ID, CONTRACT_ID) "
	+"AS ( "
	+"    SELECT "  
	+"	    PROFILE_ID, "
	+"	    CONTRACT_ID "
	+"    FROM  "
	+"		EZK100.EMPLOYEE_ADDRESS_HISTORY "
	+"    WHERE "
	+"		DATE(EMPLOYEE_ADDR_EFF_TS) >= ? AND "
	+"		DATE(EMPLOYEE_ADDR_EFF_TS) <  ?  "
	+"    GROUP BY "
	+"		PROFILE_ID, "
	+"		CONTRACT_ID "
	+"    HAVING "
	+"	   COUNT(*) > 1  "
	+") "
	+"SELECT "  
	+"	EAH.PROFILE_ID, "
	+"	EAH.CONTRACT_ID, "
	+"	CS.CONTRACT_NAME, "
	+"	CS.PLAN_REPORTING_YEAR_END_DATE, "
	+"	ED.SOCIAL_SECURITY_NO, "
	+"	ED.LAST_NAME, "
	+"	ED.FIRST_NAME, "
	+"	EMPLOYEE_ADDR_EFF_TS, "
	+"	ADDR_LINE1, "
	+"	ADDR_LINE2, "
	+"	CITY_NAME, "
	+"	STATE_CODE, "
	+"	ZIP_CODE, "
	+"	COUNTRY_NAME, "
	+"  CASE WHEN "
	+"		EAH.SOURCE_SYSTEM_CODE = 'PS' "
	+"		THEN 'Plan Sponsor website' "
	+"    	     WHEN "
	+"		EAH.SOURCE_SYSTEM_CODE = 'IF' "
	+"		THEN 'Census File' "
	+"    	     WHEN "
	+"		EAH.SOURCE_SYSTEM_CODE = 'FL' "
	+"		THEN 'Census File' "
	+"    	     WHEN "
	+"		EAH.SOURCE_SYSTEM_CODE = 'PA' "
	+"		THEN 'Participant website' "
	+"    	     WHEN "	
	+"		EAH.SOURCE_SYSTEM_CODE = 'AL' "
	+"		THEN 'Payroll Company' "
	+"	    ELSE 'Unknown' "
	+"	END AS SOURCE, "
	+"	CREATED_FIRST_NAME, "
	+"	CREATED_LAST_NAME, "
	+"  CASE WHEN "
	+"		       ED.EMPLOYMENT_STATUS_CODE = 'A' "
	+"	     THEN 'Active' "
	+"       WHEN "
	+"		       ED.EMPLOYMENT_STATUS_CODE = 'C' "
	+"		 THEN 'Canceled' "
	+"       WHEN "
	+"		       ED.EMPLOYMENT_STATUS_CODE = 'D' "
	+"		 THEN 'Deceased' "
	+"       WHEN "
	+"		       ED.EMPLOYMENT_STATUS_CODE = 'P' "
	+"		 THEN 'Disabled' "
	+"       WHEN "
	+"		       ED.EMPLOYMENT_STATUS_CODE = 'R' "
	+"		 THEN 'Retired' "
	+"       WHEN "
	+"		       ED.EMPLOYMENT_STATUS_CODE = 'T' "
	+"		 THEN 'Terminated' "	
	+"	     ELSE '' "
	+"	END AS STATUS "
	+"FROM  "
	+"	EZK100.EMPLOYEE_ADDRESS_HISTORY EAH, "
	+"	EZK100.EMPLOYEE_CONTRACT ED, "
	+"	EZK100.PARTICIPANT_CONTRACT PC, "
	+"	EZK100.CONTRACT_CS CS, "
	+"	ParticipantsWithMultipleAddressUpdates PMAU "
	+"WHERE "
	+"	EAH.PROFILE_ID = PMAU.PROFILE_ID AND "
	+"	EAH.CONTRACT_ID = PMAU.CONTRACT_ID AND "
	+"	ED.PROFILE_ID = PMAU.PROFILE_ID AND "
	+"	ED.CONTRACT_ID = PMAU.CONTRACT_ID AND "
	+"	PC.PROFILE_ID = PMAU.PROFILE_ID AND "
	+"	PC.CONTRACT_ID = PMAU.CONTRACT_ID AND "
	+"	CS.CONTRACT_ID = PMAU.CONTRACT_ID AND "
	+"	DATE(EMPLOYEE_ADDR_EFF_TS) >= ? AND "
	+"	DATE(EMPLOYEE_ADDR_EFF_TS) <  ? "
	
	+"ORDER BY  "
	+"	EAH.CONTRACT_ID ASC, "
	+"	ED.LAST_NAME ASC, "
	+"	EMPLOYEE_ADDR_EFF_TS DESC "
	+" with ur";		
	

	

	public static void main(String[] args) throws Exception
	{
		Calendar calendar = GregorianCalendar.getInstance();
		Date today = calendar.getTime();

		calendar.add(Calendar.DAY_OF_MONTH,1);
		Date endDate = calendar.getTime();
		
		calendar.add(Calendar.MONTH,-1);
		calendar.set(Calendar.DAY_OF_MONTH,1);
		Date startDate = calendar.getTime();
		
		ParticipantsWithMultipleAddressChangesReport report = new ParticipantsWithMultipleAddressChangesReport();
		Collection<Participant> participants = report.getParticipantsWithMultipleAddressChange(startDate, endDate);
		report.writeToFile(today, startDate, participants);
	}
	
	
	private Collection<Participant> getParticipantsWithMultipleAddressChange(Date startDate, Date endDate) throws Exception
	{
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

		Connection connection = getConnection("jdbc:db2:u1csdb","ezcsdba","easy1234");
		connection.setAutoCommit(false);
		connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
		
		PreparedStatement statement = connection.prepareStatement(SQL_SELECT_PARTICIPANTS_WITH_MULTIPLE_ADDRESS_CHANGE);

		statement.setString(1, dateFormatter.format(startDate));
		statement.setString(2, dateFormatter.format(endDate));
		statement.setString(3, dateFormatter.format(startDate));
		statement.setString(4, dateFormatter.format(endDate));		

		statement.execute();
		
		ResultSet rs = statement.getResultSet();
		ArrayList<Participant> participants = new ArrayList<Participant>();
		
		if (rs != null) {
		    
    		while(rs.next())
    		{
    			Participant participant = new Participant();
    
    			participant.contractNumber = customTrim(rs.getString(2));
    			participant.contractName = customTrim(rs.getString(3));
    			participant.planYearEnd = rs.getDate(4);
    			participant.ssn = rs.getString(5);
    			participant.lastName = customTrim(rs.getString(6));
    			participant.firstName = customTrim(rs.getString(7));
    			participant.dateOfChange = rs.getTimestamp(8);
    			participant.addressLine1 = rs.getString(9).trim();
    			participant.addressLine2 = customTrim(rs.getString(10));
    			participant.city = customTrim(rs.getString(11));
    			participant.state = customTrim(rs.getString(12));
    			participant.zip = customTrim(rs.getString(13));
    			participant.country = customTrim(rs.getString(14));
    			participant.source = rs.getString(15);
    			participant.updatedByFirstName = rs.getString(16);
    			participant.updatedByLastName = rs.getString(17);
    			participant.status = rs.getString(18);
    			participants.add(participant);
    		}
    		
		}
		
		close(statement, connection);
		
		return participants;
	}

	private String customTrim(String str) {
		return str == null ? "" : str.trim();
	}

	private void writeToFile(Date today, Date reportDate, Collection<Participant> participantList) throws Exception
	{
		SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd");
		SimpleDateFormat dateFormatter2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
		SimpleDateFormat dateFormatter3 = new SimpleDateFormat("MM/yyyy");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(FILE_NAME)));
		writer.write("Report Run Date: "+dateFormatter2.format(today)+"\n");
		writer.write("Reporting Month/Year: "+dateFormatter3.format(reportDate)+"\n\n");
		writer.write(HEADER);
		StringBuffer tmp = new StringBuffer();
		Participant[] participants = (Participant[])participantList.toArray(new Participant[0]);
		for(int i = 0; i < participants.length; i++)
		{
			tmp.append(participants[i].contractNumber).append(",\"")
				.append(participants[i].contractName).append("\",")
				.append(dateFormatter.format(participants[i].planYearEnd)).append(",\"")
				.append(participants[i].lastName).append("\",\"")
				.append(participants[i].firstName).append("\",")
				.append(participants[i].ssn).append(",")
				.append(participants[i].status).append(",\"")
				.append(dateFormatter2.format(participants[i].dateOfChange)).append("\",")
				.append(participants[i].source).append(",\"")
				.append(participants[i].updatedByLastName).append("\",\"")
				.append(participants[i].updatedByFirstName).append("\",\"")
				.append(participants[i].addressLine1).append("\",\"")
				.append(participants[i].addressLine2).append("\",\"")
				.append(participants[i].city).append("\",\"")
				.append(participants[i].state).append("\",\"")
				.append(participants[i].zip).append("\",\"")
				.append(participants[i].country).append("\"")
				.append("\n");
			writer.write(tmp.toString());
			tmp.delete(0,tmp.length());
		}
		writer.flush();
		writer.close();
	}




}
