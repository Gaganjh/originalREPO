package com.manulife.pension.ps.web.profiles;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.passcode.MobileMask;
import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWNull;
import com.manulife.pension.service.contract.valueobject.ContactCommentVO;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.security.exception.DuplicateSSNException;
import com.manulife.pension.service.security.exception.NoContractsWithPRoleException;
import com.manulife.pension.service.security.role.ExternalClientUser;
import com.manulife.pension.service.security.role.IntermediaryContact;
import com.manulife.pension.service.security.role.UserRoleFactory;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.role.type.RoleType;
import com.manulife.pension.service.security.valueobject.ContactInfo;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.content.GenericException;

/**
 * @author Charles Chan
 * @author Steven Wang
 */
@Controller
@RequestMapping( value ="/profiles")
@SessionAttributes({"clientAddEditUserForm","managePasswordForm"})

public class ViewClientUserController extends AbstractAddEditClientUserController {
	@ModelAttribute("clientAddEditUserForm")
	public AddEditClientUserForm populateForm()
	{
		return new AddEditClientUserForm();
		}
	@ModelAttribute("managePasswordForm")
	public ManagePasswordForm populatePasswordForm()
	{
		return new ManagePasswordForm();
		}
	public static HashMap<String,String> forwards=new HashMap<String,String>();
	static{
		forwards.put("input","/profiles/viewExternalUser.jsp");
		forwards.put("unlock","/password/unlockPassword.jsp");
		forwards.put("refresh","redirect:/do/profiles/viewUser/?action=refresh");
		forwards.put("reset","/password/resetPassword.jsp");
		forwards.put("planSponsorContacts","redirect:/do/contacts/planSponsor/");
		forwards.put("viewPermissions","redirect:/do/profiles/userPermissions/?fromPage=view");
		}
	
    private static final List<String> statusList = new ArrayList<String>();
    
    static {
    	statusList.add("AC");
    	statusList.add("CA");
    	statusList.add("CF");
    	statusList.add("PS");
    	statusList.add("DC");
    	statusList.add("PC");
    	statusList.add("IA");
    	statusList.add("DI");
    }

    /**
     * Constructor.
     */
    public ViewClientUserController() {
        super(ViewClientUserController.class);
        securityExceptionFormFieldMap.put(DuplicateSSNException.class,
                AddEditClientUserForm.FIELD_SSN);
        securityExceptionFormFieldMap.put(NoContractsWithPRoleException.class,
                AddEditClientUserForm.FIELD_CONTRACT_ACCESS + "[0]."
                        + ClientUserContractAccess.FIELD_CONTRACT_NUMBER);
    }

