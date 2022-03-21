/*
 * Created on May 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.web.iloans;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.util.SsnRule;
import com.manulife.pension.platform.web.validation.rules.ContractNumberRule;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.RestrictedMaxLengthRule;
import com.manulife.pension.ps.service.iloans.exception.IloansServiceException;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.IloansServiceDelegate;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.account.valueobject.LoanRequestData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.util.iloans.PropertyManager;
import com.manulife.pension.util.content.GenericException;

/**
 * @author sternlu
 * 
 * InitiateLoanRequestAction validate input on the initiateLoanRequest page,
 * call ejb layer to insert loanRequest entry into csdb if validation and insert
 * are successful - forward request to LoanRequestAction
 */

@Controller
@RequestMapping( value = "/iloans")
@SessionAttributes({"initiateLoanRequestsForm"})

public class InitiateLoanRequestsController extends PsAutoController {

	@ModelAttribute("initiateLoanRequestsForm") 
	public InitiateLoanRequestsForm populateForm() 
	{
		return new InitiateLoanRequestsForm();
		}
	private static final String LOAN_REQUEST_FORWARD = "loanRequestPage1";

	private static final String VIEW_LOAN_REQUESTS_FORWARD = "viewLoanRequests";
	private static final String FORWARD_REFRESH ="refresh";
	private static final String FORWARD_INPUT ="input";
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static{
		forwards.put(FORWARD_INPUT,"/iloans/initiateLoanRequests.jsp");
		forwards.put(LOAN_REQUEST_FORWARD,"redirect:/do/iloans/loanRequestPage1/");
		forwards.put(FORWARD_REFRESH,"redirect:/do/iloans/initiateLoanRequests/?action=refresh");
		forwards.put(VIEW_LOAN_REQUESTS_FORWARD,"redirect:/do/iloans/viewLoanRequests/ ");
		}

	
	public InitiateLoanRequestsController() {
		super(InitiateLoanRequestsController.class);
	}
	@RequestMapping(value ="/initiateLoanRequests/",  method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("initiateLoanRequestsForm") InitiateLoanRequestsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		validate(actionForm,request);
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(FORWARD_INPUT);
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDefault");
		}
		HttpSession session = request.getSession(false);
		String parentPage = (String) session.getAttribute("iloansParentPage");
		if(parentPage==null)
			
			return forwards.get(VIEW_LOAN_REQUESTS_FORWARD);
		if(!"initiate".equals(parentPage)&& !"view".equals(parentPage))
			
			return forwards.get(VIEW_LOAN_REQUESTS_FORWARD);
		
