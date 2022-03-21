package com.manulife.pension.service.loan.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.SQLDeleteHandler;
import com.intware.dao.jdbc.SQLInsertHandler;
import com.intware.dao.jdbc.SQLUpdateHandler;
import com.intware.dao.jdbc.SelectMultiFieldMultiRowQueryHandler;
import com.intware.dao.jdbc.SelectSingleValueQueryHandler;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.AtRiskHandler;
import com.manulife.pension.service.distribution.dao.DeclarationDao;
import com.manulife.pension.service.distribution.dao.DistributionDao;
import com.manulife.pension.service.distribution.dao.FeeDao;
import com.manulife.pension.service.distribution.dao.ManagedContentDao;
import com.manulife.pension.service.distribution.dao.NoteDao;
import com.manulife.pension.service.distribution.dao.RecipientDao;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsInputVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsVO;
import com.manulife.pension.service.distribution.valueobject.Declaration;
import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.distribution.valueobject.ManagedContent;
import com.manulife.pension.service.distribution.valueobject.Note;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.distribution.valueobject.RequestType;
import com.manulife.pension.service.loan.domain.LoanConstants;
import com.manulife.pension.service.loan.domain.LoanStateEnum;
import com.manulife.pension.service.loan.exception.LoanDaoException;
import com.manulife.pension.service.loan.valueobject.EjbLoanSupportDataRetriever;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanAddress;
import com.manulife.pension.service.loan.valueobject.LoanDeclaration;
import com.manulife.pension.service.loan.valueobject.LoanFee;
import com.manulife.pension.service.loan.valueobject.LoanMoneyType;
import com.manulife.pension.service.loan.valueobject.LoanNote;
import com.manulife.pension.service.loan.valueobject.LoanParameter;
import com.manulife.pension.service.loan.valueobject.LoanPayee;
import com.manulife.pension.service.loan.valueobject.LoanPaymentInstruction;
import com.manulife.pension.service.loan.valueobject.LoanRecipient;
import com.manulife.pension.service.loan.valueobject.MoneyTypeComparator;
import com.manulife.pension.util.JdbcHelper;
import com.manulife.pension.util.Pair;

public class LoanDao extends DistributionDao {

	private static final String CLASS_NAME = LoanDao.class.getName();

	private static final Logger logger = Logger.getLogger(LoanDao.class);

	private LoanMoneyTypeDao moneyTypeDao = new LoanMoneyTypeDao();

	private LoanParameterDao parameterDao = new LoanParameterDao();

	private FeeDao feeDao = new FeeDao();

	private RecipientDao recipientDao = new RecipientDao();

	private DeclarationDao declarationDao = new DeclarationDao();

	private NoteDao noteDao = new NoteDao();

	private ManagedContentDao managedContentDao = new ManagedContentDao();

