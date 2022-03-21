package com.manulife.pension.ps.service.report.transaction.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.manulife.pension.service.account.valueobject.LoanGeneralInfoVO;
import com.manulife.pension.service.report.valueobject.ReportData;


public class LoanRepaymentDetailsReportData extends ReportData implements Serializable {
	
	public static final String REPORT_ID = "LoanRepaymentDetailsReportHandler"; // no handler for this - this is never used? [AP - May 15, 2006]


	private String name;
	private String maskedSsn;
	private String ssn;	
	private long returnCode;
	private Date inquiryDate;
	
	private int number = -1;
	private String status;
	private Date transferDate;
	private BigDecimal transferAmount;
	private BigDecimal transferRate;
	private Date maturityDate;
	private Date lastRepaymentDate;
	private BigDecimal lastRepaymentAmount;
	private BigDecimal remainingPrincipal;
	private BigDecimal outstandingBalanceAmount;
	private String loanOriginIndicator;
	private boolean expenseMargin;
	private long activityReturnCode;
	private BigDecimal principalPaidWithinLast12Months;
	private int daysSinceLastPayment;
	private int amoritizationPeriod;
	
	
	
	private LoanRepaymentDetailsItem[] items;

	private Map<String,BigDecimal> moneyTypeFunds;
	

	/**
	 * Gets the name
	 * @return Returns a String
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets the name
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * Gets the maskedSsn
	 * @return Returns a String
	 */
	public String getMaskedSsn() {
		return maskedSsn;
	}
	/**
	 * Sets the maskedSsn
	 * @param maskedSsn The maskedSsn to set
	 */
	public void setMaskedSsn(String maskedSsn) {
		this.maskedSsn = maskedSsn;
	}


	/**
	 * Gets the returnCode
	 * @return Returns a long
	 */
	public long getReturnCode() {
		return returnCode;
	}
	/**
	 * Sets the returnCode
	 * @param returnCode The returnCode to set
	 */
	public void setReturnCode(long returnCode) {
		this.returnCode = returnCode;
	}


	/**
	 * Gets the inquiryDate
	 * @return Returns a Date
	 */
	public Date getInquiryDate() {
		return inquiryDate;
	}
	/**
	 * Sets the inquiryDate
	 * @param inquiryDate The inquiryDate to set
	 */
	public void setInquiryDate(Date inquiryDate) {
		this.inquiryDate = inquiryDate;
	}


	/**
	 * Gets the number
	 * @return Returns a int
	 */
	public int getNumber() {
		return number;
	}
	/**
	 * Sets the number
	 * @param number The number to set
	 */
	public void setNumber(int number) {
		this.number = number;
	}


