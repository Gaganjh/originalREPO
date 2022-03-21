package com.manulife.pension.ps.service.report.transaction.valueobject;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.ObjectUtils;

import com.manulife.pension.util.StaticHelperClass;

/**
 * This value object represents the data transformed from information retreievd from Apollo.
 * This value object is used for the presentation layer. 
 */
public class TransactionHistoryDownloadItem implements java.io.Serializable, Comparable {

	private boolean complete = true;

	private Date transactionDate;
	private Date payrollEndingDate;
	private String typeDescription1;
	private String transactionNumber;
	private BigDecimal amount;
	private BigDecimal chequeAmount;
	
	private String fundName;
	private String riskCategoryName;
	private String moneyTypeDescription;
	private BigDecimal unitValue;
	private BigDecimal numberOfUnits;
	private BigDecimal interestRate;
	private String comments;	

	
	/**
	 * Constructor.
	 *  
	 */
	public TransactionHistoryDownloadItem() {
		super();
	}

	/**
	 * @return Returns the creditAmount.
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	public String getDisplayAmount() {
		if (amount == null || amount.toBigInteger().intValue() == 0) {
			return "-";
		} else {
			return amount.toString().trim();
		}
	}
	public String getDisplayChequeAmount() {
		if (chequeAmount == null || chequeAmount.toBigInteger().intValue() == 0) {
			return "-";
		} else {
			return chequeAmount.toString().trim();
		}
	}
	
	/**
	 * @param creditAmount
	 *            The creditAmount to set.
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return Returns the transactionDate.
	 */
	public Date getTransactionDate() {
		return transactionDate;
	}

	/**
	 * @param transactionDate
	 *            The transactionDate to set.
	 */
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * @return Returns the transactionNumber.
	 */
	public String getTransactionNumber() {
		return transactionNumber;
	}

	/**
	 * @param transactionNumber
	 *            The transactionNumber to set.
	 */
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}


	/**
	 * @return Returns the transactionType.
	 */
	public String getTypeDescription1() {
		return typeDescription1;
	}

	/**
	 * @param transactionType
	 *            The transactionType to set.
	 */
	public void setTypeDescription1(String transactionType1) {
		this.typeDescription1 = transactionType1;
	}



	/**
	 * Gets the isComplete
	 * 
	 * @return Returns a boolean
	 */
	public boolean isComplete() {
		return complete;
	}

	/**
	 * Sets the isComplete
	 * 
	 * @param isComplete
	 *            The isComplete to set
	 */
	public void setComplete(boolean complete) {
		this.complete = complete;
	}


	public int compareTo(Object o) {
		if (o instanceof TransactionHistoryDownloadItem) {
			TransactionHistoryDownloadItem item = (TransactionHistoryDownloadItem) o;
			return this.transactionDate.compareTo(item.transactionDate);
		}
		throw new UnsupportedOperationException(
				"Cannot compare CashAccountItem to " + o.getClass());
	}

 	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (this == o) {
			return true;
		}
		if (o instanceof TransactionHistoryDownloadItem) {
			TransactionHistoryDownloadItem item = (TransactionHistoryDownloadItem) o;
			return ObjectUtils
					.equals(transactionNumber, item.transactionNumber)
					&& ObjectUtils
							.equals(transactionDate, item.transactionDate)
					&& ObjectUtils.equals(typeDescription1,
							item.typeDescription1)
//					&& ObjectUtils.equals(typeDescription2,
//							item.typeDescription2)
					&& ObjectUtils.equals(amount, item.amount)
					&& (complete == item.complete);
		}
		return false;
	}
 
	public String toString() {
		
		return StaticHelperClass.toString(this);	
	}


	/**
	 * Gets the payrollEndingDate
	 * @return Returns a Date
	 */
	public Date getPayrollEndingDate() {
		return payrollEndingDate;
	}
	/**
	 * Sets the payrollEndingDate
	 * @param payrollEndingDate The payrollEndingDate to set
	 */
	public void setPayrollEndingDate(Date payrollEndingDate) {
		this.payrollEndingDate = payrollEndingDate;
	}

	public boolean displayPayrollDate() {
		if (payrollEndingDate != null) {
			Calendar payrollEndingCalendar = new GregorianCalendar();
			payrollEndingCalendar.setTime(payrollEndingDate);
			return (payrollEndingCalendar.get(Calendar.YEAR) > 1 );
		} else {
			return false;
		}
	}
	/**
	 * Gets the chequeAmount
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getChequeAmount() {
		return chequeAmount;
	}
	/**
	 * Sets the chequeAmount
	 * @param chequeAmount The chequeAmount to set
	 */
	public void setChequeAmount(BigDecimal chequeAmount) {
		this.chequeAmount = chequeAmount;
	}


	/**
	 * Gets the comments
	 * @return Returns a String
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * Sets the comments
	 * @param comments The comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * Gets the fundName
	 * @return Returns a String
	 */
	public String getFundName() {
		return fundName;
	}
	/**
	 * Sets the fundName
	 * @param fundName The fundName to set
	 */
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	/**
	 * Gets the interestRate
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getInterestRate() {
		return interestRate;
	}
	/**
	 * Sets the interestRate
	 * @param interestRate The interestRate to set
	 */
	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}

	/**
	 * Gets the moneyTypeDescription
	 * @return Returns a String
	 */
	public String getMoneyTypeDescription() {
		return moneyTypeDescription;
	}
	/**
	 * Sets the moneyTypeDescription
	 * @param moneyTypeDescription The moneyTypeDescription to set
	 */
	public void setMoneyTypeDescription(String moneyTypeDescription) {
		this.moneyTypeDescription = moneyTypeDescription;
	}

	/**
	 * Gets the numberOfUnits
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getNumberOfUnits() {
		return numberOfUnits;
	}
	/**
	 * Sets the numberOfUnits
	 * @param numberOfUnits The numberOfUnits to set
	 */
	public void setNumberOfUnits(BigDecimal numberOfUnits) {
		this.numberOfUnits = numberOfUnits;
	}

	/**
	 * Gets the unitValue
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getUnitValue() {
		return unitValue;
	}
	/**
	 * Sets the unitValue
	 * @param unitValue The unitValue to set
	 */
	public void setUnitValue(BigDecimal unitValue) {
		this.unitValue = unitValue;
	}

	/**
	 * Gets the riskCategoryName
	 * @return Returns a String
	 */
	public String getRiskCategoryName() {
		return riskCategoryName;
	}
	/**
	 * Sets the riskCategoryName
	 * @param riskCategoryName The riskCategoryName to set
	 */
	public void setRiskCategoryName(String riskCategoryName) {
		this.riskCategoryName = riskCategoryName;
	}

}