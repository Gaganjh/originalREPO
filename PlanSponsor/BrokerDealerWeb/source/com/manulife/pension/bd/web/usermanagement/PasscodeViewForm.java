package com.manulife.pension.bd.web.usermanagement;

import java.text.SimpleDateFormat;
import java.util.List;

import com.manulife.pension.bd.web.userprofile.BDUserRoleDisplayNameUtil;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;
import com.manulife.pension.service.broker.valueobject.ProducerCodeInfo;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBDExtUserProfile;
import com.manulife.pension.service.security.passcode.UserPasscodeDetailedInfo;

public class PasscodeViewForm extends AutoForm{
	
	private static final long serialVersionUID = 1L;
	
	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss:a 'ET'"); 
	private String lastPasscodeTimestamp;
	private String lastPasscodeDestination;
	private String lastPasscodeSuccess;
	private String lastPasscodeFailure;
	private String passcodeStatus;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String userRole;
	private String parentUserName;
	private boolean disabled = false;
	private boolean success = false;
	private boolean resetButton= false;


	


	public void populate(ExtendedBDExtUserProfile user, UserPasscodeDetailedInfo details) {
		firstName = user.getFirstName();
		lastName = user.getLastName();
		emailAddress = user.getCommunicationEmailAddress();
		userRole = BDUserRoleDisplayNameUtil.getInstance().getDisplayName(user.getRoleType());

		lastPasscodeTimestamp = null != details.getLastPasscodeEmailSent()
				? DATE_FORMATTER.format(details.getLastPasscodeEmailSent())
				: "";
		lastPasscodeDestination = details.getLastPasscodeEmailDestination();

		lastPasscodeSuccess = null != details.getLastPasscodeSuccess()
				? DATE_FORMATTER.format(details.getLastPasscodeSuccess())
				: "";
		lastPasscodeFailure = null != details.getLastPasscodeFail()
				? DATE_FORMATTER.format(details.getLastPasscodeFail())
				: "";

		passcodeStatus = details.isPasscodeLocked() ? "Locked" : details.isPasscodeCooling() ? "Cooling period" : "Active";
	    resetButton= details.isPasscodeLocked() ? true : details.isPasscodeCooling() ? true : false ;
		 
	}
	
	public String getLastPasscodeTimestamp() {
		return lastPasscodeTimestamp;
	}

	public void setLastPasscodeTimestamp(String lastPasscodeTimestamp) {
		this.lastPasscodeTimestamp = lastPasscodeTimestamp;
	}

	public String getLastPasscodeDestination() {
		return lastPasscodeDestination;
	}

	public void setLastPasscodeDestination(String lastPasscodeDestination) {
		this.lastPasscodeDestination = lastPasscodeDestination;
	}

	public String getLastPasscodeSuccess() {
		return lastPasscodeSuccess;
	}

	public void setLastPasscodeSuccess(String lastPasscodeSuccess) {
		this.lastPasscodeSuccess = lastPasscodeSuccess;
	}

	public String getLastPasscodeFailure() {
		return lastPasscodeFailure;
	}

	public void setLastPasscodeFailure(String lastPasscodeFailure) {
		this.lastPasscodeFailure = lastPasscodeFailure;
	}

	public String getPasscodeStatus() {
		return passcodeStatus;
	}

	public void setPasscodeStatus(String passcodeStatus) {
		this.passcodeStatus = passcodeStatus;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getUserRole() {
		return userRole;
	}

	public String getParentUserName() {
		return parentUserName;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	public boolean isResetButton() {
		return resetButton;
	}

	public void setResetButton(boolean resetButton) {
		this.resetButton = resetButton;
	}

	}
