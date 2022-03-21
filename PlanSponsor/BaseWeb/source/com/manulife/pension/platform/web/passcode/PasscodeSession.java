package com.manulife.pension.platform.web.passcode;

import java.io.Serializable;
import java.util.Set;
import java.util.Date;

import com.manulife.pension.service.security.passcode.PasscodeChannel;

public interface PasscodeSession<LoginVo>
extends Serializable {
    
    public LoginVo getLoginInformation();
    public String getUserName();
    public long getProfileId();
    public String getRecipientEmail();
    public String getRecipientMobile();
    public String getMaskedRecipientMobile();
    public String getRecipientPhone();
    public String getMaskedRecipientPhone();
    public String getFromAddress();
    public void setFromAddress(final String fromAddress);
    public String getMaskedRecipientEmail();
    public String getCreatedTimeStamp();  
    public void setCreatedTimeStamp(final String createdTimeStamp);
    public String getExpriredTimeStamp();
    public void setExpriredTimeStamp(final String expriredTimeStamp);
	public int getResendAttemptCount();
	public void setResendAttemptCount(final int resendAttemptCount);
	public boolean getResendFlag();
	public void setResendFlag(final boolean resendFlag);	
	public void setCoolingTimeStamp(final Date date);
	public Date getCoolingTimeStamp();
	public String getDevicePrint();
	public PasscodeChannel getPasscodeChannel();
	public void setPasscodeChannel(PasscodeChannel channel);
	public Set<Integer> getContracts();
	public Set<Integer> getTpaFirms();
}