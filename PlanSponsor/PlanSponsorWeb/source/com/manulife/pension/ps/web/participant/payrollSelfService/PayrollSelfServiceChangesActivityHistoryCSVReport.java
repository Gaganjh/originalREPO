package com.manulife.pension.ps.web.participant.payrollSelfService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceChangeRecord;
import com.manulife.pension.ps.web.util.ProtectedStringBuffer;

/**
 * Activity History CSV Report Writer for Payroll Self Service Changes. This is
 * a Singleton and therefore should not contains any state of the Report.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class PayrollSelfServiceChangesActivityHistoryCSVReport {

	private static final Logger logger = Logger.getLogger(PayrollSelfServiceChangesActivityHistoryCSVReport.class);

	private static final String DEFAULT_LINE_SEPERATOR = "\n";
	public static final String CSV_DELIMITER = ",";
	public static final String CONTRACT_NUMBER_CSV = "Contract ";
	public static final String CONTRACT_NAME_CSV = "Contract Name";
	public static final String EFFECTIVE_DATE_FROM_CSV = "Effective date from ";
	public static final String TO_DATE_CSV = "to";
	public static final String ACTIVITY_HISTORY_REPORT = "Activity History Report";	
	private static final String ACTIVITY_HISTORY_REPORT_DATE_FORMAT = "dd-MMM-yy";
	private static final String ACTIVITY_HISTORY_REPORT_TIME_FORMAT = "hh.mm aa";

	public static final ActivityHistoryCSVProcessor processor = new ActivityHistoryCSVProcessorImpl();

	private static final Set<String> DATA_HEADER_TITLES = Collections
			.unmodifiableSet(new LinkedHashSet<>(Arrays.asList("Participant Name", "SSN", "Participant Status", "Division",
					"Description", "Initiated", "Initiated Time", "Effective Date", "Details", "Money Type", "Method", "Initiated By",
					 "Status", "Action Date", "Action Time", "Action Taken By")));

	private static String getLineDelimiter() {
		return ObjectUtils.firstNonNull(BaseReportController.LINE_BREAK, DEFAULT_LINE_SEPERATOR);
	}

	private void generateHeader(PayrollSelfServiceChangesForm payrollSelfServiceChangesForm, ProtectedStringBuffer sb) {
		sb.append(ACTIVITY_HISTORY_REPORT).append(getLineDelimiter());
		sb.append(CONTRACT_NUMBER_CSV).append(Objects.toString(payrollSelfServiceChangesForm.getContractId(), ""))
				.append(" ");
		sb.append(processor.wrapDoubleQuotesIfContainsDelimiter(
				(Supplier<String>) () -> Objects.toString(payrollSelfServiceChangesForm.getContractName(), "")))
				.append(getLineDelimiter());
		if (StringUtils.isNotBlank(payrollSelfServiceChangesForm.getEffectiveDateFrom())
				&& StringUtils.isNotBlank(payrollSelfServiceChangesForm.getEffectiveDateTo())) {
			StringBuilder effectiveDateFromAndToString = new StringBuilder();
			effectiveDateFromAndToString.append(EFFECTIVE_DATE_FROM_CSV).append(" ");
			effectiveDateFromAndToString.append(payrollSelfServiceChangesForm.getEffectiveDateFromAsLocalDate()
					.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
			effectiveDateFromAndToString.append(" ").append(TO_DATE_CSV).append(" ");
			effectiveDateFromAndToString.append(payrollSelfServiceChangesForm.getEffectiveDateToAsLocalDate()
					.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));

			sb.append(processor.wrapDoubleQuotesIfContainsDelimiter(
					(Supplier<String>) () -> Objects.toString(effectiveDateFromAndToString.toString(), "")))
					.append(getLineDelimiter());
		}
	}

	public String generateCSV(Collection<PayrollSelfServiceChangeRecord> changes,
			PayrollSelfServiceChangesForm payrollSelfServiceChangesForm, final boolean maskSSN) {
		ProtectedStringBuffer sb = new ProtectedStringBuffer();
		// Header
		generateHeader(payrollSelfServiceChangesForm, sb);
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
			logger.debug(" The PayrollSelfServiceChange Record to generate CSV is " + payrollSelfServiceChange.toString());
		}
		
		ActivityHistoryCSVRecordProcessingDetail recordProcessingDetail = getActivityHistoryCSVRecordProcessingDetail(payrollSelfServiceChange);
		
		// Participant Name
		sb.append(processor.wrapDoubleQuotesIfContainsDelimiter(
				(Supplier<String>) () -> Objects.toString(payrollSelfServiceChange.getParticipantName(), "")))
				.append(CSV_DELIMITER);
		// SSN
		sb.append(payrollSelfServiceChange.getMaskedSsn())
				.append(CSV_DELIMITER);
		// Participant status
		sb.append(payrollSelfServiceChange.getParticipantStatusDescription()).append(CSV_DELIMITER);
		// Division
		sb.append(processor.wrapDoubleQuotesIfContainsDelimiter(
				(Supplier<String>) () -> Objects.toString(payrollSelfServiceChange.getEmployerDivision(), "")))
				.append(CSV_DELIMITER);
		// Description
		sb.append(processor.wrapDoubleQuotesIfContainsDelimiter(
				(Supplier<String>) () -> Objects.toString(payrollSelfServiceChange.getDescription(), "")))
				.append(CSV_DELIMITER);
		// Initiated
		sb.append(getHistoryReportDateFormat(payrollSelfServiceChange.getCreatedDate(),
				ACTIVITY_HISTORY_REPORT_DATE_FORMAT)).append(CSV_DELIMITER);
		// Initiated Time
		{
			String element = payrollSelfServiceChange.isInitiatedTimeReported() ? 
					getHistoryReportDateFormat(payrollSelfServiceChange.getCreatedDate(), ACTIVITY_HISTORY_REPORT_TIME_FORMAT) : StringUtils.EMPTY;
			sb.append(element).append(CSV_DELIMITER);
		}	
		// Effective Date
		sb.append(getHistoryReportDateFormat(payrollSelfServiceChange.getEffectiveDate(),
				ACTIVITY_HISTORY_REPORT_DATE_FORMAT)).append(CSV_DELIMITER);
		// Details
		sb.append(Objects.toString(payrollSelfServiceChange.getDetails(), "")).append(CSV_DELIMITER);
		// Money Type
		sb.append(Objects.toString(payrollSelfServiceChange.getMoneyTypeDescription(), "")).append(CSV_DELIMITER);
		// Method
		sb.append(Objects.toString(payrollSelfServiceChange.getInitiationMethod(), "")).append(CSV_DELIMITER);
		// Initiated by
		sb.append(processor.wrapDoubleQuotesIfContainsDelimiter(
				(Supplier<String>) () -> Objects.toString(payrollSelfServiceChange.getInitiatedBy(), "")))
				.append(CSV_DELIMITER);
		// Status
		sb.append(Objects.toString(recordProcessingDetail.status, "")).append(CSV_DELIMITER);
		// Status date
		sb.append(Objects.toString(recordProcessingDetail.formattedProcessedDate, "")).append(CSV_DELIMITER);
		// Action Time
		sb.append(Objects.toString(recordProcessingDetail.formattedProcessedTime, "")).append(CSV_DELIMITER);
		// Action Taken by
		sb.append(processor.wrapDoubleQuotesIfContainsDelimiter(
				(Supplier<String>) () -> Objects.toString(recordProcessingDetail.processedBy, "")))
				.append(CSV_DELIMITER);		
		if (logger.isDebugEnabled()) {
			logger.debug(" The generated CSV record for this payrollSelfServiceChangeRecord " + sb.toString());
		}

	}

	private String getHistoryReportDateFormat(Date date, String dateFormat) {
		if (date == null) {
			return "";
		}
		DateFormat df = new SimpleDateFormat(dateFormat);
		return df.format(date);
	}

	private ActivityHistoryCSVRecordProcessingDetail getActivityHistoryCSVRecordProcessingDetail(PayrollSelfServiceChangeRecord changeRecord) {
		ActivityHistoryCSVRecordProcessingDetail recordProcessingDetail = new ActivityHistoryCSVRecordProcessingDetail();
		if(changeRecord.isProcessingDetailReported()) {
			recordProcessingDetail.status = changeRecord.getStatusDescription();
			recordProcessingDetail.formattedProcessedDate = getHistoryReportDateFormat(changeRecord.getProcessedDate(), ACTIVITY_HISTORY_REPORT_DATE_FORMAT);
			recordProcessingDetail.formattedProcessedTime = getHistoryReportDateFormat(changeRecord.getProcessedDate(), ACTIVITY_HISTORY_REPORT_TIME_FORMAT);
			recordProcessingDetail.processedBy = changeRecord.getProcessedBy();
		}

		return recordProcessingDetail;
	}


}

interface ActivityHistoryCSVProcessor {
	String wrapDoubleQuotesIfContainsDelimiter(Supplier<String> str);
}

class ActivityHistoryCSVProcessorImpl implements ActivityHistoryCSVProcessor {
	private static final String DOUBLE_QUOTES = "\"";

	@Override
	public String wrapDoubleQuotesIfContainsDelimiter(Supplier<String> str) {

		if (str.get().contains(",")) {
			return DOUBLE_QUOTES + str.get() + DOUBLE_QUOTES;
		}
		return str.get();
	}

}

class ActivityHistoryCSVRecordProcessingDetail {
	public String status;
	public String formattedProcessedDate = StringUtils.EMPTY;
	public String formattedProcessedTime = StringUtils.EMPTY;
	public String processedBy = StringUtils.EMPTY;
}
