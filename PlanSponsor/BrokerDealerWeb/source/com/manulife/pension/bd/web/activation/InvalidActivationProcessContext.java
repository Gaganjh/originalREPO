package com.manulife.pension.bd.web.activation;

import java.util.List;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.security.bd.valueobject.BDSecurityInteractionRequestEx;
import com.manulife.pension.validator.ValidationError;

/**
 * This process represents the case the BDSecurity request is invalid.
 * The activation process has to be stopped.
 * 
 * @author guweigu
 *
 */
public class InvalidActivationProcessContext extends
		SimpleActivationProcessContext {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public ActivationHandler getActivationHandler() {
		return new ActivationHandler(){

			public List<ValidationError> activate(
					BDSecurityInteractionRequestEx request)
					throws SystemException {
				return null;
			}};
	}
}
