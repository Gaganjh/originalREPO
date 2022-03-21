package com.manulife.pension.ps.web.iloans;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.manulife.pension.delegate.AccountServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.iloans.util.DateFormatter;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.account.AccountException;
import com.manulife.pension.service.account.valueobject.CustomerServicePrincipal;
import com.manulife.pension.service.account.valueobject.LoanRequestData;
import com.manulife.pension.service.account.valueobject.LoanRequestMoneyType;
import com.manulife.pension.service.account.valueobject.LoanRequestStatusData;
import com.manulife.pension.service.account.valueobject.LoanRequestTPAApprovalTransactionStartUpData;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;

/**
 * Action class for the Loan Request Report. It gets the data from Customer
 * Service database,
 *
 *
 * @author Chris Shin
 * @version CS1.0 (April 1, 2005)
 */

public abstract class LoanRequestBaseController extends PsController {

	// following 3 values passed over from calling session
	public static final String EMPLOYEE_CATEGORY_CODE = "EE";

	public static final BigDecimal ZERO = new BigDecimal("0");

	public static final BigDecimal MIN_INTEREST_RATE = new BigDecimal("0.001");

	public static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

	public static final String MAX_LOAN_CALCULATED_CLICKED = "Y";

	public static final String MAX_LOAN_CALCULATED_MANUAL = "N";

	public static final String SAVE_ONLY_YES = "Y";

	public static final String SAVE_ONLY_NO = "N";
	public static final String FORWARD_REFRESH = "refresh";
	public static final String FORWARD_LOAN_REQUEST_PAGE_1 = "loanRequestPage1";

	public static final String FORWARD_LOAN_REQUEST_PAGE_2 = "loanRequestPage2";

	public static final String FORWARD_CONTINUE = "continue";

	public static final String FORWARD_BACK = "back";

	public static final String FORWARD_VIEW_LOAN_REQUEST = "viewLoanRequest";

	public LoanRequestBaseController(Class clazz) {
		super(clazz);
	}


	public LoanRequestTPAApprovalTransactionStartUpData getLoanRequest(
			HttpServletRequest request) throws SystemException {

		HttpSession session = request.getSession(false);
		LoanRequestData loanRequest = null;
		String contractNumber = null;
		String profileId = null;
		String loanRequestId = null;
		//first check the request
		if (request.getParameter("profileId") != null) {
			profileId = request.getParameter("profileId");
			contractNumber = request.getParameter("contractNumber");
			loanRequestId = request.getParameter("loanRequestId");

		}
		//if empty, the arguments are comming from initiate loan request and
		// are stored in the session
		else {
			profileId = (String) session
					.getAttribute(IloansHelper.PROFILE_ID_PARM);

			contractNumber = (String) session
					.getAttribute(IloansHelper.CONTRACT_NUMBER_PARM);

			loanRequestId = (String) session
					.getAttribute(IloansHelper.LOAN_REQUEST_ID_PARM);

		}

		TPAFirmInfo firm = TPAServiceDelegate.getInstance()
				.getFirmInfoByContractId(Integer.parseInt(contractNumber));
		String tpaId = String.valueOf(firm.getId());

		LoanRequestTPAApprovalTransactionStartUpData startUpData = startApprovalFromService(
				tpaId, profileId, contractNumber, loanRequestId);

		return startUpData;
	}

	private LoanRequestTPAApprovalTransactionStartUpData startApprovalFromService(
			String tpaId, String profileId, String contractNumber,
			String loanRequestId) throws SystemException {

		LoanRequestTPAApprovalTransactionStartUpData valobj = null;
		//gets from service
		try {
			valobj = (getAccountService() == null) ? null : getAccountService()
					.startLoanRequestTPAApprovalTransaction(tpaId, profileId,
							contractNumber, loanRequestId);

			if (valobj == null)
				return null;

		} catch (Exception e) {
			SystemException se = new SystemException(e, this.getClass()
					.getName(), "startApprovalFromService",
					"Exception occurred calling service.  profileId = "
							+ profileId);
			throw se;
		}

		return valobj;
	}

