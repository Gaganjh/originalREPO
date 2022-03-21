package com.manulife.pension.service.loan;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.dao.DaoConstants;
import com.manulife.pension.service.distribution.dao.ActivityDetailDao;
import com.manulife.pension.service.distribution.dao.ActivityDynamicDetailDao;
import com.manulife.pension.service.distribution.dao.ActivitySummaryDao;
import com.manulife.pension.service.distribution.dao.ManagedContentDao;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.log.DistributionEventEnum;
import com.manulife.pension.service.distribution.valueobject.ActivitySummary;
import com.manulife.pension.service.distribution.valueobject.ManagedContent;
import com.manulife.pension.service.loan.dao.LoanDao;
import com.manulife.pension.service.loan.dao.LoanParameterDao;
import com.manulife.pension.service.loan.dao.LoanSupportDao;
import com.manulife.pension.service.loan.domain.LoanState;
import com.manulife.pension.service.loan.domain.LoanStateContext;
import com.manulife.pension.service.loan.domain.LoanStateEnum;
import com.manulife.pension.service.loan.domain.LoanStateFactory;
import com.manulife.pension.service.loan.domain.LoanValidationHelper;
import com.manulife.pension.service.loan.event.EventFactory;
import com.manulife.pension.service.loan.event.LoanEventGenerator;
import com.manulife.pension.service.loan.exception.LoanDaoException;
import com.manulife.pension.service.loan.exception.LoanValidationException;
import com.manulife.pension.service.loan.log.LoanEventLog;
import com.manulife.pension.service.loan.util.LoanDataHelper;
import com.manulife.pension.service.loan.util.LoanObjectFactory;
import com.manulife.pension.service.loan.valueobject.EjbLoanSupportDataRetriever;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanActivities;
import com.manulife.pension.service.loan.valueobject.LoanActivityDetail;
import com.manulife.pension.service.loan.valueobject.LoanActivitySummary;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.loan.valueobject.OutstandingLoan;
import com.manulife.pension.util.Pair;
import com.manulife.pension.util.log.EventLog;
import com.manulife.pension.util.log.EventLoggingHelper;

/**
 * This EJB provides services to fulfill the main loan request business
 * requirements. It employs the same State transition design pattern as what the
 * Withdrawals code has been using. The major difference between the two is in
 * where validation logic resides. In Withdrawals, most of the validations are
 * performed on the client side (Actions and JSP). In Loans, however, only data
 * type related validations are done on the client side. The rest of the
 * validations are done on the server side. This allows us to reuse the same
 * validations for both the PlanSponsor website and the EZk website.
 * 
 * @ejb.bean name="LoanService" display-name="Loan Service" type="Stateless"
 *           view-type="both" transaction-type="Container"
 *           jndi-name="com.manulife.pension.service.loan.LoanServiceHome"
 *           local-jndi-name="com.manulife.pension.service.loan.LoanServiceLocalHome"
 * 
 * @ejb.interface generate="local,remote"
 * 
 * @ejb.transaction type="Required"
 * 
 * @ejb.util generate="logical"
 * 
 * @websphere.bean
 */
public class LoanServiceBean implements javax.ejb.SessionBean {

