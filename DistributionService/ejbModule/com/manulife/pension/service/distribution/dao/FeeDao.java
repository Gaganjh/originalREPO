package com.manulife.pension.service.distribution.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.SQLDeleteHandler;
import com.intware.dao.jdbc.SQLInsertHandler;
import com.intware.dao.jdbc.SQLUpdateHandler;
import com.intware.dao.jdbc.SelectBeanListQueryHandler;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.exception.DistributionServiceDaoException;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.Fee;

/**
 * The FeeDao performs database access operations for {@link Fee} objects.
 */
public class FeeDao extends BaseDatabaseDAO {

	/**
	 * selects an {@link ArrayList} of {@link Fee} objects that are contained in
	 * the given submission.
	 * 
	 * @param contractId
	 *            The contract Id
	 * @param submissionId
	 *            The submission Id
	 * @param userProfileId
	 *            The user profile Id
	 * @return A list of fee value objects
	 * @throws DistributionServiceException
	 *             If there is a data tier exception.
	 */
	public List<? extends Fee> select(final Integer submissionId,
			final Integer contractId, final Integer userProfileId,
			Class<? extends Fee> feeType) throws DistributionServiceException {
		// The method name.
		List<Fee> returnList = new ArrayList<Fee>();
		List tempList = null;

		// The insert handler.
		SelectBeanListQueryHandler selectBeanListQueryHandler = new SelectBeanListQueryHandler(
				BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
				FeeDaoSql.SELECT_ALL_FEES_SQL,
				FeeDaoSql.SELECT_ALL_FEES_PARAMETER_TYPES, feeType,
				FeeDaoSql.SELECT_ALL_FEES_RESULT_FIELDS);

		final ArrayList<Object> parameters = new ArrayList<Object>(
				FeeDaoSql.SELECT_ALL_FEES_PARAMETER_TYPES.length);

		// The order here must match the query order.
		parameters.add(submissionId);

		try {
			tempList = (List) selectBeanListQueryHandler.select(parameters
					.toArray());
		} catch (DAOException daoException) {
			throw new DistributionServiceDaoException("Submission ID: "
					+ submissionId + " contract ID: " + contractId
					+ " userProfileId:" + userProfileId + " type:"
					+ feeType.getName(), daoException);
		}

		for (Object o : tempList) {
			returnList.add((Fee) o);
		}

		return returnList;

	}

	/**
	 * @param contractId
	 *            The currect Contract Id
	 * @param submissionId
	 *            The Submission Id
	 * @param userProfileId
	 *            The users profile Id
	 * @param fees
	 *            The collection of WithdrawalRequestFee's - must be size 0 or 1
	 * @throws DistributionServiceException
	 *             Thrown if there is an underlying error
	 */
	public void insert(final Integer submissionId, final Integer contractId,
			final Integer userProfileId, final Collection<Fee> fees)
			throws DistributionServiceException {

		if (fees == null) {
			return;
		}
		if (fees.size() > 1) {
			throw new DistributionServiceDaoException(
					"FeeDao doesn't support more than 1 "
							+ "fee. Because the database doesn't either.");
		}

		List<Object> params = null;
		SQLInsertHandler handler = new SQLInsertHandler(
				BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
				FeeDaoSql.SQL_INSERT_FEE, FeeDaoSql.INSERT_TYPES);

		for (Fee fee : fees) {
			try {
				params = new ArrayList<Object>();
				params.add(submissionId);
				params.add(fee.getTypeCode());
				params.add(fee.getValue());
				params.add(userProfileId);
				params.add(userProfileId);
				handler.insert(params.toArray());

			} catch (DAOException e) {
				throw new DistributionServiceDaoException(
						"Problem occurred in prepared call: "
								+ FeeDaoSql.SQL_INSERT_FEE
								+ " for contract ID " + contractId
								+ " and SubmissionId " + submissionId, e);
			}
		}
	}

	/**
	 * @param contractId
	 *            The currect Contract Id
	 * @param submissionId
	 *            The Submission Id
	 * @param userProfileId
	 *            The users profile Id
	 * @param fees
	 *            The collection of WithdrawalRequestFee's must be 0 or 1 in
	 *            size
	 * @throws DistributionServiceException
	 *             Thrown if there is an underlying error
	 */

