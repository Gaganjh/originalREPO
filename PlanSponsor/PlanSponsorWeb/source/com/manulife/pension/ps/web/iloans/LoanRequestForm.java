package com.manulife.pension.ps.web.iloans;

import java.math.BigDecimal;
import java.util.Date;

import com.manulife.pension.ps.web.iloans.util.DateFormatter;

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

public class LoanRequestForm extends LoanRequestBaseForm {

	private boolean loaded;
	

	private String[] moneyType;

	private String[] moneyTypeAmount;

	private String[] moneyTypeVesting;

	private String[] moneyTypeCategory;

	private String[] moneyTypeId;

	private String[] moneyTypeCheckbox;

	private String highestLoanBalanceInLast12Mths;

	private String numberOfOutstandingLoans;

	private String currentOutstandingBalance;

	private String maxLoanAvailable;

	private String otherMoneyType;

	private String otherMoneyTypeAmount;

	private String otherMoneyTypeVesting;

	private boolean otherMoneyTypeExclude;

	private String transactionId;

	private String button = "";

	private String maxAmortizationPeriod;

	private String loanInterestRate;

	private String repaymentFrequency;

	private String amortizationPeriod;

	private String loanAmount;

	private String expiryDate;

	private String loanSetupFee;

	private String spousalConsentRadio;

	private String defProvision;

	private String addComments;

	private String proceedWithLoan;

	private boolean page2Entered = false;

    private boolean isGatewayStatusActive = false;

	public static final String FIELD_NUMBER_OF_OUTSTANDING_LOAN_REQUESTS = "numberOfOustandingLoanRequests";

	public static final String FIELD_CURRENT_OUTSTANDING_BALANCE = "currentOutstandingBalance";

	public static final String FIELD_HIGHEST_LOAN_BALANCE_12_MTHS = "highestLoanBalance12Mths";

	public static final String FIELD_MAX_AMORTIZATION_PERIOD = "maxAmortizationPeriod";

	public static final String FIELD_LOAN_INTEREST_RATE = "loanInterestRate";

	public static final String FIELD_REPAYMENT_FREQUENCY = "repaymentFreq";

	public static final String FIELD_MAX_LOAN_AVAILABLE = "maxLoanAvailable";

	public static final String FIELD_AMORTIZATION_PERIOD = "amortizationPeriod";

	public static final String FIELD_LOAN_AMOUNT = "loanAmount";

	public static final String FIELD_REPAYMENT_AMOUNT = "repaymentAmount";

	public static final String FIELD_EXPIRY_DATE = "expiryDate";

	public static final String FIELD_DEFAULT_PROVISION = "defaultProvision";

	public static final String FIELD_LOAN_SETUP_FEE = "loanSetupFee";

	public static final String FIELD_SPOUSAL_CONSENT = "spousalConsent";

	public static final String FIELD_ADDITIONAL_COMMENTS = "additionalComments";

	/**
	 * @return Returns the currentOutstandingBalance.
	 */
	public String getCurrentOutstandingBalance() {
		return currentOutstandingBalance;
	}

	/**
	 * @param currentOutstandingBalance
	 *            The currentOutstandingBalance to set.
	 */
	public void setCurrentOutstandingBalance(String currentOutstandingBalance) {
		this.currentOutstandingBalance = currentOutstandingBalance;
	}

	/**
	 * @return Returns the highestLoanBalanceInLast12Mths.
	 */
	public String getHighestLoanBalanceInLast12Mths() {
		return highestLoanBalanceInLast12Mths;
	}

	/**
	 * @param highestLoanBalanceInLast12Mths
	 *            The highestLoanBalanceInLast12Mths to set.
	 */
	public void setHighestLoanBalanceInLast12Mths(
			String highestLoanBalanceInLast12Mths) {
		this.highestLoanBalanceInLast12Mths = highestLoanBalanceInLast12Mths;
	}

	/**
	 * @return Returns the maxLoanAvailable.
	 */
	public String getMaxLoanAvailable() {
		return maxLoanAvailable;
	}

	/**
	 * @param maxLoanAvailable
	 *            The maxLoanAvailable to set.
	 */
	public void setMaxLoanAvailable(String maxLoanAvailable) {
		this.maxLoanAvailable = maxLoanAvailable;
	}

