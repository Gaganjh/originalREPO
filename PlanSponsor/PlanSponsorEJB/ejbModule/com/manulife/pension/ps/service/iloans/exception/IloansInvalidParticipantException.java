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
	 *IloansPareticipantIsNotOnContractException    . Thrown by validation methods of IloansService
	 * The SSN must be valid (found) for the input Contract number	
	 */
	public class IloansInvalidParticipantException   extends IloansServiceException{

		public IloansInvalidParticipantException(String arg)
		{
			super (arg);
		}
		/**
		 * @see com.manulife.pension.exception.LoggableException#getErrorCode()
		 */
		public String getErrorCode() {
			return "2324";
		}
	}