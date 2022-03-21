package com.manulife.pension.ps.web.messagecenter.personalization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
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
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.messagecenter.MCConstants;
import com.manulife.pension.ps.web.messagecenter.util.MessageServiceFacadeFactory;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.environment.valueobject.LabelValueBean;
import com.manulife.pension.service.message.valueobject.MessagePreferenceError;
import com.manulife.pension.service.message.valueobject.MessagePreferenceError.ErrorCode;
import com.manulife.pension.service.security.role.TeamLead;
import com.manulife.pension.validator.ValidationError;

/**
 * Action for the Message priority page
 * 
 * @author snowdde
 *
 */
@Controller
@RequestMapping(value ="/mcCarView/editTpaMessagePreferences")
@SessionAttributes({"messagePrefForm"})

public class CarEditsTpaMessagePreferencesController extends CarTpaMessagePreferencesController implements
		MCConstants {
	@ModelAttribute("messagePrefForm")
	public MCMessagePreferenceForm populateForm() {
		return new MCMessagePreferenceForm();
	}
	public CarEditsTpaMessagePreferencesController(){
		
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input","/messagecenter/car_edits_tpa_message_preferences.jsp");
		forwards.put("preference","/messagecenter/car_edits_tpa_message_preferences.jsp");
		forwards.put("viewTpaMessagePreferences","redirect:/do/mcCarView/viewTpaMessagePreferences");
		forwards.put("error","redirect:/do/mcCarView/editTpaMessagePreferences");
	}

	private static final LabelValueBean EmptyLabel = new LabelValueBean("", "");

	private static EnumMap<ErrorCode, Integer> ErrorMap = new EnumMap<ErrorCode, Integer>(
			ErrorCode.class);
	static {
		ErrorMap.put(ErrorCode.CANNOT_TURN_OFF, ErrorLastUserTurnOff);
	}

	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})
	public String doDefault(@Valid @ModelAttribute("messagePrefForm") MCMessagePreferenceForm form,BindingResult bindingResult,ModelMap model, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");
			}
		}
		if ( !(getUserProfile(request).getRole() instanceof TeamLead) ) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
	
		String forward = super.doDefault(form, request, response);
		return forwards.get(forward);
	}	
	

	/**
	 * Cancel the any change in the preference page
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(params="action=cancel", method = RequestMethod.POST) 
	public String doCancel (@Valid @ModelAttribute("messagePrefForm") MCMessagePreferenceForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");
			}
		}
	
	
		
        /*if (!(isTokenValid(request, true))) {
            return forwards.get("error");
        } */
        
		ControllerRedirect forward = new ControllerRedirect(("viewTpaMessagePreferences"));
        forward.addParameter(MCConstants.ParamUserProfileId, form.getUserProfileId());
        forward.addParameter(MCConstants.ParamContractId, form.getContractId());
		
		return forward.getPath();
	}

	/**
	 * Save the message priority preference changes
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
	
	@RequestMapping(params="action=save", method =  RequestMethod.POST)
	public String doSave(@Valid @ModelAttribute("messagePrefForm") MCMessagePreferenceForm form,BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");
			}
		}
		if ( !(getUserProfile(request).getRole() instanceof TeamLead) ) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		
		savePreference(request, form,request.getServletContext());
		form.storeClonedForm();

		request.setAttribute(MCConstants.ParamContractId, form.getContractId());
		request.setAttribute(MCConstants.ParamUserProfileId, form.getUserProfileId());
		request.setAttribute(MCConstants.AttrUserIdTpa, form.isTpa());
		
		return forwards.get("preference");
	}

	/**
	 * Save & Finish the changes
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
	@RequestMapping(params="action=finish", method =  RequestMethod.POST)
	public String doFinish(@Valid @ModelAttribute("messagePrefForm") MCMessagePreferenceForm actionForm, BindingResult bindingResult, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");
			}
		}
		
		if ( !(getUserProfile(request).getRole() instanceof TeamLead) ) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		
		/*if (!(isTokenValid(request, true))) {
			return mapping.findForward("error");
		}*/

		MCMessagePreferenceForm form = (MCMessagePreferenceForm) actionForm;

		if (savePreference(request, (MCMessagePreferenceForm) actionForm,request.getServletContext())) {
			ControllerRedirect forward = new ControllerRedirect(("viewTpaMessagePreferences"));
			forward.addParameter(MCConstants.ParamUserProfileId, form.getUserProfileId());
			forward.addParameter(MCConstants.ParamContractId, form.getContractId());
			return forward.getPath();
		} else {
			return forwards.get("preference");
		}
	}
	

	/**
	 * Save the preferences
	 * 
	 * @param request
	 * @param form
	 * @return
	 * @throws SystemException
	 */
	private boolean savePreference(HttpServletRequest request,
			MCMessagePreferenceForm form ,ServletContext servlet) throws SystemException {

		List<MessagePreferenceError> errors = null;

		errors = MessageServiceFacadeFactory.getInstance(servlet)
					.updateTpaFirmMessagePreference(
							SessionHelper.getUserProfile(request),
							form.getUserProfileId(),
							form.getMessagePreferences());
		
		if (errors != null && !errors.isEmpty()) {
			request.setAttribute(AttrPreferenceError, errors);
			setErrorsInRequest(request, mapValidationErrors(errors));
			return false;
		} else {
			return true;
		}
	}

	private List<ValidationError> mapValidationErrors(
			List<MessagePreferenceError> errors) {
		List<ValidationError> vErrors = new ArrayList<ValidationError>();
		for (MessagePreferenceError mpe : errors) {
			int errorCode = ErrorMap.get(mpe.getCode());
			addError(vErrors, errorCode, mpe.getMessageTemplateId());
		}
		return vErrors;
	}

	private void addError(List<ValidationError> errors, int code, int templateId) {
		for (ValidationError e : errors) {
			if (e.getErrorCode() == code) {
				e.getFieldIds().add(Integer.toString(templateId));
				return;
			}
		}
		errors.add(new ValidationError(Integer.toString(templateId), code));
		return;
	}
	
	@Autowired
	   private PSValidatorFWInput  psValidatorFWInput;

	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
}
