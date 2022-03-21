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
import com.manulife.pension.service.security.bd.exception.BDFailedNTimesRegistrationException;
import com.manulife.pension.service.security.bd.exception.BDRegistrationValidationException;
import com.manulife.pension.service.security.bd.exception.BDSecurityRequestCompleteException;
import com.manulife.pension.service.security.bd.exception.BDSecurityRequestExpiredException;
import com.manulife.pension.service.security.bd.exception.BDSecurityRequestFailedException;
import com.manulife.pension.service.security.bd.valueobject.FirmRepCreationValueObject;
import com.manulife.pension.service.security.exception.DisabledUserException;
import com.manulife.pension.service.security.exception.UserAlreadyRegisteredException;

/**
 * The process context for the Firm Rep registration process
 * 
 * @author guweigu
 * 
 */
public class RegisterFirmRepProcessContext extends AbstractActivationProcessContext {

    private static final long serialVersionUID = 1L;

    // States definitions
    public final static String ProcessName = "security.registerFirmRep";

    public final static SimpleProcessState Step0 = new SimpleProcessState("step0",
            "/do/registerFirmRep/start");

    public final static SimpleProcessState Step1 = new SimpleProcessState("step1",
            "/do/registerFirmRep/step1");

    public final static SimpleProcessState Step2 = new SimpleProcessState("step2",
            "/do/registerFirmRep/step2");

    public final static SimpleProcessState Step3 = new SimpleProcessState("complete",
            "/do/registerFirmRep/step3");

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
				BDErrorCodes.FIRMREP_REG_REGISTERED);
		ProcessSecurityServiceExceptionMapping.put(
				BDSecurityRequestExpiredException.class.getName(),
				BDErrorCodes.FIRMREP_REG_EXPIRED);
		ProcessSecurityServiceExceptionMapping.put(
				BDSecurityRequestFailedException.class.getName(),
				BDErrorCodes.FIRMREP_REG_LOCKED);
		ProcessSecurityServiceExceptionMapping.put(
				BDFailedNTimesRegistrationException.class.getName(),
				BDErrorCodes.FIRMREP_REG_LOCKED);				
		ProcessSecurityServiceExceptionMapping.put(
				BDRegistrationValidationException.class.getName(),
				BDErrorCodes.FIRMREP_REG_VALIDATION_FAIL);
		ProcessSecurityServiceExceptionMapping.put(
				DisabledUserException.class.getName(),
				BDErrorCodes.ACTIVATION_PROFILE_DELETED);
		ProcessSecurityServiceExceptionMapping.put(
				UserAlreadyRegisteredException.class.getName(),
				BDErrorCodes.PROFILE_NOT_IN_NEW_STATUS);
    }

    private FirmRepCreationValueObject creationVO;

    private RegisterFirmRepValidationForm step1Form;

    private RegisterFirmRepStep2Form step2Form;

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
        RegisterFirmRepValidationForm validationForm = (RegisterFirmRepValidationForm) form;
        if (step1Form != null) {
            validationForm.copyFrom(step1Form);
        }
        validationForm.setDisabled(isDisabled());
    }

    /**
     * populate the form for the Step2
     * 
     * @param form
     */
    private void populateStep2Form(ActionForm form) {
        RegisterFirmRepStep2Form firmRepStep2Form = (RegisterFirmRepStep2Form) form;
        firmRepStep2Form.setCreationVO(getCreationVO());
        if (step2Form != null) {
            firmRepStep2Form.copyFrom(step2Form);
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
        RegisterFirmRepValidationForm validationForm = (RegisterFirmRepValidationForm) form;
        if (step1Form == null) {
            step1Form = new RegisterFirmRepValidationForm();
        }
        step1Form.copyFrom(validationForm);
    }

    /**
     * Update the context step2
     * 
     * @param form
     * @throws SystemException
     */
    private void updateContextStep2(ActionForm form) throws SystemException {
        RegisterFirmRepStep2Form firmRepStep2Form = (RegisterFirmRepStep2Form) form;
        if (step2Form == null) {
            step2Form = new RegisterFirmRepStep2Form();
        }
        step2Form.copyFrom(firmRepStep2Form);
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
     * Returns the FirmRepCreationValueObject
     * 
     * @return FirmRepCreationValueObject
     */
    public FirmRepCreationValueObject getCreationVO() {
        return creationVO;
    }

    /**
     * Sets the FirmRepCreationValueObject
     * 
     * @param creationVO
     */
    public void setCreationVO(FirmRepCreationValueObject creationVO) {
        this.creationVO = creationVO;
    }

    /**
     * Returns the userName
     * 
     * @return String
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the userName
     * 
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
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

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}