	/**
	 * Default serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger
			.getLogger(LoanServiceBean.class);

	private javax.ejb.SessionContext mySessionCtx;

	private LoanDao loanDao = null;

	private LoanSupportDao loanSupportDao = null;

	private ActivityDetailDao detailDao = null;

	private ActivityDynamicDetailDao dynamicDetailDao = null;

	private ActivitySummaryDao summaryDao = null;
	
	private LoanParameterDao loanParameterDao = null;

	/**
	 * {@inheritDoc}
	 */
	public void setSessionContext(final javax.ejb.SessionContext ctx) {
		mySessionCtx = ctx;
		loanDao = new LoanDao();
		detailDao = new ActivityDetailDao();
		dynamicDetailDao = new ActivityDynamicDetailDao();
		summaryDao = new ActivitySummaryDao();
		loanParameterDao = new LoanParameterDao();

		try {
			loanSupportDao = new LoanSupportDao(
					BaseDatabaseDAO
							.getDataSource(DaoConstants.DataSourceJndiName.CUSTOMER_SERVICE));
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void ejbActivate() {
	}

	/**
	 * ejbCreate is called as part of the EJB lifecycle.
	 * 
	 * @throws javax.ejb.CreateException
	 *             If an create exception occurs.
	 */
	public void ejbCreate() throws javax.ejb.CreateException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void ejbPassivate() {
		loanDao = null;
		loanSupportDao = null;
		detailDao = null;
		dynamicDetailDao = null;
		summaryDao = null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void ejbRemove() {
		loanDao = null;
		loanSupportDao = null;
		detailDao = null;
		dynamicDetailDao = null;
		summaryDao = null;
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
	 * Initiates a loan request. This method does not persist the loan request
	 * to the database.
	 * 
	 * @param profileId
	 *            the employee profile id
	 * @param contractId
	 *            the employee contract id
	 * @param principal
	 *            the user principal
	 * @return Loan The initial data object. If an exception occurs.
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public Loan initiate(final Integer participantProfileId,
			final Integer contractId, final Integer userProfileId) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> initiateLoan");
		}
		try {
			/**
			 * User can only initiate a DRAFT loan request. So, the logic
			 * resides in the DRAFT state object.
			 */
			LoanState state = LoanStateFactory.getState(LoanStateEnum.DRAFT);
			return state.initiate(participantProfileId, contractId,
					userProfileId);
		} catch (DistributionServiceException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	/**
	 * Saves a loan request. Do not use this method for state transition because
	 * state transition logic will not be performed.
	 * 
	 * @param loan
	 *            The loan object to save.
	 * @return a Loan object.
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public Loan save(final Loan inputLoan) {
		try {
			inputLoan.setDataRetriever(new EjbLoanSupportDataRetriever());
			if (inputLoan.getParticipantId() == null) {
				Integer participantId = loanSupportDao
						.getParticipantIdByProfileId(inputLoan
								.getParticipantProfileId());
				inputLoan.setParticipantId(participantId);
			}
			return LoanStateFactory.getState(inputLoan).saveAndExit(inputLoan);
		} catch (DistributionServiceException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	/**
	 * Saves a loan document and prepare it for the Print Loan Document
	 * function. The Print Loan Document function performs additional validation
	 * checks.
	 * 
	 * @param loan
	 *            The loan object to save.
	 * @return a Loan object.
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public Loan printLoanDocument(final Loan inputLoan) {
		try {
			inputLoan.setDataRetriever(new EjbLoanSupportDataRetriever());
			if (inputLoan.getParticipantId() == null) {
				Integer participantId = loanSupportDao
						.getParticipantIdByProfileId(inputLoan
								.getParticipantProfileId());
				inputLoan.setParticipantId(participantId);
			}
			return LoanStateFactory.getState(inputLoan).printLoanDocument(
					inputLoan);
		} catch (DistributionServiceException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}
	
	/**
	 * Saves a loan document and prepare it for the Print Loan Document Review
	 * function. The Print Loan Document function performs additional validation
	 * checks.
	 * 
	 * @param loan
	 *            The loan object to save.
	 * @return a Loan object.
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public Loan printLoanDocumentReview(final Loan inputLoan) {
		try {
			inputLoan.setDataRetriever(new EjbLoanSupportDataRetriever());
			if (inputLoan.getParticipantId() == null) {
				Integer participantId = loanSupportDao
						.getParticipantIdByProfileId(inputLoan
								.getParticipantProfileId());
				inputLoan.setParticipantId(participantId);
			}
			return LoanStateFactory.getState(inputLoan).printLoanDocumentReview(
					inputLoan);
		} catch (DistributionServiceException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	/**
	 * Sends a loan request for review.
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public Loan sendForReview(final Loan loan) {
		try {
			loan.setDataRetriever(new EjbLoanSupportDataRetriever());
			if (loan.getParticipantId() == null) {
				Integer participantId = loanSupportDao
						.getParticipantIdByProfileId(loan
								.getParticipantProfileId());
				loan.setParticipantId(participantId);
			}
			return LoanStateFactory.getState(loan).sendForReview(loan);
		} catch (DistributionServiceException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	/**
	 * Sends a loan request for acceptance.
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public Loan sendForAcceptance(Loan loan) {
		try {
			loan.setDataRetriever(new EjbLoanSupportDataRetriever());
			return LoanStateFactory.getState(loan).sendForAcceptance(loan);
		} catch (DistributionServiceException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	/**
	 * Administrator declines the loan request.
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public Loan decline(Loan loan) {
		try {
			loan.setDataRetriever(new EjbLoanSupportDataRetriever());
			return LoanStateFactory.getState(loan).decline(loan);
		} catch (DistributionServiceException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	/**
	 * Moves the loan request to the Loan Package state. A loan package document
	 * (PDF) can only be generated for loan requests that are in the Loan
	 * Package state.
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public Loan loanPackage(Loan loan) {
		try {
			loan.setDataRetriever(new EjbLoanSupportDataRetriever());
			return LoanStateFactory.getState(loan).loanPackage(loan);
		} catch (DistributionServiceException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	/**
	 * Sends the loan request for approval.
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public Loan sendForApproval(Loan loan) {
		try {
			loan.setDataRetriever(new EjbLoanSupportDataRetriever());
			return LoanStateFactory.getState(loan).sendForApproval(loan);
		} catch (DistributionServiceException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	/**
	 * Approves a loan request.
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public Loan approve(Loan loan) {
		try {
			if (!LoanValidationHelper.validateIsUserRoleAllowedToApprove(loan.getUserRole())) {
			    throw new LoanValidationException("Admin Contact is not allowed to approve loan.  "
			            + "Contract: " + loan.getContractId() 
			            + "profile id:" + loan.getLoginUserProfileId());
			}
	        loan.setDataRetriever(new EjbLoanSupportDataRetriever());
			return LoanStateFactory.getState(loan).approve(loan);
		} catch (DistributionServiceException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	/**
	 * Validates a loan request prior to approval. The Loan approval screen
	 * requires two steps. The first step is to make sure the loan request is
	 * ready for approval (validateApprove), the second step is the actual state
	 * transition. The same validation logics will be used for both steps.
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public Loan validateApprove(Loan loan) {
		try {
			loan.setDataRetriever(new EjbLoanSupportDataRetriever());
			LoanStateEnum fromState = LoanStateEnum.fromStatusCode(loan
					.getStatus());
			LoanStateContext context = new LoanStateContext(loan);
			LoanStateFactory.getState(LoanStateEnum.APPROVED).validate(
					fromState, LoanStateEnum.APPROVED, context);
			return loan;
		} catch (DistributionServiceException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	/**
	 * Expires a loan request.
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public Loan expire(Loan loan) {
		try {
			loan.setDataRetriever(new EjbLoanSupportDataRetriever());
			return LoanStateFactory.getState(loan).expire(loan);
		} catch (DistributionServiceException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	/**
	 * Completes a loan request.
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public Loan complete(Loan loan) {
		try {
			loan.setDataRetriever(new EjbLoanSupportDataRetriever());
			return LoanStateFactory.getState(loan).complete(loan);
		} catch (DistributionServiceException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	/**
	 * Participant rejects a loan request.
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public Loan reject(Loan loan) {
		try {
			loan.setDataRetriever(new EjbLoanSupportDataRetriever());
			return LoanStateFactory.getState(loan).reject(loan);
		} catch (DistributionServiceException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	/**
	 * Deletes a loan request. This is a logical deletion.
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public Loan delete(Loan loan) {
		try {
			loan.setDataRetriever(new EjbLoanSupportDataRetriever());
			return LoanStateFactory.getState(loan).delete(loan);
		} catch (DistributionServiceException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	/**
	 * Retrieves a loan request and populates its dependent objects.
	 * 
	 * @param profileId
	 *            the employee profile id
	 * @param contractId
	 *            the employee contract id
	 * @param principal
	 *            the user principal
	 * @return WithdrawalRequest The initial data object. If an exception
	 *         occurs.
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public Loan read(final Integer userProfileId, final Integer contractId,
			final Integer submissionId) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> initiateLoan");
		} // fi
		Loan returnLoan = null;
		try {
			/*
			 * Read loan object from the DAO first. Notice that before we read
			 * from the database, we have no idea what the status of the loan
			 * is. Therefore, we cannot use the state object to perform the read
			 * for us.
			 */
			returnLoan = loanDao.read(submissionId, contractId, userProfileId);

			if (returnLoan != null) {
				/*
				 * Populate any additional information into the loan object.
				 */
				LoanStateFactory.getState(returnLoan).populate(returnLoan);
			}
			return returnLoan;
		} catch (DistributionServiceException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	/**
	 * Retrieves all activities related to the given loan request.
	 * 
	 * @param userProfileId
	 * @param contractId
	 * @param submissionId
	 * @return
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public LoanActivities readActivities(Integer userProfileId,
			Integer contractId, String submissionId,
			Integer participantUserProfileId, String participantFirstName,
			String participantLastName) {
		LoanActivities activities = new LoanActivities(submissionId,
				contractId, participantUserProfileId, participantFirstName,
				participantLastName);
		if (submissionId != null) {
			try {
				activities.addActivityDetails(detailDao.select(Integer
						.valueOf(submissionId), contractId, userProfileId,
						LoanActivityDetail.class));
				activities.addActivityDetails(dynamicDetailDao.select(Integer
						.valueOf(submissionId), contractId, userProfileId));
				activities.addActivityDetails(detailDao.selectSystemOfRecord(
						contractId, Integer.valueOf(submissionId),
						userProfileId, LoanActivityDetail.class));
				activities.addActivityDetails(dynamicDetailDao
						.selectSystemOfRecord(contractId, Integer
								.valueOf(submissionId), userProfileId));

				activities.setSummaries(summaryDao.select(Integer
				        .valueOf(submissionId), contractId, userProfileId,
				        LoanActivitySummary.class));
			} catch (DistributionServiceException e) {
				throw ExceptionHandlerUtility.wrap(e);
			}
		}
		return activities;
	}

	/**
	 * Retrieves a list of loan request created by the given user profile ID.
	 * 
	 * @param userProfileId
	 * @param contractId
	 * @return
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public List getLoanRequestsByCreatedId(Integer userProfileId,
			Integer contractId) {
		try {
			List<Loan> loans = loanDao.getLoanRequestsByCreatedId(contractId,
					userProfileId);
			for (Loan loan : loans) {
				LoanStateFactory.getState(loan).populate(loan);
			}
			return loans;
		} catch (DistributionServiceException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	/**
	 * Retrieves the last loan request for the given user profile ID regardless
	 * of its status.
	 * 
	 * @param userProfileId
	 * @param contractId
	 * @return
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public Loan getLastLoanRequest(Integer userProfileId, Integer contractId) {
		try {
			Loan loan = loanDao.getLastLoanRequest(contractId, userProfileId);
			if (loan != null) {
				LoanStateFactory.getState(loan).populate(loan);
			}
			return loan;
		} catch (DistributionServiceException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	/**
	 * Retrieves loan related plan data from the database.
	 * 
	 * @param contractId
	 * @return
	 * @ejb.interface-method view-type="local"
	 */
	public LoanPlanData getLoanPlanData(Integer contractId) {
		LoanDataHelper loanDataHelper = LoanObjectFactory.getInstance()
				.getLoanDataHelper();
		return loanDataHelper.getLoanPlanData(contractId);
	}

	/**
	 * Retrieves loan related settings from the database.
	 * 
	 * @param contractId
	 * @return
	 * @ejb.interface-method view-type="local"
	 */
	public LoanSettings getLoanSettings(final Integer contractId) {
		LoanDataHelper loanDataHelper = LoanObjectFactory.getInstance()
				.getLoanDataHelper();
		return loanDataHelper.getLoanSettings(contractId);
	}

	/**
	 * Retrieves a participant's money types and their account balances for the
	 * purpose of loan calculation.
	 * 
	 * @param contractId
	 * @return A Pair object where the first element is a list of LoanMoneyType
	 *         and the second element is the EmployeeVestingInformation object.
	 * @ejb.interface-method view-type="local"
	 */
	public Pair getParticipantMoneyTypesForLoans(Integer contractId,
			Integer participantProfileId) {
		LoanDataHelper loanDataHelper = LoanObjectFactory.getInstance()
				.getLoanDataHelper();
		return loanDataHelper.getParticipantMoneyTypesForLoans(contractId,
				participantProfileId);
	}

	/**
	 * Retrieves participant data for loan.
	 * 
	 * @param contractId
	 * @return
	 * @ejb.interface-method view-type="local"
	 */
	public LoanParticipantData getLoanParticipantData(Integer contractId,
			Integer participantProfileId) {
		LoanDataHelper loanDataHelper = LoanObjectFactory.getInstance()
				.getLoanDataHelper();
		return loanDataHelper.getLoanParticipantData(contractId,
				participantProfileId);
	}

	/**
	 * Marks all Loan requests having the expiry date before the current
	 * expiration date and being in Draft or Pending states as Expired.
	 * 
	 * Logs this event in the Activity History and via MRL
	 * 
	 * @param checkDate
	 *            Date used to test if Loan requests expired (typically the
	 *            current date)
	 * @param profileId
	 *            String value containing the user profile ID or system profile
	 *            ID (3)
	 * @return int - The number of withdrawals that have been marked as expired.
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public int markExpiredLoans(final Date checkDate, final Integer profileId) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> markExpiredLoans");
		} // fi
		int noOfExpiredRecords = 0;

		try {
			Collection<Pair<Integer, Integer>> loanRequests = loanDao
					.getExpiringLoans(checkDate);
			Timestamp lastUpdatedTs = new Timestamp(new Date().getTime());
			ActivitySummaryDao summaryDao = new ActivitySummaryDao();
			for (Pair<Integer, Integer> pair : loanRequests) {
				Integer contractId = pair.getFirst();
				Integer submissionId = pair.getSecond();
				if (loanDao.expireLoan(submissionId, profileId, lastUpdatedTs)) {
					logger.debug("Expired submission ID: " + submissionId);

					/*
					 * Save activity summary
					 */
					ActivitySummary summary = new LoanActivitySummary();
					summary.setCreatedById(profileId);
					summary.setCreated(lastUpdatedTs);
					summary.setStatusCode(ActivitySummary.EXPIRED);
					summary.setSubmissionId(submissionId);
					summaryDao.insert(submissionId, contractId, profileId,
							summary);

					/**
					 * MRL Logging
					 */
					Map<String, String> additionalLogInfo = new HashMap<String, String>();
					additionalLogInfo.put(EventLog.SUBMISSION_ID, submissionId
							.toString());
					additionalLogInfo.put(EventLog.ACTION,
							DistributionEventEnum.EXPIRE.getEventName());
					EventLoggingHelper.log(LoanEventLog.class, null,
							additionalLogInfo, profileId);

					noOfExpiredRecords++;

					/**
					 * LoanRequestExpiredEvent Generation
					 */
					LoanEventGenerator loanEventGenerator = EventFactory.getInstance()
							.getLoanEventGenerator(contractId,
									submissionId,
									profileId);
					loanEventGenerator.prepareAndSendExpiredEvent();

				} else {
					logger.error("Could not expire submission ID: "
							+ submissionId);
				}
			}

		} catch (Exception e) {
			throw ExceptionHandlerUtility.wrap(new SystemException(e,
					"Could not expire submission. "));
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- markExpiredLoans");
		} // fi

		return noOfExpiredRecords;
	}

