package com.manulife.pension.ps.web.withdrawal;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.cache.CodeLookupCache;
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.delegate.ParticipantServiceDelegate;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.withdrawal.util.WithdrawalIrsDistributionCodesUtil;
import com.manulife.pension.ps.web.withdrawal.util.WithdrawalWebUtil;
import com.manulife.pension.service.contract.valueobject.PlanData;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.environment.valueobject.StateTaxVO;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.withdrawal.dao.WithdrawalInfoDao;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;
import com.manulife.pension.service.withdrawal.mock.MockWithdrawalFactory;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantInfo;
import com.manulife.pension.service.withdrawal.valueobject.UserName;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMetaData;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestNote;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.ServiceLogRecord;

/**
 * WithdrawalReviewAction is the action class for the withdrawal review page.
 * 
 */
@Controller
@RequestMapping(value = "/withdrawal")
@SessionAttributes({ "withdrawalForm" })

public class WithdrawalReviewController extends BaseWithdrawalController {
	@ModelAttribute("withdrawalForm")
	public WithdrawalForm populateForm() {
		return new WithdrawalForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("default", "/withdrawal/review/review.jsp");
		forwards.put("cancelAndExit", "redirect:/do/withdrawal/loanAndWithdrawalRequests/");
		forwards.put("saveAndExit", "redirect:/do/withdrawal/loanAndWithdrawalRequests/");
		forwards.put("delete", "redirect:/do/withdrawal/loanAndWithdrawalRequests/");
		forwards.put("deny", "redirect:/do/withdrawal/loanAndWithdrawalRequests/");
		forwards.put("sendForReview", "redirect:/do/withdrawal/confirmation/");
		forwards.put("sendForApproval", "redirect:/do/withdrawal/confirmation/");
		forwards.put("approve", "redirect:/do/withdrawal/confirmation/");
		forwards.put("recalculate", "/withdrawal/review/review.jsp");
		forwards.put("error", "/withdrawal/review/review.jsp");
		forwards.put("legalese", "/withdrawal/review/review.jsp");
	}

	/**
	 * This is a static reference to the logger.
	 */
	private static final Logger logger = Logger.getLogger(WithdrawalReviewController.class);

	private Category interactionLog = Logger.getLogger(ServiceLogRecord.class);
	private ServiceLogRecord logRecord = new ServiceLogRecord(WithdrawalReviewController.class.getName());

