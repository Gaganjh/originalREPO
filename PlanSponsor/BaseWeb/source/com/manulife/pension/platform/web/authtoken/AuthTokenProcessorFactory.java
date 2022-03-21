/**
 * 
 */
package com.manulife.pension.platform.web.authtoken;

/**
 * @author murisai
 *
 */
import com.manulife.pension.platform.web.authtoken.AuthTokenProcessor;
import com.manulife.pension.platform.web.authtoken.TokenServiceAuthTokenProcessor;

/**
 * Factory for Authentication Token processor framework. 
 * Any future different implementation of authentication token processing can be switched here and 
 * used without modifying existing implementation.
 * 
 * This is singleton class to ensure one instance of factory invoked.
 *
 *
 */
public class AuthTokenProcessorFactory {
	
	private static AuthTokenProcessorFactory authTokenFactoryInstance;

	/**
	 * Prevent anyone from instantiation except children
	 */
	protected AuthTokenProcessorFactory() {
		
	}
	
	/**
	 * Return singleton instance if does not exists then instantiates.
	 * @return AuthTokenProcessorFactory singleton instance
	 */
	public static AuthTokenProcessorFactory getInstance() {
		if (authTokenFactoryInstance == null) {
			authTokenFactoryInstance = new AuthTokenProcessorFactory();
	    }
		return authTokenFactoryInstance;
	}
	
	/**
	 * Returns concrete implementation of authentication token processor
	 * 
	 * @return concrete implementation of authentication token processor.
	 */
	public AuthTokenProcessor getAuthTokenProcessor() {
		return new TokenServiceAuthTokenProcessor();
	}

}



