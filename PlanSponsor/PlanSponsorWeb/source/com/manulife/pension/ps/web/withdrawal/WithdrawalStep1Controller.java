package com.manulife.pension.ps.web.withdrawal;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.cache.CodeLookupCache;
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.withdrawal.util.WithdrawalIrsDistributionCodesUtil;
import com.manulife.pension.service.contract.valueobject.PlanData;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.environment.valueobject.DeCodeVO;
import com.manulife.pension.service.withdrawal.dao.WithdrawalInfoDao;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;
import com.manulife.pension.service.withdrawal.mock.MockWithdrawalFactory;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMetaData;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;

/**
 * WithdrawalStep1Action is the action class for the withdrawal entry step 1 page.
 * 
 * @author Dennis_Snowdon
 * @author glennpa
 * @version 1.1.2.3 2006/09/11 15:27:33
 */
@Controller
@RequestMapping( value = "/withdrawal")
@SessionAttributes({"withdrawalForm"})

public class WithdrawalStep1Controller extends BaseWithdrawalController {
	@ModelAttribute("withdrawalForm") 
	public WithdrawalForm populateForm() 
	{
		return new WithdrawalForm();
		}
	public static HashMap<String,String> forwards =new HashMap<String,String>();
	static{
		forwards.put("default","/withdrawal/initiate/step1/entryStep1.jsp");
		forwards.put("cancel","redirect:/do/withdrawal/loanAndWithdrawalRequestsInit/");
		forwards.put("saveAndExit","redirect:/do/withdrawal/loanAndWithdrawalRequests/");
		forwards.put("delete","redirect:/do/withdrawal/loanAndWithdrawalRequests/");
		forwards.put("error","/withdrawal/initiate/step1/entryStep1.jsp");
		forwards.put("next","redirect:/do/withdrawal/entryStep2/");
		forwards.put("preventBookmarkStep1","redirect:/do/withdrawal/entryStep2/");
		}

	
	
    /**
     * HOUR_OF_DAY_FOUR_PM.
     */
    private static final int HOUR_OF_DAY_FOUR_PM = 16;

    private static final String CLASS_NAME = WithdrawalStep1Controller.class.getName();

    /**
     * This is a static reference to the logger.
     */
    private static final Logger logger = Logger.getLogger(WithdrawalStep1Controller.class);

    /**
     * {@inheritDoc}
     */
    
