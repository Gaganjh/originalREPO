/*
 * WithdrawalServiceDelegate.java,v 1.1.2.1 2006/08/28 20:47:41 Paul_Glenn Exp
 * WithdrawalServiceDelegate.java,v
 * Revision 1.1.2.1  2006/08/28 20:47:41  Paul_Glenn
 * Initial.
 *
 */
package com.manulife.pension.service.withdrawal.delegate;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.apache.commons.lang.exception.NestableRuntimeException;

import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.environment.valueobject.StateTaxVO;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.withdrawal.WithdrawalService;
import com.manulife.pension.service.withdrawal.WithdrawalServiceHome;
import com.manulife.pension.service.withdrawal.WithdrawalServiceLocalHome;
import com.manulife.pension.service.withdrawal.WithdrawalServiceRemote;
import com.manulife.pension.service.withdrawal.WithdrawalServiceUtil;
import com.manulife.pension.service.withdrawal.dao.HardshipWithdrawalDAO;
import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantCategory;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantFlag;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantInfo;
import com.manulife.pension.service.withdrawal.valueobject.PendingReviewApproveWithdrawalCount;
import com.manulife.pension.service.withdrawal.valueobject.TaxesFlag;
import com.manulife.pension.service.withdrawal.valueobject.UserName;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalMultiPayee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMetaData;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;
import com.manulife.pension.util.email.BodyPart;

/**
 * WithdrawalServiceDelegate delegates requests to the EJB from the web tier.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1.2.1 2006/08/28 20:47:41
 */
public final class WithdrawalServiceDelegate {

    // TODO: Singleton for now, should use a factory, so we can mock it.
    private static WithdrawalServiceDelegate withdrawalServiceDelegate;

    // private static final Logger logger = Logger.getLogger(WithdrawalServiceDelegate.class);

    /**
     * Default Constructor.
     */
    private WithdrawalServiceDelegate() {
        super();
    }

    /**
     * This method gets an instance of the object. This is the singleton pattern.
     * 
     * @return WithdrawalServiceDelegate The singleton instance.
     */
    public static WithdrawalServiceDelegate getInstance() {
        if (withdrawalServiceDelegate == null) {
            withdrawalServiceDelegate = new WithdrawalServiceDelegate();
        } // fi
        return withdrawalServiceDelegate;
    }

    /**
     * Calls the {@link WithdrawalServiceUtil} getLocalHome method to find the local home.
     * 
     * @see com.manulife.pension.service.delegate.LocalServiceDelegate#getLocalHome()
     * 
     * @return Object - The local home.
     */
    private Object getLocalHome() {
        try {
            return WithdrawalServiceUtil.getLocalHome();
        } catch (NamingException namingException) {
            throw new NestableRuntimeException(namingException);
        }
    }

    /**
     * Gets the service.
     * 
     * @return WithdrawalService
     * @throws SystemException if there is an exception.
     */
    private WithdrawalService getLocalWithdrawalService() throws SystemException {
        final WithdrawalServiceLocalHome withdrawalServiceLocalHome;
        withdrawalServiceLocalHome = (WithdrawalServiceLocalHome) getLocalHome();

        try {
            return withdrawalServiceLocalHome.create();
        } catch (CreateException createException) {
            throw new SystemException(createException, this.getClass().getName(),
                    "getLocalWithdrawalService", "CreateException occured.");
        } // end try/catch
    }

    /**
     * Gets the remote service.
     * 
     * @return WithdrawalServiceRemote The remote interface for the service.
     * @throws SystemException if there is an exception.
     */
    private WithdrawalServiceRemote getWithdrawalService() throws SystemException {
        return getWithdrawalService(null);
    }

    /**
     * Gets the remote service.
     * 
     * @param environment Parameters to use for creating the initial context.
     * @return WithdrawalServiceRemote The remote interface for the service.
     * @throws SystemException if there is an exception.
     */
    private WithdrawalServiceRemote getWithdrawalService(final Hashtable environment)
            throws SystemException {
        try {
            if (environment == null) {
                return WithdrawalServiceUtil.getHome().create();
            } else {
                return WithdrawalServiceUtil.getHome(environment).create();
            } // fi
        } catch (RemoteException remoteException) {
            throw new SystemException(remoteException, this.getClass().getName(),
                    "getLocalWithdrawalService", "RemoteException occured.");
        } catch (CreateException createException) {
            throw new SystemException(createException, this.getClass().getName(),
                    "getLocalWithdrawalService", "CreateException occured.");
        } catch (NamingException namingException) {
            throw new SystemException(namingException, this.getClass().getName(),
                    "getLocalWithdrawalService", "NamingException occured.");
        } // end try/catch
    }

