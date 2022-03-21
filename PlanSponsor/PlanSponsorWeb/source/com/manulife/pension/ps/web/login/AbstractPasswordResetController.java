package com.manulife.pension.ps.web.login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.security.exception.ChallengeAnswerDoesNotMatchException;
import com.manulife.pension.service.security.exception.ChallengeQuestionDoesNotMatchException;
import com.manulife.pension.service.security.exception.EmailDoesNotMatchException;
import com.manulife.pension.service.security.exception.FailedAlmostNTimesException;
import com.manulife.pension.service.security.exception.FailedNTimesException;
import com.manulife.pension.service.security.exception.IncorrectPasswordException;
import com.manulife.pension.service.security.exception.LockedUserException;
import com.manulife.pension.service.security.exception.PasswordChangeNotAllowedException;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.exception.UserNotFoundException;
import com.manulife.pension.service.security.role.ExternalClientUser;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.content.GenericException;

/**
 * This is the abstract class for all Password Reset actions.  All pages only have a 
 * Continue button.
 * 
 * @author Chris Shin
 * @version CS 1.0
 */
public abstract class AbstractPasswordResetController extends PsController {

	protected SecurityServiceDelegate delegate = SecurityServiceDelegate.getInstance();
	/**
	 * Constructor.
	 */
	public AbstractPasswordResetController() {
		super(AbstractPasswordResetController.class);
	}
	
	/**
	 * Constructor.
	 */
	public AbstractPasswordResetController(Class clazz) {
		super(clazz);
	}
	
	/**
	 * Gets the step number for the given action form.
	 * 
	 * @return the step number
	 */
	protected abstract int getStep();

	/**
	 * Processes that occur after the Continue button is clicked and the doValidate
	 * returns no errors
	 * 
	 * @param form
	 *            The action form containing the form values
	 * @param errors
	 *            The Collection of errors that is displayed on the page.
	 * @param mapping
	 *            The actionMapping object
	 * @param request
	 *            The request object
	 * @param response
	 *            The response object
	 * @param String
	 *            The default URL
	 * @return String
	 * 			  The target URL
	 * 			  
	 */
	protected  String processFormValidatedData(PasswordResetAuthenticationForm form,
		 Collection errors, HttpServletRequest request,
		 HttpServletResponse response, String defaultTarget)
		 throws SystemException {
		return null;
	}

	/**
	 * The process that occurs if the action is called but the button was not clicked.
	 * 
	 * 
	 * @param form
	 *            The action form containing the form values
	 * @param mapping
	 *            The actionMapping object
	 * @param request
	 *            The request object
	 * @return
	 */
	protected  void processDefault(PasswordResetAuthenticationForm form,
		 HttpServletRequest request) {
	}

	
	@Override
	 protected String getSubmitAction(HttpServletRequest request, ActionForm form) throws ServletException {
		String action = super.getSubmitAction(request,form);
		 PasswordResetAuthenticationForm actionForm = (PasswordResetAuthenticationForm)form ;
   	  if(StringUtils.isNotBlank(actionForm.getButton()))
 		  action = actionForm.getButton()!= null ? actionForm.getButton():StringUtils.EMPTY;
 		  
		return action;
		 
	 }
	
	
	/*
	 */
	public String doExecute(
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {

		String forward=null;
		Collection errors = new ArrayList();
		
		PasswordResetAuthenticationForm form = (PasswordResetAuthenticationForm) actionForm;
		
		if ( form.getButton()!= null && form.getButton().equals(PasswordResetAuthenticationForm.CANCEL) ) {
		
			/*
			 * the action was invoked from CANCEL button
			 */
			forward=PasswordResetAuthenticationForm.LOGIN;
		} else if (form.isStepValid(getStep())) {
			/*
			 * Ensure that the step that we are on has met the previous requirements
			 */
			if ((form.getButton()!= null) &&
				(form.getButton().equals(PasswordResetAuthenticationForm.CONTINUE) ||
				 form.getButton().equals(PasswordResetAuthenticationForm.FINISHED))) {
			
				String actionTarget = processFormValidatedData(form, errors, request, response, PasswordResetAuthenticationForm.CONTINUE);

				if (errors != null && errors.size()>0) {
					forward=actionTarget;
					SessionHelper.setErrorsInSession(request, errors);
	
				} else {
					
					/*
					 * All validation passed and CONTINUE clicked
					 * Go on to the next page 
					 */
					
					Object dispalyUserName =  request.getSession().getAttribute(PasswordResetAuthenticationForm.DISPALY_USER_NAME);
					if(dispalyUserName!= null){
						request.getSession().removeAttribute(PasswordResetAuthenticationForm.DISPALY_USER_NAME);
					}
					
					form.setStepCompleted(getStep());
					forward=actionTarget;
				}
			} else {

				/*
				 * the action was invoked for the first time
				 */
				processDefault(form,  request);
				forward=PasswordResetAuthenticationForm.DEFAULT;
			}
		} else {
			/*
			 * the action was invoked but not from a CONTINUE button
			 */
			forward=PasswordResetAuthenticationForm.RESTART;
		}

		form.resetButton();
		return forward;
	}

	/**
	 * Validate the input action form. 
	 * 
	 */
	protected Collection doValidate( ActionForm form, HttpServletRequest request) {

		Collection errors = super.doValidate( form, request);

		return errors;
	}
	
	protected String handleErrors(SecurityServiceException e, Collection errors) {
		
		String forward = PasswordResetAuthenticationForm.ERRORS;

		errors.add(new GenericException(Integer.parseInt(e.getErrorCode())));

		if ((e instanceof IncorrectPasswordException) || 
			(e instanceof UserNotFoundException) ||
			(e instanceof EmailDoesNotMatchException) ||
			(e instanceof ChallengeQuestionDoesNotMatchException) ||
			(e instanceof ChallengeAnswerDoesNotMatchException) ||
			(e instanceof FailedAlmostNTimesException) || 
			(e instanceof PasswordChangeNotAllowedException)) {
			//do nothing special

		} else if (e instanceof FailedNTimesException) {
			forward = PasswordResetAuthenticationForm.LOGIN;
		} else if (e instanceof LockedUserException) {
			forward = PasswordResetAuthenticationForm.LOGIN;
		} else {
			//todo: remove once we know all the exceptions coming back and how to handle them.
			e.printStackTrace();
			forward = PasswordResetAuthenticationForm.LOGIN;
		}			
		
		return forward;
	}	
	
	protected UserInfo getUserInfo(PasswordResetAuthenticationForm form) {
		
		UserInfo user = new UserInfo();
		
		user.setContractNumber(Integer.parseInt(form.getContractNumber()));
		user.setEmail(form.getEmailAddress());
		user.setSsn(form.getSsn().toString());
		user.setChallengeQuestion(form.getSavedChallengeQuestion());
		user.setChallengeAnswer(form.getChallengeAnswer());
		user.setNewPassword(form.getNewPassword());
		user.setUserName(form.getUsername());
		user.setProfileId(form.getProfileId());
		user.setFirstName(form.getFirstName());
		user.setLastName(form.getLastName());		
		user.setRole(new ExternalClientUser());
		return user;
	}				
}