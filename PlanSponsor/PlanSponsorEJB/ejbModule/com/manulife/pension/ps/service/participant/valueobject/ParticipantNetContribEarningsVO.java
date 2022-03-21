package com.manulife.pension.ps.service.participant.valueobject;

import java.io.Serializable;

/**
 * ParticipantNetContribEarningsVO class
 * This class is used as a value object used on the Participant Account page After Tax Money tab,
 * to retrieve the information that will be displayed in the page 
 * 
 * @author Jan Barnes
 *
 **/
public class ParticipantNetContribEarningsVO implements Serializable {
	
	private String moneyTypeName;
	private double netContributions;
	private double earnings;
	private boolean rothMoneyTypeInd;
	private boolean nonRothMoneyTypeInd;
    /**
     * Contructors
     */
	public ParticipantNetContribEarningsVO() {
	} 
    
	public ParticipantNetContribEarningsVO(String moneyTypeName,
									double netContributions,
									double earnings) {
		this.moneyTypeName = moneyTypeName;
		this.netContributions = netContributions;
		this.earnings = earnings;
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
	 * Gets the netContributions
	 * @return Returns a double
	 */
	public double getNetContributions() {
		return netContributions;
	}

	/**
	 * Sets the netContributions
	 * @param netContributions The netContributions to set
	 */
	public void setNetContributions(double netContributions) {
		this.netContributions = netContributions;
	}

	/**
	 * Gets the earnings
	 * @return Returns a double
	 */
	public double getEarnings() {
		return earnings;
	}

	/**
	 * Sets the earnings
	 * @param earnings The earnings to set
	 */
	public void setEarnings(double earnings) {
		this.earnings = earnings;
	}

	public String toString()
	{
		return com.manulife.pension.util.StaticHelperClass.toString(this);
	}

	/**
	 * @return the rothMoneyTypeInd
	 */
	public boolean isRothMoneyTypeInd() {
		return rothMoneyTypeInd;
	}

	/**
	 * @param rothMoneyTypeInd the rothMoneyTypeInd to set
	 */
	public void setRothMoneyTypeInd(boolean rothMoneyTypeInd) {
		this.rothMoneyTypeInd = rothMoneyTypeInd;
	}

	/**
	 * @return the nonRothMoneyTypeInd
	 */
	public boolean isNonRothMoneyTypeInd() {
		return nonRothMoneyTypeInd;
	}

	/**
	 * @param nonRothMoneyTypeInd the nonRothMoneyTypeInd to set
	 */
	public void setNonRothMoneyTypeInd(boolean nonRothMoneyTypeInd) {
		this.nonRothMoneyTypeInd = nonRothMoneyTypeInd;
	}
	
	
}
