package com.manulife.pension.service.loan.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.distribution.dao.ActivityDetailDao;
import com.manulife.pension.service.distribution.dao.ActivityDynamicDetailDao;
import com.manulife.pension.service.distribution.dao.ActivitySummaryDao;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.log.DistributionEventEnum;
import com.manulife.pension.service.distribution.valueobject.ActivityDetail;
import com.manulife.pension.service.distribution.valueobject.ActivityDynamicDetail;
import com.manulife.pension.service.distribution.valueobject.ActivitySummary;
import com.manulife.pension.service.distribution.valueobject.DistributionAddress;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.PaymentInstruction;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.employee.EmployeeConstants.ParticipantStatus;
import com.manulife.pension.service.loan.LoanDefaults;
import com.manulife.pension.service.loan.LoanError;
import com.manulife.pension.service.loan.LoanErrorCode;
import com.manulife.pension.service.loan.LoanField;
import com.manulife.pension.service.loan.LoanMessage;
import com.manulife.pension.service.loan.dao.LoanDao;
import com.manulife.pension.service.loan.event.EventFactory;
import com.manulife.pension.service.loan.event.LoanEventGenerator;
import com.manulife.pension.service.loan.log.LoanEventLog;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanActivityDetail;
import com.manulife.pension.service.loan.valueobject.LoanActivitySummary;
import com.manulife.pension.service.loan.valueobject.LoanAddress;
import com.manulife.pension.service.loan.valueobject.LoanMoneyType;
import com.manulife.pension.service.loan.valueobject.LoanParameter;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPayee;
import com.manulife.pension.service.loan.valueobject.LoanPaymentInstruction;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.loan.valueobject.MoneyTypeComparator;
import com.manulife.pension.service.vesting.EmployeeVestingInformation;
import com.manulife.pension.service.vesting.MoneyTypeVestingPercentage;
import com.manulife.pension.service.vesting.util.VestingMessageType;
import com.manulife.pension.util.BusinessCalendar;
import com.manulife.pension.util.Pair;
import com.manulife.pension.util.log.EventLog;
import com.manulife.pension.util.log.EventLoggingHelper;

public abstract class DefaultLoanState implements LoanState {

	/**
	 * This map contains the mapping from a vesting error/message to the loan
	 * error code.
	 */
	private static final Map<VestingMessageType, LoanErrorCode> vestingLoanErrorCodeMap = new HashMap<VestingMessageType, LoanErrorCode>();

	static {
		// Here is where all the mappings go.
		vestingLoanErrorCodeMap.put(
				VestingMessageType.CREDITING_METHOD_IS_UNSPECIFIED,
				LoanErrorCode.VESTING_CREDITING_METHOD_IS_UNSPECIFIED);
		vestingLoanErrorCodeMap.put(
				VestingMessageType.VESTING_SCHEDULE_HAS_NOT_BEEN_SET_UP,
				LoanErrorCode.VESTING_SCHEDULE_HAS_NOT_BEEN_SET_UP);
		vestingLoanErrorCodeMap
				.put(
						VestingMessageType.PREVIOUS_YEARS_OF_SERVICE_AND_PLAN_YTD_HOURS_WORKED_NOT_PROVIDED,
						LoanErrorCode.VESTING_MISSING_EMPLOYEE_DATA);
		vestingLoanErrorCodeMap.put(VestingMessageType.HIRE_DATE_NOT_PROVIDED,
				LoanErrorCode.VESTING_MISSING_EMPLOYEE_DATA);
		vestingLoanErrorCodeMap.put(
				VestingMessageType.EMPLOYMENT_STATUS_NOT_PROVIDED,
				LoanErrorCode.VESTING_MISSING_EMPLOYEE_DATA);
		vestingLoanErrorCodeMap
				.put(
						VestingMessageType.MORE_RECENT_DATA_USED_FOR_CALCULATION,
						LoanErrorCode.VESTING_MORE_RECENT_DATA_USED_FOR_CALCULATION_CODE);
	}

    protected LoanStateContext getLoanStateContext(Loan loan) {
		return new LoanStateContext(loan);
	}

	public void validateToState(LoanStateEnum fromState, LoanStateEnum toState,
			LoanStateContext context) throws DistributionServiceException {
		LoanState toStateObj = LoanStateFactory.getState(toState);
		toStateObj.validate(fromState, toState, context);
	}

	public void validate(LoanStateEnum fromState, LoanStateEnum toState, LoanStateContext context)
			throws DistributionServiceException {
		validateOnSave(context, fromState, toState);
	}