	/**
	 * {@inheritDoc}
	 */
	@RequestMapping(value = "/review/", method = { RequestMethod.POST, RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("withdrawalForm") WithdrawalForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		}

		final StopWatch stopWatch = new StopWatch();
		try {
			logger.info("doDefault> Entry - starting timer.");
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}

		final WithdrawalRequest withdrawalRequest;
		try {
			withdrawalRequest = getReviewWithdrawalRequest(form, request);
		} catch (DistributionServiceException e) {
			throw new SystemException(e, "WithdrawalReviewAction:doDefault, could not get load withdrawal request.");
		}
		//ULTRAS-2121
		String reasonCode=withdrawalRequest.getReasonCode();
        if (StringUtils.equals(reasonCode,
                WithdrawalRequest.WITHDRAWAL_REASON_EE_ROLLOVER_MONEY_CODE)) {
        	 withdrawalRequest.setMoneyTypes(withdrawalRequest.getMoneyTypes());
          List<WithdrawalRequestMoneyType> newMoneyTypes = new ArrayList<WithdrawalRequestMoneyType>();
              Collection<WithdrawalRequestMoneyType> newWithdrawalMoneyTypes=withdrawalRequest.getMoneyTypes();
              for (final WithdrawalRequestMoneyType withdrawalRequestMoneyType : newWithdrawalMoneyTypes) {
                if( getMoneyType().contains(withdrawalRequestMoneyType.getMoneyTypeId().trim())){
                      newMoneyTypes.add(withdrawalRequestMoneyType);
              }
              }
               withdrawalRequest.setMoneyTypes(newMoneyTypes);
        }
		// CL 131784 added contract issued date to withdrawal request
		PlanData planData = ContractServiceDelegate.getInstance().readPlanData(withdrawalRequest.getContractId());
		withdrawalRequest.setContractIssuedStateCode(planData.getContractIssuedStateCode());
		if(withdrawalRequest.getReasonCode().equals(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE)){
			withdrawalRequest.setMaximumHarshipAmount(planData.getMaximumHardshipAmount());
	        withdrawalRequest.setMinimumHarshipAmount(planData.getMinimumHardshipAmount());
	        withdrawalRequest.setAvilableHarshipMoneyType(planData.getAllowableMoneyTypesForWithdrawals());;
        }
		// Perform back end default set up (taxes etc.)
		final WithdrawalRequest initializedWithdrawalRequest = getWithdrawalServiceDelegate()
				.performReviewDefaultSetup(withdrawalRequest);

		// Check for initial messages and set them up if they exist
		if (initializedWithdrawalRequest.doAlertCodesExist()) {
			handleInitialMessages(request, initializedWithdrawalRequest);
		}

		// Ensure we've set the current user ID.
		initializedWithdrawalRequest.setPrincipal(getUserProfile(request).getPrincipal());

		if (logger.isDebugEnabled()) {
			logger.debug(new StringBuffer("WithdrawalReviewAction.doDefault> Retrieved withdrawal request [")
					.append(initializedWithdrawalRequest).append("].").toString());
		}

		final WithdrawalRequestUi requestUi = new WithdrawalRequestUi(initializedWithdrawalRequest);
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

		final Map lookupData = WithdrawalServiceDelegate.getInstance().getLookupData(
				withdrawalRequest.getContractInfo(), withdrawalRequest.getParticipantInfo().getParticipantStatusCode(),
				getReviewLookupKeys());

		final Collection<StateTaxVO> taxes = getWithdrawalServiceDelegate().getAllStateTaxOptions(withdrawalRequest);
		if (logger.isDebugEnabled()) {
			logger.debug(new StringBuffer("WithdrawalReviewAction.doDefault> Finished loading state tax options [")
					.append(taxes).append("].").toString());
		}

		if (logger.isDebugEnabled()) {
			logger.debug(new StringBuffer(
					"WithdrawalReviewAction.doDefault> Finished processing with withdrawal request ui [")
							.append(requestUi).append("].").toString());
		}

		// Perform default setup
		requestUi.performReviewDefaultSetup(getUserProfile(request));
		requestUi.filterReviewLookupData(lookupData);
		new WithdrawalIrsDistributionCodesUtil().getIrsDistributionCodeListForReview(lookupData, form, withdrawalRequest);
	    new WithdrawalIrsDistributionCodesUtil().getPaymentToList(lookupData, form, withdrawalRequest);
		// Set the action form data
		form.setWithdrawalRequestUi(requestUi);
		//form.setLookupData(lookupData);
		form.setStateTaxOptions(taxes);
		form.setUserNames(getNoteUserNames(initializedWithdrawalRequest));

		// Set contract information if TPA
		if (getUserProfile(request).getRole().isTPA()) {
			setTpaContractInformation(request, initializedWithdrawalRequest);
		}

		// Clear initiates page flag
		form.clearPageAllowed();
		refreshLockIfAvailable(request, form);

		// GIFL 1C
		Principal principal = getUserProfile(request).getPrincipal();
		String participantId = (withdrawalRequest.getParticipantId()).toString();
		String contractNumber = String.valueOf(withdrawalRequest.getContractInfo().getContractId());
		String giflStatus = ParticipantServiceDelegate.getInstance().getParticipantGIFLStatus(participantId,
				contractNumber);
		if (giflStatus.equals(Constants.GIFL_SELECTED)) {
			request.setAttribute("isParticipantGIFLEnabled", new Boolean(true));
		} else {
			request.setAttribute("isParticipantGIFLEnabled", new Boolean(false));
		}

		// GIFL 1C end
		
		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer("doDefault> Exiting - time duration [").append(stopWatch.toString())
						.append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
		return forwards.get(ACTION_FORWARD_DEFAULT);
	}