    @RequestMapping(value = "/viewUser/" ,params="action=back", method =  {RequestMethod.GET,RequestMethod.POST}) 
    public String doCancel( @ModelAttribute("clientAddEditUserForm") AddEditClientUserForm form, HttpServletRequest request,HttpServletResponse response) 
    throws SystemException {
    	return super.doCancel( form, request, response);
    }
    
     
  @RequestMapping(value ="/viewUser/",params={"action=finish"}, method =  {RequestMethod.GET,RequestMethod.POST}) 
    public String doFinish (@Valid @ModelAttribute("clientAddEditUserForm") AddEditClientUserForm form, BindingResult bindingResult,ModelMap model,HttpServletRequest request,HttpServletResponse response)  {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
    	model.addAttribute("mapping","view");
    	String formErrors=validate(form,model, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
        SessionHelper.unsetShowEditExternalUserButton(request);
        model.remove("mapping","view");
        return forwards.get(super.doFinish( form, request, response));
    }

  

    protected String validate( ActionForm actionForm,ModelMap model,
            HttpServletRequest request) {


        /*
         * Disabled selections are not sent back from the browser. This is a workaround to clear
         * those flags.
         */
        return super.validate( actionForm,model,request);
    }

    /**
     * Populates the UserInfo object with the given action form.
     * 
     * @see com.manulife.pension.ps.web.profiles.AbstractAddEditUserController#populateUserInfo(javax.servlet.http.HttpServletRequest,
     *      com.manulife.pension.service.security.valueobject.UserInfo,
     *      com.manulife.pension.ps.web.profiles.AddEditClientUserForm)
     */
    public void populateUserInfo(HttpServletRequest request, UserInfo userInfo,
            AddEditClientUserForm form) throws SystemException {

        userInfo.setRole(new ExternalClientUser());
        userInfo.setSsn(form.getSsn().toString());

        for (Iterator it = form.getAllContractAccesses().iterator(); it.hasNext();) {

            ClientUserContractAccess access = (ClientUserContractAccess) it.next();

            /*
             * Skip any "new" contract that has No Access permission.
             */
            if (access.getPlanSponsorSiteRole().equals(AccessLevelHelper.NO_ACCESS)) {

                AddEditClientUserForm clonedForm = (AddEditClientUserForm) form
                        .getClonedForm();

                ClientUserContractAccess clonedAccess = clonedForm.findContractAccess(access
                        .getContractNumber().intValue());

                /*
                 * If we cannot find the corresponding contract access in the cloned form. That
                 * means this is newly added. Another case is when the cloned access is no access,
                 * we should remove the item too.
                 */
                if (clonedAccess == null
                        || clonedAccess.getPlanSponsorSiteRole()
                                .equals(AccessLevelHelper.NO_ACCESS)) {
                    /*
                     * We also remove the access from the FORM so that our confirmation page won't
                     * show it.
                     */
                    if (clonedForm.getAllContractAccesses().size() > 0) {
                        clonedForm.getAllContractAccesses().remove(0);
                    }
                    it.remove();
                    continue;
                }
            }

            ContractPermission permission = new ContractPermission(null);

            if (!access.getPlanSponsorSiteRole().equals(AccessLevelHelper.NO_ACCESS)) {
                permission.setRole(UserRoleFactory.getUserRole(access.getPlanSponsorSiteRole().getValue()));
                permission.setRoleType(RoleType.getRoleTypeById(access.getRoleType()));
            }
            
            permission.setCompanyName(access.getContractName());
            ClientUserContractAccessActionHelper.populateContractPermission(permission, access);
            userInfo.addContractPermission(permission);
        }

    }

    /**
     * Populates the action form from the given UserInfo object.
     * 
     * @see com.manulife.pension.ps.web.profiles.AbstractAddEditUserController#populateForm(javax.servlet.http.HttpServletRequest,
     *      com.manulife.pension.ps.web.profiles.AddEditClientUserForm,
     *      com.manulife.pension.service.security.valueobject.UserInfo)
     */
    protected void populateForm(HttpServletRequest request, AddEditClientUserForm form,
            UserInfo userInfo) throws SystemException {

        String ssn = userInfo.getSsn();

        if (ssn != null && ssn.length() > 0) {
            form.setSsn(new Ssn(ssn));
        }

        UserProfile loginUserProfile = getUserProfile(request);
        UserInfo loginUserInfo = SecurityServiceDelegate.getInstance().getUserInfo(loginUserProfile.getPrincipal());
        form.setLoginUserRole(loginUserProfile.getRole().getDisplayName());

        List<Integer> cannotManageRoleContracts = new ArrayList<Integer>();
        form.setContractAccesses(ClientUserContractAccessActionHelper.buildContractAccesses(loginUserProfile, loginUserInfo, userInfo, cannotManageRoleContracts));
        form.setCannotManageRoleContracts(cannotManageRoleContracts);
        //setting contact comment value object
        if(loginUserProfile.getCurrentContract() != null) {
	        ContactInfo contactInfo = new ContactInfo();
        	ContactCommentVO contactCommentVO = userInfo.getContactComment();
        	contactInfo.setContractId(loginUserProfile.getCurrentContract().getContractNumber());
        	contactInfo.setUserProfileId(BigDecimal.valueOf(userInfo.getProfileId()));
	        contactCommentVO = SecurityServiceDelegate.getInstance().getContactComment(contactInfo);
	        userInfo.setContactComment(contactCommentVO);
        }
        form.setCommentDetails(userInfo.getContactComment());
        // scc 104 set client user manage all contract flag & svc 42b
        if (form.getContractAccesses().size() == userInfo.getContractPermissions().length && ClientUserContractAccessActionHelper.canManageAllContracts(loginUserInfo, userInfo)) {
            form.setCanManageAllContracts(true);
        } else {
            form.setCanManageAllContracts(false);
        }
        //
        if (loginUserInfo.getRole().isInternalUser()) {
            form.setFieldsEnableForInternalUser(loginUserInfo.getRole().hasPermission(PermissionType.MANAGE_EXTERNAL_USERS_TRUSTEE_AND_AUTH_SIGNOR));
        }
    }

    @Autowired
    private PSValidatorFWNull psValidatorFWNull;  
    @Autowired 
    private AddEditClientUserValidator addEditClientUserValidator;

    @InitBinder
    protected void initBinder(HttpServletRequest request,
    			ServletRequestDataBinder  binder) {
    	binder.addValidators(psValidatorFWNull);
    	binder.addValidators(addEditClientUserValidator);
    }

    @RequestMapping(value ="/viewUser/",params={"action=changePermissions"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doChangePermissions (@Valid @ModelAttribute("clientAddEditUserForm") AddEditClientUserForm form, BindingResult bindingResult,ModelMap model,HttpServletRequest request,HttpServletResponse response) {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
    	model.addAttribute("mapping","view");
    	String formErrors=validate(form,model, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doChangePermissions");
        }
        String forward = null;
        List errors = null;
        // error checking

       
        ClientUserContractAccess currentContractAccess = ClientUserContractAccessActionHelper
                .findContractAccess(form.getContractAccesses(), form.getSelectedContractNumber());

        errors = new ArrayList();
        String[] params = new String[] { currentContractAccess.getContractNumber().toString() };
        if (currentContractAccess.getPlanSponsorSiteRole().equals(null) || currentContractAccess.getPlanSponsorSiteRole().equals("")) {
            errors.add(new GenericException(ErrorCodes.CHANGE_PERMISSION_WITHOUT_ROLE, params));
        } else {
            if (currentContractAccess.getPlanSponsorSiteRole().equals(IntermediaryContact.ID)
                    && (currentContractAccess.getRoleType() == null || "".equals(currentContractAccess.getRoleType()))) {
                errors.add(new GenericException(2537, params));
            }
        }

        if (!errors.isEmpty()) {
            SessionHelper.setErrorsInSession(request, errors);
            forward = forwards.get("input");
        }

        if (forward == null)
            forward = forwards.get("changePermissions");

        currentContractAccess.setChangePermissionsClicked(true);
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doChangePermissions");
        }
        model.remove("mapping","view");
        return forward;
    }
    @RequestMapping(value ="/viewUser/", params={"action=viewPermissions"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doViewPermissions (@Valid @ModelAttribute("clientAddEditUserForm") AddEditClientUserForm form, BindingResult bindingResult,ModelMap model,HttpServletRequest request,HttpServletResponse response)  {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
    	model.addAttribute("mapping","view");
    	String formErrors=validate(form,model, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doViewPermissions");
        }

        String forward = forwards.get("viewPermissions");

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doViewPermissions");
        }
        model.remove("mapping","view");
        return forward;
    }
        @RequestMapping(value ="/viewUser/",  method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doDefault (@Valid @ModelAttribute("clientAddEditUserForm") AddEditClientUserForm form,BindingResult bindingResult,ModelMap model,HttpServletRequest request,HttpServletResponse response) 
    throws SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
    	model.addAttribute("mapping","view");
    	String formErrors=validate(form,model, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
        if (logger.isDebugEnabled()) {
            logger.debug("entry <-- doDefault");
        }
        
        String mapping=(String)model.get("mapping");
        /*
         * Call super's doDefault. If it returns any forward other than the input forward, we just
         * return.
         */
        String forward = super.doDefault( form,model, request, response);

        if (!forward.equals("input")) {
            return forwards.get(forward);
        }

        
        request.getSession().setAttribute("userProfileForm", form);

        if (isAddUser(mapping) && form.getAllContractAccesses().size() == 0) {

            UserProfile userProfile = getUserProfile(request);
            
             
            ClientUserContractAccess accessForm = new ClientUserContractAccess();

            if (userProfile.getCurrentContract() != null) {
                UserInfo loginUserInfo = SecurityServiceDelegate.getInstance().getUserInfo(getUserProfile(request).getPrincipal());

                int contractNumber = userProfile.getCurrentContract().getContractNumber();

                Contract loginContract = ClientUserContractAccessActionHelper.getContract(userProfile.getRole(), new Integer(contractNumber));
                // SAC74 for web access field always be Yes
                form.setWebAccess(AddEditClientUserForm.FORM_YES_CONSTANT);
                form.setOriginalWebAccess(form.getWebAccess());

                if (loginUserInfo.getRole().isInternalUser()) {
                    form.setFieldsEnableForInternalUser(loginUserInfo.getRole().hasPermission(PermissionType.MANAGE_EXTERNAL_USERS_TRUSTEE_AND_AUTH_SIGNOR));
                }

                if (loginUserInfo.getRole().isExternalUser()) {
                    // For external users we need the contract-level role
                    ContractPermission contractPermission = loginUserInfo.getContractPermission(contractNumber);
                    ClientUserContractAccessActionHelper.populateNewContractAccess(contractPermission.getRole(), accessForm, loginContract);
                } else {
                    ClientUserContractAccessActionHelper.populateNewContractAccess(loginUserInfo.getRole(), accessForm, loginContract);
                }
                form.getAllContractAccesses().add(accessForm);

                // Since this is a new profile the logged in user can manage all of this profile's contracts 
                form.setCanManageAllContracts(true);
                
                 
                String contactId = (String)request.getParameter("contactId");
                
                //contact details are populated for the corresponding contact id obtained from request
                if(contactId != null && StringUtils.isNumeric(contactId)) {
                	ContactInfo contactInfo = SecurityServiceDelegate.getInstance().getStagingContactInfo(new Integer(contactId));
                	if(contactInfo != null) {
                		populateContactDetailsInForm(contactInfo, form);
                	}
                }
                form.storeClonedForm();
            }
        }

        ClientUserContractAccessActionHelper.populateContractDropDown( form, request);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <-- doDefault");
        }
        model.remove("mapping","view");
        return forwards.get("input");
    }

   
    @RequestMapping(value ="/viewUser/",params={"action=refresh"}, method =  RequestMethod.GET) 
    public String doRefresh (@Valid @ModelAttribute("clientAddEditUserFor") AddEditClientUserForm form,BindingResult bindingResult,ModelMap model,HttpServletRequest request,HttpServletResponse response) 
    		throws SystemException {
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
    		}
    	}
    	/*model.addAttribute("mapping","view");
    	String formErrors=validate(form,model, request);
    	if(formErrors!=null){
    		return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
    	}*/
    	model.addAttribute("mapping","view");
    	String forward=super.doRefresh( form,model,request, response);
    	model.remove("mapping","view");
    	return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }
    
    @RequestMapping(value ="/viewUser/", params={"action=reset"}, method =  RequestMethod.GET) 
    public String doReset (@ModelAttribute("clientAddEditUserForm") AddEditClientUserForm form, HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	
	       String forward=super.doReset( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
    @RequestMapping(value ="/viewUser/", params={"action=unlock"}, method =  RequestMethod.GET) 
    public String doUnlock (@Valid @ModelAttribute("clientAddEditUserForm") AddEditClientUserForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
	       String forward=super.doUnlock( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 } 
    
    /**
     * Populates the contact details into form
     * @param contactInfo
     * @param clientUserForm
     */
    private void populateContactDetailsInForm(ContactInfo contactInfo, AddEditClientUserForm clientUserForm ) {
    	clientUserForm.setFirstName(contactInfo.getFirstName());
    	clientUserForm.setLastName(contactInfo.getLastName());
    	
    	if(null != contactInfo.getMobile()) {
    		clientUserForm.setMobileNumber(MobileMask.maskPhone(contactInfo.getMobile().toString()));
    	}	
    	
    	if(null != contactInfo.getPhone()) {
    		clientUserForm.setTelephoneNumber(contactInfo.getPhone());
    	}	
    	
    	if(null != contactInfo.getFax()) {
    		clientUserForm.setFaxNumber(contactInfo.getFax());
    	}
    	clientUserForm.setTelephoneExtension(contactInfo.getExtension());
    	clientUserForm.setAddStaggingContact(Boolean.TRUE);
    	clientUserForm.setStaggingContactId(contactInfo.getContactId());
    	
    }
    
    /**
     * returns true if editing permissions is allowed for the given contract status 
     * @param contractStatus
     * @return boolean
     */
    private boolean isContractStatusValid(String contractStatus) {
    	if (StringUtils.isEmpty(contractStatus) 
    			|| StringUtils.isBlank(contractStatus)) {
    		return false;
    	}

    	if (statusList.contains(contractStatus)) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    
}