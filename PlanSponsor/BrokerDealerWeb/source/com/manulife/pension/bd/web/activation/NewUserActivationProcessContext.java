package com.manulife.pension.bd.web.activation;

/**
 * The process context for new user activation
 * @author guweigu
 *
 */
public class NewUserActivationProcessContext extends
		SimpleActivationProcessContext {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public ActivationHandler getActivationHandler() {
		return NewUserActivationHandler.getInstance();
	}
}