	protected AccountServiceDelegate getAccountService() {
		return AccountServiceDelegate.getInstance();
	}

	private CustomerServicePrincipal getCustomerServicePrincipal(String clientId) {

		CustomerServicePrincipal principal = new CustomerServicePrincipal();
		principal.setName(clientId);
		principal
				.setRoles(new String[] { CustomerServicePrincipal.ROLE_SUPER_USER });

		return principal;
	}

	/**
	 * Validate the input action form.
	 *
	 */
/*	@SuppressWarnings("rawtypes")
	protected Collection doValidate( Form form,
			HttpServletRequest request) {
		This code has been changed and added to Validate form and
		 * request against penetration attack, prior to other validations
		 
	    Collection penErrors = PsValidation.doValidatePenTestAutoAction(form,
				mapping, request, CommonConstants.INPUT);
		if (penErrors != null && penErrors.size() > 0) {
			request.removeAttribute(PsBaseAction.ERROR_KEY);
			return penErrors;
		}
		Collection errors = super.doValidate( form, request);

		return errors;
	}*/
	@Autowired
    private PSValidatorFWInput psValidatorFWInput;  

	@InitBinder
	protected void initBinder(HttpServletRequest request,
				ServletRequestDataBinder  binder) {
		binder.addValidators(psValidatorFWInput);
	}

	protected String getTPAId(String contractNumber) throws SystemException {
		TPAFirmInfo firm = TPAServiceDelegate.getInstance()
				.getFirmInfoByContractId(Integer.parseInt(contractNumber));
		String tpaId = String.valueOf(firm.getId());

		return tpaId;
	}

	public Collection saveLoanRequest(LoanRequestForm theForm)
			throws SystemException {

		Collection errors = new ArrayList();
		LoanRequestData loanRequestData = null;
		LoanRequestStatusData loanRequestStatusData = null;
		String tpaId = getTPAId(theForm.getContractNumber());
		if (!theForm.isTpaInitiated()
				|| (theForm.isTpaInitiated() && !LoanRequestData.REQUEST_STATUS_CODE_LOAN_DENIED
						.equals(theForm.getProceedWithLoan()))) {
			try {
				loanRequestStatusData = getAccountService()
						.getLoanRequestStatusData(
								getCustomerServicePrincipal(tpaId),
								theForm.getLoanRequestData().getProfileId(),
								theForm.getContractNumber());

				if (loanRequestStatusData.getCurrentLoanRequestId() != null) {
					if (!loanRequestStatusData.getCurrentLoanRequestId()
							.equals(
									theForm.getLoanRequestData()
											.getLoanRequestId())) {
						// ids are not the same
						if (!"DE".equalsIgnoreCase(loanRequestStatusData
								.getCurrentRequestStatusCode())) {
							errors
									.add(new ValidationError(
											"submit",
											ErrorCodes.ILOANS_OUTSTANDING_REQUESTS_EXIST));
						}
					} else {
						// the request ids are the same
						// if approved or denied, and is not a tpa initiated
						// request, then this cannot happen.
						if (("AP".equalsIgnoreCase(loanRequestStatusData
								.getCurrentRequestStatusCode()) || "DE"
								.equalsIgnoreCase(loanRequestStatusData
										.getCurrentRequestStatusCode()))
								&& !theForm.isTpaInitiated()) {
							errors
									.add(new ValidationError(
											"submit",
											ErrorCodes.ILOANS_OUTSTANDING_REQUESTS_EXIST));
						}
					}
				}
			} catch (Exception e) {
				throw new SystemException(e, this.getClass().getName(),
						"saveLoanRequest", e.getMessage());
			}
		}
		if (errors == null || errors.size() == 0) {
			try {
				completeLoanAction(theForm, tpaId);

				//This transaction is invalidated during the commit. Remove
				// reference to it here.
				theForm.setTransactionId(null);
			} catch (AccountException e) {
				errors.add(new GenericException(2307, new String[] { e
						.getMessage() }));
			}
		}

		return errors;
	}

