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

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.ContractDoesNotExistException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.messagecenter.MCConstants;
import com.manulife.pension.ps.web.messagecenter.util.MCUtils;
import com.manulife.pension.ps.web.messagecenter.util.MessageServiceFacadeFactory;
import com.manulife.pension.ps.web.noticemanager.util.NoticeManagerUtility;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.message.valueobject.EmailPreference;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.role.TeamLead;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.validator.ValidationError;

@Controller
@SessionAttributes({"emailPrefForm"})

public class CarViewsEmailPreferencesController extends PsAutoController {
	@ModelAttribute("emailPrefForm") 
	public MCEmailPreferenceForm populateForm() 
	{
		return new MCEmailPreferenceForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/messagecenter/car_views_email_preferences.jsp");
		forwards.put("preference","/messagecenter/car_views_email_preferences.jsp");
		forwards.put("messagecenter","redirect:/do/mcCarView/");
		forwards.put("error","redirect:/do/mcCarView/viewEmailPreferences");
		}

	@RequestMapping(value ="/mcCarView/viewEmailPreferences",  method =  RequestMethod.GET) 
	public String doDefault(@Valid @ModelAttribute("emailPrefForm") MCEmailPreferenceForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	    	   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		if (getUserProfile(request).getRole().isExternalUser()) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}

		MCEmailPreferenceForm form = (MCEmailPreferenceForm) actionForm;
		long userProfileId = Long.parseLong(request.getParameter(MCConstants.ParamUserProfileId));
		form.setUserProfileId(userProfileId);
		form.setContractId(Integer.valueOf(request.getParameter(MCConstants.ParamContractId)));
		
		if ( getUserProfile(request).getRole() instanceof TeamLead ) {
			form.setUserCanEdit(true);
		}
		
		form.update(MessageServiceFacadeFactory.getInstance(request.getServletContext()).getEmailPreference(getUserProfile(request),
				userProfileId));
		form.storeClonedForm();
		UserInfo userInfo=null;
		try {
			userInfo = SecurityServiceDelegate.getInstance().searchByProfileId(
					getUserProfile(request).getPrincipal(), userProfileId);
			form.setFirstName(userInfo.getFirstName());
			form.setLastName(userInfo.getLastName());
			request.setAttribute(MCConstants.AttrUserIdTpa, userInfo.getRole().isTPA());
			
		} catch (SecurityServiceException e) {
			throw new SystemException(e, "could not find user: " + userProfileId);
		}
		//added for notice mgr tab show
		UserProfile userProfile  = SessionHelper.getUserProfile(request);
		boolean showNoticePreferenceTab = true;
		boolean enableNoticePreferenceTab = true;
		Contract contract=null;
		try {
			contract = ContractServiceDelegate.getInstance().getContractDetails(form.getContractId(), 
					  EnvironmentServiceDelegate.getInstance()
					 .retrieveContractDiDuration(userProfile.getRole(), 0,null));
		} catch (ContractNotExistException e) {
			logger.debug("CarViewsEmailPreferencesAction:ContractDoesNotExistException ",e);
		}
		try {
				if(!(MCUtils.isInGlobalContext(request)) && contract!=null) {
					if  (NoticeManagerUtility.validateProductRestriction(contract)
							|| NoticeManagerUtility.validateContractRestriction(userProfile.getCurrentContract()))
					{
					showNoticePreferenceTab = false;
					}
					if(Contract.STATUS_CONTRACT_DISCONTINUED.equals(contract.getStatus()))
					{
						enableNoticePreferenceTab = false;
					}
				}
			} catch(ContractDoesNotExistException ex)
			{
				logger.debug("CarViewsEmailPreferencesAction:ContractDoesNotExistException ",ex);
			}
		// Add a struts token to ensure we don't get double
		// submissions/re-submissions.
		////TODO saveToken(request);
		request.getSession(false).setAttribute(MCConstants.ALERT_NOITICE_PREFERENCE, showNoticePreferenceTab);
		request.getSession(false).setAttribute(MCConstants.ENABLE_ALERT_NOITICE_PREFERENCE, enableNoticePreferenceTab);
		request.setAttribute(MCConstants.ParamContractId,form.getContractId());
		request.setAttribute(MCConstants.ParamUserProfileId,form.getUserProfileId());

		return forwards.get("preference");
	}

	@RequestMapping(value ="/mcCarView/viewEmailPreferences" ,params="action=save" , method =  RequestMethod.POST) 
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
       /* if (!(isTokenValid(request, true))) {
            return forwards.get("error");
        } */// fi

		
		if (save(form, request,request.getServletContext())) {
			// store the new cloned form
			form.storeClonedForm();
		}
		
		// Adds a new token, as we forward to a JSP (if it were an action we wouldn't need this).
        ////TODO saveToken(request);

		return forwards.get("preference");
	}

	@RequestMapping(value ="/mcCarView/viewEmailPreferences", params="action=cancel" , method =  RequestMethod.POST) 
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
        } */// fi

		return forwards.get("messagecenter");
	}
	
	@RequestMapping(value ="/mcCarView/viewEmailPreferences", params="action=finish"  , method =  RequestMethod.POST) 
	public String doFinish (@Valid @ModelAttribute("emailPrefForm") MCEmailPreferenceForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
		
		if (save(form, request,request.getServletContext())) {
		    return forwards.get("messagecenter");
		} else {
	        // Adds a new token, as we forward to a JSP (if it were an action we wouldn't need
            // this).
            ////TODO saveToken(request);

			return forwards.get("preference");
		}
	}

	private boolean save(MCEmailPreferenceForm form, HttpServletRequest request,ServletContext servlet) throws SystemException {
		List<ValidationError> errors = new ArrayList<ValidationError>();
		form.validate(errors);
		if (errors.size() > 0) {
			setErrorsInRequest(request, errors);
			return false;
		}
		EmailPreference preference = form.getEmailPreference();
		MessageServiceFacadeFactory.getInstance(servlet).updateEmailPreference(
				SessionHelper.getUserProfile(request).getPrincipal(), form.getUserProfileId(), preference);
		form.update(preference);
		form.storeClonedForm();
		return true;
	}
	
	@RequestMapping(value ="/mcCarView/viewEmailPreferences",params="action=printPDF"  , method =  RequestMethod.POST) 
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
