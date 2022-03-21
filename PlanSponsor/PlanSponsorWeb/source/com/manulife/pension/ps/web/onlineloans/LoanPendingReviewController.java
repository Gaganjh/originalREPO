package com.manulife.pension.ps.web.onlineloans;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.onlineloans.displayrules.AbstractDisplayRules;
import com.manulife.pension.ps.web.onlineloans.displayrules.EditDisplayRules;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanActivities;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.withdrawal.valueobject.UserName;
import com.manulife.pension.util.log.LogUtility;

@Controller
@RequestMapping(value ="/onlineloans")
@SessionAttributes({"loanForm"})

public class LoanPendingReviewController extends AbstractLoanController {

	@ModelAttribute("loanForm")
	public LoanForm populateForm() {
		return new LoanForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("default","/onlineloans/review.jsp");
		forwards.put("initiate","redirect:/do/onlineloans/initiate/");
		forwards.put("draft","redirect:/do/onlineloans/draft/");
		forwards.put("pendingApproval","redirect:/do/onlineloans/approve/");
		forwards.put("view","redirect:/do/onlineloans/view/");
		forwards.put("error","/onlineloans/review.jsp");
		forwards.put("lockError","/do/withdrawal/loanAndWithdrawalRequests/");
		forwards.put("toLoanAndWithdrawal","/do/withdrawal/loanAndWithdrawalRequests/");
		forwards.put("loanList","redirect:/do/withdrawal/loanAndWithdrawalRequests/");
		forwards.put("loanConfirmation","redirect:/do/onlineloans/confirmation/");
	}

	protected String getCurrentForward() {
		return ACTION_FORWARD_PENDING_REVIEW;
	}