	/**
	 * Populate initial messages/errors.
	 * 
	 * @param loan
	 * @param loanPlanData
	 * @param loanParticipantData
	 * @param loanSettings
	 * @throws DistributionServiceException
	 */
	void populateMessages(LoanStateContext context)
			throws DistributionServiceException {
		LoanDao loanDao = new LoanDao();
		LoanParticipantData loanParticipantData = context
				.getLoanParticipantData();
		LoanPlanData loanPlanData = context.getLoanPlanData();
		Loan loan = context.getLoan();
		LoanSettings loanSettings = context.getLoanSettings();

		List<LoanMessage> messages = loan.getMessages();
		LoanValidationHelper.validateAllowLoans(messages, loanSettings);

		if (loan.isAnyMoneyTypeNotAContractMoneyType()) {
            LoanError error = new LoanError(
                    LoanErrorCode.MONEY_TYPE_IS_NO_LONGER_A_CONTRACT_MONEY_TYPE);
            messages.add(error);
		}
		if (BigDecimal.ZERO.compareTo(loan.getCurrentAccountBalance()) >= 0) {
			LoanError error = new LoanError(
					LoanErrorCode.PARTICIPANT_CURRENT_ACCOUNT_BALANCE_IS_ZERO);
			messages.add(error);
		}
		if (!ParticipantStatus.ACTIVE.equals(loanParticipantData
				.getParticipantStatusCode())) {
			LoanError error = new LoanError(
					LoanErrorCode.PARTICIPANT_STATUS_IS_NOT_ACTIVE);
			messages.add(error);
		}
		if (loanParticipantData.isPositivePbaMoneyTypeBalance()) {
			LoanError error = new LoanError(
					LoanErrorCode.PARTICIPANT_HAS_POSITIVE_PBA_BALANCE);
			messages.add(error);
		}
		if (loanParticipantData.getOutstandingLoansCount() != null
				&& loanPlanData.getMaxNumberOfOutstandingLoans() != null
				&& loanParticipantData.getOutstandingLoansCount().intValue() >= loanPlanData
						.getMaxNumberOfOutstandingLoans().intValue()) {
			/*
			 * Both count will never be null.
			 */
			LoanError error = new LoanError(
					LoanErrorCode.PARTICIPANT_NUMBER_OF_LOANS_EXCEEDED);
			messages.add(error);
		}
		Integer contractId = loan.getContractId();
		if (loan.getLoginUserProfileId() != null
				&& loan.getSubmissionId() == null) {
			Integer participantProfileId = loan.getParticipantProfileId();
			boolean hasDraftRequest = loanDao.hasDraftRequest(contractId,
					participantProfileId, loan.getLoginUserProfileId());
			if (hasDraftRequest) {
				LoanError error = new LoanError(
						LoanErrorCode.PARTICIPANT_DRAFT_LOAN_REQUEST_EXISTS);
				messages.add(error);
			}
		}
		if (!loan.isParticipantInitiated()) {
		LoanValidationHelper.validateParticipantInstallmentWithdrawal(contractId,messages,
				loanParticipantData);
		}
		LoanValidationHelper.validateParticipantLoans(messages, loan,
				loanParticipantData);
		
		if (loanParticipantData.isGiflFeatureSelected()) {
			if (loan.isParticipantInitiated()) {
				LoanError error = new LoanError(
						LoanErrorCode.PARTICIPANT_HAS_GIFL_FOR_PARTICIPANT_INITIATED);
				messages.add(error);
			} else { // is external user initiated
				LoanError error = new LoanError(
						LoanErrorCode.PARTICIPANT_HAS_GIFL_FOR_EXTERNAL_USER_INITIATED);
				messages.add(error);
			}
		}

		if (loanParticipantData.isPendingWithdrawalRequestExist()) {
		
			LoanError error = new LoanError(
					LoanErrorCode.PARTICIPANT_HAS_WITHDRAWAL_REQUEST);
			messages.add(error);
		}

		if (loan.getExpirationDate() != null) {
			Calendar offset = Calendar.getInstance();
			offset.add(Calendar.DATE, 3);
			Date offsetDate = DateUtils.truncate(offset, Calendar.DATE)
					.getTime();
			Date expirationDate = DateUtils.truncate(loan.getExpirationDate(),
					Calendar.DATE);

			if (expirationDate.compareTo(offsetDate) < 0) {
				LoanError error = new LoanError(
						LoanErrorCode.LOAN_ABOUT_TO_EXPIRE);
				messages.add(error);
			}
		}

		EmployeeVestingInformation vestingInfo = loan
				.getEmployeeVestingInformation();

		if (vestingInfo == null) {
			// critical error
			LoanError error = new LoanError(
					LoanErrorCode.VESTING_SERVICE_CRITICAL_ERROR);
			messages.add(error);
		} else {
			if (CollectionUtils.isNotEmpty(vestingInfo.getErrors())) {
				for (final Iterator<VestingMessageType> iterator = vestingInfo
						.getErrors().iterator(); iterator.hasNext();) {
					final VestingMessageType vestingMessageType = iterator
							.next();
					LoanErrorCode errorCode = vestingLoanErrorCodeMap
							.get(vestingMessageType);
					if (errorCode == null) {
						errorCode = LoanErrorCode.VESTING_SERVICE_OTHER_NON_CRITICAL_ERROR;
					}
					LoanError error = new LoanError(errorCode);
					messages.add(error);
				}
			}
		}

		if (loan.getCurrentLoanParameter() != null
				&& loan.getCurrentLoanParameter().getLoanAmount() != null) {
			if (LoanStateEnum.PENDING_APPROVAL.getStatusCode().equals(
					loan.getStatus())) {
				BigDecimal requestedAmount = loan.getCurrentLoanParameter()
						.getLoanAmount();
				BigDecimal currentAccountBalance = loan
						.getCurrentAvailableAccountBalance();
				if (requestedAmount
						.compareTo(currentAccountBalance
								.add(LoanDefaults
										.getRequestedAmountOverAvailableAmountThreshold())) > 0) {
					LoanError error = new LoanError(
							LoanErrorCode.REQUESTED_AMOUNT_EXCEEDS_AVAILABLE_AMOUNT);
					messages.add(error);
				}
			}
		}

	}

	public Loan loanPackage(Loan loan) throws DistributionServiceException {
		throw new UnsupportedOperationException("Not implemented");
	}

	public void setAdditionalPlanAndContractData(Loan loan) {
		LoanStateContext context = getLoanStateContext(loan);
		LoanPlanData loanPlanData = context.getLoanPlanData();
		loan.setSpousalConsentReqdInd(loanPlanData.getSpousalConsentReqdInd());
		loan.setContractLoanExpenseMarginPct(loanPlanData
				.getContractLoanExpenseMarginPct());
		loan.setContractLoanMonthlyFlatFee(loanPlanData
				.getContractLoanMonthlyFlatFee());
		loan.setMaximumLoanAmount(loanPlanData.getMaximumLoanAmount());
		loan.setMinimumLoanAmount(loanPlanData.getMinimumLoanAmount());
		loan.setMaximumLoanPercentage(loanPlanData.getMaximumLoanPercentage());
	}

	public void populate(Loan loan) throws DistributionServiceException {
	}

	public Loan approve(Loan loan) throws DistributionServiceException {
		throw new UnsupportedOperationException("Not implemented");
	}

	public Loan complete(Loan loan) throws DistributionServiceException {
		throw new UnsupportedOperationException("Not implemented");
	}

	public Loan decline(Loan loan) throws DistributionServiceException {
        throw new UnsupportedOperationException("Not implemented");
	}

	public Loan expire(Loan loan) throws DistributionServiceException {
		throw new UnsupportedOperationException("Not implemented");
	}

