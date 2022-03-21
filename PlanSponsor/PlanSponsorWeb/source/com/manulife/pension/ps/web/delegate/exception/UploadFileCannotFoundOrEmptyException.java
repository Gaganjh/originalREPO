package com.manulife.pension.ps.web.delegate.exception;

import com.manulife.pension.exception.ApplicationException;


public class UploadFileCannotFoundOrEmptyException extends ApplicationException {

	private String APP_DEFAULT_ERROR_CODE = "4001";

	public UploadFileCannotFoundOrEmptyException(String className, String methodName, String message) {
		super(className, methodName, message);		
	}
	
	public String getErrorCode() {
		//TODO: return appropriate code
		return APP_DEFAULT_ERROR_CODE;
	}
}

