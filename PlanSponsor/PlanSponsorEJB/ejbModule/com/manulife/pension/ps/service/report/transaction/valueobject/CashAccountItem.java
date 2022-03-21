package com.manulife.pension.ps.service.report.transaction.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Charles Chan
 */
public abstract class CashAccountItem extends TransactionHistoryItem
		implements Serializable, Comparable {

	/**
	 * @return Returns the creditAmount.
	 */
	public abstract BigDecimal getCreditAmount();

	/**
	 * @return Returns the debitAmount.
	 */
	public abstract BigDecimal getDebitAmount();

	/**
	 * @return Returns the runningBalance.
	 */
	public BigDecimal getRunningBalance() {
		return runningBalance;
	}

	/**
	 * @param runningBalance
	 *            The runningBalance to set.
	 */
	public void setRunningBalance(BigDecimal runningBalance) {
		this.runningBalance = runningBalance;
	}

	/**
	 * @return the originalAmount
	 */
	public BigDecimal getOriginalAmount() {
		return originalAmount;
	}

	/**
	 * @param originalAmount the originalAmount to set
	 */
	public void setOriginalAmount(BigDecimal originalAmount) {
		this.originalAmount = originalAmount;
	}

	/**
	 * @return the availableAmount
	 */
	public BigDecimal getAvailableAmount() {
		return availableAmount;
	}

	/**
	 * @param availableAmount the availableAmount to set
	 */
	public void setAvailableAmount(BigDecimal availableAmount) {
		this.availableAmount = availableAmount;
	}


	/**
	 * @return the moneyType
	 */
	public String getMoneyType() {
		return moneyType;
	}

	/**
	 * @param moneyType the moneyType to set
	 */
	public void setMoneyType(String moneyType) {
		this.moneyType = moneyType;
	}


	private BigDecimal runningBalance;
	private  BigDecimal originalAmount;
	private  BigDecimal availableAmount;
	private String moneyType;

}