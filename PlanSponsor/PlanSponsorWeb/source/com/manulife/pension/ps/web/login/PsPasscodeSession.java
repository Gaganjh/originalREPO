package com.manulife.pension.ps.web.login;

import java.util.Set;
import java.util.Date;

import com.manulife.pension.platform.web.passcode.EmailAddressMaskFormat;
import com.manulife.pension.platform.web.passcode.MobileMask;
import com.manulife.pension.platform.web.passcode.PasscodeSession;
import com.manulife.pension.service.security.passcode.PasscodeChannel;
import com.manulife.pension.service.security.valueobject.LoginPSValueObject;

class PsPasscodeSession
implements PasscodeSession<LoginPSValueObject> {
    
    private static final long serialVersionUID = 1L;
    private static final EmailAddressMaskFormat MASK_FORMAT = new EmailAddressMaskFormat("the primary email address on your profile");
    private static final MobileMask MOBILE_MASK_FORMAT = new MobileMask();
    private final LoginPSValueObject loginVo;
    private String fromAddress;
    private String createdTimeStamp;
	private String expriredTimeStamp;
	private int resendAttemptCount = 0;
	private boolean resendFlag;
	private Date coolingTimeStamp;
	private String devicePrint;
	private PasscodeChannel passcodeChannel;
	private boolean isPasswordChangeOrUpdateRequired;
    
    PsPasscodeSession(final LoginPSValueObject loginVo) {
        this.loginVo = loginVo;
    }
    
    @Override
    public LoginPSValueObject getLoginInformation() { return loginVo; }
    
    @Override
    public String getUserName() { return loginVo.getPrincipal().getUserName(); }
    
    @Override
    public long getProfileId() { return loginVo.getPrincipal().getProfileId(); }
    
    @Override
    public String getRecipientEmail() { return loginVo.getEmail(); }
    
    @Override
    public String getFromAddress() { return fromAddress; }
    @Override
    public void setFromAddress(final String fromAddress) { this.fromAddress = fromAddress; }
    
    @Override
    public String getMaskedRecipientEmail() { return MASK_FORMAT.format(loginVo.getEmail()); }

    @Override
    public String getCreatedTimeStamp() { return createdTimeStamp;}  
    
    @Override
    public void setCreatedTimeStamp(final String createdTimeStamp) { this.createdTimeStamp = createdTimeStamp; }

    @Override
	public String getExpriredTimeStamp() { return expriredTimeStamp; }

    @Override
	public void setExpriredTimeStamp(final String expriredTimeStamp) { this.expriredTimeStamp = expriredTimeStamp; }
	
    @Override
	public int getResendAttemptCount() { return resendAttemptCount; }

    @Override
	public void setResendAttemptCount(final int resendAttemptCount) { this.resendAttemptCount = resendAttemptCount; }
	
    @Override
	public boolean getResendFlag() { return resendFlag; }

    @Override
	public void setResendFlag(final boolean resendFlag) { this.resendFlag = resendFlag; }

	@Override
	public void setCoolingTimeStamp(Date coolingTimeStamp) {this.coolingTimeStamp = coolingTimeStamp; }

	@Override
	public Date getCoolingTimeStamp() {return coolingTimeStamp;}
	
	@Override
	public String getDevicePrint() { return devicePrint;}

	@Override
	public String getMaskedRecipientMobile() {
		
	return MobileMask.maskPhone(loginVo.getMobileNumber().toString());
		
	}
	
	@Override
	public String getMaskedRecipientPhone() {
		
	return MobileMask.maskPhone(loginVo.getPhoneNumber().toString());
		
	}

	@Override
	public PasscodeChannel getPasscodeChannel() {
		return passcodeChannel;
	}

	@Override
	public void setPasscodeChannel(PasscodeChannel channel) {
		this.passcodeChannel = channel;
		
	}

	@Override
	public String getRecipientMobile() {

		return loginVo.getMobileNumber() != null ? loginVo.getMobileNumber().toString() : "";

	}
	
	@Override
	public String getRecipientPhone() {
		return loginVo.getPhoneNumber() != null ? loginVo.getPhoneNumber().toString() : "";
	}

	@Override
	public Set<Integer> getContracts() {
		return loginVo.getAccessibleContracts();
	}

	@Override
	public Set<Integer> getTpaFirms() {
		return loginVo.getTpaFirms();
	}

	public boolean isPasswordChangeOrUpdateRequired() {
		return isPasswordChangeOrUpdateRequired;
	}

	public void setPasswordChangeOrUpdateRequired(boolean isPasswordChangeOrUpdateRequired) {
		this.isPasswordChangeOrUpdateRequired = isPasswordChangeOrUpdateRequired;
	}

	
	
	
	
	    
}
