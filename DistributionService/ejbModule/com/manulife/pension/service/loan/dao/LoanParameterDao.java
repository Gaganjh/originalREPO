/*
 * LoanMoneyTypeDao.java,v 1.1 2006/09/01 18:42:11 Paul_Glenn Exp
 * LoanMoneyTypeDao.java,v
 * Revision 1.1  2006/09/01 18:42:11  Paul_Glenn
 * Initial.
 *
 */
package com.manulife.pension.service.loan.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.SQLDeleteHandler;
import com.intware.dao.jdbc.SQLInsertHandler;
import com.intware.dao.jdbc.SQLUpdateHandler;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.loan.domain.LoanConstants;
import com.manulife.pension.service.loan.domain.LoanStateEnum;
import com.manulife.pension.service.loan.exception.LoanDaoException;
import com.manulife.pension.service.loan.valueobject.LoanMoneyType;
import com.manulife.pension.service.loan.valueobject.LoanParameter;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;

/**
 * The LoanMoneyTypeDao performs database access operations for
 * {@link WithdrawalRequestMoneyType} objects.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1 2006/09/01 18:42:11
 */
public class LoanParameterDao extends BaseDatabaseDAO {

	private static final String CLASS_NAME = LoanParameterDao.class.getName();

	/**
	 * This method inserts a set of {@link LoanParameter}s into the database.
	 * 
	 * @param submissionId
	 *            The Submission Id
	 * @param contractId
	 *            The currect Contract Id
	 * @param userProfileId
	 *            The users profile Id
	 * @param timestamp
	 *            The timestamp used for created and last updated
	 * @param moneyTypes
	 *            The collection of WithdrawalRequestMoneyType's
	 * @throws DistributionServiceException
	 *             Thrown if there is an underlying error
	 */
	public void insert(final Integer submissionId, final Integer contractId,
			final Integer userProfileId, final Timestamp timestamp,
			final Collection<LoanParameter> objects)
			throws DistributionServiceException {

		if (objects == null) {
			return;
		}

		List<Object> params = null;
		SQLInsertHandler handler = new SQLInsertHandler(
				BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
				LoanParameterDaoSql.SQL_INSERT_LOAN_PARAMETER,
				LoanParameterDaoSql.INSERT_TYPES);

		for (LoanParameter obj : objects) {
			try {
				params = new ArrayList<Object>();
				params.add(submissionId);
				params.add(obj.getStatusCode());
				params.add(obj.getMaximumAvailable());
				params.add(obj.getLoanAmount());
				params.add(obj.getPaymentAmount());
				params.add(obj.getPaymentFrequency());
				params.add(obj.getAmortizationMonths());
				params.add(obj.getInterestRate());
				params.add(userProfileId);
				params.add(timestamp);
				params.add(userProfileId);
				params.add(timestamp);
				handler.insert(params.toArray());

			} catch (DAOException e) {
				throw new LoanDaoException(
						"Problem occurred in prepared call: "
								+ LoanParameterDaoSql.SQL_INSERT_LOAN_PARAMETER
								+ " for contract ID " + contractId
								+ " and SubmissionId " + submissionId, e);
			}
		}
	}

	/**
	 * Updates a record in the database.
	 * 
	 * @param submissionId
	 *            The Submission Id
	 * @param contractId
	 *            The currect Contract Id
	 * @param userProfileId
	 *            The users profile Id
	 * @param timestamp
	 *            The timestamp used for last updated.
	 * @param moneyTypes
	 *            The collection of LoanMoneyType's
	 * @throws DistributionServiceException
	 *             Thrown if there is an underlying error
	 */
	public void update(final Integer submissionId, final Integer contractId,
			final Integer userProfileId, final Timestamp timestamp,
			final Collection<LoanParameter> objects)
			throws DistributionServiceException {

		if (objects == null) {
			return;
		}

		List<Object> params = null;
		SQLUpdateHandler handler = new SQLUpdateHandler(
				BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
				LoanParameterDaoSql.SQL_UPDATE_LOAN_PARAMETER,
				LoanParameterDaoSql.UPDATE_TYPES);
		for (LoanParameter obj : objects) {

			try {
				params = new ArrayList<Object>();
				params.add(obj.getMaximumAvailable());
				params.add(obj.getLoanAmount());
				params.add(obj.getPaymentAmount());
				params.add(obj.getPaymentFrequency());
				params.add(obj.getAmortizationMonths());
				params.add(obj.getInterestRate());
				params.add(userProfileId);
				params.add(timestamp);
				params.add(submissionId);
				params.add(obj.getStatusCode());
				handler.update(params.toArray());

			} catch (DAOException e) {
				throw new LoanDaoException(
						"Problem occurred in prepared call: "
								+ LoanParameterDaoSql.SQL_UPDATE_LOAN_PARAMETER
								+ " for contract ID " + contractId
								+ " and SubmissionId " + submissionId, e);
			}
		}
	}

