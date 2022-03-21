package com.manulife.pension.platform.web.passcode;

import javax.servlet.http.HttpSession;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.security.passcode.IllegalPasscodeStateException;
import com.manulife.pension.service.security.passcode.MailServerInactiveException;
import com.manulife.pension.service.security.passcode.PasscodeDeliveryPreference;
import com.manulife.pension.service.security.passcode.PasscodeGenerationResult;
import com.manulife.pension.service.security.passcode.PasscodeVerificationResult;
import com.manulife.pension.service.security.passcode.RequestDetails;
import com.manulife.pension.service.security.passcode.SmsServerInactiveException;
import com.manulife.pension.service.security.passcode.VoiceServerInactiveException;

public interface PasscodeVerification<LoginVo> {
    
    public PasscodeSession<LoginVo> getPasscodeSession(final HttpSession session);

    public void clearPasscodeSession(final HttpSession session);

    public PasscodeGenerationResult generatePasscodeForUser(
            final LoginVo loginVo,
            final HttpSession session,
            final RequestDetails details, PasscodeDeliveryPreference optedChannel)
    throws SystemException, IllegalPasscodeStateException, SmsServerInactiveException, MailServerInactiveException, VoiceServerInactiveException;

    
    public PasscodeGenerationResult resendPasscode(
            final HttpSession session,
            final RequestDetails details)
    throws SystemException, IllegalPasscodeStateException, SmsServerInactiveException, VoiceServerInactiveException;

    public PasscodeVerificationResult verify(
            final HttpSession session,
            final String token,
            final String passcodeSubmission,
            final RequestDetails details)
    throws SystemException, IllegalPasscodeStateException;
    
    public void setHelpfulHintPasscodeTS(final HttpSession session, long profileId)
    throws SystemException;

	void setPasscodeExpiredTS(HttpSession session, long profileId) throws SystemException;

}
