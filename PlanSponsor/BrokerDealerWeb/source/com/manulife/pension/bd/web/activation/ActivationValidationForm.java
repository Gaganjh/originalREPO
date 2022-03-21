package com.manulife.pension.bd.web.activation;

import org.apache.commons.lang.StringUtils;
import com.manulife.pension.ezk.web.ActionForm;

/**
 * The form for activation validations 
 * It includes the userId and password validation
 * 
 * @author guweigu
 * 
 */
public class ActivationValidationForm implements ActionForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userId;
	private String password;
	/*
	 * The content id for different type of activation
	 */
	private int contextContent;
	
	/*
	 * Whether the form should be disabled. The submit will be suppressed
	 */
	private boolean disabled;
	
	/*
	 * Whether the broker is pending after forget pwd activation
	 */
	private boolean brokerPendingWarning = false;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = StringUtils.trimToEmpty(userId);
	}

	public String getPassword() {
		return password;
	}

	// password should not be trimmed
	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public int getContextContent() {
		return contextContent;
	}

	public void setContextContent(int contextContent) {
		this.contextContent = contextContent;
	}

	public boolean isBrokerPendingWarning() {
		return brokerPendingWarning;
	}

	public void setBrokerPendingWarning(boolean brokerPendingWarning) {
		this.brokerPendingWarning = brokerPendingWarning;
	}
}
