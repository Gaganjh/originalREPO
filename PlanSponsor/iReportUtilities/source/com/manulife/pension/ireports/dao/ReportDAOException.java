package com.manulife.pension.ireports.dao;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class ReportDAOException extends NestableRuntimeException {

	private static final long serialVersionUID = 4628319844200259511L;

	public ReportDAOException() {
		super();
	}
	
	public ReportDAOException(String message) {
		super(message);
	}

	public ReportDAOException(String message, Throwable cause) {
		super(message, cause);
	}

}
