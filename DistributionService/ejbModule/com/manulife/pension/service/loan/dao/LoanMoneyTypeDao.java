/*
 * LoanMoneyTypeDao.java,v 1.1 2006/09/01 18:42:11 Paul_Glenn Exp
 * LoanMoneyTypeDao.java,v
 * Revision 1.1  2006/09/01 18:42:11  Paul_Glenn
 * Initial.
 *
 */
package com.manulife.pension.service.loan.dao;

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
import com.intware.dao.InvalidKeyException;
import com.intware.dao.jdbc.SQLDeleteHandler;
import com.intware.dao.jdbc.SQLInsertHandler;
import com.intware.dao.jdbc.SQLUpdateHandler;
import com.intware.dao.jdbc.SelectBeanQueryHandler;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.loan.exception.LoanDaoException;
import com.manulife.pension.service.loan.valueobject.LoanMoneyType;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;
import com.manulife.pension.util.JdbcHelper;

/**
 * The LoanMoneyTypeDao performs database access operations for
 * {@link LoanMoneyType} objects.
 * 
 */
public class LoanMoneyTypeDao extends BaseDatabaseDAO {

	private static final String CLASS_NAME = LoanMoneyTypeDao.class.getName();

	/**
	 * This method will inserts the {@link WithdrawalRequestMoneyType} if it
	 * does not exist and update it if it does exist.
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
	 * @param moneyTypes
	 *            The collection of WithdrawalRequestMoneyType's
	 * @throws DistributionServiceException
	 *             Thrown if there is an underlying error
	 */
	public void insertUpdate(final Integer submissionId,
			final Integer contractId, final Integer userProfileId,
			final Timestamp timestamp,
			final Collection<LoanMoneyType> moneyTypes)
			throws DistributionServiceException {

		if (moneyTypes == null || moneyTypes.size() == 0) {
			return;
		}

		Collection<LoanMoneyType> inserts = new ArrayList<LoanMoneyType>();
		Collection<LoanMoneyType> updates = new ArrayList<LoanMoneyType>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = getDefaultConnection(CLASS_NAME,
					BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME);
			for (LoanMoneyType moneyType : moneyTypes) {

				List<Object> params = new ArrayList<Object>(2);
				params.add(submissionId);
				params.add(moneyType.getMoneyTypeId());

				stmt = conn
						.prepareStatement(LoanMoneyTypeDaoSql.SQL_CHECK_EXISTS);
				stmt.setInt(1, submissionId);
				stmt.setString(2, moneyType.getMoneyTypeId());

				stmt.execute();
				rs = stmt.getResultSet();
				
				if (rs != null) {
				    
    				if (rs.next()) {
    					updates.add(moneyType);
    				} else {
    					inserts.add(moneyType);
    				}
    				rs.close();
    				
				}
				
				stmt.close();
			}

		} catch (SQLException sqlE) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ LoanMoneyTypeDaoSql.SQL_INSERT_MONEY_TYPE
					+ " for contract ID " + contractId + " and SubmissionId "
					+ submissionId, new DAOException(sqlE));
		} catch (SystemException sysE) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ LoanMoneyTypeDaoSql.SQL_INSERT_MONEY_TYPE
					+ " for contract ID " + contractId + " and SubmissionId "
					+ submissionId, new DAOException(sysE));
		} finally {
			close(stmt, conn);
		}

		insert(submissionId, contractId, userProfileId, timestamp, inserts);
		update(submissionId, contractId, userProfileId, timestamp, updates);
	}

	/**
	 * This method inserts a set of {@link WithdrawalRequestMoneyType}s into
	 * the database.
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
			final Collection<LoanMoneyType> moneyTypes)
			throws DistributionServiceException {

		if (moneyTypes == null || moneyTypes.size() == 0) {
			return;
		}

		List<Object> params = null;
		SQLInsertHandler handler = new SQLInsertHandler(
				BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
				LoanMoneyTypeDaoSql.SQL_INSERT_MONEY_TYPE,
				LoanMoneyTypeDaoSql.INSERT_TYPES);

		for (LoanMoneyType moneyType : moneyTypes) {
			try {
				params = new ArrayList<Object>();
				params.add(submissionId);
				params.add(moneyType.getMoneyTypeId());
				params.add(moneyType.getVestingPercentage());
				params.add(moneyType.getAccountBalance());
				params.add(moneyType.getLoanBalance());
				params.add(moneyType.getExcludeIndicator());
				params.add(moneyType.isVestingPercentageUpdateable());
				params.add(userProfileId);
				params.add(timestamp);
				params.add(userProfileId);
				params.add(timestamp);
				handler.insert(params.toArray());

			} catch (DAOException e) {
				throw new LoanDaoException(
						"Problem occurred in prepared call: "
								+ LoanMoneyTypeDaoSql.SQL_INSERT_MONEY_TYPE
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
			final Collection<LoanMoneyType> moneyTypes)
			throws DistributionServiceException {

		if (moneyTypes == null || moneyTypes.size() == 0) {
			return;
		}

		List<Object> params = null;
		SQLUpdateHandler handler = new SQLUpdateHandler(
				BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
				LoanMoneyTypeDaoSql.SQL_UPDATE_MONEY_TYPE,
				LoanMoneyTypeDaoSql.UPDATE_TYPES);
		try {
			for (LoanMoneyType moneyType : moneyTypes) {
				params = new ArrayList<Object>();
				params.add(moneyType.getVestingPercentage());
				params.add(moneyType.getAccountBalance());
				params.add(moneyType.getLoanBalance());
				params.add(moneyType.getExcludeIndicator());
				params.add(moneyType.isVestingPercentageUpdateable());
				params.add(userProfileId);
				params.add(timestamp);
				params.add(submissionId);
				params.add(moneyType.getMoneyTypeId());
				handler.update(params.toArray());
			}
		} catch (DAOException e) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ LoanMoneyTypeDaoSql.SQL_UPDATE_MONEY_TYPE
					+ " for contract ID " + contractId + " and SubmissionId "
					+ submissionId, e);
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
					LoanMoneyTypeDaoSql.SQL_DELETE_ALL_MONEY_TYPES)
					.delete(params.toArray());

		} catch (DAOException e) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ LoanMoneyTypeDaoSql.SQL_DELETE_ALL_MONEY_TYPES
					+ " for contract ID " + contractId + " and SubmissionId "
					+ submissionId, e);
		}
	}

	/**
	 * Populate information from the contract money type table.
	 * 
	 * @param moneyType
	 * @throws DistributionServiceException
	 */
	private void populateContractMoneyTypeInfo(Integer contractId,
			LoanMoneyType moneyType) throws DistributionServiceException {

		SelectBeanQueryHandler handler = new SelectBeanQueryHandler(
				BaseDatabaseDAO.CUSTOMER_DATA_SOURCE_NAME,
				LoanMoneyTypeDaoSql.SQL_SELECT_CONTRACT_MONEY_TYPE_INFO,
				LoanMoneyType.class);
		try {
			LoanMoneyType contractMoneyType = (LoanMoneyType) handler
					.select(new Object[] { moneyType.getMoneyTypeId(),
							contractId });
			moneyType.setMoneyTypeAliasId(contractMoneyType
					.getMoneyTypeAliasId());
			moneyType.setContractMoneyTypeLongName(contractMoneyType
					.getContractMoneyTypeLongName());
			moneyType.setContractMoneyTypeShortName(contractMoneyType
					.getContractMoneyTypeShortName());
			moneyType.setMoneyTypeCategoryCode(contractMoneyType
					.getMoneyTypeCategoryCode());
        } catch (InvalidKeyException e) {
            // Money type has been deleted from the contract.
            moneyType.setMoneyTypeAliasId(moneyType.getMoneyTypeId());
            moneyType.setContractMoneyTypeLongName(moneyType.getMoneyTypeId());
            moneyType.setContractMoneyTypeShortName(moneyType.getMoneyTypeId());
            moneyType.setAContractMoneyType(false);
		} catch (DAOException e) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ LoanMoneyTypeDaoSql.SQL_SELECT_CONTRACT_MONEY_TYPE_INFO
					+ " for money type id [" + moneyType.getMoneyTypeId() + "]",
					e);
		}
	}

	/**
	 * Retrieves all of the money types for a loan
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
	public List<LoanMoneyType> read(final Integer submissionId,
			final Integer contractId, final Integer userProfileId)
			throws DistributionServiceException {
		List<LoanMoneyType> returnList = new ArrayList<LoanMoneyType>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = BaseDatabaseDAO.getDefaultConnection(CLASS_NAME,
					BaseDatabaseDAO.STP_DATA_SOURCE_NAME);
			stmt = conn
					.prepareStatement(LoanMoneyTypeDaoSql.SQL_SELECT_MONEY_TYPES);
			stmt.setInt(1, submissionId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				LoanMoneyType moneyType = new LoanMoneyType();
				moneyType.setMoneyTypeId(StringUtils.trim(rs
						.getString("MONEY_TYPE_ID")));
				moneyType.setVestingPercentage(rs.getBigDecimal("VESTING_PCT"));
				moneyType.setAccountBalance(rs
						.getBigDecimal("MONEY_TYPE_ACCT_BALANCE_AMT"));
				moneyType.setLoanBalance(rs
						.getBigDecimal("MONEY_TYPE_LOAN_BALANCE_AMT"));
				moneyType.setExcludeIndicator(JdbcHelper.getBoolean(rs,
						"MONEY_TYPE_EXCLUDE_IND", false));
				moneyType.setCreatedById(rs.getInt("CREATED_USER_PROFILE_ID"));
				moneyType.setCreated(rs.getTimestamp("CREATED_TS"));
				moneyType.setLastUpdatedById(rs
						.getInt("LAST_UPDATED_USER_PROFILE_ID"));
				moneyType.setLastUpdated(rs.getTimestamp("LAST_UPDATED_TS"));
				moneyType.setVestingPercentageUpdateable(JdbcHelper.getBoolean(
						rs, "VESTING_PCT_UPDATABLE_IND", false));

				populateContractMoneyTypeInfo(contractId, moneyType);

				returnList.add(moneyType);
			}
			rs.close();
			stmt.close();

		} catch (SQLException e) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ LoanMoneyTypeDaoSql.SQL_SELECT_MONEY_TYPES
					+ " for newSubmissionId " + submissionId, e);
		} catch (SystemException e) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ LoanMoneyTypeDaoSql.SQL_SELECT_MONEY_TYPES
					+ " for newSubmissionId " + submissionId, e);
		} finally {
			BaseDatabaseDAO.close(stmt, conn);
		}
		return returnList;
	}
}
