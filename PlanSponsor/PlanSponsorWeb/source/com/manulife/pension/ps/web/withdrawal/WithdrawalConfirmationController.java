package com.manulife.pension.ps.web.withdrawal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.ps.common.util.Constants;
import com.manulife.pension.ps.web.withdrawal.util.WithdrawalIrsDistributionCodesUtil;
import com.manulife.pension.service.fee.valueobject.WithdrawalTransactionalFee;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;
import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestNote;


/**
 * WithdrawalConfirmationAction is the action class for the withdrawal
 * confirmation page.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1.2.1 2006/08/02 19:33:19
 */
@Controller
@RequestMapping(value = "/withdrawal")
@SessionAttributes({ "withdrawalForm" })

public class WithdrawalConfirmationController extends BaseWithdrawalController {

	@ModelAttribute("withdrawalForm")
	public WithdrawalForm populateForm() {
		return new WithdrawalForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("default", "/withdrawal/view/confirmation.jsp");
		forwards.put("finished", "redirect:/do/withdrawal/loanAndWithdrawalRequestsInit/");
	}

	private static final Logger logger = Logger.getLogger(WithdrawalConfirmationController.class);

	/**
	 * {@inheritDoc}
	 */

	@RequestMapping(value = "/confirmation/", method = {  RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("withdrawalForm") WithdrawalForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		}

		checkAndEnableSearchSummaryBookmarkTracking(request);

		form.clearPageAllowed();

		WithdrawalRequest withdrawalRequest;
		WithdrawalRequestUi withdrawalRequestUi = form.getWithdrawalRequestUi();
		withdrawalRequest = withdrawalRequestUi.getWithdrawalRequest();

		Integer submissionId = withdrawalRequest.getSubmissionId();
		if (logger.isDebugEnabled()) {
			logger.debug("ID:: " + withdrawalRequest.getSubmissionId());
			logger.debug("User ID:: " + (int) withdrawalRequest.getPrincipal().getProfileId());
		} // fi

		form.setActionInvoked(StringUtils.EMPTY);
		withdrawalRequest = WithdrawalServiceDelegate.getInstance().readWithdrawalRequestForView(submissionId,
				withdrawalRequest.getPrincipal());
		ContractInfo contractInfo = withdrawalRequest.getContractInfo();
		WithdrawalRequestUi.populatePermissions(contractInfo, getUserProfile(request).getPrincipal());
		withdrawalRequest.setContractInfo(contractInfo);
		final WithdrawalRequestUi requestUi = new WithdrawalRequestUi(withdrawalRequest);

		Map lookupData = null;
		lookupData = WithdrawalServiceDelegate.getInstance().getLookupData(null, StringUtils.EMPTY, getAllLookupKeys());

		form.setLookupData(lookupData);
		// Set the action form data
		requestUi.setConfirmAction(true);
		 if(withdrawalRequest.getPaymentTo().trim().equals(WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION)){
			 WithdrawalServiceDelegate.getInstance().getMultipayeeSection(withdrawalRequest.getContractId(), withdrawalRequest);
			 
			 	requestUi.setTb(withdrawalRequest.getTaxableBal());
				requestUi.setPa(withdrawalRequest.getParticipant());
				requestUi.setPaat(withdrawalRequest.getParticipantAftrTax());
				requestUi.setPar(withdrawalRequest.getParticipantRoth());
				requestUi.setRb(withdrawalRequest.getRothBal());
				requestUi.setNrat(withdrawalRequest.getParticipantNonRoth());
				
				requestUi.setRothPayeeFlag(withdrawalRequest.isTotalRothBalFlag());
				requestUi.setNonTaxablePayeeFlag(withdrawalRequest.isNonTaxableFlag());
				
				new WithdrawalIrsDistributionCodesUtil().getSavedValuesForMultipayee(withdrawalRequest);
	        	
	        	if(withdrawalRequest.getTaxableParticipantInfo() != null){
	        		requestUi.setTbCategory(withdrawalRequest.getTaxableParticipantInfo());
	        	}
	        	if (withdrawalRequest.getRothParticaipantInfo() != null){
	        		requestUi.setRbCategory(withdrawalRequest.getRothParticaipantInfo());
	        	}
	        	if(withdrawalRequest.getNonTaxableParticipantInfo() != null){
	        		requestUi.setNratCategory(withdrawalRequest.getNonTaxableParticipantInfo());
	        	}
	        	if(withdrawalRequest.getParticipantDetails() !=null){
	        		requestUi.setPayDirectlyTome(withdrawalRequest.getParticipantDetails());
	        	}
	        	if(withdrawalRequest.getPayDirectlyTomeAmount() !=null){
	        		requestUi.setPayDirectlyTomeAmount(withdrawalRequest.getPayDirectlyTomeAmount().toString());
	        	}
	        }
		form.setWithdrawalRequestUi(requestUi);
		form.setLookupData(lookupData);
		form.clearPageAllowed();

		// retrieve the list of User Profile IDs and fetch the user names
		ArrayList<Integer> userProfileIds = new ArrayList<Integer>();

		for (WithdrawalRequestNote note : form.getWithdrawalRequestUi().getWithdrawalRequest()
				.getReadOnlyAdminToParticipantNotes()) {
			if (note.getCreatedById() != null && !userProfileIds.contains(note.getCreatedById())) {
				userProfileIds.add(note.getCreatedById());
			} // fi
		}
		for (WithdrawalRequestNote note : form.getWithdrawalRequestUi().getWithdrawalRequest()
				.getReadOnlyAdminToAdminNotes()) {
			if (note.getCreatedById() != null && !userProfileIds.contains(note.getCreatedById())) {
				userProfileIds.add(note.getCreatedById());
			} // fi
		}

		form.setWithdrawalTransactionalFee(null);
		List<WithdrawalTransactionalFee> WithdrawalTransactionalFee = FeeServiceDelegate
				.getInstance(Constants.PS_APPLICATION_ID).getContractWithdrawalProcessingFees(
						withdrawalRequest.getContractId(), withdrawalRequest.getRequestDate());
		for (WithdrawalTransactionalFee withdrawalTransactionalFee : WithdrawalTransactionalFee) {
			if (withdrawalTransactionalFee.getReasonCode().equals(withdrawalRequest.getReasonCode())) {
				form.setWithdrawalTransactionalFee(withdrawalTransactionalFee);
				break;
			}
		}

		form.setUserNames(getWithdrawalServiceDelegate().getUserNamesForIds(userProfileIds));
		if (logger.isDebugEnabled()) {
			logger.debug("CONFIRM :: " + requestUi);
		}
		return forwards.get(ACTION_FORWARD_DEFAULT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.withdrawal.BaseWithdrawalAction#
	 * prepareXMLFromReport(com.manulife.pension.platform.web.controller.
	 * AutoForm, com.manulife.pension.service.report.valueobject.ReportData,
	 * javax.servlet.http.HttpServletRequest)
	 */
	public Object prepareXMLFromReport(AutoForm actionForm, ReportData report, HttpServletRequest request)
			throws ParserConfigurationException, SystemException, ContentException {
		WithdrawalForm confirmForm = (WithdrawalForm) actionForm;
		confirmForm.parseRequestStatusCollection();
		return super.prepareXMLFromReport(actionForm, report, request);
	}

	/**
	 * doFinished is called when the page 'finished' button is pressed.
	 * 
	 * @param mapping
	 *            The action mapping.
	 * @param actionForm
	 *            The action form.
	 * @param request
	 *            The HTTP request.
	 * @param response
	 *            The HTTP response.
	 * @return ActionForward The forward to process.
	 * @throws IOException
	 *             When an IO problem occurs.
	 * @throws ServletException
	 *             When an Servlet problem occurs.
	 * @throws SystemException
	 *             When an generic application problem occurs.
	 */

	@RequestMapping(value = "/confirmation/", params = { "action=finished"}, method = {
			RequestMethod.POST})
	public String doFinished(@Valid @ModelAttribute("withdrawalForm") WithdrawalForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		}
		// Clean the form
		((WithdrawalForm) form).clean();

		return forwards.get(ACTION_FORWARD_FINISHED);
	}
	@RequestMapping(value ="/confirmation/", params= {"actionLabel=printPDF"}, method =  {RequestMethod.POST,RequestMethod.GET})
	public String doPrintPDF( @ModelAttribute("withdrawalForm") WithdrawalForm form,BindingResult bindingResult,
			HttpServletRequest request,  HttpServletResponse response)
					throws IOException, ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
			}
		}
		 String forward=super.doExecute( form, request, response);
		 form.setActionLabel(null);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.ps.web.withdrawal.BaseWithdrawalAction#getLayoutKey()
	 */
	public String getLayoutKey() {
		return "/withdrawal/view/confirmation.jsp";
	}

}
