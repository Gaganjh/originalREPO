/**
 * 
 */
package com.manulife.pension.bd.web.registration;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.manulife.pension.ezk.web.ActionForm;

import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.activation.AbstractActivationProcessContext;
import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.platform.web.process.SimpleProcessState;
import com.manulife.pension.service.security.bd.exception.BDFailedAlmostNTimesRegistrationException;
import com.manulife.pension.service.security.bd.exception.BDFailedNTimesRegistrationException;
import com.manulife.pension.service.security.bd.exception.BDRegistrationValidationException;
import com.manulife.pension.service.security.bd.exception.BDSecurityRequestCompleteException;
import com.manulife.pension.service.security.bd.exception.BDSecurityRequestExpiredException;
import com.manulife.pension.service.security.bd.exception.BDSecurityRequestFailedException;
import com.manulife.pension.service.security.bd.valueobject.RiaUserCreationValueObject;
import com.manulife.pension.service.security.exception.DisabledUserException;
import com.manulife.pension.service.security.exception.UserAlreadyRegisteredException;

/**
 * @author narintr
 *
 */
public class RegisterRiaUserProcessContext extends AbstractActivationProcessContext  {

	private static final long serialVersionUID = 1L;
	
	 // States definitions
    public final static String ProcessName = "security.registerRIAUser";

    public final static SimpleProcessState Step0 = new SimpleProcessState("step0",
            "/do/registerRiaUser/start");

    public final static SimpleProcessState Step1 = new SimpleProcessState("step1",
            "/do/registerRiaUser/step1");

    public final static SimpleProcessState Step2 = new SimpleProcessState("step2",
            "/do/registerRiaUser/step2");

    public final static SimpleProcessState Step3 = new SimpleProcessState("complete",
            "/do/registerRiaUser/step3");

    public final static SimpleProcessState Login = new SimpleProcessState("login",
            URLConstants.PostLogin, true);

    public final static SimpleProcessState StepCancel = new SimpleProcessState("cancel",
            URLConstants.HomeURL, true);

    public final static Map<String, Integer> ProcessSecurityServiceExceptionMapping = new HashMap<String, Integer>(
			17);
    
    static {
        Step0.addNext(ACTION_CONTINUE, Step1);
        Step1.addNext(ACTION_CONTINUE, Step2);
        Step1.addNext(ACTION_CANCEL, StepCancel);
        Step2.addNext(ACTION_CONTINUE, Step3);
        Step2.addNext(ACTION_CANCEL, StepCancel);
        Step3.addNext(ACTION_CONTINUE, Login);

        /*
         * Set up exception to error code mapping
         */
		ProcessSecurityServiceExceptionMapping.put(
				BDSecurityRequestCompleteException.class.getName(),
				BDErrorCodes.RIAUSER_REG_ACTIVATION_NOT_FOUND);
		ProcessSecurityServiceExceptionMapping.put(
				BDSecurityRequestFailedException.class.getName(),
				BDErrorCodes.RIAUSER_REG_CANCEL_CLICKED);
		ProcessSecurityServiceExceptionMapping.put(
				BDSecurityRequestExpiredException.class.getName(),
				BDErrorCodes.RIAUSER_REG_EXPIRED);				
		ProcessSecurityServiceExceptionMapping.put(
				BDRegistrationValidationException.class.getName(),
				BDErrorCodes.RIAUSER_REG_VALIDATION_FAIL);
		ProcessSecurityServiceExceptionMapping.put(
				BDFailedAlmostNTimesRegistrationException.class.getName(),
				BDErrorCodes.RIAUSER_REG_FAIL_ALMOST_NTIMES);
		ProcessSecurityServiceExceptionMapping.put(
				BDFailedNTimesRegistrationException.class.getName(),
				BDErrorCodes.RIAUSER_REG_LOCKED);
		ProcessSecurityServiceExceptionMapping.put(
				DisabledUserException.class.getName(),
				BDErrorCodes.RIAUSER_USER_PROFILE_DELETED);
		ProcessSecurityServiceExceptionMapping.put(
				UserAlreadyRegisteredException.class.getName(),
				BDErrorCodes.PROFILE_NOT_IN_NEW_STATUS);
    }
    
    private RiaUserCreationValueObject creationVO;
    
