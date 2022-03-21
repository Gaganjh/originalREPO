package com.manulife.pension.bd.web.activation;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.delegate.BDPublicSecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.platform.web.process.SimpleProcessState;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.bd.valueobject.BDSecurityInteractionRequestEx;
import com.manulife.pension.service.security.bd.valueobject.BDSecurityInteractionRequest.Status;
import com.manulife.pension.service.security.exception.FailedNTimesException;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.validator.ValidationError;

/**
 * The super class of simple activation process. These process involves: 1. A
 * login page to validate the user's identity 2. Invoke an activation handler to
 * activates the security request
 * 
 * @author guweigu
 * 
 */
abstract public class SimpleActivationProcessContext extends
		AbstractActivationProcessContext {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger
			.getLogger(SimpleActivationProcessContext.class);

	public static final String ProcessName = "security.activation";

	public static SimpleProcessState InputState = new SimpleProcessState(
			"input", "/do/activation/validation");
	public static SimpleProcessState CompleteState = new SimpleProcessState(
			"complete", URLConstants.PostLogin, true);
	public static SimpleProcessState CancelState = new SimpleProcessState(
			"cancel", URLConstants.HomeURL, true);

	static {
		InputState.addNext(ACTION_CONTINUE, CompleteState);
		InputState.addNext(ACTION_CANCEL, CancelState);
	}

	private String userId;
	private String password;
	// whether the process should be disabled
	private boolean disabled = false;
	/*
	 * Whether the broker is pending after forget pwd activation
	 */
	private boolean brokerPendingWarning = false;


	@Override
	public void populateForm(ActionForm form) throws SystemException {
		ActivationValidationForm f = (ActivationValidationForm) form;
		f.setDisabled(disabled);
		f.setBrokerPendingWarning(isBrokerPendingWarning());
		if (securityRequest != null) {
			switch (securityRequest.getType()) {
			case NewPasswordActivation:
				f.setContextContent(BDContentConstants.ACTIVATION_NEW_PASSWORD);
				break;
			case UserPartyActivation:
				f
						.setContextContent(BDContentConstants.ACTIVATION_ADD_BROKER_ENTITY);
				break;
			case UserActivation:
				BDUserRoleType role = securityRequest.getUserRoleType();
				if (role.compareTo(BDUserRoleType.BasicFinancialRep) == 0) {
					f
							.setContextContent(BDContentConstants.ACTIVATION_BASIC_BROKER);
				} else if (role.compareTo(BDUserRoleType.FinancialRep) == 0) {
					f.setContextContent(BDContentConstants.ACTIVATION_BROKER);
				}
				break;
			}
		}
	}

	@Override
	public void updateContext(ActionForm form) throws SystemException {
		ActivationValidationForm f = (ActivationValidationForm) form;
		userId = f.getUserId();
		password = f.getPassword();
	}

	/**
	 * Validate the user id, password matching the request's user profile
	 * information
	 * 
	 * @return
	 * @throws SystemException
	 */
	public List<ValidationError> validate(HttpServletRequest request ) throws SystemException {
		List<ValidationError> errors = new ArrayList<ValidationError>(2);
		// form validation
		if (StringUtils.isEmpty(userId)) {
			errors.add(new ValidationError("userId",
					CommonErrorCodes.EMPTY_USERNAME));
		}
		if (StringUtils.isEmpty(password)) {
			errors.add(new ValidationError("password",
					CommonErrorCodes.EMPTY_PASSWORD));
		}

		if (errors.isEmpty()) {
			try {
				String ipAddress = IPAddressUtils.getRemoteIpAddress(request);
				BDPublicSecurityServiceDelegate.getInstance()
						.validateInteractionRequest(
								getSecurityRequest().getId(), userId, password.trim(), ipAddress);
				
			} catch (SecurityServiceException ex) {
				if (ex.getCause() != null
						&& FailedNTimesException.class.getName().equalsIgnoreCase(ex.getCause().getClass().getName())) {
					getSecurityRequest().setStatus(Status.Failed);
				}
				log.debug("Fail to validate the user id and password", ex);
				errors
						.add(new ValidationError("",
								SecurityServiceExceptionHelper
										.getErrorCodeForCause(ex)));
			}
		}
		return errors;
	}

	@Override
	public ProcessState getStartState() {
		return InputState;
	}

	/**
	 * Defines the activation handler for different type of security request
	 * 
	 * @return
	 */
	abstract public ActivationHandler getActivationHandler();

	@Override
	public String getName() {
		return ProcessName;
	}

	public String getUserId() {
		return userId;
	}

	public String getPassword() {
		return password;
	}

	/**
	 * Set the disable flag. If the disable is already set to true,
	 * can not changed it back
	 * 
	 * @param disabled
	 */
	public void setDisabled(boolean disabled) {
		if (this.disabled) {
			return;
		}
		this.disabled = disabled;
	}

	@Override
	public void setSecurityRequest(
			BDSecurityInteractionRequestEx securityRequest) {
		super.setSecurityRequest(securityRequest);
		if (securityRequest != null
				&& securityRequest.getStatus().compareTo(Status.Open) == 0) {
			disabled = false;
		} else {
			disabled = true;
		}
	}

	public boolean isBrokerPendingWarning() {
		return brokerPendingWarning;
	}

	public void setBrokerPendingWarning(boolean brokerPendingWarning) {
		this.brokerPendingWarning = brokerPendingWarning;
	}
}
