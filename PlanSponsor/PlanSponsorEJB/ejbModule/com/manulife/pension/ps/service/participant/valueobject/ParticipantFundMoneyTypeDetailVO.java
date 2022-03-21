package com.manulife.pension.ps.service.participant.valueobject;

import java.io.Serializable;

/**
 * ParticipantFundMoneyTypeDetailVO class
 * This class is used as a value object used on the Participant Account page Money Type Details tab,
 * to retrieve the information that will be displayed in the page 
 * 
 * @author Ilker Celikyilmaz
 *
 **/
public class ParticipantFundMoneyTypeDetailVO implements Serializable, Comparable {
	
	String moneyTypeName;
	String moneyType;
	double fundUnitValue;
	double numberOfUnitsHeld;
	double compositeRate;
	double balance;
	
    /**
     * Contructors
     */
	public ParticipantFundMoneyTypeDetailVO() {
	} 
    
	public ParticipantFundMoneyTypeDetailVO(String moneyTypeName,
									String moneyType,
									double numberOfUnitsHeld,
									double compositeRate,
									double balance) {
		this.moneyTypeName = moneyTypeName;
		this.moneyType = moneyType;
		this.numberOfUnitsHeld = numberOfUnitsHeld;
		this.compositeRate = compositeRate;
		this.balance = balance;
	} 
	
	public int compareTo(Object o) {
		ParticipantFundMoneyTypeDetailVO other = (ParticipantFundMoneyTypeDetailVO)o;
		String compareThisStr = getMoneyType() + getMoneyTypeName();
		String compareOtherStr = other.getMoneyType() + other.getMoneyTypeName();
		return compareThisStr.compareToIgnoreCase(compareOtherStr);
	}	
	
	public boolean equals(Object o) {
		return ((ParticipantFundMoneyTypeDetailVO)o).getMoneyTypeName().equals(moneyTypeName);
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
	 * Gets the fundUnitValue
	 * @return Returns a double
	 */
	public double getFundUnitValue() {
		return fundUnitValue;
	}

	/**
	 * Sets the fundUnitValue
	 * @param fundUnitValue The fundUnitValue to set
	 */
	public void setFundUnitValue(double fundUnitValue) {
		this.fundUnitValue = fundUnitValue;
	}

	/**
	 * Gets the numberOfUnitsHeld
	 * @return Returns a double
	 */
	public double getNumberOfUnitsHeld() {
		return numberOfUnitsHeld;
	}

	/**
	 * Sets the numberOfUnitsHeld
	 * @param numberOfUnitsHeld The numberOfUnitsHeld to set
	 */
	public void setNumberOfUnitsHeld(double numberOfUnitsHeld) {
		this.numberOfUnitsHeld = numberOfUnitsHeld;
	}

	/**
	 * Gets the compositeRate
	 * @return Returns a double
	 */
	public double getCompositeRate() {
		return compositeRate;
	}

	/**
	 * Sets the compositeRate
	 * @param compositeRate The compositeRate to set
	 */
	public void setCompositeRate(double compositeRate) {
		this.compositeRate = compositeRate;
	}

	/**
	 * Gets the balance
	 * @return Returns a double
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * Sets the balance
	 * @param balance The balance to set
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}

	/**
	 * Gets the moneyType
	 * @return Returns a String
	 */
	public String getMoneyType() {
		return moneyType;
	}

	/**
	 * Sets the moneyType
	 * @param moneyType The moneyType to set
	 */
	public void setMoneyType(String moneyType) {
		this.moneyType = moneyType;
	}
	
	public String toString()
	{
		return com.manulife.pension.util.StaticHelperClass.toString(this);
	}
    
}
