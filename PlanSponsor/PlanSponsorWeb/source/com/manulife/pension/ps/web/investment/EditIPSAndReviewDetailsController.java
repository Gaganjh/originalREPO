package com.manulife.pension.ps.web.investment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.IPSRServiceDelegate;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.investment.IPSAndReviewDetailsDataValidator;
import com.manulife.pension.platform.web.investment.IPSAndReviewDetailsForm;
import com.manulife.pension.platform.web.investment.IPSManagerUtility;
import com.manulife.pension.platform.web.investment.valueobject.CriteriaAndWeightingPresentation;
import com.manulife.pension.platform.web.util.DataValidationHelper;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.transaction.TransactionHistoryReportForm;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.contract.valueobject.DayOfYear;
import com.manulife.pension.service.contract.valueobject.InvestmentPolicyStatementVO;
import com.manulife.pension.service.ipsr.valueobject.IPSRReviewRequest;
import com.manulife.pension.service.security.utility.SecurityConstants;
import com.manulife.pension.util.content.GenericException;
import com.manulife.util.piechart.PieChartBean;

@Controller
@RequestMapping( value = "/investment")
@SessionAttributes({"iPSAndReviewForm"})


public class EditIPSAndReviewDetailsController extends BaseIPSAndReviewDetailsController {

	@ModelAttribute("iPSAndReviewForm") 
	public IPSAndReviewForm populateForm()
	{ 
		return new IPSAndReviewForm();
		
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		
		forwards.put("input","/investment/editIPSAndReviewDetails.jsp");
		forwards.put("default","/investment/editIPSAndReviewDetails.jsp");
	    forwards.put("viewIPSAndDetails","redirect:/do/investment/ipsManager/");
	    forwards.put("editIPSAndDetails","/investment/editIPSAndReviewDetails.jsp");
	}
	
