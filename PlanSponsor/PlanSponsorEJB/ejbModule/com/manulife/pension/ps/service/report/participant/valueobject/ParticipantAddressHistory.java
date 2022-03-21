package com.manulife.pension.ps.service.report.participant.valueobject;

import java.io.Serializable;
import java.util.Date;

import com.manulife.pension.ps.service.submission.valueobject.Address;
import com.manulife.pension.util.StaticHelperClass;

/**
 * Holds result data from stored proc call to get address history list.
 * 
 * @author Glen Lalonde
 *
 */
public class ParticipantAddressHistory extends Address implements Serializable {
	private static final long serialVersionUID = 1L; 

	private String profileId;
	private String firstName;
	private String middleInitial;
	private String lastName;

	// history
	private Date updateDate;
	private String changedByUserId;
	private String source;
	private String userTypeCode;
	private String changedByFirstName;
	private String changedByLastName;
	private String division;
	private boolean accountHolder;

	public ParticipantAddressHistory() {}

	public ParticipantAddressHistory(String profileId,
							  String firstName,
							  String middleInitial,
							  String lastName,
							  String employerProvidedEmailAddress,
							  String address1,
							  String address2,
							  String city,
							  String stateCode,
							  String zipCode,
							  String country,
							  String ssn,
							  String employeeId,
							  Date updateDate,
							  String changedByUserId,
							  String changedByFirstName,
							  String changedByLastName,
							  String source,
							  String userTypeCode,
							  String division,
							  boolean accountHolderInd)
	{
		this.profileId = profileId;
		this.firstName = firstName.trim();
		this.middleInitial = middleInitial;
		this.lastName = lastName.trim();
		this.employerProvidedEmailAddress = employerProvidedEmailAddress;
		this.addressLine1 = address1;
		this.addressLine2 = address2;
		this.city = city;
		this.stateCode = stateCode;
		this.zipCode = zipCode;
		this.country = country;
		this.ssn = ssn;
		this.empId = employeeId;
		this.updateDate = updateDate;
		this.changedByUserId = changedByUserId;
		this.changedByLastName = changedByLastName;
		this.changedByFirstName = changedByFirstName;
		this.source = source;
		this.userTypeCode = userTypeCode;
		this.division = division;
		this.accountHolder = accountHolderInd;
	}

	/**
	 * Gets the profileId
	 * @return Returns a String
	 */
	public String getProfileId() {
		return profileId;
	}

	public boolean isAccountHolder() {
		return this.accountHolder;
	}
	
	
	/**
	 * Gets the middleInitial
	 * @return Returns a String
	 */
	public String getMiddleInitial() {
		return middleInitial;
	}

	/**
	 * Gets the firstName
	 * @return Returns a String
	 */
	public String getFirstName() {
		return firstName;
	}


	/**
	 * Gets the lastName
	 * @return Returns a String
	 */
	public String getLastName() {
		return lastName;
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
	 * Little formatting here to render it the way I want.
	 * The render:zip tag in jsp was leaving a - after the 5 char version
	 * of the zip, which I think looks bad, Spec also does not show this -.
	 * 
	 * Only need the - for USA, other don't like a mangled zip.
	 */
	public String getZipCode() {
		String rawZip = super.getZipCode();
		if (rawZip==null) return "";
		rawZip=rawZip.trim(); 
		if ("USA".equalsIgnoreCase(this.getCountry().trim())) {
			if (rawZip.length()>5) {
				return rawZip.substring(0, 5)+"-"+rawZip.substring(5);
			} else {
				return rawZip;
			}
		} else {
			return rawZip;
		}
	}
	

	/**
	 * used to get the hover over text for the jsp
	 */ 
	public String getHoverText(boolean isInternalUser) {
		if (isInternalUser) {
			return this.changedByLastName+", " + this.changedByFirstName;
		} else {
			return "";
		}
	}


	public String getAddressLine2NotNull() {
		String data = super.getAddressLine2();
		if (data==null) {
			return "";
		} else {
			return data;
		}
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.service.submission.valueobject.Address#getFullName()
	 */
	public String getFullName() {
		String fullName = "";
		fullName = lastName + ", " + firstName;
		return fullName;
	}

	public String getUserTypeCode() {
		return userTypeCode;
	}

	public void setUserTypeCode(String userTypeCode) {
		this.userTypeCode = userTypeCode;
	}

	public String getChangedByLastName() {
		return changedByLastName;
	}

	public String getChangedByFirstName() {
		return changedByFirstName;
	}

	public String getChangedByUserId() {
		return changedByUserId;
	}

	public String getDivision() {
		return division;
	}
	
	public String getStateCode() {
		if (this.stateCode == null) {
			return "";
		} else {
			return this.stateCode;
		}
	}

	public String toString() {
	  	return StaticHelperClass.toString(this);
	}
	
	public String getEmployerProvidedEmailAddressNotNull() {
		String data = super.getEmployerProvidedEmailAddress();
		if (data==null) {
			return "";
		} else {
			return data;
		}
	}

}
