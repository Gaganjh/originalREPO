package com.manulife.pension.ps.web.profiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.LoanServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.util.ExtensionRule;
import com.manulife.pension.platform.web.util.FaxRule;
import com.manulife.pension.platform.web.util.MobileRule;
import com.manulife.pension.platform.web.util.PasswordMeterUtility;
import com.manulife.pension.platform.web.util.PhoneRule;
import com.manulife.pension.platform.web.validation.rules.NameRule;
import com.manulife.pension.platform.web.validation.rules.NewPasswordRule;
import com.manulife.pension.platform.web.validation.rules.PrimaryEmailRule;
import com.manulife.pension.platform.web.validation.rules.SecondaryEmailRule;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.ps.service.lock.valueobject.Lock;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.LockServiceDelegate;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWNull;
import com.manulife.pension.ps.web.validation.rules.AnswerConfirmRule;
import com.manulife.pension.ps.web.validation.rules.PrimaryEmailNotLocalRule;
import com.manulife.pension.ps.web.validation.rules.QuestionRule;
import com.manulife.pension.ps.web.validation.rules.SecondaryEmailNotLocalRule;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.ParticipantAddressContractServiceFeature;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.exception.IncorrectPasswordException;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.passcode.PasscodeSecurity;
import com.manulife.pension.service.security.role.AdministrativeContact;
import com.manulife.pension.service.security.role.AuthorizedSignor;
import com.manulife.pension.service.security.role.InternalUser;
import com.manulife.pension.service.security.role.InternalUserManager;
import com.manulife.pension.service.security.role.PlanSponsorUser;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.role.Trustee;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.service.security.valueobject.UserPreferenceKeys;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.util.Pair;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;

/**
 * This is Action class for EditProfile
 *
 * @author Ludmila Stern
 */
@Controller
@RequestMapping( value="/profiles")
@SessionAttributes({"editMyProfileForm"})

public class EditMyProfileController extends PsAutoController {
	
	private UserInfo userInfo;
	@ModelAttribute("editMyProfileForm")
	public EditMyProfileForm populateForm() 
	{
		return new EditMyProfileForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/profiles/editMyProfile.jsp");
		forwards.put("confirm","redirect:/do/profiles/editMyProfile/?action=confirm");
		forwards.put("refresh","redirect:/do/profiles/editMyProfile/?action=refresh");
		forwards.put("confirmPage","/profiles/editMyProfileConfirmation.jsp");
		forwards.put("manageUsers","redirect:/do/profiles/manageUsers/?lastVisited=true");
		forwards.put("challengePasscode", "redirect:/do/passcodeTransition/");
		}

	
    private static final String CONFIRMATION_FORWARD = "confirm";

    private static final String CONFIRMATION_JSP_FORWARD = "confirmPage";

    private static final String REFRESH_FORWARD = "refresh";

    private static final String MANAGE_USERS_FORWARD = "manageUsers";
    
    private static final String LOGIN_FLOW = "loginFlow";

    private Map<Class<IncorrectPasswordException>, String> securityExceptionFormFieldMap = new HashMap<Class<IncorrectPasswordException>, String>();

    /**
     * Constructor.
     */
    public EditMyProfileController() {
        super(EditMyProfileController.class);
        securityExceptionFormFieldMap.put(IncorrectPasswordException.class,
                EditMyProfileForm.FIELD_CURRENT_PASSWORD);
    }

    /*
     *
     * @see com.manulife.pension.ps.web.controller.PsAutoAction#doDefault(org.apache.struts.action.ActionMapping,
     *      com.manulife.pension.ps.web.controller.PsAutoForm,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    
    @RequestMapping(value ="/editMyProfile/",  method =  {RequestMethod.GET}) 
    public String doDefault(@Valid @ModelAttribute("editMyProfileForm") EditMyProfileForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	        if(errDirect!=null){
	        	request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	        	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	        }
		}
    	
		final HttpSession session = request.getSession(false);
		
		//setting this variable here because on redirect from passcode validation
		//this value becomes null and the user is directed to the home page, he should stay on same page.
		request.getSession().setAttribute("challengeRequestFrom", "editProfilePage");
		
		if (session != null && session.getAttribute(Constants.CHALLENGE_PASSCODE_IND) != null ) {
			boolean challengeUserInd = (boolean) session.getAttribute(Constants.CHALLENGE_PASSCODE_IND);
			
			if(challengeUserInd) {
	    		return forwards.get("challengePasscode");
	    	}	
		}
		
		if (session != null && session.getAttribute(Constants.PHONE_COLLECTION) != null) {
			session.setAttribute(Constants.PHONE_COLLECTION_EDIT_MY_PROFILE, true);
		}
		
    	String formErrors=validate(form, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
        if (logger.isDebugEnabled()) {
            logger.debug("entry <-- doDefault");
        }

        
        UserProfile userProfile = getUserProfile(request);
        Principal principal = userProfile.getPrincipal();
        long userProfileId = principal.getProfileId();

        if (!LockServiceDelegate.getInstance().lock(LockHelper.USER_PROFILE_LOCK_NAME, LockHelper.USER_PROFILE_LOCK_NAME + userProfileId, userProfileId))
        {
        	try
        	{
            	Lock lockInfo = LockServiceDelegate.getInstance().getLockInfo(LockHelper.USER_PROFILE_LOCK_NAME, LockHelper.USER_PROFILE_LOCK_NAME + userProfileId);

            	UserInfo lockOwnerUserInfo = null;

            	try
            	{
	        		lockOwnerUserInfo = SecurityServiceDelegate.getInstance().searchByProfileId(
	        				principal, lockInfo.getLockUserProfileId());
            	}
            	catch(SystemException e)
            	{
            		// do this for now (unbelieveable)
            		// this will mostly likely occur when an internal user
            		// has a lock and a tpa tries to acquire the lock.
            	}

        		String lockOwnerDisplayName = LockHelper.getLockOwnerDisplayName(userProfile, lockOwnerUserInfo);
        		Collection<GenericException> errors = new ArrayList<GenericException>();
        		errors.add(new GenericException(1057, new String[] {lockOwnerDisplayName}));
                SessionHelper.setErrorsInSession(request, errors);
                
				if (session != null && session.getAttribute(Constants.PHONE_COLLECTION) != null) {
					session.removeAttribute(Constants.PHONE_COLLECTION);
				}

				if (session != null && session.getAttribute(Constants.PHONE_COLLECTION_EDIT_MY_PROFILE) != null) {
					session.removeAttribute(Constants.PHONE_COLLECTION_EDIT_MY_PROFILE);
				}
					
                if(form.isLoginFlow()) {
                	return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;	
                }
                else {
                	return forwards.get(MANAGE_USERS_FORWARD);
                }	
    		}
    		catch(SecurityServiceException e)
    		{
    			throw new SystemException(e, "com.manulife.pension.ps.web.profiles.EditMyProfileAction", "doDefault", "Failed to get user info of lock own. " + e.toString());
    		}
    	}


        userInfo = SecurityServiceDelegate.getInstance().getUserInfo(
				userProfile.getPrincipal());
        //load security code delivery preference
        userInfo.setPasscodeDeliveryPreference(SecurityServiceDelegate.getInstance().getPasscodeDeliveryPreference(userProfileId));
		// load email preferences on from with currentContract
		populateForm(form, userInfo, request);
		if (logger.isDebugEnabled()) {
			logger.debug("exit <-- doDefault");
		}
		return forwards.get("input");
    }

    /**
	 * Saves the user input form.
	 *
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
    
    @RequestMapping(value ="/editMyProfile/" ,params={"action=save"}, method =  {RequestMethod.POST}) 
    public String doSave (@Valid @ModelAttribute("editMyProfileForm") EditMyProfileForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	     return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
    	
    	// changes for US 44837
    	
		
		UserProfile profile = getUserProfile(request);
		
		userInfo = SecurityServiceDelegate.getInstance().getUserInfo(
				profile.getPrincipal());
		
		//boolean businessIndicator = SessionHelper.getBusinessParamFlag(request, userInfo);
		
		String formErrors = null;
		
		//if(!businessIndicator){
    	   formErrors=validate(form, request);
		//}
		
		// End changes for US 44837
		//Deapi is down return to default page and display the warning message
    	if((null!= request.getSession(false).getAttribute("Deapi") && request.getSession(false).getAttribute("Deapi").equals("down"))){
			 return forwards.get("input");
		}
		
    	if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
        if (logger.isDebugEnabled()) {
            logger.debug("entry <-- doSave");
            logger.debug(form);
        }

        String forward = null;
        UserInfo userInfo = null;
        try {
            /*
             * We have already check that user have made some changes in doValidate(), so we don't
             * need to check updatedInformation() again.
             */
            SecurityServiceDelegate service = SecurityServiceDelegate.getInstance();
            userInfo = service.getUserInfo(getUserProfile(request).getPrincipal());
            populateUserInfo(userInfo, form);
            
