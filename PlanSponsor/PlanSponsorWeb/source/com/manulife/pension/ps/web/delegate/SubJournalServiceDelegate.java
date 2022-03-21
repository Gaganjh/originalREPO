package com.manulife.pension.ps.web.delegate;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.lp.model.submission.journal.GFTSubmissionDetailsRequest;
import com.manulife.pension.lp.model.submission.journal.GFTSubmissionDetailsResponse;
import com.manulife.pension.lp.model.submission.journal.GFTSubmissionSummaryRequest;
import com.manulife.pension.lp.model.submission.journal.GFTSubmissionSummaryResponse;
import com.manulife.pension.lp.model.submission.journal.RequestValidationException;
import com.manulife.pension.lp.model.submission.journal.SubmissionJournalService;
import com.manulife.pension.lp.model.submission.journal.SubmissionJournalSystemException;
import com.manulife.pension.platform.web.delegate.AbstractRMIServiceDelegate;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.delegate.exception.UnableToAccessIFileException;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.log.LogUtility;

/**
 * @author drotele
 * This class uses RMI connection to perform i:file operations
 * File history etc.
 *
 */
public class SubJournalServiceDelegate extends AbstractRMIServiceDelegate {

	private final static String RMI_SERVICE_NAME = "SubmissionJournalService";
	private static final Logger logger = Logger.getLogger(SubJournalServiceDelegate.class);
	private static final String SECURED_SUB_JOURNAL_SERVICE_PORT = "SecuredSubJournalServicePort";
	private static SubJournalServiceDelegate instance =
		new SubJournalServiceDelegate();

	/**
	 * constructor
	 */
	public SubJournalServiceDelegate() {
	}

	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.delegate.AbstractRMIServiceDelegate#getServiceName()
	 */
	protected String getServiceName() {
		return RMI_SERVICE_NAME;
	}
	/*
	 * This method is used to get the Secured Sub Journal Service  Port Number 
	 */
	protected int getPortNumber() {
	   String portNumber = new BaseEnvironment().getNamingVariable(SECURED_SUB_JOURNAL_SERVICE_PORT, null);
	   return Integer.parseInt(portNumber); 
   }

	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.delegate.AbstractRMIServiceDelegate#getRmiServer()
	 */
	/**
	 * overrige the RMI server name that could be different for the
	 * i:file server
	 * @return
	 */
	protected String getRmiServer() {
		if (this.rmiServer == null) {
			this.rmiServer = Environment.getInstance().getRMISubmissionJournalServerName();
		}

		return rmiServer;
	}

	/**
	 * @return
	 */
	public static SubJournalServiceDelegate getInstance() {
		return instance;
	}

	/**
	 * Service Delegate method to retrieve the i:file History
	 *
	 * @param gftsubmissionsummaryrequest
	 * @return GFTSubmissionSummaryResponse
	 * @throws SystemException
	 */
	public GFTSubmissionSummaryResponse getFileHistory(GFTSubmissionSummaryRequest gftsubmissionsummaryrequest)
		throws SystemException, UnableToAccessIFileException {
	if (logger.isDebugEnabled() )
			logger.debug("entry -> getFileHistory");
		GFTSubmissionSummaryResponse list = null;
		try {
			SubmissionJournalService journalService =
				(SubmissionJournalService) getRemoteService();
			list =
				journalService.getSubmissionSummaryRequest(
					gftsubmissionsummaryrequest);

		} catch (RemoteException re) {
			SystemException se =
				new SystemException(
					re,
					SubJournalServiceDelegate.class.getName(),
					"getFileHistory",
					null);
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
			throw new UnableToAccessIFileException(
					SubJournalServiceDelegate.class.getName(),
					"checkData",
					"unable to checkData for contractId[" + gftsubmissionsummaryrequest.getContractNumber() + "] "
							+ " as SubmissionJournalService cannot be accessed");
		} catch (SubmissionJournalSystemException sjse) {
			SystemException se =
				new SystemException(
					sjse,
					SubJournalServiceDelegate.class.getName(),
					"getFileHistory",
					null);
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
			throw new UnableToAccessIFileException(
					SubJournalServiceDelegate.class.getName(),
					"checkData",
					"unable to checkData for contractId[" + gftsubmissionsummaryrequest.getContractNumber() + "] "
							+ " as SubmissionJournalService cannot be accessed");
		} catch (RequestValidationException rve) {
			SystemException se =
				new SystemException(
					rve,
					SubJournalServiceDelegate.class.getName(),
					"getFileHistory",
					null);
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
			throw se;
		}

			if (logger.isDebugEnabled() )
			logger.debug("exit <- getFileHistory");
		return list;
	}

	/**
	 * Service Delegate method to retrieve the i:file History Details
	 *
	 * @param gftsubmissiondetailsrequest
	 * @return
	 * @throws SystemException
	 */
	public GFTSubmissionDetailsResponse getFileHistoryDetails(GFTSubmissionDetailsRequest gftsubmissiondetailsrequest)
		throws SystemException, UnableToAccessIFileException {
	if (logger.isDebugEnabled() )
			logger.debug("entry -> getFileHistoryDetails");
		GFTSubmissionDetailsResponse details = null;
		try {
			SubmissionJournalService journalService =
				(SubmissionJournalService) getRemoteService();
			details =
				journalService.getSubmissionDetailsRequest(
					gftsubmissiondetailsrequest);

		} catch (RemoteException re) {
			SystemException se =
				new SystemException(
					re,
					FileUploadServiceDelegate.class.getName(),
					"getFileHistoryDetails",
					null);
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
			throw se;
		} catch (SubmissionJournalSystemException sjse) {
			SystemException se =
				new SystemException(
					sjse,
					FileUploadServiceDelegate.class.getName(),
					"getFileHistoryDetails",
					null);
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
			throw se;
		} catch (RequestValidationException rve) {
			SystemException se =
				new SystemException(
					rve,
					FileUploadServiceDelegate.class.getName(),
					"getFileHistoryDetails",
					null);
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
			throw se;
		}

			if (logger.isDebugEnabled() )
			logger.debug("exit <- getFileHistoryDetails");
		return details;
	}

}
