/**
 * 
 */
package com.manulife.pension.ps.service.report.participant.valueobject;

import java.io.Serializable;

/**
 * @author arugupu
 *
 */
public class ParticipantStatementItem implements Serializable {
	/**
	 * Default serialVersionUID has been defined  
	 */
	private static final long serialVersionUID = 1L;

	private String firstName;
	private String lastName;
	private String ssn;
	private String profileId;

	public ParticipantStatementItem() {}

	public ParticipantStatementItem(
			String firstName,
			String lastName, 
			String ssn,
			String profileId){
		this.firstName = firstName;
		this.lastName = lastName;		
		this.ssn = ssn;
		this.setProfileId(profileId);

	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the ssn
	 */
	public String getSsn() {
		return ssn;
	}
	/**
	 * @param ssn the ssn to set
	 */
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	/**
	 * @param profileId the profileId to set
	 */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	/**
	 * @return the profileId
	 */
	public String getProfileId() {
		return profileId;
	}



}
