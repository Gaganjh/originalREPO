package com.manulife.pension.ps.web.messagecenter.personalization;

import java.io.IOException;
import java.util.ArrayList;

import java.util.EnumMap;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;		
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.HashMap;
import javax.validation.Valid;


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
import com.manulife.pension.service.message.valueobject.MessagePreference;
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
@RequestMapping(value ="/mcPersonalizeMessage")
@SessionAttributes({"messagePrefForm"})

public class MCMessagePreferenceController extends PsAutoController implements
		MCConstants {
	@ModelAttribute("messagePrefForm") 
	public MCMessagePreferenceForm populateForm() 
	{
		return new MCMessagePreferenceForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/messagecenter/message_preference.jsp"); 
		forwards.put("preference","/messagecenter/message_preference.jsp");
		forwards.put("messagecenter","redirect:/do/messagecenter/summary");
		forwards.put("error","redirect:/do/mcPersonalizeMessage");
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
	@RequestMapping(value ="", method =  {RequestMethod.GET,RequestMethod.POST}) 
	public String doDefault(@Valid @ModelAttribute("messagePrefForm") MCMessagePreferenceForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		
		//TODO need to take care
		Set<Integer> accessibleTemplates = SessionHelper.getAccessibleContractMessageTemplates(request.getServletContext(), request);
		MCMessagePreferenceForm form = (MCMessagePreferenceForm) actionForm;
		form.reset( request);
		
		form.setTemplateRepository(MCUtils.getContractMessageTemplateRepository(
				request.getServletContext(), request, MCUtils.getMessageCenterTree(
						request.getServletContext()).getId(),accessibleTemplates));
		form.setMessageCenterTop(MCUtils.getMessageCenterTree(request.getServletContext()));
		UserProfile userProfile = SessionHelper.getUserProfile(request);
	
		form.update(userProfile.getCurrentContract().getContractNumber(), MessageServiceFacadeFactory.getInstance(
				request.getServletContext()).getContractMessagePreference(userProfile.getRole(),
				userProfile.getPrincipal().getProfileId(), userProfile.getCurrentContract().getContractNumber(),
				accessibleTemplates));
		form.setContracts(getContractList(userProfile));
		form.storeClonedForm();
		// Add a struts token to ensure we don't get double submissions.
		//saveToken(request);
		

		return forwards.get("preference");
	}

	/**
	 * The action triggered by selecting another contract to copy the
	 * preference from
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
	@RequestMapping(value ="" ,params={"action=copyPreference"}, method ={RequestMethod.POST}) 
	public String doCopyPreference (@Valid @ModelAttribute("messagePrefForm") MCMessagePreferenceForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
        }*/ // fi

        
		String contractNum = form.getFromContract();
		if (!StringUtils.isEmpty(contractNum)) {
			int contractId = Integer.parseInt(contractNum);
			UserProfile userProfile = SessionHelper.getUserProfile(request);
			List<MessagePreference> prefs = MessageServiceFacadeFactory
					.getInstance(request.getServletContext())
					.getMessagePreferenceFromContract(userProfile, contractId);
			form.copyPreferencesFrom(prefs);
		}

		// Adds a new token, as we forward to a JSP (if it were an action we wouldn't need this).
       // saveToken(request);

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
	@RequestMapping(value ="",params={"action=cancel"}, method =  {RequestMethod.POST}) 
	public String doCancel (@Valid @ModelAttribute("messagePrefForm") MCMessagePreferenceForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
	@RequestMapping(value ="", params={"action=save"} , method =  {RequestMethod.POST}) 
	public String doSave (@Valid @ModelAttribute("messagePrefForm") MCMessagePreferenceForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
        }*/ // fi

		
		savePreference(request, form);
		form.storeClonedForm();

		// Adds a new token, as we forward to a JSP (if it were an action we wouldn't need
        // this).
       // saveToken(request);

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
	@RequestMapping(value ="", params={"action=finish"}, method =  {RequestMethod.POST}) 
	public String doFinish (@Valid @ModelAttribute("messagePrefForm") MCMessagePreferenceForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

        // Check if the token is valid - If not, send to the "error" mapping.
       /* if (!(isTokenValid(request, true))) {
            return forwards.get("error");
        } */// fi

		if (savePreference(request, (MCMessagePreferenceForm) form)) {
			return forwards.get("messagecenter");
		} else {
            // Adds a new token, as we forward to a JSP (if it were an action we wouldn't need
            // this).
            //saveToken(request);

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
			MCMessagePreferenceForm form) throws SystemException {
		List<MessagePreferenceError> errors = null;
		List<LabelValueBean> cs = form.getContracts();
		if (form.isApplyToAll() && cs != null && !cs.isEmpty()) {
			List<Integer> contracts = new ArrayList<Integer>(cs.size() - 1);
			for (LabelValueBean c : cs) {
				String cidStr = c.getValue();
				if (!StringUtils.isEmpty(cidStr)) {
					contracts.add(Integer.parseInt(cidStr));
				}
			}
			errors = MessageServiceFacadeFactory.getInstance(request.getServletContext())
					.updateContractMessagePreferencesToAllContracts(SessionHelper.getUserProfile(request), contracts,
							form.getMessagePreferences());

		} else {
			UserProfile userProfile = getUserProfile(request);
			errors = MessageServiceFacadeFactory.getInstance(request.getServletContext())
					.updateContractMessagePreference(userProfile, userProfile.getPrincipal().getProfileId(),
							form.getMessagePreferences());
		}
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

	private List<LabelValueBean> getContractList(UserProfile profile)
			throws SystemException {
		if (profile.getNumberOfContracts() > 1) {
			int currentContract = profile.getCurrentContract().getContractNumber();
			List<Integer> contracts = new ArrayList<Integer>(profile.getMessageCenterAccessibleContracts());
			List<LabelValueBean> contractList = new ArrayList<LabelValueBean>(contracts.size());
			contractList.add(EmptyLabel);
			for (Integer cid : contracts) {
				if (cid != currentContract) {
					contractList.add(new LabelValueBean(cid.toString(), cid.toString()));
				}
			}
			return contractList;
		} else {
			return null;
		}
	}
	
	@RequestMapping(value ="",params={"task=printPDF"}  , method =  {RequestMethod.GET}) 
	public String doPrintPDF (@Valid @ModelAttribute("messagePrefForm") MCMessagePreferenceForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
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
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}
}
