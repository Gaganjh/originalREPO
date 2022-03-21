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
import com.manulife.pension.service.distribution.valueobject.ActivityDynamicDetail;
import com.manulife.pension.service.withdrawal.helper.PayeeFieldDef;

public class ActivityDynamicDetailDao extends BaseDatabaseDAO {

    private static final String SQL_SELECT_DYNAMIC_DETAILS = new StringBuffer().append("call ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME).append(
                    "SELECT_ACTIVITY_DYNAMIC_DETAILS(?)").toString();

    private static final String SQL_SELECT_SOR_DYNAMIC_DETAILS = new StringBuffer().append("call ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME).append(
                    "SELECT_ACTIVITY_SOR_DYNAMIC_DETAILS(?)").toString();

    private static final String SQL_INSERT_ACTIVITY_DYNAMIC_DETAIL = new StringBuffer().append(
            "call ").append(BaseDatabaseDAO.STP_SCHEMA_NAME).append(
            "INSERT_ACTIVITY_DYNAMIC_DETAIL(?,?,?,?,?,?,?,?)").toString();

    private static final String SQL_UPDATE_ACTIVITY_DYNAMIC_DETAIL = new StringBuffer().append(
            "call ").append(BaseDatabaseDAO.STP_SCHEMA_NAME).append(
            "UPDATE_ACTIVITY_DYNAMIC_DETAIL(?,?,?,?,?,?,?,?)").toString();
    
    private static final String SQL_EXISTS_ACTIVITY_DYNAMIC_DETAIL = new StringBuffer().append(
            "call ").append(BaseDatabaseDAO.STP_SCHEMA_NAME).append(
            "EXISTS_ACTIVITY_DYNAMIC_DETAIL(?,?,?,?,?)").toString();

    
    private static final String SQL_DELETE_ACTIVITY_DYNAMIC_DETAIL = new StringBuffer().append(
            "call ").append(BaseDatabaseDAO.STP_SCHEMA_NAME).append(
            "DELETE_ACTIVITY_DYNAMIC_DETAIL(?,?,?,?,?)").toString();

    private static final String SQL_DELETE_ALL_ACTIVITY_DYNAMIC_DETAILS = new StringBuffer()
            .append("call ").append(BaseDatabaseDAO.STP_SCHEMA_NAME).append(
                    "DELETE_ALL_ACTIVITY_DYNAMIC_DETAILS(?)").toString();

    private static final int[] SELECT_ALL_TYPES = { Types.INTEGER };

    private static final int[] INSERT_TYPES = { Types.INTEGER, Types.INTEGER, Types.VARCHAR,
            Types.INTEGER, Types.CHAR, Types.VARCHAR, Types.INTEGER, Types.TIMESTAMP };

    private static final int[] UPDATE_TYPES = { Types.INTEGER, Types.INTEGER, Types.VARCHAR,
            Types.INTEGER, Types.CHAR, Types.VARCHAR, Types.INTEGER, Types.TIMESTAMP };

    private static final int[] EXISTS_TYPES = { Types.INTEGER, Types.INTEGER, Types.VARCHAR,
            Types.INTEGER, Types.CHAR};

    private static final int[] DELETE_TYPES = { Types.INTEGER };

    private static final int[] DELETE_ALL_TYPES = { Types.INTEGER };

    private static final String[] SELECT_ALL_FIELDS = { "itemNumber", "secondaryName",
            "secondaryNumber", "lastUpdated", "typeCode", "value", "lastUpdatedById" };
    
    private static final String[] EXISTS_FIELDS = { };

