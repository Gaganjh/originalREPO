package com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;

import com.manulife.pension.service.account.valueobject.Deferral;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter
@ToString
public abstract class PayrollSelfServiceChangeRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	static final int MAXIMUM_DECIMAL_DIGIT_FOR_AMOUNT_DISPLAY	= 2;
	static final int MAXIMUM_DECIMAL_DIGIT_FOR_PERCENT_DISPLAY 	= 3;
	static final String REGEX_SSN_CAPTURE_LAST_FOUR_DIGITS 		= "(?:\\d{3})(?:\\d{2})(\\d{4})";
	static final String USER_ID_TYPE_PARTICIPANT_CODE			= "PAR";
	static final String USER_ID_TYPE_INTERNAL_CODE				= "UPI";
	static final String JOHN_HANCOCK 							= "John Hancock";
	static final String ENROLLMENT_METHOD_DEFAULT_LABEL 		= "Default";
	static final String AUTOMATED_PROCESSED_SOURCE_CODE 		= "AU";
	static final String PARTICIPANT_PROCESSED_SOURCE_CODE 		= "PA";
	static final String ACTION_TAKEN_BY_AUTOMATED 				= "Automated";
	static final String ONLINE_ENROLLMENT_METHOD_CODE 			= "I";	

	public static final String VALUE_TYPE_PERCENTAGE_CODE 			= "P";
	public static final String VALUE_TYPE_FLAT_DOLLAR_AMOUNT_CODE 	= "D";

	static final Map<String, String> PARTICIPANT_STATUS_DESCRIPTION_MAP = Stream
			.of(new String[][] { { "AC", "Active" }, { "AP", "Active" }, { "AT", "Active" }, { "AU", "Active" },
				{ "BP", "Disabled" }, { "BT", "Disabled" }, { "BU", "Disabled" }, { "CN", "Cancelled" },
				{ "DI", "Discontinued" }, { "DP", "Deceased" }, { "DT", "Deceased" }, { "DU", "Deceased" },
				{ "NT", "Opt Out" }, { "OT", "Other" }, { "PC", "Active" }, { "PT", "Active" }, { "PU", "Active" },
				{ "RP", "Retired" }, { "RT", "Retired" }, { "RU", "Retired" }, { "SR", "Terminated" },
				{ "TP", "Terminated" }, { "TT", "Terminated" }, { "TU", "Terminated" }})
			.collect(Collectors.collectingAndThen(Collectors.toMap(data -> data[0], data -> data[1]),
					Collections::<String, String>unmodifiableMap));

	static final Map<String, String> MONEY_TYPE_DESCRIPTION_MAP = Stream
			.of(new String[][] { { "EEDEF", "Before tax" }, { "EEROT", "Roth" }})
			.collect(Collectors.collectingAndThen(Collectors.toMap(data -> data[0], data -> data[1]),
					Collections::<String, String>unmodifiableMap));

	public enum TransactionType {
		PRE_TAX_DEFERRAL_PERCENT("PTXP", "Pre Tax Deferral Percentage"),
		ROTH_DEFERRAL_PERCENT("RTHP", "Roth Deferral Percentage"), 
		PRE_TAX_DOLLARS("PTXD", "Pre Tax Dollars"),
		ROTH_DOLLARS("RTHD", "Roth Dollars"), LOAN_ISSUE("LOAN", "Loan Issue"), LOAN_CLOSURE("LPAY", "Loan Closure"), 
		LOAN_PAID_IN_FULL("LPAY", "Loan Paid in Full")
		;

		private final String code;
		private final String description;

		TransactionType(String code, String description) {
			this.code = code;
			this.description = description;
		}

		public String getCode() {
			return this.code;
		}

		public String getDescription() {
			return this.description;
		}
	}

	private Long contractId;
	private String contractName;

	private Long profileId;
	private String firstName;
	private String middleInitial;
	private String lastName;
	private String employeeId;
	private String employerDivision;
	private String ssn;
	private String participantStatusCode;

	private Date createdDate;
	private String createdUserIdTypeCode;
	private String createdSourceCode;

	private Date effectiveDate;
	private BigDecimal value;
	private String valueTypeCode;

	private String statusCode;

	private Date processedDate;
	private String processedUserIdTypeCode;
	private String processedUserProfileId;
	private String processedSourceCode;

	private String moneyTypeCode;

	public String getParticipantName() {
		StringBuilder nameBuilder = new StringBuilder();
		if(StringUtils.isNotBlank(this.lastName)) {
			nameBuilder.append(this.lastName.trim());
		}
		if(StringUtils.isNotBlank(this.firstName)) {
			nameBuilder.append(", ").append(this.firstName.trim());
		}
		if(StringUtils.isNotBlank(this.middleInitial)) {
			nameBuilder.append(" ").append(this.middleInitial.trim());
		}

		return nameBuilder.toString();
	}

	public abstract String getDescription();

	public Date getInitiatedDate() {
		return this.createdDate;
	}

	public String getInitiatedBy() {
		if(PayrollSelfServiceChangeRecord.USER_ID_TYPE_PARTICIPANT_CODE.equalsIgnoreCase(this.getCreatedUserIdTypeCode())) {
			return this.getParticipantName();
		}

		return JOHN_HANCOCK;
	}

	public String getInitiationMethod() {
		return StringUtils.EMPTY;
	}

	public String getFormattedValue() {
		if(this.value == null) {
			return null;
		}

		DecimalFormat df = new DecimalFormat();
		df.setGroupingUsed(false);

		if(PayrollSelfServiceChangeRecord.VALUE_TYPE_PERCENTAGE_CODE.equals(this.valueTypeCode)) {
			df.setMaximumFractionDigits(PayrollSelfServiceChangeRecord.MAXIMUM_DECIMAL_DIGIT_FOR_PERCENT_DISPLAY);
		} else {
			df.setMaximumFractionDigits(PayrollSelfServiceChangeRecord.MAXIMUM_DECIMAL_DIGIT_FOR_AMOUNT_DISPLAY);
			df.setMinimumFractionDigits(PayrollSelfServiceChangeRecord.MAXIMUM_DECIMAL_DIGIT_FOR_AMOUNT_DISPLAY);
		}

		return df.format(this.value);
	}

	public String getDetails() {
		StringBuilder detailBuilder = new StringBuilder();
		if(PayrollSelfServiceChangeRecord.VALUE_TYPE_FLAT_DOLLAR_AMOUNT_CODE.equals(this.valueTypeCode)) {
			detailBuilder.append("$");
		}
		detailBuilder.append(String.valueOf(this.getFormattedValue()));
		if(PayrollSelfServiceChangeRecord.VALUE_TYPE_PERCENTAGE_CODE.equals(this.valueTypeCode)) {
			detailBuilder.append("%");
		}		

		return detailBuilder.toString();
	}

	public String getProcessedBy() {
		if(StringUtils.isBlank(this.processedUserIdTypeCode)) {
			return StringUtils.EMPTY;
		}

		if(PayrollSelfServiceChangeRecord.USER_ID_TYPE_PARTICIPANT_CODE.equalsIgnoreCase(this.processedUserIdTypeCode)) {
			return this.getParticipantName();
		}

		if(PayrollSelfServiceChangeRecord.USER_ID_TYPE_INTERNAL_CODE.equalsIgnoreCase(this.processedUserIdTypeCode)) {
			if(PayrollSelfServiceChangeRecord.AUTOMATED_PROCESSED_SOURCE_CODE.equalsIgnoreCase(this.processedSourceCode)) {
				return ACTION_TAKEN_BY_AUTOMATED;
			}
		}

		return JOHN_HANCOCK;
	}

	public String getMoneyTypeDescription() {
		return PayrollSelfServiceChangeRecord.MONEY_TYPE_DESCRIPTION_MAP.get(this.moneyTypeCode);
	}

	public String getMaskedSsn() {
		if(ssn == null) {
			return null;
		}
		return ssn.replaceAll(REGEX_SSN_CAPTURE_LAST_FOUR_DIGITS, "xxx-xx-$1");
	}

	public String getParticipantStatusDescription() {
		return ObjectUtils.firstNonNull(
				PARTICIPANT_STATUS_DESCRIPTION_MAP.get(this.participantStatusCode),
				this.participantStatusCode
				);
	}

	public String getStatusDescription() {
		return StringUtils.EMPTY;
	}

	/**
	 * Item reported in CSV view under column "Deferral Value/Loan Start Date/Name-Address Value"
	 * <p />
	 * @return
	 */
	public Object getFileReportedValue() {
		return this.getValue();
	}
	
	public String getFormattedLoanRepaymentAmount() {
		return StringUtils.EMPTY;
	}

	public String getFormattedLoanPrincipalAmount() {
		return StringUtils.EMPTY;
	}

	public String getFormattedLoanTotalInterestAmount() {
		return StringUtils.EMPTY;
	}

	public String getFormattedLoanGoalAmount() {
		return StringUtils.EMPTY;
	}

	public String getFormattedNumberOfLoanPayments() {
		return StringUtils.EMPTY;
	}

	public String getFormattedLoanNumber() {
		return StringUtils.EMPTY;
	}

	public TransactionType getTransactionType() {
		if(PayrollSelfServiceChangeRecord.VALUE_TYPE_PERCENTAGE_CODE.equals(this.valueTypeCode)) {
			if (Deferral.MoneyType.ROTH.getCode().equalsIgnoreCase(StringUtils.trimToNull(this.getMoneyTypeCode()))) {
				return TransactionType.ROTH_DEFERRAL_PERCENT;
			}

			return TransactionType.PRE_TAX_DEFERRAL_PERCENT;	
		}

		if(PayrollSelfServiceChangeRecord.VALUE_TYPE_FLAT_DOLLAR_AMOUNT_CODE.equals(this.valueTypeCode)) {
			if (Deferral.MoneyType.ROTH.getCode().equalsIgnoreCase(StringUtils.trimToNull(this.getMoneyTypeCode()))) {
				return TransactionType.ROTH_DOLLARS;
			}

			return TransactionType.PRE_TAX_DOLLARS;
		}

		return null;
	}

	public boolean isWarningIconApplicable() {
		return false;
	}

	public boolean isInitiatedTimeReported() {
		return false;
	}

	public boolean isProcessingDetailReported() {
		return false;
	}


}
