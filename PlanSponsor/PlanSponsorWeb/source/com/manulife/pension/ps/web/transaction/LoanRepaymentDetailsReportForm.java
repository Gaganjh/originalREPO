package com.manulife.pension.ps.web.transaction;

import com.manulife.pension.ps.web.controller.PsForm;

public class LoanRepaymentDetailsReportForm extends PsForm {

	private String profileId;
	private String maskedSsn;
	private String ssn;
	private String name;
	private String loanNumber;
	private String participantId;


	
	/**
	 * Gets the profileId
	 * @return Returns a String
	 */
	public String getProfileId() {
		return profileId;
	}
	/**
	 * Sets the profileId
	 * @param profileId The profileId to set
	 */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}


	/**
	 * Gets the maskedSsn
	 * @return Returns a String
	 */
	public String getMaskedSsn() {
		return maskedSsn;
	}
	/**
	 * Sets the maskedSsn
	 * @param maskedSsn The maskedSsn to set
	 */
	public void setMaskedSsn(String maskedSsn) {
		this.maskedSsn = maskedSsn;
	}


	/**
	 * Gets the name
	 * @return Returns a String
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets the name
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * Gets the loanNumber
	 * @return Returns a String
	 */
	public String getLoanNumber() {
		return loanNumber;
	}
	/**
	 * Sets the loanNumber
	 * @param loanNumber The loanNumber to set
	 */
	public void setLoanNumber(String loanNumber) {
		this.loanNumber = loanNumber;
	}


	/**
	 * Gets PArticipant Id
	 * @return String
	 */
	public String getParticipantId() {
		return participantId;
	}

	/**
	 * Sets Participant Id
	 * @param string
	 */
	public void setParticipantId(String string) {
		participantId = string;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getSsn() {
		return ssn;
	}

}
