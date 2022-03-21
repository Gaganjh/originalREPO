package com.manulife.pension.bd.web.login;

import java.util.Date;
import java.util.Set;

import com.manulife.pension.platform.web.passcode.EmailAddressMaskFormat;
import com.manulife.pension.platform.web.passcode.PasscodeSession;
import com.manulife.pension.service.security.bd.valueobject.BDLoginValueObject;
import com.manulife.pension.service.security.passcode.PasscodeChannel;

class BdPasscodeSession implements PasscodeSession<BDLoginValueObject> {
	
    
    private static final long serialVersionUID = 1L;
    private static final EmailAddressMaskFormat MASK_FORMAT = new EmailAddressMaskFormat("the email address we have on file");
    
	private final BDLoginValueObject loginVo;
	private String fromAddress;
	private String createdTimeStamp;
	private String expriredTimeStamp;
	private int resendAttemptCount = 0;
	private boolean resendFlag;
	private Date coolingTimeStamp;
	private PasscodeChannel passcodeChannel; 
	private boolean isPasswordChangeOrUpdateRequired;
	private String businessParamValue;

	BdPasscodeSession(final BDLoginValueObject loginVo) {
		this.loginVo = loginVo;
	}
	
	    @Override
		public BDLoginValueObject getLoginInformation() { return loginVo; }
	    
	    @Override
		public String getUserName() { return loginVo.getPrincipal().getUserName(); }
	    
	    @Override
	    public long getProfileId() { return loginVo.getPrincipal().getProfileId(); }
	    
	    @Override
	    public String getRecipientEmail() { return loginVo.getPrincipal().getEmailAddress(); } 
	    
	    @Override
	    public String getFromAddress() { return fromAddress; }
	    
	    @Override
	    public void setFromAddress(final String fromAddress) { this.fromAddress = fromAddress; }
	    
	    @Override
	    public String getMaskedRecipientEmail() { return MASK_FORMAT.format(loginVo.getPrincipal().getEmailAddress()); } 
	      
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
		public String getDevicePrint() { return loginVo.getDevicePrint();}

		@Override
		public String getRecipientMobile() {
			return null;
		}

		@Override
		public PasscodeChannel getPasscodeChannel() {
			return passcodeChannel;
		}

		@Override
		public void setPasscodeChannel(PasscodeChannel passcodeChannel) {
			this.passcodeChannel=passcodeChannel;
			
		}

		@Override
		public String getMaskedRecipientMobile() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Set<Integer> getContracts() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Set<Integer> getTpaFirms() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getRecipientPhone() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getMaskedRecipientPhone() {
			// TODO Auto-generated method stub
			return null;
		}
		
		public boolean isPasswordChangeOrUpdateRequired() {
			return isPasswordChangeOrUpdateRequired;
		}

		public void setPasswordChangeOrUpdateRequired(boolean isPasswordChangeOrUpdateRequired) {
			this.isPasswordChangeOrUpdateRequired = isPasswordChangeOrUpdateRequired;
		}
			   
		
		public String getBusinessParamValue() {
			return businessParamValue;
		}
		
		public void setBusinessParamValue(String businessParamValue) {
			this.businessParamValue = businessParamValue;
		}
}
