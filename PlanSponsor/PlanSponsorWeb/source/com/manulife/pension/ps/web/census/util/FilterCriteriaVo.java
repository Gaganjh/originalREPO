package com.manulife.pension.ps.web.census.util;

import com.manulife.pension.platform.web.util.Ssn;

/**
 * This VO is used to get and set the filter criterias. This vo will be maintained in the session.
 * 
 * @author jthangad
 */
public class FilterCriteriaVo implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	private String lastName;

	private int contractNumber;

	private boolean isEzStartOn;

	private String employmentStatus;

	private String enrollmentStatus;

	private String segment;

	private String ssnThree;
	
	private String ssnOne;
	
	private String ssnTwo;
	
	private String division;
	
    private String enrolledFrom="";
    
    private String enrolledTo=""; 
	
	private String asOfDate;
	
	private String eligibilitySortField;
	
	private String eligibilitySortDirection;
	
	private int eligibilityPageNumber = 1;
	
	private String deferralSortField;
	
	private String deferralSortDirection;
	
	private int deferralPageNumber = 1;
	
	private String fromPED = "";
	
	private String toPED = "";
	
	private String moneyTypeSelected = "''";
	
	private String moneyTypeFilter = "";
	 
	private String participantName;
	
	private String wdStatus;
	
	private String wdType;
	
	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}

	public String getWdStatus() {
		return wdStatus;
	}

	public void setWdStatus(String wdStatus) {
		this.wdStatus = wdStatus;
	}

	public String getWdType() {
		return wdType;
	}

	public void setWdType(String wdType) {
		this.wdType = wdType;
	}

	public String getAsOfDate() {
		return asOfDate;
	}

	public void setAsOfDate(String asOfDate) {
		this.asOfDate = asOfDate;
	}

	public String getEnrolledFrom() {
		return enrolledFrom;
	}

	public void setEnrolledFrom(String enrolledFrom) {
		this.enrolledFrom = enrolledFrom;
	}

	public String getEnrolledTo() {
		return enrolledTo;
	}

	public void setEnrolledTo(String enrolledTo) {
		this.enrolledTo = enrolledTo;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public int getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(int contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getEmploymentStatus() {
		return employmentStatus;
	}

	public void setEmploymentStatus(String employmentStatus) {
		this.employmentStatus = employmentStatus;
	}

	public String getEnrollmentStatus() {
		return enrollmentStatus;
	}

	public void setEnrollmentStatus(String enrollmentStatus) {
		this.enrollmentStatus = enrollmentStatus;
	}

	public boolean isEzStartOn() {
		return isEzStartOn;
	}

	public void setEzStartOn(boolean isEzStartOn) {
		this.isEzStartOn = isEzStartOn;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}
	
	public Ssn getSsn() {
		Ssn ssnTemp = new Ssn();
		ssnTemp.setDigits(0,ssnOne);
		ssnTemp.setDigits(1,ssnTwo);
		ssnTemp.setDigits(2,ssnThree);
		return ssnTemp;
	}

	public String getSsnOne() {
		return ssnOne;
	}

	public void setSsnOne(String ssnOne) {
		this.ssnOne = ssnOne;
	}

	public String getSsnThree() {
		return ssnThree;
	}

	public void setSsnThree(String ssnThree) {
		this.ssnThree = ssnThree;
	}

	public String getSsnTwo() {
		return ssnTwo;
	}

	public void setSsnTwo(String ssnTwo) {
		this.ssnTwo = ssnTwo;
	}

	public int getEligibilityPageNumber() {
		return eligibilityPageNumber;
	}

	public void setEligibilityPageNumber(int eligibilityPageNumber) {
		this.eligibilityPageNumber = eligibilityPageNumber;
	}

	public String getEligibilitySortDirection() {
		return eligibilitySortDirection;
	}

	public void setEligibilitySortDirection(String eligibilitySortDirection) {
		this.eligibilitySortDirection = eligibilitySortDirection;
	}

	public String getEligibilitySortField() {
		return eligibilitySortField;
	}

	public void setEligibilitySortField(String eligibilitySortField) {
		this.eligibilitySortField = eligibilitySortField;
	}
	
	public int getDeferralPageNumber() {
		return deferralPageNumber;
	}
	
	public void setDeferralPageNumber(int deferralPageNumber) {
		this.deferralPageNumber = deferralPageNumber;
	}

	public String getDeferralSortDirection() {
		return deferralSortDirection;
	}

	public void setDeferralSortDirection(String deferralSortDirection) {
		this.deferralSortDirection = deferralSortDirection;
	}

	public String getDeferralSortField() {
		return deferralSortField;
	}

	public void setDeferralSortField(String deferralSortField) {
		this.deferralSortField = deferralSortField;
	}
	
	/**
	 * Clears the cached page number and sort details of eligibility tab
	 */
	public void clearEligibilitySortDetails(){
		this.eligibilityPageNumber = 1;
		this.eligibilitySortDirection = null;
		this.eligibilitySortField = null;
	}
	
	/**
	 * Clears the page number and sort details of deferral tab 
	 */
	public void clearDeferralSortDetails(){
		this.deferralPageNumber = 1;
		this.deferralSortDirection = null;
		this.deferralSortField = null;
	}

	public void setFromPED(String fromPED) {
		this.fromPED = fromPED;
	}

	public void setToPED(String toPED) {
		this.toPED = toPED;
	}

	public String getFromPED() {
		return fromPED;
	}

	public String getToPED() {
		return toPED;
	}

	public String getMoneyTypeSelected() {
		return moneyTypeSelected;
	}

	public void setMoneyTypeSelected(String moneyTypeSelected) {
		this.moneyTypeSelected = moneyTypeSelected;
	}

	public String getMoneyTypeFilter() {
	    return moneyTypeFilter;
	}

	public void setMoneyTypeFilter(String moneyTypeFilter) {
	    this.moneyTypeFilter = moneyTypeFilter;
	}
}
