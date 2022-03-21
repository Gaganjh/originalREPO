package com.manulife.pension.service.loan.valueobject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.manulife.pension.common.BaseSerializableCloneableObject;

public class LoanParticipantData extends BaseSerializableCloneableObject {

	private static final long serialVersionUID = 1L;
	private String firstName;
	private String middleInitial;
	private String lastName;
	private String ssn;
	private String legallMarriedInd;
	private String employmentStatusCode;
	private Integer participantId;
	private String participantStatusCode;
	private boolean positivePbaMoneyTypeBalance;
	private Integer outstandingLoansCount;
	private BigDecimal currentOutstandingBalance;
	private BigDecimal maxBalanceLast12Months;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String stateCode;
	private String zipCode;
	private String country;
	private String employeeNumber;
	private List<Integer> pendingRequests = new ArrayList<Integer>();
	private boolean forwardUnreversedLoanTransactionExist;
	private boolean giflFeatureSelected;
	private boolean pendingWithdrawalRequestExist;

	public boolean isForwardUnreversedLoanTransactionExist() {
		return forwardUnreversedLoanTransactionExist;
	}

	public void setForwardUnreversedLoanTransactionExist(
			boolean forwardUnreversedLoanTransactionExist) {
		this.forwardUnreversedLoanTransactionExist = forwardUnreversedLoanTransactionExist;
	}

	public List<Integer> getPendingRequests() {
		return pendingRequests;
	}

	public void setPendingRequests(List<Integer> pendingRequests) {
		this.pendingRequests = pendingRequests;
	}

	public String getMiddleInitial() {
		return middleInitial;
	}

	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	public Integer getParticipantId() {
		return participantId;
	}

	public void setParticipantId(Integer participantId) {
		this.participantId = participantId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getEmploymentStatusCode() {
		return employmentStatusCode;
	}

	public void setEmploymentStatusCode(String employmentStatusCode) {
		this.employmentStatusCode = employmentStatusCode;
	}

	public String getLegallMarriedInd() {
		return legallMarriedInd;
	}

	public void setLegallMarriedInd(String legallMarriedInd) {
		this.legallMarriedInd = legallMarriedInd;
	}

	public String getParticipantStatusCode() {
		return participantStatusCode;
	}

	public void setParticipantStatusCode(String participantStatusCode) {
		this.participantStatusCode = participantStatusCode;
	}

	public boolean isPositivePbaMoneyTypeBalance() {
		return positivePbaMoneyTypeBalance;
	}

	public void setPositivePbaMoneyTypeBalance(
			boolean positivePbaMoneyTypeBalance) {
		this.positivePbaMoneyTypeBalance = positivePbaMoneyTypeBalance;
	}

	public Integer getOutstandingLoansCount() {
		return outstandingLoansCount;
	}

	public void setOutstandingLoansCount(Integer outstandingLoansCount) {
		this.outstandingLoansCount = outstandingLoansCount;
	}

	public BigDecimal getCurrentOutstandingBalance() {
		return currentOutstandingBalance;
	}

	public void setCurrentOutstandingBalance(
			BigDecimal currentOutstandingBalance) {
		this.currentOutstandingBalance = currentOutstandingBalance;
	}

	public BigDecimal getMaxBalanceLast12Months() {
		return maxBalanceLast12Months;
	}

	public void setMaxBalanceLast12Months(BigDecimal maxBalanceLast12Months) {
		this.maxBalanceLast12Months = maxBalanceLast12Months;
	}

	public boolean isGiflFeatureSelected() {
		return giflFeatureSelected;
	}

	public void setGiflFeatureSelected(boolean giflFeatureSelected) {
		this.giflFeatureSelected = giflFeatureSelected;
	}

	public boolean isPendingWithdrawalRequestExist() {
		return pendingWithdrawalRequestExist;
	}

	public void setPendingWithdrawalRequestExist(
			boolean pendingWithdrawalRequestExist) {
		this.pendingWithdrawalRequestExist = pendingWithdrawalRequestExist;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}
}
