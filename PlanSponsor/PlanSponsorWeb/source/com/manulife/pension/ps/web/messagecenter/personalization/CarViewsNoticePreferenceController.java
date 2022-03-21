package com.manulife.pension.ps.web.messagecenter.personalization;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.delegate.PlanNoticeDocumentServiceDelegate;
import com.manulife.pension.ps.service.report.notice.valueobject.LookupDescription;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.messagecenter.MCConstants;
import com.manulife.pension.ps.web.messagecenter.util.MessageServiceFacadeFactory;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * CarViewsNoticePreferenceAction class for viewing alert message for CAR user
 * @author krishta
 *
 */
@Controller
@RequestMapping( value ="/mcCarView/viewNoticePreferences")
@SessionAttributes({"noticePrefForm"})

public class CarViewsNoticePreferenceController extends PsAutoController {
	
	@ModelAttribute("noticePrefForm") 
	public MCNoticePreferenceForm populateForm()
	{
		return new MCNoticePreferenceForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/messagecenter/car_views_notice_preference.jsp");
		forwards.put("preference","/messagecenter/car_views_notice_preference.jsp");
		forwards.put("messagecenter","redirect:/do/mcCarView/");
		forwards.put("error","redirect:/do/mcCarView/viewEmailPreferences ");}

	private static final String LOGGED = "NOTICE_MANAGER_ALERTS";

	
	@RequestMapping(  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("noticePrefForm") MCNoticePreferenceForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	    	     request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		long userProfileId = Long.parseLong(request.getParameter(MCConstants.ParamUserProfileId));
		
		UserInfo userInfo=null;
		try {
			userInfo = SecurityServiceDelegate.getInstance().searchByProfileId(
					getUserProfile(request).getPrincipal(), userProfileId);
			request.setAttribute(MCConstants.AttrUserIdTpa, userInfo.getRole().isTPA());
			
		} catch (SecurityServiceException e) {
			throw new SystemException(e, "could not find user: " + userProfileId);
		}
		
		actionForm.setProfileId(new BigDecimal(userProfileId));
		actionForm.setContractId(Integer.valueOf(request.getParameter(MCConstants.ParamContractId)));
		actionForm.setUserNoticeManagerAlertList(MessageServiceFacadeFactory.getInstance(request.getServletContext())
				.getNoticePreferences(actionForm.getProfileId(),actionForm.getContractId()));
		populateAlertFrequency( actionForm, request, response);
		// Add a struts token to ensure we don't get double submissions/re-submissions.
		////TODO saveToken(request);
		request.setAttribute(MCConstants.ParamContractId,actionForm.getContractId());
		request.setAttribute(MCConstants.ParamUserProfileId,actionForm.getProfileId());
		return forwards.get(Constants.CAR_VIEWS_NOTICE_MANAGER_ALERTS_TAB);
	}
	/**
	 * Populates the drop down lists
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	private void populateAlertFrequency(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		MCNoticePreferenceForm noticePreferenceForm = (MCNoticePreferenceForm) actionForm;
		//UserProfile userProfile  = SessionHelper.getUserProfile(request);
		Integer contractId =  noticePreferenceForm.getContractId();
		List<LookupDescription> alertFrequencyCodes = PlanNoticeDocumentServiceDelegate.getInstance().getUserManagerAlertFrequencyCodes(contractId);
		noticePreferenceForm.setAlertFrequencyCodes(alertFrequencyCodes);
	}
	/**
	 * Adds the user action information
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	protected void postExecute( ActionForm form, HttpServletRequest request, 
			HttpServletResponse response,ServletContext servlet) throws ServletException, IOException, SystemException {

		HttpSession session = request.getSession(false);
		super.postExecute( form, request, response);
		long userProfileId = Long.parseLong(request.getParameter(MCConstants.ParamUserProfileId));
		BigDecimal pofileId = new BigDecimal(userProfileId);
		Integer contractId =Integer.valueOf(request.getParameter(MCConstants.ParamContractId));
		if(session.getAttribute(LOGGED) == null)
		{
			String userAction =CommonConstants.NOTICE_MANAGER_PAGE;
			//Adds the page log information
			MessageServiceFacadeFactory.getInstance(servlet).userActionLog(contractId,pofileId,userAction);
			session.setAttribute(LOGGED, "VISITED");
		}
	}
	/**This code has been changed and added  to 
   	 * Validate form and request against penetration attack, prior to other validations.
   	 */
	@RequestMapping(params="action=printPDF" , method =  RequestMethod.POST) 
	public String doPrintPDF (@Valid @ModelAttribute("noticePrefForm") MCNoticePreferenceForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;

	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
}

