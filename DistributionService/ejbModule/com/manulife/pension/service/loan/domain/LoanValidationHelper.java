package com.manulife.pension.service.loan.domain;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;



import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.contract.ContractConstants.ContractStatus;
import com.manulife.pension.service.contract.report.reporthandler.SystematicWithdrawReportHandler;
import com.manulife.pension.service.contract.report.valueobject.SystematicWithdrawDataItem;
import com.manulife.pension.service.contract.report.valueobject.SystematicWithdrawReportData;
import com.manulife.pension.service.contract.util.Constants;
import com.manulife.pension.service.contract.util.SystematicWithdrawalReportUtility.WDStatusCodes;
import com.manulife.pension.service.contract.valueobject.LifeIncomeAmountDetailsVO;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.DistributionAddress;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.PaymentInstruction;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.employee.EmployeeConstants.ParticipantStatus;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.employee.valueobject.EmployeeFunctionShutdownVO.FunctionCode;
import com.manulife.pension.service.loan.LoanDefaults;
import com.manulife.pension.service.loan.LoanError;
import com.manulife.pension.service.loan.LoanErrorCode;
import com.manulife.pension.service.loan.LoanField;
import com.manulife.pension.service.loan.LoanMessage;
import com.manulife.pension.service.loan.LoanWarning;
import com.manulife.pension.service.loan.exception.LoanValidationException;
import com.manulife.pension.service.loan.util.LoanDataHelper;
import com.manulife.pension.service.loan.util.LoanObjectFactory;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanDeclaration;
import com.manulife.pension.service.loan.valueobject.LoanNote;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPayee;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.loan.valueobject.LoanTypeVO;
import com.manulife.pension.service.security.role.AdministrativeContact;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.BusinessCalendar;
import com.manulife.pension.util.CalendarUtils;
import com.manulife.pension.util.JdbcHelper;
import com.manulife.pension.util.Pair;
import com.manulife.util.converter.ConverterHelper;

public class LoanValidationHelper {

	private static final Pattern apolloAllowedCharacterRegEx = Pattern
			.compile("[\\x20-\\x7e]");

	private static final char[] electronicPaymentInvalidCharacters = new char[] {
			'"', '&', '<' };

	private static final char[] specialCharacters = new char[] { '!', '{', '}',
			'|', '%', '¬', '"', '&', '<', '>', '-', '¢', '¦', '$', '#', '=',
			';', '~', '@', '^' };