	protected Collection calculateVestedABAndMaxLoanAvailable(
			LoanRequestForm theForm, String saveOnlyInd)
			throws SystemException {

		Collection errors = null;
		LoanRequestData loanRequestData = null;

		String calculatedInd = getCalculatedInd(theForm);

		try {

			calculateVestedAccountBalance(theForm, saveOnlyInd);
		} catch (GenericException e) {
			errors.add(e);
		}
		if (errors == null || errors.size() == 0) {
			try {
				if (theForm.getMaxLoanAvailable() == null) {
					theForm.setMaxLoanAvailable("0");
				}
				calculateMaxLoanAvailable(theForm, calculatedInd, saveOnlyInd);
			} catch (GenericException e) {
				errors.add(e);
			}

		}
		return errors;
	}

	protected void calculateVestedAccountBalance(
			LoanRequestForm loanRequestForm, String saveOnlyInd)
			throws SystemException, GenericException {

		LoanRequestMoneyType[] loanRequestMoneyTypes = loanRequestForm
				.getLoanRequestData().getMoneyTypes();

		String[] MTExcluded = loanRequestForm.getMoneyTypeCheckbox();

		for (int i = 0; i < loanRequestMoneyTypes.length; i++) {
			loanRequestMoneyTypes[i].setBalanceAmt(new BigDecimal(
					loanRequestForm.getMoneyTypeAmount()[i]));
			loanRequestMoneyTypes[i].setVestingPct(new BigDecimal(
					loanRequestForm.getMoneyTypeVesting()[i]));

			boolean moneyTypeFound = false;
			for (int k = 0; k < loanRequestForm.getMoneyTypeCheckbox().length; k++) {
				if (MTExcluded[k].equalsIgnoreCase(loanRequestMoneyTypes[i]
						.getMoneyTypeId())) {
					moneyTypeFound = true;
					break;
				}
			}
			if (moneyTypeFound) {
				loanRequestMoneyTypes[i].setMoneyTypeExcluded(true);
			} else {
				loanRequestMoneyTypes[i].setMoneyTypeExcluded(false);
			}
		}
			LoanRequestData data = loanRequestForm.getLoanRequestData();
		data.setMoneyTypes(loanRequestMoneyTypes);
		data.setAppOtherBalanceAmt(new BigDecimal(loanRequestForm
				.getOtherMoneyTypeAmount()));
		data.setAppOtherVestingPct(new BigDecimal(loanRequestForm
				.getOtherMoneyTypeVesting()));

		boolean moneyTypeFound = false;
		for (int k = 0; k < MTExcluded.length; k++) {
			if (MTExcluded[k].equalsIgnoreCase("OTH")) {
				moneyTypeFound = true;
				break;
			}
		}
		if (moneyTypeFound) {
			data.setAppOtherExcluded(true);
		} else {
			data.setAppOtherExcluded(false);
		}

		// Input edit passed, kick of the start process
		// any exceptions will invoke the error page
		LoanRequestData loanRequestData = null;
		String calculatedInd =null;
		if (!"AP".equalsIgnoreCase(loanRequestForm.getProceedWithLoan()) &&(loanRequestForm.getMaxLoanAvailable()==null||loanRequestForm.getMaxLoanAvailable().length()==0))
			calculatedInd =loanRequestForm.getAppMaxCalculated();
		else
			calculatedInd = getCalculatedInd(loanRequestForm);

		try {
			loanRequestData = getAccountService()
					.mergeLoanRequestTPAApprovalTransactionVestedMoney(
							getTPAId(loanRequestForm.getContractNumber()),
							loanRequestForm.getTransactionId(),
							data.getAppOtherBalanceAmt(),
							data.getAppOtherVestingPct(),
							data.isAppOtherExcluded(), data.getMoneyTypes(),
							saveOnlyInd, calculatedInd);

		} catch (AccountException e) {
			throw new GenericException(2307, new String[] { e.getMessage() });

		} catch (Exception e) {
			throw new SystemException(e, this.getClass().getName(),
					"calculateVested", e.getMessage());
		}

		loanRequestForm.setLoanRequestData(loanRequestData);

		if(loanRequestData.getAppMaxLoanAmt()==null)


		loanRequestForm.setMaxLoanAvailable("");


	}

