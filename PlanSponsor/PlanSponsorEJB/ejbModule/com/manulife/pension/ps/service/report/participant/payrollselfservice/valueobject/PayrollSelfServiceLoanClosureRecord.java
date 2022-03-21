package com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ObjectUtils;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class PayrollSelfServiceLoanClosureRecord extends PayrollSelfServiceLoanRecord {

	private static final long serialVersionUID = 1L;
	
	private static final Map<String, String> CLOSURE_TYPE_DESCRIPTION_MAP = Stream
			.of(new String[][] { 
				{"LDNT", "Loan Closure"},
				{"LDTX", "Loan Default"}, 
				{"LNOF", "Loan Offset"}, 
				{"LNRV", "Loan Correction"}
			}).collect(Collectors.collectingAndThen(
					Collectors.toMap(data -> data[0], data -> data[1]), Collections::<String, String>unmodifiableMap));
	
	private Date lastRepaymentDate;
	
	private String loanClosureTypeCode;
	
	public PayrollSelfServiceLoanClosureRecord() {
		super.setValueTypeCode(VALUE_TYPE_FLAT_DOLLAR_AMOUNT_CODE);
	}

	@Override
	public String getDescription() {
		return "Loan Closure";
	}
	
	@Override
	public String getInitiationMethod() {
		return ObjectUtils.firstNonNull(
				PayrollSelfServiceLoanClosureRecord.CLOSURE_TYPE_DESCRIPTION_MAP.get(this.getLoanClosureTypeCode()),
				this.getLoanClosureTypeCode()
				);
	}
	
	@Override
	public TransactionType getTransactionType() {
		return PayrollSelfServiceChangeRecord.TransactionType.LOAN_CLOSURE;
	}
	
	@Override
	public Object getFileReportedValue() {
		return getLoanIssueEffectiveDate();
	}

}