	/**
	 * Returns a list of loan requests that will be expired in the given number of days.
	 * 
	 * @param numberOfDaysBeforeExpiryDate
	 * @return
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public List getAboutToExpireLoanRequests(
			Integer numberOfDaysBeforeExpiryDate) {
		Calendar nextThreeDays = Calendar.getInstance();
		nextThreeDays.add(Calendar.DAY_OF_MONTH, numberOfDaysBeforeExpiryDate);
		try {
			List<Loan> loanRequests = loanDao
					.getAboutToExpireLoans(nextThreeDays.getTime());
			return loanRequests;
		} catch (Exception e) {
			throw ExceptionHandlerUtility.wrap(new SystemException(e,
					"Could not retrieve loans. "));
		}
	}

	/**
	 * returns the count of outstanding old i:loan requests by status
	 * 
	 * @param contractId
	 * @return Integer count of requests
	 * @ejb.interface-method view-type="local"
	 */
	public Integer getOutstandingOldILoanRequestCount(int contractId) {
		Integer outStandingOldILoanRequests = null;
		try {
			outStandingOldILoanRequests = loanSupportDao
					.getOutstandingOldIloanRequestsCount(contractId);
		} catch (DistributionServiceException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
		return outStandingOldILoanRequests;
	}
	
	/**
	 * get LRK01 and Allow Online Loans CSF information for a given list of contracts
	 * @param contractIdList
	 * @return Map of Contract Id & its specific LoanSettings
	 * @ejb.interface-method view-type="local"
	 */
	public Map <Integer, ArrayList<LoanSettings>> getPartialLoanSettingsData(Integer[] contractIdList) {
		
		try {
			Map <Integer, ArrayList<LoanSettings>> results = loanSupportDao.getPartialLoanSettingsData(contractIdList);
			return results;
		} catch (DAOException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}
	
	/**
	 * get if the contract has Loan Record Keeping Product Feature
	 * 
	 * @param contractId
	 * @return
	 * @throws LoanDaoException
	 * @ejb.interface-method view-type="local"
	 */
	public boolean hasLoanRecordKeepingProductFeature(int contractId) {
		try {
			boolean result = loanSupportDao.hasLoanRecordKeepingProductFeature(contractId);
			return result;
		} catch (DistributionServiceException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}
	
	/**
	 * Retrieves a the outstanding loan for the the given ppt profileID, contractId, and loan id.
	 * 
	 * @param contractId
	 * 		Contract Id of the ppt
	 * @param profileId
	 * 		ProfileId of the ppt
	 * @param loanId
	 * 		Loan Id 
	 * @return
	 * 		Outstanding Loan object
	 * @ejb.interface-method view-type="local"
	 */
	public OutstandingLoan getOutstandingLoan(Integer contractId, Long profileId
			, Integer loanId) {
		try {
			OutstandingLoan loan = loanSupportDao.getOutstandingLoan(contractId, profileId, loanId);
			return loan;
		} catch (DistributionServiceException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}
	
	/**
	 *Retrieves the UOL count for given contract id.
	 * 
	 * @param contractId
	 * 		Contract Id 
	 * @ejb.interface-method view-type="local"
	 */
	public Integer getUOLCount(Integer contractId)throws SystemException{
		try{
			return loanDao.getUOLCount(contractId);
		}
		catch (DistributionServiceException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
						
	}
	
	/**
	 * Returns true if there exists of given Loan status code in status history.
	 * 
	 * @param submissionId
	 * @param statusCode
	 * @return boolean
	 * @ejb.interface-method view-type="local"
	 */
	public boolean checkLoanStatusExists(final Integer submissionId, final String statusCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> checkLoanStatusExists");
		}
		boolean loanStatusExists = false;
		try {

			loanStatusExists = loanParameterDao
					.checkLoanStatusExists(submissionId, statusCode);

		} catch (DistributionServiceException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
		return loanStatusExists;
	}
	
	/**
	 * Retrieves  ManagedContent from  MANAGED_CONTENT_REFERENCE table
	 * 
	 * @param submissionId
	 * @param contractId
	 * @param userProfileId
	 * @return
	 * @ejb.interface-method view-type="local"
	 */
	public List<ManagedContent> getManagedContent(Integer submissionId, Integer contractId, Integer userProfileId){

		final ManagedContentDao managedContentDao = new ManagedContentDao();
		try {
				return  managedContentDao.read(submissionId, contractId , userProfileId);
		}catch (DistributionServiceException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}
	
}
