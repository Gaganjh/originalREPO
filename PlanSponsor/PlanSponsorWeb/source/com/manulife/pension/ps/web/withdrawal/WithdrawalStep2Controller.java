package com.manulife.pension.ps.web.withdrawal;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.cache.CodeLookupCache;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.withdrawal.util.WithdrawalIrsDistributionCodesUtil;
import com.manulife.pension.ps.web.withdrawal.util.WithdrawalWebUtil;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessage;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageType;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;
import com.manulife.pension.service.withdrawal.util.WithdrawalRequestHelper;
import com.manulife.pension.service.withdrawal.valueobject.TaxesFlag;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.util.log.ServiceLogRecord;
import com.manulife.pension.validator.ValidationError;
/**
 * WithdrawalStep2Action is the action class for the withdrawal entry step 2 page.
 * 
 * @author Dennis_Snowdon
 * @author glennpa
 * @version 1.1.2.8 2006/08/29 19:02:36
 */
@Controller
@RequestMapping( value = "/withdrawal")
@SessionAttributes({"withdrawalForm"})

public class WithdrawalStep2Controller extends BaseWithdrawalController {
	@ModelAttribute("withdrawalForm") 
	public WithdrawalForm populateForm()
	{
		return new WithdrawalForm();
		
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("default","/withdrawal/initiate/step2/entryStep2.jsp");
		forwards.put("back","redirect:/do/withdrawal/entryStep1/");
		forwards.put("saveAndExit","redirect:/do/withdrawal/loanAndWithdrawalRequests/");
		forwards.put("cancelAndExit","redirect:/do/withdrawal/loanAndWithdrawalRequests/");
		forwards.put("delete","redirect:/do/withdrawal/loanAndWithdrawalRequests/");
		forwards.put("sendForReview","redirect:/do/withdrawal/confirmation/");
		forwards.put("sendForApproval","redirect:/do/withdrawal/confirmation/");
		forwards.put("approve","redirect:/do/withdrawal/confirmation/");
		forwards.put("recalculate","/withdrawal/initiate/step2/entryStep2.jsp");
		forwards.put("error","/withdrawal/initiate/step2/entryStep2.jsp");
		forwards.put("legalese","/withdrawal/initiate/step2/entryStep2.jsp");
		forwards.put("preventBookmarkStep2","redirect:/do/withdrawal/entryStep1/");
					}


    /**
     * This is a static reference to the logger.
     */
    private static final Logger logger = Logger.getLogger(WithdrawalStep2Controller.class);
    private Category interactionLog = Logger.getLogger(ServiceLogRecord.class);
    private ServiceLogRecord logRecord = new ServiceLogRecord(WithdrawalReviewController.class.getName());

    private static final Integer ZERO = new Integer(0);

    /**
     * {@inheritDoc}
     */
    @RequestMapping(value = "/entryStep2/",  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("withdrawalForm") WithdrawalForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
    	
    	if(bindingResult.hasErrors()){
 	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
 	       if(errDirect!=null){
 	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
 	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
 	       }
 		}
    	form.setAction("default");
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
        	
            withdrawalRequest = getStep2WithdrawalRequest(form, request);
           
        } catch (DistributionServiceException e) {
            throw new SystemException(e,
                    "WithdrawalStep2Action:doDefault, could not get load withdrawal request.");
        }

