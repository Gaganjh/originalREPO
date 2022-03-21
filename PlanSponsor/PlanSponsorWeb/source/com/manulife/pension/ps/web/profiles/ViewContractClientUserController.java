package com.manulife.pension.ps.web.profiles;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.passcode.MobileMask;
import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.valueobject.ContactCommentVO;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.security.exception.DisabledContractException;
import com.manulife.pension.service.security.exception.DuplicateSSNException;
import com.manulife.pension.service.security.exception.NoContractsWithPRoleException;
import com.manulife.pension.service.security.role.ExternalClientUser;
import com.manulife.pension.service.security.role.IntermediaryContact;
import com.manulife.pension.service.security.role.Trustee;
import com.manulife.pension.service.security.role.UserRoleFactory;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.role.type.RoleType;
import com.manulife.pension.service.security.valueobject.ContactInfo;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;


@Controller
@RequestMapping( value = "/profiles")
@SessionAttributes({"clientAddEditUserForm"})

public class ViewContractClientUserController extends AbstractAddEditContractClientUserController {

	@ModelAttribute("clientAddEditUserForm") 
	public AddEditClientUserForm populateForm() 
	{
		return new AddEditClientUserForm();
	}
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/profiles/viewContractInfoExternalUser.jsp"); 
		forwards.put("unlock","/password/unlockPassword.jsp");
		forwards.put("viewPermissions","redirect:/do/profiles/userPermissions/?fromPage=viewContractInfo");
		forwards.put("refresh","redirect:/do/profiles/viewContractInfo/?action=refresh");
		forwards.put("reset","/password/resetPassword.jsp");
		forwards.put("planSponsorContacts","redirect:/do/contacts/planSponsor/");
	}
	
	private static final String ADD_CONTRACT_ACTION = "addContract";
    
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
    public ViewContractClientUserController() {
        super(ViewContractClientUserController.class);
        securityExceptionFormFieldMap.put(DuplicateSSNException.class,
                AddEditClientUserForm.FIELD_SSN);
        securityExceptionFormFieldMap.put(NoContractsWithPRoleException.class,
                AddEditClientUserForm.FIELD_CONTRACT_ACCESS + "[0]."
                        + ClientUserContractAccess.FIELD_CONTRACT_NUMBER);
    }
  

    @RequestMapping(value = "/viewContractInfo/", params={"action=cancel"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doCancel(@Valid @ModelAttribute("clientAddEditUserForm") AddEditClientUserForm actionForm,BindingResult bindingResult,ModelMap model,HttpServletRequest request,HttpServletResponse response) 
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
    	model.addAttribute("mapping", "view");
        return forwards.get(super.doCancel( actionForm,model, request, response));
    }

    @RequestMapping(value ="/viewContractInfo/",params={"action=finish"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doFinish (@Valid @ModelAttribute("clientAddEditUserForm") AddEditClientUserForm form,BindingResult bindingResult,ModelMap model,HttpServletRequest request,HttpServletResponse response) 
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
    	model.addAttribute("mapping","view");
        SessionHelper.unsetShowEditExternalUserButton(request);
        return forwards.get(super.doFinish( form,model, request, response));
    }

    @RequestMapping(value ="/viewContractInfo/", params={"action=confirm"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doConfirm (@Valid @ModelAttribute("clientAddEditUserForm") AddEditClientUserForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {


        /*
         * Check if we should display the edit user button in the confirmation page. If the action
         * form does NOT have any contract access, we should suppress the edit user button.
         */
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
        List contractAccesses = form.getContractAccesses();
        boolean hasContractAccess = false;
        for (Iterator it = contractAccesses.iterator(); it.hasNext() && !hasContractAccess;) {
            ClientUserContractAccess contractAccess = (ClientUserContractAccess) it.next();
            if (!contractAccess.getPlanSponsorSiteRole().equals(AccessLevelHelper.NO_ACCESS)) {
                hasContractAccess = true;
            }
        }

        
        if (!hasContractAccess) {
            SessionHelper.setShowEditExternalUserButton(request, Boolean.FALSE);
        } else {
            SessionHelper.setShowEditExternalUserButton(request, Boolean.TRUE);
        }

        return forwards.get(super.doConfirm( form, request, response));
    }

    protected String validate( ActionForm actionForm,ModelMap model,
            HttpServletRequest request) {

        // AddEditClientUserForm form = (AddEditClientUserForm) actionForm;

        /*
         * Disabled selections are not sent back from the browser. This is a workaround to clear
         * those flags.
         */
    	model.addAttribute("mapping","addContract");
        return super.validate(actionForm,model,request);
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
        
        String contractNo = request.getParameter("contractNumber");
        int contractNumber = 0;
        
        if (contractNo != null  && !"".equals(contractNo)) {
        	try {
        	contractNumber = Integer.parseInt(contractNo);
        	} catch (NumberFormatException nfe) {
        		contractNumber = 0;
        	}
        } else {
        	contractNumber = 0;
        }

        if (ssn != null && ssn.length() > 0) {
            form.setSsn(new Ssn(ssn));
        }

        UserProfile loginUserProfile = getUserProfile(request);
        UserInfo loginUserInfo = SecurityServiceDelegate.getInstance().getUserInfo(loginUserProfile.getPrincipal());
        form.setLoginUserRole(loginUserProfile.getRole().getDisplayName());

        List<Integer> cannotManageRoleContracts = new ArrayList<Integer>();
        if (!"addContract".equalsIgnoreCase(form.getClientUserAction())) {
        	form.setContractAccesses(ClientUserContractAccessActionHelper.buildContractAccesses(loginUserProfile, loginUserInfo, userInfo, cannotManageRoleContracts));
        } else {
        	form.setContractAccesses(ClientUserContractAccessActionHelper.buildContractAccessesForLoggedInContract(loginUserProfile, loginUserInfo, userInfo, cannotManageRoleContracts, contractNumber));
        }
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
        if (ClientUserContractAccessActionHelper.canManageAllContracts(loginUserInfo, userInfo)) {
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
	private PSValidatorFWInput psValidatorFWInput;
    
   @Autowired
   private AddEditContractClientUserValidator addEditContractClientUserValidator;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
		binder.addValidators(addEditContractClientUserValidator);
	}

    /**
     * Action when add contract link is clicked from Add Contract page
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws SystemException
     */
    @RequestMapping(value ="/viewContractInfo/", params={"action=addContract"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doAddContract (@Valid @ModelAttribute("clientAddEditUserForm") AddEditClientUserForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doAddContract");
        }
        String forward = null;
        
        ClientUserContractAccess accessForm = new ClientUserContractAccess();
        UserProfile userProfile = getUserProfile(request);
        Integer contractNumber = Integer.valueOf(form.getContractToAdd());
        Contract contract = ClientUserContractAccessActionHelper.getContract(userProfile.getRole(), contractNumber);
        
        List<Integer> contractIdList = new ArrayList<Integer>();
        
    	// Get user name by using the profile id
  		String userName = null;
  		String sessionProfileId = (String) request.getParameter("profileId");
  		if (!StringUtils.isBlank(sessionProfileId) && Long.valueOf(sessionProfileId) > 0) {
  			userName = SecurityServiceDelegate.getInstance().getLDAPUserNameByProfileId(Long.valueOf(sessionProfileId));
  		}

  		if (StringUtils.isBlank(userName)) {
  			userName = form.getUserName();
  		}
  		
  		/*
 		 * If we're in edit mode and the form is empty, we forward back to
 		 * the manage users page.
 		 */
 		if (userName == null || userName.length() == 0) {
 			// return mapping.findForward(MANAGE_USERS);
 			return forwards.get(Constants.FORWARD_PLANSPONSOR_CONTACTS);
 		}

 		UserInfo managedUserInfo = ClientUserContractAccessActionHelper.getManagedUserInfo(userProfile.getPrincipal(),
 				userName);

         List errors = null;

         if (contract != null && isContractStatusValid(contract.getStatus())) {
             UserInfo loginUserInfo = SecurityServiceDelegate.getInstance().getUserInfo(getUserProfile(request).getPrincipal());
             
             ContractPermission[] permissions = managedUserInfo.getContractPermissions();
             
             /*
              * Sort managed user's contracts by contract number.
              */
             Arrays.sort(permissions, new Comparator() {
                 public int compare(Object o1, Object o2) {
                     ContractPermission bean1 = (ContractPermission) o1;
                     ContractPermission bean2 = (ContractPermission) o2;
                     Integer contractNumber1 = new Integer(bean1.getContractNumber());
                     Integer contractNumber2 = new Integer(bean2.getContractNumber());
                     return contractNumber1.compareTo(contractNumber2);
                 }
             });
             
             // build contract access list
             for (int i = 0; i < permissions.length; i++) {
             	contractIdList.add(permissions[i].getContractNumber());
             }
	
	        if (loginUserInfo.getRole().isExternalUser()) {
	            // For external users we need the contract-level role
	            ContractPermission contractPermission = loginUserInfo.getContractPermission(contractNumber);
	            ClientUserContractAccessActionHelper.populateNewContractAccess(contractPermission.getRole(), accessForm, contract);
	        } else {
	            ClientUserContractAccessActionHelper.populateNewContractAccess(loginUserInfo.getRole(), accessForm, contract);
	        }
	
	        // If the contract is already on the list (i.e. was deleted) remove it
	        ClientUserContractAccess existingContractAccess = ClientUserContractAccessActionHelper.findContractAccess(contractNumber.intValue(), userProfile, loginUserInfo, managedUserInfo);
	        if (existingContractAccess != null) {
	            form.getAllContractAccesses().remove(existingContractAccess);
	        }
	
	        /*
	         * Add to the beginning of the list.
	         */
	        form.getAllContractAccesses().add(0, accessForm);
	
	        /*
	         * Add a the new contract to the cloned form to maintain the order and track any changes from the defaults
	         */
	        AddEditClientUserForm clonedForm = (AddEditClientUserForm) form.getClonedForm();
	        ClientUserContractAccess clonedContractAccess = ClientUserContractAccessActionHelper.findContractAccess(contractNumber.intValue(), userProfile, loginUserInfo, managedUserInfo);
	        if (clonedContractAccess == null) {
	            // If this is a new contract access, clone the existing one and add to the cloned list
	            clonedContractAccess = (ClientUserContractAccess) accessForm.clone();
	            clonedContractAccess.setPlanSponsorSiteRole(new RoleValueLabelBean(AccessLevelHelper.NO_ACCESS, AccessLevelHelper.NO_ACCESS));
	        } else {
	            // Otherwise, move the found contract access to the front of the cloned list
	            clonedForm.getAllContractAccesses().remove(clonedContractAccess);
	        }
	        clonedForm.getAllContractAccesses().add(0, clonedContractAccess);

        
        } else {
            errors = new ArrayList();
            if (contract == null) {
                errors.add(new ValidationError(AddEditClientUserForm.FIELD_CONTRACT_TO_ADD,
                        ErrorCodes.CONTRACT_NUMBER_INVALID));
            } else {
                DisabledContractException e = new DisabledContractException(this.getClass()
                        .getName(), "addContract", "contract is discontinued " + contractNumber);
                errors.add(new GenericException(Integer.parseInt(e.getErrorCode())));
            }
        }

        if (errors != null) {
            SessionHelper.setErrorsInSession(request, errors);
            forward = forwards.get("input");
        } else {
            /*
             * If the contract ID is valid, reset the field. Otherwise, leave the contract ID as is.
             */
            form.setContractToAdd(null);
        }

        ClientUserContractAccessActionHelper.populateContractDropDown( form, request, contractIdList);

        if (forward == null)
            forward = forwards.get(REFRESH);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doAddContract");
        }
        return forward;
    
    }

    /**
     * Delete current profile contract view action
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws SystemException
     */
    @RequestMapping(value ="/viewContractInfo/", params={"action=deleteContract"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doDeleteContract (@Valid @ModelAttribute("clientAddEditUserForm") AddEditClientUserForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doDeleteContract");
        }
        String contractNumber = null;
        String forward = null;
        List errors = new ArrayList();

        
        contractNumber = form.getSelectedContractNumber();
        
        List contractAccesses = form.getContractAccesses();
        ClientUserContractAccess deletingContractAccess = ClientUserContractAccessActionHelper
                .findContractAccess(contractAccesses, contractNumber);
        
        UserProfile userProfile = getUserProfile(request);
        UserInfo managedUserInfo = (UserInfo) request.getSession(false).getAttribute("managedUserInfo");
        
        List<Integer> contractIdList = ClientUserContractAccessActionHelper.getContractIdList( form, request, userProfile, managedUserInfo.getContractPermissions());

        if (form.isCanManageAllContracts() && contractIdList.size() < 2) {
            errors.add(new GenericException(ErrorCodes.REMOVE_LAST_CONTRACT));
        }

        if (!deletingContractAccess.isNewContract()) {
            // We need to check the cloned version of the contract for the next few scenarios
            AddEditClientUserForm clonedForm = (AddEditClientUserForm) form.getClonedForm();
            ClientUserContractAccess clonedContractAccess = clonedForm.findContractAccess(deletingContractAccess.getContractNumber().intValue());

            if (clonedContractAccess != null) {
                Map<String, Boolean> lastUserFlagMap = ClientUserContractAccessActionHelper.getLastUserFlags(deletingContractAccess.getContractNumber().intValue(), form
                        .getProfileId(), false);
                String[] params = new String[] { contractNumber };

                if (deletingContractAccess.isCbcIndicator()) {
                    if (lastUserFlagMap.get(Trustee.ID).booleanValue()) {
                        GenericException ex = new GenericException(1066, params);
                        errors.add(ex);
                    }
                    /*if (clonedContractAccess.isPrimaryContact()) {
                        GenericException ex = new GenericException(1055, params);
                        errors.add(ex);
                    }
                    if (clonedContractAccess.isMailRecepient()) {
                        GenericException ex = new GenericException(1056, params);
                        errors.add(ex);
                    }
                    */
                }
                // SVC.7 both system converted and business converted contract
                if (clonedContractAccess.isPrimaryContact()) {
                    GenericException ex = new GenericException(1055, params);
                    errors.add(ex);
                }
             // SVC.8 both system converted and business converted contract
                if (clonedContractAccess.isMailRecepient()) {
                    GenericException ex = new GenericException(1056, params);
                    errors.add(ex);
                }
                
                
        }
        }
        
        if (errors.size() > 0) {
            SessionHelper.setErrorsInSession(request, errors);
        } else {
            deletingContractAccess.setPlanSponsorSiteRole(new RoleValueLabelBean(AccessLevelHelper.NO_ACCESS, AccessLevelHelper.NO_ACCESS));
        }

        ClientUserContractAccessActionHelper.populateContractDropDown( form, request);

        if (forward == null)
            forward = forwards.get(REFRESH);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doDeleteContract");
        }
        return forward;
    }

    @RequestMapping(value ="/viewContractInfo/",params={"action=changePermissions"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doChangePermissions (@Valid @ModelAttribute("clientAddEditUserForm") AddEditClientUserForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
        return forward;
    }

    @RequestMapping(value ="/viewContractInfo/", params={"action=viewPermissions"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doViewPermissions (@Valid @ModelAttribute("clientAddEditUserForm") AddEditClientUserForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doViewPermissions");
        }

        String forward = forwards.get("viewPermissions");

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doViewPermissions");
        }
        return forward;
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
    @RequestMapping(value ="/viewContractInfo/", params={"action=changeWebAccess"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doChangeWebAccess (@Valid @ModelAttribute("clientAddEditUserForm") AddEditClientUserForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
   
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doChangeWebAccess");
        }

        List errors = null;

        String forward = null;

        if (errors != null) {
            SessionHelper.setErrorsInSession(request, errors);
            forward = forwards.get("input");
            
        }

        // populate Contract Dropdown list since it is in a request scope
        ClientUserContractAccessActionHelper.populateContractDropDown( form, request);

        if (forward == null)
            forward = forwards.get(REFRESH);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doChangeWebAccess");
        }
        return forward;
    }

    /**
     * User change contract role. It will results reload email preference & permission
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws SystemException
     */
    @RequestMapping(value ="/viewContractInfo/", params={"action=changeRole"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doChangeRole (@Valid @ModelAttribute("clientAddEditUserForm") AddEditClientUserForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doChangeRole");
        }

        List errors = null;
        String forward = null;
        String contractNumber = null;
       
        contractNumber = form.getSelectedContractNumber();
        if (errors != null) {
            SessionHelper.setErrorsInSession(request, errors);
            forward = forwards.get("input");
        }
        ClientUserContractAccess roleChangedContractAccess = ClientUserContractAccessActionHelper.findContractAccess(form.getContractAccesses(), form.getSelectedContractNumber());
        // scc59
        // to do repopulate email preference & default permission
        // populate contract drop down it is in request scope
        UserInfo loginUserInfo = SecurityServiceDelegate.getInstance().getUserInfo(getUserProfile(request).getPrincipal());
        Contract contract = null;
        try{
            contract = ContractServiceDelegate.getInstance().getContractDetails(Integer.parseInt(contractNumber), 6);
        } catch (ContractNotExistException ae){
            throw new SystemException(ae, this.getClass().getName(), "doChangeRole",
            "Contract does not exist.");
        }
        ClientUserContractAccessActionHelper.reloadContractAccessForRoleChange(roleChangedContractAccess, loginUserInfo.getRole(), contract);
        ClientUserContractAccessActionHelper.populateContractDropDown( form, request);
        
        roleChangedContractAccess.setRoleType(null);

        // Update the permissions and preferences to the new default values
        AddEditClientUserForm clonedForm = (AddEditClientUserForm) form.getClonedForm();
        ClientUserContractAccess clonedContractAccess = clonedForm.findContractAccess(Integer.parseInt(contractNumber));
        if (clonedContractAccess != null) {
            clonedContractAccess.setUserPermissions((UserPermissions) roleChangedContractAccess.getUserPermissions().clone());
        }

        // populate flags for warning messages
        if (roleChangedContractAccess.isNewContract()) {
            ClientUserContractAccessActionHelper.setLastPermissionFlags(roleChangedContractAccess, form.getProfileId(), contract);
        }

        if (forward == null)
            forward = forwards.get(REFRESH);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doChangeRole");
        }

        return forward;
    }

    /**
     * @see com.manulife.pension.ps.web.controller.PsAutoController#doDefault(org.apache.struts.action.ActionMapping,
     *      AutoForm,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
 
    
  @RequestMapping(value ="/viewContractInfo/",  method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doDefault (@Valid @ModelAttribute("clientAddEditUserForm") AddEditClientUserForm actionForm,
    		BindingResult bindingResult,ModelMap model,HttpServletRequest request,HttpServletResponse response) 
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
            logger.debug("entry <-- doDefault");
        }
        
        /*
         * Call super's doDefault. If it returns any forward other than the input forward, we just
         * return.
         */
        model.addAttribute("mapping","view");
        String forward = super.doDefault( actionForm,model,request, response);

        if (!forward.equals("input")) {
            return forward;
        }

       String mapping=(String)model.get("mapping");
        request.getSession().setAttribute("userProfileForm", actionForm);

        if (isAddUser(mapping) && actionForm.getAllContractAccesses().size() == 0) {

            UserProfile userProfile = getUserProfile(request);
            /*
             * MPR 17. Add one empty contract for the current contract.
             */
            ClientUserContractAccess accessForm = new ClientUserContractAccess();

            if (userProfile.getCurrentContract() != null) {
                UserInfo loginUserInfo = SecurityServiceDelegate.getInstance().getUserInfo(getUserProfile(request).getPrincipal());

                int contractNumber = userProfile.getCurrentContract().getContractNumber();

                Contract loginContract = ClientUserContractAccessActionHelper.getContract(userProfile.getRole(), new Integer(contractNumber));
                // SAC74 for web access field always be Yes
                actionForm.setWebAccess(AddEditClientUserForm.FORM_YES_CONSTANT);
                actionForm.setOriginalWebAccess(actionForm.getWebAccess());

                if (loginUserInfo.getRole().isInternalUser()) {
                	actionForm.setFieldsEnableForInternalUser(loginUserInfo.getRole().hasPermission(PermissionType.MANAGE_EXTERNAL_USERS_TRUSTEE_AND_AUTH_SIGNOR));
                }

                if (loginUserInfo.getRole().isExternalUser()) {
                    // For external users we need the contract-level role
                    ContractPermission contractPermission = loginUserInfo.getContractPermission(contractNumber);
                    ClientUserContractAccessActionHelper.populateNewContractAccess(contractPermission.getRole(), accessForm, loginContract);
                } else {
                    ClientUserContractAccessActionHelper.populateNewContractAccess(loginUserInfo.getRole(), accessForm, loginContract);
                }
                actionForm.getAllContractAccesses().add(accessForm);

                // Since this is a new profile the logged in user can manage all of this profile's contracts 
                actionForm.setCanManageAllContracts(true);
                
                 
                String contactId = (String)request.getParameter("contactId");
                
                //contact details are populated for the corresponding contact id obtained from request
                if(contactId != null && StringUtils.isNumeric(contactId)) {
                	ContactInfo contactInfo = SecurityServiceDelegate.getInstance().getStagingContactInfo(new Integer(contactId));
                	if(contactInfo != null) {
                		populateContactDetailsInForm(contactInfo, actionForm);
                	}
                }
                
                /*
                 * Clone the form after the dummy contract access object is created in the add user case.
                 */
                actionForm.storeClonedForm();
            }
        }

        ClientUserContractAccessActionHelper.populateContractDropDown( actionForm, request);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <-- doDefault");
        }
        
        return forwards.get("input");
    }

    @RequestMapping(value ="/viewContractInfo/",params={"action=save"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doSave (@Valid @ModelAttribute("clientAddEditUserForm") AddEditClientUserForm form,BindingResult bindingResult,ModelMap model,HttpServletRequest request,HttpServletResponse response) 
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
    	model.addAttribute("mapping", "view");
	       String forward=super.doSave( form,model,request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
    @RequestMapping(value ="/viewContractInfo/",params={"action=refresh"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doRefresh (@Valid @ModelAttribute("clientAddEditUserForm") AddEditClientUserForm form,BindingResult bindingResult,ModelMap model,HttpServletRequest request,HttpServletResponse response) 
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
    	model.addAttribute("mapping", "view");
	       String forward=super.doRefresh( form,model, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
    
    @RequestMapping(value ="/viewContractInfo/", params={"action=reset"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doReset (@Valid @ModelAttribute("clientAddEditUserForm") AddEditClientUserForm form,BindingResult bindingResult,ModelMap model,HttpServletRequest request,HttpServletResponse response) 
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
    	model.addAttribute("mapping", "view");
	       String forward=super.doRefresh( form,model, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
    @RequestMapping(value ="/viewContractInfo/", params={"action=unlock"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doUnlock (@Valid @ModelAttribute("clientAddEditUserForm") AddEditClientUserForm form,BindingResult bindingResult,ModelMap model,HttpServletRequest request,HttpServletResponse response) 
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
    	model.addAttribute("mapping", "view");
	       String forward=super.doUnlock( form,model, request, response);
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

