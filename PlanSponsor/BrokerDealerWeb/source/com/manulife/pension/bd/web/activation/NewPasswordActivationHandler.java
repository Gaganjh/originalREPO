package com.manulife.pension.bd.web.activation;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.delegate.BDPublicSecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.security.bd.valueobject.BDSecurityInteractionRequestEx;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.validator.ValidationError;

/**
 * Handler for activating new password
 * 
 * @author guweigu
 * 
 */
public class NewPasswordActivationHandler implements ActivationHandler {
	private static Logger logger = Logger
			.getLogger(NewPasswordActivationHandler.class);

	private static NewPasswordActivationHandler instance = new NewPasswordActivationHandler();

	private NewPasswordActivationHandler() {
	}

	public List<ValidationError> activate(BDSecurityInteractionRequestEx request)
			throws SystemException {
		List<ValidationError> errors = new ArrayList<ValidationError>();
		try {
			BDPublicSecurityServiceDelegate.getInstance().activateNewPassword(
					request.getId());
		} catch (SecurityServiceException e) {
			logger.debug("Fail to activate new password with request id = "
					+ request.getId(), e);
			errors.add(new ValidationError("",
					SecurityServiceExceptionHelper.getErrorCode(e)));
		}
		return errors;
	}

	public static NewPasswordActivationHandler getInstance() {
		return instance;
	}
}
