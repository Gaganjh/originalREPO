package com.manulife.pension.ps.web.contract.csf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.ApolloBackEndException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.service.lock.valueobject.Lock;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.LockServiceDelegate;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWCSF;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.plan.valueobject.PlanDataLite;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;

/**
 * Handles the CSF Confirmation Action
 * 
 * @author Puttaiah Arugunta
 */
@Controller
@RequestMapping( value = "/contract")
@SessionAttributes({"csfForm"})
public class CsfConfirmationController extends CsfBaseController {
	
	@ModelAttribute("csfForm") 
	public CsfForm populateForm() 
	{
		return new CsfForm();
	}
	
	public static Map<String,String> forwards = new HashMap<>();
	static{
		forwards.put("input","/csf/csfConfirmation.jsp");
		forwards.put("csfElectronServices", "redirect:/do/contract/viewServiceFeatures/");
		forwards.put("csfElectronServicesEdit", "redirect:/do/contract/editServiceFeatures/"); 
		forwards.put("csfElectronServicesConf","/csf/csfConfirmation.jsp");
		} 	
	
	/**
	 * Default Constructor 
	 * 
	 */
	public CsfConfirmationController() {
		super(CsfConfirmationController.class);
	}


	/**
     * Method to load the CSF Confirmation page.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws SystemException 
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
	@RequestMapping(value ="/confServiceFeatures/", method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("csfForm") CsfForm csfForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException, ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
	    	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	    	if(errDirect!=null){
	         request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	         return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	    	}
	}

		// Retrieve and set form data for Contract Service Features
		//CsfForm csfForm = (CsfForm) actionForm;

		// Get the Principal from the request
		UserProfile userProfile = getUserProfile(request);
		// get the user profile object and set the current contract to null
		Contract currentContract = userProfile.getCurrentContract();

		if (currentContract == null || !isAllowedPageAccess(userProfile, currentContract)) {
			return forwards.get(CsfConstants.GLOBAL_HOME_PAGE_FINDER);
		}
		// set the loan record keeping indicator
		csfForm.setLoanRecordKeepingInd(currentContract.isLoansRecordKeepingIndicator() ? CsfConstants.CSF_YES : CsfConstants.CSF_NO);

		int contractId = currentContract.getContractNumber();
		
		String forward = null;
		
		getPlanDataLite(request, contractId);
		
		Collection<GenericException> errors = new ArrayList<GenericException>();
		
		// We're just refreshing the page, no button (Edit or Cancel was clicked)
		if (csfForm.getButton() == null) {
			// If this request is not coming form a csf page, go to view mode
			if (request.getSession().getAttribute("csf") == null) {
				return forwards.get(getConfForwardName());
			} else {
				if (LockServiceDelegate.getInstance().lock(LockHelper.CSF_LOCK_NAME,
						LockHelper.CSF_LOCK_NAME + contractId, userProfile.getPrincipal().getProfileId())){
					CsfDataHelper.getInstance().populateCheckBoxValues(csfForm);
					forward =  forwards.get(getConfForwardName());

				}
				else{
					// error message
					forward =  forwards.get(getEditForwardName());
				}
			}
		}else if (LockServiceDelegate.getInstance().lock(LockHelper.CSF_LOCK_NAME, 
				LockHelper.CSF_LOCK_NAME + contractId, userProfile.getPrincipal().getProfileId())){
			CsfDataHelper.getInstance().populateCheckBoxValues(csfForm);
			forward = forwards.get(getConfForwardName());
		}

		else{
			try	{
				Lock lockInfo = LockServiceDelegate.getInstance().getLockInfo(LockHelper.CSF_LOCK_NAME, LockHelper.CSF_LOCK_NAME + contractId);

				UserInfo lockOwnerUserInfo =
					SecurityServiceDelegate.getInstance().searchByProfileId(
							userProfile.getPrincipal(), lockInfo.getLockUserProfileId());

				String lockOwnerDisplayName = LockHelper.getLockOwnerDisplayName(userProfile, lockOwnerUserInfo);
				
				errors.add(new ValidationError(
						CsfConstants.EMPTY_STRING, ErrorCodes.ERROR_CSF_LOCKED_BY_JH_REPRESENTATIVE, new String[] {lockOwnerDisplayName}));

				forward = forwards.get(getEditForwardName());
			}
			catch(SecurityServiceException e)	{
				throw new SystemException(e + "com.manulife.pension.ps.web.contract.csf.CsfAction" 
						+ " doExecute " + " Failed to get user info of lock own. " + e.toString());
			}
		}
		
		// to handle double submissions

		//saveToken(request);

		////TODO saveToken(request);

		
		if (!errors.isEmpty()) {
    		setErrorsInSession(request, errors);
		}
		return forward;
	}
	
	 /**
     * Method to Cancel the current Edit page request and redirects to the View page.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws SystemException 
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
	@RequestMapping(value ="/confServiceFeatures/" ,params={"action=continueEdit"}  , method =  {RequestMethod.POST}) 
	public String doContinueEdit (@Valid @ModelAttribute("csfForm") CsfForm csfForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException{
		
		if(bindingResult.hasErrors()){
	    	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	    	if(errDirect!=null){
	         request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	         return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	    	}
	}
    	 //CsfForm csfForm = (CsfForm) form;
    	 
		csfForm.setButton(CsfConstants.CONTINUE_EDIT_VALUE);
    	
    	 /*UserProfile userProfile = getUserProfile(request);
    	int contractId = userProfile.getCurrentContract().getContractNumber();
    	
		LockServiceDelegate.getInstance().releaseLock(LockHelper.CSF_LOCK_NAME,
				LockHelper.CSF_LOCK_NAME + contractId);
    	myForm.revert();*/
    	
