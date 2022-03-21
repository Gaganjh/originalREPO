package com.manulife.pension.platform.web.passcode;

import com.manulife.pension.platform.web.controller.BaseForm;
import com.manulife.pension.service.security.passcode.PasscodeDeliveryPreference;
public class PasscodeTransitionForm extends BaseForm{
	
	private static final long serialVersionUID = 1L;
	private PasscodeDeliveryPreference passcodeDeliveryPreference;
	private String maskedMobile;
	private String maskedPhone;
	private String maskedEmail;
	private boolean smsSwithOn = false;
	private boolean voiceSwithOn = false;
	private boolean emailSwithOn = false;
	
	public PasscodeDeliveryPreference getPasscodeDeliveryPreference() {
		return passcodeDeliveryPreference;
	}
	public void setPasscodeDeliveryPreference(PasscodeDeliveryPreference passcodeDeliveryPreference) {
		this.passcodeDeliveryPreference = passcodeDeliveryPreference;
	}
	public String getMaskedMobile() {
		return maskedMobile;
	}
	public void setMaskedMobile(String maskedMobile) {
		this.maskedMobile = maskedMobile;
	}
	public String getMaskedPhone() {
		return maskedPhone;
	}
	public void setMaskedPhone(String maskedPhone) {
		this.maskedPhone = maskedPhone;
	}
	public String getMaskedEmail() {
		return maskedEmail;
	}
	public void setMaskedEmail(String maskedEmail) {
		this.maskedEmail = maskedEmail;
	}
	public boolean isSmsSwithOn() {
		return smsSwithOn;
	}
	public void setSmsSwithOn(boolean smsSwithOn) {
		this.smsSwithOn = smsSwithOn;
	}
	public boolean isVoiceSwithOn() {
		return voiceSwithOn;
	}
	public void setVoiceSwithOn(boolean voiceSwithOn) {
		this.voiceSwithOn = voiceSwithOn;
	}
	public boolean isEmailSwithOn() {
		return emailSwithOn;
	}
	public void setEmailSwithOn(boolean emailSwithOn) {
		this.emailSwithOn = emailSwithOn;
	}
}
