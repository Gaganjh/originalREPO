package com.manulife.pension.ps.web.transaction;

import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.service.report.participant.transaction.valueobject.TransactionDetailsContributionReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;

public class ParticipantTransactionDetailsContributionForm extends ReportForm {

	public static final String application = "PS";
	public static final String PARAMETER_KEY_PROFILE_ID = "profileId";
	public static final String PARAMETER_KEY_PARTICIPANT_ID = "participantId";
	public static final String PARAMETER_KEY_CONTRACT_NUMBER = "contractNumber";
	public static final String PARAMETER_KEY_TRANSACTION_NUMBER = "transactionNumber";

	private String sortDirection = ReportSort.ASC_DIRECTION;
	private String sortField = TransactionDetailsContributionReportData.SORT_FIELD_RISK_CATEGORY;
	
	private String ssn;
	private String ssnUnmasked;
	private String firstName;
	private String lastName;
	private String transactionNumber;
	private String profileId;
	private String participantId;
	
	/**
	 * 
	 */
	public ParticipantTransactionDetailsContributionForm() {
		super();
	}


	public String getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}



	public String getSsn() {
		return ssn;
	}


	public void setSsn(String ssn) {
		this.ssn = ssn;
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


	public String getParticipantId() {
		return participantId;
	}


	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}


	public void setSsnUnmasked(String ssnUnmasked) {
		this.ssnUnmasked = ssnUnmasked;
	}


	public String getSsnUnmasked() {
		return ssnUnmasked;
	}

}
