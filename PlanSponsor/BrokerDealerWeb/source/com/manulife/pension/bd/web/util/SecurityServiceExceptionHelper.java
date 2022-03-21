package com.manulife.pension.bd.web.util;

import java.util.HashMap;
import java.util.Map;

import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.service.security.bd.exception.BDBasicBrokerNotFoundException;
import com.manulife.pension.service.security.bd.exception.BDBrokerAssistantSecurityException;
import com.manulife.pension.service.security.bd.exception.BDBrokerEmailAddressMismatchException;
import com.manulife.pension.service.security.bd.exception.BDBrokerEmailAddressMissingException;
import com.manulife.pension.service.security.bd.exception.BDBrokerNoActiveAssociatedPartyException;
import com.manulife.pension.service.security.bd.exception.BDBrokerNoActiveContractException;
import com.manulife.pension.service.security.bd.exception.BDChangeTempPasswordAccessCodeNotMatchException;
import com.manulife.pension.service.security.bd.exception.BDChangeTempPasswordFailedException;
import com.manulife.pension.service.security.bd.exception.BDDuplicateAssistantException;
import com.manulife.pension.service.security.bd.exception.BDFailedAlmostNTimesRegistrationException;
import com.manulife.pension.service.security.bd.exception.BDFinRepUserExistException;
import com.manulife.pension.service.security.bd.exception.BDInactivePasswordException;
import com.manulife.pension.service.security.bd.exception.BDInvalidBrokerUserException;
import com.manulife.pension.service.security.bd.exception.BDInvalidFirmRepUserException;
import com.manulife.pension.service.security.bd.exception.BDNewPasswordExpiredException;
import com.manulife.pension.service.security.bd.exception.BDParentBrokerNoActiveAssociatedPartyException;
import com.manulife.pension.service.security.bd.exception.BDPasswordNotActiveException;
import com.manulife.pension.service.security.bd.exception.BDRVPUserInvalidPartyException;
import com.manulife.pension.service.security.bd.exception.BDSecurityOperationNotAllowedException;
import com.manulife.pension.service.security.bd.exception.BDSecurityRequestCompleteException;
import com.manulife.pension.service.security.bd.exception.BDSecurityRequestExpiredException;
import com.manulife.pension.service.security.bd.exception.BDSecurityRequestFailedException;
import com.manulife.pension.service.security.bd.exception.BDSecurityRequestInvalidException;
import com.manulife.pension.service.security.bd.exception.BDSecurityRequestInvalidPasswordStatusException;
import com.manulife.pension.service.security.bd.exception.BDSecurityRequestUserDeletedException;
import com.manulife.pension.service.security.bd.exception.BDSecurityRequestWrongUserException;
import com.manulife.pension.service.security.bd.exception.BDSsnTaxIdContractValidationException;
import com.manulife.pension.service.security.bd.exception.BDTempPasswordExpiredException;
import com.manulife.pension.service.security.bd.exception.BDUserPartyNotExistException;
import com.manulife.pension.service.security.bd.exception.BDUserPartyNotPendingException;
import com.manulife.pension.service.security.passcode.MailServerInactiveException;
import com.manulife.pension.service.security.bd.exception.MigrationFailedAlmostNTimesException;
import com.manulife.pension.service.security.bd.exception.UserWithNoBDAccessionException;
import com.manulife.pension.service.security.bd.migration.exception.MigrationAlreadyCompletedException;
import com.manulife.pension.service.security.bd.migration.exception.MigrationAuthenticationException;
import com.manulife.pension.service.security.bd.migration.exception.MigrationBrokerEntityUsedException;
import com.manulife.pension.service.security.bd.migration.exception.MigrationFailedNTimesException;
import com.manulife.pension.service.security.bd.migration.exception.MigrationNotAllowedException;
import com.manulife.pension.service.security.bd.migration.exception.MigrationUserLockedException;
import com.manulife.pension.service.security.bd.migration.exception.MigrationUserNotFoundException;
import com.manulife.pension.service.security.exception.ChallengeAnswerDoesNotMatchException;
import com.manulife.pension.service.security.exception.DisabledUserException;
import com.manulife.pension.service.security.exception.FailedAlmostNTimesException;
import com.manulife.pension.service.security.exception.FailedNTimesException;
import com.manulife.pension.service.security.exception.FailedNTimesForgotPasswordException;
import com.manulife.pension.service.security.exception.IncorrectPasswordException;
import com.manulife.pension.service.security.exception.InvalidNetworkLocationException;
import com.manulife.pension.service.security.exception.LockedUserException;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.exception.UserAlreadyRegisteredException;
import com.manulife.pension.service.security.exception.UserNameIsInUseException;
import com.manulife.pension.service.security.exception.UserNotFoundException;
import com.manulife.pension.service.security.exception.UserNotRegisteredException;

