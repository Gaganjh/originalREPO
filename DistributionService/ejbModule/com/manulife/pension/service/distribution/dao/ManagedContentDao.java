package com.manulife.pension.service.distribution.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.SQLDeleteHandler;
import com.intware.dao.jdbc.SQLInsertHandler;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.exception.DistributionServiceDaoException;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.ManagedContent;
import com.manulife.pension.service.loan.exception.LoanDaoException;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;

/**
 * The LoanManagedContentDao performs database access operations for
 * {@link ManagedContent} objects.
 * 
 * @author snowdde
 */
public class ManagedContentDao extends BaseDatabaseDAO {

	private static final String CLASS_NAME = ManagedContentDao.class.getName();

	/**
	 * This method inserts a set of {@link WithdrawalRequestMoneyType}s into the
	 * database.
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
			final Collection<ManagedContent> contents)
			throws DistributionServiceException {

		if (contents == null) {
			return;
		}

		List<Object> params = null;
		SQLInsertHandler handler = new SQLInsertHandler(
				DataSourceJndiName.SUBMISSION,
				ManagedContentDaoSql.SQL_INSERT_MANAGED_CONTENT,
				ManagedContentDaoSql.INSERT_TYPES);

		for (ManagedContent content : contents) {
			try {
				params = new ArrayList<Object>();
				params.add(submissionId);
				params.add(content.getContentKey());
				params.add(content.getContentId());
				params.add(content.getCmaSiteCode());
				params.add(content.getContentTypeCode());
				params.add(userProfileId);
				params.add(timestamp);
				handler.insert(params.toArray());

			} catch (DAOException e) {
				throw new LoanDaoException(
						"Problem occurred in prepared call: "
								+ ManagedContentDaoSql.SQL_INSERT_MANAGED_CONTENT
								+ " for contract ID " + contractId
								+ " and SubmissionId " + submissionId, e);
			}
		}
	}

	/**
	 * deletes all of the managed content related to the given submission id.
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

		List<Object> params = new ArrayList<Object>(1);
		params.add(submissionId);
		SQLDeleteHandler handler = new SQLDeleteHandler(
				DataSourceJndiName.SUBMISSION,
				ManagedContentDaoSql.SQL_DELETE_ALL_MANAGED_CONTENT);
		try {
			handler.delete(params.toArray());
		} catch (DAOException e) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ ManagedContentDaoSql.SQL_DELETE_ALL_MANAGED_CONTENT
					+ " for contract ID " + contractId + " and SubmissionId "
					+ submissionId, e);
		}
	}

	   /**
     * deletes all of the managed content related to the given submission id, that
     * match the content_type_code provided.
     * 
     * @param contractId
     *            The contract Id
     * @param submissionId
     *            the submission id
     * @param TypeCodes
     *            the list of content_type_code values to delete
     * @throws DistributionServiceException
     *             If there is a data tier exception.
     */
    public void delete(final Integer submissionId, final Integer contractId,
            final List<String> typeCodes) throws DistributionServiceException {
        // TODO:tm1 need to create a test for this method.
        // Below corresponds to maximum entries in the "in" clause in
        // ManagedContentDaoSql.SQL_DELETE_SPECIFIC_MANAGED_CONTENT_TYPES.
        int typeCodesMaxSize = 13;

        if (typeCodes.size() == 0) {
            return; // no type codes, so nothing to delete.
        }
        if (typeCodes.size() > typeCodesMaxSize) {
            throw new IllegalArgumentException("TypeCodes list contains too "
                    + "many entries.  It can support a maximum of "
                    + typeCodesMaxSize + " but contains: "
                    + typeCodes.toString());
        }

        try {
            List<Object> params = new ArrayList<Object>(7);
            params.add(submissionId);
            for (int i = 0; i < typeCodesMaxSize; i++) {
                if (i < typeCodes.size()) {
                    params.add(typeCodes.get(i));
                } else {
                    params.add(typeCodes.get(0));
                }
            }
            new SQLDeleteHandler(
                    DataSourceJndiName.SUBMISSION,
                    ManagedContentDaoSql.SQL_DELETE_SPECIFIC_MANAGED_CONTENT_TYPES)
                    .delete(params.toArray());

        } catch (DAOException e) {
            throw new DistributionServiceDaoException(
                    "Problem occurred in prepared call: "
                            + ManagedContentDaoSql.SQL_DELETE_SPECIFIC_MANAGED_CONTENT_TYPES
                            + " for contract ID " + contractId
                            + " and SubmissionId " + submissionId, e);
        }

    }

	
	/**
	 * Retrives all of the managed content for a loan
	 * 
	 * @param submissionId
	 *            The submission Id of the loan
	 * @param contractId
	 *            The contract of the loan
	 * @param userProfileId
	 *            The user profile id
	 * @return A list of {@link ManagedContent} objects
	 * @throws DistributionServiceException
	 *             Thrown if there is an underlying error.
	 */
	public List<ManagedContent> read(final Integer submissionId,
			final Integer contractId, final Integer userProfileId)
			throws DistributionServiceException {
		List<ManagedContent> returnList = new ArrayList<ManagedContent>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = BaseDatabaseDAO.getDefaultConnection(CLASS_NAME,
					DataSourceJndiName.SUBMISSION);
			stmt = conn
					.prepareStatement(ManagedContentDaoSql.SQL_SELECT_MANAGED_CONTENTS);
			stmt.setInt(1, submissionId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				ManagedContent vo = new ManagedContent();
				vo.setContentKey(rs.getInt("CONTENT_KEY"));
				vo.setContentId(rs.getInt("CONTENT_ID"));
				vo.setCmaSiteCode(rs.getString("CMA_SITE_CODE"));
				vo.setContentTypeCode(rs.getString("CONTENT_TYPE_CODE"));
				vo.setCreatedById(rs.getInt("CREATED_USER_PROFILE_ID"));
				vo.setCreated(rs.getTimestamp("CREATED_TS"));
				returnList.add(vo);
			}
			rs.close();
			stmt.close();

		} catch (SQLException e) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ ManagedContentDaoSql.SQL_SELECT_MANAGED_CONTENTS
					+ " for submissionId " + submissionId, e);
		} catch (SystemException e) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ ManagedContentDaoSql.SQL_SELECT_MANAGED_CONTENTS
					+ " for submissionId " + submissionId, e);
		} finally {
			BaseDatabaseDAO.close(stmt, conn);
		}
		return returnList;
	}
}
