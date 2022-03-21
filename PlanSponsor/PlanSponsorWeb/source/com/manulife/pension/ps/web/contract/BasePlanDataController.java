package com.manulife.pension.ps.web.contract;

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
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.delegate.ContractAssemblyServiceDelegate;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.LockServiceDelegate;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.withdrawal.GraphLocation;
import com.manulife.pension.service.contract.util.WithdrawalReasonCodeEqualityPredicate;
import com.manulife.pension.service.contract.valueobject.PlanData;
import com.manulife.pension.service.contract.valueobject.WithdrawalReason;
import com.manulife.pension.service.employee.valueobject.UserIdType;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.ServiceLogRecord;
import com.manulife.pension.validator.ValidationError;

public abstract class BasePlanDataController extends PsAutoController {

	private static final Logger logger = Logger.getLogger(BasePlanDataController.class);
    /**
     * ACTION_FORWARD_DEFAULT.
     */
    public static final String ACTION_FORWARD_DEFAULT = "default";

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
    
    public static final String QUALIFIED_BIRTH_OR_ADOPTION_TEXT = "Qualified birth or adoption";
    
    /**
     * Default constructor.
     */
    protected BasePlanDataController() {
        super(BasePlanDataController.class);
    }

    /**
     * Constructs a base withdrawal class using the specified logging class.
     * 
     * @param clazz The class used to configure the logger.
     */
    protected BasePlanDataController(final Class clazz) {
        super(clazz);
    }

    /**
     * Retrieves the plan data information for the view screen.
     * 
     * @param contractId The contract id to retrieve plan data for.
     * @return PlanData - The plan data information for the specified contract id.
     */
    protected PlanData getPlanData(final Integer contractId) throws SystemException {

        final PlanData planData = ContractServiceDelegate.getInstance().readPlanData(contractId);
        return planData;
    }

    /**
     * Handles errors in the business tier. Extracts the errors, converts them to UI tier errors,
     * and makes them available in the UI.
     * 
     * @param mapping The mapping.
     * @param request The request.
     * @param planData The Plan Data to handle.
     * @return The ActionForward to go to.
     */
    protected void handleBusinessErrors(final HttpServletRequest request, final PlanData planData) {
        final PlanDataUi resultUi = new PlanDataUi(planData);
        final Collection<ValidationError> businessMessages = resultUi
                .getValidationMessages(new GraphLocation("planDataUi"));

        if (logger.isDebugEnabled()) {
            logger
                    .debug(new StringBuffer(
                            "handleBusinessErrors> Validation errors from biz tier [").append(
                            businessMessages).append("].").toString());
        }
        setErrorsInSession(request, businessMessages);

        if (logger.isDebugEnabled()) {
            final Collection<GenericException> errorsInSession = SessionHelper
                    .getErrorsInSession(request);
            logger.debug(new StringBuffer(
                    "handleBusinessErrors> Validation errors stored in session [").append(
                    errorsInSession).append("].").toString());
        }
    }

    /**
     * Prepares for the confirm page with any necessary modifications.
     * 
     * @param planData The plan data to prep.
     */
    protected void prepareLookupData(final Map<String, Object> lookupData, final PlanData planData) {

        // Check if mandatory distribution should be added/removed from list
        final List<WithdrawalReason> withdrawalReasons = (List<WithdrawalReason>) lookupData
                .get(CodeLookupCache.PLAN_WITHDRAWAL_REASONS);
        
        //Removing the 'IO' Withdrawal Reason
        for (int i = 0; i < withdrawalReasons.size(); i++) {							//CL 106624
        	WithdrawalReason withdrawalReason = withdrawalReasons.get(i);				//CL 106624
        	if("IO".equalsIgnoreCase(withdrawalReason.getWithdrawalReasonCode())){		//CL 106624
        		withdrawalReasons.remove(withdrawalReason);								//CL 106624
        	}																			//CL 106624
		}																				//CL 106624
        
        if (BooleanUtils.isTrue(planData.getAllowMandatoryDistributions())) {
            if (!CollectionUtils.exists(withdrawalReasons,
                    new WithdrawalReasonCodeEqualityPredicate(
                            WithdrawalReason.MANDATORY_DISTRIBUTION_TERM))) {
                withdrawalReasons.add(planData.getMandatoryDistribution());
                Collections.sort(withdrawalReasons);
            }
        } else {
            withdrawalReasons.remove(planData.getMandatoryDistribution());
        }
        
        //Adding Withdrawal Reason QBAD
        if (BooleanUtils.isTrue(planData.getAllowQualifiedBirthOrAdoptionDistribution())) {
            planData.setQbadProvision(new WithdrawalReason(
                    WithdrawalReason.QUALIFIED_BIRTH_OR_ADOPTION_DISTRIBUTION_TERM,
                    QUALIFIED_BIRTH_OR_ADOPTION_TEXT));
            if (!CollectionUtils.exists(withdrawalReasons,
                    new WithdrawalReasonCodeEqualityPredicate(
                            WithdrawalReason.QUALIFIED_BIRTH_OR_ADOPTION_DISTRIBUTION_TERM))) {
                withdrawalReasons.add(planData.getQbadProvision());
                Collections.sort(withdrawalReasons);
            }
        }
        
    }

