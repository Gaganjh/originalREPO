package com.manulife.pension.ps.service.report.census.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * This Value Object class is used to define all the required attributes for
 * EligibilityIssues Report
 * 
 * @author Saravanan Narayanasamy
 * 
 */
public class EligibilityIssuesReportVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal profileId;
	private int contractId;
	private String lastName;
	private String firstName;
	private String middleName;
	private String ssn;
	private String employeeId;
	private String division;
	private String eligibilityIndicator;
	private String moneyTypes;
	private Date planEntryDate;
	private String transactionType;
	private String enrollmentMethod;
	private Date enrollmentEffectiveDate;
	private Date transactionEffectiveDate;
	private Date transactionDate;
	private Date enrollmentProcessedDate;
	private Date payrollApplicableDate;
	private int submissionNumber;
	private long transactionId;

	public BigDecimal getProfileId() {
		return profileId;
	}

	public void setProfileId(BigDecimal profileId) {
		this.profileId = profileId;
	}

	public int getContractId() {
		return contractId;
	}

	public void setContractId(int contractId) {
		this.contractId = contractId;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getEligibilityIndicator() {
		return eligibilityIndicator;
	}

	public void setEligibilityIndicator(String eligibilityIndicator) {
		this.eligibilityIndicator = eligibilityIndicator;
	}

	public String getMoneyTypes() {
		return moneyTypes;
	}

	public void setMoneyTypes(String moneyTypes) {
		this.moneyTypes = moneyTypes;
	}

	public Date getPlanEntryDate() {
		return planEntryDate;
	}

	public void setPlanEntryDate(Date planEntryDate) {
		this.planEntryDate = planEntryDate;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getEnrollmentMethod() {
		return enrollmentMethod;
	}

	public void setEnrollmentMethod(String enrollmentMethod) {
		this.enrollmentMethod = enrollmentMethod;
	}

	public Date getEnrollmentEffectiveDate() {
		return enrollmentEffectiveDate;
	}

	public void setEnrollmentEffectiveDate(Date enrollmentEffectiveDate) {
		this.enrollmentEffectiveDate = enrollmentEffectiveDate;
	}
	
	public Date getTransactionDate(){
		return transactionDate;
	}
	
	public void setTransactionDate(Date transactionDate){
		this.transactionDate = transactionDate;
	}

	public Date getTransactionEffectiveDate() {
		return transactionEffectiveDate;
	}

	public void setTransactionEffectiveDate(Date transactionDate) {
		this.transactionEffectiveDate = transactionDate;
	}

	public Date getPayrollApplicableDate() {
		return payrollApplicableDate;
	}

	public void setPayrollApplicableDate(Date payrollApplicableDate) {
		this.payrollApplicableDate = payrollApplicableDate;
	}

	public int getSubmissionNumber() {
		return submissionNumber;
	}

	public void setSubmissionNumber(int submissionNumber) {
		this.submissionNumber = submissionNumber;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public Date getEnrollmentProcessedDate() {
		return enrollmentProcessedDate;
	}

	public void setEnrollmentProcessedDate(Date enrollmentProcessedDate) {
		this.enrollmentProcessedDate = enrollmentProcessedDate;
	}

}
