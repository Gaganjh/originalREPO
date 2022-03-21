package com.manulife.pension.bd.web.password;

import org.apache.commons.collections.CollectionUtils;
import com.manulife.pension.ezk.web.ActionForm;

import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.process.BDWizardProcessContext;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.platform.web.process.SimpleProcessState;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.bd.valueobject.BDUserSecurityProfile;
import com.manulife.pension.service.security.role.BDFinancialRep;
import com.manulife.pension.service.security.role.BDUserRole;

/**
 * The context class for Forget password process
 * 
 * @author guweigu
 * 
 */
public class ForgetPasswordContext extends BDWizardProcessContext {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Definition of the process states
	 */

	public final static SimpleProcessState Step1 = new SimpleProcessState(
			"step1", "/do/forgetPassword/step1");
	public final static SimpleProcessState Step2 = new SimpleProcessState(
			"step2", "/do/forgetPassword/step2");
	//StepUp Challenge and Validation steps
	public final static SimpleProcessState StepUpChallengeStep = new SimpleProcessState(
			"stepUpChallenge", "/do/forgetPassword/stepUpChallenge");
	public final static SimpleProcessState StepUpValidationStep = new SimpleProcessState(
			"stepUpValidation", "/do/forgetPassword/stepUpValidation");
	public final static SimpleProcessState Step3 = new SimpleProcessState(
			"step3", "/do/forgetPassword/step3");
	public final static SimpleProcessState StepComplete = new SimpleProcessState(
			"step4", "/do/forgetPassword/step4");
	public final static SimpleProcessState StepCancel = new SimpleProcessState(
			"cancel", URLConstants.HomeURL, true);

	static {
		Step1.addNext(ACTION_CONTINUE, Step2);
		Step1.addNext(ACTION_CANCEL, StepCancel);
		
		Step2.addNext(ACTION_CONTINUE, StepUpChallengeStep);
		Step2.addNext(ACTION_CANCEL, StepCancel);
		
		StepUpChallengeStep.addNext(ACTION_CONTINUE, StepUpValidationStep);
		StepUpChallengeStep.addNext(ACTION_CANCEL, StepCancel);
		
		StepUpValidationStep.addNext(ACTION_CONTINUE, Step3);
		StepUpValidationStep.addNext(ACTION_CANCEL, StepCancel);
		
		Step3.addNext(ACTION_CONTINUE, StepComplete);
		Step3.addNext(ACTION_CANCEL, StepCancel);
	}

	public static final String ProcessName = "security.forgetPassword";

	private BDUserSecurityProfile securityProfile;

	private ForgetPasswordForm savedForm;

	/*
	 * Whether the process is disable
	 */
	private boolean disabled = false;

	private boolean brokerWithoutNoActiveEntity = false;
	
	public ForgetPasswordContext() {
		super();
	}

	public BDUserSecurityProfile getSecurityProfile() {
		return securityProfile;
	}

	public void setSecurityProfile(BDUserSecurityProfile securityProfile) {
		this.securityProfile = securityProfile;
		// set the indicator of broker without active entity
		brokerWithoutNoActiveEntity = getBrokerWithNoActiveEntity(securityProfile);
	}

	@Override
	public ProcessState getStartState() {
		return Step1;
	}

	@Override
	public void populateForm(ActionForm actionForm) throws SystemException {
		ForgetPasswordForm f = (ForgetPasswordForm) actionForm;
		if (savedForm != null) {
			f.copyFrom(this.savedForm);
			if (securityProfile != null) {
				f.setQuestion1(securityProfile.getChallengeQuestions()[0]);
				f.setQuestion2(securityProfile.getChallengeQuestions()[1]);
			}
		}
		f.setDisabled(disabled);
		f.setBrokerWithNoActiveEntity(brokerWithoutNoActiveEntity);
	}

	/**
	 * Check if the user is a broker with no active broker entity
	 * 
	 * @param securityProfile
	 * @return
	 */
	private boolean getBrokerWithNoActiveEntity(
			BDUserSecurityProfile securityProfile) {
		if (securityProfile != null) {
			BDUserRole role = securityProfile.getPrincipal().getBDUserRole();
			if (role.getRoleType().compareTo(BDUserRoleType.FinancialRep) == 0) {
				BDFinancialRep broker = (BDFinancialRep) role;
				if (CollectionUtils.isEmpty(broker.getBrokerEntities())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void updateContext(ActionForm form) throws SystemException {
		if (savedForm == null) {
			savedForm = new ForgetPasswordForm();
		}
		this.savedForm.copyFrom((ForgetPasswordForm) form);
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

    /**
     * Sets the current state
     * 
     * @param currentState
     */
    @Override
    public void setCurrentState(ProcessState currentState) {
        super.setCurrentState(currentState);
        if (StepComplete.isSameState(currentState)) {
    		securityProfile = null;
    		savedForm = null;
    		disabled = false;
        }
    }
}
