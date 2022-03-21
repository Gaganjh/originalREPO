package com.manulife.pension.ps.web.investment;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
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

import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.delegate.IPSRServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.investment.IPSAndReviewDetailsDataValidator;
import com.manulife.pension.platform.web.investment.IPSManagerUtility;
import com.manulife.pension.ps.service.lock.valueobject.Lock;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.LockServiceDelegate;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.account.AccountException;
import com.manulife.pension.service.account.entity.AvailabilityStatus;
import com.manulife.pension.service.environment.util.BusinessParamConstants;
import com.manulife.pension.service.environment.util.ProcessControlConstants;
import com.manulife.pension.service.environment.valueobject.ProcessControlVO;
import com.manulife.pension.service.ipsr.valueobject.IPSRReviewRequest;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.utility.SecurityConstants;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.DateRender;
import com.manulife.pension.delegate.AccountServiceDelegate;

/**
 * Action class for IPS Edit review Results page
 * 
 * @author Karthik
 *
 */

@Controller
@RequestMapping( value = "/investment")
@SessionAttributes({"iPSReviewResultsForm"})

public class EditIPSReviewResultsController extends BaseIPSAndReviewDetailsController{
	@ModelAttribute("iPSReviewResultsForm") 
	public  IPSReviewResultsForm populateForm() 
	{
		return new  IPSReviewResultsForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/investment/editIPSReviewResults.jsp");
		//forwards.put("default","/investment/editIPSAndReviewDetails.jsp" );
		forwards.put("default","/investment/editIPSReviewResults.jsp");
		forwards.put("back","redirect:/do/investment/ipsManager/");
		forwards.put("viewIPSAndDetails","redirect:/do/investment/ipsManager/");
		forwards.put("viewIPSReviewResults","redirect:/do/investment/viewIPSReviewResults/");
		forwards.put("approveIPSReviewResults","redirect:/do/investment/approveIPSReviewResults/");
		forwards.put("editIPSAndDetails","/investment/editIPSAndReviewDetails.jsp");
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
	@RequestMapping(value ="/editIPSReviewResults/",  method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("iPSReviewResultsForm") IPSReviewResultsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             IPSReviewResultsForm ipsRevievForm = (IPSReviewResultsForm) actionForm;
             ipsRevievForm.setMode(VIEW_MODE);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }
	
		
		
		
		String reviewRequestId = request.getParameter("reviewRequestId");
		int reviewId = 0;
		
		if (StringUtils.isNotBlank(reviewRequestId)) {
			reviewId = Integer.parseInt(reviewRequestId);
		} else if (StringUtils.isNotBlank(actionForm.getReviewRequestId())){
			reviewId = Integer.parseInt(actionForm.getReviewRequestId());
		}

		
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		int contractId = userProfile.getCurrentContract().getContractNumber();
		String contractStatus = userProfile.getCurrentContract().getStatus();
		
		IPSRServiceDelegate ipsrServiceDelegate = IPSRServiceDelegate
				.getInstance();
		IPSRReviewRequest ipsrReviewRequest = ipsrServiceDelegate
				.getIPSRReviewRequestForRequestId(reviewId);
		
		// Check whether the access is valid. If not redirect to home page.
		boolean isValid = true;
		
		// Check whether the review id is valid. Invalid review id is possible
		// when trying to access the page through book mark
		if (ipsrReviewRequest == null
				|| ipsrReviewRequest.getContractId() != contractId) {
			isValid = false;
		} else if (!IPSManagerUtility.isEditAvailable(ipsrReviewRequest
				.getReviewRequestStatus(), ipsrReviewRequest
				.getReviewRequestSubStatus(),contractStatus,actionForm.isAllFundInstructionsIgnored(),contractId)) {
			isValid = false;
		}
	//	isValid = true;
		if (!isValid){
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		
		if (StringUtils.isNotBlank(reviewRequestId)
				&& !StringUtils.equalsIgnoreCase(reviewRequestId, String
						.valueOf(reviewId))){
			actionForm.resetData();
		}
		
		actionForm.setReviewRequestId(String.valueOf(reviewId));
		actionForm.setMode(EDIT_MODE);

		// Get the IPS Review Requests for Contract
		List<IPSRReviewRequest> ipsrReviewRequests = getIPSReviewRequests(contractId);
		
		if (IPSManagerUtility.isCurrentReview(ipsrReviewRequests, reviewId)) {
			actionForm.setCurrentReview(true);
		}
		
		actionForm.setProcessingDateForReportLink(DateRender
				.formatByPattern(ipsrReviewRequest.getProjectedReviewDate(),
						"", MEDIUM_MMMMDDYYYY));
		
		// Validation when multiple Trustee is editing same Contract 
		if (SecurityConstants.TRUSTEE_ID.equals(userProfile.getRole()
				.getRoleId())) {
			if (!LockServiceDelegate.getInstance().lock(
					LockHelper.EDIT_IPS_REVIEW_LOCK_NAME,
					LockHelper.EDIT_IPS_REVIEW_LOCK_NAME + contractId,
					userProfile.getPrincipal().getProfileId())) {
				Collection<GenericException> errors = new ArrayList<GenericException>();
				try {
					Lock lockInfo = LockServiceDelegate.getInstance()
							.getLockInfo(
									LockHelper.EDIT_IPS_REVIEW_LOCK_NAME,
									LockHelper.EDIT_IPS_REVIEW_LOCK_NAME
											+ contractId);
					
					List<String> userInfoBDW = SecurityServiceDelegate.getInstance().getUserInfoForLock(BigDecimal.valueOf(lockInfo.getLockUserProfileId()));
					
					String lockOwnerDisplayName = StringUtils.EMPTY;
					
					if(userInfoBDW == null || userInfoBDW.isEmpty() || userInfoBDW.get(2) == null){
						UserInfo lockOwnerUserInfo = SecurityServiceDelegate
								.getInstance().searchByProfileId(
										userProfile.getPrincipal(),
										lockInfo.getLockUserProfileId());
	
						lockOwnerDisplayName = LockHelper
								.getLockOwnerDisplayName(userProfile,
										lockOwnerUserInfo);
					} else {
						lockOwnerDisplayName = userInfoBDW.get(0) + " " + userInfoBDW.get(1); 
					}
					errors.add(new ValidationError(Constants.EMPTY_STRING,
							ErrorCodes.IPSM_TRUSTEE_LOCK,
							new String[] { lockOwnerDisplayName }));

					SessionHelper.setErrorsInSession(request, errors);

				} catch (SecurityServiceException e) {
					throw new SystemException(
							"com.manulife.pension.ps.web.investment.ViewIPSAndReviewDetailsAction.doDefault "
									+ "Failed to get user info of lock own. "
									+ e.toString());
				}

				return forwards.get(BACK_ACTION);
			}
		}

		// Populates the Fund Instruction of a Review Request Id to the Form
		// Bean. If the request is from approve page or view page then no need
		// to reload the data since the data will be already there in the
		// session
		if (!(actionForm.isFromApprovePage() || actionForm
				.isFromViewPage())) {
			populateIPSReviewFundInstructionToForm(actionForm,
					ipsrReviewRequest, userProfile.getPrincipal()
							.getProfileId(), ipsrServiceDelegate);
			
		}
		
		// If no Fund Instruction is available for the Review then redirect to Home
		if (actionForm.getIpsReviewFundInstructionList() == null
				|| (actionForm.getIpsReviewFundInstructionList() != null && actionForm
						.getIpsReviewFundInstructionList().isEmpty()) || actionForm.isAllFundInstructionsIgnored()||ipsrReviewRequest.getExpiryDate() == null) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		 
		// Populate the valid dates that needs to be shown in the web page
		populateValidIPSIATEffectiveDates(actionForm, ipsrReviewRequest.getExpiryDate());
		
		GregorianCalendar reviewDateCalendar = new GregorianCalendar();
		reviewDateCalendar.setTime(ipsrReviewRequest.getProjectedReviewDate());

		actionForm.setAsOfDate(reviewDateCalendar.getTime());
		
		
		//	if (StringUtils.isNotBlank(isFromViewPage)
		//		&& FALSE.equals(isFromViewPage)) {
			//ipsAssistServiceForm.setFromViewPage(false);
		//}

		actionForm.setFromEditPage(true);
		
		// Clone the form to track the changes
		actionForm.storeClonedForm();
		
		//saveToken(request);
		
		return forwards.get(DEFAULT_ACTION);
	}
	
	/**
	 * Validates the Fund Instructions and forwards to the Approve Confirmation
	 * action
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
	@RequestMapping(value ="/editIPSReviewResults/" ,params={"action=next"}   , method =  {RequestMethod.POST}) 
	public String doNext (@Valid @ModelAttribute("iPSReviewResultsForm") IPSReviewResultsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws AccountException, Exception {

		
		// If the token is not valid then forward to view page
		/*if(!isTokenValid(request)){
			logger.error("Detect double submission");
			return forwards.get(DEFAULT_ACTION);
		}
		
		resetToken(request);*/
		
		
		List<GenericException> errors = new ArrayList<GenericException>();
		
		// Validate the Fund Instruction, whether Action taken for all Fund
		// Instructions
		boolean valid = IPSAndReviewDetailsDataValidator
				.validateFundInstructions(form
						.getIpsReviewFundInstructionList(), errors);
		
		// valid = true;

		if (!valid) {
			IPSAndReviewDetailsDataValidator.addError(errors,
					IPSAndReviewDetailsDataValidator.NO_ACTION_TAKEN);
		}
		
		
		IPSManagerUtility.populateFundInstructionFromPresentation(form
				.getIpsReviewFundInstructionList(), form
				.getFundInstructionList());
		
		boolean isAllFundInstructionsIgnored = IPSManagerUtility
				.isAllFundsInstructionsIgnored(form
						.getFundInstructionList());

		//Validate the IPS IAT Effective Date
		if (!isAllFundInstructionsIgnored
				&& form.isIpsIATEffectiveDateAvailable()) {
			// Validate the IPs effective date for proper format
			IPSAndReviewDetailsDataValidator.validateIPSIATEffectiveDate(
					request, form, errors);
			
			if (errors.isEmpty()) {
				
				// If the format is valid then validate whether the IAT
				// effective date is within the valid range
				Map<String, String> businessParam = EnvironmentServiceDelegate
						.getInstance().getBusinessParamMap();

				int iatEffectiveDateRange = Integer.parseInt(businessParam
						.get(BusinessParamConstants.IAT_EFFECTIVE_DATE_RANGE));

				String iatEffectiveDate = form.getIpsIatEffectiveDate();
				
				Date ipsIatEffectiveDate;
				try {
					ipsIatEffectiveDate = IPSManagerUtility
							.getIATEffectiveDateInDateFormat(iatEffectiveDate);
				} catch (ParseException e) {
					throw new SystemException(e,
							"Exception while parsing the iatEffectiveDate");
				}
				
				int reviewRequestId = Integer.parseInt(form
						.getReviewRequestId());

				IPSRServiceDelegate ipsrServiceDelegate = IPSRServiceDelegate
						.getInstance();
				IPSRReviewRequest ipsrReviewRequest = ipsrServiceDelegate
						.getIPSRReviewRequestForRequestId(reviewRequestId);
				
				AvailabilityStatus availabilityStatus = AccountServiceDelegate 
						.getInstance().getNYSEAvailabilityStatusAsOf(
								ipsIatEffectiveDate, false);

				// Validate whether the IAT effective date is a valid business date
				IPSAndReviewDetailsDataValidator
						.validateIATEffectiveDateFoNonBusinessDate(errors,
								availabilityStatus);
				
				// Validate IPS IAT Effective Date Range and whether IPS IAT
				// Effective Date is Business Date
				IPSAndReviewDetailsDataValidator.validateIATEffectiveDateRange(
						ipsrReviewRequest.getExpiryDate(), ipsIatEffectiveDate,
						iatEffectiveDateRange, errors);
				
				// Retrieve the fund merge date and validate whether the IAT
				// effective date and fund merge date are same. If so return
				// error
				EnvironmentServiceDelegate environmentService = EnvironmentServiceDelegate
						.getInstance();
				ProcessControlVO processControlVO = environmentService
						.getProcessControlData(ProcessControlConstants.NEXT_FUND_MERGE_DATE_CODE);

				if (processControlVO != null) {
					Date fundMergeDate = processControlVO.getDateValue();
					IPSAndReviewDetailsDataValidator.validateIATEffectiveDateForFundMergeDate(
							ipsIatEffectiveDate, fundMergeDate, errors);
				}
			}
		}

		if (!errors.isEmpty()) {
			setErrorsInRequest(request, errors);
			//saveToken(request);
			return forwards.get(DEFAULT_ACTION);
		}

		return forwards.get(APPROVE_CONFIRMATION_ACTION);
	}
	
