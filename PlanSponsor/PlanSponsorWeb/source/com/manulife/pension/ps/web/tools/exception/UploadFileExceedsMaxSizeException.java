package com.manulife.pension.ps.web.tools.exception;

import com.manulife.pension.exception.ApplicationException;
public class UploadFileExceedsMaxSizeException extends ApplicationException{


	private String APP_DEFAULT_ERROR_CODE = "4000";

	public UploadFileExceedsMaxSizeException(String className, String methodName, String message) {
		super(className, methodName, message);		
	}
	
	public String getErrorCode() {
		//TODO: return appropriate code
		return APP_DEFAULT_ERROR_CODE;
	}
}