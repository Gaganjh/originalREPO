package com.manulife.pension.ps.service.report.transaction.valueobject;

import java.math.BigDecimal;
import java.util.Date;

import com.manulife.pension.ps.service.report.transaction.reporthandler.LoanRepaymentTransactionReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * @author Ludmila Stern
 */
public class LoanRepaymentTransactionReportData extends ReportData{

	public static final String REPORT_ID = LoanRepaymentTransactionReportHandler.class.getName();
	
	public static final String REPORT_NAME = "loanRepaymentTransactionReport";
	
	/**
	 * The criteria filter parameter for transaction number
	 */
	public static final String FILTER_TRANSACTION_NUMBER = "transactionNumber";

	/**
	 * The criteria filter parameter for transaction number
	 */
	public static final String FILTER_CONTRACT_NUMBER= "contractNumber";

	/**
	 * Sorts fields
	 */
	public static final String SORT_FIELD_LAST_NAME = "lastName";
	public static final String SORT_FIELD_FIRST_NAME = "firstName";
	public static final String SORT_FIELD_SSN = "ssn";
	public static final String SORT_FIELD_LOAN_NUMBER = "loanNumber";
	public static final String SORT_FIELD_REPAYMENT = "repaymentAmount";	
	public static final String SORT_FIELD_PRINCIPAL = "principalAmount";
	public static final String SORT_FIELD_INTEREST = "interestAmount";

	private BigDecimal totalRepaymentAmount;
	private BigDecimal totalInterestAmount;
	private BigDecimal totalPrincipalAmount;
	private Date transactionDate;
	private int numberOfParticipants;
	private String transactionNumber;
	private String contractNumber;

	public void setTotalInterestAmount(BigDecimal totalInterestAmount) {
		this.totalInterestAmount = totalInterestAmount;
	}

	public BigDecimal getTotalInterestAmount() {
		return totalInterestAmount;
	}

	public void setTotalPrincipalAmount(BigDecimal totalPrincipalAmount) {
		this.totalPrincipalAmount = totalPrincipalAmount;
	}

	public BigDecimal getTotalPrincipalAmount() {
		return totalPrincipalAmount;
	}

	public void setTotalRepaymentAmount(BigDecimal totalRepaymentAmount) {
		this.totalRepaymentAmount = totalRepaymentAmount;
	}

	public BigDecimal getTotalRepaymentAmount() {
		return totalRepaymentAmount;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate){
		this.transactionDate = transactionDate;
	}

	public int getNumberOfParticipants() {
		return numberOfParticipants;
	}

	public void setNumberOfParticipants(int numberOfParticipants) {
		this.numberOfParticipants = numberOfParticipants;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber (String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
	
	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}	
	public LoanRepaymentTransactionReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
	}
	
	public String toString() {
		StringBuffer strbf = new StringBuffer();
		strbf.append(" totalRepaymentAmount: ").append( totalRepaymentAmount.toString())
			 .append(" totalInterestAmount: ").append(totalInterestAmount.toString())
		     .append(" totalPrincipalAmount: ").append(totalPrincipalAmount.toString())
		     .append(" transactionDate: ").append(transactionDate.toString())
		     .append(" numberOfParticipants: ").append(numberOfParticipants)
		     .append(" transactionNumber: ").append(transactionNumber);
			          
		return strbf.toString();
	}  
}