            String ipAddress = IPAddressUtils.getRemoteIpAddress(request);
       	 	userInfo.setIpAddress(ipAddress);

            if (logger.isDebugEnabled()) {
                logger.debug("Updating user: " + userInfo);
            }

           
            form.setSubmitted(true);
            if(profile != null && profile.getRole() != null 
            		&& profile.getRole().isExternalUser() && !profile.getRole().isTPA()) {
            	updateContractContacts(profile.getPrincipal(), userInfo);
            }
            
            service.updateUser(profile.getPrincipal(), Environment.getInstance().getSiteLocation(), userInfo);
            
            
           // start changes for US44837
            
            final HttpSession session = request.getSession(false);
            
            String userflow = "EditMyProfile";
            if(form.getNewPassword()!= null && !StringUtils.isEmpty(form.getNewPassword())){
            service.updateDBTransactionPassword(profile.getPrincipal(),userInfo,userflow,
            		IPAddressUtils.getRemoteIpAddress(request),request.getSession(false).getId());
            }
           
            if (session != null && session.getAttribute(Constants.PHONE_COLLECTION) != null){
            	if ((boolean)session.getAttribute(Constants.PHONE_COLLECTION)) {//when user sets the preferences for the first time.

            		if(Objects.isNull(session.getAttribute(Constants.IS_CHALLENGED_IN_FORGOT_PASSWORD_FLOW))
            				&& Objects.isNull(session.getAttribute("isChallengedAtLogin"))) {
            			boolean isExmpted = isUserExempted(userInfo.getProfileId(),  IPAddressUtils.getRemoteIpAddress(request));
            			if(!isExmpted)
            				request.getSession().setAttribute(Constants.CHALLENGE_PASSCODE_IND, true);
            		}
            	}
            }

           // End changes for US 44837 
			
			if (session != null && session.getAttribute(Constants.PHONE_COLLECTION) != null) {
				session.removeAttribute(Constants.PHONE_COLLECTION);
			}

			if (session != null && session.getAttribute(Constants.PHONE_COLLECTION_EDIT_MY_PROFILE) != null) {
				session.removeAttribute(Constants.PHONE_COLLECTION_EDIT_MY_PROFILE);
			}
            /*
             * Update the first name and last name and preferences in the HTTP session.
             */
            profile.getPrincipal().setFirstName(userInfo.getFirstName());
            profile.getPrincipal().setLastName(userInfo.getLastName());
            updateSessionPreferences(profile, userInfo);
            if (profile.getRole().isTPA()) {
                profile.getPreferences().put(UserPreferenceKeys.EMAIL_NEWSLETTER_PREFERENCE,
                        profile.getRole().hasPermission(PermissionType.NEWSLETTER_EMAIL) ? Constants.YES : Constants.NO);
            }

