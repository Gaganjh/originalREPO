package com.manulife.pension.ps.web.profiles;

import static com.manulife.pension.ps.web.Constants.FORWARD_TPA_CONTACTS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.LockServiceDelegate;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.passcode.RequestDetails;
import com.manulife.pension.service.security.utility.log.PsEventLog;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.EventLogFactory;
import com.manulife.pension.util.log.LogEventException;

/**
 * This is Action class for ManagePassword
 *
 * @author Ludmila Stern
 */
@Controller
@RequestMapping( value = "/password")
@SessionAttributes({"managePasswordForm"})

public class ManagePasswordController extends PsAutoController {
	
	@ModelAttribute("managePasswordForm") 
	public ManagePasswordForm populateForm() 
	{
		return new ManagePasswordForm();
}
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	
	private static final String REFRESH_ACTION = "refresh";
	private static final String INPUT = "input";
	private static final String MANAGE_USERS_ACTION = "manageUsers";	
	private static final String RESET_ACTION = "reset";
	private static final String RESET_ACTION_INT = "resetInt";
	private static final String RESET_VIEW_PASSWORD_ACTION = "resetViewPassword";
	private static final String RESET_PAGE = "resetPage";
	private static final String RESET_PAGE_INT = "resetPageInt";
	private static final String UNLOCK_PASSWORD_ACTION = "unlock";
	private static final String UNLOCK_PASSWORD_ACTION_INT = "unlockInt";
	private static final String UNLOCK_PASSWORD_PAGE = "unlockPage";
	private static final String UNLOCK_PASSWORD_PAGE_INT = "unlockPageInt";	
	private static final String UNLOCKPASSCODE_ACTION = "unlockPasscode";
	private static final String FINISH_ACTION = "finish";
	private static final String UNLOCKPASSCODEPAGE_ACTION = "unlockPasscodePage";
	private static final String PSCONTACTS_ACTION = "planSponsorContacts";
	private static final String TPACONTACTS_ACTION = "tpaContactsTab";
	private static final String INTRENAL_ACTION = "internal";
	private static final String UNLOCKPASSINT_ACTION = "unlockPasscodeInt";
	private static final String FINISHINT_ACTION = "finishInt";
	private static final String UNLOCKPASSPAGEINT_ACTION = "unlockPasscodePageInt";
	private static final String PSCONTACTSINT_ACTION = "planSponsorContactsInt";
	private static final String REFRESHINT_ACTION = "refreshInt";
	private static final String TPA_ACTION = "tpa";
	static{
		forwards.put(INPUT,"/password/managePassword.jsp");
		
	    forwards.put(RESET_ACTION,"redirect:/do/password/managePassword/?action=refreshReset");
        forwards.put(UNLOCK_PASSWORD_ACTION,"redirect:/do/password/managePassword/?action=refreshUnlock");
        forwards.put(UNLOCKPASSCODE_ACTION,"redirect:/do/password/managePassword/?action=refreshUnlockPasscode");
        forwards.put(FINISH_ACTION,"redirect:/do/contacts/planSponsor/?lastVisited=true");
        forwards.put(RESET_PAGE,"/password/resetPassword.jsp");
        forwards.put(UNLOCK_PASSWORD_PAGE,"/password/unlockPassword.jsp");
        forwards.put(UNLOCKPASSCODEPAGE_ACTION,"/password/unlockPasscode.jsp");
        forwards.put(PSCONTACTS_ACTION,"/do/contacts/planSponsor/?lastVisited=true");
        forwards.put(REFRESH_ACTION,"redirect:/do/password/managePassword/?action=refresh");
        forwards.put(TPACONTACTS_ACTION,"redirect:/do/contacts/thirdPartyAdministrator/");
        forwards.put(MANAGE_USERS_ACTION,"redirect:/do/profiles/manageUsers/");
        forwards.put(TPA_ACTION,"redirect:/do/profiles/manageUsers/");
        forwards.put(INTRENAL_ACTION,"redirect:/do/profiles/manageInternalUsers/");
        
        //Internal urls
        forwards.put(RESET_ACTION_INT,"redirect:/do/password/manageInternalPassword/?action=refreshReset");
        forwards.put(UNLOCK_PASSWORD_ACTION_INT,"redirect:/do/password/manageInternalPassword/?action=refreshUnlock");
        forwards.put(UNLOCKPASSINT_ACTION,"redirect:/do/password/managePassword/?action=refreshUnlockPasscode");
        forwards.put(FINISHINT_ACTION,"redirect:/do/profiles/manageUsers/?lastVisited=true");
        forwards.put(RESET_PAGE_INT,"/password/resetPassword.jsp");
        forwards.put(UNLOCK_PASSWORD_PAGE_INT,"/password/unlockPassword.jsp");
        forwards.put(UNLOCKPASSPAGEINT_ACTION,"/password/unlockPasscode.jsp");
        forwards.put(PSCONTACTSINT_ACTION,"/do/contacts/planSponsor/?lastVisited=true");
        forwards.put(REFRESHINT_ACTION,"redirect:/do/password/manageInternalPassword/?action=refresh");

	}


