package com.manulife.pension.ps.service.report.participant.valueobject;

import java.io.Serializable;

/**
 * ParticipantForfeituresTotals is a ValueObject class to hold the  
 * Accounts Forfeitures Summary Information. This class has been
 * extended from ParticipantSummaryTotals to reuse the existing
 * fields of Accounts Summary page.
 * 
 * @author Vinothkumar Balasubramaniyam
 */
public class ParticipantForfeituresTotals extends ParticipantSummaryTotals implements Serializable {
	
	/**
	 * Default Serial Version UID.  
	 */
	private static final long serialVersionUID = 1L;
	
	//The following attributes are required additionally for Accounts Forfeitures Page
	private double forfeituresTotal;
	private double forfeituresAverage;
	
	/**
	 * Default Constructor
	 */
	public ParticipantForfeituresTotals() {}
	
	/**
	 * Argument constructor to initialize the parent class and self
	 * 
	 * @param totalParticipants
	 * @param employeeAssetsTotal
	 * @param employerAssetsTotal
	 * @param totalAssets
	 * @param forfeituresTotal
	 * @param employeeAssetsAverage
	 * @param employerAssetsAverage
	 * @param totalAssetsAverage
	 * @param forfeituresAverage
	 */
	public ParticipantForfeituresTotals( int totalParticipants,
							  		  	double employeeAssetsTotal, 
							  		 	double employerAssetsTotal,
							  		 	double totalAssets,
							  		 	double forfeituresTotal,
							  			double employeeAssetsAverage, 
							  			double employerAssetsAverage, 
							  			double totalAssetsAverage,
							  			double forfeituresAverage){
		//Assigning the parameters to Super class
		this.setTotalParticipants(totalParticipants);
		this.setEmployeeAssetsTotal(employeeAssetsTotal);
		this.setEmployerAssetsTotal(employerAssetsTotal);
		this.setTotalAssets(totalAssets);
		this.setEmployeeAssetsAverage(employeeAssetsAverage);
		this.setEmployerAssetsAverage(employerAssetsAverage);
		this.setTotalAssetsAverage(totalAssetsAverage);
		
		this.forfeituresTotal = forfeituresTotal;
		this.forfeituresAverage = forfeituresAverage;
	}
	
	/**
	 * Gets the forfeituresTotal
	 * 
	 * @return the forfeituresTotal
	 */
	public double getForfeituresTotal() {
		return forfeituresTotal;
	}

	/**
	 * Sets the forfeituresTotal
	 * 
	 * @param forfeituresTotal the forfeituresTotal to set
	 */
	public void setForfeituresTotal(double forfeituresTotal) {
		this.forfeituresTotal = forfeituresTotal;
	}

	/**
	 * Gets the forfeituresAverage
	 * 
	 * @return the forfeituresAverage
	 */
	public double getForfeituresAverage() {
		return forfeituresAverage;
	}

	/**
	 * Sets the forfeituresAverage
	 * 
	 * @param forfeituresAverage the forfeituresAverage to set
	 */
	public void setForfeituresAverage(double forfeituresAverage) {
		this.forfeituresAverage = forfeituresAverage;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		return result.append("ParticipantForfeituresTotals :")
					.append("[forfeituresAverage=").append(forfeituresAverage)
					.append(", forfeituresTotal=").append(forfeituresTotal)
					.append("]").toString();
	}
	
}
