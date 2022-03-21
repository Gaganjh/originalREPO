package com.manulife.pension.ps.web.delegate.exception;

import com.manulife.pension.exception.ApplicationException;


public class UnableToAccessIFileException extends ApplicationException {

	private String ERROR_CODE = "2377";

	public UnableToAccessIFileException(String className, String methodName, String message) {
		super(className, methodName, message);		
	}
	
	public String getErrorCode() {
		return ERROR_CODE;
	}
}

