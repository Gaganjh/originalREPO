/*
 * Created on Jun 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.service.iloans.exception;

import com.manulife.pension.exception.ApplicationException;

/**
 * @author sternlu
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class IloansServiceException extends ApplicationException {

	public IloansServiceException (String errorCode) {
		super(errorCode);
	}
}
