package com.manulife.pension.ps.web.profiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

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
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.passcode.MobileMask;
import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.LockServiceDelegate;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.utility.SecurityConstants;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.content.GenericException;


@Controller
@RequestMapping( value ="/profiles")
@SessionAttributes({"userProfileForm"})

public class UnsuspendProfileController extends PsAutoController
{
	
	@ModelAttribute("userProfileForm") 
	public UserProfileForm populateForm() 
	{
		return new UserProfileForm();
		}
	
	public static HashMap<String,String> forwards=new HashMap<String,String>();
	static{
		forwards.put("input","/profiles/unsuspendProfile.jsp");
		forwards.put("unsuspendPage","redirect:/do/profiles/unsuspendProfile/?action=confirm");
		forwards.put("viewPermissions","redirect:/do/profiles/userPermissions/?fromPage=unsuspend");
		forwards.put("refresh","redirect:/do/profiles/unsuspendProfile/?action=refresh");
		forwards.put("confirmation","/profiles/unsuspendProfileConfirm.jsp");
		forwards.put("planSponsorContacts","redirect:/do/contacts/planSponsor/"); 
		forwards.put("manageTPAUsers","redirect:/do/profiles/manageTpaUsers/");
		forwards.put("manageInternalUsers","redirect:/do/profiles/manageInternalUsers/");
		}


	private static final String UNSUSPEND_PROFILE = "unsuspendPage";
    private static final String VIEW_PERMISSIONS = "viewPermissions";
	private static final String CONFIRMATION_PAGE = "confirmation";
	private static final String MANAGE_INTERNAL_USERS = "manageInternalUsers";
	private static final String MANAGE_TPA_USERS = "manageTPAUsers";
	private static final String REFRESH = "refresh";

	private static SecurityServiceDelegate service = SecurityServiceDelegate.getInstance();


	/**
	 * Constructor for UnsuspendProfileAction
	 */
	public UnsuspendProfileController()
	{
		super();
	}


	/**
	 * Constructor for UnsuspendProfileAction
	 */
	public UnsuspendProfileController(Class clazz)
	{
		super(clazz);
	}


	/**
	 * @see PsAutoController#doDefault(ActionMapping, AutoForm,
	 *      HttpServletRequest, HttpServletResponse)
	 */
	@RequestMapping(value ="/unsuspendProfile/", method =  RequestMethod.GET) 
	public String doDefault(@Valid @ModelAttribute("userProfileForm") UserProfileForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
		String forward = forwards.get("input");

        UserInfo userInfo = null;

        
		if (form.getAction().equals(REFRESH))
		{
			return forwards.get(REFRESH);			
		}
				
		UserProfile loggedInUserProfile = getUserProfile(request);
		Principal loggedInPrincipal = loggedInUserProfile.getPrincipal();
	
		String userName = form.getUserName();
		form.clear( request);
        

		try
		{
			if (loggedInUserProfile.getCurrentContract() != null && 
					loggedInUserProfile.getCurrentContract().isBusinessConverted()) {
                // If the contract is business converted pass in null for the Principal
                // This will bypass some contract security checks in the stored proc
                userInfo = SecurityServiceDelegate.getInstance().searchByUserName(
                        AbstractAddEditUserController.newPrincipal(new Long(0), null, null),
                        userName);
            } else {
                userInfo = SecurityServiceDelegate.getInstance().searchByUserName(
                        loggedInPrincipal, userName);
            }

			if (userInfo == null)
			{
				setError(request, ErrorCodes.USER_DOES_NOT_EXIST);
				
				forward = forwards.get(Constants.FORWARD_PLANSPONSOR_CONTACTS);
			}
			else if (!userInfo.isWebAccessInd())
			{
				setError(request, ErrorCodes.NO_WEB_ACCESS);
				
				forward = forwards.get(Constants.FORWARD_PLANSPONSOR_CONTACTS);
			}
			else if (SecurityConstants.DELETED_PROFILE_STATUS.equalsIgnoreCase(userInfo.getProfileStatus()))
			{
				setError(request, ErrorCodes.USER_ALREADY_DELETED);
				
				forward = forwards.get(Constants.FORWARD_PLANSPONSOR_CONTACTS);
			}
			else
			{
                Collection<GenericException> errors = ClientUserContractAccessActionHelper.checkForLockOrDelete(loggedInUserProfile, userInfo.getProfileId(), userName);
                if (!errors.isEmpty()) {
                    SessionHelper.setErrorsInSession(request, errors);
                    
                    return forwards.get(Constants.FORWARD_PLANSPONSOR_CONTACTS);
                }

				int contractNumber = 0;
				
				if (loggedInUserProfile.getCurrentContract()!= null)
				{
					contractNumber=loggedInUserProfile.getCurrentContract().getContractNumber();
				}

				populateForm(request, form, userInfo, contractNumber);
			}
		}
		catch(SecurityServiceException e)
		{
			SystemException se = new SystemException(e, "com.manulife.pension.ps.web.profiles.UnsuspendProfileAction", "doDefault", e.toString());
			throw se;
		}

		return forward;
	}


