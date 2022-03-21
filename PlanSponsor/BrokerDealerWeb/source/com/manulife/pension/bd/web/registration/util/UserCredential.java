package com.manulife.pension.bd.web.registration.util;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * This is a value object class for user credential info
 * 
 * @author guweigu
 * 
 */
public class UserCredential implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String userId="";

    private String password="";

    private String confirmedPassword="";

    /**
     * Returns the userId
     * 
     * @return String
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the userId
     * 
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = StringUtils.trimToEmpty(userId);
    }

    /**
     * Returns the password
     * 
     * @return String
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password
     * 
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the confirmedPassword
     * 
     * @return String
     */
    public String getConfirmedPassword() {
        return confirmedPassword;
    }

    /**
     * Sets the confirmedPassword
     * 
     * @param confirmedPassword
     */
    public void setConfirmedPassword(String confirmedPassword) {
        this.confirmedPassword = confirmedPassword;
    }

    /**
     * Copy data from one object to another
     * 
     * @param src
     */
    public void copyFrom(UserCredential src) {
        this.userId = src.userId;
        this.password = src.password;
        this.confirmedPassword = src.confirmedPassword;
    }
}
