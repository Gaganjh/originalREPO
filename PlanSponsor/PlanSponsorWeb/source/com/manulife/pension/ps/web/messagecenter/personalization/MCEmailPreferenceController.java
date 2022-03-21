package com.manulife.pension.ps.web.messagecenter.personalization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
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

import com.manulife.pension.exception.ContractDoesNotExistException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.messagecenter.MCConstants;
import com.manulife.pension.ps.web.messagecenter.util.MCEnvironment;
import com.manulife.pension.ps.web.messagecenter.util.MCUtils;
import com.manulife.pension.ps.web.messagecenter.util.MessageServiceFacadeFactory;
import com.manulife.pension.ps.web.noticemanager.util.NoticeManagerUtility;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.message.valueobject.EmailPreference;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.role.PayrollAdministrator;
import com.manulife.pension.validator.ValidationError;

@Controller
@RequestMapping( value ="/messagecenter")
@SessionAttributes({"emailPrefForm"})

public class MCEmailPreferenceController extends PsAutoController {
	@ModelAttribute("emailPrefForm") 
	public MCEmailPreferenceForm populateForm() 
	{
		return new MCEmailPreferenceForm();
		}
	public static HashMap<String,String>forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/messagecenter/email_preference.jsp"); 
		forwards.put("preference","/messagecenter/email_preference.jsp");
		forwards.put("messagecenter","redirect:/do/messagecenter/summary");
		forwards.put("error","redirect:/do/messagecenter/personalizeEmail");
		}

	@RequestMapping(value ="/personalizeEmail",  method = {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("emailPrefForm") MCEmailPreferenceForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		preExecute(actionForm, request, response);
		
		if(bindingResult.hasErrors()){
			
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	    	   
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		if ( !(getUserProfile(request).getRole().isExternalUser() || getUserProfile(request).isBundledGACAR())) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		
		if ( ! MCEnvironment.isMessageCenterAvailable(request)) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		MCEmailPreferenceForm form = (MCEmailPreferenceForm) actionForm;

		form.update(MessageServiceFacadeFactory.getInstance(request.getServletContext())
				.getEmailPreference(SessionHelper.getUserProfile(request)));
		form.storeClonedForm();
		boolean showNoticePreferenceTab = true;
		boolean enableNoticePreferenceTab = true;
		UserProfile userProfile  = getUserProfile(request);	
		Contract contract = userProfile.getCurrentContract();
		try{
			if(!(MCUtils.isInGlobalContext(request))&& !(contract==null)) {
				if  (NoticeManagerUtility.validateProductRestriction(contract)
						|| NoticeManagerUtility.validateContractRestriction(contract))
				{

					showNoticePreferenceTab = false;
				}
				if(Contract.STATUS_CONTRACT_DISCONTINUED.equals(contract.getStatus()))
				{
					enableNoticePreferenceTab = false;
				}
			}
		}catch(ContractDoesNotExistException e)
		{
			throw new SystemException(e,"Getting contract number" +request.getAttribute("contractId") +" doesn't existing  ");
		}
		
		
        // Add a struts token to ensure we don't get double submissions/re-submissions.
       // saveToken(request);
        request.getSession(false).setAttribute(MCConstants.ALERT_NOITICE_PREFERENCE, showNoticePreferenceTab);
        request.getSession(false).setAttribute(MCConstants.ENABLE_ALERT_NOITICE_PREFERENCE, enableNoticePreferenceTab);
		request.setAttribute(MCConstants.ParamContractId,form.getContractId());
		request.setAttribute(MCConstants.ParamUserProfileId,form.getUserProfileId());
		
		return forwards.get("preference");
	}
	 
	protected String preExecute ( MCEmailPreferenceForm form,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		boolean showNoticePreferenceTab = false;
		UserProfile userProfile  = getUserProfile(request);
		if (userProfile.getRole()instanceof PayrollAdministrator)
		{
			showNoticePreferenceTab = false;
			request.getSession(false).setAttribute(MCConstants.ALERT_NOITICE_PREFERENCE, showNoticePreferenceTab);
			return forwards.get("preference");
		}
		return forwards.get(null);
	
	}

	@RequestMapping(value ="/personalizeEmail" ,params={"action=save"}   , method =  {RequestMethod.POST}) 
	public String doSave (@Valid @ModelAttribute("emailPrefForm") MCEmailPreferenceForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
        // Check if the token is valid - If not, send to the "error" mapping.
        /*if (!(isTokenValid(request, true))) {
            return forwards.get("error");
        } */// fi

		
		if (save(form, request,request.getServletContext())) {
			// store the new cloned form
			form.storeClonedForm();
		}
		
		// Adds a new token, as we forward to a JSP (if it were an action we wouldn't need this).
        //saveToken(request);

		return forwards.get("preference");
	}

	@RequestMapping(value ="/personalizeEmail", params={"action=cancel"} , method =  {RequestMethod.POST}) 
	public String doCancel (@Valid @ModelAttribute("emailPrefForm") MCEmailPreferenceForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
        // Check if the token is valid - If not, send to the "error" mapping.
        /*if (!(isTokenValid(request, true))) {
            return forwards.get("error");
        } // fi
*/
		return forwards.get("messagecenter");
	}
	@RequestMapping(value ="/personalizeEmail", params={"action=finish"}  , method =  {RequestMethod.POST}) 
	public String doFinish (@Valid @ModelAttribute("emailPrefForm") MCEmailPreferenceForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response ) 
	throws IOException,ServletException, SystemException {

		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
        // Check if the token is valid - If not, send to the "error" mapping.
        /*if (!(isTokenValid(request, true))) {
            return forwards.get("error");
        } */// fi

		
		if (save(form, request,request.getServletContext())) {
		    return forwards.get("messagecenter");
		} else {
	        // Adds a new token, as we forward to a JSP (if it were an action we wouldn't need
            // this).
           // saveToken(request);

			return forwards.get("preference");
		}
	}

	private boolean save(MCEmailPreferenceForm form, HttpServletRequest request,ServletContext servlet)
			throws SystemException {
		List<ValidationError> errors = new ArrayList<ValidationError>();
		form.validate(errors);
		if (errors.size() > 0) {
			setErrorsInRequest(request, errors);
			return false;
		}
		EmailPreference preference = form.getEmailPreference();
		Principal principal = SessionHelper.getUserProfile(request)
				.getPrincipal();
		MessageServiceFacadeFactory.getInstance(servlet)
				.updateEmailPreference(principal, principal.getProfileId(),
						preference);
		form.update(preference);
		form.storeClonedForm();
		return true;
	}
	
	@RequestMapping(value ="/personalizeEmail",params={"action=printPDF"}  , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doPrintPDF (@Valid @ModelAttribute("emailPrefForm") MCEmailPreferenceForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		       String forward=super.doPrintPDF( form, request, response);
				return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	 }
	
	
	
	
	/**This code has been changed and added  to 
   	 * Validate form and request against penetration attack, prior to other validations.
   	 */
	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
}
