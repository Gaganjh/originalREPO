package com.manulife.pension.ps.web.iloans;

import java.math.BigDecimal;

import com.manulife.pension.ps.web.controller.PsForm;
import com.manulife.pension.ps.web.iloans.util.BigDecimalFormatter;
import com.manulife.pension.ps.web.iloans.util.DateFormatter;
import com.manulife.pension.ps.web.iloans.util.HTMLUtils;
import com.manulife.pension.service.account.valueobject.LoanPayrollFrequency;
import com.manulife.pension.service.account.valueobject.LoanRequestData;
import com.manulife.pension.service.account.valueobject.LoanRequestMoneyType;

/*
 File: ViewLoanRequestsForm.java

 Version   Date         Author           Change Description
 -------   ----------   --------------   ------------------------------------------------------------------
 CS1.0     2005-04-28   Chris Shin       Initial version.
 */

/**
 * This class is the action form for the View Loan requests form
 * 
 * @author Chris Shin
 * @version CS1.0 (April 28, 2005)
 */

public class LoanRequestBaseForm extends PsForm {

	protected static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

	protected static final BigDecimal ZERO = new BigDecimal("0");

	protected static final String CHECKED = "checked";

	private String errorMessage = "";

	private LoanRequestData loanRequestData;

	private boolean waiveFee = false;

	private boolean isApproved = true;

	public LoanRequestBaseForm() {
		super();
	}

	public String getAdditionalComments() {

		if (getLoanRequestData().getAppRemarks() == null) {
			return "";
		}

		return HTMLUtils.encode(getLoanRequestData().getAppRemarks());
	}

	public String getAppAmortizationPeriod() {

		String suffix = null;
		if (getLoanRequestData().getAppAmortizationYears() == 1) {
			suffix = " year";
		} else {
			suffix = " years";
		}
		return getLoanRequestData().getAppAmortizationYears() + suffix;

	}

	public String getAppDate() {

		if (getLoanRequestData().getAppDate() == null) {
			return "";
		}

		return DateFormatter.format(getLoanRequestData().getAppDate());
	}

	public String getAppLoanAmount() {

		if (getLoanRequestData().getAppLoanAmt() == null) {
			return "";
		}

		return getLoanRequestData().getAppLoanAmt().toString();
	}

	public String getAppLoanInterestRate() {

		if (getLoanRequestData().getAppInterestRatePct() == null
				|| ZERO.compareTo(getLoanRequestData().getAppInterestRatePct()) == 0) {
			return "";
		} else {
			return BigDecimalFormatter.format(getLoanRequestData()
					.getAppInterestRatePct(), -1, 4);
		}

	}

	public String getAppMaxLoanAvailable() {

		if (getLoanRequestData().getAppMaxLoanAmt() == null) {
			return "";
		}

		return getLoanRequestData().getAppMaxLoanAmt().toString();
	}

	public String getAppPaymentAmount() {

		if (getLoanRequestData().getAppPaymentAmt() == null) {
			return "";
		}

		return getLoanRequestData().getAppPaymentAmt().toString();
	}

	public String getAppPaymentFrequency() {

		int paymentsPerYear = getLoanRequestData().getAppPaymentsPerYear();
		return LoanPayrollFrequency
				.getDescriptionForPeriodsPerYear(paymentsPerYear);
	}

	public String getAppVestedAccountBalance() {

		if (getLoanRequestData().getAppVestedAccountAmt() != null) {
			return getLoanRequestData().getAppVestedAccountAmt().toString();
		} else {
			return "";
		}
	}

	public String getConfirmationNumber() {

		return "" + getLoanRequestData().getConfirmationNumber();
	}

	public String getContractName() {

		return HTMLUtils.encode(getLoanRequestData().getContractName());
	}

	public String getContractNumber() {

		return getLoanRequestData().getContractNumber();
	}

	public String getCurrentOutstandingLoanBalance() {

		return getLoanRequestData().getCurrentOutstandingLoanBalance()
				.toString();
	}

	public String getDefaultProvision() {

		if (getLoanRequestData().getAppDefaultProvision() == null) {
			return "";
		}

		return HTMLUtils.encode(getLoanRequestData().getAppDefaultProvision());
	}

	public String getReasonForLoan() {

		return HTMLUtils.encode(getLoanRequestData().getReasonForLoan());
	}

	/**
	 * getErrorMessage method comment.
	 */
	public String getErrorMessage() {
		return this.errorMessage;
	}

	public String getHighestLoanBalanceLast12Months() {
		if (getLoanRequestData().getMaxOutstandingLoanBalanceLast12Months() == null) {
			return "";
		}
		return getLoanRequestData().getMaxOutstandingLoanBalanceLast12Months()
				.toString();
	}

	protected LoanRequestData getLoanRequestData() {

		return this.loanRequestData;
	}

	public String getLoanSetUpFee() {
		return "" + getLoanRequestData().getAppLoanSetupFee();
	}

	public boolean getLoanSetupFeeReadonlyText() {

		if (getLoanRequestData().isContractLoanSetupFeeAutodeduct()) {
			return true;
		} else {
			return false;
		}
	}