	 @RequestMapping(value = "/editIPSManager/",  method =  {RequestMethod.GET}) 
		public String doDefault(@Valid @ModelAttribute("iPSAndReviewForm") IPSAndReviewForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException { 
	    	if(bindingResult.hasErrors()){
	 	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	 	       if(errDirect!=null){
	 	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	 	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	 	       }
	 		}
		
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		int contractId = userProfile.getCurrentContract().getContractNumber();
		
		InvestmentPolicyStatementVO ipsBaseData = ContractServiceDelegate
				.getInstance().getIpsBaseData(contractId);

		// if there is no IPS service Data then forward to home page
		if (ipsBaseData == null) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		
		// if the IPS service is not turned on then forward to home page
		if(ipsBaseData != null && !ipsBaseData.isIpsAvailable()) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		
		List<IPSRReviewRequest> ipsReviewRequestList = getIPSReviewRequests(contractId);
		
		// When Edit Manager is Book marked redirect to Home page
		// when Service Date is not available to Edit
		if (ipsReviewRequestList != null && !ipsReviewRequestList.isEmpty()) {
			if (IPSManagerUtility
					.isEditIPSManagerNotAvailable(ipsReviewRequestList)) {
				return Constants.HOMEPAGE_FINDER_FORWARD;
			}
		}
		
		// Get the IPS criteria and description from db
		Map<String, String> ipsCriteriaDescMap = ContractServiceDelegate
				.getInstance().getIpsFundMetrics();
		
		// Load the criteria and description to session to display in drop down
		Map<String, String> criteriaDescriptionMap = new LinkedHashMap<String, String>();
		criteriaDescriptionMap.put("", SPLITTER_TEXT);
		criteriaDescriptionMap.putAll(ipsCriteriaDescMap);
		request.getSession(false).setAttribute(Constants.IPSR_CRITERIA_DESC, criteriaDescriptionMap);
		
		form.setMode(EDIT_MODE);
		
		// Populate the form bean with necessary data for display
		populateFormBean(userProfile.getCurrentContract().getContractNumber(),
				form, ipsCriteriaDescMap);

		// Create a pieChartBean with the weighting percentage
		PieChartBean pieChart = createPieChartBean(form
				.getCriteriaAndWeighting(), Constants.ipsColorCode);
		request.getSession(false).setAttribute(Constants.IPSR_CRITERIA_WEIGHTING_PIECHART, pieChart);
		
		boolean isNewServiceDateAvailable= true;
		boolean isInitialNotificationSent = false;
		
		for (IPSRReviewRequest ipsrReviewRequest : ipsReviewRequestList) {
			String reviewRequestStatus = ipsrReviewRequest
					.getReviewRequestStatus() != null ? ipsrReviewRequest
					.getReviewRequestStatus().trim() : Constants.EMPTY_STRING;
			String reviewRequestSubStatus = ipsrReviewRequest
					.getReviewRequestSubStatus() != null ? ipsrReviewRequest
					.getReviewRequestSubStatus().trim()
					: Constants.EMPTY_STRING;

			if (com.manulife.pension.service.contract.util.Constants.IPSR_INITIAL_STATUS
					.equals(reviewRequestStatus)
					&& com.manulife.pension.service.contract.util.Constants.IPSR_INITIAL_SUB_STATUS
							.equals(reviewRequestSubStatus)) {
				// Condition to check whether to show New Service Date in
				// Edit
				// Manager page
				Date processingDate = ipsrReviewRequest
						.getProjectedReviewDate();

				GregorianCalendar currentDateCal = new GregorianCalendar();
				currentDateCal = resetTheTime(currentDateCal);

				GregorianCalendar processingDateCal = new GregorianCalendar();
				processingDateCal.setTime(processingDate);

				GregorianCalendar twoDaysBeforeProcessingDateCal = new GregorianCalendar();
				twoDaysBeforeProcessingDateCal.setTime(processingDate);
				twoDaysBeforeProcessingDateCal.add(Calendar.DAY_OF_MONTH, -2);
				twoDaysBeforeProcessingDateCal = resetTheTime(twoDaysBeforeProcessingDateCal);

				GregorianCalendar twoDaysAfterProcessingDateCal = new GregorianCalendar();
				twoDaysAfterProcessingDateCal.setTime(processingDate);
				twoDaysAfterProcessingDateCal.add(Calendar.DAY_OF_MONTH, 2);
				twoDaysAfterProcessingDateCal = resetTheTime(twoDaysAfterProcessingDateCal);

				if (currentDateCal.before(twoDaysBeforeProcessingDateCal)
						|| currentDateCal.after(twoDaysAfterProcessingDateCal)) {
					isNewServiceDateAvailable = true;
				} else {
					isNewServiceDateAvailable = false;
					break;
				}
				
				if(ipsrReviewRequest.getNotificationSentDate() != null) {
					isInitialNotificationSent = true;
				}				
			} else if (com.manulife.pension.service.contract.util.Constants.IPSR_PENDING_ANALYSIS_STATUS
					.equals(reviewRequestStatus)
					&& com.manulife.pension.service.contract.util.Constants.IPSR_PENDING_ANALYSIS_SUB_STATUS
							.equals(reviewRequestSubStatus)) {
				isNewServiceDateAvailable = false;
				break;
			} else if ((com.manulife.pension.service.contract.util.Constants.IPSR_PENDING_REPORT_GENERATION_STATUS
					.equals(reviewRequestStatus) && com.manulife.pension.service.contract.util.Constants.IPSR_PENDING_REPORT_GENERATION_SUB_STATUS
					.equals(reviewRequestSubStatus))) {
				isNewServiceDateAvailable = false;
				break;
			} else if (com.manulife.pension.service.contract.util.Constants.IPSR_PENDING_APPROVAL_STATUS
					.equals(reviewRequestStatus)
					&& com.manulife.pension.service.contract.util.Constants.IPSR_PENDING_APPROVAL_SUB_STATUS
							.equals(reviewRequestSubStatus)) {
				isNewServiceDateAvailable = false;
				break;
			} else if (com.manulife.pension.service.contract.util.Constants.IPSR_PENDING_COMPLETION_STATUS
					.equals(reviewRequestStatus)
					&& com.manulife.pension.service.contract.util.Constants.IPSR_PENDING_COMPLETION_SUB_STATUS
							.equals(reviewRequestSubStatus)) {
				isNewServiceDateAvailable = false;
				break;
			}
		}

		// Show the New Service Date in Edit IPS Manager page if it satisfies
		// the corresponding Review Status
		if (isNewServiceDateAvailable || ipsReviewRequestList.isEmpty()) {
			form.setNewAnnualReviewDateAvailable(true);
		} else {
			form.setNewAnnualReviewDateAvailable(false);
		}
		
		// Load the Months to session to display in drop down
		// for New Annual Review Month
		Map<String, String> monthMap = loadMonths();
		
		request.getSession(false).setAttribute(
				Constants.IPSR_NEW_ANNUAL_REVIEW_MONTH, monthMap);

		// Load the Dates to session to display in drop down
		// for New Annual Review Date
		Map<String, String> dateMap = new LinkedHashMap<String, String>();

		for (int i = 1; i <= 31; i++) {
			String value = Integer.toString(i);
			dateMap.put(value, value);
		}

		request.getSession(false).setAttribute(
				Constants.IPSR_NEW_ANNUAL_REVIEW_DATE, dateMap);

		form.setInitialNotificationSent(isInitialNotificationSent);
		
		// Clone the form to track the changes
		form.storeClonedForm();
		
		
		form.setNewServiceDateConfirmationText(null);
		
		//saveToken(request);
		
		return forwards.get(EDIT_IPS_AND_DETAILS_PAGE);
	}
	
	/**
	 * Save the data after validation
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
	 @RequestMapping(value = "/editIPSManager/", params= {"action=save"}, method =  {RequestMethod.POST}) 
		public String doSave(@Valid @ModelAttribute("iPSAndReviewForm") IPSAndReviewForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException { 
	    	if(bindingResult.hasErrors()){
	 	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	 	       if(errDirect!=null){
	 	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	 	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	 	       }
	 		}
	 
	
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		
		InvestmentPolicyStatementVO investmentPolicyStatementVO = new InvestmentPolicyStatementVO();
		Map<String, Integer> investmentPolicyStatementCriteria = new HashMap<String, Integer>();
		
		// If the token is not valid then forward to view page
		/*if(!isTokenValid(request)){
			logger.error("Detect double submission");
			return mapping.findForward(VIEW_IPS_AND_DETAILS_PAGE);
		}*/
		
		//resetToken(request);
		
		Collection<GenericException> errors = validateEditDetails( actionForm, request);
		
		if (!errors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(EDIT_IPS_AND_DETAILS_PAGE);
		}
		
		int contractId = userProfile.getCurrentContract().getContractNumber();
		
		ContractServiceDelegate delegate = ContractServiceDelegate
				.getInstance();
		
		// Save the data only if the logged user is Trustee otherwise forward to
		// view page
		// Save the data only if there is any change to the form
		if (SecurityConstants.TRUSTEE_ID.equals(userProfile.getPrincipal()
				.getRole().getRoleId())
				&& (actionForm.isFormChanged() && actionForm
						.isCriteriaChanged())) {

			// Populate the investmentPolicyStatementVO with selected
			// criteria and weighting
			for (CriteriaAndWeightingPresentation criteriaAndWeighting : actionForm
					.getCriteriaAndWeightingPresentationList()) {
				if (!(DataValidationHelper.isBlankOrNull(criteriaAndWeighting
						.getCriteriaCode()) || DataValidationHelper
						.isBlankOrNull(criteriaAndWeighting.getWeighting()))) {
					investmentPolicyStatementCriteria.put(criteriaAndWeighting
							.getCriteriaCode(), Integer
							.parseInt(criteriaAndWeighting.getWeighting()));
				}
			}

			investmentPolicyStatementVO
					.setInvestmentPolicyStatementCriteria(investmentPolicyStatementCriteria);
			investmentPolicyStatementVO.setIpsEffectiveDate(new Date());
			investmentPolicyStatementVO.setContractId(contractId);
			investmentPolicyStatementVO.setCreatedUserId(Long
					.toString(userProfile.getPrincipal().getProfileId()));
			investmentPolicyStatementVO
					.setCreatedUserIdType(getGetUserIdType(userProfile));
			investmentPolicyStatementVO
					.setCreatedSourceChannelCode(GlobalConstants.PSW_APPLICATION_ID);
			investmentPolicyStatementVO.setIpsAvailable(actionForm
					.isiPSAssistServiceAvailable());

			// Update the criteria and weighting
			delegate.updateIPSWeightingHist(investmentPolicyStatementVO);

			// If the save is successful then show the success message in
			// view
			// page
			actionForm.setSaveSuccess(true);
			request.setAttribute(SUCCESS_IND, true);
			request.getSession().setAttribute(SUCCESS_IND, true);
			
			actionForm.setCriteriaChanged(false);
		}

		if (SecurityConstants.TRUSTEE_ID.equals(userProfile.getPrincipal()
				.getRole().getRoleId())) {
			
			if (actionForm.isFormChanged()
					&& actionForm.isDateChanged()
					&& StringUtils.isNotBlank(actionForm
							.getNewAnnualReviewMonth())
					&& StringUtils.isNotBlank(actionForm
							.getNewAnnualReviewDate())
					&& actionForm.isNewAnnualReviewDateAvailable()) {
				// Update the New Service Date selected by a Trustee
				int newMonth = Integer.parseInt(actionForm
						.getNewAnnualReviewMonth());
				int newDate = Integer.parseInt(actionForm
						.getNewAnnualReviewDate());
				InvestmentPolicyStatementVO invPolicyDao = new InvestmentPolicyStatementVO();
				invPolicyDao.setAnnualReviewDate(new DayOfYear(newMonth,
						newDate));

				InvestmentPolicyStatementVO ipsBaseData = delegate
						.getIpsBaseData(contractId);

				ipsBaseData
						.setAnnualReviewDate(new DayOfYear(newMonth, newDate));
				// Update the New Service Date
				delegate.updateIPSAnnualReviewDate(ipsBaseData);

				// Update the Review Request Status to Void if there is a
				// existing Review with Initial Status

				IPSRServiceDelegate ipsrServiceDelegate = IPSRServiceDelegate
						.getInstance();

				List<IPSRReviewRequest> ipsrIntialRequests = ipsrServiceDelegate
						.getIPSRReviewRequest(
								contractId,
								com.manulife.pension.service.contract.util.Constants.IPSR_INITIAL_STATUS,
								com.manulife.pension.service.contract.util.Constants.IPSR_INITIAL_SUB_STATUS);

				for (IPSRReviewRequest ipsrReviewRequest : ipsrIntialRequests) {
					ipsrReviewRequest = ipsrServiceDelegate
							.sendToVoid(ipsrReviewRequest);
					createStatusChangeEvent(ipsrReviewRequest);
				}
				// If the save is successful then show the success message in
				// view page
				actionForm.setSaveSuccess(true);
			}
		}
		
		// Redirect to view page after saving the data
		return forwards.get(VIEW_IPS_AND_DETAILS_PAGE);
	}
	
