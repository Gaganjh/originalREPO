package com.manulife.pension.bd.web.util;

import com.manulife.pension.security.cryptography.DESedeCipher;

/**
 * @author kuthiha
 *
 */
public class LoginCookieEncryptorDecryptor {
	
    
    private static final String   key = "F2F80AEE931B4C6FE1A65C4AD5FDB37BF0EB57C7D945CD70";
    private static final String   initVector = "BE9F2DFC263F2AC9";
    
    
    public LoginCookieEncryptorDecryptor () {
    	
    }
    
    /**
     * This method encrypts the login name.
     * 
     * @param stringToEncrypt
     * @return string (encrypted)
     */
    public static String encrypt (String stringToEncrypt) {
    	return DESedeCipher.encrypt(stringToEncrypt, key, initVector);
    }
    
    /**
     * This method decrypts the string encrypted by the encrypt method.
     * @param stringToDecrypt
     * @return string (clear text)
     */
    public static String decrypt (String stringToDecrypt) {
    	return DESedeCipher.decrypt(stringToDecrypt, key, initVector);
    }
    
}
