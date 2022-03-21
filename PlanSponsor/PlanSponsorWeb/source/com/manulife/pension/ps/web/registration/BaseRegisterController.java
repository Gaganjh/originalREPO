package com.manulife.pension.ps.web.registration;


import javax.servlet.http.HttpServletRequest;

import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * This is the base action for all of the registration actions
 * @author Tony Tomasone
 * 
 */
public abstract class BaseRegisterController extends PsAutoController {
	protected final static String ACTION_CONTINUE 			= "continue";
	protected final static String ACTION_DEFAULT 			= "default";
	protected final static String ACTION_ACCEPT 			= "accept";	
	protected final static String FORWARD_CONFIRM 			= "confirm";
	protected final static String FORWARD_AUTHENTICATE 		= "authenticate";
	protected final static String FORWARD_TPAAUTHENTICATE	= "tpaauthenticate";
	protected final static String FORWARD_TERMS 			= "terms";
	protected final static String FORWARD_REGISTER 			= "register";
	protected final static String FORWARD_TPAREGISTER 		= "tparegister";
	protected final static String FORWARD_LOGIN				= "login";

	/**
	 * Constructor for BaseRegisterAction
	 */
	public BaseRegisterController() {
		super(AuthenticationController.class);
	}


	/**
	 * Constructor for BaseRegisterAction
	 */
	public BaseRegisterController(Class clazz) {
		super(clazz);
	}

	/**
	 * Populates the UserInfor object from the RegisterForm.
	 * This is called after the validation has been done
	 * so we don't need to worry about the validation here.
	 * 
	 * @param request
	 * @param userInfo the container for the Security calls
	 * @param form the Registration form which is the source of the data
	 * 
	 */
	protected void populateUserInfo(HttpServletRequest request,
			UserInfo userInfo, RegisterForm form) {

		if ((form.getUserName() != null) && !form.getUserName().equals(form.getDefaultUserName()) )
			userInfo.setDefaultUserNameChanged(true);


		userInfo.setSsn(form.getSsn().toString());
		userInfo.setMobileNumber(form.getMobile());
		userInfo.setPhoneNumber(form.getPhone());
		userInfo.setPhoneExtension(form.getExt());
		// by now these strings in the form have already been validated
		// so we don't need to worry about NumberFormatExceptions
		if(form.getTpa()) {
			userInfo.setTpaFirmId(Integer.parseInt(form.getFirmId()));
			userInfo.setFax(form.getFax()); 
			userInfo.setChallengeQuestion(null);
			userInfo.setChallengeAnswer(null);
		}
		else {
			userInfo.setContractNumber(Integer.parseInt(form.getContractNumber()));
			userInfo.setChallengeQuestion(form.getChallengeQuestion());
			userInfo.setChallengeAnswer(form.getAnswer());
			userInfo.setPasswordFailedAttemptCount(form.getExistingSiteUserFailedCount());
		}
		userInfo.setUserName(form.getUserName());
		// if we have not passed the first page
		if(!form.getTermsValid())
		{
			userInfo.setPassword(form.getPin());
		}
		else
		{
			userInfo.setPassword(form.getPassword());
		}
		userInfo.setEmail(form.getEmail());
		userInfo.setPasscodeDeliveryPreference(form.getPasscodeDeliveryPreference());
		userInfo.setFirstName(form.getFirstName());
		userInfo.setLastName(form.getLastName());
	}

	/**
	 * A common method wich determines whether we are in the correct step
	 * of the wizard.  This is required in case the user tries to jump ahead
	 * in the process through a bookmark.
	 * @param step the current step we think we're on
	 * @param registerForm the session form to check the status of our wizard
	 * @return boolean true if we're in the correct step
	 * 
	 */
	protected boolean isInCorrectStep(String step, RegisterForm registerForm) {
		if(step==null) return false;
		
		// if someone is trying to jump ahead in the 
	    // registration wizard, send them to the right page/step
	    if (step.equals(FORWARD_TERMS)) {
	    	if(!registerForm.getAuthenticateValid())
	    		return false;
	    	else
	    		return true;

	    }
	    else if (step.equals(FORWARD_REGISTER)) {
	    	if(
	    		!registerForm.getAuthenticateValid()||
		    	!registerForm.getTermsValid()
	    	)	return false;
	    	else
	    		return true;

	    }
	    else if (step.equals(FORWARD_CONFIRM)) {
	    	if(
	    		!registerForm.getAuthenticateValid()||
		    	!registerForm.getTermsValid()||
		    	!registerForm.getRegisterValid()
	    	)	return false;
	    	else
	    		return true;
	    }
	    else return true;
	}
	
	
	/**
	 * Returns the correct action forward corresponding to what step we are
	 * in the wizard.
	 * @param mapping the ActionMapping for this request
	 * @param registerForm the RegisterForm for the session
	 * @reurtns ActionForwad the correct step based on the session form
	 * 
	 */
	protected String getStepForward(RegisterForm registerForm) {
		// if someone is trying to jump ahead in the
		// registration wizard, send them to the right page/step
		if (!registerForm.getAuthenticateValid()) {
			if (registerForm.getTpa()) {
				return findForward(FORWARD_TPAAUTHENTICATE);
			} else {
				return findForward(FORWARD_AUTHENTICATE);
			}
		} else if (!registerForm.getTermsValid()) {
			return findForward(FORWARD_TERMS);
		} else if (!registerForm.getRegisterValid()) {
			if (registerForm.getTpa()) {
				return findForward(FORWARD_TPAREGISTER);
			} else {
				return findForward(FORWARD_REGISTER);
			}
		} else {
			if (registerForm.getTpa()) {
				return findForward("tpainput");
			} else {
				return findForward("input");
			}
		}
	}
}


