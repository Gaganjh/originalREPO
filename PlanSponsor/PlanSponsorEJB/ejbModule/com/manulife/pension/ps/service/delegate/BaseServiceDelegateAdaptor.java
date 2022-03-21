package com.manulife.pension.ps.service.delegate;

import com.manulife.pension.service.account.valueobject.CustomerServicePrincipal;

/**
 * Base ServiceDelegate Adaptor class
 * Contains mentods for integration with the external delegates (ezk for example)
 * 
 * @author Leon Drotenko
 * Created on May 12, 2004
 */
public abstract class BaseServiceDelegateAdaptor {

	/**
	 * Constructor for BaseServiceDelegateAdaptor
	 * 
	 */
	public BaseServiceDelegateAdaptor() {
		super();
	}

	/**
	 * Returns an integration object to successfully use ezk's delegates
	 * @param profileId
	 * @return
	 */
	protected CustomerServicePrincipal createCustomerServicePrincipal(String profileId) {
		CustomerServicePrincipal cs = new CustomerServicePrincipal();
		cs.setName(profileId);
		String[] roles =
			new String[] {
				CustomerServicePrincipal
					.ROLE_SUPER_USER };
		cs.setRoles(roles);
		return cs;
	}

}
