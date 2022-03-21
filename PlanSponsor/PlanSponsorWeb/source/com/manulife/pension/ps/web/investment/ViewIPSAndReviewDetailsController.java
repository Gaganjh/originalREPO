package com.manulife.pension.ps.web.investment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
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

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.IPSRServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.investment.IPSAndReviewDetailsDataValidator;
import com.manulife.pension.platform.web.investment.IPSAndReviewDetailsForm;
import com.manulife.pension.platform.web.investment.IPSManagerUtility;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWBaseIPSDefault;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.InvestmentPolicyStatementVO;
import com.manulife.pension.service.ipsr.valueobject.IPSRReviewRequest;
import com.manulife.pension.service.security.utility.SecurityConstants;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.util.piechart.PieChartBean;
import com.manulife.util.render.DateRender;

/**
 * Action class for IPS and Review details page
 * 
 * @author thangjo
 *
 */
@Controller
@RequestMapping( value = "/investment")
@SessionAttributes({"ipsAndReviewDetailsForm"})

public class ViewIPSAndReviewDetailsController extends BaseIPSAndReviewDetailsController {

	
	@ModelAttribute("ipsAndReviewDetailsForm") 
	public IPSAndReviewDetailsForm populateIPSAndReviewDetailsForm()
	{
		return new IPSAndReviewDetailsForm();
		
	}

	public static Map<String,String> forwards = new HashMap<>();
	static{
		forwards.put("input","/investment/ipsAndReviewDetails.jsp"); 
		forwards.put("default","/investment/ipsAndReviewDetails.jsp"); 
		forwards.put("editIPSAndDetails","redirect:/do/investment/editIPSManager/" );
		forwards.put("editIPSReviewResults","redirect:/do/investment/editIPSReviewResults/");
		}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.platform.web.controller.BaseAutoAction#doDefault
	 * (org.apache.struts.action.ActionMapping,
	 * com.manulife.pension.platform.web.controller.AutoForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	
	@RequestMapping(value ="/ipsManager/",  method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("ipsAndReviewDetailsForm") IPSAndReviewDetailsForm ipsAssistServiceForm,BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(ipsAssistServiceForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("default");
			}
		}
		// Remove the IPS review results form bean from session
		removeIPSResultsFormFromSession(request);
		
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		int contractId = userProfile.getCurrentContract().getContractNumber();
		String contractStatus = userProfile.getCurrentContract().getStatus();
		ContractServiceDelegate delegate = ContractServiceDelegate
				.getInstance();
		
		InvestmentPolicyStatementVO investmentPolicyStatement = delegate.getIpsBaseData(contractId);
		
		if(investmentPolicyStatement == null) {
			return forwards.get(PSW_HOME_ACTION);
		}

		// Get the IPS criteria and description from db
		Map<String, String> ipsCriteriaDescMap = delegate.getIpsFundMetrics();

		// Populate the form bean with necessary data for display
		ipsAssistServiceForm.setMode(VIEW_MODE);
		populateFormBean(contractId, ipsAssistServiceForm, ipsCriteriaDescMap);

		// ipsAssistServiceForm.setSaveSuccess(request.getAttribute(SUCCESS_IND));

		// Create a pieChartBean with the weightings percentage and set it to
		// the request
		PieChartBean pieChart = createPieChartBean(ipsAssistServiceForm
				.getCriteriaAndWeighting(), Constants.ipsColorCode);
		request.setAttribute(Constants.IPSR_CRITERIA_WEIGHTING_PIECHART,
				pieChart);

		// Set the IPS criteria history in request to render it while doing
		// hover over
		InvestmentPolicyStatementVO investmentPolicyStatementVO = delegate
				.getIPSCriteria(contractId);

		// Set the first name and last name of the last changed user to the vo
		UserInfo userInfo = getLastChangedUserDetail(
				investmentPolicyStatementVO.getCreatedUserId(),
				investmentPolicyStatementVO.getCreatedUserIdType());
		if (userInfo != null) {
			investmentPolicyStatementVO.setCreatedUserFirstName(userInfo
					.getFirstName());
			investmentPolicyStatementVO.setCreatedUserLastName(userInfo
					.getLastName());
		}
		request.setAttribute(CHANGE_HISTORY, investmentPolicyStatementVO);

		// Change criteria and weighting link should only be visible to trustee
		// and Internal users
		boolean isEditLinkAccesible = false;
		if (userProfile.isInternalUser()
				|| SecurityConstants.TRUSTEE_ID.equals(userProfile.getRole()
						.getRoleId())) {
			isEditLinkAccesible = true;
		}
		ipsAssistServiceForm.setEditLinkAccessible(isEditLinkAccesible);

		// Get the IPS Review Requests for Contract
		List<IPSRReviewRequest> ipsReviewRequestList = getIPSReviewRequests(contractId);

		// Check whether there is access to the edit criteria and weightings page
		if (ipsReviewRequestList != null && !ipsReviewRequestList.isEmpty()) {
			if(!IPSAndReviewDetailsDataValidator.isInvalidcontract(contractStatus)){
				if (IPSManagerUtility
						.isEditIPSManagerNotAvailable(ipsReviewRequestList)) {
					try {
						Content message = ContentCacheManager
								.getInstance()
								.getContentById(
										ContentConstants.MESSAGE_IPS_REVIEW_INPROCESS_WHEN_CHANGING_SERVICE_DATE,
										ContentTypeManager.instance().MISCELLANEOUS);
	
						String contents = ContentUtility.getContentAttribute(
								message, "text");
	
						request.setAttribute(
										com.manulife.pension.ps.web.Constants.IPS_SERVICE_DATE_CHANGE_NOT_AVAILABLE_TEXT,
										StringUtils.trim(contents));
					} catch (ContentException exp) {
						throw new SystemException(exp, "Something wrong with CMA");
					}		
				}
			}
		}
		
		// Set the form for IPS Review Report Details
		populateIPSReviewDetailsToForm(ipsReviewRequestList,
				ipsAssistServiceForm, contractId, isEditLinkAccesible,
				contractStatus, userProfile.getCurrentContract()
						.getCompanyName());

		if (ipsAssistServiceForm.isSaveSuccess()) {

			request.setAttribute(SUCCESS_IND, true);
			ipsAssistServiceForm.setSaveSuccess(false);
			ipsAssistServiceForm.setDateChanged(false);
		}
		
		return forwards.get(DEFAULT_ACTION);
	}
	
