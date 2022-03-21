package com.manulife.pension.ps.web.pif;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import com.manulife.pension.cache.CodeLookupCache;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.LockServiceDelegate;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.service.contract.util.WithdrawalReasonCodeEqualityPredicate;
import com.manulife.pension.service.contract.valueobject.PlanData;
import com.manulife.pension.service.contract.valueobject.WithdrawalReason;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.log.ServiceLogRecord;
import com.manulife.pension.validator.ValidationError;

/**
 * Base Action that handles requests for the Edit Plan Data screen.
 * 
 * @author rajenra
 */
public abstract class BasePIFDataController extends PsAutoController {

    /**
     * ACTION_FORWARD_DEFAULT.
     */
    public static final String ACTION_FORWARD_DEFAULT = "default";
    
    public static final String ACTION_FORWARD_PIF_SUBMISSION = "plansubmission";
    public static final String GENERAL_INFORMATION_TAB = "general";

    /**
     * ACTION_FORWARD_DEFAULT.
     */
    public static final String ACTION_FORWARD_DEFAULT_FOR_PRINT_FRIENDLY = "defaultForPrintFriendly";

    /**
     * ACTION_FORWARD_SAVE.
     */
    public static final String ACTION_FORWARD_SAVE = "save";

    /**
     * ACTION_FORWARD_SAVE.
     */
    public static final String ACTION_FORWARD_CONFIRM = "confirm";

    /**
     * ACTION_FORWARD_EDIT.
     */
    public static final String ACTION_FORWARD_EDIT = "edit";

    /**
     * ACTION_FORWARD_CANCEL.
     */
    public static final String ACTION_FORWARD_CANCEL = "cancel";

    /**
     * ACTION_FORWARD_ACCEPT.
     */
    public static final String ACTION_FORWARD_ACCEPT = "accept";

    /**
     * ACTION_FORWARD_CONTINUE_EDITING.
     */
    public static final String ACTION_FORWARD_CONTINUE_EDITING = "continueEditing";

    /**
     * ACTION_FORWARD_ERROR.
     */
    public static final String ACTION_FORWARD_ERROR = "error";

    /**
     * ACTION_FORWARD_BOOKMARK_DETECTED.
     */
    public static final String ACTION_FORWARD_BOOKMARK_DETECTED = "bookmarkDetected";

    /**
     * Field code to use when the error is global (not specific to any particular field(s).
     */
    public static final String GLOBAL_ERROR = "GLOBAL";

    public static final String CONTRACT_NUMBER_LOG_TEXT = "Contract Number";
    
    public static final String PLAN_INFORMATION_CHANGE_LOG_TEXT = "Plan information change";
    
    /**
     * Default constructor.
     */
    protected BasePIFDataController() {
        super(BasePIFDataController.class);
    }

    /**
     * Constructs a base withdrawal class using the specified logging class.
     * 
     * @param clazz The class used to configure the logger.
     */
	protected BasePIFDataController(final Class clazz) {
        super(clazz);
    }

    /**
     * Retrieves the plan data information for the view screen.
     * 
     * @param contractId The contract id to retrieve plan data for.
     * @return PlanData - The plan data information for the specified contract id.
     */
    protected PlanData getPIFDataFromPlanData(final Integer contractId) throws SystemException {

        final PlanData planData = ContractServiceDelegate.getInstance().readPlanData(contractId);
        return planData;
    }

