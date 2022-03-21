package com.manulife.pension.ps.service.report.participant.valueobject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;

public class ParticipantEnrollmentDetails implements Serializable {
	
	private double profileId;
	private String firstName;
	private String lastName;
	private String ssn;
	private Date birthDate;
	private Date enrollmentProcessedDate;
	private Date mfcContractEnrollmentDate;//enrollment effective date
	private String enrollmentMethod;//Internet, Default, Paper
	private double contributionAmt;
	private double contributionPct;	
	private String participantStatus;
	private String employerDesignatedID;//PayrollNumber
	private Date normalRetirementDate;
	private String eligibleToDeferInd;
	private String organizationUnitID;//Division		
	private String stateCode;
	private String contributionStatus;
	private static DecimalFormat decimalFormatter;
	private static String DECIMAL_FORMATTER = "##########";
	
	//synchronized method to avoid race condition.
	public static synchronized String formatDecimalFormatter(Double value) { 
        return decimalFormatter.format(value); 
    }
	public ParticipantEnrollmentDetails() {}
	
	public ParticipantEnrollmentDetails( double profileId,
										String firstName,
							  		  	String lastName, 
							  		 	String ssn,
							  		 	Date birthDate,
							  		 	Date enrollmentProcessedDate,
							  		 	Date mfcContractEnrollmentDate,
							  		 	String enrollmentMethod,
							  		 	double contributionAmt,
							  		 	double contributionPct,
							  		 	String participantStatus,
							  		 	String employerDesignatedID,
							  		 	Date normalRetirementDate,
							  		 	String eligibleToDeferInd,
							  		 	String organizationUnitID,
										String stateCode,
										String contributionStatus){
		this.profileId = profileId;
		this.firstName = firstName;
		this.lastName = lastName;		
		this.ssn = ssn;
		this.birthDate = birthDate;
		this.enrollmentProcessedDate = enrollmentProcessedDate;
		this.mfcContractEnrollmentDate = mfcContractEnrollmentDate;
		this.enrollmentMethod = enrollmentMethod;
		this.contributionAmt = contributionAmt;
		this.contributionPct = contributionPct;
		this.participantStatus = participantStatus;
		this.employerDesignatedID = employerDesignatedID;
		this.normalRetirementDate = normalRetirementDate;
		this.eligibleToDeferInd = eligibleToDeferInd;
		this.organizationUnitID = organizationUnitID;
		this.stateCode = stateCode;
		this.contributionStatus = contributionStatus;
	}
	
