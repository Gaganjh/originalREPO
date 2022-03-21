package com.manulife.pension.ps.service.report.participant.valueobject;

import java.io.Serializable;
import java.util.Date;

import com.manulife.pension.ps.service.submission.valueobject.Address;

public class ParticipantAddress extends Address implements Serializable {
	
	private String profileId;
	private String firstName;
	private String lastName;
    private String status;
    private String division;
    private boolean participantIndicator;
	
	public ParticipantAddress() {}
	
	public ParticipantAddress(String profileId,
							  String firstName,
							  String lastName, 
							  String address1, 
							  String address2,
							  String city, 
							  String stateCode, 
							  String zipCode, 
							  String country, 
							  String ssn,
                              String division,
                              String status,
                              boolean participantIndicator )
	{
		this.profileId = profileId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.addressLine1 = address1;
		this.addressLine2 = address2;
		this.city = city;
		this.stateCode = stateCode;
		this.zipCode = zipCode;
		this.country = country;
		this.ssn = ssn;
        this.division = division;
        this.status = status;
        this.participantIndicator = participantIndicator;
	}
	
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
	 * Gets the firstName
	 * @return Returns a String
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * Sets the firstName
	 * @param firstName The firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
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
	 * Gets the employeeId
	 * @return Returns a String
	 * @deprecated replaced by getEmpId()
	 */
	public String getEmployeeId() {
		return this.empId;
	}
	/**
	 * Sets the employeeId
	 * @param employeeId The employeeId to set
	 * @deprecated replaced by setEmpId()
	 */
	public void setEmployeeId(String employeeId) {
		this.empId = employeeId;
	}

	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.service.submission.valueobject.Address#getFullName()
	 */
	public String getFullName() {
		String fullName = "";
		fullName = lastName + ", " + firstName;
		return fullName;
	}

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isParticipantIndicator() {
        return participantIndicator;
    }

    public void setParticipantIndicator(boolean participantIndicator) {
        this.participantIndicator = participantIndicator;
    }

	
	// TODO: toString...
}

