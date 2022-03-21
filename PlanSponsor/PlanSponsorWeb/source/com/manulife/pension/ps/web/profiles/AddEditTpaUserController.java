package com.manulife.pension.ps.web.profiles;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.util.ExtensionRule;
import com.manulife.pension.platform.web.util.FaxRule;
import com.manulife.pension.platform.web.util.PhoneRule;
import com.manulife.pension.platform.web.util.SsnRule;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.ps.web.validation.rules.TpaAcessLevelRule;
import com.manulife.pension.ps.web.validation.rules.TpaFirmRule;
import com.manulife.pension.service.security.exception.DuplicateSSNException;
import com.manulife.pension.service.security.exception.InvalidTPAFirmIDException;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.role.UserRoleFactory;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.Pair;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;

/**
 * @author Charles Chan
 */

@Controller
@RequestMapping(value ="/profiles")
@SessionAttributes({"addEditUserForm"})

public class AddEditTpaUserController extends AbstractTpaUserController {
	@ModelAttribute("addEditUserForm")
	public AddEditUserForm populateForm()
	{
		return new AddEditUserForm();
		}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
	forwards.put("input","/profiles/addTpaUser.jsp");
	forwards.put("confirm","redirect:/do/profiles/addTpaUser/?action=confirm");
	forwards.put("refresh","redirect:/do/profiles/addTpaUser/?action=refresh");
	forwards.put("confirmPage","/profiles/addTpaUserConfirmation.jsp");
	forwards.put("manageUsers","redirect:/do/profiles/manageTpaUsers/");
	forwards.put("new","redirect:/do/profiles/addTpaUser/");
	forwards.put("changePermissions","redirect:/do/profiles/tpaFirmUserPermissions/?action=edit&source=add");
	forwards.put("tpaContactsTab","redirect:/do/contacts/thirdPartyAdministrator/");
	}

	private static final String TPAUM = "tpaum";								//CL 110473
	
	/**
	 * Constructor.
	 */
	public AddEditTpaUserController() {
		super(AddEditTpaUserController.class);
		
		securityExceptionFormFieldMap.put(DuplicateSSNException.class,AddEditUserForm.FIELD_SSN);
		securityExceptionFormFieldMap.put(InvalidTPAFirmIDException.class,AddEditUserForm.FIELD_TPA_FIRMS);
	}
	
	@RequestMapping(value = "/addTpaUser/",method =  {RequestMethod.GET})
	public String doDefault(@Valid @ModelAttribute("addEditUserForm")AddEditUserForm form,BindingResult bindingResult,ModelMap model,HttpServletRequest request,
		 			             HttpServletResponse response)throws ServletException, IOException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		Collection formErrors=doValidate(form, request);
		
		// To retain TPA Contacts tab flag.
		if(!formErrors.isEmpty()){
			//SessionHelper.setErrorsInSession(request, formErrors);
			if(form.isFromTPAContactsTab()){
			
			ControllerForward result = new ControllerForward(forwards.get("refresh")+"&fromTPAContactsTab=true", true);
			return result.getPath();
			}
			else
			return forwards.get("refresh");
		}
		model.addAttribute("mapping", "add");
		String forward= super.doDefault(form,model,request,response);
		request.setAttribute(Constants.SHOW_TPA_PERMISSIONS_ONLY, "true");
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	
	}
	
	/**
	 * Sets the request parameters after each and every task.
	 */
	/*protected void postExecute( Form form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SystemException {
		super.postExecute( form, request, response);
		
		request.setAttribute(Constants.SHOW_TPA_PERMISSIONS_ONLY, "true");
	}*/
	public String validate(AddEditUserForm form, HttpServletRequest request){
		
		return super.validate(form, request);
	}

	
	protected void populateUserInfo(HttpServletRequest request,
			UserInfo userInfo, AddEditUserForm form) {

		userInfo.setWebAccessInd(form.isWebAccess());
		userInfo.setSsn(form.getSsn().toString());
		userInfo.setPhoneNumber(form.getPhone());
		userInfo.setPhoneExtension(form.getExt());
		userInfo.setFax(form.getFax()); 
		
		userInfo.setRole(new ThirdPartyAdministrator());
		userInfo.setHiddenFirmExist(form.isHiddenFirmExist().booleanValue());
		if(form.getProfileStatus() == null && !form.isWebAccess()){
			// For a New profile : When web access = No, permission's should be set to default
			for(TpaFirm tpaFirm : form.getTpaFirms()){
	        	// Populate default permissions
	        	TPAUserContractAccessActionHelper.populatePermissionsFromDefaults(tpaFirm.getContractAccess(0));
	        	TPAActionHelper.copyAttributesToForm(tpaFirm.getContractAccess(0).getUserPermissions(), tpaFirm.getId(), form);
	        }
		}

		for (Iterator it = form.getTpaFirms().iterator(); it.hasNext();) {
			TpaFirm tpaFirmForm = (TpaFirm) it.next();

			TPAFirmInfo tpaFirm = new TPAFirmInfo();
			tpaFirm.setId(tpaFirmForm.getId().intValue());
			tpaFirm.setName(tpaFirmForm.getName());

			if (tpaFirmForm.isRemoved()) {
				// need to send ContractPermissions with UserRole = null in this case.
				ContractPermission permission = new ContractPermission(null); // role = NoAccess
				tpaFirm.setContractPermission(permission);
			} else {				
				Iterator ait = tpaFirmForm.getContractAccesses().iterator();
				if (ait.hasNext()) {
					ContractPermission permission = new ContractPermission(null);
					TPAUserContractAccess access = (TPAUserContractAccess) ait.next();

					// want the confirmation page to have the correct values, so...
                    TPAActionHelper.syncSettingsToUserPermissions(form, tpaFirmForm.getId().intValue(), true);

					// Te following piece added to set the Firm role
					// on the contractpermission (TPA or TPAUM)
					if (!access.getPlanSponsorSiteRole().equals(
							AccessLevelHelper.NO_ACCESS)) {
						permission.setRole(UserRoleFactory.getUserRole(access
								.getPlanSponsorSiteRole()));

					}
					TPAUserContractAccessActionHelper.populateContractPermission(permission, access);
					tpaFirm.setContractPermission(permission);
				}
				
				form.getEmailPreferences().populatePermissions(tpaFirm.getContractPermission());
			}
			
			userInfo.addTpaFirm(tpaFirm);
						
		} // end For all tpa firms		
		
		// To update special attributes for current contract in single TPA firm view
		if(form.isFromTPAContactsTab()){
			 int currentContract = getUserProfile(request).getCurrentContract().getContractNumber();
			 ContractPermission permsission = new ContractPermission(new ThirdPartyAdministrator());
			 permsission.setContractNumber(currentContract);
			 permsission.setPrimaryContact(form.isPrimaryContact());
			 permsission.setSignatureReceivedAuthSigner(form.isSignatureReceivedAuthSigner());
			 userInfo.addContractPermission(permsission);
		}
	}


	/**
	 * Returns the TPA firm name of the given TPA firm ID.
	 * 
	 * @param tpaFirmId
	 *            The TPA firm ID
	 * @return The TPA firm name of the TPA firm ID.
	 * @throws SystemException
	 */
	private TpaFirm getTpaFirm(Integer tpaFirmId) throws SystemException {
		TPAFirmInfo info = TPAServiceDelegate.getInstance().getFirmInfo(
				tpaFirmId.intValue());
		if (info == null) {
			return null;
		}
		
		/*
		 * We don't care about the registered user count of a newly added TPA
		 * firm.
		 */
		TpaFirm firm = new TpaFirm(tpaFirmId, info.getName(), new Integer(9999));
        firm.setNewFirm(true);
		setTpaFirmCommon(firm, info);
		return firm;
	}

	

	/**
	 * @see com.manulife.pension.ps.web.controller.PsController#doValidate(
	 *      javax.servlet.http.HttpServletRequest)
	 */
	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;

	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
	 
	  protected String doWarningsValidation(
			AddEditUserForm form, HttpServletRequest request) {
		
        // Doing warnings in javascript for now
		
        /*
        if (!form.isSaveAction()) return null; // load of page
	
		StringBuffer warningMessages = new StringBuffer();
		
		// TTP.5
		Iterator it = form.getTpaFirms().iterator();
		while(it.hasNext()) {
			TpaFirm tpaFirmForm = (TpaFirm) it.next();

			// if count = 1 and if this has been turned off, and prior to save I had it, then 
			// generate the warning.
			if ((tpaFirmForm.getILoanEmialPermissionCount() == 1) &&
				 (form.getEmailPreferences().getReceiveiLoads() == false)) {
			    AddEditUserForm originalForm = (AddEditUserForm)form.getClonedForm();
			    if (originalForm.getEmailPreferences().getReceiveiLoads()) {
			    	if (warningMessages.length() >0) warningMessages.append("\\n");
			    	warningMessages.append("Firm(s): " + tpaFirmForm.getId() + 
			    			               " Warning!  This is the last user at the listed firm(s) with Receive i:loans Email permission."); // 53972
			    }
			}
		}
	    	
		if (warningMessages.length()>0) {
			request.setAttribute("WARNINGS", warningMessages.toString());
			
			return mapping.getInputForward();
		}
		*/
		return null; // no warnings generated
	}		
	
	
	/**
	 * Called when user clicks on add TPA firm.
	 */
	@RequestMapping(value ="/addTpaUser/", params={"action=addTpaFirm"} , method =  {RequestMethod.POST}) 
	public String doAddTpaFirm(@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form,ModelMap model, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default

		}
		if (logger.isDebugEnabled()) {
			logger.debug("entry --> doAddTpaFirm");
		}
		Collection formErrors=doValidate(form, request);
		
		// To retain TPA Contacts tab flag.
		if(!formErrors.isEmpty()){
			//SessionHelper.setErrorsInSession(request, formErrors);
			if(form.isFromTPAContactsTab()){
			
			ControllerForward result = new ControllerForward(forwards.get("refresh")+"&fromTPAContactsTab=true", true);
			return result.getPath();
			}
			else
			return forwards.get("refresh");
		}

		boolean invalidTpaFirmId = true;
		boolean foundExisting = false;

		/*
		 * Try to find an existing TPA firm in the list.
		 */
		for (Iterator it = form.getTpaFirms().iterator(); it.hasNext();) {
			TpaFirm tpaFirmForm = (TpaFirm) it.next();
			if (tpaFirmForm.getId().toString().equals(form.getTpaFirmId())) {
				tpaFirmForm.setRemoved(false);
				foundExisting = true;
				break;
			}
		}

		if (!foundExisting) {
			Integer tpaFirmId = Integer.valueOf(form.getTpaFirmId());

			TpaFirm tpaFirm = getTpaFirm(tpaFirmId);

			if (tpaFirm != null) {
				TPAUserContractAccessActionHelper.populatePermissionDefaults(tpaFirm.getContractAccess(0), null);
				TPAUserContractAccessActionHelper.populatePermissionsFromDefaults(tpaFirm.getContractAccess(0));

				UserProfile userProfile = getUserProfile(request);

				UserInfo loginUserInfo = SecurityServiceDelegate.getInstance()
						.getUserInfo(userProfile.getPrincipal());
				TPAUserContractAccessActionHelper.filterFirmContractAccess(
						loginUserInfo, tpaFirm);

				// Populate warning flags
				TPAUserContractAccessActionHelper.setLastPermissionFlags(tpaFirm, form.getProfileId());

				form.getTpaFirms().add(0, tpaFirm);

				// Copy the permission default values to the form
				TPAActionHelper.copyAttributesToForm(tpaFirm.getContractAccess(0).getUserPermissions(), tpaFirmId, form);

				/*
				 * Add a dummy TPA firm to the cloned form to maintain the
				 * order.
				 */
				((AddEditUserForm) form.getClonedForm()).getTpaFirms()
				.add(0, new TpaFirm());

				invalidTpaFirmId = false;
			}

			if (invalidTpaFirmId) {
				List errors = new ArrayList();
				errors.add(new ValidationError(
						AddEditUserForm.FIELD_TPA_FIRM_ID_TO_ADD,
						ErrorCodes.TPA_FIRM_ID_INVALID));
				SessionHelper.setErrorsInSession(request, errors);
			}
		}

		evaluateEmailDisplayRules(form, request);
		form.setGenerateChangeTrackingMessage(true); // signal to gen warning on cancel

		/*
		 * Reset the tpa firm id.
		 */
		form.setTpaFirmId(null);

		request.getSession(false).setAttribute(FLOW_TOKEN, "addEdit");

		if (logger.isDebugEnabled()) {
			logger.debug("exit <-- doAddTpaFirm");
		}

		return forwards.get(REFRESH);
	}

	
	@RequestMapping(value ="/addTpaUser/" ,params={"action=removeTpaFirm"}, method =  {RequestMethod.POST}) 
	public String doRemoveTpaFirm (@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		Collection formErrors=doValidate(form, request);
		
		// To retain TPA Contacts tab flag.
		if(!formErrors.isEmpty()){
			//SessionHelper.setErrorsInSession(request, formErrors);
			if(form.isFromTPAContactsTab()){
			
			ControllerForward result = new ControllerForward(forwards.get("refresh")+"&fromTPAContactsTab=true", true);
			return result.getPath();
			}
			else
			return forwards.get("refresh");
		}

  AddEditUserForm clonedForm = (AddEditUserForm) form.getClonedForm();
        Collection<GenericException> errors = new ArrayList<GenericException>();

        /*
		 * Go through the entire TPA firm list and remove the TPA firm from the
		 * list.
		 */
		int index = 0;
        for (Iterator it = form.getTpaFirms().iterator(); it.hasNext(); index++) {
            TpaFirm tpaFirmForm = (TpaFirm) it.next();
            if (tpaFirmForm.getId().toString().equals(form.getRemoveTpaFirmId())) {
                if (tpaFirmForm.isPersisted()) {
                    if (errors.isEmpty()) {
                        tpaFirmForm.setRemoved(true);
                        form.setGenerateChangeTrackingMessage(true); // signal to gen warning on cancel
                    }
                } else {
                    it.remove();
                    ((AddEditUserForm) form.getClonedForm()).getTpaFirms().remove(index);
                }
                evaluateEmailDisplayRules(form, request);
                break;
            }
        }

        if (!errors.isEmpty()) {
            SessionHelper.setErrorsInSession(request, errors);
        }

        /*
         * Reset the tpa firm id.
         */
        form.setRemoveTpaFirmId(null);
        request.getSession(false).setAttribute(FLOW_TOKEN, "addEdit");
        return forwards.get(REFRESH);
	}
	
	
	/**
	 * Naviate over to the sub-page (TPA User permissions)
	 * 
	 */
	@RequestMapping(value ="/addTpaUser/", params={"actionLabel=change Permissions"}, method =  {RequestMethod.POST}) 
	public String doChangePermissions (@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}
		Collection formErrors=doValidate(form, request);

		// To retain TPA Contacts tab flag.
		if(!formErrors.isEmpty()){
			//SessionHelper.setErrorsInSession(request, formErrors);
			if(form.isFromTPAContactsTab()){
			
			ControllerForward result = new ControllerForward(forwards.get("refresh")+"&fromTPAContactsTab=true", true);
			return result.getPath();
			}
			else
			return forwards.get("refresh");
		}

		/*
		 * Prior to navigation to the permissions sub-page we need to setup the defaults and
		 * also some of the settings on the sub-page are also on the main page, so those
		 * need to be copied over.
		 */
		TPAActionHelper.syncSettingsToUserPermissions(form, 
				Integer.parseInt(form.getTpaFirmId()), true);

		return forwards.get(CHANGE_PERMISSIONS);
	}
	

	@RequestMapping(value ="/addTpaUser/", params={"action=reload"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doReload (@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}
		Collection formErrors=doValidate(form, request);

		// To retain TPA Contacts tab flag.
		if(!formErrors.isEmpty()){
			//SessionHelper.setErrorsInSession(request, formErrors);
			if(form.isFromTPAContactsTab()){
			
			ControllerForward result = new ControllerForward(forwards.get("refresh")+"&fromTPAContactsTab=true", true);
			return result.getPath();
			}
			else
			return forwards.get("refresh");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry --> doReload");
		}
		return forwards.get(super.doReload(form, request, response));

	}
	@RequestMapping(value ="/addTpaUser/", params={"action=save"}, method =  {RequestMethod.POST}) 
	public String doSave (@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form,BindingResult bindingResult,ModelMap model,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		Collection formErrors=doValidate(form, request);
		
		// To retain TPA Contacts tab flag.
		if(!formErrors.isEmpty()){
			//SessionHelper.setErrorsInSession(request, formErrors);
			if(form.isFromTPAContactsTab()){
				
			ControllerForward result = new ControllerForward(forwards.get("refresh")+"&fromTPAContactsTab=true", true);
			return result.getPath();
			}
			else
			return forwards.get("refresh");
		}

		 
		if (logger.isDebugEnabled()) {
			logger.debug("entry --> doSave");
		}
		model.addAttribute("mapping", "add");
		String forward = super.doSave(form,model, request, response);
		model.remove("mapping");
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		
	}
	@RequestMapping(value ="/addTpaUser/", params={"action=cancel"}, method =  {RequestMethod.POST}) 
	public String doCancel (@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form,BindingResult bindingResult,ModelMap model,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		Collection formErrors=doValidate(form, request);
		
		// To retain TPA Contacts tab flag.
		if(!formErrors.isEmpty()){
			//SessionHelper.setErrorsInSession(request, formErrors);
			if(form.isFromTPAContactsTab()){
			
			ControllerForward result = new ControllerForward(forwards.get("refresh")+"&fromTPAContactsTab=true", true);
			return result.getPath();
			}
			else
			return forwards.get("refresh");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry --> doCancel");
		}
		model.addAttribute("mapping", "add");
		String forward = super.doCancel(form,model,request, response);
		model.remove("mapping");
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		
	}
	@RequestMapping(value ="/addTpaUser/", params={"action=refresh"}, method =  {RequestMethod.GET,RequestMethod.POST}) 
	public String doRefresh (@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		Collection formErrors=doValidate(form, request);
		if(!formErrors.isEmpty()){
			//SessionHelper.setErrorsInSession(request, formErrors);
			if(form.isFromTPAContactsTab()){
			
			ControllerForward result = new ControllerForward(forwards.get("input")+"&fromTPAContactsTab=true", true);
			return result.getPath();
			}
			else
			return forwards.get("input");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry --> doRefresh");
		}

		String forward = forwards.get("input");
		
		//Check to make sure we are coming from the add/edit page rather than
		// from an already committed
		//user(via the back button).
		if (request.getSession(false).getAttribute(FLOW_TOKEN) == null) {
			return forwards.get(NEW);
		}
		resetTpaDropdowns( form, request);
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <-- doRefresh");
		}
		
		//CL 110473 Begin
		UserProfile userProfile = getUserProfile(request);

		if (TpaumHelper.isTPAUM(userProfile)) {
			request.setAttribute(TPAUM, TPAUM);									
		}
		//CL 110473 End
		
		return forward;
	}

	@RequestMapping(value ="/addTpaUser/", params={"action=confirm"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doConfirm (@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		Collection formErrors=doValidate(form, request);
		if(!formErrors.isEmpty()){
			//SessionHelper.setErrorsInSession(request, formErrors);
			if(form.isFromTPAContactsTab()){
			
			ControllerForward result = new ControllerForward(forwards.get("refresh")+"&fromTPAContactsTab=true", true);
			return result.getPath();
			}
			else
			return forwards.get("refresh");
		}


        /*
         * Check if we should display the edit user button in the confirmation
         * page. If the action form does NOT have any firm access, we should
         * suppress the edit user button.
         */
       
        boolean hasAccess = false;
        for (TpaFirm firm : form.getTpaFirms()) {
            if (!firm.isRemoved() && !firm.isHidden()) {
                hasAccess = true;
                break;
            }
        }

        if (!hasAccess) {
            SessionHelper.setShowEditExternalUserButton(request, Boolean.FALSE);
        } else {
            SessionHelper.setShowEditExternalUserButton(request, Boolean.TRUE);
        }

        return forwards.get(super.doConfirm( form, request, response));
    }
	
	  
	@RequestMapping(value ="/addTpaUser/" ,params={"action=finish"}  , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doFinish (@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form,BindingResult bindingResult,ModelMap model,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		 model.addAttribute("mapping","add");
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		Collection formErrors=doValidate(form, request);
		if(!formErrors.isEmpty()){
			//SessionHelper.setErrorsInSession(request, formErrors);
			if(form.isFromTPAContactsTab()){
			
			ControllerForward result = new ControllerForward(forwards.get("refresh")+"&fromTPAContactsTab=true", true);
			return result.getPath();
			}
			else
			return forwards.get("refresh");
		}
		
        SessionHelper.unsetShowEditExternalUserButton(request);
        return forwards.get(super.doFinish( form,model, request, response));
    }
    
    /**
     * Change web access fields. It will change page elements and reload some information
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws SystemException
     */      
	@RequestMapping(value ="/addTpaUser/", params={"action=changeWebAccess"}, method =  {RequestMethod.POST}) 
	public String doChangeWebAccess (@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form,BindingResult bindingResult,ModelMap model,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		Collection formErrors=doValidate(form, request);
		if(!formErrors.isEmpty()){
			//SessionHelper.setErrorsInSession(request, formErrors);
			if(form.isFromTPAContactsTab()){
			
			ControllerForward result = new ControllerForward(forwards.get("refresh")+"&fromTPAContactsTab=true", true);
			return result.getPath();
			}
			else
			return forwards.get("refresh");
		}
			

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doChangeWebAccess");
        }
        
       model.addAttribute("mapping","add");
        request.getSession(false).setAttribute(FLOW_TOKEN, "changeWebAccess");
        
        // In Add action, when web access is set to No, all permissions will be set to default
        //Added mapping value as edit
        String mapping=(String)model.get("mapping");
        if(isAddUser(mapping) && !form.isWebAccess()){
	        for(TpaFirm tpaFirm : form.getTpaFirms()){
	        	// Populate default permissions
	        	TPAUserContractAccessActionHelper.populatePermissionsFromDefaults(tpaFirm.getContractAccess(0));
	        	TPAActionHelper.copyAttributesToForm(tpaFirm.getContractAccess(0).getUserPermissions(), tpaFirm.getId(), form);
	        }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doChangeWebAccess");
        }
        return forwards.get(doRefresh( form, request, response));
    }
    @Override
	 protected String getSubmitAction(HttpServletRequest request, ActionForm form) throws ServletException {
		String action = super.getSubmitAction(request,form);
		AddEditUserForm actionForm = (AddEditUserForm)form ;
  	  if(StringUtils.isNotBlank(actionForm.getAction()))
		  action = actionForm.getAction()!= null ? actionForm.getAction():StringUtils.EMPTY;
		  
		return action;
		 
	 }
    
   /*  avoids token generation as this class acts as intermediate for many
	 * transactions.
	 * 
     * (non-Javadoc)
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
    	return StringUtils.isNotEmpty(action)
				&& (StringUtils.equalsIgnoreCase(action, "removeTpaFirm")|| StringUtils.equalsIgnoreCase(action, "changePermissions")
					|| StringUtils.equalsIgnoreCase(action, "save"))?true:false;
					
    		    }*/
    
   protected Collection doValidate(ActionForm actionForm, HttpServletRequest request) {
		Collection errors = super.doValidate(actionForm, request);
		
		AddEditUserForm form = (AddEditUserForm) actionForm;

		/*
		 * Different validation rules when we save and when we add tpa firm.
		 */
		if (form.isSaveAction()) {
			// SSN field is mandatory only when web access is Yes
			if(form.isWebAccess() || !form.getSsn().isEmpty()){ 
				SsnRule.getInstance().validate(AddEditUserForm.FIELD_SSN,	errors, form.getSsn());
			}
			
			
			PhoneRule.getInstance().validate(AddEditUserForm.FIELD_PHONE_NUMBER,	errors, form.getPhone().getValueTPA());
			if(StringUtils.isNotEmpty(form.getPhone().getValueTPA()))
			{
				if(StringUtils.isEmpty(form.getPhone().getAreaCode()) || StringUtils.isEmpty(form.getPhone().getPhonePrefix())
							|| StringUtils.isEmpty(form.getPhone().getPhoneSuffix()) || form.getPhone().getValueTPA().length() < 10)
				{
					errors.add(new ValidationError(AddEditUserForm.FIELD_PHONE_NUMBER, ErrorCodes.PHONE_NOT_COMPLETE));
				}
				if(StringUtils.isNotEmpty(form.getPhone().getAreaCode()) && StringUtils.isNotEmpty(form.getPhone().getPhonePrefix()))
				{
					String areaCode = null,phonePrefix = null;
					areaCode = form.getPhone().getAreaCode();
					phonePrefix = form.getPhone().getPhonePrefix();
					if( areaCode.charAt(0) == '0' || areaCode.charAt(0) == '1' || phonePrefix.charAt(0) == '0' || phonePrefix.charAt(0) == '1')
					{
						errors.add(new ValidationError(AddEditUserForm.FIELD_PHONE_NUMBER, ErrorCodes.PHONE_INVALID));
					}
				}
			}
			FaxRule.getInstance().validate(AddEditUserForm.FIELD_FAX_NUMBER,	errors, form.getFax().getValueTPA());
			if(StringUtils.isNotEmpty(form.getFax().getValue()))
			{
				if(StringUtils.isEmpty(form.getFax().getAreaCode()) || StringUtils.isEmpty(form.getFax().getFaxPrefix())
							|| StringUtils.isEmpty(form.getFax().getFaxSuffix()) || form.getFax().getValueTPA().length() < 10)
				{
					errors.add(new ValidationError(AddEditUserForm.FIELD_FAX_NUMBER, ErrorCodes.FAX_NOT_COMPLETE));
				}
				if(StringUtils.isNotEmpty(form.getFax().getAreaCode()) && StringUtils.isNotEmpty(form.getFax().getFaxPrefix()))
				{
					String areaCode = null,faxPrefix = null;
					areaCode = form.getFax().getAreaCode();
					faxPrefix = form.getFax().getFaxPrefix();
					if( areaCode.charAt(0) == '0' || areaCode.charAt(0) == '1' || faxPrefix.charAt(0) == '0' || faxPrefix.charAt(0) == '1')
					{
						errors.add(new ValidationError(AddEditUserForm.FIELD_FAX_NUMBER, ErrorCodes.FAX_INVALID));
					}
				}
			}
			ExtensionRule.getInstance().validate(AddEditUserForm.FIELD_EXTENSION_NUMBER,	errors, form.getExt());
			if( StringUtils.isEmpty(form.getPhone().getValue()) && StringUtils.isNotEmpty(form.getExt()) )
			{
				errors.add(new ValidationError(AddEditUserForm.FIELD_EXTENSION_NUMBER, ErrorCodes.PH_NOTENTERED_EXT_ENTERED));
			}

			// MPR 575, section 3.3.8 email address.
			if (form.getEmail() !=null && 
					(form.getEmail().toLowerCase().indexOf("@jhancock") != -1 || form.getEmail().toLowerCase().indexOf("@manulife") != -1)) {
				
				// check if TPA firm id = 52801 has been selected or not
				boolean specialFirmFound = false;
				for (Iterator it = form.getTpaFirms().iterator(); it.hasNext();) {
					TpaFirm tpaFirmForm = (TpaFirm) it.next();
					if (tpaFirmForm.getId().intValue() == 52801) {
						specialFirmFound = true;
					}
				}
				
				if (!specialFirmFound)
				   errors.add(new ValidationError(AddEditUserForm.FIELD_EMAIL, ErrorCodes.EMAIL_INVALID));
			}	
			
			if (form.getSecondaryEmail() !=null && 
					(form.getSecondaryEmail().toLowerCase().indexOf("@jhancock") != -1 || form.getSecondaryEmail().toLowerCase().indexOf("@manulife") != -1)) {
				
				// check if TPA firm id = 52801 has been selected or not
				boolean specialFirmFound = false;
				for (Iterator it = form.getTpaFirms().iterator(); it.hasNext();) {
					TpaFirm tpaFirmForm = (TpaFirm) it.next();
					if (tpaFirmForm.getId().intValue() == 52801) {
						specialFirmFound = true;
					}
				}
				
				if (!specialFirmFound)
				   errors.add(new ValidationError(AddEditUserForm.FIELD_SECONDARY_EMAIL, ErrorCodes.SECONDARY_EMAIL_INVALID));
			}	
			
			// TODO; This CMA error text needs to be updated
			TpaAcessLevelRule accessRule = new TpaAcessLevelRule(ErrorCodes.BAD_TPA_ACCESS_LEVEL);
            accessRule.validate("", errors, new Pair(form.getUndeletedTpaFirms(), form.isHiddenFirmExist()));
            
            resetTpaDropdowns(form, request);
		} else if (form.getAction().equals(ADD_TPA_FIRM_ACTION)) {

			TpaFirmRule.getInstance().validate(
					AddEditUserForm.FIELD_TPA_FIRM_ID_TO_ADD, errors,
					new Pair(form.getTpaFirms(), form.getTpaFirmId()));

            resetTpaDropdowns(form, request);
		}
		
		if (!errors.isEmpty()) {
            SessionHelper.setErrorsInSession(request, errors);
            request.getSession(false).setAttribute(FLOW_TOKEN, "addEdit");
           
        }

		return errors;
	}
}
 