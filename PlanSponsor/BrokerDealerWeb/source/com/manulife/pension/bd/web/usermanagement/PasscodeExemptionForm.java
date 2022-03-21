package com.manulife.pension.bd.web.usermanagement;

import com.manulife.pension.platform.web.controller.AutoForm;

public class PasscodeExemptionForm extends AutoForm {
	
	private static final long serialVersionUID = 1L;
	private String firstName;
	private String lastName;
	private String email;
	private String exemptionType;
	private String exemptionRequestedBy;
	private String ppmTicket;
	private String exemptTimeStamp;
	private String exemptProccessedBy;
	private String exemptProccessedByName;
	private String exemptionStatusCode;
	private String userName;
	private String exemptionReason;
	private String exemptRequestedName;
	private String userRole;
	private long userProfileId;
	
	public static final String EXEMPTINFO_KEY = "EXEMPTINFO_KEY";
	public static final String FIELD_EXEMPTION_REASON = "exemptionReason";
	
	public static final String BUTTON_LABEL_FINISH = "finish";
	
	public String getExemptionType() {
		return exemptionType;
	}

	public void setExemptionType(String exemptionType) {
		this.exemptionType = exemptionType;
	}

	public String getExemptionReason() {
		return exemptionReason;
	}

	public void setExemptionReason(String exemptionReason) {
		this.exemptionReason = exemptionReason;
	}

	public String getExemptionRequestedBy() {
		return exemptionRequestedBy;
	}

	public void setExemptionRequestedBy(String exemptionRequestedBy) {
		this.exemptionRequestedBy = exemptionRequestedBy;
	}

	public String getExemptProccessedByName() {
		return exemptProccessedByName;
	}

	public void setExemptProccessedByName(String exemptProccessedByName) {
		this.exemptProccessedByName = exemptProccessedByName;
	}

	public String getPpmTicket() {
		return ppmTicket;
	}

	public void setPpmTicket(String ppmTicket) {
		this.ppmTicket = ppmTicket;
	}

	public String getExemptProccessedBy() {
		return exemptProccessedBy;
	}

	public void setExemptProccessedBy(String exemptProccessedBy) {
		this.exemptProccessedBy = exemptProccessedBy;
	}

	public String getExemptTimeStamp() {
		return exemptTimeStamp;
	}

	public void setExemptTimeStamp(String exemptTimeStamp) {
		this.exemptTimeStamp = exemptTimeStamp;
	}

	public String getExemptRequestedName() {
		return exemptRequestedName;
	}

	public void setExemptRequestedName(String exemptRequestedName) {
		this.exemptRequestedName = exemptRequestedName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getExemptionStatusCode() {
		return exemptionStatusCode;
	}

	public void setExemptionStatusCode(String exemptionStatusCode) {
		this.exemptionStatusCode = exemptionStatusCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
		
	public long getUserProfileId() {
		return userProfileId;
	}

	public void setUserProfileId(long userProfileId) {
		this.userProfileId = userProfileId;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public void resetForm(){
		
		this.ppmTicket = null;
		this.exemptRequestedName = null;
		this.exemptionReason = null;
		this.exemptProccessedByName = null;
		this.exemptionStatusCode = null;
				
	}
}