	/**
	 * Gets the status
	 * @return Returns a String
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * Sets the status
	 * @param status The status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}


	/**
	 * Gets the transferDate
	 * @return Returns a Date
	 */
	public Date getTransferDate() {
		return transferDate;
	}
	/**
	 * Sets the transferDate
	 * @param transferDate The transferDate to set
	 */
	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}


	/**
	 * Gets the transferAmount
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getTransferAmount() {
		return transferAmount;
	}
	/**
	 * Sets the transferAmount
	 * @param transferAmount The transferAmount to set
	 */
	public void setTransferAmount(BigDecimal transferAmount) {
		this.transferAmount = transferAmount;
	}


	/**
	 * Gets the transferRate
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getTransferRate() {
		return transferRate;
	}
	/**
	 * Sets the transferRate
	 * @param transferRate The transferRate to set
	 */
	public void setTransferRate(BigDecimal transferRate) {
		this.transferRate = transferRate;
	}


	/**
	 * Gets the maturityDate
	 * @return Returns a Date
	 */
	public Date getMaturityDate() {
		return maturityDate;
	}
	/**
	 * Sets the maturityDate
	 * @param maturityDate The maturityDate to set
	 */
	public void setMaturityDate(Date maturityDate) {
		this.maturityDate = maturityDate;
	}


	/**
	 * Gets the lastRepaymentDate
	 * @return Returns a Date
	 */
	public Date getLastRepaymentDate() {
		return lastRepaymentDate;
	}
	/**
	 * Sets the lastRepaymentDate
	 * @param lastRepaymentDate The lastRepaymentDate to set
	 */
	public void setLastRepaymentDate(Date lastRepaymentDate) {
		this.lastRepaymentDate = lastRepaymentDate;
	}


	/**
	 * Gets the lastRepaymentAmount
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getLastRepaymentAmount() {
		return lastRepaymentAmount;
	}
	/**
	 * Sets the lastRepaymentAmount
	 * @param lastRepaymentAmount The lastRepaymentAmount to set
	 */
	public void setLastRepaymentAmount(BigDecimal lastRepaymentAmount) {
		this.lastRepaymentAmount = lastRepaymentAmount;
	}


	/**
	 * Gets the remainingPrincipal
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getRemainingPrincipal() {
		return remainingPrincipal;
	}
	/**
	 * Sets the remainingPrincipal
	 * @param remainingPrincipal The remainingPrincipal to set
	 */
	public void setRemainingPrincipal(BigDecimal remainingPrincipal) {
		this.remainingPrincipal = remainingPrincipal;
	}


	/**
	 * Gets the outstandingBalanceAmount
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getOutstandingBalanceAmount() {
		return outstandingBalanceAmount;
	}
	/**
	 * Sets the outstandingBalanceAmount
	 * @param outstandingBalanceAmount The outstandingBalanceAmount to set
	 */
	public void setOutstandingBalanceAmount(BigDecimal outstandingBalanceAmount) {
		this.outstandingBalanceAmount = outstandingBalanceAmount;
	}


	/**
	 * Gets the loanOriginIndicator
	 * @return Returns a String
	 */
	public String getLoanOriginIndicator() {
		return loanOriginIndicator;
	}
	/**
	 * Sets the loanOriginIndicator
	 * @param loanOriginIndicator The loanOriginIndicator to set
	 */
	public void setLoanOriginIndicator(String loanOriginIndicator) {
		this.loanOriginIndicator = loanOriginIndicator;
	}


	/**
	 * Gets the expenseMargin
	 * @return Returns a boolean
	 */
	public boolean getExpenseMargin() {
		return expenseMargin;
	}
	/**
	 * Sets the expenseMargin
	 * @param expenseMargin The expenseMargin to set
	 */
	public void setExpenseMargin(boolean expenseMargin) {
		this.expenseMargin = expenseMargin;
	}


	/**
	 * Gets the activityReturnCode
	 * @return Returns a long
	 */
	public long getActivityReturnCode() {
		return activityReturnCode;
	}
	/**
	 * Sets the activityReturnCode
	 * @param activityReturnCode The activityReturnCode to set
	 */
	public void setActivityReturnCode(long activityReturnCode) {
		this.activityReturnCode = activityReturnCode;
	}


	/**
	 * Gets the principalPaidWithinLast12Months
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getPrincipalPaidWithinLast12Months() {
		return principalPaidWithinLast12Months;
	}
	/**
	 * Sets the principalPaidWithinLast12Months
	 * @param principalPaidWithinLast12Months The principalPaidWithinLast12Months to set
	 */
	public void setPrincipalPaidWithinLast12Months(BigDecimal principalPaidWithinLast12Months) {
		this.principalPaidWithinLast12Months = principalPaidWithinLast12Months;
	}



	/**
	 * Gets the items
	 * @return Returns a LoanRepaymentDetailsItem[]
	 */
	public LoanRepaymentDetailsItem[] getItems() {
		return items;
	}
	/**
	 * Sets the items
	 * @param items The items to set
	 */
	public void setItems(LoanRepaymentDetailsItem[] items) {
		this.items = items;
	}


	/**
	 * @return
	 */
	public int getDaysSinceLastPayment() {
		return daysSinceLastPayment;
	}

	/**
	 * @param i
	 */
	public void setDaysSinceLastPayment(int i) {
		daysSinceLastPayment = i;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getSsn() {
		return ssn;
	}

	
	public int getAmoritizationPeriod() {
		return amoritizationPeriod;
	}
	public void setAmoritizationPeriod(int amoritizationPeriod) {
		this.amoritizationPeriod = amoritizationPeriod;
	}
	public Map<String, BigDecimal> getMoneyTypeFunds() {
		return moneyTypeFunds;
	}
	public void setMoneyTypeFunds(Map<String, BigDecimal> moneyTypeFunds) {
		this.moneyTypeFunds = moneyTypeFunds;
	}
	private LoanGeneralInfoVO loanGeneralInfo;
	
	public LoanGeneralInfoVO getLoanGeneralInfo() {
		return loanGeneralInfo;
	}
	public void setLoanGeneralInfo(LoanGeneralInfoVO loanGeneralInfo) {
		this.loanGeneralInfo = loanGeneralInfo;
	}
}

