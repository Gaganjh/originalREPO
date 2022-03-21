package com.manulife.pension.ps.service.report.exception;

import com.manulife.pension.service.report.exception.ReportServiceException;

/**
 * @author parkand
 */
public class MultipleAddressesForParticipantException extends
		ReportServiceException {

	/**
	 * @param arg0
	 */
	public MultipleAddressesForParticipantException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public MultipleAddressesForParticipantException(String arg0, String arg1,
			String arg2) {
		super(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public MultipleAddressesForParticipantException(Throwable arg0,
			String arg1, String arg2, String arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	/* (non-Javadoc)
	 * @see com.manulife.pension.exception.LoggableException#getErrorCode()
	 */
	public String getErrorCode() {
		return "2372";
	}

}
