package com.manulife.pension.ps.service.delegate;

import com.manulife.pension.ps.common.util.Constants;
import com.manulife.pension.service.delegate.AbstractServiceDelegate;


public abstract class PsAbstractServiceDelegate extends AbstractServiceDelegate {

	protected String getApplicationId() {
		return Constants.PS_APPLICATION_ID;
	}
}
