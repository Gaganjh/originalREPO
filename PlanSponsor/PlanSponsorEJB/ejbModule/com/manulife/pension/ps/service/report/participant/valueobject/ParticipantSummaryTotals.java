package com.manulife.pension.ps.service.report.participant.valueobject;

import java.io.Serializable;

public class ParticipantSummaryTotals implements Serializable{
	
	private int totalParticipants;
	private double employeeAssetsTotal;
	private double employerAssetsTotal;
	private double totalAssets;
	private double outstandingLoans;
	private double employeeAssetsAverage;
	private double employerAssetsAverage;
	private double totalAssetsAverage;
	private double outstandingLoansAverage;
	
	
	
	
	public ParticipantSummaryTotals() {}
	
	public ParticipantSummaryTotals( 	int totalParticipants,
							  		  	double employeeAssetsTotal, 
							  		 	double employerAssetsTotal,
							  		 	double totalAssets,
							  			double outstandingLoans, 
							  			double employeeAssetsAverage, 
							  			double employerAssetsAverage, 
							  			double totalAssetsAverage,
							  			double outstandingLoansAverage)
	{
		this.totalParticipants = totalParticipants;
		this.employeeAssetsTotal = employeeAssetsTotal;		
		this.employerAssetsTotal = employerAssetsTotal;
		this.totalAssets = totalAssets;
		this.outstandingLoans = outstandingLoans;
		this.employeeAssetsAverage = employeeAssetsAverage;
		this.employerAssetsAverage = employerAssetsAverage;
		this.totalAssetsAverage = totalAssetsAverage;
		this.outstandingLoansAverage = outstandingLoansAverage;
	}
	
	/**
	 * Gets the totalParticipants
	 * @return Returns a int
	 */
	public int getTotalParticipants() {
		return totalParticipants;
	}
	/**
	 * Sets the totalParticipants
	 * @param totalParticipants The totalParticipants to set
	 */
	public void setTotalParticipants(int totalParticipants) {
		this.totalParticipants = totalParticipants;
	}

	/**
	 * Gets the employeeAssetsTotal
	 * @return Returns a double
	 */
	public double getEmployeeAssetsTotal() {
		return employeeAssetsTotal;
	}
	/**
	 * Sets the employeeAssetsTotal
	 * @param employeeAssetsTotal The employeeAssetsTotal to set
	 */
	public void setEmployeeAssetsTotal(double employeeAssetsTotal) {
		this.employeeAssetsTotal = employeeAssetsTotal;
	}

	/**
	 * Gets the employerAssetsTotal
	 * @return Returns a double
	 */
	public double getEmployerAssetsTotal() {
		return employerAssetsTotal;
	}
	/**
	 * Sets the employerAssetsTotal
	 * @param employerAssetsTotal The employerAssetsTotal to set
	 */
	public void setEmployerAssetsTotal(double employerAssetsTotal) {
		this.employerAssetsTotal = employerAssetsTotal;
	}

	/**
	 * Gets the totalAssets
	 * @return Returns a double
	 */
	public double getTotalAssets() {
		return totalAssets;
	}
	/**
	 * Sets the totalAssets
	 * @param totalAssets The totalAssets to set
	 */
	public void setTotalAssets(double totalAssets) {
		this.totalAssets = totalAssets;
	}

	/**
	 * Gets the outstandingLoans
	 * @return Returns a double
	 */
	public double getOutstandingLoans() {
		return outstandingLoans;
	}
	/**
	 * Sets the outstandingLoans
	 * @param outstandingLoans The outstandingLoans to set
	 */
	public void setOutstandingLoans(double outstandingLoans) {
		this.outstandingLoans = outstandingLoans;
	}

	/**
	 * Gets the employeeAssetsAverage
	 * @return Returns a double
	 */
	public double getEmployeeAssetsAverage() {
		return employeeAssetsAverage;
	}
	/**
	 * Sets the employeeAssetsAverage
	 * @param employeeAssetsAverage The employeeAssetsAverage to set
	 */
	public void setEmployeeAssetsAverage(double employeeAssetsAverage) {
		this.employeeAssetsAverage = employeeAssetsAverage;
	}

	/**
	 * Gets the employerAssetsAverage
	 * @return Returns a double
	 */
	public double getEmployerAssetsAverage() {
		return employerAssetsAverage;
	}
	/**
	 * Sets the employerAssetsAverage
	 * @param employerAssetsAverage The employerAssetsAverage to set
	 */
	public void setEmployerAssetsAverage(double employerAssetsAverage) {
		this.employerAssetsAverage = employerAssetsAverage;
	}

	/**
	 * Gets the totalAssetsAverage
	 * @return Returns a double
	 */
	public double getTotalAssetsAverage() {
		return totalAssetsAverage;
	}
	/**
	 * Sets the totalAssetsAverage
	 * @param totalAssetsAverage The totalAssetsAverage to set
	 */
	public void setTotalAssetsAverage(double totalAssetsAverage) {
		this.totalAssetsAverage = totalAssetsAverage;
	}

	/**
	 * Gets the outstandingLoansAverage
	 * @return Returns a double
	 */
	public double getOutstandingLoansAverage() {
		return outstandingLoansAverage;
	}
	/**
	 * Sets the outstandingLoansAverage
	 * @param outstandingLoansAverage The outstandingLoansAverage to set
	 */
	public void setOutstandingLoansAverage(double outstandingLoansAverage) {
		this.outstandingLoansAverage = outstandingLoansAverage;
	}

}

