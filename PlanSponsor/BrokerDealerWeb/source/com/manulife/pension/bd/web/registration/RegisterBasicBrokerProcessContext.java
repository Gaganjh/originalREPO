package com.manulife.pension.bd.web.registration;

import org.apache.commons.lang.StringUtils;


import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.process.BDWizardProcessContext;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.platform.web.process.SimpleProcessState;
import com.manulife.pension.service.security.bd.valueobject.BasicBrokerRegistrationValueObject;

/**
 * This is the ProcessContext class for Basic Broker Registration
 * 
 * @author Ilamparithi
 * 
 */
public class RegisterBasicBrokerProcessContext extends BDWizardProcessContext {

    private static final long serialVersionUID = 1L;

    // / States definitions
    public final static String ProcessName = "security.registerBasicBroker";

    public final static SimpleProcessState Step0 = new SimpleProcessState("step0",
            "/do/registerExternalBroker/start");

    public final static SimpleProcessState Step1 = new SimpleProcessState("step1",
            "/do/registerExternalBroker/basicBroker/step1");

    public final static SimpleProcessState Step2 = new SimpleProcessState("step2",
            "/do/registerExternalBroker/basicBroker/step2");

    public final static SimpleProcessState Step3 = new SimpleProcessState("complete",
            "/do/registerExternalBroker/basicBroker/step3");

    public final static SimpleProcessState Login = new SimpleProcessState("login",
            URLConstants.HomeURL, true);

    public final static SimpleProcessState StepCancel = new SimpleProcessState("cancel",
            URLConstants.HomeURL, true);

    static {
        Step1.addNext(ACTION_CONTINUE, Step2);
        Step1.addNext(ACTION_CANCEL, StepCancel);
        Step2.addNext(ACTION_CONTINUE, Step3);
        Step2.addNext(ACTION_CANCEL, StepCancel);
        Step3.addNext(ACTION_CONTINUE, Login);
    }

    private BasicBrokerRegistrationValueObject basicBrokerRegistrationVO;

    private RegisterBasicBrokerValidationForm step1Form;

    private RegisterBasicBrokerStep2Form step2Form;

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
     * Returns the start state
     */
    @Override
    public ProcessState getStartState() {
        return Step0;
    }

    /**
     * Returns the user name
     * 
     * @return String
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user name
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

    /**
     * Returns the BasicBrokerRegistrationValueObject
     * 
     * @return BasicBrokerRegistrationValueObject
     */
    public BasicBrokerRegistrationValueObject getBasicBrokerRegistrationVO() {
        return basicBrokerRegistrationVO;
    }

    /**
     * Sets the BasicBrokerRegistrationValueObject
     * 
     * @param basicBrokerRegistrationVO
     */
    public void setBasicBrokerRegistrationVO(
            BasicBrokerRegistrationValueObject basicBrokerRegistrationVO) {
        this.basicBrokerRegistrationVO = basicBrokerRegistrationVO;
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
        if (step1Form != null) {
            RegisterBasicBrokerValidationForm validationForm = (RegisterBasicBrokerValidationForm) form;
            validationForm.copyFrom(step1Form);
            validationForm.setDisabled(isDisabled());
        }
    }

    /**
     * populate the form for the Step2
     * 
     * @param form
     */
    private void populateStep2Form(ActionForm form) {
        RegisterBasicBrokerStep2Form basicBrokerstep2Form = (RegisterBasicBrokerStep2Form) form;
        if (step2Form != null) {
            basicBrokerstep2Form.copyFrom(step2Form);
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
        RegisterBasicBrokerValidationForm validationForm = (RegisterBasicBrokerValidationForm) form;
        if (step1Form == null) {
            step1Form = new RegisterBasicBrokerValidationForm();
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
        RegisterBasicBrokerStep2Form basicBrokerstep2Form = (RegisterBasicBrokerStep2Form) form;
        if (step2Form == null) {
            step2Form = new RegisterBasicBrokerStep2Form();
        }
        step2Form.copyFrom(basicBrokerstep2Form);
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

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

}
