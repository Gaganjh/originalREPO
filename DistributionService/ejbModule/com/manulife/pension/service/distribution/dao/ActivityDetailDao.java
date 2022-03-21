package com.manulife.pension.service.distribution.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.StoredProcedureHandler;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.exception.DistributionServiceDaoException;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.ActivityDetail;
import com.manulife.pension.service.withdrawal.exception.WithdrawalDaoException;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalActivityDetail;

public class ActivityDetailDao extends BaseDatabaseDAO {

	private static final String SQL_SELECT_ALL_DETAILS = new StringBuffer()
			.append("call ").append(BaseDatabaseDAO.STP_SCHEMA_NAME).append(
					"SELECT_ACTIVITY_DETAILS(?)").toString();

	private static final String SQL_SELECT_ALL_SYSTEM_OF_RECORD_DETAILS = new StringBuffer()
			.append("call ").append(BaseDatabaseDAO.STP_SCHEMA_NAME).append(
					"SELECT_ACTIVITY_SOR_DETAILS(?)").toString();

	private static final String SQL_EXISTS_ACTIVITY_DETAIL = new StringBuffer()
			.append("call ").append(BaseDatabaseDAO.STP_SCHEMA_NAME).append(
					"EXISTS_ACTIVITY_DETAIL(?,?,?)").toString();

	private static final String SQL_INSERT_ACTIVITY_DETAIL = new StringBuffer()
			.append("call ").append(BaseDatabaseDAO.STP_SCHEMA_NAME).append(
					"INSERT_ACTIVITY_DETAIL(?,?,?,?,?,?)").toString();

	private static final String SQL_UPDATE_ACTIVITY_DETAIL = new StringBuffer()
			.append("call ").append(BaseDatabaseDAO.STP_SCHEMA_NAME).append(
					"UPDATE_ACTIVITY_DETAIL(?,?,?,?,?,?)").toString();

	private static final String SQL_DELETE_ACTIVITY_DETAIL = new StringBuffer()
			.append("call ").append(BaseDatabaseDAO.STP_SCHEMA_NAME).append(
					"DELETE_ACTIVITY_DETAIL(?,?,?)").toString();

	private static final String SQL_DELETE_ALL_ACTIVITY_DETAILS = new StringBuffer()
			.append("call ").append(BaseDatabaseDAO.STP_SCHEMA_NAME).append(
					"DELETE_ALL_ACTIVITY_DETAILS(?)").toString();

	private static final int[] SELECT_ALL_TYPES = { Types.INTEGER };

	private static final int[] INSERT_TYPES = { Types.INTEGER, Types.INTEGER,
			Types.CHAR, Types.VARCHAR, Types.INTEGER, Types.TIMESTAMP };

	private static final int[] UPDATE_TYPES = { Types.INTEGER, Types.INTEGER,
			Types.CHAR, Types.VARCHAR, Types.INTEGER, Types.TIMESTAMP };

	private static final int[] EXIST_TYPES = { Types.INTEGER, Types.INTEGER,
			Types.CHAR };

	private static final int[] DELETE_TYPES = { Types.INTEGER, Types.INTEGER,
			Types.CHAR };

	private static final int[] DELETE_ALL_TYPES = { Types.INTEGER };

	private static final String[] SELECT_ALL_FIELDS = { "itemNumber",
			"lastUpdated", "typeCode", "value", "lastUpdatedById" };

	private static final String[] EXISTS_FIELDS = {};