	public Loan sendForAcceptance(Loan loan)
			throws DistributionServiceException {
		throw new UnsupportedOperationException("Not implemented");
	}

	public Loan sendForApproval(Loan loan) throws DistributionServiceException {
		throw new UnsupportedOperationException("Not implemented");
	}

	public Loan sendForReview(Loan loan) throws DistributionServiceException {
		throw new UnsupportedOperationException("Not implemented");
	}

	public Loan delete(Loan loan) throws DistributionServiceException {
		loan.setStatus(LoanStateEnum.DELETED.getStatusCode());
		LoanStateContext context = getLoanStateContext(loan);
        setCommonLoanValues(context);
		loan = save(context);
		if (loan.isOK()) {
		    if (!LoanStateEnum.DRAFT.getStatusCode().equals(
		            loan.getPreviousStatus())) {
	            LoanStateEnum fromState = LoanStateEnum.fromStatusCode(
	                    loan.getPreviousStatus());
	            saveActivityHistory(fromState, LoanStateEnum.DELETED, 
	                    context, loan.getLastUpdated());
		    }
		    if (LoanStateEnum.PENDING_APPROVAL.getStatusCode()
		            .equals(loan.getPreviousStatus())) {
	            logToMrl(DistributionEventEnum.DELETE_FROM_APPROVAL
	                    .getEventName(), context);
		        
		    } else {
	            logToMrl(DistributionEventEnum.DELETE_FROM_REVIEWED
	                    .getEventName(), context);
		    }

		    // Triggering LoanRequestDeleted Event
			LoanEventGenerator loanEventGenerator = EventFactory.getInstance()
					.getLoanEventGenerator(loan.getContractId(),
							loan.getSubmissionId(),
							loan.getLoginUserProfileId());
			loanEventGenerator.prepareAndSendDeletedEvent(loan
					.getParticipantProfileId(), loan.getPreviousStatus());

		} else {
		    loan.setStatus(loan.getPreviousStatus());
		}
		return loan;
	}

	public Loan reject(Loan loan) throws DistributionServiceException {
		throw new UnsupportedOperationException("Not implemented");
	}

	private String getActivitySummaryStatusCode(String statusCode) {
		if (LoanStateEnum.PENDING_REVIEW.getStatusCode().equals(statusCode)) {
			return ActivitySummary.SENT_FOR_REVIEW;
		}
		if (LoanStateEnum.PENDING_APPROVAL.getStatusCode().equals(statusCode)) {
			return ActivitySummary.SENT_FOR_APPROVAL;
		}
		if (LoanStateEnum.PENDING_ACCEPTANCE.getStatusCode().equals(statusCode)) {
			return ActivitySummary.SENT_FOR_ACCEPTANCE;
		}
		if (LoanStateEnum.APPROVED.getStatusCode().equals(statusCode)) {
			return ActivitySummary.APPROVED;
		}
		if (LoanStateEnum.DECLINED.getStatusCode().equals(statusCode)) {
			return ActivitySummary.DENIED;
		}
		if (LoanStateEnum.DELETED.getStatusCode().equals(statusCode)) {
			return ActivitySummary.DELETED;
		}
		if (LoanStateEnum.EXPIRED.getStatusCode().equals(statusCode)) {
			return ActivitySummary.EXPIRED;
		}
		if (LoanStateEnum.LOAN_PACKAGE.getStatusCode().equals(statusCode)) {
			return ActivitySummary.LOAN_PACKAGE_REQUESTED;
		}
		/*
		 * If we cannot find any match, use the original status code.
		 */
		return statusCode;
	}

	/**
	 * Inserts a record into the activity summary table.
	 * 
	 * @param loan
	 * @param updatedTs
	 * @throws DistributionServiceException
	 */
	protected void saveActivitySummary(Loan loan, Timestamp updatedTs)
			throws DistributionServiceException {
		if (loan.isStatusChange()) {
			ActivitySummaryDao summaryDao = new ActivitySummaryDao();
			ActivitySummary summary = new LoanActivitySummary();
			summary.setCreatedById(loan.getCreatedId());
			summary.setCreated(updatedTs);
			summary
					.setStatusCode(getActivitySummaryStatusCode(loan
							.getStatus()));
			summary.setSubmissionId(loan.getSubmissionId());
			Integer submissionId = loan.getSubmissionId();
			Integer contractId = loan.getContractId();
			Integer userProfileId = loan.getLoginUserProfileId();
			summaryDao.insert(submissionId, contractId, userProfileId, summary);
		}
	}

