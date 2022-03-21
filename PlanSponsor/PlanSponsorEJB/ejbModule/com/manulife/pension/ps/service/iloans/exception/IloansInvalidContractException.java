 /** Created on Jun 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.service.iloans.exception;

/**
 * @author sternlu
 *
 *IIloansContractIsNotActiveException. Thrown by validation methods of IloansService
 * Contract number must be valid (found)
 * current signed-in user is not a TPA Firm user of the Contract TPA Firm [31.3]
 *  Contract Staff Plan Indicator = Y and the current signed-in user does not have for the Contract TPA Firm a permission grant 
 *  with security task permission code = TSPA (TPA Staff Plan Access) [31.4]
 */
public class IloansInvalidContractException extends IloansServiceException{

	public IloansInvalidContractException(String arg)
	{
		super (arg);
	}
	/**
	 * @see com.manulife.pension.exception.LoggableException#getErrorCode()
	 */
	public String getErrorCode() {
		return "2323";
	}
}