	public void update(final Integer submissionId, final Integer contractId,
			final Integer userProfileId, final Collection<Fee> fees)
			throws DistributionServiceException {

		if (fees == null) {
			return;
		}
		if (fees.size() > 1) {
			throw new DistributionServiceDaoException(
					"FeeDao doesn't support more than 1 "
							+ "fee. Because the database doesn't either.");
		}

		List<Object> params = null;
		SQLUpdateHandler handler = new SQLUpdateHandler(
				BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
				FeeDaoSql.SQL_UPDATE_FEE, FeeDaoSql.UPDATE_TYPES);

		for (Fee fee : fees) {
			try {
				params = new ArrayList<Object>();
				params.add(fee.getTypeCode());
				params.add(fee.getValue());
				params.add(userProfileId);
				params.add(submissionId);
				handler.update(params.toArray());

			} catch (DAOException e) {
				throw new DistributionServiceDaoException(
						"Problem occurred in prepared call: "
								+ FeeDaoSql.SQL_UPDATE_FEE
								+ " for contract ID " + contractId
								+ " and SubmissionId " + submissionId, e);
			}
		}
	}

	/**
	 * Deletes a collection of fee's from the database.
	 * 
	 * @param contractId
	 *            The currect Contract Id
	 * @param submissionId
	 *            The Submission Id
	 * @param userProfileId
	 *            The users profile Id
	 * @param fees
	 *            The collection of WithdrawalRequestFee's
	 * @throws DistributionServiceException
	 *             Thrown if there is an underlying error
	 */
	public void delete(final Integer submissionId, final Integer contractId,
			final Integer userProfileId, final Collection<Fee> fees)
			throws DistributionServiceException {

		if (fees == null) {
			return;
		}
		if (fees.size() > 1) {
			throw new DistributionServiceDaoException(
					"FeeDao doesn't support more than 1 "
							+ "fee. Because the database doesn't either.");
		}

		List<Object> params = null;
		SQLDeleteHandler handler = new SQLDeleteHandler(
				BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
				FeeDaoSql.SQL_DELETE_FEE);
		// this for loop is not really needed since
		// there is no fee_id on the fee table. However, once more than 1 fee is
		// needed
		// the loop will be necessary, and we will need to add a parameter to
		// the delete
		// statement. I am leaving it in, because its the correct template for
		// this
		// type of operation.
		for (Fee fee : fees) {
			try {
				params = new ArrayList<Object>(2);
				params.add(submissionId);
				handler.delete(params.toArray());

			} catch (DAOException e) {
				throw new DistributionServiceDaoException(
						"Problem occurred in prepared call: "
								+ FeeDaoSql.SQL_DELETE_FEE
								+ " for contract ID " + contractId
								+ " and SubmissionId " + submissionId, e);
			}
		}
	}

	/**
	 * Inserts a record if it does not exist. Updates it if it does exist, and
	 * prunes all records in the database if they do not exist in
	 * 
	 * @param loans
	 * 
	 * @param contractId
	 *            The currect Contract Id
	 * @param submissionId
	 *            The Submission Id
	 * @param userProfileId
	 *            The users profile Id
	 * @param input
	 *            The collection of WithdrawalRequestMoneyType's
	 * @throws DistributionServiceException
	 *             Thrown if there is an underlying error
	 */

	public void insertUpdatePrune(final Integer submissionId,
			final Integer contractId, final Integer userProfileId,
			final Collection<? extends Fee> input, Class<? extends Fee> feeType)
			throws DistributionServiceException {

		if (input != null && input.size() > 1) {
			throw new DistributionServiceDaoException(
					"FeeDao doesn't support more than 1 "
							+ "fee. Because the database doesn't either.");
		}
		Collection<Fee> referenceInput = new ArrayList<Fee>();

		// Load the referenceInput with only fees that aren't 'blank' (i.e. have
		// values).
		if (input != null) {
			for (Fee fee : input) {
				if (!(fee.isBlank())) {
					referenceInput.add(fee);
				} // fi
			} // end for
		} // fi

		if (referenceInput.size() == 0) {
			deleteAll(submissionId, contractId, userProfileId);
		} else {
			List<? extends Fee> fees = select(submissionId, contractId,
					userProfileId, feeType);
			if (fees.size() == 1) {
				update(submissionId, contractId, userProfileId, referenceInput);
			} else {
				insert(submissionId, contractId, userProfileId, referenceInput);
			}
		}
	}

	/**
	 * deletes all of the fees related to the given submission id.
	 * 
	 * @param contractId
	 *            The contract Id
	 * @param submissionId
	 *            the submission id
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
					FeeDaoSql.SQL_DELETE_ALL_FEES).delete(params.toArray());

		} catch (DAOException e) {
			throw new DistributionServiceDaoException(
					"Problem occurred in prepared call: "
							+ FeeDaoSql.SQL_DELETE_ALL_FEES
							+ " for contract ID " + contractId
							+ " and SubmissionId " + submissionId, e);
		}
	}
}