	/**
	 * doCancelAndExit is called when the page 'cancel & exit' button is
	 * pressed.
	 * 
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
	@RequestMapping(value = "/review/", params = { "action=cancel & exit" }, method = { RequestMethod.POST })
	public String doCancelAndExit(@Valid @ModelAttribute("withdrawalForm") WithdrawalForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		}

		final StopWatch stopWatch = new StopWatch();
		try {
			logger.info("doCancelAndExit> Entry - starting timer.");
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}

		// Get the clean withdrawal request
		final WithdrawalRequestUi withdrawalRequestUi = form.getWithdrawalRequestUi();

		// Convert the types
		withdrawalRequestUi.convertToBean();
		if (logger.isDebugEnabled()) {
			logger.debug(new StringBuffer("doCancelAndExit> Canceling.").toString());
		} // fi
		releaseLock(request, form);

		// Clean the form
		((WithdrawalForm) form).clean();

		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer("doCancelAndExit> Exiting - time duration [").append(stopWatch.toString())
						.append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
		return forwards.get(ACTION_FORWARD_CANCEL_AND_EXIT);
	}

	/**
	 * doSaveAndExit is called when the page 'save & exit' button is pressed.
	 * 
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
	@RequestMapping(value = "/review/", params = { "action=save & exit" }, method = { RequestMethod.POST })
	public String doSaveAndExit(@Valid @ModelAttribute("withdrawalForm") WithdrawalForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		}

		final StopWatch stopWatch = new StopWatch();
		try {
			logger.info("doSaveAndExit> Entry - starting timer.");
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}

		// Get the clean withdrawal request
		final WithdrawalRequestUi withdrawalRequestUi = form.getWithdrawalRequestUi();

		// Convert the types
		withdrawalRequestUi.convertToBean();
		final WithdrawalRequest withdrawalRequest = withdrawalRequestUi.getWithdrawalRequest();
		if (logger.isDebugEnabled()) {
			logger.debug(new StringBuffer("doSaveAndExit> Saving with object [\n").append(withdrawalRequest)
					.append("\n]\n").toString());
		}

		// final WithdrawalRequest result =
		getWithdrawalServiceDelegate().save(withdrawalRequest);

		final WithdrawalRequest result = withdrawalRequest;
		// Check for business validations
		if (!(result.isValidToProcess())) {
			refreshLockIfAvailable(request, form);
			try {
				stopWatch.stop();
				if (logger.isInfoEnabled()) {
					logger.info(new StringBuffer("doSaveAndExit> Exiting with business errors - time duration [")
							.append(stopWatch.toString()).append("]").toString());
				}
			} catch (IllegalStateException e) {
				final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
						+ Thread.currentThread().toString();
				logger.error(message);
			}
			String forward = handleBusinessErrors(request, result, withdrawalRequestUi);
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		} else {
			logger.debug("No mesages found, successful.");
		} // fi

		releaseLock(request, form);
		// Reset the token
		// resetToken(request);

		// Clean the form
		((WithdrawalForm) form).clean();

		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer("doSaveAndExit> Exiting - time duration [").append(stopWatch.toString())
						.append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
		return forwards.get(ACTION_FORWARD_SAVE_AND_EXIT);
	}

	/**
	 * doSendForApproval is called when the page 'send for approval' button is
	 * pressed.
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

	@RequestMapping(value = "/review/", params = { "action=send for approval" }, method = { RequestMethod.POST })
	public String doSendForApproval(@Valid @ModelAttribute("withdrawalForm") WithdrawalForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		}
		final StopWatch stopWatch = new StopWatch();
		try {
			logger.info("doSendForApproval> Entry - starting timer.");
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}


		// Get the clean withdrawal request
		final WithdrawalRequestUi withdrawalRequestUi = form.getWithdrawalRequestUi();

		// Convert the types
		withdrawalRequestUi.convertToBean();
		final WithdrawalRequest withdrawalRequest = withdrawalRequestUi.getWithdrawalRequest();
		if (logger.isDebugEnabled()) {
			logger.debug(new StringBuffer("doSendForApproval> Sending for approval with object [\n")
					.append(withdrawalRequest).append("\n]\n").toString());
		}

		// Call the biz tier for processing.
		final WithdrawalRequest result = getWithdrawalServiceDelegate().sendForApproval(withdrawalRequest);

		// Check for business validations
		if (!(result.isValidToProcess())) {
			refreshLockIfAvailable(request, form);
			try {
				stopWatch.stop();
				if (logger.isInfoEnabled()) {
					logger.info(new StringBuffer("doSendForApproval> Exiting with business errors - time duration [")
							.append(stopWatch.toString()).append("]").toString());
				}
			} catch (IllegalStateException e) {
				final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
						+ Thread.currentThread().toString();
				logger.error(message);
			}
			 String forward = handleBusinessErrors(request, result, withdrawalRequestUi);
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		} // fi

		// Store recalculated object
		storeWithdrawalRequest(result, request);
		((WithdrawalForm) form).setActionInvoked(ACTION_FORWARD_SEND_FOR_APPROVAL);
		releaseLock(request, form);
		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer("doSendForApproval> Exiting - time duration [")
						.append(stopWatch.toString()).append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
		return forwards.get(ACTION_FORWARD_SEND_FOR_APPROVAL);
	}

	/**
	 * doApprove is called when the page 'approve' button is pressed.
	 * 
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

	@RequestMapping(value = "/review/", params = { "action=approve" }, method = { RequestMethod.POST })
	public String doApprove(@Valid @ModelAttribute("withdrawalForm") WithdrawalForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		}

		final StopWatch stopWatch = new StopWatch();
		try {
			logger.info("doApprove> Entry - starting timer.");
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}


		// Get the clean withdrawal request
		final WithdrawalRequestUi withdrawalRequestUi = form.getWithdrawalRequestUi();

		// Convert the types
		withdrawalRequestUi.convertToBean();
		final WithdrawalRequest withdrawalRequest = withdrawalRequestUi.getWithdrawalRequest();
		if (logger.isDebugEnabled()) {
			logger.debug(new StringBuffer("WithdrawalReviewAction.doApprove> Approving with object [\n")
					.append(withdrawalRequest).append("\n]\n").toString());
		}

		// Call the biz tier for processing.
		final WithdrawalRequest result = getWithdrawalServiceDelegate().approve(withdrawalRequest);
		Collection <Recipient> recipient = result.getRecipients();
        Collection<Payee> payee ;
        int payeeCount = 0;
        for(Recipient recp : recipient ){
        	payee = recp.getPayees();
        	payeeCount = payee.size(); 
        }
		String controlBlock = "";
		boolean stpExceptionFlag = false;
		String statusCode = "";
		if(result.getSubmissionId() != null && "W7".equals(withdrawalRequest.getStatusCode())) {
			ValidateSubmissionsForSTP validateSubmissionsForSTP = new ValidateSubmissionsForSTP();
		    boolean isValidForSTP = validateSubmissionsForSTP.isValidForSTP(withdrawalRequest);
  		    if (isValidForSTP) {
  		    	logger.info("invoking STP Call for the Submision id :"+result.getSubmissionId());
			try {
			   BigDecimal tpaFeeValue = null ;
			   String tpaFeeFlag = null;
			   for (Fee fee :withdrawalRequest.getFees()){
				   tpaFeeValue = fee.getValue();
			   }
			   if(null != tpaFeeValue && tpaFeeValue.compareTo(BigDecimal.ZERO) > 0){
				   tpaFeeFlag = "Y";
			   }else{
				   tpaFeeFlag = "N";
			   }
			   String inputparam = "input Param for SP1" + " Submission id : "+result.getSubmissionId()+ " ParticipantId  : "+ result.getParticipantId()+ " ContractId  : "+ result.getContractId()
			   + " ReasonCode : "+ withdrawalRequest.getReasonCode()+ " ExpectedProcessingDate : "+ WithdrawalWebUtil.getSqlDate(withdrawalRequest.getExpectedProcessingDate()) + " tpaFeeFlag : "+ tpaFeeFlag
			   +" Payee Count : "+payeeCount;
			   WithdrawalWebUtil.logSTP(withdrawalRequest, inputparam, this.getClass().getName(), "doApprove");
			   controlBlock = getWithdrawalServiceDelegate().executeLpTxnGenSTPStoredProc(
						result.getSubmissionId(), result.getParticipantId(), result.getContractId(),
						withdrawalRequest.getReasonCode(), WithdrawalWebUtil.getSqlDate(withdrawalRequest.getExpectedProcessingDate()),tpaFeeFlag,payeeCount);
			   if (StringUtils.isNotEmpty(controlBlock)) {
					statusCode = controlBlock.substring(3, 7);
				}
			    if("0000".contains(statusCode)) {
				   controlBlock = getWithdrawalServiceDelegate().callApolloSTPForOnlineWithdrawal(
							result.getSubmissionId(), result.getParticipantId(), result.getContractId());
			 
			    }
				if(StringUtils.isNotEmpty(controlBlock) && controlBlock.contains("STP SUCCESSFUL")) {
	    			  String transactionId = controlBlock.substring(controlBlock.indexOf(":") + 1).trim();
	        		  if(StringUtils.isNotEmpty(transactionId) && transactionId.length() > 9)
	        		    {
	        		      transactionId = transactionId.substring(0,10);
	        		      controlBlock = controlBlock+new WithdrawalSubmissionToAWD().sendSubmissionToAWD(result, transactionId);//send the details of successful submission to AWD via API call
	        		   }
	    		 }
				 WithdrawalWebUtil.logSTP(withdrawalRequest, controlBlock, this.getClass().getName(), "doApprove");
				} catch (Exception e) {
					WithdrawalWebUtil.logSTP(withdrawalRequest, controlBlock, this.getClass().getName(), "doApprove");
					stpExceptionFlag = true;
				}
			if (StringUtils.isNotEmpty(controlBlock)) {
					statusCode = controlBlock.substring(3, 7);
				}		
			}
			  		
  		  if (stpExceptionFlag || !"0000".contains(statusCode) || validateSubmissionsForSTP.isRouteToAWD(withdrawalRequest)) {
  			  logger.info("invoking AWD Call for the Submision id :"+result.getSubmissionId());
  			  getWithdrawalServiceDelegate().sendReadyForEntryEmail(withdrawalRequest);
		  }       
		}
		
		
		// Check for business validations
		if (!(result.isValidToProcess())) {
			refreshLockIfAvailable(request, form);
			try {
				stopWatch.stop();
				if (logger.isInfoEnabled()) {
					logger.info(new StringBuffer("doApprove> Exiting with business validations - time duration [")
							.append(stopWatch.toString()).append("]").toString());
				}
			} catch (IllegalStateException e) {
				final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
						+ Thread.currentThread().toString();
				logger.error(message);
			}
			 String forward = handleBusinessErrors(request, result, withdrawalRequestUi);
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		} // fi

		// There were no errors, but the user hasn't confirmed the legalese yet,
		// so we forward to
		// the legalese display.
		if (BooleanUtils.isFalse(result.getIsLegaleseConfirmed())) {
			result.setIsLegaleseConfirmed(Boolean.TRUE);
			refreshLockIfAvailable(request, form);
			// storeWithdrawalRequest(result, request);
			return forwards.get(ACTION_FORWARD_LEGALESE);
		} // fi

		// Store recalculated object
		storeWithdrawalRequest(result, request);

		// doReadyForEntryEmail(withdrawalForm, request, result);
		form.setActionInvoked(ACTION_FORWARD_APPROVE);
		releaseLock(request, form);
		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer("doApprove> Exiting - time duration [").append(stopWatch.toString())
						.append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
		return forwards.get(ACTION_FORWARD_APPROVE);
	}

	/**
	 * doDelete is called when the page 'delete' button is pressed.
	 * @param actionForm
	 *            The action form.
	 * @param request
	 *            The HTTP request.
	 * @param response
	 *            The HTTP response.
	 * @return ActionForward The forward to process.
	 * @throws SystemException
	 *             When an generic application problem occurs.
	 */