	public void saveActivityHistory(LoanStateEnum fromState,
			LoanStateEnum toState, LoanStateContext context, Timestamp updatedTs)
			throws DistributionServiceException {

		boolean savePaymentHistory = true;

		ActivityDetailDao detailDao = new ActivityDetailDao();
		ActivityDynamicDetailDao dynamicDetailDao = new ActivityDynamicDetailDao();

		Loan loan = context.getLoan();
		LoanPlanData loanPlanData = context.getLoanPlanData();

		Integer submissionId = loan.getSubmissionId();
		Integer contractId = loan.getContractId();
		Integer userProfileId = loan.getLastUpdatedId();

		List<String> typeCodes = new ArrayList<String>();
		typeCodes.add(ActivityDetail.TYPE_SAVED);

		if (LoanStateEnum.DRAFT.equals(fromState)
				&& (LoanStateEnum.PENDING_REVIEW.equals(toState)
						|| LoanStateEnum.LOAN_PACKAGE.equals(toState) || LoanStateEnum.PENDING_APPROVAL
						.equals(toState))) {
			typeCodes.add(ActivityDetail.TYPE_ORIGINAL);
			if (loan.isParticipantInitiated()) {
				savePaymentHistory = false;
			}
		}

		if (LoanStateEnum.PENDING_REVIEW.equals(toState)
				|| LoanStateEnum.PENDING_APPROVAL.equals(toState)
				|| (LoanStateEnum.DRAFT.equals(fromState)
		                && (LoanStateEnum.LOAN_PACKAGE.equals(toState)))) {
			typeCodes.add(ActivityDetail.TYPE_SYSTEM_OF_RECORD);
		}
		if (LoanStateEnum.PENDING_REVIEW.equals(fromState) 
		        && loan.isParticipantInitiated()) {
		    savePaymentHistory = false;
		}

        String paymentMethodCode = null;
        LoanPayee payee = null;
        if (loan.getRecipient() != null
                && loan.getRecipient().getPayees() != null) {
            payee = (LoanPayee) loan.getRecipient()
                    .getPayees().iterator().next();
            paymentMethodCode = payee.getPaymentMethodCode();
        }
        
		for (String typeCode : typeCodes) {
			ActivityDetail detail = null;

			List<ActivityDetail> details = new ArrayList<ActivityDetail>();
			List<ActivityDynamicDetail> dynamicDetails = new ArrayList<ActivityDynamicDetail>();

			// TPA loan issue fee.
			detail = new LoanActivityDetail();
			detail.setItemNumber(LoanField.TPA_LOAN_ISSUE_FEE
					.getActivityDetailItemNo());
			detail.setTypeCode(typeCode);
			BigDecimal fee = null;
			if (!typeCode.equals(ActivityDetail.TYPE_SYSTEM_OF_RECORD)) {
				if (loan.getFee() != null && loan.getFee().getValue() != null) {
					fee = loan.getFee().getValue();
				}
			} else {
				fee = loanPlanData.getContractLoanSetupFeeAmount();
			}

			if (fee != null) {
				detail.setValue(fee.setScale(LoanConstants.AMOUNT_SCALE)
						.toString());
			}
			detail.setLastUpdated(updatedTs);
			detail.setLastUpdatedById(userProfileId);
			details.add(detail);

			// Create a money type vesting percentage detail entry.
			detail = new LoanActivityDetail();
			detail.setItemNumber(LoanField.MONEY_TYPE_VESTING_PERCENTAGE_PREFIX
					.getActivityDetailItemNo());
			detail.setTypeCode(typeCode);
			detail.setValue(null);
			detail.setLastUpdated(updatedTs);
			detail.setLastUpdatedById(userProfileId);
			details.add(detail);

			// Now create a dynamic detail entry for each of the various vesting
			// percentage values associated with each money type.
			EmployeeVestingInformation vestingInfo = loan
					.getEmployeeVestingInformation();

			for (LoanMoneyType loanMoneyType : loan.getMoneyTypesWithAccountBalance()) {
				BigDecimal vestingPercentage = null;

				if (!typeCode.equals(ActivityDetail.TYPE_SYSTEM_OF_RECORD)) {
					vestingPercentage = loanMoneyType.getVestingPercentage();
				} else {
					/*
					 * Use system of record vesting percentage.
					 */
					MoneyTypeVestingPercentage moneyTypeVestingPercentage = (MoneyTypeVestingPercentage) vestingInfo
							.getMoneyTypeVestingPercentages().get(
									loanMoneyType.getMoneyTypeId());
					if (moneyTypeVestingPercentage != null) {
	                    vestingPercentage = moneyTypeVestingPercentage
                        .getPercentage();
					}
				}

				ActivityDynamicDetail dynamicDetail = new ActivityDynamicDetail();
				dynamicDetail.setTypeCode(typeCode);
				if (vestingPercentage != null) {
					dynamicDetail.setValue(vestingPercentage.setScale(
							LoanConstants.VESTING_PERCENTAGE_SCALE).toString());
				}
				dynamicDetail.setSecondaryName(loanMoneyType.getMoneyTypeId());
				dynamicDetail
						.setItemNumber(LoanField.MONEY_TYPE_VESTING_PERCENTAGE_PREFIX
								.getActivityDetailItemNo());
				dynamicDetail.setSecondaryNumber(1);
				dynamicDetail.setLastUpdated(updatedTs);
				dynamicDetail.setLastUpdatedById(userProfileId);
				dynamicDetails.add(dynamicDetail);
			}

			if (savePaymentHistory) {
				DistributionAddress address = null;

				if (typeCode.equals(ActivityDetail.TYPE_SYSTEM_OF_RECORD)) {
					LoanParticipantData loanParticipantData = context
							.getLoanParticipantData();
					/*
					 * If we're storing system of record, we use the data coming
					 * from LoanParticipantData.
					 */
					address = new LoanAddress();
					address.setAddressLine1(loanParticipantData
							.getAddressLine1());
					address.setAddressLine2(loanParticipantData
							.getAddressLine2());
					address.setCity(loanParticipantData.getCity());
					address.setStateCode(loanParticipantData.getStateCode());
					address.setCountryCode(loanParticipantData.getCountry());
					address.setZipCode(loanParticipantData.getZipCode());
				} else {
					/*
					 * Otherwise, we use what is in the recipient object.
					 */
					if (loan.getRecipient() != null
							&& loan.getRecipient().getAddress() != null) {
						address = (DistributionAddress) loan.getRecipient()
								.getAddress();
					} else {
						/*
						 * If the recipient object is empty or the address is
						 * empty, use null for all of the address fields.
						 */
						address = new LoanAddress();
						address.setAddressLine1(null);
						address.setAddressLine2(null);
						address.setCity(null);
						address.setStateCode(null);
						address.setCountryCode(null);
						address.setZipCode(null);
					}
				}

				// address line 1
				detail = new LoanActivityDetail();
				detail.setItemNumber(LoanField.ADDRESS_LINE1
						.getActivityDetailItemNo());
				detail.setTypeCode(typeCode);
				detail.setValue(address.getAddressLine1());
				detail.setLastUpdated(updatedTs);
				detail.setLastUpdatedById(userProfileId);
				details.add(detail);

				// address line 2
				detail = new LoanActivityDetail();
				detail.setItemNumber(LoanField.ADDRESS_LINE2
						.getActivityDetailItemNo());
				detail.setTypeCode(typeCode);
				detail.setValue(address.getAddressLine2());
				detail.setLastUpdated(updatedTs);
				detail.setLastUpdatedById(userProfileId);
				details.add(detail);

				// city
				detail = new LoanActivityDetail();
				detail.setItemNumber(LoanField.CITY.getActivityDetailItemNo());
				detail.setTypeCode(typeCode);
				detail.setValue(address.getCity());
				detail.setLastUpdated(updatedTs);
				detail.setLastUpdatedById(userProfileId);
				details.add(detail);

				// state
				detail = new LoanActivityDetail();
				detail.setItemNumber(LoanField.STATE.getActivityDetailItemNo());
				detail.setTypeCode(typeCode);
				detail.setValue(address.getStateCode());
				detail.setLastUpdated(updatedTs);
				detail.setLastUpdatedById(userProfileId);
				details.add(detail);

				// zip code
				detail = new LoanActivityDetail();
				detail.setItemNumber(LoanField.ZIP_CODE
						.getActivityDetailItemNo());
				detail.setTypeCode(typeCode);
				detail.setValue(address.getZipCode());
				detail.setLastUpdated(updatedTs);
				detail.setLastUpdatedById(userProfileId);
				details.add(detail);

				// country
				detail = new LoanActivityDetail();
				detail.setItemNumber(LoanField.COUNTRY
						.getActivityDetailItemNo());
				detail.setTypeCode(typeCode);
				detail.setValue(address.getCountryCode());
				detail.setLastUpdated(updatedTs);
				detail.setLastUpdatedById(userProfileId);
				details.add(detail);

                // Payment method
                detail = new LoanActivityDetail();
                detail.setItemNumber(LoanField.PAYMENT_METHOD
                        .getActivityDetailItemNo());
                detail.setTypeCode(typeCode);
                detail.setValue(paymentMethodCode);
                detail.setLastUpdated(updatedTs);
                detail.setLastUpdatedById(userProfileId);
                details.add(detail);

				if (!ActivityDetail.TYPE_SYSTEM_OF_RECORD.equals(typeCode)) {
					PaymentInstruction paymentInstruction = new LoanPaymentInstruction();

					if (payee != null) {
						paymentInstruction = payee.getPaymentInstruction();
					} else {
						paymentInstruction.setBankAccountTypeCode(null);
						paymentInstruction.setBankTransitNumber(null);
						paymentInstruction.setBankAccountNumber(null);
						paymentInstruction.setBankName(null);
					}

					// account type
					detail = new LoanActivityDetail();
					detail.setItemNumber(LoanField.ACCOUNT_TYPE
					        .getActivityDetailItemNo());
					detail.setTypeCode(typeCode);
					detail
					.setValue(paymentInstruction
					        .getBankAccountTypeCode());
					detail.setLastUpdated(updatedTs);
					detail.setLastUpdatedById(userProfileId);
					details.add(detail);

					// aba routing number
					detail = new LoanActivityDetail();
					detail.setItemNumber(LoanField.ABA_ROUTING_NUMBER
							.getActivityDetailItemNo());
					detail.setTypeCode(typeCode);
					detail
							.setValue(paymentInstruction.getBankTransitNumber() == null 
							        || paymentInstruction.getBankTransitNumber() == 0
							        ? null
									: paymentInstruction.getBankTransitNumber()
											.toString());
					detail.setLastUpdated(updatedTs);
					detail.setLastUpdatedById(userProfileId);
					details.add(detail);

					// account number
					detail = new LoanActivityDetail();
					detail.setItemNumber(LoanField.ACCOUNT_NUMBER
							.getActivityDetailItemNo());
					detail.setTypeCode(typeCode);
					detail.setValue(paymentInstruction.getBankAccountNumber());
					detail.setLastUpdated(updatedTs);
					detail.setLastUpdatedById(userProfileId);
					details.add(detail);

					// bank name
					detail = new LoanActivityDetail();
					detail.setItemNumber(LoanField.BANK_NAME
							.getActivityDetailItemNo());
					detail.setTypeCode(typeCode);
					detail.setValue(paymentInstruction.getBankName());
					detail.setLastUpdated(updatedTs);
					detail.setLastUpdatedById(userProfileId);
					details.add(detail);
				}
			} // savePaymentHistory

			if (ActivityDetail.TYPE_SAVED.equals(typeCode)) {
				detailDao.insertUpdateIfChanged(submissionId, contractId,
						userProfileId, details, LoanActivityDetail.class);
				dynamicDetailDao.insertUpdateIfChanged(submissionId,
						contractId, userProfileId, dynamicDetails);
			} else {
				detailDao.insertUpdate(submissionId, contractId, userProfileId,
						details, LoanActivityDetail.class);
				dynamicDetailDao.insertUpdate(submissionId, contractId,
						userProfileId, dynamicDetails);
			}
		}
	}