    /**
     * Cached local home (EJBLocalHome). Uses lazy loading to obtain its value (loaded by
     * getLocalHome() methods).
     */
    private static WithdrawalServiceLocalHome cachedLocalHome = null;

    /**
     * Cached remote home (EJBHome). Uses lazy loading to obtain its value (loaded by getHome()
     * methods).
     */
    private static WithdrawalServiceHome cachedRemoteHome = null;

    private static Object lookupHome(final java.util.Hashtable environment, final String jndiName,
            final Class narrowTo) throws javax.naming.NamingException {
        // Obtain initial context
        javax.naming.InitialContext initialContext = new javax.naming.InitialContext(environment);
        try {
            Object objRef = initialContext.lookup(jndiName);
            // only narrow if necessary
            if (java.rmi.Remote.class.isAssignableFrom(narrowTo)) {
                return javax.rmi.PortableRemoteObject.narrow(objRef, narrowTo);
            } else {
                return objRef;
            } // fi
        } finally {
            initialContext.close();
        } // end try/finally
    }

    /**
     * Obtain remote home interface from default initial context.
     * 
     * @return Home interface for WithdrawalService. Lookup using JNDI_NAME
     */
    public static WithdrawalServiceHome getPhysicalHome() throws javax.naming.NamingException {
        if (cachedRemoteHome == null) {
            cachedRemoteHome = (WithdrawalServiceHome) lookupHome(null,
                    com.manulife.pension.service.withdrawal.WithdrawalServiceHome.JNDI_NAME,
                    com.manulife.pension.service.withdrawal.WithdrawalServiceHome.class);
        }
        return cachedRemoteHome;
    }

    /**
     * Obtain local home interface from default initial context.
     * 
     * @return Local home interface for WithdrawalService. Lookup using JNDI_NAME
     */
    private static WithdrawalServiceLocalHome getPhysicalLocalHome()
            throws javax.naming.NamingException {
        if (cachedLocalHome == null) {
            cachedLocalHome = (WithdrawalServiceLocalHome) lookupHome(null,
                    com.manulife.pension.service.withdrawal.WithdrawalServiceLocalHome.JNDI_NAME,
                    com.manulife.pension.service.withdrawal.WithdrawalServiceLocalHome.class);
        }
        return cachedLocalHome;
    }

    /**
     * Gets the service using the physical JNDI lookup. <b>NOTE: </b> This lookup for the service
     * should only be used in places where you do not have access to the application server context.
     * In most cases, you should use <code>getWithdrawalService()</code>.
     * 
     * @return WithdrawalService
     * @throws SystemException if there is an exception.
     */
    private WithdrawalService getWithdrawalServiceWithPhysicalJndiName() throws SystemException {
        try {
            final WithdrawalServiceLocalHome withdrawalServiceLocalHome = getPhysicalLocalHome();
            return withdrawalServiceLocalHome.create();
        } catch (NamingException namingException) {
            throw new SystemException(namingException, this.getClass().getName(),
                    "getWithdrawalServiceWithPhysicalJndiName", "NamingException occured.");
        } catch (CreateException createException) {
            throw new SystemException(createException, this.getClass().getName(),
                    "getWithdrawalServiceWithPhysicalJndiName", "CreateException occured.");
        } // end try/catch
    }

    /**
     * Gets the remote service using the physical JNDI lookup. <b>NOTE: </b> This lookup for the
     * service should only be used in places where you do not have access to the application server
     * context. In most cases, you should use <code>getWithdrawalService()</code>.
     * 
     * @return WithdrawalServiceRemote
     * @throws SystemException if there is an exception.
     */
    private WithdrawalServiceRemote getWithdrawalServiceRemoteWithPhysicalJndiName()
            throws SystemException {
        try {
            final WithdrawalServiceHome withdrawalServiceHome = getPhysicalHome();
            return withdrawalServiceHome.create();
        } catch (NamingException namingException) {
            throw new SystemException(namingException, this.getClass().getName(),
                    "getWithdrawalServiceRemoteWithPhysicalJndiName", "NamingException occured.");
        } catch (CreateException createException) {
            throw new SystemException(createException, this.getClass().getName(),
                    "getWithdrawalServiceRemoteWithPhysicalJndiName", "CreateException occured.");
        } catch (RemoteException remoteException) {
            throw new SystemException(remoteException, this.getClass().getName(),
                    "getWithdrawalServiceRemoteWithPhysicalJndiName", "RemoteException occured.");
        } // end try/catch
    }