	protected void populateForm(
			HttpServletRequest request,
			UserProfileForm form,
			UserInfo userInfo,
			int contractNumber)
	throws SystemException
	{
		UserInfo loginUserInfo = SecurityServiceDelegate.getInstance().getUserInfo(
				getUserProfile(request).getPrincipal());
	    UserProfile loginUserProfile = getUserProfile(request);

	    form.setProfileId(userInfo.getProfileId());
		form.setUserName(userInfo.getUserName());
		form.setFirstName(userInfo.getFirstName());
		form.setLastName(userInfo.getLastName());
		form.setEmail(userInfo.getEmail());
		form.setSecondaryEmail(userInfo.getSecondaryEmail());
		form.setMobileNumber(MobileMask.maskPhone(userInfo.getMobileNumber().toString()));
		form.setTelephoneNumber(userInfo.getPhoneNumber());
		form.setTelephoneExtension(userInfo.getPhoneExtension());
		form.setFaxNumber(userInfo.getFax());
		form.setCommentDetails(userInfo.getContactComment());
		form.setPasswordState(userInfo.getPasswordState());
		form.setProfileStatus(userInfo.getProfileStatus());
		form.setWebAccess(userInfo.isWebAccessInd() ? UserProfileForm.FORM_YES_CONSTANT : UserProfileForm.FORM_NO_CONSTANT);
		form.setUserRole(userInfo.getRole());

        String ssn = userInfo.getSsn();

	    if (ssn != null && ssn.length() > 0)
	    {
	    	form.setSsn(new Ssn(ssn));
	    }
	        
	    form.setContractAccesses(ClientUserContractAccessActionHelper.buildContractAccesses(loginUserProfile, loginUserInfo, userInfo));
        
	    if (form.getContractAccesses().size() == userInfo.getContractPermissions().length && ClientUserContractAccessActionHelper.canManageAllContracts(loginUserInfo, userInfo)) {
            form.setCanManageAllContracts(true);
        } else {
            form.setCanManageAllContracts(false);
	    }
	}

	@RequestMapping(value ="/unsuspendProfile/", params="action=unsuspend", method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doUnsuspend(@Valid @ModelAttribute("userProfileForm") UserProfileForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
		String forward = null;
		UserProfile userProfile = getUserProfile(request);
		Principal principal = userProfile.getPrincipal();
		Collection errors = new ArrayList();
		
		

		if (logger.isDebugEnabled())
		{
			logger.debug("entry <-- doUnsuspend");
		}

		UserInfo userInfo = new UserInfo();
		userInfo.setUserName(form.getUserName());
		userInfo.setRole(form.getUserRole());
		userInfo.setEmail(form.getEmail());
		
		for (Iterator it = form.getContractAccesses().iterator(); it.hasNext();) 
		{
			ClientUserContractAccess access = (ClientUserContractAccess) it.next();
			ContractPermission permission = new ContractPermission(null);
			permission.setCompanyName(access.getContractName());
            ClientUserContractAccessActionHelper.populateContractPermission(permission, access);
            userInfo.addContractPermission(permission);
		}
		
		try
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("unsuspendProfile");
			}

			request.setAttribute("userProfileForm", form);
			service.unsuspendUser(principal, userInfo, Environment.getInstance().getSiteLocation());
            
			LockServiceDelegate.getInstance().releaseLock(
                	LockHelper.USER_PROFILE_LOCK_NAME, 
                	LockHelper.USER_PROFILE_LOCK_NAME + form.getProfileId());
            
			forward = forwards.get(UNSUSPEND_PROFILE);
		}
		catch (SecurityServiceException e)
		{
			errors.add(new GenericException(Integer.parseInt(e.getErrorCode())));
			SessionHelper.setErrorsInSession(request, errors);
			forward = forwards.get("input");
			
			if (logger.isDebugEnabled())
			{
				logger.debug("security exception" + e);
			}
		}

