/**
 * 
 */
package com.manulife.pension.platform.web.authtoken;

import java.net.URL;
import java.security.KeyStore;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.util.log.LogUtility;

/**
 * @author murisai
 *
 */
public class AuthTokenHttpsConnection {

	
	/**
     * Logger
     **/
    private static Logger logger = Logger.getLogger(AuthTokenHttpsConnection.class);
    
    /**
     * Private Constructor to prevent accidental instantiation of the class.
     */
    private AuthTokenHttpsConnection() {
    	
    }

	/**
	 * Retrieves the Secure Connection 
	 * 
	 * @param uriString url to be connected
	 * @param httpRequestMethod GET or POST
	 * @return HttpsURLConnection
	 * @throws SystemException
	 */
	public static HttpsURLConnection getSecureConnection(String uriString, String httpRequestMethod)
			throws SystemException {
		try {
			
			SSLSocketFactory sslsocketfactory = getSSLSocketFactory();
			if (sslsocketfactory == null) {
				throw new SystemException("SSLSocketFactory is null for connecting to Bearer Token Service.");
			}
			URL url = new URL(uriString);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setSSLSocketFactory(sslsocketfactory);
			connection.setDoOutput(true);
			connection.setRequestMethod(httpRequestMethod);
			connection.setRequestProperty("Accept-Charset", "UTF-8");
			connection.setRequestProperty("Content-Type", "application/json");
			return connection;
		} catch (Exception e) {
			LogUtility.logSystemException("Unable to establish a secure connection to Token service" + uriString ,new SystemException(e.getMessage()));
			// unable to create a secure connection
			logger.error("Unable to establish a secure connection to Token service: "+ uriString +"Exception: " +e);
			//AuthTokenLogger.logInteraction("AuthTokenHttpsConnection,getSecureConnection", "Not able to establish a secure connection to Token service: "+ uriString +", Exception: " +e, "","");
			throw new SystemException(e, "Unable to establish a secure connection to Token service: " + uriString);
		}
	}
	
	/**
	 * Retrieve an SSL socket factory for secure connections
	 */
	private static SSLSocketFactory getSSLSocketFactory() throws SystemException {

		SSLSocketFactory sslsocketfactory = null;

		String javaHome = System.getProperty("java.home");
		System.setProperty("javax.net.ssl.trustStore", javaHome + "/lib/security/cacerts");
		System.setProperty("javax.net.ssl.trustStorePassword", CommonConstants.JRE_TRUST_STORE_DEFAULT_KEY);
		
		try {
			TrustManagerFactory trustManagerFactory = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init((KeyStore) null);
			TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
			
			if (AuthTokenUtility.TOKEN_SERVICE_SSL_CIPHER == null) {
				//AuthTokenLogger.logInteraction("AuthTokenHttpsConnection,getSSLSocketFactory", "Error in retrieving the token service ssl cipher from namespace bindings.","","");
				throw new SystemException("Error in retrieving token service ssl cipher from namespace bindings.");
			}
			SSLContext sslContext = SSLContext.getInstance(AuthTokenUtility.TOKEN_SERVICE_SSL_CIPHER);
			sslContext.init(null, trustManagers, null);
			sslsocketfactory = sslContext.getSocketFactory();
		} catch (Exception e) {
			// unable to create an SSL socket factory
			LogUtility.logSystemException("Unable to create SSLSocketFactory Object :",
					new SystemException(e, e.getMessage()));
			throw new SystemException(e, "Unable to create SSLSocketFactory");
		}

		return sslsocketfactory;
	}
	
	/**
	 * Sets the trust stored properties
	 * 
	 * @param trustStore
	 * @param trustStoreKey
	 */
	public static void setTrustStoreProperties(String trustStore, String trustStoreKey) {
		try {
			System.setProperty("javax.net.ssl.trustStore", trustStore);
			System.setProperty("javax.net.ssl.trustStorePassword", trustStoreKey);
		} catch (Exception e) {
			logger.info("Cannot set trust store propererties");
		}		
	}
}