	@RequestMapping(value = "/review/", params = { "action=delete" }, method = { RequestMethod.POST })
	public String doDelete(@Valid @ModelAttribute("withdrawalForm") WithdrawalForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		}
		final StopWatch stopWatch = new StopWatch();
		try {
			logger.info("doDelete> Entry - starting timer.");
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}


		// We don't need to convert the types as the existing request will be
		// marked deleted

		final WithdrawalRequest withdrawalRequest = form.getWithdrawalRequestUi().getWithdrawalRequest();
		logger.debug("WithdrawalReviewAction.doDelete> Deleting.");

		// Set the user that deleted the request.
		final UserProfile userProfile = getUserProfile(request);
		final Integer currentUserId = new Integer(String.valueOf(userProfile.getPrincipal().getProfileId()));
		withdrawalRequest.setLastUpdatedById(currentUserId);

		// Call the biz tier for processing.
		if (logger.isDebugEnabled()) {
			logger.debug(new StringBuffer("WithdrawalReviewAction.doDelete> Deleting with object [\n")
					.append(withdrawalRequest).append("\n]\n").toString());
		}
		getWithdrawalServiceDelegate().delete(withdrawalRequest);

		// Check if we had validation errors
		if (!CollectionUtils.isEmpty(withdrawalRequest.getErrorCodes())) {

			try {
				stopWatch.stop();
				if (logger.isInfoEnabled()) {
					logger.info(new StringBuffer("doDelete> Exiting with business errors - time duration [")
							.append(stopWatch.toString()).append("]").toString());
				}
			} catch (IllegalStateException e) {
				final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
						+ Thread.currentThread().toString();
				logger.error(message);
			}
			String forward = handleBusinessErrors(request, withdrawalRequest, form.getWithdrawalRequestUi());
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}

