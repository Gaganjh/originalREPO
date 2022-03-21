package com.manulife.pension.ps.web.messagecenter.personalization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.messagecenter.MCConstants;
import com.manulife.pension.ps.web.messagecenter.util.MCUtils;
import com.manulife.pension.ps.web.messagecenter.util.MessageServiceFacadeFactory;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.environment.valueobject.LabelValueBean;
import com.manulife.pension.service.message.valueobject.MessagePreferenceError;
import com.manulife.pension.service.message.valueobject.MessagePreferenceError.ErrorCode;
import com.manulife.pension.validator.ValidationError;

/**
 * Action for the Message priority page
 * 
 * @author guweigu
 *
 */
@Controller
@RequestMapping(value ="/messagecenter")
@SessionAttributes({"messagePrefForm"})

public class TPAMessagePreferenceController extends PsAutoController implements
		MCConstants {

	@ModelAttribute("messagePrefForm") 
	public MCMessagePreferenceForm populateForm()
	{
		return new MCMessagePreferenceForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{ 
		forwards.put("input","/messagecenter/tpa_message_preferences.jsp");
		forwards.put("preference","/messagecenter/tpa_message_preferences.jsp");
		forwards.put("messagecenter","redirect:/do/messagecenter/summary");
		}

	private static final LabelValueBean EmptyLabel = new LabelValueBean("", "");

	private static EnumMap<ErrorCode, Integer> ErrorMap = new EnumMap<ErrorCode, Integer>(
			ErrorCode.class);
	static {
		ErrorMap.put(ErrorCode.CANNOT_TURN_OFF, ErrorLastUserTurnOff);
	}

	/**
	 * Default action is to retrieve the message priority perferences from CSDB
	 * 
	 */
	@RequestMapping(value ="/tpaMessagePreferences",  method = {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("messagePrefForm") MCMessagePreferenceForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		Set<Integer> accessibleTemplates = SessionHelper.getAccessibleTpaFirmMessageTemplates(request.getServletContext(), request);
		MCMessagePreferenceForm form = (MCMessagePreferenceForm) actionForm;
		form.reset( request);
		
		form.setTemplateRepository(MCUtils.getTpaFirmMessageTemplateRepository(
				request.getServletContext(), request, MCUtils.getMessageCenterTree(
						request.getServletContext()).getId(),accessibleTemplates));
		form.setMessageCenterTop(MCUtils.getMessageCenterTree(request.getServletContext()));
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		form.update(0, MessageServiceFacadeFactory.getInstance(request.getServletContext()).getTpaFirmMessagePreference(
				userProfile.getRole(), userProfile.getPrincipal().getProfileId(), accessibleTemplates));
		form.storeClonedForm();
		
		return forwards.get("preference");
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
	@RequestMapping(value ="/tpaMessagePreferences" ,params={"action=cancel"}   , method =  {RequestMethod.POST}) 
	public String doCancel (@Valid @ModelAttribute("messagePrefForm") MCMessagePreferenceForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		return forwards.get("messagecenter");
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
	@RequestMapping(value ="/tpaMessagePreferences", params={"action=save"} , method =  {RequestMethod.POST}) 
	public String doSave (@Valid @ModelAttribute("messagePrefForm") MCMessagePreferenceForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		savePreference(request, form);
		form.storeClonedForm();
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
	@RequestMapping(value ="/tpaMessagePreferences", params={"action=finish"}  , method =  {RequestMethod.POST}) 
	public String doFinish (@Valid @ModelAttribute("messagePrefForm") MCMessagePreferenceForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		if (savePreference(request,form )) {
			return forwards.get("messagecenter");
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
	private boolean savePreference(HttpServletRequest request, MCMessagePreferenceForm form) throws SystemException {

		List<MessagePreferenceError> errors = null;
		UserProfile userProfile = getUserProfile(request);
		errors = MessageServiceFacadeFactory.getInstance(request.getServletContext()).updateTpaFirmMessagePreference(userProfile,
				userProfile.getPrincipal().getProfileId(), form.getMessagePreferences());

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
	
	@RequestMapping(value ="/tpaMessagePreferences",params={"task=printPDF"}  , method =  {RequestMethod.GET}) 
	public String doPrintPDF (@Valid @ModelAttribute("messagePrefForm") MCMessagePreferenceForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");
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