    private RegisterRiaUserValidationForm step1Form;
    
    private RegisterRiaUserStep2Form step2Form;
    
    /*
     * If the registration should be disabled
     */
    private boolean disabled = false;
    /**
     * The user name used for login
     */
    private String userName;

    /**
     * The password used for login
     */
    private String password;

    /**
     * Returns the start date
     */
    @Override
    public ProcessState getStartState() {
        return Step0;
    }

    /**
     * Returns the process name
     */
    @Override
    public String getName() {
        return ProcessName;
    }
	
	 /**
     * Populate the form from the context
     */
    @Override
    public void populateForm(ActionForm form) throws SystemException {
        ProcessState current = getCurrentState();
        if (Step1.isSameState(current)) {
            populateStep1Form(form);
        } else if (Step2.isSameState(current)) {
            populateStep2Form(form);
        } else if (Step3.isSameState(current)) {
            populateStep3Form(form);
        }
    }
    
    /**
     * populate the form for the Step1
     * 
     * @param form
     */
    private void populateStep1Form(ActionForm form) {
        RegisterRiaUserValidationForm registerRiaUserValidationForm = (RegisterRiaUserValidationForm) form;
        if (step1Form != null) {
        	registerRiaUserValidationForm.copyFrom(step1Form);
        }
        registerRiaUserValidationForm.setDisabled(isDisabled());
    }

    /**
     * populate the form for the Step2
     * 
     * @param form
     */
    private void populateStep2Form(ActionForm form) {
        RegisterRiaUserStep2Form registerRiaUserStep2Form = (RegisterRiaUserStep2Form) form;
        registerRiaUserStep2Form.setCreationVO(creationVO);
        if (step2Form != null) {
        	registerRiaUserStep2Form.copyFrom(step2Form);
        }
    }

    /**
     * populate the form for the Step3
     * 
     * @param form
     */
    private void populateStep3Form(ActionForm form) {
        RegistrationCompleteForm completeForm = (RegistrationCompleteForm) form;
        if (!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password)) {
            completeForm.setSuccess(true);
        } else {
            completeForm.setSuccess(false);
        }
    }

    /**
     * Update the process context from the Form of the state
     */
    @Override
    public void updateContext(ActionForm form) throws SystemException {
        if (Step1.isSameState(getCurrentState())) {
            updateContextStep1(form);
        } else if (Step2.isSameState(getCurrentState())) {
            updateContextStep2(form);
        }
    }
    
    /**
     * Update the context step1
     * 
     * @param form
     * @throws SystemException
     */
    private void updateContextStep1(ActionForm form) throws SystemException {
        RegisterRiaUserValidationForm registerRiaUserValidationForm = (RegisterRiaUserValidationForm) form;
        if (step1Form == null) {
            step1Form = new RegisterRiaUserValidationForm();
        }
        step1Form.copyFrom(registerRiaUserValidationForm);
    }

    /**
     * Update the context step2
     * 
     * @param form
     * @throws SystemException
     */
    private void updateContextStep2(ActionForm form) throws SystemException {
        RegisterRiaUserStep2Form registerRiaUserStep2Form = (RegisterRiaUserStep2Form) form;
        if (step2Form == null) {
            step2Form = new RegisterRiaUserStep2Form();
        }
        step2Form.copyFrom(registerRiaUserStep2Form);
    }
    
    /**
     * Sets the current state
     * 
     * @param currentState
     */
    @Override
    public void setCurrentState(ProcessState currentState) {
        super.setCurrentState(currentState);
        if (!Step1.isSameState(currentState)) {
            step1Form = null; // clear it
        }

        if (!Step2.isSameState(currentState)) {
            step2Form = null;
        }
    }

	/**
	 * @return the riaUserCreationValueObject
	 */
	public RiaUserCreationValueObject getRiaUserCreationValueObject() {
		return creationVO;
	}

	/**
	 * @param riaUserCreationValueObject the riaUserCreationValueObject to set
	 */
	public void setRiaUserCreationValueObject(
			RiaUserCreationValueObject creationVO) {
		this.creationVO = creationVO;
	}

	/**
	 * @return the disabled
	 */
	public boolean isDisabled() {
		return disabled;
	}

	/**
	 * @param disabled the disabled to set
	 */
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
    
}