            LockServiceDelegate.getInstance().releaseLock(
            	LockHelper.USER_PROFILE_LOCK_NAME,
            	LockHelper.USER_PROFILE_LOCK_NAME + userInfo.getProfileId());
            
            
        } catch (SecurityServiceException e) {
            List<GenericException> errors = new ArrayList<GenericException>();

            String fieldId = (String) securityExceptionFormFieldMap.get(e.getClass());

            if (fieldId == null) {
                errors.add(new GenericException(Integer.parseInt(e.getErrorCode())));
            } else {
                errors.add(new ValidationError(fieldId, Integer.parseInt(e.getErrorCode())));
            }

            SessionHelper.setErrorsInSession(request, errors);
            return forwards.get(REFRESH_FORWARD);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <-- doSave");
        }

        forward = forwards.get(CONFIRMATION_FORWARD);
        return forward;
    }

    /**
     * Refresh the edit my profile input page. This method is invoked by the GET after a redirect
     * after an unsuccessful Save POST.
     */
    @RequestMapping(value ="/editMyProfile/", params={"action=refresh"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doRefresh (@Valid @ModelAttribute("editMyProfileForm") EditMyProfileForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
    	String formErrors=validate(form, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
     return forwards.get("input");
    }

    /**
     * Display the confirmation page. This method is invoked by the GET after a redirect after a
     * successful Save POST.
     */
    @RequestMapping(value ="/editMyProfile/", params={"action=confirm"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doConfirm (@Valid @ModelAttribute("editMyProfileForm") EditMyProfileForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
    	String formErrors=validate(form, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
     if (form.isSubmitted()) {
        	return forwards.get(CONFIRMATION_JSP_FORWARD);
        } else {
            if(form.isLoginFlow()) {
            	return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;	
            }
            else {
            	return forwards.get(MANAGE_USERS_FORWARD);
            }	
        }
    }

    /**
     * Cancel the current operation. The form in the session is removed.
     */
    
    @RequestMapping(value ="/editMyProfile/", params={"action=cancel"}, method =  {RequestMethod.POST}) 
    public String doCancel (@Valid @ModelAttribute("editMyProfileForm") EditMyProfileForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
    	String formErrors=validate(form, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
        LockServiceDelegate.getInstance().releaseLock(
            	LockHelper.USER_PROFILE_LOCK_NAME,
            	LockHelper.USER_PROFILE_LOCK_NAME + getUserProfile(request).getPrincipal().getProfileId());
        request.getSession(false).removeAttribute("editMyProfileForm");
       
        if(form.isLoginFlow()) {
        	return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;	
        }
        else {
        	return forwards.get(MANAGE_USERS_FORWARD);
        }	
    }

    /**
     * Finish after confirmation. The form in the session is removed.
     */
    @RequestMapping(value ="/editMyProfile/" ,params={"action=finish"}, method =  {RequestMethod.POST}) 
    public String doFinish (@Valid @ModelAttribute("editMyProfileForm") EditMyProfileForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
    	String formErrors=validate(form, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
       request.getSession(false).removeAttribute("editMyProfileForm");
       
		final HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute(Constants.PHONE_COLLECTION) != null) {
			session.removeAttribute(Constants.PHONE_COLLECTION);
		}

		if (session != null && session.getAttribute(Constants.PHONE_COLLECTION_EDIT_MY_PROFILE) != null) {
			session.removeAttribute(Constants.PHONE_COLLECTION_EDIT_MY_PROFILE);
		}
       
        if(form.isLoginFlow()) {
        	return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;	
        }
        else {
        	return forwards.get(MANAGE_USERS_FORWARD);
        }	
    }

    /**
     * Prepares the request. Specifically, it sets up the contract drop down list if the user is a
     * PSEUM and has multiple contracts.
     *
     * @param actionForm
     * @param request
     */
    protected void populateUserInfo(UserInfo userInfo, EditMyProfileForm form) {

        if (form.isFirstNameChanged())
            userInfo.setFirstName(form.getFirstName());

        if (form.isLastNameChanged())
            userInfo.setLastName(form.getLastName());

        if (form.isEmailChanged())
            userInfo.setEmail(form.getEmail());
        
        if (form.isSecondaryEmailChanged())
            userInfo.setSecondaryEmail(form.getSecondaryEmail());
        
        if (form.isMobileNumberChanged()){
        	userInfo.setMobileNumber(form.getMobileNumber());
        }
        
		userInfo.setPasscodeDeliveryPreference(form.getPasscodeDeliveryPreference());

        if (form.isPhoneNumberChanged()){
        	userInfo.setPhoneNumber(form.getTelephoneNumber());
        }
        if (form.isExtensionChanged()){
        	userInfo.setPhoneExtension(form.getTelephoneExtension());
        }
        if (form.isFaxNumberChanged()){
        	userInfo.setFax(form.getFaxNumber());
        }

        if (form.isPasswordChanged())
            userInfo.setNewPassword(form.getNewPassword());

        userInfo.setPassword(form.getCurrentPassword());
        if (form.isChallengeQuestionRequired()) {
            if (form.isChallengeQuestionChanged())
                userInfo.setChallengeQuestion(form.getChallengeQuestion());
            if (form.isChallengeAnswerChanged())
                userInfo.setChallengeAnswer(form.getChallengeAnswer());
        }
        userInfo.setUpdatedInformation(form.getUpdatedInformation());

        // Populate current Contract Permissions
        if (userInfo.getRole().isTPA()) {
            MyProfileContractAccess contractAccess = form.getContractAccess(0);
            if (contractAccess != null) {
                for (TPAFirmInfo tpaFirm : userInfo.getTpaFirms()) {
                    tpaFirm.getContractPermission().setReceiveIloansEmail(EditMyProfileForm.YES.equals(contractAccess.getReceiveILoanEmail()));
                    tpaFirm.getContractPermission().setNewslettersEmail(EditMyProfileForm.YES.equals(contractAccess.getEmailNewsletter()));
                }
            }
        } else {
            for (MyProfileContractAccess contractAccess : form.getContractAccesses()) {
                ContractPermission contractPermission = userInfo.getContractPermission(contractAccess.getContractNumber().intValue());
                if (contractAccess != null) {
                	if(contractAccess.isPrimaryContactChanged()){
                		contractPermission.setPrimaryContact(contractAccess.isPrimaryContact());
                	}
                	if(contractAccess.isMailRecepientChanged()){
                		contractPermission.setMailRecipient(contractAccess.isMailRecepient());	
                	}
                	if(contractAccess.isTrusteeMailRecepientChanged()){
                		contractPermission.setTrusteeMailRecepient(contractAccess.isTrusteeMailRecepient());	
                	}
                    contractPermission.setReceiveIloansEmail(EditMyProfileForm.YES.equals(contractAccess.getReceiveILoanEmail()));
                    contractPermission.setNewslettersEmail(EditMyProfileForm.YES.equals(contractAccess.getEmailNewsletter()));
                }
            }
        }

    }

    protected void updateSessionPreferences(UserProfile profile, UserInfo userInfo) {
        ContractPermission contractPermission = null;
        if (profile.getRole().isTPA()) {
            contractPermission = userInfo.getTpaFirms()[0].getContractPermission();
        } else {
         if(profile.getCurrentContract() != null) {
            contractPermission = userInfo.getContractPermission(profile.getCurrentContract().getContractNumber());
         } 
        }
        if (contractPermission != null) {
            setPreference(profile, contractPermission, PermissionType.RECEIVE_ILOANS_EMAIL);
            setPreference(profile, contractPermission, PermissionType.NEWSLETTER_EMAIL);
        }
    }

    private void setPreference(UserProfile profile, ContractPermission contractPermission, PermissionType permissionType) {
        if (contractPermission.getRole().hasPermission(permissionType)) {
            profile.getRole().addPermission(permissionType);
        } else {
            profile.getRole().removePermission(permissionType);
        }
    }

    protected void populateForm(EditMyProfileForm form, UserInfo userInfo,
            HttpServletRequest request) throws SystemException {
    	if(StringUtils.isNotEmpty(request.getParameter(LOGIN_FLOW))){
    		form.setLoginFlow(Boolean.TRUE);
    	}
        form.setProfileId(userInfo.getProfileId());
        form.setUserName(userInfo.getUserName());
        form.setFirstName(userInfo.getFirstName());
        form.setOldFirstName(userInfo.getFirstName());
        form.setLastName(userInfo.getLastName());
        form.setOldLastName(userInfo.getLastName());
        form.setEmail(userInfo.getEmail());
        form.setOldEmail(userInfo.getEmail());
        form.setSecondaryEmail(userInfo.getSecondaryEmail());
        form.setOldSecondaryEmail(userInfo.getSecondaryEmail());
        if(userInfo.getPhoneNumber() != null) {
            form.setTelephoneNumber(userInfo.getPhoneNumber());
            form.setOldTelephoneNumber(userInfo.getPhoneNumber().getValue());
        }
        
        if(userInfo.getMobileNumber() != null) {
            form.setMobileNumber(userInfo.getMobileNumber());
            form.setOldMobileNumber(userInfo.getMobileNumber().getValue());
        }
        
        if(userInfo.getPasscodeDeliveryPreference() != null) {
            form.setPasscodeDeliveryPreference(userInfo.getPasscodeDeliveryPreference());
            form.setOldPasscodeDeliveryPreference(userInfo.getPasscodeDeliveryPreference());
        }
        
        
        if(userInfo.getPhoneExtension() != null) {
        	form.setTelephoneExtension(userInfo.getPhoneExtension());
            form.setOldTelephoneExtension(userInfo.getPhoneExtension());
        }
        if(userInfo.getFax() != null) {
        	form.setFaxNumber(userInfo.getFax());
            form.setOldFaxNumber(userInfo.getFax().getValue());
        }
        form.setNewPassword("");
        form.setConfirmNewPassword("");
        form.setCurrentPassword("");
        form.setOldPassword(userInfo.getPassword());
        form.setPasscodeDeliveryPreference(userInfo.getPasscodeDeliveryPreference());
        UserProfile userProfile = getUserProfile(request);
        UserRole role = userProfile.getRole();
        if (!(role instanceof InternalUser || role instanceof ThirdPartyAdministrator)) {
            form.setChallengeQuestionRequired(true);
        } else {
            form.setChallengeQuestionRequired(false);
        }
        if (form.isChallengeQuestionRequired()) {
            form.setChallengeQuestion(userInfo.getChallengeQuestion());
            form.setOldChallengeQuestion(userInfo.getChallengeQuestion());
            form.setChallengeAnswer("");
            form.setOldChallengeAnswer(userInfo.getChallengeAnswer());
            form.setVerifyChallengeAnswer("");
        }
        if (role.isTPA()) {
            populateContractAccesses(form, userInfo.getTpaFirms());
        } else {
            populateContractAccesses(form, userInfo.getContractPermissions());
            if (role.isExternalUser()) {
            	populateContractAccessForExternalUser(form, userInfo, request);
            }	
        }

        form.setProfileLastUpdatedBy(userInfo.getProfileLastUpdatedBy());
        form.setProfileLastUpdatedTS(userInfo.getProfileLastUpdatedTS());
        form.setProfileLastUpdatedByInternal(userInfo.isProfileLastUpdatedByInternal());
        if (userInfo.getRole() instanceof InternalUserManager)
            form.setInternalUser(true);
        form.resetUpdatedInformation();
    }

    private void populateContractAccesses(EditMyProfileForm form, ContractPermission[] contractPermissions) throws SystemException {
        List<MyProfileContractAccess> contractAccesses = new ArrayList<MyProfileContractAccess>();
        for (ContractPermission contractPermission : contractPermissions) {
            MyProfileContractAccess contractAccess = new MyProfileContractAccess();

            contractAccess.setContractNumber(new Integer(contractPermission.getContractNumber()));
            contractAccess.setContractName(contractPermission.getCompanyName());
            populateContractAccess(contractAccess, contractPermission);
            evaluateEmailDisplayRules(contractAccess, contractPermission);
 
            contractAccesses.add(contractAccess);
        }
        form.setContractAccesses(contractAccesses);

    }

    private void populateContractAccesses(EditMyProfileForm form, TPAFirmInfo[] tpaFirms) throws SystemException {
        List<MyProfileContractAccess> contractAccesses = new ArrayList<MyProfileContractAccess>();
        MyProfileContractAccess contractAccess = new MyProfileContractAccess();

        for (TPAFirmInfo tpaFirm : tpaFirms) {
            contractAccess.getTpaFirmIds().add(new Integer(tpaFirm.getId()));
        }
        populateContractAccess(contractAccess, tpaFirms[0].getContractPermission());
        evaluateTPAEmailDisplayRules(contractAccess, tpaFirms);

        contractAccesses.add(contractAccess);
        form.setContractAccesses(contractAccesses);
    }

    private void populateContractAccess(MyProfileContractAccess contractAccess, ContractPermission contractPermission) throws SystemException {
        UserRole userRole = contractPermission.getRole();

        if (userRole.hasPermission(PermissionType.RECEIVE_ILOANS_EMAIL)) {
            contractAccess.setOldReceiveILoanEmail(EditMyProfileForm.YES);
            contractAccess.setReceiveILoanEmail(EditMyProfileForm.YES);
        } else {
            contractAccess.setOldReceiveILoanEmail(EditMyProfileForm.NO);
            contractAccess.setReceiveILoanEmail(EditMyProfileForm.NO);
        }

        if (userRole.hasPermission(PermissionType.NEWSLETTER_EMAIL)) {
            contractAccess.setEmailNewsletter(EditMyProfileForm.YES);
            contractAccess.setOldEmailNewsletter(EditMyProfileForm.YES);
        } else {
            contractAccess.setEmailNewsletter(EditMyProfileForm.NO);
            contractAccess.setOldEmailNewsletter(EditMyProfileForm.NO);
        }
    }

    private void evaluateEmailDisplayRules(MyProfileContractAccess contractAccess, ContractPermission contractPermission) throws SystemException {
        UserRole userRole = contractPermission.getRole();
        if (userRole.getDefaultRolePermissions() == null) {
            userRole.setDefaultRolePermissions(SecurityServiceDelegate.getInstance().getDefaultRolePermissions(userRole.toString()));
        }

        boolean hasWithdrawalsServiceFeature = false;
        boolean hasReviewWithdrawalsServiceFeature = false;
        boolean hasPAMServiceFeature = false;
        boolean hasAutoEnrollmentServiceFeature = false;
        boolean hasPayrollPathServiceFeature = false;
        boolean aciOn = false;

        ArrayList<String> serviceFeatureList = new ArrayList<String>();

        serviceFeatureList.add(ServiceFeatureConstants.IWITHDRAWALS_FEATURE);
        serviceFeatureList.add(ServiceFeatureConstants.ADDRESS_MANAGEMENT_FEATURE);
        serviceFeatureList.add(ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE);
        serviceFeatureList.add(ServiceFeatureConstants.PAYROLL_PATH_FEATURE);
        serviceFeatureList.add(ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE);


        try {
            Map<String, ContractServiceFeature> serviceFeatureMap = ContractServiceDelegate.getInstance().getContractServiceFeatures(contractPermission.getContractNumber(),
                    serviceFeatureList);

            ContractServiceFeature withdrawalsCSF = serviceFeatureMap.get(ServiceFeatureConstants.IWITHDRAWALS_FEATURE);
            if (withdrawalsCSF != null) {
                hasWithdrawalsServiceFeature = ContractServiceFeature.internalToBoolean(withdrawalsCSF.getValue()).booleanValue();
                hasReviewWithdrawalsServiceFeature = ContractServiceFeature.internalToBoolean(withdrawalsCSF.getAttributeValue(ServiceFeatureConstants.IWITHDRAWALS_REVIEW)).booleanValue();
            }

            ContractServiceFeature pamCSF = serviceFeatureMap.get(ServiceFeatureConstants.ADDRESS_MANAGEMENT_FEATURE);
            if (pamCSF != null) {
                ParticipantAddressContractServiceFeature pamPACSF = new ParticipantAddressContractServiceFeature(pamCSF);
                hasPAMServiceFeature = (pamPACSF.getValues().size() > 0);
            }

            ContractServiceFeature autoEnrollmentCSF = serviceFeatureMap.get(ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE);
            if (autoEnrollmentCSF != null) {
                hasAutoEnrollmentServiceFeature = ContractServiceFeature.internalToBoolean(autoEnrollmentCSF.getValue()).booleanValue();
            }

            ContractServiceFeature payrollPathCSF = serviceFeatureMap.get(ServiceFeatureConstants.PAYROLL_PATH_FEATURE);
            if (payrollPathCSF != null) {
                hasPayrollPathServiceFeature = ContractServiceFeature.internalToBoolean(payrollPathCSF.getValue()).booleanValue();
            }

            ContractServiceFeature csf =  serviceFeatureMap.get(ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE);
            if ((csf !=null) &&
    			(ContractServiceFeature.internalToBoolean(csf.getValue()).booleanValue() ||
            	 Constants.ACI_DEFAULTED_TO_YES.equals(csf.getValue()))) {
            	aciOn = true;
    		}
        } catch (ApplicationException ae) {
            throw new SystemException(ae, EditMyProfileController.class.getName(), "populateContractAccess", ae.getMessage());
        }

        contractAccess.setShowReceiveILoanEmail(false);
        contractAccess.setShowEmailNewsletter(false);
    }

    private void evaluateTPAEmailDisplayRules(MyProfileContractAccess contractAccess, TPAFirmInfo[] tpaFirms) throws SystemException {

        boolean aciOn = false;
        boolean aeOn = false;
        boolean showReceiveILoanEmail = false;

        for (TPAFirmInfo tpaFirm : tpaFirms) {

            List<Integer> contractNumbers = TPAServiceDelegate.getInstance().getContractsByFirm(tpaFirm.getId());
            for (Integer contractNumber : contractNumbers) {
                LoanSettings loanSettings = LoanServiceDelegate.getInstance().getLoanSettings(
                        contractNumber);
                if (loanSettings.isLrk01() && !(loanSettings.isAllowOnlineLoans())) {
                    showReceiveILoanEmail = true;
                } 
            }
        }

        contractAccess.setShowEmailNewsletter(true);
        contractAccess.setShowReceiveILoanEmail(showReceiveILoanEmail);
    }

    /**
     * Overrides the super.validate() method to set the error collection into the session.
     *
     * @see com.manulife.pension.ps.web.controller.PsController#validate(org.apache.struts.action.ActionMapping, org.apache.struts.action.Form,
     *      javax.servlet.http.HttpServletRequest)
     */
    @Autowired
	   private PSValidatorFWNull  psValidatorFWNull;
    @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWNull);
	}
    
   protected String validate( ActionForm actionForm,
            HttpServletRequest request) {
        EditMyProfileForm form = (EditMyProfileForm) actionForm;

        if ("confirm".equals(form.getAction())) {
            if (form.getUserName() == null || form.getUserName().length() == 0) {
                if(form.isLoginFlow()) {
                	return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;	
                }
                else {
                	return forwards.get(MANAGE_USERS_FORWARD);
                }	
            }
        }

        Collection errors = doValidate( actionForm, request);
        if (!errors.isEmpty()) {
            SessionHelper.setErrorsInSession(request, errors);
            return forwards.get(REFRESH_FORWARD);
        }

        return null;
    }

    protected Collection doValidate( ActionForm actionForm, HttpServletRequest request) {
        Collection errors =  new ArrayList();
        if (logger.isDebugEnabled()) {
            logger.debug("entry <-- doValidate");
        }
        Pair pair = null;
        EditMyProfileForm form = (EditMyProfileForm) actionForm;

        if (form.isSaveAction()) {
            UserProfile user = getUserProfile(request);

            if (form.isFirstNameChanged()) {
                NameRule.getFirstNameInstance().validate(EditMyProfileForm.FIELD_FIRST_NAME, errors, form.getFirstName());
            }
            if (form.isLastNameChanged()) {
                NameRule.getLastNameInstance().validate(EditMyProfileForm.FIELD_LAST_NAME, errors, form.getLastName());
            }
 
            if (form.isEmailChanged()) {
            	PrimaryEmailRule.getInstance().validate(EditMyProfileForm.FIELD_EMAIL, errors, form.getEmail());
                if (user.getRole().isTPA()) {
                    if (form.getEmail().toLowerCase().contains("@jhancock") || form.getEmail().toLowerCase().contains("@manulife")) {
                        if (!form.getContractAccess(0).getTpaFirmIds().contains(new Integer(52801)) || form.getContractAccess(0).getTpaFirmIds().size() > 1) {
                            errors.add(new GenericException(ErrorCodes.PRIMARY_EMAIL_MUST_BE_EXTERNAL));
                        }
                    }
                } else {
                int currentContractNumber = 0;
                Contract currentContract = user.getCurrentContract();
                if (currentContract != null) {
                	currentContractNumber = currentContract.getContractNumber();
                }
                PrimaryEmailNotLocalRule emailNotLocalRule = new PrimaryEmailNotLocalRule(currentContractNumber);
                emailNotLocalRule.validate(EditMyProfileForm.FIELD_EMAIL, errors, form.getEmail());
                }
            }
            
            if (form.isSecondaryEmailChanged() && StringUtils.isNotBlank(form.getSecondaryEmail())) {
            	SecondaryEmailRule.getInstance().validate(EditMyProfileForm.FIELD_SECONDARY_EMAIL, errors, form.getSecondaryEmail());
            	if(form.getSecondaryEmail().equals(form.getEmail())){
            		errors.add(new GenericException(ErrorCodes.EMAIL_MATCHES_WITH_PRIMARY_EMAIL));
            	}
                if (user.getRole().isTPA()) {
                    if (form.getSecondaryEmail().toLowerCase().contains("@jhancock") || form.getSecondaryEmail().toLowerCase().contains("@manulife")) {
                        if (!form.getContractAccess(0).getTpaFirmIds().contains(new Integer(52801)) || form.getContractAccess(0).getTpaFirmIds().size() > 1) {
                            errors.add(new GenericException(ErrorCodes.SECONDARY_EMAIL_MUST_BE_EXTERNAL));
                        }
                    }
                } else {
                int currentContractNumber = 0;
                Contract currentContract = user.getCurrentContract();
                if (currentContract != null) {
                	currentContractNumber = currentContract.getContractNumber();
                }
                SecondaryEmailNotLocalRule emailNotLocalRule = new SecondaryEmailNotLocalRule(currentContractNumber);
                emailNotLocalRule.validate(EditMyProfileForm.FIELD_SECONDARY_EMAIL, errors, form.getSecondaryEmail());
                }
            }
            
            
            if(form.isPhoneNumberChanged()) {
	            PhoneRule.getInstance().validate(EditMyProfileForm.FIELD_TELEPHONE_NUMBER,	errors, form.getTelephoneNumber().getValue());
				if(StringUtils.isNotEmpty(form.getTelephoneNumber().getValue()))
				{
					if(StringUtils.isEmpty(form.getTelephoneNumber().getAreaCode()) || StringUtils.isEmpty(form.getTelephoneNumber().getPhonePrefix())
								|| StringUtils.isEmpty(form.getTelephoneNumber().getPhoneSuffix()) || form.getTelephoneNumber().getValue().length() < 10)
					{
						errors.add(new ValidationError(EditMyProfileForm.FIELD_TELEPHONE_NUMBER, ErrorCodes.PHONE_NOT_COMPLETE));
					}
					if(StringUtils.isNotEmpty(form.getTelephoneNumber().getAreaCode()) && StringUtils.isNotEmpty(form.getTelephoneNumber().getPhonePrefix()))
					{
						String areaCode = null,phonePrefix = null;
						areaCode = form.getTelephoneNumber().getAreaCode();
						phonePrefix = form.getTelephoneNumber().getPhonePrefix();
						if( areaCode.charAt(0) == '0' || areaCode.charAt(0) == '1' || phonePrefix.charAt(0) == '0' || phonePrefix.charAt(0) == '1')
						{
							errors.add(new ValidationError(EditMyProfileForm.FIELD_TELEPHONE_NUMBER, ErrorCodes.PHONE_INVALID));
						}
					}
				}
            }	
            
            if(form.getTelephoneExtension() != null) {
				ExtensionRule.getInstance().validate(EditMyProfileForm.FIELD_EXTENSION_NUMBER,	errors, form.getTelephoneExtension());
				if( StringUtils.isEmpty(form.getTelephoneNumber().getValue()) && StringUtils.isNotEmpty(form.getTelephoneExtension()) )
				{
					errors.add(new ValidationError(EditMyProfileForm.FIELD_EXTENSION_NUMBER, ErrorCodes.PH_NOTENTERED_EXT_ENTERED));
				}
            }
			
            if(form.isFaxNumberChanged()) {
				FaxRule.getInstance().validate(EditMyProfileForm.FIELD_FAX_NUMBER,	errors, form.getFaxNumber().getValue());
				if(StringUtils.isNotEmpty(form.getFaxNumber().getValue()))
				{
					if(StringUtils.isEmpty(form.getFaxNumber().getAreaCode()) || StringUtils.isEmpty(form.getFaxNumber().getFaxPrefix())
								|| StringUtils.isEmpty(form.getFaxNumber().getFaxSuffix()) || form.getFaxNumber().getValue().length() < 10)
					{
						errors.add(new ValidationError(EditMyProfileForm.FIELD_FAX_NUMBER, ErrorCodes.FAX_NOT_COMPLETE));
					}
					if(StringUtils.isNotEmpty(form.getFaxNumber().getAreaCode()) && StringUtils.isNotEmpty(form.getFaxNumber().getFaxPrefix()))
					{
						String areaCode = null,faxPrefix = null;
						areaCode = form.getFaxNumber().getAreaCode();
						faxPrefix = form.getFaxNumber().getFaxPrefix();
						if( areaCode.charAt(0) == '0' || areaCode.charAt(0) == '1' || faxPrefix.charAt(0) == '0' || faxPrefix.charAt(0) == '1')
						{
							errors.add(new ValidationError(EditMyProfileForm.FIELD_FAX_NUMBER, ErrorCodes.FAX_INVALID));
						}
					}
				}
            }
            
            if(form.isMobileNumberChanged()) {
	            MobileRule.getInstance().validate(EditMyProfileForm.FIELD_MOBILE_NUMBER,	errors, form.getMobileNumber().getValue());
				if(StringUtils.isNotEmpty(form.getMobileNumber().getValue()))
				{
					if(StringUtils.isEmpty(form.getMobileNumber().getAreaCode()) || StringUtils.isEmpty(form.getMobileNumber().getPhonePrefix())
								|| StringUtils.isEmpty(form.getMobileNumber().getPhoneSuffix()) || form.getMobileNumber().getValue().length() < 10)
					{
						errors.add(new ValidationError(EditMyProfileForm.FIELD_MOBILE_NUMBER, ErrorCodes.MOBILE_NOT_COMPLETE));
					}
					if(StringUtils.isNotEmpty(form.getMobileNumber().getAreaCode()) && StringUtils.isNotEmpty(form.getMobileNumber().getPhonePrefix()))
					{
						String areaCode = null,phonePrefix = null;
						areaCode = form.getMobileNumber().getAreaCode();
						phonePrefix = form.getMobileNumber().getPhonePrefix();
						if( areaCode.charAt(0) == '0' || areaCode.charAt(0) == '1')
						{
							errors.add(new ValidationError(EditMyProfileForm.FIELD_MOBILE_NUMBER, ErrorCodes.MOBILE_INVALID));
						}
					}
				}
            }
            
            if(form.getPasscodeDeliveryPreference() == null){
            	errors.add(new ValidationError(EditMyProfileForm.FIELD_PASSCODE_DELIVERY_PREF, ErrorCodes.NO_PHONE_NUMBER_OR_MOBILE_NUMBER_HAS_BEEN_PROVIDED));
            }else{ 

            	switch (form.getPasscodeDeliveryPreference()) {
            	case SMS:
            		if(StringUtils.isEmpty(form.getMobileNumber().getValue())){
            			errors.add(new ValidationError(EditMyProfileForm.FIELD_PASSCODE_DELIVERY_PREF, ErrorCodes.MOBILE_MISSING_FOR_TEXT));
            		}
            		break;
            	case VOICE_TO_MOBILE:
            		if(StringUtils.isEmpty(form.getMobileNumber().getValue())){
            			errors.add(new ValidationError(EditMyProfileForm.FIELD_PASSCODE_DELIVERY_PREF, ErrorCodes.MOBILE_MISSING_FOR_VOICE_MESSAGE));
            		}
            		break;
            	case VOICE_TO_PHONE:
            		if(StringUtils.isEmpty(form.getTelephoneNumber().getValue())){
            			errors.add(new ValidationError(EditMyProfileForm.FIELD_PASSCODE_DELIVERY_PREF, ErrorCodes.PHONE_MISSING_FOR_VOICE_MESSAGE));
            		}
            		if(StringUtils.isNotEmpty(form.getTelephoneExtension())){
            			errors.add(new ValidationError(EditMyProfileForm.FIELD_PASSCODE_DELIVERY_PREF, ErrorCodes.PHONE_NUMBER_HAS_EXTENSION));
            		}
            		break;
            	
            	default:
            		break;
            	}

            }            
			// changes for US 44837
           
            
            if (form.isPasswordChanged() || form.isConfirmNewPasswordChanged()) {
                pair = new Pair(form.getNewPassword(), form.getConfirmNewPassword());
              NewPasswordRule.getInstance().validate(new String[] 
            		{ EditMyProfileForm.FIELD_NEW_PASSWORD, EditMyProfileForm.FIELD_VERIFY_PASSWORD }, 
            		errors, pair);
              // changes for defect 8589 , 8590
              if(null != errors && errors.isEmpty()) {
            	  SecurityServiceDelegate serviceInstance = null;
                  String responseText = null;
                serviceInstance = SecurityServiceDelegate.getInstance();
                try{
                	responseText = serviceInstance.passwordStrengthValidation(form.getNewPassword(),
    					form.getUserName(),Boolean.FALSE);
                	getPasswordScore(responseText,errors,request);
                }catch(Exception e){
                	if (logger.isDebugEnabled()) {
                        logger.debug("exception occured while calling "
                        		+ "passwordStrengthValidation service call" +e.getMessage());
                    }
                }
              }
            }
             // end changes for defect 8589,8590
			// End changes for us 44837
            
            // this section is for External users only
            if (form.isChallengeQuestionRequired()) {
                if (form.isChallengeQuestionChanged()) {
                    QuestionRule.getInstance().validate(EditMyProfileForm.FIELD_CHALLENGE_QUESTION, errors, form.getChallengeQuestion());
                }
                if (form.isChallengeAnswerChanged() || form.isVerifyAnswerChanged()) {
                    pair = new Pair(form.getChallengeAnswer(), form.getVerifyChallengeAnswer());
                    AnswerConfirmRule.getInstance().validate(new String[] { EditMyProfileForm.FIELD_CHALLENGE_ANSWER, EditMyProfileForm.FIELD_VERIFY_ANSWER }, errors,
                            pair);
                }
            }
            // end ExternalOnly sesction

            // System must display an error if current password has not been entered.
            if (!form.isInformationUpdated()) {
                errors.add(new GenericException(ErrorCodes.SAVING_WITH_NO_CHANGES));
            } else {
                MandatoryRule rule = new MandatoryRule(ErrorCodes.CURRENT_PASSWORD_MANDATORY);
                rule.validate(EditMyProfileForm.FIELD_CURRENT_PASSWORD, errors, form.getCurrentPassword());
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <-- doValidate");
        }
        return errors;
    }
	  
    /**
     * Method populateContractAccessForExternalUser is used to load the Contract access for External user
     * @param form
     * @param userInfo
     * @param request
     * @throws SystemException
     */
    private void populateContractAccessForExternalUser(EditMyProfileForm form, UserInfo userInfo, HttpServletRequest request) throws SystemException {
    	try {
    		UserRole role = null;
	    	for(MyProfileContractAccess contractAccess : form.getContractAccesses()) {
	    		ContractPermission contractPermission = userInfo.getContractPermission(contractAccess.getContractNumber());
		    	if(null != contractPermission) {
		    		role = contractPermission.getRole();
			    	if(role != null ) {
			        	contractAccess.setTrusteeMailRecepientAllowed(Boolean.FALSE);
			        	contractAccess.setDisableAttributes(Boolean.FALSE);
				        if(role instanceof Trustee || role instanceof PlanSponsorUser) {
				        	contractAccess.setSpecialAttributeAllowed(Boolean.TRUE);
				        	contractAccess.setTrusteeMailRecepientAllowed(Boolean.TRUE);
				        }
				        else if(role instanceof AuthorizedSignor) {
				        	contractAccess.setSpecialAttributeAllowed(Boolean.TRUE);
				        }
				        else if(role instanceof AdministrativeContact) {
				        	contractAccess.setSpecialAttributeAllowed(Boolean.TRUE);
				        }
				        else if(contractPermission.isPrimaryContact() || contractPermission.isMailRecipient() 
				        		|| contractPermission.isTrusteeMailRecepient()) {
				        	contractAccess.setDisableAttributes(Boolean.TRUE);
				        	contractAccess.setSpecialAttributeAllowed(Boolean.TRUE);
				        	if(contractPermission.isTrusteeMailRecepient()) {
				        		contractAccess.setTrusteeMailRecepientAllowed(Boolean.TRUE);
				        	}
				        }
				        else {
				        	contractAccess.setSpecialAttributeAllowed(Boolean.FALSE);
				        }
			        }
			        contractAccess.setPrimaryContact(contractPermission.isPrimaryContact());
			        contractAccess.setOldPrimaryContact(contractPermission.isPrimaryContact());
			       	contractAccess.setMailRecepient(contractPermission.isMailRecipient());
			       	contractAccess.setOldMailRecepient(contractPermission.isMailRecipient());
			       	contractAccess.setTrusteeMailRecepient(contractPermission.isTrusteeMailRecepient());
			       	contractAccess.setOldTrusteeMailRecepient(contractPermission.isTrusteeMailRecepient());
			       	
			       	SecurityServiceDelegate service = SecurityServiceDelegate.getInstance();
			        UserInfo primaryContactUserInfo = null;
			        UserInfo mailRecipientUserInfo = null;
			        UserInfo trusteeMailRecipientUserInfo = null;
			       	int contractNumber = contractPermission.getContractNumber();
			        // Get the contract info since the cached contract info is stale
			        Contract contract = ClientUserContractAccessActionHelper.getContract(contractPermission.getRole(), contractNumber);
			       	
			        if (contract.getPrimaryContact() != null) {
			            long primaryContactProfileId = contract.getPrimaryContact().getProfileId();
			            if (primaryContactProfileId != 0 && primaryContactProfileId != userInfo.getProfileId()) {
			                primaryContactUserInfo = service.searchByProfileId(AbstractAddEditClientUserController.newPrincipal(new Long(0), null, null), primaryContactProfileId);
                        	contractAccess.setPrimaryContactFirstName(primaryContactUserInfo.getFirstName());
                        	contractAccess.setPrimaryContactLastName(primaryContactUserInfo.getLastName());			                
			            }
			        }
			        
			       	if (contract.getMailRecipient() != null) {
                        long mailRecipientProfileId = contract.getMailRecipient().getProfileId();
                        if (mailRecipientProfileId != 0 && mailRecipientProfileId != userInfo.getProfileId()) {
                        	mailRecipientUserInfo = service.searchByProfileId(AbstractAddEditClientUserController.newPrincipal(new Long(0), null, null), mailRecipientProfileId);
                        	contractAccess.setClientMailFirstName(mailRecipientUserInfo.getFirstName());
                        	contractAccess.setClientMailLastName(mailRecipientUserInfo.getLastName());
                        }
                    }
			       	
			     	if (contract.getTrusteeMailRecipient() != null) {
                        long trusteeMailRecipientProfileId = contract.getTrusteeMailRecipient().getProfileId();
                        if (trusteeMailRecipientProfileId != 0 && trusteeMailRecipientProfileId != userInfo.getProfileId()) {
                        	trusteeMailRecipientUserInfo = service.searchByProfileId(AbstractAddEditClientUserController.newPrincipal(new Long(0), null, null), trusteeMailRecipientProfileId);
                        	contractAccess.setTrusteeMailFirstName(trusteeMailRecipientUserInfo.getFirstName());
                        	contractAccess.setTrusteeMailLastName(trusteeMailRecipientUserInfo.getLastName());
                        }
                    }
		    	}	
	    	}	
    	}
    	catch (SecurityServiceException e) {
    		 List errors = new ArrayList();
             String fieldId = (String) securityExceptionFormFieldMap.get(e.getClass());

             if (fieldId == null) {
                 errors.add(new GenericException(Integer.parseInt(e.getErrorCode())));
             } else {
                 errors.add(new ValidationError(fieldId, Integer.parseInt(e.getErrorCode())));
             }

             SessionHelper.setErrorsInSession(request, errors);
        }
    }
    
    /**
     * Method to remove any previous primary contact/client mail recipient/trustee mail recepient 
     * on the contracts
     * @param principal
     * @param userInfo
     * @throws SecurityServiceException
     * @throws SystemException
     */
    private void updateContractContacts(Principal principal, UserInfo userInfo) throws SecurityServiceException, SystemException {
        SecurityServiceDelegate service = SecurityServiceDelegate.getInstance();
        for (ContractPermission contractPermission : userInfo.getContractPermissions()) {
            if (contractPermission.isPrimaryContact() || contractPermission.isMailRecipient() || contractPermission.isTrusteeMailRecepient()) {
                int contractNumber = contractPermission.getContractNumber();
                // Get the contract info since the cached contract info is stale
                Contract contract = ClientUserContractAccessActionHelper.getContract(contractPermission.getRole(), contractNumber);
                if (contract != null) {
                    UserInfo primaryContactUserInfo = null;
                    UserInfo mailRecipientUserInfo = null;
                    UserInfo trusteeMailRecipientUserInfo = null;
                    // If this user has been designated as the primary contact we need to change the existing primary contact to no
                    if (contractPermission.isPrimaryContact() && contract.getPrimaryContact() != null) {
                        long primaryContactProfileId = contract.getPrimaryContact().getProfileId();
                        if (primaryContactProfileId != 0 && primaryContactProfileId != userInfo.getProfileId()) {
                            primaryContactUserInfo = service.searchByProfileId(AbstractAddEditClientUserController.newPrincipal(new Long(0), null, null), primaryContactProfileId);
                            primaryContactUserInfo.getContractPermission(contractNumber).setPrimaryContact(false);
                        }
                    }
                    // If this user has been designated as the mail recipient we need to change the existing mail recipient to no
                    if (contractPermission.isMailRecipient() && contract.getMailRecipient() != null) {
                        long mailRecipientProfileId = contract.getMailRecipient().getProfileId();
                        if (mailRecipientProfileId != 0 && mailRecipientProfileId != userInfo.getProfileId()) {
                            if (primaryContactUserInfo != null && primaryContactUserInfo.getProfileId() == mailRecipientProfileId) {
                                primaryContactUserInfo.getContractPermission(contractNumber).setMailRecipient(false);
                            } else {
                                mailRecipientUserInfo = service.searchByProfileId(AbstractAddEditClientUserController.newPrincipal(new Long(0), null, null), mailRecipientProfileId);
                                mailRecipientUserInfo.getContractPermission(contractNumber).setMailRecipient(false);
                            }
                        }
                    }
                    // If this user has been designated as the trustee mail recipient we need to change the existing trustee mail recipient to no
                    if (contractPermission.isTrusteeMailRecepient() && contract.getTrusteeMailRecipient() != null) {
                        long trusteeMailRecipientProfileId = contract.getTrusteeMailRecipient().getProfileId();
                        if (trusteeMailRecipientProfileId != 0 && trusteeMailRecipientProfileId != userInfo.getProfileId()) {
                            if (primaryContactUserInfo != null && primaryContactUserInfo.getProfileId() == trusteeMailRecipientProfileId) {
                                primaryContactUserInfo.getContractPermission(contractNumber).setTrusteeMailRecepient(false);
                            } 
                            else if(mailRecipientUserInfo != null && mailRecipientUserInfo.getProfileId() == trusteeMailRecipientProfileId) {
                            	mailRecipientUserInfo.getContractPermission(contractNumber).setTrusteeMailRecepient(false);
                            }
                            else {
                            	trusteeMailRecipientUserInfo = service.searchByProfileId(AbstractAddEditClientUserController.newPrincipal(new Long(0), null, null), trusteeMailRecipientProfileId);
                            	trusteeMailRecipientUserInfo.getContractPermission(contractNumber).setTrusteeMailRecepient(false);
                            }
                        }
                    }
                    if (primaryContactUserInfo != null) {
                        service.updateUser(principal, Environment.getInstance().getSiteLocation(), primaryContactUserInfo);
                    }
                    if (mailRecipientUserInfo != null) {
                        service.updateUser(principal, Environment.getInstance().getSiteLocation(), mailRecipientUserInfo);
                    }
                    if(trusteeMailRecipientUserInfo != null) {
                    	service.updateUser(principal, Environment.getInstance().getSiteLocation(), trusteeMailRecipientUserInfo);
                    }
                }
            }
        }
    }
    /*
    * @see com.manulife.pension.platform.web.controller.BaseAction#getSubmitAction(javax.servlet.http.HttpServletRequest, org.apache.struts.action.Form)
    */
   @Override
   protected String getSubmitAction(HttpServletRequest request, ActionForm form)
   		throws ServletException {
   	String action = super.getSubmitAction(request,form);
   	EditMyProfileForm actionForm = (EditMyProfileForm)form ;
   	 if(StringUtils.isNotBlank(actionForm.getAction()))
  		  action = actionForm.getAction()!= null ? actionForm.getAction():StringUtils.EMPTY;
   	return action;
   }
    
   /*  avoids token generation as this class acts as intermediate for many
	 * transactions (non-Javadoc)
     * 
     * @see com.manulife.pension.platform.web.controller.BaseAction#isTokenRequired(java.lang.String)
     
    @Override
	protected boolean isTokenRequired(String action) {
		return true;
	}
    
    
	 * Returns true if token has to be validated for the particular action call
	 * to avoid CSRF vulnerability else false. (non-Javadoc)
	 * 
	 * @see com.manulife.pension.platform.web.controller.BaseAction#isTokenValidatorEnabled(java.lang.String)
	 
    @Override
	protected boolean isTokenValidatorEnabled(String action) {
    	// avoids methods from validation which ever is not required
    	if (StringUtils.isNotEmpty(action)
				&& (StringUtils.containsIgnoreCase(action, "Save"))){
					return true;
				}
		return false;
	}*/
   
   @RequestMapping(value ="/editMyProfile/ajaxvalidator/" ,method =  {RequestMethod.POST})
	@ResponseBody
	public String doDeApiAjaxCall(@Valid @ModelAttribute("editMyProfileForm") EditMyProfileForm form, BindingResult bindingResult,
           HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SystemException,SecurityServiceException, IOException{
		
      //Password Meter Validation DeApi Call
	  //Passing Boolean value as False for only FRW External Users to validate
      	PasswordMeterUtility.validatePasswordMeter(request,response,userInfo.getUserName().toString(),Boolean.FALSE); 
		return null;
	}
   
   private boolean isUserExempted(final long profileId, final String ipAddress) {
		return PasscodeSecurity.PS.isExemptUserProfile(profileId, ipAddress);
}
   private void getPasswordScore(String responseText, Collection errors,HttpServletRequest request) {
	   int score = 0;
		String deApiStatus = null;
		JsonElement jsonElement = null;
		final int passwordScore = 2;
		if (null != responseText) {
			jsonElement = new JsonParser().parse(responseText);
		}
		JsonObject jsonObject = null;
		if (null != jsonElement) {
			jsonObject = jsonElement.getAsJsonObject();
		}
		if (null != jsonObject && null != jsonObject.get("Deapi")) {
			deApiStatus = jsonObject.get("Deapi").getAsString();
		}

		if (null != deApiStatus && !deApiStatus.isEmpty() && deApiStatus.equalsIgnoreCase("down")) {
			request.getSession(false).setAttribute("Deapi", "down");
		} else {
			if (null != jsonObject && null != jsonObject.get("score")) {
				score = jsonObject.get("score").getAsInt();
			}
			request.getSession(false).setAttribute("Deapi", "up");
			if (score < passwordScore) {
				errors.add(new ValidationError
						(EditMyProfileForm.FIELD_NEW_PASSWORD ,CommonErrorCodes.PASSWORD_FAILS_STANDARDS));
			}
		}
	}
}
