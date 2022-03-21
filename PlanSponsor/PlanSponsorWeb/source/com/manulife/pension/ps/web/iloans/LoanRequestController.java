package com.manulife.pension.ps.web.iloans;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.iloans.util.BigDecimalFormatter;
import com.manulife.pension.ps.web.iloans.util.DateFormatter;
import com.manulife.pension.ps.web.validation.rules.loanRequest.AmountRule;
import com.manulife.pension.service.account.valueobject.LoanRequestData;
import com.manulife.pension.service.account.valueobject.LoanRequestMoneyType;
import com.manulife.pension.service.account.valueobject.LoanRequestTPAApprovalTransactionStartUpData;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;

/**
 * * Action class for the Loan Request Report. It gets the data from Customer
 * Service database,
 * 
 * @author Chris Shin
 * @version CS1.0 (April 1, 2005)
 *  
 */
@Controller
@RequestMapping( value = "/iloans")
@SessionAttributes({"loanRequestForm"})
public class LoanRequestController extends LoanRequestBaseController {
	@ModelAttribute("loanRequestForm")
	public LoanRequestForm populateForm() {
		return new LoanRequestForm();
	}
	public static HashMap<String,String> forwards =new HashMap<String,String>();
	private static final String FORWARD_INPUT ="input";
	static {
		forwards.put(FORWARD_INPUT, "/iloans/loanRequestPage1.jsp");
		forwards.put(FORWARD_LOAN_REQUEST_PAGE_1, "/iloans/loanRequestPage1.jsp");
		forwards.put(FORWARD_REFRESH, "redirect:/do/iloans/loanRequestPage1/?action=refresh");
		forwards.put(FORWARD_CONTINUE, "redirect:/do/iloans/loanRequestPage2/");
		forwards.put(FORWARD_BACK, "redirect:/do/iloans/viewLoanRequests/");

	}
	public LoanRequestController() {
		super(LoanRequestBaseController.class);
	}
	@RequestMapping(value ="/loanRequestPage1/",  method =  {RequestMethod.GET,RequestMethod.POST})
	public String doExecute( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		//ActionForward forward = mapping.findForward(FORWARD_LOAN_REQUEST_PAGE_1);
		String forward=FORWARD_REFRESH;
		Collection errors = null;
		LoanRequestForm loanRequestForm = (LoanRequestForm) form;
		String button = loanRequestForm.getButton();
		HttpSession session = request.getSession(false);
		String parentPage = (String) session.getAttribute("iloansParentPage");

		if (parentPage == null)
		{
			return forwards.get(FORWARD_BACK);
		}
		if(!"initiate".equals(parentPage)&& !"view".equals(parentPage)&& !"confirmation".equals(parentPage)
						&& !"second".equals(parentPage) && !"first".equals(parentPage))
			return forwards.get(FORWARD_BACK);
		if("initiate".equals(parentPage))
		if("refresh".equals(request.getParameter("action")))
		{
			return forwards.get(FORWARD_INPUT);
		}
		if ("".equals(button)) {
			forward=FORWARD_LOAN_REQUEST_PAGE_1;
			if (session.getAttribute(IloansHelper.PROFILE_ID_PARM) == null)
				return forwards.get(FORWARD_BACK);
			session.setAttribute("iloansParentPage", "first");
			if (!loanRequestForm.isLoaded())
				errors = processStart(loanRequestForm, request);
			if (errors != null && errors.size() > 0) {

				forward=FORWARD_BACK;
			}

		} else if ("calculateStep1".equals(button)) {

			errors = processStep1(loanRequestForm);

		} else if ("calculateStep2".equals(button)) {

			errors = processStep2(loanRequestForm);

		} else if ("continue".equals(button)) {
			//if (!isTokenValid(request))
			if ((request) != null) {
				forward=FORWARD_BACK;
			} else {
				errors = processContinue(loanRequestForm);

				if (errors == null || errors.size() == 0) {

					forward=FORWARD_CONTINUE;
				}
				
			}

		} else if ("saveExit".equals(button)) {
			/*if (!isTokenValid(request))*/
			if ((request) != null){
				
				forward=FORWARD_BACK;
			} else {
				errors = processSaveExit(loanRequestForm);

				if (errors == null || errors.size() == 0) {
					errors = saveLoanRequest(loanRequestForm);

					////TODO resetToken(request);
					request.getSession(false)
							.removeAttribute("loanRequestForm");
					forward=FORWARD_BACK;
		}
			}
		} else if ("back".equals(button)) {

			////TODO resetToken(request);
			request.getSession(false).removeAttribute("loanRequestForm");
			forward=FORWARD_BACK;
}

		if (errors != null && errors.size() > 0) {
			setErrorsInSession(request, errors);
		}

		loanRequestForm.setButton("");
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	private Collection processStart(LoanRequestForm loanRequestForm,
			HttpServletRequest request) throws SystemException {

		Collection errors = new ArrayList();

		HttpSession session = request.getSession(false);

		if (request.getParameter("profileId") != null
				|| session.getAttribute(IloansHelper.LOAN_REQUEST_ID_PARM) != null) {

			LoanRequestTPAApprovalTransactionStartUpData startUpData = getLoanRequest(request);
			populateForm(startUpData, loanRequestForm);
			loanRequestForm.setLoaded(true);
			String status = loanRequestForm.getLoanRequestData()
					.getRequestStatusCode();

			if (!loanRequestForm.isTpaInitiated()
					&& ("AP".equalsIgnoreCase(status) || "DE"
							.equalsIgnoreCase(status))) {
				errors.add(new ValidationError("button",
						ErrorCodes.ILOANS_OUTSTANDING_REQUESTS_EXIST));
			}

		}

		return errors;
	}

	private Collection processStep1(LoanRequestForm loanRequestForm)
			throws SystemException {

		Collection errors = validateVestedAccountBalance(loanRequestForm);

		if (errors == null || errors.size() == 0) {
			try {
				calculateVestedAccountBalance(loanRequestForm, SAVE_ONLY_NO);

			} catch (GenericException e) {
				errors.add(e);
			}
		}
		return errors;
	}

	private Collection processStep2(LoanRequestForm loanRequestForm)
			throws SystemException {

		Collection errors = validateMaxLoanAvailable(loanRequestForm, "Y");

		if (errors == null || errors.size() == 0) {
			try {
				calculateMaxLoanAvailable(loanRequestForm, "Y", SAVE_ONLY_NO);
			} catch (GenericException e) {
				errors.add(e);
			}
		}
		return errors;
	}

	private Collection processContinue(LoanRequestForm loanRequestForm)
			throws SystemException {

		Collection errors = validateContinue(loanRequestForm);

		//option 2
		if (errors == null || errors.size() == 0) {
			try {
				if ("AP".equalsIgnoreCase(loanRequestForm.getProceedWithLoan())) {
					String maxLoan = loanRequestForm.getMaxLoanAvailable();
					calculateVestedAccountBalance(loanRequestForm, SAVE_ONLY_NO);

					if (!loanRequestForm.getAppMaxLoanAvailable().equals(
							maxLoan)) {
						if ("Y".equalsIgnoreCase(loanRequestForm
								.getAppMaxCalculated())) {
							loanRequestForm.setMaxLoanAvailable(null);
						}
					}
				} else {
					//if
					// ("Y".equalsIgnoreCase(loanRequestForm.getAppMaxCalculated()))
					calculateVestedAccountBalance(loanRequestForm,
							SAVE_ONLY_YES);
				}

			} catch (GenericException e) {
				errors.add(e);
			}
		}

		if (errors == null || errors.size() == 0) {
			String calculatedInd = getCalculatedInd(loanRequestForm);

			try {
				if ("AP".equalsIgnoreCase(loanRequestForm.getProceedWithLoan())) {
					calculateMaxLoanAvailable(loanRequestForm, calculatedInd,
							SAVE_ONLY_NO);
				} else {
					calculateMaxLoanAvailable(loanRequestForm, calculatedInd,
							SAVE_ONLY_YES);
				}
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
			errors = calculateVestedABAndMaxLoanAvailable(loanRequestForm,
					SAVE_ONLY_YES);
		}

		if ((errors == null || errors.size() == 0)
				&& loanRequestForm.isPage2Entered())
			errors = calculateRepaymentAndApproval(loanRequestForm);

		return errors;
	}

	private void populateForm(
			LoanRequestTPAApprovalTransactionStartUpData startUpData,
			LoanRequestForm theForm) {

		theForm.setLoanRequestData(startUpData.getLoanRequestData());
		theForm.setTransactionId(startUpData.getTransactionId());
		theForm.setExpiryDate(DateFormatter.format(startUpData
				.getLoanRequestData().getAppExpiryDate()));

		if  (theForm.getLoanRequestData().getRequestStatusCode().equals(LoanRequestData.REQUEST_STATUS_CODE_LOAN_REQUESTED)) {

			if (theForm.getLoanRequestData().isContractLoanSetupFeeAutodeduct()) {
		        theForm.setLoanSetupFee(theForm.getLoanRequestData()
					.getContractLoanSetupFeeAmt().toString());
		    } else {
		        //theForm.setLoanSetupFee("");
		        theForm.setLoanSetupFee(theForm.getLoanRequestData().getAppLoanSetupFee().toString());	
		    }

			
		} else {
		    
		    theForm.setLoanSetupFee(""
					+ theForm.getLoanRequestData().getAppLoanSetupFee());
		}
		
		
		LoanRequestMoneyType[] moneyTypes = theForm.getLoanRequestData()
				.getMoneyTypes();

		String[] moneyTypeName = new String[moneyTypes.length];
		String[] moneyTypeVesting = new String[moneyTypes.length];
		String[] moneyTypeAmount = new String[moneyTypes.length];
		String[] moneyTypeCategory = new String[moneyTypes.length];
		String[] moneyTypeId = new String[moneyTypes.length];
		boolean[] moneyTypeExcluded = new boolean[moneyTypes.length];
		List moneyTypeCheckbox = new ArrayList();

		for (int i = 0; i < moneyTypes.length; i++) {
			moneyTypeName[i] = moneyTypes[i].getMoneyTypeName();
			moneyTypeAmount[i] = BigDecimalFormatter.format(moneyTypes[i]
					.getBalanceAmt(), 2, 2);
			moneyTypeVesting[i] = BigDecimalFormatter.format(moneyTypes[i]
					.getVestingPct(), -1, 4);
			moneyTypeId[i] = moneyTypes[i].getMoneyTypeId();
			moneyTypeExcluded[i] = moneyTypes[i].isMoneyTypeExcluded();
			if (moneyTypes[i].isMoneyTypeExcluded()) {
				moneyTypeCheckbox.add(moneyTypes[i].getMoneyTypeId());
			}

			moneyTypeCategory[i] = moneyTypes[i].getMoneyTypeCategoryCode();
		}

		theForm.setMoneyTypeId(moneyTypeId);
		theForm.setMoneyType(moneyTypeName);
		theForm.setMoneyTypeAmount(moneyTypeAmount);
		theForm.setMoneyTypeVesting(moneyTypeVesting);
		theForm.setMoneyTypeCategory(moneyTypeCategory);
		theForm.setMoneyTypeCheckbox((String[]) moneyTypeCheckbox
				.toArray(new String[0]));

		theForm.setOtherMoneyTypeAmount(BigDecimalFormatter.format(theForm
				.getLoanRequestData().getAppOtherBalanceAmt(), 2, 2));
		theForm.setOtherMoneyTypeVesting(BigDecimalFormatter.format(theForm
				.getLoanRequestData().getAppOtherVestingPct(), -1, 4));
		theForm.setOtherMoneyType("OTHER VESTED");
		theForm.setOtherMoneyTypeExclude(theForm.getLoanRequestData()
				.isAppOtherExcluded());
		if (theForm.getLoanRequestData().isAppOtherExcluded()) {
			theForm.addMoneyType("OTH");
		}
		theForm.setHighestLoanBalanceInLast12Mths(BigDecimalFormatter.format(
				theForm.getLoanRequestData()
						.getMaxOutstandingLoanBalanceLast12Months(), 2, 2));
		theForm.setNumberOfOutstandingLoans(""
				+ theForm.getLoanRequestData().getOutstandingLoansCount());
		theForm
				.setCurrentOutstandingBalance(BigDecimalFormatter.format(
						theForm.getLoanRequestData()
								.getCurrentOutstandingLoanBalance(), 2, 2));

		if (LoanRequestData.REQUEST_STATUS_CODE_LOAN_DENIED
				.equalsIgnoreCase(theForm.getLoanRequestData()
						.getRequestStatusCode())) {
			theForm
					.setProceedWithLoan(LoanRequestData.REQUEST_STATUS_CODE_LOAN_DENIED);
		} else {
			theForm
					.setProceedWithLoan(LoanRequestData.REQUEST_STATUS_CODE_LOAN_APPROVED);
		}

		if ("N".equalsIgnoreCase(theForm.getLoanRequestData()
				.getAppMaxLoanCalculatedInd())) {
			theForm.setMaxLoanAvailable(BigDecimalFormatter.format(theForm
					.getLoanRequestData().getAppMaxLoanAmt(), 2, 2));
		} else {
			theForm.getLoanRequestData().setAppMaxLoanAmt(null);
			theForm.setMaxLoanAvailable("");
		}
		//set the step 3 fields to blank for RE status
		if(LoanRequestData.REQUEST_STATUS_CODE_LOAN_REQUESTED.equalsIgnoreCase(theForm.getLoanRequestData()
						.getRequestStatusCode()))
		{
			theForm.setMaxAmortizationPeriod("");
			theForm.setLoanInterestRate("");
			theForm.setAmortizationPeriod("");
			theForm.setLoanAmount("");
		}
		else
		{
			theForm.setMaxAmortizationPeriod("" +  theForm.getLoanRequestData().getMaxAmortizationYears());
			theForm.setLoanInterestRate("" +theForm.getLoanRequestData().getAppInterestRatePct());
			theForm.setAmortizationPeriod("" +theForm.getLoanRequestData().getAppAmortizationYears());
			theForm.setLoanAmount("" + theForm.getLoanRequestData().getAppLoanAmt());
		}
		theForm.setExpiryDate(DateFormatter.format(theForm.getLoanRequestData()
				.getAppExpiryDate()));
		theForm.setDefProvision(""
				+ theForm.getLoanRequestData().getAppDefaultProvision());
		theForm.setSpousalConsentRadio(""
				+ theForm.getLoanRequestData().getSpousalConsent());
		theForm.setAddComments(""
				+ theForm.getLoanRequestData().getAppRemarks());
        theForm.setGatewayStatusActive(startUpData.getLoanRequestData().isGatewayOptionStatusActive());
    }

	private Collection validateContinue(LoanRequestForm theForm)
			throws SystemException {

		Collection errors = validateVestedAccountBalance(theForm);
		if (errors == null || errors.size() == 0)
		{
		// if the TPA chooses to not proceed, only the minimum field validations
		// are required just in case
		// the TPA decides to save the details.
		if ("DE".equalsIgnoreCase(theForm.getProceedWithLoan())) {
			//did the TPA enter in a value?
			if (theForm.getMaxLoanAvailable() != null
					&& theForm.getMaxLoanAvailable().trim().length() == 0) {
				theForm.setMaxLoanAvailable("0");
			}		
		
			errors.addAll(validateBasicMaxLoanAvailableFields(theForm, "N"));

		} else {

			if (theForm.getMaxLoanAvailable() != null) {
				if (theForm.getLoanRequestData().getAppMaxLoanAmt() == null) {

					errors = validateMaxLoanAvailable(theForm, "N");

					if (errors == null || errors.size() == 0) {
						try {
							// the max loan was entered by the TPA and we are
							// continuing.
							calculateMaxLoanAvailable(theForm, "N",
									SAVE_ONLY_NO);

						} catch (GenericException e) {
							errors.add(e);
						}
					}
				} else {
					// values are the same
					if (theForm.getMaxLoanAvailable().equals(
							theForm.getLoanRequestData().getAppMaxLoanAmt()
									.toString())) {
						errors = validateMaxLoanAvailable(theForm, "N");
						if (errors == null || errors.size() == 0) {
						if (theForm.getLoanRequestData()
								.getAppVestedAccountAmt() == null
								&& "AP".equalsIgnoreCase(theForm
										.getProceedWithLoan())) {
							errors
									.add(new ValidationError(
											"button",
											ErrorCodes.ILOANS_CALCULATE_VESTED_BUTTON_NOT_CLICKED));
						}
						}
						//continue
					} else {
						errors = validateMaxLoanAvailable(theForm, "N");

						if (errors == null || errors.size() == 0) {
							try {
								calculateMaxLoanAvailable(theForm, "N",
										SAVE_ONLY_NO);

							} catch (GenericException e) {
								errors.add(e);
							}
						}
					}
				}

			}
		}
		}

		return errors;
	}

	private Collection validateVestedAccountBalance(
			LoanRequestForm loanRequestForm) throws SystemException {

		Collection errors = new ArrayList();
		AmountRule amountRule =new AmountRule(ErrorCodes.ILOANS_MONEY_TYPE_AMOUNT_INVALID);
		LoanRequestMoneyType[] loanRequestMoneyTypes = loanRequestForm
		.getLoanRequestData().getMoneyTypes();

		for (int i = 0; i < loanRequestMoneyTypes.length; i++) {
			String moneyTypeBalance = loanRequestForm.getMoneyTypeAmount()[i];	
			amountRule.validate("",errors, moneyTypeBalance);
		}
		amountRule = new AmountRule(ErrorCodes.ILOANS_MONEY_TYPE_AMOUNT_INVALID);
		amountRule.validate("",errors, loanRequestForm.getOtherMoneyTypeAmount());
		// edit and set money type amounts
		/*
		if (!validateMoneyTypeValues(loanRequestForm)) {
			errors.add(new ValidationError("moneyTypes",
					ErrorCodes.ILOANS_MONEY_TYPE_AMOUNT_INVALID));
		}*/
		//edit and set money type amounts
		if (!validateMoneyTypePercentages(loanRequestForm)) {
			errors.add(new ValidationError("moneyTypes",
					ErrorCodes.ILOANS_MONEY_TYPE_PERCENTAGE_INVALID));
		}

		return errors;
	}

	//	private boolean validateAccountBalanceChanges(LoanRequestForm
	// loanRequestForm) throws SystemException {
	//
	//		Collection errors = new ArrayList();
	//		boolean diffFound = false;
	//		
	//		LoanRequestMoneyType[] mt =
	// loanRequestForm.getLoanRequestData().getMoneyTypes();
	//		System.out.println("checking....");
	//		// edit and set money type amounts
	//		String[] mtcb = loanRequestForm.getMoneyTypeCheckbox();
	//
	//		for (int i=0;i <mt.length;i++) {
	//
	//		    if ((mt[i].getVestingPct().compareTo(new
	// BigDecimal(loanRequestForm.getMoneyTypeVesting(i))) !=0) ||
	//		        (mt[i].getBalanceAmt().compareTo(new
	// BigDecimal(loanRequestForm.getMoneyTypeAmount(i))) != 0)) {
	//
	//		        diffFound=true;
	//		        break;
	//		    }
	//
	//		    boolean diff = false;
	//		    if (mt[i].isMoneyTypeExcluded()) {
	//		        for (int j=1;j<mtcb.length;j++) {
	//			        if (mtcb[j].equals(mt[i].getMoneyTypeId())) {
	//			            diff = true;
	//			            break;
	//			        }
	//		        }
	//				
	//		        if (!diff) {
	//				    diffFound=true;
	//				}
	//			
	//			} else {
	//			    for (int j=1;j<mtcb.length;j++) {
	//			        if (mtcb[j].equals(mt[i].getMoneyTypeId())) {
	//			            diff=true;
	//			            break;
	//			        }
	//			    }
	//
	//			    if (diff) {
	//				    diffFound=true;
	//				}
	//
	//			}
	//			
	//	
	//		}
	//		
	//
	//		return diffFound;
	//	}

	

	public boolean validateMoneyTypePercentages(LoanRequestForm theForm) {

		LoanRequestMoneyType[] loanRequestMoneyTypes = theForm
				.getLoanRequestData().getMoneyTypes();

		for (int i = 0; i < loanRequestMoneyTypes.length; i++) {
			String vestedPercentage = theForm.getMoneyTypeVesting()[i];

			if (!EMPLOYEE_CATEGORY_CODE.equals(loanRequestMoneyTypes[i]
					.getMoneyTypeCategoryCode())) {
				if (!validateVestingPercentage(vestedPercentage)) {
					return false;
				}
			}
		}

		//set OTHER Money Type
		String vestedPercentage = theForm.getOtherMoneyTypeVesting();

		if (!validateVestingPercentage(vestedPercentage)) {
			return false;
		}

		return true;
	}

	private boolean validateVestingPercentage(String percentageString) {

		try {
			BigDecimal percentage = new BigDecimal(percentageString);
			if (percentage.compareTo(ZERO) < 0
					|| percentage.compareTo(ONE_HUNDRED) > 0) {
				return false;
			}

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	private Collection validateBasicMaxLoanAvailableFields(
			LoanRequestForm theForm, String maxLoanCalculatedInd) {

		Collection errors = new ArrayList();

		// retrieve values from page
		String highestLoanBalanceInLast12Months = theForm
				.getHighestLoanBalanceInLast12Mths();
		String numberOfOutstandingLoans = theForm.getNumberOfOutstandingLoans();
		String currentOutstandingBalance = theForm
				.getCurrentOutstandingBalance();

		if (!validateNumberOfLoans(numberOfOutstandingLoans)) {
			errors
					.add(new ValidationError(
							LoanRequestForm.FIELD_NUMBER_OF_OUTSTANDING_LOAN_REQUESTS,
							ErrorCodes.ILOANS_NUMBER_OF_OUTSTANDING_LOANS_INVALID));
		}
		new AmountRule(ErrorCodes.ILOANS_CURRENT_OUTSTANDING_LOAN_BALANCE_INVALID)
		.validate(LoanRequestForm.FIELD_CURRENT_OUTSTANDING_BALANCE,errors,currentOutstandingBalance);
		new AmountRule(ErrorCodes.ILOANS_HIGHEST_LOAN_BALANCE_LAST_12MTHS_INVALID)
		.validate(LoanRequestForm.FIELD_HIGHEST_LOAN_BALANCE_12_MTHS,errors,highestLoanBalanceInLast12Months);

		String maxLoan = theForm.getMaxLoanAvailable();
		//	 validate the highest loan amount after the current loan amount has
		// been validated
		if (errors == null || errors.size() == 0) {
			if ("N".equalsIgnoreCase(maxLoanCalculatedInd)) {
				new AmountRule(ErrorCodes.ILOANS_MAX_LOAN_AVAILABLE_ENTERED_INVALID)
				.validate(LoanRequestForm.FIELD_MAX_LOAN_AVAILABLE,errors,maxLoan);
			}
		}
		//todo: validate max loan available
		//todo: correct max loan calculated indicator
		return errors;
	}

	private Collection validateMaxLoanAvailable(LoanRequestForm theForm,
			String maxLoanCalculatedInd) {

		Collection errors = new ArrayList();
		if (theForm.getLoanRequestData().getAppVestedAccountAmt() == null
				&& "AP".equalsIgnoreCase(theForm.getProceedWithLoan())) {
			errors.add(new ValidationError("button",
					ErrorCodes.ILOANS_CALCULATE_VESTED_BUTTON_NOT_CLICKED));
		}
		errors.addAll(validateBasicMaxLoanAvailableFields(theForm, maxLoanCalculatedInd));

		if (errors != null && errors.size() > 0) {
			return errors;
		}

		String highestLoanBalanceInLast12Months = theForm
				.getHighestLoanBalanceInLast12Mths();
		String currentOutstandingBalance = theForm
				.getCurrentOutstandingBalance();

		//	 validate the highest loan amount after the current loan amount has
		// been validated
		if (errors == null || errors.size() == 0) {
			if (!validateHighestLoanAmountAgainstLoanAmount(
					highestLoanBalanceInLast12Months, currentOutstandingBalance)) {
				errors
						.add(new ValidationError(
								LoanRequestForm.FIELD_HIGHEST_LOAN_BALANCE_12_MTHS,
								ErrorCodes.ILOANS_HIGHEST_OS_LOAN_BALANCE_GT_CURRENT));
			}
		}

	//	String maxLoan = theForm.getMaxLoanAvailable();
		//	 validate the highest loan amount after the current loan amount has
		// been validated
		//if (errors == null || errors.size() == 0) {
		//	if ("N".equalsIgnoreCase(maxLoanCalculatedInd)) {
		//		new AmountRule(ErrorCodes.ILOANS_MAX_LOAN_AVAILABLE_ENTERED_INVALID)
		//		.validate(LoanRequestForm.FIELD_MAX_LOAN_AVAILABLE,errors,maxLoan);
		//	}
	//	}

		return errors;
	}
/*
	private boolean validateAmount(String amountString) {

		BigDecimal result = null;

		try {
			BigDecimal amount = new BigDecimal(amountString);
			if (amount.compareTo(ZERO) < 0) {
				return false;
			} else {
				return true;
			}

		} catch (Exception e) {
			return false;
		}
	}
*/
	private boolean validateHighestLoanAmountAgainstLoanAmount(
			String amountString, String currentOutstandingLoanBalance) {

		try {
			BigDecimal amount = new BigDecimal(amountString);
			BigDecimal currentBalance = new BigDecimal(
					currentOutstandingLoanBalance);

			if (amount.compareTo(currentBalance) < 0) { // should be greater or
				// equel
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}

	private boolean validateNumberOfLoans(String countString) {

		int result = 0;

		try {
			int count = Integer.parseInt(countString);
			if (count < 0) {
				return false;
			} else {
				return true;
			}

		} catch (Exception e) {
			return false;
		}
	}

	private Collection validateSave(LoanRequestForm theForm)
			throws SystemException {

		Collection errors = new ArrayList();

		errors = validateVestedAccountBalance(theForm);

		if (errors == null || errors.size() == 0) {

			//did the TPA enter in a value?
			if (theForm.getMaxLoanAvailable() != null
					&& theForm.getMaxLoanAvailable().trim().length() == 0) {
				theForm.setMaxLoanAvailable("0");
			}
			
			errors.addAll(validateBasicMaxLoanAvailableFields(theForm, "N"));
			
		}

		return errors;
	}

	protected void completeLoanAction(LoanRequestForm theForm,
			String tpaId) throws SystemException {
		String status = LoanRequestData.REQUEST_STATUS_CODE_LOAN_PENDING;
		//     3.5.6 If a TPA Initiated Loan Request and Loan Request Status not =
		// Not Proceed [13.11]
		if (theForm.isTpaInitiated()
				&& LoanRequestData.REQUEST_STATUS_CODE_LOAN_DENIED
						.equals(theForm.getProceedWithLoan()))
			status = LoanRequestData.REQUEST_STATUS_CODE_LOAN_DENIED;

		try {
			LoanRequestData loanRequestData = getAccountService()
					.saveLoanRequestTPAApprovalTransaction(tpaId,
							theForm.getTransactionId(), status);
		} catch (Exception e) {
			throw new SystemException(e, this.getClass().getName(),
					"completeLoanAction", e.getMessage());
		}
	}

	
}