package com.manulife.pension.service.distribution.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.StoredProcedureHandler;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.exception.DistributionServiceDaoException;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.ActivitySummary;

public class ActivitySummaryDao extends BaseDatabaseDAO {

	private static final String SQL_SELECT_ALL_SUMMARIES = new StringBuffer()
			.append("call ").append(BaseDatabaseDAO.STP_SCHEMA_NAME).append(
					"SELECT_ALL_ACTIVITY_SUMMARIES(?)").toString();

	private static final String SQL_INSERT_SUMMARY = new StringBuffer().append(
			"call ").append(BaseDatabaseDAO.STP_SCHEMA_NAME).append(
			"INSERT_ACTIVITY_SUMMARY(?,?,?,?)").toString();

	private static final String SQL_DELETE_SUMMARY = new StringBuffer().append(
			"call ").append(BaseDatabaseDAO.STP_SCHEMA_NAME).append(
			"DELETE_ACTIVITY_SUMMARY(?,?)").toString();

	private static final String SQL_DELETE_ALL_SUMMARIES = new StringBuffer()
			.append("call ").append(BaseDatabaseDAO.STP_SCHEMA_NAME).append(
					"DELETE_ALL_ACTIVITY_SUMMARIES(?)").toString();

	private static final int[] SELECT_ALL_TYPES = { Types.INTEGER };
	private static final int[] INSERT_TYPES = { Types.INTEGER, Types.CHAR,
			Types.INTEGER, Types.TIMESTAMP };
	private static final int[] DELETE_TYPES = { Types.INTEGER };
	private static final int[] DELETE_ALL_TYPES = { Types.INTEGER };

	private static final String[] SELECT_ALL_FIELDS = { "statusCode",
			"createdById", "created" };

	/**
	 * selects an {@link ArrayList} of {@link ActivitySummary} objects that are
	 * contained in the given distribution submission.
	 * 
	 * @param contractId
	 *            The contract Id
	 * @param submissionId
	 *            The submission Id
	 * @param userProfileId
	 *            The user profile Id
	 * @return A list of withdrawal request fee value objects
	 * @throws DistributionServiceDaoException
	 *             If there is a data tier exception.
	 */
	public List<? extends ActivitySummary> select(final Integer submissionId,
			final Integer contractId, final Integer userProfileId,
			Class<? extends ActivitySummary> activitySummaryType)
			throws DistributionServiceException {
		// The method name.
		List<ActivitySummary> returnList = new ArrayList<ActivitySummary>();
		Object[] tempList = null;

		StoredProcedureHandler.OutputDefinition[] outputDefinition = new StoredProcedureHandler.OutputDefinition[] { new StoredProcedureHandler.BeanOutputDefinition(
				activitySummaryType, SELECT_ALL_FIELDS,
				StoredProcedureHandler.MANY) };

		StoredProcedureHandler handler = new StoredProcedureHandler(
				BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
				SQL_SELECT_ALL_SUMMARIES, outputDefinition);

		final ArrayList<Object> parameters = new ArrayList<Object>(
				SELECT_ALL_TYPES.length);

		parameters.add(submissionId);

		try {
			tempList = handler.execute(parameters.toArray());
		} catch (DAOException daoException) {
			throw new DistributionServiceDaoException(
					"Exception performing select [submissionId:" + submissionId
							+ "][contractId:" + contractId + "][userProfileId:"
							+ userProfileId + "]", daoException);
		}

		for (Object o : tempList) {
			for (Object oo : (Object[]) o) {
				ActivitySummary summary = (ActivitySummary) oo;
				summary.setSubmissionId(submissionId);
				returnList.add(summary);
			}
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
	 * @param activitySummary
	 *            The activitySummary to insert
	 * @throws DistributionServiceDaoException
	 *             Thrown if there is an underlying error
	 * 
	 */
	public void insert(final Integer submissionId, final Integer contractId,
			final Integer userProfileId, final ActivitySummary activitySummary)
			throws DistributionServiceException {

		if (activitySummary == null) {
			return;
		}

		List<Object> params = null;
		try {
			StoredProcedureHandler handler = new StoredProcedureHandler(
					BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
					SQL_INSERT_SUMMARY, null, INSERT_TYPES);
			params = new ArrayList<Object>();
			params.add(submissionId);
			params.add(activitySummary.getStatusCode());
			params.add(userProfileId);
			params.add(activitySummary.getCreated());
			handler.execute(params.toArray());

		} catch (DAOException e) {
			throw new DistributionServiceDaoException(
					"Exception performing insert [submissionId:" + submissionId
							+ "][contractId:" + contractId + "][userProfileId:"
							+ userProfileId + "]", e);
		}
	}

	/**
	 * Deletes a collection of activity summaries from the database.
	 * 
	 * @param contractId
	 *            The currect Contract Id
	 * @param submissionId
	 *            The Submission Id
	 * @param userProfileId
	 *            The users profile Id
	 * @param activitySummary
	 *            The activity summary to delete
	 * @throws DistributionServiceDaoException
	 *             Thrown if there is an underlying error
	 */

	public void delete(final Integer submissionId, final Integer contractId,
			final Integer userProfileId, final ActivitySummary activitySummary)
			throws DistributionServiceException {

		if (activitySummary == null) {
			return;
		}

		List<Object> params = null;
		StoredProcedureHandler handler = new StoredProcedureHandler(
				BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
				SQL_DELETE_SUMMARY, null, DELETE_TYPES);
		try {
			params = new ArrayList<Object>(2);
			params.add(submissionId);
			params.add(activitySummary.getStatusCode());
			handler.execute(params.toArray());

		} catch (DAOException e) {
			throw new DistributionServiceDaoException(
					"Exception performing delete [submissionId:" + submissionId
							+ "][contractId:" + contractId + "][userProfileId:"
							+ userProfileId + "]", e);
		}
	}

	/**
	 * deletes all of the activity summaries related to the given submission id.
	 * 
	 * @param contractId
	 *            The contract Id
	 * @param submissionId
	 *            the submission id
	 * @param userProfileId
	 *            the user profile id
	 * @throws DistributionServiceDaoException
	 *             If there is a data tier exception.
	 */
	public void deleteAll(final Integer submissionId, final Integer contractId,
			final Integer userProfileId) throws DistributionServiceException {

		try {
			List<Object> params = new ArrayList<Object>();
			params.add(submissionId);
			StoredProcedureHandler handler = new StoredProcedureHandler(
					BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
					SQL_DELETE_ALL_SUMMARIES, null, DELETE_ALL_TYPES);
			handler.execute(params.toArray());

		} catch (DAOException e) {
			throw new DistributionServiceDaoException(
					"Exception performing deleteAll [submissionId:"
							+ submissionId + "][contractId:" + contractId
							+ "][userProfileId:" + userProfileId + "]", e);
		}
	}
}
