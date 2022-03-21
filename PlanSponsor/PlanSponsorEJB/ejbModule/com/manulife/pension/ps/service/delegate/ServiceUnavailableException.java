package com.manulife.pension.ps.service.delegate;

import com.manulife.pension.exception.ApplicationException;

/**
 * @author Charles Chan
 */
public class ServiceUnavailableException extends ApplicationException {

	private static final int ERROR_CODE = 2123;

	/**
	 * Constructor.
	 * 
	 * @param className
	 * @param methodName
	 * @param message
	 */
	public ServiceUnavailableException(String className,
			String methodName, String message) {
		super(className, methodName, message);
	}

	public ServiceUnavailableException(Throwable throwable,
			String className, String methodName, String message) {
		super(throwable, className, methodName, message);
	}

	public String getErrorCode() {
		return String.valueOf(ERROR_CODE);
	}
}