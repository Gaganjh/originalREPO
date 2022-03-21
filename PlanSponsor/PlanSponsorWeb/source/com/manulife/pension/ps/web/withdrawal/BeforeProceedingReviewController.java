package com.manulife.pension.ps.web.withdrawal;


import static com.manulife.pension.ps.web.withdrawal.WebConstants.WITHDRAWAL_REQUEST_METADATA_ATTRIBUTE;
import static com.manulife.pension.ps.web.withdrawal.WebConstants.WITHDRAWAL_REQUEST_METADATA_UI_ATTRIBUTE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.StopWatch;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.delegate.ParticipantServiceDelegate;
import com.manulife.pension.ps.service.submission.valueobject.Lock;
import com.manulife.pension.ps.service.submission.valueobject.Lockable;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.SubmissionServiceDelegate;
import com.manulife.pension.ps.web.tools.util.LockManager;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.withdrawal.util.WithdrawalBookmarkHelper;
import com.manulife.pension.ps.web.withdrawal.util.WithdrawalWebUtil;
import com.manulife.pension.service.contract.valueobject.LifeIncomeAmountDetailsVO;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.environment.valueobject.DeCodeVO;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.utility.SecurityHelper;
import com.manulife.pension.service.security.valueobject.UserPreferenceKeys;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;
import com.manulife.pension.service.withdrawal.domain.WithdrawalStateEnum;
import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMetaData;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;

/**
 * Defines the action based methods that support the Before Proceeding page.
 * 
 * @author dickand
 */
@Controller
@RequestMapping( value = "/withdrawal")
@SessionAttributes({"withdrawalBeforeProceedingForm"})

public class BeforeProceedingReviewController extends BaseWithdrawalController {
	@ModelAttribute("withdrawalBeforeProceedingForm") 
	public BeforeProceedingForm populateForm() 
	{
		return new BeforeProceedingForm();
		
	}
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static{
	forwards.put("default","/withdrawal/beforeProceedingReview.jsp");
	forwards.put("defaultForPrintFriendly","/withdrawal/beforeProceedingReview.jsp?printFriendly=true");
	forwards.put("cancel","redirect:/do/withdrawal/loanAndWithdrawalRequests/");
	forwards.put("next" ,"redirect:/do/withdrawal/review/");
	forwards.put("toParticipantAccount","redirect:/do/participant/participantAccount/");
	forwards.put("toLoanAndWithdrawal","redirect:/do/withdrawal/loanAndWithdrawalRequests/");
	forwards.put("toSearchSummary","redirect:/do/withdrawal/searchSummary/?task=fromSession");
	forwards.put("lockError","redirect:/do/withdrawal/loanAndWithdrawalRequests/");
	forwards.put("withdrawalsBookmark","redirect:/do/withdrawal/loanAndWithdrawalRequestsInit/");
	}
	
	
	
		

	
    public BeforeProceedingReviewController() {
        super(BeforeProceedingReviewController.class);
    }

