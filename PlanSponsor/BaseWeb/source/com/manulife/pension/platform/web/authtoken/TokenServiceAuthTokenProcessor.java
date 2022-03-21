/**
 * 
 */
package com.manulife.pension.platform.web.authtoken;

import java.util.UUID;
import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.JsonObject;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.util.log.LogUtility;

/**
 * @author murisai
 *
 */
public class TokenServiceAuthTokenProcessor extends BaseAuthTokenProcessor implements AuthTokenProcessor {

	private static Logger logger = Logger
			.getLogger(com.manulife.pension.platform.web.authtoken.TokenServiceAuthTokenProcessor.class);
	

	/**
	 * This method will make a call to the TRS Token service to validate the token
	 * and will fill in token validation information in the VO
	 * 
	 * @param token
	 * @return AuthTokenVO which holds various attributes of token including
	 *         validity and expiration.
	 * @throws SystemException 
	 */
	@Override
	public String generateAuthToken(int contractNumber, String contractAccessCode, String application) throws SystemException {

		logger.info("Entry validateAuthToken.......");

		HttpsURLConnection connection = null;
		String token = null;
		String uriString = null;
		String existingTrustStore = System.getProperty("javax.net.ssl.trustStore");
		String existingTrustStorePwd = System.getProperty("javax.net.ssl.trustStorePassword");
		String uuid = UUID.randomUUID().toString();
		String clientId = null;
		String clientSecret = null;
		
		try {
			// Get the bearer token, needed to validate the JWT token
			// Get bearer token connection string
			
			if (StringUtils.equalsIgnoreCase(application, "PSW")) {
				clientId = AuthTokenUtility.GENERATE_BEARER_TOKEN_SERVICE_PSW_CLIENT_ID;
				clientSecret = AuthTokenUtility.GENERATE_BEARER_TOKEN_SERVICE_PSW_CLIENT_SECRET;
			} else {
				clientId = AuthTokenUtility.GENERATE_BEARER_TOKEN_SERVICE_FRW_CLIENT_ID;
				clientSecret = AuthTokenUtility.GENERATE_BEARER_TOKEN_SERVICE_FRW_CLIENT_SECRET;
			}
			
			String bearerToken = getBearerToken(existingTrustStore, existingTrustStorePwd, clientId, clientSecret);

			// set valid to true to ensure token is processed only when there is no error
			
			if(StringUtils.equalsIgnoreCase(application, "PSW")) {
			
			 uriString = AuthTokenUtility.JWT_TOKEN_SERVICE_URL + "?AppId="
					+ AuthTokenUtility.JWT_TOKEN_SERVICE_PSW_APP_ID;
			} else {
			
			 uriString = AuthTokenUtility.JWT_TOKEN_SERVICE_URL + "?AppId="
					+ AuthTokenUtility.JWT_TOKEN_SERVICE_FRW_APP_ID;
			}

			// get the connection
			connection = AuthTokenHttpsConnection.getSecureConnection(uriString, CommonConstants.HTTP_REQUEST_POST);

			// Set up the bearer token
			connection.setRequestProperty("Authorization", "Bearer " + bearerToken);
			
			String generateTokenParams = "{\"input1\": \"" + contractNumber + "\", \"input2\": \"" + contractAccessCode + "\",\"input3\": \""+ application + "\", \"input4\": \"" + uuid + "\"}";
			
			// open connection stream
			openConnectionStream(connection, generateTokenParams, CommonConstants.BEARER_TOKEN_SERVICE_CONNECTION_ERROR);

			JsonObject jsonObject = getJSONObjectFromResponse(connection);

			// throw error if no response
			if (jsonObject == null) {
				logger.error("Error in Token generation, no response from Token Service.");
				throw new SystemException("Error in Token generation, no response from Token Service.");
			}
				token =  getJsonElementValueAsString(jsonObject, "details");
				
			} catch (Exception e) {
				// cannot validate Token
				LogUtility.logSystemException("Cannot Generate JWT Token for contractNumber: " + contractNumber + " : Unexpected exception", new SystemException(e,
						"Cannot Generate JWT Token: Unexpected exception: " + e.getMessage()));

				throw new SystemException(e, "Cannot Generate JWT Token - Unexpected exception: " + e.getMessage());

			} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		logger.info("Exit validateAuthToken.......");
		return token;
	}
	
}