		session.setAttribute("iloansParentPage", "initiate");
		////TODO saveToken(request);
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDefault");
		}
		/*
		 * Save the token for this form. We have to validate this token when we
		 * save so that duplicate submits are avoided.
		 */

		return forwards.get(FORWARD_INPUT);

	}
	
	@RequestMapping(value ="/initiateLoanRequests/", params= {"task=refresh"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doRefresh(@Valid @ModelAttribute("initiateLoanRequestsForm") InitiateLoanRequestsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		validate(actionForm, request);
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(FORWARD_INPUT);
			}
		}
	
		HttpSession session = request.getSession(false);
		String parentPage = (String) session.getAttribute("iloansParentPage");
		if(parentPage==null)
			
			return forwards.get(VIEW_LOAN_REQUESTS_FORWARD);
		if(!"initiate".equals(parentPage)&& !"view".equals(parentPage))
			
			return forwards.get(VIEW_LOAN_REQUESTS_FORWARD);
		return forwards.get(FORWARD_INPUT);
	}
	
	@RequestMapping(value = "/initiateLoanRequests/", params = { "actionLabel=continue" }, method = {RequestMethod.POST})
	public String doContinue(
			@Valid @ModelAttribute("initiateLoanRequestsForm") InitiateLoanRequestsForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		validate(actionForm, request);
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(FORWARD_INPUT);
			}
		}
	
		int reasonCode = 0;
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doContinue");
		}
		UserProfile user = getUserProfile(request);
		BigDecimal userProfileId = new BigDecimal(String.valueOf(user.getPrincipal().getProfileId()));
		Collection errors = new ArrayList();
		InitiateLoanRequestsForm form = (InitiateLoanRequestsForm) actionForm;	
		
		// validates against online loan for the given contract number
		if (isOnlineLoanAllowed(form.getContractNumber())) {
			form.setOnlineLoan(true);
			
			return forwards.get(FORWARD_REFRESH);
		}
		
		//ActionForward forward =mapping.findForward(LOAN_REQUEST_FORWARD);
		LoanRequestData data = new LoanRequestData();
		try {

			populateLoanRequestData(data, form);

			IloansServiceDelegate delegate = IloansServiceDelegate
					.getInstance();
			delegate.insertLoanRequest(data, form.getSsn(), userProfileId);
		} catch (IloansServiceException ie) {
			ie.printStackTrace();
			errors
					.add(new GenericException(Integer.parseInt(ie
							.getErrorCode())));
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doContinue");
		}
		if (errors.isEmpty()) {
			HttpSession session = request.getSession(false);
			session.setAttribute(IloansHelper.CONTRACT_NUMBER_PARM, data
					.getContractNumber());
			session.setAttribute(IloansHelper.PROFILE_ID_PARM, data
					.getProfileId());
			session.setAttribute(IloansHelper.LOAN_REQUEST_ID_PARM, data
					.getLoanRequestId());

			/*
			 * Resets the token
			 */
			session.removeAttribute("initiateLoanRequestsForm");
					
			return forwards.get(LOAN_REQUEST_FORWARD);
		} else {
			SessionHelper.setErrorsInSession(request, errors);
			//setErrorsInRequest(request, errors);
			
			return forwards.get(FORWARD_REFRESH);
		}

	}

	@RequestMapping(value = "/initiateLoanRequests/", params = { "actionLabel=back" }, method = { RequestMethod.POST })
	public String doBack(@Valid @ModelAttribute("initiateLoanRequestsForm") InitiateLoanRequestsForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		validate(actionForm, request);
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(FORWARD_INPUT);
			}
		}

		return forwards.get(VIEW_LOAN_REQUESTS_FORWARD);
	}

	@SuppressWarnings("rawtypes")
	protected Collection doValidate( ActionForm form,
			HttpServletRequest request) {
		/*This code has been changed and added to Validate form and
		 * request against penetration attack, prior to other validations
		 */
	   
		Collection errors = super.doValidate(form, request);
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doValidate");
		}

		InitiateLoanRequestsForm requestForm = (InitiateLoanRequestsForm) form;
		if (requestForm.isContinueAction()) {
			// validate contract number
			ContractNumberRule.getInstance().validate(
					InitiateLoanRequestsForm.FIELD_CONTRACT, errors,
					requestForm.getContractNumber());
			// validate ssn

			SsnRule.getInstance().validate(
					InitiateLoanRequestsForm.FIELD_SSN, errors,
					requestForm.getSsn());
			// typeOfLoan is mandatory
			MandatoryRule rule = new MandatoryRule(
					ErrorCodes.ILOANS_TYPE_MANDATORY);
			rule.validate(InitiateLoanRequestsForm.FIELD_TYPE_OF_LOAN,
					errors, requestForm.getTypeOfLoan());

			// Reason for Loan up to 250 characters
			if (requestForm.getReasonForLoan() != null) {
				new RestrictedMaxLengthRule(
						ErrorCodes.ILOANS_REASON_TEXT_TOO_LONG)
						.validate(
								InitiateLoanRequestsForm.FIELD_REASON_FOR_LOAN,
								errors,
								requestForm.getReasonForLoan(),
								InitiateLoanRequestsForm.REASON_FOR_LOAN_MAX_LENGTH);
			}

		}
		return errors;
	}

	private void populateLoanRequestData(LoanRequestData data,
			InitiateLoanRequestsForm form) {
		// the three fields below will be populated by the IloansServiceBean
		data.setProfileId(null);
		data.setLoanRequestId(null);
		data.setConfirmationNumber(0);
		data.setContractNumber(form.getContractNumber());
		data.setTpaInitiated(true);
		String reasonForLoan = form.getReasonForLoan();
		data.setReasonForLoan(reasonForLoan == null ? "" : reasonForLoan);
		data
				.setRequestStatusCode(LoanRequestData.REQUEST_STATUS_CODE_LOAN_REQUESTED);
		data.setReqInterestRatePct(new BigDecimal("0"));
		data.setReqPaymentAmt(new BigDecimal("0"));
		data.setReqLoanAmt(new BigDecimal("0"));
		data.setReqPaymentAdjustmentAmt(new BigDecimal("0"));
		data.setReqAmortizationYears(0);
		data.setReqPaymentsPerYear(0);
		data.setReqLoanReasonCode(form.getTypeOfLoan());
		data.setReqVestingPct(new BigDecimal("0"));
		data.setReqMaxLoanAmt(new BigDecimal("0"));
		//request date - expiry date should be 30 days in future
		Calendar cal = Calendar.getInstance();
		data.setReqDate(cal.getTime());
		cal.add(Calendar.DAY_OF_YEAR, PropertyManager.getInt(PropertyManager.ILOANS_PROPERTY_PREFIX+".request_expiry_days"));
		data.setReqExpiryDate(cal.getTime());
		String legallyMarried = form.getLegallyMarried();
		if(legallyMarried == null )
			data.setLegallyMarried("");
		else if("yes".equalsIgnoreCase(legallyMarried))
			data.setLegallyMarried( "Y");
		else if("no".equalsIgnoreCase(legallyMarried))
			data.setLegallyMarried( "N");
		else
			data.setLegallyMarried( "");

	}

	/**
	 * Checks whether we're in the right state.
	 * 
	 * @see com.manulife.pension.ps.web.controller.PsController#validate(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	protected String validate(
			ActionForm actionForm, HttpServletRequest request) {

		InitiateLoanRequestsForm requestForm = (InitiateLoanRequestsForm) actionForm;

		/*
		 * If this is a save action, we should compare the token and make sure
		 * it's still valid. Token is initialized in the doDefault() method and
		 * reset in the doContinue() method.
		 */
		if (requestForm.isContinueAction()) {
				return forwards.get(VIEW_LOAN_REQUESTS_FORWARD);
		}

		Collection errors = doValidate( actionForm, request);
		//setErrorsInRequest(request, errors);

		if (!errors.isEmpty()) {
			/*
			 * Go to the input page if validation fails.
			 */
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(FORWARD_REFRESH);
		}
		return null;

	}
	private boolean isOnlineLoanAllowed(String contractNumber)
			throws NumberFormatException, SystemException {

		LoanSettings loanSettings = LoanServiceDelegate.getInstance()
				.getLoanSettings(new Integer(contractNumber));
		if (loanSettings != null && loanSettings.isLrk01()
				&& loanSettings.isAllowOnlineLoans()) {
			return true;
		}

		return false;
	}

	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}
	
}