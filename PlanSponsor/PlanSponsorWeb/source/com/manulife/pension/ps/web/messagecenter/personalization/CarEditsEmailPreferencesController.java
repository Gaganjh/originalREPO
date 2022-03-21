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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.messagecenter.MCConstants;
import com.manulife.pension.ps.web.messagecenter.util.MessageServiceFacadeFactory;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.message.valueobject.EmailPreference;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.role.TeamLead;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.validator.ValidationError;

/**
 * This class is used to manage email preferences of TPA/PS users (by Team Lead)
 * 
 * @author Dennis Snowdon
 *
 */
@Controller
@SessionAttributes({"emailPrefForm"})

public class CarEditsEmailPreferencesController extends PsAutoController {
	
	@ModelAttribute("emailPrefForm") 
	public MCEmailPreferenceForm populateForm() 
	{
		return new MCEmailPreferenceForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/messagecenter/car_edits_email_preferences.jsp");
		forwards.put("preference","/messagecenter/car_edits_email_preferences.jsp");
		forwards.put("viewEmailPreferences","redirect:/do/mcCarView/viewEmailPreferences");
		forwards.put("error","redirect:/do/mcCarView/editEmailPreferences");
		}

	@RequestMapping(value ="/mcCarView/editEmailPreferences", method = RequestMethod.GET) 
	public String doDefault(@Valid @ModelAttribute("emailPrefForm") MCEmailPreferenceForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				ControllerForward forward = new ControllerForward("refresh","/do" +  new UrlPathHelper().getPathWithinServletMapping(request)+"?userProfileId="+actionForm.getUserProfileId()+"&contractId="+actionForm.getContractId(),true);//if input forward not //available, provided default
				return "redirect:" + forward.getPath();
			}
		}
		if ( !(getUserProfile(request).getRole() instanceof TeamLead) ) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}

		MCEmailPreferenceForm form = (MCEmailPreferenceForm) actionForm;
		long userProfileId = Long.parseLong(request.getParameter(MCConstants.ParamUserProfileId));
		form.update(MessageServiceFacadeFactory.getInstance(request.getServletContext()).getEmailPreference(getUserProfile(request),
				userProfileId));
		form.setUserProfileId(userProfileId);
		form.setContractId(Integer.valueOf(request.getParameter(MCConstants.ParamContractId)));
		form.storeClonedForm();
		
		try {
			UserInfo userInfo = SecurityServiceDelegate.getInstance().searchByProfileId(getUserProfile(request).getPrincipal(), userProfileId);
			form.setFirstName(userInfo.getFirstName());
			form.setLastName(userInfo.getLastName());
			// save the flag whether it is tpa user
			boolean isTpa = userInfo.getRole().isTPA(); 
			form.setTpa(isTpa);
			request.setAttribute(MCConstants.AttrUserIdTpa, isTpa);
			
		} catch (SecurityServiceException e) {
			throw new SystemException(e, "could not find user: " + userProfileId);
		}

		// Add a struts token to ensure we don't get double
		// submissions/re-submissions.
		//TODO saveToken(request);

		request.setAttribute(MCConstants.ParamContractId, form.getContractId());
		request.setAttribute(MCConstants.ParamUserProfileId, form.getUserProfileId());
		return forwards.get("preference");
	}

	/**
	 * This method is called by the Save button
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	
	@RequestMapping(value ="/mcCarView/editEmailPreferences",params= "action=save",method =RequestMethod.POST) 
	public String doSave(@Valid @ModelAttribute("emailPrefForm") MCEmailPreferenceForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				ControllerForward forward = new ControllerForward("refresh","/do" +  new UrlPathHelper().getPathWithinServletMapping(request)+"?userProfileId="+actionForm.getUserProfileId()+"&contractId="+actionForm.getContractId(),true);//if input forward not //available, provided default
				return "redirect:" + forward.getPath();//if input forward not //available, provided default
			}
		}
	
		if ( !(getUserProfile(request).getRole() instanceof TeamLead) ) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		
        // Check if the token is valid - If not, send to the "error" mapping.
      /* TODO
		if (!(isTokenValid(request, true))) {
            return  forwards.get("error");
        } // fi
*/
		MCEmailPreferenceForm form = (MCEmailPreferenceForm) actionForm;
		if (save(form, request, request.getServletContext())) {
			// store the new cloned form
			form.storeClonedForm();
		}
		
		// Adds a new token, as we forward to a JSP (if it were an action we wouldn't need this).
        //TODO saveToken(request);

		request.setAttribute(MCConstants.ParamContractId, form.getContractId());
		request.setAttribute(MCConstants.ParamUserProfileId, form.getUserProfileId());
		request.setAttribute(MCConstants.AttrUserIdTpa, form.isTpa());		
		return forwards.get("preference");
	}

	/**
	 * This method is called by cancel button
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	
	@RequestMapping(value ="/mcCarView/editEmailPreferences",params= "action=cancel",method =RequestMethod.POST) 
	public String doCancel(@Valid @ModelAttribute("emailPrefForm") MCEmailPreferenceForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				ControllerForward forward = new ControllerForward("refresh","/do" +  new UrlPathHelper().getPathWithinServletMapping(request)+"?userProfileId="+actionForm.getUserProfileId()+"&contractId="+actionForm.getContractId(),true);//if input forward not //available, provided default
				return "redirect:" + forward.getPath();//if input forward not //available, provided default
			}
		}
	
        // Check if the token is valid - If not, send to the "error" mapping.
        /*if (!(isTokenValid(request, true))) {
            return  forwards.get("error");
        } */// fi
		MCEmailPreferenceForm form = (MCEmailPreferenceForm) actionForm;
        ControllerRedirect forward = new ControllerRedirect(forwards.get("viewEmailPreferences"));
        forward.addParameter(MCConstants.ParamUserProfileId, form.getUserProfileId());
        forward.addParameter(MCConstants.ParamContractId, form.getContractId());
        
		return forward.getPath();
	}
	
	/**
	 * This method is called by Save & Finish button.
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value ="/mcCarView/editEmailPreferences",params= "action=finish",method =RequestMethod.POST) 
	public String doFinish(@Valid @ModelAttribute("emailPrefForm") MCEmailPreferenceForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				ControllerForward forward = new ControllerForward("refresh","/do" +  new UrlPathHelper().getPathWithinServletMapping(request)+"?userProfileId="+actionForm.getUserProfileId()+"&contractId="+actionForm.getContractId(),true);//if input forward not //available, provided default
				return "redirect:" + forward.getPath();//if input forward not //available, provided default
			}
		}
	
		if ( !(getUserProfile(request).getRole() instanceof TeamLead) ) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		
        // Check if the token is valid - If not, send to the "error" mapping.
       /* if (!(isTokenValid(request, true))) {
            return  forwards.get("error");
        } // fi
*/
		MCEmailPreferenceForm form = (MCEmailPreferenceForm) actionForm;
		if (save(form, request, request.getServletContext())) {
	        ControllerRedirect forward = new ControllerRedirect(forwards.get("viewEmailPreferences"));
	        forward.addParameter(MCConstants.ParamUserProfileId, form.getUserProfileId());
	        forward.addParameter(MCConstants.ParamContractId, form.getContractId());
	        return forward.getPath();
		} else {
	        // Adds a new token, as we forward to a JSP (if it were an action we wouldn't need
            // this).
            //TODO saveToken(request);

    		request.setAttribute(MCConstants.ParamContractId, form.getContractId());
    		request.setAttribute(MCConstants.ParamUserProfileId, form.getUserProfileId());
			return forwards.get("preference");
		}
	}

	/**
	 * This method is called to save the email preferences
	 * 
	 * @param form
	 * @param request
	 * @return
	 * @throws SystemException
	 */
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
