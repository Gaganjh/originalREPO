package com.manulife.pension.service.withdrawal;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.environment.valueobject.StateTaxVO;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.PrincipalFactory;
import com.manulife.pension.service.security.SystemUser;
import com.manulife.pension.service.withdrawal.common.WithdrawalDataManager;
import com.manulife.pension.service.withdrawal.common.WithdrawalLookupDataManager;
import com.manulife.pension.service.withdrawal.dao.HardshipWithdrawalDAO;
import com.manulife.pension.service.withdrawal.dao.OnlineWithdrawalSTPDao;
import com.manulife.pension.service.withdrawal.dao.WithdrawalDao;
import com.manulife.pension.service.withdrawal.dao.WithdrawalInfoDao;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;
import com.manulife.pension.service.withdrawal.domain.Withdrawal;
import com.manulife.pension.service.withdrawal.domain.WithdrawalStateEnum;
import com.manulife.pension.service.withdrawal.helper.ActivityHistoryHelper;
import com.manulife.pension.service.withdrawal.log.ExpiredWithdrawalEventLog;
import com.manulife.pension.service.withdrawal.log.WithdrawalEventLog;
import com.manulife.pension.service.withdrawal.util.ReadyForEntryEmailHandler;
import com.manulife.pension.service.withdrawal.util.WithdrawalHelper;
import com.manulife.pension.service.withdrawal.util.WithdrawalRequestHelper;
import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantCategory;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantFlag;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantInfo;
import com.manulife.pension.service.withdrawal.valueobject.PendingReviewApproveWithdrawalCount;
import com.manulife.pension.service.withdrawal.valueobject.TaxesFlag;
import com.manulife.pension.service.withdrawal.valueobject.UserName;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalActivitySummary;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalMultiPayee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMetaData;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;
import com.manulife.pension.util.email.BodyPart;
import com.manulife.pension.util.log.EventLog;
import com.manulife.pension.util.log.EventLogFactory;
import com.manulife.pension.util.log.LogEventException;

import asposewobfuscated.wm;

/**
 * Bean implementation class for Enterprise Bean: WithdrawalService.
 * 
 * @ejb.bean name="WithdrawalService" display-name="Withdrawal Service" type="Stateless"
 *           view-type="both" transaction-type="Container"
 *           jndi-name="com.manulife.pension.service.withdrawal.WithdrawalServiceHome"
 *           local-jndi-name="com.manulife.pension.service.withdrawal.WithdrawalServiceLocalHome"
 * 
 * 
 * @ejb.interface generate="local,remote"
 * 
 * @ejb.transaction type="Required"
 * 
 * @ejb.util generate="logical"
 * 
 * @ejb.env-entry name="completedApolloWDThreshold" type="java.lang.Integer" value="5"
 * 
 * @ejb.env-entry name="pendingApolloWDThreshold" type="java.lang.Integer" value="30"
 * 
 * @websphere.bean
 */
public class WithdrawalServiceBean implements javax.ejb.SessionBean {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    // application ID (required by the EmployeeService)
    private static final String PS_APPLICATION_ID = "PS";

    private javax.ejb.SessionContext mySessionCtx;

    private Logger logger = Logger.getLogger(WithdrawalServiceBean.class);

    private static final String CLASS_NAME = WithdrawalServiceBean.class.getName();

    /**
     * @param profileId the employee profile id
     * @param contractId the employee contract id
     * @param principal the user principal
     * @return WithdrawalRequest The initial data object. If an exception occurs.
     * 
     * @ejb.interface-method view-type="local"
     */
    public WithdrawalRequest initiateNewWithdrawalRequest(final Integer profileId,
            final Integer contractId, final Principal principal) {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> initiateNewWithdrawalRequest");
        } // fi

