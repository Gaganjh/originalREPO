/*
 * Created on Jun 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.service.iloans.exception;

/**
 * @author sternlu
 *
 *IloansUnexpiredLoanRequestsException. Thrown by validation methods of IloansService
 * there is another un-expired loan request for the same participant 
 * with a loan request status not = Not Proceed [32.2]
 */
public class IloansUnexpiredLoanRequestsException extends IloansServiceException{

	public IloansUnexpiredLoanRequestsException(String arg)
	{
		super (arg);
	}
	/**
	 * @see com.manulife.pension.exception.LoggableException#getErrorCode()
	 */
	public String getErrorCode() {
		return "2328";
	}
}