		if (logger.isDebugEnabled())
		{
			logger.debug("exit <-- doUnsuspend");
		}

		return forward;
	}

	@RequestMapping(value ="/unsuspendProfile/", params="action=viewPermissions", method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doViewPermissions(@Valid @ModelAttribute("userProfileForm") UserProfileForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		String formErrors=validate(actionForm, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doViewPermissions");
        }

        String forward = forwards.get(VIEW_PERMISSIONS);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doViewPermissions");
        }
        return forward;
    }
    
	public String doCancel (@Valid @ModelAttribute("userProfileForm") UserProfileForm form, HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		LockServiceDelegate.getInstance().releaseLock(
			LockHelper.USER_PROFILE_LOCK_NAME, 
			LockHelper.USER_PROFILE_LOCK_NAME + form.getProfileId());
        request.getSession(false).removeAttribute("userProfileForm");
        request.getSession(false).removeAttribute("userPermissionsForm");
        return forwards.get(Constants.FORWARD_PLANSPONSOR_CONTACTS);
	}

	@RequestMapping(value ="/unsuspendProfile/", params={"action=back"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doBack (@Valid @ModelAttribute("userProfileForm") UserProfileForm form,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
        String forward=doCancel(form, request,response);
        return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }
	@RequestMapping(value ="/unsuspendProfile/", params="action=confirm", method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doConfirm (@Valid @ModelAttribute("userProfileForm") UserProfileForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
		return forwards.get(CONFIRMATION_PAGE);
	}

	@RequestMapping(value ="/unsuspendProfile/", params="action=refresh", method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doRefresh (@Valid @ModelAttribute("userProfileForm") UserProfileForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
	
	@RequestMapping(value ="/unsuspendProfile/", params="action=finish", method =  RequestMethod.GET) 
	public String doFinish (@Valid @ModelAttribute("userProfileForm") UserProfileForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
		String forward = null;
		UserRole role = form.getUserRole();
		form.clear(request);

		if (role != null)
		{
			if (role.isInternalUser())
			{
				forward = forwards.get(MANAGE_INTERNAL_USERS);
			}
			else
			{
				if (role.isTPA())
				{
					forward = forwards.get(MANAGE_TPA_USERS);
				}
				else
				{
									
					forward = forwards.get(Constants.FORWARD_PLANSPONSOR_CONTACTS);
				}
			}
		}
		else
		{
			
			forward = forwards.get(Constants.FORWARD_PLANSPONSOR_CONTACTS);
		}
		
        request.getSession(false).removeAttribute("userProfileForm");
        request.getSession(false).removeAttribute("userPermissionsForm");

		return forward;
	}
	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
	 
	
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
	
    protected String validate( ActionForm actionForm,
            HttpServletRequest request) {

    	UserProfileForm form = (UserProfileForm) actionForm;

    	/*This code has been changed and added  to 
	   	 * Validate form and request against penetration attack, prior to other validations as part of the .
	   	 */
    	
        
        if ("unsuspend".equals(form.getAction())) {
            Collection errors = doValidate( form, request);

            /*
             * Errors are stored in the session so that our REDIRECT can look up the errors.
             */
            if (!errors.isEmpty()) {
                SessionHelper.setErrorsInSession(request, errors);
                return forwards.get(REFRESH);
            }

        }

        return null;
    }

    protected Collection doValidate( UserProfileForm form, HttpServletRequest request) {

        Collection<GenericException> errors =  new ArrayList();

        if (getUserProfile(request).getRole().isExternalUser() && !form.isCanManageAllContracts()) {
            errors.add(new GenericException(1082));
        }

        return errors;
    }

    private void setError(HttpServletRequest request, int errorCode)
    {
		Collection errors = new ArrayList();
		errors.add(new GenericException(errorCode));
		SessionHelper.setErrorsInSession(request, errors);
    }
    
}