	/**
	 * Constructor.
	 */
	public ManagePasswordController() {
		super();
	}

		/**
	 *      ManagePasswordForm,
	 *      BindingResult,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	
	@RequestMapping(value ={"/managePassword"} , method = {RequestMethod.GET,RequestMethod.POST}) 
	public String doDefault(@Valid @ModelAttribute("managePasswordForm") ManagePasswordForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
        		  UserInfo userInfo = populateUserInfo(actionForm);
                  request.setAttribute(Constants.USERINFO_KEY, userInfo);
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(INPUT);//if input forward not //available, provided default
        	}
        } 
	

		if (logger.isDebugEnabled()) {
			logger.debug("entry <-- doDefault");
		}

		try {
			UserProfile loggedInUserProfile = getUserProfile(request);
			String profileId =  request.getParameter("profileId");
			String userName = StringUtils.EMPTY;
			userName = request.getParameter("userName");
			
			if(StringUtils.isBlank(profileId) && StringUtils.isBlank(userName) ){
				if(actionForm.isFromTPAContactsTab()){
                	return forwards.get(FORWARD_TPA_CONTACTS);
                } else if (actionForm.isFromPSContactTab()){
                	return forwards.get(Constants.FORWARD_PLANSPONSOR_CONTACTS);
                } else {
                	return forwards.get(MANAGE_USERS_ACTION);
                }
			}
			
			if(StringUtils.isBlank(userName) && !StringUtils.isBlank(profileId)){
				userName =  SecurityServiceDelegate.getInstance().getLDAPUserNameByProfileId(Long.valueOf(profileId));
			}
			
		    final UserInfo userInfo = getUserInfoInstance(userName, getUserProfile(request).getPrincipal());
		    
			if (userInfo != null)
			{
				long lockedUserProfileId = userInfo.getProfileId();
				populateForm(actionForm, userInfo);
				request.setAttribute(Constants.USERINFO_KEY, userInfo);
				
                Collection<GenericException> errors = TPAUserContractAccessActionHelper.checkForLockOrDelete(loggedInUserProfile, lockedUserProfileId, userName);
                if (!errors.isEmpty()) {
                    SessionHelper.setErrorsInSession(request, errors);
                    if(actionForm.isFromTPAContactsTab()){
                    	return forwards.get(FORWARD_TPA_CONTACTS);
                    } else if (actionForm.isFromPSContactTab()){
                    	return forwards.get(Constants.FORWARD_PLANSPONSOR_CONTACTS);
                    } else {
                    	return forwards.get(MANAGE_USERS_ACTION);
                    }
                }
			}

		} catch (SecurityServiceException e){
			Collection<GenericException> errors = new ArrayList<GenericException>();
			errors
					.add(new GenericException(Integer
							.parseInt(e.getErrorCode())));
			SessionHelper.setErrorsInSession(request, errors);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <-- doDefault");
		}

		return forwards.get(INPUT);
	}
	@RequestMapping(value ={"/manageInternalPassword/"} , method = {RequestMethod.GET,RequestMethod.POST}) 
	public String doDefaultInt(@Valid @ModelAttribute("managePasswordForm") ManagePasswordForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
        		  UserInfo userInfo = populateUserInfo(actionForm);
                  request.setAttribute(Constants.USERINFO_KEY, userInfo);
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(INPUT);//if input forward not //available, provided default
        	}
        } 
	

		if (logger.isDebugEnabled()) {
			logger.debug("entry <-- doDefault");
		}

		try {
			UserProfile loggedInUserProfile = getUserProfile(request);
			String profileId =  request.getParameter("profileId");
			String userName = StringUtils.EMPTY;
			userName = request.getParameter("userName");
			
			if(StringUtils.isBlank(profileId) && StringUtils.isBlank(userName) ){
				if(actionForm.isFromTPAContactsTab()){
                	return forwards.get(FORWARD_TPA_CONTACTS);
                } else if (actionForm.isFromPSContactTab()){
                	return forwards.get(Constants.FORWARD_PLANSPONSOR_CONTACTS);
                } else {
                	return forwards.get(MANAGE_USERS_ACTION);
                }
			}
			
			if(StringUtils.isBlank(userName) && !StringUtils.isBlank(profileId)){
				userName =  SecurityServiceDelegate.getInstance().getLDAPUserNameByProfileId(Long.valueOf(profileId));
			}
			
		    final UserInfo userInfo = getUserInfoInstance(userName, getUserProfile(request).getPrincipal());
		    
			if (userInfo != null)
			{
				long lockedUserProfileId = userInfo.getProfileId();
				populateForm(actionForm, userInfo);
				request.setAttribute(Constants.USERINFO_KEY, userInfo);
				
                Collection<GenericException> errors = TPAUserContractAccessActionHelper.checkForLockOrDelete(loggedInUserProfile, lockedUserProfileId, userName);
                if (!errors.isEmpty()) {
                    SessionHelper.setErrorsInSession(request, errors);
                    if(actionForm.isFromTPAContactsTab()){
                    	return forwards.get(FORWARD_TPA_CONTACTS);
                    } else if (actionForm.isFromPSContactTab()){
                    	return forwards.get(Constants.FORWARD_PLANSPONSOR_CONTACTS);
                    } else {
                    	return forwards.get(MANAGE_USERS_ACTION);
                    }
                }
			}

		} catch (SecurityServiceException e){
			Collection<GenericException> errors = new ArrayList<GenericException>();
			errors
					.add(new GenericException(Integer
							.parseInt(e.getErrorCode())));
			SessionHelper.setErrorsInSession(request, errors);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <-- doDefault");
		}

		return forwards.get(INPUT);
	}
	@RequestMapping(value ="/managePassword",params="action=printPDF" , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doPrintPDF (@Valid @ModelAttribute("managePasswordForm") ManagePasswordForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		   String forward=super.doPrintPDF( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
 		
	/**
	 * Simply refresh the page. This action is used when we try to perform a
	 * REDIRECT after an unsuccessful POST.
	 *
	 * @param form
	 * @param request
	 * @param response
	 * @return The input forward.
	 * @throws SecurityServiceException 
	 */
	@RequestMapping(value ="/managePassword" ,params="action=refresh"  , method =  RequestMethod.GET) 
	public String doRefresh (@Valid @ModelAttribute("managePasswordForm") ManagePasswordForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException, SecurityServiceException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
        		UserInfo userInfo = populateUserInfo(form);
                request.setAttribute(Constants.USERINFO_KEY, userInfo);
                request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
                return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(INPUT);//if input forward not //available, provided default
        	}
        } 
	
	
		
		final String forward = handleUnexpectedPageState(form,false);
		if (forward != null) {
		    return forward;
		}
		
		final UserInfo userInfo = getUserInfoInstance(form.getUserName(), getUserProfile(request).getPrincipal());
		
		if (userInfo != null) {
			populateForm(form, userInfo);
			request.setAttribute(Constants.USERINFO_KEY, userInfo);
		}

		return forwards.get(INPUT);
	}
	@RequestMapping(value ="/manageInternalPassword/" ,params="action=refresh"  , method =  RequestMethod.GET) 
	public String doRefreshInter (@Valid @ModelAttribute("managePasswordForm") ManagePasswordForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException, SecurityServiceException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
        		UserInfo userInfo = populateUserInfo(form);
                request.setAttribute(Constants.USERINFO_KEY, userInfo);
                request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
                return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(INPUT);//if input forward not //available, provided default
        	}
        } 
		final String forward = handleUnexpectedPageState(form,true);
		if (forward != null) {
		    return forward;
		}
		
		final UserInfo userInfo = getUserInfoInstance(form.getUserName(), getUserProfile(request).getPrincipal());
		
		if (userInfo != null) {
			populateForm(form, userInfo);
			request.setAttribute(Constants.USERINFO_KEY, userInfo);
		}

		return forwards.get(INPUT);
	}


	/**
	 * Simply refresh the page. This action is used when we try to perform a
	 * REDIRECT after a POST.
	 *
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return The input forward.
	 */
	@RequestMapping(value ="/managePassword", params="action=refreshReset" , method =  RequestMethod.GET) 
	public String doRefreshReset (@Valid @ModelAttribute("managePasswordForm") ManagePasswordForm actionform, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
        	 UserInfo userInfo = populateUserInfo(actionform);
             request.setAttribute(Constants.USERINFO_KEY, userInfo);
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(INPUT);//if input forward not //available, provided default
        	}
        } 
	    
	    return refreshAndForwardTo(RESET_PAGE, actionform, request,false);
	    
	}
	
	@RequestMapping(value ="/manageInternalPassword/", params="action=refreshReset" , method =  RequestMethod.GET) 
	public String doRefreshResetInt (@Valid @ModelAttribute("managePasswordForm") ManagePasswordForm actionform, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
        		UserInfo userInfo = populateUserInfo(actionform);
        		request.setAttribute(Constants.USERINFO_KEY, userInfo);	
        		request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
        		return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(INPUT);//if input forward not //available, provided default
        	}
        } 
	    
	    return refreshAndForwardTo(RESET_PAGE_INT, actionform, request,true);
	    
	}

	/**
	 * Simply refresh the page. This action is used when we try to perform a
	 * REDIRECT after a POST.
	 *
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return The input forward.
	 */
	@RequestMapping(value ="/managePassword", params="action=refreshUnlock", method = RequestMethod.GET) 
	public String doRefreshUnlock (@Valid @ModelAttribute("managePasswordForm") ManagePasswordForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response)  {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
        		UserInfo userInfo = populateUserInfo(actionForm);
        		request.setAttribute(Constants.USERINFO_KEY, userInfo);	
        		request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
        		return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(INPUT);//if input forward not //available, provided default
        	}
        } 
	
        return refreshAndForwardTo(UNLOCK_PASSWORD_PAGE,  actionForm, request,false);
        
	}
	@RequestMapping(value ="/manageInternalPassword/", params="action=refreshUnlock", method =  RequestMethod.GET) 
	public String doRefreshUnlockInt (@Valid @ModelAttribute("managePasswordForm") ManagePasswordForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
        		UserInfo userInfo = populateUserInfo(actionForm);
        		request.setAttribute(Constants.USERINFO_KEY, userInfo);	
        		request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
        		return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(INPUT);//if input forward not //available, provided default
        	}
        } 
	
        return refreshAndForwardTo(UNLOCK_PASSWORD_PAGE_INT,  actionForm, request,true);
        
	}
	@RequestMapping(value ="/managePassword", params="action=refreshUnlockPasscode", method = RequestMethod.GET) 
	public String doRefreshUnlockPasscode (@Valid @ModelAttribute("managePasswordForm") ManagePasswordForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response)  {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
        		UserInfo userInfo = populateUserInfo(actionForm);
        		request.setAttribute(Constants.USERINFO_KEY, userInfo);	
        		request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
        		return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(INPUT);//if input forward not //available, provided default
        	}
        } 
        return refreshAndForwardTo(UNLOCKPASSCODEPAGE_ACTION, actionForm, request,false);
        
    }

	/**
	 * Invokes the reset task. Calls SecurityServiceDelegate to reset the
	 * password. Receives temp password as a return and stores it in the
	 * session.
	 */
	@RequestMapping(value ="/managePassword" , params="action=reset password", method =  RequestMethod.POST) 
	public String doReset (@Valid @ModelAttribute("managePasswordForm") ManagePasswordForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
        		UserInfo userInfo = populateUserInfo(form);
        		request.setAttribute(Constants.USERINFO_KEY, userInfo);	
        		request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(INPUT);//if input forward not //available, provided default
        	}
        } 

		if (logger.isDebugEnabled()) {
			logger.debug("entry <-- doReset");
		}

		UserProfile userProfile = getUserProfile(request);
		Principal principal = userProfile.getPrincipal();
		// call serviceDelegate to reset password
		String forward = forwards.get( RESET_ACTION);

		try {
			SecurityServiceDelegate service = SecurityServiceDelegate
					.getInstance();
			UserInfo userInfo = populateUserInfo(form);

			/**
			 * resetPassword method of SecurityService returns decrypted temp.
			 * password if the profile manager is CAR or SCAR this string will
			 * be the returns null otherwise.
             *
             * UPDATE: resetPassword no longer required by the .jsp
			 */
			service.resetPassword(principal, userInfo, Environment.getInstance().getSiteLocation());

			LockServiceDelegate.getInstance().releaseLock(
					LockHelper.USER_PROFILE_LOCK_NAME,
					LockHelper.USER_PROFILE_LOCK_NAME + userInfo.getProfileId());

		} catch (SecurityServiceException e) {
			List errors = new ArrayList();
			errors
					.add(new GenericException(Integer
							.parseInt(e.getErrorCode())));
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(REFRESH_ACTION);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <-- doReset");
		}

		return forward;
	}
	
	@RequestMapping(value ="/manageInternalPassword/" , params="action=reset password", method =  {RequestMethod.GET,RequestMethod.POST}) 
	public String doResetInternal (@Valid @ModelAttribute("managePasswordForm") ManagePasswordForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
        		UserInfo userInfo = populateUserInfo(form);
        		request.setAttribute(Constants.USERINFO_KEY, userInfo);	
        		request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(INPUT);//if input forward not //available, provided default
        	}
        } 

		if (logger.isDebugEnabled()) {
			logger.debug("entry <-- doReset");
		}

		UserProfile userProfile = getUserProfile(request);
		Principal principal = userProfile.getPrincipal();
		// call serviceDelegate to reset password
		String forward = forwards.get( RESET_ACTION_INT);

		try {
			SecurityServiceDelegate service = SecurityServiceDelegate
					.getInstance();
			UserInfo userInfo = populateUserInfo(form);

			/**
			 * resetPassword method of SecurityService returns decrypted temp.
			 * password if the profile manager is CAR or SCAR this string will
			 * be the returns null otherwise.
             *
             * UPDATE: resetPassword no longer required by the .jsp
			 */
			service.resetPassword(principal, userInfo, Environment.getInstance().getSiteLocation());

			LockServiceDelegate.getInstance().releaseLock(
					LockHelper.USER_PROFILE_LOCK_NAME,
					LockHelper.USER_PROFILE_LOCK_NAME + userInfo.getProfileId());

		} catch (SecurityServiceException e) {
			List errors = new ArrayList();
			errors
					.add(new GenericException(Integer
							.parseInt(e.getErrorCode())));
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(REFRESHINT_ACTION);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <-- doReset");
		}

		return forward;
	}

	/**
	 * Invokes the print task. It uses the common workflow with validateForm set
	 * to false.
	 */
	@RequestMapping(value ="/managePassword", params="action=unlock password", method =  RequestMethod.POST) 
	public String doUnlock (@Valid @ModelAttribute("managePasswordForm") ManagePasswordForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
        		UserInfo userInfo = populateUserInfo(form);
        		request.setAttribute(Constants.USERINFO_KEY, userInfo);	
        		request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(INPUT);//if input forward not //available, provided default
        	}
        } 

		if (logger.isDebugEnabled()) {
			logger.debug("entry <-- doUnlock");
		}

		UserProfile userProfile = getUserProfile(request);
		UserInfo userInfo = null;
		Principal principal = userProfile.getPrincipal();
		// call serviceDelegate to reset password
		try {
			userInfo = populateUserInfo(form);
			SecurityServiceDelegate service = SecurityServiceDelegate
					.getInstance();
			service.unlockPassword(principal, userInfo);

			LockServiceDelegate.getInstance().releaseLock(
					LockHelper.USER_PROFILE_LOCK_NAME,
					LockHelper.USER_PROFILE_LOCK_NAME + userInfo.getProfileId());

		} catch (SecurityServiceException e) {
			Collection<GenericException> errors = new ArrayList<GenericException>();
			errors
					.add(new GenericException(Integer
							.parseInt(e.getErrorCode())));
			SessionHelper.setErrorsInSession(request, duplicateSubmitErrorsCheck(request, errors));
			return forwards.get(REFRESH_ACTION);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <-- doUnlock");
		}

		return forwards.get( UNLOCK_PASSWORD_ACTION);
	}
	
	@RequestMapping(value ="/manageInternalPassword/", params="action=unlock password", method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doUnlockInt (@Valid @ModelAttribute("managePasswordForm") ManagePasswordForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
        		UserInfo userInfo = populateUserInfo(form);
        		request.setAttribute(Constants.USERINFO_KEY, userInfo);	
        		request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(INPUT);//if input forward not //available, provided default
        	}
        } 

		if (logger.isDebugEnabled()) {
			logger.debug("entry <-- doUnlock");
		}

		UserProfile userProfile = getUserProfile(request);
		UserInfo userInfo = null;
		Principal principal = userProfile.getPrincipal();
		// call serviceDelegate to reset password
		try {
			userInfo = populateUserInfo(form);
			SecurityServiceDelegate service = SecurityServiceDelegate
					.getInstance();
			service.unlockPassword(principal, userInfo);

			LockServiceDelegate.getInstance().releaseLock(
					LockHelper.USER_PROFILE_LOCK_NAME,
					LockHelper.USER_PROFILE_LOCK_NAME + userInfo.getProfileId());

		} catch (SecurityServiceException e) {
			Collection<GenericException> errors = new ArrayList<GenericException>();
			errors
					.add(new GenericException(Integer
							.parseInt(e.getErrorCode())));
			SessionHelper.setErrorsInSession(request, duplicateSubmitErrorsCheck(request, errors));
			return forwards.get(REFRESHINT_ACTION);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <-- doUnlock");
		}

		return forwards.get( UNLOCK_PASSWORD_ACTION_INT);
	}
	
	 @RequestMapping(value ="/managePassword", params="action=unlock security code" ,method =  RequestMethod.POST) 
		public String doUnlockPasscode (@Valid @ModelAttribute("managePasswordForm") ManagePasswordForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws SystemException {	
		 if(bindingResult.hasErrors()){
	        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	        	if(errDirect!=null){
	        		UserInfo userInfo = populateUserInfo(form);
	        		request.setAttribute(Constants.USERINFO_KEY, userInfo);	
	        		request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(INPUT);//if input forward not //available, provided default
	        	}
	        } 
	    
	    final UserInfo userInfo = populateUserInfo((ManagePasswordForm) form);
	    
	    try {
	        
	        SecurityServiceDelegate.getInstance().unlockPasscode(
	        		userInfo.getUserName(),
	                getUserProfile(request).getPrincipal(),
	                new RequestDetails(request,null));
	        
            LockServiceDelegate.getInstance().releaseLock(
                    LockHelper.USER_PROFILE_LOCK_NAME,
                    LockHelper.USER_PROFILE_LOCK_NAME + userInfo.getProfileId());

	    } catch (final SecurityServiceException sse) {
	        
	           Collection<GenericException> errors = new ArrayList<GenericException>();
	            errors
	                    .add(new GenericException(Integer
	                            .parseInt(sse.getErrorCode())));
	            SessionHelper.setErrorsInSession(request, duplicateSubmitErrorsCheck(request, errors));
	            return forwards.get(REFRESH_ACTION);

	    }
	    
	    return forwards.get( UNLOCKPASSCODE_ACTION);
	    
	}
	 
	 @RequestMapping(value ="/manageInternalPassword/", params="action=unlock security code" ,method =  {RequestMethod.GET,RequestMethod.POST}) 
		public String doUnlockPasscodeInt (@Valid @ModelAttribute("managePasswordForm") ManagePasswordForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws SystemException {	
		 if(bindingResult.hasErrors()){
	        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	        	if(errDirect!=null){
	        		UserInfo userInfo = populateUserInfo(form);
	        		request.setAttribute(Constants.USERINFO_KEY, userInfo);	
	        		request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(INPUT);//if input forward not //available, provided default
	        	}
	        } 
	    
	    final UserInfo userInfo = populateUserInfo((ManagePasswordForm) form);
	    
	    try {
	        
	        SecurityServiceDelegate.getInstance().unlockPasscode(
	        		userInfo.getUserName(),
	                getUserProfile(request).getPrincipal(),
	                new RequestDetails(request,null));
	        
         LockServiceDelegate.getInstance().releaseLock(
                 LockHelper.USER_PROFILE_LOCK_NAME,
                 LockHelper.USER_PROFILE_LOCK_NAME + userInfo.getProfileId());

	    } catch (final SecurityServiceException sse) {
	        
	           Collection<GenericException> errors = new ArrayList<GenericException>();
	            errors
	                    .add(new GenericException(Integer
	                            .parseInt(sse.getErrorCode())));
	            SessionHelper.setErrorsInSession(request, duplicateSubmitErrorsCheck(request, errors));
	            return forwards.get(REFRESHINT_ACTION);

	    }
	    
	    return forwards.get( UNLOCKPASSINT_ACTION);
	    
	}

	/**
	 * Invokes the showPassword task. Removes tempPassword from session and adds
	 * it to request
	 */
	
	 @RequestMapping(value ={"/managePassword","/manageInternalPassword/"},params="action=viewPassword", method =  {RequestMethod.POST,RequestMethod.GET}) 
		public String doViewPassword (@Valid @ModelAttribute("managePasswordForm") ManagePasswordForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws SystemException {
		 if(bindingResult.hasErrors()){
	        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	        	if(errDirect!=null){
	        		UserInfo userInfo = populateUserInfo(form);
	        		request.setAttribute(Constants.USERINFO_KEY, userInfo);	
	        		request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(INPUT);//if input forward not //available, provided default
	        	}
	        } 

		if (logger.isDebugEnabled()) {
			logger.debug("entry <-- doViewPassword");
		}
		//ManagePasswordForm form = (ManagePasswordForm) actionForm;
		// to do - 3.6.5.4 MPR 149. The system would log date, time userid
		// that viewed password, user id of profile who's password was viewed
		try {

			// 3.6.5.4 MPR 149. The system would log date, time userid
			// that viewed password, user id of profile who's password was
			// viewed

			String eventLogType = EventLogFactory.VIEW_TEMPORARY_PASSWORD_EVENT_LOG;
			PsEventLog eventLog = (PsEventLog) EventLogFactory.getInstance().createEventLog(
					eventLogType);
			eventLog.setPrincipal(getUserProfile(request).getPrincipal());
			eventLog.setClassName(this.getClass().getName());
			eventLog.setMethodName("doViewPassword");
			eventLog.setUserName(form.getUserName());
			eventLog.log();
		} catch (LogEventException e) {
			SystemException se = new SystemException(
					e,
					this.getClass().getName(),
					"addUser",
					"Problem occurred during logging while viewing temporary password. of the user: "
							+ form.getUserName()
							+ " by the profile manager: "
							+ getUserProfile(request).getPrincipal().toString());
			throw ExceptionHandlerUtility.wrap(se);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <-- doViewPassword");
		}

		return forwards.get(RESET_VIEW_PASSWORD_ACTION);
	}

	/**
	 * Invokes the Finish task. Removes tempPassword from session
	 *
	 * To do - have to forward back to the page I initially came from
	 */
	 @RequestMapping(value ={"/managePassword","/manageInternalPassword/"},params="action=finish" , method =  RequestMethod.POST) 
		public String doFinish (@Valid @ModelAttribute("managePasswordForm") ManagePasswordForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response)  {
		 if(bindingResult.hasErrors()){
	        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	        	if(errDirect!=null){
	        		UserInfo userInfo = populateUserInfo(form);
	        		request.setAttribute(Constants.USERINFO_KEY, userInfo);	
	        		request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(INPUT);//if input forward not //available, provided default
	        	}
	        } 
	

		if (logger.isDebugEnabled()) {
			logger.debug("entry <-- doFinish");
		}

		request.getSession(false).removeAttribute("managePasswordForm");

		if (logger.isDebugEnabled()) {
			logger.debug("exit <-- doFinish");
		}
		return forwards.get(SessionHelper.getLastVisitedManageUsersPage(request));
		 
	}

	protected void populateForm(ManagePasswordForm form, UserInfo userInfo) {
		form.setUserName(userInfo.getUserName());
		form.setFirstName(userInfo.getFirstName());
		form.setLastName(userInfo.getLastName());
		form.setEmail(userInfo.getEmail());
		form.setUserProfileId(userInfo.getProfileId());
	}

	protected UserInfo populateUserInfo(ManagePasswordForm form) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserName(form.getUserName());
		userInfo.setFirstName(form.getFirstName());
		userInfo.setLastName(form.getLastName());
		userInfo.setEmail(form.getEmail());
		userInfo.setProfileId(form.getUserProfileId());
		return userInfo;
	}
    
    private Collection<GenericException> duplicateSubmitErrorsCheck(HttpServletRequest request, Collection<GenericException> errors){
//      check for errors already in session scope
        Collection<GenericException> filterErrors = new ArrayList<GenericException>();
        Collection<GenericException> existingErrors = (Collection<GenericException>) request.getSession(false).getAttribute(PsBaseUtil.ERROR_KEY);
        if (existingErrors != null) {
            for (GenericException oe : existingErrors){
                for (GenericException ne : errors){
                    if (oe.getErrorCode()!=ne.getErrorCode()){
                        filterErrors.add(ne);
                    }
                }
            }
          return filterErrors;  
        } else {
            return errors;
        }
    }
    
    @Override
	protected boolean isTokenValidatorEnabled(String action) {
    	// avoids methods from validation which ever is not required
    	if (StringUtils.isNotEmpty(action)
				&& (StringUtils.contains(action, "Default")||StringUtils.contains(action, "default")||
						StringUtils.contains(action, REFRESH_ACTION))){
					return false;
				}
		return true;
	}

    /**This code has been changed and added  to 
   	 * Validate form and request against penetration attack, prior to other validations.
   	 */
	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}
    private static UserInfo getUserInfoInstance(final String userName, final Principal initiator)
    throws SecurityServiceException, SystemException {
        
        UserInfo userInfo = null;
        
        if (StringUtils.isNotBlank(userName)) {
            userInfo = SecurityServiceDelegate.getInstance().searchByUserName(initiator, userName);
        }
        
        if (userInfo != null) {
            userInfo.setPasscodeInfo(SecurityServiceDelegate.getInstance().getUserPasscodeInfo(userInfo.getProfileId()));
        }
        
        return userInfo;
        
    }
    
    private static String handleUnexpectedPageState(final ManagePasswordForm form,boolean interFlag) {
        if (form.getUserName() == null) {
            if(form.isFromTPAContactsTab()){
               return forwards.get(FORWARD_TPA_CONTACTS);
            } else if (form.isFromPSContactTab()){
            	if(interFlag)
            		return forwards.get(PSCONTACTSINT_ACTION);
            	else return forwards.get(Constants.FORWARD_PLANSPONSOR_CONTACTS);
            } else {
               return forwards.get(MANAGE_USERS_ACTION);
            }
       }
       return null;
    }
	public String refreshAndForwardTo (String forwardName,ManagePasswordForm form, HttpServletRequest request,boolean interFlag) {
		final String forward = handleUnexpectedPageState(form,interFlag);
        if (forward != null) {
            return forward;
        }
        
        final UserInfo userInfo = populateUserInfo(form);
        request.setAttribute(Constants.USERINFO_KEY, userInfo);

        return forwards.get(forwardName);
        
    }
    
}