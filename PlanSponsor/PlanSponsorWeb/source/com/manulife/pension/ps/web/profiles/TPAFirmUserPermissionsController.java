package com.manulife.pension.ps.web.profiles;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
 *	In support of the add/edit/view tpa user permission screen. 
 * 
 */
@Controller
@RequestMapping( value ="/profiles")
@SessionAttributes({"userPermissionsForm"})

public class TPAFirmUserPermissionsController extends PsAutoController {
	@ModelAttribute("userPermissionsForm") public UserPermissionsForm populateForm() {return new UserPermissionsForm();}
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static
	{
		forwards.put("viewPermissions","/profiles/viewTpaPermissions.jsp");
		forwards.put("addPermissions","/profiles/addTpaUserPermissions.jsp");
		forwards.put("editPermissions","/profiles/editTpaUserPermissions.jsp");
		forwards.put("editTpaUser","redirect:/do/profiles/editTpaUser/?action=reload");
		forwards.put("addTpaUser","redirect:/do/profiles/addTpaUser/?action=reload");
		forwards.put("view","redirect:/do/profiles/viewTpaUser/?action=reload");
		forwards.put("suspend","redirect:/do/profiles/suspendTpaProfile/?action=reload");
		forwards.put("unsuspend","redirect:/do/profiles/unsuspendTpaProfile/?action=reload");
		forwards.put("delete","redirect:/do/profiles/deleteTpaProfile/?action=reload");
		forwards.put("manageUsers","redirect:/do/profiles/manageUsers/");
		}

    private static final String FORWARD_VIEW_PERMISSIONS = "viewPermissions";
    private static final String FORWARD_EDIT_PERMISSIONS = "editPermissions";
    private static final String FORWARD_ADD_PERMISSIONS = "addPermissions";    
    private static final String FORWARD_EDIT_TPA_USER = "editTpaUser";
    private static final String FORWARD_ADD_TPA_USER = "addTpaUser";
    private static final String FORWARD_MANAGE_USERS = "manageUsers";
    private static final String RETURN_TOKEN = "source"; 

    public TPAFirmUserPermissionsController() {
        super(TPAFirmUserPermissionsController.class);
    }

    protected String validate(ActionForm actionForm, HttpServletRequest request) {
    	
        String forward = null;
        /**
		 * This code has been changed and added to Validate form and request against
		 * penetration attack, prior to other validations.
		 */
        AddEditUserForm profileForm = (AddEditUserForm) request.getSession().getAttribute("addEditUserForm");
        if (profileForm == null) {
            forward = forwards.get(FORWARD_MANAGE_USERS);
        } else {
            UserPermissionsForm permissionsForm = (UserPermissionsForm) actionForm;
            if ("default".equals(permissionsForm.getAction()) || "view".equals(permissionsForm.getAction()) || "add".equals(permissionsForm.getAction())
                    || "edit".equals(permissionsForm.getAction())) {
                if (profileForm.getTpaFirmId() == null || "".equals(profileForm.getTpaFirmId())) {
                    forward = forwards.get(FORWARD_MANAGE_USERS);
                }
            }
        }
       
        return forward;
    }

