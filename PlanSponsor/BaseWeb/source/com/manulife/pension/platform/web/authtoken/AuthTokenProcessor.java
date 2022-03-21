/**
 * 
 */
package com.manulife.pension.platform.web.authtoken;

import com.manulife.pension.exception.SystemException;

/**
 * High level interface for authentication token processing framework. Any concrete implementation of authentication token processing should implement this 
 * interface and app controller should only rely on this interface operations for authentication token processing.
 *
 */
public interface AuthTokenProcessor {

	/**
	 * Method to generate the JWT Token 
	 * @param profileID 
	 * @param contractId 
	 * @return String
	 */
	 public  String generateAuthToken(int contractNumber, String contractAccessCode, String application) throws SystemException;
	 
	 
	
}

