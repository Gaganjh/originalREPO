package com.manulife.pension.ps.web.onlineloans;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.onlineloans.displayrules.AbstractDisplayRules;
import com.manulife.pension.ps.web.onlineloans.displayrules.EditDisplayRules;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanActivities;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.withdrawal.valueobject.UserName;

@Controller
@RequestMapping(value = "/onlineloans")
@SessionAttributes({ "loanForm" })
public class LoanPendingApprovalController extends AbstractLoanController {

	@ModelAttribute("loanForm")
	public LoanForm populateForm() {
		return new LoanForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("default","/onlineloans/approve.jsp");
		forwards.put("draft","redirect:/do/onlineloans/draft/");
		forwards.put("initiate","redirect:/do/onlineloans/approve/");
		forwards.put("pendingReview","redirect:/do/onlineloans/review/");
		forwards.put("view","redirect:/do/onlineloans/view/");
		forwards.put("error","/onlineloans/approve.jsp");
		forwards.put("lockError","redirect:/do/withdrawal/loanAndWithdrawalRequests/");
		forwards.put("toLoanAndWithdrawal","redirect:/do/withdrawal/loanAndWithdrawalRequests/");
		forwards.put("loanList","redirect:/do/withdrawal/loanAndWithdrawalRequests/");
	}

	protected String getCurrentForward() {
		return ACTION_FORWARD_PENDING_APPROVAL;
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

	protected String preExecute(
			@Valid @ModelAttribute("loanForm") LoanForm actionForm,
			BindingResult bindingResult, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {

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
	@RequestMapping(value = "/approve/", method = {RequestMethod.GET})
	public String doDefualt(@Valid @ModelAttribute("loanForm") LoanForm actionForm,BindingResult bindingResult, HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException,
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
						.get(errDirect) : forwards.get("default");
			}
		}
		Loan loan = getLoan(actionForm, request);
		updateRequest(actionForm, request, true, loan, false, true);
		return forwards.get(ACTION_FORWARD_DEFAULT);
	}

	@RequestMapping(value = "/approve/", params = {"actionLabel=printLoanDocumentsPdf"}, method = {RequestMethod.POST,RequestMethod.GET})
	public String doPrintLoanDocumentsPdf(@Valid @ModelAttribute("loanForm") LoanForm actionForm,BindingResult bindingResult, HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException,
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
						.get(errDirect) : forwards.get("default");
			}
		}
		forward = super.doPrintLoanDocumentsPdf(actionForm, request,
				response);
		return StringUtils.contains(forward, '/') ? forward
				: forwards.get(forward);
	}
	
	@RequestMapping(value ="/approve/", params={"actionLabel=print loan documents"} , method =  {RequestMethod.POST,RequestMethod.GET})
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
	
	@RequestMapping(value ="/approve/", params={"action=PrintLoanDocuments"} , method =  {RequestMethod.POST,RequestMethod.GET})
	public String doPrintLoanDocumentsFromWarningPopUp(@Valid @ModelAttribute("loanForm") LoanForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		return doPrintLoanDocuments(actionForm, bindingResult, request, response);
	}
	
	@RequestMapping(value ="/approve/", params={"actionLabel=delete"} , method =  {RequestMethod.POST}) 
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

	@RequestMapping(value = "/approve/", params = {"action=showLoanPackagePdf", "task=showLoanPackagePdf"}, method = {RequestMethod.POST, RequestMethod.GET})
	public String doShowLoanPackagePdf(@Valid @ModelAttribute("loanForm") LoanForm actionForm,BindingResult bindingResult, HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(
					CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(
						CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards
						.get(errDirect) : forwards.get("default");
			}
		}
		String forward = super.doShowLoanPackagePdf(actionForm, request,
				response);
		return StringUtils.contains(forward, '/') ? forward
				: forwards.get(forward);
	}

	@RequestMapping(value = "/approve/", params = {"action=printPDF","task=printPDF"}, method = {RequestMethod.POST, RequestMethod.GET})
	public String doPrintPDF(@Valid @ModelAttribute("loanForm") LoanForm actionForm,BindingResult bindingResult, HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(
					CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(
						CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards
						.get(errDirect) : forwards.get("default");
			}
		}
		String forward = super.doPrintPDF(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward
				: forwards.get(forward);
	}

	@RequestMapping(value = "/approve/", params = {"actionLabel=approve"}, method = {RequestMethod.POST})
	public String doApprove(@Valid @ModelAttribute("loanForm") LoanForm actionForm,BindingResult bindingResult, HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException,
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
		forward = super.doApprove(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards
				.get(forward);
	}

	@RequestMapping(value = "/approve/", params = {"action=agree"}, method = {RequestMethod.POST, RequestMethod.GET })
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

	@RequestMapping(value = "/approve/", params = {"action=Deny"}, method = {RequestMethod.POST})
	public String doDenyProcess(@Valid @ModelAttribute("loanForm") LoanForm actionForm,BindingResult bindingResult, HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException,
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
		forward = super.doDeny(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards
				.get(forward);
	}

	@RequestMapping(value = "/approve/", params = {"action=Approve"}, method = {RequestMethod.POST})
	public String doApproval(@Valid @ModelAttribute("loanForm") LoanForm actionForm,BindingResult bindingResult, HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException,
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
		forward = super.doApprove(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards
				.get(forward);
	}

	@RequestMapping(value = "/approve/", params = {"actionLabel=deny"}, method = {RequestMethod.POST})
	public String doDeny(@Valid @ModelAttribute("loanForm") LoanForm actionForm,BindingResult bindingResult, HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException,
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
		forward = super.doDeny(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards
				.get(forward);
	}

	@RequestMapping(value = "/approve/", params = {"actionLabel=exit"}, method = {RequestMethod.POST})
	public String doExit(@Valid @ModelAttribute("loanForm") LoanForm actionForm,BindingResult bindingResult, HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException,
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
		forward = super.doExit(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards
				.get(forward);
	}

	@RequestMapping(value = "/approve/", params = {"actionLabel=save & exit"}, method = {RequestMethod.POST})
	public String doSaveAndExit(@Valid @ModelAttribute("loanForm") LoanForm actionForm,BindingResult bindingResult, HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException,
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
		forward = super.doSaveAndExit(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards
				.get(forward);
	}

	@RequestMapping(value = "/approve/", params = {"action=SaveAndExit"}, method = {RequestMethod.POST})
	public String doSaveExit(@Valid @ModelAttribute("loanForm") LoanForm actionForm,BindingResult bindingResult, HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException,
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
		forward = super.doSaveAndExit(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards
				.get(forward);
	}
}
