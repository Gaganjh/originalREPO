package com.manulife.pension.ps.web.withdrawal;

import static com.manulife.pension.ps.web.withdrawal.WebConstants.WITHDRAWAL_REQUEST_METADATA_UI_ATTRIBUTE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.delegate.ParticipantServiceDelegate;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.ps.web.withdrawal.util.WithdrawalIrsDistributionCodesUtil;
import com.manulife.pension.ps.web.withdrawal.util.WithdrawalWebUtil;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;
import com.manulife.pension.service.withdrawal.mock.MockWithdrawalFactory;
import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestNote;
import com.manulife.pension.util.content.GenericException;

/**
 * WithdrawalViewRequestAction is the action class for the view withdrawal request page.
 * 
 * @author Aurelian Penciu
 * 
 * @action-url /withdrawal/viewRequest
 * @action-form WithdrawalForm
 * @action-forward default /withdrawal/viewRequest.jsp
 * @action-forward finished [redirect] /do/withdrawal/loanAndWithdrawalRequests (if the user presses
 *                 cancel)
 * @action-forward delete [redirect] /do/withdrawal/loanAndWithdrawalRequests
 */

@Controller
@RequestMapping( value = "/withdrawal")

public class WithdrawalViewRequestController extends BaseWithdrawalController {
	@ModelAttribute("withdrawalForm")
	public WithdrawalForm populateForm()
	{
		return new WithdrawalForm();
		}
	
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static{
	forwards.put("default","/withdrawal/view/viewRequest.jsp");
	forwards.put("finished","redirect:/do/withdrawal/loanAndWithdrawalRequests/");
	forwards.put("delete","redirect:/do/withdrawal/loanAndWithdrawalRequests/");
	forwards.put("error","/withdrawal/view/viewRequest.jsp");
	forwards.put("lockError","redirect:/do/withdrawal/loanAndWithdrawalRequests/");
	}

    private static final Logger logger = Logger.getLogger(WithdrawalViewRequestController.class);

