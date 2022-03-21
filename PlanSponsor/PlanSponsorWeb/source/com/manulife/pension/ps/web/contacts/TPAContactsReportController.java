package com.manulife.pension.ps.web.contacts;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
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
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.LockServiceDelegate;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.CommonMrlLoggingUtil;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.ContactCommentVO;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.role.BasicInternalUser;
import com.manulife.pension.service.security.role.BundledGaApprover;
import com.manulife.pension.service.security.role.InternalUserManager;
import com.manulife.pension.service.security.role.PFSRelationshipManager;
import com.manulife.pension.service.security.role.RelationshipManager;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.ContactInfo;
import com.manulife.pension.service.security.valueobject.ManageUsersReportData;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.ServiceLogRecord;

/**
 * Action class for TPA contacts tab
 * @author nbalaji
 *
 */
@Controller
@RequestMapping (value="/contacts")
@SessionAttributes({"tpaContactsReportForm"})

public class TPAContactsReportController extends ReportController {

	@ModelAttribute("tpaContactsReportForm") 
	public TPAContactsReportForm populateForm() 
	{
		return new TPAContactsReportForm();
	}
	private static final String TPA_CONTACT_LEVEL_TYPE_CODE = "TP";
	
	private static final String MANAGE_TPA_USERS_PERMISSION_CODE = "TUMN";
	
	private  static HashMap<String,String> forwards = new HashMap<String,String>();
	
	
	  
	static{
		forwards.put("default","/contacts/tpaContacts.jsp");
		forwards.put("common","/contacts/tpaContacts.jsp");
		forwards.put("sort","/contacts/tpaContacts.jsp");
		forwards.put("print","/contacts/tpaContacts.jsp");
		forwards.put("editTpaComments","/contacts/tpaContacts.jsp");
		forwards.put("updateTpaComments","/contacts/tpaContacts.jsp");//redirect
		forwards.put("editCommentsCancel","/contacts/tpaContacts.jsp");//redirect
		forwards.put("input","/contacts/tpaContacts.jsp");
	}
	
	/**
	 * Default Constructor
	 */
	public TPAContactsReportController(){
		super(TPAContactsReportController.class);
	}
	@Override
	protected String getDefaultSort() {
		return ManageUsersReportData.SORT_FIELD_LAST_NAME;
	}

	@Override
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {
		return null;
	}
	

	@Override
	protected String getReportId() {
		return ManageUsersReportData.REPORT_ID;
	}

	@Override
	protected String getReportName() {
		return ManageUsersReportData.REPORT_NAME;
	}