	/**
	 * Gets the profile id
	 * @return Returns a double
	 */
	public double getProfileId() {
		
		return profileId;
	}
	/**
	 * Sets the profileId
	 * @param profileId The profileId to set
	 */
	public void setProfileId(double profileId) {
		this.profileId = profileId;
	}


			
	/**
	 * Gets the firstName
	 * @return Returns a String
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * Sets the firstName
	 * @param firstName The firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * Gets the lastName
	 * @return Returns a String
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * Sets the lastName
	 * @param lastName The lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the ssn
	 * @return Returns a String
	 */
	public String getSsn() {
		return ssn;
	}
	/**
	 * Sets the ssn
	 * @param ssn The ssn to set
	 */
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}




	/**
	 * Gets the birthDate
	 * @return Returns a Date
	 */
	public Date getBirthDate() {
		return birthDate;
	}
	/**
	 * Sets the birthDate
	 * @param birthDate The birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * Gets the enrollmentProcessedDate
	 * @return Returns a Date
	 */
	public Date getEnrollmentProcessedDate() {
		return enrollmentProcessedDate;
	}
	/**
	 * Sets the enrollmentProcessedDate
	 * @param enrollmentProcessedDate The enrollmentProcessedDate to set
	 */
	public void setEnrollmentProcessedDate(Date enrollmentProcessedDate) {
		this.enrollmentProcessedDate = enrollmentProcessedDate;
	}

	/**
	 * Gets the mfcContractEnrollmentDate
	 * @return Returns a Date
	 */
	public Date getMfcContractEnrollmentDate() {
		return mfcContractEnrollmentDate;
	}
	/**
	 * Sets the mfcContractEnrollmentDate
	 * @param mfcContractEnrollmentDate The mfcContractEnrollmentDate to set
	 */
	public void setMfcContractEnrollmentDate(Date mfcContractEnrollmentDate) {
		this.mfcContractEnrollmentDate = mfcContractEnrollmentDate;
	}

	/**
	 * Gets the enrollmentMethod
	 * @return Returns a String
	 */
	public String getEnrollmentMethod() {
		return enrollmentMethod;
	}
	/**
	 * Sets the enrollmentMethod
	 * @param enrollmentMethod The enrollmentMethod to set
	 */
	public void setEnrollmentMethod(String enrollmentMethod) {
		this.enrollmentMethod = enrollmentMethod;
	}


	/**
	 * Gets the contributionAmt
	 * @return Returns a double
	 */
	public double getContributionAmt() {
		return contributionAmt;
	}
	/**
	 * Sets the contributionAmt
	 * @param contributionAmt The contributionAmt to set
	 */
	public void setContributionAmt(double contributionAmt) {
		this.contributionAmt = contributionAmt;
	}


	/**
	 * Gets the contributionPct
	 * @return Returns a double
	 */
	public double getContributionPct() {
		return contributionPct;
	}
	/**
	 * Sets the contributionPct
	 * @param contributionPct The contributionPct to set
	 */
	public void setContributionPct(double contributionPct) {
		this.contributionPct = contributionPct;
	}


	/**
	 * Gets the participantStatus
	 * @return Returns a String
	 */
	public String getParticipantStatus() {
		return participantStatus;
	}
	/**
	 * Sets the participantStatus
	 * @param participantStatus The participantStatus to set
	 */
	public void setParticipantStatus(String participantStatus) {
		this.participantStatus = participantStatus;
	}

	/**
	 * Gets the employerDesignatedID
	 * @return Returns a String
	 */
	public String getEmployerDesignatedID() {
		return employerDesignatedID;
	}
	/**
	 * Sets the employerDesignatedID
	 * @param employerDesignatedID The employerDesignatedID to set
	 */
	public void setEmployerDesignatedID(String employerDesignatedID) {
		this.employerDesignatedID = employerDesignatedID;
	}


	/**
	 * Gets the normalRetirementDate
	 * @return Returns a Date
	 */
	public Date getNormalRetirementDate() {
		return normalRetirementDate;
	}
	/**
	 * Sets the normalRetirementDate
	 * @param normalRetirementDate The normalRetirementDate to set
	 */
	public void setNormalRetirementDate(Date normalRetirementDate) {
		this.normalRetirementDate = normalRetirementDate;
	}

	/**
	 * Gets the eligibleToDeferInd
	 * @return Returns a String
	 */
	public String getEligibleToDeferInd() {
		return eligibleToDeferInd;
	}
	/**
	 * Sets the eligibleToDeferInd
	 * @param eligibleToDeferInd The eligibleToDeferInd to set
	 */
	public void setEligibleToDeferInd(String eligibleToDeferInd) {
		this.eligibleToDeferInd = eligibleToDeferInd;
	}
	
	/**
	 * Gets the organizationUnitID
	 * @return Returns a String
	 */
	public String getOrganizationUnitID() {
		return organizationUnitID;
	}
	/**
	 * Sets the organizationUnitID
	 * @param organizationUnitID The organizationUnitID to set
	 */
	public void setOrganizationUnitID(String organizationUnitID) {
		this.organizationUnitID = organizationUnitID;
	}


	/**
	 * Gets the stateCode
	 * @return Returns a String
	 */
	public String getStateCode() {
		return stateCode;
	}
	/**
	 * Sets the stateCode
	 * @param stateCode The stateCode to set
	 */
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}


	/**
	 * Gets the contributionStatus
	 * @return Returns a String
	 */
	public String getContributionStatus() {
		return contributionStatus;
	}
	/**
	 * Sets the contributionStatus
	 * @param contributionStatus The contributionStatus to set
	 */
	public void setContributionStatus(String contributionStatus) {
		this.contributionStatus = contributionStatus;
	}


	public String getLinkProfileId(){
		decimalFormatter = new DecimalFormat(DECIMAL_FORMATTER);
		return formatDecimalFormatter(this.profileId);
	}
	
	public double getDisplayContributionPct(){
		return this.contributionPct/100;	
	}	
}

