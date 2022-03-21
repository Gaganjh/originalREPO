package com.manulife.pension.ps.web.iloans;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.iloans.util.DateFormatter;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.ps.web.validation.rules.loanRequest.InterestRatePctRule;
import com.manulife.pension.ps.web.validation.rules.loanRequest.LoanSetupFeeRule;
import com.manulife.pension.service.account.AccountException;
import com.manulife.pension.service.account.valueobject.LoanPayrollFrequency;
import com.manulife.pension.service.account.valueobject.LoanRequestData;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;

/**
 * Action class for the Loan Request Report. It gets the data from Customer
 * Service database,
 * 
 * 
 * @author Chris Shin
 * @version CS1.0 (March 1, 2004)
 */
@Controller
@RequestMapping( value = "/iloans")
@SessionAttributes({"loanRequestForm"})

public class LoanRequestPage2Controller extends LoanRequestBaseController {
	
	@ModelAttribute("loanRequestForm") 
	public LoanRequestForm populateForm() 
	{
		return new LoanRequestForm();
		}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	private static final String FORWARD_INPUT = "input";
	static {
		forwards.put(FORWARD_INPUT, "/iloans/loanRequestPage2.jsp");
		forwards.put(FORWARD_BACK, "redirect:/do/iloans/loanRequestPage1/");
		forwards.put(FORWARD_LOAN_REQUEST_PAGE_2, "/iloans/loanRequestPage2.jsp");
		forwards.put(FORWARD_REFRESH, "redirect:/do/iloans/loanRequestPage2/?action=refresh");
		forwards.put(FORWARD_VIEW_LOAN_REQUEST, "redirect:/do/iloans/viewLoanRequests/");
		forwards.put(FORWARD_CONTINUE, "redirect:/do/iloans/loanRequestConfirmation/");
	}

	public LoanRequestPage2Controller() {
		super(LoanRequestPage2Controller.class);
	}