    /**
     * This method delegates the request to the EJB.
     * 
     * @param profileId the employee profile id
     * @param contractid the contract id of the employee
     * @param principal the users principal
     * @return WithdrawalRequest The data from the database.
     * @throws SystemException If a {@link RemoteException} occurs.
     * @throws DistributionServiceException If an application error occurs.
     * @see WithdrawalService#initiateNewWithdrawalRequest()
     */
    public WithdrawalRequest initiateNewWithdrawalRequest(final Integer profileId,
            final Integer contractId, final Principal principal) throws SystemException,
            DistributionServiceException {

        WithdrawalRequest wr = null;
        wr = getLocalWithdrawalService().initiateNewWithdrawalRequest(profileId, contractId,
                principal);
        return wr;
    }

    /**
     * This method delegates the request to the EJB primarily called for the participant initiated
     * withdrawal txns
     * 
     * @param profileId the employee profile id
     * @param contractid the contract id of the employee
     * @param principal the users principal
     * @return WithdrawalRequest The data from the database.
     * @throws SystemException If a {@link RemoteException} occurs.
     * @throws DistributionServiceException If an application error occurs.
     * @see WithdrawalService#initiateNewWithdrawalRequest()
     */
    public WithdrawalRequest initiateNewParticipantWithdrawalRequest(final Integer profileId,
            final Integer contractId) throws SystemException, DistributionServiceException {

        WithdrawalRequest wr = null;
        wr = getLocalWithdrawalService().initiateNewParticipantWithdrawalRequest(profileId,
                contractId);
        return wr;
    }

    /**
     * Read a withdrawal request for editing
     * 
     * @param submissionId the submission id
     * @param userProfileId the users profile id
     * @return the value object
     * @throws SystemException thrown if a remote exception occurs
     * @throws DistributionServiceException thrown if there is an application exception
     */
    // [AP 20070416] Refactor method signature: Eliminate redundant parameters contractId, profileId
    public WithdrawalRequest readWithdrawalRequestForEdit(final Integer submissionId,
            final Principal principal) throws SystemException, DistributionServiceException {

        WithdrawalRequest wr = null;
        wr = getLocalWithdrawalService().readWithdrawalRequestForEdit(submissionId, principal);
        return wr;
    }

    /**
     * Read a withdrawal request for viewing
     * 
     * @param submissionId the submission id
     * @param userProfileId the users profile id
     * @return the value object
     * @throws SystemException thrown if a remote exception occurs
     */
    // [AP 20070416] Refactor method signature: Eliminate redundant parameters contractId, profileId
    public WithdrawalRequest readWithdrawalRequestForView(final Integer submissionId,
    // final Principal principal, final String cmaSiteCode) throws SystemException {
            final Principal principal) throws SystemException {

        WithdrawalRequest wr = null;
        // wr = getLocalWithdrawalService().readWithdrawalRequestForView(submissionId, principal,
        // cmaSiteCode);
        wr = getLocalWithdrawalService().readWithdrawalRequestForView(submissionId, principal);
        return wr;
    }

    /**
     * Sends a withdrawal request object for review.
     * 
     * @param withdrawalRequest The value object to work with.
     * @return WithdrawalRequest The updated value object.
     * @throws SystemException If a {@link RemoteException} occurs.
     * @see WithdrawalService#sendForReview(WithdrawalRequest)
     */
    public WithdrawalRequest sendForReview(final WithdrawalRequest withdrawalRequest)
            throws SystemException {
        return getLocalWithdrawalService().sendToReviewer(withdrawalRequest);
    }

