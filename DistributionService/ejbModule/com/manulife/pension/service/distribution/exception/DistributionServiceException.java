package com.manulife.pension.service.distribution.exception;

import com.manulife.pension.exception.ApplicationException;

public abstract class DistributionServiceException extends ApplicationException {

	public DistributionServiceException(String message) {
		super(message);
	}

	public DistributionServiceException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public DistributionServiceException(String className, String methodName, String message) {
		super(className, methodName, message);
	}

	public DistributionServiceException(Throwable exception, String className, String methodName,
			String message) {
		super(exception, className, methodName, message);
	}

}