    /**
     * {@inheritDoc}
     * @throws DistributionServiceException 
     * 
     * @implements com.manulife.pension.ps.web.controller.psAutoAction.doDefault
     */
    @RequestMapping(value ={"/beforeProceedingReview/"},  method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("withdrawalBeforeProceedingForm") BeforeProceedingForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException, DistributionServiceException {

    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
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
        WithdrawalRequestMetaData metaData = null;
       
        WithdrawalRequestMetaDataUi metaDataUi = (WithdrawalRequestMetaDataUi) request.getSession(
                false).getAttribute(WITHDRAWAL_REQUEST_METADATA_UI_ATTRIBUTE);

        request.getSession(false).removeAttribute(WITHDRAWAL_REQUEST_METADATA_UI_ATTRIBUTE);
        if (metaDataUi != null) {
            metaData = metaDataUi.getMetaData();
            request.getSession(false).setAttribute(WITHDRAWAL_REQUEST_METADATA_ATTRIBUTE, metaData);
        } else {
            metaData = (WithdrawalRequestMetaData) request.getSession(false).getAttribute(
                    WITHDRAWAL_REQUEST_METADATA_ATTRIBUTE);
        }
        if (metaData == null) {
            logger.debug("doDefault> Meta data was null.");
            return  forwards.get(WithdrawalBookmarkHelper.ACTION_FORWARD_BOOKMARK_DETECTED);
        }
        actionForm.setWithdrawalRequestMetaDataForPrint(metaData);

        // Load contract information
        final UserProfile userProfile = getUserProfile(request);
        final ContractInfo contractInfo = WithdrawalServiceDelegate.getInstance().getContractInfo(
                metaData.getContractId(),
                WithdrawalWebUtil.getPrincipalFromUserProfile(userProfile));

        // If the user is a TPA or Bundled GA Rep, and they haven't selected a contract, we have to load the userRole
        // differently for the given contract. Otherwise it's already all been loaded for us.
        final UserRole userRole;
        if (userProfile.getCurrentContract() == null) {
            userRole = SecurityHelper.getUserRoleForContract(userProfile.getPrincipal(),
                    userProfile.getRole(), contractInfo.getContractId());
        } else {
            userRole = userProfile.getRole();
        } // fi

        WithdrawalRequestUi.populatePermissions(contractInfo, userProfile.getPrincipal());

        
        final boolean isInitiate = StringUtils.isBlank(metaData.getStatusCode())
                || StringUtils.equals(metaData.getStatusCode(), WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);
        final boolean isReview = StringUtils.equals(metaData.getStatusCode(), WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_REVIEW_CODE);
        final boolean isApprove = StringUtils.equals(metaData.getStatusCode(), WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_APPROVAL_CODE);
        
        /* This is to prevent internal user withdrawal request URL hijacks.
         */
        
        if((userRole.isInternalUser() && isInitiate && !(userProfile.isBundledGACAR() && contractInfo.isBundledGaIndicator()))){
        	return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
        }
        
        /* If the user injects a valid withdrawal request initiation/ review/ approve 
         * URL from an invalid login, this piece of code will redirect the same to Home Page.
         */
        if ((isInitiate && !contractInfo.getHasInitiatePermission()) 
        		|| (isReview && !contractInfo.getHasReviewPermission())
        		|| (isApprove && !contractInfo.getHasApprovePermission())) {
        	return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
        } 

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("doDefault> Loaded contract info[").append(contractInfo)
                    .append("].").toString());
        }
        actionForm.setContractInfo(contractInfo);

        // Load employee information - need participant id
        final Employee employee = EmployeeServiceDelegate
                .getInstance(Constants.PS_APPLICATION_ID)
                .getEmployeeByProfileId(new Long(metaData.getProfileId()), metaData.getContractId(), null);
        if (employee == null) {
            throw new SystemException("doDefault> Employee was null.");
        }
        
        if (employee.getParticipantContract() == null) {
            throw new SystemException("doDefault> Participant contract was null.");
        }

