package com.manulife.pension.ps.web.profiles;

import java.io.IOException;
import java.util.HashMap;

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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWTpaViewPermission;

/**
 * @author Aron Rogers
 * 
 * Created on Nov 13, 2006
 */
@Controller
@RequestMapping( value ="/profiles")
@SessionAttributes({"userPermissionsForm"})

public class UserPermissionsController extends PsAutoController {

	@ModelAttribute("userPermissionsForm")
	public UserPermissionsForm populateForm() 
	{
		return new UserPermissionsForm();
		}
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static{
		forwards.put("viewPermissions","/profiles/viewUserPermissions.jsp");
		forwards.put("addPermissions","/profiles/addUserPermissions.jsp");
		forwards.put("editPermissions","/profiles/editUserPermissions.jsp" );
		forwards.put("editProfile","redirect:/do/profiles/editUser/?action=refresh");
		forwards.put("addProfile","redirect:/do/profiles/addUser/?action=refresh");
		forwards.put("deleteProfile","redirect:/do/profiles/deleteProfile/?action=refresh");
		forwards.put("suspendProfile","redirect:/do/profiles/suspendProfile/?action=refresh");
		forwards.put("unsuspendProfile","redirect:/do/profiles/unsuspendProfile/?action=refresh");
		forwards.put("viewContractInfoProfile","redirect:/do/profiles/viewContractInfo/?action=refresh");
		forwards.put("viewProfile","redirect:/do/profiles/viewUser/?action=refresh");
		forwards.put("manageUsers","redirect:/do/profiles/manageUsers/");
		forwards.put("refresh","redirect:/do/profiles/userPermissions/?action=edit");
		
		}

    private static final String FORWARD_VIEW_PERMISSIONS = "viewPermissions";

    private static final String FORWARD_ADD_PERMISSIONS = "addPermissions";

    private static final String FORWARD_EDIT_PERMISSIONS = "editPermissions";

    private static final String FORWARD_ADD_PROFILE = "addProfile";

    private static final String FORWARD_EDIT_PROFILE = "editProfile";

    private static final String FORWARD_DELETE_PROFILE = "deleteProfile";

    private static final String FORWARD_SUSPEND_PROFILE = "suspendProfile";

    private static final String FORWARD_UNSUSPEND_PROFILE = "unsuspendProfile";

    private static final String FORWARD_VIEW_PROFILE = "viewProfile";
    
    private static final String FORWARD_VIEW_CONTRACT_INFO_PROFILE = "viewContractInfoProfile";

    private static final String FORWARD_MANAGE_USERS = "manageUsers";