    /**
     * Sends a withdrawal request object for approval.
     * 
     * @param withdrawalRequest The value object to work with.
     * @return WithdrawalRequest The updated value object.
     * @throws SystemException If a {@link RemoteException} occurs.
     * @see WithdrawalService#sendForApproval(WithdrawalRequest)
     */
    public WithdrawalRequest sendForApproval(final WithdrawalRequest withdrawalRequest)
            throws SystemException {
        return getLocalWithdrawalService().sendToApprover(withdrawalRequest);
    }

    /**
     * Approves a withdrawal request object.
     * 
     * @param withdrawalRequest The value object to work with.
     * @return WithdrawalRequest The updated value object.
     * @throws SystemException If a {@link RemoteException} occurs.
     * @see WithdrawalService#approve(WithdrawalRequest)
     */
    public WithdrawalRequest approve(final WithdrawalRequest withdrawalRequest)
            throws SystemException {
        return getLocalWithdrawalService().approve(withdrawalRequest);
    }

    /**
     * Processes an approved withdrawal request object.
     * 
     * @param withdrawalRequest The approved withdrawal request.
     */
    public void processApproved(final WithdrawalRequest withdrawalRequest) throws SystemException {
        getLocalWithdrawalService().processApproved(withdrawalRequest);
    }

    /**
     * Recalculates the taxes for a withdrawal request object.
     * 
     * @param withdrawalRequest The value object to work with.
     * @return WithdrawalRequest The updated value object.
     * @throws SystemException If a {@link CreateException} occurs.
     * @see WithdrawalService#recalculate(WithdrawalRequest)
     */
    public void recalculate(final WithdrawalRequest withdrawalRequest) throws SystemException {
        getLocalWithdrawalService().recalculate(withdrawalRequest);
    }

    /**
     * This method retrieves a collection of all state tax options.
     * 
     * @param withdrawalRequest The withdrawal request to retrieve the state tax options for.
     * @return Collection<StateTaxVO> The collection of state tax options.
     * @throws SystemException If a {@link CreateException} occurs.
     * @see WithdrawalService#getAllStateTaxOptions(WithdrawalRequest)
     */
    public Collection<StateTaxVO> getAllStateTaxOptions(final WithdrawalRequest withdrawalRequest)
            throws SystemException {
        return getLocalWithdrawalService().getAllStateTaxOptions(withdrawalRequest);
    }

    /**
     * Delegate the call to the WithdrawalService Facade.
     * 
     * @param withdrawalRequest The object to save.
     * @throws SystemException If an exception occurs.
     */
    public void save(final WithdrawalRequest withdrawalRequest) throws SystemException {
        getLocalWithdrawalService().save(withdrawalRequest);
    }

    /**
     * Delegate the call to the WithdrawalService Facade.
     * 
     * @param withdrawalRequest The object to delete.
     * @throws SystemException If an exception occurs.
     */
    public void delete(final WithdrawalRequest withdrawalRequest) throws SystemException {
        getLocalWithdrawalService().delete(withdrawalRequest);
    }

    /**
     * Delegate the call to the WithdrawalService Facade.
     * 
     * @param withdrawalRequest The object to deny.
     * @throws SystemException If an exception occurs.
     */
    public void deny(final WithdrawalRequest withdrawalRequest) throws SystemException {
        getLocalWithdrawalService().deny(withdrawalRequest);
    }

    /**
     * Delegates to the withdrawal service getWithdrawalInfo method
     * 
     * @param participantId
     * @param contractId
     * @throws SystemException
     * @throws DistributionServiceException
     */
    public WithdrawalInfo getWithdrawalInfo(int participantId, int contractId)
            throws SystemException, DistributionServiceException {
        return getLocalWithdrawalService().getWithdrawalInfo(participantId, contractId);
    }

    /**
     * Delegates to the withdrawal service getParticipantInfo method
     * 
     * @param profileId
     * @param participantId
     * @param contractId
     * @throws SystemException
     * @throws DistributionServiceException
     */
    public ParticipantInfo getParticipantInfo(final int profileId, final int participantId,
            final int contractId) throws SystemException, DistributionServiceException {
        return getLocalWithdrawalService().getParticipantInfo(profileId, participantId, contractId);
    }

    /**
     * Delegates to the withdrawal service getWithdrawalRequests method
     * 
     * @param rofileId
     * @param contractId
     * @throws SystemException
     */
    public Collection getWithdrawalRequests(int profileId, int contractId) throws SystemException {
        return getLocalWithdrawalService().getWithdrawalRequests(profileId, contractId);
    }

