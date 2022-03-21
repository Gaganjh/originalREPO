package com.manulife.pension.service.loan.exception;

import com.manulife.pension.service.distribution.exception.DistributionServiceException;

/**
 * LoanEmailException is used to signal exceptions that happen while generating
 * emails for the when status changes.
 * 
 * @author Dennis Snowdon
 */
public class LoanValidationException extends DistributionServiceException {

	@Override
	public String getErrorCode() {
		return "";
	}

	static final long serialVersionUID = 1L;

	public LoanValidationException(final String message) {
		super(message);
	}

	public LoanValidationException(final String message,
			final Throwable throwable) {
		super(message, throwable);
	}
}