        return Withdrawal.initiateNewWithdrawalRequest(profileId, contractId, principal);
    }

    /**
     * @param profileId the employee profile id
     * @param contractId the employee contract id
     * @param principal the user principal
     * @return WithdrawalRequest The initial data object. If an exception occurs.
     * 
     * @ejb.interface-method view-type="local"
     */
    public WithdrawalRequest initiateNewParticipantWithdrawalRequest(final Integer profileId,
            final Integer contractId) {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> initiateNewParticipantWithdrawalRequest");
        }
        return Withdrawal.initiateNewParticipantWithdrawalRequest(profileId, contractId);
    }

    /**
     * performStep1DefaultSetup is called to execute the default setup steps when step 2 is entered.
     * 
     * @param withdrawalRequest The withdrawal request to work with.
     * @return {@link WithdrawalRequest} The {@link WithdrawalRequest} that has been updated.
     * 
     * @ejb.interface-method view-type="local"
     */
    public WithdrawalRequest performStep1DefaultSetup(final WithdrawalRequest withdrawalRequest) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> performStep1DefaultSetup");
        } // fi

        WithdrawalRequest result = new Withdrawal(withdrawalRequest).performStep1DefaultSetup();

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- performStep1DefaultSetup");
        } // fi
        return result;
    }

    /**
     * performStep2DefaultSetup is called to execute the default setup steps when step 2 is entered.
     * 
     * @param withdrawalRequest The withdrawal request to work with.
     * @return {@link WithdrawalRequest} The {@link WithdrawalRequest} that has been updated.
     * 
     * @ejb.interface-method view-type="local"
     */
    public WithdrawalRequest performStep2DefaultSetup(final WithdrawalRequest withdrawalRequest) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> performStep2DefaultSetup");
        } // fi

        WithdrawalRequest result = new Withdrawal(withdrawalRequest).performStep2DefaultSetup();
       
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- performStep2DefaultSetup");
        } // fi
        return result;
    }

    /**
     * performReviewDefaultSetup is called to execute the default setup steps when review is
     * entered.
     * 
     * @param withdrawalRequest The withdrawal request to work with.
     * @return {@link WithdrawalRequest} The {@link WithdrawalRequest} that has been updated.
     * 
     * @ejb.interface-method view-type="local"
     */
    public WithdrawalRequest performReviewDefaultSetup(final WithdrawalRequest withdrawalRequest) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> performReviewDefaultSetup");
        } // fi

        WithdrawalRequest result = new Withdrawal(withdrawalRequest).performReviewDefaultSetup();
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- performReviewDefaultSetup");
        } // fi
        return result;
    }

    /**
     * Retrieve a withdrawal request for editing.
     * 
     * @param submissionId the id of the submission to read.
     * @param principal the {@link Principal} that requests this action.
     * @return WithdrawalRequest - the withdrawal Request value object.
     * 
     * @ejb.interface-method view-type="local"
     */
    public WithdrawalRequest readWithdrawalRequestForEdit(final Integer submissionId,
            final Principal principal) {

        final WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
        withdrawalRequest.setSubmissionId(submissionId);
        withdrawalRequest.setPrincipal(principal);
        withdrawalRequest.setStatusCode(WithdrawalStateEnum.DRAFT.getStatusCode());

        final Withdrawal withdrawal = new Withdrawal(withdrawalRequest);
        try {
            withdrawal.readWithdrawalRequestForEdit();
        } catch (DistributionServiceException e) {
            throw ExceptionHandlerUtility.wrap(e);
        }

        return withdrawal.getWithdrawalRequest();
    }

    /**
     * Retrieve a withdrawal request for viewing.
     * 
     * @param submissionId the id of the submission to read
     * @param principal the user principal
     * @return the withdrawal Request value object
     * 
     * @ejb.interface-method view-type="local"
     */
    public WithdrawalRequest readWithdrawalRequestForView(final Integer submissionId,
    // final Principal principal, final String cmaSiteCode){
            final Principal principal) {

        final WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
        withdrawalRequest.setSubmissionId(submissionId);
        withdrawalRequest.setStatusCode(WithdrawalStateEnum.DRAFT.getStatusCode());
        withdrawalRequest.setPrincipal(principal);
        // withdrawalRequest.setCmaSiteCode(cmaSiteCode);

        final Withdrawal withdrawal = new Withdrawal(withdrawalRequest);
        try {
            withdrawal.readWithdrawalRequestForView();
        } catch (DistributionServiceException e) {
            throw ExceptionHandlerUtility.wrap(e);
        }

        // TODO: Note that the userProfile ID is not set in the Withdrawal Request
        // Theoretically it shouldn't and read should return the current user profile id
        return withdrawal.getWithdrawalRequest();
    }

    /**
     * This method looks up the meta data for the given submission.
     * 
     * @param submissionId - The submission ID to look up the meta data for.
     * @return {@link WithdrawalRequestMetaData} - The meta data for the given submissionId.
     * 
     * @ejb.interface-method view-type="local"
     */
    public WithdrawalRequestMetaData getWithdrawalRequestMetaData(final Integer submissionId) {
        return WithdrawalDataManager.getWithdrawalRequestMetaData(submissionId);
    }

    /**
     * sendToApprover processes the sendToApprover action from Step2.
     * 
     * @param withdrawalRequest The Request to process.
     * @return WithdrawalRequest The value object.
     * 
     * @ejb.interface-method view-type="local"
     */
    public WithdrawalRequest sendToApprover(final WithdrawalRequest withdrawalRequest) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> sendToApprover");
        } // fi

        /*
         * Do business level validations of the request. Errors and warnings should be embedded into
         * the object they are related to. The errorCodes and warningCodes Collections exist for
         * each value object.
         */
        final Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        try {
            withdrawal.sendForApproval();
        } catch (DistributionServiceException e) {
            throw ExceptionHandlerUtility.wrap(e);
        }

        final WithdrawalRequest updatedWithdrawalRequest = withdrawal.getWithdrawalRequest();

        /*
         * If there are errors, we return the original request with the errors embedded in the
         * errorCodes Collection.
         */

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- sendToApprover");
        } // fi

        return updatedWithdrawalRequest;
    }

    /**
     * sendToReviewer processes the sendToReviewer action from Step2.
     * 
     * @param withdrawalRequest The Request to process.
     * @return WithdrawalRequest The value object.
     * 
     * @ejb.interface-method view-type="local"
     */
    public WithdrawalRequest sendToReviewer(final WithdrawalRequest withdrawalRequest) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> sendToReviewer");
        } // fi

        /*
         * Do business level validations of the request. Errors and warnings should be embedded into
         * the object they are related to. The errorCodes and warningCodes Collections exist for
         * each value object.
         */
        final Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        try {
            withdrawal.sendForReview();
        } catch (DistributionServiceException e) {
            throw ExceptionHandlerUtility.wrap(e);
        }

        final WithdrawalRequest updatedWithdrawalRequest = withdrawal.getWithdrawalRequest();

        /*
         * If there are errors, we return the original request with the errors embedded in the
         * errorCodes Collection.
         */

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- sendToReviewer");
        } // fi

        return updatedWithdrawalRequest;
    }

    /**
     * approve processes the approve action from Step2.
     * 
     * @param withdrawalRequest The Request to process.
     * @return WithdrawalRequest The value object.
     * 
     * @ejb.interface-method view-type="local"
     */
    public WithdrawalRequest approve(final WithdrawalRequest withdrawalRequest) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> approve");
        } // fi

        /*
         * Do business level validations of the request. Errors and warnings should be embedded into
         * the object they are related to. The errorCodes and warningCodes Collections exist for
         * each value object.
         */
        final Withdrawal withdrawal = new Withdrawal(withdrawalRequest);
        try {
            withdrawal.approve();
        } catch (DistributionServiceException e) {
            throw ExceptionHandlerUtility.wrap(e);
        }

        final WithdrawalRequest updatedWithdrawalRequest = withdrawal.getWithdrawalRequest();

        /*
         * If there are errors, we return the original request with the errors embedded in the
         * errorCodes Collection.
         */

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- approve");
        } // fi

        return updatedWithdrawalRequest;
    }

    /**
     * Processes an approved withdrawal request object.
     * 
     * @param withdrawalRequest - The approved withdrawal request.
     * 
     * @ejb.interface-method view-type="local"
     */
    public void processApproved(final WithdrawalRequest withdrawalRequest) {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> processApproved");
        }

        final Withdrawal withdrawal = new Withdrawal(withdrawalRequest);
        try {
            withdrawal.processApproved();
        } catch (DistributionServiceException e) {
            throw ExceptionHandlerUtility.wrap(e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- processApproved");
        }
    }

    /**
     * @param withdrawalRequest - The withdrawal request to be saved
     * 
     * @ejb.interface-method view-type="local"
     */
    public void save(final WithdrawalRequest withdrawalRequest) {
        final Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        try {
            withdrawal.save();
        } catch (DistributionServiceException e) {
            throw ExceptionHandlerUtility.wrap(e);
        }
    }

    /**
     * @param withdrawalRequest - The withdrawal request to be saved
     * 
     * @ejb.interface-method view-type="local"
     */
    public void delete(final WithdrawalRequest withdrawalRequest) {
        final Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        try {
            withdrawal.delete();
        } catch (DistributionServiceException e) {
            throw ExceptionHandlerUtility.wrap(e);

        }
    }

    /**
     * @param withdrawalRequest - The withdrawal request to be saved
     * 
     * @ejb.interface-method view-type="local"
     */
    public void deny(final WithdrawalRequest withdrawalRequest) {
        final Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        try {
            withdrawal.deny();
        } catch (DistributionServiceException e) {
            throw ExceptionHandlerUtility.wrap(e);
        }
    }

    /**
     * This method recalculates the money type section values.
     * 
     * @param withdrawalRequest The withdrawal request to be recalculated.
     * 
     * @ejb.interface-method view-type="local"
     */
    public void recalculate(final WithdrawalRequest withdrawalRequest) {
        new Withdrawal(withdrawalRequest).recalculate();
    }

    /**
     * This method retrieves a collection of all state tax options.
     * 
     * @param withdrawalRequest The withdrawal request to retrieve the state tax options for.
     * @return Collection The collection of {@link StateTaxVO} objects that are the available
     *         options.
     * 
     * @ejb.interface-method view-type="local"
     */
    public Collection getAllStateTaxOptions(final WithdrawalRequest withdrawalRequest) {
        final Withdrawal withdrawal = new Withdrawal(withdrawalRequest);
        return withdrawal.getAllStateTaxOptions();
    }

    /**
     * @see WithdrawalService#getWithdrawalInfo
     * 
     * @param participantId The participant ID to use.
     * @param contractId The contract ID to use.
     * @return WithdrawalInfo The data that was found.
     * 
     * @ejb.interface-method view-type="local"
     */
    public WithdrawalInfo getWithdrawalInfo(final int participantId, final int contractId) {
        return WithdrawalDataManager.getWithdrawalInfo(participantId, contractId);
    }

    /**
     * @see WithdrawalService#getParticipantInfo
     * 
     * @param profileId The profile ID to use.
     * @param participantId The participant ID to use.
     * @param contractId The contract ID to use.
     * @return ParticipantInfo The data that was found.
     * 
     * @ejb.interface-method view-type="local"
     * 
     */
    public ParticipantInfo getParticipantInfo(final int profileId, final int participantId,
            final int contractId) {
        return WithdrawalDataManager.getParticipantInfo(profileId, participantId, contractId);
    }

    /**
     * Gets the withdrawal request for the given contract and profile.
     * 
     * @param profileId The profile ID to use.
     * @param contractId The contract ID to use.
     * @return Collection - The withdrawal requests found.
     * 
     * @ejb.interface-method view-type="local"
     */
    public Collection getWithdrawalRequests(final Integer profileId, final Integer contractId) {
        return Withdrawal.getWithdrawalRequests(profileId, contractId);
    }

    /**
     * Returns an ordered list of WD reasons. The order in which the codes are returned is the
     * specified display order.
     * 
     * @param contractStatus Contract status code
     * @param participantStatus Participant status code
     * @return Collection - An ordered list of withdrawal reasons matching the contract and
     *         participant contract status which are enabled for OnlineWithdrawals.
     * @see http://mlisusgpsdev1/usgpdict/ASP_DataElementValuesForUserView.asp?ParmUserView=PARTICIPANT%5FCONTRACT
     * 
     * @ejb.interface-method view-type="local"
     */
    public Collection getParticipantWithdrawalReasons(final String contractStatus,
            final String participantStatus) {

        try {
            return WithdrawalInfoDao.getParticipantWithdrawalReasons(contractStatus,
                    participantStatus);
        } catch (SystemException e) {
            throw ExceptionHandlerUtility.wrap(e);
        }
    }

    /**
     * Returns an ordered list of WD reasons. The order in which the codes are returned is the
     * specified display order.
     * 
     * @param participantInfo Participant information object.
     * @return Collection - An ordered list of withdrawal reasons matching the contract and
     *         participant contract status which are enabled for OnlineWithdrawals.
     * @see http://mlisusgpsdev1/usgpdict/ASP_DataElementValuesForUserView.asp?ParmUserView=PARTICIPANT%5FCONTRACT
     * 
     * @ejb.interface-method view-type="local"
     */
    public Collection getParticipantPaymentToOptions(final ParticipantInfo participantInfo) {
        return WithdrawalDataManager.getParticipantPaymentToOptions(participantInfo);
    }

    /**
     * Gets the SessionContext.
     * 
     * @return SessionContext The session context.
     */
    public javax.ejb.SessionContext getSessionContext() {
        return mySessionCtx;
    }

    /**
     * {@inheritDoc}
     */
    public void setSessionContext(final javax.ejb.SessionContext ctx) {
        mySessionCtx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    public void ejbActivate() {
    }

    /**
     * ejbCreate is called as part of the EJB lifecycle.
     * 
     * @throws javax.ejb.CreateException If an create exception occurs.
     */
    public void ejbCreate() throws javax.ejb.CreateException {
    }

    /**
     * {@inheritDoc}
     */
    public void ejbPassivate() {
    }

    /**
     * {@inheritDoc}
     */
    public void ejbRemove() {
    }

    /**
     * @param contractId The current contract Id
     * @param employeeProfileId The employee profile id
     * @return the number of completed withdrawal transactions
     * 
     * @ejb.interface-method view-type="local"
     */
    public int getNumberOfCompletedWithdrawalTransaction(final Integer contractId,
            final Integer employeeProfileId) {

        return Withdrawal.getNumberOfCompletedWithdrawalTransaction(contractId, employeeProfileId);

    }

    /**
     * @param contractId The current contract Id
     * @param employeeProfileId The employee profile id
     * @return the number of completed withdrawal transactions
     * @ejb.interface-method view-type="local"
     */
    public int getNumberOfPendingWithdrawalTransaction(final Integer contractId,
            final Integer employeeProfileId) {

        return Withdrawal.getNumberOfPendingWithdrawalTransaction(contractId, employeeProfileId);

    }

    /**
     * Returns the user names (last name, first name) for each unique user_profile_id in the given
     * list.
     * 
     * @param userProfileIds Collection of unique user profile IDs
     * @return Map of UserName VOs keyed by user profile ID
     * 
     * @ejb.interface-method view-type="local"
     */
    public Map<Integer, UserName> getUserNames(final Collection<Integer> userProfileIds) {

        try {
            return WithdrawalInfoDao.getUserNames(userProfileIds);

        } catch (DistributionServiceException DistributionServiceException) {
            throw ExceptionHandlerUtility
                    .wrap(new SystemException(DistributionServiceException, CLASS_NAME,
                            "getUserNames", "Could not retrieve User Names from USER_PROFILE "));
        }
    }

    /**
     * Returns the count of Pending Review and Pending Approve withdrawal requests for the contract.
     * 
     * @param contractId The contract to get the count for.
     * @return {@link PendingReviewApproveWithdrawalCount} counts for pending Review/Approve
     *         withdrawal requests
     * @ejb.interface-method view-type="both"
     */
    public PendingReviewApproveWithdrawalCount getPendingReviewApproveWdRequestCounts(
            final Integer contractId) {
        final PendingReviewApproveWithdrawalCount pendingReviewApproveWithdrawalCount;

        try {
            pendingReviewApproveWithdrawalCount = new WithdrawalDao()
                    .getPendingReviewApproveCount(contractId);
        } catch (SystemException e) {
            throw ExceptionHandlerUtility.wrap(new SystemException(e, CLASS_NAME,
                    "getPendingReviewApproveWdRequestCounts",
                    "Could not get counts for Pending Review / Approve Requests "));
        }

        return pendingReviewApproveWithdrawalCount;
    }

    /**
     * Runs the default setup/validations for moving from step 1 to step 2.
     * 
     * @param withdrawalRequest The withdrawal request.
     * @ejb.interface-method view-type="local"
     */
    public void proceedToStep2(final WithdrawalRequest withdrawalRequest) {
        new Withdrawal(withdrawalRequest).proceedToStep2();
    }

    /**
     * Runs the default setup/validations for moving from step 2 to step 1.
     * 
     * @param withdrawalRequest The withdrawal request.
     * @ejb.interface-method view-type="local"
     */
    public void returnToStep1(final WithdrawalRequest withdrawalRequest) {
        new Withdrawal(withdrawalRequest).returnToStep1();
    }
    
    /**
     * Runs the termination and retirement date validation for moving from step 2 to step 1.
     * 
     * @param withdrawalRequest The withdrawal request.
     * @ejb.interface-method view-type="local"
     */
    public void returnToStep1WithTerminationOrRetirementDate(final WithdrawalRequest withdrawalRequest) {
        new Withdrawal(withdrawalRequest).returnToStep1WithTerminationOrRetirementDate();
    }
    
    /**
     * Marks all WithdrawalRequests having the expiry date before the current expiration date and
     * being in Draft or Pending states as Expired.
     * 
     * Logs this event in the Activity History and via MRL.
     * 
     * @param checkDate Date used to test if WithdrawalRequests expired (typically the current date)
     * @param profileId String value containing the user profile ID or system profile ID (3)
     * @return int - The number of withdrawals that have been marked as expired.
     * 
     * @ejb.interface-method view-type="both"
     */
    public int markExpiredWithdrawals(final Date checkDate, final Integer profileId) {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> markExpiredWithdrawals");
        } // fi
        int noOfExpiredRecords = 0;

        try {
            WithdrawalDao wdDao = new WithdrawalDao();
            Collection<Integer> wdRequests = wdDao.getExpiringWithdrawals(checkDate);

            for (Integer submissionId : wdRequests) {
                if (wdDao.expireWithdrawal(submissionId, profileId)) {
                    logger.debug("Expired submission ID: " + submissionId);
                    wdDao.logActivitySummary(submissionId, WithdrawalActivitySummary.ACTION_CODE_EXPIRED,
                            profileId);

                    // MRL Event logging
                    EventLog eventLog = EventLogFactory.getInstance().createEventLog(
                            ExpiredWithdrawalEventLog.class.getName());
                    eventLog.setClassName(this.getClass().getName());
                    eventLog.setMethodName("markExpiredWithdrawals");
                    eventLog.setUserName(Integer.toString(profileId)); // String conversion
                    // required
                    eventLog.addLogInfo(ExpiredWithdrawalEventLog.SUBMISSION_ID, submissionId);

                    try {
                        eventLog.log();
                    } catch (LogEventException e) {
                        SystemException se = new SystemException(e, this.getClass().getName(),
                                "markExpiredWithdrawals",
                                "Problem occured during logging for Expired Withdrawal. "
                                        + "Submission ID was: " + submissionId
                                        + ";  User Profile ID was: " + profileId
                                        + "; applicationId: " + PS_APPLICATION_ID);
                        throw ExceptionHandlerUtility.wrap(se);
                    }

                    final WithdrawalRequestMetaData withdrawalRequestMetaData = WithdrawalDataManager
                            .getWithdrawalRequestMetaData(submissionId);
                    WithdrawalHelper.fireWithdrawalExpiredEvent(withdrawalRequestMetaData,
                            profileId);

                    noOfExpiredRecords++;
                } else {
                    logger.error("Could not expire submission ID: " + submissionId);
                }
            }

        } catch (SystemException e) {
            throw ExceptionHandlerUtility.wrap(new SystemException(e, CLASS_NAME,
                    "markExpiredWithdrawals", "Could not expire submission. "));
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- markExpiredWithdrawals");
        } // fi

        return noOfExpiredRecords;
        //
        // if (logger.isDebugEnabled()) {
        // logger.debug("entry -> markExpiredWithdrawals");
        // } // fi
        // int noOfExpiredRecords = 0;
        //
        // final WithdrawalDao withdrawalDao = new WithdrawalDao();
        // final Collection<Integer> wdRequests;
        // try {
        // wdRequests = withdrawalDao.getExpiringWithdrawals(checkDate);
        // } catch (SystemException systemException) {
        // throw ExceptionHandlerUtility.wrap(new SystemException(systemException, CLASS_NAME,
        // "markExpiredWithdrawals", "Could not lookup submissions with check date of ["
        // + checkDate + "] and profile ID [" + profileId + "]."));
        // } // end try/catch
        //
        // for (Integer submissionId : wdRequests) {
        // final boolean expireSucceded;
        //
        // try {
        // final WithdrawalService withdrawalService = WithdrawalServiceUtil.getLocalHome()
        // .create();
        //
        // expireSucceded = withdrawalService.expireSingleWithdrawal(submissionId, profileId);
        // } catch (EJBException ejbException) {
        // // The single update failed, but we want to continue running the rest.
        // logger.error("expireSingleWithdrawal caused an exception", ejbException);
        //
        // continue;
        // } catch (CreateException createException) {
        // // The single update failed, but we want to continue running the rest.
        // logger.error("expireSingleWithdrawal caused an exception", createException);
        //
        // continue;
        // } catch (NamingException namingException) {
        // // The single update failed, but we want to continue running the rest.
        // logger.error("expireSingleWithdrawal caused an exception", namingException);
        //
        // continue;
        // } // end try/catch
        //
        // if (expireSucceded) {
        // logger.debug("Expired submission ID: " + submissionId);
        // try {
        // withdrawalDao.logActivitySummary(submissionId,
        // ActivitySummary.ACTION_CODE_EXPIRED, profileId);
        // } catch (SystemException systemException) {
        // throw ExceptionHandlerUtility.wrap(new SystemException(systemException,
        // CLASS_NAME, "markExpiredWithdrawals",
        // "Could not expire submission with ID [" + submissionId
        // + "] and profile ID [" + profileId + "]."));
        // } // end try/catch
        //
        // // MRL Event logging
        // EventLog eventLog;
        // try {
        // eventLog = EventLogFactory.getInstance().createEventLog(
        // ExpiredWithdrawalEventLog.class.getName());
        // } catch (SystemException systemException) {
        // throw ExceptionHandlerUtility.wrap(new SystemException(systemException,
        // CLASS_NAME, "markExpiredWithdrawals",
        // "Could not expire submission with ID [" + submissionId
        // + "] and profile ID [" + profileId + "]."));
        // } // end try/catch
        // eventLog.setClassName(this.getClass().getName());
        // eventLog.setMethodName("markExpiredWithdrawals");
        // eventLog.setUserName(Integer.toString(profileId)); // String conversion
        // // required
        // eventLog.addLogInfo(ExpiredWithdrawalEventLog.SUBMISSION_ID, submissionId);
        //
        // try {
        // eventLog.log();
        // } catch (LogEventException logEventException) {
        // SystemException systemException = new SystemException(logEventException, this
        // .getClass().getName(), "markExpiredWithdrawals",
        // "Problem occured during logging for Expired Withdrawal. "
        // + "Submission ID was: " + submissionId
        // + "; User Profile ID was: " + profileId + "; applicationId: "
        // + PS_APPLICATION_ID);
        // throw ExceptionHandlerUtility.wrap(systemException);
        // } // end try/catch
        //
        // noOfExpiredRecords++;
        // } else {
        // logger.error("Could not expire with submission ID [" + submissionId + "].");
        // } // fi
        // } // end for
        //
        // if (logger.isDebugEnabled()) {
        // logger.debug("exit <- markExpiredWithdrawals");
        // } // fi
        //
        // return noOfExpiredRecords;
    }

    /**
     * Expires a single withdrawal request.
     * 
     * @param submissionId The submission ID to expire.
     * @param profileId The profile ID to use to expire.
     * @return boolean - True if successful, false otherwise.
     * 
     * @ejb.interface-method view-type="local"
     * @ejb.transaction type="RequiresNew"
     */
    public boolean expireSingleWithdrawal(final Integer submissionId, final Integer profileId) {
        try {
            return new WithdrawalDao().expireWithdrawal(submissionId, profileId);
        } catch (SystemException systemException) {
            throw ExceptionHandlerUtility.wrap(new SystemException(systemException, CLASS_NAME,
                    "expireSingleWithdrawal", "Could not expire submission with ID ["
                            + submissionId + "] and profile ID [" + profileId + "]."));
        } // end try/catch
    }

    /**
     * Handler invoked after setting CSF "Allow Online Withdrawal Requests" to NO: - marks all
     * withdrawal request that are not in an end state as expired.
     * 
     * @param contractId ID of the contract for which the online W/D feature has changed
     * @param profileId ID of the user that modified the Contract Feature
     * 
     * @ejb.interface-method view-type="local"
     */
    public void handleDisableOnlineWithdrawals(final Integer contractId, final Integer profileId,
            final String userRoleCode) {

        logger.debug("entry -> handleDisableOnlineWithdrawals");

        WithdrawalDao wdDao = new WithdrawalDao();

        final Integer systemUserProfileId = SystemUser.SUBMISSION.getProfileId();

        ArrayList<WithdrawalStateEnum> statusList = new ArrayList<WithdrawalStateEnum>();
        statusList.add(WithdrawalStateEnum.DRAFT);
        statusList.add(WithdrawalStateEnum.PENDING_APPROVAL);
        statusList.add(WithdrawalStateEnum.PENDING_REVIEW);

        GregorianCalendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE, -1);

        try {
            Collection<Integer> ids = wdDao.getWithdrawalsByContractAndStatus(contractId,
                    statusList, userRoleCode);
            for (Integer id : ids) {
                logger.debug("Setting expiration date to "
                        + java.text.SimpleDateFormat.getDateTimeInstance().format(cal.getTime())
                        + " for submission ID = " + id + " and contract ID = " + contractId);
                wdDao.setExpirationDate(id, new java.sql.Date(cal.getTimeInMillis()),
                        systemUserProfileId);

                // MRL Event logging
                EventLog eventLog = EventLogFactory.getInstance().createEventLog(
                        WithdrawalEventLog.class.getName());
                eventLog.setClassName(this.getClass().getName());
                eventLog.setMethodName("handleDisableOnlineWithdrawals");
                // required
                eventLog.setUserName(Integer.toString(systemUserProfileId));
                eventLog.addLogInfo(ExpiredWithdrawalEventLog.SUBMISSION_ID, id);
                eventLog.addLogInfo(WithdrawalEventLog.WITHDRAWAL_ACTION,
                        "handleDisableOnlineWithdrawals");

                try {
                    eventLog.log();
                } catch (LogEventException e) {
                    SystemException se = new SystemException(e, this.getClass().getName(),
                            "handleDisableOnlineWithdrawals",
                            "Problem occured during logging for Expired Withdrawal. "
                                    + "Submission ID was: " + id + "; applicationId: "
                                    + PS_APPLICATION_ID);
                    throw ExceptionHandlerUtility.wrap(se);
                }
            }
        } catch (DistributionServiceException DistributionServiceException) {
            throw ExceptionHandlerUtility.wrap(DistributionServiceException);
        } catch (SystemException systemException) {
            throw ExceptionHandlerUtility.wrap(systemException);
        }

        logger.debug("exit <- handleDisableOnlineWithdrawals");
    }

    /**
     * Handler invoked after setting CSF "Allow Online Withdrawal Requests" to YES: - currently no
     * action.
     * 
     * @param contractId ID of the contract for which the online W/D feature has changed
     * @param profileId ID of the user that modified the Contract Feature
     * 
     * @ejb.interface-method view-type="local"
     */
    public void handleEnableOnlineWithdrawals(final Integer contractId, final Integer profileId) {
        logger.debug("entry -> handleEnableOnlineWithdrawals");
        logger.debug("exit <- handleEnableOnlineWithdrawals");
    }

    /**
     * Handler invoked after setting CSF "Allow Online Withdrawal Requests" to NO: - Change the
     * status to Pending Approval for all Withdrawal requests that are currently in the Pending
     * Review status.
     * 
     * @param contractId ID of the contract for which the online W/D feature has changed
     * @param principal principal of the user that modified the Contract Feature
     * 
     * @ejb.interface-method view-type="local"
     */
    public void handleEnableOneStepApprovals(final Integer contractId, final Principal principal,
            final String cmaSiteCode) {
        logger.debug("entry -> handleEnableOneStepApprovals");

        WithdrawalDao wdDao = new WithdrawalDao();

        final Principal systemPrincipal = PrincipalFactory.instance().getSystemPrincipal(
                SystemUser.SUBMISSION);

        ArrayList<WithdrawalStateEnum> statusList = new ArrayList<WithdrawalStateEnum>();
        statusList.add(WithdrawalStateEnum.PENDING_REVIEW);

        try {
            Collection<Integer> submissionIds = wdDao.getWithdrawalsByContractAndStatus(contractId,
                    statusList, WithdrawalRequest.USER_ROLE_PLAN_SPONSOR_CODE);
            for (Integer submissionId : submissionIds) {
                logger.debug("Moving submission ID = " + submissionId + " (contract ID = "
                        + contractId + ") from status=PendingReview to status=PendingApproval");

                // WithdrawalRequest wdReq = readWithdrawalRequestForView(id, principal,
                // cmaSiteCode);
                final WithdrawalRequest withdrawalRequest = readWithdrawalRequestForView(
                        submissionId, systemPrincipal);


                // Set the profile ID of the user that modifies this request (via a CSF change)
                withdrawalRequest.setPrincipal(systemPrincipal);
                withdrawalRequest.setIgnoreErrors(Boolean.TRUE);
                withdrawalRequest.setIgnoreWarnings(Boolean.TRUE);

                final Withdrawal withdrawal = new Withdrawal(withdrawalRequest);
                withdrawal.sendForApproval();
            } // end for

        } catch (DistributionServiceException e) {
            throw ExceptionHandlerUtility.wrap(e);
        }

        logger.debug("exit <- handleEnableOneStepApprovals");
    }

    /**
     * Handler invoked after setting CSF "Allow Online Withdrawal Requests" to YES: - currently no
     * action.
     * 
     * @param contractId ID of the contract for which the online W/D feature has changed
     * @param profileId ID of the user that modified the Contract Feature
     * 
     * @ejb.interface-method view-type="local"
     */
    public void handleEnableTwoStepApprovals(final Integer contractId, final Integer profileId) {
        logger.debug("entry -> handleEnableTwoStepApprovals");
        logger.debug("exit <- handleEnableTwoStepApprovals");
    }

    /**
     * Retrieves contract information pertinent to a withdrawal request.
     * 
     * @param contractId The employee contract id
     * @param principal The user's principal
     * @return ContractInfo - The contract information relevant to a withdrawal request.
     * 
     * @ejb.interface-method view-type="local"
     */
    public ContractInfo getContractInfo(final Integer contractId, final Principal principal) {

        return WithdrawalDataManager.loadContractInfo(contractId, principal);
    }

    /**
     * Retreives the Lookup data for the withdrawal request.
     * 
     * @param contractInfo The contract information object.
     * @param particiapntStatusCode The participant status code.
     * @param keys Collection of keys to get lookup data for.
     * @return Map - collection of the look up data.
     * 
     * @ejb.interface-method view-type="local"
     */
    public Map getLookupData(final ContractInfo contractInfo, final String participantStatusCode,
            final Collection<String> keys) {

        return new WithdrawalLookupDataManager(contractInfo, participantStatusCode, keys)
                .getLookupData();

    }

    /**
     * Returns the agreed legalese content depending on whether it's psw or ezk
     * 
     * @param submissionId the submission id
     * @param cmaSiteCode whether the agreed legalese is psw or ezk
     * @return String object
     * 
     * @ejb.interface-method view-type="local"
     */
    public String getAgreedLegaleseText(final Integer submissionId, final String cmaSiteCode) {

        WithdrawalDao dao = new WithdrawalDao();
        try {
            return dao.getAgreedLegaleseText(submissionId, cmaSiteCode);
        } catch (SystemException e) {
            throw ExceptionHandlerUtility.wrap(e);
        }
    }

    /**
     * Returns the most recent withdrawal txn for a given contract and participant
     * 
     * @param profileId the participant's profile id
     * @param contractId the contract number
     * @return WithdrawalRequest object
     * 
     * @ejb.interface-method view-type="both"
     */
    public WithdrawalRequest getMostRecentWithdrawalRequest(final Integer profileId,
            final Integer contractId) throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getMostRecentWithdrawalRequest");
        }
        WithdrawalRequest wr = null;
        try {
            wr = Withdrawal.getMostRecentWithdrawalRequest(profileId, contractId);
        } catch (DistributionServiceException e) {
            throw ExceptionHandlerUtility.wrap(new SystemException(e, CLASS_NAME,
                    "getMostRecentWithdrawalRequest()", "contractId " + contractId + " profileId "
                            + profileId));
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getMostRecentWithdrawalRequest");
        }
        return wr;
    }

    /**
     * submitParticipantInitiatedWithdrawal - processes the participant initiated withdrawal from
     * ezk.
     * 
     * @param withdrawalRequest The Request to process.
     * @return WithdrawalRequest The value object.
     * 
     * @ejb.interface-method view-type="local"
     */
    public WithdrawalRequest submitParticipantInitiatedWithdrawal(
            final WithdrawalRequest withdrawalRequest) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> submitParticipantInitiatedWithdrawal");
        }

        final Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        try {
            withdrawal.submitParticipantInitiatedWithdrawal();
            withdrawal.updateAtRiskAddress();
        } catch (SystemException e) {
            throw ExceptionHandlerUtility.wrap(e);
        } catch (RuntimeException e) {
            throw ExceptionHandlerUtility.wrap(new SystemException(e, CLASS_NAME,
                    "submitParticipantInitiatedWithdrawal()",
                    "RuntimeException thrown for contractId " + withdrawalRequest.getContractId()
                            + " profileId " + withdrawalRequest.getEmployeeProfileId()));
        }
        
        final WithdrawalRequest updatedWithdrawalRequest = withdrawal.getWithdrawalRequest();
        

        /*
         * If there are errors, we return the original request with the errors embedded in the
         * errorCodes Collection.
         */

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- submitParticipantInitiatedWithdrawal");
        } // fi

        return updatedWithdrawalRequest;
    }
    /**
     * 
     * @param contractNumber
     * @return
     * @ejb.interface-method view-type="local"
     */
    public String getRequiresSpousalConsentForDistributions(final Integer contractNumber) {
        try {
            return new WithdrawalDao().getRequiresSpousalConsentForDistributions(contractNumber);
        } catch (SystemException systemException) {
            throw ExceptionHandlerUtility.wrap(new SystemException(systemException, CLASS_NAME,
            		"getRequiresSpousalConsentForDistributions()", "contractNumber: "
                            + contractNumber + "]."));
        } // end try/catch
    } 
    /**
     * 
     * @return
     *  @ejb.interface-method view-type="local"
     */
    public List<ParticipantCategory> getParticipantCategoryList(){
        try {
            return new WithdrawalDao().getParticipantCategoryList();
        } catch (SystemException systemException) {
        	throw ExceptionHandlerUtility.wrap(new SystemException(systemException, CLASS_NAME,
            		"getRequiresSpousalConsentForDistributions()", "contractNumber"));
        } // end try/catch
    } 
    /**
     * 
     * @param profileId
     * @param contractNumber
     * @return
     * @ejb.interface-method view-type="local"
     */
    public ParticipantFlag getPartitcipantExceptionFlagDetials(String profileId , String contractNumber) {
        try {
            return new WithdrawalDao().getPartitcipantExceptionFlagDetials(profileId, contractNumber);
        } catch (SystemException systemException) {
        	throw ExceptionHandlerUtility.wrap(new SystemException(systemException, CLASS_NAME,
            		"getRequiresSpousalConsentForDistributions()", "contractNumber"));
        } // end try/catch
    } 
    
    /**
     * 
     * @param participantFlag
     * @ejb.interface-method view-type="local"
     */
    public void saveParticipantFlagInfo(ParticipantFlag participantFlag)  {
     try {
    	new WithdrawalDao().insertParticipantFlagInfo(participantFlag);
      } catch (DistributionServiceException systemException) {
    	  throw ExceptionHandlerUtility.wrap(new SystemException(systemException, CLASS_NAME,
        		"insertParticipantFlagInfo()", "contractNumber"));
      } // end try/catch
     } 
    
    
    /**
     * get AWD Email Content to send via API
     * 
     * @param withdrawalRequest The Request to process.
     * @return WithdrawalRequest The value object.
     * @throws SystemException 
     * 
     * @ejb.interface-method view-type="local"
     */
    public ArrayList<BodyPart> getReadyForEntryEmailContent(final WithdrawalRequest withdrawalRequest) throws SystemException {
        try {
            Collection<String> keys = ReadyForEntryEmailHandler.getLookupKeys();
        Collection<Integer> userProfileIds = ReadyForEntryEmailHandler
                .getUserProfileIds(withdrawalRequest);

        Map lookupDataMap = new WithdrawalLookupDataManager(null, StringUtils.EMPTY, keys)
                .getLookupData();

        Map<Integer, UserName> userNamesMap;
        userNamesMap = WithdrawalInfoDao.getUserNames(userProfileIds);

        final ReadyForEntryEmailHandler readyForEntryEmailHandler = new ReadyForEntryEmailHandler(
           withdrawalRequest, lookupDataMap, userNamesMap);
           return readyForEntryEmailHandler.getSubmissionEmailContent();
        } catch (final DistributionServiceException e) {
            throw ExceptionHandlerUtility.wrap(e);
           
        } // end try/catch
    }
    
    /**
     * send AWD Email
     * 
     * @param withdrawalRequest The Request to process.
     * @return WithdrawalRequest The value object.
     * @throws SystemException 
     * 
     * @ejb.interface-method view-type="local"
     */
    public void sendReadyForEntryEmail(final WithdrawalRequest withdrawalRequest) throws SystemException {
        try {
            Collection<String> keys = ReadyForEntryEmailHandler.getLookupKeys();
        Collection<Integer> userProfileIds = ReadyForEntryEmailHandler
                .getUserProfileIds(withdrawalRequest);

        Map lookupDataMap = new WithdrawalLookupDataManager(null, StringUtils.EMPTY, keys)
                .getLookupData();

        Map<Integer, UserName> userNamesMap;
        userNamesMap = WithdrawalInfoDao.getUserNames(userProfileIds);

        final ReadyForEntryEmailHandler readyForEntryEmailHandler = new ReadyForEntryEmailHandler(
           withdrawalRequest, lookupDataMap, userNamesMap);
           readyForEntryEmailHandler.sendEmail();
        } catch (final DistributionServiceException e) {
            throw ExceptionHandlerUtility.wrap(e);
           
        } // end try/catch
    }
    
    /**
     * A Withdrawal request.
     *
     * @param submissionId The submission ID for the withdrawal request.
     * @param participantId The of the user for whom this withdrawal request is forr.
     * @param contractId of the participant.
     * @return String - control block returned from Apollo
     *
     * @ejb.interface-method view-type="local"
     * @ejb.transaction type="RequiresNew"
     */
    public String callApolloSTPForOnlineWithdrawal(final Integer submissionId, final int participantId, Integer contractId) {
        String controlBlock = null;
        try {
            controlBlock = new OnlineWithdrawalSTPDao().executeApolloOWSTPStoredProc(contractId, BigDecimal.valueOf(participantId), submissionId);
            String statusCode = "";
			if (StringUtils.isNotEmpty(controlBlock)) {
				statusCode = controlBlock.substring(3, 7);
			}
            if(!"0000".contains(statusCode)){  // if status code is not success then we rollback
                mySessionCtx.setRollbackOnly(); // this is called to explicitly rollback
           }
        } catch (SystemException systemException) {
            mySessionCtx.setRollbackOnly();
            throw ExceptionHandlerUtility.wrap(new SystemException(systemException, CLASS_NAME,
                    "callApolloSTPForOnlineWithdrawal", "STP failed for submission with ID ["
                            + submissionId + "] and participantId ID [" + participantId + "]."));
        } // end try/catch
		return controlBlock;
        
    }

    /**
     * A Withdrawal request.
     *
     * @param submissionId The submission ID for the withdrawal request.
     * @param participantId The of the user for whom this withdrawal request is forr.
     * @param contractId of the participant.
     * @return String - control block returned from Apollo
     *
     * @ejb.interface-method view-type="local"
     * @ejb.transaction type="RequiresNew"
     */
    public String executeLpTxnGenSTPStoredProc(final Integer submissionId, final int participantId, Integer contractId , String reasonCode,final java.sql.Date rateEffectiveDate,final String tpaFeeFlag,int payeeCount) {
        String controlBlock = null;
        try {
            controlBlock = new OnlineWithdrawalSTPDao().executeLpTxnGenSTPStoredProc(contractId, BigDecimal.valueOf(participantId), submissionId, reasonCode, rateEffectiveDate, tpaFeeFlag,payeeCount);
            String statusCode = "";
			if (StringUtils.isNotEmpty(controlBlock)) {
				statusCode = controlBlock.substring(3, 7);
			}
            if(!"0000".contains(statusCode)){  // if status code is not success then we rollback
                mySessionCtx.setRollbackOnly(); // this is called to explicitly rollback
           }
        } catch (SystemException systemException) {
            mySessionCtx.setRollbackOnly();
            throw ExceptionHandlerUtility.wrap(new SystemException(systemException, CLASS_NAME,
                    "callApolloSTPForOnlineWithdrawal", "STP failed for submission with ID ["
                            + submissionId + "] and participantId ID [" + participantId + "]."));
        } // end try/catch
		return controlBlock;
        
    }
    /**
     * 
     * @param request
     * @ejb.interface-method view-type="local"
     */
    public void updatedPayeesForMultipleDestination(WithdrawalRequest request){
    	Withdrawal.updatedPayeesForMultipleDestination( request);
    }
    /**
     * get Payee Tax Flag Details
     * @return
     * @throws SystemException
     * @throws SQLException
     * @ejb.interface-method view-type="local"
     */
    public  List<TaxesFlag> getPayeeTaxFlag() throws SystemException, SQLException {
    	return WithdrawalInfoDao.getPayeeTaxFlag();
    }
    
    /**
     * get getMultipayeeSection
     * @param contractId
     * @param req
     * @ejb.interface-method view-type="local"
     */
    public void getMultipayeeSection(Integer contractId, WithdrawalRequest req){
    	Withdrawal.getMultipayeeSection(contractId,req);
    }
    /**
     * get getMultipayeeSection
     * @param contractId
     * @param req
     * @throws SystemException 
     * @ejb.interface-method view-type="local"
     */

    public boolean validateMD(Integer contractId) throws SystemException {
    	
		return WithdrawalInfoDao.getvalidateMultiDestination(contractId);
    	
    }
    /**
	  * return contribution for Hardship
	  * @param contractNumber
	  * @param ssn
	  * @param asOfDate
	  * @return
	  * @throws SystemException
	  * @ejb.interface-method view-type="local
	  * 
	  */
	 public double getParticipantNetEEDeferralContributions(int profileId,int contractNumber, String ssn, Date asOfDate) throws SystemException{
		 
		 double eeDeferralBalance = HardshipWithdrawalDAO.getParticiantEEDeferralContributions(profileId, contractNumber);
		 
		 ContractServiceDelegate delegate = ContractServiceDelegate.getInstance();           
	        String eedefInd = delegate.getEEDEFEarningsFlagInd(contractNumber);
	        
	        double netEEDeferralContributions= HardshipWithdrawalDAO.getParticipantNetEEDeferralContributions(contractNumber, ssn, asOfDate);
	        
	        if("Y".equals(eedefInd)){
	        	return eeDeferralBalance;
	        }else if (netEEDeferralContributions < eeDeferralBalance){
	        		return netEEDeferralContributions;
	        	}
	        
	        return eeDeferralBalance;
	        
	 }
	 /**
	     * Move Withdrawal Request To Review/Approve State
	     * @param submissionId the id of the submission 
	     * @param principal the user principal
	     * @return the withdrawal Request value object
	     * 
	     * @ejb.interface-method view-type="local"
	     */
	    public boolean sendWithdrawalNotificationsForReviewOrApprove(final Integer submissionId,String statusCode, WithdrawalRequest withdrawalReq) {
	        boolean transitionCompleted=false;
	        withdrawalReq.setSubmissionId(submissionId);
	        withdrawalReq.setStatusCode(statusCode);
	        final Withdrawal withdrawal = new Withdrawal(withdrawalReq);
	        try {
	            withdrawal.readWithdrawalRequestForView();
	            withdrawal.sendWithdrawalNotificationsForReviewOrApprove(statusCode);
	            transitionCompleted=true;
	        } catch (DistributionServiceException e) {
	            throw ExceptionHandlerUtility.wrap(e);
	        }
	        return transitionCompleted;
	    }
	    
	    /**
	     * Update the Activity tables
	     * @param submissionId the id of the submission 
	     * @return boolean to say update failed
	     * @ejb.interface-method view-type="local"
	     */
	    public boolean updateActivityData(final Integer submissionId,final Principal principal) {
	        boolean transitionCompleted=false;
	        final WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
	        withdrawalRequest.setSubmissionId(submissionId);
	        withdrawalRequest.setPrincipal(principal);
	        final Withdrawal withdrawal = new Withdrawal(withdrawalRequest);
	        try {
	            withdrawal.readWithdrawalRequestForView();
	            ActivityHistoryHelper.saveOriginalValues(withdrawal);
	            ActivityHistoryHelper.saveSummary(withdrawal,
	                        WithdrawalActivitySummary.ACTION_CODE_SENT_FOR_REVIEW);
	            transitionCompleted=true;
	        } catch (DistributionServiceException e) {
	            throw ExceptionHandlerUtility.wrap(e);
	        }
	        return transitionCompleted;
	    }
}