    /**
     * Delegates to the withdrawal service getNumberOfCompletedWithdrawalTransaction method
     * 
     * @param contractId
     * @param participantId
     * @return
     * @throws DistributionServiceException
     * @throws SystemException
     */
    public int getNumberOfCompletedWithdrawalTransaction(Integer contractId, Integer participantId)
            throws DistributionServiceException, SystemException {
        return getLocalWithdrawalService().getNumberOfCompletedWithdrawalTransaction(contractId,
                participantId);
    }

    /**
     * Delegates to the withdrawal service getNumberOfPendingWithdrawalTransaction method
     * 
     * @param contractId
     * @param participantId
     * @return
     * @throws DistributionServiceException
     * @throws SystemException
     */
    public int getNumberOfPendingWithdrawalTransaction(Integer contractId, Integer participantId)
            throws DistributionServiceException, SystemException {
        return getLocalWithdrawalService().getNumberOfPendingWithdrawalTransaction(contractId,
                participantId);
    }

    /**
     * This method simply delegates the call to the local interface of the withdrawal service.
     * 
     * @param withdrawalRequest The withdrawal request to use.
     * @return {@link WithdrawalRequest} The result.
     * @throws SystemException If an exception occurs.
     */
    public WithdrawalRequest performStep1DefaultSetup(final WithdrawalRequest withdrawalRequest)
            throws SystemException {
        return getLocalWithdrawalService().performStep1DefaultSetup(withdrawalRequest);
    }

    /**
     * This method simply delegates the call to the local interface of the withdrawal service.
     * 
     * @param withdrawalRequest The withdrawal request to use.
     * @return {@link WithdrawalRequest} The result.
     * @throws SystemException If an exception occurs.
     */
    public WithdrawalRequest performStep2DefaultSetup(final WithdrawalRequest withdrawalRequest)
            throws SystemException {
        return getLocalWithdrawalService().performStep2DefaultSetup(withdrawalRequest);
    }

    /**
     * This method simply delegates the call to the local interface of the withdrawal service.
     * 
     * @param withdrawalRequest The withdrawal request to use.
     * @return {@link WithdrawalRequest} The result.
     * @throws SystemException If an exception occurs.
     */
    public WithdrawalRequest performReviewDefaultSetup(final WithdrawalRequest withdrawalRequest)
            throws SystemException {
        return getLocalWithdrawalService().performReviewDefaultSetup(withdrawalRequest);
    }

    /**
     * Returns an ordered list of WD reasons. The order in which the codes are returned is the
     * specified display order.
     * 
     * @param contractStatus Contract status code
     * @param participantStatus Participant status code
     * @return Collection<DeCodeVO> - An ordered list of withdrawal reasons matching the contract
     *         and participant contract status which are enabled for OnlineWithdrawals.
     * @throws DistributionServiceException thrown if there is a withdrawl service exception
     */
    public Collection getParticipantWithdrawalReasons(final String contractStatus,
            final String participantStatus) throws DistributionServiceException, SystemException {
        return getLocalWithdrawalService().getParticipantWithdrawalReasons(contractStatus,
                participantStatus);
    }

    /**
     * Returns an ordered list of Payment To options. The order in which the codes are returned is
     * the specified display order.
     * 
     * @param participantInfo The participant information to filter the payment to options with.
     * @return Collection<DeCodeVO> - An ordered list of payment to options matching the
     *         participant information.
     * @throws DistributionServiceException thrown if there is a withdrawl service exception
     */
    public Collection getParticipantPaymentToOptions(final ParticipantInfo participantInfo)
            throws DistributionServiceException, SystemException {
        return getLocalWithdrawalService().getParticipantPaymentToOptions(participantInfo);
    }

    /**
     * returns a
     * 
     * @param contractId
     * @return count of pending review and pending approve withdrawal requests
     * @throws DistributionServiceException
     * @throws SystemException
     */
    public PendingReviewApproveWithdrawalCount getPendingReviewApproveWithdrawalRequests(
            final Integer contractId) throws DistributionServiceException, SystemException {

        return getLocalWithdrawalService().getPendingReviewApproveWdRequestCounts(contractId);
    }