    public UserPermissionsController() {
        super(UserPermissionsController.class);
    }

    
    @Autowired
    private PSValidatorFWTpaViewPermission psValidatorFWTpaViewPermission;
    @InitBinder
    public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWTpaViewPermission);
	}
    
    
    protected String validate( ActionForm actionForm, HttpServletRequest request) {
        String forward = null;
        
        /**
		 * This code has been changed and added to Validate form and request against
		 * penetration attack, prior to other validations.
		 */
       
   		
        UserProfileForm profileForm = (UserProfileForm) request.getSession().getAttribute("userProfileForm");
        if (profileForm == null) {
            forward = forwards.get(FORWARD_MANAGE_USERS);
        } else {
            UserPermissionsForm permissionsForm = (UserPermissionsForm) actionForm;
            if ("default".equals(permissionsForm.getAction()) || "view".equals(permissionsForm.getAction()) || "add".equals(permissionsForm.getAction())
                    || "edit".equals(permissionsForm.getAction())) {
                if (profileForm.getSelectedContractNumber() == null || "".equals(profileForm.getSelectedContractNumber())) {
                    forward =  forwards.get(FORWARD_MANAGE_USERS);
                }
            }
        }
        return forward;
    }

    @RequestMapping(value ="/userPermissions/", method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doDefault(@Valid @ModelAttribute("userPermissionsForm") UserPermissionsForm actionForm,BindingResult bindingResult,ModelMap model,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("viewPermissions");//if viewPermissions forward not //available, provided default
    		}
    	}
    	String formErrors=validate(actionForm, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
        return doViewFlow( actionForm,model, request, response);
    }

    @RequestMapping(value ="/userPermissions/" ,params={"action=view"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doView (@Valid @ModelAttribute("userPermissionsForm") UserPermissionsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("viewPermissions");//if viewPermissions forward not //available, provided default
    		}
    	}
    	String formErrors=validate(actionForm, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
		
        if (logger.isDebugEnabled()) {
            logger.debug("entry --> doView");
        }

        // Populate the form
        
        UserProfileForm profileForm =	(UserProfileForm) request.getSession().getAttribute("userProfileForm");
        
        populateUserPermissionsForm(actionForm, profileForm, request);


        String fromPage = request.getParameter("fromPage");
        if ("delete".equals(fromPage)) {
        	actionForm.setReturnToScreen(FORWARD_DELETE_PROFILE);
        } else if ("suspend".equals(fromPage)) {
        	actionForm.setReturnToScreen(FORWARD_SUSPEND_PROFILE);
        } else if ("unsuspend".equals(fromPage)) {
        	actionForm.setReturnToScreen(FORWARD_UNSUSPEND_PROFILE);
        } else if ("view".equals(fromPage)){
        	actionForm.setReturnToScreen(FORWARD_VIEW_PROFILE);
        } else {
        	actionForm.setReturnToScreen(FORWARD_VIEW_CONTRACT_INFO_PROFILE);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <-- doView");
        }

        return forwards.get(FORWARD_VIEW_PERMISSIONS);
    }
    //This method is used to call the functionality for the call from doDefault Method
    public String doViewFlow (UserPermissionsForm actionForm, ModelMap model,HttpServletRequest request,HttpServletResponse response) 
    	    throws IOException,ServletException, SystemException {
    	    	
    	        // Populate the form
    	        
    	        UserProfileForm profileForm =	(UserProfileForm) request.getSession().getAttribute("userProfileForm");
    	        
    	        populateUserPermissionsForm(actionForm, profileForm, request);


    	        String fromPage = request.getParameter("fromPage");
    	        if ("delete".equals(fromPage)) {
    	        	actionForm.setReturnToScreen(FORWARD_DELETE_PROFILE);
    	        } else if ("suspend".equals(fromPage)) {
    	        	actionForm.setReturnToScreen(FORWARD_SUSPEND_PROFILE);
    	        } else if ("unsuspend".equals(fromPage)) {
    	        	actionForm.setReturnToScreen(FORWARD_UNSUSPEND_PROFILE);
    	        } else if ("view".equals(fromPage)){
    	        	actionForm.setReturnToScreen(FORWARD_VIEW_PROFILE);
    	        } else {
    	        	actionForm.setReturnToScreen(FORWARD_VIEW_CONTRACT_INFO_PROFILE);
    	        }

    	        if (logger.isDebugEnabled()) {
    	            logger.debug("exit <-- doView");
    	        }

    	        return forwards.get(FORWARD_VIEW_PERMISSIONS);
    	    }

    @RequestMapping(value ="/userPermissions/", params={"action=add"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doAdd (@Valid @ModelAttribute("userPermissionsForm") UserPermissionsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("viewPermissions");//if viewPermissions forward not //available, provided default
    		}
    	}
    	
    	String formErrors=validate(actionForm, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
		
        if (logger.isDebugEnabled()) {
            logger.debug("entry --> doAdd");
        }

        // Populate the form
       
        UserProfileForm profileForm =	(UserProfileForm) request.getSession().getAttribute("userProfileForm");
        populateUserPermissionsForm(actionForm, profileForm, request);
        actionForm.setReturnToScreen(FORWARD_ADD_PROFILE);

        // Clone the form
        actionForm.storeClonedForm();

        if (logger.isDebugEnabled()) {
            logger.debug("exit <-- doAdd");
        }

        return forwards.get(FORWARD_ADD_PERMISSIONS);
    }
    @RequestMapping(value ="/userPermissions/", params={"action=edit"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doEdit (@Valid @ModelAttribute("userPermissionsForm") UserPermissionsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("viewPermissions");//if viewPermissions forward not //available, provided default
    		}
    	}
    	
    	String formErrors=validate(actionForm, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
		
        if (logger.isDebugEnabled()) {
            logger.debug("entry --> doEdit");
        }

        // Populate the form
       
        UserProfileForm profileForm =	(UserProfileForm) request.getSession().getAttribute("userProfileForm");
        populateUserPermissionsForm(actionForm, profileForm, request);
        actionForm.setReturnToScreen(FORWARD_EDIT_PROFILE);

        // Clone the form
        actionForm.storeClonedForm();

        if (logger.isDebugEnabled()) {
            logger.debug("exit <-- doEdit");
        }

        return forwards.get(FORWARD_EDIT_PERMISSIONS);
    }

    @RequestMapping(value ="/userPermissions/", params={"action=cancel"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doCancel (@Valid @ModelAttribute("userPermissionsForm") UserPermissionsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("viewPermissions");//if viewPermissions forward not //available, provided default
	       }
		}
    	
    	String formErrors=validate(actionForm, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
		
        if (logger.isDebugEnabled()) {
            logger.debug("entry --> doCancel");
        }

        // Reset the UserPermissions object in the Profile form
        UserProfileForm profileForm = (UserProfileForm) request.getSession()
                .getAttribute("userProfileForm");
      
        profileForm.findContractAccess(actionForm.getContractNumber()).setUserPermissions(
                ((UserPermissionsForm) actionForm.getClonedForm()).getUserPermissions());

        String forward = forwards.get(actionForm.getReturnToScreen());

        if (logger.isDebugEnabled()) {
            logger.debug("exit <-- doCancel");
        }

        return forward;
    }
    @RequestMapping(value ="/userPermissions/" ,params={"action=continue"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doContinue (@Valid @ModelAttribute("userPermissionsForm") UserPermissionsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("viewPermissions");//if viewPermissions forward not //available, provided default
    		}
    	}
    	String formErrors=validate(actionForm, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}

        if (logger.isDebugEnabled()) {
            logger.debug("entry --> doContinue");
        }
        
       
        UserProfileForm profileForm = (UserProfileForm) request.getSession().getAttribute("userProfileForm");

        // Update fields that may be disabled on the jsp
        if (!actionForm.getUserPermissions().isViewSubmissions()) {
        	actionForm.getUserPermissions().setCreateUploadSubmissions(false);
        	actionForm.getUserPermissions().setViewAllUsersSubmissions(false);
        	actionForm.getUserPermissions().setCashAccount(false);
        	actionForm.getUserPermissions().setDirectDebit(false);
        }
        if (!actionForm.getUserPermissions().isViewAllWithdrawals()) {
        	actionForm.getUserPermissions().setReviewWithdrawals(false);
        	actionForm.getUserPermissions().setSigningAuthority(false);
        }

        String forward = forwards.get(actionForm.getReturnToScreen());
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit <-- doContinue");
        }

        return forward;
    }

    @RequestMapping(value ="/userPermissions/", params={"action=back"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doBack (@Valid @ModelAttribute("userPermissionsForm") UserPermissionsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("viewPermissions");//if viewPermissions forward not //available, provided default
    		}
    	}
    	String formErrors=validate(actionForm, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
        if (logger.isDebugEnabled()) {
            logger.debug("entry --> doBack");
        }

        
        String forward = forwards.get(actionForm.getReturnToScreen());

        if (logger.isDebugEnabled()) {
            logger.debug("exit <-- doBack");
        }

        return forward;
    }

    private void populateUserPermissionsForm(UserPermissionsForm permissionsForm,
    		UserProfileForm profileForm,
            HttpServletRequest request) throws SystemException {
        permissionsForm.setTpaData(false);

        // get contract access
        int contractNumber = Integer.parseInt(profileForm.getSelectedContractNumber());
        ClientUserContractAccess currentContract = profileForm.findContractAccess(contractNumber);

        permissionsForm.setContractNumber(contractNumber);
        permissionsForm.setContractName(currentContract.getContractName());
        permissionsForm.setUserFirstName(profileForm.getFirstName());
        permissionsForm.setUserLastName(profileForm.getLastName());
        permissionsForm.setRole(currentContract.getPlanSponsorSiteRole());
        permissionsForm.setContactType(currentContract.getRoleTypeDisplayName());
        permissionsForm.setPrimaryContact(currentContract.isPrimaryContact());
        permissionsForm.setMailRecipient(currentContract.isMailRecepient());
        permissionsForm.setBusinessConverted(currentContract.isCbcIndicator());
        permissionsForm.setUserPermissions(currentContract.getUserPermissions());

        permissionsForm.setLastUserWithManageUsers(currentContract.isLastUserWithManageUsers());
        
        permissionsForm.setLastUserWithSigningAuthority(currentContract.isLastUserWithSigningAuthority());
        permissionsForm.setLastUserWithSubmissionsAccess(currentContract.isLastUserWithSubmissionsAccess());
        
        permissionsForm.setLastClientUserWithReviewLoans(currentContract.isLastClientUserWithReviewLoans());
        permissionsForm.setLastUserWithReviewLoans(currentContract.isLastUserWithReviewLoansPermission());
        
        permissionsForm.setLastClientUserWithReviewIWithdrawals(currentContract.isLastClientUserWithReviewIWithdrawals());
        permissionsForm.setLastUserWithReviewIWithdrawals(currentContract.isLastUserWithReviewIWithdrawals());
        
        permissionsForm.setUserName(profileForm.getUserName());
    }
    
    @RequestMapping(value ="/userPermissions/",params={"action=printPDF"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doPrintPDF (@Valid @ModelAttribute("userPermissionsForm") UserPermissionsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("viewPermissions");//if viewPermissions forward not //available, provided default
    		}
    	}
    	String formErrors=validate(actionForm, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
		
	       String forward=super.doPrintPDF( actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
    

}
