package com.manulife.pension.bd.web.bob.investment;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

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

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.IPSRServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.investment.IPSManagerUtility;
import com.manulife.pension.service.contract.util.Constants;
import com.manulife.pension.service.contract.util.ContractShutdownConstants;
import com.manulife.pension.service.ipsr.valueobject.IPSRReviewRequest;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;

/**
 * Action Class for Cancel IPS Review Page 
 * 
 * @author narintr
 *
 */




@Controller
@RequestMapping( value = "/bob")
@SessionAttributes({"editIPSReviewResultsForm"})

public class CancelIPSReviewController extends BaseIPSAndReviewDetailsController {
	
	@ModelAttribute("editIPSReviewResultsForm") 
	public IPSReviewResultsForm populateForm() 
	{
		return new IPSReviewResultsForm();
		}
	public static HashMap<String,String>forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/investment/cancelIPSReview.jsp");
		forwards.put("default","/investment/cancelIPSReview.jsp");
		forwards.put("canceIPSReview","redirect:/do/bob/investment/ipsManager/");
		forwards.put("back","redirect:/do/bob/investment/ipsManager/");
		forwards.put("homePage","redirect:/do/home/");
	}
	
	
	
	public static final String FRW_HOME_ACTION = "homePage";

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
	@RequestMapping(value ="/investment/cancelIPSReview/",   method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("editIPSReviewResultsForm") IPSReviewResultsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		IPSReviewResultsForm ipsAndReviewDetailsForm = (IPSReviewResultsForm) actionForm;
		
		BobContext bob = BDSessionHelper.getBobContext(request) ;
		int contractId = bob.getCurrentContract().getContractNumber();
		String contractStatus = bob.getCurrentContract().getStatus();
		
		String reviewRequestId = request.getParameter("reviewRequestId");

		int reviewId = 0;

		if (StringUtils.isNotBlank(reviewRequestId)) {
			reviewId = Integer.parseInt(reviewRequestId);
			ipsAndReviewDetailsForm.setReviewRequestId(reviewRequestId);
		} else {
			reviewId = Integer.parseInt(ipsAndReviewDetailsForm
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
			return forwards.get(FRW_HOME_ACTION);
		}

		// Check for the Review Status, if its Approved show IPS IAT Effective Date
		// else show Projected Review Date
		if (Constants.IPSR_PENDING_COMPLETION_STATUS.equals(ipsrReviewRequest
				.getReviewRequestStatus())
				&& Constants.IPSR_PENDING_COMPLETION_SUB_STATUS.equals(ipsrReviewRequest
						.getReviewRequestSubStatus())) {
			// IPS IAT Effective Date
			ipsAndReviewDetailsForm.setAsOfDate(ipsrReviewRequest
					.getProcessingDate());
		} else {
			// Projected Review Date
			ipsAndReviewDetailsForm.setAsOfDate(ipsrReviewRequest
					.getProjectedReviewDate());
		}
		
		// If Current Date is as on IPS IAT Effective Date
		// Check whether NYSE is available
		ipsAndReviewDetailsForm.setNyseAvailable(IPSManagerUtility
				.isCancellationAllowed(ipsrReviewRequest.getProcessingDate()));
		
		try {
			// Confirmation message for Cancel
			Content message = ContentCacheManager.getInstance().getContentById(
					BDContentConstants.IPS_CANCEL_CONNFIRAMTION_TEXT,
					ContentTypeManager.instance().MISCELLANEOUS);

			String contents = ContentUtility.getContentAttribute(message,
					"text");

			request.setAttribute(BDConstants.IPS_CANCEL_CONFIRMATION_TEXT,
							StringUtils.trim(contents));

			// Confirmation message for NYSE Not Available
			Content nyseNotAvailablemessage = ContentCacheManager
					.getInstance()
					.getContentById(BDContentConstants.IPS_CANCEL_NYSE_NOT_AVAILABLE_TEXT,
							ContentTypeManager.instance().MISCELLANEOUS);

			String nyseNotAvailableContents = ContentUtility
					.getContentAttribute(nyseNotAvailablemessage, "text");
			ipsAndReviewDetailsForm.setNyseNotAvailableText(StringUtils
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
	@RequestMapping(value ="/investment/cancelIPSReview/", params={"action=cancel"} , method =  {RequestMethod.POST}) 
	public String doCancel(@Valid @ModelAttribute("editIPSReviewResultsForm") IPSReviewResultsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		String forward=preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		// If the token is not valid then forward to view page
		/*if(!isTokenValid(request)){
			logger.error("Detect double submission");
			return forwards.get(VIEW_IPS_AND_DETAILS_PAGE);
		}*/
		
		//resetToken(request);
		
		IPSReviewResultsForm ipsAndReviewDetailsForm = (IPSReviewResultsForm) actionForm;
		
		BobContext bob = BDSessionHelper.getBobContext(request) ;
		int contractId = bob.getCurrentContract().getContractNumber();
		BDUserProfile bduserProfile = bob.getUserProfile();
		
		boolean riaDesignationInd = SecurityServiceDelegate.getInstance().hasRia338DesignationForUser(contractId, BigDecimal.valueOf(bduserProfile.getBDPrincipal().getProfileId()));
		
		// Validation when multiple RIA designated user or Trustee is editing same Contract 
		if (riaDesignationInd && !bduserProfile.isInMimic()) {
			if (ipsAndReviewDetailsForm.isNyseAvailable()) {
				ContractServiceDelegate delegate = ContractServiceDelegate
						.getInstance();

				// Delete the Shutdown Event for IPSR
				delegate.deleteEventShutdown(contractId,
						ContractShutdownConstants.SHUTDOWN_EVENT);

				// Update the IPS Review Status to cancel
				IPSRServiceDelegate ipsrServiceDelegate = IPSRServiceDelegate
						.getInstance();
				IPSRReviewRequest ipsrReviewRequest = ipsrServiceDelegate
						.getIPSRReviewRequestForRequestId(Integer
								.parseInt(ipsAndReviewDetailsForm
										.getReviewRequestId()));

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
	@RequestMapping(value ="/investment/cancelIPSReview/", params={"action=back"} , method =  {RequestMethod.POST}) 
	public String doBack(@Valid @ModelAttribute("editIPSReviewResultsForm") IPSReviewResultsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		return forwards.get(BACK_ACTION);
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
	}
*/
	/**
	 * Returns true if token has to be validated for the particular action call
	 * to avoid CSRF vulnerability else false. (non-Javadoc)
	 * 
	 * @see com.manulife.pension.platform.web.controller.BaseAction#isTokenValidatorEnabled(java.lang.String)
	 */
	/*@Override
	protected boolean isTokenValidatorEnabled(String action) {
		// avoids methods from validation which ever is not required
		return StringUtils.isNotEmpty(action)
				&&  ( StringUtils.equalsIgnoreCase(action, "Cancel")
					|| StringUtils.equalsIgnoreCase(action, "Back"))?true:false;
	} */	
}
