package com.manulife.pension.platform.web.passcode;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.security.passcode.IllegalPasscodeStateException;
import com.manulife.pension.service.security.passcode.MailServerInactiveException;
import com.manulife.pension.service.security.passcode.PasscodeChannel;
import com.manulife.pension.service.security.passcode.PasscodeDeliveryPreference;
import com.manulife.pension.service.security.passcode.PasscodeGenerationResult;
import com.manulife.pension.service.security.passcode.PasscodeVerificationResult;
import com.manulife.pension.service.security.passcode.RequestDetails;
import com.manulife.pension.service.security.passcode.SmsServerInactiveException;
import com.manulife.pension.service.security.passcode.VoiceServerInactiveException;

public class PasscodeVerificationImpl<LoginVo>
implements PasscodeVerification<LoginVo> {
    
    public interface PasscodeVerificationApplication<LoginVo> {
        
        PasscodeSession<LoginVo> createPasscodeSession(LoginVo loginVo);
        
        PasscodeGenerationResult generatePasscodeForUser(PasscodeSession<LoginVo> session, RequestDetails details, PasscodeDeliveryPreference optedChannel)
        throws SystemException, IllegalPasscodeStateException, SmsServerInactiveException, MailServerInactiveException, VoiceServerInactiveException;
        
        PasscodeGenerationResult resendPasscode(PasscodeSession<LoginVo> session, RequestDetails details)
        throws SystemException, IllegalPasscodeStateException, SmsServerInactiveException, VoiceServerInactiveException;
        
        PasscodeVerificationResult verifyPasscode(PasscodeSession<LoginVo> session, String token, String passcodeSubmission, RequestDetails details)
        throws SystemException, IllegalPasscodeStateException;
        
        String getPasscodeCreatedTs(long profileId)
        throws SystemException;

    }
    
	private static final String SESSION_KEY = "PASSCODE_SESSION_KEY";
	private static final String PASSCODE_CREATED_TIME = "PASSCODE_CREATED_TIME";
	private static final String PASSCODE_EXPIRED_TIME = "PASSCODE_EXPIRED_TIME";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a 'ET'");

	
	private final PasscodeVerificationApplication<LoginVo> appContext;
	
	public PasscodeVerificationImpl(final PasscodeVerificationApplication<LoginVo> appContext) { this.appContext = appContext; }
	
	public PasscodeSession<LoginVo> getPasscodeSession(final HttpSession session) {
		return (PasscodeSession<LoginVo>) session.getAttribute(SESSION_KEY);
	}

	public void clearPasscodeSession(final HttpSession session) {
		session.setAttribute(SESSION_KEY, null);
	}

	public PasscodeGenerationResult generatePasscodeForUser(final LoginVo loginVo, final HttpSession session,
			final RequestDetails details, PasscodeDeliveryPreference optedChannel) throws SystemException, IllegalPasscodeStateException, SmsServerInactiveException, MailServerInactiveException, VoiceServerInactiveException {

		final PasscodeSession<LoginVo> savedPasscodeSession = getPasscodeSession(session);

		final PasscodeSession<LoginVo> passcodeSession = savedPasscodeSession == null ? appContext.createPasscodeSession(loginVo)
				: savedPasscodeSession;

		final PasscodeGenerationResult result = appContext.generatePasscodeForUser(passcodeSession, details, optedChannel);

		switch (result.getResult()) {

		case PREVIOUSLY_LOCKED:
			clearPasscodeSession(session);
			break;

		case SUCCESS:
		case EMAIL_WAIT:
			passcodeSession.setFromAddress(result.getFromAddress());
			passcodeSession.setCreatedTimeStamp(sdf.format(result.getPasscodeCreatedTs()));
			passcodeSession.setExpriredTimeStamp(sdf.format(result.getPasscodeExpiredTs()));
			passcodeSession.setPasscodeChannel(result.getPasscodeChannel());
			if (savedPasscodeSession == null) {
				session.setAttribute(SESSION_KEY, passcodeSession);
			}			
			break;

		case COOLING:
			break;

		default:
			throw new AssertionError("Unhandled GenerationResult " + result);

		}
		setHelpfulHintPasscodeTS(session, passcodeSession.getProfileId());
		setPasscodeExpiredTS(session, passcodeSession.getProfileId());
		
		return result;

	}
	
	public PasscodeGenerationResult resendPasscode(final HttpSession session, final RequestDetails details)
            throws SystemException, IllegalPasscodeStateException, SmsServerInactiveException, VoiceServerInactiveException {

        final PasscodeSession<LoginVo> passcodeSession = getPasscodeSession(session);

        if (passcodeSession == null) {
            throw new IllegalPasscodeStateException();
        }

        final PasscodeGenerationResult result = appContext.resendPasscode(passcodeSession, details);

        switch (result.getResult()) {

        case PREVIOUSLY_LOCKED:
            clearPasscodeSession(session);
            break;

        case SUCCESS:
        case EMAIL_WAIT:
            passcodeSession.setFromAddress(result.getFromAddress());
			passcodeSession.setCreatedTimeStamp(sdf.format(result.getPasscodeCreatedTs()));
			passcodeSession.setExpriredTimeStamp(sdf.format(result.getPasscodeExpiredTs()));
			passcodeSession.setResendAttemptCount(result.getRemainingEmailsCount());
			passcodeSession.setResendFlag(true);
            break;

        case COOLING:
        	passcodeSession.setCoolingTimeStamp(result.getCoolingTimestamp());
            break;

        default:
            throw new AssertionError("Unhandled GenerationResult " + result);

        }
        setHelpfulHintPasscodeTS(session, passcodeSession.getProfileId());
        return result;

    }

	public PasscodeVerificationResult verify(final HttpSession session, final String token, final String passcodeSubmission,
			final RequestDetails details) throws SystemException, IllegalPasscodeStateException {

		final PasscodeSession<LoginVo> passcodeSession = getPasscodeSession(session);

		if (passcodeSession == null) {
			throw new IllegalPasscodeStateException();
		}

		try {

			final PasscodeVerificationResult result = appContext.verifyPasscode(passcodeSession, token, passcodeSubmission, details);
			
			switch (result.getResult()) {

			case PREVIOUSLY_LOCKED:
			case EXHAUSTED:
				clearPasscodeSession(session);
				break;

			case EXPIRED:
			case MATCH:
			case RETRY:
				break;

			default:
				throw new AssertionError("Unhandled VerificationResult " + result);

			}
			setHelpfulHintPasscodeTS(session, passcodeSession.getProfileId());
			return result;
		} catch (final IllegalPasscodeStateException ipse) {
			clearPasscodeSession(session);
			throw ipse;
		}
	}
	
	public void setHelpfulHintPasscodeTS(final HttpSession session, long profileId) throws SystemException {
		String passcodeCreatedTs;
		try {
			passcodeCreatedTs = appContext.getPasscodeCreatedTs(profileId);
			PasscodeSession<LoginVo> passcodeSession = getPasscodeSession(session);

			if (StringUtils.isNotEmpty(passcodeCreatedTs) && passcodeSession != null && passcodeSession.getPasscodeChannel() == PasscodeChannel.EMAIL ) {
				session.setAttribute(PASSCODE_CREATED_TIME, new SimpleDateFormat("MMddyyyy HH:mm:ss:SSS")
						.format(Timestamp.valueOf(passcodeCreatedTs).getTime()));

			} else {
				session.setAttribute(PASSCODE_CREATED_TIME, null);
			}
		} catch (final SystemException se) {
			throw se;
		}

	}
	
	public void setPasscodeExpiredTS(final HttpSession session, long profileId) throws SystemException {
		String passcodeCreatedTs;
		try {
			passcodeCreatedTs = appContext.getPasscodeCreatedTs(profileId);
			PasscodeSession<LoginVo> passcodeSession = getPasscodeSession(session);

			if (StringUtils.isNotEmpty(passcodeCreatedTs) && passcodeSession != null && passcodeSession.getPasscodeChannel() != PasscodeChannel.EMAIL ) {
				session.setAttribute(PASSCODE_EXPIRED_TIME, StringUtils.strip(passcodeSession.getExpriredTimeStamp(), "ET")); 

			} else {
				session.setAttribute(PASSCODE_EXPIRED_TIME, null);
			}
		} catch (final SystemException se) {
			throw se;
		}

	}
	
	
	

}
