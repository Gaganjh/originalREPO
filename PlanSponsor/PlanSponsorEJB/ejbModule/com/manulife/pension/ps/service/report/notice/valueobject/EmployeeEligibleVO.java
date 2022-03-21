package com.manulife.pension.ps.service.report.notice.valueobject;

import java.io.Serializable;
import java.util.Date;


public class EmployeeEligibleVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String particiapantFirstName =null;
	private String particiapantLastName =null;
	private String particiapantAddressLine1=null;
	private String particiapantAddressLine2=null;
	private String particiapantCity=null;
	private String particiapantState = null;
	private String particiapantZip=null;
	private String particiapantCountry=null;
	private String employeeStatus=null;
	private String accountBalance=null;
	private String eligibilityIndicator=null;
	private Date planEntryDate=null;
	private String accountHolderFlag=null;
	
	public String getParticiapantFirstName() {
		return particiapantFirstName;
	}
	public void setParticiapantFirstName(String particiapantFirstName) {
		this.particiapantFirstName = particiapantFirstName;
	}
	public String getParticiapantLastName() {
		return particiapantLastName;
	}
	public void setParticiapantLastName(String particiapantLastName) {
		this.particiapantLastName = particiapantLastName;
	}
	public String getParticiapantAddressLine1() {
		return particiapantAddressLine1;
	}
	public void setParticiapantAddressLine1(String particiapantAddressLine1) {
		this.particiapantAddressLine1 = particiapantAddressLine1;
	}
	public String getParticiapantAddressLine2() {
		return particiapantAddressLine2;
	}
	public void setParticiapantAddressLine2(String particiapantAddressLine2) {
		this.particiapantAddressLine2 = particiapantAddressLine2;
	}
	public String getParticiapantCity() {
		return particiapantCity;
	}
	public void setParticiapantCity(String particiapantCity) {
		this.particiapantCity = particiapantCity;
	}
	public String getParticiapantState() {
		return particiapantState;
	}
	public void setParticiapantState(String particiapantState) {
		this.particiapantState = particiapantState;
	}
	public String getParticiapantZip() {
		return particiapantZip;
	}
	public void setParticiapantZip(String particiapantZip) {
		this.particiapantZip = particiapantZip;
	}
	public String getParticiapantCountry() {
		return particiapantCountry;
	}
	public void setParticiapantCountry(String particiapantCountry) {
		this.particiapantCountry = particiapantCountry;
	}
	public String getEmployeeStatus() {
		return employeeStatus;
	}
	public void setEmployeeStatus(String employeeStatus) {
		this.employeeStatus = employeeStatus;
	}
	public String getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(String accountBalance) {
		this.accountBalance = accountBalance;
	}
	public String getEligibilityIndicator() {
		return eligibilityIndicator;
	}
	public void setEligibilityIndicator(String eligibilityIndicator) {
		this.eligibilityIndicator = eligibilityIndicator;
	}
	public Date getPlanEntryDate() {
		return planEntryDate;
	}
	public void setPlanEntryDate(Date planEntryDate) {
		this.planEntryDate = planEntryDate;
	}
	public String getAccountHolderFlag() {
		return accountHolderFlag;
	}
	public void setAccountHolderFlag(String accountHolderFlag) {
		this.accountHolderFlag = accountHolderFlag;
	}
	
	
	
}