public class SecurityServiceExceptionHelper {
	private static final Map<String, Integer> SecurityServiceExceptionMapping = new HashMap<String, Integer>();

	static {
		SecurityServiceExceptionMapping.put(UserNotFoundException.class
				.getName(), CommonErrorCodes.USER_DOES_NOT_EXIST);

		SecurityServiceExceptionMapping.put(
				UserWithNoBDAccessionException.class.getName(),
				BDErrorCodes.NO_BDW_ACCESS);

		SecurityServiceExceptionMapping.put(UserNotRegisteredException.class
				.getName(), BDErrorCodes.PROFILE_NOT_REGISTERED);

		SecurityServiceExceptionMapping.put(
				BDBrokerNoActiveAssociatedPartyException.class.getName(),
				BDErrorCodes.NO_ACTIVE_BROKER_ENTITY);

		SecurityServiceExceptionMapping.put(BDInvalidBrokerUserException.class
				.getName(), BDErrorCodes.NO_ACTIVE_BROKER_ENTITY);

		SecurityServiceExceptionMapping.put(
				BDRVPUserInvalidPartyException.class.getName(),
				BDErrorCodes.RVP_USER_INVALID_PARTY);

		SecurityServiceExceptionMapping.put(BDInvalidFirmRepUserException.class
				.getName(), BDErrorCodes.FIRMREP_USER_NO_PARTY);

		SecurityServiceExceptionMapping.put(BDInactivePasswordException.class
				.getName(), BDErrorCodes.INACTIVE_PASSWORD);

		SecurityServiceExceptionMapping.put(
				BDSecurityRequestInvalidException.class.getName(),
				BDErrorCodes.ACTIVATION_INVALID_REQUEST);

		SecurityServiceExceptionMapping.put(
				BDSecurityRequestFailedException.class.getName(),
				BDErrorCodes.ACTIVATION_STATUS_FAILED);

		SecurityServiceExceptionMapping.put(
				BDSecurityRequestCompleteException.class.getName(),
				BDErrorCodes.ACTIVATION_STATUS_COMPLETE);

		SecurityServiceExceptionMapping.put(
				BDSecurityRequestExpiredException.class.getName(),
				BDErrorCodes.ACTIVATION_STATUS_EXPIRED);

		SecurityServiceExceptionMapping.put(
				BDSecurityRequestUserDeletedException.class.getName(),
				BDErrorCodes.ACTIVATION_PROFILE_DELETED);

		SecurityServiceExceptionMapping.put(BDNewPasswordExpiredException.class
				.getName(), BDErrorCodes.ACTIVATION_NEW_PWD_EXPIRED);

		SecurityServiceExceptionMapping.put(
				FailedNTimesForgotPasswordException.class.getName(), 1006);

		SecurityServiceExceptionMapping.put(
				ChallengeAnswerDoesNotMatchException.class.getName(),
				BDErrorCodes.FORGET_PWD_ANSWER_NOT_MATCH);

		SecurityServiceExceptionMapping.put(UserNameIsInUseException.class
				.getName(), 1035);

		SecurityServiceExceptionMapping.put(FailedAlmostNTimesException.class
				.getName(), BDErrorCodes.PASSWORD_FAILED_ALMOST_N_TIMES);

		SecurityServiceExceptionMapping.put(FailedNTimesException.class
				.getName(), BDErrorCodes.PASSWORD_FAILED_N_TIMES);

		SecurityServiceExceptionMapping.put(IncorrectPasswordException.class
				.getName(), BDErrorCodes.PASSWORD_INCORRECT);

		SecurityServiceExceptionMapping.put(
				BDTempPasswordExpiredException.class.getName(),
				BDErrorCodes.RESET_PASSWORD_EXPIRED);
		SecurityServiceExceptionMapping.put(DisabledUserException.class
				.getName(), CommonErrorCodes.USER_DOES_NOT_EXIST);

		SecurityServiceExceptionMapping.put(
				UserAlreadyRegisteredException.class.getName(),
				BDErrorCodes.USER_MANAGEMENT_CONFLICT_ERROR);

		SecurityServiceExceptionMapping
				.put(LockedUserException.class.getName(),
						BDErrorCodes.ACCESS_LOCKED);

		SecurityServiceExceptionMapping.put(BDPasswordNotActiveException.class
				.getName(), BDErrorCodes.CURRENT_PWD_NOT_ACTIVE);

		SecurityServiceExceptionMapping.put(
				BDChangeTempPasswordFailedException.class.getName(),
				BDErrorCodes.CHANGE_TEMP_PWD_FAIL);

		SecurityServiceExceptionMapping
				.put(BDChangeTempPasswordAccessCodeNotMatchException.class
						.getName(),
						BDErrorCodes.CHANGE_TEMP_PWD_ACCESSCODE_NOT_MATCH);
		SecurityServiceExceptionMapping.put(BDDuplicateAssistantException.class
				.getName(), BDErrorCodes.ASSISTANT_DUPLICATE);

		SecurityServiceExceptionMapping.put(
				BDBrokerEmailAddressMissingException.class.getName(),
				BDErrorCodes.BROKER_ENTITY_MISSING_EMAIL);

		SecurityServiceExceptionMapping.put(
				BDBrokerEmailAddressMismatchException.class.getName(),
				BDErrorCodes.BROKER_VERIFICATION_FAIL);

		SecurityServiceExceptionMapping.put(
				BDSsnTaxIdContractValidationException.class.getName(),
				BDErrorCodes.BROKER_VERIFICATION_FAIL);

		SecurityServiceExceptionMapping.put(BDFinRepUserExistException.class
				.getName(), BDErrorCodes.BROKER_ENTITY_ATTACHED);

		SecurityServiceExceptionMapping.put(
				BDBrokerNoActiveContractException.class.getName(),
				BDErrorCodes.BROKER_ENTITY_NO_ACTIVE_CONTRACT);

		SecurityServiceExceptionMapping.put(
				BDSecurityRequestWrongUserException.class.getName(),
				BDErrorCodes.ACTIVATION_USERNAME_INVALID);

		SecurityServiceExceptionMapping
				.put(BDSecurityRequestInvalidPasswordStatusException.class
						.getName(),
						BDErrorCodes.ACTIVATION_PASSWORD_STATUS_INVALID);
		SecurityServiceExceptionMapping.put(
				MigrationAuthenticationException.class.getName(), 1002);

		SecurityServiceExceptionMapping.put(
				MigrationUserNotFoundException.class.getName(), 1002);

		SecurityServiceExceptionMapping.put(
				MigrationAlreadyCompletedException.class.getName(),
				BDErrorCodes.MIGRATION_ALREADY_COMPLETED);

		SecurityServiceExceptionMapping.put(MigrationNotAllowedException.class
				.getName(), BDErrorCodes.MIGRATION_NO_CONTRACTS);

		SecurityServiceExceptionMapping.put(MigrationUserLockedException.class
				.getName(), BDErrorCodes.MIGRATION_PROFILE_LOCKED);

		SecurityServiceExceptionMapping.put(
				MigrationFailedAlmostNTimesException.class.getName(),
				BDErrorCodes.MIGRATION_PASSWORD_ALMOST_LOCKED);
		SecurityServiceExceptionMapping.put(
				MigrationFailedNTimesException.class.getName(),
				BDErrorCodes.MIGRATION_PASSWORD_LOCKED);
		SecurityServiceExceptionMapping.put(
				MigrationBrokerEntityUsedException.class.getName(),
				BDErrorCodes.MIGRATION_ENTITY_USED);

		SecurityServiceExceptionMapping.put(
				BDFailedAlmostNTimesRegistrationException.class.getName(),
				BDErrorCodes.REG_FAIL_ALMOST_NTIMES);

		SecurityServiceExceptionMapping.put(
				BDBasicBrokerNotFoundException.class.getName(),
				BDErrorCodes.BASIC_BROKER_NOT_FOUND_IN_PEOPLESOFT);

		SecurityServiceExceptionMapping.put(
				BDUserPartyNotPendingException.class.getName(),
				BDErrorCodes.ACTIVATION_USERPARTY_STATUS_INVALID);
		
		SecurityServiceExceptionMapping.put(BDUserPartyNotExistException.class
				.getName(), BDErrorCodes.ACTIVATION_USERPARTY_STATUS_INVALID);
		
		SecurityServiceExceptionMapping.put(
				BDBrokerAssistantSecurityException.class.getName(),
				BDErrorCodes.MY_PROFILE_CONFLICT_ERROR);

		SecurityServiceExceptionMapping.put(
				BDParentBrokerNoActiveAssociatedPartyException.class.getName(),
				BDErrorCodes.ASSISTANT_DISABLED_LOGIN);
		
		SecurityServiceExceptionMapping.put(
				BDSecurityOperationNotAllowedException.class.getName(),
				BDErrorCodes.USER_MANAGEMENT_CONFLICT_ERROR);
		
		SecurityServiceExceptionMapping.put(
		        InvalidNetworkLocationException.class.getName(), 
		        CommonErrorCodes.INVALID_NETWORK_LOCATION);

		SecurityServiceExceptionMapping.put(
				MailServerInactiveException.class.getName(), 
				BDErrorCodes.ERROR_EMAIL_SERVER_INACTIVE);

	}

