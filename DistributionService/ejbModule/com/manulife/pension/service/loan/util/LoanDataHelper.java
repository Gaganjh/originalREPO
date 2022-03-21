package com.manulife.pension.service.loan.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.manulife.pension.cma.domain.DeployedEnvironment;
import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.content.view.ContentText;
import com.manulife.pension.delegate.AccountServiceDelegate;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.ContractDoesNotExistException;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.account.dao.AccountBalanceTotalLoanDAO;
import com.manulife.pension.service.account.dao.ParticipantBenefitBaseDAO;
import com.manulife.pension.service.account.valueobject.ParticipantGiflData;
import com.manulife.pension.service.contract.dao.ContractServiceFeatureDAO;
import com.manulife.pension.service.contract.util.PlanConstants;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.dao.DaoConstants;
import com.manulife.pension.service.distribution.exception.DistributionServiceDaoException;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.loan.LoanDefaults;
import com.manulife.pension.service.loan.dao.ApolloDao;
import com.manulife.pension.service.loan.dao.ContentDao;
import com.manulife.pension.service.loan.dao.LoanDao;
import com.manulife.pension.service.loan.dao.LoanSupportDao;
import com.manulife.pension.service.loan.domain.LoanConstants;
import com.manulife.pension.service.loan.exception.LoanDaoException;
import com.manulife.pension.service.loan.valueobject.LoanEventData;
import com.manulife.pension.service.loan.valueobject.LoanMoneyType;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.vesting.EmployeeVestingInformation;
import com.manulife.pension.service.vesting.MoneyTypeVestingPercentage;
import com.manulife.pension.service.vesting.PlanVestingParameterBundle;
import com.manulife.pension.service.vesting.VestingEngine;
import com.manulife.pension.service.vesting.VestingException;
import com.manulife.pension.service.vesting.VestingRetriever;
import com.manulife.pension.service.vesting.dao.Db2VestingDAO;
import com.manulife.pension.service.vesting.dao.VestingDAO;
import com.manulife.pension.service.withdrawal.dao.WithdrawalDao;
import com.manulife.pension.service.withdrawal.domain.WithdrawalStateEnum;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.BusinessCalendar;
import com.manulife.pension.util.BusinessCalendarFactory;
import com.manulife.pension.util.Pair;

/**
 * This helper class retrieves loan related data from the database.
 * 
 */
public final class LoanDataHelper extends BaseSerializableCloneableObject {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(LoanDataHelper.class);

	private static final boolean DEBUG_APOLLO_PERF = false;

	/**
	 * Default Constructor.
	 */
	LoanDataHelper() {
	}