	protected String getCalculatedInd(LoanRequestForm theForm) {
		String calculatedInd = theForm.getAppMaxCalculated();

		//calculatedInd ="Y";
		if (theForm.getMaxLoanAvailable() != null &&theForm.getMaxLoanAvailable().length()>0) {
			if (theForm.getLoanRequestData().getAppMaxLoanAmt() == null) {

				calculatedInd = "N";
			} else {
				// values are the same
				if (theForm.getMaxLoanAvailable().equals(
						theForm.getLoanRequestData().getAppMaxLoanAmt()
								.toString())) {


					//continue
				} else {

					calculatedInd = "N";
				}
			}
		} else {

			calculatedInd = "";
		}

		return calculatedInd;
	}

	public void calculateMaxLoanAvailable(LoanRequestForm theForm,
			String maxLoanCalculatedInd, String saveOnlyInd)
			throws SystemException, GenericException {

		// retrieve values from page
		BigDecimal highestLoanBalanceInLast12Months = new BigDecimal(theForm
				.getHighestLoanBalanceInLast12Mths());
		int numberOfOutstandingLoans = Integer.parseInt(theForm
				.getNumberOfOutstandingLoans());
		BigDecimal currentOutstandingBalance = new BigDecimal(theForm
				.getCurrentOutstandingBalance());
		BigDecimal maxLoan = null;
		if (MAX_LOAN_CALCULATED_MANUAL.equalsIgnoreCase(maxLoanCalculatedInd)) {
			if (theForm.getMaxLoanAvailable() == null
					|| theForm.getMaxLoanAvailable().trim().length() == 0) {
				maxLoan = ZERO;

			} else {
				maxLoan = new BigDecimal(theForm.getMaxLoanAvailable());

			}
		}

        LoanRequestData loanRequestData = null;
		try {
			loanRequestData = getAccountService()
					.mergeLoanRequestTPAApprovalTransactionLoanInfo(
							getTPAId(theForm.getContractNumber()),
							theForm.getTransactionId(),
							highestLoanBalanceInLast12Months,
							numberOfOutstandingLoans,
							currentOutstandingBalance, maxLoan,
							maxLoanCalculatedInd, saveOnlyInd);


		} catch (AccountException e) {
			throw new GenericException(2307, new String[] { e.getMessage() });

		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException(e, this.getClass().getName(),
					"calculateHighestOutstandingLoanBalance", e.getMessage());
		}

		theForm.setLoanRequestData(loanRequestData);
		String maxAmount = loanRequestData.getAppMaxLoanAmt()==null?"":loanRequestData.getAppMaxLoanAmt().toString();
		theForm.setMaxLoanAvailable(maxAmount);



	}

	//=============================================================================

	protected Collection calculateRepaymentAndApproval(
			LoanRequestForm theForm) throws SystemException {

		Collection errors = null;
		LoanRequestData loanRequestData = null;

		try {
			calculateRepayment(theForm, SAVE_ONLY_YES);
		} catch (GenericException e) {
			errors.add(e);
		}

		if (errors == null || errors.size() == 0) {
			try {
				processApprovalInfo(theForm, SAVE_ONLY_YES);
			} catch (GenericException e) {
				errors.add(e);
			}
		}

		return errors;
	}