	public Loan saveAndExit(Loan loan) throws DistributionServiceException {
		LoanStateContext context = getLoanStateContext(loan);
		context.setSaveAndExit(true);
		LoanValidationHelper.validateLoanIssueFee(loan.getErrors(), loan,
				context.getLoanPlanData());
		LoanStateEnum currentState = LoanStateEnum.fromStatusCode(loan.getStatus());
		validateOnSave(context, currentState, currentState);
		if (!loan.isOK()) {
			return loan;
		}

        setCommonLoanValues(context);
		loan = save(context);
		if (loan.isOK()) {
			logToMrl(DistributionEventEnum.SAVE_AND_EXIT.getEventName(), context);
		}
		return loan;
	}

	public Loan printLoanDocument(Loan loan)
			throws DistributionServiceException {
		LoanStateContext context = getLoanStateContext(loan);
		context.setPrintLoanDocument(true);
		LoanValidationHelper.validateLoanIssueFee(loan.getErrors(), loan,
				context.getLoanPlanData());
		LoanValidationHelper.validateDefaultProvision(loan);
        LoanStateEnum currentState = LoanStateEnum.fromStatusCode(loan.getStatus());
		validateOnSave(context, currentState, currentState);
		
		if (!loan.isOK()) {
			return loan;
		}
        setCommonLoanValues(context);
        loan = save(context);
        if (loan.isOK()) {
            // Note: Print loan document button is not available when the 
            // request status is new or draft.
            saveActivityHistory(currentState, currentState, 
                    context, loan.getLastUpdated());
            logToMrl(DistributionEventEnum.PRINT_LOAN_DOCUMENT.getEventName(), context);
        }
		return loan;
	}
	
	
	public Loan printLoanDocumentReview(Loan loan)
						throws DistributionServiceException {
		LoanStateContext context = getLoanStateContext(loan);
		context.setPrintLoanDocument(true);
		List<LoanMessage> errors = loan.getErrors();
		LoanPlanData loanPlanData = context.getLoanPlanData();
		LoanValidationHelper.validateLegallyMarriedInd(errors, loan
				.getLegallyMarriedInd(), loanPlanData);
		LoanValidationHelper.validateLoanIssueFee(errors, loan,
				loanPlanData);
		LoanValidationHelper.validateDefaultProvision(loan);
		LoanValidationHelper.validateLoanReason(loan);
		LoanStateEnum currentState = LoanStateEnum.fromStatusCode(loan.getStatus());
		validateOnSave(context, currentState, currentState);
		
		if (!loan.isOK()) {
			return loan;
		}
		setCommonLoanValues(context);
		loan = save(context);
		if (loan.isOK()) {
			// Note: Print loan document button is not available when the 
			// request status is new or draft.
			saveActivityHistory(currentState, currentState, 
					context, loan.getLastUpdated());
			logToMrl(DistributionEventEnum.PRINT_LOAN_DOCUMENT.getEventName(), context);
		}
		return loan;
}
	protected void setCommonLoanValues(LoanStateContext context) {
        Loan loan = context.getLoan();
        if (loan.isFeeChanged()) {
            if (LoanConstants.USER_ROLE_TPA_CODE.equals(loan.getLoginRoleCode()) ||
            	LoanConstants.USER_ROLE_JH_BUNDLED_GA_CODE.equals(loan.getLoginRoleCode())) {
                // BGA Project 2012 - Modified for distinguishing loan/withdrawal entries
            	// Submitted by JH staff (Bundled GA CAR).  Submission DB code is "JH"
            	loan.setLastFeeChangedByTpaProfileId(loan.getLoginUserProfileId());
                loan.setLastFeeChangedWasPlanSponsorUserInd(false);
            } else if (LoanConstants.USER_ROLE_PLAN_SPONSOR_CODE.equals(loan.getLoginRoleCode())
                    && loan.getLastFeeChangedByTpaProfileId() != null) {
                loan.setLastFeeChangedWasPlanSponsorUserInd(true);
            }
        }
	}