	public boolean hasPendingWithdrawalRequest(Integer contractId,
			Integer participantProfileId) {
		boolean participantHasAnotherRequest = false;
		boolean participantHasReadyForEntryRequest = false;

		try {
			// expected processing date >= current business date
			final Collection<WithdrawalRequest> withdrawalRequests = new WithdrawalDao()
					.getWithdrawalRequests(participantProfileId, contractId);

			// Check for any existing requests (other than itself) in pending
			// review, pending
			// approval or approved state
			for (final WithdrawalRequest request : withdrawalRequests) {
				final String existingRequestStatusCode = request
						.getStatusCode();
				if ((StringUtils.equals(existingRequestStatusCode,
						WithdrawalStateEnum.PENDING_REVIEW.getStatusCode())
						|| StringUtils.equals(existingRequestStatusCode,
								WithdrawalStateEnum.PENDING_APPROVAL
										.getStatusCode()) || StringUtils
						.equals(existingRequestStatusCode,
								WithdrawalStateEnum.APPROVED.getStatusCode()))) {
					participantHasAnotherRequest = true;
					break;
				}
			}

			if (!participantHasAnotherRequest) {

				Date currentBusinessDate = DateUtils.truncate(new Date(),
						Calendar.DATE);

				for (final WithdrawalRequest request : withdrawalRequests) {
					if (request.getStatusCode()
							.equals(
									WithdrawalStateEnum.READY_FOR_ENTRY
											.getStatusCode())) {
						if (request.getExpectedProcessingDate() == null) {
							throw new IllegalStateException(
									"did not find a valid processing date");
						}
						Date expectedProcessingDate = new Date(request
								.getExpectedProcessingDate().getTime());
						expectedProcessingDate = DateUtils.truncate(
								expectedProcessingDate, Calendar.DATE);
						if (expectedProcessingDate.getTime() >= currentBusinessDate
								.getTime()) {
							participantHasReadyForEntryRequest = true;
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			throw ExceptionHandlerUtility.wrap(e);
		}

		return participantHasAnotherRequest
				|| participantHasReadyForEntryRequest;
	}

	public Pair<List<LoanMoneyType>, EmployeeVestingInformation> getParticipantMoneyTypesForLoans(
			Integer contractId, Integer participantProfileId) {

		try {
			LoanSupportDao loanSupportDao = getLoanSupportDao();

			List<LoanMoneyType> participantMoneyTypes = loanSupportDao
					.getParticipantMoneyTypesForLoans(contractId,
							participantProfileId);

			EmployeeVestingInformation vestingInformation = getEmployeeVestingInformation(
					contractId, participantProfileId);

			/*
			 * Fill in any missing money types.
			 */
			for (Iterator<LoanMoneyType> it = participantMoneyTypes.iterator(); it
					.hasNext();) {
				LoanMoneyType participantMoneyType = it.next();
				MoneyTypeVestingPercentage moneyTypeVestingPercentage = (MoneyTypeVestingPercentage) vestingInformation
						.getMoneyTypeVestingPercentages().get(
								participantMoneyType.getMoneyTypeId());
				participantMoneyType
						.setVestingPercentage(moneyTypeVestingPercentage
								.getPercentage());
				participantMoneyType
						.setVestingPercentageUpdateable(moneyTypeVestingPercentage
								.isChangePermitted());
			}
			
			Pair<List<LoanMoneyType>, EmployeeVestingInformation> result = new Pair<List<LoanMoneyType>, EmployeeVestingInformation>(
					participantMoneyTypes, vestingInformation);
			return result;
		} catch (Exception e) {
			throw ExceptionHandlerUtility.wrap(e);
		}

	}

	public EmployeeVestingInformation getEmployeeVestingInformation(
			Integer contractId, Integer participantProfileId) {
		try {
			VestingDAO vestingDao = null;

			try {
				vestingDao = new Db2VestingDAO(
						BaseDatabaseDAO
								.getDataSource(DaoConstants.DataSourceJndiName.CUSTOMER_SERVICE));

			} catch (DAOException e) {
				throw new DistributionServiceDaoException(
						"Cannot instantiate loan support dao", e);
			}

			EmployeeVestingInformation vestingInformation = null;

			try {
				VestingEngine vestingEngine = new VestingEngine(vestingDao);
				vestingInformation = vestingEngine
						.getEmployeeVestingInformation(contractId, Long
								.valueOf(participantProfileId.longValue()),
								new Date(),
								VestingRetriever.PARAMETER_USAGE_CODE_UNUSED);
			} catch (VestingException e) {
				throw new DistributionServiceDaoException(
						"Cannot get vesting information from VestingEngine", e);
			}
			return vestingInformation;

		} catch (Exception e) {
			throw ExceptionHandlerUtility.wrap(e);
		}

	}

	/**
	 * Returns whether Apollo is available or not.
	 * 
	 * @return
	 */
	public boolean isApolloAvailable() {
		AccountServiceDelegate delegate = AccountServiceDelegate.getInstance();
		try {
			return delegate.isAPOLLOAvailable(null);
		} catch (Exception e) {
			// If there is an exception checking Apollo's availability, we just
			// assume that it's not available.
			return false;
		}
	}

	public BusinessCalendar getBusinessCalendar() {
		try {
			DataSource mrlDataSource;
			mrlDataSource = BaseDatabaseDAO
					.getDataSource(BaseDatabaseDAO.MANULIFE_REUSABLE_LIBRARY_DATA_SOURCE_NAME);
			return BusinessCalendarFactory.getInstance(mrlDataSource);
		} catch (Exception e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	public ContentText getContentTextByKey(String applicationId,
			Location location, Integer key) throws LoanDaoException {

		try {
			/*
			 * Retrieve the isLive indicator for the current application. Notice
			 * that this isLive indicator may be different from the given
			 * application ID's isLive indicator.
			 */
			Boolean isLive = Boolean.valueOf(DeployedEnvironment.instance()
					.getEnvironmentVariable(
							DeployedEnvironment.IS_LIVE_ENV_VAR_NAME));
			String cmaJndiName = null;
			String cmaSchemaName = null;

			if (GlobalConstants.PSW_APPLICATION_ID.equals(applicationId)) {
				cmaJndiName = DaoConstants.DataSourceJndiName.PSW_CMA;
				cmaSchemaName = DaoConstants.SchemaName.PSW_CMA;
			} else if (GlobalConstants.EZK_APPLICATION_ID.equals(applicationId)) {
				cmaJndiName = DaoConstants.DataSourceJndiName.EZK_CMA;
				cmaSchemaName = DaoConstants.SchemaName.EZK_CMA;
			}

			ContentDao contentDao = new ContentDao();

			Set<ContentText> contentTextSet = contentDao.getContentTextByKey(
					cmaJndiName, cmaSchemaName, key, isLive, true);
			Set<ContentText> result = null;
			if (Location.US.equals(location) || Location.USA.equals(location)) {
				result = contentDao.getUsContentText(contentTextSet);
			} else {
				result = contentDao.getNyContentText(contentTextSet);
			}
			if (result.size() > 0) {
				return result.iterator().next();
			}

			return null;
		} catch (DAOException e) {
			throw new LoanDaoException("Problem with locating content", e);
		}
	}

	public ContentText getContentTextById(String applicationId,
			Location location, Integer id) throws LoanDaoException {

		try {
			String cmaJndiName = null;
			String cmaSchemaName = null;

			if (GlobalConstants.PSW_APPLICATION_ID.equals(applicationId)) {
				cmaJndiName = DaoConstants.DataSourceJndiName.PSW_CMA;
				cmaSchemaName = DaoConstants.SchemaName.PSW_CMA;
			} else if (GlobalConstants.EZK_APPLICATION_ID.equals(applicationId)) {
				cmaJndiName = DaoConstants.DataSourceJndiName.EZK_CMA;
				cmaSchemaName = DaoConstants.SchemaName.EZK_CMA;
			}

			ContentDao contentDao = new ContentDao();

			Set<ContentText> contentTextSet = contentDao.getContentTextById(
					cmaJndiName, cmaSchemaName, id);
			Set<ContentText> result = null;
			if (Location.US.equals(location) || Location.USA.equals(location)) {
				result = contentDao.getUsContentText(contentTextSet);
			} else {
				result = contentDao.getNyContentText(contentTextSet);
			}
			if (result.size() > 0) {
				return result.iterator().next();
			}

			return null;
		} catch (DAOException e) {
			throw new LoanDaoException("Problem with locating content", e);
		}
	}

	public LoanParticipantData getLoanParticipantData(Integer contractId,
			Integer participantProfileId) {

		LoanParticipantData loanParticipantData = new LoanParticipantData();
		try {

			Employee employee = EmployeeServiceDelegate.getInstance(
					new BaseEnvironment().getAppId()).getEmployeeByProfileId(
					new Long(participantProfileId), contractId, new Date());
			if (employee != null && loanParticipantData != null) {
				loanParticipantData.setParticipantId(employee
						.getParticipantId().intValue());
				loanParticipantData.setFirstName(employee.getEmployeeDetailVO()
						.getFirstName());
				loanParticipantData.setLastName(employee.getEmployeeDetailVO()
						.getLastName());
				loanParticipantData.setMiddleInitial(employee
						.getEmployeeDetailVO().getMiddleInitial());
				loanParticipantData.setLegallMarriedInd(employee
						.getEmployeeDetailVO().getLegallyMariedIndicator());
				loanParticipantData.setEmploymentStatusCode(employee
						.getEmployeeDetailVO().getEmploymentStatusCode());
				loanParticipantData.setSsn(employee.getEmployeeDetailVO()
						.getSocialSecurityNumber());
				loanParticipantData.setParticipantStatusCode(employee
						.getParticipantContract().getParticipantStatusCode());
				loanParticipantData.setEmployeeNumber(employee
						.getEmployeeDetailVO().getEmployeeId());
				if (employee.getAddressVO() != null) {
					loanParticipantData.setAddressLine1(employee.getAddressVO()
							.getAddressLine1());
					loanParticipantData.setAddressLine2(employee.getAddressVO()
							.getAddressLine2());
					loanParticipantData.setCity(employee.getAddressVO()
							.getCity());
					loanParticipantData.setStateCode(employee.getAddressVO()
							.getStateCode());
					loanParticipantData.setCountry(employee.getAddressVO()
							.getCountry());
					loanParticipantData.setZipCode(employee.getAddressVO()
							.getZipCode());
				}

				/**
				 * Populate loan balances (outstanding loan balance, highest
				 * outstanding balance within 12 months, etc.)
				 */
				AccountBalanceTotalLoanDAO totalLoanDAO = new AccountBalanceTotalLoanDAO();
				LoanSupportDao loanSupportDao = new LoanSupportDao(
						BaseDatabaseDAO
								.getDataSource(DaoConstants.DataSourceJndiName.CUSTOMER_SERVICE));

				/*
				 * The following section of code is copied from
				 * ParticipantLoanEntityBean.
				 */
				BigDecimal outstandingLoanBalance = null;
				BigDecimal highestOutstandingLoanBalanceWithin12Months = null;

				outstandingLoanBalance = totalLoanDAO.getTotalOutstandingLoan(
						String.valueOf(participantProfileId), String
								.valueOf(contractId));

				highestOutstandingLoanBalanceWithin12Months = totalLoanDAO
						.getTotalHighestLoanBalWithinLast12Months(String
								.valueOf(participantProfileId), String
								.valueOf(contractId));

				if (outstandingLoanBalance
						.compareTo(highestOutstandingLoanBalanceWithin12Months) > 0) {
					loanParticipantData
							.setCurrentOutstandingBalance(outstandingLoanBalance);
					loanParticipantData
							.setMaxBalanceLast12Months(outstandingLoanBalance);
				} else {
					loanParticipantData
							.setCurrentOutstandingBalance(outstandingLoanBalance);
					loanParticipantData
							.setMaxBalanceLast12Months(highestOutstandingLoanBalanceWithin12Months);
				}

				/**
				 * Check if participant has positive PBA balance.
				 */
				boolean positivePbaMoneyTypeBalance = loanSupportDao
						.isPositivePbaBalance(contractId, participantProfileId);

				loanParticipantData
						.setPositivePbaMoneyTypeBalance(positivePbaMoneyTypeBalance);

				/**
				 * Set the outstanding loan count. All entries in
				 * PARTICIPANT_LOAN table are considered outstanding. So, we
				 * just need to return the count of the result.
				 */
				int outstandingLoanCount = loanSupportDao
						.getNumberOfOutstandingLoans(contractId,
								loanParticipantData.getParticipantId());
				loanParticipantData
						.setOutstandingLoansCount(outstandingLoanCount);

				LoanDao loanDao = new LoanDao();
				List<Integer> pendingRequests = loanDao
						.getPendingRequestSubmissionIds(contractId,
								participantProfileId);
				loanParticipantData.setPendingRequests(pendingRequests);

				if (DEBUG_APOLLO_PERF)
					logger.error("Before isApolloAvailable");
				if (isApolloAvailable()) {
					if (DEBUG_APOLLO_PERF)
						logger
								.error("After isApolloAvailable, before getNumberOfCompletedLoanTransaction");
					try {
						ApolloDao apolloDao = new ApolloDao();
						Integer participantId = loanParticipantData
								.getParticipantId();

						int count1 = apolloDao
								.getNumberOfCompletedLoanTransaction(
										contractId, participantId);
						if (DEBUG_APOLLO_PERF)
							logger
									.error("After getNumberOfCompletedLoanTransaction, before getNumberOfPendingLoanTransaction");

						int count2 = apolloDao
								.getNumberOfPendingLoanTransaction(contractId,
										participantId);

						if (DEBUG_APOLLO_PERF)
							logger
									.error("After getNumberOfPendingLoanTransaction");

						if (count1 > 0 || count2 > 0) {
							loanParticipantData
									.setForwardUnreversedLoanTransactionExist(true);
						}
					} catch (Exception e) {
						/*
						 * Sometimes, Apollo reports available but we still
						 * don't have enough CPU cycles to fulfil the request.
						 * So, we may still get a TX timeout. In this case, we
						 * simply ignore it and continue.
						 */
					}
				}

				boolean hasPendingWithdrawalRequest = hasPendingWithdrawalRequest(
						contractId, participantProfileId);
				loanParticipantData
						.setPendingWithdrawalRequestExist(hasPendingWithdrawalRequest);

				// is participant gifl selected
				boolean giflFeatureSelected = isParticipantGiflSelected(
						contractId, loanParticipantData.getParticipantId());
				loanParticipantData.setGiflFeatureSelected(giflFeatureSelected);

			}
		} catch (Exception e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
		return loanParticipantData;
	}

	public LoanPlanData getLoanPlanData(Integer contractId) {
		LoanPlanData loanPlanData;
		try {
			loanPlanData = getLoanSupportDao().getLoanPlanData(contractId);
		} catch (DistributionServiceException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
		
		// Getting the Payroll Frequency CSF value
		ContractServiceFeature payrollFrequencyCSF;
		try {
			payrollFrequencyCSF = ContractServiceDelegate.getInstance().
			getContractServiceFeature(contractId, ServiceFeatureConstants.PAYROLL_FREQUENCY);
		} catch (ApplicationException ae) {
			throw ExceptionHandlerUtility.wrap(ae);
		} catch (SystemException se) {
			throw ExceptionHandlerUtility.wrap(se);
		}
		if(payrollFrequencyCSF != null){
			if(ServiceFeatureConstants.PAYROLL_FREQUENCY_SEMI_MONTHLY_CODE.equals(payrollFrequencyCSF.getValue())){
				loanPlanData.setPayrollFrequency(GlobalConstants.FREQUENCY_TYPE_SEMI_MONTHLY);
			}else{
				loanPlanData.setPayrollFrequency(payrollFrequencyCSF.getValue());
			}
		}


		// Invoke the VestingEngine to retrieve the Plan vesting service
		// crediting method.
		VestingDAO vestingDao = null;
		try {
			vestingDao = new Db2VestingDAO(
					BaseDatabaseDAO
							.getDataSource(DaoConstants.DataSourceJndiName.CUSTOMER_SERVICE));
		} catch (DAOException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}

		PlanVestingParameterBundle vestingParameterBundle;
		try {
			VestingEngine vestingEngine = new VestingEngine(vestingDao);
			vestingParameterBundle = vestingEngine
					.getPlanVestingParameterBundle(contractId, new Date());
			loanPlanData.setVestingServiceCreditMethod(vestingParameterBundle
					.getPlanVestingParameter().getCreditingMethod());
			loanPlanData.setVestingServiceFeature(vestingParameterBundle
					.getPlanVestingParameter().getVestingServiceFeature());
		} catch (VestingException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
		
		// Determine if all contract moneyTypes are fully vested.
		Map<String, Collection> lookupData;
        try {
            lookupData = ContractServiceDelegate.getInstance().getLookupData(contractId);
        } catch (SystemException e) {
            throw ExceptionHandlerUtility.wrap(e);
        }
		final List<MoneyTypeVO> contractMoneyTypes = (List<MoneyTypeVO>)lookupData.get(PlanConstants.MONEY_TYPES_BY_CONTRACT);
		loanPlanData.setAllMoneyTypesFullyVested(MoneyTypeVO.getAreAllMoneyTypesFullyVested(contractMoneyTypes));

		return loanPlanData;
	}

	public LoanSupportDao getLoanSupportDao() {
		try {
			return new LoanSupportDao(
					BaseDatabaseDAO
							.getDataSource(DaoConstants.DataSourceJndiName.CUSTOMER_SERVICE));
		} catch (DAOException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	/**
	 * Retrieve loan related settings from the database.
	 * 
	 * @param contractId
	 * @return
	 */
	public LoanSettings getLoanSettings(Integer contractId) {
		try {

			LoanSettings loanSettings = new LoanSettings();
			// retrieve LRK01 feature
			LoanSupportDao loanSupportDao = new LoanSupportDao(
					BaseDatabaseDAO
							.getDataSource(DaoConstants.DataSourceJndiName.CUSTOMER_SERVICE));
			boolean hasLrkFeatureOn = loanSupportDao
					.hasLoanRecordKeepingProductFeature(contractId);
			loanSettings.setLrk01(hasLrkFeatureOn);
			// Retrieving Loans CSF data
			ArrayList<String> serviceFeatureNameList = new ArrayList<String>();
			serviceFeatureNameList
					.add(ServiceFeatureConstants.ALLOW_LOANS_FEATURE);
			serviceFeatureNameList
					.add(ServiceFeatureConstants.PARTICIPANT_INITIATE_LOANS_FEATURE);
			Map csfMap = ContractServiceFeatureDAO.getContractServiceFeatures(
					contractId, serviceFeatureNameList);
			ContractServiceFeature loansCSF = null;
			ContractServiceFeature participantInitiateLoansCSF = null;

			if (csfMap != null) {
				loansCSF = (ContractServiceFeature) csfMap
						.get(ServiceFeatureConstants.ALLOW_LOANS_FEATURE);
				participantInitiateLoansCSF = (ContractServiceFeature) csfMap
						.get(ServiceFeatureConstants.PARTICIPANT_INITIATE_LOANS_FEATURE);
			}

			if (loansCSF != null) {
				loanSettings.setAllowOnlineLoans(ContractServiceFeature
						.internalToBoolean(loansCSF.getValue()).booleanValue());
				loanSettings
						.setAllowLoanPackageGeneration(ContractServiceFeature
								.internalToBoolean(
										loansCSF
												.getAttributeValue(ServiceFeatureConstants.ALLOW_LOANS_PACKAGE_TO_GENERATE))
								.booleanValue());
				loanSettings
						.setInitiatorCanApproveLoan(ContractServiceFeature
								.internalToBoolean(
										loansCSF
												.getAttributeValue(ServiceFeatureConstants.CREATOR_OF_LOANREQUEST_APPROVE))
								.booleanValue());
			}

			if (participantInitiateLoansCSF != null) {
				loanSettings
						.setAllowParticipantInitiateLoan(ContractServiceFeature
								.internalToBoolean(
										participantInitiateLoansCSF.getValue())
								.booleanValue());
			}

			return loanSettings;
			
		} catch (ContractDoesNotExistException cde) {
			return null;
		} catch (ApplicationException ex) {
			throw ExceptionHandlerUtility.wrap(ex);
		} catch (DAOException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (SystemException ex) {
			throw ExceptionHandlerUtility.wrap(ex);
		}
	}

	/**
	 * returns the participantId given the profile id
	 * 
	 * @param profileId
	 * @return participantId
	 */
	public Integer getParticipantIdByProfileId(Integer profileId) {
		try {
			LoanSupportDao supportDao = getLoanSupportDao();
			Integer participantId = supportDao
					.getParticipantIdByProfileId(profileId);
			return participantId;
		} catch (LoanDaoException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	/**
	 * returns the gifl status for the participant
	 * 
	 * @param contractId
	 * @param participantId
	 * @return true or false
	 */
	public boolean isParticipantGiflSelected(Integer contractId,
			Integer participantId) {
		// Obtain the participant GIFL status
		boolean status = false;
		try {
			ParticipantBenefitBaseDAO benefitBaseDAO = new ParticipantBenefitBaseDAO();
			ParticipantGiflData giflData = benefitBaseDAO
					.getParticipantGiflDataByParticipantIdAndContractId(String
							.valueOf(contractId), String.valueOf(participantId));
			if (giflData != null) {
				String giflDeselectionDate = giflData
						.getDisplayGiflDeselectionDate();
				if ((giflDeselectionDate != null)
						&& (String.valueOf(giflDeselectionDate)
								.equals(LoanDefaults.getGiflDeselectedDate()))) {
					status = true;
				}
			}
			return status;
		} catch (DAOException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	/**
	 * method that can return participant profile id for the given submission
	 * number & contract Id
	 * 
	 * @param submissionNumber
	 * @param contractId
	 * @return
	 */
	public Integer getParticipantProfileId(int submissionNumber, int contractId) {
		Integer profileId = null;
		try {
			LoanSupportDao supportDao = getLoanSupportDao();
			profileId = supportDao.getParticipantProfileIdForSubmission(
					submissionNumber, contractId);

		} catch (LoanDaoException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
		return profileId;
	}

	/**
	 * LoanEventHandlers requires the information about initiator, reviwer &
	 * approver of a loan request. This method is created to support the same.
	 * 
	 * @param submissionId
	 * @return
	 * @throws LoanDaoException
	 */
	public LoanEventData getLoanDataForEventMessages(int submissionId) {
		LoanEventData eventData = null;
		try {
			LoanSupportDao supportDao = getLoanSupportDao();
			eventData = supportDao.getLoanDataForEventMessages(submissionId);

		} catch (LoanDaoException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
		return eventData;
	}

	/**
	 * retrieves the name of the contract created for the use of event
	 * processing
	 * 
	 * @param contractId
	 * @return
	 * @throws LoanDaoException
	 */
	public String getContractName(int contractId) {
		String contractName;
		try {
			LoanSupportDao supportDao = getLoanSupportDao();
			contractName = supportDao.getContractName(contractId);

		} catch (LoanDaoException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
		return contractName;

	}

	public String getParticipantName(Long participantProfileId,
			Integer contractId) {
		String participantName = "";

		try {
			// Employee employee = EmployeeDAO.getEmployeeAllByProfile(
			// participantProfileId, contractId);

			Employee employee = EmployeeServiceDelegate.getInstance(
					new BaseEnvironment().getAppId()).getEmployeeByProfileId(
					participantProfileId, contractId, new Date());

			if ((employee != null) && (employee.getEmployeeDetailVO() != null)) {
				if (employee.getEmployeeDetailVO().getFirstName() != null)
					participantName += employee.getEmployeeDetailVO()
							.getFirstName();

				if (employee.getEmployeeDetailVO().getMiddleInitial() != null)
					participantName += employee.getEmployeeDetailVO()
							.getMiddleInitial();

				if (employee.getEmployeeDetailVO().getLastName() != null)
					participantName += employee.getEmployeeDetailVO()
							.getLastName();

			}
		} catch (Exception e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
		return participantName;
	}

	/**
	 * Check if country code is USA or blank/null (which implies USA).
	 * 
	 * @return
	 */
	public boolean isCountryUSA(String countryCode) {
		// Blank/null implies USA.
		if (StringUtils.isEmpty(countryCode)) {
			return true;
		}

		if (countryCode.equals(LoanConstants.USA)) {
			return true;
		}
		return false;
	}

}
