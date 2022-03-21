package com.manulife.pension.service.distribution.exception;

public class DistributionServiceDaoException extends DistributionServiceException {

	private static final long serialVersionUID = 1L;

	public DistributionServiceDaoException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public DistributionServiceDaoException(String string) {
		super(string);
	}

	@Deprecated
	public DistributionServiceDaoException(String className, String methodName,
			String message) {
		super(className, methodName, message);
	}

	@Deprecated
	public DistributionServiceDaoException(Throwable exception,
			String className, String methodName, String message) {
		super(exception, className, methodName, message);
	}

	@Override
	public String getErrorCode() {
		return null;
	}

}
