package com.manulife.pension.ps.web.profiles;

import java.util.HashMap;
import java.util.Map;

import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.passcode.MobileMask;

@SuppressWarnings({ "serial", "unchecked" })
public class ManagePasscodeExemptionForm extends AutoForm {
	
	private String exemptionType;
	private String exemptionReason;
	private String exemptionRequestedBy;
	private String ppmTicket;
	private String exemptTimeStamp;
	private String exemptRequestedName;
	private String exemptProccessedBy;
	private String exemptProccessedByName;
	private String exemptionStatusCode;
	private String userName;
	private boolean fromTPAContactsTab;
	private boolean fromPSContactTab;
	private String email;
	private String firstName;
	private String lastName;
	private long userProfileId;
	private String userRole;
	private String mobile;
//	private String mobilePattern;
	private String actionLabel;
	public static final String EXEMPTINFO_KEY = "EXEMPTINFO_KEY";
	public static final String FIELD_EXEMPTION_REASON = "exemptionReason";
	public static final String FIELD_PPMTICKET = "ppmTicket";
	public static final String FIELD_EXEMPTION_REQUESTEDBY = "exemptionRequestedBy";
	public static final String TEMPORARY = "TEMP";
	
	
	private static final Map ACTION_LABEL_MAP = new HashMap();

	/*
	 * Maps the button label to the corresponding action.
	 */
	public static final String BUTTON_LABEL_BACK = "back";
	public static final String BUTTON_LABEL_FINISH = "finish";
	public static final String BUTTON_LABEL_EXEMPT="exempt";
	public static final String BUTTON_LABEL_REMOVE_EXEMPT="remove";
	static {
		ACTION_LABEL_MAP.put(BUTTON_LABEL_BACK, "back");
		ACTION_LABEL_MAP.put(BUTTON_LABEL_FINISH, "finish");
		ACTION_LABEL_MAP.put(BUTTON_LABEL_EXEMPT, "exempt");
		ACTION_LABEL_MAP.put(BUTTON_LABEL_REMOVE_EXEMPT, "removeExempt");
	}
	
	
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
	public boolean isFromTPAContactsTab() {
		return fromTPAContactsTab;
	}
	public void setFromTPAContactsTab(boolean fromTPAContactsTab) {
		this.fromTPAContactsTab = fromTPAContactsTab;
	}
	public boolean isFromPSContactTab() {
		return fromPSContactTab;
	}
	public void setFromPSContactTab(boolean fromPSContactTab) {
		this.fromPSContactTab = fromPSContactTab;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
//	public String getMobilePattern() {
//		return mobilePattern;
//	}
//	public void setMobilePattern(String mobilePattern) {
//		this.mobilePattern = MobileMask.mask(mobilePattern);
//	}
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
	public String getExemptionStatusCode() {
		return exemptionStatusCode;
	}
	public void setExemptionStatusCode(String exemptionStatusCode) {
		this.exemptionStatusCode = exemptionStatusCode;
	}
	public void setActionLabel(String actionLabel) {
		this.actionLabel = actionLabel;
		setAction((String) ACTION_LABEL_MAP.get(actionLabel));
	}

	public String getAction() {
		if (super.getAction().length() == 0 && actionLabel != null
				&& actionLabel.length() > 0) {
			setAction((String) ACTION_LABEL_MAP.get(actionLabel));
		}
		return super.getAction();
	}
	
	public void resetForm(){
		
		this.ppmTicket = null;
		this.exemptRequestedName = null;
		this.exemptionReason = null;
		this.exemptProccessedByName = null;
		this.exemptionStatusCode = null;
		fromTPAContactsTab = false;
		fromPSContactTab = false;
		
	}
}