	/**
	 * Releases the IPSR Edit Lock for Trustee and Redirects to IPS Landing page
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
	@RequestMapping(value ="/editIPSReviewResults/", params={"action=back"} , method =  {RequestMethod.POST}) 
	public String doBack (@Valid @ModelAttribute("iPSReviewResultsForm") IPSReviewResultsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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

		// Release the Edit IPS Lock for Trustee
		LockServiceDelegate.getInstance().releaseLock(LockHelper.EDIT_IPS_REVIEW_LOCK_NAME,
				LockHelper.EDIT_IPS_REVIEW_LOCK_NAME + contractId);

		return forwardToSource(  form, request, response);
	}
	
	/**
	 * Forwards to View Results Page or Landing Page based on corresponding page
	 * 
	 * @param mapping
	 * @param request
	 * @param actionForm
	 * @return
	 */
	private String forwardToSource ( IPSReviewResultsForm form,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
	
		
		if (form.isFromViewPage()) {
			return forwardToViewResults(form, request, response);
		} else {

			return forwards.get(BACK_ACTION);
		}
	}
	
	/**
	 * Forwards View Results Page
	 * 
	 * @param mapping
	 * @param request
	 * @param ipsAssistServiceForm
	 * @return
	 */
	private String forwardToViewResults (IPSReviewResultsForm form, HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException { 
	
		form.setFromViewPage(false);
		return forwards.get(VIEW_REVIEW_ACTION);
	}
	
}