    @RequestMapping(value = "/entryStep1/",  method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("withdrawalForm") WithdrawalForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException { 
    	
    	if(bindingResult.hasErrors()){
 	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
 	       if(errDirect!=null){
 	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
 	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
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
            withdrawalRequest = getStep1WithdrawalRequest(form, request);
        } catch (DistributionServiceException e) {
            throw new SystemException(e, "doDefault> could not get load withdrawal request.");
        }

        // TODO: Remove this once withdrawal info is pulled into contract info and participant
        // info
        WithdrawalInfo withdrawalInfo = null;        
        try {
            WithdrawalServiceDelegate withdrawalServiceDelegate = WithdrawalServiceDelegate
                    .getInstance();
            Integer participantId = withdrawalRequest.getParticipantId();
            Integer contractId = withdrawalRequest.getContractId();
            withdrawalInfo = withdrawalServiceDelegate.getWithdrawalInfo(participantId,
                    withdrawalRequest.getContractId());
         // loading multip payee money type logic             
            
        
        } catch (DistributionServiceException e) {
            throw new SystemException(e, "doDefault> could not get Withdrawal Info.");
        }
        
        // CL 131784 added contract issued date to withdrawal request
        PlanData planData=ContractServiceDelegate.getInstance().readPlanData(withdrawalRequest.getContractId());
        withdrawalRequest.setContractIssuedStateCode(planData.getContractIssuedStateCode());

        // Retrieve the lookup data
        //form.setLookupData(getLookupData(withdrawalRequest));
        new WithdrawalIrsDistributionCodesUtil().getPaymentToList(getLookupData(withdrawalRequest), form, withdrawalRequest);
        withdrawalRequest.setSpousalConsentRequired(withdrawalRequest.getContractInfo()
                .getSpousalConsentRequired());
        withdrawalRequest.setEmployeeProfileId(withdrawalRequest.getEmployeeProfileId());
        withdrawalRequest.setContractId(withdrawalRequest.getContractId());

        // Perform default setup
        final UserProfile userProfile = getUserProfile(request);
        withdrawalRequest.setPrincipal(userProfile.getPrincipal());
        final WithdrawalRequest initializedWithdrawalRequest = getWithdrawalServiceDelegate()
                .performStep1DefaultSetup(withdrawalRequest);

        // Check for initial messages and set them up if they exist
        if (initializedWithdrawalRequest.doAlertCodesExist()) {
            handleInitialMessages( request, initializedWithdrawalRequest);
        }

        initializedWithdrawalRequest.updateOriginalStep1DriverFields();
        final WithdrawalRequestUi withdrawalRequestUi = new WithdrawalRequestUi(
                initializedWithdrawalRequest);
        withdrawalRequestUi.performStep1DefaultSetup(userProfile);
        form.setWithdrawalRequestUi(withdrawalRequestUi);
        form.setStep1Allowed();

        // Set contract information if TPA
        if (getUserProfile(request).getRole().isTPA()) {
            setTpaContractInformation(request, initializedWithdrawalRequest);
        }

        // check permissions and contract statuses
        int errorCode = checkPermissionsAndStatuses(request, form);
        if (errorCode != 0) {
            refreshLockIfAvailable(request, form);
            String forward=handlePermissionsOrStatusChanged( request, errorCode);
            return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }

        // Add token to prevent double click and resubmitting after hitting back button
        refreshLockIfAvailable(request, form);

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("doDefault> Request saved to form is [").append(
                    withdrawalRequestUi).append("]").toString());
        }
		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer(
						"doDefault> Exiting - time duration [").append(
						stopWatch.toString()).append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        return  forwards.get(ACTION_FORWARD_DEFAULT);
    }

