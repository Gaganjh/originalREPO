package com.manulife.pension.ps.service.report.exception;

import com.manulife.pension.service.report.exception.ReportServiceException;
/**
 * The exception is thrown when the two contract numbers do not match.
 * @author Samir Kolarkar
 */
public class ContractNumberDoesNotmatchException extends ReportServiceException {

	/**
	 * Constructor.
	 * @param arg0
	 */
	public ContractNumberDoesNotmatchException(String arg0) {
		super(arg0);
	}

	/**
	 * Constructor.
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public ContractNumberDoesNotmatchException(String arg0, String arg1,
			String arg2) {
		super(arg0, arg1, arg2);
	}

	/**
	 * Constructor.
	 * @param cause
	 * @param className
	 * @param methodName
	 * @param details
	 */
	public ContractNumberDoesNotmatchException(Throwable cause, String className,
			String methodName, String details) {
		super(cause, className, methodName, details);
	}

	/**
	 * @see com.manulife.pension.exception.LoggableException#getErrorCode()
	 */
	public String getErrorCode() {
		return "1047";
	}

}