	public String getMaxAmortizationYears() {

		if (getLoanRequestData().getMaxAmortizationYears() == 0) {
			return "";
		}

		return "" + getLoanRequestData().getMaxAmortizationYears();

	}

	public String getNumberOfOutstandingLoansCount() {

		return "" + getLoanRequestData().getOutstandingLoansCount();

	}

	public String getParticipantName() {

		return HTMLUtils.encode(getLoanRequestData().getParticipantName());
	}

	public String getParticipantSSN() {

		return this.getLoanRequestData().getParticipantSSN();
	}

	public String getPlanInfoExpiryDate() {

		return DateFormatter.format(getLoanRequestData().getAppExpiryDate());
	}

	public String getReqAmortizationPeriod() {

		String suffix = null;
		if (getLoanRequestData().getReqAmortizationYears() == 1) {
			suffix = " year";
		} else {
			suffix = " years";
		}
		return getLoanRequestData().getReqAmortizationYears() + suffix;
	}

	public String getReqDate() {

		if (getLoanRequestData().getReqDate() == null) {
			return "";
		}

		return DateFormatter.format(getLoanRequestData().getReqDate());
	}

	public String getReqLoanAmount() {

		if (getLoanRequestData().getReqLoanAmt() == null) {
			return "";
		}

		return getLoanRequestData().getReqLoanAmt().toString();
	}

	public String getReqLoanInterestRate() {
		if (getLoanRequestData().getReqInterestRatePct() == null) {
			return "";
		}

		return BigDecimalFormatter.format(getLoanRequestData()
				.getReqInterestRatePct(), -1, 4);
	}

	public String getReqLoanReasonCode() {

		if (getLoanRequestData().getReqLoanReasonCode().equals(
				LoanRequestData.LOAN_REASON_CODE_PRIMARY_RESIDENCE)) {
			return "Primary residence";
		} else if (getLoanRequestData().getReqLoanReasonCode().equals(
				LoanRequestData.LOAN_REASON_CODE_HARDSHIP)) {
			return "Hardship";
		} else {
			return "General purpose";
		}
	}

	public String getReqMaxLoanAvailable() {

		if (getLoanRequestData().getReqMaxLoanAmt() == null) {
			return "";
		}

		return getLoanRequestData().getReqMaxLoanAmt().toString();
	}

	public String getReqPaymentAmount() {

		if (getLoanRequestData().getReqPaymentAmt() == null) {
			return "";
		}

		return getLoanRequestData().getReqPaymentAmt().toString();
	}

	public String getReqPaymentFrequency() {

		int paymentsPerYear = getLoanRequestData().getReqPaymentsPerYear();
		return LoanPayrollFrequency
				.getDescriptionForPeriodsPerYear(paymentsPerYear);
	}

	public String getTotalParticipantBalance() {

		if (getLoanRequestData().getParticipantBalanceTotal() == null) {
			return null;
		}

		return this.getLoanRequestData().getParticipantBalanceTotal()
				.toString();
	}

	public String getLegallyMarried() {
		String legallyMarried ="";

		if ("Y".equalsIgnoreCase(getLoanRequestData().getLegallyMarried()))
			legallyMarried= "Yes";
		if ("N".equalsIgnoreCase(getLoanRequestData().getLegallyMarried())) 
			legallyMarried ="No";
		return legallyMarried;
	}

	public void setLoanRequestData(LoanRequestData loanRequestData) {
		this.loanRequestData = loanRequestData;
	}

	public boolean isLoanRequestApprovable() {

		return !getLoanRequestData().isCommitted();
	}

	public LoanRequestMoneyType[] getLoanRequestMoneyTypes() {

		return getLoanRequestData().getMoneyTypes();
	}

	public String getOtherMoneyTypeBalanceAmt() {
		if (getLoanRequestData().getAppOtherBalanceAmt() == null) {
			return "";
		}

		return getLoanRequestData().getAppOtherBalanceAmt().toString();
	}

	public String getOtherMoneyTypeBalanceVestingPct() {
		if (getLoanRequestData().getAppOtherVestingPct() == null) {
			return "";
		}

		return "" + getLoanRequestData().getAppOtherVestingPct();
	}

	public boolean isOtherMoneyTypeBalanceExcluded() {
		return getLoanRequestData().isAppOtherExcluded();
	}

	public String getApproveLoan() {

		if (LoanRequestData.REQUEST_STATUS_CODE_LOAN_APPROVED
				.equals(getLoanRequestData().getRequestStatusCode())) {
			return "Yes";
		} else if (LoanRequestData.REQUEST_STATUS_CODE_LOAN_DENIED
				.equals(getLoanRequestData().getRequestStatusCode())) {
			return "No";
		} else {
			return "Unknown";
		}
	}

	public String getSpousalConsent() {

		if ("Y".equalsIgnoreCase(getLoanRequestData().getSpousalConsent())) {
			return "Yes";
		} else {
			return "No";
		}
	}

	public boolean isTpaInitiated() {
		return getLoanRequestData().isTpaInitiated();

	}

	public boolean isRequestEditable() {

		if (getLoanRequestData().isTpaInitiated() || isLoanRequestApprovable()) {
			return true;
		}

		return false;

	}

	public String getAppMaxCalculated() {

		return getLoanRequestData().getAppMaxLoanCalculatedInd();

	}

}