	/**
	 * selects an {@link ArrayList} of {@link ActivityDetail} objects that are
	 * contained in the given request.
	 * 
	 * @param contractId
	 *            The contract Id
	 * @param submissionId
	 *            The submission Id
	 * @param userProfileId
	 *            The user profile Id
	 * @return A list of withdrawal request fee value objects
	 * @throws DistributionServiceException
	 *             If there is a data tier exception.
	 */
	public List<? extends ActivityDetail> select(final Integer submissionId,
			final Integer contractId, final Integer userProfileId,
			Class<? extends ActivityDetail> activityDetailType)
			throws DistributionServiceException {
		// The method name.
		List<ActivityDetail> returnList = new ArrayList<ActivityDetail>();
		Object[] tempList = null;

		StoredProcedureHandler.OutputDefinition[] outputDefinition = new StoredProcedureHandler.OutputDefinition[] { new StoredProcedureHandler.BeanOutputDefinition(
				activityDetailType, SELECT_ALL_FIELDS,
				StoredProcedureHandler.MANY) };

		StoredProcedureHandler handler = new StoredProcedureHandler(
				BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
				SQL_SELECT_ALL_DETAILS, outputDefinition);

		final ArrayList<Object> parameters = new ArrayList<Object>(
				SELECT_ALL_TYPES.length);

		parameters.add(submissionId);

		try {
			tempList = handler.execute(parameters.toArray());
		} catch (DAOException daoException) {
			throw new DistributionServiceDaoException("submissionId ["
					+ submissionId + "] contractId [" + contractId
					+ "] userProfileId [" + userProfileId + "]", daoException);
		}

		for (Object o : tempList) {
			for (Object oo : (Object[]) o) {
				returnList.add((ActivityDetail) oo);
			}
		}

		return returnList;

	}

