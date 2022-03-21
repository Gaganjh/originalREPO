package com.manulife.pension.ps.web.onlineloans;



import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.LoanServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.onlineloans.displayrules.AbstractDisplayRules;
import com.manulife.pension.ps.web.onlineloans.displayrules.EditDisplayRules;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.ps.web.withdrawal.LoanAndWithdrawalRequestsForm;
import com.manulife.pension.ps.web.withdrawal.WebConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.loan.LoanErrorCode;
import com.manulife.pension.service.loan.LoanMessage;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanActivities;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.withdrawal.valueobject.UserName;
import com.manulife.pension.validator.ValidationError;


@Controller
@RequestMapping( value ="/onlineloans")
@SessionAttributes({"loanForm","loanAndWithdrawalRequestsForm"})

public class LoanInitiateController extends AbstractLoanController {
	@ModelAttribute("loanForm") 
	public LoanForm populateForm() 
	{
		return new LoanForm();
	}
	
	@ModelAttribute("loanAndWithdrawalRequestsForm")
	public LoanAndWithdrawalRequestsForm populateWithdarawalForm() {
		return new LoanAndWithdrawalRequestsForm();
	}
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("default","/onlineloans/initiate.jsp");
		forwards.put("error","/onlineloans/initiate.jsp"); 
		forwards.put("draft","redirect:/do/onlineloans/draft/");
		forwards.put("pendingReview","redirect:/do/onlineloans/review/");
		forwards.put("pendingApproval","redirect:/do/onlineloans/approve/");
		forwards.put("view","/do/onlineloans/view/");
		forwards.put("lockError","redirect:/do/withdrawal/loanAndWithdrawalRequests/");
		forwards.put("toParticipantAccount","redirect:/do/participant/participantAccount/");
		forwards.put("toSearchSummary","redirect:/do/loan/searchSummary/?task=fromSession");
		forwards.put("toLoanAndWithdrawal","redirect:/do/withdrawal/loanAndWithdrawalRequests/");
		forwards.put("loanList","redirect:/do/withdrawal/loanAndWithdrawalRequests/");
		forwards.put("loanConfirmation","redirect:/do/onlineloans/confirmation/");
		
	}

	private static final Logger logger = Logger.getLogger(LoanInitiateController.class);
	protected String getCurrentForward() {
		return ACTION_FORWARD_INITIATE;
	}

	@Override
	protected AbstractDisplayRules getDisplayRules(UserProfile userProfile,
			UserRole userRoleWithPermissions, Loan loan,
			LoanPlanData loanPlanData, LoanParticipantData loanParticipantData,
			LoanSettings loanSettings, LoanActivities loanActivities,
			Map<Integer, UserName> userNames, Map<String, String> stateMap,
			Map<String, String> countryMap) {
		return new EditDisplayRules(userProfile, userRoleWithPermissions, loan,
				loanPlanData, loanParticipantData, loanSettings,
				loanActivities, userNames, stateMap, countryMap);
	}

	protected Loan getLoan(final AutoForm actionForm,
			final HttpServletRequest request) throws SystemException {

		/*
		 * First, check if there is already an loan object in the request. It's
		 * safe to use the request's loan because it's gone after each request.
		 */
		Loan loan = (Loan) request.getAttribute(REQ_LOAN_DATA);
		if (loan != null) {
			return loan;
		}

		UserProfile userProfile = getUserProfile(request);
		LoanForm form = (LoanForm) actionForm;

		/*
		 * Get contract ID from parameter, if it's not found, check the FORM
		 * itself and then the ID in the currently selected contract.
		 */
		Integer contractId = null;

		String contractIdStr = request.getParameter(PARAM_CONTRACT_ID);
		if (StringUtils.isBlank(contractIdStr)) {
			Contract contract = userProfile.getCurrentContract();
			contractId = contract.getContractNumber();
		} else {
			try {
				contractId = org.apache.commons.lang.math.NumberUtils
						.createInteger(contractIdStr);
			} catch (NumberFormatException e) {
				return null;
			}
		}
		loan = LoanServiceDelegate.getInstance().initiateLoan(
				form.getParticipantProfileId(), contractId,
				(int) userProfile.getPrincipal().getProfileId());
		
		if (loan != null) {
			// Need to set the BGA indicator as well as the Contract TPA permission to validate them in back-end
			populateAdditionalLoanData(contractId, request, loan);
			request.setAttribute(REQ_LOAN_DATA, loan);
		}

		return loan;
	}
	 
	 
	protected String preExecute(LoanForm actionForm,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	

        /* Preserve the last active page location, as the call to the superclass
         * removes it.  Required to allow navigation to return to Participant
         * search page if that's where the loan initiate action was invoked from
         */
        final String lastActivePageLocation = (String) request.getSession().getAttribute(
                WebConstants.LAST_ACTIVE_PAGE_LOCATION);

		String forward = super.preExecute( actionForm, request,
				response);
		
        request.getSession().setAttribute(WebConstants.LAST_ACTIVE_PAGE_LOCATION,
                lastActivePageLocation);

		if (forward != null) {
			return forward;
		}

		LoanForm form = (LoanForm) actionForm;

		/*
		 * Refresh the lock on every action.
		 */
		String lockErrorForward = acquireLockOrErrorForward(
				request, form);
		if (lockErrorForward != null) {
			return lockErrorForward;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@RequestMapping(value ="/initiate/",  method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("loanForm")LoanForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
		}
		actionForm.setSubmissionId(null);
		Loan loan = getLoan(actionForm, request);
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
		
		/*
		 * Clear the submission ID from the form so that we are getting a new
		 * loan request.
		 */
		
		if (hasInitializationError(loan)) {
			Set<ValidationError> validationErrors = LoanMessageHelper
					.toValidationError(loan.getMessages(), true, false);
			setMessagesInSession(request, validationErrors);
			String originator = request
					.getParameter(WebConstants.ORIGINATOR_PARAMETER);
			if (WebConstants.PARTICIPANT_ACCOUNT_ORIGINATOR.equals(originator)) {
				 forward = forwards.get( ACTION_FORWARD_BACK_TO_PARTICIPANT_ACCOUNT);
				Integer profileId = loan.getParticipantProfileId();
	            if (profileId != null) {
	            	
	            	 ControllerForward forwardWithProfile = new ControllerForward(forward, true); 
		                forwardWithProfile.setPath(forward+"?profileId=" + profileId.toString());
	            	
	            	
	                return forwardWithProfile.getPath();
	            } else {
	                return forward;
	            }				
			} else if (WebConstants.SEARCH_PARTICIPANT_ORIGINATOR
					.equals(originator)) {
				return forwards.get(ACTION_FORWARD_BACK_TO_SEARCH_SUMMARY);
			} else {
				return forwards.get(ACTION_FORWARD_BACK_TO_LOAN_AND_WITHDRAWAL);
			}
		} else {
			updateRequest(actionForm, request, true, loan, false, true);
		}
		return forwards.get( ACTION_FORWARD_DEFAULT);
	}

	private boolean hasInitializationError(Loan loan) {
		List<LoanMessage> errors = new ArrayList<LoanMessage>();
		errors.addAll(loan.getMessages());
		errors.addAll(loan.getErrors());

		LoanErrorCode[] errorCodeList = new LoanErrorCode[] {
				LoanErrorCode.LRK01_IS_OFF,
				LoanErrorCode.PARTICIPANT_CURRENT_ACCOUNT_BALANCE_IS_ZERO,
				LoanErrorCode.PARTICIPANT_STATUS_IS_NOT_ACTIVE,
				LoanErrorCode.PARTICIPANT_HAS_POSITIVE_PBA_BALANCE,
				LoanErrorCode.PARTICIPANT_NUMBER_OF_LOANS_EXCEEDED,
				LoanErrorCode.PARTICIPANT_HAS_PENDING_LOAN_REQUEST,
				LoanErrorCode.PARTICIPANT_DRAFT_LOAN_REQUEST_EXISTS,
				LoanErrorCode.FORWARD_UNREVERSED_LOAN_TRANSACTION_EXISTS,
				LoanErrorCode.LIA_ENABLED_FOR_PARTICIPANT,
				LoanErrorCode.PARTICIPANT_HAS_INSTALLMENT_WITHDRAWAL
				};

		for (LoanMessage error : errors) {
			for (LoanErrorCode errorCode : errorCodeList) {
				if (error.getErrorCode().equals(errorCode)) {
					return true;
				}
			}
		}
		return false;
	}	
	
	@RequestMapping(value ="/initiate/", params={"actionLabel=printLoanDocumentsPdf"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doPrintLoanDocumentsPdf(@Valid @ModelAttribute("loanForm")LoanForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
		}
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
		 forward=super.doPrintLoanDocumentsPdf( actionForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	@RequestMapping(value ="/initiate/",params={"action=showLoanPackagePdf","task=showLoanPackagePdf"}   , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doShowLoanPackagePdf(@Valid @ModelAttribute("loanForm")LoanForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
		}
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
		 forward=super.doShowLoanPackagePdf( actionForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	@RequestMapping(value ="/initiate/" ,params={"action=printPDF","task=printPDF"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doPrintPDF (@Valid @ModelAttribute("loanForm") LoanForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
		}
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
		 forward=super.doPrintPDF( actionForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

	
	
	@RequestMapping(value ="/initiate/", params={"actionLabel=save & exit"} , method =  {RequestMethod.POST}) 
	public String doSaveAndExit(@Valid @ModelAttribute("loanForm")LoanForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
		}
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
		 forward=super.doSaveAndExit(actionForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	@RequestMapping(value ="/initiate/", params={"action=SaveAndExit"} , method =  {RequestMethod.POST}) 
	public String doSaveExit(@Valid @ModelAttribute("loanForm")LoanForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
		}
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
		 forward=super.doSaveAndExit(actionForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	@RequestMapping(value ="/initiate/", params={"actionLabel=exit"} , method =  {RequestMethod.POST}) 
	public String doExit(@Valid @ModelAttribute("loanForm")LoanForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
		}
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
		 forward=super.doExit(actionForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	@RequestMapping(value ="/initiate/", params={"actionLabel=send for review"} , method =  {RequestMethod.POST}) 
	public String doSendReview(@Valid @ModelAttribute("loanForm")LoanForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
		}
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
		 forward=super.doSendForReview(actionForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	
	
	@RequestMapping(value ="/initiate/", params={"action=SendForReview"} , method =  {RequestMethod.POST}) 
	public String doSendForReview(@Valid @ModelAttribute("loanForm")LoanForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
		}
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
		 forward=super.doSendForReview(actionForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	
	@RequestMapping(value ="/initiate/", params={"action=SendForApproval"} , method =  {RequestMethod.POST}) 
	public String doSendApproval(@Valid @ModelAttribute("loanForm")LoanForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
		}
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
		forward=super.doSendForApproval(actionForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	@RequestMapping(value ="/initiate/", params={"actionLabel=send for approval"} , method =  {RequestMethod.POST}) 
	public String doSendForApproval(@Valid @ModelAttribute("loanForm")LoanForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
		}
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
		forward=super.doSendForApproval(actionForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	@RequestMapping(value ="/initiate/",params={"action=loanPackage"}   , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doLoanPackage(@Valid @ModelAttribute("loanForm")LoanForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
		}
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
		 forward=super.doLoanPackage( actionForm, request, response);
		 return StringUtils.contains(forward,'/')?forward:forwards.get(forward);  
	}
	
	@Autowired
	private PSValidatorFWDefault psValidatorFWDefault;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWDefault);
	}
}
