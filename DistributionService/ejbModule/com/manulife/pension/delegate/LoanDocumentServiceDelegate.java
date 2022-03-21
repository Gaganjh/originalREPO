package com.manulife.pension.delegate;

import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.log4j.Logger;

import com.manulife.pension.content.view.ContentText;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.loan.LoanDocumentService;
import com.manulife.pension.service.loan.LoanDocumentServiceHome;
import com.manulife.pension.service.loan.LoanDocumentServiceLocalHome;
import com.manulife.pension.service.loan.LoanDocumentServiceRemote;
import com.manulife.pension.service.loan.LoanDocumentServiceUtil;
import com.manulife.pension.service.loan.valueobject.Loan;

/**
 * LoanServiceDelegate delegates requests to the EJB from the web tier.
 */
public final class LoanDocumentServiceDelegate {

	private static LoanDocumentServiceDelegate loanDocumentServiceDelegate;

	private static final Logger logger = Logger
			.getLogger(LoanDocumentServiceDelegate.class);

	/**
	 * Default Constructor.
	 */
	private LoanDocumentServiceDelegate() {
		super();
	}

	/**
	 * This method gets an instance of the object. This is the singleton
	 * pattern.
	 * 
	 * @return LoanDocumentServiceDelegate The singleton instance.
	 */
	public static LoanDocumentServiceDelegate getInstance() {
		if (loanDocumentServiceDelegate == null) {
			loanDocumentServiceDelegate = new LoanDocumentServiceDelegate();
		} // fi
		return loanDocumentServiceDelegate;
	}

	/**
	 * Calls the {@link LoanDocumentServiceUtil} getLocalHome method to find the
	 * local home.
	 * 
	 * @see com.manulife.pension.service.delegate.LocalServiceDelegate#getLocalHome()
	 * 
	 * @return Object - The local home.
	 */
	private Object getLocalHome() {
		try {
			return LoanDocumentServiceUtil.getLocalHome();
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
	private LoanDocumentService getLocalLoanDocumentService()
			throws SystemException {
		final LoanDocumentServiceLocalHome loanServiceLocalHome;
		loanServiceLocalHome = (LoanDocumentServiceLocalHome) getLocalHome();

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
	 * @param environment
	 *            Parameters to use for creating the initial context.
	 * @return LoanDocumentServiceRemote The remote interface for the service.
	 * @throws SystemException
	 *             if there is an exception.
	 */
	private LoanDocumentServiceRemote getLoanDocumentService(
			final Hashtable environment) throws SystemException {
		try {
			if (environment == null) {
				return LoanDocumentServiceUtil.getHome().create();
			} else {
				return LoanDocumentServiceUtil.getHome(environment).create();
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
	private static LoanDocumentServiceLocalHome cachedLocalHome = null;

	/**
	 * Cached remote home (EJBHome). Uses lazy loading to obtain its value
	 * (loaded by getHome() methods).
	 */
	private static LoanDocumentServiceHome cachedRemoteHome = null;

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
	 * @return Home interface for LoanDocumentService. Lookup using JNDI_NAME
	 */
	public static LoanDocumentServiceHome getPhysicalHome()
			throws javax.naming.NamingException {
		if (cachedRemoteHome == null) {
			cachedRemoteHome = (LoanDocumentServiceHome) lookupHome(
					null,
					com.manulife.pension.service.loan.LoanDocumentServiceHome.JNDI_NAME,
					com.manulife.pension.service.loan.LoanDocumentServiceHome.class);
		}
		return cachedRemoteHome;
	}

	/**
	 * Obtain local home interface from default initial context.
	 * 
	 * @return Local home interface for LoanDocumentService. Lookup using
	 *         JNDI_NAME
	 */
	private static LoanDocumentServiceLocalHome getPhysicalLocalHome()
			throws javax.naming.NamingException {
		if (cachedLocalHome == null) {
			cachedLocalHome = (LoanDocumentServiceLocalHome) lookupHome(
					null,
					com.manulife.pension.service.loan.LoanDocumentServiceLocalHome.JNDI_NAME,
					com.manulife.pension.service.loan.LoanDocumentServiceLocalHome.class);
		}
		return cachedLocalHome;
	}

	/**
	 * Gets the service using the physical JNDI lookup. <b>NOTE: </b> This
	 * lookup for the service should only be used in places where you do not
	 * have access to the application server context. In most cases, you should
	 * use <code>getLoanDocumentService()</code>.
	 * 
	 * @return LoanDocumentService
	 * @throws SystemException
	 *             if there is an exception.
	 */
	private LoanDocumentService getLoanDocumentServiceWithPhysicalJndiName()
			throws SystemException {
		try {
			final LoanDocumentServiceLocalHome LoanDocumentServiceLocalHome = getPhysicalLocalHome();
			return LoanDocumentServiceLocalHome.create();
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
	 * should use <code>getLoanDocumentService()</code>.
	 * 
	 * @return LoanDocumentServiceRemote
	 * @throws SystemException
	 *             if there is an exception.
	 */
	private LoanDocumentServiceRemote getLoanDocumentServiceRemoteWithPhysicalJndiName()
			throws SystemException {
		try {
			final LoanDocumentServiceHome loanServiceHome = getPhysicalHome();
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

	public String getTruthInLendingNoticeHtml(Integer userProfileId,
			Integer contractId, Integer submissionId) throws SystemException {
		return getLocalLoanDocumentService().getTruthInLendingNoticeHtml(
				userProfileId, contractId, submissionId);
	}

	public String getPromissoryNoteAndIrrevocablePledgeHtml(final Loan loan)
			throws SystemException {
		return getLocalLoanDocumentService()
				.getPromissoryNoteAndIrrevocablePledgeHtml(loan);
	}

	public String getTruthInLendingNoticeHtml(final Loan loan)
			throws SystemException {
		return getLocalLoanDocumentService().getTruthInLendingNoticeHtml(loan);
	}

	public String getPromissoryNoteAndIrrevocablePledgeHtml(
			Integer userProfileId, Integer contractId, Integer submissionId)
			throws SystemException {
		return getLocalLoanDocumentService()
				.getPromissoryNoteAndIrrevocablePledgeHtml(userProfileId,
						contractId, submissionId);
	}

	public String getAmortizationScheduleHtml(Integer userProfileId,
			Integer contractId, Integer submissionId) throws SystemException {
		return getLocalLoanDocumentService().getAmortizationScheduleHtml(
				userProfileId, contractId, submissionId);
	}

	public String getAmortizationScheduleHtml(final Loan loan)
			throws SystemException {
		return getLocalLoanDocumentService().getAmortizationScheduleHtml(loan);
	}

	public byte[] getLoanDocuments(final Integer userProfileId,
			final Integer contractId, final Integer submissionId, 
			final boolean useEffectiveDateFromDB)
			throws SystemException {
		return getLocalLoanDocumentService().getLoanDocuments(userProfileId,
				contractId, submissionId, useEffectiveDateFromDB);
	}

	public byte[] getLoanPackage(final Integer userProfileId,
			final Integer contractId, final Integer submissionId)
			throws SystemException {
		return getLocalLoanDocumentService().getLoanPackage(userProfileId,
				contractId, submissionId);
	}

	public ContentText getContentTextByKey(int contentKey)
			throws SystemException {
		return getLocalLoanDocumentService().getContentTextByKey(contentKey);
	}

	public ContentText getContentTextById(int contentId)
			throws SystemException {
		return getLocalLoanDocumentService().getContentTextById(contentId);
	}
}