	/**
	 * @return Returns the moneyTypeAmount.
	 */
	public String[] getMoneyTypeAmount() {
		return moneyTypeAmount;
	}

	/**
	 * @param moneyTypeAmount
	 *            The moneyTypeAmount to set.
	 */
	public void setMoneyTypeAmount(String[] moneyTypeAmount) {
		this.moneyTypeAmount = moneyTypeAmount;
	}

	/**
	 * @return Returns the moneyTypeAmount
	 */
	public String getMoneyTypeAmount(int index) {
		return this.moneyTypeAmount[index];
	}

	/**
	 * @param moneyTypeAmount
	 *            The moneyTypeAmount to set.
	 */
	public void setMoneyTypeAmount(int index, String moneyTypeAmount) {
		this.moneyTypeAmount[index] = moneyTypeAmount;
	}

	/**
	 * @return Returns the moneyTypeVesting.
	 */
	public String[] getMoneyTypeVesting() {
		return moneyTypeVesting;
	}

	/**
	 * @param moneyTypeVesting
	 *            The moneyTypeVesting to set.
	 */
	public void setMoneyTypeVesting(String[] moneyTypeVesting) {
		this.moneyTypeVesting = moneyTypeVesting;
	}

	/**
	 * @return Returns the moneyTypeVesting
	 */
	public String getMoneyTypeVesting(int index) {
		return this.moneyTypeVesting[index];
	}

	/**
	 * @param moneyTypeVesting
	 *            The moneyTypeVesting to set.
	 */
	public void setMoneyTypeVesting(int index, String moneyTypeVesting) {
		this.moneyTypeVesting[index] = moneyTypeVesting;
	}

	/**
	 * @return Returns the numberOfOutstandingLoans.
	 */
	public String getNumberOfOutstandingLoans() {
		return numberOfOutstandingLoans;
	}

	/**
	 * @param numberOfOutstandingLoans
	 *            The numberOfOutstandingLoans to set.
	 */
	public void setNumberOfOutstandingLoans(String numberOfOutstandingLoans) {
		this.numberOfOutstandingLoans = numberOfOutstandingLoans;
	}

	/**
	 * @return Returns the moneyType.
	 */
	public String[] getMoneyType() {
		return moneyType;
	}

	/**
	 * @param moneyType
	 *            The moneyType to set.
	 */
	public void setMoneyType(String[] moneyType) {

		this.moneyType = moneyType;
	}

	/**
	 * @return Returns the moneyType
	 */
	public String getMoneyType(int index) {
		return this.moneyType[index];
	}

	/**
	 * @param moneyType
	 *            The moneyType to set.
	 */
	public void setMoneyType(int index, String moneyType) {
		this.moneyType[index] = moneyType;
	}

	/**
	 * @return Returns the moneyTypeCategory.
	 */
	public String[] getMoneyTypeCategory() {
		return moneyTypeCategory;
	}

	/**
	 * @param moneyTypeCategory
	 *            The moneyTypeCategory to set.
	 */
	public void setMoneyTypeCategory(String[] moneyTypeCategory) {
		this.moneyTypeCategory = moneyTypeCategory;
	}

	/**
	 * @return Returns the moneyTypeCategory
	 */
	public String getMoneyTypeCategory(int index) {
		return this.moneyTypeCategory[index];
	}

	/**
	 * @param moneyTypeCategory
	 *            The moneyTypeCategory to set.
	 */
	public void setMoneyTypeCategory(int index, String moneyTypeCategory) {
		this.moneyTypeCategory[index] = moneyTypeCategory;
	}

	/**
	 * @return Returns the otherMoneyType.
	 */
	public String getOtherMoneyType() {
		return otherMoneyType;
	}

	/**
	 * @param otherMoneyType
	 *            The otherMoneyType to set.
	 */
	public void setOtherMoneyType(String otherMoneyType) {
		this.otherMoneyType = otherMoneyType;
	}

	/**
	 * @return Returns the otherMoneyTypeAmount.
	 */
	public String getOtherMoneyTypeAmount() {
		return otherMoneyTypeAmount;
	}

	/**
	 * @param otherMoneyTypeAmount
	 *            The otherMoneyTypeAmount to set.
	 */
	public void setOtherMoneyTypeAmount(String otherMoneyTypeAmount) {
		this.otherMoneyTypeAmount = otherMoneyTypeAmount;
	}

