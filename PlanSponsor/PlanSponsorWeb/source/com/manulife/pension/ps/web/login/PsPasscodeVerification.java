package com.manulife.pension.ps.web.login;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.passcode.PasscodeSession;
import com.manulife.pension.platform.web.passcode.PasscodeVerification;
import com.manulife.pension.platform.web.passcode.PasscodeVerificationImpl;
import com.manulife.pension.platform.web.passcode.PasscodeVerificationImpl.PasscodeVerificationApplication;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.service.security.passcode.IllegalPasscodeStateException;
import com.manulife.pension.service.security.passcode.MailServerInactiveException;
import com.manulife.pension.service.security.passcode.PasscodeDeliveryPreference;
import com.manulife.pension.service.security.passcode.PasscodeGenerationResult;
import com.manulife.pension.service.security.passcode.PasscodeRecipient;
import com.manulife.pension.service.security.passcode.PasscodeVerificationResult;
import com.manulife.pension.service.security.passcode.RequestDetails;
import com.manulife.pension.service.security.passcode.SmsServerInactiveException;
import com.manulife.pension.service.security.passcode.VoiceServerInactiveException;
import com.manulife.pension.service.security.utility.email.Company;
import com.manulife.pension.service.security.valueobject.LoginPSValueObject;

enum PsPasscodeVerification
implements PasscodeVerification<LoginPSValueObject> {
    
    INSTANCE;
    
    private final PasscodeVerificationImpl<LoginPSValueObject> delegate =
            new PasscodeVerificationImpl<LoginPSValueObject>(PsVerificationApplication.INSTANCE);
    
    private enum PsVerificationApplication
    implements PasscodeVerificationApplication<LoginPSValueObject> {
        
        INSTANCE;

        @Override
        public PasscodeSession<LoginPSValueObject> createPasscodeSession(final LoginPSValueObject loginVo) {
            return new PsPasscodeSession(loginVo);
        }

        @Override
        public PasscodeGenerationResult generatePasscodeForUser(
                final PasscodeSession<LoginPSValueObject> passcodeSession,
                final RequestDetails details, PasscodeDeliveryPreference optedChannel)
        throws SystemException, IllegalPasscodeStateException, SmsServerInactiveException, MailServerInactiveException, VoiceServerInactiveException{
        	
        	final String mobile = StringUtils.isNotEmpty(passcodeSession.getRecipientMobile())
					? passcodeSession.getRecipientMobile() : null;
        	final String phone = StringUtils.isNotEmpty(passcodeSession.getRecipientPhone())
					? passcodeSession.getRecipientPhone() : null;
					
			return SecurityServiceDelegate.getInstance().generatePasscodeForUser(StringUtils.EMPTY,
					passcodeSession.getProfileId(),
					new PasscodeRecipient(passcodeSession.getRecipientEmail(), mobile, phone, optedChannel),
					Company.ofCode(Environment.getInstance().getSiteLocation()), details);
            
        }

        @Override
        public PasscodeGenerationResult resendPasscode(
                final PasscodeSession<LoginPSValueObject> passcodeSession,
                final RequestDetails details)
        throws SystemException, IllegalPasscodeStateException, SmsServerInactiveException, VoiceServerInactiveException {
            
            return
                    SecurityServiceDelegate.getInstance().resendPasscode(
                    		StringUtils.EMPTY,
                            passcodeSession.getProfileId(),
                            passcodeSession.getRecipientEmail(),
                            Company.ofCode(Environment.getInstance().getSiteLocation()),
                            details);

        }

        @Override
        public PasscodeVerificationResult verifyPasscode(
                final PasscodeSession<LoginPSValueObject> passcodeSession,
                final String token,
                final String passcodeSubmission,
                final RequestDetails details)
        throws SystemException, IllegalPasscodeStateException {
            
        	return 
        			SecurityServiceDelegate.getInstance().verifyPasscode(
        			passcodeSession.getProfileId(),
        			passcodeSession.getLoginInformation().getRsaSession(),
        			token,
        			passcodeSubmission,
        			details,
        			Company.ofCode(Environment.getInstance().getSiteLocation()),
        			passcodeSession.getLoginInformation());

        }

        @Override
        public String getPasscodeCreatedTs(long profileId) throws SystemException {

            return SecurityServiceDelegate.getInstance().getPasscodeCreatedTS(profileId);
            
        }
    }

    @Override
    public PasscodeSession<LoginPSValueObject> getPasscodeSession(final HttpSession session) {
        return delegate.getPasscodeSession(session);
    }

    @Override
    public void clearPasscodeSession(final HttpSession session) {
    	delegate.clearPasscodeSession(session);
    }

    @Override
    public PasscodeGenerationResult generatePasscodeForUser(
            final LoginPSValueObject loginVo,
            final HttpSession session,
            final RequestDetails details, PasscodeDeliveryPreference optedChannel)
    throws SystemException, IllegalPasscodeStateException, SmsServerInactiveException, MailServerInactiveException, VoiceServerInactiveException{
        return delegate.generatePasscodeForUser(loginVo, session, details, optedChannel);
    }

    @Override
    public PasscodeGenerationResult resendPasscode(
            final HttpSession session,
            final RequestDetails details)
    throws SystemException, IllegalPasscodeStateException, SmsServerInactiveException, VoiceServerInactiveException {
        return delegate.resendPasscode(session, details);
    }

    @Override
    public PasscodeVerificationResult verify(
            final HttpSession session,
            final String token,
            final String passcodeSubmission,
            final RequestDetails details)
    throws SystemException, IllegalPasscodeStateException {
        return delegate.verify(session, token, passcodeSubmission, details);
    }

    @Override
    public void setHelpfulHintPasscodeTS(final HttpSession session, final long profileId) throws SystemException {
    	delegate.setHelpfulHintPasscodeTS(session, profileId);
    }
    
    @Override
    public void setPasscodeExpiredTS(final HttpSession session, long profileId) throws SystemException {
    	delegate.setPasscodeExpiredTS(session, profileId);
    }
}
