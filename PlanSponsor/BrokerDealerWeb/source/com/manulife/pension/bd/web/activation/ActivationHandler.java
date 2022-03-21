package com.manulife.pension.bd.web.activation;

import java.util.List;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.security.bd.valueobject.BDSecurityInteractionRequestEx;
import com.manulife.pension.validator.ValidationError;

/**
 * ActivationHandler which calls the Security service module's various methods
 * to do activation for different type of request.
 * 
 * Each type of request should implement this ActivationHandler interface
 * @author guweigu
 *
 */
public interface ActivationHandler {
	/**
	 * Activate the security request. Returns any errors occured in the activation
	 * process, except the SystemException.
	 * 
	 * @param request
	 * @return
	 * @throws SystemException
	 */
	List<ValidationError> activate(BDSecurityInteractionRequestEx request)
			throws SystemException;
}
