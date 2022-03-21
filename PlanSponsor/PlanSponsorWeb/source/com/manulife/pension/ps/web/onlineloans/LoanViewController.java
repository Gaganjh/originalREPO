package com.manulife.pension.ps.web.onlineloans;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.LoanDocumentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.onlineloans.displayrules.AbstractDisplayRules;
import com.manulife.pension.ps.web.onlineloans.displayrules.ViewDisplayRules;
import com.manulife.pension.ps.web.withdrawal.LoanAndWithdrawalRequestsForm;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanActivities;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.withdrawal.valueobject.UserName;

@Controller
@RequestMapping(value ="/onlineloans")
@SessionAttributes({"loanForm","loanAndWithdrawalRequestsForm"})

public class LoanViewController extends AbstractLoanController {
	@ModelAttribute("loanForm")
	public LoanForm populateForm() {
		return new LoanForm();
	}
	
	@ModelAttribute("loanAndWithdrawalRequestsForm")
	public LoanAndWithdrawalRequestsForm populateWithdarawalForm() {
		return new LoanAndWithdrawalRequestsForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("default","/onlineloans/view.jsp");
		forwards.put("error","/onlineloans/view.jsp");
		forwards.put("toLoanAndWithdrawal","redirect:/do/withdrawal/loanAndWithdrawalRequests/");
		forwards.put("loanList","redirect:/do/withdrawal/loanAndWithdrawalRequests/");
	}

	/**
	 * This is a static reference to the logger.
	 */
	private static final Logger logger = Logger.getLogger(LoanViewController.class);

	protected String getCurrentForward() {
		return ACTION_FORWARD_VIEW;
	}

	@Override
	protected AbstractDisplayRules getDisplayRules(UserProfile userProfile, UserRole userRoleWithPermissions, Loan loan,
			LoanPlanData loanPlanData, LoanParticipantData loanParticipantData, LoanSettings loanSettings,
			LoanActivities loanActivities, Map<Integer, UserName> userNames, Map<String, String> stateMap,
			Map<String, String> countryMap) {
		return new ViewDisplayRules(userProfile, userRoleWithPermissions, loan, loanPlanData, loanParticipantData,
				loanSettings, loanActivities, userNames, stateMap, countryMap);
	}

	/**
	 * {@inheritDoc}
	 */

	@RequestMapping(value = "/view/", method = {RequestMethod.GET})
	public String doDefault(@Valid @ModelAttribute("loanForm") LoanForm actionForm,
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
		Loan loan = getLoan(actionForm, request);
		updateRequest(actionForm, request, true, loan, false, false);
		return forwards.get(ACTION_FORWARD_DEFAULT);
	}

	@RequestMapping(value = "/view/", params = {"actionLabel=exit"}, method = {RequestMethod.POST,RequestMethod.GET})
	public String doExit(@Valid @ModelAttribute("loanForm") LoanForm actionForm,
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

		return forwards.get(ACTION_FORWARD_BACK_TO_LOAN_AND_WITHDRAWAL);
	}

	
	@RequestMapping(value = "/view/", params = { "actionLabel=printLoanDocumentsPdf"}, method = {RequestMethod.GET})
	public String doPrintLoanDocumentsPdf(@Valid @ModelAttribute("loanForm") LoanForm actionForm,
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
		String forward = super.doPrintLoanDocumentsPdf(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}


	@RequestMapping(value="/view/",params = {"actionLabel=showLoanPackagePdf"},method = {RequestMethod.GET })
	public String doShowLoanPackagePdf(@Valid @ModelAttribute("loanForm") LoanForm actionForm,
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
		String forward = super.doShowLoanPackagePdf(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/view/",params = {"actionLabel=printPDF"}, method = {RequestMethod.GET})
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

	
	@RequestMapping(value ="/view/", params={"actionLabel=delete"} ,method =  {RequestMethod.POST,RequestMethod.GET}) 
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
	

	
	
	@Override
	protected byte[] getLoanDocumentsData(Integer userProfileId, Integer contractId, Integer submissionId)
			throws SystemException {
		return LoanDocumentServiceDelegate.getInstance().getLoanDocuments(userProfileId, contractId, submissionId,
				true);
	}


}
