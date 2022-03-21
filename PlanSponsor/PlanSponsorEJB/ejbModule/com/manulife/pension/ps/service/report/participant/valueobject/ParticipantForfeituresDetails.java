package com.manulife.pension.ps.service.report.participant.valueobject;

import java.io.Serializable;

/**
 * ParticipantForfeituresDetails is a ValueObject class to hold the  
 * Accounts Forfeitures page's Participant Information. This class has been
 * extended from ParticipantSummaryDetails to reuse the existing
 * fields of Accounts Summary page.
 * 
 * @author Vinothkumar Balasubramaniyam
 */
public class ParticipantForfeituresDetails extends ParticipantSummaryDetails implements Serializable {
	
	/**
	 * Default Serial Version UID.  
	 */
	private static final long serialVersionUID = 1L;
	
	//Attributes to hold the forfeitures amount and terminationDate
	private double forfeitures;
	private String terminationDate;
	
	/**
	 * Default Constructor
	 */
	public ParticipantForfeituresDetails() {}
	
	/**
	 * Argument constructor to initialize the parent class and self
	 * 
	 * @param firstName
	 * @param lastName
	 * @param ssn
	 * @param division
	 * @param status
	 * @param employeeAssets
	 * @param employerAssets
	 * @param totalAssets
	 * @param profileId
	 * @param forfeitures
	 */
	public ParticipantForfeituresDetails( String firstName,
							  		  	String lastName, 
							  		 	String ssn,
							  		 	String division,
							  			String status,
							  			double employeeAssets, 
							  			double employerAssets,
							  			double totalAssets, 
							  			String profileId,
							  			double forfeitures,
							  			String terminationDate){
		//Assigning the parameters to Super class
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setSsn(ssn);
		this.setDivision(division);
		this.setStatus(status);
		this.setEmployeeAssets(employeeAssets);
		this.setEmployerAssets(employerAssets);
		this.setTotalAssets(totalAssets);		
		this.setProfileId(profileId);
		
		//Assigning the forfeitures parameter
		this.forfeitures = forfeitures;
		this.terminationDate = terminationDate;
	}

	/**
	 * Gets the forfeitures
	 * 
	 * @return the forfeitures
	 */
	public double getForfeitures() {
		return forfeitures;
	}

	/**
	 * Sets the forfeitures
	 * 
	 * @param forfeitures the forfeitures to set
	 */
	public void setForfeitures(double forfeitures) {
		this.forfeitures = forfeitures;
	}
	
	/**
	 * Gets the terminationDate
	 * 
	 * @return the terminationDate
	 */
	public String getTerminationDate() {
		return terminationDate;
	}

	/**
	 * Sets the terminationDate
	 * 
	 * @param terminationDate the terminationDate to set
	 */
	public void setTerminationDate(String terminationDate) {
		this.terminationDate = terminationDate;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		return result.append("ParticipantForfeituresDetails :")
					.append("[forfeitures=").append(forfeitures).append("]")
					.append("[terminationDate=").append(terminationDate).append("]")
					.toString();
	}
}