	/**
	 * @return Returns the otherMoneyTypeExclude.
	 */
	public boolean isOtherMoneyTypeExclude() {
		return otherMoneyTypeExclude;
	}

	/**
	 * @param otherMoneyTypeExclude
	 *            The otherMoneyTypeExclude to set.
	 */
	public void setOtherMoneyTypeExclude(boolean otherMoneyTypeExclude) {
		this.otherMoneyTypeExclude = otherMoneyTypeExclude;
	}

	/**
	 * @return Returns the otherMoneyTypeVesting.
	 */
	public String getOtherMoneyTypeVesting() {
		return otherMoneyTypeVesting;
	}

	/**
	 * @param otherMoneyTypeVesting
	 *            The otherMoneyTypeVesting to set.
	 */
	public void setOtherMoneyTypeVesting(String otherMoneyTypeVesting) {
		this.otherMoneyTypeVesting = otherMoneyTypeVesting;
	}

	/**
	 * @return Returns the button.
	 */
	public String getButton() {
		return button;
	}

	/**
	 * @param button
	 *            The button to set.
	 */
	public void setButton(String button) {
		this.button = button;
	}

	/**
	 * @return Returns the transactionId.
	 */
	public String getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionId
	 *            The transactionId to set.
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * @return Returns the moneyTypeCheckbox.
	 */
	public String[] getMoneyTypeCheckbox() {
		return moneyTypeCheckbox;
	}

	/**
	 * @param moneyTypeCheckbox
	 *            The moneyTypeCheckbox to set.
	 */
	public void setMoneyTypeCheckbox(String[] moneyTypeCheckbox) {
		this.moneyTypeCheckbox = moneyTypeCheckbox;
	}

	public void addMoneyType(String moneyTypeCheckbox) {
		String[] newCheckbox = new String[this.moneyTypeCheckbox.length + 1];
		for (int i = 0; i < this.moneyTypeCheckbox.length; i++) {
			newCheckbox[i] = this.moneyTypeCheckbox[i];
		}

		newCheckbox[this.moneyTypeCheckbox.length] = moneyTypeCheckbox;
		this.moneyTypeCheckbox = newCheckbox;
	}

	/**
	 * @return Returns the moneyTypeId.
	 */
	public String[] getMoneyTypeId() {
		return moneyTypeId;
	}

	/**
	 * @return Returns the moneyTypeId.
	 */
	public String getMoneyTypeId(int index) {
		return moneyTypeId[index];
	}

	/**
	 * @param moneyTypeId
	 *            The moneyTypeId to set.
	 */
	public void setMoneyTypeId(String[] moneyTypeId) {
		this.moneyTypeId = moneyTypeId;
	}

	/**
	 * @return Returns the loanAmount.
	 */
	public String getLoanAmount() {
		return loanAmount;
	}