    /**
     * Attempts to obtain a lock for the specified plan submission.
     * 
     * @param submissionId The plan that is being locked.
     * @param request The user's request.
     * @return boolean - True if the lock was successfully obtained, false otherwise.
     */
    protected boolean obtainLock(final Integer submissionId, final HttpServletRequest request)
            throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("obtainLock> Obtaining lock for plan submission [").append(
            		submissionId).append("].").toString());
        }
        return LockServiceDelegate.getInstance().lock(LockHelper.TPA_PLAN_LOCK_NAME,
                LockHelper.TPA_PLAN_LOCK_NAME + submissionId,
                getUserProfile(request).getPrincipal().getProfileId());
    }

    /**
     * Attempts to release any lock held for the specified plan submission.
     * 
     * @param submission The plan that is being locked.
     * @param request The user's request.
     */
    protected void releaseLock(final Integer submissionId, final HttpServletRequest request)
            throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("releaseLock> Releasing lock for plan submission [").append(
            		submissionId).append("].").toString());
        }
        LockServiceDelegate.getInstance().releaseLock(LockHelper.TPA_PLAN_LOCK_NAME,
                LockHelper.TPA_PLAN_LOCK_NAME + submissionId);
    }

    /**
     * Attempts to refresh any lock held for the specified plan submission.
     * 
     * @param submissionId The plan that is being locked.
     * @param request The user's request.
     * @return boolean - True if the lock was successfully refreshed, false otherwise.
     */
    protected boolean refreshLock(final Integer submissionId, final HttpServletRequest request)
            throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("refreshLock> Refreshing lock for plan [").append(
            		submissionId).append("].").toString());
        }
        return LockServiceDelegate.getInstance().lock(LockHelper.TPA_PLAN_LOCK_NAME,
                LockHelper.TPA_PLAN_LOCK_NAME + submissionId,
                getUserProfile(request).getPrincipal().getProfileId());
    }

    /**
     * Handles the lock obtain failure by generating an appropriate error message.
     * 
     * @param submissionId The plan that was being locked.
     * @param request The user's request.
     */
    protected void handleObtainLockFailure(final Integer submissionId, final HttpServletRequest request)
            throws SystemException {

        UserInfo lockOwnerUserInfo = null;
        try {
            lockOwnerUserInfo = SecurityServiceDelegate.getInstance().searchByProfileId(
                    getUserProfile(request).getPrincipal(),
                    LockServiceDelegate.getInstance().getLockInfo(LockHelper.TPA_PLAN_LOCK_NAME,
                            LockHelper.TPA_PLAN_LOCK_NAME + submissionId)
                            .getLockUserProfileId());
        } catch (final Exception exception) {
			final String message = "@@@@@ Exception while obtaining Lock: "
				+ exception.getMessage();
            logger.error(message);
        }
        final String lockOwnerDisplayName = (lockOwnerUserInfo == null) ? LockHelper.JH_REP_LABEL
                : LockHelper.getLockOwnerDisplayName(getUserProfile(request), lockOwnerUserInfo);

        final Collection<ValidationError> errors = new ArrayList<ValidationError>();
        errors.add(new ValidationError(new String[] { GLOBAL_ERROR },
                ErrorCodes.PLAN_LOCK_FAILURE_BECAUSE_ALREADY_LOCKED,
                new Object[] { lockOwnerDisplayName }, ValidationError.Type.warning));
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer(
                    "handleObtainLockFailure> Generated errors because of lock failure [").append(
                    errors).append("].").toString());
        }
        setErrorsInSession(request, errors);
    }

  
    /**
     * Logs the web activities
     * 
     * @param className
     * @param methodName
     * @param logData
     * @param profile
     * @param logger
     * @param interactionLog
     * @param logRecord
     * @throws SystemException
     */
	protected static void logWebActivity(String className, String methodName,
			String serviceName, String logData, UserProfile profile,
			Logger logger, Category interactionLog, ServiceLogRecord logRecord)
			throws SystemException {

		try {
			ServiceLogRecord record = (ServiceLogRecord) logRecord.clone();
			record.setServiceName(serviceName);
			record.setMethodName(className + ":" + methodName);
			record.setApplicationId(Environment.getInstance().getApplicationId());
			record.setData(logData);
			record.setDate(new Date());
			record.setPrincipalName(profile.getPrincipal().getUserName());
			record.setUserIdentity(String.valueOf(profile.getPrincipal()
					.getProfileId()));

			interactionLog.error(record);
		} catch (CloneNotSupportedException e) {
			// log the error, but don't interrupt regular processing
			logger.error("error when trying to log into MRL the data:"
					+ logData + ". Exception caught= " + e);
		}
	}
    /**
     * Prepares lookup data from cache. These data can be used in PIF pages
     * 
     * @param lookupData
     * @param planData
     */
	@SuppressWarnings("unchecked")
    protected void prepareLookupData(final Map<String, Object> lookupData, final PlanData planData) {

        // Check if mandatory distribution should be added/removed from list
        final List<WithdrawalReason> withdrawalReasons = (List<WithdrawalReason>) lookupData
                .get(CodeLookupCache.PLAN_WITHDRAWAL_REASONS);
        
        //Removing the 'IO' Withdrawal Reason
        for (int i = 0; i < withdrawalReasons.size(); i++) {							
        	WithdrawalReason withdrawalReason = withdrawalReasons.get(i);	
        	if(WithdrawalReason.IN_SERVICE_FOR_OTHER.equalsIgnoreCase(withdrawalReason.getWithdrawalReasonCode())){		
        		withdrawalReasons.remove(withdrawalReason);								
        	}																			
		}																				
        
        if (BooleanUtils.isTrue(planData.getAllowMandatoryDistributions())) {
            if (!CollectionUtils.exists(withdrawalReasons,
                    new WithdrawalReasonCodeEqualityPredicate(WithdrawalReason.MANDATORY_DISTRIBUTION_TERM))) {
                withdrawalReasons.add(planData.getMandatoryDistribution());
                Collections.sort(withdrawalReasons);
            }
        } else {
            withdrawalReasons.remove(planData.getMandatoryDistribution());
        }
    }
}
