/*
 * Created on May 26, 2005
 *
 * Value object to store participant enrollment summary info.
 */
package com.manulife.pension.ps.service.report.participant.valueobject;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class ParticipantEnrollmentSummary implements Serializable{

	private double profileId;
	private String firstName;
	private String lastName;
	private String ssn;
	private Date birthDate;
	private String employerDesignatedID;//PayrollNumber
	private String eligibleToDeferInd;
	private String organizationUnitID;//Division
	private Date enrollmentProcessedDate;
	private String enrollmentMethod;//Internet, Auto, Was Auto Enroll, Default, Paper
	private static DecimalFormat decimalFormatter;
	private String linkProfileId;
	
	private Date mfcContractEnrollmentDate;
	private String contributionInstructSrcCode;
	//private double contributionAmount;
	//private double contributionPercent;
	private ArrayList deferralItems;
	private String deferralComment;
	private String participantStatusCode;
	private Date normalRetirementDate;
	private String planEntryDate;
	private String residenceStateCode;
	private String contributionStatus;
	private double displayContributionPct;
	private Timestamp deferralCreatedTS;


	private double displayContributionPctRoth;	
	private double contributionAmt;
	private double contributionPct;
	private double contributionAmtRoth;
	private double contributionPctRoth;

	//synchronized method to avoid race condition. 
	public static synchronized String formatDecimalFormatter(Double value) { 
        return decimalFormatter.format(value); 
    }
	
	/**
	 * @param profileId
	 * @param firstName
	 * @param lastName
	 * @param ssn
	 * @param birthDate
	 * @param employerDesignatedID
	 * @param eligibleToDeferInd
	 * @param organizationUnitID
	 * @param enrollmentProcessedDate
	 * @param enrollmentMethod
	 * @param mfcContractEnrollmentDate
	 * @param contributionInstructSrcCode
	 * @param participantStatusCode
	 * @param normalRetirementDate
	 * @param planEntryDate
	 * @param residenceStateCode
	 * @param contributionStatus
	 */
	public ParticipantEnrollmentSummary(double profileId, String firstName,
			String lastName, String ssn, Date birthDate,
			String employerDesignatedID, String eligibleToDeferInd,
			String organizationUnitID, Date enrollmentProcessedDate,
			String enrollmentMethod, Date mfcContractEnrollmentDate,
			String contributionInstructSrcCode,	ArrayList deferralItems, 
			String deferralComment,	String participantStatusCode, 
			Date normalRetirementDate, String planEntryDate, 
			String residenceStateCode, String contributionStatus) {
		super();
		this.profileId = profileId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.ssn = ssn;
		this.birthDate = birthDate;
		this.employerDesignatedID = employerDesignatedID;
		this.eligibleToDeferInd = eligibleToDeferInd;
		this.organizationUnitID = organizationUnitID;
		this.enrollmentProcessedDate = enrollmentProcessedDate;
		this.enrollmentMethod = enrollmentMethod;
		this.mfcContractEnrollmentDate = mfcContractEnrollmentDate;
		this.contributionInstructSrcCode = contributionInstructSrcCode;
		this.deferralItems = deferralItems;
		this.deferralComment = deferralComment;
		this.participantStatusCode = participantStatusCode;
		this.normalRetirementDate = normalRetirementDate;
		this.planEntryDate = planEntryDate;
		this.residenceStateCode = residenceStateCode;
		this.contributionStatus = contributionStatus;
	}
	/**
	 * @param profileId
	 * @param firstName
	 * @param lastName
	 * @param ssn
	 * @param birthDate
	 * @param employerDesignatedID
	 * @param eligibleToDeferInd
	 * @param organizationUnitID
	 * @param enrollmentProcessedDate
	 * @param enrollmentMethod
	 */
	
	
	/**
	 * @return Returns the birthDate.
	 */
	public Date getBirthDate() {
		return birthDate;
	}
	/**
	 * @param birthDate The birthDate to set.
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	/**
	 * @return Returns the eligibleToDeferInd.
	 */
	public String getEligibleToDeferInd() {
		return eligibleToDeferInd;
	}
	/**
	 * @param eligibleToDeferInd The eligibleToDeferInd to set.
	 */
	public void setEligibleToDeferInd(String eligibleToDeferInd) {
		this.eligibleToDeferInd = eligibleToDeferInd;
	}
	/**
	 * @return Returns the employerDesignatedID.
	 */
	public String getEmployerDesignatedID() {
		return employerDesignatedID;
	}
	/**
	 * @param employerDesignatedID The employerDesignatedID to set.
	 */
	public void setEmployerDesignatedID(String employerDesignatedID) {
		this.employerDesignatedID = employerDesignatedID;
	}
	/**
	 * @return Returns the enrollmentMethod.
	 */
	public String getEnrollmentMethod() {
		return enrollmentMethod;
	}
	/**
	 * @param enrollmentMethod The enrollmentMethod to set.
	 */
	public void setEnrollmentMethod(String enrollmentMethod) {
		this.enrollmentMethod = enrollmentMethod;
	}
	/**
	 * @return Returns the enrollmentProcessedDate.
	 */
	public Date getEnrollmentProcessedDate() {
		return enrollmentProcessedDate;
	}
	/**
	 * @param enrollmentProcessedDate The enrollmentProcessedDate to set.
	 */
	public void setEnrollmentProcessedDate(Date enrollmentProcessedDate) {
		this.enrollmentProcessedDate = enrollmentProcessedDate;
	}
	/**
	 * @return Returns the firstName.
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName The firstName to set.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * @return Returns the lastName.
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName The lastName to set.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return Returns the organizationUnitID.
	 */
	public String getOrganizationUnitID() {
		return organizationUnitID;
	}
	/**
	 * @param organizationUnitID The organizationUnitID to set.
	 */
	public void setOrganizationUnitID(String organizationUnitID) {
		this.organizationUnitID = organizationUnitID;
	}
	/**
	 * @return Returns the profileId.
	 */
	public double getProfileId() {
		return profileId;
	}
	/**
	 * @param profileId The profileId to set.
	 */
	public void setProfileId(double profileId) {
		this.profileId = profileId;
	}
	/**
	 * @return Returns the ssn.
	 */
	public String getSsn() {
		return ssn;
	}
	/**
	 * @param ssn The ssn to set.
	 */
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	
	public String getLinkProfileId(){
		decimalFormatter = new DecimalFormat("##########");
		return formatDecimalFormatter(this.profileId);
	}
	

	/**
	 * @return Returns the mfcContractEnrollmentDate.
	 */
	public Date getMfcContractEnrollmentDate() {
		return mfcContractEnrollmentDate;
	}
	/**
	 * @param mfcContractEnrollmentDate The mfcContractEnrollmentDate to set.
	 */
	public void setMfcContractEnrollmentDate(Date mfcContractEnrollmentDate) {
		this.mfcContractEnrollmentDate = mfcContractEnrollmentDate;
	}
	/**
	 * @return Returns the normalRetirementDate.
	 */
	public Date getNormalRetirementDate() {
		return normalRetirementDate;
	}
	/**
	 * @param normalRetirementDate The normalRetirementDate to set.
	 */
	public void setNormalRetirementDate(Date normalRetirementDate) {
		this.normalRetirementDate = normalRetirementDate;
	}
	/**
	 * @return Returns the participantStatusCode.
	 */
	public String getParticipantStatusCode() {
		return participantStatusCode;
	}
	/**
	 * @param participantStatusCode The participantStatusCode to set.
	 */
	public void setParticipantStatusCode(String participantStatusCode) {
		this.participantStatusCode = participantStatusCode;
	}
	/**
	 * @return Returns the planEntryDate.
	 */
	public String getPlanEntryDate() {
		return planEntryDate;
	}
	/**
	 * @param planEntryDate The planEntryDate to set.
	 */
	public void setPlanEntryDate(String planEntryDate) {
		this.planEntryDate = planEntryDate;
	}
	/**
	 * @return Returns the residenceDateCode.
	 */
	public String getResidenceStateCode() {
		return residenceStateCode;
	}
	/**
	 * @param residenceStateCode The residenceStateCode to set.
	 */
	public void setResidenceStateCode(String residenceStateCode) {
		this.residenceStateCode = residenceStateCode;
	}

	/**
	 * @return Returns the contributionAmount.
	 */

	
	/**
	 * @return Returns the contributionInstructSrcCode.
	 */
	public String getContributionInstructSrcCode() {
		return contributionInstructSrcCode;
	}
	/**
	 * @param contributionInstructSrcCode The contributionInstructSrcCode to set.
	 */
	public void setContributionInstructSrcCode(
			String contributionInstructSrcCode) {
		this.contributionInstructSrcCode = contributionInstructSrcCode;
	}
	
	
	/**
	 * @return Returns the deferralComment.
	 */
	public String getDeferralComment() {
		return deferralComment;
	}

	/**
	 * @param deferralComment The deferralComment to set.
	 */
	public void setDeferralComment(String deferralComment) {
		this.deferralComment = deferralComment;
	}

	

	public double getDisplayContributionPct(){
		return this.contributionPct/100;	
	}

	public double getDisplayContributionPctRoth(){
		return this.contributionPctRoth/100;	
	}		


	
	/**
	 * @return Returns the contributionStatus.
	 */
	public String getContributionStatus() {
		return contributionStatus;
	}
	/**
	 * @param contributionStatus The contributionStatus to set.
	 */
	public void setContributionStatus(String contributionStatus) {
		this.contributionStatus = contributionStatus;
	}

	/**
	 * @return Returns the deferralCreatedTS.
	 */
	public Timestamp getDeferralCreatedTS() {
		return deferralCreatedTS;
	}
	/**
	 * @param deferralCreatedTS The deferralCreatedTS to set.
	 */
	public void setDeferralCreatedTS(Timestamp deferralCreatedTS) {
		this.deferralCreatedTS = deferralCreatedTS;
	}
	
	
	/**
	 * @return Returns the deferralItems.
	 */
	public ArrayList getDeferralItems() {
		return deferralItems;
	}
	/**
	 * @param deferralItems The deferralItems to set.
	 */
	public void setDeferralItems(ArrayList deferralItems) {
		this.deferralItems = deferralItems;
	}


	public void setContributionAmt(double contributionAmt) {
		this.contributionAmt = contributionAmt;
	}
	
	public double getContributionAmt() {
		return this.contributionAmt; 
	}	
	
	public void setContributionPct(double contributionPct) {
		this.contributionPct = contributionPct; 
	}
	
	public double getContributionPct() {
		return this.contributionPct; 
	}		

	
	public void setContributionAmtRoth(double contributionAmtRoth) {
		this.contributionAmtRoth = contributionAmtRoth;
	}
	
	public double getContributionAmtRoth() {
		return this.contributionAmtRoth; 
	}	
	
	public void setContributionPctRoth(double contributionPctRoth) {
		this.contributionPctRoth = contributionPctRoth; 
	}
	
	public double getContributionPctRoth() {
		return this.contributionPctRoth; 
	}			
	
	
	public boolean hasRothDeferral(){
		boolean result = false;
			
		if (this.deferralItems != null){
			if (this.deferralItems.size()> 0){
				for (int i = 0; i< deferralItems.size(); i++){
					ParticipantDeferralChangesItem item = (ParticipantDeferralChangesItem)deferralItems.get(i);
					if (item.getMoneyTypeCode().equals("EEROT")){
						this.setContributionAmtRoth(item.getContributionAmt());
						this.setContributionPctRoth(item.getContributionPct());
						result =  true;
						break;
					}
				}
			}
		}
		
		return result;
	}	

	public boolean hasTradDeferral(){
		boolean result = false;
			
		if (this.deferralItems != null){
			if (this.deferralItems.size()> 0){
				for (int i = 0; i< deferralItems.size(); i++){
					ParticipantDeferralChangesItem item = (ParticipantDeferralChangesItem)deferralItems.get(i);
					if (!item.getMoneyTypeCode().equals("EEROT")){
						this.setContributionAmt(item.getContributionAmt());
						this.setContributionPct(item.getContributionPct());
						result =  true;
						break;
					}
				}
			}
		}
		
		return result;
	}
	
}
