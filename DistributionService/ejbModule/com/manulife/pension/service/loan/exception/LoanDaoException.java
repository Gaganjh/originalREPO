package com.manulife.pension.service.loan.exception;

import com.manulife.pension.service.distribution.exception.DistributionServiceException;

/**
 * LoanEmailException is used to signal exceptions that happen while generating
 * emails for the when status changes.
 * 
 * @author Dennis Snowdon
 */
public class LoanDaoException extends DistributionServiceException {

	@Override
	public String getErrorCode() {
		return "";
	}

	static final long serialVersionUID = 1L;

	public LoanDaoException(final String message) {
		super(message);
	}

	public LoanDaoException(final String message, final Throwable throwable) {
		super(message, throwable);
	}
}