	/**
	 * Inserts a Loan in the submission Database.
	 * 
	 * The timestamp must be provided, and will be used for both the created
	 * date and the last updated date.
	 * 
	 * @param contractId
	 *            The contract the loan is for
	 * @param userProfileId
	 *            The Id of the user that created the loan
	 * @param loan
	 *            The loan value object
	 * @param timestamp
	 *            The created and last updated timestamp
	 * @return the submission id of the new loan
	 * @throws DistributionServiceException
	 *             Thrown if there is an underlying exception.
	 */
	public Integer insert(final Integer contractId,
			final Integer userProfileId, final Loan loan,
			final Timestamp timestamp) throws DistributionServiceException {

		if (contractId == null || userProfileId == null || loan == null
				|| timestamp == null) {
			if (contractId == null) {
				throw new LoanDaoException(
						"LoanDao.insert failed.  contract id is null");
			} else if (userProfileId == null) {
				throw new LoanDaoException(
						"LoanDao.insert failed.  user profile id is null");
			} else if (loan == null) {
				throw new LoanDaoException(
						"LoanDao.insert failed.  loan is null");
			} else if (timestamp == null) {
				throw new LoanDaoException(
						"LoanDao.insert failed.  timestamp is null");
			}
		}

		/*
		 * Make sure we do not have multiple pending loan requests inserted. The
		 * same check is done early on as well but is checked again here to
		 * minimize the risk of duplicate rows.
		 */
		List<Integer> pendingRequests = getPendingRequestSubmissionIds(
				contractId, loan.getParticipantProfileId());
		if (pendingRequests.size() > 0) {
			return null;
		}
		
		List<Object> params;
		Integer submissionId = null;
		// create a submission
		try {
			submissionId = new Integer(BaseDatabaseDAO.getNextSequenceValue(
					BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
					LoanDaoSql.SUBMISSION_SEQUENCE).intValue());

			params = new ArrayList<Object>();
			params.add(submissionId);
			params.add(timestamp);
			params.add(userProfileId);
			params.add(userProfileId);
			params.add(timestamp);
			params.add(userProfileId);
			params.add(timestamp);

			new SQLInsertHandler(SUBMISSION_DATA_SOURCE_NAME,
					LoanDaoSql.SQL_INSERT_SUBMISSION,
					LoanDaoSql.SQL_INSERT_SUBMISSION_PARAMETER_TYPES)
					.insert(params.toArray());
		} catch (DAOException e) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ LoanDaoSql.SQL_INSERT_SUBMISSION + " for contract ID "
					+ contractId + " and newSubmissionId " + submissionId, e);
		}

		// create a submission case
		try {
			params = new ArrayList<Object>();
			params.add(submissionId);
			params.add(contractId);
			params.add(LoanConstants.SUBMISSION_CASE_TYPE_CODE);
			params.add(timestamp);
			params.add(loan.getStatus());
			params.add(userProfileId);
			params.add(timestamp);
			params.add(userProfileId);
			params.add(timestamp);

			new SQLInsertHandler(SUBMISSION_DATA_SOURCE_NAME,
					LoanDaoSql.SQL_INSERT_SUBMISSION_CASE).insert(params
					.toArray());
		} catch (DAOException e) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ LoanDaoSql.SQL_INSERT_SUBMISSION_CASE
					+ " for contract ID " + contractId
					+ " and newSubmissionId " + submissionId, e);
		}
		
		// create loan request
		try {
			params = new ArrayList<Object>();
			params.add(submissionId);
			params.add(contractId);
			params.add(LoanConstants.SUBMISSION_CASE_TYPE_CODE);
			params.add(loan.getParticipantProfileId());
			params.add(loan.getParticipantId());
			params.add(loan.getCreatedByRoleCode());
			params.add(loan.getLegallyMarriedInd());
			params.add(loan.getLoanType());
			params.add(loan.getLoanReason());
			params.add(loan.getRequestDate());
			params.add(loan.getExpirationDate());
			params.add(loan.getEffectiveDate());
			params.add(loan.getMaturityDate());
			params.add(loan.getFirstPayrollDate());
			params.add(loan.getMaximumAmortizationYears());
			params.add(loan.getDefaultProvision());
			params.add(loan.getMaxBalanceLast12Months());
			params.add(loan.getOutstandingLoansCount());
			params.add(loan.getCurrentOutstandingBalance());
			params.add(loan.getLastFeeChangedByTpaProfileId());
			params.add(loan.getLastFeeChangedWasPlanSponsorUserInd());
			params.add(userProfileId);
			params.add(timestamp);
			params.add(userProfileId);
			params.add(timestamp);
			params.add(loan.getContractLoanExpenseMarginPct());
			params.add(loan.getMinimumLoanAmount());
			params.add(loan.getMaximumLoanAmount());
			params.add(loan.getMaximumLoanPercentage());
			params.add(loan.getContractLoanMonthlyFlatFee());
			params.add(loan.getSpousalConsentReqdInd());
			params.add(loan.getAtRiskInd());
			params.add(loan.getApplyIrs10KDollarRuleInd());
			new SQLInsertHandler(SUBMISSION_DATA_SOURCE_NAME,
					LoanDaoSql.SQL_INSERT_SUBMISSION_LOAN,
					LoanDaoSql.SQL_INSERT_SUBMISSION_LOAN_PARAMETER_TYPES)
					.insert(params.toArray());

		} catch (DAOException e) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ LoanDaoSql.SQL_INSERT_SUBMISSION_LOAN
					+ " for contract ID " + contractId
					+ " and newSubmissionId " + submissionId, e);
		}
		Collection<LoanParameter> loanParameters = new ArrayList<LoanParameter>();
		if (loan.getOriginalParameter() != null) {
			loanParameters.add(loan.getOriginalParameter());
		}
		if (loan.getReviewedParameter() != null) {
			loanParameters.add(loan.getReviewedParameter());
		}
		if (loan.getAcceptedParameter() != null) {
			loanParameters.add(loan.getAcceptedParameter());
		}
		parameterDao.insertUpdate(submissionId, contractId, userProfileId,
				timestamp, loanParameters);
		moneyTypeDao.insertUpdate(submissionId, contractId, userProfileId,
				timestamp, loan.getMoneyTypes());

		if (loan.getFee() != null) {
			Collection<Fee> fees = new ArrayList<Fee>();
			fees.add(loan.getFee());
			feeDao.insertUpdatePrune(submissionId, contractId, userProfileId,
					fees, LoanFee.class);
		}

		if (loan.getRecipient() != null) {
			recipientDao.insert(submissionId, contractId, userProfileId, Arrays
					.asList(new Recipient[] { loan.getRecipient() }));
		}

		declarationDao.insert(submissionId, contractId, userProfileId, loan
				.getDeclarations());

		if (loan.getCurrentAdministratorNote() != null) {
			noteDao.insert(submissionId, contractId, userProfileId, loan
					.getCurrentAdministratorNote());
		}

		if (loan.getCurrentParticipantNote() != null) {
			noteDao.insert(submissionId, contractId, userProfileId, loan
					.getCurrentParticipantNote());
		}

		managedContentDao.insert(submissionId, contractId, userProfileId,
				timestamp, loan.getManagedContents());

		if (loan.getAtRiskDetailsVO() != null) {
			List<AtRiskDetailsVO> objects = new ArrayList<AtRiskDetailsVO>();
			AtRiskDetailsVO atRiskDetailsVO = loan.getAtRiskDetailsVO();
			atRiskDetailsVO.setSubmissionId(submissionId);
			objects.add(atRiskDetailsVO);
			DistributionAtRiskDetailsDAO.insertOrUpdate(objects);
		}
		return submissionId;
	}

	/**
	 * Updates an existing loan in the submission Database.
	 * 
	 * The timestamp must be provided, and will be used for only the last
	 * updated date.
	 * 
	 * @param contractId
	 *            The contract the loan is for
	 * @param userProfileId
	 *            The Id of the user that created the loan
	 * @param loan
	 *            The loan value object
	 * @param timestamp
	 *            The last updated timestamp
	 * @return true if update is successful. false, if there are concurrent
	 *         updates to the record.
	 * @throws DistributionServiceException
	 *             Thrown if there is an underlying exception.
	 */
	public boolean update(final Integer submissionId, final Integer contractId,
			final Integer userProfileId, final Loan loan,
			final Timestamp timestamp) throws DistributionServiceException {

		if (contractId == null || userProfileId == null || loan == null
				|| timestamp == null) {
			if (contractId == null) {
				throw new LoanDaoException(
						"LoanDao.insert failed.  contract id is null");
			} else if (userProfileId == null) {
				throw new LoanDaoException(
						"LoanDao.insert failed.  user profile id is null");
			} else if (loan == null) {
				throw new LoanDaoException(
						"LoanDao.insert failed.  loan is null");
			} else if (timestamp == null) {
				throw new LoanDaoException(
						"LoanDao.insert failed.  timestamp is null");
			}
		}

		List<Object> params;
		String oldProcessedStatusCode = getProcessStatusCode(submissionId);
		boolean statusChanged = !StringUtils.equals(oldProcessedStatusCode,
				loan.getStatus());

		// Special for BGA loans being pulled back to review from pending approval by a BGA Rep.
		// ONLY a BGA Loan can move from pending approve to pending review, so we are using this
		// test as a way of detecting this particular special case.
		boolean stateChangeFromApprovalToReview = false;
		if (loan.getPreviousStatus() != null && loan.getStatus() != null) {
			stateChangeFromApprovalToReview = (LoanStateEnum.PENDING_APPROVAL.getStatusCode().equals(loan.getPreviousStatus())) 
										   && (LoanStateEnum.PENDING_REVIEW.getStatusCode().equals(loan.getStatus()));
		}
		
		// update the submission table
		try {

			params = new ArrayList<Object>();
			params.add(timestamp);
			params.add(userProfileId);
			params.add(timestamp);
			params.add(submissionId);

			new SQLUpdateHandler(SUBMISSION_DATA_SOURCE_NAME,
					LoanDaoSql.SQL_UPDATE_SUBMISSION,
					LoanDaoSql.SQL_UDPATE_SUBMISSION_PARAMETER_TYPES)
					.update(params.toArray());
		} catch (DAOException e) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ LoanDaoSql.SQL_UPDATE_SUBMISSION + " for contract ID "
					+ contractId + " and newSubmissionId " + submissionId, e);
		}

		// update the submission case table.
		// the processed timestamp only gets updated if it changes.
		// therefore, grab this one value and examine it before deciding how to
		// update.
		try {
			params = new ArrayList<Object>();
			if (statusChanged) {
				params.add(loan.getStatus());
				params.add(timestamp);
			}
			params.add(userProfileId);
			params.add(timestamp);
			params.add(submissionId);
			params.add(loan.getLastUpdated());

			/*
			 * Check concurrent update. Use SC's last updated ts because the
			 * expiration process only updates the SC table.
			 */
			int updateCount = new SQLUpdateHandler(
					SUBMISSION_DATA_SOURCE_NAME,
					statusChanged ? LoanDaoSql.SQL_UPDATE_SUBMISSION_CASE
							: LoanDaoSql.SQL_UPDATE_SUBMISSION_CASE_NO_STATUS_CHANGE)
					.update(params.toArray());
			if (updateCount == 0) {
				return false;
			}
		} catch (DAOException e) {
			throw new LoanDaoException(
					"Problem occurred in prepared call: "
							+ (statusChanged ? LoanDaoSql.SQL_UPDATE_SUBMISSION_CASE
									: LoanDaoSql.SQL_UPDATE_SUBMISSION_CASE_NO_STATUS_CHANGE)
							+ " for contract ID " + contractId
							+ " and newSubmissionId " + submissionId, e);
		}
		if (!stateChangeFromApprovalToReview) {
			// We update the loan (as usual) if the special case of a
			// BGA contract moving from pending approve to pending review is not present.
			// In the case of stateChangeFromApprovalToReview = true we want to skip all this... 
			try {
				params = new ArrayList<Object>();
				params.add(contractId);
				params.add(loan.getParticipantProfileId());
				params.add(loan.getParticipantId());
				params.add(loan.getLegallyMarriedInd());
				params.add(loan.getLoanType());
				params.add(loan.getLoanReason());
				params.add(loan.getRequestDate());
				params.add(loan.getExpirationDate());
				params.add(loan.getEffectiveDate());
				params.add(loan.getMaturityDate());
				params.add(loan.getFirstPayrollDate());
				params.add(loan.getMaximumAmortizationYears());
				params.add(loan.getDefaultProvision());
				params.add(loan.getMaxBalanceLast12Months());
				params.add(loan.getOutstandingLoansCount());
				params.add(loan.getCurrentOutstandingBalance());
				params.add(loan.getLastFeeChangedByTpaProfileId());
				params.add(loan.getLastFeeChangedWasPlanSponsorUserInd());
				params.add(userProfileId);
				params.add(timestamp);
				params.add(loan.getContractLoanExpenseMarginPct());
				params.add(loan.getMinimumLoanAmount());
				params.add(loan.getMaximumLoanAmount());
				params.add(loan.getMaximumLoanPercentage());
				params.add(loan.getContractLoanMonthlyFlatFee());
	            params.add(loan.getSpousalConsentReqdInd());
				params.add(loan.getAtRiskInd());
				params.add(loan.getApplyIrs10KDollarRuleInd());
				params.add(submissionId);
				new SQLUpdateHandler(SUBMISSION_DATA_SOURCE_NAME,
						LoanDaoSql.SQL_UPDATE_SUBMISSION_LOAN,
						LoanDaoSql.SQL_UPDATE_SUBMISSION_LOAN_PARAMETER_TYPES)
						.update(params.toArray());
			} catch (DAOException e) {
				throw new LoanDaoException("Problem occurred in prepared call: "
						+ LoanDaoSql.SQL_UPDATE_SUBMISSION_LOAN
						+ " for contract ID " + contractId
						+ " and newSubmissionId " + submissionId, e);
			}
	
			Collection<LoanParameter> loanParameters = new ArrayList<LoanParameter>();
			if (loan.getOriginalParameter() != null) {
				loanParameters.add(loan.getOriginalParameter());
			}
			if (loan.getReviewedParameter() != null) {
				loanParameters.add(loan.getReviewedParameter());
			}
			if (loan.getAcceptedParameter() != null) {
				loanParameters.add(loan.getAcceptedParameter());
			}
			parameterDao.insertUpdate(submissionId, contractId, userProfileId,
					timestamp, loanParameters);
			moneyTypeDao.insertUpdate(submissionId, contractId, userProfileId,
					timestamp, loan.getMoneyTypes());
	
			if (loan.getFee() != null) {
				Collection<Fee> fees = new ArrayList<Fee>();
				fees.add(loan.getFee());
				feeDao.insertUpdatePrune(submissionId, contractId, userProfileId,
						fees, LoanFee.class);
			} else {
				feeDao.insertUpdatePrune(submissionId, contractId, userProfileId,
						null, LoanFee.class);
			}
	
			recipientDao.deleteAll(submissionId, contractId, userProfileId);
	
			if (loan.getRecipient() != null) {
				recipientDao.insert(submissionId, contractId, userProfileId, Arrays
						.asList(new Recipient[] { loan.getRecipient() }));
			}
	
			if (loan.isLoginUserPlanSponsorOrTpa()) {
	            List<String> declarationTypeCodesToDelete = new ArrayList<String>();
	            List<String> contentTypeCodesToDelete = new ArrayList<String>();
	            contentTypeCodesToDelete.add(ManagedContent.
	                    LOAN_APPROVAL_PARTICIPANT_INITIATED_PLAN_SPOUSAL_CONSENT_YES);
	            contentTypeCodesToDelete.add(ManagedContent.
	                    LOAN_APPROVAL_PARTICIPANT_INITIATED_PLAN_SPOUSAL_CONSENT_NO);
	            contentTypeCodesToDelete.add(ManagedContent.
	                    LOAN_APPROVAL_PARTICIPANT_INITIATED_PLAN_SPOUSAL_CONSENT_NULL);
	            contentTypeCodesToDelete.add(ManagedContent.
	                    LOAN_APPROVAL_EXT_USER_INITIATED_PLAN_SPOUSAL_CONSENT_YES);
	            contentTypeCodesToDelete.add(ManagedContent.
	                    LOAN_APPROVAL_EXT_USER_INITIATED_PLAN_SPOUSAL_CONSENT_NO);
	            contentTypeCodesToDelete.add(ManagedContent.
	                    LOAN_APPROVAL_EXT_USER_INITIATED_PLAN_SPOUSAL_CONSENT_NULL);
	            contentTypeCodesToDelete.add(ManagedContent.
	                    LOAN_APPROVAL_GENERIC);
	            
		        if (loan.isParticipantInitiated()) { 
		            if (JdbcHelper.INDICATOR_YES.equals(loan.getAtRiskInd())) {
		                declarationTypeCodesToDelete.add(Declaration.AT_RISK_TRANSACTION_TYPE_CODE);
		                if(loan.getAtRiskDetailsVO() != null) {
		                	//TODO: PA Do we need to compare with AddressLine1?
			                contentTypeCodesToDelete.add(ManagedContent.AT_RISK_TEXT);	                	
		                }
		            }
		        } else {
	                declarationTypeCodesToDelete.add(Declaration.TRUTH_IN_LENDING_NOTICE);
	                declarationTypeCodesToDelete.add(Declaration.PROMISSORY_NOTE_AND_IRREVOCABLE_PLEDGE);
	                contentTypeCodesToDelete.add(ManagedContent.TRUTH_IN_LENDING_NOTICE_HTML);
	                contentTypeCodesToDelete.add(ManagedContent.TRUTH_IN_LENDING_NOTICE_PDF);
	                contentTypeCodesToDelete.add(ManagedContent.PROMISSORY_NOTE_AND_IRREVOCABLE_PLEDGE_HTML);
	                contentTypeCodesToDelete.add(ManagedContent.PROMISSORY_NOTE_AND_IRREVOCABLE_PLEDGE_PDF);
	                contentTypeCodesToDelete.add(ManagedContent.AMORTIZATION_SCHEDULE_HTML);
	                contentTypeCodesToDelete.add(ManagedContent.AMORTIZATION_SCHEDULE_PDF);
		        }
	
		        if (declarationTypeCodesToDelete.size() > 0) {
		            declarationDao.delete(submissionId, contractId, declarationTypeCodesToDelete);
		        }
	            managedContentDao.delete(submissionId, contractId, contentTypeCodesToDelete);
			} else {
			    // Logged in user is a Participant
		        declarationDao.deleteAll(submissionId, contractId, userProfileId);
		        managedContentDao.deleteAll(submissionId, contractId, userProfileId);
			}
			
	        declarationDao.insert(submissionId, contractId, userProfileId, loan
	                .getDeclarations());
	        managedContentDao.insert(submissionId, contractId, userProfileId,
	                timestamp, loan.getManagedContents());
			
			if ((LoanStateEnum.DRAFT.getStatusCode().equals(loan
					.getPreviousStatus()))) {
				noteDao.deleteAll(submissionId, contractId, userProfileId);
			}
			noteDao.insert(submissionId, contractId, userProfileId, loan
					.getCurrentAdministratorNote());
			noteDao.insert(submissionId, contractId, userProfileId, loan
					.getCurrentParticipantNote());
			
			if (loan.getAtRiskDetailsVO() != null) {
				List<AtRiskDetailsVO> objects = new ArrayList<AtRiskDetailsVO>();
				AtRiskDetailsVO atRiskDetailsVO = loan.getAtRiskDetailsVO();
				atRiskDetailsVO.setSubmissionId(submissionId);
				objects.add(atRiskDetailsVO);
				DistributionAtRiskDetailsDAO.insertOrUpdate(objects);
			}
		}
		/*
		 * There is no need to update suspicious address.
		 */
		return true;
	}

	public void delete(final Integer submissionId, final Integer contractId,
			final Integer userProfileId) throws DistributionServiceException {

		parameterDao.deleteAll(submissionId, contractId, userProfileId);
		moneyTypeDao.deleteAll(submissionId, contractId, userProfileId);
		managedContentDao.deleteAll(submissionId, contractId, userProfileId);
		declarationDao.deleteAll(submissionId, contractId, userProfileId);
		feeDao.deleteAll(submissionId, contractId, userProfileId);
		noteDao.deleteAll(submissionId, contractId, userProfileId);
		recipientDao.deleteAll(submissionId, contractId, userProfileId);
		managedContentDao.deleteAll(submissionId, contractId, userProfileId);
		
		List<Object> params = new ArrayList<Object>();
		params.add(submissionId);
		try {
			new SQLDeleteHandler(BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
					LoanDaoSql.SQL_DELETE_SUBMISSION_LOAN).delete(params
					.toArray());
			new SQLDeleteHandler(BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
					LoanDaoSql.SQL_DELETE_SUBMISSION_CASE).delete(params
					.toArray());
			new SQLDeleteHandler(BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
					LoanDaoSql.SQL_DELETE_SUBMISSION).delete(params.toArray());

		} catch (DAOException e) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ LoanDaoSql.SQL_DELETE_SUBMISSION_LOAN
					+ " for contract ID " + contractId
					+ " and newSubmissionId " + submissionId, e);
		}
	}

	private List<Loan> getLoans(String sql, Integer contractId,
			Integer userProfileId) throws DistributionServiceException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Loan> result = new ArrayList<Loan>();
		try {
			conn = BaseDatabaseDAO.getDefaultConnection(CLASS_NAME,
					BaseDatabaseDAO.STP_DATA_SOURCE_NAME);
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userProfileId);
			stmt.setInt(2, contractId);
			rs = stmt.executeQuery();
			Loan returnVo = null;
			while (rs.next()) {
				returnVo = toLoan(rs);
				populateLoanDependentObjects(returnVo);
				result.add(returnVo);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ sql + " for contract ID:" + contractId + " profile ID:"
					+ userProfileId, e);
		} catch (SystemException e) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ sql + " for contract ID:" + contractId + " profile ID:"
					+ userProfileId, e);
		} finally {
			BaseDatabaseDAO.close(stmt, conn);
		}
		return result;
	}

	public List<Loan> getLoanRequestsByCreatedId(Integer contractId,
			Integer userProfileId) throws DistributionServiceException {
		String sql = LoanDaoSql.SQL_SELECT_LOAN
				+ LoanDaoSql.SQL_WHERE_CREATED_USER_PROFILE_ID_AND_CONTRACT_ID;
		return getLoans(sql, contractId, userProfileId);
	}

	/**
	 * Retrieves the last loan request for the given user profile ID.
	 * 
	 * @param contractId
	 * @param participantProfileId
	 * @return
	 * @throws DistributionServiceException
	 */
	public Loan getLastLoanRequest(Integer contractId, Integer profileId)
			throws DistributionServiceException {
		String sql = LoanDaoSql.SQL_SELECT_LOAN
				+ LoanDaoSql.SQL_WHERE_PROFILE_ID_AND_CONTRACT_ID
				+ " ORDER BY SL.CREATED_TS DESC";
		List<Loan> result = getLoans(sql, contractId, profileId);
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	private void populateLoanDependentObjects(Loan loan)
			throws DistributionServiceException {
		Integer submissionId = loan.getSubmissionId();
		Integer contractId = loan.getContractId();
		Integer userProfileId = loan.getLoginUserProfileId();

		List<LoanParameter> parameters = parameterDao.read(submissionId,
				contractId, userProfileId);

		for (LoanParameter parameter : parameters) {
			if (LoanStateEnum.DRAFT.getStatusCode().equals(
					parameter.getStatusCode())) {
				loan.setOriginalParameter(parameter);
			} else if (LoanStateEnum.PENDING_REVIEW.getStatusCode().equals(
					parameter.getStatusCode())) {
				loan.setReviewedParameter(parameter);
			}
			if (LoanStateEnum.PENDING_ACCEPTANCE.getStatusCode().equals(
					parameter.getStatusCode())) {
				loan.setAcceptedParameter(parameter);
			}
		}

		List<LoanMoneyType> moneyTypes = moneyTypeDao.read(submissionId,
				contractId, userProfileId);
		loan.setMoneyTypes(moneyTypes);
		
		// Sort the moneyTypes.
		Comparator <LoanMoneyType> moneyTypeComparator = new MoneyTypeComparator();
		Collections.sort(loan.getMoneyTypes(), moneyTypeComparator);

		List<? extends Fee> fees = feeDao.select(submissionId, contractId,
				userProfileId, LoanFee.class);
		loan.setFee(fees.size() == 1 ? fees.get(0) : null);

		List<? extends Recipient> recipients = recipientDao.select(
				submissionId, contractId, userProfileId, LoanRecipient.class,
				LoanPayee.class, LoanAddress.class,
				LoanPaymentInstruction.class);
		loan.setRecipient(recipients.size() == 1 ? (LoanRecipient) recipients
				.get(0) : null);

		List<LoanDeclaration> declarations = (List<LoanDeclaration>) declarationDao
				.select(submissionId, contractId, userProfileId,
						LoanDeclaration.class);
		loan.setDeclarations(declarations);

		List<LoanNote> loanNotes = (List<LoanNote>) noteDao.select(
				submissionId, LoanNote.class);
		for (LoanNote loanNote : loanNotes) {
			if (Note.ADMIN_TO_PARTICIPANT_TYPE_CODE.equals(loanNote
					.getNoteTypeCode())) {
				if (LoanStateEnum.DRAFT.getStatusCode()
						.equals(loan.getStatus())) {
					loan.setCurrentParticipantNote(loanNote);
				} else {
					loan.getPreviousParticipantNotes().add(loanNote);
				}
			} else if (Note.ADMIN_TO_ADMIN_TYPE_CODE.equals(loanNote
					.getNoteTypeCode())) {
				if (LoanStateEnum.DRAFT.getStatusCode()
						.equals(loan.getStatus())) {
					loan.setCurrentAdministratorNote(loanNote);
				} else {
					loan.getPreviousAdministratorNotes().add(loanNote);
				}
			}
		}

		List<ManagedContent> contents = managedContentDao.read(submissionId,
				contractId, userProfileId);
		loan.setManagedContents(contents);

		// Retrieve AtRiskDetailsVO object from DISTRIBUTION_AT_RISK table.
		
		AtRiskDetailsInputVO atRiskDetils = new AtRiskDetailsInputVO();
		atRiskDetils.setContractId(loan.getContractId());
		atRiskDetils.setProfileId(loan.getParticipantProfileId());
		atRiskDetils.setSubmissionId(submissionId);
		atRiskDetils.setLoanOrWithdrawalReq(RequestType.LOAN);
		
		AtRiskDetailsVO atRiskDetailsVO = DistributionAtRiskDetailsDAO.retrieve(atRiskDetils);
		if (atRiskDetailsVO != null && atRiskDetailsVO.getAddresschange() != null
				&& atRiskDetailsVO.getAddresschange().getApprovalAddress() != null) {
			loan.setAtRiskAddress(atRiskDetailsVO.getAddresschange().getApprovalAddress());
			if(atRiskDetailsVO.getPasswordReset() != null){
				
				try {
					atRiskDetailsVO.setPasswordReset(AtRiskHandler.getInstance().getForgotPasswordFunction(atRiskDetils));
				} catch (SystemException e) {
					throw new LoanDaoException("Problem while calling the AtRiskHandler while calling getForgotPasswordFunction  " + submissionId, e);
				}
			}
			loan.setAtRiskDetailsVO(atRiskDetailsVO);
		}
	}

	public Loan read(Integer submissionId, Integer contractId,
			Integer userProfileId) throws DistributionServiceException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Loan returnVo = null;
		String sql = LoanDaoSql.SQL_SELECT_LOAN;
		if (contractId != null) {
			sql += LoanDaoSql.SQL_WHERE_SUBMISSION_CONTRACT_ID;
		} else {
			sql += LoanDaoSql.SQL_WHERE_SUBMISSION_ID;
		}
		try {
			conn = BaseDatabaseDAO.getDefaultConnection(CLASS_NAME,
					BaseDatabaseDAO.STP_DATA_SOURCE_NAME);
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, submissionId);
			if (contractId != null) {
				stmt.setInt(2, contractId);
			}
			rs = stmt.executeQuery();
			if (rs.next()) {
				returnVo = toLoan(rs);
				rs.close();
				stmt.close();
			}

		} catch (SQLException e) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ sql + " for newSubmissionId " + submissionId, e);
		} catch (SystemException e) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ sql + " for newSubmissionId " + submissionId, e);
		} finally {
			BaseDatabaseDAO.close(stmt, conn);
		}

		if (returnVo != null) {
			populateLoanDependentObjects(returnVo);
			returnVo.setLoginUserProfileId(userProfileId);
		}

		return returnVo;
	}

	private Loan toLoan(ResultSet rs) throws SQLException {
		Loan returnVo = new Loan();
		/*
		 * By default, data retrieved from EJB layer. The web container should
		 * change the data retriever to its own implementation.
		 */
		returnVo.setDataRetriever(new EjbLoanSupportDataRetriever());
		returnVo.setSubmissionId(rs.getInt("SUBMISSION_ID"));
		returnVo.setContractId(rs.getInt("CONTRACT_ID"));
		returnVo.setParticipantProfileId(rs.getInt("PROFILE_ID"));
		returnVo.setParticipantId(rs.getInt("PARTICIPANT_ID"));
		returnVo.setCreatedByRoleCode(rs.getString("CREATED_BY_ROLE_CODE"));
		returnVo.setLegallyMarriedInd(JdbcHelper.getBoolean(rs,
				"LEGALLY_MARRIED_IND"));
		returnVo.setLoanType(rs.getString("LOAN_TYPE_CODE"));
		returnVo.setLoanReason(rs.getString("LOAN_REASON_EXPLANATION"));
		returnVo.setRequestDate(JdbcHelper.getUtilDate(rs, "REQUEST_DATE"));
		returnVo.setExpirationDate(JdbcHelper
				.getUtilDate(rs, "EXPIRATION_DATE"));
		returnVo.setEffectiveDate(JdbcHelper.getUtilDate(rs,
				"LOAN_EFFECTIVE_DATE"));
        returnVo.setEffectiveDateOriginalDBValue(returnVo.getEffectiveDate());
		returnVo.setMaturityDate(JdbcHelper.getUtilDate(rs,
				"LOAN_MATURITY_DATE"));
		returnVo.setMaturityDateOriginalDBValue(returnVo.getMaturityDate());
		returnVo.setFirstPayrollDate(JdbcHelper.getUtilDate(rs,
				"FIRST_PAYROLL_DATE"));
		returnVo.setMaximumAmortizationYears(rs
				.getInt("MAX_AMORTIZATION_YEARS"));
		returnVo.setDefaultProvision(rs.getString("DEFAULT_PROVISION"));
		returnVo.setMaxBalanceLast12Months(rs
				.getBigDecimal("MAX_OS_LOAN_BAL_LAST12MTHS_AMT"));
		returnVo.setOutstandingLoansCount(rs.getInt("OUTSTANDING_LOANS_COUNT"));
		returnVo.setCurrentOutstandingBalance(rs
				.getBigDecimal("CURR_OUTSTANDING_LOAN_BAL_AMT"));
		returnVo.setLastFeeChangedByTpaProfileId(JdbcHelper.getInteger(rs,
				"LAST_FEE_CHNG_BY_TPA_PROF_ID"));
		returnVo.setLastFeeChangedWasPlanSponsorUserInd(JdbcHelper.getBoolean(
				rs, "LAST_FEE_CHNG_WAS_PS_USER_IND"));
		returnVo.setCreatedId(rs.getInt("CREATED_USER_PROFILE_ID"));
		returnVo.setCreated(rs.getTimestamp("CREATED_TS"));
		returnVo.setContractLoanExpenseMarginPct(rs
				.getBigDecimal("LOAN_EXPENSE_MARGIN_PCT"));
		returnVo.setMinimumLoanAmount(rs.getBigDecimal("MINIMUM_LOAN_AMT"));
		returnVo.setMaximumLoanAmount(rs.getBigDecimal("MAXIMUM_LOAN_AMT"));
		returnVo.setMaximumLoanPercentage(rs.getBigDecimal("MAXIMUM_LOAN_PCT"));
		returnVo.setContractLoanMonthlyFlatFee(rs
				.getBigDecimal("LOAN_MONTHLY_FLAT_FEE_AMT"));
		returnVo.setAtRiskInd(rs.getString("AT_RISK_IND"));
		returnVo.setLastUpdatedId(rs.getInt("LAST_UPDATED_USER_PROFILE_ID"));
		returnVo.setSpousalConsentReqdInd(rs.getString("SPOUSAL_CONSENT_REQD_IND"));
		returnVo.setLastUpdated(rs.getTimestamp("LAST_UPDATED_TS"));
		returnVo.setStatus(rs.getString("PROCESS_STATUS_CODE"));
		returnVo.setPreviousStatus(returnVo.getStatus());
		returnVo.setApplyIrs10KDollarRuleInd(JdbcHelper.getBoolean(rs,
				"APPLY_IRS_10K_DOLLAR_RULE_IND"));
		return returnVo;
	}

	/**
	 * Retrieve the process status code from the submission case table for a
	 * given submission
	 * 
	 * @param submissionId
	 *            The submission id
	 * @return The process status code of the submission
	 * @throws DistributionServiceException
	 *             thrown if an error occurs
	 */
	private String getProcessStatusCode(Integer submissionId)
			throws DistributionServiceException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String processedStatusCode = null;

		try {
			conn = getDefaultConnection(LoanDao.CLASS_NAME,
					SUBMISSION_DATA_SOURCE_NAME);
			stmt = conn
					.prepareStatement(LoanDaoSql.SQL_SELECT_PROCESS_STATUS_CODE);
			stmt.setInt(1, submissionId.intValue());

			stmt.execute(); // submission detail result set

			rs = stmt.getResultSet();
			
			if (rs != null) {
			    
    			if (rs.next()) {
    				processedStatusCode = rs.getString("PROCESS_STATUS_CODE");
    			}
    			
			}
			
		} catch (SQLException e) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ LoanDaoSql.SQL_SELECT_PROCESS_STATUS_CODE
					+ " for newSubmissionId " + submissionId, e);
		} catch (SystemException e) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ LoanDaoSql.SQL_SELECT_PROCESS_STATUS_CODE
					+ " for newSubmissionId " + submissionId, e);
		} finally {
			close(stmt, conn);
		}

		return processedStatusCode;
	}

	/**
	 * Retrieve the list of Submission IDs to be marked as expired
	 * 
	 * @param checkDate
	 *            The date (java.util.Date) used to check if records are expired
	 * @return List of contract IDs/submission IDs to be marked as expired. If
	 *         no requests the returned Collection is empty.
	 */
	public Collection<Pair<Integer, Integer>> getExpiringLoans(
			java.util.Date checkDate) throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("checkDate -> "
					+ java.text.SimpleDateFormat.getDateInstance().format(
							checkDate));
		}

		Collection<Pair<Integer, Integer>> expiringLoans = new ArrayList<Pair<Integer, Integer>>();
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;

		try {
			conn = getDefaultConnection(CLASS_NAME, SUBMISSION_DATA_SOURCE_NAME);
			if (logger.isDebugEnabled()) {
				logger.debug("Executing SQL_SELECT_EXPIRING_REQUESTS Query: "
						+ LoanDaoSql.SQL_SELECT_EXPIRING_REQUESTS);
			}
			stmt = conn
					.prepareStatement(LoanDaoSql.SQL_SELECT_EXPIRING_REQUESTS);
			stmt.setDate(1, new Date(checkDate.getTime()));
			rs = stmt.executeQuery();
			while (rs.next()) {
				Integer submissionId = rs.getInt("submission_id");
				Integer contractId = rs.getInt("contract_id");
				Pair<Integer, Integer> pair = new Pair<Integer, Integer>(
						contractId, submissionId);
				expiringLoans.add(pair);
			}

		} catch (SQLException e) {
			throw new SystemException(e, "checkDate: "
					+ java.text.SimpleDateFormat.getDateInstance().format(
							checkDate));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				logger
						.error("could not close connection in withdrawalDAO.getPendingReviewApproveCount()");
			}
		}
		return expiringLoans;
	}

	/**
	 * Marks a specific Submission IDs as expired.
	 * 
	 * @param submissionId
	 *            ID of submission to be marked as expired.
	 * @param userProfileId -
	 *            User Profile ID of the user that changed the record (could be
	 *            system).
	 * 
	 * @return boolean - True if one row was updated, false otherwise.
	 * 
	 * @throws SystemException -
	 *             If an exception occurs.
	 */
	public boolean expireLoan(final Integer submissionId,
			final Integer userProfileId, Timestamp lastUpdatedTs)
			throws SystemException {
		return expireDistributionRequest(submissionId, userProfileId,
				LoanStateEnum.EXPIRED.getStatusCode(), lastUpdatedTs);
	}

	/**
	 * Checks if the participant has pending loan request (PENDING REVIEW,
	 * PENDING ACCEPTANCE, PENDING APPROVAL, APPROVED)
	 * 
	 * @param contractId
	 * @param profileId
	 * @return
	 * @throws LoanDaoException
	 */
	public List<Integer> getPendingRequestSubmissionIds(Integer contractId,
			Integer profileId) throws LoanDaoException {

		SelectMultiFieldMultiRowQueryHandler handler = new SelectMultiFieldMultiRowQueryHandler(
				SUBMISSION_DATA_SOURCE_NAME, LoanDaoSql.SQL_PENDING_REQUEST,
				new Class[] { Integer.class });

		try {
			Object[][] rows = (Object[][]) handler.select(new Object[] {
					profileId, contractId });
			List<Integer> ids = new ArrayList<Integer>();
			if (rows != null) {
				for (int i = 0; i < rows.length; i++) {
					Object[] columns = rows[i];
					ids.add((Integer) columns[0]);
				}
			}
			return ids;
		} catch (DAOException e) {
			throw new LoanDaoException("Cannot retrieve data contract ID ["
					+ contractId + "] profile ID [" + profileId + "]", e);
		}
	}

	/**
	 * Checks if the participant has a draft request that is created by the
	 * given created user profile ID.
	 * 
	 * @param contractId
	 * @param profileId
	 * @return
	 * @throws LoanDaoException
	 */
	public boolean hasDraftRequest(Integer contractId,
			Integer participantProfileId, Integer createdUserProfileId)
			throws LoanDaoException {

		SelectSingleValueQueryHandler handler = new SelectSingleValueQueryHandler(
				SUBMISSION_DATA_SOURCE_NAME,
				LoanDaoSql.SQL_COUNT_DRAFT_REQUEST_BY_PARTICIPANT_AND_CREATED_USER,
				Integer.class);

		try {
			Integer count = (Integer) handler.select(new Object[] {
					participantProfileId, createdUserProfileId, contractId });
			return count.intValue() > 0;

		} catch (DAOException e) {
			throw new LoanDaoException("Cannot retrieve data contract ID ["
					+ contractId + "] participantProfileId ["
					+ participantProfileId + "] createdUserProfileId ["
					+ participantProfileId + "]", e);
		}
	}

	/**
	 * Retrieve the list of loan requests that are about to expire 
	 * 
	 * @param checkDate
	 *            The date (java.util.Date) used to check if records are expired
	 * @return List of loan requests 
	 */
	public List<Loan> getAboutToExpireLoans(
			java.util.Date checkDate) throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("checkDate -> "
					+ java.text.SimpleDateFormat.getDateInstance().format(
							checkDate));
		}

		List<Loan> aboutToexpireLoans = new ArrayList<Loan>();
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;

		try {
			conn = getDefaultConnection(CLASS_NAME, SUBMISSION_DATA_SOURCE_NAME);
			if (logger.isDebugEnabled()) {
				logger.debug("Executing SQL_SELECT_ABOUT_TO_EXPIRE_REQUESTS Query: "
						+ LoanDaoSql.SQL_SELECT_ABOUT_TO_EXPIRE_REQUESTS);
			}
			stmt = conn
					.prepareStatement(LoanDaoSql.SQL_SELECT_ABOUT_TO_EXPIRE_REQUESTS);
			stmt.setDate(1, new Date(checkDate.getTime()));
			rs = stmt.executeQuery();
			while (rs.next()) {
				Loan loan = new Loan();
				loan.setSubmissionId(rs.getInt("submission_id"));
				loan.setContractId(rs.getInt("contract_id"));
				loan.setCreatedByRoleCode(rs.getString("created_by_role_code"));
				loan.setExpirationDate(rs.getDate("expiration_date"));
				loan.setParticipantProfileId(rs.getInt("profile_id"));
				loan.setStatus(rs.getString("process_status_code"));
				aboutToexpireLoans.add(loan);
			}

		} catch (SQLException e) {
			throw new SystemException(e, "checkDate: "
					+ java.text.SimpleDateFormat.getDateInstance().format(
							checkDate));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				logger
						.error("could not close connection in LoanDao.getAboutToExpireLoans()");
			}
		}
		return aboutToexpireLoans;
	}
	
	
	
	/**Retrieves record count for UOL for the given contract id.
	 * @param contractId
	 * @return
	 * @throws LoanDaoException
	 */
	public Integer getUOLCount(Integer contractId)throws LoanDaoException {

		SelectSingleValueQueryHandler handler = new SelectSingleValueQueryHandler(
				CUSTOMER_DATA_SOURCE_NAME,
				LoanDaoSql.SQL_FETCH_UOL_COUNT,
				Integer.class);

		try {			
			Integer uolCount = (Integer) handler.select(new Object[] {
					contractId });
			return uolCount;

		} catch (DAOException e) {
			throw new LoanDaoException("Cannot retrieve data contract ID :"
					+ contractId 
					, e);
		}
	}
	
}
