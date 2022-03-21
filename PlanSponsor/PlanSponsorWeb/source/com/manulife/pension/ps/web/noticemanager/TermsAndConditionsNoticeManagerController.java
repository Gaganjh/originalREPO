package com.manulife.pension.ps.web.noticemanager;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.delegate.PlanNoticeDocumentServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;


/**
 * This is the action class for the Terms and conditions page. 
 * In this page user can see the Terms and Conditions of Custom plan Notices
 * User can navigate to add page also get the edit and delete permissions only if user agree the terms and conditions 
 * 
 * @author Yeshwanth Kumar
 * 
 */
@Controller
@RequestMapping(value = "/noticemanager")

public class TermsAndConditionsNoticeManagerController extends PsAutoController  {

	@ModelAttribute("termsAndConditionsForm") 
	public TermsAndConditionsNoticeManagerForm populateForm() 
	{
		return new TermsAndConditionsNoticeManagerForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{ 
		forwards.put("input","/noticemanager/termsandconditions.jsp");
		forwards.put("default","/noticemanager/termsandconditions.jsp");
		forwards.put("uploadandShare","redirect:/do/noticemanager/uploadandsharepages/");
		forwards.put("addPage","redirect:/do/noticemanager/addcustomplandocument/");
		forwards.put("build","redirect:/do/noticemanager/buildyourpackage/?fromPage=terms");
		}

	/**
     * This method will be executed first time as default
     * 
     * 
     * @param form
     *            AutoForm
     * @param request
     *            HttpServletRequest     
     * @param response
     *            HttpServletResponse     
     * @return String
     * 
     */
	@RequestMapping(value ="/termsandconditions/", method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("termsAndConditionsForm") TermsAndConditionsNoticeManagerForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
        	}
        }  

		String navigatePath = (String) request.getSession().getAttribute(Constants.ACTION_PERFORMED);
		UserProfile userProfile = getUserProfile(request);
		if(StringUtils.isBlank(navigatePath) || !userProfile.isNoticeManagerAccessAllowed()){
			return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
		}
		
		if(navigatePath.equals(Constants.BUILD_PAGE_ACTION)){
			actionForm.setFromPage(Constants.BUILD_PAGE_ACTION);
		}
		request.getSession().removeAttribute(Constants.ACTION_PERFORMED);
		populateForm(actionForm);
		postExecute(actionForm, request, response);
		return forwards.get(Constants.DEFAULT_ACTION);
	}
	
	
    /**
     * Invokes the print task. It uses the common workflow with validateForm set
     * to false.
     * @throws ServletException 
     * @throws IOException 
     *
     * @see #doCommon(BaseReportForm, HttpServletRequest,
     *      HttpServletResponse, boolean)
     */
	
	@RequestMapping(value ="/termsandconditions/" ,params={"action=print"}, method =  {RequestMethod.GET}) 
	public String doPrint (@Valid @ModelAttribute("termsAndConditionsForm") TermsAndConditionsNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
        	}
        }  

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doPrint");
        }
        populateForm(form);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doPrint");
        }
        postExecute(form, request, response);
        return forwards.get(Constants.DEFAULT_ACTION);
    }
    
	/**
     * This method will be executed when user dis-agrees
     * This will create a record in acceptance log
     * 
     * @param form
     *            TermsAndConditionsNoticeManagerForm
     * @param request
     *            HttpServletRequest     
     * @param response
     *            HttpServletResponse     
     * @return String
     * 
     */
	@RequestMapping(value ="/termsandconditions/",params={"action=cancel"},method =  {RequestMethod.POST}) 
	public String doCancel (@Valid @ModelAttribute("termsAndConditionsForm") TermsAndConditionsNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
        	}
        }  
	
		UserProfile userProfile = getUserProfile(request);
		BigDecimal ProfileId = new BigDecimal(userProfile.getPrincipal().getProfileId());
		
		PlanNoticeDocumentServiceDelegate.getInstance().updateUsersTermConditionAcceptance(ProfileId, false);
		
		populateForm(form);
		postExecute(form, request, response);
		return forwards.get(Constants.UPLOAD_PAGE_ACTION);
	}
	
	/**
     * This method will be executed when user agrees
     * This will create a record in acceptance log
     * 
     * 
     * @param form
     *            TermsAndConditionsNoticeManagerForm
     * @param request
     *            HttpServletRequest     
     * @param response
     *            HttpServletResponse     
     * @return String
     * 
     */
	
	@RequestMapping(value ="/termsandconditions/",params={"action=agree"}, method =  {RequestMethod.POST}) 
	public String doAgree (@Valid @ModelAttribute("termsAndConditionsForm") TermsAndConditionsNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

	
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
        	}
        }  
		
		populateForm(form);
		// Get profile id of logged in user
		UserProfile userProfile = getUserProfile(request);
		BigDecimal ProfileId = new BigDecimal(userProfile.getPrincipal().getProfileId());
		
		PlanNoticeDocumentServiceDelegate.getInstance().updateUsersTermConditionAcceptance(ProfileId, true);
		
				
		form.setAction(Constants.DEFAULT_ACTION);
		request.getSession().setAttribute(Constants.ACTION_PERFORMED, Constants.ADD);
		postExecute(form, request, response);
		if(form.getFromPage().equals(Constants.BUILD_PAGE_ACTION)){
			return forwards.get(Constants.BUILD_PAGE_ACTION);
		}
		return forwards.get(Constants.ADD_PAGE_ACTION);
		
	}
	

	/**
	 * populating default action
	 * @param form
	 * @throws SystemException
	 */
	protected void populateForm(AutoForm form)
			throws SystemException {
		
		
		form.setAction(Constants.DEFAULT_ACTION);		
	}
	
	/**
	 * Adds the page log information
	 * @param form
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	protected void postExecute(ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SystemException {

		HttpSession session = request.getSession(false);
		super.postExecute(form, request, response);
		UserProfile userProfile = getUserProfile(request);
		BigDecimal profileId = new BigDecimal(userProfile.getPrincipal().getProfileId());
		Integer contractId = userProfile.getCurrentContract().getContractNumber();
		String userAction = CommonConstants.TERMS_AND_CONDITIONS_PAGE;
		if (session.getAttribute("Terms Of Use Page") == null) {
			PlanNoticeDocumentServiceDelegate.getInstance().userActionLog(contractId, profileId, userAction);
			session.setAttribute("Terms Of Use Page", "visited");
		}
	}
	
	
	  /**
	   * This code has been changed and added to Validate form and request against
	   * penetration attack, prior to other validations.
	   */
	@Autowired
	private PSValidatorFWDefault psValidatorFWDefault;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWDefault);
	}
}
