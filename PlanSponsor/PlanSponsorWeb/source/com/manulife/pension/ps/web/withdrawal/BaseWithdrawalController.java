package com.manulife.pension.ps.web.withdrawal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.manulife.pension.cache.CodeLookupCache;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.content.view.MutableDisclaimer;
import com.manulife.pension.content.view.MutablePageFootnote;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.content.CommonContentConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.util.XStreamCustomConverters;
import com.manulife.pension.ps.service.submission.valueobject.Lock;
import com.manulife.pension.ps.service.submission.valueobject.Lockable;
import com.manulife.pension.ps.service.withdrawal.valueobject.LoanAndWithdrawalLockableStub;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.SubmissionServiceDelegate;
import com.manulife.pension.ps.web.pagelayout.LayoutBean;
import com.manulife.pension.ps.web.pagelayout.LayoutBeanRepository;
import com.manulife.pension.ps.web.tools.util.LockManager;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.ps.web.withdrawal.util.WithdrawalBookmarkHelper;
import com.manulife.pension.service.distribution.valueobject.Declaration;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;
import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestDeclaration;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMetaData;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.ServiceLogRecord;
import com.manulife.pension.validator.ValidationError;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * BaseWithdrawalAction provides a base class for the withdrawal actions.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1.2.5 2006/08/29 17:10:05
 */
public abstract class BaseWithdrawalController extends PsAutoController {

	private static final String XSLT_FILE_KEY_NAME = "OnlineWithdrawals.XSLFile";
	private static final String INCLUDED_XSL_PATH_KEY_NAME = "OnlineWithdrawals.IncludedXSLPath";
	
    /**
     * Default constructor.
     */
    protected BaseWithdrawalController() {
        super(BaseWithdrawalController.class);
    }

    /**
     * Constructs a base withdrawal class using the specified logging class.
     * 
     * @param clazz The class used to configure the logger.
     */
    protected BaseWithdrawalController(final Class clazz) {
        super(clazz);
    }

    /**
     * WITHDRAWAL_VO_SESSION_KEY.
     */
    protected static final String WITHDRAWAL_VO_SESSION_KEY = "withdrawalVoSessionKey";

    /**
     * The action forward name for where to go if the user is missing access permission.
     */
    public static final String ACTION_FORWARD_NO_PERMISSION = "noPermission";

    /**
     * ACTION_FORWARD_BACK.
     */
    public static final String ACTION_FORWARD_BACK = "back";

    /**
     * ACTION_FORWARD_CANCEL.
     */
    public static final String ACTION_FORWARD_CANCEL = "cancel";

    /**
     * ACTION_FORWARD_CONTINUE.
     */
    public static final String ACTION_FORWARD_CONTINUE = "continue";

    /**
     * ACTION_FORWARD_DEFAULT.
     */
    public static final String ACTION_FORWARD_DEFAULT = "default";

    /**
     * ACTION_FORWARD_DEFAULT.
     */
    public static final String ACTION_FORWARD_DEFAULT_FOR_PRINT_FRIENDLY = "defaultForPrintFriendly";

    /**
     * ACTION_FORWARD_FINISHED.
     */
    public static final String ACTION_FORWARD_FINISHED = "finished";

    /**
     * ACTION_FORWARD_NEXT.
     */
    public static final String ACTION_FORWARD_NEXT = "next";

    /**
     * ACTION_FORWARD_SAVE_AND_EXIT.
     */
    public static final String ACTION_FORWARD_SAVE_AND_EXIT = "saveAndExit";

    /**
     * ACTION_FORWARD_CANCEL_AND_EXIT.
     */
    public static final String ACTION_FORWARD_CANCEL_AND_EXIT = "cancelAndExit";

    /**
     * ACTION_FORWARD_SELECT.
     */
    public static final String ACTION_FORWARD_SELECT = "select";

    /**
     * ACTION_FORWARD_SEND_FOR_APPROVAL.
     */
    public static final String ACTION_FORWARD_SEND_FOR_APPROVAL = "sendForApproval";

    /**
     * ACTION_FORWARD_SEND_FOR_REVIEW.
     */
    public static final String ACTION_FORWARD_SEND_FOR_REVIEW = "sendForReview";

    /**
     * ACTION_FORWARD_APPROVE.
     */
    public static final String ACTION_FORWARD_APPROVE = "approve";

    /**
     * ACTION_FORWARD_RECALCULATE.
     */
    public static final String ACTION_FORWARD_RECALCULATE = "recalculate";

    /**
     * ACTION_FORWARD_VIEW_ITEM.
     */
    public static final String ACTION_FORWARD_VIEW_ITEM = "viewItem";

    /**
     * ACTION_FORWARD_ERROR.
     */
    public static final String ACTION_FORWARD_ERROR = "error";

    public static final String ACTION_FORWARD_LOCK_ERROR = "lockError";

    /**
     * ACTION_FORWARD_DELETE.
     */
    public static final String ACTION_FORWARD_DELETE = "delete";

    /**
     * ACTION_FORWARD_DENY.
     */
    public static final String ACTION_FORWARD_DENY = "deny";

    public static final String ACTION_FORWARD_SEARCH = "search";

    public static final String ACTION_PARAMETER_INIT = "init";

    public static final String ACTION_PARAMETER_EDIT = "edit";

    public static final String ACTION_PARAMETER_DEFUALT = "default";

    public static final String ACTION_PARAMETER_REVIEW = "review";

    public static final String ACTION_FORWARD_LEGALESE = "legalese";

    public static final String ACTION_FORWARD_BACK_TO_PARTICIPANT_ACCOUNT = "toParticipantAccount";

    public static final String ACTION_FORWARD_BACK_TO_LOAN_AND_WITHDRAWAL = "toLoanAndWithdrawal";

    public static final String ACTION_FORWARD_BACK_TO_SEARCH_SUMMARY = "toSearchSummary";

    public static final String PERMISSIONS_KEY = "withdrawalPermissionsKey";

    public static final String TASK_KEY = "task";

    public static final String PRINT_FRIENDLY = "printFriendly";
    
    public static final String ACTION_CALCULATE = "calculate"; 

    /**
     * This method puts the {@link WithdrawalRequest} into the session so that it can be passed to
     * the next action.
     * 
     * @param withdrawalRequest The request to store.
     * @param httpServletRequest The http servlet request.
     */
    public void storeWithdrawalRequest(final WithdrawalRequest withdrawalRequest,
            final HttpServletRequest httpServletRequest) {

        httpServletRequest.getSession().setAttribute(WITHDRAWAL_VO_SESSION_KEY, withdrawalRequest);
    }

    /**
     * This method gets the {@link WithdrawalRequest} from the session so that it can be retrieved
     * from the previous action. It also removes it from the session.
     * 
     * @param httpServletRequest The http servlet request.
     * @return WithdrawalRequest The request that was set in the http request, or null if none is
     *         found.
     */
    public WithdrawalRequest fetchAndClearWithdrawalRequest(
            final HttpServletRequest httpServletRequest) {

        final HttpSession httpSession = httpServletRequest.getSession();

        final WithdrawalRequest withdrawalRequest = (WithdrawalRequest) httpSession
                .getAttribute(WITHDRAWAL_VO_SESSION_KEY);

        // Remove it from the session.
        httpSession.removeAttribute(WITHDRAWAL_VO_SESSION_KEY);

        return withdrawalRequest;
    }

