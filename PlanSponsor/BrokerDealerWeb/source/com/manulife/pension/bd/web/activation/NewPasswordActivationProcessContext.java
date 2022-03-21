package com.manulife.pension.bd.web.activation;

/**
 * The process context for NewPasswordActivation
 * It defines the specific actionvationHandler for the NewPassword activation
 * 
 * @author guweigu
 *
 */
public class NewPasswordActivationProcessContext extends
		SimpleActivationProcessContext {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public ActivationHandler getActivationHandler() {
		return NewPasswordActivationHandler.getInstance();
	}

}
