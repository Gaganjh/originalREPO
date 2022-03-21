package com.manulife.pension.ps.service.report.participant.valueobject;

import java.util.Date;

import com.manulife.pension.ps.service.report.participant.reporthandler.ParticipantEnrollmentSummaryReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class ParticipantEnrollmentSummaryReportData extends ReportData {

	public static final String REPORT_ID = ParticipantEnrollmentSummaryReportHandler.class.getName();
	public static final String REPORT_NAME = "participantEnrollmentSummaryReport"; 
	public static final String FILTER_FIELD_1 = "contractNumber";
	public static final String FILTER_FIELD_2 = "fromDate";
	public static final String FILTER_FIELD_3 = "toDate";
	public static final String FILTER_FIELD_4 = "lastName";
	public static final String FILTER_FIELD_5 = "ssn";	
	public static final String DEFAULT_SORT = "enrollmentProcessedDate";
	public static final int    PAGE_SIZE = 35;
	public static final String FILTER_FIELD_6 = "profileId";

	
	private int contractNumber;
	private Date fromDate;
	private Date toDate;
	private String lastName;
	private String ssn;
	private int numberParticipantsWithEnrollments;
	private int nonEmptyPayrollCount;	//count of payroll_no that is not null and == ' '
	private int nonEmptyOrganizationUnitCount; //count of organization_unit_id that is not null and == ' '
	private int numberInternetEnrollments;//number of i-enrollments - if 0 then hide EligibleToDefer and BenefeficiariesAtEnrollment
    private int numberAutoEnrollments; // number of 'Auto' and 'Was Auto Enroll' 
	private int numberOfRothParticipants;	//Total number of Roth participants
		
	public ParticipantEnrollmentSummaryReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
	}

	/**
	 * Gets the contractNumber
	 * @return Returns a String
	 */
	public int getContractNumber() {
		return contractNumber;
	}

	/**
	 * Sets the contractNumber
	 * @param contractNumber The contractNumber to set
	 */
	public void setContractNumber(int contractNumber) {
		this.contractNumber = contractNumber;
	}

	/**
	 * Gets the fromDate
	 * @return Returns a Date
	 */
	public Date getFromDate() {
		return fromDate;
	}

	/**
	 * Sets the fromDate
	 * @param fromDate The fromDate to set
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * Gets the toDate
	 * @return Returns a Date
	 */
	public Date getToDate() {
		return toDate;
	}

	/**
	 * Sets the toDate
	 * @param toDate The toDate to set
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
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
	 * Gets the numberParticipantsWithEnrollments
	 * @return Returns an int
	 */
	public int getNumberParticipantsWithEnrollments() {
		return numberParticipantsWithEnrollments;
	}

	/**
	 * Sets the numberParticipantsWithEnrollments
	 * @param numberParticipantsWithEnrollments The numberParticipantsWithEnrollments to set
	 */
	public void setNumberParticipantsWithEnrollments(int numberParticipantsWithEnrollments) {
		this.numberParticipantsWithEnrollments = numberParticipantsWithEnrollments;
	}
	
	/**
	 * Gets the nonEmptyPayrollCount
	 * @return Returns a int
	 */
	public int getNonEmptyPayrollCount() {
		return nonEmptyPayrollCount;
	}

	/**
	 * Sets the nonEmptyPayrollCount
	 * @param nonEmptyPayrollCount The nonEmptyPayrollCount to set
	 */
	public void setNonEmptyPayrollCount(int nonEmptyPayrollCount) {
		this.nonEmptyPayrollCount = nonEmptyPayrollCount;
	}
	
	/**
	 * Gets the nonEmptyOrganizationUnitCount
	 * @return Returns a int
	 */
	public int getNonEmptyOrganizationUnitCount() {
		return nonEmptyOrganizationUnitCount;
	}

	/**
	 * Sets the nonEmptyOrganizationUnitCount
	 * @param nonEmptyOrganizationUnitCount The nonEmptyOrganizationUnitCount to set
	 */
	public void setNonEmptyOrganizationUnitCount(int nonEmptyOrganizationUnitCount) {
		this.nonEmptyOrganizationUnitCount = nonEmptyOrganizationUnitCount;
	}


	/**
	 * Gets the numberInternetEnrollments
	 * @return Returns a int
	 */
	public int getNumberInternetEnrollments() {
		return numberInternetEnrollments;
	}

	/**
	 * Sets the numberInternetEnrollments
	 * @param numberInternetEnrollments The numberInternetEnrollments to set
	 */
	public void setNumberInternetEnrollments(int numberInternetEnrollments) {
		this.numberInternetEnrollments = numberInternetEnrollments;
	}   
    
    /**
     * Gets numberAutoEnrollments
     * @return int
     */
	public int getNumberAutoEnrollments() {
        return numberAutoEnrollments;
    }
	
    /**
     * Sets numberAutoEnrollments
     * @param numberAutoEnrollments
     */
    public void setNumberAutoEnrollments(int numberAutoEnrollments) {
        this.numberAutoEnrollments = numberAutoEnrollments;
    }

	public String toString() {
		StringBuffer dump = new StringBuffer();
		dump.append( super.toString() ).append("\n");
		dump.append("contractNmber: ").append(getContractNumber()).append("\n");
		dump.append("fromDate: ").append(getFromDate()).append("\n");
		dump.append("toDate: ").append(getToDate()).append("\n");
		dump.append("lastName: ").append(getLastName()).append("\n");
		dump.append("ssn: ").append(getSsn()).append("\n");		
		return dump.toString();
	}	
	/**
	 * @return Returns the numberOfRothParticipants.
	 */
	public int getNumberOfRothParticipants() {
		return numberOfRothParticipants;
	}
	/**
	 * @param numberOfRothParticipants The numberOfRothParticipants to set.
	 */
	public void setNumberOfRothParticipants(int numberOfRothParticipants) {
		this.numberOfRothParticipants = numberOfRothParticipants;
	}
}
