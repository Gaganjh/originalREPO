package com.manulife.pension.ps.service.participant.valueobject;

import java.io.Serializable;

/**
 * ParticipantFundMoneyTypeTotalsVO class
 * This class is used as a value object used on the Participant Account page Money Type Summary tab,
 * to retrieve the information that will be displayed in the page 
 * 
 * @author Ilker Celikyilmaz
 *
 **/
public class ParticipantFundMoneyTypeTotalsVO implements Serializable, Comparable {
	
	String moneyTypeName;
	double balance;
	double loanBalance;	
	
    /**
     * Contructors
     */
	public ParticipantFundMoneyTypeTotalsVO() {
	} 
    
	public ParticipantFundMoneyTypeTotalsVO(String moneyTypeName,
									double balance,
									double loanBalance) {
		this.moneyTypeName = moneyTypeName;
		this.balance = balance;
		this.loanBalance = loanBalance;		
	} 
	
	public int compareTo(Object o) {
		ParticipantFundMoneyTypeTotalsVO other = (ParticipantFundMoneyTypeTotalsVO)o;
		return getMoneyTypeName().compareToIgnoreCase(other.getMoneyTypeName());
	}	
	
	public boolean equals(Object o) {
		return ((ParticipantFundMoneyTypeTotalsVO)o).getMoneyTypeName().equals(moneyTypeName);
	}	

	/**
	 * Gets the moneyTypeName
	 * @return Returns a String
	 */
	public String getMoneyTypeName() {
		return moneyTypeName;
	}

	/**
	 * Sets the moneyTypeName
	 * @param moneyTypeName The moneyTypeName to set
	 */
	public void setMoneyTypeName(String moneyTypeName) {
		this.moneyTypeName = moneyTypeName;
	}

	/**
	 * Gets the balance
	 * @return Returns a double
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * Add the balance
	 * @param balance The balance to add
	 */
	public void addBalance(double balance) {
		this.balance += balance;
	}

	public String toString()
	{
		return com.manulife.pension.util.StaticHelperClass.toString(this);
	}
    
	/**
	 * @return Returns the loanBalance.
	 */
	public double getLoanBalance() {
		return loanBalance;
	}
	/**
	 * @param loanBalance The loanBalance to add.
	 */
	public void addLoanBalance(double loanBalance) {
		this.loanBalance += loanBalance;
	}
}