		request.getSession().setAttribute("csf", Boolean.TRUE);

		return forwards.get(getEditForwardName());
	
    }
    /**
     * Method to Accept the current Edit page request and redirects to the View page.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws SystemException 
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     * @throws SecurityServiceException 
     */
	@RequestMapping(value ="/confServiceFeatures/", params={"action=accept"} , method =  {RequestMethod.POST}) 
	public String doAccept (@Valid @ModelAttribute("csfForm") CsfForm csfForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException, SecurityServiceException{		
	    if(bindingResult.hasErrors()){
	    	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	    	if(errDirect!=null){
	         request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	         return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	    	}
	}
		

    	// this is protect the double submit. If double submit happens,
    	// then redirect to View Page

        /*if (!(isTokenValid(request, true))) {
            return mapping.findForward(CsfConstants.CSF_ELECTRON_CONTRACT_SERVICES_PAGE);
*/
        
    	//CsfForm csfForm = (CsfForm) form;
	    csfForm.setButton(CsfConstants.SAVE_BUTTON);
    	UserProfile userProfile = getUserProfile(request);
    	
    	int contractId = userProfile.getCurrentContract().getContractNumber();
 	
    	if (LockServiceDelegate.getInstance().lock(LockHelper.CSF_LOCK_NAME, LockHelper.CSF_LOCK_NAME + contractId, userProfile.getPrincipal().getProfileId())) {
			PlanDataLite planDataLite = getPlanDataLite(request, contractId);
			CsfDataValidator.getInstance().validatePlanData(planDataLite, csfForm);
			
			try {
				Collection<GenericException> errors= saveContractServiceFeatureData(csfForm, request);
				if(errors != null && errors.isEmpty()){
					// All the clear does is set the cloned form to null
					csfForm.clear(request);

					
					csfForm.storeClonedForm();
					//Copy the last updated values of Eligibility CSF after a successful save
				    if(csfForm.getEligibilityServiceMoneyTypes()!=null){
				    	csfForm.setLastUpdatedEligibilityServiceMoneyTypes(csfForm.copyLastUpdatedEligibityServiceMoneyTypes(csfForm.getEligibilityServiceMoneyTypes()));
				    }
				    LockServiceDelegate.getInstance().releaseLock(LockHelper.CSF_LOCK_NAME,
							LockHelper.CSF_LOCK_NAME + contractId);
					
				    csfForm.setButton(null);
				}else{
					setErrorsInSession(request, errors);
		    		return forwards.get(getEditForwardName());
				}
			} catch (ApolloBackEndException e) {
				Collection<GenericException> errors = new ArrayList<GenericException>();
				errors.add(new ValidationError(CsfConstants.EMPTY_STRING,ErrorCodes.TECHNICAL_DIFFICULTIES));
				setErrorsInSession(request, errors);
				return forwards.get(getEditForwardName());
			}
			userProfile.getContractProfile().setQuickReportList(null);
			return forwards.get(getViewForwardName());
		}
		else{
			// error message
			return forwards.get(getEditForwardName());
		}
    }
	 
	/*@SuppressWarnings("rawtypes")
	public Collection doValidate(ActionMapping mapping, Form form,
			HttpServletRequest request) {
		Collection penErrors = PsValidation.doValidatePenTestAutoAction(form,
				mapping, request, CommonConstants.INPUT);
		if (penErrors != null && penErrors.size() > 0) {
			request.removeAttribute(ERROR_KEY);
			return penErrors;
		}
		return super.doValidate(mapping, form, request);
	}*/
	
	@Autowired
	   private PSValidatorFWCSF  psValidatorFWInput;

	@InitBinder
	 public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}

}