	protected Loan save(LoanStateContext context)
			throws DistributionServiceException {

		Loan loan = context.getLoan();
		
		/*
		 * The following is a safeguard edit to ensure that we never store
		 * a null paymentAmount, which should never happen unless the user's
		 * browser is not interpreting Javascript properly.
		 */
		LoanParameter loanParameter = loan.getCurrentLoanParameter();
		if (loanParameter != null) {
	        if (loanParameter.getPaymentAmount() == null) {
	            throw new RuntimeException("Calculated page field paymentAmount"
	                    + " is blank, this should never happen.  Cannot"
	                    + " proceeed.  The user's browser may not be processing"
	                    + " Javascript.");
	        }    
		}
		
		LoanDao dao = new LoanDao();
		Integer submissionId = loan.getSubmissionId();
		Timestamp updateTs = new Timestamp(System.currentTimeMillis());
		if (submissionId == null) {
			submissionId = dao.insert(loan.getContractId(), loan
					.getLoginUserProfileId(), loan, updateTs);
			if (submissionId == null) {
				LoanError error = new LoanError(
						LoanErrorCode.PARTICIPANT_HAS_PENDING_LOAN_REQUEST);
				loan.getErrors().add(error);
			} else {
				loan.setSubmissionId(submissionId);
				loan.setCreated(updateTs);
				loan.setCreatedId(loan.getLoginUserProfileId());
			}
		} else {
			boolean updated = dao.update(submissionId, loan.getContractId(),
					loan.getLoginUserProfileId(), loan, updateTs);
			if (!updated) {
				/*
				 * Detect concurrent update
				 */
				LoanError error = new LoanError(
						LoanErrorCode.CONCURRENT_UPDATE_DETECTED);
				loan.getErrors().add(error);
			}
		}

		/*
		 * Check again after DB update/insert.
		 */
		if (!loan.isOK()) {
			return loan;
		}

		loan.setLastUpdated(updateTs);
		loan.setLastUpdatedId(loan.getLoginUserProfileId());
		if (!loan.isStatusChange()) {
			/*
			 * We handle the case when there is no status change in here.
			 */
			LoanStateEnum loanStateEnum = LoanStateEnum.fromStatusCode(loan
					.getStatus());
			if (LoanStateEnum.PENDING_REVIEW.equals(loanStateEnum)
					|| LoanStateEnum.PENDING_APPROVAL.equals(loanStateEnum)) {
				saveActivityHistory(loanStateEnum, loanStateEnum, context,
						updateTs);
			}
		}

		/*
		 * Log status change into activity summary table.
		 */
		saveActivitySummary(loan, updateTs);

		return loan;
	}

	public void validateOnSave(LoanStateContext context, 
	        LoanStateEnum fromState, LoanStateEnum toState)
			throws DistributionServiceException {
		Loan loan = context.getLoan();
		LoanSettings loanSettings = context.getLoanSettings();
		if (fromState.getStatusCode().equals(toState.getStatusCode())) {
			LoanParticipantData loanParticipantData = context
					.getLoanParticipantData();
			LoanPlanData loanPlanData = context.getLoanPlanData();

			// No status change, just doing a Save & exit
			LoanValidationHelper.validateExpirationDate(loan);
			/*
			 * Check outstanding loan balance, and maximum
			 * loan balance in last 12 months only if we're in
			 * DRAFT and PENDING REVIEW
			 */
			if (LoanStateEnum.DRAFT.equals(fromState)
					|| LoanStateEnum.PENDING_REVIEW.equals(fromState)) {
				LoanValidationHelper.validateCurrentOutstandingBalance(loan,
						loanParticipantData);
				LoanValidationHelper.validateMaxBalanceLast12Months(loan,
						loanParticipantData);
			}

			LoanValidationHelper.validateOutstandingLoansCount(loan,
					loanParticipantData, loanPlanData);

			validateLoanCalculationSection(loan, loanPlanData,
			        toState);
			validatePaymentSectionInvalidCharacter(loan);

			if (context.isSaveAndExit() || context.isPrintLoanDocument()) {
				// Validations for save and exit and print loan documents only.
				LoanValidationHelper.validatePayrollDate(context, null);
			}
			List<LoanMessage> errors = loan.getErrors();
			LoanValidationHelper.validateParticipantLoans(errors, loan,
					loanParticipantData);
			
			if (LoanStateEnum.DRAFT.equals(fromState)) {
				LoanValidationHelper.validateLoanTypeCode(errors, loan
	    				.getLoanType(), loanPlanData.getLoanTypeList());
			}
		}
		LoanValidationHelper.validateAllowLoans(loan.getErrors(), loanSettings);
	}