	/**
	 * deletes all of the loan money types related to the given submission id.
	 * 
	 * @param submissionId
	 *            the submission id
	 * @param contractId
	 *            The contract Id
	 * @param userProfileId
	 *            the user profile id
	 * @throws DistributionServiceException
	 *             If there is a data tier exception.
	 */
	public void deleteAll(final Integer submissionId, final Integer contractId,
			final Integer userProfileId) throws DistributionServiceException {

		try {
			List<Object> params = new ArrayList<Object>();
			params.add(submissionId);
			new SQLDeleteHandler(BaseDatabaseDAO.STP_DATA_SOURCE_NAME,
					LoanParameterDaoSql.SQL_DELETE_ALL_LOAN_PARAMETERS)
					.delete(params.toArray());

		} catch (DAOException e) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ LoanParameterDaoSql.SQL_DELETE_ALL_LOAN_PARAMETERS
					+ " for contract ID " + contractId + " and SubmissionId "
					+ submissionId, e);
		}
	}

	/**
	 * This method will inserts the {@link LoanParameter} if it does not exist
	 * and update it if it does exist.
	 * 
	 * @param submissionId
	 *            The Submission Id
	 * @param contractId
	 *            The currect Contract Id
	 * @param userProfileId
	 *            The users profile Id
	 * @param timestamp
	 *            The timestamp used for created ( insert ) , and last updated (
	 *            insert and update )
	 * @param objects
	 *            The collection of LoanParameter value objects
	 * @throws DistributionServiceException
	 *             Thrown if there is an underlying error
	 */
	public void insertUpdate(final Integer submissionId,
			final Integer contractId, final Integer userProfileId,
			final Timestamp timestamp, final Collection<LoanParameter> objects)
			throws DistributionServiceException {

		if (objects == null) {
			return;
		}

		Collection<LoanParameter> inserts = new ArrayList<LoanParameter>();
		Collection<LoanParameter> updates = new ArrayList<LoanParameter>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = getDefaultConnection(CLASS_NAME,
					BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME);
			for (LoanParameter obj : objects) {

				if (!obj.isReadyToSave()) {
					/*
					 * Only save parameters that are ready to be saved.
					 */
					continue;
				}

				List<Object> params = new ArrayList<Object>(2);
				params.add(submissionId);
				params.add(obj.getStatusCode());

				stmt = conn
						.prepareStatement(LoanParameterDaoSql.SQL_CHECK_EXISTS);
				stmt.setInt(1, submissionId);
				stmt.setString(2, obj.getStatusCode());

				stmt.execute();
				rs = stmt.getResultSet();
				
				if (rs != null) {
				    
    				if (rs.next()) {
    					updates.add(obj);
    				} else {
    					inserts.add(obj);
    				}
    				rs.close();
    				
				}
				
				stmt.close();
			}

		} catch (SQLException sqlE) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ LoanParameterDaoSql.SQL_CHECK_EXISTS
					+ " for contract ID " + contractId + " and SubmissionId "
					+ submissionId, new DAOException(sqlE));
		} catch (SystemException sysE) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ LoanParameterDaoSql.SQL_CHECK_EXISTS
					+ " for contract ID " + contractId + " and SubmissionId "
					+ submissionId, new DAOException(sysE));
		} finally {
			close(stmt, conn);
		}

		insert(submissionId, contractId, userProfileId, timestamp, inserts);
		update(submissionId, contractId, userProfileId, timestamp, updates);

	}

	/**
	 * Retrives all of the money types for a loan
	 * 
	 * @param submissionId
	 *            The submission Id of the loan
	 * @param contractId
	 *            The contract of the loan
	 * @param userProfileId
	 *            The user profile id
	 * @return A list of {@link LoanMoneyType} objects
	 * @throws DistributionServiceException
	 *             Thrown if there is an underlying error.
	 */
	public List<LoanParameter> read(final Integer submissionId,
			final Integer contractId, final Integer userProfileId)
			throws DistributionServiceException {
		List<LoanParameter> returnList = new ArrayList<LoanParameter>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = BaseDatabaseDAO.getDefaultConnection(CLASS_NAME,
					BaseDatabaseDAO.STP_DATA_SOURCE_NAME);
			stmt = conn
					.prepareStatement(LoanParameterDaoSql.SQL_SELECT_LOAN_PARAMETERS);
			stmt.setInt(1, submissionId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				LoanParameter obj = new LoanParameter();
				obj.setStatusCode(StringUtils.trim(rs
						.getString("SUB_CASE_PROCESS_STATUS_CODE")));
				obj.setMaximumAvailable(rs
						.getBigDecimal("MAX_LOAN_AVAILABLE_AMT"));
				obj.setLoanAmount(rs.getBigDecimal("LOAN_AMOUNT"));
				obj.setPaymentAmount(rs.getBigDecimal("PAYMENT_AMOUNT"));
				obj.setPaymentFrequency(StringUtils.trim(rs
						.getString("PAYMENT_FREQUENCY_CODE")));
				obj.setAmortizationMonths(rs.getInt("AMORTIZATION_MONTHS"));
				BigDecimal interestRate = rs.getBigDecimal("INTEREST_RATE_PCT");
				if (interestRate != null) {
					obj.setInterestRate(interestRate.setScale(
							LoanConstants.LOAN_INTEREST_RATE_SCALE,
							LoanConstants.LOAN_INTEREST_RATE_ROUND_RULE));
				}
				obj.setCreatedById(rs.getInt("CREATED_USER_PROFILE_ID"));
				obj.setCreated(rs.getTimestamp("CREATED_TS"));
				obj.setLastUpdatedById(rs
						.getInt("LAST_UPDATED_USER_PROFILE_ID"));
				obj.setLastUpdated(rs.getTimestamp("LAST_UPDATED_TS"));
				returnList.add(obj);
			}
			rs.close();
			stmt.close();

		} catch (SQLException e) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ LoanParameterDaoSql.SQL_SELECT_LOAN_PARAMETERS
					+ " for newSubmissionId " + submissionId, e);
		} catch (SystemException e) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ LoanParameterDaoSql.SQL_SELECT_LOAN_PARAMETERS
					+ " for newSubmissionId " + submissionId, e);
		} finally {
			BaseDatabaseDAO.close(stmt, conn);
		}
		return returnList;
	}
	
	/**
	 * Returns true if there exists of given Loan status code in status history.
	 * 
	 * @param submissionId
	 * @param statusCode
	 * @return boolean
	 * @throws DistributionServiceException
	 */
	public boolean checkLoanStatusExists(final Integer submissionId, final String statusCode)
			throws DistributionServiceException {

		boolean loanStatusExists = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = getDefaultConnection(CLASS_NAME,
					BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME);

			stmt = conn.prepareStatement(LoanParameterDaoSql.SQL_CHECK_EXISTS);
			stmt.setInt(1, submissionId);
			stmt.setString(2, statusCode);

			stmt.execute();
			rs = stmt.getResultSet();

			if (rs != null) {

				if (rs.next()) {
					loanStatusExists = true;
					rs.close();

				}
				stmt.close();
			}

		} catch (SQLException sqlE) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ LoanParameterDaoSql.SQL_CHECK_EXISTS
					+ " for SubmissionId " + submissionId, new DAOException(
					sqlE));
		} catch (SystemException sysE) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ LoanParameterDaoSql.SQL_CHECK_EXISTS
					+ " for SubmissionId " + submissionId, new DAOException(
					sysE));
		} finally {
			close(stmt, conn);
		}
		return loanStatusExists;

	}
}