	/**
	 * @param loanAmount
	 *            The loanAmount to set.
	 */
	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}

	/**
	 * @return Returns the loanInterestRate.
	 */
	public String getLoanInterestRate() {
		return loanInterestRate;
	}

	/**
	 * @param loanInterestRate
	 *            The loanInterestRate to set.
	 */
	public void setLoanInterestRate(String loanInterestRate) {
		this.loanInterestRate = loanInterestRate;
	}

	/**
	 * @return Returns the maxAmortizationPeriod.
	 */
	public String getMaxAmortizationPeriod() {
		return maxAmortizationPeriod;
	}

	/**
	 * @param maxAmortizationPeriod
	 *            The maxAmortizationPeriod to set.
	 */
	public void setMaxAmortizationPeriod(String maxAmortizationPeriod) {
		this.maxAmortizationPeriod = maxAmortizationPeriod;
	}

	/**
	 * @return Returns the expiryDate.
	 */
	public String getExpiryDate() {
		return expiryDate;
	}

	/**
	 * @param planInfoExpiryDate
	 *            The expiryDate to set.
	 */
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	/**
	 * @return Returns the repaymentFrequency.
	 */
	public String getRepaymentFrequency() {
		return repaymentFrequency;
	}

	/**
	 * @param repaymentFrequency
	 *            The repaymentFrequency to set.
	 */
	public void setRepaymentFrequency(String repaymentFrequency) {
		this.repaymentFrequency = repaymentFrequency;
	}

	/**
	 * @return Returns the amortizationPeriod.
	 */
	public String getAmortizationPeriod() {
		return amortizationPeriod;
	}

	/**
	 * @param amortizationPeriod
	 *            The amortizationPeriod to set.
	 */
	public void setAmortizationPeriod(String amortizationPeriod) {
		this.amortizationPeriod = amortizationPeriod;
	}

	/**
	 * @return Returns the loanSetupFee.
	 */
	public String getLoanSetupFee() {
		return loanSetupFee;
	}

	/**
	 * @param loanSetupFee
	 *            The loanSetupFee to set.
	 */
	public void setLoanSetupFee(String loanSetupFee) {
		this.loanSetupFee = loanSetupFee;
	}

	/**
	 * @return Returns the spousalConsentCheckbox.
	 */
	public String getSpousalConsentRadio() {
		return spousalConsentRadio;
	}

	/**
	 * @param spousalConsentRadio
	 *            The spousalConsentRadio to set.
	 */
	public void setSpousalConsentRadio(String spousalConsentRadio) {
		this.spousalConsentRadio = spousalConsentRadio;
	}

	/**
	 * @return Returns the addComments.
	 */
	public String getAddComments() {
		return addComments;
	}

	/**
	 * @param addComments
	 *            The addComments to set.
	 */
	public void setAddComments(String addComments) {
		this.addComments = addComments;
	}

	/**
	 * @return Returns the defProvision.
	 */
	public String getDefProvision() {
		return defProvision;
	}

	/**
	 * @param defProvision
	 *            The defProvision to set.
	 */
	public void setDefProvision(String defProvision) {
		this.defProvision = defProvision;
	}

	/**
	 * @return Returns the proceedWithLoan.
	 */
	public String getProceedWithLoan() {
		return proceedWithLoan;
	}

	/**
	 * @param proceedWithLoan
	 *            The proceedWithLoan to set.
	 */
	public void setProceedWithLoan(String proceedWithLoan) {
		this.proceedWithLoan = proceedWithLoan;
	}

	public String getAppDate() {

		// Until commit, appDate in loanRequestData will be null,
		// so use today's date until committal.

		if (isLoanRequestApprovable()) {
			return DateFormatter.format(new Date());
		} else {
			return super.getAppDate();
		}

	}

	/**
	 * @return Returns the page2Entered.
	 */
	public boolean isPage2Entered() {
		return page2Entered;
	}

	/**
	 * @param page2Entered
	 *            The page2Entered to set.
	 */
	public void setPage2Entered(boolean page2Entered) {
		this.page2Entered = page2Entered;
	}

	/**
	 * @param loaded
	 *            The loaded to set.
	 */
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	/**
	 * @return Returns the loaded.
	 */
	public boolean isLoaded() {
		return loaded;
	}
	
	public boolean isModified(){
		return isTpaInitiated() && isPage2Entered();
	}
	
	public boolean isStep3FieldsChanged()
	{
		boolean step3FieldsChanged = false;
		try{
		if(Integer.parseInt(maxAmortizationPeriod) !=getLoanRequestData().getMaxAmortizationYears())
			step3FieldsChanged=true;
		else if (!new BigDecimal(loanInterestRate).equals(getLoanRequestData().getAppInterestRatePct()))
				step3FieldsChanged=true;
		else if(Integer.parseInt(repaymentFrequency)!=getLoanRequestData().getAppPaymentsPerYear())
			step3FieldsChanged=true;
		else if (Integer.parseInt(amortizationPeriod)!=getLoanRequestData().getAppAmortizationYears())
			step3FieldsChanged=true;
		else if (!new BigDecimal(loanAmount).equals(getLoanRequestData().getAppLoanAmt()))
				step3FieldsChanged=true;
		}catch(Exception e)
		{
			step3FieldsChanged=true;
		}
		return step3FieldsChanged;
	}

    public boolean isGatewayStatusActive() {
        return isGatewayStatusActive;
    }

    public void setGatewayStatusActive(boolean isGatewayStatusActive) {
        this.isGatewayStatusActive = isGatewayStatusActive;
    }

}

