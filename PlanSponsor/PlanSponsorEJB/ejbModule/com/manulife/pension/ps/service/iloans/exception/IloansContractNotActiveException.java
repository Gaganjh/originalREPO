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
	 *IloansWrongTpaUserException  . Thrown by validation methods of IloansService
	 * If the Contract Status is not ‘AC’ (Active) [30.1]
	 * Display error message ‘NNNN The contract status is not active.’ 	

	 */
	public class IloansContractNotActiveException   extends IloansServiceException{

		public IloansContractNotActiveException (String arg)
		{
			super (arg);
		}
		/**
		 * @see com.manulife.pension.exception.LoggableException#getErrorCode()
		 */
		public String getErrorCode() {
			return "2325";
		}
	}


