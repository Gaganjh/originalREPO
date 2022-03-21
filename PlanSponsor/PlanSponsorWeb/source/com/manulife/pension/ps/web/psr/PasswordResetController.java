/**
 * 
 */
package com.manulife.pension.ps.web.psr;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.delegate.PartyServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.census.CensusConstants;
import com.manulife.pension.ps.web.census.util.EmployeeServiceFacade;
import com.manulife.pension.ps.web.census.util.ParameterizedActionForward;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWPasswordReset;
import com.manulife.pension.service.account.valueobject.CustomerServicePrincipal;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.psr.valueobject.PasswordResetRequestEntity;
import com.manulife.pension.service.psr.valueobject.PasswordResetRequestEntity.DestinationType;
import com.manulife.pension.service.psr.valueobject.PasswordResetRequestEntity.PasswordResetActionSource;
import com.manulife.pension.service.psr.valueobject.PasswordResetRequestEntity.RequestRestriction;
import com.manulife.pension.service.psr.valueobject.PasswordResetRequestEntity.RequestType;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;

/**
 * Action that performs all the Password Reset Validations and forward to Reset Confirmation Page.
 * @author gazulra
 *
 */

@Controller
@RequestMapping(value ="/pwdemail")

public class PasswordResetController extends PsAutoController{
	
	@ModelAttribute("passwordResetForm") 
	public PasswordResetForm populateForm()
	{
		return new PasswordResetForm();
	}
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{ 
		forwards.put("resetPassword","/pwdemail/resetPptPassword.jsp");
		forwards.put("censusSummary","redirect:/do/census/censusSummary/"); 
		forwards.put("editEmployeeSnapshot","redirect:/do/census/editEmployeeSnapshot/");
		forwards.put("resetPasswordConfirmation","redirect:/do/pwdemail/ResetPasswordConfirm/");
		}
	
	private static EmployeeServiceFacade serviceFacade = new EmployeeServiceFacade();
	private static BaseEnvironment environment = new BaseEnvironment();

	/**
	 * Constructor
	 */
	public PasswordResetController() {
		super(PasswordResetController.class);
	}

