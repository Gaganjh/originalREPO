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
 * Activate the BrokerEntity relationship
 * 
 * @author guweigu
 *
 */
public class BrokerEntityActivationHandler implements ActivationHandler {
	private static Logger logger = Logger
			.getLogger(NewPasswordActivationHandler.class);

	private static BrokerEntityActivationHandler instance = new BrokerEntityActivationHandler();

	public static BrokerEntityActivationHandler getInstance() {
		return instance;
	}

	private BrokerEntityActivationHandler() {
	}

	public List<ValidationError> activate(BDSecurityInteractionRequestEx request)
			throws SystemException {
		List<ValidationError> errors = new ArrayList<ValidationError>();
		try {
			BDPublicSecurityServiceDelegate.getInstance().activateBrokerEntity(
					request.getId());
		} catch (SecurityServiceException e) {
			logger.debug("Fail to activate UserParty with request id = "
					+ request.getId(), e);
			errors.add(new ValidationError("",
					SecurityServiceExceptionHelper.getErrorCode(e)));
		}
		return errors;
	}

}