    /**
     * Called to load the withdrawal request for initiate or edit.
     * 
     * @param actionForm The action form.
     * @param request The HTTP request.
     * @return WithdrawalRequest The withdrawal request or null if a bookmarking attempt was
     *         detected.
     * @throws DistributionServiceException When a problem occurs.
     * @throws SystemException When an generic application problem occurs.
     */
    public WithdrawalRequest getStep1WithdrawalRequest(final WithdrawalForm actionForm,
            final HttpServletRequest request) throws DistributionServiceException, SystemException {

        // Attempt to load via meta data from the session
        final WithdrawalRequestMetaData metaData = (WithdrawalRequestMetaData) request.getSession()
                .getAttribute(WebConstants.WITHDRAWAL_REQUEST_METADATA_ATTRIBUTE);
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("getStep1WithdrawalRequest> Retrieved meta data [")
                    .append(metaData).append("] from session.").toString());
        }
        // Clear meta-data to prevent future bookmarking
       request.getSession().removeAttribute(WebConstants.WITHDRAWAL_REQUEST_METADATA_ATTRIBUTE);

        // Check if meta-data is null - in which case, load from the form if present
        if (metaData == null) {

            // If request UI is null, then return null to signify bookmarking attempt
            final WithdrawalRequestUi withdrawalRequestUi = actionForm.getWithdrawalRequestUi();
            if (logger.isDebugEnabled()) {
                logger
                        .debug(new StringBuffer(
                                "getStep1WithdrawalRequest> Retrieved withdrawal request UI from the form [")
                                .append(withdrawalRequestUi).append("].").toString());
            }
            if (withdrawalRequestUi == null) {
                if (logger.isDebugEnabled()) {
                    logger
                            .debug(new StringBuffer(
                                    "getStep1WithdrawalRequest> Bookmark detected - withdrawal request ui is null [")
                                    .append(withdrawalRequestUi).append("].").toString());
                }
                return null;
            }
            // Check that status is new or draft (not post draft)
            if (withdrawalRequestUi.getWithdrawalRequest().getIsPostDraft()) {
                if (logger.isDebugEnabled()) {
                    logger
                            .debug(new StringBuffer(
                                    "getStep1WithdrawalRequest> Bookmark detected - withdrawal request is postdraft [")
                                    .append(
                                            withdrawalRequestUi.getWithdrawalRequest()
                                                    .getStatusCode()).append("].").toString());
                }
                return null;
            }

            // Check if we are allowed on step 1 (proper page navigation)
            if (!actionForm.isStep1Allowed()) {
                if (logger.isDebugEnabled()) {
                    logger
                            .debug(new StringBuffer(
                                    "getStep1WithdrawalRequest> Bookmark detected - withdrawal request is not allowed on step 1 [")
                                    .append(actionForm.getPageAllowed()).append("].").toString());
                }
                return null;
            }

            return withdrawalRequestUi.getWithdrawalRequest();
        }

        // Check for bookmarking if any required meta-data is not available
        if ((metaData.getContractId() == null)
                || (metaData.getProfileId() == null)
                || (metaData.getContractId().compareTo(GlobalConstants.INTEGER_ZERO) == 0)
                || ((StringUtils.equals(metaData.getStatusCode(),
                        WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE)) && (metaData
                        .getSubmissionId() == null))) {

            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuffer(
                        "getStep1WithdrawalRequest> Bookmarking detected with meta-data [").append(
                        metaData).append("].").toString());
            }
            return null;
        }

        // Check if we should use our mock object factory
        if (StringUtils.isNotBlank(request.getParameter("mock"))) {
            final WithdrawalRequest withdrawalRequest = MockWithdrawalFactory
                    .getMockWithdrawal(request.getParameterMap());
            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuffer(
                        "getStep1WithdrawalRequest> Request from mock object factory is [").append(
                        withdrawalRequest).append("]").toString());
            }
            return withdrawalRequest;
        }

        // Determine where to load withdrawal request from
        final WithdrawalRequest withdrawalRequest;
        if (StringUtils.isBlank(metaData.getStatusCode())) {

            // Is a new request - initiate it
            withdrawalRequest = WithdrawalServiceDelegate.getInstance()
                    .initiateNewWithdrawalRequest(metaData.getProfileId(),
                            metaData.getContractId(), getUserProfile(request).getPrincipal());
            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuffer(
                        "getStep1WithdrawalRequest> Request from initiate new withdrawal is [")
                        .append(withdrawalRequest).append("]").toString());
            }
        } else {

            withdrawalRequest = WithdrawalServiceDelegate.getInstance()
                    .readWithdrawalRequestForEdit(metaData.getSubmissionId(),
                            getUserProfile(request).getPrincipal());
            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuffer(
                        "getStep1WithdrawalRequest> Request from read withdrawal for edit is [")
                        .append(withdrawalRequest).append("]").toString());
            }
        }

        withdrawalRequest.setContractInfo(metaData.getContractInfo());
        withdrawalRequest.setTrusteeAddress(metaData.getContractInfo().getTrusteeAddress());
        return withdrawalRequest;
    }
    
    @RequestMapping(value = "/entryStep1/",params= {"action=delete"},  method =  {RequestMethod.POST}) 
   	public String doDelete(@Valid @ModelAttribute("withdrawalForm") WithdrawalForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   	throws IOException,ServletException, SystemException { 
    	
    	if(bindingResult.hasErrors()){
 	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
 	       if(errDirect!=null){
 	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
 	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
 	       }
 		}
    	 String forward=super.doDelete(form, request, response);
    	return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }
    /**
     * doNext is called when the page 'next' button is pressed.
     * 
     * @param mapping The action mapping.
     * @param actionForm The action form.
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @return ActionForward The forward to process.
     * @throws IOException When an IO problem occurs.
     * @throws ServletException When an Servlet problem occurs.
     * @throws SystemException When an generic application problem occurs.
     */
    @RequestMapping(value = "/entryStep1/",params= {"action=next"},  method =  {RequestMethod.POST}) 
	public String doNext(@Valid @ModelAttribute("withdrawalForm") WithdrawalForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException { 
    	
    	if(bindingResult.hasErrors()){
 	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
 	       if(errDirect!=null){
 	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
 	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
 	       }
 		}
        
        final WithdrawalRequest withdrawalRequest = form.getWithdrawalRequestUi()
                .getWithdrawalRequest();
        
        if(withdrawalRequest.getPaymentTo().equals(WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION)){
        	WithdrawalServiceDelegate.getInstance().getMultipayeeSection(withdrawalRequest.getContractId(), withdrawalRequest);
        }
        if(withdrawalRequest.getReasonCode().equals(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE)){
        	getHardshipWithdrawalDetails(withdrawalRequest);
        }
        final StopWatch stopWatch = new StopWatch();
		try {
			logger.info("doNext> Entry - starting timer.");
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
       

        // check permissions and contract statuses
        int errorCode = checkPermissionsAndStatuses(request, form);
        if (errorCode != 0) {
           String forward=handlePermissionsOrStatusChanged( request, errorCode);
            return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        } // fi

        form.getWithdrawalRequestUi().convertToBean(true);
        WithdrawalServiceDelegate.getInstance().proceedToStep2(withdrawalRequest);

        if (!(withdrawalRequest.isValidToProcess())) {
            refreshLockIfAvailable(request, form);

             String forward= handleBusinessErrors( request, withdrawalRequest, form
                    .getWithdrawalRequestUi());
            return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        } // fi

        // We don't want to carry this value forward into the next form.
        form.getWithdrawalRequestUi().getWithdrawalRequest().setIgnoreWarnings(Boolean.FALSE);

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("doNext> Forwarding to step 2 with request [").append(
                    form.getWithdrawalRequestUi()).append("]").toString());
        }
        form.setStep2Allowed();

		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer(
						"doNext> Exiting - time duration [").append(
						stopWatch.toString()).append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        
        //CL 103133 Begin
        WithdrawalRequest WithdrawalReq = form.getWithdrawalRequestUi().getWithdrawalRequest();
        //ULTRAS-2121
        String reasonCode=withdrawalRequest.getReasonCode();
        if (StringUtils.equals(reasonCode,
                WithdrawalRequest.WITHDRAWAL_REASON_EE_ROLLOVER_MONEY_CODE)) {
          List<WithdrawalRequestMoneyType> newMoneyTypes = new ArrayList<WithdrawalRequestMoneyType>();
              Collection<WithdrawalRequestMoneyType> newWithdrawalMoneyTypes=withdrawalRequest.getMoneyTypes();
              for (final WithdrawalRequestMoneyType withdrawalRequestMoneyType : newWithdrawalMoneyTypes) {
                if( getMoneyType().contains(withdrawalRequestMoneyType.getMoneyTypeId().trim())){
                      newMoneyTypes.add(withdrawalRequestMoneyType);
              }
              }
               withdrawalRequest.setMoneyTypes(newMoneyTypes);
        }else  {
        	ParticipantInfo participantInfo = WithdrawalInfoDao.getParticipantInfo(
                    (long) withdrawalRequest.getParticipantId(), withdrawalRequest.getContractId());
        	withdrawalRequest.setMoneyTypes(participantInfo.getMoneyTypes());
        }
        //if participant state of residence is PR set state tax =0
        if("PR".equals(WithdrawalReq.getParticipantStateOfResidence()) || "PR".equals(WithdrawalReq.getContractIssuedStateCode())){
            for (final Recipient recipient : WithdrawalReq.getRecipients()) {
                recipient.setStateTaxPercent(new BigDecimal(0));
            }
        }
        if("MS".equals(WithdrawalReq.getParticipantStateOfResidence())){
	        for (final Recipient recipient : WithdrawalReq.getRecipients()) {
	        	recipient.setStateTaxPercent(recipient.getStateTaxVo().getDefaultTaxRatePercentage());
	        }
        }
        //CL 103133 End
        
        return forwards.get(ACTION_FORWARD_NEXT);
    }

    /**
     * doCancel is called when the page 'cancel' button is pressed.
     * 
     * @param mapping The action mapping.
     * @param actionForm The action form.
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @return ActionForward The forward to process.
     * @throws IOException When an IO problem occurs.
     * @throws ServletException When an Servlet problem occurs.
     * @throws SystemException When an generic application problem occurs.
     */
    @RequestMapping(value = "/entryStep1/", params= {"action=cancel & exit"}, method =  {RequestMethod.POST}) 
   	public String doCancelAndExit(@Valid @ModelAttribute("withdrawalForm") WithdrawalForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   	throws IOException,ServletException, SystemException { 
    	
    	if(bindingResult.hasErrors()){
 	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
 	       if(errDirect!=null){
 	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
 	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
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
       

        releaseLock(request, form);
        // Clean the form
        ((WithdrawalForm) form).clean();

		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer(
						"doCancelAndExit> Exiting - time duration [").append(
						stopWatch.toString()).append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        return forwards.get(ACTION_FORWARD_CANCEL);
    }

    /**
     * doSaveAndExit is called when the page 'save & exit' button is pressed.
     * 
     * @param mapping The action mapping.
     * @param actionForm The action form.
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @return ActionForward The forward to process.
     * @throws IOException When an IO problem occurs.
     * @throws ServletException When an Servlet problem occurs.
     * @throws SystemException When an generic application problem occurs.
     */
    
    @RequestMapping(value = "/entryStep1/", params= {"action=save & exit"}, method =  {RequestMethod.POST}) 
   	public String doSaveAndExit(@Valid @ModelAttribute("withdrawalForm") WithdrawalForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   	throws IOException,ServletException, SystemException { 
    	
    	if(bindingResult.hasErrors()){
 	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
 	       if(errDirect!=null){
 	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
 	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
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
        // check permissions and contract statuses
        int errorCode = checkPermissionsAndStatuses(request, form);
        if (errorCode != 0) {
            refreshLockIfAvailable(request, form);
            String forward= handlePermissionsOrStatusChanged( request, errorCode);
            return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }

        final WithdrawalRequestUi withdrawalRequestUi = form
                .getWithdrawalRequestUi();
        // Setup the bean for saving.
        withdrawalRequestUi.performDefaultSetup(getUserProfile(request));
        withdrawalRequestUi.convertToBean(true);
        final WithdrawalRequest withdrawalRequest = withdrawalRequestUi.getWithdrawalRequest();

        // Save the current work.
        WithdrawalServiceDelegate.getInstance().save(withdrawalRequest);

        if (!(withdrawalRequest.isValidToProcess())) {
            refreshLockIfAvailable(request, form);
            String forward=handleBusinessErrors( request, withdrawalRequest, withdrawalRequestUi);
            return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        } // fi

        releaseLock(request, form);

        // Clean the form
        ((WithdrawalForm) form).clean();

		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer(
						"doSaveAndExit> Exiting - time duration [").append(
						stopWatch.toString()).append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        return forwards.get(ACTION_FORWARD_SAVE_AND_EXIT);
    }
    @RequestMapping(value ="/entryStep1/", params= {"action=print"}, method =  {RequestMethod.POST})
	   public String doPrint( @Valid @ModelAttribute("withdrawalForm") WithdrawalForm form,BindingResult bindingResult,
	             HttpServletRequest request,  HttpServletResponse response)
	            throws IOException, ServletException, SystemException {
    	
    	if(bindingResult.hasErrors()){
 	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
 	       if(errDirect!=null){
 	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
 	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
 	       }
 		}
	   String forward=super.doExecute( form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
}
    
    @RequestMapping(value ="/entryStep1/", params= {"action=PrintPDF"}, method =  {RequestMethod.POST,RequestMethod.GET})
	   public String doPrintPDF( @Valid @ModelAttribute("withdrawalForm") WithdrawalForm form,BindingResult bindingResult,
	             HttpServletRequest request,  HttpServletResponse response)
	            throws IOException, ServletException, SystemException {
 	
 	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
	       }
		}
	   String forward=super.doExecute( form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
}
    /**
     * Loads the necessary lookup data for step 1.
     * 
     * @param withdrawalRequest The withdrawal request to load lookup data with.
     */
    private Map<String, Collection> getLookupData(final WithdrawalRequest withdrawalRequest)
            throws SystemException {

        Map lookupData = null;

        try {
            lookupData = WithdrawalServiceDelegate.getInstance().getLookupData(
                    withdrawalRequest.getContractInfo(),
                    withdrawalRequest.getParticipantInfo().getParticipantStatusCode(),
                    getStepOneLookupKeys());

            final Collection paymentToOptions = WithdrawalServiceDelegate.getInstance()
                    .getParticipantPaymentToOptions(withdrawalRequest.getParticipantInfo());
            
            lookupData.put(CodeLookupCache.PAYMENT_TO_TYPE, paymentToOptions);
            //ULTRAS-2091
            final  Collection<DeCodeVO> reasons = EmployeeRolloverWithdrawalCheck(lookupData,withdrawalRequest);
            
            lookupData.put(CodeLookupCache.ONLINE_WITHDRAWAL_REASONS, reasons);

        } catch (DistributionServiceException wse) {
            throw new SystemException(wse, WithdrawalStep1Controller.class.getName(), "getLookupData",
                    "Error loading lookup data for step 1.");
        }

        return lookupData;
    }
    /**
     * toSet the HarshipDetails (US:2082,2075,2074) changes
     * @param withdrawalRequest
     * @throws SystemException
     */
    private void getHardshipWithdrawalDetails(WithdrawalRequest withdrawalRequest) throws SystemException{
    	DecimalFormat df = new DecimalFormat("0.00");
    	df.setRoundingMode(RoundingMode.DOWN);
    	PlanData planData=ContractServiceDelegate.getInstance().readPlanData(withdrawalRequest.getContractId());
        withdrawalRequest.setMaximumHarshipAmount(planData.getMaximumHardshipAmount());
        withdrawalRequest.setMinimumHarshipAmount(planData.getMinimumHardshipAmount());
        withdrawalRequest.setAvilableHarshipMoneyType( planData.getAllowableMoneyTypesForWithdrawals());
        	
    	Date asOfDate = EnvironmentServiceDelegate.getInstance().getAsOfDate();
        double countributions = WithdrawalServiceDelegate.getInstance().getParticipantNetEEDeferralContributions(withdrawalRequest.getEmployeeProfileId(), withdrawalRequest.getContractId(), withdrawalRequest.getParticipantSSN(), asOfDate);
        Collection<WithdrawalRequestMoneyType> moneyType = withdrawalRequest.getMoneyTypes();
        for(WithdrawalRequestMoneyType wdMoneyType : moneyType){
        	if(planData.getAllowableMoneyTypesForWithdrawals().contains(wdMoneyType.getMoneyTypeId().trim())){
        		if("EEDEF".equals(wdMoneyType.getMoneyTypeId().trim())){
        			wdMoneyType.setEedefFlag(true);
        			wdMoneyType.setAvailableHarshipAmount(new BigDecimal(df.format(countributions)));
        		}else{
        			wdMoneyType.setAvailableHarshipAmount(wdMoneyType.getTotalBalance());
        		}
        	}else{
        		wdMoneyType.setAvailableHarshipAmount(new BigDecimal(0.00));
        		
        	}
        }
    }
    public Collection EmployeeRolloverWithdrawalCheck(final Map lookupData, WithdrawalRequest withdrawalRequest){
    	final Collection withdrawalReasons = (Collection) lookupData
                .get(CodeLookupCache.ONLINE_WITHDRAWAL_REASONS);
    	if (CollectionUtils.isNotEmpty(withdrawalReasons)) {
       	 Iterator<DeCodeVO> iter = withdrawalReasons.iterator();
            while(iter.hasNext()){
                if(iter.next().getCode().equals("IR")&& ! EmployeeRolloverReasonCheck(withdrawalRequest)){
                
                		 iter.remove();
                	}
            }
    	}
    	return withdrawalReasons;
    	
    }
    public   boolean EmployeeRolloverReasonCheck(WithdrawalRequest withdrawalRequest){
    	boolean returnValue = false;
		  for (final WithdrawalRequestMoneyType withdrawalRequestMoneyType : withdrawalRequest
				  .getMoneyTypes()) {
		  
			if( (withdrawalRequestMoneyType.getMoneyTypeId()!= null) && (getMoneyType().contains(withdrawalRequestMoneyType.getMoneyTypeId().trim()))){
				returnValue =true;
			  }
		  }
    	return returnValue ;
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