	/**
	 * selects an {@link ArrayList} of {@link ActivityDetail} objects that are
	 * contained in the given request.
	 * 
	 * @param contractId
	 *            The contract Id
	 * @param submissionId
	 *            The submission Id
	 * @param userProfileId
	 *            The user profile Id
	 * @return A list of withdrawal request fee value objects
	 * @throws DistributionServiceException
	 *             thrown in there is an exception
	 */
	public List<? extends ActivityDetail> selectSystemOfRecord(
			final Integer contractId, final Integer submissionId,
			final Integer userProfileId,
			Class<? extends ActivityDetail> activityDetailType)
			throws DistributionServiceException {
		// The method name.
		List<ActivityDetail> returnList = new ArrayList<ActivityDetail>();
		Object[] tempList = null;

		StoredProcedureHandler.OutputDefinition[] outputDefinition = new StoredProcedureHandler.OutputDefinition[] { new StoredProcedureHandler.BeanOutputDefinition(
				activityDetailType, SELECT_ALL_FIELDS,
				StoredProcedureHandler.MANY) };

		StoredProcedureHandler handler = new StoredProcedureHandler(
				BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
				SQL_SELECT_ALL_SYSTEM_OF_RECORD_DETAILS, outputDefinition);

		final ArrayList<Object> parameters = new ArrayList<Object>(
				SELECT_ALL_TYPES.length);

		parameters.add(submissionId);

		try {
			tempList = handler.execute(parameters.toArray());
		} catch (DAOException daoException) {
			throw new DistributionServiceDaoException("submissionId ["
					+ submissionId + "] contractId [" + contractId
					+ "] userProfileId [" + userProfileId + "]", daoException);
		}

		for (Object o : tempList) {
			for (Object oo : (Object[]) o) {
				returnList.add((ActivityDetail) oo);
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
	 * @param activityDetails
	 *            The ActivityDetails to insert
	 * @throws DistributionServiceException
	 *             Thrown if there is an underlying error
	 * 
	 */
	public void insert(final Integer submissionId, final Integer contractId,
			final Integer userProfileId,
			final Collection<? extends ActivityDetail> activityDetails)
			throws DistributionServiceException {

		if (activityDetails == null) {
			return;
		}

		List<Object> params = null;
		try {
			StoredProcedureHandler handler = new StoredProcedureHandler(
					BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
					SQL_INSERT_ACTIVITY_DETAIL, null, INSERT_TYPES);
			for (ActivityDetail activityDetail : activityDetails) {
				params = new ArrayList<Object>();
				params.add(submissionId);
				params.add(activityDetail.getItemNumber());
				params.add(activityDetail.getTypeCode());
				params.add(activityDetail.getValue());
				params.add(userProfileId);
				params.add(activityDetail.getLastUpdated());
				handler.execute(params.toArray());
			}

		} catch (DAOException e) {
			throw new DistributionServiceDaoException("submissionId ["
					+ submissionId + "] contractId [" + contractId
					+ "] userProfileId [" + userProfileId + "]", e);
		}
	}

	/**
	 * Updates a record in the database.
	 * 
	 * @param contractId
	 *            The currect Contract Id
	 * @param submissionId
	 *            The Submission Id
	 * @param userProfileId
	 *            The users profile Id
	 * @param activityDetails
	 *            The collection of activity details
	 * @throws DistributionServiceException
	 *             Thrown if there is an underlying error
	 */
	public void update(final Integer submissionId, final Integer contractId,
			final Integer userProfileId,
			final Collection<? extends ActivityDetail> activityDetails)
			throws DistributionServiceException {

		if (activityDetails == null) {
			return;
		}

		List<Object> params = null;
		StoredProcedureHandler handler = new StoredProcedureHandler(
				BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
				SQL_UPDATE_ACTIVITY_DETAIL, null, UPDATE_TYPES);
		for (ActivityDetail activityDetail : activityDetails) {

			try {
				params = new ArrayList<Object>();
				params.add(submissionId);
				params.add(activityDetail.getItemNumber());
				params.add(activityDetail.getTypeCode());
				params.add(activityDetail.getValue());
				params.add(userProfileId);
				params.add(activityDetail.getLastUpdated());
				handler.execute(params.toArray());

			} catch (DAOException e) {
				throw new DistributionServiceDaoException("submissionId ["
						+ submissionId + "] contractId [" + contractId
						+ "] userProfileId [" + userProfileId + "]", e);
			}
		}
	}

	/**
	 * Deletes a collection of activity detail records corresponding to a
	 * withdrawal.
	 * 
	 * @param contractId
	 *            The current Contract Id
	 * @param submissionId
	 *            The Submission Id
	 * @param userProfileId
	 *            The users profile Id
	 * @param activityDetails
	 *            The activity detail record to delete
	 * @throws DistributionServiceException
	 *             Thrown if there is an underlying error
	 */

	public void delete(final Integer submissionId, final Integer contractId,
			final Integer userProfileId,
			final Collection<? extends ActivityDetail> activityDetails)
			throws DistributionServiceException {

		if (activityDetails == null) {
			return;
		}

		List<Object> params = null;
		StoredProcedureHandler handler = new StoredProcedureHandler(
				BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
				SQL_DELETE_ACTIVITY_DETAIL, null, DELETE_TYPES);
		for (ActivityDetail activityDetail : activityDetails) {
			try {
				params = new ArrayList<Object>(2);
				params.add(submissionId);
				params.add(activityDetail.getItemNumber());
				params.add(activityDetail.getTypeCode());
				handler.execute(params.toArray());

			} catch (DAOException e) {
				throw new DistributionServiceDaoException("submissionId ["
						+ submissionId + "] contractId [" + contractId
						+ "] userProfileId [" + userProfileId + "]", e);
			}
		}
	}

	/**
	 * deletes all of the activity detail records related to the given
	 * submission id.
	 * 
	 * @param contractId
	 *            The contract Id
	 * @param submissionId
	 *            the submission id
	 * @param userProfileId
	 *            the user profile id
	 * @throws WithdrawalDaoException
	 *             If there is a data tier exception.
	 */
	public void deleteAll(final Integer submissionId, final Integer contractId,
			final Integer userProfileId) throws DistributionServiceException {

		try {
			List<Object> params = new ArrayList<Object>();
			params.add(submissionId);
			StoredProcedureHandler handler = new StoredProcedureHandler(
					BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
					SQL_DELETE_ALL_ACTIVITY_DETAILS, null, DELETE_ALL_TYPES);
			handler.execute(params.toArray());

		} catch (DAOException e) {
			throw new DistributionServiceDaoException("submissionId ["
					+ submissionId + "] contractId [" + contractId
					+ "] userProfileId [" + userProfileId + "]", e);
		}
	}

	/**
	 * This method will inserts the {@link WithdrawalActivityDetail} if it does
	 * not exist and update it if it does exist.
	 * 
	 * @param contractId
	 *            The current Contract Id
	 * @param submissionId
	 *            The Submission Id
	 * @param userProfileId
	 *            The users profile Id
	 * @param details
	 *            The collection of ActivityDetail
	 * @throws DistributionServiceException
	 *             Thrown if there is an underlying error
	 */
	public void insertUpdate(final Integer submissionId,
			final Integer contractId, final Integer userProfileId,
			final Collection<? extends ActivityDetail> details,
			Class<? extends ActivityDetail> activityDetailType)
			throws DistributionServiceException {

		if (details == null) {
			return;
		}
		StoredProcedureHandler.OutputDefinition[] outputDefinition = new StoredProcedureHandler.OutputDefinition[] { new StoredProcedureHandler.BeanOutputDefinition(
				activityDetailType, EXISTS_FIELDS, StoredProcedureHandler.MANY) };

		Collection<ActivityDetail> inserts = new ArrayList<ActivityDetail>();
		Collection<ActivityDetail> updates = new ArrayList<ActivityDetail>();
		try {
			for (ActivityDetail activityDetail : details) {
				List<Object> params = new ArrayList<Object>();
				StoredProcedureHandler handler = new StoredProcedureHandler(
						BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
						SQL_EXISTS_ACTIVITY_DETAIL, outputDefinition,
						EXIST_TYPES);
				params = new ArrayList<Object>();
				params.add(submissionId);
				params.add(activityDetail.getItemNumber());
				params.add(activityDetail.getTypeCode());
				Object[] tmpList = handler.execute(params.toArray());
				if (tmpList != null && ((Object[]) tmpList[0]).length > 0) {
					updates.add(activityDetail);
				} else {
					inserts.add(activityDetail);
				}

			}

		} catch (DAOException e) {
			throw new DistributionServiceDaoException("submissionId ["
					+ submissionId + "] contractId [" + contractId
					+ "] userProfileId [" + userProfileId + "]", e);
		}

		insert(submissionId, contractId, userProfileId, inserts);
		update(submissionId, contractId, userProfileId, updates);

	}

    public void insertUpdateIfChanged(final Integer submissionId,
            final Integer contractId, final Integer userProfileId,
            final Collection<? extends ActivityDetail> details,
            Class<? extends ActivityDetail> activityDetailType)
            throws DistributionServiceException {

        if (details == null) {
            return;
        }
        Collection<ActivityDetail> inserts = new ArrayList<ActivityDetail>();
        Collection<ActivityDetail> updates = new ArrayList<ActivityDetail>();
        
        List<? extends ActivityDetail> oldActivityDetails = select(submissionId, contractId,
                userProfileId, activityDetailType);
        
        for (ActivityDetail activityDetail : details) {
            boolean found = false;
            for (ActivityDetail oldActivityDetail : oldActivityDetails) {
                if (oldActivityDetail.getItemNumber().equals(activityDetail.getItemNumber())
                        && oldActivityDetail.getTypeCode().equals(activityDetail.getTypeCode())) {
                    if (!ObjectUtils
                            .equals(oldActivityDetail.getValue(), activityDetail.getValue())) {
                        updates.add(activityDetail);
                    }
                    found = true;
                }
            }
            if (!found) {
                inserts.add(activityDetail);
            }
        }

        insert(submissionId, contractId, userProfileId, inserts);
        update(submissionId, contractId, userProfileId, updates);

    }

}
