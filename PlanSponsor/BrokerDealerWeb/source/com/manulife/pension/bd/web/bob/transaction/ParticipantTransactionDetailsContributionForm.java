package com.manulife.pension.bd.web.bob.transaction;

import com.manulife.pension.platform.web.report.BaseReportForm;

/**
 * This is the Action Form for Participant Transaction Details Contribution page.
 * 
 * @author harlomte
 * 
 */
public class ParticipantTransactionDetailsContributionForm extends BaseReportForm {

	public static final String application = "PS";
	public static final String PARAMETER_KEY_PROFILE_ID = "profileId";
	public static final String PARAMETER_KEY_PARTICIPANT_ID = "participantId";
	public static final String PARAMETER_KEY_CONTRACT_NUMBER = "contractNumber";
	public static final String PARAMETER_KEY_TRANSACTION_NUMBER = "transactionNumber";

	private String ssn;
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

}