    /**
     * This method provides access to the {@link WithdrawalServiceDelegate}.
     * 
     * @return WithdrawalServiceDelegate The withdrawal service delegate.
     */
    protected WithdrawalServiceDelegate getWithdrawalServiceDelegate() {
        return WithdrawalServiceDelegate.getInstance();
    }

    /**
     * This method returns true if the user is initiating a withdrawal request.
     * 
     * @param mapping The action mapping containing the comparison data
     * @return tru if the user is initiating a withdrawal request
     */
    protected boolean isInit(final String mapping) {
        //return mapping.getParameter().equals(BaseWithdrawalAction.ACTION_PARAMETER_INIT);
    	return mapping.equals(BaseWithdrawalController.ACTION_PARAMETER_INIT);
    }

    /**
     * This method returns true if the user is editing a withdrawal request in draft state.
     * 
     * @param mapping The action mapping containing the comparison data
     * @return true if the user is editing a withdrawal request in draft state
     */
    protected boolean isEdit(final String mapping) {
        //return mapping.getParameter().equals(BaseWithdrawalAction.ACTION_PARAMETER_EDIT);
    	return mapping.equals(BaseWithdrawalController.ACTION_PARAMETER_EDIT);
    }

    /**
     * This method returns true if the default action is to be take.
     * 
     * @param mapping The action mapping containing the comparison data
     * @return true if the user is editing a withdrawal request in draft state
     */
    protected boolean isDefault(final String mapping) {
        //return mapping.getParameter().equals(BaseWithdrawalAction.ACTION_PARAMETER_DEFUALT);
    	return mapping.equals(BaseWithdrawalController.ACTION_PARAMETER_DEFUALT);
    }

    /**
     * This method returns true if the user is reviewing a withdrawal request.
     * 
     * @param mapping The action mapping containing the comparison data
     * @return true if the user is reviewing a withdrawal request
     */
    protected boolean isReview(final String mapping) {
        //return mapping.getParameter().equals(BaseWithdrawalAction.ACTION_PARAMETER_REVIEW);
    	return mapping.equals(BaseWithdrawalController.ACTION_PARAMETER_REVIEW);
    }

    /**
     * This function puts a WithdrawalPermissions value object in request scope so that the jsp
     * pages use to determine visibility of buttons and etc..
     * 
     * @param request The Http Servelet request
     * @param mapping The action mapping
     * @param withdrawalRequest The current withdrawal request
     */
    protected void setupPermissions(final HttpServletRequest request, ModelMap model,
            final WithdrawalRequest withdrawalRequest) {
        WithdrawalPermissions vo = new WithdrawalPermissions();
String mapping=(String) model.get("mapping");
        if (isInit(mapping)) {
            vo.setDelete(false);
        } else {
            boolean canUserDelete = (withdrawalRequest != null)
                    && (withdrawalRequest.getCreatedById() != null && withdrawalRequest
                            .getCreatedById().longValue() == getUserProfile(request).getPrincipal()
                            .getProfileId())
                    && (StringUtils.equals(withdrawalRequest.getStatusCode(),
                            WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_APPROVAL_CODE) || StringUtils
                            .equals(withdrawalRequest.getStatusCode(),
                                    WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_REVIEW_CODE));

            vo.setDelete(canUserDelete);
        }

        request.setAttribute(PERMISSIONS_KEY, vo);
    }

    /**
     * doDelete is called when the page 'delete' button is pressed.
     * 
     * @param mapping The action mapping.
     * @param actionForm The action form.
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @return ActionForward The forward to process.
     * @throws SystemException When an generic application problem occurs.
     */
    public String doDelete( AutoForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response)
            throws SystemException {

        final WithdrawalForm withdrawalForm = (WithdrawalForm) actionForm;

        // check permissions and contract statuses
        int errorCode = checkPermissionsAndStatuses(request, withdrawalForm);
        if (errorCode != 0) {
            return handlePermissionsOrStatusChanged( request, errorCode);
        }

        final Integer userProfileId = new Integer((int) getUserProfile(request).getPrincipal()
                .getProfileId());

        final WithdrawalRequest withdrawalRequest = withdrawalForm.getWithdrawalRequestUi()
                .getWithdrawalRequest();

        // If the request hasn't even been created yet, we just return successfully.
        if (withdrawalRequest.getSubmissionId() == null) {
        	return ACTION_FORWARD_DELETE;
        } // fi

        final boolean theUserIsTheCreator = withdrawalRequest.getCreatedById()
                .equals(userProfileId);

        if (theUserIsTheCreator) {
            // WVI-41: Lock the WD request if possible
            final Lockable withdrawalStub = SubmissionServiceDelegate.getInstance()
                    .getLoanAndWithdrawalLockable(withdrawalRequest.getSubmissionId(),
                            withdrawalRequest.getContractId(), userProfileId);
            final boolean locked = LockManager.getInstance(request.getSession()).lock(
                    withdrawalStub, userProfileId.toString());

            if (!locked) {
                // cannot obtain a lock, generate an error and redisplay the page
                final Collection lockError = new ArrayList(1);
                lockError.add(new ValidationError("LOCKED", ErrorCodes.WITHDRAWAL_LOCKED));
                setErrorsInSession(request, lockError);
                return findForward( ACTION_FORWARD_LOCK_ERROR);
            }

            // Set the user that deleted the request.
            final UserProfile userProfile = getUserProfile(request);
            final Integer currentUserId = new Integer(String.valueOf(userProfile.getPrincipal()
                    .getProfileId()));
            withdrawalRequest.setLastUpdatedById(currentUserId);

            // WVI-41: delete request (change status and save)
            WithdrawalServiceDelegate.getInstance().delete(withdrawalRequest);

            // Check if we had validation errors
            if (!CollectionUtils.isEmpty(withdrawalRequest.getErrorCodes())) {

                return handleBusinessErrors( request, withdrawalRequest,
                        withdrawalForm.getWithdrawalRequestUi());
            }

            // WVI-41: unlocks
            releaseLock(request, withdrawalForm);

            // WVI-41: back to list page
            return ACTION_FORWARD_DELETE;
        } else {
            // User has tried to delete and is not the creator.
            logger.debug("Attempt to delete, and user is not the creator.");
            return WithdrawalBookmarkHelper.ACTION_FORWARD_BOOKMARK_DETECTED;
        } // fi
    }

