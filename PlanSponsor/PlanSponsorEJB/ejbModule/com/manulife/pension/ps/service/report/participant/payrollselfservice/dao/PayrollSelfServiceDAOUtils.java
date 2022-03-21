package com.manulife.pension.ps.service.report.participant.payrollselfservice.dao;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceChangeRecord;
import com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceChangesReportData;
import com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceDeferralRecord;
import com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceEnrollmentRecord;
import com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceLoanClosureRecord;
import com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceLoanIssueRecord;
import com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceLoanPayOffRecord;
import com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceLoanRecord;

public class PayrollSelfServiceDAOUtils {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(PayrollSelfServiceDAOUtils.class);
	
	private static final String LOAN_ORIGIN_TRANSFERRED_CODE = "T";

	/**
	 * Parses out Deferral Change Record(s) from query result row.
	 * <p />
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	public static Collection<PayrollSelfServiceChangeRecord> getDeferralChangeRecords(ResultSet resultSet) throws SQLException {

		PayrollSelfServiceDeferralRecord deferralChangeRecord = 
				(PayrollSelfServiceDeferralRecord) PayrollSelfServiceDAOUtils.getChangeRecord(PayrollSelfServiceDeferralRecord.class, resultSet);

		BigDecimal percentageValue = resultSet.getBigDecimal(PayrollSelfServiceChangesReportData.CONTRIB_PCT_COLUMN);
		if(percentageValue != null) {
			deferralChangeRecord.setValueTypeCode(PayrollSelfServiceChangeRecord.VALUE_TYPE_PERCENTAGE_CODE);
			deferralChangeRecord.setValue(percentageValue);
		}

		BigDecimal dollarValue = resultSet.getBigDecimal(PayrollSelfServiceChangesReportData.CONTRIB_AMT_COLUMN);
		if(dollarValue != null) {
			deferralChangeRecord.setValueTypeCode(PayrollSelfServiceChangeRecord.VALUE_TYPE_FLAT_DOLLAR_AMOUNT_CODE);
			deferralChangeRecord.setValue(dollarValue);
		}
		
		deferralChangeRecord.setCreatedUserIdTypeCode(resultSet.getString(PayrollSelfServiceChangesReportData.CREATED_USER_ID_TYPE_COLUMN));
		deferralChangeRecord.setProcessedDate(resultSet.getDate(PayrollSelfServiceChangesReportData.TIME_PROCESSED_COLUMN));
		deferralChangeRecord.setProcessedUserIdTypeCode(resultSet.getString( PayrollSelfServiceChangesReportData.PROCESSED_USER_ID_TYPE_COLUMN));
		deferralChangeRecord.setProcessedSourceCode(resultSet.getString(PayrollSelfServiceChangesReportData.PROCESSED_SOURCE_CODE_COLUMN));
		deferralChangeRecord.setStatusCode(resultSet.getString(PayrollSelfServiceChangesReportData.STATUS_COLUMN));
		deferralChangeRecord.setMoneyTypeCode(resultSet.getString(PayrollSelfServiceChangesReportData.MONEY_TYPE_CODE_COLUMN));		

		return Arrays.asList(deferralChangeRecord);
	}

	/**
	 * Parses out Enrollment Change Record(s) from query result row.
	 * <p />
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	public static Collection<PayrollSelfServiceEnrollmentRecord> getEnrollmentChangeRecords(ResultSet resultSet) throws SQLException {
		Collection<PayrollSelfServiceEnrollmentRecord> enrollmentChangeRecords = new ArrayList<>();
		
		// Trad %
		{
			BigDecimal value = resultSet.getBigDecimal(PayrollSelfServiceChangesReportData.ENROLL_BEFORE_TAX_DEFER_PCT_COLUMN);
			if(PayrollSelfServiceDAOUtils.isNonZeroValue(value)) {
				PayrollSelfServiceEnrollmentRecord enrollmentRecord = 
						(PayrollSelfServiceEnrollmentRecord) PayrollSelfServiceDAOUtils.getChangeRecord(PayrollSelfServiceEnrollmentRecord.class, resultSet);
				enrollmentRecord.setMoneyTypeCode(PayrollSelfServiceChangesReportData.TRAD_MONEY_TYPE_CODE);
				enrollmentRecord.setValue(value);
				enrollmentRecord.setValueTypeCode(PayrollSelfServiceChangeRecord.VALUE_TYPE_PERCENTAGE_CODE);

				enrollmentChangeRecords.add(enrollmentRecord);
				
			}
		}

		// Trad $
		{
			BigDecimal value = resultSet.getBigDecimal(PayrollSelfServiceChangesReportData.ENROLL_BEFORE_TAX_DEFER_AMT_COLUMN);
			if(PayrollSelfServiceDAOUtils.isNonZeroValue(value)) {
				PayrollSelfServiceEnrollmentRecord enrollmentRecord = 
						(PayrollSelfServiceEnrollmentRecord) PayrollSelfServiceDAOUtils.getChangeRecord(PayrollSelfServiceEnrollmentRecord.class, resultSet);
				enrollmentRecord.setMoneyTypeCode(PayrollSelfServiceChangesReportData.TRAD_MONEY_TYPE_CODE);
				enrollmentRecord.setValue(value);
				enrollmentRecord.setValueTypeCode(PayrollSelfServiceChangeRecord.VALUE_TYPE_FLAT_DOLLAR_AMOUNT_CODE);

				enrollmentChangeRecords.add(enrollmentRecord);
			}
		}


		// Roth %
		{
			BigDecimal value = resultSet.getBigDecimal(PayrollSelfServiceChangesReportData.ENROLL_DESIG_ROTH_DEF_PCT_COLUMN);
			if(PayrollSelfServiceDAOUtils.isNonZeroValue(value)) {
				PayrollSelfServiceEnrollmentRecord enrollmentRecord = 
						(PayrollSelfServiceEnrollmentRecord) PayrollSelfServiceDAOUtils.getChangeRecord(PayrollSelfServiceEnrollmentRecord.class, resultSet);
				enrollmentRecord.setMoneyTypeCode(PayrollSelfServiceChangesReportData.ROTH_MONEY_TYPE_CODE);
				enrollmentRecord.setValue(value);
				enrollmentRecord.setValueTypeCode(PayrollSelfServiceChangeRecord.VALUE_TYPE_PERCENTAGE_CODE);

				enrollmentChangeRecords.add(enrollmentRecord);
			}
		}

		// Roth $
		{
			BigDecimal value = resultSet.getBigDecimal(PayrollSelfServiceChangesReportData.ENROLL_DESIG_ROTH_DEF_AMT_COLUMN);
			if(PayrollSelfServiceDAOUtils.isNonZeroValue(value)) {
				PayrollSelfServiceEnrollmentRecord enrollmentRecord = 
						(PayrollSelfServiceEnrollmentRecord) PayrollSelfServiceDAOUtils.getChangeRecord(PayrollSelfServiceEnrollmentRecord.class, resultSet);
				enrollmentRecord.setMoneyTypeCode(PayrollSelfServiceChangesReportData.ROTH_MONEY_TYPE_CODE);
				enrollmentRecord.setValue(value);
				enrollmentRecord.setValueTypeCode(PayrollSelfServiceChangeRecord.VALUE_TYPE_FLAT_DOLLAR_AMOUNT_CODE);

				enrollmentChangeRecords.add(enrollmentRecord);
			}
		}
		
		return enrollmentChangeRecords;		
	}

	/**
	 * Parses out Loan Change Record(s) from query result row.
	 * <p />
	 * @param resultSet
	 * @param effectiveDateFrom
	 * @param effectiveDateTo
	 * @return
	 * @throws SQLException
	 */
	public static Collection<PayrollSelfServiceChangeRecord> getLoanChangeRecords(ResultSet resultSet, java.util.Date effectiveDateFrom, java.util.Date effectiveDateTo) throws SQLException {
		Date payrollFeedbackServiceAddedDate = resultSet.getDate(PayrollSelfServiceChangesReportData.TIME_PAYROLL_FEEDBACK_SERVICE_ADDED_COLUMN);
		
		if(payrollFeedbackServiceAddedDate == null) {
			 return Collections.emptyList();
		}
		Collection<PayrollSelfServiceChangeRecord> loanChangeRecords = new ArrayList<>();

		// Issue
		{
			Date loanIssueDate = resultSet.getDate(PayrollSelfServiceChangesReportData.ISSUE_TRANSACTION_EFFECTIVE_DATE_COLUMN);
			if(loanIssueDate != null 
					&& loanEventDateInRange(loanIssueDate, payrollFeedbackServiceAddedDate, effectiveDateFrom, effectiveDateTo)
					&& !LOAN_ORIGIN_TRANSFERRED_CODE.equalsIgnoreCase(resultSet.getString(PayrollSelfServiceChangesReportData.LOAN_ORIGIN_CODE_COLUMN))) {

				PayrollSelfServiceLoanIssueRecord loanChangeRecord = 
						(PayrollSelfServiceLoanIssueRecord) PayrollSelfServiceDAOUtils.getChangeRecord(PayrollSelfServiceLoanIssueRecord.class, resultSet);
				loanChangeRecord.setLoanNumber(getInteger(resultSet, PayrollSelfServiceChangesReportData.LOAN_NUMBER_COLUMN));
				loanChangeRecord.setCreatedDate(resultSet.getDate(PayrollSelfServiceChangesReportData.ISSUE_TRANSACTION_PROCESSING_DATE_COLUMN));
				loanChangeRecord.setEffectiveDate(resultSet.getDate(PayrollSelfServiceChangesReportData.ISSUE_TRANSACTION_EFFECTIVE_DATE_COLUMN));
				loanChangeRecord.setPrincipalAmount(resultSet.getBigDecimal(PayrollSelfServiceChangesReportData.LOAN_PRINCIPAL_AMOUNT_COLUMN));
				loanChangeRecord.setTotalInterestAmount(resultSet.getBigDecimal(PayrollSelfServiceChangesReportData.LOAN_TOTAL_INTEREST_AMOUNT_COLUMN));
				loanChangeRecord.setGoalAmount(resultSet.getBigDecimal(PayrollSelfServiceChangesReportData.LOAN_GOAL_AMOUNT_COLUMN));	
				loanChangeRecord.setNumberOfPayments(getInteger(resultSet, PayrollSelfServiceChangesReportData.LOAN_NUMBER_OF_PAYMENTS_COLUMN));	
				loanChangeRecord.setValue(resultSet.getBigDecimal(PayrollSelfServiceChangesReportData.LOAN_EXPECTED_REPAYMENT_AMOUNT_COLUMN));
				loanChangeRecord.setValueTypeCode(PayrollSelfServiceChangeRecord.VALUE_TYPE_FLAT_DOLLAR_AMOUNT_CODE);
				loanChangeRecord.setInitiatedByLastName(resultSet.getString(PayrollSelfServiceChangesReportData.LOAN_SUBMITTED_BY_LAST_NAME_COLUMN));
				loanChangeRecord.setInitiatedByFirstName(resultSet.getString(PayrollSelfServiceChangesReportData.LOAN_SUBMITTED_BY_FIRST_NAME_COLUMN));

				loanChangeRecords.add(loanChangeRecord);
			}
		}
		
		// Closures // Pay off
		final String closureTypeCode = StringUtils
				.upperCase(resultSet.getString(PayrollSelfServiceChangesReportData.LOAN_STATUS_CODE_COLUMN));
		if(!PayrollSelfServiceLoanRecord.LOAN_STATUS_ACTIVE_CODE.equals(StringUtils.upperCase(closureTypeCode))) {
			final Date loanStatusDate = resultSet.getDate(PayrollSelfServiceChangesReportData.LOAN_STATUS_DATE_COLUMN);
			if(loanStatusDate != null 
					&& loanEventDateInRange(loanStatusDate, payrollFeedbackServiceAddedDate, effectiveDateFrom, effectiveDateTo)) {
				PayrollSelfServiceLoanRecord loanChangeRecord = null;
				if (PayrollSelfServiceLoanPayOffRecord.LOAN_CLOSURE_TYPE_PAY_OFF_CODE.equals(closureTypeCode)) {
					loanChangeRecord = (PayrollSelfServiceLoanRecord) PayrollSelfServiceDAOUtils
							.getChangeRecord(PayrollSelfServiceLoanPayOffRecord.class, resultSet);
				} else {
					PayrollSelfServiceLoanClosureRecord loanClosureRecord = (PayrollSelfServiceLoanClosureRecord) PayrollSelfServiceDAOUtils
							.getChangeRecord(PayrollSelfServiceLoanClosureRecord.class, resultSet);

					loanClosureRecord.setLoanClosureTypeCode(closureTypeCode);
					loanChangeRecord = loanClosureRecord;
				}
				loanChangeRecord.setLoanNumber(getInteger(resultSet, PayrollSelfServiceChangesReportData.LOAN_NUMBER_COLUMN));
				loanChangeRecord.setCreatedDate(resultSet.getDate(PayrollSelfServiceChangesReportData.TIME_CREATED_COLUMN));
				loanChangeRecord.setEffectiveDate(loanStatusDate);
				loanChangeRecord.setLoanIssueEffectiveDate(resultSet.getDate(PayrollSelfServiceChangesReportData.ISSUE_TRANSACTION_EFFECTIVE_DATE_COLUMN));
				loanChangeRecord.setValue(BigDecimal.ZERO);
				loanChangeRecord.setValueTypeCode(PayrollSelfServiceChangeRecord.VALUE_TYPE_FLAT_DOLLAR_AMOUNT_CODE);
				loanChangeRecords.add(loanChangeRecord);
			}
		}

		return loanChangeRecords;
	}
	
