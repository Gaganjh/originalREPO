package com.manulife.pension.ps.web.investment;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
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

import com.manulife.pension.delegate.IPSRServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.investment.IPSManagerUtility;
import com.manulife.pension.ps.service.lock.valueobject.Lock;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.LockServiceDelegate;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWBaseIPSDefault;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.ipsr.valueobject.IPSRReviewRequest;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.utility.SecurityConstants;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;



/**
 * Action class for IPS View Review Results Page
 * 
 * @author Karthik
 *
 */
@Controller
@RequestMapping( value = "/investment")
@SessionAttributes({"iPSReviewResultsForm"})

public class ViewIPSReviewResultsController extends BaseIPSAndReviewDetailsController {

	@ModelAttribute("iPSReviewResultsForm") public  IPSReviewResultsForm populateForm() 
	{
		return new  IPSReviewResultsForm();
	}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
	forwards.put("input","/investment/viewIPSReviewResults.jsp");
	forwards.put("default","/investment/viewIPSReviewResults.jsp");
	forwards.put("back","redirect:/do/investment/ipsManager/");
	forwards.put("viewIPSReviewResults","redirect:/do/investment/viewIPSReviewResults/");
	forwards.put("approveIPSReviewResults","redirect:/do/investment/approveIPSReviewResults/");
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
	
	
	@RequestMapping(value ="/viewIPSReviewResults/",  method =  {RequestMethod.GET,RequestMethod.POST}) 
	public String doDefault(@Valid @ModelAttribute("iPSReviewResultsForm") IPSReviewResultsForm actionForm,BindingResult bindingResult,ModelMap model,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		
		String contractStatus = userProfile.getCurrentContract().getStatus();
		
		IPSReviewResultsForm iPSReviewResultsForm = (IPSReviewResultsForm) actionForm;
		
		String reviewRequestId = request.getParameter("reviewRequestId");
		
		// Mode will viewMode for View page
		String mode = request.getParameter("mode");

		int reviewId = 0;
		int contractId = userProfile.getCurrentContract().getContractNumber();
		
		// Take the request id from request or from the form bean
		if (StringUtils.isNotBlank(reviewRequestId)) {
			reviewId = Integer.parseInt(reviewRequestId);
		} else if (StringUtils.isNotBlank(iPSReviewResultsForm
				.getReviewRequestId())) {
			reviewId = Integer.parseInt(iPSReviewResultsForm
					.getReviewRequestId());
		}
		
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
		} else if (!IPSManagerUtility.isViewAvailable (ipsrReviewRequest
				.getReviewRequestStatus(), ipsrReviewRequest
				.getReviewRequestSubStatus())) {
			isValid = false;
		}
		
		if (!isValid){
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		
		// Remove the old bean from the session before loading the data
		iPSReviewResultsForm.resetData();
		//removeIPSResultsFormFromSession(request);

		iPSReviewResultsForm.setReviewRequestId(String.valueOf(reviewId));
		iPSReviewResultsForm.setMode(mode);
		iPSReviewResultsForm.setProcessingDateForReportLink(DateRender
				.formatByPattern(ipsrReviewRequest.getProjectedReviewDate(),
						"", MEDIUM_MMMMDDYYYY_COMMA));
		
		// Get the IPS Review Requests for Contract
		List<IPSRReviewRequest> ipsrReviewRequests = getIPSReviewRequests(contractId);
		
		if (IPSManagerUtility.isCurrentReview(ipsrReviewRequests, reviewId)) {
			iPSReviewResultsForm.setCurrentReview(true);
		}
		
		//Populate the Fund Instructions to Form
		populateIPSReviewFundInstructionToForm(iPSReviewResultsForm,
				ipsrReviewRequest, userProfile.getPrincipal().getProfileId(),
				ipsrServiceDelegate);
		
		// If no Fund Instruction is available for the Review then redirect to Home
		if (!IPSManagerUtility
				.isInstructionsAvailableForView(iPSReviewResultsForm
						.getIpsReviewFundInstructionList())) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		
		iPSReviewResultsForm.setAsOfDate(ipsrReviewRequest
				.getProjectedReviewDate());
		
		String iatEffectiveDate = DateRender.formatByPattern(ipsrReviewRequest
				.getProcessingDate(), "", RenderConstants.MEDIUM_MDY_SLASHED);

		iPSReviewResultsForm.setIpsIatEffectiveDate(iatEffectiveDate);
		
		
		if (userProfile.isInternalUser()
				|| SecurityConstants.TRUSTEE_ID.equals(userProfile.getRole()
						.getRoleId())) {
			if (IPSManagerUtility.isEditAvailable(ipsrReviewRequest
					.getReviewRequestStatus(), ipsrReviewRequest
					.getReviewRequestSubStatus(), contractStatus, iPSReviewResultsForm
					.isAllFundInstructionsIgnored(),contractId)) {
				request.setAttribute(EDIT_AVAILABLE, true);
			}
		}
		
		iPSReviewResultsForm.setFromViewPage(true);
		
		return forwards.get(DEFAULT_ACTION);
	}
	
	/**
	 * Redirects Edit IPS Review Results Action, Locks Edit IPS for Trustee's
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
	@RequestMapping(value ="/viewIPSReviewResults/",params = {"action=edit"},  method =  {RequestMethod.POST}) 
	public String doEdit (@Valid @ModelAttribute("iPSReviewResultsForm") IPSReviewResultsForm actionform,ModelMap model, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	
	
		String forward = null;
		Collection<GenericException> errors = new ArrayList<GenericException>();
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		int contractId = userProfile.getCurrentContract().getContractNumber();

		if (SecurityConstants.TRUSTEE_ID.equals(userProfile.getRole()
				.getRoleId())) {
			if (LockServiceDelegate.getInstance().lock(
					LockHelper.EDIT_IPS_REVIEW_LOCK_NAME,
					LockHelper.EDIT_IPS_REVIEW_LOCK_NAME + contractId,
					userProfile.getPrincipal().getProfileId())) {
				forward = forwards.get(EDIT_REVIEW_ACTION);
			} else {
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
					
					setErrorsInSession(request, errors);

					return doDefault( actionform,model, request, response);
				} catch (SecurityServiceException e) {
					throw new SystemException(
							"com.manulife.pension.ps.web.investment.ViewIPSReviewResultsAction.doEdit "
									+ "Failed to get user info of lock own. "
									+ e.toString());
				}
			}
		} else {
			IPSReviewResultsForm iPSReviewResultsForm = (IPSReviewResultsForm) actionform;
			iPSReviewResultsForm.setFromViewPage(true);
			iPSReviewResultsForm.setFromApprovePage(false);
			forward = forwards.get(EDIT_REVIEW_ACTION);
		}
		return forward;
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
	

	@RequestMapping(value ="/viewIPSReviewResults/", params={"action=back"}  , method =  {RequestMethod.POST}) 
	public String doBack (@Valid @ModelAttribute("iPSReviewResultsForm") IPSReviewResultsForm actionform, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		return forwards.get(BACK_ACTION);
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
	
	@RequestMapping(value ="/viewIPSReviewResults/" ,params={"action=generateReviewReport"}   , method =  {RequestMethod.GET}) 
	public String doGenerateReviewReport(@Valid @ModelAttribute("iPSReviewResultsForm") IPSReviewResultsForm actionform,ModelMap model, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
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
			setErrorsInSession(request, errors);
			//return doDefault( actionform,model, request, response);
			return forwards.get("default");
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
		binder.bind(request);
		binder.addValidators(psValidatorFWBaseIPSDefault);
	}

		
}
