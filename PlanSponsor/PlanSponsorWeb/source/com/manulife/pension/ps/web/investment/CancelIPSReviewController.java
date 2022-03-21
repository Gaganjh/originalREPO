package com.manulife.pension.ps.web.investment;

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

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.IPSRServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.investment.IPSManagerUtility;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWBaseIPSDefault;
import com.manulife.pension.service.contract.util.Constants;
import com.manulife.pension.service.contract.util.ContractShutdownConstants;
import com.manulife.pension.service.ipsr.valueobject.IPSRReviewRequest;
import com.manulife.pension.service.security.utility.SecurityConstants;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;

/**
 * Action Class for Cancel IPS Review Page 
 * 
 * @author Karthik
 *
 */
@Controller
@RequestMapping( value = "/investment")
@SessionAttributes({"iPSReviewResultsForm"})

public class CancelIPSReviewController extends BaseIPSAndReviewDetailsController {

	@ModelAttribute("iPSReviewResultsForm") 
	public  IPSReviewResultsForm  populateForm() 
	{
		return new  IPSReviewResultsForm ();
	}

	 public static HashMap<String,String> forwards = new HashMap<String,String>();
	 static{
		 forwards.put("input","/investment/cancelIPSReview.jsp");
		 forwards.put("default","/investment/cancelIPSReview.jsp");
		 forwards.put("canceIPSReview","redirect:/do/investment/ipsManager/");
		 forwards.put("back","redirect:/do/investment/ipsManager/");
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
	@RequestMapping(value ="/cancelIPSReview",  method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("iPSReviewResultsForm") IPSReviewResultsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}

		//IPSReviewResultsForm ipsAndReviewDetailsForm = (IPSReviewResultsForm) actionForm;
		
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		int contractId = userProfile.getCurrentContract().getContractNumber();
		String contractStatus = userProfile.getCurrentContract().getStatus();

		String reviewRequestId = request.getParameter("reviewRequestId");

		int reviewId = 0;

		if (StringUtils.isNotBlank(reviewRequestId)) {
			reviewId = Integer.parseInt(reviewRequestId);
			actionForm.setReviewRequestId(reviewRequestId);
		} else {
			reviewId = Integer.parseInt(actionForm
					.getReviewRequestId());
		}

		IPSRServiceDelegate ipsrServiceDelegate = IPSRServiceDelegate
				.getInstance();

		IPSRReviewRequest ipsrReviewRequest = ipsrServiceDelegate
				.getIPSRReviewRequestForRequestId(reviewId);
		
		// If IPS Service is deactivated then redirect to IPS Landing page
		// This is to avoid if the Cancel IPS Results page has been book marked
		// Check whether the access is valid. If not redirect to home page.
		boolean isValid = true;

		// Check whether the review id is valid. Invalid review id is possible
		// when trying to access the page through book mark
		if (ipsrReviewRequest == null
				|| ipsrReviewRequest.getContractId() != contractId) {
			isValid = false;
		} else if (!IPSManagerUtility.isCancelAvailable(ipsrReviewRequest
				.getReviewRequestStatus(), ipsrReviewRequest
				.getReviewRequestSubStatus(), ipsrReviewRequest
				.getProcessingDate(), contractStatus,contractId)) {
			isValid = false;
		}        
	
		if (!isValid) {
//			return forwards.get(com.manulife.pension.ps.web.Constants.HOMEPAGE_FINDER_FORWARD);
			return com.manulife.pension.ps.web.Constants.HOMEPAGE_FINDER_FORWARD;
		}

		// Check for the Review Status, if its Approved show IPS IAT Effective Date
		// else show Projected Review Date
		if (Constants.IPSR_PENDING_COMPLETION_STATUS.equals(ipsrReviewRequest
				.getReviewRequestStatus())
				&& Constants.IPSR_PENDING_COMPLETION_SUB_STATUS.equals(ipsrReviewRequest
						.getReviewRequestSubStatus())) {
			// IPS IAT Effective Date
			actionForm.setAsOfDate(ipsrReviewRequest
					.getProcessingDate());
		} else {
			// Projected Review Date
			actionForm.setAsOfDate(ipsrReviewRequest
					.getProjectedReviewDate());
		}
		
		// If Current Date is as on IPS IAT Effective Date
		// Check whether NYSE is available
		actionForm.setNyseAvailable(IPSManagerUtility
				.isCancellationAllowed(ipsrReviewRequest.getProcessingDate()));
		
		try {
			// Confirmation message for Cancel
			Content message = ContentCacheManager.getInstance().getContentById(
					ContentConstants.IPS_CANCEL_CONNFIRAMTION_TEXT,
					ContentTypeManager.instance().MISCELLANEOUS);

			String contents = ContentUtility.getContentAttribute(message,
					"text");

			request
					.setAttribute(
							com.manulife.pension.ps.web.Constants.IPS_CANCEL_CONFIRMATION_TEXT,
							StringUtils.trim(contents));

			// Confirmation mwssage for NYSE Not Available
			Content nyseNotAvailablemessage = ContentCacheManager
					.getInstance()
					.getContentById(
							ContentConstants.IPS_CANCEL_NYSE_NOT_AVAILABLE_TEXT,
							ContentTypeManager.instance().MISCELLANEOUS);

			String nyseNotAvailableContents = ContentUtility
					.getContentAttribute(nyseNotAvailablemessage, "text");
			actionForm.setNyseNotAvailableText(StringUtils
					.trim(nyseNotAvailableContents));
		} catch (ContentException exp) {
			throw new SystemException(exp, "Something wrong with CMA");
		}
		
		//saveToken(request);

		return forwards.get(DEFAULT_ACTION);
	}
	
