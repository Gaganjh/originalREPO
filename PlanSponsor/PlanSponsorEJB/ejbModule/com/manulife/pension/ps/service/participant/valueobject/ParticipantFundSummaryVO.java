package com.manulife.pension.ps.service.participant.valueobject;

import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * ParticipantFundSummaryVO class
 * This class is used as a value object used on the Participant Account page,
 * to retrieve the information that will be displayed in the table 
 * 
 * @author Simona Stoicescu
 **/
public class ParticipantFundSummaryVO implements Serializable, Comparable {
	
	String	fundId;
	String 	fundName;
	String	fundType;
	private String  rateType;
	int 	sortNumber;
	double	fundUnitValue;
	double 	fundTotalNumberOfUnitsHeld;
	double 	fundTotalCompositeRate;
	double	fundTotalBalance;
	double	fundTotalPercentageOfTotal;
	double	fundTotalOngoingContributions;
	double 	employeeNumberOfUnitsHeld;
	double 	employeeCompositeRate;
	double	employeeBalance;
	double 	employerNumberOfUnitsHeld;
	double 	employerCompositeRate;
	double	employerBalance;
	double 	employeeOngoingContributions;
	double 	employerOngoingContributions;
	SortedSet	moneyTypeDetails;
	
	private String fundClass;
	private boolean svgifFlg;
	
    /**
     * Contructors
     */
	public ParticipantFundSummaryVO() {
		this.moneyTypeDetails = new TreeSet();
	}
    
	public ParticipantFundSummaryVO(String fundId, 
									String fundName,
									double fundTotalNumberOfUnitsHeld,
									double fundTotalCompositeRate,
									double fundTotalBalance,
									double fundTotalPercentageOfTotal,
									double fundTotalOngoingContributions,
									double employeeNumberOfUnitsHeld,
									double employeeCompositeRate,
									double employeeBalance,
									double employerNumberOfUnitsHeld,
									double employerCompositeRate,
									double employerBalance) {
		this.fundId = fundId;
		this.fundName = fundName;
		this.fundTotalNumberOfUnitsHeld = fundTotalNumberOfUnitsHeld;
		this.fundTotalCompositeRate = fundTotalCompositeRate;
		this.fundTotalBalance = fundTotalBalance;
		this.fundTotalPercentageOfTotal = fundTotalPercentageOfTotal;
		this.fundTotalOngoingContributions = fundTotalOngoingContributions;
		this.employeeNumberOfUnitsHeld = employeeNumberOfUnitsHeld;
		this.employeeCompositeRate = employeeCompositeRate;
		this.employeeBalance = employeeBalance;
		this.employerNumberOfUnitsHeld = employerNumberOfUnitsHeld;
		this.employerCompositeRate = employerCompositeRate;
		this.employerBalance = employerBalance;
		this.moneyTypeDetails = new TreeSet();
	} 
	
	public int compareTo(Object o) {
		return sortNumber - ((ParticipantFundSummaryVO)o).sortNumber ;
	}	
	
	public boolean equals(Object o) {
		return ((ParticipantFundSummaryVO)o).sortNumber == sortNumber;
	}	

	/**
	 * Gets the fundId
	 * @return Returns a String
	 */
	public String getFundId() {
		return fundId;
	}

	/**
	 * Sets the fundId
	 * @param fundId The fundId to set
	 */
	public void setFundId(String fundId) {
		this.fundId = fundId;
	}

	public int getSortNumber() {
		return sortNumber;
	}

