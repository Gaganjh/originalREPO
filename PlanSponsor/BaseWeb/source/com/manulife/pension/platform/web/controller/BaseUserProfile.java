package com.manulife.pension.platform.web.controller;

import java.io.Serializable;

import com.manulife.pension.service.security.AbstractPrincipal;
import com.manulife.pension.service.security.valueobject.UserPreferences;

/**
 * UserProfile common for psw/bd application
 * 
 * @author guweigu
 *
 */
public class BaseUserProfile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected AbstractPrincipal principal;

	protected UserPreferences preferences;

	protected BaseUserProfile() {
		preferences = new UserPreferences();
	}

	/**
	 * Gets the principal
	 * @return Returns a Principal
	 */
	public AbstractPrincipal getAbstractPrincipal() {
		return this.principal;
	}

	/**
	 * Sets the principal
	 * @param principal The principal to set
	 */
	public void setAbstractPrincipal(AbstractPrincipal principal) {
		this.principal = principal;
	}
	
    /**
     * @return Returns the preferences.
     */
    public UserPreferences getPreferences() {
        return preferences;
    }

    /**
     * @param preferences The preferences to set.
     */
    public void setPreferences(UserPreferences preferences) {
        this.preferences = preferences;
    }	
}