	private static final Logger log = Logger.getLogger(LoanValidationHelper.class);
	/**
	 * Returns true if any one of the field names in the input fields array is
	 * marked as an error field in the input loanErrors List.
	 * 
	 * @param fields
	 *            Array of field names
	 * @param loanErrors
	 *            List of loan errors
	 * @return true if any one of the field names in the input fields array is
	 *         marked as an error field in the input loanErrors List. false if
	 *         otherwise.
	 */
	public static boolean isAnyFieldInError(String[] fields,
			List<LoanMessage> loanErrors) {
		if (loanErrors != null) {
			for (String field : fields) {
				for (LoanMessage error : loanErrors) {
					for (String errorFieldId : error.getFieldNames()) {
						if (field.equals(errorFieldId)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public static void validateParticipantStatusForApproval(
			List<LoanMessage> errors, LoanParticipantData loanParticipantData) {
		if (!ParticipantStatus.ACTIVE.equals(loanParticipantData
				.getParticipantStatusCode())) {
			LoanError error = new LoanError(
					LoanErrorCode.PARTICIPANT_STATUS_IS_NOT_ACTIVE_FOR_APPROVAL);
			errors.add(error);
		}
	}

	public static void validateContractStatusForApproval(
			List<LoanMessage> errors, LoanPlanData loanPlanData) {
		if (ContractStatus.FROZEN.equals(loanPlanData.getContractStatusCode())) {
			LoanError error = new LoanError(
					LoanErrorCode.CONTRACT_STATUS_IS_FROZEN_FOR_APPROVAL);
			errors.add(error);
		}
	}

	public static void validateLegallyMarriedInd(List<LoanMessage> errors,
			Boolean legallyMarriedInd, LoanPlanData loanPlanData) {
		if (!JdbcHelper.INDICATOR_NO.equals(loanPlanData
				.getSpousalConsentReqdInd())) {
			if (legallyMarriedInd == null) {
				errors.add(new LoanError(
						LoanErrorCode.MISSING_LEGALLY_MARRIED_IND,
						LoanField.LEGALLY_MARRIED_IND));
			}
		}
	}

	public static void validateLoanTypeCode(List<LoanMessage> errors,
			String loanType, List<LoanTypeVO> LoanTypeList) {
		if (loanType == null) {
			errors.add(new LoanError(LoanErrorCode.MISSING_LOAN_TYPE,
					LoanField.LOAN_TYPE));
		}
		
		//if selected Loan Type is disabled one - somehow if it can set by hackers.
		for(LoanTypeVO laonTypeVO : LoanTypeList) {
			if(StringUtils.equalsIgnoreCase(loanType, laonTypeVO.getLoanTypeCode())) {
				
				if(laonTypeVO.isDisabled()) {
					errors.add(new LoanError(LoanErrorCode.MISSING_LOAN_TYPE,
							LoanField.LOAN_TYPE));
					break;
				}
			}
		}
	}

	public static void validateCurrentParticipantNote(List<LoanMessage> errors,
			LoanNote note) {
		if (note == null || StringUtils.isBlank(note.getNote())) {
			errors.add(new LoanError(
					LoanErrorCode.MUST_ENTER_NOTE_TO_PARTICIPANT_ON_DENY,
					LoanField.CURRENT_PARTICIPANT_NOTE));
		}
	}

	public static void validateLoanReason(Loan loan) {
		if (StringUtils.isBlank(loan.getLoanReason())
				&& (LoanConstants.TYPE_GENERAL_PURPOSE.equals(loan
						.getLoanType()) || LoanConstants.TYPE_HARDSHIP
						.equals(loan.getLoanType()))) {
			loan.getErrors().add(
					new LoanError(LoanErrorCode.MUST_ENTER_LOAN_REASON,
							LoanField.LOAN_REASON));
		}
	}

	public static void validateExpirationDate(Loan loan) {
        if (isAnyFieldInError(new String[] { LoanField.EXPIRATION_DATE
                .getFieldName() }, loan.getErrors())) {
            /*
             * Don't do validation if this field is already in error.
             */
            return;
        }
        String expiredDays=null;
        Date requestDate=loan.getRequestDate();
        Date expirationDate=loan.getExpirationDate();
		Date systemDate = Calendar.getInstance().getTime();
		systemDate = DateUtils.truncate(systemDate, Calendar.DATE);
		Date minimumFutureDate = DateUtils.addDays(requestDate, LoanDefaults
				.getExpirationDateMinimumFutureDatedDays());
		if (loan.getExpirationDate().compareTo(minimumFutureDate) < 0) {
			LoanError error = new LoanError(
					LoanErrorCode.EXPIRATION_DATE_LESS_THAN_MININUM_ALLOWED,
					LoanField.EXPIRATION_DATE);
			loan.getErrors().add(error);
		} else {
			Date maximumFutureDate = DateUtils.addDays(requestDate, LoanDefaults
					.getExpirationDateMaximumFutureDatedDays());
			if (loan.getExpirationDate().compareTo(maximumFutureDate) > 0) {
				expiredDays=CalendarUtils.getIntervalInDays(requestDate, expirationDate);
				LoanError error = new LoanError(
						LoanErrorCode.EXPIRATION_DATE_GREATER_THAN_MAXIMUM_ALLOWED,
						LoanField.EXPIRATION_DATE,
						expiredDays);
				loan.getErrors().add(error);
			}
		}
	}

	public static void validatePayrollDateAgainstPaymentFrequency(
			List<LoanMessage> loanErrors, Date date, String paymentFrequency) {
		if (GlobalConstants.FREQUENCY_TYPE_SEMI_MONTHLY
				.equals(paymentFrequency)) {
			/*
			 * Check if the date falls on 1, 15 or end of month.
			 */
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);

			int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

			if (dayOfMonth != 1
					&& dayOfMonth != 15
					&& dayOfMonth != calendar
							.getActualMaximum(Calendar.DAY_OF_MONTH)) {
				LoanError error = new LoanError(
						LoanErrorCode.PAYROLL_DATE_INVALID_FOR_SEMI_MONTHLY_PAYMENT_FREQUENCY,
						LoanField.PAYROLL_DATE);
				loanErrors.add(error);
			}
		}
	}

	public static void validatePayrollDate(LoanStateContext context,
			LoanStateEnum toState) {
        Loan loan = context.getLoan();
        String[] fields = new String[] { LoanField.PAYROLL_DATE
                .getFieldName() };

        // If an error has already been flagged for the FirstPayrollDate
        // field, implying it's not a valid date, then don't bother doing 
        // the below edits
        if (isAnyFieldInError(fields, loan.getErrors())) {
            return;
        }

		Date systemDate = Calendar.getInstance().getTime();
		// Now set the time to be start of day (i.e. zero hundred hours).
		systemDate = DateUtils.truncate(systemDate, Calendar.DATE);

		/*
		 * Common case regardless of status.
		 */
		if (loan.getFirstPayrollDate() != null
				&& loan.getFirstPayrollDate()
						.compareTo(loan.getEffectiveDate()) <= 0) {
			LoanError error = new LoanError(
					LoanErrorCode.PAYROLL_DATE_LESS_THAN_EQUAL_TO_ESTIMATED_LOAN_START_DATE,
					LoanField.PAYROLL_DATE);
			loan.getErrors().add(error);

		}

		/*
		 * If Payroll date is not null, it must not be greater than the loan
		 * effectiveDate + nn calendar days.
		 */
		if (loan.getFirstPayrollDate() != null
				&& loan.getEffectiveDate() != null) {
			Date maxPayrollDateAllowed = DateUtils.addDays(loan
					.getEffectiveDate(), LoanDefaults
					.getEstimatedLoanStartDateMaximumFutureDatedDays());
			if (loan.getFirstPayrollDate().compareTo(maxPayrollDateAllowed) > 0) {
				LoanError error = new LoanError(
						LoanErrorCode.PAYROLL_DATE_GREATER_THAN_MAXIMUM_ALLOWED,
						LoanField.PAYROLL_DATE,
						Integer
								.toString(LoanDefaults
										.getEstimatedLoanStartDateMaximumFutureDatedDays()));
				loan.getErrors().add(error);
			}
		}

		/*
		 * Loan package, approve or Print loan documents
		 */
		if (LoanStateEnum.LOAN_PACKAGE.equals(toState)
				|| LoanStateEnum.PENDING_APPROVAL.equals(toState)
				|| LoanStateEnum.APPROVED.equals(toState)
				|| context.isPrintLoanDocument()) {

			if (loan.getFirstPayrollDate() == null) {
				loan.getErrors().add(
						new LoanError(LoanErrorCode.MISSING_PAYROLL_DATE,
								LoanField.PAYROLL_DATE));
			} else {

				validatePayrollDateAgainstPaymentFrequency(loan.getErrors(),
						loan.getFirstPayrollDate(), loan
								.getCurrentLoanParameter()
								.getPaymentFrequency());

				if (LoanStateEnum.LOAN_PACKAGE.equals(toState)) {
					/*
					 * Loan package case
					 */
					Date minimumFutureDate = DateUtils
							.addDays(systemDate, LoanDefaults
									.getPayrollDateMinimumFutureDatedDays());
					if (loan.getFirstPayrollDate().compareTo(minimumFutureDate) <= 0) {
						LoanError error = new LoanError(
								LoanErrorCode.PAYROLL_DATE_LESS_THAN_MININUM_ALLOWED,
								LoanField.PAYROLL_DATE,
								Integer
										.toString(LoanDefaults
												.getPayrollDateMinimumFutureDatedDays()));
						loan.getErrors().add(error);
					}
				} else if (LoanStateEnum.APPROVED.equals(toState)) {
					/*
					 * Approve case
					 */
					BusinessCalendar businessCalendar = context
							.getBusinessCalendar();
					Date nextBusinessDate = businessCalendar
							.getCurrentOrNextBusinessDate(new Date());
					// Set the effectiveDate so the time is the start of day
					// (i.e. zero hundred hours).
					nextBusinessDate = DateUtils.truncate(nextBusinessDate,
							Calendar.DATE);

					if (loan.getFirstPayrollDate().compareTo(nextBusinessDate) <= 0) {
						DateFormat dateFormatter = ConverterHelper
								.getDefaultDateFormat();
						LoanError error = new LoanError(
								LoanErrorCode.PAYROLL_DATE_LESS_THAN_EQUAL_TO_NEXT_BUSINESS_DATE,
								LoanField.PAYROLL_DATE, dateFormatter
										.format(nextBusinessDate));
						loan.getErrors().add(error);
					}
				}
			}
		}
	}

	public static void validateDefaultProvision(Loan loan) {
		if (StringUtils.isBlank(loan.getDefaultProvision())) {
			loan.getErrors().add(
					new LoanError(LoanErrorCode.MISSING_DEFAULT_PROVISION,
							LoanField.DEFAULT_PROVISION));
		}
	}

	public static void validateCurrentOutstandingBalance(Loan loan,
			LoanParticipantData loanParticipantData) {
		String[] fields = new String[] { LoanField.CURRENT_OUTSTANDING_LOAN_BALANCE
				.getFieldName() };

		// If no errors have yet been flagged for the currentOutstandingBalance
		// field, implying it's in the proper format, then check that its value
		// is less than the participant's current outstanding loan balance.
		if (loan.getCurrentOutstandingBalance() != null
				&& !isAnyFieldInError(fields, loan.getErrors())) {
			if (loan.getCurrentOutstandingBalance().compareTo(
					loanParticipantData.getCurrentOutstandingBalance()) < 0) {
			    String displayAmount = formatAmountForDisplay(
			            loanParticipantData.getCurrentOutstandingBalance());
			    loan
						.getErrors()
						.add(
								new LoanError(
										LoanErrorCode.CURRENT_OUTSTANDING_LOAN_BALANCE_TOO_LOW,
										LoanField.CURRENT_OUTSTANDING_LOAN_BALANCE,
										displayAmount));
			}
		}
	}

	/**
	 * Formats an amount for display by adding a '$' sign, including the thousands
	 * separators, rounding to 2 decimal places, and showing 2 decimal places.
	 * @param amount
	 * @return
	 */
	public static String formatAmountForDisplay(BigDecimal amount) {
        NumberFormat amountFormatter = NumberFormat.getNumberInstance();
        amountFormatter.setMinimumFractionDigits(2);
        amountFormatter.setMaximumFractionDigits(2);
        String displayAmount = "$" + amountFormatter.format(
                amount.doubleValue());
        return displayAmount;
	}
	
	public static void validateMaxBalanceLast12Months(Loan loan,
			LoanParticipantData loanParticipantData) {
		String[] fields = new String[] { LoanField.MAXIMUM_LOAN_BALANCE_IN_LAST_12_MONTHS
				.getFieldName() };

		// If no errors have yet been flagged for the maxBalanceLast12Months
		// field, implying it's in the proper format, then check that its value
		// is less than the participant's highest loan balance in the last 12
		// months.
		if (loan.getMaxBalanceLast12Months() != null
				&& loanParticipantData.getMaxBalanceLast12Months() != null
				&& !isAnyFieldInError(fields, loan.getErrors())) {
			if (loan.getMaxBalanceLast12Months().compareTo(
					loanParticipantData.getMaxBalanceLast12Months()) < 0) {
			    String displayAmount = formatAmountForDisplay(
			    		loanParticipantData.getMaxBalanceLast12Months());
				loan
						.getErrors()
						.add(
								new LoanError(
										LoanErrorCode.MAXIMUM_LOAN_BALANCE_IN_LAST_12_MONTHS_TOO_LOW,
										LoanField.MAXIMUM_LOAN_BALANCE_IN_LAST_12_MONTHS,
										displayAmount));
			}
		}
	}

	public static void validateOutstandingLoansCount(Loan loan,
			LoanParticipantData loanParticipantData, LoanPlanData loanPlanData) {
		String[] fields = new String[] { LoanField.OUTSTANDING_LOANS_COUNT
				.getFieldName() };

		if (loan.getOutstandingLoansCount() != null
				&& !isAnyFieldInError(fields, loan.getErrors())) {
			// No errors have yet been flagged for the getOutstandingLoansCount
			// field, implying it's in the proper format.

			// Check if its value is less than the participant's current
			// outstanding loan count.
			if (loanParticipantData.getOutstandingLoansCount() != null) {
				if (loan.getOutstandingLoansCount().compareTo(
						loanParticipantData.getOutstandingLoansCount()) < 0) {
					loan
							.getErrors()
							.add(
									new LoanError(
											LoanErrorCode.OUTSTANDING_LOANS_COUNT_TOO_LOW,
											LoanField.OUTSTANDING_LOANS_COUNT,
											loanParticipantData
													.getOutstandingLoansCount()
													.toString()));
				}
			}

			// Check if its value is greater than,
			// a) the Plan maximum number of loans allowed if available or
			// b) the Plan default value if the plan value is not available
			if (loanPlanData.getMaxNumberOfOutstandingLoans() != null) {
				if (loan.getOutstandingLoansCount().compareTo(
						loanPlanData.getMaxNumberOfOutstandingLoans()) > 0) {
					loan
							.getErrors()
							.add(
									new LoanError(
											LoanErrorCode.OUTSTANDING_LOANS_COUNT_TOO_HIGH,
											LoanField.OUTSTANDING_LOANS_COUNT,
											loanPlanData
													.getMaxNumberOfOutstandingLoans()
													.toString()));
				}
			}
		}
	}

	/**
	 * Validate if the participant has outstanding loans or if they have a
	 * forward unreversed loan transaction in Apollo. This method takes an array
	 * of loan message as the first parameter because it is used by both initial
	 * message validation and by most state transition methods.
	 * 
	 * @param messages
	 * @param loanParticipantData
	 */
	public static void validateParticipantLoans(List<LoanMessage> messages,
			Loan loan, LoanParticipantData loanParticipantData) {

		if (loan.getSubmissionId() == null) {
			if (loanParticipantData.getPendingRequests().size() > 0) {
				LoanError error = new LoanError(
						LoanErrorCode.PARTICIPANT_HAS_PENDING_LOAN_REQUEST);
				messages.add(error);
			}
		} else {
			for (Integer submissionId : loanParticipantData
					.getPendingRequests()) {
				if (!loan.getSubmissionId().equals(submissionId)) {
					LoanError error = new LoanError(
							LoanErrorCode.PARTICIPANT_HAS_PENDING_LOAN_REQUEST);
					messages.add(error);
					break;
				}
			}
		}

		if (loanParticipantData.isForwardUnreversedLoanTransactionExist()) {
			LoanError error = new LoanError(
					LoanErrorCode.FORWARD_UNREVERSED_LOAN_TRANSACTION_EXISTS);
			messages.add(error);
		}
	}

	/**
	 * Validate if the LRK01 flag and the Allow online loan flag is still valid.
	 * This method takes an array of loan message as the first parameter because
	 * it is used by both initial message validation and by most state
	 * transition methods.
	 * 
	 * @param messages
	 * @param loanSettings
	 */
	public static void validateAllowLoans(List<LoanMessage> messages,
			LoanSettings loanSettings) {
		if (!loanSettings.isLrk01()) {
			LoanError error = new LoanError(LoanErrorCode.LRK01_IS_OFF);
			messages.add(error);
		}
		if (!loanSettings.isAllowOnlineLoans() && loanSettings.isLrk01()) {
			LoanError error = new LoanError(
					LoanErrorCode.ALLOW_ONLINE_LOANS_IS_OFF);
			messages.add(error);
		}
	}

	/**
	 * Validate the loan issue fee such that it must be less than or equal to
	 * 80% of the plan minimum loan amount (if there is any). This validation is
	 * only executed if the login user is TPA or PS.
	 * 
	 * @param messages
	 * @param loan
	 * @param loanPlanData
	 */
	public static void validateLoanIssueFee(List<LoanMessage> messages,
			Loan loan, LoanPlanData loanPlanData) {
		if (loan.getFee() != null && loan.isLoginUserPlanSponsorOrTpa()) {
			BigDecimal fee = loan.getFee().getValue();
			if (fee != null) {
				BigDecimal adjFee = fee.multiply(new BigDecimal(1.25));
				if (adjFee.compareTo(loanPlanData.getMinimumLoanAmount()) > 0) {
	                String displayAmount = formatAmountForDisplay(
	                        loanPlanData.getMinimumLoanAmount());
					LoanWarning error = new LoanWarning(
							LoanErrorCode.LOAN_ISSUE_FEE_GREATER_THAN_PLAN_MINIMUM,
							LoanField.TPA_LOAN_ISSUE_FEE, displayAmount);
					messages.add(error);
				}
			}
		}
	}

	public static void validateLoanAmount(List<LoanMessage> errors,
			BigDecimal loanAmount, BigDecimal maximumLoanPermitted,
			BigDecimal availableAccountBalance, LoanPlanData loanPlanData) {

		if (isAnyFieldInError(new String[] { LoanField.LOAN_AMOUNT
				.getFieldName() }, errors)) {
			/*
			 * Don't do validation if this field is already in error.
			 */
			return;
		}

		if (loanAmount == null) {
			LoanError error = new LoanError(
					LoanErrorCode.LOAN_AMOUNT_BLANK_OR_NON_NUMERIC,
					LoanField.LOAN_AMOUNT);
			errors.add(error);
		} else {
			BigDecimal minimumLoanAmount = loanPlanData.getMinimumLoanAmount();
			if (loanAmount.compareTo(minimumLoanAmount) < 0) {
                String displayAmount = formatAmountForDisplay(
                        minimumLoanAmount);
				LoanError error = new LoanError(
						LoanErrorCode.LOAN_AMOUNT_LESS_THAN_MINIMUM,
						LoanField.LOAN_AMOUNT, new String[] { displayAmount });
				errors.add(error);
			}
			boolean hasGreaterThanMaxError = false;
			if (maximumLoanPermitted != null) {
				if (loanAmount.compareTo(maximumLoanPermitted) > 0) {
					LoanError error = new LoanError(
							LoanErrorCode.LOAN_AMOUNT_GREATER_THAN_MAXIMUM,
							LoanField.LOAN_AMOUNT);
					errors.add(error);
					hasGreaterThanMaxError = true;
				}
			}
			/*
			 * Don't duplicate the check if the greater than maximum error is
			 * already raised.
			 */
			if (availableAccountBalance != null && !hasGreaterThanMaxError) {
				if (loanAmount.compareTo(availableAccountBalance) > 0) {
					LoanError error = new LoanError(
							LoanErrorCode.LOAN_AMOUNT_GREATER_THAN_MAXIMUM,
							LoanField.LOAN_AMOUNT);
					errors.add(error);
				}
			}
		}
	}

	public static void validateAmortizationMonths(List<LoanMessage> errors,
			Integer amortizationMonths, Integer maximumAmortizationYears) {
		if (amortizationMonths == null || amortizationMonths.intValue() == 0) {
			LoanError error = new LoanError(
					LoanErrorCode.AMORTIZATION_MONTHS_IS_ZERO,
					LoanField.AMORTIZATION_MONTHS);
			errors.add(error);
		}
		if (amortizationMonths > (maximumAmortizationYears * 12)) {
			LoanError error = new LoanError(
					LoanErrorCode.AMORTIZATION_PERIOD_TOO_HIGH,
					LoanField.AMORTIZATION_MONTHS);
			errors.add(error);
		}
	}

	public static void validatePaymentFrequency(List<LoanMessage> errors,
			String paymentFrequency) {
		if (StringUtils.isBlank(paymentFrequency)) {
			LoanError error = new LoanError(
					LoanErrorCode.PAYMENT_FREQUENCY_IS_EMPTY,
					LoanField.PAYMENT_FREQUENCY);
			errors.add(error);
		}
	}

	public static void validatePaymentFrequencyAgainstPlanPayrollFrequency(
			List<LoanMessage> errors, String paymentFrequency,
			String planPaymentFrequency) {
		if (!StringUtils.isBlank(planPaymentFrequency)
				&& !paymentFrequency.equals(planPaymentFrequency)) {
			LoanWarning error = new LoanWarning(
					LoanErrorCode.PAYMENT_FREQUENCY_NOT_MATCHING_PLAN_VALUE,
					LoanField.PAYMENT_FREQUENCY);
			errors.add(error);
		}
	}

	public static void validateInterestRate(List<LoanMessage> errors,
			BigDecimal interestRate) {
		if (isAnyFieldInError(new String[] { LoanField.INTEREST_RATE
				.getFieldName() }, errors)) {
			/*
			 * Don't do validation if this field is already in error.
			 */
			return;
		}

		if (interestRate == null) {
			LoanError error = new LoanError(
					LoanErrorCode.INTEREST_RATE_BLANK_OR_NON_NUMERIC,
					LoanField.INTEREST_RATE);
			errors.add(error);
		}
	}

	public static void validateCharacterFieldInPaymentSection(
			List<LoanMessage> errors, LoanErrorCode errorCode, LoanField field,
			String fieldValue, String paymentMethodCode) {

		if (!StringUtils.isBlank(fieldValue)) {
			String invalidCharacters = null;
			if (LoanField.ACCOUNT_NUMBER.equals(field)) {
				/*
				 * Special consideration of special characters for account
				 * number.
				 */
				invalidCharacters = getNotAllowedCharacter(
						apolloAllowedCharacterRegEx, specialCharacters,
						fieldValue);
			} else {
				if (Payee.ACH_PAYMENT_METHOD_CODE.equals(paymentMethodCode)
						|| Payee.WIRE_PAYMENT_METHOD_CODE
								.equals(paymentMethodCode)) {
					invalidCharacters = getNotAllowedCharacter(
							apolloAllowedCharacterRegEx,
							electronicPaymentInvalidCharacters, fieldValue);
				} else {
					invalidCharacters = getNotAllowedCharacter(
							apolloAllowedCharacterRegEx, fieldValue);
				}
			}
			if (invalidCharacters != null && invalidCharacters.length() > 0) {
				LoanError error = new LoanError(errorCode, field,
						invalidCharacters);
				errors.add(error);
			}
		}
	}

	public static void validateUsZipCode(List<LoanMessage> errors,
			String zipCode, String stateCode, boolean overrideZipCode,
			boolean flagRangeCheckAsWarning) {
		if (!StringUtils.isBlank(zipCode)) {
			Pattern numericCharacterRegEx = Pattern.compile("[0-9]");
			String invalidCharacters = getNotAllowedCharacter(
					numericCharacterRegEx, zipCode.trim());
			boolean zipCodeInvalidFormat = false;
			if (invalidCharacters.length() > 0) {
				LoanError error = new LoanError(
						LoanErrorCode.ZIP_CODE_INVALID_US_FORMAT,
						LoanField.ZIP_CODE);
				errors.add(error);
				zipCodeInvalidFormat = true;
			} else {
				if (zipCode.length() != 5 && zipCode.length() != 9) {
					LoanError error = new LoanError(
							LoanErrorCode.ZIP_CODE_INVALID_US_FORMAT,
							LoanField.ZIP_CODE);
					errors.add(error);
					zipCodeInvalidFormat = true;
				}
			}
			if (!overrideZipCode) {
				if (!zipCodeInvalidFormat && !StringUtils.isBlank(stateCode)) {
					EnvironmentServiceDelegate environmentServiceDelegate = EnvironmentServiceDelegate
							.getInstance(GlobalConstants.PSW_APPLICATION_ID);
					Map<String, List<Pair<Long, Long>>> zipRangesMap = null;
					try {
						zipRangesMap = (Map<String, List<Pair<Long, Long>>>) environmentServiceDelegate
								.getZipCodeRanges();
					} catch (SystemException e) {
						throw new RuntimeException(
								"Unable to retrieve zip code ranges", e);
					}
					List<Pair<Long, Long>> zipRangesList = zipRangesMap
							.get(stateCode);
					if (zipRangesList != null) {
						Long zip = Long.valueOf(zipCode.substring(0, 5));
						boolean validRange = false;
						for (Pair<Long, Long> zipRange : zipRangesList) {
							if (zip.longValue() >= zipRange.getFirst()
									.longValue()
									&& zip.longValue() <= zipRange.getSecond()
											.longValue()) {
								validRange = true;
								break;
							}
						}
						
						if (!validRange) {
						    if (flagRangeCheckAsWarning) {
	                            LoanWarning warning = new LoanWarning(
	                                    LoanErrorCode.ZIP_CODE_INVALID_RANGE,
	                                    LoanField.ZIP_CODE);
	                            errors.add(warning);
						    } else {
	                            LoanError error = new LoanError(
	                                    LoanErrorCode.ZIP_CODE_INVALID_RANGE,
	                                    LoanField.ZIP_CODE);
	                            errors.add(error);
						    }
						}
					}
				}
			}
		}
	}

	public static void validateRequiredField(List<LoanMessage> errors,
			LoanErrorCode errorCode, LoanField field, String fieldValue) {
		if (StringUtils.isBlank(fieldValue)) {
			LoanError error = new LoanError(errorCode, field);
			errors.add(error);
		}
	}

	public static String getNotAllowedCharacter(
			Pattern allowedCharacterPattern, String string) {
		Matcher m = allowedCharacterPattern.matcher(string);
		String invalidChars = m.replaceAll("");
		return invalidChars;
	}

	public static String getNotAllowedCharacter(
			Pattern allowedCharacterPattern,
			char[] additionalInvalidCharacters, String string) {
		StringBuffer invalidCharacters = new StringBuffer(
				getNotAllowedCharacter(allowedCharacterPattern, string));
		for (int i = 0; i < string.length(); i++) {
			for (int j = 0; j < additionalInvalidCharacters.length; j++) {
				if (string.charAt(i) == additionalInvalidCharacters[j]) {
					invalidCharacters.append(string.charAt(i));
				}
			}
		}
		return invalidCharacters.toString();
	}

	public static void validateDeclarationsAccepted(List<LoanMessage> errors,
			Loan loan, boolean isApproved) {
		boolean truthInLendingAccepted = false;
		boolean promissoryNoteAccepted = false;
		boolean atRiskTransactionAccepted = false;
		if(loan.getDeclarations() != null){
			for (LoanDeclaration declaration : loan.getDeclarations()) {
				if (LoanDeclaration.TRUTH_IN_LENDING_NOTICE.equals(declaration
						.getTypeCode())) {
					truthInLendingAccepted = true;
				} else if (LoanDeclaration.PROMISSORY_NOTE_AND_IRREVOCABLE_PLEDGE
						.equals(declaration.getTypeCode())) {
					promissoryNoteAccepted = true;
				} else if (LoanDeclaration.AT_RISK_TRANSACTION_TYPE_CODE
						.equals(declaration.getTypeCode())) {
					atRiskTransactionAccepted = true;
				}
			}
		}

		if (!truthInLendingAccepted && !loan.isParticipantInitiated()
				&& loan.isDeclartionSectionDisplayed()) {
			LoanError error = new LoanError(
					LoanErrorCode.TRUTH_IN_LENDING_NOTICE_NOT_ACCEPTED,
					LoanField.TRUTH_IN_LENDING_NOTICE);
			errors.add(error);
		}
		if (!promissoryNoteAccepted && !loan.isParticipantInitiated()
				&& loan.isDeclartionSectionDisplayed()) {
			LoanError error = new LoanError(
					LoanErrorCode.PROMISSORY_NOTE_NOT_ACCEPTED,
					LoanField.PROMISSORY_NOTE);
			errors.add(error);
		}
		if (!atRiskTransactionAccepted && loan.isParticipantInitiated()
				&& JdbcHelper.INDICATOR_YES.equals(loan.getAtRiskInd()) && isApproved) {
			LoanError error = new LoanError(
					LoanErrorCode.AT_RISK_TRANSACTION_NOT_ACCEPTED,
					LoanField.AT_RISK_TRANSACTION);
			errors.add(error);
		}
	}
	
	public static void validateAddressPaymentInfo(List<LoanMessage> errors, Loan loan) {

		Recipient recipient = loan.getRecipient();
		Collection<Payee> payees = recipient.getPayees();
		if (payees != null && payees.size() > 0) {
			Payee payee = payees.iterator().next();
			DistributionAddress address = payee.getAddress();
			validateRequiredField(errors,
					LoanErrorCode.ADDRESS_LINE1_REQUIRED,
					LoanField.ADDRESS_LINE1, address.getAddressLine1());
			validateRequiredField(errors,
					LoanErrorCode.CITY_REQUIRED, LoanField.CITY, address
					.getCity());
			validateRequiredField(errors,
					LoanErrorCode.STATE_REQUIRED, LoanField.STATE, address
					.getStateCode());
			LoanDataHelper loanDataHelper = LoanObjectFactory.getInstance()
			.getLoanDataHelper();
			if (loanDataHelper.isCountryUSA(address.getCountryCode())) {
				validateRequiredField(errors,
						LoanErrorCode.ZIP_CODE_REQUIRED, LoanField.ZIP_CODE,
						address.getZipCode());
			}
			validateRequiredField(
					errors, LoanErrorCode.COUNTRY_REQUIRED, LoanField.COUNTRY, 
					address.getCountryCode());
			PaymentInstruction paymentInstruction = payee
			.getPaymentInstruction();
			if (paymentInstruction != null
					&& !Payee.CHECK_PAYMENT_METHOD_CODE.equals(payee
							.getPaymentMethodCode())) {
				if (!Payee.WIRE_PAYMENT_METHOD_CODE.equals(payee
						.getPaymentMethodCode())) {
					validateRequiredField(errors,
							LoanErrorCode.ACCOUNT_TYPE_REQUIRED,
							LoanField.ACCOUNT_TYPE, paymentInstruction
							.getBankAccountTypeCode());
				}
				validateRequiredField(errors,
						LoanErrorCode.ACCOUNT_NUMBER_REQUIRED,
						LoanField.ACCOUNT_NUMBER, paymentInstruction
						.getBankAccountNumber());
				validateRequiredField(errors,
						LoanErrorCode.BANK_NAME_REQUIRED,
						LoanField.BANK_NAME, paymentInstruction
						.getBankName());
			}
		}
	}
	
	/**
     * Validate participant is applicable to LIA.
     */
	public static void validateLIA(
			List<LoanMessage> errors, Loan loan)
			throws DistributionServiceException {
		try {
			LifeIncomeAmountDetailsVO liaDetails = ContractServiceDelegate
					.getInstance()
					.getLIADetailsByParticipantId(
							String.valueOf(loan.getParticipantId()));
			if (liaDetails.isLIAAvailableForParticipant()) {
				LoanError error = new LoanError(
						LoanErrorCode.LIA_ENABLED_FOR_PARTICIPANT);
				errors.add(error);
			}
		} catch (SystemException exception) {
			throw new DistributionServiceException(exception,
					LoanValidationHelper.class.getName(),
					"validateParticpantApplicableToLIA", exception.getMessage()) {
				@Override
				public String getErrorCode() {
					return null;
				}
			};
		}
	}
	
	//CL121427 fix
	public static void validateMaximumLoanAmount(List<LoanMessage> errors, 
			BigDecimal maximumLoanPermitted) {
        
        if (maximumLoanPermitted == null) {
            LoanError error = new LoanError(LoanErrorCode.LOAN_AMOUNT_GREATER_THAN_MAXIMUM, 
            		LoanField.LOAN_AMOUNT);
            errors.add(error);
        }        
    }
	
    /**
     * validate for the right user role to approve loan
     * @param userRole
     * @return false if user role not allowed
     */
    public static boolean validateIsUserRoleAllowedToApprove(UserRole userRole) {
        
        if (userRole.hasPermission(PermissionType.SIGNING_AUTHORITY)) {            
            return true;
        }
        
        return false;        
    }	

    /**
     * Determines if the participant is allowed to send the loan for approval or not.
     * It is not allowed:
     *    - if within the shutdown period
     *    - and the payment method requested is not Check
     * @param loan
     * @return true if it is allowed
     */
    public static boolean isParticipantAllowedToSendForApproval(Loan loan) {
                   
        boolean isAllowed = false;
      
        try {
            isAllowed = EmployeeServiceDelegate.getInstance(new BaseEnvironment().getAppId())
                    .isFunctionAvailableForParticipant(loan.getContractId(), 
                            loan.getParticipantProfileId().longValue(),
                            FunctionCode.LOAN); 
        } catch (Exception e) {
            throw ExceptionHandlerUtility.wrap(e);
        }   
                
        if (! isAllowed) {
            String paymentMethodCode = null;
            if (loan.getRecipient() != null
                    && loan.getRecipient().getPayees() != null) {
                
                LoanPayee payee = (LoanPayee) loan.getRecipient()
                        .getPayees().iterator().next();
                
                paymentMethodCode = payee.getPaymentMethodCode();
            }
            // allowed if the payment method is check
            if (StringUtils.equalsIgnoreCase(
                    paymentMethodCode, Payee.CHECK_PAYMENT_METHOD_CODE)) {
                isAllowed = true;
            }
        }
        return isAllowed;
    }
    
    
    /**
   	 * Validate if the participant has a systematic withdrawals equal to one of 
   		1.	YP - Systematic Pre retirement Distribution (PD)
   		OR
   		2.	YR - Systematic Retirement Distribution (RE)
   		OR
   		3.	YT - Systematic Termination Distribution (TE)
   		AND
   	 Parent systematic withdrawal transaction status is equal to 
   	       1. ‘Active’ 
   	  or 
   	       2. ‘Pending’
   	 * 
   	 * @param messages
   	 * @param loanParticipantData
   	 * @throws SystemException 
   	 */
       public static void validateParticipantInstallmentWithdrawal(Integer contractNumber,List<LoanMessage> messages,
   			LoanParticipantData loanParticipantData) {
    	   try{
      			
      			List<SystematicWithdrawDataItem> reportItems=(List<SystematicWithdrawDataItem>)  ContractServiceDelegate.getInstance().getSystematicWithdrawalData(contractNumber, loanParticipantData.getParticipantId());
      			if(null!=reportItems && !reportItems.isEmpty()){   				
      			for(SystematicWithdrawDataItem item:reportItems)
      			{
      				if(item.getWdType().equalsIgnoreCase(Constants.REASONCODES_INSTALLMENT)){
      				if((item.getWdStatus().equalsIgnoreCase(WDStatusCodes.PENDING.getCode()))||(item.getWdStatus().equalsIgnoreCase(WDStatusCodes.ACTIVE.getCode()))){
      					if(null!=item.getParticipant() && null!=item.getParticipant().getId() && item.getParticipant().getId().intValue()==loanParticipantData.getParticipantId().intValue()){
      					 LoanError error = new LoanError(
   			                    LoanErrorCode.PARTICIPANT_HAS_INSTALLMENT_WITHDRAWAL);
   			            messages.add(error);
   			            break;
   							}
   						}

   					}
      				}
   				}
      		} catch (SystemException exception) {
   			try {
   				throw new DistributionServiceException(exception,
   						LoanValidationHelper.class.getName(),
   						"validateParticpantApplicableToLIA", exception.getMessage()) {
   					@Override
   					public String getErrorCode() {
   						return null;
   					}
   				};
   			} catch (DistributionServiceException e) {				
   				e.printStackTrace();
   			}
   			}
       }   

}