	/**
	 * Cancels the IPS Review and redirects to IPS Landing page
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
	@RequestMapping(value ="/cancelIPSReview" ,params={"action=cancel"}   , method =  {RequestMethod.POST}) 
	public String doCancel (@Valid @ModelAttribute("iPSReviewResultsForm") IPSReviewResultsForm actionform, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	
	
		// If the token is not valid then forward to view page
		/*if(!isTokenValid(request)){
			logger.error("Detect double submission");
			return forwards.get(VIEW_IPS_AND_DETAILS_PAGE);
		}*/
		
		//resetToken(request);
		
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		
		//IPSReviewResultsForm ipsAndReviewDetailsForm = (IPSReviewResultsForm) actionForm;
		
		// Cancel the IPS Review Request only if its Trustee
		// For Internal users - it will not update the IPS Review
		if (SecurityConstants.TRUSTEE_ID.equals(userProfile.getPrincipal()
				.getRole().getRoleId())) {
			
			if (actionform.isNyseAvailable()) {
				ContractServiceDelegate delegate = ContractServiceDelegate
						.getInstance();

				int contractId = userProfile.getCurrentContract()
						.getContractNumber();

				// Delete the Shutdown Event for IPSR
				delegate.deleteEventShutdown(contractId,
						ContractShutdownConstants.SHUTDOWN_EVENT);

				// Update the IPS Review Status to cancel
				IPSRServiceDelegate ipsrServiceDelegate = IPSRServiceDelegate
						.getInstance();
				IPSRReviewRequest ipsrReviewRequest = ipsrServiceDelegate
						.getIPSRReviewRequestForRequestId(Integer
								.parseInt(actionform.getReviewRequestId()));

				ipsrServiceDelegate.cancel(ipsrReviewRequest);
			}
		}
		
		return forwards.get(CANCEL_CONFIRMATION_ACTION);
	}

	/**
	 * Redirects to IPS Landing page from IPS Cancel page
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
	
	
	@RequestMapping(value ="/cancelIPSReview", params={"action=back"} , method =  {RequestMethod.POST}) 
	public String doBack (@Valid @ModelAttribute("iPSReviewResultsForm") IPSReviewResultsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
	@Autowired
    private PSValidatorFWBaseIPSDefault psValidatorFWBaseIPSDefault;  

	@InitBinder
	protected void initBinder(HttpServletRequest request,
				ServletRequestDataBinder  binder) {
		binder.bind( request);
		binder.addValidators(psValidatorFWBaseIPSDefault);
	}

}
