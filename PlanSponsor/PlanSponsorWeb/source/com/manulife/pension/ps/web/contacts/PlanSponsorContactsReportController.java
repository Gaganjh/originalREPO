package com.manulife.pension.ps.web.contacts;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
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

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.lock.valueobject.Lock;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.contract.AddressUi;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.LockServiceDelegate;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.ps.web.util.CommonMrlLoggingUtil;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.contract.valueobject.Address;
import com.manulife.pension.service.contract.valueobject.ContactCommentVO;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.PlanData;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.role.AuthorizedSignor;
import com.manulife.pension.service.security.role.Trustee;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.ContactInfo;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.security.valueobject.ManageUsersReportData;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.pension.util.log.ServiceLogRecord;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * <p>
 *	Plansponsor contacts report. 
 *	This report will display the contract addresses, client contacts along with the staging contacts.  
 * </p>
 * @author Ranjith Kumar
 *
 */
@Controller
@RequestMapping( value = "/contacts")
@SessionAttributes({"planSponsorContactsReportForm"})
public class PlanSponsorContactsReportController extends ReportController {
	
	private Category interactionLog = Category.getInstance(ServiceLogRecord.class);
	
	private ServiceLogRecord logRecord = new ServiceLogRecord("PlanSponsorContactsReportAction");
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/contacts/planSponsorContacts.jsp"); 
		forwards.put("default","/contacts/planSponsorContacts.jsp"); 
		forwards.put("sort","/contacts/planSponsorContacts.jsp"); 
		forwards.put("deleteStagingContact","redirect:/do/contacts/planSponsor/"); 
		forwards.put("editContractComments","/contacts/planSponsorContacts.jsp"); 
		forwards.put("updateContractComments","/contacts/planSponsorContacts.jsp"); 
		forwards.put("cancelContractComments","/contacts/planSponsorContacts.jsp"); 
		forwards.put("print","/contacts/planSponsorContacts.jsp"); 
		
	}
	

	@ModelAttribute("planSponsorContactsReportForm") 
	public PlanSponsorContactsReportForm populateForm() 
	{
		return new PlanSponsorContactsReportForm();
		}
	/**
	 * Default constructor
	 *
	 */
	public PlanSponsorContactsReportController() {
		super(PlanSponsorContactsReportController.class);
	}

	/**
	 * Method getDefaultSort returns the default sort criteria
	 * @return last name 
	 */
	protected String getDefaultSort() {
		return ManageUsersReportData.SORT_FIELD_LAST_NAME;
	}

	/**
	 * Method getDefaultSortDirection returns the default sort direction ASC or DESC
	 * @return Asc direction 
	 */
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}
	
	/**
	 * Method getDownloadData is used to return byte information of report, which has null implementation
	 * @param reportForm
	 * @param report
	 * @param request
	 * @return
	 * @throws SystemException
	 */
	protected byte[] getDownloadData(ReportForm reportForm, ReportData report, HttpServletRequest request) throws SystemException {
		return null;
	}

	/**
	 * Method getReportId is used to return report id
	 * @return report id
	 */
	protected String getReportId() {
		return ManageUsersReportData.REPORT_ID;
	}

	/**
	 * Method getReportName returns the report name
	 * @return report name
	 */
	protected String getReportName() {
		return ManageUsersReportData.REPORT_NAME;
	}
	
	/**
	 * Populates the report action form
	 * @param mapping
	 * @param reportForm
	 * @param request 
	 */
	protected void populateReportForm(
			BaseReportForm reportForm, HttpServletRequest request) {
		final String methodName = "populateReportForm";
		LogUtility.logEntry(logger, methodName);

		super.populateReportForm( reportForm, request);
		PlanSponsorContactsReportForm theForm = (PlanSponsorContactsReportForm) reportForm;

		UserProfile userProfile = getUserProfile(request);
		theForm.setFilter(ManageUsersReportData.FILTER_CONTRACT_NUMBER);
		Contract contract = userProfile.getCurrentContract();

		if (contract != null) {
			theForm.setFilterValue(String.valueOf(contract.getContractNumber()));
		} else {
			theForm.setFilterValue("");
		}

		LogUtility.logExit(logger, methodName);
	}
	

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#populateReportCriteria(com.manulife.pension.service.report.valueobject.ReportCriteria,
	 *      com.manulife.pension.ps.web.report.BaseReportForm,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request) {
		final String methodName = "populateReportCriteria";
		LogUtility.logEntry(logger, methodName);

		UserProfile userProfile = getUserProfile(request);

		criteria.addFilter(ManageUsersReportData.FILTER_FILTER, ManageUsersReportData.FILTER_CONTRACT_NUMBER);
		criteria.addFilter(ManageUsersReportData.FILTER_VALUE, String.valueOf(userProfile.getCurrentContract().getContractNumber()));
		
		criteria.addFilter(ManageUsersReportData.FILTER_PRINCIPAL, userProfile.getPrincipal());
		
		criteria.setSearchPSContacts(true);
		criteria.setPageSize(ReportCriteria.NOLIMIT_PAGE_SIZE);

		LogUtility.logExit(logger, methodName);

	}
	
	/**
	 * Method doCommon will be invoked to do common functionality before proceeding with corresponding page functionality
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return action forward
	 * @throws SystemException  
	 */
	@SuppressWarnings("unchecked")
	protected String doCommon(
            BaseReportForm reportForm, HttpServletRequest request,
            HttpServletResponse response) throws
            SystemException {
		
		final String methodName = "doCommon";
		LogUtility.logEntry(logger, methodName);
		
		UserProfile userProfile = getUserProfile(request);
		UserInfo loginUserInfo = SecurityServiceDelegate.getInstance().getUserInfo(
				userProfile.getPrincipal());
	    request.setAttribute(Constants.USERINFO_KEY, loginUserInfo);
		UserRole userRole = userProfile.getRole();
		Contract currentContract = userProfile.getCurrentContract();
		
		//validate whether the role and contract is applicable for PS Tab
		if (userRole.isInternalUser() && currentContract == null) {
			return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
		}
		if (userRole.isPlanSponsor() && userProfile.isSelectedAccess()) {
			CommonMrlLoggingUtil.logUnAuthAcess(request,"User is not authorized",this.getClass().getName()+":"+"processRequest");
			return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
		}
			
		final Integer contractId = currentContract.getContractNumber();
		PlanSponsorContactsReportForm contactsReportForm = (PlanSponsorContactsReportForm)reportForm;
		UserInfo userInfo = SecurityServiceDelegate.getInstance().getUserInfo(
				userProfile.getPrincipal());
		request.setAttribute("userInfo", userInfo);
		
		
		
		// check if the contract has TPA firm assigned		
		TPAFirmInfo firmInfo = TPAServiceDelegate.getInstance().getFirmInfoByContractId(currentContract.getContractNumber());
		if (firmInfo != null) {
			request.setAttribute("tpaFirmAccessForContract", Boolean.TRUE);
		}
		else {
			request.setAttribute("tpaFirmAccessForContract", Boolean.FALSE);
		}
		
		// check if the contract has FR tab display		
		boolean frTabDisplay = SecurityServiceDelegate.getInstance().displayBrokerContactsTab(contractId);
		if (frTabDisplay) {
			request.setAttribute("displayBrokerTab", Boolean.TRUE);
		}
		else {
			request.setAttribute("displayBrokerTab", Boolean.FALSE);
		}
		
		SessionHelper.setLastVisitedManageUsersPage(request, Constants.FORWARD_PLANSPONSOR_CONTACTS);
		
		// To retrieve staging contacts information 
		logDebug(" retrieving Staging Contacts information  ");
		List<ContactInfo> stagingContactList = SecurityServiceDelegate.getInstance().getContractStagingContactList(contractId.intValue());
		
		// To retrieve contract comments 
		ContactCommentVO contractComment = new ContactCommentVO();
		contractComment.setContractId(getUserProfile(request).getCurrentContract().getContractNumber());
		contractComment.setContactLevelTypeCode(Constants.CONTRACT_CONTACT_LEVEL_TYPE_CODE);
		contractComment = ContractServiceDelegate.getInstance().getContactComment(contractComment);
		contactsReportForm.setContractComments(contractComment.getCommentText());
		//To retrieve contract passiveTrustee information
		String passiveTrustee = ContractServiceDelegate.getInstance().getPassiveTrustee(currentContract.getContractNumber()); 
		if (StringUtils.equalsIgnoreCase(Constants.NO, passiveTrustee)
				|| StringUtils.isBlank(passiveTrustee)) {
			request.setAttribute("passiveTrustee", Constants.NO_INDICATOR);
		} else {
			request.setAttribute("passiveTrustee", Constants.YES_INDICATOR);
		}
		
		// Contact address
		Collection<Address> contractAddresses = ContractServiceDelegate
				.getInstance().getContractAddresses(contractId);
		//to suppress line 2 or not
		contactsReportForm.setSuppressLine2(getSuppressLine2(contractAddresses));
		
		super.doCommon( reportForm,	request, response);
		
		if (userRole.isInternalUser()) {
			ManageUsersReportData reportData = (ManageUsersReportData) request
					.getAttribute(Constants.REPORT_BEAN);
			Map<Long, Boolean> deleteMap = reportData.getDeleteMap();
			boolean hasManageExterUsersTrusteeAndAuthSignor = userRole
					.hasPermission(PermissionType.MANAGE_EXTERNAL_USERS_TRUSTEE_AND_AUTH_SIGNOR);

			// now fill in the the delete map;
			try {
				if (reportData.getDetails() != null) {
					logDebug("No of Client Profiles in Report: " + reportData.getDetails().size());

					if (hasManageExterUsersTrusteeAndAuthSignor) {
						Iterator it = reportData.getDetails().iterator();
						while (it.hasNext()) {
							UserInfo uinfo = (UserInfo) it.next();
							deleteMap.put(uinfo.getProfileId(), Boolean.TRUE);
						}
					} else {
						List userInfos = SecurityServiceDelegate.getInstance()
								.searchUserAllContractPermissions(
										currentContract.getContractNumber());
						Iterator it = reportData.getDetails().iterator();
						while (it.hasNext()) {
							UserInfo uinfo = (UserInfo) it.next();
							// need to find out if the current uinfo is a
							// trustee or
							// auth signor on any contracts
							Iterator it2 = userInfos.iterator();
							while (it2.hasNext()) {
								UserInfo uinfo2 = (UserInfo) it2.next();
								if (uinfo.getProfileId() == uinfo2
										.getProfileId()) {
									Iterator it3 = uinfo2
											.getContractPermissionsAsCollection()
											.iterator();
									boolean foundOne = false;
									while (it3.hasNext()) {
										ContractPermission cp = (ContractPermission) it3
												.next();
										if (cp.getRole() instanceof Trustee
												|| cp.getRole() instanceof AuthorizedSignor) {
											foundOne = true;
											break;
										}
									}
									deleteMap.put(uinfo.getProfileId(),
											!foundOne);
									break;
								}
							}
						}
					}
				}
			} catch (SecurityServiceException serviceException) {
				logger.error("Received a Security service exception: ", serviceException);
				List errors = new ArrayList();
				errors.add(new GenericException(Integer.parseInt(serviceException
						.getErrorCode())));
				setErrorsInRequest(request, errors);
				return forwards.get("input");
			}
		}
	
		//request.setAttribute("contractComment", getContactCommentsUI(contractComment));
		setContactCommentsUI(contractComment, request);
		request.setAttribute("contractAddresses", getCollapsedAddresses(contractAddresses));
		request.setAttribute("stagingContactList", stagingContactList);
		
		LogUtility.logExit(logger, methodName);
		
		 String forward = request.getParameter(TASK_KEY);
	        if (forward == null) {
	        	forward = forwards.get("input");;
	        }
		return forward;
	}
	
	
	/**
	 * Sets the comments and last updated user details in request
	 * @param contactComment
	 * @param request
	 */
	private void setContactCommentsUI(ContactCommentVO contactComment, HttpServletRequest request) {
		StringBuffer bufferComment = new StringBuffer(StringUtils.EMPTY);
		String commentText = contactComment.getCommentText();
		if(!StringUtils.isBlank(contactComment.getCommentText())) {
			if(!StringUtils.isBlank(contactComment.getLastUpdatedUserFirstName()) || 
					!StringUtils.isBlank(contactComment.getLastUpdatedUserLastName())) {
				// if last updated profile user lastname or first name available
				if(!StringUtils.isBlank(contactComment.getLastUpdatedUserFirstName())) {
					bufferComment.append(contactComment.getLastUpdatedUserLastName());
				} 
					if(!StringUtils.isEmpty(bufferComment.toString())) {
						bufferComment.append(CommonConstants.SINGLE_SPACE_SYMBOL);
					}
					bufferComment.append(contactComment.getLastUpdatedUserFirstName());
					bufferComment.append(CommonConstants.SINGLE_SPACE_SYMBOL);
			} else {
				// if last updated profile user lastname and firstname not available
				bufferComment.append(contactComment.getLastUpdatedByProfileId());
				bufferComment.append(CommonConstants.SINGLE_SPACE_SYMBOL);				
			}
			
			if(contactComment.getLastUpdatedTS() != null) {
				bufferComment.append(
					DateRender.formatByPattern(new Date(contactComment
							.getLastUpdatedTS().getTime()), "",
							RenderConstants.MEDIUM_MDY_SLASHED));
							//.append(CommonConstants.COLON_SYMBOL);
			}	
			
			// In View mode - Comments will be displayed as HTML text
			// Replacing spaces and line breaks with respective HTML entities
			commentText = contactComment.getCommentText().replaceAll("  ", "&nbsp;&nbsp;");
			commentText = commentText.replaceAll("\r\n", "<br>");
		}
		request.setAttribute("lastUpdatedByUser", bufferComment.toString() );
		request.setAttribute("contractComment", 
				StringUtils.defaultIfEmpty(commentText, StringUtils.EMPTY));
	}
	
	 /**
     * Retrieves the collapsed addresses.
     * 
     * @param addresses The collection of addresses to collapse.
     * @return Collection<AddressUi> The collection of collapsed addresses.
     */
    protected Collection<AddressUi> getCollapsedAddresses(final Collection<Address> addresses) {
    	
        final Collection<AddressUi> collapsedAddresses = new ArrayList<AddressUi>();
        boolean isLegalAddressExist = false;
        for (Address address : addresses) {
        	if(!address.isBlank() && Address.Type.LEGAL.getCode().equals(address.getType().getCode())){
        		isLegalAddressExist = true;
        	}
            boolean exists = false;
            for (AddressUi ui : collapsedAddresses) {

                // If address exists in the collection
                if (ui.equalsIgnoreCase(address) 
                		|| (isLegalAddressExist && address.isBlank() 
               				&& address.getType().getCode().equals(Address.Type.MAILING.getCode()))) {

                    // Modify the type and header
                    ui.setType(Address.Type.COMBINED);
                    ui.setHeader(new StringBuffer(ui.getHeader()).append(AddressUi.SEPARATOR)
                            .append(AddressUi.SPACE).append(
                                    AddressUi.getHeaderForType(address.getType())).toString());

                    // Set flag and break
                    exists = true;
                    break;
                }
            }
            // Check if we found the address, if not add it
            if (!exists) {
                final AddressUi ui = new AddressUi(address);
                ui.setHeader(AddressUi.getHeaderForType(address.getType()));
                collapsedAddresses.add(ui);
            }
        }

        // Fix up the pluralization
        String headerString = null;
        int lastIndex = -1;
        for (AddressUi ui : collapsedAddresses) {
            lastIndex = StringUtils.lastIndexOf(ui.getHeader(), AddressUi.SEPARATOR);
            if(lastIndex != -1) {
                headerString = ui.getHeader();
            	headerString = headerString.substring(0, lastIndex) +  AddressUi.SPACE 
            					+ AddressUi.AND + headerString.substring(lastIndex + 1);
            	ui.setHeader(headerString);
            }
            // Check for no address on record and combined type
            if ((StringUtils.equals(ui.getLine1(), PlanData.NO_ADDRESS_ON_RECORD)  
            		|| StringUtils.isBlank(ui.getLine1()))) {
            	if(ui.getType() == Address.Type.COMBINED) {
            		ui.setLine1(PlanData.NO_ADDRESSES_ON_RECORD);
            	}
            	else {
            		ui.setLine1(PlanData.NO_ADDRESS_ON_RECORD);
            	}
            }
        }
        return collapsedAddresses;
    }

	/**
	 * Method getDownloadData is used to return byte information of report, which has null implementation
	 * @param reportForm
	 * @param report
	 * @param request
	 * @return
	 * @throws SystemException
	 */
	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request) throws SystemException {
		return null;
	}
	
	
	 @RequestMapping(value ="/planSponsor/" ,method =  {RequestMethod.GET,RequestMethod.POST})
	public String doDefault(@Valid @ModelAttribute("planSponsorContactsReportForm") PlanSponsorContactsReportForm reportForm,BindingResult bindingResult,
			HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		
		LogUtility.logEntry(logger, "doDefault");
		reportForm.clear();
		String forward=doCommon(reportForm, request, response);
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

	@RequestMapping(value ="/planSponsor/" ,params={"task=deleteStagingContact"}, method =  {RequestMethod.GET})
	public String doDeleteStagingContact(@Valid @ModelAttribute("planSponsorContactsReportForm") PlanSponsorContactsReportForm reportForm,BindingResult bindingResult,
			HttpServletRequest request,
			HttpServletResponse response) throws SystemException {

		final String methodName = "doDeleteStagingContact";
		LogUtility.logEntry(logger, methodName);
		boolean deletedStagingContact = false;
		String contactId = (String)request.getParameter("contactId");
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
		UserProfile userProfile = getUserProfile(request);
		
		long loggedInUserProfileId = userProfile.getPrincipal().getProfileId();
		
		if(StringUtils.isNotBlank(contactId) && StringUtils.isNumeric(contactId)) {
			Principal principal = userProfile.getPrincipal();
			
			try {
				deletedStagingContact = SecurityServiceDelegate.getInstance().deleteStagingContact(principal, userProfile.getCurrentContract().getContractNumber(), 
					Environment.getInstance().getSiteLocation(), new Integer(contactId).intValue(), new BigDecimal(loggedInUserProfileId));
			}
			catch(SecurityServiceException serviceException){
				ArrayList errors = new ArrayList();
				errors.add(new GenericException(Integer.parseInt(serviceException.getErrorCode())));
				request.setAttribute(Environment.getInstance().getErrorKey(), errors);
			}
			if(deletedStagingContact) {
				logDeleteStagingContact(contactId, userProfile);
			}
		}
		LogUtility.logExit(logger, methodName);		
		String forward=getTask(request);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	@RequestMapping(value ="/planSponsor/" ,params={"task=filter"}  , method =  {RequestMethod.GET}) 
	public String doFilter (@Valid @ModelAttribute("planSponsorContactsReportForm") PlanSponsorContactsReportForm reportForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
		
       String forward=super.doFilter( reportForm, request, response);
	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	@RequestMapping(value = "/planSponsor/", params = {"task=print"}, method = {RequestMethod.GET })
	public String doPrint(@Valid @ModelAttribute("planSponsorContactsReportForm") PlanSponsorContactsReportForm reportForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");// if
			}
		}
		String forward = super.doPrint(reportForm, request, response);
		request.setAttribute("printFriendly", true);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
    
	
	@RequestMapping(value ="/planSponsor/", params={"task=sort"}  , method =  {RequestMethod.GET}) 
	public String doSort (@Valid @ModelAttribute("planSponsorContactsReportForm") PlanSponsorContactsReportForm reportForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
       String forward=super.doSort( reportForm, request, response);
	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	/**
	 * Logs the staging contact delete status to mrl database
	 * @param stagingContactId
	 * @param profileId
	 * @param userProfile
	 */
	private void logDeleteStagingContact(String stagingContactId, UserProfile userProfile) {

		StringBuffer logData = new StringBuffer();

		logData.append("Staging contact id : ");
		logData.append(stagingContactId);
		logData.append(" , ");
		logData.append("contract id : ");
		logData.append(userProfile.getCurrentContract().getContractNumber());
		logData.append(" , ");
		logData.append("Deleted by profile id : ");
		logData.append(userProfile.getPrincipal().getProfileId());

		logWebActivity("Delete staging contact", logData.toString(),
				stagingContactId, userProfile, logger, interactionLog,
				logRecord);
	}

	/**
	 * Log web activity
	 * @param action
	 * @param logData
	 * @param stagingContactId
	 * @param profile
	 * @param logger
	 * @param interactionLog
	 * @param logRecord
	 */
	private static void logWebActivity(String action, String logData,
			String stagingContactId, UserProfile profile, Logger logger,
			Category interactionLog, ServiceLogRecord logRecord) {

		try {
			ServiceLogRecord record = (ServiceLogRecord) logRecord.clone();
			record.setServiceName(logRecord.getServiceName());
			record.setMethodName(action);
			record.setData(logData);
			record.setDate(new Date());
			record.setPrincipalName(profile.getPrincipal().getUserName());
			record.setUserIdentity(profile.getPrincipal().getProfileId()
					+ " : " + profile.getPrincipal().getUserName());
			interactionLog.error(record);
		} catch (CloneNotSupportedException serviceException) {
			// log the error, but don't interrupt regular processing
			logger
					.error("error trying to log a delete staging contact for contact id ["
							+ stagingContactId
							+ "] contract number ["
							+ profile.getCurrentContract().getContractNumber()
							+ "]: " + serviceException);
		}
	}
	
	/**
	 * Display the contract comments for edit and lock the contract comments to edit by other users.
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value ="/planSponsor/" , params={"task=editContractComments"},method =  {RequestMethod.POST})
	public String doEditContractComments(@Valid @ModelAttribute("planSponsorContactsReportForm") PlanSponsorContactsReportForm reportForm,BindingResult bindingResult,
			HttpServletRequest request,
			HttpServletResponse response) throws SystemException {

		final String methodName = "doEditContractComments";
		LogUtility.logEntry(logger, methodName);
		
		PlanSponsorContactsReportForm theForm = (PlanSponsorContactsReportForm) reportForm;
		UserProfile loggedUserProfile = getUserProfile(request);
		Contract currentContract = loggedUserProfile.getCurrentContract();
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
		// To obtain lock for comment record
		boolean isLockObtained = LockServiceDelegate.getInstance().lock(LockHelper.CONTACT_COMMENT_LOCK_NAME,
                LockHelper.CONTACT_COMMENT_LOCK_NAME + currentContract.getContactId(),
                loggedUserProfile.getPrincipal().getProfileId());
		
		if(!isLockObtained){
			theForm.setTask(null); // to forward to default page with warnings
			
			try {
				Lock lockInfo = LockServiceDelegate.getInstance().getLockInfo(
						LockHelper.CONTACT_COMMENT_LOCK_NAME,
						LockHelper.CONTACT_COMMENT_LOCK_NAME
								+ currentContract.getContactId());
				UserInfo lockOwnerUserInfo = SecurityServiceDelegate
						.getInstance().searchByProfileId(
								loggedUserProfile.getPrincipal(),
								lockInfo.getLockUserProfileId());
				String lockOwnerDisplayName = LockHelper
						.getLockOwnerDisplayName(loggedUserProfile,
								lockOwnerUserInfo);
				
				Collection<GenericException> errors = new ArrayList<GenericException>();
				errors.add(new GenericException(ErrorCodes.CONTACT_COMMENT_LOCKED, new String[] {lockOwnerDisplayName}));
				SessionHelper.setErrorsInSession(request, errors);
				
			} catch(SecurityServiceException serviceException) {
				throw new SystemException(
						serviceException,
						"com.manulife.pension.ps.web.contacts.PlanSponsorContactsReportAction",
						"doEditContractComments",
						"Failed to get the lock on contract contact comments "
								+ serviceException.toString());
			}
			
		}
		
		LogUtility.logExit(logger, methodName);
		String forward=doCommon(reportForm, request, response);
		
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}
	
	
	
	
	/**
	 * To cancel the contract comments edit and release the lock for other users to edit
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	
	@RequestMapping(value ="/planSponsor/" , params={"task=cancelContractComments"} ,method =  {RequestMethod.POST})
	public String doCancelContractComments(@Valid @ModelAttribute("planSponsorContactsReportForm") PlanSponsorContactsReportForm reportForm,BindingResult bindingResult,
			HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		final String methodName = "doCancelContractComments";
		LogUtility.logEntry(logger, methodName);
		PlanSponsorContactsReportForm theForm = (PlanSponsorContactsReportForm) reportForm;
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		
		LockServiceDelegate.getInstance().releaseLock(LockHelper.CONTACT_COMMENT_LOCK_NAME,
                LockHelper.CONTACT_COMMENT_LOCK_NAME + currentContract.getContactId());		
		String forward=doCommon(reportForm, request, response);
		theForm.setTask(null);
		
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
		
		
		
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
	
	
	
	@RequestMapping(value ="/planSponsor/" , params={"task=updateContractComments"} ,method =  {RequestMethod.POST})
	public String doUpdateContractComments(@Valid @ModelAttribute("planSponsorContactsReportForm") PlanSponsorContactsReportForm reportForm,BindingResult bindingResult,
			HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
		
		final String methodName = "doUpdateContractComments";
		LogUtility.logEntry(logger, methodName);
		
		PlanSponsorContactsReportForm theForm = (PlanSponsorContactsReportForm) reportForm;
		UserProfile userProfile = getUserProfile(request);
		
		
		int contactId = userProfile.getCurrentContract().getContactId();
		long updatedUserProfileId = userProfile.getPrincipal().getProfileId();
		String commentText = theForm.getContractComments();
		
		ContactInfo contactInfo = new ContactInfo();
		
		contactInfo.setContactId(contactId);
		contactInfo.setComment(commentText);
		contactInfo.setContractId(userProfile.getCurrentContract().getContractNumber());
		contactInfo.setUpdatedUserProfileId(updatedUserProfileId);
		contactInfo.setContactLevelTypeCode(Constants.CONTRACT_CONTACT_LEVEL_TYPE_CODE);
		contactInfo = SecurityServiceDelegate.getInstance().updateContactComments(contactInfo);
		userProfile.getCurrentContract().setContactId(contactInfo.getContactId());
		String forward=doCommon(reportForm, request, response);
		LockServiceDelegate.getInstance().releaseLock(LockHelper.CONTACT_COMMENT_LOCK_NAME,
                LockHelper.CONTACT_COMMENT_LOCK_NAME + contactId);
		
		LogUtility.logExit(logger, methodName);
		
		
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

   /**
     * Queries if the address line 2 row should be suppressed (if all address line 2 elements are
     * blank).
     * 
     * @return boolean - True if address line 2 row should be suppressed.
     */
    private boolean getSuppressLine2(Collection<Address> addressList) {
    	if(addressList != null) {
	        for (Address address : addressList) {
	            if (StringUtils.isNotEmpty(address.getLine2())) {
	                return false;
	            }
	        }
    	}
        // All address line 2 data was blank - suppress the row
        return true;
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
	   private PSValidatorFWDefault  psValidatorFWDefault;
    @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWDefault);
	}
}
