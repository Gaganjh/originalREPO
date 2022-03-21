package com.manulife.pension.bd.web.bob.investment;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import java.util.HashMap;
import javax.validation.Valid;
import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.delegate.AccountServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.delegate.IPSRServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.delegate.LockServiceDelegate;
import com.manulife.pension.platform.web.investment.IPSAndReviewDetailsDataValidator;
import com.manulife.pension.platform.web.investment.IPSManagerUtility;
import com.manulife.pension.ps.service.lock.valueobject.Lock;
import com.manulife.pension.service.account.AccountException;
import com.manulife.pension.service.account.entity.AvailabilityStatus;
import com.manulife.pension.service.environment.util.BusinessParamConstants;
import com.manulife.pension.service.environment.util.ProcessControlConstants;
import com.manulife.pension.service.environment.valueobject.ProcessControlVO;
import com.manulife.pension.service.ipsr.valueobject.IPSRReviewRequest;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.DateRender;

/**
 * Action class for IPS Edit review Results page
 * 
 * @author narintr
 *
 */
@Controller
@RequestMapping(value ="/bob")
@SessionAttributes({"editIPSReviewResultsForm"})

public class EditIPSReviewResultsController extends BaseIPSAndReviewDetailsController{
	@ModelAttribute("editIPSReviewResultsForm") 
	public IPSReviewResultsForm populateForm()
	{
		return new IPSReviewResultsForm();
		}
	public static HashMap<String,String>forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/investment/editIPSReviewResults.jsp");
		forwards.put("default","/investment/editIPSReviewResults.jsp");
		forwards.put("back","redirect:/do/bob/investment/ipsManager/");
		forwards.put("viewIPSReviewResults ","redirect:/do/bob/investment/viewIPSReviewResults/");
		forwards.put("approveIPSReviewResults","redirect:/do/bob/investment/approveIPSReviewResults/");
		forwards.put("homePage","redirect:/do/home/");
		}
	
	public static final String EDIT_IPS_REVIEW_LOCK_NAME = "editIPSReview";
	
	public static final String FRW_HOME_ACTION = "homePage";
	
	public static final String JH_REP_LABEL = "John Hancock Representative";
	
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
	@RequestMapping(value ="/investment/editIPSReviewResults/",  method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("editIPSReviewResultsForm") IPSReviewResultsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		
		String reviewRequestId = request.getParameter("reviewRequestId");
		int reviewId = 0;
		
		if (StringUtils.isNotBlank(reviewRequestId)) {
			reviewId = Integer.parseInt(reviewRequestId);
		} else if (StringUtils.isNotBlank(actionForm.getReviewRequestId())){
			reviewId = Integer.parseInt(actionForm.getReviewRequestId());
		}

		
		BobContext bob = BDSessionHelper.getBobContext(request) ;
		int contractId = bob.getCurrentContract().getContractNumber();
		String contractStatus = bob.getCurrentContract().getStatus();
		
		BDUserProfile bdUserProfile = bob.getUserProfile();
		
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
		
		if (!isValid){
			return forwards.get(FRW_HOME_ACTION);
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
		
		boolean riaDesignationInd = SecurityServiceDelegate.getInstance().hasRia338DesignationForUser(contractId, BigDecimal.valueOf(bdUserProfile.getBDPrincipal().getProfileId()));
		
		// Validation when multiple RIA designated user or Trustee is editing same Contract 
		if (riaDesignationInd && !bdUserProfile.isInMimic()) {
			if (!LockServiceDelegate.getInstance().lock(EDIT_IPS_REVIEW_LOCK_NAME, EDIT_IPS_REVIEW_LOCK_NAME + contractId,
					bdUserProfile.getAbstractPrincipal().getProfileId())) {
				Collection<GenericException> errors = new ArrayList<GenericException>();
				Lock lockInfo = LockServiceDelegate.getInstance()
						.getLockInfo(EDIT_IPS_REVIEW_LOCK_NAME,EDIT_IPS_REVIEW_LOCK_NAME + contractId);
				
				List<String> userInfoBDW = SecurityServiceDelegate.getInstance().getUserInfoForLock(BigDecimal.valueOf(lockInfo.getLockUserProfileId()));
				
				String lockOwnerDisplayName = StringUtils.EMPTY;
				
				if(userInfoBDW != null && !userInfoBDW.isEmpty()){
					lockOwnerDisplayName = userInfoBDW.get(0) + " " + userInfoBDW.get(1); 
				}

				errors.add(new ValidationError(StringUtils.EMPTY,
						BDErrorCodes.IPSM_TRUSTEE_LOCK,
						new String[] { lockOwnerDisplayName }));

				setErrorsInSession(request, errors);

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
					ipsrReviewRequest, bdUserProfile.getAbstractPrincipal().getProfileId(), ipsrServiceDelegate);
		}
		
		// If no Fund Instruction is available for the Review then redirect to Home
		if (actionForm.getIpsReviewFundInstructionList() == null
				|| (actionForm.getIpsReviewFundInstructionList() != null && actionForm
						.getIpsReviewFundInstructionList().isEmpty()) || actionForm.isAllFundInstructionsIgnored()) {
			return forwards.get(FRW_HOME_ACTION);
		}
		 
		// Populate the valid dates that needs to be shown in the web page
		if(ipsrReviewRequest.getExpiryDate() == null){
			ipsrReviewRequest.setExpiryDate(new Date());
		}
		
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
	 * @throws Exception 
	 * @throws AccountException 
	 */
	@RequestMapping(value ="/investment/editIPSReviewResults/" ,params={"action=next"}   , method =  {RequestMethod.POST}) 
	public String doNext (@Valid @ModelAttribute("editIPSReviewResultsForm") IPSReviewResultsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws AccountException, Exception {

		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		
		/*// If the token is not valid then forward to view page
		if(!isTokenValid(request)){
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
	 * Releases the IPSR Edit Lock for RIA designated user and Redirects to IPS Landing page
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
	@RequestMapping(value ="/investment/editIPSReviewResults/", params={"action=back"} , method =  {RequestMethod.POST}) 
	public String doBack (@Valid @ModelAttribute("editIPSReviewResultsForm") IPSReviewResultsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		String forward=preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		
		BobContext bob = BDSessionHelper.getBobContext(request) ;
		int contractId = bob.getCurrentContract().getContractNumber();

		// Release the Edit IPS Lock for Trustee
		LockServiceDelegate.getInstance().releaseLock(EDIT_IPS_REVIEW_LOCK_NAME,
				EDIT_IPS_REVIEW_LOCK_NAME + contractId);

		return forwardToSource( request, actionForm);
	}
	
	/**
	 * Forwards to View Results Page or Landing Page based on corresponding page
	 * 
	 * @param mapping
	 * @param request
	 * @param actionForm
	 * @return
	 */
	private String forwardToSource(
			HttpServletRequest request, AutoForm actionForm) {
		IPSReviewResultsForm ipsAssistServiceForm = (IPSReviewResultsForm) actionForm;
		if (ipsAssistServiceForm.isFromViewPage()) {
			return forwardToViewResults( request, ipsAssistServiceForm);
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
	private String forwardToViewResults(
			HttpServletRequest request, IPSReviewResultsForm ipsAssistServiceForm) {
		ipsAssistServiceForm.setFromViewPage(false);
		return forwards.get(VIEW_REVIEW_ACTION);
	}
	
	/**
	 * This method is used to get the locking user info
	 * @param loggedInUser
	 * @param lockOwnerUserInfo
	 * @return
	 */
	public static String getLockOwnerDisplayName(BDUserProfile loggedInUser) {
        String name = null;
        if (loggedInUser != null) {
                if (!loggedInUser.isInternalUser()) {
                    name = JH_REP_LABEL;
                } else {
                    name = new StringBuffer(loggedInUser.getBDPrincipal().getFirstName()).append(" ")
                        .append(loggedInUser.getBDPrincipal().getLastName()).append(" ")
                        .append(JH_REP_LABEL).toString();
                }
        } else {
            name = JH_REP_LABEL;
        }
        return name;
    }

	/**
	 * avoids token generation as this class acts as intermediate for many
	 * transactions.
	 * 
	 * (non-Javadoc)
	 * @see com.manulife.pension.platform.web.controller.BaseAction#isTokenRequired(java.lang.String)
	 */
	/*@Override
	protected boolean isTokenRequired(String action) {
		return true;
	}*/

	/**
	 * Returns true if token has to be validated for the particular action call
	 * to avoid CSRF vulnerability else false. (non-Javadoc)
	 * 
	 * @see com.manulife.pension.platform.web.controller.BaseController#isTokenValidatorEnabled(java.lang.String)
	 */
	@Override
	protected boolean isTokenValidatorEnabled(String action) {
		// avoids methods from validation which ever is not required
		return StringUtils.isNotEmpty(action)
				&&  ( StringUtils.equalsIgnoreCase(action, "Next")
						|| StringUtils.equalsIgnoreCase(action, "Back"))?true:false;
	} 

}
