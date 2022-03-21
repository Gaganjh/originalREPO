/*
 * Created on May 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.web.resources;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;

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

import com.manulife.pension.delegate.AccountServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.ps.web.validation.rules.amortizationSchedule.DateOfFirstPaymentGTLoanDateRule;
import com.manulife.pension.ps.web.validation.rules.amortizationSchedule.DateRule;
import com.manulife.pension.ps.web.validation.rules.amortizationSchedule.GreaterThanMinimumRule;
import com.manulife.pension.ps.web.validation.rules.amortizationSchedule.GreaterThanZeroLessThanHundredRule;
import com.manulife.pension.service.account.AccountException;
import com.manulife.pension.service.account.AccountTransactionValidationException;
import com.manulife.pension.service.account.valueobject.LoanScenarioTPA;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.Pair;
import com.manulife.pension.validator.ValidationError;

/**
 * @author sternlu
 * 
 * Action 
 */
@Controller
@RequestMapping( value ="/resources")

public class AmortizationScheduleController extends PsAutoController {
	@ModelAttribute("amortizationScheduleForm") 
	public AmortizationScheduleForm populateForm()
	{
		return new AmortizationScheduleForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/resources/amortizationScheduleGenerator.jsp");
		}

	
	/*
	 * (non-Javadoc)
	 * 
	 *      com.manulife.pension.ps.web.controller.PsAutoForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@RequestMapping(value ="/amortizationScheduleGenerator/" , method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("amortizationScheduleForm") AmortizationScheduleForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDefault");
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
		       }
		}
		Collection errors =doValidate(form,request);
		if(errors.size()>0){
			setErrorsInRequest(request, errors);
			return forwards.get("input");
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDefault");
		}
		return forwards.get("input");

	}

	@RequestMapping(value ="/amortizationScheduleGenerator/" ,params={"actionLabel=Generate Pdf"}   , method =  {RequestMethod.POST}) 
	public String doGeneratePdf (@Valid @ModelAttribute("amortizationScheduleForm") AmortizationScheduleForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
			Collection errors = new ArrayList();

			errors =doValidate(form,request);
			if(errors.size()>0){
				setErrorsInRequest(request, errors);
				return forwards.get("input");
			}
			if (form.isValidated()) {
				UserProfile userProfile = getUserProfile(request);
				UserInfo userInfo = SecurityServiceDelegate.getInstance().getUserInfo(
						userProfile.getPrincipal());
				String tpaId = new Integer(userInfo.getTpaFirmId()).toString();

				LoanScenarioTPA loanScenario = new LoanScenarioTPA();
				populateLoanScenario(form, loanScenario);
				//ByteArrayInputStream pdfStream = null;
				int pdfStreamLength = 0;
				AccountServiceDelegate delegate = AccountServiceDelegate
						.getInstance();
				try {
					byte[] pdf = delegate.getLoanAmortizationSchedulePDF(tpaId,
							loanScenario);
					parseErrorCodes(loanScenario.getErrorCodes(), errors,
							form);
					if (errors.isEmpty()) {
						// add pdf byte [] to the session
						request.getSession(false).setAttribute("pdf", pdf);
					} else
						setErrorsInRequest(request, errors);

				} catch (AccountTransactionValidationException e) {
					parseErrorCodes(e.getErrorCodes(), errors,
							form);
					if (!errors.isEmpty()) 
						setErrorsInRequest(request, errors);
					
				} catch (AccountException e) {
					throw new SystemException(e, this.getClass().getName(),
							"generatePdf", e.getMessage());
				} catch (Exception e) {

					throw new SystemException(e, this.getClass().getName(),
							"generatePdf", e.getMessage());
				}

			}

			return forwards.get("input");

		}



	@RequestMapping(value ="/amortizationScheduleGenerator/",params={"action=printPDF"}  , method =  {RequestMethod.GET}) 
	public String doPrintPDF (@Valid @ModelAttribute("amortizationScheduleForm") AmortizationScheduleForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doPrintPDF(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}
	
	@SuppressWarnings("rawtypes")
	protected Collection doValidate( ActionForm form,
			HttpServletRequest request) {
		
		 //This code has been changed and added  to 
		 //Validate form and request against penetration attack, prior to other validations.
		
		Collection errors = super.doValidate( form, request);
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doValidate");
		}

		AmortizationScheduleForm amortizationScheduleForm = (AmortizationScheduleForm) form;
		if (logger.isDebugEnabled()) {
			logger.debug(amortizationScheduleForm.toString());
			Enumeration enumParams = request.getParameterNames();
			while (enumParams.hasMoreElements()) {
				String key = (String) enumParams.nextElement();
				String value = request.getParameter(key);
				logger.debug("Request Param " + key + " = " + value);
			}

		}
		// only validate for the generatePdfAction
		if (amortizationScheduleForm.isGeneratePdfAction()) {

			amortizationScheduleForm.setValidated(false);
			//FGA.28.User needs to enter Date of Loan. The date has to be
			// in
			// MM/DD/YYYY format,
			DateRule dateRule = new DateRule(
					ErrorCodes.AMORTIZATION_SCHEDULE_INVALID_LOAN_DATE);
			amortizationScheduleForm.setDLoanDate(dateRule.validate(
					AmortizationScheduleForm.FIELD_LOAN_DATE, errors,
					amortizationScheduleForm.getLoanDate()));

			//FGA.29. User needs to enter Date of First Payment for Loan.
			// The date
			// has to be in MM/DD/YYYY format.
			dateRule = new DateRule(
					ErrorCodes.AMORTIZATION_SCHEDULE_INVALID_FIRST_LOAN_PAYMENT_DATE);
			amortizationScheduleForm
					.setDFirstPaymentDate(dateRule
							.validate(
									AmortizationScheduleForm.FIELD_FIRST_PAYMENT_DATE,
									errors, amortizationScheduleForm
											.getFirstPaymentDate()));

			//The Date of First Payment needs to be an older date than the
			// date of the loan. We only check on this condition if the
			// valid dates have been entered
			if (amortizationScheduleForm.getDLoanDate() != null
					&& amortizationScheduleForm.getDFirstPaymentDate() != null) {
				Pair pair = new Pair(amortizationScheduleForm
						.getDLoanDate(), amortizationScheduleForm
						.getDFirstPaymentDate());
				new DateOfFirstPaymentGTLoanDateRule(ErrorCodes.AMORTIZATION_SCHEDULE_FIRST_LOAN_PAYMENT_DATE_LT_LOAN_DATE)
						.validate(
								AmortizationScheduleForm.FIELD_FIRST_PAYMENT_DATE,
								errors, pair);
			}

			//FGA.30. User needs to enter Loan Amount.
			//Loan amount needs to be greater than 100. The field needs to be
			// numeric, with no more than 2 decimal places.
			int scale = 2;
			GreaterThanMinimumRule greaterThanMinimumRule = new GreaterThanMinimumRule(
					ErrorCodes.AMORTIZATION_SCHEDULE_INVALID_LOAN_AMOUNT);
			BigDecimal temp = greaterThanMinimumRule.validate(
					AmortizationScheduleForm.FIELD_LOAN_AMOUNT, errors,
					amortizationScheduleForm.getLoanAmount(), scale,"100.00");
			amortizationScheduleForm.setBdLoanAmount(temp);

			//FGA.31. User needs to enter Amortization period.
			// The field needs to be numeric and a whole number that is
			// greater than zero. No decimal places
			scale = 0;
			GreaterThanZeroLessThanHundredRule greaterThanZeroLessThanHundredRule =  new GreaterThanZeroLessThanHundredRule(
					ErrorCodes.AMORTIZATION_SCHEDULE_INVALID_AMORTIZATION_YEARS);
			temp = greaterThanZeroLessThanHundredRule.validate(
					AmortizationScheduleForm.FIELD_AMORTIZATION_YEARS,
					errors, amortizationScheduleForm
							.getAmortizationYears(), scale);
			amortizationScheduleForm.setIAmortizationYears(temp
					.intValue());

			//FGA.33. User needs to enter in a number for Nominal Annual
			// Rate.
			//This has to be a numeric field greater than zero, less then
			// hundreed.
			scale = 3;
			greaterThanZeroLessThanHundredRule =  new GreaterThanZeroLessThanHundredRule(
					ErrorCodes.AMORTIZATION_SCHEDULE_INVALID_NOMINAL_RATE);
			amortizationScheduleForm
					.setBdNominalAnnualRate(greaterThanZeroLessThanHundredRule.validate(
									AmortizationScheduleForm.FIELD_NOMINAL_ANNUAL_RATE,
									errors, amortizationScheduleForm
											.getNominalAnnualRate()));

			if (errors.isEmpty())
				amortizationScheduleForm.setValidated(true);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doValidate");
		}
		return errors;
	}

	private AccountServiceDelegate getAccountService() {

		return AccountServiceDelegate.getInstance();
	}

	private void populateLoanScenario(AmortizationScheduleForm form,
			LoanScenarioTPA loanScenario) {

		loanScenario.setAmortizationYears(form.getIAmortizationYears());
		loanScenario.setContractName(form.getContractName());
		loanScenario.setParticipantName(form.getParticipantName());
		loanScenario.setFirstLoanPaymentDate(form.getDFirstPaymentDate());
		loanScenario.setInterestRate(form.getBdNominalAnnualRate().divide(
				new BigDecimal("100"), 6, BigDecimal.ROUND_HALF_UP));
		loanScenario.setPaymentPeriodsPerYear(Integer.parseInt(form
				.getRepaymentFrequency()));
		loanScenario.setLoanAmount(form.getBdLoanAmount());
		loanScenario.setLoanDate(form.getDLoanDate());

	}

	private void parseErrorCodes(int[] errorCodes, Collection errors,
			AmortizationScheduleForm amortizationScheduleForm) {
		for (int i = 0; i < errorCodes.length; i++) {
			String errorMessage = null;
			switch (errorCodes[i]) {
			case LoanScenarioTPA.ERROR_CODE_BAD_INTEREST_RATE: // 5501
				errors
						.add(new ValidationError(
								AmortizationScheduleForm.FIELD_NOMINAL_ANNUAL_RATE,
								ErrorCodes.AMORTIZATION_SCHEDULE_INVALID_NOMINAL_RATE));
				break;
			case LoanScenarioTPA.ERROR_CODE_BAD_PAYROLL_FREQUENCY://5502
				errors
						.add(new ValidationError(
								AmortizationScheduleForm.FIELD_PAYMENT_PER_YEAR,
								ErrorCodes.AMORTIZATION_SCHEDULE_INVALID_PAYROLL_FREQUENCY));
				break;
			case LoanScenarioTPA.ERROR_CODE_BAD_AMORTIZATION_YEARS://5503
				errors
						.add(new ValidationError(
								AmortizationScheduleForm.FIELD_AMORTIZATION_YEARS,
								ErrorCodes.AMORTIZATION_SCHEDULE_INVALID_AMORTIZATION_YEARS));
				break;

			case LoanScenarioTPA.ERROR_CODE_BAD_LOAN_AMOUNT: //5504
				errors.add(new ValidationError(
						AmortizationScheduleForm.FIELD_LOAN_AMOUNT,
						ErrorCodes.AMORTIZATION_SCHEDULE_INVALID_LOAN_AMOUNT));
				break;

			case LoanScenarioTPA.ERROR_CODE_BAD_PAYMENT_AMOUNT: //5505
				errors.add(new ValidationError(
						AmortizationScheduleForm.FIELD_LOAN_AMOUNT,
						ErrorCodes.AMORTIZATION_SCHEDULE_BAD_PAYMENT_AMOUNT));
				break;
			case LoanScenarioTPA.ERROR_CODE_BAD_FIRST_LOAN_PAYMENT_DATE://5543
				errors
						.add(new ValidationError(
								AmortizationScheduleForm.FIELD_FIRST_PAYMENT_DATE,
								ErrorCodes.AMORTIZATION_SCHEDULE_FIRST_LOAN_PAYMENT_DATE_LT_LOAN_DATE));
				break;

			case LoanScenarioTPA.ERROR_CODE_BAD_LOAN_DATE://5542
				errors.add(new ValidationError(
						AmortizationScheduleForm.FIELD_LOAN_DATE,
						ErrorCodes.AMORTIZATION_SCHEDULE_INVALID_LOAN_DATE));
				break;

			case LoanScenarioTPA.ERROR_CODE_FIRST_LOAN_PAYMENT_DATE_LT_LOAN_DATE://5544
				errors
						.add(new ValidationError(
								AmortizationScheduleForm.FIELD_FIRST_PAYMENT_DATE,
								ErrorCodes.AMORTIZATION_SCHEDULE_FIRST_LOAN_PAYMENT_DATE_LT_LOAN_DATE));
				break;

			case LoanScenarioTPA.ERROR_CODE_NO_ERROR:
				break;
			default:
				errors.add(new ValidationError("",
						ErrorCodes.ILOANS_GENERIC_ERROR));
			}
		}
	}
}