    /**
     * Try to release any previously set locks (when coming from the list page -- edit).
     * 
     * @param request The {@link HttpServletRequest}.
     * @param withdrawalForm The {@link WithdrawalForm}.
     * @throws SystemException If an exception occurs.
     */
    protected void releaseLock(final HttpServletRequest request,
            final WithdrawalForm withdrawalForm) throws SystemException {

        // release any locks on this submission
        WithdrawalForm form = (WithdrawalForm) withdrawalForm;
        WithdrawalRequestUi wui = form.getWithdrawalRequestUi();
        WithdrawalRequest withdrawalRequest = wui.getWithdrawalRequest();
        if (withdrawalRequest.getSubmissionId() == null) {
            // This request hasn't been saved yet.
            return;
        } // fi

        Integer contractId = Integer.valueOf(withdrawalRequest.getContractId());
        Integer submissionId = Integer.valueOf(withdrawalRequest.getSubmissionId());

        Lockable withdrawalStub = SubmissionServiceDelegate.getInstance()
                .getLoanAndWithdrawalLockable(submissionId, contractId, null);
        Lock lock = SubmissionServiceDelegate.getInstance().checkLock(withdrawalStub, true);
        if (lock != null) {
            withdrawalStub.setLock(lock);
            boolean unlocked = LockManager.getInstance(request.getSession(false)).release(
                    withdrawalStub);
            if (!unlocked) {
                logger.debug("BaseWithdrawalAction -- problem unlocking");
            }
        } else {
            logger.debug("BaseWithdrawalAction -- lock is null!");
        }
    }

    /**
     * Try to refresh any previously set locks.
     * 
     * @param request The {@link HttpServletRequest}.
     * @param beforeProceedingForm The {@link BeforeProceedingForm}.
     * @throws SystemException If an exception occurs.
     */
    protected void refreshLock(final HttpServletRequest request,
            final BeforeProceedingForm beforeProceedingForm) throws SystemException {

        // Retrieve meta information
        final WithdrawalRequestMetaData metaData = (WithdrawalRequestMetaData) request.getSession()
                .getAttribute(WebConstants.WITHDRAWAL_REQUEST_METADATA_ATTRIBUTE);

        if ((metaData != null) && (metaData.getSubmissionId() != null)) {
            refreshLock(request, metaData.getContractId(), metaData.getSubmissionId(), metaData
                    .getProfileId().toString());
        } // fi
    }

    /**
     * Try to refresh any previously set locks.
     * 
     * @param request The {@link HttpServletRequest}.
     * @param withdrawalForm The {@link WithdrawalForm}.
     * @throws SystemException If an exception occurs.
     */
    protected void refreshLock(final HttpServletRequest request,
            final WithdrawalForm withdrawalForm) throws SystemException {
        refreshLockIfAvailable(request, withdrawalForm);
    }

    /**
     * Try to refresh any previously set locks. The method will quietly return if the lock didn't
     * exist, or the request is not longer available.
     * 
     * @param request The {@link HttpServletRequest}.
     * @param withdrawalForm The {@link WithdrawalForm}.
     * @return boolean - True if the lock was obtained successfully, false otherwise.
     * @throws SystemException If an exception occurs.
     */
    protected boolean refreshLockIfAvailable(final HttpServletRequest request,
            final WithdrawalForm withdrawalForm) throws SystemException {

        // refresh any locks on this submission
        final WithdrawalRequestUi withdrawalRequestUi = withdrawalForm
                .getWithdrawalRequestUi();
        final WithdrawalRequest withdrawalRequest = withdrawalRequestUi.getWithdrawalRequest();
        if (withdrawalRequest.getIsParticipantCreated()) {
            convertPersistentDeclarationsToArray(withdrawalRequestUi);
            withdrawalForm.setWithdrawalRequestUi(withdrawalRequestUi);
        }
        if (withdrawalRequest.getSubmissionId() == null) {
            // This request hasn't been saved yet.
            return false;
        } // fi
        // Check if request has been marked no longer valid (deleted, expired etc.).
        if (withdrawalRequest.getIsNoLongerValid()) {
            return false;
        }

        final Integer contractId = Integer.valueOf(withdrawalRequest.getContractId());
        final Integer submissionId = Integer.valueOf(withdrawalRequest.getSubmissionId());
        final String userid = new Integer((int) withdrawalRequest.getPrincipal().getProfileId())
                .toString();

        refreshLock(request, contractId, submissionId, userid);

        return true;
    }

    /**
     * Common method to refresh the lock.
     * 
     * @param request The {@link HttpServletRequest}.
     * @param contractId The contract ID.
     * @param submissionId The submission ID.
     * @param userid The user ID of the current user.
     * @throws SystemException If an exception occurs.
     */
    private void refreshLock(final HttpServletRequest request, final Integer contractId,
            final Integer submissionId, final String userid) throws SystemException {
        final Lockable withdrawalStub = SubmissionServiceDelegate.getInstance()
                .getLoanAndWithdrawalLockable(submissionId, contractId, null);
        final Lock lock = SubmissionServiceDelegate.getInstance().checkLock(withdrawalStub, true);
        if (lock != null) {
            withdrawalStub.setLock(lock);
            final boolean refreshed = LockManager.getInstance(request.getSession(false)).refresh(
                    withdrawalStub, userid);
            if (!refreshed) {
                logger.debug("BaseWithdrawalAction -- problem refreshing lock");
                throw new SystemException("BaseWithdrawalAction -- problem refreshing lock");
            }
        } else {
            logger.debug("BaseWithdrawalAction -- lock is null!");
            throw new SystemException("BaseWithdrawalAction -- lock is null!");
        }
    }

    /**
     * For every request that comes in, we need to check the user permissions, request and contract
     * statuses, see if we can go ahead with the processing.
     * 
     * @param request The {@link HttpServletRequest}.
     * @param withdrawalForm The {@link WithdrawalForm}.
     * @throws SystemException If an exception occurs.
     * @return int - The error code.
     */
    protected int checkPermissionsAndStatuses(final HttpServletRequest request,
            final WithdrawalForm withdrawalForm) throws SystemException {

        int errorCode = 0;

        WithdrawalRequest withdrawalRequest = withdrawalForm.getWithdrawalRequestUi()
                .getWithdrawalRequest();
        // validate user permissions
        SecurityServiceDelegate securityServiceDelegate = SecurityServiceDelegate.getInstance();
        // FIXME --add security check once available
        // securityServiceDelegate.get..

        // validate contract status
        ContractInfo ci = withdrawalRequest.getContractInfo();
        if (!(BooleanUtils.isTrue(ci.getCsfAllowOnlineWithdrawals()))) {
            return ErrorCodes.CONTRACT_ONLINE_WITHDRAWALS;
        }
        return errorCode;
    }

    /**
     * This creates a new {@link GenericException} from the provided error code, sets it to the
     * session, and forwards to the 'default' page to display the error.
     * 
     * @param request The {@link HttpServletRequest}.
     * @param errorCode The error code to report.
     * @return String - The page to forward to.
     * @throws SystemException If an exception occurs.
     */
    protected String handlePermissionsOrStatusChanged(
            final HttpServletRequest request, final int errorCode) throws SystemException {

        Collection<GenericException> errors = new ArrayList<GenericException>();

        errors.add(new GenericException(errorCode));
        SessionHelper.setErrorsInSession(request, errors);
        return  ACTION_FORWARD_DEFAULT;
    }

