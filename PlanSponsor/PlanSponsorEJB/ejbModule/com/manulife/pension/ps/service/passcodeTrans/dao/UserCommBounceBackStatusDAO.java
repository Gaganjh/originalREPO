package com.manulife.pension.ps.service.passcodeTrans.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.passcodeTrans.valueobject.UserCommBounceBackStatusVO;
import com.manulife.pension.service.dao.BaseDatabaseDAO;

public class UserCommBounceBackStatusDAO extends BaseDatabaseDAO {

	private static final String className = UserCommBounceBackStatusDAO.class.getName();
	private static final Logger logger = Logger.getLogger(UserCommBounceBackStatusDAO.class);

	private static String SELECT_USER_PASSCODE_COMM_STATUS_SQL = "SELECT USER_IDENTITY, SID, COMM_TYPE, NO_OF_ATTEMPTS FROM "
			+ PLAN_SPONSOR_SCHEMA_NAME
			+ "USER_PASSCODE_COMM_STATUS WHERE COMM_TYPE in('SMS', 'VOICE') and COMM_STATUS not in('delivered','completed') "
			+ " and NO_OF_ATTEMPTS < ";

	private static String UPDATE_USER_PASSCODE_COMM_STATUS_SQL = "UPDATE " + PLAN_SPONSOR_SCHEMA_NAME
			+ "USER_PASSCODE_COMM_STATUS SET COMM_STATUS = ? ,"
			+ " NO_OF_ATTEMPTS = ? , LAST_UPDATED_TS = CURRENT TIMESTAMP" + " WHERE SID = ? AND USER_IDENTITY = ? ";
	
    private static String FOR_FETCH_ONLY = " FOR FETCH ONLY ";
	
	private static String FETCH_FIRST_ROWS_ONLY_1 = " FETCH FIRST ";
	
	private static String FETCH_FIRST_ROWS_ONLY_2 = " ROWS ONLY ";

	/**
	 * To Get the un-delivered SID status from user_passcode_comm_status
	 * 
	 * @return
	 * @throws SystemException
	 */
	public static List<UserCommBounceBackStatusVO> getUserCommBounceBackStatus() throws SystemException {
		logger.info("Entry -> UserCommBounceBackStatusDAO.getUserCommBounceBackStatus");
		List<UserCommBounceBackStatusVO> userCommBounceBackStatusVO = new ArrayList<>();
		UserCommBounceBackStatusVO userCommBounceBackStatus = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = conn.prepareCall(SELECT_USER_PASSCODE_COMM_STATUS_SQL);
			rs = stmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					userCommBounceBackStatus = new UserCommBounceBackStatusVO();
					userCommBounceBackStatus.setUserProfileId(rs.getString("USER_IDENTITY"));
					userCommBounceBackStatus.setsId(rs.getString("SID"));
					userCommBounceBackStatus.setCommType(rs.getString("COMM_TYPE"));
					userCommBounceBackStatus.setNoOfAttempts(rs.getInt("NO_OF_ATTEMPTS"));
					userCommBounceBackStatusVO.add(userCommBounceBackStatus);
				}
			}
		} catch (SQLException e) {
			logger.error("Retrieve the User Passcode Transition Status", e);
			throw new SystemException(e, "Problem occurred during getUserCommBounceBackStatus sql call. ");
		} finally {
			close(stmt, conn);
		}
		logger.info("Exit <-- UserCommBounceBackStatusDAO.getUserCommBounceBackStatus ");
		return userCommBounceBackStatusVO;
	}
	
	
	/**
	 * To Get the un-delivered SID status from user_passcode_comm_status to a maximum limit
	 * 
	 * @return
	 * @throws SystemException
	 */
	public static List<UserCommBounceBackStatusVO> getMaxUserCommBounceBackStatus(Integer maxRecords, Integer maxRetry) throws SystemException {
		logger.info("Entry -> UserCommBounceBackStatusDAO.getMaxUserCommBounceBackStatus");
		List<UserCommBounceBackStatusVO> userCommBounceBackStatusVO = new ArrayList<>();
		UserCommBounceBackStatusVO userCommBounceBackStatus = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String query = buildSqlForMaxRows(SELECT_USER_PASSCODE_COMM_STATUS_SQL,maxRecords.intValue(), maxRetry.intValue());

		try {
			conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = conn.prepareCall(query);
			rs = stmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					userCommBounceBackStatus = new UserCommBounceBackStatusVO();
					userCommBounceBackStatus.setUserProfileId(rs.getString("USER_IDENTITY"));
					userCommBounceBackStatus.setsId(rs.getString("SID"));
					userCommBounceBackStatus.setCommType(rs.getString("COMM_TYPE"));
					userCommBounceBackStatus.setNoOfAttempts(rs.getInt("NO_OF_ATTEMPTS"));
					userCommBounceBackStatusVO.add(userCommBounceBackStatus);
				}
			}
		} catch (SQLException e) {
			logger.error("Retrieve the User Passcode Transition Status", e);
			throw new SystemException(e, "Problem occurred during getMaxUserCommBounceBackStatus sql call. ");
		} finally {
			close(stmt, conn);
		}
		logger.info("Exit <-- UserCommBounceBackStatusDAO.getMaxUserCommBounceBackStatus ");
		return userCommBounceBackStatusVO;
	}

	/**
	 * To update the un-delivered SID status in user_passcode_comm_status table
	 * 
	 * @return count
	 * @throws SystemException
	 */
	public static int updateUserPasscodeCommStatus(String profileId, String sId, String status, int noOfAttempts)
			throws SystemException {
		int count = 0;
		logger.info("Entry -> UserCommBounceBackStatusDAO.updateUserPasscodeCommStatus");
		Connection conn = null;
		PreparedStatement statement = null;

		try {
			conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			statement = conn.prepareStatement(UPDATE_USER_PASSCODE_COMM_STATUS_SQL);
			statement.setString(1, status);
			statement.setInt(2, noOfAttempts);
			statement.setString(3, sId);
			statement.setString(4, profileId);

			count = statement.executeUpdate();
			logger.info("Executing update SQL statement: " + UPDATE_USER_PASSCODE_COMM_STATUS_SQL);

		} catch (SQLException e) {
			logger.error("Failed to update user passcode comm status on CSDB for user with profile ID: " + profileId,
					e);
			throw new SystemException(e, "updateUserPasscodeCommStatus for user with profile ID: " + profileId
					+ "Problem occurred update call: " + UPDATE_USER_PASSCODE_COMM_STATUS_SQL);
		} finally {
			close(statement, conn);
		}
		logger.info("Exit <-- UserCommBounceBackStatusDAO.updateUserPasscodeCommStatus ");
		return count;
	}
	
private static String buildSqlForMaxRows(String sql, int maxRows,int maxRetry) {
		
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(sql);
		sqlBuffer.append(maxRetry);
		sqlBuffer.append(FETCH_FIRST_ROWS_ONLY_1);
		sqlBuffer.append(maxRows);
		sqlBuffer.append(FETCH_FIRST_ROWS_ONLY_2);
		sqlBuffer.append(FOR_FETCH_ONLY);
		return sqlBuffer.toString();
	}

}