	@RequestMapping(value ="/loanRequestPage2/",  method =  {RequestMethod.GET,RequestMethod.POST}) 
	public String doExecute(@Valid @ModelAttribute("loanRequestForm") LoanRequestForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(FORWARD_INPUT);
        	}
        }  
		
	
	
		//ActionForward forward = mapping
		//		.findForward(FORWARD_LOAN_REQUEST_PAGE_2);
		//ActionForward forward = mapping.findForward(FORWARD_REFRESH);
		String forward=FORWARD_REFRESH;
		Collection errors = null;

		//LoanRequestForm loanRequestForm = (LoanRequestForm) form;
		String button = actionForm.getButton();
		HttpSession session = request.getSession(false);
		String parentPage = (String)session.getAttribute("iloansParentPage");	
		// user is using back button from confirmation page. Redirect to page 1
		if("confirmation".equals(parentPage))
			
			forward= forwards.get(FORWARD_BACK);
		if(parentPage ==null ||(!"first".equals(parentPage) && !"second".equals(parentPage)))

			
			return  forwards.get(FORWARD_VIEW_LOAN_REQUEST);
		if("refresh".equals(request.getParameter("action")))
		{
			forward =FORWARD_INPUT;
			return forwards.get(FORWARD_INPUT);
		}
		if ("".equals(button)) {	
			
			forward=   forwards.get(FORWARD_LOAN_REQUEST_PAGE_2);
			if (!actionForm.isLoaded())
				
				forward= forwards.get(FORWARD_VIEW_LOAN_REQUEST);
			else
			{
				populateForm(actionForm);
				session.setAttribute("iloansParentPage","second");
			}
		} else if ("back".equals(button)) {
			errors = processBack(actionForm);
			if (errors == null || errors.size() == 0) {
				actionForm.setPage2Entered(true);
				
				forward= forwards.get(FORWARD_BACK);
			}

		} else if ("calculateStep3".equals(button)) {
			errors = processStep3(actionForm);

		} else if ("saveExit".equals(button)) {
			//if (!isTokenValid(request)) 
			if(request!=null){
				
				forward= forwards.get(FORWARD_VIEW_LOAN_REQUEST);
				
			} else {
				errors = processSaveExit(actionForm);
				if (errors == null || errors.size() == 0) {
					errors = saveLoanRequest(actionForm);
					session.removeAttribute("loanRequestForm");
					if (errors == null || errors.size() == 0)
						IloansHelper.removeSessionAttributes(session);
					////TODO resetToken(request);
					
					forward=forwards.get(FORWARD_VIEW_LOAN_REQUEST);
				}
			}

		} else if ("continue".equals(button)) {
			//if (!isTokenValid(request))
			if(request!=null)
				
				forward=forwards.get(FORWARD_VIEW_LOAN_REQUEST);

			else {
				errors = processContinue(actionForm);
				if (errors == null || errors.size() == 0) {
					actionForm.setPage2Entered(true);
					////TODO resetToken(request);
					
					forward=forwards.get(FORWARD_CONTINUE);
				}
			}
		}

		if (errors != null && errors.size() > 0) {
			setErrorsInSession(request, errors);
		}

		actionForm.setButton("");
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	private Collection processBack(LoanRequestForm loanRequestForm)
			throws SystemException {

		Collection errors = validateSave(loanRequestForm);
		return errors;
	}

	private Collection processStep3(LoanRequestForm loanRequestForm)
			throws SystemException {

		Collection errors = validateLoanInfo(loanRequestForm, SAVE_ONLY_NO);
		;
        //String saveOnly ="AP".equalsIgnoreCase(loanRequestForm.getProceedWithLoan())?SAVE_ONLY_NO:SAVE_ONLY_YES;

		if (errors == null || errors.size() == 0) {
			try {
				//calculateRepayment(loanRequestForm, saveOnly);
				calculateRepayment(loanRequestForm, SAVE_ONLY_NO);
			} catch (GenericException e) {
				errors.add(e);
			}
		}

		return errors;
	}

	private Collection processSaveExit(LoanRequestForm loanRequestForm)
			throws SystemException {

		Collection errors = validateSave(loanRequestForm);

		if (errors == null || errors.size() == 0) {
			errors = calculateRepaymentAndApproval(loanRequestForm);
		}

		return errors;
	}

	private Collection processContinue(LoanRequestForm loanRequestForm)
			throws SystemException {

		Collection errors = new ArrayList();

		if (errors == null || errors.size() == 0) {
			try {
				if ("AP".equalsIgnoreCase(loanRequestForm.getProceedWithLoan())) {
					// first check if have to recalculate
					if(loanRequestForm.isStep3FieldsChanged())
						errors.add(new GenericException(ErrorCodes.ILOANS_RECALCULATE_REPAYMENT_AMT));
					if (errors == null || errors.size() == 0) 
					{						
						errors =validateApprovalInfo(loanRequestForm, SAVE_ONLY_NO);
						if (errors == null || errors.size() == 0) 
							errors = processStep3(loanRequestForm);
						if (errors == null || errors.size() == 0)
							processApprovalInfo(loanRequestForm, SAVE_ONLY_NO);
					}
				} else {
					errors =validateApprovalInfo(loanRequestForm, SAVE_ONLY_NO);
					if (errors == null || errors.size() == 0) 				
						processApprovalInfo(loanRequestForm, SAVE_ONLY_YES);
				}
			} catch (GenericException e) {
				errors.add(e);
			}
		}

		return errors;
	}

	private void populateForm(LoanRequestForm theForm) {

		theForm.setRepaymentFrequency(""
				+ theForm.getLoanRequestData().getAppPaymentsPerYear());
		//theForm.setLoanSetupFee(theForm.getLoanSetUpFee());
	}

	private Collection validateBaseLoanInfo(LoanRequestForm theForm,
			String saveOnlyInd) {

		Collection errors = new ArrayList();

		//validate
		ValidationError valError = validateMaxAmortizationYears(theForm
				.getMaxAmortizationPeriod(), saveOnlyInd);
		if (valError != null) {
			errors.add(valError);
		}
		//LS 2005 - August 8 - always perform basic validatiion (numeric, greater then zero
		//, even for save only
		//if (SAVE_ONLY_NO.equalsIgnoreCase(saveOnlyInd)
		//		&& !validateLoanInterestRatePct(theForm.getLoanInterestRate())) {
		
		new InterestRatePctRule(ErrorCodes.ILOANS_INTEREST_RATE_INVALID, saveOnlyInd).
		validate(LoanRequestForm.FIELD_LOAN_INTEREST_RATE, errors,theForm.getLoanInterestRate() );

		//LS 2005 - August 8 - always perform basic validatiion (numeric, positive)
		//, even for save only
		//if (SAVE_ONLY_NO.equalsIgnoreCase(saveOnlyInd)
		//		&& !validatePayrollFrequency(theForm.getRepaymentFrequency())) {
		if (!validatePayrollFrequency(theForm.getRepaymentFrequency())) {		
			errors.add(new ValidationError(
					LoanRequestForm.FIELD_REPAYMENT_FREQUENCY,
					ErrorCodes.ILOANS_PAYROLL_FREQUENCY_INVALID));
		}

		return errors;
	}

	private Collection validateLoanInfo(LoanRequestForm theForm,
			String saveOnlyInd) {

		Collection errors = new ArrayList();
		errors = validateBaseLoanInfo(theForm, saveOnlyInd);
		
		ValidationError valError = null;
//		 LS August 8, 2005 . Continue validation only if there were no errors
//		to avoide duplicate error messages. validate max amort period against reason for Save only NO
		if (SAVE_ONLY_NO.equals(saveOnlyInd)&&(errors == null || errors.size() == 0)) {
		valError = validateMaxAmortizationYearsAgainstReason(theForm
				.getMaxAmortizationPeriod(), theForm.getLoanRequestData().getReqLoanReasonCode(),
				saveOnlyInd, theForm.getProceedWithLoan());

		if (valError != null) {
			errors.add(valError);
			valError = null;
		}
		}
		if (errors == null || errors.size() == 0) { 
			
			valError = validateAmortizationPeriod(theForm
					.getAmortizationPeriod(), theForm
					.getMaxAmortizationPeriod(), saveOnlyInd);
			
			if (valError != null) {
			errors.add(valError);
			valError = null;
			}
		}	
		if (errors == null || errors.size() == 0) {
			valError = validateLoanAmount(theForm.getLoanAmount(), theForm
					.getMaxLoanAvailable(), saveOnlyInd);
		}

		if (valError != null) {
			errors.add(valError);
		}
		return errors;

	}

	private Collection validateApprovalInfo(LoanRequestForm theForm,
			String saveOnlyInd) {

		Collection errors = new ArrayList();

		// ensure that the Calculate loan repayment button was clicked
		if ((SAVE_ONLY_NO.equalsIgnoreCase(saveOnlyInd) && theForm.getAppPaymentAmount().trim().length() == 0)
				&& "AP".equalsIgnoreCase(theForm.getProceedWithLoan())) {
			errors.add(new ValidationError(
					LoanRequestForm.FIELD_REPAYMENT_AMOUNT,
					ErrorCodes.ILOANS_CALCULATE_REPAYMENT_NOT_CLICKED));
			return errors;
		}

		if (SAVE_ONLY_NO.equalsIgnoreCase(saveOnlyInd)
				&& !theForm.isTpaInitiated()
				&& !validateAdditionalComments(
						theForm.getAddComments(),
						theForm.getProceedWithLoan().equalsIgnoreCase("AP") ? true
								: false)) {
			errors.add(new ValidationError(
					LoanRequestForm.FIELD_ADDITIONAL_COMMENTS,
					ErrorCodes.ILOANS_MISSING_COMMENT_FOR_DENIED_REQUEST));
		}

		if (!validateText(theForm.getDefProvision(), theForm
				.getProceedWithLoan().equalsIgnoreCase("AP") ? true : false,
				saveOnlyInd)) {
			errors.add(new ValidationError(
					LoanRequestForm.FIELD_DEFAULT_PROVISION,
					ErrorCodes.ILOANS_MISSING_DEFAULT_PROVISION_FOR_REQUEST));
		}

		ValidationError valError = validateApprovedLoanAmount(theForm
				.getLoanRequestData().getAppLoanAmt(), theForm
				.getProceedWithLoan().equalsIgnoreCase("AP") ? true : false,
				theForm.getLoanRequestData().getMinLoanAmt(), saveOnlyInd);

		if (valError != null) {
			errors.add(valError);
			valError = null;
		}
	
		validateLoanSetUpFee(errors, theForm.getLoanSetupFee(), theForm
				.getLoanRequestData().getAppLoanAmt(),	theForm.getProceedWithLoan().equalsIgnoreCase("AP") ? "N"
						: "Y");

		if (!validatePlanInfoExpiryDate(theForm.getExpiryDate())) {
			errors.add(new ValidationError(
					LoanRequestForm.FIELD_EXPIRY_DATE,
					ErrorCodes.ILOANS_PLAN_INFO_EXPIRY_DATE_INVALID));
		}

		if (!validateText(theForm.getSpousalConsentRadio(), theForm
				.getProceedWithLoan().equalsIgnoreCase("AP") ? true : false,
				saveOnlyInd)) {
			errors.add(new ValidationError(
					LoanRequestForm.FIELD_SPOUSAL_CONSENT,
					ErrorCodes.ILOANS_SPOUSAL_CONSENT_NOT_ENTERED));
		}
		return errors;
	}

	private boolean validateAdditionalComments(String comments,
			boolean isApproved) {

		if (!isApproved && (comments == null || comments.trim().equals(""))) {
			return false;
		}
		return true;
	}

	private boolean validateText(String text, boolean isApproved,
			String saveOnlyInd) {

		if (SAVE_ONLY_NO.equalsIgnoreCase(saveOnlyInd) && isApproved
				&& (text == null || text.trim().equals(""))) {
			return false;
		}
		return true;
	}

	private ValidationError validateAmortizationPeriod(
			String amortizationPeriod, String maxAmortizationPeriod,
			String saveOnlyInd) {
	// LS added basic edits for the field on saveOnlyInd condition	
		if (SAVE_ONLY_YES.equalsIgnoreCase(saveOnlyInd)&&
		(amortizationPeriod == null || amortizationPeriod.trim().length() == 0))
					return null;

		try {
			int result = Integer.parseInt(amortizationPeriod);
			if (SAVE_ONLY_YES.equalsIgnoreCase(saveOnlyInd)&& result==0)
				return null;
			if (result <= 0) {
				return new ValidationError(
						LoanRequestForm.FIELD_AMORTIZATION_PERIOD,
						ErrorCodes.ILOANS_INVALID_AMORTIZATION_PERIOD);
			}

			if (SAVE_ONLY_NO.equalsIgnoreCase(saveOnlyInd)
					&& (result > Integer.parseInt(maxAmortizationPeriod))) {
				return new ValidationError(
						LoanRequestForm.FIELD_AMORTIZATION_PERIOD,
						ErrorCodes.ILOANS_AMORTIZATION_PERIOD_GT_MAX);
			}
		} catch (Exception e) {
			return new ValidationError(
					LoanRequestForm.FIELD_AMORTIZATION_PERIOD,
					ErrorCodes.ILOANS_INVALID_AMORTIZATION_PERIOD);
		}

		return null;
	}

	private ValidationError validateApprovedLoanAmount(BigDecimal amount,
			boolean isApproved, BigDecimal minLoanAmt, String saveOnlyInd) {	
		
		try {
			if (SAVE_ONLY_NO.equalsIgnoreCase(saveOnlyInd) && isApproved
					&& (amount.compareTo(minLoanAmt) < 0)) {
				return new ValidationError(
						LoanRequestForm.FIELD_LOAN_AMOUNT,
						ErrorCodes.ILOANS_LOAN_AMOUNT_LT_MIN_ALLOWED);
			}

		} catch (Exception e) {
			return new ValidationError(LoanRequestForm.FIELD_LOAN_AMOUNT,
					ErrorCodes.ILOANS_LOAN_AMOUNT_INVALID);
		}

		return null;

	}

	private ValidationError validateLoanAmount(String loanAmount,
			String maxLoanAmount, String saveOnlyInd) {
		// LS added basic edits for the field on saveOnlyInd condition	
		if (SAVE_ONLY_YES.equalsIgnoreCase(saveOnlyInd)&&
		(loanAmount == null || loanAmount.trim().length() == 0))
					return null;			
		try {
			BigDecimal amount = new BigDecimal(loanAmount);
			//allow the loan amount to be zero
			if (amount.compareTo(ZERO) < 0) {
				return new ValidationError(
						LoanRequestForm.FIELD_LOAN_AMOUNT,
						ErrorCodes.ILOANS_LOAN_AMOUNT_INVALID);
			}

			if (SAVE_ONLY_NO.equalsIgnoreCase(saveOnlyInd)
					&& amount.compareTo(new BigDecimal(maxLoanAmount)) > 0) {
				return new ValidationError(
						LoanRequestForm.FIELD_LOAN_AMOUNT,
						ErrorCodes.ILOANS_LOAN_AMOUNT_GT_MAX_ALLOWED);
			}

		} catch (Exception e) {
			return new ValidationError(LoanRequestForm.FIELD_LOAN_AMOUNT,
					ErrorCodes.ILOANS_LOAN_AMOUNT_INVALID);
		}

		return null;
	}