    /**
     * Handles errors in the business tier. Extracts the errors, converts them to UI tier errors,
     * and makes them available in the UI.
     * 
     * @param mapping The mapping.
     * @param request The request.
     * @param withdrawalRequest The Withdrawal Request to handle.
     * @param requestUi The withdrawal request UI object to update, if we need to force a data
     *            change.
     * @return The ActionForward to go to.
     */
    protected String handleBusinessErrors(
            final HttpServletRequest request, final WithdrawalRequest withdrawalRequest,
            final WithdrawalRequestUi requestUi) {
        final WithdrawalRequestUi resultUi = new WithdrawalRequestUi(withdrawalRequest);
        final Collection<ValidationError> businessMessages = resultUi
                .getValidationMessages(new GraphLocation("withdrawalRequestUi"));

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

        // Check if we have updated data from validations
        if (BooleanUtils.isTrue(withdrawalRequest.getForceData())) {

            requestUi.updateForcedData();

            // Reset flag
            withdrawalRequest.setForceData(false);
        }
        // Convert Collections of Declaration to Array for Ui object
        if (withdrawalRequest.getIsParticipantCreated()) {
            convertPersistentDeclarationsToArray(requestUi);
        }

        final String forward = ACTION_FORWARD_ERROR;
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("handleBusinessErrors> Forwarding to error [").append(
                    forward).append("].").toString());
        }
        return forward;
    }
    
    /**
     * Handles Termination date error in the business tier. Extracts the errors, converts them to UI tier errors,
     * and makes them available in the UI.
     * 
     * @param mapping The mapping.
     * @param request The request.
     * @param withdrawalRequest The Withdrawal Request to handle.
     * @param requestUi The withdrawal request UI object to update, if we need to force a data
     *            change.
     * @return The ActionForward to go to.
     */
    protected String handleTerminationDateErrors(
            final HttpServletRequest request, final WithdrawalRequest withdrawalRequest,
            final WithdrawalRequestUi requestUi) {
        final WithdrawalRequestUi resultUi = new WithdrawalRequestUi(withdrawalRequest);
        final Collection<ValidationError> businessMessages = resultUi
                .getValidationMessages(new GraphLocation("withdrawalRequestUi"));

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

        // Check if we have updated data from validations
        if (BooleanUtils.isTrue(withdrawalRequest.getForceData())) {
            requestUi.updateForcedData();
            // Reset flag
            withdrawalRequest.setForceData(false);
        }
        // Convert Collections of Declaration to Array for Ui object
        if (withdrawalRequest.getIsParticipantCreated()) {
            convertPersistentDeclarationsToArray(requestUi);
        }
        final String forward = ACTION_FORWARD_BACK;
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("handleBusinessErrors> Forwarding to error [").append(
                    forward).append("].").toString());
        }
        return forward;
    }

    /**
     * Handles initial messages in the business tier. Extracts the messages, converts them to UI
     * tier messages, and makes them available in the UI.
     * 
     * @param mapping The mapping.
     * @param request The request.
     * @param withdrawalRequest The Withdrawal Request to handle.
     */
    protected void handleInitialMessages(
            final HttpServletRequest request, final WithdrawalRequest withdrawalRequest) {
        final WithdrawalRequestUi resultUi = new WithdrawalRequestUi(withdrawalRequest);
        final Collection<ValidationError> businessMessages = resultUi
                .getValidationMessages(new GraphLocation("withdrawalRequestUi"));

        if (logger.isDebugEnabled()) {
            logger
                    .debug(new StringBuffer(
                            "handleInitialMessages> Initial messages from biz tier [").append(
                            businessMessages).append("].").toString());
        }
        setErrorsInSession(request, businessMessages);

        if (logger.isDebugEnabled()) {
            final Collection<GenericException> messagesInSession = SessionHelper
                    .getErrorsInSession(request);
            logger.debug(new StringBuffer(
                    "handleInitialMessages> Initial messages stored in session [").append(
                    messagesInSession).append("].").toString());
        }
    }

    /**
     * Returns the lookup keys for Step One.
     * 
     * @return Collection - A {@link Collection} of {@link String} objects.
     */
    protected Collection<String> getStepOneLookupKeys() {
        Collection<String> lookupKeys = new ArrayList<String>();
        lookupKeys.add(CodeLookupCache.LOAN_OPTION_TYPE);
        // lookupKeys.add(CodeLookupCache.PAYMENT_TO_TYPE);
        lookupKeys.add(CodeLookupCache.HARDSHIP_REASONS);
        lookupKeys.add(CodeLookupCache.IRS_DISTRIBUTION_FOR_LOANS);
        lookupKeys.add(CodeLookupCache.USA_STATE_WITHOUT_MILITARY_TYPE);
        return lookupKeys;
    }

    /**
     * Returns the lookup keys for Step Two.
     * 
     * @return Collection - A {@link Collection} of {@link String} objects.
     */
    protected Collection<String> getStepTwoLookupKeys() {
        final Collection<String> lookupDataKeys = new ArrayList<String>();
        lookupDataKeys.add(CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE);
        lookupDataKeys.add(CodeLookupCache.COUNTRY_COLLECTION_TYPE);
        lookupDataKeys.add(CodeLookupCache.USA_STATE_WITH_MILITARY_TYPE);
        lookupDataKeys.add(CodeLookupCache.TPA_TRANSACTION_FEE_TYPE);
        lookupDataKeys.add(CodeLookupCache.OPTIONS_FOR_UNVESTED_AMOUNTS);

        lookupDataKeys.add(CodeLookupCache.IRS_DISTRIBUTION_FOR_WITHDRAWALS);
        lookupDataKeys.add(CodeLookupCache.IRS_DISTRIBUTION_FOR_LOANS);
        lookupDataKeys.add(CodeLookupCache.PAYMENT_TO_TYPE);
        lookupDataKeys.add(CodeLookupCache.ONLINE_WITHDRAWAL_REASONS); // for Floating Summary

        return lookupDataKeys;
    }

    /**
     * Returns the lookup keys for Review.
     * 
     * @return Collection - A {@link Collection} of {@link String} objects.
     */
    protected Collection<String> getReviewLookupKeys() {
        final Collection<String> lookupDataKeys = new ArrayList<String>();
        lookupDataKeys.add(CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE);
        lookupDataKeys.add(CodeLookupCache.COUNTRY_COLLECTION_TYPE);
        lookupDataKeys.add(CodeLookupCache.USA_STATE_WITH_MILITARY_TYPE);
        lookupDataKeys.add(CodeLookupCache.TPA_TRANSACTION_FEE_TYPE);
        lookupDataKeys.add(CodeLookupCache.OPTIONS_FOR_UNVESTED_AMOUNTS);
        lookupDataKeys.add(CodeLookupCache.IRS_DISTRIBUTION_FOR_WITHDRAWALS);
        lookupDataKeys.add(CodeLookupCache.PAYMENT_TO_TYPE);
        lookupDataKeys.add(CodeLookupCache.ONLINE_WITHDRAWAL_REASONS);
        lookupDataKeys.add(CodeLookupCache.LOAN_OPTION_TYPE);
        lookupDataKeys.add(CodeLookupCache.PAYMENT_TO_TYPE);
        lookupDataKeys.add(CodeLookupCache.HARDSHIP_REASONS);
        lookupDataKeys.add(CodeLookupCache.IRS_DISTRIBUTION_FOR_LOANS);
        lookupDataKeys.add(CodeLookupCache.USA_STATE_WITHOUT_MILITARY_TYPE);
        return lookupDataKeys;
    }

    /**
     * Returns the all lookup keys used in withdrawals.
     * 
     * @return Collection - A {@link Collection} of {@link String} objects.
     */
    protected Collection<String> getAllLookupKeys() {
        Collection<String> lookupKeys = new ArrayList<String>();
        // Step One
        lookupKeys.add(CodeLookupCache.ONLINE_WITHDRAWAL_REASONS);
        lookupKeys.add(CodeLookupCache.LOAN_OPTION_TYPE);
        lookupKeys.add(CodeLookupCache.PAYMENT_TO_TYPE);
        lookupKeys.add(CodeLookupCache.HARDSHIP_REASONS);
        lookupKeys.add(CodeLookupCache.IRS_DISTRIBUTION_FOR_LOANS);
        lookupKeys.add(CodeLookupCache.USA_STATE_WITHOUT_MILITARY_TYPE);

        // Step Two
        lookupKeys.add(CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE);
        lookupKeys.add(CodeLookupCache.COUNTRY_COLLECTION_TYPE);
        lookupKeys.add(CodeLookupCache.USA_STATE_WITH_MILITARY_TYPE_BY_DESC);
        lookupKeys.add(CodeLookupCache.TPA_TRANSACTION_FEE_TYPE);
        lookupKeys.add(CodeLookupCache.OPTIONS_FOR_UNVESTED_AMOUNTS);
        lookupKeys.add(CodeLookupCache.IRS_DISTRIBUTION_FOR_WITHDRAWALS);
        lookupKeys.add(CodeLookupCache.PAYMENT_TO_TYPE);

        // added for the back button
        lookupKeys.add(CodeLookupCache.USA_STATE_WITH_MILITARY_TYPE);
        lookupKeys.add(CodeLookupCache.WITHDRAWAL_REQUEST_STATUS_ORDERED);
        return lookupKeys;
    }

    /**
     * Sets a session attribute with the task, if it's not already set.
     * 
     * @param request The {@link HttpServletRequest} to use to get the session attribuite.
     */
    protected void checkAndEnableSearchSummaryBookmarkTracking(final HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String bookmark = (String) session.getAttribute(TASK_KEY);
        if (bookmark == null) {
            session.setAttribute(TASK_KEY, "participantSearch");
        }
    }

    /**
     * Determines if the print friendly has been clicked.
     * 
     * @param request The {@link HttpServletRequest}.
     * @return boolean - True if the print friendly parameter is true, false otherwise.
     */
    protected boolean isPrinterFriendlyClicked(final HttpServletRequest request) {
        final String printFriendlyValue = request.getParameter(PRINT_FRIENDLY);
        return com.manulife.pension.util.StringUtils.isStringTrue(printFriendlyValue);
    }

    /**
     * Attempts to create a lock for the given request. If the request has not yet been persisted,
     * the method simply returns <code>true</code>. If creating the lock is successful,
     * <code>true</code> is returned, and <code>false</code> is returned otherwise.
     * 
     * @param request The {@link HttpServletRequest}.
     * @param withdrawalRequestMetaData The data to use to create the lock.
     * @return boolean - True if the lock is created (or not needed (new request)), false otherwise.
     */
    protected boolean createLock(final HttpServletRequest request,
            final WithdrawalRequestMetaData withdrawalRequestMetaData) {

        if ((withdrawalRequestMetaData == null)
                || (withdrawalRequestMetaData.getSubmissionId() == null)) {
            return true;
        } // fi

        final LoanAndWithdrawalLockableStub loanAndWithdrawalLockableStub = SubmissionServiceDelegate
                .getInstance().getLoanAndWithdrawalLockable(
                        withdrawalRequestMetaData.getSubmissionId(),
                        withdrawalRequestMetaData.getContractId(),
                        withdrawalRequestMetaData.getProfileId());

        final UserProfile userProfile = getUserProfile(request);

        final boolean result = LockManager.getInstance(request.getSession()).lock(
                loanAndWithdrawalLockableStub,
                String.valueOf(userProfile.getPrincipal().getProfileId()));
        return result;
    }

    /**
     * Sets up contract information required for links when user is a TPA.
     */
    protected void setTpaContractInformation(final HttpServletRequest request,
            final WithdrawalRequest withdrawalRequest) {

        final HttpSession session = request.getSession();
        session.setAttribute(Constants.TPA_CONTRACT_ID_KEY, withdrawalRequest.getContractInfo()
                .getContractId());
        session.setAttribute(Constants.TPA_CONTRACT_NAME_KEY, withdrawalRequest
                .getParticipantInfo().getContractName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String preExecute(final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException, SystemException {

        // Preserve the last active page location, as the call to the superclass removes it.
        final String lastActivePageLocation = (String) request.getSession().getAttribute(
                WebConstants.LAST_ACTIVE_PAGE_LOCATION);

        final String normalPreExecuteForward = super.preExecute( form, request,
                response);

        request.getSession().setAttribute(WebConstants.LAST_ACTIVE_PAGE_LOCATION,
                lastActivePageLocation);

        final String bookmarkDetectedForward = WithdrawalBookmarkHelper.preExecute(form, request, response);

        if (bookmarkDetectedForward != null) {
            return bookmarkDetectedForward;
        }

        return normalPreExecuteForward;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String doExecute(final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response)
            throws IOException, ServletException, SystemException {

        final String actionForward = super.doExecute( actionForm, request, response);

        WithdrawalBookmarkHelper.handleForward(actionForward,  actionForm, request,
                response);

        return actionForward;
    }

    /**
     * This method is used to convert the Delarationslist stored in the WithdrawalRequest object to
     * an Array of Strings for display purposes. It updates the WithdrawalRequestUi object with the
     * Array of selected delarations.
     * 
     * @param requestUi
     */
    private void convertPersistentDeclarationsToArray(WithdrawalRequestUi requestUi) {
        Collection<Declaration> coll = requestUi.getWithdrawalRequest().getDeclarations();
        String[] declarationsUi = new String[coll.size()];
        int i = 0;
        for (Iterator iter = coll.iterator(); iter.hasNext();) {
            WithdrawalRequestDeclaration element = (WithdrawalRequestDeclaration) iter.next();
            declarationsUi[i++] = element.getTypeCode();
        }
        requestUi.setSelectedDeclarations(declarationsUi);
    }

    /*
     * (non-Javadoc)
     * @see com.manulife.pension.platform.web.controller.BasePdfAutoAction#doPrintPDF(com.manulife.pension.platform.web.controller.AutoForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public String doPrintPDF( AutoForm actionForm,ModelMap model,
            HttpServletRequest request, HttpServletResponse response) throws SystemException, IOException, ServletException {
    	doDefault( actionForm,model, request, response);
    	return super.doPrintPDF( actionForm, request, response);
    }
    
    /*
     * (non-Javadoc)
     * @see com.manulife.pension.platform.web.controller.BasePdfAutoAction#prepareXMLFromReport(com.manulife.pension.platform.web.controller.AutoForm, com.manulife.pension.service.report.valueobject.ReportData, javax.servlet.http.HttpServletRequest)
     */
    public Object prepareXMLFromReport(AutoForm actionForm, ReportData report, 
			HttpServletRequest request) throws ParserConfigurationException, SystemException, ContentException {
    	
    	XStream stream = new XStream(new DomDriver());
    	stream.setMode(XStream.NO_REFERENCES);
    	XStreamCustomConverters customConverters = XStreamCustomConverters.getInstance();
//    	stream.registerConverter(customConverters.new CustomReflectionConverter(stream.getClassMapper(), 
//    			new JVM().bestReflectionProvider(), (DefaultConverterLookup) stream.getConverterLookup()));
		stream.registerConverter(customConverters.new CollectionConverterX(stream.getClassMapper()));
		stream.registerConverter(customConverters.new MapConverter());
		stream.registerConverter(customConverters.new DateConverterX());
		stream.registerConverter(customConverters.new BigDecimalConverterX());
		stream.alias("withdrawalData", com.manulife.pension.ps.web.withdrawal.WithdrawalForm.class);
		stream.alias("withdrawalRequestDeclarataion", com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestDeclaration.class);
		stream.alias("withdrawalRequestFee", com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestFee.class);
		stream.alias("withdrawalRequestMoneyType", com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType.class);
		stream.alias("withdrawalRequestNote", com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestNote.class);
		stream.alias("withdrawalRequestRecipient", com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient.class);
		stream.alias("withdrawalRequestPayee", com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee.class);
		stream.alias("withdrawalReason", com.manulife.pension.service.contract.valueobject.WithdrawalReason.class);
		stream.alias("withdrawalActivitySummary", com.manulife.pension.service.withdrawal.valueobject.WithdrawalActivitySummary.class);
		stream.alias("withdrawalRequestFeeUi", com.manulife.pension.ps.web.withdrawal.WithdrawalRequestFeeUi.class);
		stream.alias("withdrawalRequestMoneyTypeUi", com.manulife.pension.ps.web.withdrawal.WithdrawalRequestMoneyTypeUi.class);
		stream.alias("withdrawalRequestRecipientUi", com.manulife.pension.ps.web.withdrawal.WithdrawalRequestRecipientUi.class);
		stream.alias("withdrawalRequestPayeeUi", com.manulife.pension.ps.web.withdrawal.WithdrawalRequestPayeeUi.class);
		stream.alias("withdrawalRequestLoan", com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestLoan.class);
		stream.alias("userName", com.manulife.pension.service.withdrawal.valueobject.UserName.class);
		stream.alias("cmaContent", com.manulife.pension.ps.web.withdrawal.WithdrawalCmaContent.class);

		WithdrawalRequestUi withdrawalRequestUi = ((WithdrawalForm)actionForm).getWithdrawalRequestUi();
		Boolean isParticipantGIFLEnabled = (Boolean)request.getAttribute("isParticipantGIFLEnabled");
		withdrawalRequestUi.setParticipantGIFLEnabled(isParticipantGIFLEnabled == null ? false : isParticipantGIFLEnabled.booleanValue());
		withdrawalRequestUi.setPrintParticipant(getPrintParticipant(request));
		withdrawalRequestUi.setValuesForPDF(setCmaContent(request, withdrawalRequestUi));
		
		//View Page Timestamp
		if(withdrawalRequestUi.isViewAction()){
			stream.registerConverter(customConverters.new ViewPageTimestampConverterX());
		}
		//Confirmation Page Timestamp
		if(withdrawalRequestUi.isConfirmAction()){
			stream.registerConverter(customConverters.new TimestampConverterX());
		}
		
		String xml = stream.toXML(actionForm);
		xml = replaceOLtagWithSpace(xml);
		return xml;
		
    }
    
    /**
     * Replaces the <ol> HTML tag with EMPTY space
     * 
     * @param xml
     * @return
     */
	private String replaceOLtagWithSpace(String xml) {
		if (StringUtils.isNotBlank(xml)) {
			xml = xml.replaceAll("&lt;ol&gt;", StringUtils.EMPTY);
			xml = xml.replaceAll("&lt;/ol&gt;", StringUtils.EMPTY);
		}
		
		return xml;
	}
    
    /**
     * set the cma content for the PDF
     * @param request
     * @param withdrawalRequestUi
     * @return
     * @throws NumberFormatException
     * @throws ContentException
     */
    private WithdrawalCmaContent setCmaContent(HttpServletRequest request, WithdrawalRequestUi withdrawalRequestUi) throws NumberFormatException, ContentException {
    	WithdrawalCmaContent cmaContent = new WithdrawalCmaContent();
    	cmaContent.setTax1099rSectionTitle(getCMAContentAttributeText(
				String.valueOf(ContentConstants.SECTION_HEADING_WITHDRAWAL_STEP_2_1099R_SECTION_TITLE)));
		cmaContent.setDeclarationsSectionTitle(getCMAContentAttributeText(
				String.valueOf(ContentConstants.SECTION_HEADING_WITHDRAWAL_STEP_2_DECLARATION_SECTION_TITLE)));
		cmaContent.setDeclarationTaxNoticeText(getCMAContentAttributeTitle(
				String.valueOf(ContentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_DECLARATION_TAX_NOTICE_TEXT)));
		cmaContent.setDeclarationTaxNoticeLinkEzk(getCMAContentAttributeText(
				String.valueOf(ContentConstants.MISCELLANEOUS_WITHDRAWAL_DECLARATION_TAX_NOTICE_LINK_EZK)));
		cmaContent.setDeclarationTaxNoticeLinkPsw(getCMAContentAttributeText(
				String.valueOf(ContentConstants.MISCELLANEOUS_WITHDRAWAL_DECLARATION_TAX_NOTICE_LINK_PSW)));
		cmaContent.setWaitingPeriodText(getCMAContentAttributeText(
				String.valueOf(ContentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_DECLARATION_WAITING_PERIOD_TEXT)));
		cmaContent.setIraProviderText(getCMAContentAttributeText(
				String.valueOf(ContentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_DECLARATION_IRA_PROVIDER_TEXT)));
		cmaContent.setLegaleseText(getCMAContentAttributeText(
				String.valueOf(ContentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_APPROVE_LEGALESE_TEXT)));
		cmaContent.setRiskIndicatorText(getCMAContentAttributeText(
				String.valueOf(ContentConstants.MISCELLANEOUS_WITHDRAWAL_REVIEW_APPROVE_DECLARATION_RISK_INDICATOR_TEXT)));
		cmaContent.setParticipantInitiatedRequestMessage(getCMAContentAttributeText(
				String.valueOf(ContentConstants.MISCELLANEOUS_WITHDRAWAL_VIEW_PARTICIPANT_MESSAGE_TEXT)));
		cmaContent.setPsTPAInitiatedRequestMessage(getCMAContentAttributeText(
				String.valueOf(ContentConstants.MISCELLANEOUS_WITHDRAWAL_VIEW_PS_TPA_MESSAGE_TEXT)));
		cmaContent.setMaskedAccountNumber(CommonConstants.MASKED_ACCOUNT_NUMBER);
		cmaContent.setLoanInformation(getCMAContentAttributeText(
				String.valueOf(ContentConstants.SECTION_HEADING_WITHDRAWAL_STEP_1_LOAN_INFORMATION)));
		cmaContent.setNotesSectionTitle(getCMAContentAttributeText(
				String.valueOf(ContentConstants.SECTION_HEADING_WITHDRAWAL_STEP_2_NOTES_SECTION_TITLE)));
		cmaContent.setNotesSpecialContent(getCMAContentAttributeText(
				String.valueOf(ContentConstants.MISCELLANEOUS_WITHDRAWAL_PRINT_SPECIAL_CONTENT_TEXT)));
		cmaContent.setPersonalInformation(getCMAContentAttributeText(
				String.valueOf(ContentConstants.SECTION_HEADING_WITHDRAWAL_STEP_1_PERSONAL_INFORMATION)));
		cmaContent.setEmployeeSnapshot(getCMAContentAttributeText(
				String.valueOf(ContentConstants.SECTION_HEADING_WITHDRAWAL_STEP_1_EE_SNAPSHOT)));
		cmaContent.setPaymentInstructionsSectionTitle(getCMAContentAttributeText(
				String.valueOf(ContentConstants.SECTION_HEADING_WITHDRAWAL_STEP_2_PAYMENT_INSTRUCTIONS_TITLE)));
		cmaContent.setEftPayeeSectionTitle(getCMAContentAttributeText(
				String.valueOf(ContentConstants.SECTION_HEADING_WITHDRAWAL_STEP_2_EFT_PAYEE_TITLE)));
		cmaContent.setChequePayeeSectionTitle(getCMAContentAttributeText(
				String.valueOf(ContentConstants.SECTION_HEADING_WITHDRAWAL_STEP_2_CHEQUE_PAYEE_TITLE)));
		
		// Security Enhancements Withdrawals - remove obsolete content
//		cmaContent.setOverrideCsfMailText(getCMAContentAttributeText(
//				String.valueOf(ContentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_OVERRIDE_CSF_MAIL_TEXT)));
		
		cmaContent.setPayeexSectionContent(getCMAContentAttributeText(
				String.valueOf(ContentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_PAYEE_X_SECTION_CONTENT_TEXT)));
		cmaContent.setActivityHistory(getCMAContentAttributeText(
				String.valueOf(ContentConstants.MISCELLANEOUS_WITHDRAWAL_VIEW_ACTIVITY_HISTORY)));
		cmaContent.setAccountBalanceFootnotePbaAndLoan(getCMAContentAttributeText(
				String.valueOf(ContentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_1_2_ACCOUNT_BALANCE_PBA_AND_LOAN_TEXT)));
		cmaContent.setAccountBalanceFootnotePbaOnly(getCMAContentAttributeText(
				String.valueOf(ContentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_1_2_ACCOUNT_BALANCE_PBA_TEXT)));
		cmaContent.setAccountBalanceFootnoteLoanOnly(getCMAContentAttributeText(
				String.valueOf(ContentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_1_2_ACCOUNT_BALANCE_LOAN_TEXT)));

		cmaContent.setWithdrawalAmountSectionTitle(getCMAContentAttributeText(
				String.valueOf(ContentConstants.SECTION_HEADING_WITHDRAWAL_STEP_2_WITHDRAWAL_AMOUNT_TITLE)));
//		cmaContent.setUnvestedMoneySectionContent(getCMAContentAttributeText(
//				String.valueOf(ContentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_UNVESTED_MONEY_SECTION_CONTENT_TEXT)));
		cmaContent.setVestingInformationText(getCMAContentAttributeText(
				String.valueOf(ContentConstants.MISCELLANEOUS_VESTING_INFO_LINK)));
		cmaContent.setTaxWithholdingSectionTitle(getCMAContentAttributeText(
				String.valueOf(ContentConstants.SECTION_HEADING_WITHDRAWAL_STEP_2_TAX_WITHHOLDING_TITLE)));
		cmaContent.setParticipantHasPartialStatusText(getCMAContentAttributeText(
				String.valueOf(ContentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_PARTIAL_CONTENT_TEXT)));
		cmaContent.setParticipantHasPre1987MoneyTypeText(getCMAContentAttributeText(
				String.valueOf(ContentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_PRE_1987_CONTENT_TEXT)));
		cmaContent.setWithdrawalReasonIsFullyVestedText(getCMAContentAttributeText(
				String.valueOf(ContentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_WITHDRAWAL_REASON_IS_FULLY_VESTED)));
		cmaContent.setBasicInformation(getCMAContentAttributeText(
				String.valueOf(ContentConstants.SECTION_HEADING_WITHDRAWAL_STEP_1_BASIC_INFORMATION)));
		cmaContent.setLastProcessedDateCommentText(getCMAContentAttributeText(
				String.valueOf(ContentConstants.LAST_PROCESSED_CONTRIBUTION_DATE_COMMENT_TEXT)));
		cmaContent.setConfirmationText1(getCMAContentAttributeText(
				String.valueOf(ContentConstants.MISCELLANEOUS_WITHDRAWAL_CONFIRMATION_TEXT_V1)));
		cmaContent.setConfirmationText2(getCMAContentAttributeText(
				String.valueOf(ContentConstants.MISCELLANEOUS_WITHDRAWAL_CONFIRMATION_TEXT_V2)));
		cmaContent.setConfirmationText3(getCMAContentAttributeText(
				String.valueOf(ContentConstants.MISCELLANEOUS_WITHDRAWAL_CONFIRMATION_TEXT_V3)));
		cmaContent.setStep1PageBeanBody1(getContent(
				String.valueOf(ContentConstants.LAYOUT_PAGE_WITHDRAWALS_STEP_1),
				ContentTypeManager.instance().LAYOUT_PAGE, CommonContentConstants.BODY1, null, null));
		cmaContent.setStep1PageBeanBody2(getContent(
				String.valueOf(ContentConstants.LAYOUT_PAGE_WITHDRAWALS_STEP_1),
				ContentTypeManager.instance().LAYOUT_PAGE, CommonContentConstants.BODY2, null, null));
		cmaContent.setStep1PageBeanBody3(getContent(
				String.valueOf(ContentConstants.LAYOUT_PAGE_WITHDRAWALS_STEP_1),
				ContentTypeManager.instance().LAYOUT_PAGE, CommonContentConstants.BODY3, null, null));
		cmaContent.setStep2PageBeanBody1(getContent(
				String.valueOf(ContentConstants.LAYOUT_PAGE_WITHDRAWALS_STEP_2),
				ContentTypeManager.instance().LAYOUT_PAGE, CommonContentConstants.BODY1, null, null));
		cmaContent.setStep2PageBeanBody2(getContent(
				String.valueOf(ContentConstants.LAYOUT_PAGE_WITHDRAWALS_STEP_2),
				ContentTypeManager.instance().LAYOUT_PAGE, CommonContentConstants.BODY2, null, null));
		cmaContent.setStep2PageBeanBody3(getContent(
				String.valueOf(ContentConstants.LAYOUT_PAGE_WITHDRAWALS_STEP_2),
				ContentTypeManager.instance().LAYOUT_PAGE, CommonContentConstants.BODY3, null, null));
		
		String step2PageBeanBody1Header = StringUtils.trim(getContent(
				String.valueOf(ContentConstants.LAYOUT_PAGE_WITHDRAWALS_STEP_2),
				ContentTypeManager.instance().LAYOUT_PAGE, CommonContentConstants.BODY1_HEADER, null, null));
		
		if(StringUtils.isNotBlank(step2PageBeanBody1Header)){
			String pageBeanBody1Header[] = step2PageBeanBody1Header.split("\n");
			if(pageBeanBody1Header != null && pageBeanBody1Header.length > 0){
				List<String> step2PageBeanBody1HeaderList = new ArrayList<String>();
				for(int i = 0; i < pageBeanBody1Header.length; i++){
					step2PageBeanBody1HeaderList.add(StringUtils.trim(pageBeanBody1Header[i]));
				}
				cmaContent.setStep2PageBeanBody1HeaderList(step2PageBeanBody1HeaderList);
			}
		}
		
		String step2PageBeanBody2Header = StringUtils.trim(getContent(
				String.valueOf(ContentConstants.LAYOUT_PAGE_WITHDRAWALS_STEP_2),
				ContentTypeManager.instance().LAYOUT_PAGE, CommonContentConstants.BODY2_HEADER, null, null));
		
		if(StringUtils.isNotBlank(step2PageBeanBody2Header)){
			String pageBeanBody2Header[] = step2PageBeanBody2Header.split("\n");
			if(pageBeanBody2Header != null && pageBeanBody2Header.length > 0){
				List<String> step2PageBeanBody2HeaderList = new ArrayList<String>();
				for(int i = 0; i < pageBeanBody2Header.length; i++){
					step2PageBeanBody2HeaderList.add(StringUtils.trim(pageBeanBody2Header[i]));
				}
				cmaContent.setStep2PageBeanBody2HeaderList(step2PageBeanBody2HeaderList);
			}
		}
		
		cmaContent.setStep2PageBeanBody3Header(getContent(
				String.valueOf(ContentConstants.LAYOUT_PAGE_WITHDRAWALS_STEP_2),
				ContentTypeManager.instance().LAYOUT_PAGE, CommonContentConstants.BODY3_HEADER, null, null));
		
		LayoutPage layoutPage = getLayoutPage(getLayoutKey(), request);
		cmaContent.setFooter1(layoutPage.getFooter1());
		cmaContent.setDisclaimer(getCMATextFromContentArray(layoutPage.getDisclaimer(), false));
		cmaContent.setFootnotes(getCMATextFromContentArray(layoutPage.getFootnotes(), true));
		
		return cmaContent;
    }
    
    private List<String> getCMATextFromContentArray(Content[] contents, boolean footnote) {
    	ArrayList<String> texts = new ArrayList<String>();
    	
    	if (contents != null && contents.length > 0){
			for (Content content : contents) {
				if (footnote) {
					texts.add(((MutablePageFootnote)content).getText());
				} else { 
					texts.add(((MutableDisclaimer)content).getText());
				}
			}
		}
    	return texts;
    }
    
    /*
     * (non-Javadoc)
     * @see com.manulife.pension.platform.web.controller.BasePdfAutoAction#getXSLTFileName()
     */
    protected String getXSLTFileName() {
        return XSLT_FILE_KEY_NAME;
    }
    
    /*
	 * (non-Javadoc)
	 * @see com.manulife.pension.platform.web.controller.BasePdfAutoAction#getIncludedXSLPath()
	 */
	public String getIncludedXSLPath() {
	       return INCLUDED_XSL_PATH_KEY_NAME;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.manulife.pension.platform.web.controller.BasePdfAutoAction#getFilename(com.manulife.pension.platform.web.controller.AutoForm, javax.servlet.http.HttpServletRequest)
	 */
	public String getFilename(AutoForm actionForm, HttpServletRequest request) {
		/**
		 * Print for Participant 	-> WD_PPT_<CN#>_<LASTNAME>_YYYYMMDD.pdf
		 * Print Report				-> WD_RPT_<CN#>_<LASTNAME>_YYYYMMDD.pdf
		 */
		StringBuffer filename = new StringBuffer();
		WithdrawalRequest withdrawalRequest = ((WithdrawalForm)actionForm).getWithdrawalRequestUi().getWithdrawalRequest();
		
		filename.append("WD").append(Constants.UNDERSCORE);
		if (getPrintParticipant(request)) {
			filename.append(Constants.PRINT_FOR_PARTICIPANT_ID).append(Constants.UNDERSCORE);
		} else {
			filename.append(Constants.PRINT_REPORT_ID).append(Constants.UNDERSCORE);
		}
		filename.append(withdrawalRequest.getContractId()).append(Constants.UNDERSCORE);
		filename.append(withdrawalRequest.getLastName()).append(Constants.UNDERSCORE);
		filename.append(DateFormatUtils.format(new Date(), "yyyyMMdd"));
		filename.append(Constants.PDF_FILE_NAME_EXTENSION);
		
		return filename.toString();
	}
	
	/**
	 *  @returns the layout key
	 */
	public String getLayoutKey(){
		return null;
	}
	
	/**
     * This method gets layout page for the given layout id.
     * 
     * @param path
     * @return LayoutPage
     */
    protected LayoutPage getLayoutPage(String id, HttpServletRequest request) {
        LayoutBean layoutBean = LayoutBeanRepository.getInstance().getPageBean(id);
        LayoutPage layoutPageBean = (LayoutPage) layoutBean.getLayoutPageBean();
        return layoutPageBean;
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
					.getProfileId()) + ":" + profile.getPrincipal().getUserName());

			// Comment by SYE - We have to use Category.error to log into the mrl, even the log is in trace or info level, this is a bug in mrl
			interactionLog.error(record);
		} catch (CloneNotSupportedException e) {
			// log the error, but don't interrupt regular processing
			logger.error("error when trying to log into MRL the data:"
					+ logData + ". Exception caught= " + e);
		}
	}
	
	/**
	 * * (non-Javadoc) This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations as part of
	 * the CL#137697.
	 */
	
	@Autowired
	private PSValidatorFWDefault psValidatorFWDefault;  

	@InitBinder
	protected void initBinder(HttpServletRequest request,
				ServletRequestDataBinder  binder) {
		binder.addValidators(psValidatorFWDefault);
	} 
}
