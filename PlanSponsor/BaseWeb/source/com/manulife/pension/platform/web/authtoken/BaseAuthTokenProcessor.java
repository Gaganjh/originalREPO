/**
 * 
 */
package com.manulife.pension.platform.web.authtoken;

/**
 * @author murisai
 *
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;

/**
 * Base class for concrete implementation of AuthTokenProcessor
 *
 */
public abstract class BaseAuthTokenProcessor implements AuthTokenProcessor {

	
	private static Logger logger = Logger
			.getLogger(com.manulife.pension.platform.web.authtoken.BaseAuthTokenProcessor.class);

	/**
	 * In order to verify JWT token, get the bearer token.
	 * 
	 * @return bearer token if successful otherwise null
	 */
	public String getBearerToken(String existingTrustStore, String existingTrustStorePwd,
			String clientId, String clientSecret ) {

		logger.info("Entry getBearerToken......");
		String bearerToken = null;
		HttpsURLConnection connection = null;
		
		String uriString = AuthTokenUtility.BEARER_TOKEN_SERVICE_URL;
		String grantType = AuthTokenUtility.BEARER_TOKEN_SERVICE_GRANT_TYPE;

		try {
			// Verify parameters for the services that provides bearer token
			checkServiceConnectionParameters(uriString, clientId, clientSecret, grantType);

			// get the connection
			connection = AuthTokenHttpsConnection.getSecureConnection(uriString, CommonConstants.HTTP_REQUEST_POST);
			if (connection == null) {
				throw new SystemException(
						"Unable to establish secure connection to the service for bearer token (connection is null): " + uriString);
			}

			String clientCredentials = "{\"client_id\": \"" + clientId + "\", \"client_secret\": \"" + clientSecret
					+ "\", \"grant_type\": \"" + grantType + "\"}";

			openConnectionStream(connection, clientCredentials, CommonConstants.CAS_BEARER_TOKEN_SERVICE_CONNECTION_ERROR);

			JsonObject jsonObject = getJSONObjectFromResponse(connection);

			// throw error if no response
			if (jsonObject == null) {
				throw new SystemException("Error in retrieving Bearer Token, no response from the Service that provides Bearer token.");
			}

			// Get the Bearer token value
			bearerToken = getJsonElementValueAsString(jsonObject, CommonConstants.BEARER_TOKEN);

			/*AuthTokenLog.logInteraction("BaseAuthTokenProcessor","getBearerToken", "",
					"Successfully retrieved Bearer Token : " + bearerToken, "");*/
			logger.info("Bearer token : " + bearerToken);

		} catch (Exception e) {
			logger.error("Error in getting bearer Token: " + e.getMessage(), e);

		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			
			// revert back the existing trust store properties
			AuthTokenHttpsConnection.setTrustStoreProperties(existingTrustStore, existingTrustStorePwd);
		}
		logger.info("Exit  getBearerToken.....");
		return bearerToken;
	}

	/**
	 * Checks Bearer Token Service connection parameters.
	 * 
	 * @param uriString
	 * @param clientId
	 * @param clientSecret
	 * @param grantType
	 * @throws SystemException
	 */
	public void checkServiceConnectionParameters(String uriString, String clientId, String clientSecret,
			String grantType) throws SystemException {
		if (StringUtils.isBlank(uriString) || StringUtils.isBlank(clientId) || StringUtils.isBlank(clientSecret)
				|| StringUtils.isBlank(grantType)) {
			throw new SystemException(
					"Error in getting bearer token service connection parameters from namespace bindings: "
							+ "[uriString: " + uriString + "], " + "[clientId: " + clientId + "], " + "[clientSecret: "
							+ (StringUtils.isBlank(clientSecret) ? "null"
									: clientSecret.charAt(0) + "*****" + clientSecret.charAt(clientSecret.length() - 1))
							+ "], " + "[grantType: " + grantType + "].");
		}
	}

	/**
	 * Opens the connection Stream.
	 * 
	 * @param connection
	 * @param outputData
	 * @throws SystemException
	 */
	public void openConnectionStream(HttpsURLConnection connection, String outputData, String errorMsg)
			throws SystemException {
		try {
			OutputStream outputStream = connection.getOutputStream();
			outputStream.write(outputData.getBytes("UTF-8"));
		} catch (IOException e) {
			// unable to open output stream
			String detailedErrorMsg = AuthTokenUtility.getErrorMessageWithStackTrace(errorMsg, e);
			logger.error(detailedErrorMsg);
			throw new SystemException(e, detailedErrorMsg);
		}
	}

	/**
	 * This method will read the response from an HttpURLConnection and parse it
	 * into a JsonObject
	 * 
	 * @param connection
	 * @return JsonObject
	 * @throws SystemException
	 */
	public JsonObject getJSONObjectFromResponse(HttpURLConnection connection) throws SystemException {

		JsonObject jsonObject = null;
		InputStream response = null;
		int statusCode = -1;

		if (connection == null) {
			logger.error("HttpURLConnection is null");
			throw new SystemException("HttpURLConnection is null.");
		}

		try {
			response = connection.getInputStream();
			statusCode = connection.getResponseCode();
		} catch (IOException ioe) {
			logger.error("Unexpected IOException " + connection.getURL());
			throw new SystemException(ioe,
					"Unexpected IOException when reading input stream from: " + connection.getURL());
		}

		if (statusCode != 201 && statusCode != 200) {
			// unsuccessful REST request
			logger.error("HttpURLConnection error " + connection.getURL() + " with error code : " + statusCode);
			throw new SystemException("HTTP error in response from HttpURLConnection " + connection.getURL()
					+ " with error code : " + statusCode);
		}

		if (response == null) {
			logger.error("No response from HttpURLConnection: " + connection.getURL());
			throw new SystemException("Unable to retrieve response from HttpURLConnection: " + connection.getURL());
		}

		String contentType = connection.getHeaderField("Content-Type");
		String charset = "UTF-8";

		for (String param : contentType.replace(" ", "").split(";")) {
			if (param.startsWith("charset=")) {
				charset = param.split("=", 2)[1];
				break;
			}
		}
		StringBuilder responseContent = new StringBuilder();
		if (charset != null) {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(response, charset));
				for (String line; (line = reader.readLine()) != null;) {
					responseContent.append(line);
				}
			} catch (IOException e) {
				logger.error("Unable to read response from connection: " + connection.getURL());
				throw new SystemException(e, "Unable to read response from connection: " + connection.getURL());
			}
		}

		if (responseContent.length() > 0) {
			String jsonStringStripped = responseContent.toString().trim().replace("\"{", "{").replace("}\"", "}")
					.replace("\\", "");
			jsonObject = (new JsonParser()).parse(jsonStringStripped).getAsJsonObject();
		} else {
			// empty response - no action required
			logger.error("No response from connection: " + connection.getURL());
			throw new SystemException("Empty response from connection: " + connection.getURL());
		}

		return jsonObject;

	}

	/**
	 * Returns a string value from a JsonObject for a given element with stripped
	 * quotes
	 * 
	 * @param jsonObject
	 * @param elementName
	 * @return String value of given Json element
	 */
	public String getJsonElementValueAsString(JsonObject jsonObject, String elementName) {
		String value = null;
		JsonElement el = jsonObject.get(elementName);
		if (el != null && !el.isJsonNull())
			value = el.toString().replaceAll("\"", "");
		return value;
	}
	/**
	 * Method to generate the JWT Token 
	 * @return String
	 */
	 public abstract String generateAuthToken(int contractNumber, String contractAccessCode, String application) throws SystemException;
	 
}