    /**
     * Retrieves a map of user ID-user name key-value pairs corresponding to the user IDs associated
     * with any withdrawal request notes. This method does not populate the user's role.
     * 
     * @param userIds The collection of user IDs to retrieves user names for.
     * @return Map The map of user ID, user name key value pairs.
     */
    public Map<Integer, UserName> getUserNamesForIds(final Collection<Integer> userIds)
            throws SystemException {

        return getLocalWithdrawalService().getUserNames(userIds);
    }

    /**
     * validates the date of birth entered for the participant.
     * 
     * @param request
     */
    public void proceedToStep2(WithdrawalRequest request) throws SystemException {
        try {
            getLocalWithdrawalService().proceedToStep2(request);
        } catch (SystemException e) {
            throw ExceptionHandlerUtility.wrap(new SystemException(e, this.getClass().getName(),
                    "proceedToStep2", "Could not validate data on step one"));
        }
    }

    /**
     * validates step 2 data before allowing the user to move back to step 1.
     * 
     * @param request
     */
    public void returnToStep1(WithdrawalRequest request) throws SystemException {
        try {
            getLocalWithdrawalService().returnToStep1(request);
        } catch (SystemException e) {
            throw ExceptionHandlerUtility.wrap(new SystemException(e, this.getClass().getName(),
                    "returnToStep1", "Could not validate data on step two"));
        }
    }
    
    /**
     * validates termination and retirement date when allowing the user to move back to step 1.
     * 
     * @param request
     */
    public void returnToStep1WithTerminationOrRetirementDate(WithdrawalRequest request) throws SystemException {
        try {
            getLocalWithdrawalService().returnToStep1WithTerminationOrRetirementDate(request);
        } catch (SystemException e) {
            throw ExceptionHandlerUtility.wrap(new SystemException(e, this.getClass().getName(),
                    "returnToStep1WithTerminationOrRetirementDate", "Could not validate data on termination date"));
        }
    }

    /**
     * Marks all WithdrawalRequests having the expiry date before the current expiration date and
     * being in Draft or Pending states as Expired.
     * 
     * Logs this event in the Activity History and via MRL
     * 
     * @param checkDate Date used to test if WithdrawalRequests expired (typically the current date)
     * @param profileId String value containing the user profile ID or system profile ID (3)
     */
    public int markExpiredWithdrawals(final java.util.Date checkDate, final Integer profileId)
            throws SystemException {
        try {
            // The periodic process is calling this from a thread in the web tier that may
            // not have access to the local context, so we use the remote EJB interface.
            return getWithdrawalServiceRemoteWithPhysicalJndiName().markExpiredWithdrawals(
                    checkDate, profileId);
        } catch (RemoteException remoteException) {
            throw ExceptionHandlerUtility.wrap(new SystemException(remoteException, this.getClass()
                    .getName(), "markExpiredWithdrawals",
                    "Could not retrieve the list of expiring Requests "));
        }
    }

    /**
     * Retrieves contract information pertinent to a withdrawal request.
     * 
     * @param contractId The employee contract id
     * @param principal The user {@link Principal}
     * @return ContractInfo - The contract information relevant to a withdrawal request.
     * @throws SystemException thrown if a remote exception occurs
     * @throws DistributionServiceException thrown if there is an application exception
     */
    public ContractInfo getContractInfo(final Integer contractId, final Principal principal)
            throws SystemException {

        return getLocalWithdrawalService().getContractInfo(contractId, principal);
    }

    /**
     * Retreives the Lookup data for the withdrawal request. Filtering is applied to the lookup data
     * for pages where the user can edit. To get values for view pages(no filtering), pass
     * contractInfo = null or participantStatusCode = StringUtils.EMPTY
     * 
     * @param contractInfo
     * @return
     * @throws SystemException
     */

    public Map getLookupData(final ContractInfo contractInfo, final String participantStatusCode,
            final Collection<String> keys) throws SystemException {
        return getLocalWithdrawalService().getLookupData(contractInfo, participantStatusCode, keys);
    }

    /**
     * Read the agreed legalese content depending on the cmaSiteCode
     * 
     * @param submissionId the submission id
     * @param cmaSiteCode whether it's psw or ezk
     * @return the String object
     * @throws SystemException thrown if a remote exception occurs
     */
    public String getAgreedLegaleseContent(final Integer submissionId, String cmaSiteCode)
            throws SystemException {

        String legaleseContent = null;
        legaleseContent = getLocalWithdrawalService().getAgreedLegaleseText(submissionId,
                cmaSiteCode);
        return legaleseContent;
    }

