package com.manulife.pension.service.distribution.exception;

public class DistributionServiceVestingException extends
		DistributionServiceException {

	private static final long serialVersionUID = 1L;

	public DistributionServiceVestingException(String message,
			Throwable throwable) {
		super(message, throwable);
	}

	public DistributionServiceVestingException(String message) {
		super(message);
	}

	@Override
	public String getErrorCode() {
		return null;
	}

}
