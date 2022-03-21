package com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public abstract class PayrollSelfServiceLoanRecord extends PayrollSelfServiceChangeRecord {

	private static final long serialVersionUID = 1L;
	
	public static final String LOAN_STATUS_ACTIVE_CODE = "LNAC";

	private Integer loanNumber;
	private Date loanIssueEffectiveDate;
	
	@Override
	public String getMoneyTypeDescription() {
		return new StringBuilder("Loan ").append(String.valueOf(this.loanNumber)).toString();
	}
	
	@Override
	public String getInitiatedBy() {
		return StringUtils.EMPTY;
	}
	
	@Override
	public Object getFileReportedValue() {
		return this.getEffectiveDate(); //todo: finalize requirements
	}

	public String getFormattedLoanNumber() {
		return String.valueOf(this.loanNumber);
	}
	
	@Override
	public String getFormattedLoanRepaymentAmount() {
		return this.getFormattedValue();
	}
}