	protected void calculateRepayment(LoanRequestForm theForm,
			String saveOnlyInd) throws SystemException, GenericException {

		LoanRequestData data = theForm.getLoanRequestData();
		LoanRequestData loanRequestData = null;
		// retrieve values from page
		if (SAVE_ONLY_YES.equalsIgnoreCase(saveOnlyInd)
				&& theForm.getMaxAmortizationPeriod().trim().length() == 0) {
			theForm.setMaxAmortizationPeriod("0");
		}
		data.setMaxAmortizationYears(Integer.parseInt(theForm
				.getMaxAmortizationPeriod()));

		if (SAVE_ONLY_YES.equalsIgnoreCase(saveOnlyInd)
				&& theForm.getLoanInterestRate().trim().length() == 0) {
			theForm.setLoanInterestRate("0");
		}
		data.setAppInterestRatePct(new BigDecimal(theForm
						.getLoanInterestRate()));

		if (SAVE_ONLY_YES.equalsIgnoreCase(saveOnlyInd)
				&& theForm.getRepaymentFrequency().trim().length() == 0) {
			theForm.setRepaymentFrequency("0");
		}
		data.setAppPaymentsPerYear(Integer.parseInt(theForm
				.getRepaymentFrequency()));

		if (SAVE_ONLY_YES.equalsIgnoreCase(saveOnlyInd)
				&& theForm.getAmortizationPeriod().trim().length() == 0) {
			theForm.setAmortizationPeriod("0");
		}
		data.setAppAmortizationYears(Integer.parseInt(theForm
				.getAmortizationPeriod()));

		if (SAVE_ONLY_YES.equalsIgnoreCase(saveOnlyInd)
				&& theForm.getLoanAmount().trim().length() == 0) {
			theForm.setLoanAmount("0");
		}
		data.setAppLoanAmt(new BigDecimal(theForm.getLoanAmount()));

		try {
			loanRequestData = getAccountService()
					.mergeLoanRequestTPAApprovalTransactionRepayment(
							getTPAId(theForm.getContractNumber()),
							theForm.getTransactionId(),
							data.getMaxAmortizationYears(),
							data.getAppInterestRatePct(),
							data.getAppPaymentsPerYear(),
							data.getAppAmortizationYears(),
							data.getAppLoanAmt(), saveOnlyInd);

		} catch (AccountException e) {
			throw new GenericException(2307, new String[] { e.getMessage() });

		} catch (Exception e) {
			throw new SystemException(e, this.getClass().getName(),
					"calculateRepayment", e.getMessage());
		}

		theForm.setLoanRequestData(loanRequestData);
	}

	protected void processApprovalInfo(LoanRequestForm theForm,
			String saveOnlyInd) throws SystemException, GenericException {

		LoanRequestData loanRequestData = null;
		BigDecimal loanSetUpFee = new BigDecimal(theForm.getLoanSetupFee());
		Date planInfoExpiryDate;
		try {
			planInfoExpiryDate = DateFormatter.parse(theForm.getExpiryDate());

			if (theForm.getSpousalConsentRadio() == null) {
				theForm.setSpousalConsentRadio("");
			}

			loanRequestData = getAccountService()
					.mergeLoanRequestTPAApprovalTransactionApproval(
							getTPAId(theForm.getContractNumber()),
							theForm.getTransactionId(),
							"AP".equalsIgnoreCase(theForm.getProceedWithLoan()) ? true
									: false, loanSetUpFee,
							theForm.getAddComments(), planInfoExpiryDate,
							theForm.getDefProvision(),
							theForm.getSpousalConsentRadio(), saveOnlyInd);
		} catch (AccountException e) {
			throw new GenericException(2307, new String[] { e.getMessage() });
		} catch (Exception e) {
			throw new SystemException(e, this.getClass().getName(),
					"processApprovalInfo", e.getMessage());
		}

		theForm.setLoanRequestData(loanRequestData);
	}

	protected abstract void completeLoanAction(LoanRequestForm theForm,
			String tpaId) throws SystemException, AccountException;
}