package com.manulife.pension.service.distribution.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.dao.BaseDatabaseDAO;

public class DistributionDao extends BaseDatabaseDAO {

	private static final Logger logger = Logger
			.getLogger(DistributionDao.class);

	private static final String CLASS_NAME = DistributionDao.class.getName();

	/**
	 * Marks a specific Submission IDs as expired.
	 * 
	 * @param submissionId
	 *            ID of submission to be marked as expired.
	 * @param userProfileId -
	 *            User Profile ID of the user that changed the record (could be
	 *            system).
	 * 
	 * @return boolean - True if one row was updated, false otherwise.
	 * 
	 * @throws SystemException -
	 *             If an exception occurs.
	 */
	public boolean expireDistributionRequest(final Integer submissionId,
			final Integer userProfileId, String expiredStatusCode,
			Timestamp lastUpdatedTs) throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("submissionId -> " + submissionId);
			logger.debug("userProfileId -> " + userProfileId);
		}
		Connection conn = null;
		PreparedStatement stmt = null;
		int rowCount = 0;

		try {
			conn = getDefaultConnection(CLASS_NAME, SUBMISSION_DATA_SOURCE_NAME);
			if (logger.isDebugEnabled()) {
				logger.debug("Executing SQL_SET_EXPIRED_REQUEST Query: "
						+ DistributionDaoSql.SET_EXPIRED_REQUEST);
			}
			stmt = conn
					.prepareStatement(DistributionDaoSql.SET_EXPIRED_REQUEST);
			stmt.setString(1, expiredStatusCode);
			stmt.setString(2, Integer.toString(userProfileId));
			stmt.setTimestamp(3, lastUpdatedTs);
			stmt.setTimestamp(4, lastUpdatedTs);
			stmt.setInt(5, submissionId);
			rowCount = stmt.executeUpdate();
		} catch (SQLException sqlException) {
			throw new SystemException(sqlException, "submissionId: "
					+ submissionId);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error("could not close in expireDistributionRequest()",
						e);
			}
		}
		return (rowCount == 1);
	}

}