	@Override
	protected AbstractDisplayRules getDisplayRules(UserProfile userProfile, UserRole userRoleWithPermissions, Loan loan,
			LoanPlanData loanPlanData, LoanParticipantData loanParticipantData, LoanSettings loanSettings,
			LoanActivities loanActivities, Map<Integer, UserName> userNames, Map<String, String> stateMap,
			Map<String, String> countryMap) {
		return new EditDisplayRules(userProfile, userRoleWithPermissions, loan, loanPlanData, loanParticipantData,
				loanSettings, loanActivities, userNames, stateMap, countryMap);
	}

	
	protected String preExecute(@Valid @ModelAttribute("loanForm") LoanForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		String forward = super.preExecute(actionForm, request, response);
		if (forward != null) {
			return forward;
		}
		/*
		 * Refresh the lock on every action.
		 */
		String lockErrorForward = acquireLockOrErrorForward(request, actionForm);
		if (lockErrorForward != null) {
			return lockErrorForward;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@RequestMapping(value = "/review/", method = {RequestMethod.GET})
	protected String doDefualt(@Valid @ModelAttribute("loanForm") LoanForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");// if input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		Loan loan = getLoan(actionForm, request);
		updateRequest(actionForm, request, true, loan, false, true);
		return forwards.get(ACTION_FORWARD_DEFAULT);
	}

	
	public String execute(@Valid @ModelAttribute("loanForm") LoanForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = getApplicationFacade(request).createLayoutBean(request, forwards.get(ACTION_FORWARD_DEFAULT));
		try {
			// Load loan related data and display rules.
			super.postExecute(actionForm, request, response);
			Loan loan = getLoan((AutoForm) actionForm, request);
			updateRequest((AutoForm) actionForm, request, true, loan, false, true);
			return forward;
		} catch (SystemException e) {
			logDebug("SystemException caught in PsAction:" + e.getUniqueId(), e);
			LogUtility.logSystemException(getApplicationFacade(request).getApplicationId(), e);
			request.setAttribute("errorCode", "1099");
			request.setAttribute("uniqueErrorId", e.getUniqueId());
			// forward to Error Page
			return forwards.get(SYSTEM_ERROR_PAGE);
		}
	}

	

	@RequestMapping(value = "/review/", params = {"action=loanPackage"}, method = { RequestMethod.POST, RequestMethod.GET })
	public String doLoanPackage(@Valid @ModelAttribute("loanForm") LoanForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		}
		String forward = super.doLoanPackage(actionForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	
	@RequestMapping(value ="/review/", params={"actionLabel=printLoanDocumentsPdf"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doPrintLoanDocumentsPdf (@Valid @ModelAttribute("loanForm") LoanForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
		String forward=super.doPrintLoanDocumentsPdf(actionForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	@RequestMapping(value ="/review/", params={"actionLabel=print loan documents"} , method =  {RequestMethod.POST,RequestMethod.GET})
	public String doPrintLoanDocuments(@Valid @ModelAttribute("loanForm") LoanForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
		String forward=super.doPrintLoanDocuments(actionForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	@RequestMapping(value ="/review/", params={"action=PrintLoanDocuments"} , method =  {RequestMethod.POST,RequestMethod.GET})
	public String doPrintLoanDocumentsFromWarningPopUp(@Valid @ModelAttribute("loanForm") LoanForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		return doPrintLoanDocuments(actionForm, bindingResult, request, response);
	}
	
	@RequestMapping(value ="/review/", params={"actionLabel=delete"} , method =  {RequestMethod.POST}) 
	public String doDelete(@Valid @ModelAttribute("loanForm")LoanForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
		String forward=super.doDelete(actionForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	@RequestMapping(value = "/review/", params = { "actionLabel=print" }, method = {RequestMethod.GET})
	public String doPrintPDF(@Valid @ModelAttribute("loanForm") LoanForm actionForm,
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
		String forward = super.doPrintPDF(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	
	@RequestMapping(value ="/review/", params={"actionLabel=deny"} , method =  {RequestMethod.POST}) 
	public String doDeny(@Valid @ModelAttribute("loanForm")LoanForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
		forward=super.doDeny(actionForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	@RequestMapping(value ="/review/", params={"action=Deny"} , method =  {RequestMethod.POST}) 
	public String doDenyProcess(@Valid @ModelAttribute("loanForm")LoanForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
		forward=super.doDeny(actionForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	@RequestMapping(value = "/review/", params = {"action=agree"}, method = {RequestMethod.POST, RequestMethod.GET})
	public String doAgree(@Valid @ModelAttribute("loanForm") LoanForm actionForm,BindingResult bindingResult, HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(
					CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(
						CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards
						.get(errDirect) : forwards.get("default");// if input
																	// forward
																	// not
																	// //available,
																	// provided
																	// default
			}
		}
		forward = super.doAgree(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards
				.get(forward);
	}
	
	@RequestMapping(value ="/review/", params={"action=SendForApproval"} , method =  {RequestMethod.POST}) 
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
	
	@RequestMapping(value ="/review/", params={"actionLabel=send for approval"} , method =  {RequestMethod.POST}) 
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
	@RequestMapping(value ="/review/", params={"actionLabel=send for acceptance"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doSendForAcceptance(@Valid @ModelAttribute("loanForm")LoanForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
		forward=super.doSendForAcceptance(actionForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	@RequestMapping(value ="/review/", params={"action=SendForAcceptance"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doSendForAcceptanceNew(@Valid @ModelAttribute("loanForm")LoanForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
		forward=super.doSendForAcceptance(actionForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	
	@RequestMapping(value ="/review/", params={"actionLabel=approve"} , method =  {RequestMethod.POST}) 
	public String doApprove(@Valid @ModelAttribute("loanForm")LoanForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
		forward=super.doApprove(actionForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	
	@RequestMapping(value ="/review/", params={"action=Approve"} , method =  {RequestMethod.POST}) 
	public String doApproval(@Valid @ModelAttribute("loanForm")LoanForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
		forward=super.doApprove(actionForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	@RequestMapping(value ="/review/", params={"actionLabel=exit"} , method =  {RequestMethod.POST}) 
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
	

	@RequestMapping(value ="/review/", params={"actionLabel=save & exit"} , method =  {RequestMethod.POST}) 
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
	
	
	@RequestMapping(value ="/review/", params={"action=SaveAndExit"} , method =  {RequestMethod.POST}) 
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
	
	@Autowired
	private PSValidatorFWDefault psValidatorFWDefault;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWDefault);
	}

	

}
