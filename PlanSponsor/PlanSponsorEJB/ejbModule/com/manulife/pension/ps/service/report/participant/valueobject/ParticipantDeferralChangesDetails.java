package com.manulife.pension.ps.service.report.participant.valueobject;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class ParticipantDeferralChangesDetails implements Serializable {
	
	private String firstName;
	private String lastName;
	private String ssn;
	private Timestamp changeTS;
	private Date changeDate;//Timestamp with hour, mins and seconds trimmed
	private String employerDesignatedID;//PayrollNumber
	private String organizationUnitID;//Division
	//private double contributionAmt;
	//private double contributionPct;
	private ArrayList deferralItems;
	private String contributionStatus;
	private double profileId;
	private boolean processInd;
	private boolean changeProcessInd;	//stores the temporary process indicator	
	private DecimalFormat decimalFormatter;
	private String linkProfileId;
	private double displayContributionPct;
	private double displayContributionPctRoth;
	private double contributionAmt;
	private double contributionPct;
	private double contributionAmtRoth;
	private double contributionPctRoth;
	
	public ParticipantDeferralChangesDetails() {}
	
	public ParticipantDeferralChangesDetails( String firstName,
							  		  	String lastName, 
							  		 	String ssn,
							  		 	Timestamp changeTS,
							  		 	String employerDesignatedID,
							  		 	String organizationUnitID,
							  			String contributionStatus, 
							  			double profileId,
							  			ArrayList deferralItems,
							  			boolean processInd)
	{
		this.firstName = firstName;
		this.lastName = lastName;		
		this.ssn = ssn;
		this.changeTS = changeTS;
		this.employerDesignatedID = employerDesignatedID;
		this.organizationUnitID = organizationUnitID;
		this.contributionStatus = contributionStatus;
		this.profileId = profileId;
		this.deferralItems = deferralItems;
		this.processInd = processInd;
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
	 * Gets the changeDate
	 * @return Returns a Date
	 */
	public Date getChangeDate() {
		return changeDate;
	}
	/**
	 * Sets the changeDate
	 * @param changeDate The changeDate to set
	 */
	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}
	
	/**
	 * Gets the changeTS
	 * @return Returns a Timestamp
	 */
	public Timestamp getChangeTS() {
		return changeTS;
	}
	/**
	 * Sets the changeTS
	 * @param changeTS The changeTS to set
	 */
	public void setChangeTS(Timestamp changeTS) {
		this.changeTS = changeTS;
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
	/**
	 * Sets the profileId
	 * @param profileId The profileId to set
	 */
	public void setProfileId(double profileId) {
		this.profileId = profileId;
	}
	
	/**
	 * Gets the profile id
	 * @return Returns a double
	 */
	public double getProfileId() {
		
		return profileId;
	}
	
	/**
	 * Sets the deferral items
	 * @param deferralItems The deferralItems to set. An ArrayList of ParticipantDeferralChangesItem
	 */
	public void setDeferralItems(ArrayList deferralItems) {
		this.deferralItems = deferralItems;
	}
	
	/**
	 * Gets the deferral items
	 * @return Returns an ArrayList of ParticipantDeferralChangesItem
	 */
	public ArrayList getDeferralItems() {
		return this.deferralItems;
	}

	
	/**
	 * Sets the process Indicator
	 * @param processInd The processInd to set
	 */
	public void setProcessInd(boolean processInd) {
		this.processInd = processInd;
	}
	
	/**
	 * Gets the process Ind
	 * @return Returns a boolean
	 */
	public boolean getProcessInd() {
		return processInd;
	}
	
	
	
	/**
	 * Sets the change process Indicator
	 * @param changeProcessInd The changeProcessInd to set
	 */
	public void setChangeProcessInd(boolean changeProcessInd) {
		this.changeProcessInd = changeProcessInd;
	}
	
	/**
	 * Gets the change process Ind
	 * @return Returns a boolean
	 */
	public boolean getChangeProcessInd() {
		return changeProcessInd;
	}
	
	
	/**
	 * Gets a unique id
	 * @return Returns a String
	 */
	public String getUniqueId() {
		return profileId + "_" + changeDate.getTime();
	}

	public String getLinkProfileId(){
		decimalFormatter = new DecimalFormat("##########");
		return decimalFormatter.format(this.profileId);
	}
	
	public double getDisplayContributionPct(){
		return this.contributionPct/100;	
	}	

	public double getDisplayContributionPctRoth(){
		return this.contributionPctRoth/100;	
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


