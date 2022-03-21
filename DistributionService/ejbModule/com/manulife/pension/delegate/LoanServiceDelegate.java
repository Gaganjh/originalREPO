package com.manulife.pension.delegate;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.apache.commons.lang.exception.NestableRuntimeException;

import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.distribution.valueobject.AtRiskAddressChangeVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsInputVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskPasswordResetVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskWebRegistrationVO;
import com.manulife.pension.service.distribution.valueobject.ManagedContent;
import com.manulife.pension.service.loan.LoanService;
import com.manulife.pension.service.loan.LoanServiceHome;
import com.manulife.pension.service.loan.LoanServiceLocalHome;
import com.manulife.pension.service.loan.LoanServiceRemote;
import com.manulife.pension.service.loan.LoanServiceUtil;
import com.manulife.pension.service.loan.exception.LoanDaoException;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanActivities;
import com.manulife.pension.service.loan.valueobject.LoanMoneyType;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.loan.valueobject.LoanSupportDataRetriever;
import com.manulife.pension.service.loan.valueobject.OutstandingLoan;
import com.manulife.pension.service.loan.valueobject.WebLoanSupportDataRetriever;
import com.manulife.pension.service.vesting.EmployeeVestingInformation;
import com.manulife.pension.util.Pair;

/**
 * LoanServiceDelegate delegates requests to the EJB from the web tier.
 */

public final class LoanServiceDelegate {

	private static LoanServiceDelegate loanServiceDelegate;

	/**
	 * Default Constructor.
	 */
	private LoanServiceDelegate() {
		super();
	}

	/**
	 * This method gets an instance of the object. This is the singleton
	 * pattern.
	 * 
	 * @return LoanServiceDelegate The singleton instance.
	 */
	public static LoanServiceDelegate getInstance() {
		if (loanServiceDelegate == null) {
			loanServiceDelegate = new LoanServiceDelegate();
		} // fi
		return loanServiceDelegate;
	}

	/**
	 * Calls the {@link LoanServiceUtil} getLocalHome method to find the local
	 * home.
	 * 
	 * @see com.manulife.pension.service.delegate.LocalServiceDelegate#getLocalHome()
	 * 
	 * @return Object - The local home.
	 */
	private Object getLocalHome() {
		try {
			return LoanServiceUtil.getLocalHome();
		} catch (NamingException namingException) {
			throw new NestableRuntimeException(namingException);
		}
	}

	/**
	 * Gets the service.
	 * 
	 * @return LoanService
	 * @throws SystemException
	 *             if there is an exception.
	 */
	private LoanService getLocalLoanService() throws SystemException {
		final LoanServiceLocalHome loanServiceLocalHome;
		loanServiceLocalHome = (LoanServiceLocalHome) getLocalHome();

		try {
			return loanServiceLocalHome.create();
		} catch (CreateException createException) {
			throw new SystemException(createException,
					"CreateException occured.");
		} // end try/catch
	}

	/**
	 * Gets the remote service.
	 * 
	 * @return LoanServiceRemote The remote interface for the service.
	 * @throws SystemException
	 *             if there is an exception.
	 */
	private LoanServiceRemote getLoanService() throws SystemException {
		return getLoanService(null);
	}

	/**
	 * Gets the remote service.
	 * 
	 * @param environment
	 *            Parameters to use for creating the initial context.
	 * @return LoanServiceRemote The remote interface for the service.
	 * @throws SystemException
	 *             if there is an exception.
	 */
	private LoanServiceRemote getLoanService(final Hashtable environment)
			throws SystemException {
		try {
			if (environment == null) {
				return LoanServiceUtil.getHome().create();
			} else {
				return LoanServiceUtil.getHome(environment).create();
			} // fi
		} catch (RemoteException remoteException) {
			throw new SystemException(remoteException,
					"RemoteException occured.");
		} catch (CreateException createException) {
			throw new SystemException(createException,
					"CreateException occured.");
		} catch (NamingException namingException) {
			throw new SystemException(namingException,
					"NamingException occured.");
		} // end try/catch
	}

	/**
	 * Cached local home (EJBLocalHome). Uses lazy loading to obtain its value
	 * (loaded by getLocalHome() methods).
	 */
	private static LoanServiceLocalHome cachedLocalHome = null;

	/**
	 * Cached remote home (EJBHome). Uses lazy loading to obtain its value
	 * (loaded by getHome() methods).
	 */
	private static LoanServiceHome cachedRemoteHome = null;

