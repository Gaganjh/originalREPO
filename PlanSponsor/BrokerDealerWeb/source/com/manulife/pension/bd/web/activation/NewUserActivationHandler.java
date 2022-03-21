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
 * Activate New user handler
 * 
 * @author guweigu
 *
 */
public class NewUserActivationHandler implements ActivationHandler {
	private static final Logger logger = Logger.getLogger(NewUserActivationHandler.class);
	
	private static final NewUserActivationHandler instance = new NewUserActivationHandler();

	public static NewUserActivationHandler getInstance() {
		return instance;
	}
	
	private NewUserActivationHandler() {		
	}
	
	public List<ValidationError> activate(BDSecurityInteractionRequestEx request)
			throws SystemException {
		List<ValidationError> errors = new ArrayList<ValidationError>();
		try {
			BDPublicSecurityServiceDelegate.getInstance().activateUser(
					request.getId());
		} catch (SecurityServiceException e) {
			logger.debug("Fail to activate new user with request id = " + request.getId(), e);
			errors.add(new ValidationError("",
					SecurityServiceExceptionHelper.getErrorCode(e)));
		}
		return errors;
	}

}
