package com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class PayrollSelfServiceLoanIssueRecord extends PayrollSelfServiceLoanRecord {

	private static final long serialVersionUID = 1L;

	private static final Map<String, String> INITIATION_METHOD_DESCRIPTION_MAP = Stream
			.of(new String[][] { 
				{"PA", "Paper"},
				{"IN", "Internet"},
				{"VR", "Internet"}
			}).collect(Collectors.collectingAndThen(
					Collectors.toMap(data -> data[0], data -> data[1]), Collections::<String, String>unmodifiableMap));

	private static final String CREATED_SOURCE_CODE_VR = "VR";
	private static final String CREATED_SOURCE_CODE_PAPER = "PA";
	
	private Date firstPayrollDate;
	private String initiatedByLastName;
	private String initiatedByFirstName;

	private BigDecimal principalAmount;
	private BigDecimal goalAmount;
	private BigDecimal totalInterestAmount;
	private Integer numberOfPayments;
	
	public PayrollSelfServiceLoanIssueRecord() {
		super.setValueTypeCode(VALUE_TYPE_FLAT_DOLLAR_AMOUNT_CODE);
	}

	@Override
	public String getDescription() {
		return "Loan Issue";
	}

	@Override
	public String getInitiatedBy() {
		if (CREATED_SOURCE_CODE_VR.equals(this.getCreatedSourceCode()) || CREATED_SOURCE_CODE_PAPER.equals(this.getCreatedSourceCode())){
			return PayrollSelfServiceChangeRecord.JOHN_HANCOCK;
		}
		
		String initiatedBy = Stream.of(StringUtils.trimToNull(this.initiatedByLastName), StringUtils.trimToNull(this.initiatedByFirstName))
		.filter(e -> e != null)
		.collect(Collectors.joining(", "));
		
		return initiatedBy;
	}

	@Override
	public String getInitiationMethod() {
		return ObjectUtils.firstNonNull(
				PayrollSelfServiceLoanIssueRecord.INITIATION_METHOD_DESCRIPTION_MAP.get(this.getCreatedSourceCode()),
				this.getCreatedSourceCode()
				);
	}

	@Override
	public String getFormattedLoanPrincipalAmount() {
		if(this.principalAmount == null) {
			return null;
		}
		//todo: move to utility
		DecimalFormat df = new DecimalFormat();
		df.setGroupingUsed(false);
		df.setMaximumFractionDigits(PayrollSelfServiceChangeRecord.MAXIMUM_DECIMAL_DIGIT_FOR_AMOUNT_DISPLAY);
		df.setMinimumFractionDigits(PayrollSelfServiceChangeRecord.MAXIMUM_DECIMAL_DIGIT_FOR_AMOUNT_DISPLAY);
		return df.format(this.principalAmount);
	}
	
	@Override
	public String getFormattedLoanTotalInterestAmount() {
		if(this.totalInterestAmount == null) {
			return null;
		}
		//todo: move to utility
		DecimalFormat df = new DecimalFormat();
		df.setGroupingUsed(false);
		df.setMaximumFractionDigits(PayrollSelfServiceChangeRecord.MAXIMUM_DECIMAL_DIGIT_FOR_AMOUNT_DISPLAY);
		df.setMinimumFractionDigits(PayrollSelfServiceChangeRecord.MAXIMUM_DECIMAL_DIGIT_FOR_AMOUNT_DISPLAY);
		return df.format(this.totalInterestAmount);
	}
	
	@Override
	public String getFormattedLoanGoalAmount() {
		if(this.goalAmount == null) {
			return null;
		}
		//todo: move to utility
		DecimalFormat df = new DecimalFormat();
		df.setGroupingUsed(false);
		df.setMaximumFractionDigits(PayrollSelfServiceChangeRecord.MAXIMUM_DECIMAL_DIGIT_FOR_AMOUNT_DISPLAY);
		df.setMinimumFractionDigits(PayrollSelfServiceChangeRecord.MAXIMUM_DECIMAL_DIGIT_FOR_AMOUNT_DISPLAY);
		return df.format(this.goalAmount);
	}
	
	@Override
	public String getFormattedNumberOfLoanPayments() {
		return numberOfPayments == null ? StringUtils.EMPTY : String.valueOf(numberOfPayments);
	}
	
	@Override
	public TransactionType getTransactionType() {
		return PayrollSelfServiceChangeRecord.TransactionType.LOAN_ISSUE;
	}
}