	/**
	 * This helper method checks the localExceptionMapping first (in case the
	 * Action wants to handle the exception different from common way). If the
	 * localExceptionMapping doesn't have it, falls back to
	 * 
	 * @param exception
	 * @param localExceptionMapping
	 * @return
	 */
	public static Integer getErrorCode(SecurityServiceException exception,
			Map<String, Integer> localExceptionMapping) {
		Integer value = null;
		String exceptionClsName = exception.getClass().getName();
		if (localExceptionMapping != null) {
			value = localExceptionMapping.get(exceptionClsName);
		}
		if (value == null) {
			value = SecurityServiceExceptionMapping.get(exceptionClsName);
		}
		return value == null ? 0 : value;
	}
	
	/**
	 *  This helper method get the error code from the exception for the cause
	 * 
	 * @param exception
	 * @param localExceptionMapping
	 * @return
	 */
	public static Integer getErrorCodeForCause(SecurityServiceException exception,
			Map<String, Integer> localExceptionMapping) {
		Integer value = null;
		
		if (exception.getCause() == null) {
			return getErrorCode(exception);               
		}

		String exceptionClsName = exception.getCause().getClass().getName();
		if (localExceptionMapping != null) {
			value = localExceptionMapping.get(exceptionClsName);
		}
		if (value == null) {
			value = SecurityServiceExceptionMapping.get(exceptionClsName);
		}
		return value == null ? 0 : value;
	}
	
	/**
	 * This helper method get the error code for the exception from the global
	 * mapping
	 * 
	 * @param exception
	 * @return
	 */
	public static Integer getErrorCode(SecurityServiceException exception) {
		return getErrorCode(exception, null);
	}
	
	public static Integer getErrorCodeForCause(SecurityServiceException exception) {
		return getErrorCodeForCause(exception, null);
	}
}