	/**
     * doDefault is for the default access of the action 
	 * @param actionForm
	 * @param request
	 * @param response		
     *
     * @throws IOException, ServletException, SystemException
     * @return ActionForward
     */     
	@RequestMapping(value ="/ResetPassword/",  method = {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("passwordResetForm") PasswordResetForm passwordResetForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("resetPassword");//if input forward not //available, provided default
	       }
		}
	
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> PasswordResetAction -> doDefault");
		}
		
		
		// get the current userProfile Object
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		
		long profileID =0L;
		
		try {
			// get the profileId from the actionForm
			profileID = Long.parseLong(passwordResetForm.getProfileId());
		} catch (NumberFormatException e) {
			throw new SystemException(e, "Unable parse the profileId String " 
					+ passwordResetForm.getProfileId()); 
		}
	
		// get the Employee vo for the user by passing the profileId
		Employee employee = serviceFacade.getEmployee(profileID, 
				userProfile, new Date(), false);
		
		if (employee != null && employee.getEmployeeDetailVO() != null) {
			
			// set the contract Id
			passwordResetForm.setContractId(
					employee.getEmployeeDetailVO().getContractId());
			
			// set the first name
			passwordResetForm.setFirstName(
					employee.getEmployeeDetailVO().getFirstName());
			
			// set the last name
			passwordResetForm.setLastName(
					employee.getEmployeeDetailVO().getLastName());
			
			// set the ssn
			passwordResetForm.setSsn(
					employee.getEmployeeDetailVO().getSocialSecurityNumber());
			
			// set the birth date
			passwordResetForm.setBirthDate(
					employee.getEmployeeDetailVO().getBirthDate());
			
			// set the employer email address
			passwordResetForm.setEmployerProvidedEmailAddress(
					employee.getEmployeeDetailVO().getEmployerProvidedEmailAddress());
			
			//setting values to update Security Activity and MRL Logging
			userProfile.setSsn(passwordResetForm.getSsn());
			userProfile.setEmailAdddress(employee.getEmployeeDetailVO().getEmployerProvidedEmailAddress());
		}
		
		boolean  isPptWebRegistered = PartyServiceDelegate.getInstance().verifyRegisteredUser(new BigDecimal(passwordResetForm.getProfileId()), passwordResetForm.getContractId());
		
		if (isPptWebRegistered) {
			passwordResetForm.setPptWebRegisStatus(Constants.YES_INDICATOR);
		} else {
			passwordResetForm.setPptWebRegisStatus(Constants.NO_INDICATOR);
		}

		
		// Validate for error messages
		Collection<GenericException> errors = new ArrayList<GenericException>();
		String forward = validateResetPasswordRequest(passwordResetForm, profileID, errors);
		
		if (!errors.isEmpty()) {
			passwordResetForm.setSuppressResetPasswordButton(true);
			setErrorsInSession(request, errors);
		} else {
			passwordResetForm.setSuppressResetPasswordButton(false);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> PasswordResetAction -> doDefault");
		}
		
		if (forward != null) {
			return  forwards.get(forward);
		} else {
			return forwards.get("resetPassword");
		}

	}
	
	/**
     * doBack is action to go back to source page 
     * 
	 * @param actionForm
	 * @param request
	 * @param response		
     * @throws IOException, ServletException, SystemException
     * @return ActionForward
     */
	@RequestMapping(value ="/ResetPassword/",params={"action=back"}, method =  {RequestMethod.POST}) 
	public String doBack (@Valid @ModelAttribute("passwordResetForm") PasswordResetForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("resetPassword");//if input forward not //available, provided default
	       }
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> PasswordResetAction -> doBack");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> PasswordResetAction -> doBack");
		}
		return forwards.get(form.getSource());
	}
	
	/**
     * Redirects to resetConfirmation page by adding profile Id as parameter
	 * @param actionForm
	 * @param request
	 * @param response		
     * @throws IOException, ServletException, SystemException
     * @return ActionForward
     */
	@RequestMapping(value ="/ResetPassword/", params={"action=resetPassword"}, method =  {RequestMethod.POST}) 
	public String doResetPassword (@Valid @ModelAttribute("passwordResetForm") PasswordResetForm passwordResetForm , BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("resetPassword");//if input forward not //available, provided default
	       }
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> PasswordResetAction -> doResetPassword");
		}
		
		
		
		// get the profileId
		String profileId = passwordResetForm.getProfileId();
		
		String requestStatus = createPasswordResetRequest(request, passwordResetForm);
		
		// create the parameterized forward
		ParameterizedActionForward resetConfirmForward = new ParameterizedActionForward(forwards.get(Constants.RESET_PWD_CONFIRMATION));
		
		// add the profileId as parameter
		resetConfirmForward.addParameter(Constants.PROFILE_ID, profileId);
		
		if (logger.isDebugEnabled()) {
			logger.debug("exits -> PasswordResetAction -> doResetpassword");
		}
		if(Constants.SUCCESS.equals(requestStatus)){
			return resetConfirmForward.getPath();
		}else {
			return forwards.get(SYSTEM_ERROR_PAGE);
		}
		
	}

	/**
     * doEdit is action to edit employee page
	 * @param actionForm
	 * @param request
	 * @param response		
     * @throws IOException, ServletException, SystemException
     * @return ActionForward
     */
	@RequestMapping(value ="/ResetPassword/", params={"action=edit"}, method = {RequestMethod.POST}) 
	public String doEdit (@Valid @ModelAttribute("passwordResetForm") PasswordResetForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("resetPassword");//if input forward not //available, provided default
	       }
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> PasswordResetAction -> doEdit");
		}
		
		PasswordResetForm passwordResetForm = (PasswordResetForm) form;
		
		// get the profileID
		String profileId = passwordResetForm.getProfileId();

		// create the parameterized forward
		ParameterizedActionForward editForward = new ParameterizedActionForward(forwards.get(Constants.PSW_RESET_PWD_EDIT));
		
		// add the profileId as parameter
		editForward.addParameter(Constants.PROFILE_ID, profileId);
		
		// add the source parameter
		if (passwordResetForm.getSource() != null) {
			editForward.addParameter(Constants.SOURCE, CensusConstants.RESET_PWD_PAGE);
		}
		
		// add the formReset parameter
		editForward.addParameter(Constants.FROMRESET, Constants.TRUE);
		
		// add the showConfirmationToDo parameter
		editForward.addParameter(Constants.SHOWCONFIRMATIONTODO, Constants.FALSE);
		
		if (logger.isDebugEnabled()) {
			logger.debug("exists -> PasswordResetAction -> doEdit");
		}
		return editForward.getPath();
	}
	
	
	/**
     * createResetPinRequest is for finding the proper path during default action call.
	 * @param mapping
	 * @param contractId
	 * @param profileId
	 * @return
	 * @throws SystemException
	 */
	private String createPasswordResetRequest(HttpServletRequest request,
			PasswordResetForm passwordResetForm) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> PasswordResetAction -> createResetPinRequest");
		}
		String ipAddress = IPAddressUtils.getRemoteIpAddress(request);
		
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		
		Principal principal =  userProfile.getPrincipal();
		CustomerServicePrincipal customerServicePrincipal = new CustomerServicePrincipal();
		customerServicePrincipal.setProfileId(String.valueOf(principal.getProfileId()));

		String[] role = {principal.getRole().getDisplayName()};
		customerServicePrincipal.setRoles(role);
		customerServicePrincipal.setExternalUser(principal.getRole().isExternalUser());
		
		PasswordResetRequestEntity passwordResetEntity = new PasswordResetRequestEntity();
		passwordResetEntity.setContractId(userProfile.getCurrentContract().getContractNumber());
		passwordResetEntity.setUserName(String.valueOf(principal.getProfileId()));
		passwordResetEntity.setClientIp(ipAddress);
		passwordResetEntity.setSsn(userProfile.getSsn());
		passwordResetEntity.setEmailAddress(userProfile.getEmailAdddress());
		
		updatePasswordResetRequest(passwordResetEntity, passwordResetForm);
		
		boolean result = false;
		try {
			result = SecurityServiceDelegate.getInstance().resetPassword(passwordResetEntity);
			if(result == true) return Constants.SUCCESS;
			else return Constants.FAILURE;
			
			} catch (Exception e) {

			if (logger.isDebugEnabled()) {
				logger
						.debug("exception occured in PasswordResetAction.createResetPinRequest()"
								+ e.getMessage());
			}
			return Constants.ERROR;
		}
	}
	
	/**
	 * Validates eligibility for Password Reset Email request
	 * 
	 * @param contractId
	 * @param profileId
	 * @param errors
	 * @param maxemail
	 * @return String
	 * @throws SecurityServiceException 
	 */
	private String validateResetPasswordRequest(PasswordResetForm actionForm, long profileId, 
			Collection<GenericException> errors) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> validateResetPasswordRequest");
		}
		
		Set<RequestRestriction> pwdEmailEligibilityViolations = null;
		try {
			pwdEmailEligibilityViolations = new HashSet<RequestRestriction>(PasswordResetRequestEntity.ALL_REQUEST_RESTRICTIONS);
			
			pwdEmailEligibilityViolations = SecurityServiceDelegate.getInstance()
					.validateResetPasswordRequest(actionForm.getContractId(), profileId,
							pwdEmailEligibilityViolations);
		}  catch(Exception e){
			throw new SystemException(e,
				"Exception While validating eligibility for Reset Password Email Request in Reset Password page");
		}

		if(pwdEmailEligibilityViolations != null && !pwdEmailEligibilityViolations.isEmpty()){

			// PRC. 12	If the participant has a Date of Birth equal to Null, the system will display Error Message #R5
			if (pwdEmailEligibilityViolations
					.contains(RequestRestriction.DATE_OF_BIRTH_ON_FILE)) {
				errors.add(new ValidationError(Constants.PSW_RESET_PWD_DOB,
						ErrorCodes.ERROR_RESET_PWD_DOB));
			}

			// PRC. 8	If the participant’s latest Employment Status is equal to Deceased (D), the system will display Error Message #R1
			if (pwdEmailEligibilityViolations
					.contains(RequestRestriction.VALID_EMPLOYMENT_STATUS)) {
				errors.add(new ValidationError(
						Constants.PSW_RESET_PWD_INVALID_EMPLOYMENT_STATUS,
						ErrorCodes.ERROR_RESET_PWD_INVALID_EMPLOYMENT_STATUS));
			}

			/*PRC. 10	If the participant has one of the following access statuses, the system will display Error Message #R3 
				a.	Denied – Returned Mail
				b.	Denied
			 */
			if (pwdEmailEligibilityViolations
					.contains(RequestRestriction.VALID_PASSWORD_STATUS)) {
				errors.add(new ValidationError(Constants.PSW_RESET_PWD_ACCESS_STATUS,
						ErrorCodes.ERROR_PSW_RESET_PWD_ACCESS_STATUS));
			}

			/* PRC. 11	The system will display Error Message #R4, when the number of Password Reset Requests for 
			the participant is equal to or greater than the Maximum Request allowed for the Request Suppression Period  */
			if (pwdEmailEligibilityViolations
					.contains(RequestRestriction.VALIDATE_RESET_REQUESTS)) {
				errors.add(new ValidationError(Constants.PSW_RESET_PWD_MAX_REQUESTS,
						ErrorCodes.ERROR_PSW_RESET_PWD_MAX_REQUESTS));
			}

			//PRC. 14	If the Participant is not Web Registered, the system will display Error Message #R7
			if (pwdEmailEligibilityViolations
					.contains(RequestRestriction.VALID_WEB_REGISTRATION)) {
				errors.add(new ValidationError(Constants.PSW_RESET_PWD_WEB_REGIS_STATUS,
						ErrorCodes.ERROR_PSW_RESET_PWD_WEB_REGIS_STATUS));
			}
			
			//PRC. 13	If the participant does not have an Employer Provided Email Address, the system will display Error Message #R6
			if (pwdEmailEligibilityViolations
					.contains(RequestRestriction.EMAIL_ADDRESS_ON_FILE)) {
				errors.add(new ValidationError(Constants.PSW_RESET_PWD_EMAIL,
						ErrorCodes.ERROR_RESET_PWD_EMAIL));
			}

			if (logger.isDebugEnabled()) {
				logger.debug("exit <- validatePwdEmailRequestEligibility");
			}
		}
		return null;
	}
	
	/**
	 * updates the PasswordRequest from FormBeam
	 * @param passwordResetRequest
	 * @param resetPwdform
	 */
	private void updatePasswordResetRequest(PasswordResetRequestEntity passwordResetRequest, PasswordResetForm resetPwdform ){
		passwordResetRequest.setProfileId(new BigDecimal(resetPwdform.getProfileId()));
		passwordResetRequest.setActionTypeCode(PasswordResetActionSource.PLANSPONSOR);
		passwordResetRequest.setRequestType(RequestType.RESET.getDatabaseCode());
		passwordResetRequest.setLocation(environment.getStringVariable(CommonConstants.SITE_MODE));
		passwordResetRequest.setDestinationType(DestinationType.ERP_EMAIL_ADDRESS);
	}
	

	/*
	 *  * (non-Javadoc) 
	 * This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations as part of the CL#137697.
	 * 
	 */
	
	 @Autowired
	   private PSValidatorFWPasswordReset  psValidatorFWPasswordReset;

	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWPasswordReset);
	}
}
