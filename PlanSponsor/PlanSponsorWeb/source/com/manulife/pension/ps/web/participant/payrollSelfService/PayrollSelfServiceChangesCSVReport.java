package com.manulife.pension.ps.web.participant.payrollSelfService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceChangeRecord;
import com.manulife.pension.ps.web.util.ProtectedStringBuffer;

/**
 * CSV Report Writer for Payroll Self Service Changes. 
 * This is a Singleton and therefore should not contains any state of the Report.
  */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class PayrollSelfServiceChangesCSVReport {

	private static final Logger logger = Logger.getLogger(PayrollSelfServiceChangesCSVReport.class);

	private static final String DEFAULT_LINE_SEPERATOR = "\n";
	public static final String CSV_DELIMITER = ",";
	public static final String CONTRACT_NUMBER_CSV = "Contract Number";
	public static final String CONTRACT_NAME_CSV = "Contract Name";
	public static final String FROM_DATE_CSV = "From date";
	public static final String TO_DATE_CSV = "To date";
	public static final String TRANSACTION_TYPE = "Transaction Type";
	public static final String DESCRIPTION = "Description";
	public static final String REPORTED_VALUE_DATE_FORMAT = "MMddyyyy";

	public static final CSVProcessor processor = new CSVProcessorImpl();

	private static final Map<String, String> HEADER_NOTES;
	private static final Set<String> DATA_HEADER_TITLES = Collections.unmodifiableSet(
			new LinkedHashSet<>(Arrays.asList("TransactionType", "EffectiveDate", "SSN", "EmployeeNumber", "FirstName",
					"LastName", "Divison", "Region", "PayGroup", "DeferralValue/LoanStartDate", "LoanRepayment", "NewLoanAmount",
					"TotalInterest", "GoalAmount", "Numberofpayments", "LoanID", "AggregateRepayAmount")));

	static {
		Map<String, String> headerNotesMap = new LinkedHashMap<>(7);
		headerNotesMap.put(PayrollSelfServiceChangeRecord.TransactionType.PRE_TAX_DEFERRAL_PERCENT.getCode(),
				PayrollSelfServiceChangeRecord.TransactionType.PRE_TAX_DEFERRAL_PERCENT.getDescription());
		headerNotesMap.put(PayrollSelfServiceChangeRecord.TransactionType.ROTH_DEFERRAL_PERCENT.getCode(),
				PayrollSelfServiceChangeRecord.TransactionType.ROTH_DEFERRAL_PERCENT.getDescription());
		headerNotesMap.put(PayrollSelfServiceChangeRecord.TransactionType.PRE_TAX_DOLLARS.getCode(),
				PayrollSelfServiceChangeRecord.TransactionType.PRE_TAX_DOLLARS.getDescription());
		headerNotesMap.put(PayrollSelfServiceChangeRecord.TransactionType.ROTH_DOLLARS.getCode(),
				PayrollSelfServiceChangeRecord.TransactionType.ROTH_DOLLARS.getDescription());
		headerNotesMap.put(PayrollSelfServiceChangeRecord.TransactionType.LOAN_ISSUE.getCode(), PayrollSelfServiceChangeRecord.TransactionType.LOAN_ISSUE.getDescription());
		headerNotesMap.put(PayrollSelfServiceChangeRecord.TransactionType.LOAN_PAID_IN_FULL.getCode(),
				new StringBuilder()
						.append(PayrollSelfServiceChangeRecord.TransactionType.LOAN_PAID_IN_FULL.getDescription())
						.append(" or ")
						.append(PayrollSelfServiceChangeRecord.TransactionType.LOAN_CLOSURE.getDescription())
						.toString());
		HEADER_NOTES = Collections.unmodifiableMap(headerNotesMap);
	}

	private static String getLineDelimiter() {
		return ObjectUtils.firstNonNull(BaseReportController.LINE_BREAK, DEFAULT_LINE_SEPERATOR);
	}

	private void generateHeader(PayrollSelfServiceChangesForm payrollSelfServiceChangesForm, ProtectedStringBuffer sb) {
		sb.append(CONTRACT_NUMBER_CSV).append(CSV_DELIMITER)
				.append(Objects.toString(payrollSelfServiceChangesForm.getContractId(), "")).append(getLineDelimiter());
		sb.append(CONTRACT_NAME_CSV).append(CSV_DELIMITER)
				.append(processor.wrapDoubleQuotesIfContainsDelimiter(
						(Supplier<String>) () -> Objects.toString(payrollSelfServiceChangesForm.getContractName(), "")))
				.append(getLineDelimiter());
		sb.append(FROM_DATE_CSV).append(CSV_DELIMITER)
				.append(Objects.toString(payrollSelfServiceChangesForm.getEffectiveDateFrom(), ""))
				.append(getLineDelimiter());
		sb.append(TO_DATE_CSV).append(CSV_DELIMITER)
				.append(Objects.toString(payrollSelfServiceChangesForm.getEffectiveDateTo(), ""))
				.append(getLineDelimiter());
	}

	private void generateHeaderNotes(ProtectedStringBuffer sb) {
		sb.append(TRANSACTION_TYPE).append(CSV_DELIMITER).append(DESCRIPTION).append(getLineDelimiter());
		for (Map.Entry<String, String> entry : HEADER_NOTES.entrySet()) {
			sb.append(entry.getKey()).append(CSV_DELIMITER).append(entry.getValue());
			sb.append(getLineDelimiter());
		}		
	}

	public String generateCSV(Collection<PayrollSelfServiceChangeRecord> changes,
			PayrollSelfServiceChangesForm payrollSelfServiceChangesForm, final boolean maskSSN) {
		ProtectedStringBuffer sb = new ProtectedStringBuffer();
		// Header
		generateHeader(payrollSelfServiceChangesForm, sb);
		sb.append(getLineDelimiter());
		// Header Notes
		generateHeaderNotes(sb);
		sb.append(getLineDelimiter());
		// Data Title
		DATA_HEADER_TITLES.forEach(title -> sb.append(title).append(CSV_DELIMITER));
		sb.append(getLineDelimiter());
		// Data
		for (PayrollSelfServiceChangeRecord change : changes) {
			getEachData(change, sb, maskSSN);
			sb.append(getLineDelimiter());
		}
		return sb.toString();
	}

	private void getEachData(PayrollSelfServiceChangeRecord payrollSelfServiceChange, ProtectedStringBuffer sb,
			boolean maskSSN) {
		
		if (logger.isDebugEnabled()) {
			logger.debug(" The PayrollSelfServiceChangeRecord to generate CSV is " + payrollSelfServiceChange.toString());
		}
		 
		// TransactionTYpe
		sb.append(payrollSelfServiceChange.getTransactionType() != null ? payrollSelfServiceChange.getTransactionType().getCode() : "").append(CSV_DELIMITER);
		// Effective Date
		sb.append(Objects.toString(PayrollSelfServiceChangesWebUtility.getFormattedValue(payrollSelfServiceChange.getEffectiveDate(), null), "")).append(CSV_DELIMITER);
		// SSN
		sb.append(Objects.toString(maskSSN ? payrollSelfServiceChange.getMaskedSsn() : payrollSelfServiceChange.getSsn(), ""))
				.append(CSV_DELIMITER);
		// EmployeeNumber
		sb.append(Objects.toString(payrollSelfServiceChange.getEmployeeId(), "")).append(CSV_DELIMITER);
		// First Name
		sb.append(processor.wrapDoubleQuotesIfContainsDelimiter(
				(Supplier<String>) () -> Objects.toString(payrollSelfServiceChange.getFirstName(), "")))
				.append(CSV_DELIMITER);
		// Last Name
		sb.append(processor.wrapDoubleQuotesIfContainsDelimiter(
				(Supplier<String>) () -> Objects.toString(payrollSelfServiceChange.getLastName(), "")))
				.append(CSV_DELIMITER);
		// Division
		sb.append(processor.wrapDoubleQuotesIfContainsDelimiter(
				(Supplier<String>) () -> Objects.toString(payrollSelfServiceChange.getEmployerDivision(), "")))
				.append(CSV_DELIMITER);
		// Region
		sb.append(CSV_DELIMITER);
		// PayGroup
		sb.append(CSV_DELIMITER);
		// DeferralValue
		sb.append(Objects.toString(PayrollSelfServiceChangesWebUtility.getFormattedValue(payrollSelfServiceChange.getFileReportedValue(), payrollSelfServiceChange.getValueTypeCode())));
		sb.append(CSV_DELIMITER);
		// Loan Repayment
		sb.append(Objects.toString(payrollSelfServiceChange.getFormattedLoanRepaymentAmount()));
		sb.append(CSV_DELIMITER);
		// New Loan Amount
		sb.append(Objects.toString(payrollSelfServiceChange.getFormattedLoanPrincipalAmount()));
		sb.append(CSV_DELIMITER);
		// Total Interest
		sb.append(Objects.toString(payrollSelfServiceChange.getFormattedLoanTotalInterestAmount()));
		sb.append(CSV_DELIMITER);
		// Goal Amount
		sb.append(Objects.toString(payrollSelfServiceChange.getFormattedLoanGoalAmount()));
		sb.append(CSV_DELIMITER);
		// Number of payments
		sb.append(Objects.toString(payrollSelfServiceChange.getFormattedNumberOfLoanPayments()));
		sb.append(CSV_DELIMITER);
		// Loan ID
		sb.append(Objects.toString(payrollSelfServiceChange.getFormattedLoanNumber()));
		sb.append(CSV_DELIMITER);
		// Aggregrate Repay Amount
		//sb.append(CSV_DELIMITER);
		
		if (logger.isDebugEnabled()) {
			logger.debug(" The generated CSV record for this payrollSelfServiceChangeRecord " + sb.toString());
		}
		 
	}
}

interface CSVProcessor {
	String wrapDoubleQuotesIfContainsDelimiter(Supplier<String> str);
}

class CSVProcessorImpl implements CSVProcessor {
	private static final String DOUBLE_QUOTES = "\"";

	@Override
	public String wrapDoubleQuotesIfContainsDelimiter(Supplier<String> str) {

		if (str.get().contains(",")) {
			return DOUBLE_QUOTES + str.get() + DOUBLE_QUOTES;
		}
		return str.get();
	}

}
