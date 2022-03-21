package com.manulife.pension.ps.service.submission.valueobject;

import java.io.Serializable;
import java.util.List;

public class AddableParticipantList implements Serializable {
	
	private String participantSortOption;
	private String systemStatus;
	private Integer submissionId;
	private List addableParticipants; 	
	
	public AddableParticipantList() {
	}
	
	public AddableParticipantList(String participantSortOption, String systemStatus, List addableParticipants,
			Integer submissionId) {
		this.participantSortOption = participantSortOption;
		this.systemStatus = systemStatus;
		this.addableParticipants = addableParticipants;
		this.submissionId = submissionId;
	}
	
	/**
	 * @return Returns the participantSortOption.
	 */
	public String getParticipantSortOption() {
		return participantSortOption;
	}

	/**
	 * @return Returns the addableParticipants.
	 */
	public List getAddableParticipants() {
		return addableParticipants;
	}
	
	/**
	 * @return Returns the system status.
	 */
	public String getSystemStatus() {
		return this.systemStatus;
	}
	
	public Integer getSubmissionId() {
		return this.submissionId;
	}
}
