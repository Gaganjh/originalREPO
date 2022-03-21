package com.manulife.pension.bd.web.bob.investment;


import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
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
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.delegate.IPSRServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.event.IPSRApprovalConfirmationSubmitEvent;
import com.manulife.pension.event.client.EventClientUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.delegate.LockServiceDelegate;
import com.manulife.pension.platform.web.investment.IPSAndReviewDetailsDataValidator;
import com.manulife.pension.platform.web.investment.IPSManagerUtility;
import com.manulife.pension.platform.web.investment.valueobject.IPSFundInstructionPresentation;
import com.manulife.pension.service.contract.valueobject.ContractShutdownVO;
import com.manulife.pension.service.ipsr.valueobject.FundInstruction;
import com.manulife.pension.service.ipsr.valueobject.IPSRReviewRequest;
import com.manulife.pension.util.content.GenericException;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * Action class for IPS Approval Confirmation Page
 * 
 * @author narintr
 *
 */



@Controller
@RequestMapping( value = "/bob")
@SessionAttributes({"editIPSReviewResultsForm"})

public class ApproveIPSReviewResultsController extends BaseIPSAndReviewDetailsController {
	@ModelAttribute("editIPSReviewResultsForm") 
	public IPSReviewResultsForm populateForm() 
	{
		return new IPSReviewResultsForm();
		}
	public static HashMap<String,String>forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/investment/approveIPSReviewResults.jsp");
		forwards.put("default","/investment/approveIPSReviewResults.jsp");
		forwards.put("viewIPSAndDetails","redirect:/do/bob/investment/ipsManager/");
		forwards.put("back","redirect:/do/bob/investment/editIPSReviewResults/");
	}
	public static final String FRW_HOME_ACTION = "homePage";
	
	public static final String ON = "on";
	
	public static final String EDIT_IPS_REVIEW_LOCK_NAME = "editIPSReview";
	
	public static final String FRW_APPLICATION = "FRW";

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
	
	@RequestMapping(value ="/investment/approveIPSReviewResults/",  method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("editIPSReviewResultsForm") IPSReviewResultsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		
		
		IPSReviewResultsForm ipsAssistServiceForm = (IPSReviewResultsForm) actionForm;
		
		BobContext bob = BDSessionHelper.getBobContext(request) ;
		int contractId = bob.getCurrentContract().getContractNumber();
		String contractStatus = bob.getCurrentContract().getStatus();
		
		if(ipsAssistServiceForm.getReviewRequestId() == null) {
			forwards.get(FRW_HOME_ACTION);
		}
		
		IPSRServiceDelegate ipsrServiceDelegate = IPSRServiceDelegate
				.getInstance();
		IPSRReviewRequest ipsrReviewRequest = ipsrServiceDelegate
				.getIPSRReviewRequestForRequestId(Integer
						.parseInt(ipsAssistServiceForm.getReviewRequestId()));
		
		// If Edit is not Available for the Request and 
		// if its not from Edit Page then redirect to Home Page
		// This is to avoid if the Approve page has been book marked
		if (!IPSManagerUtility.isEditAvailable(ipsrReviewRequest
				.getReviewRequestStatus(), ipsrReviewRequest
				.getReviewRequestSubStatus(), contractStatus, ipsAssistServiceForm
				.isAllFundInstructionsIgnored(),contractId) || !ipsAssistServiceForm.isFromEditPage()) {
			forwards.get(FRW_HOME_ACTION);
		}
		
		ipsAssistServiceForm.setCurrentDate(new Date());
		
		// Clone the form to track the changes
		ipsAssistServiceForm.storeClonedForm();
		
		//saveToken(request);
		
		return forwards.get(DEFAULT_ACTION);
	}
	
	/**
	 * Method for Approval Submit confirmation, will update
	 * the Fund Instruction provided by Trustee
	 * and Update the IPSR State to Approved 
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
	@RequestMapping(value ="/investment/approveIPSReviewResults/", params={"action=submitConfirmation"} , method =  {RequestMethod.POST}) 
	public String doSubmitConfirmation(@Valid @ModelAttribute("editIPSReviewResultsForm") IPSReviewResultsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		String forward=preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		BobContext bob = BDSessionHelper.getBobContext(request) ;
		int contractId = bob.getCurrentContract().getContractNumber();
		BDUserProfile userProfile = bob.getUserProfile();

		IPSReviewResultsForm ipsAssistServiceForm = (IPSReviewResultsForm) actionForm;
		
		//Check for Terms and Condition check box
		String agreeApproval = ipsAssistServiceForm.getAgreeApproval();
		if (StringUtils.isBlank(agreeApproval)
				|| !ON.equals(agreeApproval)) {
			List<GenericException> errors = new ArrayList<GenericException>();

			IPSAndReviewDetailsDataValidator
					.addError(
							errors,
							IPSAndReviewDetailsDataValidator.IPS_AGREE_APPROVAL_NOT_CHECKED_ERROR);

			setErrorsInRequest(request, errors);
			return forwards.get(DEFAULT_ACTION);
		}
		
		// If the token is not valid then forward to view page
		/*if (!isTokenValid(request)) {
			logger.error("Detect double submission");
			return forwards.get(DEFAULT_ACTION);
		}*/

		//resetToken(request);

		boolean riaDesignationInd = SecurityServiceDelegate.getInstance().hasRia338DesignationForUser(contractId, BigDecimal.valueOf(userProfile.getBDPrincipal().getProfileId()));
		
		// Validation when multiple RIA designated user or Trustee is editing same Contract 
		if (riaDesignationInd && !userProfile.isInMimic()) {
			IPSRServiceDelegate ipsrServiceDelegate = IPSRServiceDelegate
					.getInstance();
			
			List <IPSFundInstructionPresentation> presentationList = ipsAssistServiceForm.getIpsReviewFundInstructionList();
			List<FundInstruction> fundInstructionList = ipsAssistServiceForm.getFundInstructionList();
			
			// If no Fund Instruction is available for the Review then redirect to Home
			if (ipsAssistServiceForm.getIpsReviewFundInstructionList() == null
					|| (ipsAssistServiceForm.getIpsReviewFundInstructionList() != null && ipsAssistServiceForm
							.getIpsReviewFundInstructionList().isEmpty())) {
				return forwards.get(FRW_HOME_ACTION);
			}
			
			// Populate the fund instruction with the user selected values
			IPSManagerUtility.populateFundInstructionFromPresentation(presentationList, fundInstructionList);
			
			//Update the Fund Instructions provided by the Trustee
			ipsrServiceDelegate.updateFundInstructions(fundInstructionList);
			
			boolean isAllFundInstructionsIgnored = IPSManagerUtility
					.isAllFundsInstructionsIgnored(fundInstructionList);
			
			Date ipsIatEffectiveDate = null;

			try {
				IPSRReviewRequest ipsrReviewRequest = ipsrServiceDelegate
						.getIPSRReviewRequestForRequestId(Integer
								.parseInt(ipsAssistServiceForm
										.getReviewRequestId()));

				ipsrReviewRequest.setLastUpdatedUserProfileId(userProfile.getBDPrincipal().getProfileId());
				
				if (!isAllFundInstructionsIgnored
						&& ipsAssistServiceForm
								.isIpsIATEffectiveDateAvailable()) {
					
					Date processDate = IPSManagerUtility
							.getIATEffectiveDateInDateFormat(ipsAssistServiceForm
									.getIpsIatEffectiveDate());

					String formattedDate = DateRender.formatByPattern(
							processDate, "",
							RenderConstants.MEDIUM_YMD_DASHED);

					ipsIatEffectiveDate = DateUtils.parseDate(formattedDate,
							new String[] { RenderConstants.MEDIUM_YMD_DASHED });
					
					// Update the IPS IAT Effective Date provided by the Trustee
					ipsrReviewRequest.setProcessingDate(ipsIatEffectiveDate);
				}
				
				if (IPSManagerUtility
						.isAtleastIneFundsInstructionApproved(fundInstructionList)) {
					// Update the Review Request Status to Approved 
					// if at least one Fund Instruction is Approved
					ipsrServiceDelegate
							.approveAndSendForCompletion(ipsrReviewRequest);
					
					//To trigger IPSR event
					triggerIPSRSubmitConfirmationEvent(contractId, "Contract Short Name", userProfile.getBDPrincipal().getProfileId());
					
				} else if (isAllFundInstructionsIgnored) {
					
					// Update the Review Request Status to Completed 
					// if All Fund Instructions are Ignored
					ipsrServiceDelegate.complete(ipsrReviewRequest);
				}
				
				if (!isAllFundInstructionsIgnored
						&& ipsAssistServiceForm
								.isIpsIATEffectiveDateAvailable()) {
					// Create a account management shutdown record with an
					// effective
					// date equal to the IPS IAT Effective Date
					ContractServiceDelegate delegate = ContractServiceDelegate
							.getInstance();

					ContractShutdownVO contractShutdownVO = new ContractShutdownVO();
					contractShutdownVO.setContractId(contractId);
					contractShutdownVO
							.setEventTriggerTypeCode(com.manulife.pension.service.contract.util.Constants.IPSR_APPLICATION_ID);
					contractShutdownVO.setFunctionCode("");
					contractShutdownVO
							.setShutdownEffectiveDate(ipsIatEffectiveDate);

					// Get the shut down time stamp from business param
					Map<String, String> businessParam = EnvironmentServiceDelegate
							.getInstance().getBusinessParamMap();
					String shutdownThresholdTS = businessParam
							.get("IPS_SHUTDOWN_THRESHOLD_TIME");

					Calendar shutDownTSCal = Calendar.getInstance();
					shutDownTSCal.setTime(DateUtils.parseDate(
							shutdownThresholdTS, new String[] { "hh:mm:ss" }));

					// Shutdown should be enabled only from the specified time
					// stamp from business param
					Calendar cal = Calendar.getInstance();
					cal.setTime(ipsIatEffectiveDate);
					cal.set(Calendar.HOUR_OF_DAY, shutDownTSCal
							.get(Calendar.HOUR_OF_DAY));
					cal
							.set(Calendar.MINUTE, shutDownTSCal
									.get(Calendar.MINUTE));
					cal
							.set(Calendar.SECOND, shutDownTSCal
									.get(Calendar.SECOND));

					contractShutdownVO.setShutdownStartTS(new Timestamp(cal
							.getTimeInMillis()));
					delegate.createEventShutdown(contractShutdownVO);
					
					// Insert the Contract and/or Participant Level footnote content
					// into IPS_MANAGED_CONTENT_REFERENCE
					Content content = null;
					if (ipsAssistServiceForm.isContractRedemptionFeesAvailable()) {
						content = getContent(BDContentConstants.IPS_CONTRACT_LEVEL_REDEMPTION_FEES_TEXT);
						ipsrServiceDelegate.createIPSManagedContentReference(
								content, ipsrReviewRequest);
					}
					if (ipsAssistServiceForm.isParticipantRedemptionFeesAvailable()) {
						content = getContent(BDContentConstants.IPS_PARTICIPANT_LEVEL_REDEMPTION_FEES_TEXT);
						ipsrServiceDelegate.createIPSManagedContentReference(
								content, ipsrReviewRequest);
					}
				}
			} catch (ParseException e) {
				throw new SystemException(e, "Exception while parsing date");
			} catch (ContentException e) {
				throw new SystemException(e, "Exception while getting content.");
			} 
			
			// Release the Edit IPS Lock for Trustee
			LockServiceDelegate.getInstance().releaseLock(EDIT_IPS_REVIEW_LOCK_NAME, EDIT_IPS_REVIEW_LOCK_NAME + contractId);
		}
		
		// Reset the data is saved to the data base
		ipsAssistServiceForm.resetData();
		
		return forwards.get(VIEW_IPS_AND_DETAILS_PAGE);
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
	@RequestMapping(value ="/investment/approveIPSReviewResults/", params={"action=back"} , method =  {RequestMethod.POST}) 
	public String doBack(@Valid @ModelAttribute("editIPSReviewResultsForm") IPSReviewResultsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
	
		IPSReviewResultsForm iPSReviewResultsForm = (IPSReviewResultsForm) actionForm;
		iPSReviewResultsForm.setFromApprovePage(true);
		return forwards.get(BACK_ACTION);
	}
	
	/**
	 * Method to trigger IPSR message event on IPSR submit approval page
	 * @param contractId
	 * @param contractShortName
	 * @param profileId 
	 * @throws SystemException
	 */
	public void triggerIPSRSubmitConfirmationEvent(int contractId, String contractShortName, long profileId)
					throws SystemException {
		logger.info("Event trigged for IPSR submit confirmation -->Entry");
		IPSRApprovalConfirmationSubmitEvent event = new IPSRApprovalConfirmationSubmitEvent(this.getClass().getName(), "execute");
		event.setConsumerName("MessageGeneratorMDB");
		event.setOriginatorMethod("execute");
		event.setInitiator(profileId);
		event.setContractId(contractId);
		event.setContractShortName(contractShortName);
		event.setApplication(FRW_APPLICATION);
		EventClientUtility.getInstance(BDConstants.BD_APPLICATION_ID).prepareAndSendJMSMessage(event); 
		logger.info("Event trigged for IPSR submit confirmation -->Exist");
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
				&&  ( StringUtils.equalsIgnoreCase(action, "SubmitConfirmation")
						|| StringUtils.equalsIgnoreCase(action, "Back"))?true:false;
	} 
}
