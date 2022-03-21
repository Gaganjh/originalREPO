package com.manulife.pension.ps.service.report.transaction.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class LoanRepaymentDetailsItem implements Serializable {
	
	private Date date;
	private String type;
	private String typeDesc;
	private BigDecimal amount;
	private BigDecimal principal;
	private BigDecimal interest;
	private BigDecimal expenseMargin;
	private BigDecimal balance;

	/**
	 * Gets the date
	 * @return Returns a Date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * Sets the date
	 * @param date The date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}


	/**
	 * Gets the type
	 * @return Returns a String
	 */
	public String getType() {
		return type;
	}
	/**
	 * Sets the type
	 * @param type The type to set
	 */
	public void setType(String type) {
		this.type = type;
	}


	/**
	 * Gets the amount
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	/**
	 * Sets the amount
	 * @param amount The amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


	/**
	 * Gets the principal
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getPrincipal() {
		return principal;
	}
	/**
	 * Sets the principal
	 * @param principal The principal to set
	 */
	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}


	/**
	 * Gets the interest
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getInterest() {
		return interest;
	}
	/**
	 * Sets the interest
	 * @param interest The interest to set
	 */
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}


	/**
	 * Gets the expenseMargin
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getExpenseMargin() {
		return expenseMargin;
	}
	/**
	 * Sets the expenseMargin
	 * @param expenseMargin The expenseMargin to set
	 */
	public void setExpenseMargin(BigDecimal expenseMargin) {
		this.expenseMargin = expenseMargin;
	}


	/**
	 * Gets the balance
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getBalance() {
		return balance;
	}
	/**
	 * Sets the balance
	 * @param balance The balance to set
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}



	/**
	 * @return
	 */
	public String getTypeDesc() {
		return typeDesc;
	}

	/**
	 * @param string
	 */
	public void setTypeDesc(String string) {
		typeDesc = string;
	}

}