    /**
     * Saves the specified plan data object.
     * 
     * @param planData The plan data to save.
     * @param request The user's request.
     */
    protected void savePlanData(final PlanData planData, final HttpServletRequest request)
            throws SystemException {

        final UserProfile userProfile = SessionHelper.getUserProfile(request);
        final Principal principal = userProfile.getPrincipal();
        planData.setUserId(principal.getProfileId());
        planData.setUserIdType((userProfile.isInternalUser()) ? UserIdType.UP_INTERNAL
                : UserIdType.UP_EXTERNAL);
        planData.setUserName(principal.getUserName());
        planData.setPrincipalFirstName(principal.getFirstName());
        planData.setPrincipalLastName(principal.getLastName());

		ContractAssemblyServiceDelegate.getInstance(
				GlobalConstants.PSW_APPLICATION_ID)
				.savePlanDataAndTriggerECRequest(planData);
    }

    /**
     * Attempts to obtain a lock for the specified plan.
     * 
     * @param planData The plan that is being locked.
     * @param request The user's request.
     * @return boolean - True if the lock was successfully obtained, false otherwise.
     */
    protected boolean obtainLock(final PlanData planData, final HttpServletRequest request)
            throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("obtainLock> Obtaining lock for plan [").append(
                    planData.getPlanId()).append("].").toString());
        }
        return LockServiceDelegate.getInstance().lock(LockHelper.PLAN_LOCK_NAME,
                LockHelper.PLAN_LOCK_NAME + planData.getPlanId(),
                getUserProfile(request).getPrincipal().getProfileId());
    }

    /**
     * Attempts to release any lock held for the specified plan.
     * 
     * @param planData The plan that is being locked.
     * @param request The user's request.
     */
    protected void releaseLock(final PlanData planData, final HttpServletRequest request)
            throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("releaseLock> Releasing lock for plan [").append(
                    planData.getPlanId()).append("].").toString());
        }
        LockServiceDelegate.getInstance().releaseLock(LockHelper.PLAN_LOCK_NAME,
                LockHelper.PLAN_LOCK_NAME + planData.getPlanId());
    }

    /**
     * Attempts to refresh any lock held for the specified plan.
     * 
     * @param planData The plan that is being locked.
     * @param request The user's request.
     * @return boolean - True if the lock was successfully refreshed, false otherwise.
     */
    protected boolean refreshLock(final PlanData planData, final HttpServletRequest request)
            throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("refreshLock> Refreshing lock for plan [").append(
                    planData.getPlanId()).append("].").toString());
        }
        return LockServiceDelegate.getInstance().lock(LockHelper.PLAN_LOCK_NAME,
                LockHelper.PLAN_LOCK_NAME + planData.getPlanId(),
                getUserProfile(request).getPrincipal().getProfileId());
    }

    /**
     * Handles the lock obtainment failure by generating an appropriate error message.
     * 
     * @param planData The plan that was being locked.
     * @param request The user's request.
     */
    protected void handleObtainLockFailure(final PlanData planData, final HttpServletRequest request)
            throws SystemException {

        UserInfo lockOwnerUserInfo = null;
        try {
            lockOwnerUserInfo = SecurityServiceDelegate.getInstance().searchByProfileId(
                    getUserProfile(request).getPrincipal(),
                    LockServiceDelegate.getInstance().getLockInfo(LockHelper.PLAN_LOCK_NAME,
                            LockHelper.PLAN_LOCK_NAME + planData.getPlanId())
                            .getLockUserProfileId());
        } catch (final Exception exception) {
            logger.warn(exception);
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
     * Sets the plan related permissions (edit and history) for the current user.
     * 
     * @param form The user's plan data action form.
     * @param request The user's request.
     */
    protected void setPermissions(final PlanDataForm form, final HttpServletRequest request) {

        final UserProfile userProfile = getUserProfile(request);
        form.setUserCanEdit(userProfile.getPrincipal().getRole().hasPermission(
                PermissionType.EDIT_PLAN_DATA));
        form.setUserCanViewHistory(userProfile.isInternalUser() && !userProfile.isBundledGAApprover());
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("setPermissions> Set user edit permission to [").append(
                    form.getUserCanEdit()).append("] and view history permission to [").append(
                    form.getUserCanViewHistory()).append("].").toString());
        }
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
	/** (non-Javadoc)
	 *  This code has been changed and added to validate form and request against penetration attack, prior to other validations as part of the CL#137697.
	 *  
	 * @see com.manulife.pension.platform.web.controller.BaseAction#doValidate(org.apache.struts.action.ActionMapping, org.apache.struts.action.Form, javax.servlet.http.HttpServletRequest)
	*/
	/*@SuppressWarnings("rawtypes")
	protected Collection doValidate(ActionMapping mapping, Form form,
			HttpServletRequest request) {
		Collection penErrors = PsValidation.doValidatePenTestAutoAction(form,mapping, request, "default");
		if (penErrors != null && penErrors.size() > 0) {
			request.removeAttribute(PsBaseAction.ERROR_KEY);
			return penErrors;
		}
		return super.doValidate(mapping, form, request);
	}*/
	
}
