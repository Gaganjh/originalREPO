package com.manulife.pension.bd.web.login;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.delegate.BDPublicSecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.passcode.PasscodeSession;
import com.manulife.pension.platform.web.passcode.PasscodeVerification;
import com.manulife.pension.platform.web.passcode.PasscodeVerificationImpl;
import com.manulife.pension.platform.web.passcode.PasscodeVerificationImpl.PasscodeVerificationApplication;
import com.manulife.pension.service.security.bd.valueobject.BDLoginValueObject;
import com.manulife.pension.service.security.passcode.IllegalPasscodeStateException;
import com.manulife.pension.service.security.passcode.MailServerInactiveException;
import com.manulife.pension.service.security.passcode.PasscodeDeliveryPreference;
import com.manulife.pension.service.security.passcode.PasscodeGenerationResult;
import com.manulife.pension.service.security.passcode.PasscodeRecipient;
import com.manulife.pension.service.security.passcode.PasscodeSecurity.RsaSession;
import com.manulife.pension.service.security.passcode.PasscodeVerificationResult;
import com.manulife.pension.service.security.passcode.RequestDetails;
import com.manulife.pension.service.security.passcode.SmsServerInactiveException;
import com.manulife.pension.service.security.passcode.VoiceServerInactiveException;

enum BdPasscodeVerification
implements PasscodeVerification<BDLoginValueObject> {
    
    INSTANCE;
    	
    private final PasscodeVerification<BDLoginValueObject> delegate =
            new PasscodeVerificationImpl<BDLoginValueObject>(BdVerificationApplication.INSTANCE);
    
    private enum BdVerificationApplication
    implements PasscodeVerificationApplication<BDLoginValueObject> {
        
        INSTANCE;
        
        @Override
        public PasscodeSession<BDLoginValueObject> createPasscodeSession(final BDLoginValueObject loginVo) {
            return new BdPasscodeSession(loginVo);
        }
        
        @Override
        public PasscodeGenerationResult generatePasscodeForUser(
                final PasscodeSession<BDLoginValueObject> session,
                final RequestDetails details, PasscodeDeliveryPreference optedChannel)
                        throws SystemException, IllegalPasscodeStateException, SmsServerInactiveException, MailServerInactiveException, VoiceServerInactiveException {
        	final RsaSession rsaSession  = session.getLoginInformation().getRsaSession();
        	return
                    BDPublicSecurityServiceDelegate.getInstance().generatePasscodeForUser(
                    		rsaSession != null ? rsaSession.getUserId() : StringUtils.EMPTY,
                            session.getProfileId(),
                            new PasscodeRecipient(session.getRecipientEmail(), null, null),
                            details);
           
        }
        
        @Override
        public PasscodeGenerationResult resendPasscode(
                final PasscodeSession<BDLoginValueObject> session,
                final RequestDetails details)
                        throws SystemException, IllegalPasscodeStateException {
            
        	final RsaSession rsaSession  = session.getLoginInformation().getRsaSession();
            return
                    BDPublicSecurityServiceDelegate.getInstance().resendPasscode(
                    		rsaSession != null ? rsaSession.getUserId() : StringUtils.EMPTY,
                            session.getProfileId(),
                            session.getRecipientEmail(),
                            details);
            
        }
        
        @Override
        public PasscodeVerificationResult verifyPasscode(
                final PasscodeSession<BDLoginValueObject> session,
                final String token,
                final String passcodeSubmission,
                final RequestDetails details)
                        throws SystemException, IllegalPasscodeStateException {
        	
        	return
                    BDPublicSecurityServiceDelegate.getInstance().verifyPasscode(
                            session.getProfileId(),
                            session.getLoginInformation().getRsaSession(),
                            token,
                            passcodeSubmission,
                            details,
                            session.getLoginInformation());
            
        }
        
        @Override
        public String getPasscodeCreatedTs(long profileId) throws SystemException {
            
            return BDPublicSecurityServiceDelegate.getInstance().getPasscodeCreatedTS(profileId);
            
        }
        
    }

    @Override
    public PasscodeSession<BDLoginValueObject> getPasscodeSession(final HttpSession session) {
        return delegate.getPasscodeSession(session);
    }

    @Override
    public void clearPasscodeSession(final HttpSession session) {
    	delegate.clearPasscodeSession(session);
    }

    @Override
	public PasscodeGenerationResult generatePasscodeForUser(
            final BDLoginValueObject loginVo,
            final HttpSession session,
            final RequestDetails details, PasscodeDeliveryPreference optedChannel)
    throws SystemException, IllegalPasscodeStateException, SmsServerInactiveException, MailServerInactiveException, VoiceServerInactiveException {
    	
         return delegate.generatePasscodeForUser(loginVo, session, details, null);
    }

    @Override
    public PasscodeGenerationResult resendPasscode(final HttpSession session, final RequestDetails details)
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
	public void setPasscodeExpiredTS(HttpSession session, long profileId) throws SystemException {
		// TODO Auto-generated method stub
		
	}
}