        // Check if new withdrawal object
        // if (!BooleanUtils.isTrue(withdrawalRequest.getVestingCalledInd())) {
        final boolean showStep1FieldsChangedMessage;
        if (CollectionUtils.isEmpty(withdrawalRequest.getRecipients())) {
            WithdrawalRequestHelper.populateDefaultRecipient(withdrawalRequest);

            // Never show step 1 fields changed if this is a new request
            showStep1FieldsChangedMessage = false;
        } else {
            // Check if we should display step 1 fields have changed
            showStep1FieldsChangedMessage = withdrawalRequest.getHaveStep1DriverFieldsChanged();
        }
        // Update recipients
        WithdrawalRequestHelper.updateRecipients(withdrawalRequest);

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer(
                    "WithdrawalStep2Action.doDefault> Retrieved Step 1 withdrawal request [")
                    .append(withdrawalRequest).append("].").toString());
        }

        final Map lookupData = WithdrawalServiceDelegate.getInstance().getLookupData(
                withdrawalRequest.getContractInfo(),
                withdrawalRequest.getParticipantInfo().getParticipantStatusCode(),
                getStepTwoLookupKeys());
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer(
                    "\n\n\nStep2.doDefault> Pre-filter WithdrawalAmountType collection is [")
                    .append(lookupData.get(CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE)).append("]")
                    .toString());
        }
        final UserProfile userProfile = getUserProfile(request);
        withdrawalRequest.setPrincipal(userProfile.getPrincipal());
        // The perform step 2 default setup method calls both recalculate and updateTax, so that
        // the request contains the updated values.
        final WithdrawalRequest initializedWithdrawalRequest = getWithdrawalServiceDelegate()
                .performStep2DefaultSetup(withdrawalRequest);
        checkStep1DriverChangedMessage(request, initializedWithdrawalRequest,
                showStep1FieldsChangedMessage);

        final WithdrawalRequestUi requestUi = new WithdrawalRequestUi(initializedWithdrawalRequest);
        requestUi.performStep2DefaultSetup(userProfile);
        requestUi.filterStep2LookupData(lookupData);
    	new WithdrawalIrsDistributionCodesUtil().getIrsDistributionCodeList(lookupData, form, withdrawalRequest); 
       	new WithdrawalIrsDistributionCodesUtil().getPaymentToList(lookupData, form, withdrawalRequest);   	     
    	if(StringUtils.equals(withdrawalRequest.getPaymentTo().trim(), WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION)){
    		requestUi.setTb(withdrawalRequest.getTaxableBal());
    		requestUi.setPa(withdrawalRequest.getParticipant());
    		requestUi.setPaat(withdrawalRequest.getParticipantAftrTax());
    		requestUi.setPar(withdrawalRequest.getParticipantRoth());
    		requestUi.setRb(withdrawalRequest.getRothBal());
    		requestUi.setNrat(withdrawalRequest.getParticipantNonRoth());
    		if (withdrawalRequest.getSubmissionId() != null){
    			 
    		        	new WithdrawalIrsDistributionCodesUtil().getSavedValuesForMultipayee(withdrawalRequest);
    		        	//withdrawalRequestUi.setRothPayeeFlag(withdrawalRequest.isTotalRothBalFlag());
    		        	//withdrawalRequestUi.setNonTaxablePayeeFlag(withdrawalRequest.isNonTaxableFlag());
    					
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
    	}
    		
    		
    		
    	
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer(
                    "\n\n\nStep2.doDefault> Filtered WithdrawalAmountType collection is [").append(
                    lookupData.get(CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE)).append("]").toString());
        }

        // Need to update money type defaults
        updateMoneyTypeDefaults(requestUi);

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer(
                    "\n\n\nStep2.doDefault> Withdrawal being saved to form is [").append(
                    requestUi.toString()).append("].\n\n").toString());
        }

        // Set the action form data
        form.setWithdrawalRequestUi(requestUi);
        // form.setLookupData(lookupData);
        form.setStep2Allowed();

        // check permissions and contract statuses
        int errorCode = checkPermissionsAndStatuses(request, form);
        if (errorCode != 0) {
            refreshLockIfAvailable(request, form);
            String forward= handlePermissionsOrStatusChanged( request, errorCode);
            return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }

        // Add token to prevent dbl click and resubmitting
        refreshLockIfAvailable(request, form);

        // Check for business validations
        if (!(withdrawalRequest.isValidToProcess())) {
            String forward= handleBusinessErrors( request, withdrawalRequest, requestUi);
            return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        } else {
            // Setup any messages, if they exist.
            handleInitialMessages( request, initializedWithdrawalRequest);
        } // fi

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
        return forwards.get(ACTION_FORWARD_DEFAULT);
    }

    /**
     * doBack is called when the page 'back' button is pressed.
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
    @RequestMapping(value = "/entryStep2/",params= {"action=back"} , method =  {RequestMethod.POST}) 
   	public String doBack(@Valid @ModelAttribute("withdrawalForm") WithdrawalForm withdrawalForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   	throws IOException,ServletException, SystemException {
    	
    	if(bindingResult.hasErrors()){
 	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
 	       if(errDirect!=null){
 	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
 	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
 	       }
 		}
    
		final StopWatch stopWatch = new StopWatch();
		try {
			logger.info("doBack> Entry - starting timer.");
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        
        withdrawalForm.setAction(AutoForm.DEFAULT);

        //retain Federal Tax Value
        withdrawalForm.setActionInvoked(ACTION_FORWARD_BACK);


        // Validate the withdrawal request
        final WithdrawalRequestUi withdrawalRequestUi = withdrawalForm
                .getWithdrawalRequestUi();
        final Collection validations = validateWithdrawalRequest(withdrawalRequestUi);
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("doBack> Found [").append(validations.size()).append(
                    "] validations.").toString());
        }
        if (!validations.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuffer("doBack> Found [").append(validations.size()).append(
                        "] validations - sending to forward [").append(ACTION_FORWARD_ERROR)
                        .append("].").toString());
            }

            // Store validations and return to page
            setErrorsInSession(request, validations);
            refreshLockIfAvailable(request, withdrawalForm);

			try {
				stopWatch.stop();
				if (logger.isInfoEnabled()) {
					logger
							.info(new StringBuffer(
									"doBack> Exiting with web errors - time duration [")
									.append(stopWatch.toString()).append("]")
									.toString());
				}
			} catch (IllegalStateException e) {
				final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
						+ Thread.currentThread().toString();
				logger.error(message);
			}
            return forwards.get( ACTION_FORWARD_ERROR);
        }

        // Need to convert back to bean to save declarations
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("doBack> Pre conversion declarations are [").append(
                    withdrawalForm.getWithdrawalRequestUi().getWithdrawalRequest()
                            .getDeclarations()).append("].").toString());
        }
        withdrawalForm.getWithdrawalRequestUi().convertToBean();
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("doBack> Post conversion declarations are [").append(
                    withdrawalForm.getWithdrawalRequestUi().getWithdrawalRequest()
                            .getDeclarations()).append("].").toString());
        }

        // Do business validations (as user can save on step 1)
        final WithdrawalRequest withdrawalRequest = withdrawalRequestUi.getWithdrawalRequest();
        
        // Get error messages from the step2 page, to validate & display the Termination date 
        // error message in step1 page - CL 123057
        Collection<WithdrawalMessage> businessMessages = withdrawalRequest.getErrorCodes();       
        getWithdrawalServiceDelegate().returnToStep1(withdrawalRequest);

        // Check for business validations
        // Ignore warnings
        withdrawalRequest.setIgnoreWarnings(true);
        if (!(withdrawalRequest.isValidToProcess())) {
            refreshLockIfAvailable(request, withdrawalForm);

            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuffer("doBack> Current action is [").append(
                        withdrawalForm.getAction()).append("].").toString());
            }
			try {
				stopWatch.stop();
				if (logger.isInfoEnabled()) {
					logger
							.info(new StringBuffer(
									"doBack> Exiting with business errors - time duration [")
									.append(stopWatch.toString()).append("]")
									.toString());
				}
			} catch (IllegalStateException e) {
				final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
						+ Thread.currentThread().toString();
				logger.error(message);
			}

            String forward=handleBusinessErrors( request, withdrawalRequest, withdrawalRequestUi);
            return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
        // CL 123057 - Validate for Termination and Retirement date field, if any business message exists in step2 page
        if(businessMessages != null && !businessMessages.isEmpty()){ 
        	
        	//Validate termination date
        	getWithdrawalServiceDelegate().returnToStep1WithTerminationOrRetirementDate(withdrawalRequest);
        	 // Check for business validations
            // Ignore warnings
            withdrawalRequest.setIgnoreWarnings(true);
            if (!(withdrawalRequest.isValidToProcess())) {

                if (logger.isDebugEnabled()) {
                    logger.debug(new StringBuffer("doBack> Current action is [").append(
                            withdrawalForm.getAction()).append("].").toString());
                }
    			try {
    				stopWatch.stop();
    				if (logger.isInfoEnabled()) {
    					logger
    							.info(new StringBuffer(
    									"doBack> Exiting with business errors - time duration [")
    									.append(stopWatch.toString()).append("]")
    									.toString());
    				}
    			} catch (IllegalStateException e) {
    				final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
    						+ Thread.currentThread().toString();
    				logger.error(message);
    			}    			
    			withdrawalForm.setStep1Allowed();
                // Mark our vesting could not be calculated flag to false to suppress content
                withdrawalRequest.setVestingCouldNotBeCalculatedInd(false);
                // Mark that warnings should not be ignored
                withdrawalRequest.setIgnoreWarnings(false);
                String forward=handleTerminationDateErrors( request, withdrawalRequest, withdrawalRequestUi);
                return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
            }
        }

        withdrawalForm.setStep1Allowed();
        // Mark our vesting could not be calculated flag to false to suppress content
        withdrawalRequest.setVestingCouldNotBeCalculatedInd(false);
        // Mark that warnings should not be ignored
        withdrawalRequest.setIgnoreWarnings(false);
        logger.debug("doBack> No messages found, successful.");

		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer(
						"doBack> Exiting - time duration [").append(
						stopWatch.toString()).append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}

        return forwards.get(ACTION_FORWARD_BACK);
    }

    /**
     * doCancelAndExit is called when the page 'cancel & exit' button is pressed.
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
    @RequestMapping(value = "/entryStep2/",params= {"action=cancel & exit"} , method =  {RequestMethod.POST}) 
   	public String doCancelAndExit(@Valid @ModelAttribute("withdrawalForm") WithdrawalForm withdrawalForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   	throws IOException,ServletException, SystemException {
    	
    	if(bindingResult.hasErrors()){
 	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
 	       if(errDirect!=null){
 	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
 	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
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
        final WithdrawalRequestUi withdrawalRequestUi = cleanWithdrawalRequest(withdrawalForm
                .getWithdrawalRequestUi());

        // Validate the withdrawal request
        final Collection validations = validateWithdrawalRequest(withdrawalRequestUi);
        if (!validations.isEmpty()) {
            refreshLockIfAvailable(request, withdrawalForm);
            return forwards.get( ACTION_FORWARD_ERROR);
        }

        // Convert the types
        withdrawalRequestUi.convertToBean();
        final WithdrawalRequest withdrawalRequest = withdrawalRequestUi.getWithdrawalRequest();
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("WithdrawalStep2Action.doCancelAndExit> Canceling.")
                    .toString());
        } // fi

        releaseLock(request, withdrawalForm);

        // Clean the form
        withdrawalForm.clean();

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

        return forwards.get( ACTION_FORWARD_CANCEL_AND_EXIT);
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
    @RequestMapping(value = "/entryStep2/",params= {"action=save & exit"} , method =  {RequestMethod.POST}) 
   	public String doSaveAndExit(@Valid @ModelAttribute("withdrawalForm") WithdrawalForm withdrawalForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   	throws IOException,ServletException, SystemException {
    	
    	if(bindingResult.hasErrors()){
 	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
 	       if(errDirect!=null){
 	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
 	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
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
        final WithdrawalRequestUi withdrawalRequestUi = cleanWithdrawalRequest(withdrawalForm
                .getWithdrawalRequestUi());

        

        // Validate the withdrawal request
        final Collection validations = validateWithdrawalRequest(withdrawalRequestUi);
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("doSaveAndExit> Found [").append(validations.size())
                    .append("] validations.").toString());
        } // fi

        if (!validations.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuffer("doSaveAndExit> Found [").append(

                validations.size()).append("] validations - sending to forward [").append(
                        ACTION_FORWARD_ERROR).append("].").toString());
            } // fi

            // Store validations and return to page
            setErrorsInSession(request, validations);
            refreshLockIfAvailable(request, withdrawalForm);
            return forwards.get( ACTION_FORWARD_ERROR);
        }

        // Convert the types
        withdrawalRequestUi.convertToBean();
        final WithdrawalRequest withdrawalRequest = withdrawalRequestUi.getWithdrawalRequest();
        logger.debug(new StringBuffer("doSaveAndExit> Saving with object [\n").append(
                withdrawalRequest).append("\n]\n").toString());

        // Call the biz tier for processing.

        // final WithdrawalRequest result =
        getWithdrawalServiceDelegate().save(withdrawalRequest);
        
      

        final WithdrawalRequest result = withdrawalRequest;
        // Check for business validations
        if (!(result.isValidToProcess())) {
            refreshLockIfAvailable(request, withdrawalForm);
			try {
				stopWatch.stop();
				if (logger.isInfoEnabled()) {
					logger
							.info(new StringBuffer(
									"doSaveAndExit> Exiting with business errors - time duration [")
									.append(stopWatch.toString()).append("]")
									.toString());
				}
			} catch (IllegalStateException e) {
				final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
						+ Thread.currentThread().toString();
				logger.error(message);
			}
            String forward=handleBusinessErrors( request, result, withdrawalRequestUi);
            return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        } else {
            logger.debug("doSaveAndExit> No mesages found, successful.");
        } // fi


        // check permissions and contract statuses
        int errorCode = checkPermissionsAndStatuses(request, withdrawalForm);
        if (errorCode != 0) {
            refreshLockIfAvailable(request, withdrawalForm);
            String forward= handlePermissionsOrStatusChanged( request, errorCode);
            return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
        releaseLock(request, withdrawalForm);

        // Clean the form
        withdrawalForm.clean();

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

    /**
     * doSendForReview is called when the page 'send for review' button is pressed.
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
    @RequestMapping(value = "/entryStep2/",params= {"action=send for review"} , method =  {RequestMethod.POST}) 
   	public String doSendForReview(@Valid @ModelAttribute("withdrawalForm") WithdrawalForm withdrawalForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   	throws IOException,ServletException, SystemException {
    	
    	if(bindingResult.hasErrors()){
 	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
 	       if(errDirect!=null){
 	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
 	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
 	       }
 		}
    
        final StopWatch stopWatch = new StopWatch();
		try {
			logger.info("doSendForReview> Entry - starting timer.");
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        
        // Get the clean withdrawal request
        final WithdrawalRequestUi withdrawalRequestUi = cleanWithdrawalRequest(withdrawalForm
                .getWithdrawalRequestUi());

        // Validate the withdrawal request
        final Collection validations = validateWithdrawalRequest(withdrawalRequestUi);
        if (!validations.isEmpty()) {
            logger.debug("doSendForReview> Web validation errors found.");
            refreshLockIfAvailable(request, withdrawalForm);
            return forwards.get( ACTION_FORWARD_ERROR);
        }

        // Convert the types
        withdrawalRequestUi.convertToBean();
        final WithdrawalRequest withdrawalRequest = withdrawalRequestUi.getWithdrawalRequest();
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("doSendForReview> Sending for review with object [")
                    .append(withdrawalRequestUi.getWithdrawalRequest()).append("]").toString());
        } // fi
        withdrawalRequest.setPrincipal(getUserProfile(request).getPrincipal());
        // Call the biz tier for processing.
        final WithdrawalRequest result = getWithdrawalServiceDelegate().sendForReview(
                withdrawalRequest);

        // Check for business validations
        if (!(result.isValidToProcess())) {
            refreshLockIfAvailable(request, withdrawalForm);
			try {
				stopWatch.stop();
				if (logger.isInfoEnabled()) {
					logger
							.info(new StringBuffer(
									"doSendForReview> Exiting with business errors - time duration [")
									.append(stopWatch.toString()).append("]")
									.toString());
				}
			} catch (IllegalStateException e) {
				final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
						+ Thread.currentThread().toString();
				logger.error(message);
			}
            String forward= handleBusinessErrors( request, result, withdrawalRequestUi);
            return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        } else {
            logger.debug("doSendForReview> No messages found, successful.");
        } // fi

        // Store recalculated object
        storeWithdrawalRequest(result, request);

        withdrawalForm.setActionInvoked(ACTION_FORWARD_SEND_FOR_REVIEW);
        releaseLock(request, withdrawalForm);

		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer(
						"doSendForReview> Exiting - time duration [").append(
						stopWatch.toString()).append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        return forwards.get( ACTION_FORWARD_SEND_FOR_REVIEW);
    }

    /**
     * doSendForApproval is called when the page 'send for approval' button is pressed.
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
    @RequestMapping(value = "/entryStep2/",params= {"action=send for approval"} , method =  {RequestMethod.POST}) 
   	public String doSendForApproval(@Valid @ModelAttribute("withdrawalForm") WithdrawalForm withdrawalForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   	throws IOException,ServletException, SystemException {
    	
    	if(bindingResult.hasErrors()){
 	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
 	       if(errDirect!=null){
 	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
 	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
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
        final WithdrawalRequestUi withdrawalRequestUi = cleanWithdrawalRequest(withdrawalForm
                .getWithdrawalRequestUi());

        // Validate the withdrawal request
        final Collection validations = validateWithdrawalRequest(withdrawalRequestUi);
        if (!validations.isEmpty()) {
            refreshLockIfAvailable(request, withdrawalForm);
            return forwards.get( ACTION_FORWARD_ERROR);
        }

        // Convert the types
        withdrawalRequestUi.convertToBean();
        final WithdrawalRequest withdrawalRequest = withdrawalRequestUi.getWithdrawalRequest();
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("doSendForApproval> Sending for approval with object [")
                    .append(withdrawalRequestUi.getWithdrawalRequest()).append("]").toString());
        } // fi

        // Call the biz tier for processing.
        final WithdrawalRequest result = getWithdrawalServiceDelegate().sendForApproval(
                withdrawalRequest);

        // Check for business validations
        if (!(result.isValidToProcess())) {
            refreshLockIfAvailable(request, withdrawalForm);
			try {
				stopWatch.stop();
				if (logger.isInfoEnabled()) {
					logger
							.info(new StringBuffer(
									"doSendForApproval> Exiting with business errors - time duration [")
									.append(stopWatch.toString()).append("]")
									.toString());
				}
			} catch (IllegalStateException e) {
				final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
						+ Thread.currentThread().toString();
				logger.error(message);
			}
            String forward= handleBusinessErrors( request, result, withdrawalRequestUi);
            return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        } // fi

        // Store recalculated object
        storeWithdrawalRequest(result, request);
        withdrawalForm.setActionInvoked(ACTION_FORWARD_SEND_FOR_APPROVAL);

        // check permissions and contract statuses
        int errorCode = checkPermissionsAndStatuses(request, withdrawalForm);
        if (errorCode != 0) {
            String forward= handlePermissionsOrStatusChanged( request, errorCode);
            return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
        releaseLock(request, withdrawalForm);

		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer(
						"doSendForApproval> Exiting - time duration [").append(
						stopWatch.toString()).append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        return forwards.get( ACTION_FORWARD_SEND_FOR_APPROVAL);
    }

    /**
     * doApprove is called when the page 'approve' button is pressed.
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
    @RequestMapping(value = "/entryStep2/",params= {"action=approve"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
   	public String doApprove(@Valid @ModelAttribute("withdrawalForm") WithdrawalForm withdrawalForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   	throws IOException,ServletException, SystemException {
    	
    	if(bindingResult.hasErrors()){
 	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
 	       if(errDirect!=null){
 	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
 	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
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
        final WithdrawalRequestUi withdrawalRequestUi = cleanWithdrawalRequest(withdrawalForm
                .getWithdrawalRequestUi());

        // Validate the withdrawal request
        final Collection validations = validateWithdrawalRequest(withdrawalRequestUi);
        if (!validations.isEmpty()) {
            refreshLockIfAvailable(request, withdrawalForm);
            return forwards.get( ACTION_FORWARD_ERROR);
        }

        // Convert the types
        withdrawalRequestUi.convertToBean();
        final WithdrawalRequest withdrawalRequest = withdrawalRequestUi.getWithdrawalRequest();
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("doApprove> Approving with object [").append(
                    withdrawalRequestUi.getWithdrawalRequest()).append("]").toString());
        } // fi

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
				    
					   if(StringUtils.isNotEmpty(controlBlock) && controlBlock.contains("STP SUCCESSFUL")) {
	    			       String transactionId = controlBlock.substring(controlBlock.indexOf(":") + 1).trim();
	    			       if(StringUtils.isNotEmpty(transactionId) && transactionId.length() > 9)
	        		        {
	        		         transactionId = transactionId.substring(0,10);
	        		         controlBlock = controlBlock+new WithdrawalSubmissionToAWD().sendSubmissionToAWD(result, transactionId);//send the details of successful submission to AWD via API call
	        		        }
	    			       WithdrawalWebUtil.logSTP(withdrawalRequest, controlBlock, this.getClass().getName(), "doApprove");
						   // this will move the submission to STP'ed State (w8)

					   }else {
						   WithdrawalWebUtil.logSTP(withdrawalRequest, controlBlock+" , Email Routed To AWD ", this.getClass().getName(), "doApprove");	
					   } 
				    }else {
				    	WithdrawalWebUtil.logSTP(withdrawalRequest, controlBlock+" , Email Routed To AWD ", this.getClass().getName(), "doApprove");	
				    } 
				
				} catch (Exception e) {
					WithdrawalWebUtil.logSTP(withdrawalRequest, e.toString()+" , Email Routed To AWD ", this.getClass().getName(), "doApprove");
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
            refreshLockIfAvailable(request, withdrawalForm);
			try {
				stopWatch.stop();
				if (logger.isInfoEnabled()) {
					logger
							.info(new StringBuffer(
									"doApprove> Exiting with business errors - time duration [")
									.append(stopWatch.toString()).append("]")
									.toString());
				}
			} catch (IllegalStateException e) {
				final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
						+ Thread.currentThread().toString();
				logger.error(message);
			}
             
           String  forward= handleBusinessErrors( request, result, withdrawalRequestUi);
            return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        } // fi
        if (BooleanUtils.isFalse(result.getIsLegaleseConfirmed())) {
            if (logger.isDebugEnabled()) {
                logger.debug("doApprove> Need to confirm legalese.");
            } // fi
            result.setIsLegaleseConfirmed(Boolean.TRUE);
            refreshLockIfAvailable(request, withdrawalForm);
            // storeWithdrawalRequest(result, request);
            return forwards.get(ACTION_FORWARD_LEGALESE);
        }

        // Store recalculated object
        storeWithdrawalRequest(result, request);
        withdrawalForm.setActionInvoked(ACTION_FORWARD_APPROVE);

        // doReadyForEntryEmail(withdrawalForm, request, result);

      //  resetToken(request);

        // check permissions and contract statuses
        int errorCode = checkPermissionsAndStatuses(request, withdrawalForm);
        if (errorCode != 0) {
            String forward= handlePermissionsOrStatusChanged( request, errorCode);
            return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
        releaseLock(request, withdrawalForm);

		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer(
						"doApprove> Exiting - time duration [").append(
						stopWatch.toString()).append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        return forwards.get( ACTION_FORWARD_APPROVE);
    }

    /**
     * doCalculate is called when the page 'calculate' button is pressed.
     * 
     * @param mapping The action mapping.
     * @param actionForm The action form.
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @return ActionForward The forward to process.
     * @throws IOException When an IO problem occurs.
     * @throws ServletException When an Servlet problem occurs.
     * @throws SystemException When an generic application problem occurs.
     * @throws SQLException 
     */
    @RequestMapping(value = "/entryStep2/",params= {"action=calculate"} , method =  {RequestMethod.POST}) 
   	public String doCalculate(@Valid @ModelAttribute("withdrawalForm") WithdrawalForm withdrawalForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   	throws IOException,ServletException, SystemException, SQLException {
    	
    	if(bindingResult.hasErrors()){
 	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
 	       if(errDirect!=null){
 	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
 	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
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
        

        //retain Federal Tax Value
        withdrawalForm.setActionInvoked(ACTION_CALCULATE);
        // Get the clean withdrawal request
        final WithdrawalRequestUi withdrawalRequestUi = cleanWithdrawalRequest(withdrawalForm
                .getWithdrawalRequestUi());

        // Convert the types
        withdrawalRequestUi.convertToBean();
        final WithdrawalRequest withdrawalRequest = withdrawalRequestUi.getWithdrawalRequest();
        
        if(withdrawalRequest.getPaymentTo().equals(WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION)){
          	new WithdrawalIrsDistributionCodesUtil().getIrsDistributionCodeList(withdrawalForm.getLookupData(), withdrawalForm, withdrawalRequest); 
        	constructPayeeTaxesFlags(withdrawalForm , withdrawalRequest);
        }
        //Log to MRL EL_TRANSACTION
        String traceMsg = new StringBuffer()
        .append("Withdrawal Recalculated for Contract Id = ")
        .append(withdrawalRequest.getContractId())
        .append("; Contract Name = ")
        .append(withdrawalRequest.getContractName())
        .append("; First Name = ")
        .append(withdrawalRequest.getFirstName())
        .append("; Last Name = ")
        .append(withdrawalRequest.getLastName())
        .append("; MoneyTypes = ")
        .append(withdrawalRequest.getMoneyTypes()).toString();
		logWebActivity(this.getClass().getName(), "doCalculate", "withdrawalCalculate",
				traceMsg, getUserProfile(request), logger,
				interactionLog, logRecord);
        
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("doCalculate> Recalculating with object [").append(
                    withdrawalRequestUi.getWithdrawalRequest()).append("]").toString());
        }

        // Call the biz tier for processing.
        getWithdrawalServiceDelegate().recalculate(withdrawalRequest);
        
        if(withdrawalRequest.getPaymentTo().equals(WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION)){
        	getWithdrawalServiceDelegate().updatedPayeesForMultipleDestination(withdrawalRequest);
        }
        // Values have changed, so we push them back into the UI object.
        // Note: References haven't changed, so we don't need to create new objects.
        withdrawalRequestUi.convertFromBean();

        // Need to update money type defaults
        updateMoneyTypeDefaults(withdrawalRequestUi);

        // Check if we should reset our recalculation flag
        if (!withdrawalRequest.doErrorCodesExist()) {
            withdrawalRequestUi.getWithdrawalRequest().setRecalculationRequired(false);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("doCalculate> Post-recalculation request is [").append(
                    withdrawalRequestUi).append("]").toString());
        }

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer(
                    "doCalculate> Post recalculate WithdrawalAmountType collection is [").append(
                    withdrawalForm.getLookupData().get(
                            CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE)).append("]").toString());
            logger.debug("doCalculate> " + withdrawalForm.getLookupData().toString());
        }

        // Check for business validations
        if (!(withdrawalRequest.isValidToProcess())) {
            refreshLockIfAvailable(request, withdrawalForm);
			try {
				stopWatch.stop();
				if (logger.isInfoEnabled()) {
					logger
							.info(new StringBuffer(
									"doCalculate> Exiting with business errors - time duration [")
									.append(stopWatch.toString()).append("]")
									.toString());
				}
			} catch (IllegalStateException e) {
				final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
						+ Thread.currentThread().toString();
				logger.error(message);
			}
             String forward= handleBusinessErrors( request, withdrawalRequest, withdrawalRequestUi);
            return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        } // fi

        // Store recalculated object
        storeWithdrawalRequest(withdrawalRequest, request);
        refreshLockIfAvailable(request, withdrawalForm);

		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer(
						"doCalculate> Exiting - time duration [").append(
						stopWatch.toString()).append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message); 
		}
        return forwards.get(ACTION_FORWARD_RECALCULATE);
    }

    /**
     * doDelete is called when the page 'delete' button is pressed.
     * 
     * @param mapping The action mapping.
     * @param actionForm The action form.
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @return ActionForward The forward to process.
     * @throws SystemException When an generic application problem occurs.
     */
    @RequestMapping(value = "/entryStep2/",params= {"action=delete"} , method =  {RequestMethod.POST}) 
   	public String doDelete(@Valid @ModelAttribute("withdrawalForm") WithdrawalForm withdrawalForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   	throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
 	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
 	       if(errDirect!=null){
 	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
 	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
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
       

        // Get the clean withdrawal request
        final WithdrawalRequestUi withdrawalRequestUi = cleanWithdrawalRequest(withdrawalForm
                .getWithdrawalRequestUi());

        // Convert the types
        withdrawalRequestUi.convertToBean();
        final WithdrawalRequest withdrawalRequest = withdrawalRequestUi.getWithdrawalRequest();
        logger.debug("doDelete> Deleting.");

        // Set the user that deleted the request.
        final UserProfile userProfile = getUserProfile(request);
        final Integer currentUserId = new Integer(String.valueOf(userProfile.getPrincipal()
                .getProfileId()));
        withdrawalRequest.setLastUpdatedById(currentUserId);

        // Call the biz tier for processing.
        getWithdrawalServiceDelegate().delete(withdrawalRequest);

        // Check if we had validation errors
        if (!CollectionUtils.isEmpty(withdrawalRequest.getErrorCodes())) {

			try {
				stopWatch.stop();
				if (logger.isInfoEnabled()) {
					logger
							.info(new StringBuffer(
									"doDelete> Exiting with business errors - time duration [")
									.append(stopWatch.toString()).append("]")
									.toString());
				}
			} catch (IllegalStateException e) {
				final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
						+ Thread.currentThread().toString();
				logger.error(message);
			}
            String forward= handleBusinessErrors( request, withdrawalRequest, withdrawalRequestUi);
            return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }

        releaseLock(request, withdrawalForm);
        // reset the token
       // resetToken(request);

        logger.debug("doDelete> Released Lock, Delete finished.");

        // check permissions and contract statuses
        int errorCode = checkPermissionsAndStatuses(request, withdrawalForm);
        if (errorCode != 0) {
            String forward= handlePermissionsOrStatusChanged( request, errorCode);
            return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }

        // Clean the form
        withdrawalForm.clean();

		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer(
						"doDelete> Exiting - time duration [").append(
						stopWatch.toString()).append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        return forwards.get(ACTION_FORWARD_DELETE);
    }

    /**
     * Cleans the withdrawal request to ensure that any fields that should be blank / defaulted due
     * to driver fields are handled appropriately. This operation is handled redundantly from the
     * page level javascript to ensure the object graph is in a correct state.
     * 
     * @param withdrawalRequest The withdrawal request UI object that requires cleaning.
     * @return WithdrawalRequestUi The cleaned withdrawal request UI object.
     */
    private WithdrawalRequestUi cleanWithdrawalRequest(final WithdrawalRequestUi withdrawalRequest) {

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer(
                    "cleanWithdrawalRequest> Cleaning withdrawal request object [").append(
                    withdrawalRequest).append("].").toString());
        }

        return withdrawalRequest;
    }

    /**
     * Updates the withdrawal request money type defaults and updates the original amount type field
     * which is a driver of those defaults.
     * 
     * @param withdrawalRequest The withdrawal request UI object that requires updating.
     */
    private void updateMoneyTypeDefaults(final WithdrawalRequestUi withdrawalRequest) {

        // Update original amount type
        withdrawalRequest.getWithdrawalRequest().setOriginalAmountTypeCode(
                withdrawalRequest.getWithdrawalRequest().getAmountTypeCode());
    }

    /**
     * Performs simple and type validations on the withdrawal request UI object prior to doing bean
     * conversion. Many of these validations are redundancies from the page level javascript
     * validations to ensure that the object graph is in a valid state.
     * 
     * @param withdrawalRequestUi The withdrawal request UI object that requires validating.
     * @return Collection<ValidationError> The collection of validation errors.
     */
    private Collection<ValidationError> validateWithdrawalRequest(
            final WithdrawalRequestUi withdrawalRequestUi) {

        final Collection<ValidationError> errors = new ArrayList<ValidationError>();

        return errors;
    }

    private void checkStep1DriverChangedMessage(final HttpServletRequest request,
            final WithdrawalRequest withdrawalRequest, final boolean showStep1FieldsChangedMessage) {

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer(
                    "checkStep1DriverChangedMessage> Entry with setupStep1DriverInitalMessage[")
                    .append(showStep1FieldsChangedMessage).append("]").toString());
        }

        // Check if we should add the step 1 driver fields changed message
        if (showStep1FieldsChangedMessage) {
            withdrawalRequest.addMessage(new WithdrawalMessage(
                    WithdrawalMessageType.STEP_1_DRIVER_FIELDS_CHANGED_SINCE_SAVE));
        }
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
    public WithdrawalRequest getStep2WithdrawalRequest(final WithdrawalForm actionForm,
            final HttpServletRequest request) throws DistributionServiceException, SystemException {

        // If request UI is null, then return null to signify bookmarking attempt
        final WithdrawalRequestUi withdrawalRequestUi = actionForm.getWithdrawalRequestUi();
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer(
                    "getStep2WithdrawalRequest> Retrieved withdrawal request UI from the form [")
                    .append(withdrawalRequestUi).append("].").toString());
        }
        if (withdrawalRequestUi == null) {
            if (logger.isDebugEnabled()) {
                logger
                        .debug(new StringBuffer(
                                "getStep2WithdrawalRequest> Bookmark detected - withdrawal request ui is null [")
                                .append(withdrawalRequestUi).append("].").toString());
            }
            return null;
        }
        // Check that status is new or draft (not post draft)
        if (withdrawalRequestUi.getWithdrawalRequest().getIsPostDraft()) {
            if (logger.isDebugEnabled()) {
                logger
                        .debug(new StringBuffer(
                                "getStep2WithdrawalRequest> Bookmark detected - withdrawal request is postdraft [")
                                .append(withdrawalRequestUi.getWithdrawalRequest().getStatusCode())
                                .append("].").toString());
            }
            return null;
        }

        // Check if we are allowed on step 2 (proper page navigation)
        if (!actionForm.isStep2Allowed()) {
            if (logger.isDebugEnabled()) {
                logger
                        .debug(new StringBuffer(
                                "getStep2WithdrawalRequest> Bookmark detected - withdrawal request is not allowed on step 2 [")
                                .append(actionForm.getPageAllowed()).append("].").toString());
            }
            return null;
        }

        // Check if any required data is null
        final WithdrawalRequest withdrawalRequest = withdrawalRequestUi.getWithdrawalRequest();
        if ((withdrawalRequest == null) || withdrawalRequest.getContractId() == null
                || withdrawalRequest.getEmployeeProfileId() == null
                || (withdrawalRequest.getContractId().compareTo(ZERO) == 0)) {
            if (logger.isDebugEnabled()) {
                logger
                        .debug(new StringBuffer(
                                "getStep2WithdrawalRequest> Bookmark detected: Either withdrawal request is null [")
                                .append(withdrawalRequest == null).append("] or contract ID [")
                                .append(
                                        (withdrawalRequest == null) ? null : withdrawalRequest
                                                .getContractId()).append(
                                        "] is null or 0 or profile ID[").append(
                                        (withdrawalRequest == null) ? null : withdrawalRequest
                                                .getEmployeeProfileId()).append("] is null.")
                                .toString());
            }
            return null;
        }
        return withdrawalRequest;
    }
    /**
     * Construct tax falg and Participant info for Multipayee
     * @param withdrawalForm
     * @param wdRequest
     * @throws SystemException
     * @throws SQLException
     */
    private void constructPayeeTaxesFlags(WithdrawalForm withdrawalForm,WithdrawalRequest wdRequest) throws SystemException, SQLException{
    	
        
        List<TaxesFlag> taxesFlag = getWithdrawalServiceDelegate().getPayessTaxFlag();
        boolean nonTaxableFlag = false ;
        boolean botFlag = false;
        if(wdRequest.getRothBal() == null && wdRequest.getParticipantNonRoth() == null){
        	botFlag = true;
        }
        
        for (TaxesFlag taxFlag : taxesFlag){
        	String NON_TAXABLE = "N";
            String ROTH_NON_TAX = "N";
            String TAXABLE = "N";
            String ROTH_TAXABLE ="N";
            String ROTH_IRA ="N";
            //Payee 1
            if(taxFlag.getPayeeCategory().equals(wdRequest.getParticipant()) ||
         			taxFlag.getPayeeCategory().equals(wdRequest.getParticipantRoth()) || taxFlag.getPayeeCategory().equals(wdRequest.getParticipantAftrTax())){
         		if(withdrawalForm.getWithdrawalRequestUi().getPayDirectlyTome() != null && withdrawalForm.getWithdrawalRequestUi().getPayDirectlyTome().equals("PA") && taxFlag.getPayeeCategory().trim().equals("PA")){
         			
         		if(taxFlag.getNonTaxable().trim().equalsIgnoreCase("Y")){
         			NON_TAXABLE = taxFlag.getNonTaxable();
         		}if(taxFlag.getTaxable().trim().equalsIgnoreCase("Y")){
         			TAXABLE = taxFlag.getTaxable();
         		}if(taxFlag.getRothNonTax().trim().equalsIgnoreCase("Y")){
         			ROTH_NON_TAX = taxFlag.getRothNonTax();
         		}if(taxFlag.getRothTaxable().trim().equalsIgnoreCase("Y")){
         			ROTH_TAXABLE = taxFlag.getRothTaxable();
         		}if(taxFlag.getRothIRA().trim().equalsIgnoreCase("Y")){
         			ROTH_IRA = taxFlag.getRothIRA();
         		}
         		String flagValue = "{\"Roth_IRA\":\""+ROTH_IRA+"\",\"Non_Taxable\":\""+NON_TAXABLE+"\",\"Taxable\":\""+TAXABLE+"\",\"Roth_Non_Tax\":\""+ROTH_NON_TAX+"\",\"Roth_Taxable\":\""+ROTH_TAXABLE+"\"}";
             	wdRequest.setParticipantTaxesFlag(flagValue);
             	if(withdrawalForm.getWithdrawalRequestUi().getPayDirectlyTomeAmount() !=null && withdrawalForm.getWithdrawalRequestUi().getPayDirectlyTomeAmount().trim().length()>0  ){
             	wdRequest.setPayDirectlyTomeAmount(new BigDecimal( withdrawalForm.getWithdrawalRequestUi().getPayDirectlyTomeAmount()));
             	}wdRequest.setParticipantDetails("{"+"PA"+":"+"Participant"+"}");
         		}
         		else if(withdrawalForm.getWithdrawalRequestUi().getPayDirectlyTome() != null && withdrawalForm.getWithdrawalRequestUi().getPayDirectlyTome().equals("PAR")&& taxFlag.getPayeeCategory().trim().equals("PAR")){
         			wdRequest.setTotalRothBalFlag(true);
         		if(taxFlag.getNonTaxable().trim().equalsIgnoreCase("Y")){
         			NON_TAXABLE = taxFlag.getNonTaxable();
         		}if(taxFlag.getTaxable().trim().equalsIgnoreCase("Y")){
         			TAXABLE = taxFlag.getTaxable();
         		}if(taxFlag.getRothNonTax().trim().equalsIgnoreCase("Y")){
         			ROTH_NON_TAX = taxFlag.getRothNonTax();
         		}if(taxFlag.getRothTaxable().trim().equalsIgnoreCase("Y")){
         			ROTH_TAXABLE = taxFlag.getRothTaxable();
         		}if(taxFlag.getRothIRA().trim().equalsIgnoreCase("Y")){
         			ROTH_IRA = taxFlag.getRothIRA();
         		}
         		
         		String flagValue = "{\"Roth_IRA\":\""+ROTH_IRA+"\",\"Non_Taxable\":\""+NON_TAXABLE+"\",\"Taxable\":\""+TAXABLE+"\",\"Roth_Non_Tax\":\""+ROTH_NON_TAX+"\",\"Roth_Taxable\":\""+ROTH_TAXABLE+"\"}";
             	wdRequest.setParticipantTaxesFlag(flagValue);
             	wdRequest.setParticipantDetails("{"+"PA"+":"+"Participant Roth"+"}");
         	}else if( withdrawalForm.getWithdrawalRequestUi().getPayDirectlyTome() != null && withdrawalForm.getWithdrawalRequestUi().getPayDirectlyTome().equals("PAAT") && taxFlag.getPayeeCategory().trim().equals("PAAT")){
         		wdRequest.setNonTaxableFlag(true);
         		nonTaxableFlag = true;
         		if(taxFlag.getNonTaxable().trim().equalsIgnoreCase("Y")){
         			NON_TAXABLE = taxFlag.getNonTaxable();
         		}if(taxFlag.getTaxable().trim().equalsIgnoreCase("Y")){
         			TAXABLE = taxFlag.getTaxable();
         		}if(taxFlag.getRothNonTax().trim().equalsIgnoreCase("Y")){
         			ROTH_NON_TAX = taxFlag.getRothNonTax();
         		}if(taxFlag.getRothTaxable().trim().equalsIgnoreCase("Y")){
         			ROTH_TAXABLE = taxFlag.getRothTaxable();
         		}if(taxFlag.getRothIRA().trim().equalsIgnoreCase("Y")){
         			ROTH_IRA = taxFlag.getRothIRA();
         		}
         		  if( withdrawalForm.getWithdrawalRequestUi().getPayDirectlyTome() != null && withdrawalForm.getWithdrawalRequestUi().getPayDirectlyTome().equals("PAAT") && taxFlag.getPayeeCategory().trim().equals("PAAT")){
             		   // As no Non Taxbable Non Roth Balance is available
             			if(null == wdRequest.getParticipantNonRoth() ){
             				NON_TAXABLE="N";
             			}
             			// As no Roth Balance is available
             			if(null ==wdRequest.getRothBal() ){
             				ROTH_NON_TAX="N";
             			}
                 }
         		String flagValue = "{\"Roth_IRA\":\""+ROTH_IRA+"\",\"Non_Taxable\":\""+NON_TAXABLE+"\",\"Taxable\":\""+TAXABLE+"\",\"Roth_Non_Tax\":\""+ROTH_NON_TAX+"\",\"Roth_Taxable\":\""+ROTH_TAXABLE+"\"}";
         		wdRequest.setParticipantTaxesFlag(flagValue);
         		wdRequest.setParticipantDetails("{"+"PA"+":"+"Participant After Tax"+"}");
         	}
  
           }
          
            //Payee 2
            if(taxFlag.getPayee().trim().equals("RI")){
            	//user select Traditional IRA in Taxable and non taxable non roth Section
            	if((withdrawalForm.getWithdrawalRequestUi().getTbCategory() !=null && withdrawalForm.getWithdrawalRequestUi().getTbCategory().equals("TIRA")) && ( withdrawalForm.getWithdrawalRequestUi().getNratCategory() != null && withdrawalForm.getWithdrawalRequestUi().getNratCategory().equals("TIRA")))
            	{
            		NON_TAXABLE = "Y";
            		TAXABLE = "Y";
            		
            	}
            	//user select Traditional IRA in Taxable  Section
            	else if ((withdrawalForm.getWithdrawalRequestUi().getTbCategory() !=null && withdrawalForm.getWithdrawalRequestUi().getTbCategory().equals("TIRA"))){
            		TAXABLE = "Y";
            		if(botFlag || wdRequest.getParticipantNonRoth() == null ){
            			NON_TAXABLE = "Y";
            		}
            		
            	}
            	//user Select Non tax non roth section
            	else if (( withdrawalForm.getWithdrawalRequestUi().getNratCategory() != null && withdrawalForm.getWithdrawalRequestUi().getNratCategory().equals("TIRA"))){
            		NON_TAXABLE = "Y";
            		
            	}
            	String flagValue = "{\"Roth_IRA\":\""+ROTH_IRA+"\",\"Non_Taxable\":\""+NON_TAXABLE+"\",\"Taxable\":\""+TAXABLE+"\",\"Roth_Non_Tax\":\""+ROTH_NON_TAX+"\",\"Roth_Taxable\":\""+ROTH_TAXABLE+"\"}";
            	wdRequest.setTraditionalIRAFlag(flagValue);
            	wdRequest.setTraditionalIRAPayee("{"+"RI"+":"+"Traditional IRA"+"}");	
            	
            }
           // Payee 3
            if(taxFlag.getPayee().trim().equals("RR")){
            	//User Select ROTH IRA in Taxable , Roth Balance and Non roth non taxable Section
            	if(( withdrawalForm.getWithdrawalRequestUi().getTbCategory() !=null && withdrawalForm.getWithdrawalRequestUi().getTbCategory().equals("RIRA")) && (withdrawalForm.getWithdrawalRequestUi().getRbCategory() != null && withdrawalForm.getWithdrawalRequestUi().getRbCategory().equals("RIRA")) 
            			&&( withdrawalForm.getWithdrawalRequestUi().getNratCategory() != null && withdrawalForm.getWithdrawalRequestUi().getNratCategory().equals("RIRA")) ){
        	   
            		NON_TAXABLE = "Y";
            		TAXABLE ="Y";
            		ROTH_IRA="Y";
            		ROTH_TAXABLE ="Y";
            		ROTH_NON_TAX ="Y";
            		
            	}
            	//User Select ROTH IRA in Taxable , Roth Balance Section
            	else if (( withdrawalForm.getWithdrawalRequestUi().getTbCategory() !=null && withdrawalForm.getWithdrawalRequestUi().getTbCategory().equals("RIRA")) && (withdrawalForm.getWithdrawalRequestUi().getRbCategory() != null && withdrawalForm.getWithdrawalRequestUi().getRbCategory().equals("RIRA"))){
            		
            		TAXABLE ="Y";
            		ROTH_IRA ="Y";
            		ROTH_TAXABLE ="Y";
            		if(!nonTaxableFlag){
            			ROTH_NON_TAX ="Y";
            		}
            	}
            	//User Select ROTH IRA in Roth Balance and Non roth non taxable Section
            	else if (( withdrawalForm.getWithdrawalRequestUi().getNratCategory() !=null && withdrawalForm.getWithdrawalRequestUi().getNratCategory().equals("RIRA")) && (withdrawalForm.getWithdrawalRequestUi().getRbCategory() != null && withdrawalForm.getWithdrawalRequestUi().getRbCategory().equals("RIRA"))){
            		
            		NON_TAXABLE ="Y";
            		ROTH_IRA ="Y";
            		ROTH_TAXABLE ="Y";
            		ROTH_NON_TAX = "Y";
            	}
            	//User Select ROTH IRA in Taxable and Non roth non taxable Section
            	else if (( withdrawalForm.getWithdrawalRequestUi().getNratCategory() !=null && withdrawalForm.getWithdrawalRequestUi().getNratCategory().equals("RIRA")) && (withdrawalForm.getWithdrawalRequestUi().getTbCategory() != null && withdrawalForm.getWithdrawalRequestUi().getTbCategory().equals("RIRA"))){
            		
            		TAXABLE ="Y";
            		ROTH_IRA ="Y";
            		NON_TAXABLE = "Y";
            	}
            	//User Select ROTH IRA in Taxable  Section
            	else if ( withdrawalForm.getWithdrawalRequestUi().getTbCategory() !=null && withdrawalForm.getWithdrawalRequestUi().getTbCategory().equals("RIRA")){
            		TAXABLE ="Y";
            		ROTH_IRA ="Y";
            		if(botFlag || wdRequest.getParticipantNonRoth() == null){
            			NON_TAXABLE = "Y";
            		}
            	}
            	//User Select ROTH IRA in Roth Balance  Section
            	else if ( withdrawalForm.getWithdrawalRequestUi().getRbCategory() !=null && withdrawalForm.getWithdrawalRequestUi().getRbCategory().equals("RIRA")){
            		ROTH_IRA="Y";
            		ROTH_TAXABLE ="Y";
            		if(!nonTaxableFlag){
            			ROTH_NON_TAX ="Y";
            		}
            	}
            	//User Select ROTH IRA in Non roth non taxable Section
            	else if ( withdrawalForm.getWithdrawalRequestUi().getNratCategory() !=null && withdrawalForm.getWithdrawalRequestUi().getNratCategory().equals("RIRA")){
            		ROTH_IRA ="Y";
            		NON_TAXABLE ="Y";
            		
            	}
            	String flagValue = "{\"Roth_IRA\":\""+ROTH_IRA+"\",\"Non_Taxable\":\""+NON_TAXABLE+"\",\"Taxable\":\""+TAXABLE+"\",\"Roth_Non_Tax\":\""+ROTH_NON_TAX+"\",\"Roth_Taxable\":\""+ROTH_TAXABLE+"\"}";
        		wdRequest.setRothIRAFlag(flagValue);
        		wdRequest.setRothIRAPayee("{"+"RR"+":"+"Roth IRA"+"}");
           }
            
            //payee 4
            if(taxFlag.getPayee().trim().equals("EP")){
            	//User Select EMP Qualified in Taxable , Roth Balance and Non roth non taxable Section
            	if(( withdrawalForm.getWithdrawalRequestUi().getTbCategory() !=null && withdrawalForm.getWithdrawalRequestUi().getTbCategory().equals("EQP")) && (withdrawalForm.getWithdrawalRequestUi().getRbCategory() != null && withdrawalForm.getWithdrawalRequestUi().getRbCategory().equals("EQP")) 
             	  &&( withdrawalForm.getWithdrawalRequestUi().getNratCategory() != null && withdrawalForm.getWithdrawalRequestUi().getNratCategory().equals("EQP")) ){
             	   
             	   NON_TAXABLE = "Y";
             	   TAXABLE ="Y";
             	   ROTH_TAXABLE ="Y";
             	   ROTH_NON_TAX ="Y";
                }
            	//User Select EMP Qualified in Taxable , Roth Balance  Section
            	else if (( withdrawalForm.getWithdrawalRequestUi().getTbCategory() !=null && withdrawalForm.getWithdrawalRequestUi().getTbCategory().equals("EQP")) && (withdrawalForm.getWithdrawalRequestUi().getRbCategory() != null && withdrawalForm.getWithdrawalRequestUi().getRbCategory().equals("EQP"))){
            		
            		TAXABLE ="Y";
            		ROTH_TAXABLE ="Y";
            		if(!nonTaxableFlag){
            			ROTH_NON_TAX ="Y";
            		}
            	}
            	//User Select EMP Qualified in Non Tax , Roth Balance  Section
            	else if (( withdrawalForm.getWithdrawalRequestUi().getNratCategory() !=null && withdrawalForm.getWithdrawalRequestUi().getNratCategory().equals("EQP")) && (withdrawalForm.getWithdrawalRequestUi().getRbCategory() != null && withdrawalForm.getWithdrawalRequestUi().getRbCategory().equals("EQP"))){
            		
            		NON_TAXABLE ="Y";
            		ROTH_TAXABLE ="Y";
            		if(!nonTaxableFlag){
            			ROTH_NON_TAX ="Y";
            		}
            	}
            	//User Select EMP Qualified in Taxable  and Non roth non taxable Section
            	else if (( withdrawalForm.getWithdrawalRequestUi().getNratCategory() !=null && withdrawalForm.getWithdrawalRequestUi().getNratCategory().equals("EQP")) && (withdrawalForm.getWithdrawalRequestUi().getTbCategory() != null && withdrawalForm.getWithdrawalRequestUi().getTbCategory().equals("EQP"))){
            		
            		TAXABLE ="Y";
            		NON_TAXABLE = "Y";
            	}
            	//User Select EMP Qualified in Taxable Section alone
            	else if ( withdrawalForm.getWithdrawalRequestUi().getTbCategory() !=null && withdrawalForm.getWithdrawalRequestUi().getTbCategory().equals("EQP")){
            		TAXABLE ="Y";
            		if(botFlag ||  wdRequest.getParticipantNonRoth() == null ){
            			NON_TAXABLE = "Y";
            		}
            	}
            	//User Select EMP Qualified in Roth Balance  Section alone
            	else if ( withdrawalForm.getWithdrawalRequestUi().getRbCategory() !=null && withdrawalForm.getWithdrawalRequestUi().getRbCategory().equals("EQP")){
            		
            		ROTH_TAXABLE ="Y";
            		if(!nonTaxableFlag){
            			ROTH_NON_TAX ="Y";
            		}
            	}
            	//User Select EMP Qualified in Non roth non taxable Section alone
            	else if ( withdrawalForm.getWithdrawalRequestUi().getNratCategory() !=null && withdrawalForm.getWithdrawalRequestUi().getNratCategory().equals("EQP")){
            		NON_TAXABLE = "Y";
            	}

           	  String flagValue = "{\"Roth_IRA\":\""+ROTH_IRA+"\",\"Non_Taxable\":\""+NON_TAXABLE+"\",\"Taxable\":\""+TAXABLE+"\",\"Roth_Non_Tax\":\""+ROTH_NON_TAX+"\",\"Roth_Taxable\":\""+ROTH_TAXABLE+"\"}";
           	  wdRequest.setEmpQulifiedPlanFlag(flagValue);
           	  wdRequest.setEmpQulifiedPlanPayee("{"+"EP"+":"+"Employer Sponsored Qualified Plan"+"}");
            }
                   
        }
	}
}