	public void setSortNumber(int sortNumber) {
		this.sortNumber = sortNumber;
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
	 * Gets the fundTotalNumberOfUnitsHeld
	 * @return Returns a double
	 */
	public double getFundTotalNumberOfUnitsHeld() {
		return fundTotalNumberOfUnitsHeld;
	}

	/**
	 * Sets the fundTotalNumberOfUnitsHeld
	 * @param fundTotalNumberOfUnitsHeld The fundTotalNumberOfUnitsHeld to set
	 */
	public void setFundTotalNumberOfUnitsHeld(double fundTotalNumberOfUnitsHeld) {
		this.fundTotalNumberOfUnitsHeld = fundTotalNumberOfUnitsHeld;
	}

	/**
	 * Gets the fundTotalCompositeRate
	 * @return Returns a double
	 */
	public double getFundTotalCompositeRate() {
		return fundTotalCompositeRate;
	}

	/**
	 * Sets the fundTotalCompositeRate
	 * @param fundTotalCompositeRate The fundTotalCompositeRate to set
	 */
	public void setFundTotalCompositeRate(double fundTotalCompositeRate) {
		this.fundTotalCompositeRate = fundTotalCompositeRate;
	}

	/**
	 * Gets the fundTotalBalance
	 * @return Returns a double
	 */
	public double getFundTotalBalance() {
		return fundTotalBalance;
	}

	/**
	 * Sets the fundTotalBalance
	 * @param fundTotalBalance The fundTotalBalance to set
	 */
	public void setFundTotalBalance(double fundTotalBalance) {
		this.fundTotalBalance = fundTotalBalance;
	}

	/**
	 * Gets the fundTotalPercentageOfTotal
	 * @return Returns a double
	 */
	public double getFundTotalPercentageOfTotal() {
		return fundTotalPercentageOfTotal;
	}

	/**
	 * Sets the fundTotalPercentageOfTotal
	 * @param fundTotalPercentageOfTotal The fundTotalPercentageOfTotal to set
	 */
	public void setFundTotalPercentageOfTotal(double fundTotalPercentageOfTotal) {
		this.fundTotalPercentageOfTotal = fundTotalPercentageOfTotal;
	}

	/**
	 * Gets the fundTotalOngoingContributions
	 * @return Returns a double
	 */
	public double getFundTotalOngoingContributions() {
		return fundTotalOngoingContributions;
	}

	/**
	 * Sets the fundTotalOngoingContributions
	 * @param fundTotalOngoingContributions The fundTotalOngoingContributions to set
	 */
	public void setFundTotalOngoingContributions(double fundTotalOngoingContributions) {
		this.fundTotalOngoingContributions = fundTotalOngoingContributions;
	}

	/**
	 * Gets the employeeNumberOfUnitsHeld
	 * @return Returns a double
	 */
	public double getEmployeeNumberOfUnitsHeld() {
		return employeeNumberOfUnitsHeld;
	}

	/**
	 * Sets the employeeNumberOfUnitsHeld
	 * @param employeeNumberOfUnitsHeld The employeeNumberOfUnitsHeld to set
	 */
	public void setEmployeeNumberOfUnitsHeld(double employeeNumberOfUnitsHeld) {
		this.employeeNumberOfUnitsHeld = employeeNumberOfUnitsHeld;
	}
	
	/**
	 * Gets the employerNumberOfUnitsHeld
	 * @return Returns a double
	 */
	public double getEmployerNumberOfUnitsHeld() {
		return employerNumberOfUnitsHeld;
	}

	/**
	 * Sets the employerNumberOfUnitsHeld
	 * @param employerNumberOfUnitsHeld The employerNumberOfUnitsHeld to set
	 */
	public void setEmployerNumberOfUnitsHeld(double employerNumberOfUnitsHeld) {
		this.employerNumberOfUnitsHeld = employerNumberOfUnitsHeld;
	}
	
	/**
	 * Gets the employeeBalance
 	 * @return Returns a double
	 */
	public double getEmployeeBalance() {
		return employeeBalance;
	}
	
	/**
	 * Sets the employeeBalance
	 * @param employeeBalance The employeeBalance to set
	 */
	public void setEmployeeBalance(double employeeBalance) {
		this.employeeBalance = employeeBalance;
	}
	
	/**
	 * Gets the employerBalance
 	 * @return Returns a double
	 */
	public double getEmployerBalance() {
		return employerBalance;
	}
	
	/**
	 * Sets the employerBalance
	 * @param employerBalance The employerBalance to set
	 */
	public void setEmployerBalance(double employerBalance) {
		this.employerBalance = employerBalance;
	}
		
	/**
	 * Gets the employeeCompositeRate
 	 * @return Returns a double
	 */
	public double getEmployeeCompositeRate() {
		return employeeCompositeRate;
	}
	
	/**
	 * Sets the employeeCompositeRate
	 * @param employeeCompositeRate The employeeCompositeRate to set
	 */
	public void setEmployeeCompositeRate(double employeeCompositeRate) {
		this.employeeCompositeRate = employeeCompositeRate;
	}

	/**
	 * Gets the employerCompositeRate
 	 * @return Returns a double
	 */
	public double getEmployerCompositeRate() {
		return employerCompositeRate;
	}
	
	/**
	 * Sets the employerCompositeRate
	 * @param employerCompositeRate The employerCompositeRate to set
	 */
	public void setEmployerCompositeRate(double employerCompositeRate) {
		this.employerCompositeRate = employerCompositeRate;
	}
		
	/**
	 * Gets the fundType
	 * @return Returns a String
	 */
	public String getFundType() {
		return fundType;
	}

	/**
	 * Sets the fundType
	 * @param fundType The fundType to set
	 */
	public void setFundType(String fundType) {
		this.fundType = fundType;
	}
	
	public String toString()
	{
		return com.manulife.pension.util.StaticHelperClass.toString(this);
	}
	/**
	 * Gets the employeeOngoingContributions
	 * @return Returns a double
	 */
	public double getEmployeeOngoingContributions() {
		return employeeOngoingContributions;
	}
	/**
	 * Sets the employeeOngoingContributions
	 * @param employeeOngoingContributions The employeeOngoingContributions to set
	 */
	public void setEmployeeOngoingContributions(double employeeOngoingContributions) {
		this.employeeOngoingContributions = employeeOngoingContributions;
	}

	/**
	 * Gets the employerOngoingContributions
	 * @return Returns a double
	 */
	public double getEmployerOngoingContributions() {
		return employerOngoingContributions;
	}
	/**
	 * Sets the employerOngoingContributions
	 * @param employerOngoingContributions The employerOngoingContributions to set
	 */
	public void setEmployerOngoingContributions(double employerOngoingContributions) {
		this.employerOngoingContributions = employerOngoingContributions;
	}
    public void addFundMoneyTypeDetail(ParticipantFundMoneyTypeDetailVO moneyTypeDetail)
    {
    	this.moneyTypeDetails.add(moneyTypeDetail);
    }
    
    public ParticipantFundMoneyTypeDetailVO[] getFundMoneyTypeDetails(){
    	return (ParticipantFundMoneyTypeDetailVO[])moneyTypeDetails.toArray(new ParticipantFundMoneyTypeDetailVO[]{});
    }

	public void setRateType(String rateType) {
		this.rateType = rateType;
	}

	public String getRateType() {
		return rateType;
	}

	/**
	 * Returns the Funds Class
	 * 
	 * @return the fundClass
	 */
	public String getFundClass() {
		return fundClass;
	}

	/**
	 * Sets the fund Class
	 * 
	 * @param fundClass the fundClass to set
	 */
	public void setFundClass(String fundClass) {
		this.fundClass = fundClass;
	}

	public boolean isSvgifFlg() {
		return svgifFlg;
	}

	public void setSvgifFlg(boolean svgifFlg) {
		this.svgifFlg = svgifFlg;
	}

    
}