/*
	private boolean validateLoanInterestRatePct(String loanInterestRatePct, String saveOnlyInd) {
		// LS Aug 8 , 2005 allow blanks for save only
		if (SAVE_ONLY_YES.equalsIgnoreCase(saveOnlyInd)
				&& (loanInterestRatePct == null || loanInterestRatePct.trim().length() == 0)) {
			return true;
		}
		try {
			BigDecimal amount = new BigDecimal(loanInterestRatePct);
			// LS Aug 8 , 2005 allow zero amount for save only
			if (SAVE_ONLY_YES.equalsIgnoreCase(saveOnlyInd)&&amount.compareTo(ZERO) == 0)
				return true;
			if (amount.compareTo(MIN_INTEREST_RATE) < 0
					|| amount.compareTo(ONE_HUNDRED) > 0) {
				return false;
			}

		} catch (Exception e) {
			return false;
		}

		return true;
	}
*/
	private void validateLoanSetUpFee(Collection errors, String loanSetUpFee,
			BigDecimal maxContractLoanSetupFeeAmt, String saveOnlyInd) {
			int [] errorCodes = new int [] {ErrorCodes.ILOANS_LOAN_SETUP_FEE_INVALID,ErrorCodes.ILOANS_LOAN_SETUP_FEE_GT_MAX};
			LoanSetupFeeRule rule = new LoanSetupFeeRule(errorCodes,maxContractLoanSetupFeeAmt, saveOnlyInd);
			rule.validate(LoanRequestForm.FIELD_LOAN_SETUP_FEE,errors, loanSetUpFee);
	}

	private ValidationError validateMaxAmortizationYears(String yearsString,
			String saveOnlyInd) {

		int result = 0;

		if (SAVE_ONLY_YES.equalsIgnoreCase(saveOnlyInd)
				&& (yearsString == null || yearsString.trim().length() == 0)) {
			return null;
		}

		try {
			result = Integer.parseInt(yearsString);
			// LS - August 8 2005. insure that max loan is not negative
			// however allow zero amount for save only
			if (SAVE_ONLY_YES.equalsIgnoreCase(saveOnlyInd)&&result==0)
				return null;
			if(result <= 0)
				return new ValidationError(
						LoanRequestForm.FIELD_MAX_AMORTIZATION_PERIOD,
						ErrorCodes.ILOANS_INVALID_MAX_AMORTIZATION_PERIOD);			
		} catch (Exception e) {
			return new ValidationError(
					LoanRequestForm.FIELD_MAX_AMORTIZATION_PERIOD,
					ErrorCodes.ILOANS_INVALID_MAX_AMORTIZATION_PERIOD);
		}
		return null;
	}

	private ValidationError validateMaxAmortizationYearsAgainstReason(
			String yearsString, String reasonCode, String saveOnlyInd,
			String proceedWithLoan) {

		int result = 0;

		try {
			result = Integer.parseInt(yearsString);

			if ("AP".equalsIgnoreCase(proceedWithLoan)) {
				String loanReasonCode = reasonCode.trim();
				if (loanReasonCode.compareTo("PR") == 0) { // max. amortization
					// period for home
					// loans
					if (result <= 0 || result > 30) {
						return new ValidationError(
								LoanRequestForm.FIELD_MAX_AMORTIZATION_PERIOD,
								ErrorCodes.ILOANS_INVALID_PRIMARY_RESIDENCE_AMORTIZATION_PERIOD);
					}
				} else if (loanReasonCode.compareTo("HA") == 0) {
					if (result <= 0 || result > 5) {// max. amortization period
						// for hardship loans
						return new ValidationError(
								LoanRequestForm.FIELD_MAX_AMORTIZATION_PERIOD,
								ErrorCodes.ILOANS_INVALID_HARDSHIP_AMORTIZATION_PERIOD);
					}
				} else {
					if (result <= 0 || result > 5) {// max. amortization period
						// for general purpose loans
						return new ValidationError(
								LoanRequestForm.FIELD_MAX_AMORTIZATION_PERIOD,
								ErrorCodes.ILOANS_INVALID_GENERAL_PURPOSE_AMORTIZATION_PERIOD);
					}
				}
			}
		} catch (Exception e) {
			return new ValidationError(
					LoanRequestForm.FIELD_MAX_AMORTIZATION_PERIOD,
					ErrorCodes.ILOANS_INVALID_MAX_AMORTIZATION_PERIOD);
		}
		return null;
	}

	private boolean validatePayrollFrequency(String payrollFrequency) {

		int result = 0;

		try {
			result = Integer.parseInt(payrollFrequency);
			if (result <= 0) {
				return false;
			} else {

				String payrollFrequencyDescription = LoanPayrollFrequency
						.getDescriptionForPeriodsPerYear(result);

				if (payrollFrequencyDescription == null) {
					return false;
				}
			}

		} catch (Exception e) {
			return false;
		}

		return true;
	}

	private boolean validatePlanInfoExpiryDate(String expiryDate) {

		try {
			Date date = DateFormatter.parse(expiryDate);
			return true;

		} catch (Exception e) {
			return false;
		}
	}

	private Collection validateSave(LoanRequestForm theForm)
			throws SystemException {

		Collection errors = new ArrayList();

		//errors = validateBaseLoanInfo(theForm, SAVE_ONLY_YES);
		errors = validateLoanInfo(theForm, SAVE_ONLY_YES);
		if (errors == null || errors.size() == 0) {
			errors.addAll(validateApprovalInfo(theForm, SAVE_ONLY_YES));
		}

		return errors;
	}

	protected void completeLoanAction(LoanRequestForm theForm,
			String tpaId) throws SystemException, AccountException {
		//3.5.12 If a TPA Initiated Loan Request and Loan Request Status not =
		// Not Proceed [18.11]
		String status = LoanRequestData.REQUEST_STATUS_CODE_LOAN_PENDING;
		if (theForm.isTpaInitiated()
				&& LoanRequestData.REQUEST_STATUS_CODE_LOAN_DENIED
						.equals(theForm.getProceedWithLoan()))
			status = LoanRequestData.REQUEST_STATUS_CODE_LOAN_DENIED;
		try {
			LoanRequestData loanRequestData = getAccountService()
					.saveLoanRequestTPAApprovalTransaction(tpaId,
							theForm.getTransactionId(), status);
		} catch (AccountException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e, this.getClass().getName(),
					"completeLoanAction", e.getMessage());
		}

	}
	
	
	/**
	 * * (non-Javadoc) This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations
	 */
	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}



}