    /**
     * Handler invoked after setting CSF "Allow Online Withdrawal Requests" to YES: - currently no
     * action.
     * 
     * @param contractId ID of the contract for which the online W/D feature has changed
     * @param profileId ID of the user that modified the Contract Feature
     * 
     */
    public void handleDisableOnlineWithdrawals(final Integer contractId, final Integer profileId,
            String siteCode) throws SystemException {
        getLocalWithdrawalService().handleDisableOnlineWithdrawals(contractId, profileId, siteCode);
        // WithdrawalRequest.USER_ROLE_PLAN_SPONSOR_CODE);
    }

    /**
     * Handler invoked after setting CSF "Allow Online Withdrawal Requests" to NO: - Change the
     * status to Pending Approval for all Withdrawal requests that are currently in the Pending
     * Review status.
     * 
     * @param contractId ID of the contract for which the online W/D feature has changed
     * @param principal principal of the user that modified the Contract Feature
     */
    public void handleEnableOneStepApprovals(final Integer contractId, final Principal principal)
            throws SystemException {
        getLocalWithdrawalService().handleEnableOneStepApprovals(contractId, principal,
                WithdrawalRequest.CMA_SITE_CODE_PSW);
    }

    /**
     * Handler invoked after setting CSF "Allow Online Withdrawal Requests" to YES: - currently no
     * action.
     * 
     * @param contractId ID of the contract for which the online W/D feature has changed
     * @param profileId ID of the user that modified the Contract Feature
     */
    public void handleEnableOnlineWithdrawals(final Integer contractId, final Integer profileId)
            throws SystemException {
        getLocalWithdrawalService().handleEnableOnlineWithdrawals(contractId, profileId);
    }

    /**
     * Handler invoked after setting CSF "Allow Online Withdrawal Requests" to YES: - currently no
     * action.
     * 
     * @param contractId ID of the contract for which the online W/D feature has changed
     * @param profileId ID of the user that modified the Contract Feature
     */
    public void handleEnableTwoStepApprovals(final Integer contractId, final Integer profileId)
            throws SystemException {
        getLocalWithdrawalService().handleEnableTwoStepApprovals(contractId, profileId);
    }

    /**
     * Returns the most recent withdrawal txn for a given contract and participant
     * 
     * @param profileId the participant's profile id
     * @param contractId the contract number
     * @return WithdrawalRequest object
     * 
     */
    public WithdrawalRequest getMostRecentWithdrawalRequest(final Integer profileId,
            final Integer contractId) throws SystemException {

        return getLocalWithdrawalService().getMostRecentWithdrawalRequest(profileId, contractId);
    }

    /**
     * Sends a withdrawal request object for review for participant initiated.
     * 
     * @param withdrawalRequest The value object to work with.
     * @return WithdrawalRequest The updated value object.
     * @throws SystemException If a {@link RemoteException} occurs.
     * @see WithdrawalService#sendForReview(WithdrawalRequest)
     */
    public WithdrawalRequest submitParticipantInitiatedWithdrawal(
            final WithdrawalRequest withdrawalRequest) throws SystemException {
        return getLocalWithdrawalService().submitParticipantInitiatedWithdrawal(withdrawalRequest);
    }

    /**
     * This method looks up the meta data for the given submission.
     * 
     * @param submissionId - The submission ID to look up the meta data for.
     * @return {@link WithdrawalRequestMetaData} - The meta data for the given submissionId.
     * @throws SystemException If an exception occurs.
     */
    public WithdrawalRequestMetaData getWithdrawalRequestMetaData(final Integer submissionId)
            throws SystemException {
        return getLocalWithdrawalService().getWithdrawalRequestMetaData(submissionId);
    }
    
    /**
     * 
     * @param contractNumber
     * @return String The RequiresSpousalConsentForDistributions
     * @throws SystemException
     */
	public String getRequiresSpousalConsentForDistributions(final Integer contractNumber) 
			throws SystemException {
		return getLocalWithdrawalService()
				.getRequiresSpousalConsentForDistributions(contractNumber);
	}
	 