	protected static PayrollSelfServiceChangeRecord getChangeRecord(Class<? extends PayrollSelfServiceChangeRecord> changeRecordClazz,
			ResultSet resultSet) throws SQLException {

		PayrollSelfServiceChangeRecord changeRecord = null;

		try {
			changeRecord = changeRecordClazz.newInstance();
		} catch (InstantiationException | IllegalAccessException exception) {
			throw new RuntimeException(exception);
		}

		changeRecord.setContractId(resultSet.getLong(PayrollSelfServiceChangesReportData.CONTRACT_ID_COLUMN));
		changeRecord.setProfileId(resultSet.getLong( PayrollSelfServiceChangesReportData.PROFILE_ID_COLUMN));
		changeRecord.setContractName(resultSet.getString(PayrollSelfServiceChangesReportData.CONTRACT_NAME_COLUMN));

		changeRecord.setFirstName(resultSet.getString(PayrollSelfServiceChangesReportData.FIRST_NAME_COLUMN));
		changeRecord.setMiddleInitial(resultSet.getString(PayrollSelfServiceChangesReportData.MIDDLE_INITIAL_COLUMN));
		changeRecord.setLastName(resultSet.getString(PayrollSelfServiceChangesReportData.LAST_NAME_COLUMN));
		changeRecord.setEmployeeId(resultSet.getString(PayrollSelfServiceChangesReportData.EMPLOYEE_ID_COLUMN));
		changeRecord.setEmployerDivision(resultSet.getString(PayrollSelfServiceChangesReportData.EMPLOYER_DIVISION_COLUMN));
		changeRecord.setSsn(resultSet.getString(PayrollSelfServiceChangesReportData.SSN_COLUMN));
		changeRecord.setParticipantStatusCode(resultSet.getString(PayrollSelfServiceChangesReportData.PARTICIPANT_STATUS_CODE_COLUMN));

		changeRecord.setCreatedDate(resultSet.getDate(PayrollSelfServiceChangesReportData.TIME_CREATED_COLUMN));
		changeRecord.setCreatedSourceCode(resultSet.getString(PayrollSelfServiceChangesReportData.CREATED_SOURCE_CODE_COLUMN));
		changeRecord.setEffectiveDate(resultSet.getDate(PayrollSelfServiceChangesReportData.EFFECTIVE_DATE_COLUMN));
		
		return changeRecord;
	}