	@Override
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request)
			throws SystemException {
		logDebug("entry -> populateReportCriteria");

		UserProfile userProfile = getUserProfile(request);

		criteria.addFilter(ManageUsersReportData.FILTER_FILTER, ManageUsersReportData.FILTER_CONTRACT_NUMBER);
		criteria.addFilter(ManageUsersReportData.FILTER_VALUE, String.valueOf(userProfile.getCurrentContract().getContractNumber()));

		criteria.addFilter(ManageUsersReportData.FILTER_PRINCIPAL, userProfile.getPrincipal());

		criteria.setPageSize(ReportCriteria.NOLIMIT_PAGE_SIZE);
		criteria.setSearchTPAContacts(true);

		logDebug("exit <- populateReportCriteria");

	}
	@RequestMapping (value="/thirdPartyAdministrator/", method={RequestMethod.GET})
	public String doDefault(@Valid @ModelAttribute("tpaContactsReportForm") TPAContactsReportForm tpaContactsReportForm,BindingResult bindingResult,HttpServletRequest request,
			HttpServletResponse response) throws SystemException{
		
		
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              ControllerForward  forward = new ControllerForward("refresh", "/do" + new UrlPathHelper().getPathWithinServletMapping(request), true); 
					return "redirect:" + forward.getPath(); 
	       }
		}
	   
		String forward=super.doDefault(tpaContactsReportForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}
	
	
	@RequestMapping (value="/thirdPartyAdministrator/",params= {"task=common"}, method={RequestMethod.GET,RequestMethod.POST})
	public String doShow(@Valid @ModelAttribute("tpaContactsReportForm") TPAContactsReportForm tpaContactsReportForm,BindingResult bindingResult,HttpServletRequest request,
			HttpServletResponse response) throws SystemException{
		
		String forward=doCommon(tpaContactsReportForm, request, response);
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
	       }
		}
	  
		
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}
	
	public String doCommon(
			BaseReportForm reportForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		
		logDebug("entry -> doCommon");
		
		TPAContactsReportForm contactsReportForm = (TPAContactsReportForm)reportForm;
		UserProfile userProfile = getUserProfile(request);
		UserInfo loginUserInfo = SecurityServiceDelegate.getInstance().getUserInfo(
				userProfile.getPrincipal());
	    request.setAttribute(Constants.USERINFO_KEY, loginUserInfo);
		Contract currentContract = userProfile.getCurrentContract();
		UserRole principalRole = userProfile.getRole();
		
		// Client user with SelectedAccess permission will not be able to view TPA contacts tab
		if (principalRole.isPlanSponsor() && userProfile.isSelectedAccess()) {
			//return mapping.findForward(Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT);
			CommonMrlLoggingUtil.logUnAuthAcess(request,"User is not authorized",this.getClass().getName()+":"+"processRequest");
			return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
		}
		
		// check if the contract has FR tab display		
		boolean frTabDisplay = SecurityServiceDelegate.getInstance().displayBrokerContactsTab(currentContract.getContractNumber());
		if (frTabDisplay) {
			request.setAttribute("displayBrokerTab", Boolean.TRUE);
		}
		else {
			request.setAttribute("displayBrokerTab", Boolean.FALSE);
		}

		// To retrieve Plan Sponsor comments 
		getContractComment(currentContract.getContractNumber(), request);
		// To retrieve TPA comments
		// Get Firm Id for current contract and then get Firm level comments from CONTACT table
		// TPA firm address is fetched through below service call
		TPAFirmInfo firmInfo = TPAServiceDelegate.getInstance().getFirmInfoByContractId(
				currentContract.getContractNumber());
		
		if(firmInfo == null){
			// If current contract has no associated firm, then redirect to home page
			//return mapping.findForward(Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT);
			return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
		}
		
		contactsReportForm.setTpaFirmId(firmInfo.getId());
		request.setAttribute("tpaFirmInfo", firmInfo);
		
		// To retrieve TPA Firm Comments
		getTpaFirmComment(firmInfo.getId(), contactsReportForm, request);
		
		// call to super class method to get report data
		String forward = super.doCommon( reportForm, request, response);
		
		ReportData reportData = (ManageUsersReportData)request.getAttribute(CommonConstants.REPORT_BEAN);
		Collection<UserInfo> reportDetails = reportData.getDetails();
		
		if(reportDetails == null || reportDetails.size() == 0){
			// set flag to display no active users message
			contactsReportForm.setNoActiveUsers(true);
			
		}else{
			// To filter out non contRact contacts from report data
			if(principalRole.isPlanSponsor() || 
					!StringUtils.equals(CommonConstants.YES,contactsReportForm.getShowAllContactsInd())){
				// For PlanSponsor users, always contract contacts should be displayed
				// For other users, by default only Contract Contacts should be displayed.
				
				Iterator<UserInfo> reportDataIterator = reportDetails.iterator();
				while(reportDataIterator.hasNext()){
					UserInfo userInfo = reportDataIterator.next();
					
					// To display SigningAuthority image in Contacts report
					userInfo.setTpaContactWithSigningAuthority(isSigningAuthority(firmInfo, userInfo));
					
					// To remove non contRact contact
					if(!(userInfo.isTpaContactWithSigningAuthority() 
							|| userInfo.getSignReceivedAuthSigner() 
							|| userInfo.getPrimaryContact())){
					
						reportDataIterator.remove();
					}
					
				}
				
				if(reportDetails == null || reportDetails.size() == 0){
					// After filtering non contRact contacts from report data
					// set flag to display no available users message
					contactsReportForm.setNoAvailableUsers(true);
				}
				
			}else{
				// To display SigningAuthority image in Contacts report
				for(UserInfo userInfo : reportDetails){
					userInfo.setTpaContactWithSigningAuthority(isSigningAuthority(firmInfo, userInfo));
				}
			}
		}
		
		// For navigation purpose, used in ManagePasswordAction
		SessionHelper.setLastVisitedManageUsersPage(request, Constants.FORWARD_TPA_CONTACTS);
		
		// To suppress 'Action' column in Contact list and 'Add Contact' button
		displayActionColumn(userProfile, contactsReportForm, loginUserInfo);
		
		//To retrieve contract passiveTrustee information
		String passiveTrustee = ContractServiceDelegate.getInstance().getPassiveTrustee(currentContract.getContractNumber()); 
		if (StringUtils.equalsIgnoreCase(Constants.NO, passiveTrustee)
				|| StringUtils.isBlank(passiveTrustee)) {
			request.setAttribute("passiveTrustee", Constants.NO_INDICATOR);
		} else {
			request.setAttribute("passiveTrustee", Constants.YES_INDICATOR);
		}
		
		logDebug("exit <- doCommon");
		
		return forward;
	}
	
	/**
	 * To fetch TPA firm comment and store it in a request attribute
	 * 
	 * @param tpaFirmId
	 * @param contactsReportForm
	 * @param request
	 * @throws SystemException
	 */
	private void getTpaFirmComment(int tpaFirmId, TPAContactsReportForm contactsReportForm,
			HttpServletRequest request) throws SystemException {
		logDebug("entry -> getTpaFirmComment");
		
		ContactCommentVO tpaComment = new ContactCommentVO();
		tpaComment.setTpaId(BigDecimal.valueOf(tpaFirmId)); 
		tpaComment.setContactLevelTypeCode(TPA_CONTACT_LEVEL_TYPE_CODE);
		
		tpaComment = ContractServiceDelegate.getInstance().getContactComment(tpaComment);
		contactsReportForm.setTpaComments(tpaComment.getCommentText());
		contactsReportForm.setTpaFirmContactId(tpaComment.getContactId());
		
		// In View mode - Comments will be displayed as HTML text
		// Replacing spaces and line breaks with respective HTML entities
		String tpaCommentText = tpaComment.getCommentText();
		if(!StringUtils.isBlank(tpaCommentText)){
			tpaCommentText = tpaCommentText.replaceAll("  ", "&nbsp;&nbsp;"); 
			tpaCommentText = tpaCommentText.replaceAll("\r\n", "<br>");
			tpaComment.setCommentText(tpaCommentText);
		}
		request.setAttribute("tpaComment", tpaComment);
		
		logDebug("exit <- getTpaFirmComment");
	}
	
	/**
	 * To fetch PlanSponsor comment and store it in a request attribute
	 * 
	 * @param tpaFirmId
	 * @param contactsReportForm
	 * @param request
	 * @throws SystemException
	 */
	private void getContractComment(int contractId, HttpServletRequest request) throws SystemException {
		logDebug("entry -> getContractComment");
		
		ContactCommentVO contractComment = new ContactCommentVO();
		contractComment.setContractId(contractId);
		contractComment.setContactLevelTypeCode(Constants.CONTRACT_CONTACT_LEVEL_TYPE_CODE);
		
		contractComment = ContractServiceDelegate.getInstance().getContactComment(contractComment);
		
		// In View mode - Comments will be displayed as HTML text
		// Replacing spaces and line breaks with respective HTML entities
		String contractCommentText = contractComment.getCommentText();
		if(!StringUtils.isBlank(contractCommentText)){
			contractCommentText = contractCommentText.replaceAll("  ", "&nbsp;&nbsp;");  
			contractCommentText = contractCommentText.replaceAll("\r\n", "<br>");
			contractComment.setCommentText(contractCommentText);
		}
		request.setAttribute("contractComment", contractComment);
		
		logDebug("exit <- getContractComment");
	}
	
	/**
	 * This method decides whether to display Action column or not in Contacts report 
	 * based on UserRole and permission's
	 * 
	 * @param userProfile
	 * @param contactsReportForm
	 * @throws SystemException
	 */
	private void displayActionColumn(UserProfile userProfile, 
			TPAContactsReportForm contactsReportForm, UserInfo loginUserInfo) throws SystemException{
		logDebug("entry -> displayActionColumn");
		UserRole principalRole = userProfile.getRole();
		
		if(principalRole.isPlanSponsor()){
			// Action icons will NOT display for client user
			contactsReportForm.setDisplayActionColumnInd(false);
			
		}else if(principalRole.isInternalUser()){
			// Action icons will NOT display for internal users,
			//	 	Without Manage TPA users permission and
			//		With Internal User Manager or Relationship Manager role
			if(!principalRole.hasPermissionCode(MANAGE_TPA_USERS_PERMISSION_CODE) 
					&& (principalRole instanceof InternalUserManager 
							|| principalRole instanceof RelationshipManager
							|| principalRole instanceof BasicInternalUser
							|| principalRole instanceof BundledGaApprover
							|| principalRole instanceof PFSRelationshipManager)){
				contactsReportForm.setDisplayActionColumnInd(false);
			}
			
		}else if(principalRole.isTPA()){
			// Action icons will NOT display for TPA user without Manage TPA users permission
			try{
				// Need UserInfo object for logged-in user
				//UserInfo userInfo = SecurityServiceDelegate.getInstance().searchByUserName(
				//		userProfile.getPrincipal(), userProfile.getPrincipal().getUserName());
				TPAFirmInfo firmInfo = loginUserInfo.getTpaFirm(contactsReportForm.getTpaFirmId());
				if(firmInfo != null){
					principalRole = firmInfo.getContractPermission().getRole();
					if(!principalRole.hasPermissionCode(MANAGE_TPA_USERS_PERMISSION_CODE)){
						contactsReportForm.setDisplayActionColumnInd(false);
					}
				}else{
					contactsReportForm.setDisplayActionColumnInd(false);
				}
			}catch (Exception exception) {
				throw new SystemException(exception, "SecurityService failed to retrieve UserInfo object for logged-in user. " +
						"Parameters are UserName: " + userProfile.getPrincipal().getUserName()+ 
						"ProfileID: " + userProfile.getPrincipal().getProfileId());
			}
			
		}
		
		logDebug("exit <- displayActionColumn");
	}
	
	/**
	 * To determine whether a Contact has SigningAuthority permission or not.
	 * 		SigningAuthority = yes, under the contRact for both firm and user
	 * 		AND SigningAuthority = yes, at user profile level
	 * 
	 * @param firmInfo
	 * @param userInfo
	 * @return
	 */
	private boolean isSigningAuthority(TPAFirmInfo firmInfo, UserInfo userInfo){

		logDebug("entry -> isSigningAuthority");
		
		boolean isSigningAuthority = false;
		int profileID = Long.valueOf(userInfo.getProfileId()).intValue();
		
		if(firmInfo.getContractPermission() != null &&
				(firmInfo.getUseridToContractPermission() != null &&
						firmInfo.getUseridToContractPermission().get(profileID) != null) &&
				(userInfo.getTpaFirm(firmInfo.getId()) != null && 
						userInfo.getTpaFirm(firmInfo.getId()).getContractPermission() != null)
				){
			
			isSigningAuthority = ( // permission for TPA firm under current contract
					firmInfo.getContractPermission().isSigningAuthority()
					// permission for TPA User under current contract
							&& firmInfo.getUseridToContractPermission().get(profileID).isSigningAuthority()
					// permission for TPA User under TPA Firm(User Profile level)
							&& userInfo.getTpaFirm(firmInfo.getId()).getContractPermission()
									.isSigningAuthority());
			
		}else{
			// If none of the permissions are available for the firm OR the contact under the contRact 
			//		OR for the contact under the firm
			// then, SigningAuthority permission will be false
			isSigningAuthority = false;
		}
			
		logDebug("exit <- isSigningAuthority");
		
		return isSigningAuthority;
	}
	
	/**
	 * To update TPA Firm comments and display TPA contacts tab in view mode
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 */
	
	@RequestMapping (value="/thirdPartyAdministrator/",params= {"task=updateTpaComments"}, method={RequestMethod.POST})
	public String doUpdateTpaComments(@Valid @ModelAttribute("tpaContactsReportForm") TPAContactsReportForm tpaContactsReportForm,BindingResult bindingResult,HttpServletRequest request,
			HttpServletResponse response) throws SystemException{
		
		logDebug("entry -> doUpdateTpaComments");
		
		
		UserProfile userProfile = getUserProfile(request);
		
		ContactInfo contactInfo = new ContactInfo();
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	
		contactInfo.setContactId(tpaContactsReportForm.getTpaFirmContactId());
		contactInfo.setComment(tpaContactsReportForm.getTpaComments());
		contactInfo.setUpdatedUserProfileId(userProfile.getPrincipal().getProfileId());
		contactInfo.setTpaFirmId(BigDecimal.valueOf(tpaContactsReportForm.getTpaFirmId()));
		contactInfo.setContactLevelTypeCode(TPA_CONTACT_LEVEL_TYPE_CODE);
		
		SecurityServiceDelegate.getInstance().updateContactComments(contactInfo);
		
		// To release lock for comment record
		LockServiceDelegate.getInstance().releaseLock(LockHelper.CONTACT_COMMENT_LOCK_NAME,
                LockHelper.CONTACT_COMMENT_LOCK_NAME + tpaContactsReportForm.getTpaFirmContactId());
		
		logDebug("exit <- doUpdateTpaComments");
		
		String forward=doCommon(tpaContactsReportForm, request, response);
		
		
		
	
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}
	
	/**
	 * To display TPA Contacts tab in edit mode 
	 * Also locks the record for editing
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 */
	
	@RequestMapping (value="/thirdPartyAdministrator/",params= {"task=editTpaComments"}, method={RequestMethod.POST})
	public String doEditTpaComments(@Valid @ModelAttribute("tpaContactsReportForm") TPAContactsReportForm tpaContactsReportForm,BindingResult bindingResult,HttpServletRequest request,
			HttpServletResponse response) throws SystemException{
		
		logDebug("exit <- doEditTpaComments");
		
		
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
	       }
		}
	
		// To obtain lock for comment record
		boolean isLockObtained = LockServiceDelegate.getInstance().lock(LockHelper.CONTACT_COMMENT_LOCK_NAME,
                LockHelper.CONTACT_COMMENT_LOCK_NAME + tpaContactsReportForm.getTpaFirmContactId(),
                getUserProfile(request).getPrincipal().getProfileId());
		
		if(!isLockObtained){	// If record is already locked, need to display a error message
			tpaContactsReportForm.setTask("");
			Collection<GenericException> errors = new ArrayList<GenericException>();
			errors.add(new GenericException(ErrorCodes.TPA_FIRM_COMMENT_LOCKED));
			
			SessionHelper.setErrorsInSession(request, errors);
		}
		

		String forward=doCommon(tpaContactsReportForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}
	
	/**
	 * To unlock the record and display page in view mode
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 */
	
	@RequestMapping (value="/thirdPartyAdministrator/",params= {"task=editCommentsCancel"}, method={RequestMethod.POST})
	public String doEditCommentsCancel(@Valid @ModelAttribute("tpaContactsReportForm") TPAContactsReportForm tpaContactsReportForm,BindingResult bindingResult,HttpServletRequest request,
			HttpServletResponse response) throws SystemException{
		
		// To release lock for comment record
		LockServiceDelegate.getInstance().releaseLock(LockHelper.CONTACT_COMMENT_LOCK_NAME,
                LockHelper.CONTACT_COMMENT_LOCK_NAME + tpaContactsReportForm.getTpaFirmContactId());
		
		logDebug("exit <- doEditCommentsCancel");
		
		
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
	       }
		}
		String forward=doCommon(tpaContactsReportForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}
	
	/*
	 * * (non-Javadoc) This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations.
	 * 
	 * @see
	 * com.manulife.pension.platform.web.controller.BaseAction#doValidate(org
	 * .apache.struts.action.ActionMapping,
	 * org.apache.struts.action.Form,javax
	 * .servlet.http.HttpServletRequest)
	 */

	
	
	@RequestMapping(value ="/thirdPartyAdministrator/" ,params={"task=filter"}  , method =  {RequestMethod.GET}) 
	public String doFilter (@Valid @ModelAttribute("tpaContactsReportForm") TPAContactsReportForm tpaContactsReportForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	             return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
	       }
		}
		
       String forward=super.doFilter( tpaContactsReportForm, request, response);
	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	
	@RequestMapping(value = "/thirdPartyAdministrator/", params = {"task=print"}, method = {RequestMethod.GET })
	public String doPrint(@Valid @ModelAttribute("tpaContactsReportForm") TPAContactsReportForm tpaContactsReportForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
			}
		}
		String forward = super.doPrint(tpaContactsReportForm, request, response);
		request.setAttribute("printFriendly", true);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
    
	
	@RequestMapping(value ="/thirdPartyAdministrator/", params={"task=sort"}  , method =  {RequestMethod.GET}) 
	public String doSort (@Valid @ModelAttribute("tpaContactsReportForm") TPAContactsReportForm tpaContactsReportForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	             return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
	       }
		}
       String forward=super.doSort( tpaContactsReportForm, request, response);
	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	/*
	 * * (non-Javadoc) This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations.
	 * 
	 * @see
	 * com.manulife.pension.platform.web.controller.BaseAction#doValidate(org
	 * .apache.struts.action.ActionMapping,
	 * org.apache.struts.action.Form,javax
	 * .servlet.http.HttpServletRequest)
	 */
	@Autowired
	   private PSValidatorFWInput  psValidatorFWInput;

	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind(request);
	    binder.addValidators(psValidatorFWInput);
	}
	
}