	/**
	 * 
	 * @param profileId
	 * @param contractNumber
	 * @return
	 * @throws SystemException
	 */
	 public ParticipantFlag getPartitcipantExceptionFlagDetials(String profileId , String contractNumber) throws SystemException {
		 return getLocalWithdrawalService()
				 .getPartitcipantExceptionFlagDetials( profileId ,  contractNumber);
	 }
	 
	 /**
	  * 
	  * @return
	  * @throws SystemException
	  */
	 public List<ParticipantCategory> getParticipantCategoryList() throws SystemException{
		 return getLocalWithdrawalService()
				 .getParticipantCategoryList();
	 }
	 
	 /**
	  * 
	  * @param participantFlag
	  * @throws SystemException
	  */
	 public void saveParticipantFlagInfo(ParticipantFlag participantFlag) throws SystemException {
		 getLocalWithdrawalService().saveParticipantFlagInfo(participantFlag);
	 }
	 /**
	  * 
	  * @param withdrawalRequest
	  * @throws SystemException
	  */
	 public void sendReadyForEntryEmail(final WithdrawalRequest withdrawalRequest) throws SystemException {
		   getLocalWithdrawalService().sendReadyForEntryEmail(withdrawalRequest);
	 }
	 /**
	  * 
	  * @param submissionId
	  * @param participantId
	  * @param contractId
	  * @return
	  * @throws SystemException
	  */
	 public String callApolloSTPForOnlineWithdrawal(final Integer submissionId, final Integer participantId, Integer contractId) throws SystemException {
		 return getLocalWithdrawalService().callApolloSTPForOnlineWithdrawal(submissionId, participantId, contractId);
	 }
	 
	 /***
	  * 
	  * @param withdrawalRequest
	  * @return
	  * @throws SystemException
	  */
	 public ArrayList<BodyPart> getReadyForEntryEmailContent(final WithdrawalRequest withdrawalRequest) throws SystemException {
	      return getLocalWithdrawalService().getReadyForEntryEmailContent(withdrawalRequest);
	  }
	 
	 /**
	  * 
	  * @param submissionId
	  * @param participantId
	  * @param contractId
	  * @return
	  * @throws SystemException
	  */
	 public String executeLpTxnGenSTPStoredProc(final Integer submissionId, final Integer participantId, Integer contractId , String reasonCode,final java.sql.Date rateEffectiveDate,final String tpaFeeFlag,int payeeCount) throws SystemException {
		 return getLocalWithdrawalService().executeLpTxnGenSTPStoredProc(submissionId, participantId, contractId ,  reasonCode, rateEffectiveDate, tpaFeeFlag,payeeCount);
	 }
	
	 /**
	  * get Multipayee Screen
	  * @param request
	  * @throws SystemException
	  */
	 public void updatedPayeesForMultipleDestination(WithdrawalRequest request) throws SystemException{
		   getLocalWithdrawalService().updatedPayeesForMultipleDestination(request);
		   
	 }
	 /**
	  * get List of payee tax Flag
	  * @return
	  * @throws SystemException
	  * @throws SQLException
	  */
	 public List<TaxesFlag> getPayessTaxFlag() throws SystemException, SQLException{
		 return getLocalWithdrawalService().getPayeeTaxFlag();
	 }
	 
	 public void getMultipayeeSection(Integer contractId, WithdrawalRequest req) throws SystemException{
		 getLocalWithdrawalService().getMultipayeeSection(contractId, req);
	 }
	 // multidestination validation based on contract
	 public boolean validateMD(Integer contractId) throws SystemException {
		 return getLocalWithdrawalService().validateMD(contractId);
	 }
	 /**
	  * getParticipantNetEEDeferralContributions for Hasrdship
	  */
	 public double getParticipantNetEEDeferralContributions(int profileId,int contractNumber, String ssn, Date asOfDate) throws SystemException{
		 return getLocalWithdrawalService().getParticipantNetEEDeferralContributions(profileId,contractNumber, ssn, asOfDate);
	 }
	 public boolean sendWithdrawalNotificationsForReviewOrApprove(Integer submissionId, String statusCode,WithdrawalRequest withdrawalReq)throws SystemException{
         return getLocalWithdrawalService().sendWithdrawalNotificationsForReviewOrApprove(submissionId,statusCode, withdrawalReq);
	 }
	 
	 public boolean updateActivityData(Integer submissionId, Principal principal)throws SystemException{
         return getLocalWithdrawalService().updateActivityData(submissionId,principal);
     }
} 

