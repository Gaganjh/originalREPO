
package com.manulife.pension.bd.web.activation;

/**
 * The process context for Broker Entity relationship activation
 * It defines the specific actionvationHandler for the Broker Entity activation
 * 
 * @author guweigu
 *
 */

public class BrokerEntityActivationProcessContext extends
		SimpleActivationProcessContext {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public ActivationHandler getActivationHandler() {
		return BrokerEntityActivationHandler.getInstance();
	}

}