    /**
     * selects an {@link ArrayList} of {@link ActivityDynamicDetail} objects that
     * are contained in the given request.
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
    public List<ActivityDynamicDetail> select(final Integer submissionId,final Integer contractId, 
            final Integer userProfileId) throws DistributionServiceException {
        // The method name.
        List<ActivityDynamicDetail> returnList = new ArrayList<ActivityDynamicDetail>();
        Object[] tempList = null;

        StoredProcedureHandler.OutputDefinition[] outputDefinition = 
            new StoredProcedureHandler.OutputDefinition[] {
                new StoredProcedureHandler.BeanOutputDefinition(
                ActivityDynamicDetail.class, SELECT_ALL_FIELDS, StoredProcedureHandler.MANY) };

        StoredProcedureHandler handler = new StoredProcedureHandler(
                BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME, SQL_SELECT_DYNAMIC_DETAILS,
                outputDefinition);

        final ArrayList<Object> parameters = new ArrayList<Object>(SELECT_ALL_TYPES.length);

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
                ActivityDynamicDetail detail = (ActivityDynamicDetail) oo;
                // Security Enhancements - ignore id 18 - Credit party name
                if (detail.getSecondaryNumber().intValue() != PayeeFieldDef.P_CREDIT_PARTY_NAME.getId().intValue()) {
                    returnList.add((ActivityDynamicDetail) detail);
                }
            }
        }
      
        return returnList;

    }

    /**
     * selects an {@link ArrayList} of {@link ActivityDynamicDetail} objects that
     * are contained in the given request.
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
    public List<ActivityDynamicDetail> selectSystemOfRecord(final Integer contractId,
            final Integer submissionId, final Integer userProfileId)
            throws DistributionServiceException {
        // The method name.
        List<ActivityDynamicDetail> returnList = new ArrayList<ActivityDynamicDetail>();
        Object[] tempList = null;

        StoredProcedureHandler.OutputDefinition[] outputDefinition = 
            new StoredProcedureHandler.OutputDefinition[] {
                new StoredProcedureHandler.BeanOutputDefinition(
                ActivityDynamicDetail.class, SELECT_ALL_FIELDS, StoredProcedureHandler.MANY) };

        StoredProcedureHandler handler = new StoredProcedureHandler(
                BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME, SQL_SELECT_SOR_DYNAMIC_DETAILS,
                outputDefinition);

        final ArrayList<Object> parameters = new ArrayList<Object>(SELECT_ALL_TYPES.length);

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
                returnList.add((ActivityDynamicDetail) oo);
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
     *            The ActivityDynamicDetail to insert
     * @throws DistributionServiceException
     *             Thrown if there is an underlying error
     * 
     */
    public void insert(final Integer submissionId,final Integer contractId, 
            final Integer userProfileId, final Collection<ActivityDynamicDetail> activityDetails)
            throws DistributionServiceException {

        if (activityDetails == null) {
            return;
        }

        List<Object> params = null;
        try {
            StoredProcedureHandler handler = new StoredProcedureHandler(
                    BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
                    SQL_INSERT_ACTIVITY_DYNAMIC_DETAIL, null, INSERT_TYPES);
            for (ActivityDynamicDetail activityDetail : activityDetails) {
                params = new ArrayList<Object>();
                params.add(submissionId);
                params.add(activityDetail.getItemNumber());
                params.add(activityDetail.getSecondaryName());
                params.add(activityDetail.getSecondaryNumber());
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
     * @param contractId
     *            The currect Contract Id
     * @param submissionId
     *            The Submission Id
     * @param userProfileId
     *            The users profile Id
     * @param activityDetails
     *            The ActivityDynamicDetail to insert
     * @throws DistributionServiceException
     *             Thrown if there is an underlying error
     * 
     */
    public void update(final Integer submissionId,final Integer contractId, 
            final Integer userProfileId, final Collection<ActivityDynamicDetail> activityDetails)
            throws DistributionServiceException {

        if (activityDetails == null) {
            return;
        }

        List<Object> params = null;
        try {
            StoredProcedureHandler handler = new StoredProcedureHandler(
                    BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
                    SQL_UPDATE_ACTIVITY_DYNAMIC_DETAIL, null, UPDATE_TYPES);
            for (ActivityDynamicDetail activityDetail : activityDetails) {
                params = new ArrayList<Object>();
                params.add(submissionId);
                params.add(activityDetail.getItemNumber());
                params.add(activityDetail.getSecondaryName());
                params.add(activityDetail.getSecondaryNumber());
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
     * Deletes a collection of activity detail records corresponding to a
     * withdrawal.
     * 
     * @param contractId
     *            The currect Contract Id
     * @param submissionId
     *            The Submission Id
     * @param userProfileId
     *            The users profile Id
     * @param activityDynamicDetails
     *            The activity detail record to delete
     * @throws DistributionServiceException
     *             Thrown if there is an underlying error
     */

    public void delete(final Integer submissionId,final Integer contractId, 
            final Integer userProfileId,
            final Collection<ActivityDynamicDetail> activityDynamicDetails)
            throws DistributionServiceException {

        if (activityDynamicDetails == null) {
            return;
        }

        List<Object> params = null;
        StoredProcedureHandler handler = new StoredProcedureHandler(
                BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME, SQL_DELETE_ACTIVITY_DYNAMIC_DETAIL,
                null, DELETE_TYPES);
        for (ActivityDynamicDetail activityDynamicDetail : activityDynamicDetails) {
            try {
                params = new ArrayList<Object>(2);
                params.add(submissionId);
                params.add(activityDynamicDetail.getItemNumber());
                params.add(activityDynamicDetail.getSecondaryName());
                params.add(activityDynamicDetail.getSecondaryNumber());
                params.add(activityDynamicDetail.getTypeCode());
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
     * @throws DistributionServiceException
     *             If there is a data tier exception.
     */
    public void deleteAll(final Integer submissionId,final Integer contractId, 
            final Integer userProfileId) throws DistributionServiceException {

        try {
            List<Object> params = new ArrayList<Object>();
            params.add(submissionId);
            StoredProcedureHandler handler = new StoredProcedureHandler(
                    BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
                    SQL_DELETE_ALL_ACTIVITY_DYNAMIC_DETAILS, null, DELETE_ALL_TYPES);
            handler.execute(params.toArray());

        } catch (DAOException e) {
			throw new DistributionServiceDaoException("submissionId ["
					+ submissionId + "] contractId [" + contractId
					+ "] userProfileId [" + userProfileId + "]", e);
        }
    }
    /**
     * This method will inserts the {@link ActivityDynamicDetail} if it
     * does not exist and update it if it does exist.
     * 
     * @param contractId
     *            The currect Contract Id
     * @param submissionId
     *            The Submission Id
     * @param userProfileId
     *            The users profile Id
     * @param details
     *            The collection of ActivityDetail
     * @throws DistributionServiceException
     *             Thrown if there is an underlying error
     */
    public void insertUpdate(final Integer submissionId,final Integer contractId, 
            final Integer userProfileId, final Collection<ActivityDynamicDetail> details)
            throws DistributionServiceException {
        
        if (details == null) {
            return;
        }
        StoredProcedureHandler.OutputDefinition[] outputDefinition = 
            new StoredProcedureHandler.OutputDefinition[] {
                new StoredProcedureHandler.BeanOutputDefinition(
                ActivityDynamicDetail.class, EXISTS_FIELDS, StoredProcedureHandler.MANY) };
        
        Collection<ActivityDynamicDetail> inserts = new ArrayList<ActivityDynamicDetail>();
        Collection<ActivityDynamicDetail> updates = new ArrayList<ActivityDynamicDetail>();
        StoredProcedureHandler handler = new StoredProcedureHandler(
                BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME, SQL_EXISTS_ACTIVITY_DYNAMIC_DETAIL,
                outputDefinition, EXISTS_TYPES);
        try {
            for (ActivityDynamicDetail activityDetail : details) {
                List<Object> params = new ArrayList<Object>();
                params = new ArrayList<Object>();
                params.add(submissionId);
                params.add(activityDetail.getItemNumber());
                params.add(activityDetail.getSecondaryName());
                params.add(activityDetail.getSecondaryNumber());
                params.add(activityDetail.getTypeCode());
                Object [] tmpList = handler.execute(params.toArray());
                if (tmpList != null && ((Object[])tmpList[0]).length > 0) {
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
        insert( submissionId,contractId, userProfileId, inserts);
        update( submissionId,contractId, userProfileId, updates);
        
    }        

    public void insertUpdateIfChanged(final Integer submissionId, final Integer contractId,
            final Integer userProfileId, final Collection<ActivityDynamicDetail> details)
            throws DistributionServiceException {

        if (details == null) {
            return;
        }

        List<ActivityDynamicDetail> oldActivityDetails = select(submissionId, contractId,
                userProfileId);

        Collection<ActivityDynamicDetail> inserts = new ArrayList<ActivityDynamicDetail>();
        Collection<ActivityDynamicDetail> updates = new ArrayList<ActivityDynamicDetail>();

        for (ActivityDynamicDetail activityDetail : details) {
            boolean found = false;
            for (ActivityDynamicDetail oldActivityDetail : oldActivityDetails) {
                if (oldActivityDetail.getItemNumber().equals(activityDetail.getItemNumber()) 
                        && oldActivityDetail.getTypeCode().equals(activityDetail.getTypeCode())
                        && oldActivityDetail.getSecondaryName().equals(
                                activityDetail.getSecondaryName())) {
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
