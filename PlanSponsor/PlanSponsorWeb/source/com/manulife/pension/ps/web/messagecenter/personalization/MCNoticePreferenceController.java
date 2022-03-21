package com.manulife.pension.ps.web.messagecenter.personalization;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
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

import com.manulife.pension.exception.ContractDoesNotExistException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.delegate.PlanNoticeDocumentServiceDelegate;
import com.manulife.pension.ps.service.report.notice.valueobject.LookupDescription;
import com.manulife.pension.ps.service.report.notice.valueobject.UserNoticeManagerAlertVO;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.messagecenter.MCConstants;
import com.manulife.pension.ps.web.messagecenter.util.MCUtils;
import com.manulife.pension.ps.web.messagecenter.util.MessageServiceFacadeFactory;
import com.manulife.pension.ps.web.noticemanager.util.NoticeManagerUtility;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.security.role.PayrollAdministrator;

/**
 * Notice Manager Preference class for setting alert message for external user
 * In this page user can see, edit and delete their existing alerts and add new alerts
 * The users can set the date and number of days before which the the alert has displayed
 * Alerts can be prioritized as normal or urgent
 * @author krishta
 *
 */
@Controller
@RequestMapping(value ="/messagecenter")
@SessionAttributes({"noticePrefForm"})

public class MCNoticePreferenceController extends PsAutoController {
	@ModelAttribute("noticePrefForm")
	public MCNoticePreferenceForm populateForm()
	{
		return new MCNoticePreferenceForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/messagecenter/notice_preference.jsp");
		forwards.put("noticepreference","/messagecenter/notice_preference.jsp");
		forwards.put("messagecenter","redirect:/do/messagecenter/summary");
		forwards.put("error","redirect:/do/messagecenter/personalizeEmail");
		}
	
	//SimpleDateFormat is converted to FastDateFormat to make it thread safe
	private static FastDateFormat dateFormat = FastDateFormat.getInstance("MM/dd/yyyy", Locale.US);
	
	private static final Logger logger = Logger.getLogger(MCNoticePreferenceController.class);
 
	
	
	@RequestMapping(value ="/personalizeNotice", method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("noticePrefForm") MCNoticePreferenceForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		preExecute(  actionForm,  request, 
				 response);
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		UserProfile userProfile  = SessionHelper.getUserProfile(request);
		//When bookmarking is done to this page when no contract is selected the page will be redirected to Message Center page
		try{
			actionForm.setContractId(userProfile.getCurrentContract().getContractNumber());
		}catch(NullPointerException e)
		{
			logger.debug(Constants.NULL_POINTER_EXPECTION,e);
			return forwards.get(Constants.MESSAGECENTER_PAGE);
		}
		actionForm.setProfileId(new BigDecimal(userProfile.getPrincipal().getProfileId()));
		//Only External Client Users with Notice Manager Access Per
		if ((userProfile.getRole().isExternalUser() && userProfile.isNoticeManagerAccessAllowed())) {
			actionForm.setAlertsPageEnable(true);
		}else{
			actionForm.setAlertsPageEnable(false);
		}
		
		boolean showNoticePreferenceTab = true;
		boolean enableNoticePreferenceTab = true;
		Contract contract = userProfile.getCurrentContract();
		try{
			//validate the conditions to show or hide to the tab
			if(!(MCUtils.isInGlobalContext(request))&& !(userProfile.getCurrentContract()==null)) {
				if  (NoticeManagerUtility.validateProductRestriction(userProfile.getCurrentContract())
						|| NoticeManagerUtility.validateContractRestriction(userProfile.getCurrentContract()))
				{
					showNoticePreferenceTab = false;
					return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
				}
				//validate the conditions to enable or disable the tab
				if(Contract.STATUS_CONTRACT_DISCONTINUED.equals(contract.getStatus()))
				{
					enableNoticePreferenceTab = false;
					return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
				}
				
			}
		}catch(ContractDoesNotExistException e)
		{
			throw new SystemException(e, Constants.GET_CONTRACT +request.getAttribute(Constants.CONTRACT_NO) + Constants.DOES_NOT_EXIST);
		}
		//populate the alerts for the user from database 
		actionForm.setUserNoticeManagerAlertList(MessageServiceFacadeFactory.getInstance(request.getServletContext())
				.getNoticePreferences(new BigDecimal(userProfile.getPrincipal().getProfileId()),userProfile.getCurrentContract().getContractNumber()));
		populateAlertFrequency( actionForm, request, response);
		//if the there are no alerts or less than 5 alerts for the user add a row by default
		if(actionForm.getUserNoticeManagerAlertList()==null 
				|| actionForm.getUserNoticeManagerAlertList().size() < Constants.USER_ALERT_LIST_SIZE){
			actionForm.addUserNoticeManagerAlert();
			actionForm.setAlertMaxSize(false);
		}else if(actionForm.getUserNoticeManagerAlertList().size() 
				== Constants.USER_ALERT_LIST_SIZE){
			actionForm.setAlertMaxSize(true);
		}
		// Add a struts token to ensure we don't get double submissions/re-submissions.
		//saveToken(request);
		request.getSession(false).setAttribute(MCConstants.ALERT_NOITICE_PREFERENCE, showNoticePreferenceTab);
		request.getSession(false).setAttribute(MCConstants.ENABLE_ALERT_NOITICE_PREFERENCE, enableNoticePreferenceTab);
		return forwards.get(Constants.NOTICE_MANAGER_ALERTS_TAB);
	}
	
