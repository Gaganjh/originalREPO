package com.manulife.pension.ps.web.contacts;


import java.io.IOException;
import java.util.HashMap;

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
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.ContactCommentVO;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.ManageUsersReportData;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.log.LogUtility;

/**
 * <p>
 * Broker contacts report. This report will display the contract addresses,
 * client contacts along with the staging contacts.
 * </p>
 * 
 * @author Balaji Ramakrishnan
 * 
 */


@Controller
@RequestMapping( value = "/contacts")
@SessionAttributes({"brokerContactsForm"})
public class BrokerContactsReportController extends ReportController {
	@ModelAttribute("brokerContactsForm") 
	public BrokerContactsReportForm populateForm() 
	{
		return new BrokerContactsReportForm();
		}
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
	forwards.put("default","/contacts/BrokerContacts.jsp" );
	forwards.put("sort","/contacts/BrokerContacts.jsp" );
	forwards.put("deleteStagingContact","/do/contacts/broker/" );
	forwards.put("editContractComments","/do/contacts/broker/" );
	forwards.put("updateContractComments","/do/contacts/broker/" );
	forwards.put("print","/contacts/BrokerContacts.jsp" );
	forwards.put("input","/contacts/BrokerContacts.jsp" );
	}
	/**
	 * Default constructor
	 * 
	 */
	public BrokerContactsReportController() {
		super(BrokerContactsReportController.class);
	}

	/**
	 * Method getDefaultSort returns the default sort criteria
	 * 
	 * @return last name
	 */
	protected String getDefaultSort() {
		return null;
	}

	/**
	 * Method getDefaultSortDirection returns the default sort direction ASC or
	 * DESC
	 * 
	 * @return Asc direction
	 */
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	/**
	 * Method getDownloadData is used to return byte information of report,
	 * which has null implementation
	 * 
	 * @param reportForm
	 * @param report
	 * @param request
	 * @return
	 * @throws SystemException
	 */
	protected byte[] getDownloadData(ReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {
		return null;
	}

	/**
	 * Method getReportId is used to return report id
	 * 
	 * @return report id
	 */
	protected String getReportId() {
		return ManageUsersReportData.REPORT_ID;
	}

	/**
	 * Method getReportName returns the report name
	 * 
	 * @return report name
	 */
	protected String getReportName() {
		return ManageUsersReportData.REPORT_NAME;
	}

	/**
	 * Populates the report action form
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 */
	protected void populateReportForm(
			BaseReportForm reportForm, HttpServletRequest request) {
		final String methodName = "populateReportForm";
		LogUtility.logEntry(logger, methodName);

		super.populateReportForm( reportForm, request);
		BrokerContactsReportForm theForm = (BrokerContactsReportForm) reportForm;

		UserProfile userProfile = getUserProfile(request);
		theForm.setFilter(ManageUsersReportData.FILTER_CONTRACT_NUMBER);
		Contract contract = userProfile.getCurrentContract();

		if (contract != null) {
			theForm
					.setFilterValue(String
							.valueOf(contract.getContractNumber()));
		} else {
			theForm.setFilterValue("");
		}

		LogUtility.logExit(logger, methodName);
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#populateReportCriteria
	 * com.manulife.pension.ps.web.report.BaseReportForm,
	 * javax.servlet.http.HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request) {
		final String methodName = "populateReportCriteria";
		LogUtility.logEntry(logger, methodName);

		UserProfile userProfile = getUserProfile(request);

		criteria.addFilter(ManageUsersReportData.FILTER_FILTER,
				ManageUsersReportData.FILTER_CONTRACT_NUMBER);
		criteria.addFilter(ManageUsersReportData.FILTER_VALUE, String
				.valueOf(userProfile.getCurrentContract().getContractNumber()));

		criteria.addFilter(ManageUsersReportData.FILTER_PRINCIPAL, userProfile
				.getPrincipal());

		criteria.setSearchBrokerContacts(true);
		criteria.setPageSize(ReportCriteria.NOLIMIT_PAGE_SIZE);

		LogUtility.logExit(logger, methodName);

	}

	/**
	 * Method doCommon will be invoked to do common functionality before
	 * proceeding with corresponding page functionality
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return action forward
	 * @throws SystemException
	 */
	protected String doCommon(
			BaseReportForm reportForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {

		final String methodName = "doCommon";
		LogUtility.logEntry(logger, methodName);

		UserProfile userProfile = getUserProfile(request);
		UserInfo loginUserInfo = SecurityServiceDelegate.getInstance()
				.getUserInfo(userProfile.getPrincipal());
		request.setAttribute(Constants.USERINFO_KEY, loginUserInfo);
		Contract currentContract = userProfile.getCurrentContract();

		final Integer contractId = currentContract.getContractNumber();

		UserInfo userInfo = SecurityServiceDelegate.getInstance().getUserInfo(
				userProfile.getPrincipal());
		request.setAttribute("userInfo", userInfo);

		// check if the contract has TPA firm assigned
		TPAFirmInfo firmInfo = TPAServiceDelegate.getInstance()
				.getFirmInfoByContractId(currentContract.getContractNumber());
		if (firmInfo != null) {
			request.setAttribute("tpaFirmAccessForContract", Boolean.TRUE);
		} else {
			request.setAttribute("tpaFirmAccessForContract", Boolean.FALSE);
		}

		// To retrieve contract comments
		getContractComment(contractId, request);

		// To retrieve contract passiveTrustee information
		String passiveTrustee = ContractServiceDelegate.getInstance()
				.getPassiveTrustee(currentContract.getContractNumber());
		if (StringUtils.equalsIgnoreCase(Constants.NO, passiveTrustee)
				|| StringUtils.isBlank(passiveTrustee)) {
			request.setAttribute("passiveTrustee", Constants.NO_INDICATOR);
		} else {
			request.setAttribute("passiveTrustee", Constants.YES_INDICATOR);
		}

		super.doCommon( reportForm, request, response);

		request.setAttribute("brokerAccessContacts", Boolean.TRUE);
		request.setAttribute("planSponsorAccessContacts", Boolean.FALSE);

		LogUtility.logExit(logger, methodName);

		//return findForward( getTask(request));
		String forward=getTask(request);
		return forward;
	}
	
	/**
	 * To fetch PlanSponsor comment and store it in a request attribute
	 * 
	 * @param contractId
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
	 * Method getDownloadData is used to return byte information of report,
	 * which has null implementation
	 * 
	 * @param reportForm
	 * @param report
	 * @param request
	 * @return
	 * @throws SystemException
	 */
	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {
		return null;
	}

	/**
	 * Method doDefault is invoked when the forward is not specified or
	 * specified as default
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 */
	@RequestMapping(value ="/broker/" , method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("employeeForm") BrokerContactsReportForm theForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		LogUtility.logEntry(logger, "doDefault");
		String forward = doCommon(theForm, request, response);
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		theForm.clear();
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}
	

	@RequestMapping(value ="/broker/" ,params={"task=filter"}  , method =  {RequestMethod.GET}) 
	public String doFilter (@Valid @ModelAttribute("employeeForm") BrokerContactsReportForm theForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
		
       String forward=super.doFilter( theForm, request, response);
	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	@RequestMapping(value = "/broker/", params = {"task=print"}, method = {RequestMethod.GET })
	public String doPrint(@Valid @ModelAttribute("employeeForm") BrokerContactsReportForm theForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");// if
																											// input
			}
		}
		String forward = super.doPrint(theForm, request, response);
		request.setAttribute("printFriendly", true);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
		
	@RequestMapping(value ="/broker/", params={"task=sort"}  , method =  {RequestMethod.GET}) 
	public String doSort (@Valid @ModelAttribute("employeeForm") BrokerContactsReportForm theForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
       String forward=super.doSort( theForm, request, response);
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
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
	
}
