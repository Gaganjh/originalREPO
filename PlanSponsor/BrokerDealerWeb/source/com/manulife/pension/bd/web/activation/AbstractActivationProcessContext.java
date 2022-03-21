package com.manulife.pension.bd.web.activation;

import com.manulife.pension.bd.web.process.BDWizardProcessContext;
import com.manulife.pension.service.security.bd.valueobject.BDSecurityInteractionRequestEx;

/**
 * Abstract class for activation process context that stores
 * the SecurityInteractionRequest
 * 
 * @author guweigu
 *
 */
abstract public class AbstractActivationProcessContext extends BDWizardProcessContext {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected BDSecurityInteractionRequestEx securityRequest;

	public BDSecurityInteractionRequestEx getSecurityRequest() {
		return securityRequest;
	}

	public void setSecurityRequest(BDSecurityInteractionRequestEx securityRequest) {
		this.securityRequest = securityRequest;
	}	
	
	abstract public String getName();
}
