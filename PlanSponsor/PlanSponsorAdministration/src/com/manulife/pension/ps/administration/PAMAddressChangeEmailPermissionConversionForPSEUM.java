package com.manulife.pension.ps.administration;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

/**
 * @author Ilker Celikyilmaz
 *
 * This class is used to add the new PAM address Change Email permission for all ACTIVE and NEW PSEUMs.
 *
 * This will need to be run BEFORE the system conversion as it is depending on th PUM security role which will
 * go away in the system conversion
 */
public class PAMAddressChangeEmailPermissionConversionForPSEUM {

	private static final String DB_URL = "jdbc:db2:SRZKD1D1";
	private static final String DB_USER_ID = "db2admin";
	private static final String DB_PASSWORD = "db2admin";
	private static String FILE_NAME = "C:\\projects\\participantAddressChange\\PAMConvertedPSEUMUsers.csv";


	private static final String SQL_SELECT_UPDATED_PSEUM_USERS =
		"select    " +
		"		PH.permission_holder_id , " +
        "		UC.user_profile_id, " +
        "		UC.contract_id,  " +
        "		UP.FIRST_NAME, " +
        "		UP.LAST_NAME " +
		"from  " +
        "       psw100.user_contract UC , " +
        "       psw100.user_profile UP, " +
        "       psw100.permission_holder PH " +
		"where  " +
        "		UP.end_ts = '9999-12-31-00.00.00.000000' " +
		"and " +
        "		UP.user_profile_id = UC.user_profile_id " +
		"and   " +
        " 		UP.psw_directory_role_code = 'PSU' " +
		"and  " +
        "	 	UC.SECURITY_ROLE_CODE = 'PUM' " +
        "and  " +
        "       PH.user_contract_user_profile_id = uc.user_profile_id " +
        "and  " +
        "       PH.user_contract_contract_id = uc.contract_id " +
        "and " +
        "       PH.perm_relationship_type_code = 'USCO' " +
        "and " +
        "       PH.permission_holder_id not in (select permission_holder_id from psw100.permission_grant where SECURITY_TASK_PERMISSION_CODE = 'ACEM') " +
		"order by " +
		"		uc.user_profile_id, uc.contract_id ";


	private static final String SQL_INSERT_PERMISSION_GRANT =
		"insert into psw100.permission_grant(permission_holder_id,security_task_permission_code," +
		" created_user_profile_id, created_ts, last_updated_user_profile_id, last_updated_ts)" +
		" select PH.permission_holder_id," +
		"        'ACEM', " +
		"        1, " +
		"        CURRENT TIMESTAMP, " +
		"        1, " +
		"        CURRENT TIMESTAMP " +
		"from  " +
        "       psw100.user_contract UC , " +
        "       psw100.user_profile UP, " +
        "       psw100.permission_holder PH " +
		"where  " +
        "		UP.end_ts = '9999-12-31-00.00.00.000000' " +
		"and " +
        "		UP.user_profile_id = UC.user_profile_id " +
		"and   " +
        " 		UP.psw_directory_role_code = 'PSU' " +
		"and  " +
        "	 	UC.SECURITY_ROLE_CODE = 'PUM' " +
        "and  " +
        "       PH.user_contract_user_profile_id = UC.user_profile_id " +
        "and  " +
        "       PH.user_contract_contract_id = UC.contract_id " +
        "and " +
        "       PH.perm_relationship_type_code = 'USCO' " +
        "and " +
        "       PH.permission_holder_id not in (select permission_holder_id from psw100.permission_grant where SECURITY_TASK_PERMISSION_CODE = 'ACEM') " +
		"order by " +
		"		uc.user_profile_id, uc.contract_id ";

   	public static void main(String[] args) {
		Statement statement = null;
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		boolean failed = false;

		String url = DB_URL;
		String userId = DB_USER_ID;
		String password = DB_PASSWORD;

		if (args.length == 3) {
			url = args[0];
			userId = args[1];
			password = args[2];
		}
		System.out.println("URL: " + url + ", User ID: " + userId + "/" + password);

    	try {
        	Class.forName("com.ibm.db2.jcc.DB2Driver");
    		connection = DriverManager.getConnection(url, userId, password);

    		statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(SQL_SELECT_UPDATED_PSEUM_USERS);
			int count = 0;

			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(FILE_NAME)));
			writer.write("Report for the PSEUM users who have been added new Address Change Email permission during PAM.\n\n");
			writer.write("Report Run Date: "+ Calendar.getInstance().getTime() +"\n");
			writer.write("PermissionHolderId,UserProfileId,ContractId,FirstName,LastName\n");

			while (rs.next()) {
				count++;
				StringBuffer tmp = new StringBuffer();

				tmp.append(rs.getInt(1)).append(",\"")
						.append(rs.getBigDecimal(2).toString()).append("\",")
						.append(rs.getInt(3)).append(",\"")
						.append(rs.getString(4)).append("\",\"")
						.append(rs.getString(5)).append("\"")
						.append("\n");
				writer.write(tmp.toString());
				tmp.delete(0,tmp.length());
			}

			writer.flush();
			writer.close();

			System.out.println(count + " rows created for external users!");

			statement = connection.createStatement();
			int countUpdate = statement.executeUpdate(SQL_INSERT_PERMISSION_GRANT);
			statement.close();
			System.out.println(countUpdate + " rows created for grants!");

			if ( countUpdate != count ) {
				failed = true;
				System.err.println("Expected and Updated number of rows does not match. Expected:" + count + ", Updated:"+ countUpdate);
			}



    	} catch (IOException e) {
    		System.err.println("IO Exception: " + e.getMessage());
    		failed = true;
    	} catch (ClassNotFoundException e) {
    		System.err.println("Bad classpath: " + e.getMessage());
    		failed = true;
    	} catch (SQLException e) {
    		System.err.println("SQL Error: " + e.getMessage());
    		failed = true;
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				System.err.println("Error while closing statements: " + e.getMessage());
	    		failed = true;
			} finally {
				try {
					if (preparedStatement != null) {
						preparedStatement.close();
					}
				} catch (SQLException e) {
					System.err.println("Error while closing prepared statements: " + e.getMessage());
		    		failed = true;
				} finally {
					try {
						if (connection != null) {
							connection.close();
						}
					} catch (SQLException e) {
						System.err.println("Error while closing connections: " + e.getMessage());
			    		failed = true;
					}
				}
			}
		}
		System.exit(failed ? 1: 0);
    }

}