	protected String preExecute(ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException, SystemException {
		boolean showNoticePreferenceTab = false;
		UserProfile userProfile  = getUserProfile(request);
		if (userProfile.getRole()instanceof PayrollAdministrator || userProfile.getRole().isInternalUser())
		{
			showNoticePreferenceTab = false;
			request.getSession(false).setAttribute(MCConstants.ALERT_NOITICE_PREFERENCE, showNoticePreferenceTab);
			return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
			
		}
		return forwards.get(null);
		
	
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
		UserProfile userProfile  = SessionHelper.getUserProfile(request);
		Integer contractId =  userProfile.getCurrentContract().getContractNumber();
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
			HttpServletResponse response, ServletContext servlet) throws ServletException, IOException, SystemException {

		HttpSession session = request.getSession(false);
		super.postExecute( form, request, response);
		UserProfile userProfile = getUserProfile(request);
		BigDecimal profileId = new BigDecimal(userProfile.getPrincipal().getProfileId());
		Integer contractId =userProfile.getCurrentContract().getContractNumber();
		if(session.getAttribute(Constants.LOG) == null)
		{
			String userAction =CommonConstants.NOTICE_MANAGER_PAGE;
			//Adds the page log information
			MessageServiceFacadeFactory.getInstance(servlet).userActionLog(contractId,profileId,userAction);
			session.setAttribute(Constants.LOG, "VISITED");
		}
		MCNoticePreferenceForm noticePreferenceForm = (MCNoticePreferenceForm) form;
		if (noticePreferenceForm .getAction().equals(Constants.DELETE))
		{
			//log information when an alert is deleted
			String userAction =CommonConstants.ALERT_DELETE;
			MessageServiceFacadeFactory.getInstance(servlet).userActionLog(contractId,profileId,userAction);
		}
	}
	/**
	 * Cancel the alert changes and return to message summary page
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
	@RequestMapping(value ="/personalizeNotice" ,params={"action=cancel"}, method =  {RequestMethod.POST}) 
	public String doCancel (@Valid @ModelAttribute("noticePrefForm") MCNoticePreferenceForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
			return forwards.get(Constants.ALERT_ERROR);
		}*/
		
		form.setAlertMaxSize(false);
		//reset the action to default
		form.setAction(Constants.DEFAULT_ACTION);
		//direct to message center page
		return forwards.get(Constants.MESSAGECENTER_PAGE);
	}
	/**
	 * Save the alert changes and returns to the same page
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
	@RequestMapping(value ="/personalizeNotice", params={"action=save"} , method =  {RequestMethod.POST}) 
	public String doSave (@Valid @ModelAttribute("noticePrefForm") MCNoticePreferenceForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		UserProfile userProfile  = SessionHelper.getUserProfile(request);
		for(UserNoticeManagerAlertVO userNoticeManagerAlert:form.getUserNoticeManagerAlertList()){
			String stringStartDate = userNoticeManagerAlert.getStringStartDate();
			if(StringUtils.isNotBlank(stringStartDate)){
				//set the Start date in the Date property of UserNoticeManagerAlertVO
				setStartDate(userNoticeManagerAlert,stringStartDate);
			}
		}
		MessageServiceFacadeFactory.getInstance(request.getServletContext()).addUserNoticePreferences(form.getUserNoticeManagerAlertList());
		//populate the values from database to vo
		form.setUserNoticeManagerAlertList(MessageServiceFacadeFactory.getInstance(request.getServletContext())
				.getNoticePreferences(new BigDecimal(userProfile.getPrincipal().getProfileId()),userProfile.getCurrentContract().getContractNumber()));
		if(form.getUserNoticeManagerAlertList().size()< Constants.USER_ALERT_LIST_SIZE)
		{
			//add new alert
			form.addUserNoticeManagerAlert();
		}
		populateAlertFrequency( form, request, response);
		//reset the action to default
		form.setAction(Constants.DEFAULT_ACTION);
		return forwards.get(Constants.NOTICE_MANAGER_ALERTS_TAB);
	}
	/**
	 * Save & Finish the changes and return to message summary page
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
	@RequestMapping(value ="/personalizeNotice", params={"action=finish"}  , method =  {RequestMethod.POST}) 
	public String doFinish (@Valid @ModelAttribute("noticePrefForm") MCNoticePreferenceForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		for(UserNoticeManagerAlertVO userNoticeManagerAlert:form.getUserNoticeManagerAlertList()){
			String stringStartDate = userNoticeManagerAlert.getStringStartDate();
			//set the Start date in the Date property of UserNoticeManagerAlertVO
			if(StringUtils.isNotBlank(stringStartDate)){
				setStartDate(userNoticeManagerAlert,stringStartDate);
			}
		}
		//but we have to insert only new 
		MessageServiceFacadeFactory.getInstance(request.getServletContext()).addUserNoticePreferences(form.getUserNoticeManagerAlertList());
		//reset the action to default
		form.setAction(Constants.DEFAULT_ACTION);
		//direct to message center page
		return forwards.get(Constants.MESSAGECENTER_PAGE);
	}
	/**
	 * ADD a new alert row in the UI
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
	@RequestMapping(value ="/personalizeNotice", params={"action=add"}, method =  {RequestMethod.POST}) 
	public String doAdd (@Valid @ModelAttribute("noticePrefForm") MCNoticePreferenceForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		// add alert should be disabled if there are 5 rows already saved for the user
		if(form.getUserNoticeManagerAlertList().size()==Constants.USER_ALERT_LIST_SIZE)
		{
			form.setAlertMaxSize(true);
		}else
		{
			//add new alert
			form.addUserNoticeManagerAlert();
			if(form.getUserNoticeManagerAlertList().size()==Constants.USER_ALERT_LIST_SIZE)
			{
				form.setAlertMaxSize(true);
			}
		}
		populateAlertFrequency( form, request, response);
		//reset the action to default
		form.setAction(Constants.DEFAULT_ACTION);
		return forwards.get(Constants.NOTICE_MANAGER_ALERTS_TAB);
	}
	/**
	 * Delete or Reset the alert to their default values
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
	@RequestMapping(value ="/personalizeNotice" ,params={"action=delete"}  , method =  {RequestMethod.POST}) 
	public String doDelete (@Valid @ModelAttribute("noticePrefForm") MCNoticePreferenceForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		Integer alertid = form.getTempId();
		//if the alert is not a existing one then delete the alert
		if(!(form.getId() == form.getUserNoticeManagerAlertList().size()-1))
		{
			if(alertid != null && alertid !=0){
				if(alertid > 0 ){
					MessageServiceFacadeFactory.getInstance(request.getServletContext()).deleteAlert(alertid);
				}
				try{
					if(form.getUserNoticeManagerAlertList()!=null 
								&& form.getUserNoticeManagerAlert(form.getId())!=null){
						form.deleteUserNoticeManagerAlert();
					}
				} catch (Exception e) {
					logger.error("Array out of bound exception catched as user retried to " +
							"delete the same alert using retry option in bowser - Contract Number ["+ 
							getUserProfile(request).getCurrentContract().getContractNumber() + "]");
				}
				form.setAlertMaxSize(false);
			}}else{
			//make the values of fields in the row to default
			if(alertid != null && alertid !=0){
			if(alertid > 0 ){
				MessageServiceFacadeFactory.getInstance(request.getServletContext()).deleteAlert(alertid);
			}
			form.flush( request);
		}}	
		populateAlertFrequency( form, request, response);
		//reset the action to default
		postExecute( form, request, response);
		
		form.setAction(Constants.DEFAULT_ACTION);
		return forwards.get(Constants.NOTICE_MANAGER_ALERTS_TAB);
	}
	
	@RequestMapping(value ="/personalizeNotice",params={"action=printPDF"}  , method =  {RequestMethod.GET}) 
	public String doPrintPDF (@Valid @ModelAttribute("noticePrefForm") MCNoticePreferenceForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
	/**
	 * Validate the alerts 
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 */
	
	@Autowired
	private MCNoticePreferenceValidator mCNoticePreferenceValidator;
	@Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	    binder.addValidators(mCNoticePreferenceValidator);
	}
	

	/**
	 * set the Start date in the Date property of UserNoticeManagerAlertVO
	 * @param userNoticeManagerAlert
	 * @param startDate
	 */
	private void setStartDate(UserNoticeManagerAlertVO userNoticeManagerAlert,
			String startDate) {
		try {
			synchronized (dateFormat) {
				userNoticeManagerAlert.setStartDate(dateFormat.parse(startDate));	
			}
		} catch (ParseException e) {
			logger.error(Constants.DATE_PARSING_FAILED, e);
		}
	}
}

