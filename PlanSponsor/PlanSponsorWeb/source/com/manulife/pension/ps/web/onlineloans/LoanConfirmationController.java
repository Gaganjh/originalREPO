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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.onlineloans.displayrules.AbstractDisplayRules;
import com.manulife.pension.ps.web.onlineloans.displayrules.ConfirmationDisplayRules;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanActivities;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.withdrawal.valueobject.UserName;

/**
 */

@Controller
@RequestMapping(value ="/onlineloans")
@SessionAttributes({"loanForm"})

public class LoanConfirmationController extends AbstractLoanController {
	@ModelAttribute("loanForm")
	public LoanForm populateForm() 
	{
		return new LoanForm();
	}

	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static{
		forwards.put("default","/onlineloans/confirmation.jsp");
		forwards.put("draft","redirect:/do/onlineloans/draft/");
		forwards.put("view","redirect:/do/onlineloans/view/");
		forwards.put("pendingReview","redirect:/do/onlineloans/review/");
		forwards.put("pendingApproval","redirect:/do/onlineloans/approve/");
		forwards.put("toLoanAndWithdrawal","redirect:/do/withdrawal/loanAndWithdrawalRequests/");
		forwards.put("loanList","redirect:/do/withdrawal/loanAndWithdrawalRequests/");	
	}

	
	
	/**
	 * This is a static reference to the logger.
	 */
	private static final Logger logger = Logger
			.getLogger(LoanConfirmationController.class);

	protected String getCurrentForward() {
		return ACTION_FORWARD_CONFIRMATION;
	}

	 
	protected String preExecute (LoanForm actionForm,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		
		if ("exit".equalsIgnoreCase(actionForm.getActionLabel())) {
			return null;
		}
		return super.preExecute( actionForm, request, response);
	}

	@Override
	protected AbstractDisplayRules getDisplayRules(UserProfile userProfile,
			UserRole userRoleWithPermissions, Loan loan,
			LoanPlanData loanPlanData, LoanParticipantData loanParticipantData,
			LoanSettings loanSettings, LoanActivities loanActivities,
			Map<Integer, UserName> userNames, Map<String, String> stateMap,
			Map<String, String> countryMap) {
		return new ConfirmationDisplayRules(userProfile,
				userRoleWithPermissions, loan, loanPlanData,
				loanParticipantData, loanSettings, loanActivities, userNames,
				stateMap, countryMap);
	}

	/**
	 * {@inheritDoc}
	 */
	@RequestMapping(value ="/confirmation/",  method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("loanForm") LoanForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(form, request, response);
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
		Loan loan = getLoan(form, request);
		/*
		 * Show Confirmation flag is set by the Pending Review/Pending Approval actions.
		 * Since we're clearing the form in the updateRequest call, we need to preserve
		 * the value for this flag.
		 */
		boolean showConfirmation = form.isShowConfirmation();
		updateRequest(form, request, true, loan, false, false);
		form.setShowConfirmation(showConfirmation);
		return forwards.get(ACTION_FORWARD_DEFAULT);
	}
	
	
	@RequestMapping(value ="/confirmation/" ,  params={"actionLabel=exit"},method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doExit(@Valid @ModelAttribute("loanForm") LoanForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(form, request, response);
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
		
		form.clear();
		return forwards.get(ACTION_FORWARD_BACK_TO_LOAN_AND_WITHDRAWAL);
	}

	@RequestMapping(value ="/confirmation/", params={"actionLabel=printLoanDocumentsPdf"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doPrintLoanDocumentsPdf (@Valid @ModelAttribute("loanForm") LoanForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
	
	@RequestMapping(value ="/confirmation/", params={"actionLabel=print loan documents"} , method =  {RequestMethod.POST,RequestMethod.GET})
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
	
	@RequestMapping(value ="/confirmation/", params={"action=PrintLoanDocuments"} , method =  {RequestMethod.POST,RequestMethod.GET})
	public String doPrintLoanDocumentsFromWarningPopUp(@Valid @ModelAttribute("loanForm") LoanForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		return doPrintLoanDocuments(actionForm, bindingResult, request, response);
	}
	
	
	@RequestMapping(value ="/confirmation/",params={"actionLabel=showLoanPackagePdf"}   , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doShowLoanPackagePdf (@Valid @ModelAttribute("loanForm") LoanForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
	
	
	@RequestMapping(value ="/confirmation/" ,params={"action=printPDF"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
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

	
	

}