	public Loan initiate(Integer profileId, Integer contractId,
			Integer userProfileId) throws DistributionServiceException {
		return null;
	}

	protected void validateLoanCalculationSection(Loan loan,
			LoanPlanData loanPlanData, LoanStateEnum toState) {
		LoanParameter parameter = loan.getCurrentLoanParameter();
		List<LoanMessage> errors = loan.getErrors();

		if (parameter != null) {
			LoanValidationHelper.validateMaximumLoanAmount(errors, parameter
					.getMaximumAvailable());//CL121427 fix
			LoanValidationHelper.validateLoanAmount(errors, parameter
					.getLoanAmount(), parameter.getMaximumAvailable(), loan
					.getCurrentAvailableAccountBalance(), loanPlanData);
			LoanValidationHelper.validateAmortizationMonths(errors, parameter
					.getAmortizationMonths(), loan
					.getMaximumAmortizationYears());
			LoanValidationHelper.validatePaymentFrequency(errors, parameter
					.getPaymentFrequency());
			if (toState != null && LoanStateEnum.APPROVED.getStatusCode()
			        .equals(toState.getStatusCode())) {
				LoanValidationHelper
						.validatePaymentFrequencyAgainstPlanPayrollFrequency(
								errors, parameter.getPaymentFrequency(),
								loanPlanData.getPayrollFrequency());
			}
			LoanValidationHelper.validateInterestRate(errors, parameter
					.getInterestRate());
		}

	}

	protected void validatePaymentSectionInvalidCharacter(Loan loan) {
		List<LoanMessage> errors = loan.getErrors();

		/*
		 * Validate address.
		 */
		if (loan.getRecipient() != null) {
			Recipient recipient = loan.getRecipient();
			Collection<Payee> payees = recipient.getPayees();
			if (payees != null && payees.size() > 0) {
				Payee payee = payees.iterator().next();
				DistributionAddress address = payee.getAddress();
				LoanValidationHelper.validateCharacterFieldInPaymentSection(
						errors, LoanErrorCode.ADDRESS_LINE1_INVALID_CHARACTER,
						LoanField.ADDRESS_LINE1, address.getAddressLine1(),
						payee.getPaymentMethodCode());
				LoanValidationHelper.validateCharacterFieldInPaymentSection(
						errors, LoanErrorCode.ADDRESS_LINE2_INVALID_CHARACTER,
						LoanField.ADDRESS_LINE2, address.getAddressLine2(),
						payee.getPaymentMethodCode());
				LoanValidationHelper.validateCharacterFieldInPaymentSection(
						errors, LoanErrorCode.CITY_INVALID_CHARACTER,
						LoanField.CITY, address.getCity(), payee
								.getPaymentMethodCode());
				LoanValidationHelper.validateCharacterFieldInPaymentSection(
						errors, LoanErrorCode.STATE_INVALID_CHARACTER,
						LoanField.STATE, address.getStateCode(), payee
								.getPaymentMethodCode());

				if (GlobalConstants.COUNTRY_CODE_USA.equals(address
						.getCountryCode())) {
				    if (!loan.isParticipantInitiated()) {
				        LoanValidationHelper.validateUsZipCode(errors, address
                                .getZipCode(), address.getStateCode(), false, true);
				    } else {
	                    LoanValidationHelper.validateUsZipCode(errors, address
	                            .getZipCode(), address.getStateCode(), true, false);
				    }
				} else {
					LoanValidationHelper
							.validateCharacterFieldInPaymentSection(errors,
									LoanErrorCode.ZIP_CODE_INVALID_CHARACTER,
									LoanField.ZIP_CODE, address.getZipCode(),
									payee.getPaymentMethodCode());
				}

				PaymentInstruction paymentInstruction = payee
						.getPaymentInstruction();
				if (paymentInstruction != null
						&& !Payee.CHECK_PAYMENT_METHOD_CODE.equals(payee
								.getPaymentMethodCode())) {
					LoanValidationHelper
							.validateCharacterFieldInPaymentSection(
									errors,
									LoanErrorCode.ACCOUNT_NUMBER_INVALID_CHARACTER,
									LoanField.ACCOUNT_NUMBER,
									paymentInstruction.getBankAccountNumber(),
									payee.getPaymentMethodCode());
				}
			}
		}

	}