        // Load participant information
        final ParticipantInfo participantInfo;
        try {
            participantInfo = WithdrawalServiceDelegate.getInstance().getParticipantInfo(
                    metaData.getProfileId(),
                    employee.getParticipantContract().getParticipantId().intValue(),
                    metaData.getContractId());
            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuffer("doDefault> Loaded participant info[").append(
                        participantInfo).append("].").toString());
            }
            actionForm.setParticipantInfo(participantInfo);

        } catch (final DistributionServiceException distributionServiceException) {
            throw new SystemException(distributionServiceException, this.getClass().getName(),
                    "doDefault", "Error while retrieving participant information.");
        }

        // Extract the TPA firm information from the participant info
        // TODO This should be cleaned up once the backend loads contract and participant info
        // objects correctly
        contractInfo.setHasATpaFirm(participantInfo.getThirdPartyAdminId());

        // Check for errors and return to originating page if any found
        final Collection<GenericException> errors = validateBeforeProceedingRequest(request,
                metaData, contractInfo, participantInfo);
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("doDefault> Loaded errors [").append(errors).append("]")
                    .toString());
        }
        if (CollectionUtils.isNotEmpty(errors)) {

            SessionHelper.setErrorsInSession(request, errors);
			try {
				stopWatch.stop();
				if (logger.isInfoEnabled()) {
					logger
							.info(new StringBuffer(
									"doDefault> Exiting with validation errors - time duration [")
									.append(stopWatch.toString()).append("]")
									.toString());
				}
			} catch (IllegalStateException e) {
				final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
						+ Thread.currentThread().toString();
				logger.error(message);
			}
            return getErrorActionForward( metaDataUi.getOriginator(), metaData.getProfileId());
        }

        // Get skip indicator
        final boolean skipBeforeProceedingIndicator = getSkipIndicator(request, metaData
                .getIsInitiate());
        metaData.setContractInfo(contractInfo);
        // Store meta data
        request.getSession(false).setAttribute(WITHDRAWAL_REQUEST_METADATA_ATTRIBUTE, metaData);

        // Set the lock.
        if (!createLock(request, metaData)) {
            // cannot obtain a lock, generate an error and go to the error page.
            final Collection<GenericException> lockErrors = new ArrayList<GenericException>(1);
            lockErrors.add(new ValidationError("LOCKED", ErrorCodes.WITHDRAWAL_LOCKED));
            setErrorsInSession(request, lockErrors);
			try {
				stopWatch.stop();
				if (logger.isInfoEnabled()) {
					logger
							.info(new StringBuffer(
									"doDefault> Exiting with lock error - time duration [")
									.append(stopWatch.toString()).append("]")
									.toString());
				}
			} catch (IllegalStateException e) {
				final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
						+ Thread.currentThread().toString();
				logger.error(message);
			}
			return  forwards.get(ACTION_FORWARD_LOCK_ERROR);
        } // fi

         String forward;
        // Check if this request is supposed to be print friendly
        final String printFriendly = (String) request.getSession().getAttribute(
                WebConstants.PRINTFRIENDLY_KEY);
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("doDefault> Found print friendly key [").append(
                    printFriendly).append("] in session.").toString());
        }
        if (StringUtils.isNotBlank(printFriendly)) {
            logger
                    .debug("doDefault> Request is print friendly - removing from session and using print friendly forward.");
            request.getSession().removeAttribute(WebConstants.PRINTFRIENDLY_KEY);
            forward = ACTION_FORWARD_DEFAULT_FOR_PRINT_FRIENDLY;
        } else {
            logger.debug("doDefault> Request is not print friendly - use normal forward.");

            
//          GIFL 1C      
            Principal principal = getUserProfile(request).getPrincipal();
            String participantId = (employee.getParticipantId()).toString();
            String contractNumber = String.valueOf(contractInfo.getContractId());
            String giflStatus = ParticipantServiceDelegate.getInstance().getParticipantGIFLStatus(participantId, contractNumber);
	        if(giflStatus.equals(Constants.GIFL_SELECTED)){
	        	request.getSession(false).setAttribute("isParticipantGIFLEnabled", new Boolean(true));	
	        }else{
	        	request.getSession(false).setAttribute("isParticipantGIFLEnabled", new Boolean(false));
            }
            //GIFL 1C end

            if (skipBeforeProceedingIndicator) {
                forward =  ACTION_FORWARD_NEXT;
            } else {
                forward = forwards.get (ACTION_FORWARD_DEFAULT);
                if ((StringUtils.contains(forward, "Review") && metaData.getIsInitiate())
                        || StringUtils.contains(forward, "Initiate")
                        && !metaData.getIsInitiate()) {
                    return forwards.get(WithdrawalBookmarkHelper.ACTION_FORWARD_BOOKMARK_DETECTED);
                }
            }
        }

        actionForm.setInitiatedByParticipant(false);
        if (StringUtils.equalsIgnoreCase(metaData.getInitiatedBy(), WithdrawalRequestMetaData.INITIATED_BY_PARTICIPANT)) {
        	actionForm.setInitiatedByParticipant(true);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("doDefault> Exiting with forward [").append(forward)
                    .append("].").toString());
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
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    
    }

    /**
     * Obtains the skip indicator for initiate or review from the user preferences.
     * 
     * @param request The user's request.
     * @param isInitiate True if the skip indicator for initiate should be returned; false if the
     *            skip indicator for review.
     * @return boolean - True if the before proceeding should be skipped.
     */
    private boolean getSkipIndicator(final HttpServletRequest request, final boolean isInitiate) {

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("getSkipIndicator> Entry with isInitiate [").append(
                    isInitiate).append("].").toString());
        }
        final UserProfile userProfile = getUserProfile(request);
        final boolean skipIndicator = StringUtils.equals(Constants.YES, userProfile
                .getPreferences().get(
                        isInitiate ? UserPreferenceKeys.INITIATE_PAGE_SKIP_INDICATOR
                                : UserPreferenceKeys.REVIEW_PAGE_SKIP_INDICATOR, Constants.NO));

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("getSkipIndicator> Exit with skip indicator [").append(
                    skipIndicator).append("].").toString());
        }

        return skipIndicator;
    }

    /**
     * Determines the correct action forward to return to the originating page.
     * 
     * @param mapping The action mapping.
     * @param originator The originating page.
     * @return ActionForward - The action forward representing the originating page.
     */
    
    
    
    
    public String getErrorActionForward( final String originator, Integer profileId) {

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("getErrorPage> Entry with originating page [").append(
                    originator).append("].").toString());
        }
         String forward;
        if (StringUtils.equals(WebConstants.PARTICIPANT_ACCOUNT_ORIGINATOR, originator)) {
            forward = forwards.get(ACTION_FORWARD_BACK_TO_PARTICIPANT_ACCOUNT);
            if (profileId != null) {
                ControllerForward forwardWithProfile = new ControllerForward(forward, false); 
                forwardWithProfile.setPath(forward+"?profileId=" + profileId.toString());
                return forwardWithProfile.getPath();
            } else {
            	return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
            }
        } else if (StringUtils.equals(WebConstants.WITHDRAWAL_LIST_ORIGINATOR, originator)) {
            forward = forwards.get(ACTION_FORWARD_BACK_TO_LOAN_AND_WITHDRAWAL);
        } else if (StringUtils.equals(WebConstants.SEARCH_PARTICIPANT_ORIGINATOR, originator)) {
            forward = forwards.get(ACTION_FORWARD_BACK_TO_SEARCH_SUMMARY);
        } else {
            forward =  ACTION_FORWARD_CANCEL_AND_EXIT;
        }

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("getErrorPage> Exit with forward [").append(forward)
                    .append("].").toString());
        }
        return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }

    /**
     * Validates the user request to initiate or review a withdrawal request.
     * 
     * @param request The user's request.
     * @param metaData The withdrawal request meta data.
     * @param contractInfo The withdrawal related contract information.
     * @param participantInfo The withdrawal related participant information.
     * @return Collection - The collection of errors found during validation.
     * @throws SystemException Thrown if an error occurs.
     * @throws DistributionServiceException 
     */
    private Collection<GenericException> validateBeforeProceedingRequest(
            final HttpServletRequest request, final WithdrawalRequestMetaData metaData,
            final ContractInfo contractInfo, final ParticipantInfo participantInfo)
            throws SystemException, DistributionServiceException {

        if (logger.isDebugEnabled()) {
            logger
                    .debug(new StringBuffer(
                            "validateBeforeProceedingRequest> Validating with metaData [").append(
                            metaData).append("], contractInfo [").append(contractInfo).append(
                            "], participantInfo [").append(participantInfo).append("].").toString());
        }

        // Do common validation - then initiate or review specific
        final Collection<GenericException> errors = new ArrayList<GenericException>();

        if (metaData.getIsNew()) {

            // Check that online withdrawals is enabled
            if (BooleanUtils.isFalse(contractInfo.getCsfAllowOnlineWithdrawals())) {
                errors.add(new GenericException(ErrorCodes.CONTRACT_ONLINE_WITHDRAWALS));
            }

            // Check for total status
            if (participantInfo.isParticipantStatusTotal()) {
                errors.add(new GenericException(ErrorCodes.PARTICIPANT_HAS_TOTAL_STATUS));
            }

            boolean rothFlag = false;
            final Collection <DeCodeVO> paymentToOptions = WithdrawalServiceDelegate.getInstance()
                    .getParticipantPaymentToOptions(participantInfo);
            
           for (DeCodeVO vo : paymentToOptions){
        	   
        	   if (vo.getCode().trim().equals(WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION)){
        		   rothFlag = true;
        	   }
           }
            System.out.println( " Roth Flag :;"+rothFlag);
           if( !rothFlag){
        	   
        	// Check for participant roth money
               if (participantInfo.getParticipantHasRothMoney()) {
                   errors.add(new GenericException(ErrorCodes.PARTICIPANT_HAS_ROTH_MONEY));
               }
           }

            // Check for participant PBA money
            if (participantInfo.getParticipantHasPbaMoney()) {
                errors.add(new GenericException(ErrorCodes.PARTICIPANT_HAS_PBA_MONEY));
            }

            // check for participant is applicable to LIA
			LifeIncomeAmountDetailsVO particpantLIADetails = ContractServiceDelegate
					.getInstance().getLIADetailsByProfileId(
							String.valueOf(metaData.getProfileId()));
			if(particpantLIADetails.isLIAAvailableForParticipant()){
				 errors.add(new GenericException(ErrorCodes.ERROR_PARTICPANT_APPLICABLE_TO_LIA));
			}
            
            // Check for existing request validations
            final Collection<WithdrawalRequest> existingRequests = getWithdrawalServiceDelegate()
                    .getWithdrawalRequests(metaData.getProfileId(), metaData.getContractId());
            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuffer("validateBeforeProceedingRequest> Loaded [").append(
                        CollectionUtils.size(existingRequests)).append("] existing requests.")
                        .toString());
            }
            if (CollectionUtils.isNotEmpty(existingRequests)) {

                final Date currentDate = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
                for (WithdrawalRequest withdrawalRequest : existingRequests) {

                    final String status = withdrawalRequest.getStatusCode();

                    // Check for post-draft requests (pending review, pending approval, approved)
                    if (StringUtils.equals(status,
                            WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_REVIEW_CODE)
                            || StringUtils.equals(status,
                                    WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_APPROVAL_CODE)
                            || StringUtils.equals(status,
                                    WithdrawalRequest.WITHDRAWAL_STATUS_APPROVED_CODE)) {

                        errors.add(new GenericException(
                                ErrorCodes.PARTICIPANT_HAS_MULTIPLE_WITHDRAWAL_REQUESTS));
                    }

                    // Check for ready for entry request that has not been processed
                    if (StringUtils.equals(status,
                            WithdrawalRequest.WITHDRAWAL_STATUS_READY_FOR_ENTRY_CODE)
                            && (withdrawalRequest.getExpectedProcessingDate() != null)
                            && currentDate.compareTo(withdrawalRequest.getExpectedProcessingDate()) <= 0) {
                        errors.add(new GenericException(
                                ErrorCodes.WITHDRAWAL_REQUEST_STATUS_SET_WITHIN_ONE_DAY));
                    }

                    // Check if current user already has a draft request for
                    // this participant
                    final Integer currentUserId = new Integer(String
                            .valueOf(getUserProfile(request).getPrincipal().getProfileId()));
                    if (logger.isDebugEnabled()) {
                        logger.debug(new StringBuffer(
                                "validateBeforeProceedingRequest> Comparing request status [")
                                .append(status).append("] with draft status [").append(
                                        WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE).append(
                                        "], last updated by id [").append(
                                        withdrawalRequest.getLastUpdatedById()).append(
                                        "] with current user id [").append(currentUserId).append(
                                        "], and requested profile id [").append(
                                        metaData.getProfileId()).append(
                                        "] with existing request employee profile id [").append(
                                        withdrawalRequest.getEmployeeProfileId()).append(
                                        "], created by ID is [").append(
                                        withdrawalRequest.getCreatedById()).append("]").toString());
                    }
                    if (StringUtils.equals(status, WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE)
                            && ObjectUtils.equals(withdrawalRequest.getLastUpdatedById(),
                                    currentUserId)
                            && ObjectUtils.equals(withdrawalRequest.getEmployeeProfileId(),
                                    metaData.getProfileId())) {

                        errors.add(new GenericException(
                                ErrorCodes.WITHDRAWAL_REQUEST_STATUS_IS_DRAFT));
                    }
                }
            }
        } else {

            // Check if request has expired if is request being edited
            final WithdrawalRequest withdrawalRequest;
            try {
                withdrawalRequest = WithdrawalServiceDelegate.getInstance()
                        .readWithdrawalRequestForEdit(metaData.getSubmissionId(),
                                getUserProfile(request).getPrincipal());
            } catch (DistributionServiceException e) {
                throw new SystemException(e,
                        "WithdrawalStep1Action:doDefault, could not get load withdrawal request.");
            }
            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuffer(
                        "validateBeforeProceedingRequest> Comparing withdrawal state [").append(
                        withdrawalRequest.getStatusCode()).append("] to denied state [").append(
                        WithdrawalStateEnum.DENIED.getStatusCode()).append("].").toString());
            }
            if (StringUtils.equals(withdrawalRequest.getStatusCode(), WithdrawalStateEnum.DENIED
                    .getStatusCode())) {

                if (logger.isDebugEnabled()) {
                    logger
                            .debug("validateBeforeProceedingRequest> Request has been concurrently denied by another user.");
                }
                errors.add(new GenericException(ErrorCodes.WITHDRAWAL_LOCKED));
            }
            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuffer(
                        "validateBeforeProceedingRequest> Comparing withdrawal state [").append(
                        withdrawalRequest.getStatusCode()).append("] to deleted state [").append(
                        WithdrawalStateEnum.DELETED.getStatusCode()).append("].").toString());
            }
            if (StringUtils.equals(withdrawalRequest.getStatusCode(), WithdrawalStateEnum.DELETED
                    .getStatusCode())) {

                if (logger.isDebugEnabled()) {
                    logger
                            .debug("validateBeforeProceedingRequest> Request has been concurrently deleted by another user.");
                }
                errors.add(new GenericException(ErrorCodes.WITHDRAWAL_LOCKED));
            }

            final Date currentDate = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuffer(
                        "validateBeforeProceedingRequest> Comparing expiry date [").append(
                        withdrawalRequest.getExpirationDate()).append("] to current date [")
                        .append(currentDate).append("].").toString());
            }
            if (currentDate.after(withdrawalRequest.getExpirationDate())) {

                errors
                        .add(new GenericException(
                                ErrorCodes.WITHDRAWAL_REQUEST_HAS_EXPIRED_LIST_PAGE));
            }
        }

        return errors;
    }

    /**
     * Called when the page 'cancel' button is pressed.
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
    
    @RequestMapping(value ={"/beforeProceedingReview/"}, params= {"action=cancel"}, method =  {RequestMethod.POST}) 
   	public String doCancel(@Valid @ModelAttribute("withdrawalBeforeProceedingForm") BeforeProceedingForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
			logger.info("doCancel> Entry - starting timer.");
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        // Retrieve meta information
        final WithdrawalRequestMetaData metaData = (WithdrawalRequestMetaData) request.getSession()
                .getAttribute(WebConstants.WITHDRAWAL_REQUEST_METADATA_ATTRIBUTE);
        request.getSession(false).removeAttribute(WITHDRAWAL_REQUEST_METADATA_ATTRIBUTE);
        //GIFL 1C
        request.getSession(false).removeAttribute("isParticipantGIFLEnabled");
        // Release any locks if they exist
        if (metaData.getSubmissionId() != null) {

            final Lockable withdrawalLockable = SubmissionServiceDelegate.getInstance()
                    .getLoanAndWithdrawalLockable(metaData.getSubmissionId(),
                            metaData.getContractId(), metaData.getProfileId());
            final Lock lock = SubmissionServiceDelegate.getInstance().checkLock(withdrawalLockable,
                    true);

            // If lock exists - try to unlock it
            if (lock != null) {
                withdrawalLockable.setLock(lock);
                final boolean unlocked = LockManager.getInstance(request.getSession(false))
                        .release(withdrawalLockable);
                // Check if lock was unlocked
                if (!unlocked) {
                    logger.warn(new StringBuffer(
                            "doCancel> Lock did not unlock for submission id [").append(
                            metaData.getSubmissionId()).append("].").toString());
                }
            } else {
                logger.warn(new StringBuffer(
                        "doCancel> Cannot release lock - lock is null for submission id [").append(
                        metaData.getSubmissionId()).append("].").toString());
            }
        }

         String forward = ACTION_FORWARD_CANCEL;
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("doCancel> Exit with forward [").append(forward).append(
                    "].").toString());
        }
		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer(
						"doCancel> Exiting - time duration [").append(
						stopWatch.toString()).append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }

    /**
     * Called when the page 'next' button is pressed.
     * 
     * @param mapping The action mapping.
     * @param actionForm The action form.
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @return ActionForward - The forward to process.
     * @throws IOException When an IO problem occurs.
     * @throws ServletException When an Servlet problem occurs.
     * @throws SystemException When an generic application problem occurs.
     */ 

    @RequestMapping(value = {"/beforeProceedingReview/"}, params= {"action=next"}, method =  {RequestMethod.POST}) 
   	public String doNext(@Valid @ModelAttribute("withdrawalBeforeProceedingForm") BeforeProceedingForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
			logger.info("doNext> Entry - starting timer.");
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        final BeforeProceedingForm beforeProceedingForm = (BeforeProceedingForm) actionForm;

        // Get meta information
        final WithdrawalRequestMetaData metaData = (WithdrawalRequestMetaData) request.getSession()
                .getAttribute(WebConstants.WITHDRAWAL_REQUEST_METADATA_ATTRIBUTE);

        // Check if indicator has been set
        if (StringUtils.equals(Constants.YES, beforeProceedingForm.getSkipIndicator())) {

            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuffer("doNext> Skip indicator set - updating [").append(
                        metaData.getIsInitiate() ? "Initiate" : "Review").append(
                        "] indicator in profile and database.").toString());
            }
            // Determine which indicator we are dealing with
            final String key = metaData.getIsInitiate() ? UserPreferenceKeys.INITIATE_PAGE_SKIP_INDICATOR
                    : UserPreferenceKeys.REVIEW_PAGE_SKIP_INDICATOR;

            // Update the profile
            final UserProfile profile = getUserProfile(request);
            profile.getPreferences().put(key, Constants.YES);

            // Update the database
            SecurityServiceDelegate.getInstance().updateUserPreference(profile.getPrincipal(), key,
                    Constants.YES);
        }

          String forward = ACTION_FORWARD_NEXT;
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("doNext> Exit with forward [").append(forward).append(
                    "].").toString());
        }
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
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }
    @RequestMapping(value ={"/beforeProceedingReview/"}, params= {"action=printPDF"}, method =  {RequestMethod.GET})
	   public String doPrintPDF( @ModelAttribute("withdrawalBeforeProceedingForm") BeforeProceedingForm form,BindingResult bindingResult,ModelMap model,
	             HttpServletRequest request,  HttpServletResponse response)
	            throws IOException, ServletException, SystemException {
    	
	   if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
	   String forward=super.doPrintPDF(form, model, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
}
    
}
