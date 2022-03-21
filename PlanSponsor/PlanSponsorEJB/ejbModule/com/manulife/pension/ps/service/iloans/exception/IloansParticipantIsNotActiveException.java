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
	 *IloansParticipantIsNotActiveException    . Thrown by validation methods of IloansService
	 * Participant Status is not ‘AC’ (Active) [32.1]
	 */
	public class IloansParticipantIsNotActiveException   extends IloansServiceException{

		public IloansParticipantIsNotActiveException   (String arg)
		{
			super (arg);
		}
		/**
		 * @see com.manulife.pension.exception.LoggableException#getErrorCode()
		 */
		public String getErrorCode() {
			return "2327";
		}
	}
