package com.manulife.pension.ps.web.pif;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.service.lock.valueobject.Lock;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.LockServiceDelegate;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.pif.util.PIFConstants.ProcessStatus;
import com.manulife.pension.service.pif.valueobject.PlanInfoSubmissionHistoryVO;
import com.manulife.pension.util.content.GenericException;

/**
 * This Action class handles plan information delete page related actions
 * 
 * @author Vivek Lingesan
 */

@Controller
@RequestMapping( value ="/contract")
@SessionAttributes({"deletePIFDataForm"})

public class DeletePIFDataController extends BasePIFDataController{
	   
	
	@ModelAttribute("deletePIFDataForm")
	public DeletePIFDataForm populateForm() 
	{
		return new DeletePIFDataForm();
		}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/contract/pic/deletePIFData.jsp");
		forwards.put("default", "/contract/pic/deletePIFData.jsp"); 
		forwards.put("save", "redirect:/do/contract/pic/plansubmission/"); 
		forwards.put("error","/contract/pic/deletePIFData.jsp");
		forwards.put("plansubmission", "redirect:/do/contract/pic/plansubmission/");
		forwards.put("cancel", "redirect:/do/contract/pic/plansubmission/");
		forwards.put("homePageFinder","redirect:/do/home/homePageFinder/");
		}
	
	
	/**
     * This is a static reference to the logger.
     */
    private static final Logger logger = Logger.getLogger(DeletePIFDataController.class);
    
    private static final String GLOBAL_HOME_PAGE_FINDER = "homePageFinder";  

    /**
     * Used to load the delete confirmation page
     * {@inheritDoc}
     */
   
   @RequestMapping(value ="/pic/delete/",  method =  {RequestMethod.POST}) 
   public String doDefault(@Valid @ModelAttribute("deletePIFDataForm") DeletePIFDataForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   throws IOException,ServletException, SystemException {

   	if(bindingResult.hasErrors()){
       	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
       	if(errDirect!=null){
            request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
            return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
       	}
       } 
   	
   	//DeletePIFDataForm form = (DeletePIFDataForm) actionForm;
   	UserProfile userProfile = SessionHelper.getUserProfile(request);
       
   	if(actionForm.getSubmissionId() == null){
           if(userProfile.isInternalUser()){
           	//if internal user
           	return forwards.get(GLOBAL_HOME_PAGE_FINDER);
           }
          	//If external user
          	return forwards.get(ACTION_FORWARD_PIF_SUBMISSION);
       }
   	
   	if(!userProfile.isInternalUser()) {
           final boolean lockObtained = obtainLock(actionForm.getSubmissionId(), request);
           if (!lockObtained) {
               handleObtainLockFailure(actionForm.getSubmissionId(), request);
               return forwards.get(GLOBAL_HOME_PAGE_FINDER);
           }
   	}
       String contractName = ContractServiceDelegate.getInstance().getContractName(actionForm.getContractNumber());
       actionForm.setContractName(contractName);
       
		// to handle double submissions
		//saveToken(request);
		
   	return forwards.get(ACTION_FORWARD_DEFAULT);
   }
   
    /**
     * Used to delete submission data.
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return ActionForward
     * @throws SystemException
     */
   @RequestMapping(value = "/pic/delete/", params={ "action=delete"} , method =  {RequestMethod.POST}) 
   public String doDelete(@Valid @ModelAttribute("deletePIFDataForm") DeletePIFDataForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   throws IOException,ServletException, SystemException {
   
    	if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        } 

    	// this is protect the double submit. If double submit happens,
    	// then redirect to Delete Page
       /* if (!(isTokenValid(request, true))) {
            return forwards.get(ACTION_FORWARD_DEFAULT);
        }*/
        
    	//DeletePIFDataForm form = (DeletePIFDataForm) actionForm;
    	UserProfile userProfile = SessionHelper.getUserProfile(request);
        String profileId = String.valueOf(userProfile.getPrincipal().getProfileId());
        ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();
        if(!userProfile.isInternalUser()){
        	
        	Lock lock = LockServiceDelegate.getInstance().getLockInfo(LockHelper.TPA_PLAN_LOCK_NAME,
	                LockHelper.TPA_PLAN_LOCK_NAME + actionForm.getSubmissionId());
        	long profilesId = userProfile.getPrincipal().getProfileId();
			// If any one has lock on this submission id, and the user id different 
	        if (lock != null) {
	        	if(lock.getLockUserProfileId() != profilesId) {
	        		String userName = ContractServiceDelegate.getInstance().getUserName(
	        				lock.getLockUserProfileId());
	        		Object[] params = {userName};
	        		GenericException exception = new GenericException(ErrorCodes.DRAFT_IS_CURRENTLY_BEING_EDITED,params);
	                errorMessages.add(exception);
	        	}	        		
        	}
	        if (errorMessages.size() > 0) {
	            request.setAttribute(Environment.getInstance().getErrorKey(), errorMessages);
	            return forwards.get(ACTION_FORWARD_ERROR);
	        }
		        
	        PlanInfoSubmissionHistoryVO submissionHistory = new PlanInfoSubmissionHistoryVO();
	        submissionHistory.setCreatedUserProfileId(profileId);
	        submissionHistory.setLastUpdatedUserProfileId(profileId);
	        submissionHistory.setLastSubmittedUserProfileId(profileId);
	        submissionHistory.setProcessStatusCode(ProcessStatus.DELETED.getCode());
	        submissionHistory.setSubmissionId(actionForm.getSubmissionId());
	        submissionHistory.setContractNumber(actionForm.getContractNumber());
	    	ContractServiceDelegate.getInstance().updatePIFData(submissionHistory);
        }
		// reset the token, as we forward to a JSP 
		//resetToken(request);		
    	return forwards.get(ACTION_FORWARD_SAVE);    	
    }
   @RequestMapping(value ="/pic/delete/" ,params={"action=printPDF"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
   public String doPrintPDF (@Valid @ModelAttribute("deletePIFDataForm") DeletePIFDataForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   throws IOException,ServletException, SystemException {
	   if(bindingResult.hasErrors()){
       	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
       	if(errDirect!=null){
            request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
            return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
       	}
       }
	   String forward=super.doPrintPDF( actionForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	   
   }
 
   
   
   
   @RequestMapping(value = "/pic/delete/", params={ "action=cancel"} , method =  {RequestMethod.POST}) 
    public String doCancel(@Valid @ModelAttribute("deletePIFDataForm") DeletePIFDataForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {

		
    	if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        } 
    	
    	
		logger.debug("entry -> doCancel");
		//DeletePIFDataForm form = (DeletePIFDataForm) actionForm;
		
		if(actionForm.getSubmissionId() != null){
	        releaseLock(actionForm.getSubmissionId(), request);
		}
        
        String forward = forwards.get(ACTION_FORWARD_CANCEL);

        logger.debug("exit -> doCancel");
		return forward;	
	}
    
    /*
     * We do already have token validation to make prevent Double submit.
     * (non-Javadoc)
     * 
     * @see
     * com.manulife.pension.platform.web.controller.BaseAction#isTokenRequired
     * (java.lang.String)
     */
    protected boolean isTokenRequired(String action) {
     return false;
    }
    /* (non-Javadoc)
	 *  This code has been changed and added to validate form and request against penetration attack, prior to other validations.
	 *  
	 * @see com.manulife.pension.platform.web.controller.BaseAction#doValidate(org.apache.struts.action.ActionMapping, org.apache.struts.action.Form, javax.servlet.http.HttpServletRequest)
	*/
	
	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}
}