    /**
     * doDefault method is declared for the calling of printpdf Method from BaseWithdrawalAction
     */ 
    
public String doDefault( AutoForm actionForm,ModelMap model,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException{

   logger.debug("doDefault> Entry.");
   WithdrawalForm form=(WithdrawalForm)actionForm;
	final StopWatch stopWatch = new StopWatch();
	try {
		if (logger.isInfoEnabled()) {
			logger
					.info("doDefault> Entry - starting timer.xxxxxxxxxxxxxxxxxxxxx");
		} // fi
		stopWatch.start();
	} catch (IllegalStateException e) {
		final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
				+ Thread.currentThread().toString();
		logger.error(message);
	}
   

   // TODO: Determine what this does, and add docs.
   form.clearPageAllowed();

   final WithdrawalRequest withdrawalRequest;

   // TODO: Change so that mock objects are ignored dependant on property in common config
   if (StringUtils.isNotBlank(request.getParameter("mock"))) {
       withdrawalRequest = MockWithdrawalFactory.getMockWithdrawal(request.getParameterMap());
   } else {
       if (!isPrinterFriendlyClicked(request)) {
           preserveSubmissionId(request);
       }
       logger.debug("doDefault> Loading withdrawal request.");
       try {
           withdrawalRequest = loadWithdrawalRequest(request);
       } catch (final DistributionServiceException e) {
           throw new SystemException(e, this.getClass().getName(), "doDefault",
                   "DistributionServiceException occured.");
       }
       if (logger.isDebugEnabled()) {
           logger.debug(new StringBuffer("doDefault> Loaded withdrawal request [").append(
                   withdrawalRequest).append("].").toString());
       }
//       if (withdrawalRequest == null) {
//           logger.debug("doDefault> Withdrawal request is null - possible bookmark detected.");
//           form.reset(mapping, request);
//           return findForward(mapping, ACTION_FORWARD_BOOKMARK_DETECTED);
//       }
   } // fi
//GIFL 1C      
   Principal principal = getUserProfile(request).getPrincipal();
   String participantId = (withdrawalRequest.getParticipantId()).toString();
   String contractNumber = String.valueOf(withdrawalRequest.getContractInfo().getContractId());
   String giflStatus = ParticipantServiceDelegate.getInstance().getParticipantGIFLStatus(participantId, contractNumber);
   if(giflStatus.equals(Constants.GIFL_SELECTED)){
   	request.setAttribute("isParticipantGIFLEnabled", new Boolean(true));	
   }else{
   	request.setAttribute("isParticipantGIFLEnabled", new Boolean(false));
   }
//       request.setAttribute("isParticipantGIFLEnabled", new Boolean(true));
   //GIFL 1C end
   
   // Check if request has expired
   final Date now = DateUtils.truncate(new Date(), Calendar.DATE);
   if (logger.isDebugEnabled()) {
       logger.debug(new StringBuffer("doDefault> Comparing expiry date [").append(
               withdrawalRequest.getExpirationDate()).append("] to current date [")
               .append(now).append("].").toString());
   }
   if (now.after(withdrawalRequest.getExpirationDate()) 
   		&& !isAllowedToViewAfterExpiration(withdrawalRequest)) {
       final Collection<GenericException> errors = new ArrayList<GenericException>();
       errors.add(new GenericException(ErrorCodes.WITHDRAWAL_REQUEST_HAS_EXPIRED_LIST_PAGE));
       SessionHelper.setErrorsInSession(request, errors);
       return forwards.get(ACTION_FORWARD_ERROR);
   }

   final WithdrawalRequestUi requestUi = new WithdrawalRequestUi(withdrawalRequest);
   
   // Set the action form data
   requestUi.setViewAction(true);
   form.setWithdrawalRequestUi(requestUi);

   // Set contract information if TPA
   if (getUserProfile(request).getRole().isTPA()) {
       setTpaContractInformation(request, withdrawalRequest);
   }
   model.addAttribute("mapping", "viewItem");
   // Set permissions
   setupPermissions(request,model, withdrawalRequest);

   // Lookup if the contract has non-fully vested money types.
   requestUi.setContractHasNonFullyVestedMoneyTypes(WithdrawalWebUtil
           .getContractHasNonFullyVestedMoneyTypes(withdrawalRequest.getContractId()));
   
   // Get Look up data
   final Map lookupData = WithdrawalServiceDelegate.getInstance().getLookupData(null,
           StringUtils.EMPTY, getAllLookupKeys());

   // set Look-up data
   form.setLookupData(lookupData);
   form.parseRequestStatusCollection();
   // retrieve the list of User Profile IDs and fetch the user names
   final ArrayList<Integer> userProfileIds = new ArrayList<Integer>();
   for (final WithdrawalRequestNote note : form.getWithdrawalRequestUi()
           .getWithdrawalRequest().getReadOnlyAdminToParticipantNotes()) {
       if (note.getCreatedById() != null && !userProfileIds.contains(note.getCreatedById())) {
           userProfileIds.add(note.getCreatedById());
       }
   }
   for (final WithdrawalRequestNote note : form.getWithdrawalRequestUi()
           .getWithdrawalRequest().getReadOnlyAdminToAdminNotes()) {
       if (note.getCreatedById() != null && !userProfileIds.contains(note.getCreatedById())) {
           userProfileIds.add(note.getCreatedById());
       }
   }
   form.setUserNames(getWithdrawalServiceDelegate().getUserNamesForIds(userProfileIds));

   // end of mock data

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
 * doDefault method for the request of view Page
 */ 
    @RequestMapping(value = "/viewRequest/",  method =  {RequestMethod.GET}) 
	public String doDefaultView(@Valid @ModelAttribute("withdrawalForm") WithdrawalForm form, BindingResult bindingResult,ModelMap model,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
    	
    	 if(bindingResult.hasErrors()){
 	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
 	       if(errDirect!=null){
 	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
 	             return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);//if input forward not //available, provided default
 	       }
 		}
    

        logger.debug("doDefault> Entry.");

		final StopWatch stopWatch = new StopWatch();
		try {
			if (logger.isInfoEnabled()) {
				logger
						.info("doDefault> Entry - starting timer.xxxxxxxxxxxxxxxxxxxxx");
			} // fi
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        

        // TODO: Determine what this does, and add docs.
        form.clearPageAllowed();

        final WithdrawalRequest withdrawalRequest;

        // TODO: Change so that mock objects are ignored dependant on property in common config
        if (StringUtils.isNotBlank(request.getParameter("mock"))) {
            withdrawalRequest = MockWithdrawalFactory.getMockWithdrawal(request.getParameterMap());
        } else {
            if (!isPrinterFriendlyClicked(request)) {
                preserveSubmissionId(request);
            }
            logger.debug("doDefault> Loading withdrawal request.");
            try {
                withdrawalRequest = loadWithdrawalRequest(request);
            } catch (final DistributionServiceException e) {
                throw new SystemException(e, this.getClass().getName(), "doDefault",
                        "DistributionServiceException occured.");
            }
            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuffer("doDefault> Loaded withdrawal request [").append(
                        withdrawalRequest).append("].").toString());
            }
//            if (withdrawalRequest == null) {
//                logger.debug("doDefault> Withdrawal request is null - possible bookmark detected.");
//                form.reset(mapping, request);
//                return findForward(mapping, ACTION_FORWARD_BOOKMARK_DETECTED);
//            }
        } // fi
//GIFL 1C      
        Principal principal = getUserProfile(request).getPrincipal();
        String participantId = (withdrawalRequest.getParticipantId()).toString();
        String contractNumber = String.valueOf(withdrawalRequest.getContractInfo().getContractId());
        String giflStatus = ParticipantServiceDelegate.getInstance().getParticipantGIFLStatus(participantId, contractNumber);
        if(giflStatus.equals(Constants.GIFL_SELECTED)){
        	request.setAttribute("isParticipantGIFLEnabled", new Boolean(true));	
        }else{
        	request.setAttribute("isParticipantGIFLEnabled", new Boolean(false));
        }
 //       request.setAttribute("isParticipantGIFLEnabled", new Boolean(true));
        //GIFL 1C end
        
        // Check if request has expired
        final Date now = DateUtils.truncate(new Date(), Calendar.DATE);
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("doDefault> Comparing expiry date [").append(
                    withdrawalRequest.getExpirationDate()).append("] to current date [")
                    .append(now).append("].").toString());
        }
        if (now.after(withdrawalRequest.getExpirationDate()) 
        		&& !isAllowedToViewAfterExpiration(withdrawalRequest)) {
            final Collection<GenericException> errors = new ArrayList<GenericException>();
            errors.add(new GenericException(ErrorCodes.WITHDRAWAL_REQUEST_HAS_EXPIRED_LIST_PAGE));
            SessionHelper.setErrorsInSession(request, errors);
            return forwards.get(ACTION_FORWARD_ERROR);
        }

        final WithdrawalRequestUi requestUi = new WithdrawalRequestUi(withdrawalRequest);
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
        
        // Set the action form data
        requestUi.setViewAction(true);
        form.setWithdrawalRequestUi(requestUi);

        // Set contract information if TPA
        if (getUserProfile(request).getRole().isTPA()) {
            setTpaContractInformation(request, withdrawalRequest);
        }
        model.addAttribute("mapping", "viewItem");
        // Set permissions
        setupPermissions(request,model, withdrawalRequest);

        // Lookup if the contract has non-fully vested money types.
        requestUi.setContractHasNonFullyVestedMoneyTypes(WithdrawalWebUtil
                .getContractHasNonFullyVestedMoneyTypes(withdrawalRequest.getContractId()));
        
        // Get Look up data
        final Map lookupData = WithdrawalServiceDelegate.getInstance().getLookupData(null,
                StringUtils.EMPTY, getAllLookupKeys());

        // set Look-up data
        form.setLookupData(lookupData);
        form.parseRequestStatusCollection();
        // retrieve the list of User Profile IDs and fetch the user names
        final ArrayList<Integer> userProfileIds = new ArrayList<Integer>();
        for (final WithdrawalRequestNote note : form.getWithdrawalRequestUi()
                .getWithdrawalRequest().getReadOnlyAdminToParticipantNotes()) {
            if (note.getCreatedById() != null && !userProfileIds.contains(note.getCreatedById())) {
                userProfileIds.add(note.getCreatedById());
            }
        }
        for (final WithdrawalRequestNote note : form.getWithdrawalRequestUi()
                .getWithdrawalRequest().getReadOnlyAdminToAdminNotes()) {
            if (note.getCreatedById() != null && !userProfileIds.contains(note.getCreatedById())) {
                userProfileIds.add(note.getCreatedById());
            }
        }
        form.setUserNames(getWithdrawalServiceDelegate().getUserNamesForIds(userProfileIds));

        // end of mock data

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
     * doFinished is called when the page 'finished' button is pressed.
     * 
     * @param actionForm The action form.
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @return ActionForward The forward to process.
     * @throws SystemException When an generic application problem occurs.
     */
    @RequestMapping(value = "/viewRequest/", params= {"action=finished"}, method =  {RequestMethod.POST}) 
	public String doFinished(@Valid @ModelAttribute("withdrawalForm") WithdrawalForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
    	
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);//if input forward not //available, provided default
	       }
		}
   

        final StopWatch stopWatch = new StopWatch();
		try {
			logger.info("doFinished> Entry - starting timer.");
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        request.getSession().removeAttribute(
                Constants.WITHDRAWAL_VIEW_PAGE + "." + Constants.WITHDRAWAL_REQUEST_SUBMISSIONID);
        request.getSession().removeAttribute(
                Constants.WITHDRAWAL_VIEW_PAGE + "." + Constants.WITHDRAWAL_REQUEST_CONTRACTID);
        request.getSession().removeAttribute(
                Constants.WITHDRAWAL_VIEW_PAGE + "." + Constants.WITHDRAWAL_REQUEST_PROFILEID);
		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer(
						"doFinished> Exiting - time duration [").append(
						stopWatch.toString()).append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        return  forwards.get(ACTION_FORWARD_FINISHED);
    }

    /**
     * doDelete is called when the page 'delete' button is pressed. The method "cleans" the session
     * parameters under the viewItem namespace and invokes the generic doDelete implementation from
     * the super class.
     * 
     * @param actionForm The action form.
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @return ActionForward The forward to process.
     * @throws SystemException When an generic application problem occurs.
     */
    
    
    @RequestMapping(value = "/viewRequest/", params= {"action=delete"}, method =  {RequestMethod.POST}) 
	public String doDelete(@Valid @ModelAttribute("withdrawalForm") WithdrawalForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
    	
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);//if input forward not //available, provided default
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
        WithdrawalRequest withdrawalRequest;
        try {
            withdrawalRequest = loadWithdrawalRequest(request);
        } catch (final DistributionServiceException e) {
            throw new SystemException(e, this.getClass().getName(), "doDelete",
                    "DistributionServiceException occured.");
        }

        withdrawalRequest.setPrincipal(getUserProfile(request).getPrincipal());

        // This is used in delete for logging where the request came from.
        withdrawalRequest.setRequestInitiatedFromView(Boolean.TRUE);

        final WithdrawalRequestUi requestUi = new WithdrawalRequestUi(withdrawalRequest);

        // Set the action form data
       
        form.setWithdrawalRequestUi(requestUi);

        request.getSession().removeAttribute(
                Constants.WITHDRAWAL_VIEW_PAGE + "." + Constants.WITHDRAWAL_REQUEST_SUBMISSIONID);
        request.getSession().removeAttribute(
                Constants.WITHDRAWAL_VIEW_PAGE + "." + Constants.WITHDRAWAL_REQUEST_CONTRACTID);
        request.getSession().removeAttribute(
                Constants.WITHDRAWAL_VIEW_PAGE + "." + Constants.WITHDRAWAL_REQUEST_PROFILEID);

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
        String forward=super.doDelete( form, request, response);
        return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }

    private WithdrawalRequest loadWithdrawalRequest(final HttpServletRequest request)
            throws SystemException, DistributionServiceException {
        String submissionIdString = request.getParameter(WebConstants.SUBMISSION_ID_PARAMETER);
        if (StringUtils.isBlank(submissionIdString)) {
            submissionIdString = (String) request.getSession().getAttribute(
                    Constants.WITHDRAWAL_VIEW_PAGE + "."
                            + Constants.WITHDRAWAL_REQUEST_SUBMISSIONID);
        }
        
        if (StringUtils.isBlank(submissionIdString)) {
            // Try to load the request from the meta data.
            final WithdrawalRequestMetaDataUi withdrawalRequestMetaDataUi = (WithdrawalRequestMetaDataUi) request
                    .getSession(false).getAttribute(WITHDRAWAL_REQUEST_METADATA_UI_ATTRIBUTE);
            
            if (withdrawalRequestMetaDataUi != null) {
                submissionIdString = withdrawalRequestMetaDataUi.getMetaData().getSubmissionId()
                        .toString();
            } // fi
        } // fi
        
        if (StringUtils.isBlank(submissionIdString)) {
            logger.debug("Submission Id is null:: Possible Bookmark detected");
            return null;
        }

        final Integer submissionId = Integer.valueOf(submissionIdString);
        final Integer userProfileId = new Integer((int) getUserProfile(request).getPrincipal()
                .getProfileId());
        if (userProfileId == null) {
            logger.debug("UserProfile Id is null::Possible Bookmark detected");
            return null;
        }

        final WithdrawalRequest withdrawalRequest = WithdrawalServiceDelegate.getInstance()
                .readWithdrawalRequestForView(submissionId, getUserProfile(request).getPrincipal());
        
        //Added to get Permissions for the user
        ContractInfo contractInfo = WithdrawalServiceDelegate.getInstance().
        					getContractInfo(withdrawalRequest.getContractId(), getUserProfile(request).getPrincipal());
        WithdrawalRequestUi.populatePermissions(contractInfo, getUserProfile(request).getPrincipal());
        withdrawalRequest.setContractInfo(contractInfo);
        return withdrawalRequest;
    }

    private void preserveSubmissionId(final HttpServletRequest request) {
        final String submissionId = request.getParameter(WebConstants.SUBMISSION_ID_PARAMETER);
        if (submissionId != null) {
            request.getSession(false).setAttribute(
                    Constants.WITHDRAWAL_VIEW_PAGE + "."
                            + Constants.WITHDRAWAL_REQUEST_SUBMISSIONID, submissionId);
        } // fi

    }
    // Fix for viewing Approved, Rerady for Entry and Denied withdrawal requests.
    private boolean isAllowedToViewAfterExpiration (WithdrawalRequest withdrawalRequest) {
    	String statusCode = withdrawalRequest.getStatusCode();
    	if(StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_APPROVED_CODE, statusCode) 
    			|| StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_DENIED_CODE, statusCode)
    			|| StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_READY_FOR_ENTRY_CODE, statusCode)) {
    		return true;
    	}
    	return false;
    }
    
    public String getLayoutKey(){
		return "/withdrawal/view/viewRequest.jsp";
	}

  @RequestMapping(value ="/viewRequest/", params= {"actionLabel=printPDF"}, method =  {RequestMethod.POST,RequestMethod.GET})
	public String doPrintPDF(@Valid @ModelAttribute("withdrawalForm") WithdrawalForm form,BindingResult bindingResult,ModelMap model,
			HttpServletRequest request,  HttpServletResponse response)
					throws IOException, ServletException, SystemException {
	
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				 return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);//if input forward not //available, provided default
			}
		}
		String forward=super.doPrintPDF(form,model,request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations as part of the CL#137697.
	 */
    @Autowired
	   private PSValidatorFWDefault  psValidatorFWDefault;
@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWDefault);
	}
}