	/**
	 * Refresh the money type information (account balance, vesting percentages,
	 * money type names, etc.)
	 * 
	 * @param loan
	 * @param participantMoneyTypes
	 * @throws DistributionServiceException
	 */
	protected void refreshLoanMoneyTypes(LoanPlanData loanPlanData,
			List<LoanMoneyType> loanMoneyTypes,
			List<LoanMoneyType> participantMoneyTypes)
			throws DistributionServiceException {

	    boolean isNewMoneyTypesAdded = false;
	    
		/*
		 * Update account balance from the participant's current account
		 * balance.
		 */
		for (Iterator<LoanMoneyType> it = participantMoneyTypes.iterator(); it
				.hasNext();) {
			LoanMoneyType participantMoneyType = it.next();
			boolean found = false;

			for (LoanMoneyType loanMoneyType : loanMoneyTypes) {
				if (loanMoneyType.getMoneyTypeId().equals(
						participantMoneyType.getMoneyTypeId())) {
					found = true;
					/*
					 * Update account balance.
					 */
					loanMoneyType.setAccountBalance(participantMoneyType
							.getAccountBalance());
					loanMoneyType.setLoanBalance(participantMoneyType
							.getLoanBalance());
					break;
				}
			}

			/*
			 * If we cannot find the participant money type in the loan money
			 * type, we can simply add it in there.
			 */
			if (!found) {
	            if (loanPlanData.isNoMoneyTypeAllowedForLoan()) {
	                participantMoneyType.setExcludeIndicator(false);
	            }
				loanMoneyTypes.add(participantMoneyType);
				isNewMoneyTypesAdded = true;
			}
		}

		/*
		 * Lastly, we loop through the loan money types and reset all account
		 * balance that are missing in the participant money types.
		 */
		for (LoanMoneyType loanMoneyType : loanMoneyTypes) {
			boolean found = false;
			for (LoanMoneyType participantMoneyType : participantMoneyTypes) {
				if (loanMoneyType.getMoneyTypeId().equals(
						participantMoneyType.getMoneyTypeId())) {
					found = true;
					break;
				}
			}
			if (!found) {
				/*
				 * Update account balance.
				 */
				loanMoneyType.setAccountBalance(BigDecimal.ZERO);
			}
		}
		
		if (isNewMoneyTypesAdded) {
	        // New moneyTypes added, so sort the List.
	        Comparator <LoanMoneyType> moneyTypeComparator = new MoneyTypeComparator();
	        Collections.sort(loanMoneyTypes, moneyTypeComparator);
		}
	}

	protected void logToMrl(String action, LoanStateContext context) {
		/**
		 * MRL Logging
		 */
	    Loan loan = context.getLoan();
	    LoanParticipantData participantData = context.getLoanParticipantData();
	    LoanPlanData planData = context.getLoanPlanData();
	    
	    Timestamp lastUpdated = loan.getLastUpdated();
		Map<String, String> additionalLogInfo = new HashMap<String, String>();
		additionalLogInfo.put(EventLog.SUBMISSION_ID, loan.getSubmissionId()
				.toString());
		additionalLogInfo.put(EventLog.ACTION, action);
        additionalLogInfo.put("ACTION_TS", loan
                .getLastUpdated().toString());
        additionalLogInfo.put("STATUS_CHANGED_TS", 
                loan.isStatusChange()
                    ? loan.getLastUpdated().toString()
                    : null);

		if (LoanStateEnum.APPROVED.getStatusCode().equals(loan.getStatus())) {
			additionalLogInfo.put("APPROVED_TS", lastUpdated.toString());
		} else {
			additionalLogInfo.put("APPROVED_TS", "");
		}
		additionalLogInfo.put("CONTRACT_NAME", StringUtils.trimToEmpty(
		        planData.getContractName()));
		
		// Log participant info.
		StringBuffer participantName = new StringBuffer();
		participantName.append(
		        StringUtils.trimToNull(participantData.getFirstName()));
		if (participantData.getMiddleInitial() != null) {
	        participantName.append(" " + participantData.getMiddleInitial());
		}
        participantName.append(" " + 
                StringUtils.trimToNull(participantData.getLastName()));
        additionalLogInfo.put("PARTICIPANT_NAME", participantName.toString());
        additionalLogInfo.put("PARTICIPANT_SSN", participantData.getSsn());
        additionalLogInfo.put("EMPLOYMENT_STATUS", participantData.getEmploymentStatusCode());
        
		EventLoggingHelper.log(LoanEventLog.class, new Pair<String, String>(
				LoanEventLog.LOAN_REQUEST, loan.toString()), additionalLogInfo,
				loan.getLoginUserProfileId());
	}

	/**
	 * Recalculates/updates any fields required as a result of the loan package
	 * action being selected.
	 * 
	 * @param loan
	 */
	protected void setLoanPackageDates(Loan loan) {
		Calendar newEffectiveDate = Calendar.getInstance();
		newEffectiveDate.add(Calendar.DATE, LoanDefaults
				.getLoanPackageEstimatedLoanStartDateOffset());
		loan.setEffectiveDate(newEffectiveDate.getTime());
	}

	protected Date getEstimatedEffectiveDate(LoanStateContext context)
			throws DistributionServiceException {
		Date today = new Date();
		BusinessCalendar businessCalendar = context.getBusinessCalendar();
		Date thisBusinessDate = businessCalendar
				.getCurrentOrNextBusinessDate(today);
		Date newEffectiveDate = businessCalendar.getNextBusinessDate(
				thisBusinessDate, LoanDefaults
						.getEstimatedLoanStartDateOffset());
		return DateUtils.truncate(newEffectiveDate, Calendar.DATE);
	}

	/**
     * Set effectiveDate (estimated loan start date) to current business 
     * date if the NY Stock Exchange is open or the next business date if 
     * the NY Stock Exchange is closed. 
	 * 
	 * @param loan
	 */
	protected Date getEffectiveDateForApproval(LoanStateContext context)
			throws DistributionServiceException {
		Date today = new Date();
		BusinessCalendar businessCalendar = context.getBusinessCalendar();
		Date date = businessCalendar.getCurrentOrNextBusinessDate(today);
		return DateUtils.truncate(date, Calendar.DATE);
	}
	
    /**
     * Set Estimated loan maturity date = Estimated loan start date + the 
     * Amortization  months.
     * 
     * @param loan
     */
    protected Date getEstimatedMaturityDate(Loan loan) {
		Calendar newMaturityDate = Calendar.getInstance();
		newMaturityDate.setTime(loan.getEffectiveDate());
		if (loan.getCurrentLoanParameter() != null
				&& loan.getCurrentLoanParameter().getAmortizationMonths() != null) {
			newMaturityDate.add(Calendar.MONTH, loan.getCurrentLoanParameter()
					.getAmortizationMonths());
		}
		return DateUtils.truncate(newMaturityDate.getTime(), Calendar.DATE);
	}
	
    protected Date getEstimatedExpirationDate() {
		Calendar expirationCalendar = Calendar.getInstance();
		expirationCalendar.add(Calendar.DATE, LoanDefaults
				.getExpirationDateOffset());
		return DateUtils.truncate(expirationCalendar.getTime(), Calendar.DATE);
	}

}