	/**
	 * Validates the contract status and forwards to the edit action
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
	@RequestMapping(value ="/ipsManager/",params = {"action=edit"},  method = {RequestMethod.GET}) 
	public String doEdit(@Valid @ModelAttribute("ipsAndReviewDetailsForm") IPSAndReviewDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("default");
			}
		}
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		String contractStatus = userProfile.getCurrentContract().getStatus();
		List<GenericException> errors = new ArrayList<GenericException>();

		if (IPSAndReviewDetailsDataValidator.isInvalidcontract(contractStatus)) {
			IPSAndReviewDetailsDataValidator.addError(errors,
					IPSAndReviewDetailsDataValidator.NO_ACCESS_ERROR);
			SessionHelper.setErrorsInSession(request, errors);

			return doDefault(actionForm,bindingResult, request, response);
		}
		
		return forwards.get(EDIT_IPS_AND_DETAILS_PAGE);
	}
	
	/**
	 * This method is used to generate interim review report
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws SystemException
	 */
	
	@RequestMapping(value ="/ipsManager/", params = {"action=printInterimReport"}, method =  {RequestMethod.GET}) 
	public String doPrintInterimReport(@Valid @ModelAttribute("ipsAndReviewDetailsForm") IPSAndReviewDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("default");
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Inside doPrintInterimReport");
		}
		
		
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		
		Contract contract = userProfile.getCurrentContract();
		int contractId = contract.getContractNumber();
		
		IPSRServiceDelegate delegate = IPSRServiceDelegate.getInstance();
		
		// Creates Interim Report
		byte[] interimReport = delegate.generateInterimRport(contractId);
		
		ByteArrayOutputStream pdfOutStream=new ByteArrayOutputStream();
		
		try {
			
			pdfOutStream.write(interimReport);
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
	        response.setHeader("Pragma", "no-cache");
			response.setContentType("application/pdf");
			String filename = "IPSM-Interim-"
					+ DateRender.formatByPattern(new Date(),
							Constants.EMPTY_STRING,
							CommonConstants.MEDIUM_MDY_DASHED) + "-"
					+ userProfile.getCurrentContract().getCompanyName().trim()
					+ ".pdf";
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ filename + "\"");
			response.setContentLength(pdfOutStream.size());
			ServletOutputStream sos = response.getOutputStream();
			pdfOutStream.writeTo(sos);
			sos.flush();
			
		} catch (IOException ioException) {
			throw new SystemException(ioException, "Exception writing pdfData.");
		} finally {
			try {
				response.getOutputStream().close();
			} catch (IOException ioException) {
				throw new SystemException(ioException,
						"Exception writing pdfData.");
			}
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Exiting doPrintInterimReport");
		}
		return null;
	}
	
	/**
	 * This method is used to generate Annual Review Report
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws SystemException
	 * @throws IOException 
	 */
	
	
	@RequestMapping(value ="/ipsManager/", params = {"action=generateReviewReport"}, method =  {RequestMethod.GET}) 
	public String doGenerateReviewReport(@Valid @ModelAttribute("ipsAndReviewDetailsForm") IPSAndReviewDetailsForm actionForm,ModelMap model, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("default");
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Inside doGenerateReviewReport");
		}
		
		String reviewRequestId = request.getParameter("reviewRequestId");
		
		if (reviewRequestId == null) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		Contract contract = userProfile.getCurrentContract();
		
		Collection<GenericException> errors = new ArrayList<GenericException>();
		
		populateReviewReport(request, contract, response, errors);
		
		if (!errors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, errors);
			 forward=doDefault(actionForm, bindingResult, request, response);
			return  forwards.get(forward)!=null?forwards.get(forward):forwards.get("input");
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Exiting doGenerateReviewReport");
		}
		return null;
	}
	
	@Autowired
    private PSValidatorFWBaseIPSDefault psValidatorFWBaseIPSDefault;  

	@InitBinder
	protected void initBinder(HttpServletRequest request,
				ServletRequestDataBinder  binder) {
		binder.bind( request);
		binder.addValidators(psValidatorFWBaseIPSDefault);
	}

}