		releaseLock(request, form);

		logger.debug("WithdrawalReviewAction.doDelete> Released Lock, Delete finished.");

		releaseLock(request, form);

		// Clean the form
		((WithdrawalForm) form).clean();

		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer("doDelete> Exiting - time duration [").append(stopWatch.toString())
						.append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
		return forwards.get(ACTION_FORWARD_DELETE);
	}

	/**
	 * doDeny is called when the page 'deny' button is pressed.
	 * 
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

	@RequestMapping(value = "/review/", params = { "action=deny" }, method = { RequestMethod.POST })
	public String doDeny(@Valid @ModelAttribute("withdrawalForm") WithdrawalForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		}
		final StopWatch stopWatch = new StopWatch();
		try {
			logger.info("doDeny> Entry - starting timer.");
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}


		// Get the clean withdrawal request
		final WithdrawalRequestUi withdrawalRequestUi = (form).getWithdrawalRequestUi();

		// Convert the types
		withdrawalRequestUi.convertToBean();
		final WithdrawalRequest withdrawalRequest = withdrawalRequestUi.getWithdrawalRequest();
		logger.debug("WithdrawalReviewAction.doDeny> Denying.");

		// Call the biz tier for processing.
		if (logger.isDebugEnabled()) {
			logger.debug(new StringBuffer("WithdrawalReviewAction.doDeny> Denying with object [\n")
					.append(withdrawalRequest).append("\n]\n").toString());
		} // fi

		getWithdrawalServiceDelegate().deny(withdrawalRequest);

		// Check for business validations
		if (!(withdrawalRequest.isValidToProcess())) {
			refreshLockIfAvailable(request, form);
			try {
				stopWatch.stop();
				if (logger.isInfoEnabled()) {
					logger.info(new StringBuffer("doDeny> Exiting with business errors - time duration [")
							.append(stopWatch.toString()).append("]").toString());
				}
			} catch (IllegalStateException e) {
				final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
						+ Thread.currentThread().toString();
				logger.error(message);
			}
			 String forward = handleBusinessErrors(request, withdrawalRequest, withdrawalRequestUi);
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		} // fi

		releaseLock(request, form);

		logger.debug("doDeny> Released Lock, Deny finished.");

		releaseLock(request, form);

		// Clean the form
		((WithdrawalForm) form).clean();

		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer("doDeny> Exiting - time duration [").append(stopWatch.toString())
						.append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
		return forwards.get(ACTION_FORWARD_DENY);
	}

	/**
	 * doCalculate is called when the page 'recalculate' button is pressed.
	 * 
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

	@RequestMapping(value = "/review/", params = { "action=calculate" }, method = { RequestMethod.POST })
	public String doCalculate(@Valid @ModelAttribute("withdrawalForm") WithdrawalForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
	
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		}
		final StopWatch stopWatch = new StopWatch();
		try {
			logger.info("doCalculate> Entry - starting timer.");
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}

		// Get the clean withdrawal request
		final WithdrawalRequestUi withdrawalRequestUi = ((WithdrawalForm) form).getWithdrawalRequestUi();

		// Convert the types
		withdrawalRequestUi.convertToBean();
		final WithdrawalRequest withdrawalRequest = withdrawalRequestUi.getWithdrawalRequest();

		// Log to MRL EL_TRANSACTION
		String traceMsg = new StringBuffer().append("Withdrawal Recalculated for Contract Id = ")
				.append(withdrawalRequest.getContractId()).append("; Contract Name = ")
				.append(withdrawalRequest.getContractName()).append("; First Name = ")
				.append(withdrawalRequest.getFirstName()).append("; Last Name = ")
				.append(withdrawalRequest.getLastName()).append("; MoneyTypes = ")
				.append(withdrawalRequest.getMoneyTypes()).toString();
		logWebActivity(this.getClass().getName(), "doCalculate", "withdrawalCalculate", traceMsg,
				getUserProfile(request), logger, interactionLog, logRecord);

		if (logger.isDebugEnabled()) {
			logger.debug(new StringBuffer("WithdrawalReviewAction.doCalculate> Recalculating with object [")
					.append(withdrawalRequestUi.getWithdrawalRequest()).append("]").toString());
		}

		// Call the biz tier for processing.
		if (logger.isDebugEnabled()) {
			logger.debug(new StringBuffer("WithdrawalReviewAction.doCalculate> Recalculating with object [\n")
					.append(withdrawalRequest).append("\n]\n").toString());
		}
		String reasonCode = withdrawalRequest.getReasonCode();
        if (StringUtils.equals(
                WithdrawalRequest.WITHDRAWAL_REASON_PRE_RETIREMENT_WITHDRAWAL_CODE, reasonCode)&& withdrawalRequest.getIsParticipantCreated()){
        	 withdrawalRequest.setCensusInfoAvailablePDInd(true);
        }	
		getWithdrawalServiceDelegate().recalculate(withdrawalRequest);

		// Values have changed, so we push them back into the UI object.
		// Note: References haven't changed, so we don't need to create new
		// objects.
		withdrawalRequestUi.convertFromBean();

		// Perform default setup
		withdrawalRequestUi.performReviewDefaultSetup(getUserProfile(request));

		// Need to update money type defaults
		updateMoneyTypeDefaults(withdrawalRequestUi);
		// Check if we should reset our recalculation flag
		if (!withdrawalRequest.doErrorCodesExist()) {
			withdrawalRequestUi.getWithdrawalRequest().setRecalculationRequired(false);
		}
		// Check alert for Participant initiated request for change in state tax
		// and if the total amount to withdraw is < 200
		if (withdrawalRequest.doAlertCodesExist()) {
			String forward = handleBusinessErrors(request, withdrawalRequest, withdrawalRequestUi);
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (logger.isDebugEnabled()) {
			logger.debug(new StringBuffer("WithdrawalReviewAction.doCalculate> Post-recalculation request is [")
					.append(withdrawalRequestUi).append("]").toString());
		}

		if (logger.isDebugEnabled()) {
			logger.debug(new StringBuffer("Review.doCalculate> Post recalculate WithdrawalAmountType collection is [")
					.append(((WithdrawalForm) form).getLookupData().get(CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE))
					.append("]").toString());
		}

		// Check for business validations
		if (!(withdrawalRequest.isValidToProcess())) {
			refreshLockIfAvailable(request, form);
			try {
				stopWatch.stop();
				if (logger.isInfoEnabled()) {
					logger.info(new StringBuffer("doCalculate> Exiting with business errors - time duration [")
							.append(stopWatch.toString()).append("]").toString());
				}
			} catch (IllegalStateException e) {
				final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
						+ Thread.currentThread().toString();
				logger.error(message);
			}
			 String forward = handleBusinessErrors(request, withdrawalRequest, withdrawalRequestUi);
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		} // fi

		// Store recalculated object
		storeWithdrawalRequest(withdrawalRequest, request);

		final Collection<GenericException> errorsInSession = SessionHelper.getErrorsInSession(request);
		if (errorsInSession != null) {
			logger.debug("All messages (regular): " + errorsInSession.toString());
		}
		refreshLockIfAvailable(request, form);
		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer("doCalculate> Exiting - time duration [").append(stopWatch.toString())
						.append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
		return forwards.get(ACTION_FORWARD_RECALCULATE);
	}

	@RequestMapping(value = "/review/", params = { "action=printPDF" }, method = { RequestMethod.GET })
	public String doPrintPDF(@ModelAttribute("withdrawalForm") WithdrawalForm form,
			BindingResult bindingResult, ModelMap model, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		}
		String forward = super.doPrintPDF(form, model, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	/**
	 * Updates the withdrawal request money type defaults and updates the
	 * original amount type field which is a driver of those defaults.
	 * 
	 * @param withdrawalRequest
	 *            The withdrawal request UI object that requires updating.
	 */
	private void updateMoneyTypeDefaults(final WithdrawalRequestUi withdrawalRequest) {

		// Update original amount type
		withdrawalRequest.getWithdrawalRequest()
				.setOriginalAmountTypeCode(withdrawalRequest.getWithdrawalRequest().getAmountTypeCode());
	}

	/**
	 * Retrieves the user names corresponding to the user IDs associated with
	 * any withdrawal request notes.
	 * 
	 * @param request
	 *            The request to extract note user IDs from.
	 * @return Map The map of user ID, user name key value pairs.
	 */
	private Map<Integer, UserName> getNoteUserNames(final WithdrawalRequest request) throws SystemException {

		// Retrieve the list of User Profile IDs and fetch the user names
		final Collection<Integer> userIds = new HashSet<Integer>();
		for (WithdrawalRequestNote note : request.getReadOnlyAdminToParticipantNotes()) {
			CollectionUtils.addIgnoreNull(userIds, note.getCreatedById());
		}
		for (WithdrawalRequestNote note : request.getReadOnlyAdminToAdminNotes()) {
			CollectionUtils.addIgnoreNull(userIds, note.getCreatedById());
		}

		final Map<Integer, UserName> userNameMap = getWithdrawalServiceDelegate().getUserNamesForIds(userIds);

		return userNameMap;
	}

	/**
	 * Called to load the withdrawal request for initiate or edit.
	 * 
	 * @param actionForm
	 *            The action form.
	 * @param request
	 *            The HTTP request.
	 * @return WithdrawalRequest The withdrawal request or null if a bookmarking
	 *         attempt was detected.
	 * @throws DistributionServiceException
	 *             When a problem occurs.
	 * @throws SystemException
	 *             When an generic application problem occurs.
	 */
	public WithdrawalRequest getReviewWithdrawalRequest(final WithdrawalForm actionForm,
			final HttpServletRequest request) throws DistributionServiceException, SystemException {

		// Attempt to load via meta data from the session
		final WithdrawalRequestMetaData metaData = (WithdrawalRequestMetaData) request.getSession()
				.getAttribute(WebConstants.WITHDRAWAL_REQUEST_METADATA_ATTRIBUTE);
		if (logger.isDebugEnabled()) {
			logger.debug(new StringBuffer("getReviewWithdrawalRequest> Retrieved meta data [").append(metaData)
					.append("] from session.").toString());
		}
		// Clear meta-data to prevent future bookmarking
		request.getSession().removeAttribute(WebConstants.WITHDRAWAL_REQUEST_METADATA_ATTRIBUTE);

		// Check if meta-data is null - in which case, load from the form if
		// present
		if (metaData == null) {

			// If request UI is null, then return null to signify bookmarking
			// attempt
			final WithdrawalRequestUi withdrawalRequestUi = actionForm.getWithdrawalRequestUi();
			if (logger.isDebugEnabled()) {
				logger.debug(
						new StringBuffer("getReviewWithdrawalRequest> Retrieved withdrawal request UI from the form [")
								.append(withdrawalRequestUi).append("].").toString());
			}
			if (withdrawalRequestUi == null) {
				if (logger.isDebugEnabled()) {
					logger.debug(new StringBuffer(
							"getReviewWithdrawalRequest> Bookmark detected - withdrawal request is null [")
									.append(withdrawalRequestUi).append("].").toString());
				}
				return null;
			}
			// Check that status is post draft
			if (!withdrawalRequestUi.getWithdrawalRequest().getIsPostDraft()) {
				if (logger.isDebugEnabled()) {
					logger.debug(new StringBuffer(
							"getReviewWithdrawalRequest> Bookmark detected - withdrawal request is not postdraft [")
									.append(withdrawalRequestUi.getWithdrawalRequest().getStatusCode()).append("].")
									.toString());
				}
				return null;
			}
			return withdrawalRequestUi.getWithdrawalRequest();
		}

		// Check for bookmarking if any required meta-data is not available
		if ((metaData.getContractId() == null) || (metaData.getProfileId() == null)
				|| (metaData.getContractId().compareTo(GlobalConstants.INTEGER_ZERO) == 0)
				|| (metaData.getSubmissionId() == null)) {

			if (logger.isDebugEnabled()) {
				logger.debug(new StringBuffer("getReviewWithdrawalRequest> Bookmarking detected with meta-data [")
						.append(metaData).append("].").toString());
			}
			return null;
		}

		// Check if we should use our mock object factory
		if (StringUtils.isNotBlank(request.getParameter("mock"))) {
			final WithdrawalRequest withdrawalRequest = MockWithdrawalFactory
					.getMockWithdrawal(request.getParameterMap());
			if (logger.isDebugEnabled()) {
				logger.debug(new StringBuffer("getReviewWithdrawalRequest> Request from mock object factory is [")
						.append(withdrawalRequest).append("]").toString());
			}
			return withdrawalRequest;
		}

		// Determine where to load withdrawal request from
		final WithdrawalRequest withdrawalRequest = WithdrawalServiceDelegate.getInstance()
				.readWithdrawalRequestForEdit(metaData.getSubmissionId(), getUserProfile(request).getPrincipal());
		if (logger.isDebugEnabled()) {
			logger.debug(new StringBuffer("getReviewWithdrawalRequest> Request from read withdrawal for edit is [")
					.append(withdrawalRequest).append("]").toString());
		}
		return withdrawalRequest;
	}
	public  List<String> getMoneyType(){
    	 List<String> MoneyTypes = new ArrayList<String>();
    	  MoneyTypes.add("EERC");
    	  MoneyTypes.add("EEIRA");
    	  MoneyTypes.add("EE457");
    	  MoneyTypes.add("EE403");
    	  MoneyTypes.add("EERRT");
    	  MoneyTypes.add("EESEP");
    	  MoneyTypes.add("EESIR");
    	  MoneyTypes.add("EESP");
    	  MoneyTypes.add("EEAT1");
    	  MoneyTypes.add("EEAT2");
    	  return MoneyTypes;
    	}
}