	private static Integer getInteger(ResultSet resultSet, String columnName) throws SQLException {
		String value = resultSet.getString(columnName);
		if(value != null) {
			return new BigDecimal(value).intValue();
		}

		return null;
	}

	private static boolean isNonZeroValue(BigDecimal value) {
		boolean isNullOrZeroValue = value == null
				|| BigDecimal.ZERO.compareTo(value) == 0;
		return !isNullOrZeroValue;
	}
	
	/**
	 * Method ensures Loan matches filter criteria. Primary reason for this method is to reduce conditional logic typically flagged by 
	 * SonarQube when exceeding configured threshold.
	 * <br />
	 * @param loanEventDate
	 * @param payrollFeedbackServiceAddedDate
	 * @param effectiveDateFrom
	 * @param effectiveDateTo
	 * @return
	 */
	private static boolean loanEventDateInRange(Date loanEventDate, Date payrollFeedbackServiceAddedDate,
			java.util.Date effectiveDateFrom, java.util.Date effectiveDateTo) {

		if(loanEventDate.before(payrollFeedbackServiceAddedDate)) {
			return false;
		}

		if(effectiveDateFrom != null) {
			if(effectiveDateFrom.after(loanEventDate)) {
				return false;
			}
		}

		if(effectiveDateTo != null) {
			if(effectiveDateTo.before(loanEventDate)) {
				return false;
			}
		}

		return true;
	}

}
