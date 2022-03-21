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
	 *IloansContractNotSetForLoansException . Thrown by validation methods of IloansService
	 *  If the Contract has a Product Feature of ‘LRK01’ or ‘LRK02’ (does not allow loans) [31.2]
	 *   Display error message ‘NNNN The contract does not allow loans.’	

	 */
	public class IloansContractDoesNotAllowLoansException  extends IloansServiceException{

		public IloansContractDoesNotAllowLoansException(String arg)
		{
			super (arg);
		}
		/**
		 * @see com.manulife.pension.exception.LoggableException#getErrorCode()
		 */
		public String getErrorCode() {
			return "2326";
		}
	}