	/**
	 * Cancel action - Redirected to view page
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
	 
	 
	 @RequestMapping(value = "/editIPSManager/", params= {"action=cancel"}, method =  {RequestMethod.POST}) 
		public String doCancel(@Valid @ModelAttribute("iPSAndReviewForm") IPSAndReviewForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException { 
	    	if(bindingResult.hasErrors()){
	 	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	 	       if(errDirect!=null){
	 	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	 	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	 	       }
	 		}	 
	
		return forwards.get(VIEW_IPS_AND_DETAILS_PAGE);
	}
	
	private Collection<GenericException> validateEditDetails( ActionForm form,
			HttpServletRequest request) {

		List<GenericException> errors = new ArrayList<GenericException>();
		IPSAndReviewForm ipsAssistServiceForm = (IPSAndReviewForm) form;
		
		// Do validation only if the action is save and the page is in edit mode
		if (SAVE_ACTION.equals(ipsAssistServiceForm.getAction())) {
			IPSAndReviewDetailsDataValidator.validate(ipsAssistServiceForm,
					errors);

			UserProfile userProfile = SessionHelper.getUserProfile(request);
			int contractId = userProfile.getCurrentContract()
					.getContractNumber();
			IPSRServiceDelegate ipsrServiceDelegate = IPSRServiceDelegate
					.getInstance();
			try {
				List<IPSRReviewRequest> ipsrReviewRequests = ipsrServiceDelegate
						.getIPSRReviewRequest(
								contractId,
								com.manulife.pension.service.contract.util.Constants.IPSR_PARTIALLY_COMPLETE_STATUS,
								com.manulife.pension.service.contract.util.Constants.IPSR_PARTIALLY_COMPLETE_SUB_STATUS);
				// Show error message if there is a review with Partially
				// Complete status
				if (ipsrReviewRequests != null
						&& ipsrReviewRequests.size() != 0) {
					IPSAndReviewDetailsDataValidator
							.addError(
									errors,
									IPSAndReviewDetailsDataValidator.IPS_PC_REVIEW_EXISTS_ERROR);
				}
			} catch (SystemException systemException) {
				throw ExceptionHandlerUtility.wrap(systemException);
			}

			ipsAssistServiceForm.setMode(EDIT_MODE);
		}
		
		return errors;
	}
	
}
