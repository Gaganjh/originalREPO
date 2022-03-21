package com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class PayrollSelfServiceLoanPayOffRecord extends PayrollSelfServiceLoanRecord {

	private static final long serialVersionUID = 1L;
	
	private Date payoffDate;
	
	public static final String LOAN_CLOSURE_TYPE_PAY_OFF_CODE = "LNPO";
	
	public PayrollSelfServiceLoanPayOffRecord() {
		super.setValueTypeCode(VALUE_TYPE_FLAT_DOLLAR_AMOUNT_CODE);
	}
	
	@Override
	public String getDescription() {
		return "Loan Paid in Full";
	}
	
	@Override
	public TransactionType getTransactionType() {
		return PayrollSelfServiceChangeRecord.TransactionType.LOAN_PAID_IN_FULL;
	}
	
	@Override
	public Object getFileReportedValue() {
		return getLoanIssueEffectiveDate();
	}

}