	private static Object lookupHome(final java.util.Hashtable environment,
			final String jndiName, final Class narrowTo)
			throws javax.naming.NamingException {
		// Obtain initial context
		javax.naming.InitialContext initialContext = new javax.naming.InitialContext(
				environment);
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
	 * @return Home interface for LoanService. Lookup using JNDI_NAME
	 */
	public static LoanServiceHome getPhysicalHome()
			throws javax.naming.NamingException {
		if (cachedRemoteHome == null) {
			cachedRemoteHome = (LoanServiceHome) lookupHome(
					null,
					com.manulife.pension.service.loan.LoanServiceHome.JNDI_NAME,
					com.manulife.pension.service.loan.LoanServiceHome.class);
		}
		return cachedRemoteHome;
	}

	/**
	 * Obtain local home interface from default initial context.
	 * 
	 * @return Local home interface for LoanService. Lookup using JNDI_NAME
	 */
	private static LoanServiceLocalHome getPhysicalLocalHome()
			throws javax.naming.NamingException {
		if (cachedLocalHome == null) {
			cachedLocalHome = (LoanServiceLocalHome) lookupHome(
					null,
					com.manulife.pension.service.loan.LoanServiceLocalHome.JNDI_NAME,
					com.manulife.pension.service.loan.LoanServiceLocalHome.class);
		}
		return cachedLocalHome;
	}

	/**
	 * Gets the service using the physical JNDI lookup. <b>NOTE: </b> This
	 * lookup for the service should only be used in places where you do not
	 * have access to the application server context. In most cases, you should
	 * use <code>getLoanService()</code>.
	 * 
	 * @return LoanService
	 * @throws SystemException
	 *             if there is an exception.
	 */
	private LoanService getLoanServiceWithPhysicalJndiName()
			throws SystemException {
		try {
			final LoanServiceLocalHome LoanServiceLocalHome = getPhysicalLocalHome();
			return LoanServiceLocalHome.create();
		} catch (NamingException namingException) {
			throw new SystemException(namingException,
					"NamingException occured.");
		} catch (CreateException createException) {
			throw new SystemException(createException,
					"CreateException occured.");
		} // end try/catch
	}

	/**
	 * Gets the remote service using the physical JNDI lookup. <b>NOTE: </b>
	 * This lookup for the service should only be used in places where you do
	 * not have access to the application server context. In most cases, you
	 * should use <code>getLoanService()</code>.
	 * 
	 * @return LoanServiceRemote
	 * @throws SystemException
	 *             if there is an exception.
	 */
	private LoanServiceRemote getLoanServiceRemoteWithPhysicalJndiName()
			throws SystemException {
		try {
			final LoanServiceHome loanServiceHome = getPhysicalHome();
			return loanServiceHome.create();
		} catch (NamingException namingException) {
			throw new SystemException(namingException,
					"NamingException occured.");
		} catch (CreateException createException) {
			throw new SystemException(createException,
					"CreateException occured.");
		} catch (RemoteException remoteException) {
			throw new SystemException(remoteException,
					"RemoteException occured.");
		} // end try/catch
	}

	/**
	 * Initiates a loan request. This method does not persist the loan request
	 * to the database.
	 * 
	 * @param participantProfileId
	 * @param contractId
	 * @param userProfileId
	 * @return
	 * @throws SystemException
	 */
	public Loan initiateLoan(Integer participantProfileId, Integer contractId,
			Integer userProfileId) throws SystemException {
		Loan loan = getLocalLoanService().initiate(participantProfileId, contractId,
				userProfileId);
		if (loan != null) {
			loan.setDataRetriever(new WebLoanSupportDataRetriever());
		}
		return loan;
	}

	/**
	 * Retrieves a list of loan request created by the given user profile ID.
	 * 
	 * @param userProfileId
	 * @param contractId
	 * @return
	 * @throws SystemException
	 */
	public List<Loan> getLoanRequestsByCreatedId(Integer userProfileId,
			Integer contractId) throws SystemException {
		List<Loan> loans = getLocalLoanService().getLoanRequestsByCreatedId(userProfileId,
				contractId);
		LoanSupportDataRetriever dataRetriever = new WebLoanSupportDataRetriever();
		for (Loan loan: loans) {
			loan.setDataRetriever(dataRetriever);
		}
		return loans;
	}

	/**
	 * Retrieves the last loan request for the given user profile ID regardless
	 * of its status.
	 * 
	 * @param userProfileId
	 * @param contractId
	 * @return
	 * @throws SystemException
	 */
	public Loan getLastLoanRequest(Integer userProfileId, Integer contractId)
			throws SystemException {
		Loan loan = getLocalLoanService().getLastLoanRequest(userProfileId,
				contractId);
		if (loan != null) {
			loan.setDataRetriever(new WebLoanSupportDataRetriever());
		}
		return loan;
	}

	/**
	 * Saves a loan request. Do not use this method for state transition because
	 * state transition logic will not be performed.
	 * 
	 * @param loan
	 * @return
	 * @throws SystemException
	 */
	public Loan save(Loan loan) throws SystemException {
		Loan returnLoan = getLocalLoanService().save(loan);
		if (returnLoan != null) {
			returnLoan.setDataRetriever(new WebLoanSupportDataRetriever());
		}
		return returnLoan;
	}

	/**
	 * Saves a loan document and prepare it for the Print Loan Document
	 * function. The Print Loan Document function performs additional validation
	 * checks.
	 * 
	 * @param loan
	 * @return
	 * @throws SystemException
	 */
	public Loan printLoanDocument(Loan loan) throws SystemException {
		Loan returnLoan = getLocalLoanService().printLoanDocument(loan);
		if (returnLoan != null) {
			returnLoan.setDataRetriever(new WebLoanSupportDataRetriever());
		}
		return returnLoan;
	}
	
	/**
	 * Saves a loan document and prepare it for the Print Loan Document Review
	 * function. The Print Loan Document function performs additional validation
	 * checks.
	 * 
	 * @param loan
	 * @return
	 * @throws SystemException
	 */
	public Loan printLoanDocumentReview(Loan loan) throws SystemException {
		Loan returnLoan = getLocalLoanService().printLoanDocumentReview(loan);
		if (returnLoan != null) {
			returnLoan.setDataRetriever(new WebLoanSupportDataRetriever());
		}
		return returnLoan;
	}

	/**
	 * Retrieves a loan request and populates its dependent objects.
	 * 
	 * @param userProfileId
	 * @param contractId
	 * @param submissionId
	 * @return
	 * @throws SystemException
	 */
	public Loan read(Integer userProfileId, Integer contractId,
			Integer submissionId) throws SystemException {
		Loan returnLoan = getLocalLoanService().read(userProfileId, contractId,
				submissionId);
		if (returnLoan != null) {
			returnLoan.setDataRetriever(new WebLoanSupportDataRetriever());
		}
		return returnLoan;
	}

	/**
	 * Retrieves all activities related to the given loan request.
	 * 
	 * @param userProfileId
	 * @param contractId
	 * @param submissionId
	 * @param participantUserProfileId
	 * @param participantFirstName
	 * @param participantLastName
	 * @return
	 * @throws SystemException
	 */
	public LoanActivities readActivities(Integer userProfileId,
			Integer contractId, String submissionId,
			Integer participantUserProfileId, String participantFirstName,
			String participantLastName) throws SystemException {
		return getLocalLoanService().readActivities(userProfileId, contractId,
				submissionId, participantUserProfileId, participantFirstName,
				participantLastName);
	}

	/**
	 * Approves a loan request.
	 * 
	 * @param loan
	 * @return
	 * @throws SystemException
	 */
	public Loan approve(Loan loan) throws SystemException {
		Loan returnLoan = getLocalLoanService().approve(loan);
		if (returnLoan != null) {
			returnLoan.setDataRetriever(new WebLoanSupportDataRetriever());
		}
		return returnLoan;
	}

	/**
	 * Validates a loan request prior to approval. The Loan approval screen
	 * requires two steps. The first step is to make sure the loan request is
	 * ready for approval (validateApprove), the second step is the actual state
	 * transition. The same validation logics will be used for both steps.
	 * 
	 * @param loan
	 * @return
	 * @throws SystemException
	 */
	public Loan validateApprove(Loan loan) throws SystemException {
		Loan returnLoan = getLocalLoanService().validateApprove(loan);
		if (returnLoan != null) {
			returnLoan.setDataRetriever(new WebLoanSupportDataRetriever());
		}
		return returnLoan;
	}

	/**
	 * Expires a loan request.
	 * 
	 * @param loan
	 * @return
	 * @throws SystemException
	 */
	public Loan expire(Loan loan) throws SystemException {
		Loan returnLoan = getLocalLoanService().expire(loan);
		if (returnLoan != null) {
			returnLoan.setDataRetriever(new WebLoanSupportDataRetriever());
		}
		return returnLoan;
	}

	/**
	 * Completes a loan request.
	 * 
	 * @param loan
	 * @return
	 * @throws SystemException
	 */
	public Loan complete(Loan loan) throws SystemException {
		Loan returnLoan = getLocalLoanService().complete(loan);
		if (returnLoan != null) {
			returnLoan.setDataRetriever(new WebLoanSupportDataRetriever());
		}
		return returnLoan;
	}

	/**
	 * Administrator declines the loan request.
	 * 
	 * @param loan
	 * @return
	 * @throws SystemException
	 */
	public Loan decline(Loan loan) throws SystemException {
		Loan returnLoan = getLocalLoanService().decline(loan);
		if (returnLoan != null) {
			returnLoan.setDataRetriever(new WebLoanSupportDataRetriever());
		}
		return returnLoan;
	}

	/**
	 * Moves the loan request to the Loan Package state. A loan package document
	 * (PDF) can only be generated for loan requests that are in the Loan
	 * Package state.
	 * 
	 * @param loan
	 * @return
	 * @throws SystemException
	 */
	public Loan loanPackage(Loan loan) throws SystemException {
		Loan returnLoan = getLocalLoanService().loanPackage(loan);
		if (returnLoan != null) {
			returnLoan.setDataRetriever(new WebLoanSupportDataRetriever());
		}
		return returnLoan;
	}

	/**
	 * Sends a loan request for acceptance.
	 * 
	 * @param loan
	 * @return
	 * @throws SystemException
	 */
	public Loan sendForAcceptance(Loan loan) throws SystemException {
		Loan returnLoan = getLocalLoanService().sendForAcceptance(loan);
		if (returnLoan != null) {
			returnLoan.setDataRetriever(new WebLoanSupportDataRetriever());
		}
		return returnLoan;
	}

	/**
	 * Sends the loan request for approval.
	 * 
	 * @param loan
	 * @return
	 * @throws SystemException
	 */
	public Loan sendForApproval(Loan loan) throws SystemException {
		Loan returnLoan = getLocalLoanService().sendForApproval(loan);
		if (returnLoan != null) {
			returnLoan.setDataRetriever(new WebLoanSupportDataRetriever());
		}
		return returnLoan;
	}

	/**
	 * Sends a loan request for review.
	 * 
	 * @param loan
	 * @return
	 * @throws SystemException
	 */
	public Loan sendForReview(Loan loan) throws SystemException {
		Loan returnLoan = getLocalLoanService().sendForReview(loan);
		if (returnLoan != null) {
			returnLoan.setDataRetriever(new WebLoanSupportDataRetriever());
		}
		return returnLoan;
	}

	/**
	 * Deletes a loan request. This is a logical deletion.
	 * 
	 * @param loan
	 * @return
	 * @throws SystemException
	 */
	public Loan delete(Loan loan) throws SystemException {
		Loan returnLoan = getLocalLoanService().delete(loan);
		if (returnLoan != null) {
			returnLoan.setDataRetriever(new WebLoanSupportDataRetriever());
		}
		return returnLoan;
	}

	/**
	 * Participant rejects a loan request.
	 * 
	 * @param loan
	 * @return
	 * @throws SystemException
	 */
	public Loan reject(Loan loan) throws SystemException {
		Loan returnLoan = getLocalLoanService().reject(loan);
		if (returnLoan != null) {
			returnLoan.setDataRetriever(new WebLoanSupportDataRetriever());
		}
		return returnLoan;
	}

	/**
	 * Retrieves plan data related to making a loan.
	 * 
	 * @param contractId
	 * @return
	 * @throws SystemException
	 */
	public LoanPlanData getLoanPlanData(Integer contractId)
			throws SystemException {
		return getLocalLoanService().getLoanPlanData(contractId);
	}

	/**
	 * Retrieves loan related settings.
	 * 
	 * @param contractId
	 * @return
	 * @throws SystemException
	 */
	public LoanSettings getLoanSettings(Integer contractId)
			throws SystemException {
		return getLocalLoanService().getLoanSettings(contractId);
	}

	/**
	 * Retrieves participant data related to making a loan.
	 * 
	 * @param contractId
	 * @param participantProfileId
	 * @return
	 * @throws SystemException
	 */
	public LoanParticipantData getLoanParticipantData(Integer contractId,
			Integer participantProfileId) throws SystemException {
		return getLocalLoanService().getLoanParticipantData(contractId,
				participantProfileId);
	}

	/**
	 * Retrieves a participant's money types and their account balances for the
	 * purpose of loan calculation.
	 * 
	 * @param contractId
	 * @param participantProfileId
	 * @return
	 * @throws SystemException
	 */
	public Pair<List<LoanMoneyType>, EmployeeVestingInformation> getParticipantMoneyTypesForLoans(
			Integer contractId, Integer participantProfileId)
			throws SystemException {
		return getLocalLoanService().getParticipantMoneyTypesForLoans(
				contractId, participantProfileId);
	}

	/**
	 * Expires any loan requests which has expiry date before checkDate.
	 * 
	 * @param checkDate
	 *            The date to be checked.
	 * @param profileId
	 *            The user profile ID who initiates the expiration request.
	 * @throws SystemException
	 */
	public void markExpiredLoans(java.util.Date checkDate,
			java.lang.Integer profileId) throws SystemException {
		try {
			getLoanServiceRemoteWithPhysicalJndiName().markExpiredLoans(
					checkDate, profileId);
		} catch (RemoteException remoteException) {
			throw ExceptionHandlerUtility.wrap(new SystemException(
					remoteException,
					"Could not retrieve the list of expiring Requests "));
		}
	}

	/**
	 * Returns a list of loan requests that will be expired in the given number
	 * of business dates.
	 * 
	 * @param numberOfBusinessDaysBeforeExpiryDate
	 * @return
	 * @throws SystemException
	 */
	public List getAboutToExpireLoanRequests(
			Integer numberOfBusinessDaysBeforeExpiryDate)
			throws SystemException {
		try {
			return getLoanServiceRemoteWithPhysicalJndiName()
					.getAboutToExpireLoanRequests(
							numberOfBusinessDaysBeforeExpiryDate);
		} catch (RemoteException remoteException) {
			throw ExceptionHandlerUtility.wrap(new SystemException(
					remoteException,
					"Could not retrieve the list of expiring Requests "));
		}
	}

	/**
	 * gets outstanding old ILoan Requests
	 * 
	 * @param contractId
	 * @return Integer - count of requests
	 * @throws SystemException
	 */
	public Integer getOutstandingOldILoanRequestCount(int contractId)
			throws SystemException {
		return getLocalLoanService().getOutstandingOldILoanRequestCount(
				contractId);
	}

	/**
	 * get LRK01 and Allow Online Loans CSF information for a given list of
	 * contracts
	 * 
	 * @param contractIdList
	 * @return Map of Contract Id & its specific LoanSettings
	 * @throws SystemException
	 */
	public Map<Integer, ArrayList<LoanSettings>> getPartialLoanSettingsData(
			Integer[] contractIdList) throws SystemException {
		return getLocalLoanService().getPartialLoanSettingsData(contractIdList);
	}

	/**
	 * get if the contract has Loan Record Keeping Product Feature
	 * 
	 * @param contractId
	 * @return
	 * @throws LoanDaoException
	 */
	public boolean hasLoanRecordKeepingProductFeature(int contractId)
			throws SystemException {
		return getLocalLoanService().hasLoanRecordKeepingProductFeature(
				contractId);
	}

	/**
	 * get if the contract has Loan Record Keeping Product Feature
	 * 
	 * Retrieves a the outstanding loan for the the given ppt profileID,
	 * contractId, and loan id.
	 * 
	 * @param contractId
	 *            Contract Id of the ppt
	 * @param profileId
	 *            ProfileId of the ppt
	 * @param loanId
	 *            Loan Id
	 * @return Outstanding Loan object
	 * 
	 * @throws LoanDaoException
	 */
	public OutstandingLoan getOutstandingLoan(Integer contractId,
			Long profileId, Integer loanId) throws SystemException {
		return getLocalLoanService().getOutstandingLoan(contractId, profileId,
				loanId);
	}
	
	/**Retrieves UOL count for the given contract id.
	 * @param contractId
	 * @return
	 * @throws SystemException
	 */
	public Integer getUOLCount(Integer contractId)throws SystemException{
		return getLocalLoanService().getUOLCount(contractId);				
	}
	
	/**
	 * Returns true if there exists of given Loan status code in status history.
	 * 
	 * @param submissionId
	 * @param statusCode
	 * @return
	 * @throws LoanDaoException
	 */
	public boolean checkLoanStatusExists(Integer submissionId, String statusCode)
			throws SystemException {
		return getLocalLoanService().checkLoanStatusExists(submissionId, statusCode);
	}
	
	/**
	 * Retrieves  ManagedContent from  MANAGED_CONTENT_REFERENCE table
	 * 
	 * @param atRiskDetils
	 * @return AtRiskPasswordResetVO
	 * @throws SystemException
	 */
	public List<ManagedContent> getManagedContent(Integer submissionId, Integer contractId, Integer userProfileId)
	throws SystemException {
		return getLocalLoanService().getManagedContent(submissionId, contractId, userProfileId);
	}
	
}