    // gets here when it comes in from other page.
    @RequestMapping(value ="/tpaFirmUserPermissions/",  method =  {RequestMethod.GET}) 
    public String doDefault(@Valid @ModelAttribute("userPermissionsForm") UserPermissionsForm actionForm,BindingResult bindingResult,ModelMap model,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("viewPermissions");//if viewPermissions forward not //available, provided default
    		}
    	}
    	 validate(actionForm, request);
    	 return doEdit(actionForm,bindingResult,request, response);
    }
    @RequestMapping(value ="/tpaFirmUserPermissions/" ,params={"action=view"}  , method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doView (@Valid @ModelAttribute("userPermissionsForm") UserPermissionsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("viewPermissions");//if viewPermissions forward not //available, provided default
	       }
		}
    	validate(form, request);
        if (logger.isDebugEnabled()) {
            logger.debug("entry --> doView");
        }

        // Populate the form
        
        populateUserPermissionsForm(request, form, "view");
        form.setReturnToScreen(request.getParameter(RETURN_TOKEN));
        
         // add or edit (see struts-config)
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit <-- doView");
        }

        return forwards.get(FORWARD_VIEW_PERMISSIONS);
    }
    @RequestMapping(value ="/tpaFirmUserPermissions/", params={"action=edit"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doEdit (@Valid @ModelAttribute("userPermissionsForm") UserPermissionsForm permissionsForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("viewPermissions");//if viewPermissions forward not //available, provided default
	       }
		}
    	validate(permissionsForm, request);
        if (logger.isDebugEnabled()) {
            logger.debug("entry --> doEdit");
        }
    	
        // Populate the form
     
        populateUserPermissionsForm(request, permissionsForm, request.getParameter(RETURN_TOKEN));
        permissionsForm.setReturnToScreen(request.getParameter(RETURN_TOKEN)); // add or edit (see struts-config)
        
        // setup clone for 'changeTracking' on jsp used for warning message if cancel 
        // when changes have been made
        permissionsForm.storeClonedForm();   
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit <-- doEdit");
        }
        
        if ("add".equals(permissionsForm.getReturnToScreen())) {
        	return forwards.get(FORWARD_ADD_PERMISSIONS);
        } else {
        	return forwards.get(FORWARD_EDIT_PERMISSIONS);
        }
       
    }
    
    
    // support of back button on view/suspend/unsuspend/delete sub-screen
    @RequestMapping(value ="/tpaFirmUserPermissions/", params= {"action=back"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doBack (@Valid @ModelAttribute("userPermissionsForm") UserPermissionsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("viewPermissions");//if viewPermissions forward not //available, provided default
	       }
		}
    	validate(form, request);
        if (logger.isDebugEnabled()) {
            logger.debug("entry --> doBack");
        }
        
      
        String backScreen = form.getReturnToScreen();
        
        String forward = forwards.get(backScreen);
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit <-- doBack");
        }

        return forward;
    }

    @RequestMapping(value ="/tpaFirmUserPermissions/", params= {"action=cancel"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doCancel (@Valid @ModelAttribute("userPermissionsForm") UserPermissionsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("viewPermissions");//if viewPermissions forward not //available, provided default
	       }
		}
    	
    	validate(form, request);
        if (logger.isDebugEnabled()) {
            logger.debug("entry --> doCancel");
        }

        AddEditUserForm profileForm = (AddEditUserForm) request.getSession().getAttribute("addEditUserForm");
        
        // since it is cancel, need to revert the values back to what they were before.
      
        UserPermissions originalUP = ((UserPermissionsForm)form.getClonedForm()).getUserPermissions();
        
        int tpaFirmNumber= Integer.parseInt(form.getTpaFirmID());
        
        TpaFirm theTpaFirm = null;
    	for (Iterator it = profileForm.getTpaFirms().iterator(); it.hasNext();) {
			theTpaFirm = (TpaFirm) it.next();
            if (theTpaFirm.getId().intValue() == tpaFirmNumber) {
            	break; // found
            }
    	}
        
    	theTpaFirm.getContractAccess(0).setUserPermissions(originalUP);
            
        String forward = null;
       
        if ("add".equals(form.getReturnToScreen())) {
            forward = forwards.get(FORWARD_ADD_TPA_USER);
        } else {
            forward = forwards.get(FORWARD_EDIT_TPA_USER);        	
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <-- doCancel");
        }

        return forward;
    }

    
    // user want to save settings, then head back to page they came from
    @RequestMapping(value ="/tpaFirmUserPermissions/" ,params={"action=continue"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doContinue (@Valid @ModelAttribute("userPermissionsForm") UserPermissionsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("viewPermissions");//if viewPermissions forward not //available, provided default
	       }
		}
    	validate(form, request);
        if (logger.isDebugEnabled()) {
            logger.debug("entry --> doContinue");
        }

        AddEditUserForm profileForm = (AddEditUserForm) request.getSession().getAttribute("addEditUserForm");

        // write data back so it will be picked up by add/editTPAUser page
       
        int tpaFirmNumber= Integer.parseInt(form.getTpaFirmID());
        
        TPAActionHelper.copyAttributesToForm(form.getUserPermissions(), tpaFirmNumber, profileForm);
            	
        // send back to screen of origin
        String forward = null;
       
        if ("add".equals(form.getReturnToScreen())) {
            forward = forwards.get(FORWARD_ADD_TPA_USER);
        } else {
            forward = forwards.get(FORWARD_EDIT_TPA_USER);        	
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit <-- doContinue");
        }

        return forward;
    }

    
    private void populateUserPermissionsForm(HttpServletRequest request,
    		UserPermissionsForm permissionsForm,
            String action) throws SystemException {
            	
        AddEditUserForm profileForm = (AddEditUserForm) request.getSession().getAttribute("addEditUserForm");
        int tpaFirmNumber = Integer.parseInt(profileForm.getTpaFirmId());

    	TpaFirm theTpaFirm = null;
    	for (Iterator it = profileForm.getTpaFirms().iterator(); it.hasNext();) {
			theTpaFirm = (TpaFirm) it.next();
            if (theTpaFirm.getId().intValue() == tpaFirmNumber) {
            	break; // found
            }
    	}
    	
    	permissionsForm.setAction(action);
    	permissionsForm.setTpaData(true);
    	
    	permissionsForm.setTpaFirmID(String.valueOf(tpaFirmNumber)); 
    	permissionsForm.setTpaFirmName(theTpaFirm.getName());
        permissionsForm.setUserFirstName(profileForm.getFirstName());
        permissionsForm.setUserLastName(profileForm.getLastName());
        
        // Populate fields for warnings
        permissionsForm.setLastUserWithManageUsers(theTpaFirm.isLastUserWithManageUsers());
        permissionsForm.setLastUserWithReceiveILoansEmailAndTPAStaffPlan(theTpaFirm.isLastUserWithReceiveILoansEmailAndTPAStaffPlan());
        permissionsForm.setLastUserWithSigningAuthorityContracts(theTpaFirm.getLastUserWithSigningAuthorityContracts());
        permissionsForm.setLastUserWithReviewIWithdrawalsContracts(theTpaFirm.getLastUserWithReviewIWithdrawalsContracts());
        permissionsForm.setFirmContractsHaveDirectDebitAccounts(theTpaFirm.isFirmContractsHaveDirectDebitAccounts());
        
        permissionsForm.setUserPermissions(theTpaFirm.getContractAccess(0).getUserPermissions());   
        
        // populate loans stuff for warnings
        permissionsForm.setLastUserWithReviewLoansContracts(theTpaFirm.getLastUserWithReviewLoansContracts());
    }
    
    
    @Autowired
    private PSValidatorFWTpaViewPermission psValidatorFWTpaViewPermission;
    @InitBinder
    public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWTpaViewPermission);
	}

}
