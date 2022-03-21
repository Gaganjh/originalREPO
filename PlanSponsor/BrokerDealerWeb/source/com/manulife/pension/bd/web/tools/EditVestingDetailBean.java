package com.manulife.pension.bd.web.tools;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.manulife.pension.lp.model.gft.CashAccount;
import com.manulife.pension.lp.model.gft.DirectDebitAccount;
import com.manulife.pension.lp.model.gft.PaymentInstruction;
import com.manulife.pension.service.util.FormatUtils;
import com.manulife.util.render.NumberRender;

/**
 * 
 * Utility class 
 * @author Diana Macean
 */
public class EditVestingDetailBean implements Serializable {

	public static final String INSTRUCTIONS_BEAN_NAME = "instructions";

	private static final int MAX_WIDTH = 9999;
	private static final String STATEMENTS_REQUESTED =
		"Yes. Statements will be produced.";
	private static final String STATEMENTS_NOT_REQUESTED =
		"No. Statements will not be produced.";
	private static final String FILE_NA = "n/a";

	private boolean displayBillPaymentSection = false;
	private boolean displayCreditPaymentSection = false;
	private boolean displayDebitFootnote = false;
	private PaymentInstruction [] instructions;
	private ArrayList paymentInstructions;
	private double vestingTotal = 0d;
	private double billTotal = 0d;
	private double creditTotal = 0d;
	private String status;
	private String confirmationNumber;
	private String generateStatementOption;
	private Date receivedDate;
	private Date payrollDate;
	private Date requestedEffectiveDate;
	private String sender;
	private String numberOfRecords;
	private String submissionType;
	private String totalAllocations;
	private String totalVestings;
	private String totalLoanRepayments;
	private Collection vestingsByMoneyType;
	private Collection statementDates = new ArrayList();		
	
	

	/**
	 * constructor
	 */
	public EditVestingDetailBean() {
	}

	/**
	 * confirmationNumber
	 */
	public String getConfirmationNumber() {
		return confirmationNumber;
	}

	/**
	 * @param confirmationNumber The confirmationNumber to set.
	 */
	public void setConfirmationNumber(String confirmationNumber) {
		this.confirmationNumber = confirmationNumber;
	}

	/**
	 * List of the payment vesting instructions represented as an array of 
	 * the SubmissionAccountBean beans 
	 * 
	 * @return ArrayList
	 */
	private void populatePaymentTotals() {

		vestingTotal = 0d;
		billTotal = 0d;
		creditTotal = 0d;
		paymentInstructions = new ArrayList();
		int size = 0;
		if (null != getInstructions()) size = getInstructions().length;
		
		if (getPaymentInstructions() != null)
		{
			// first determine the number of accounts
			int numAccounts = 0;
			PaymentInstruction lastPaymentInstruction = null; 

			SubmissionAccountBean submissionAccountBean = null;
			for (int i = 0; i < size; i++) {
				PaymentInstruction paymentInstruction = getInstructions()[i];
				
				// we can have up to three different instructions tied to the same account
				if (lastPaymentInstruction == null || !belongToSameAccount(paymentInstruction,lastPaymentInstruction)) {
					submissionAccountBean = new SubmissionAccountBean();
					String accountName = FormatUtils.formatAccountName(paymentInstruction.getPaymentAccount(), MAX_WIDTH);
					submissionAccountBean.setLabel(accountName);

					// add it to the display intructions so that they can be displayed correctly
					paymentInstructions.add(submissionAccountBean);
					numAccounts++;
				}
				
				// we can have up to three different instructions tied to the same account
				double thisAmount =	paymentInstruction.getAmount().getAmount().doubleValue();
				
				if(paymentInstruction.getPurposeCode().equals(PaymentInstruction.CONTRIBUTION)) {
					submissionAccountBean.setContributionValue(thisAmount);
					vestingTotal+=thisAmount;
				}
				else if(paymentInstruction.getPurposeCode().equals(PaymentInstruction.BILL_PAYMENT)) {
					submissionAccountBean.setBillValue(thisAmount);
					displayBillPaymentSection = true;
					if ( 
							thisAmount != 0d &&
							paymentInstruction.getPaymentAccount() instanceof DirectDebitAccount
					) displayDebitFootnote = true;
					billTotal+=thisAmount;
				}
				else if(paymentInstruction.getPurposeCode().equals(PaymentInstruction.TEMPORARY_CREDIT)) {
					submissionAccountBean.setCreditValue(thisAmount);
					displayCreditPaymentSection = true;
					if ( 
							thisAmount != 0d &&
							paymentInstruction.getPaymentAccount() instanceof DirectDebitAccount
					) displayDebitFootnote = true;
					creditTotal+=thisAmount;
				}
				lastPaymentInstruction = paymentInstruction;
			}
		}
	}

	/**
	 * Totals for the debit accounts 
	 * @return String
	 */
	public String getDebitAccountsTotal() {
		double DebitAcctTotal = 0.0;
		for (int i = 0; i < getInstructions().length; i++) {
			if (getInstructions()[i].getPaymentAccount()
				instanceof DirectDebitAccount) {
				DebitAcctTotal =
					(DebitAcctTotal
						+ getInstructions()[i]
						.getAmount()
						.getAmount().doubleValue());
			}
		}
		String totals =
			NumberRender.formatByType(
				new BigDecimal(DebitAcctTotal), 
				"0.00", "c", false);		
		return totals;
	}

	/**
	 * @return Returns the displayBillPaymentSection.
	 */
	public boolean isDisplayBillPaymentSection() {
		return displayBillPaymentSection;
	}
	/**
	 * @return Returns the displayBillPaymentSection.
	 */
	public boolean isDisplayTotalPaymentSection() {
		return displayBillPaymentSection || displayCreditPaymentSection;
	}
	/**
	 * @param displayBillPaymentSection The displayBillPaymentSection to set.
	 */
	public void setDisplayBillPaymentSection(boolean displayBillPaymentSection) {
		this.displayBillPaymentSection = displayBillPaymentSection;
	}
	/**
	 * @return Returns the displayCreditPaymentSection.
	 */
	public boolean isDisplayCreditPaymentSection() {
		return displayCreditPaymentSection;
	}
	/**
	 * @param displayCreditPaymentSection The displayCreditPaymentSection to set.
	 */
	public void setDisplayCreditPaymentSection(
			boolean displayCreditPaymentSection) {
		this.displayCreditPaymentSection = displayCreditPaymentSection;
	}
	
	private static boolean belongToSameAccount(PaymentInstruction one, PaymentInstruction two) {
		if (one == null || two == null) return false;
		
		if (
				!one.getPaymentAccount().getClass().equals(two.getPaymentAccount().getClass())
		)	return false;
		
		if (
				one.getPaymentAccount() instanceof CashAccount
				&& two.getPaymentAccount() instanceof CashAccount 
		)	return true;
		
		// First  account
		String oneNumber = ((DirectDebitAccount) one.getPaymentAccount()).getInstructionNumber();		
		oneNumber = (oneNumber == null)? "" : oneNumber.trim();
		
		
		// Second account
		String twoNumber = ((DirectDebitAccount) two.getPaymentAccount()).getInstructionNumber();		
		twoNumber = (twoNumber == null)? "" : twoNumber.trim();

		return oneNumber.equals(twoNumber);
	}

	/**
	 * @return Returns the billTotal.
	 */
	public double getBillTotal() {
		return billTotal;
	}
	/**
	 * @param billTotal The billTotal to set.
	 */
	public void setBillTotal(double billTotal) {
		this.billTotal = billTotal;
	}
	/**
	 * @return Returns the vestingTotal.
	 */
	public double getVestingTotal() {
		return vestingTotal;
	}
	/**
	 * @param vestingTotal The vestingTotal to set.
	 */
	public void setVestingTotal(double vestingTotal) {
		this.vestingTotal = vestingTotal;
	}
	/**
	 * @return Returns the creditTotal.
	 */
	public double getCreditTotal() {
		return creditTotal;
	}
	/**
	 * @param creditTotal The creditTotal to set.
	 */
	public void setCreditTotal(double creditTotal) {
		this.creditTotal = creditTotal;
	}
	/**
	 * @return Returns the status.
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status The status to set.
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return Returns the generateStatementOption.
	 */
	public String getGenerateStatementOption() {
		return generateStatementOption;
	}
	/**
	 * @param generateStatementOption The generateStatementOption to set.
	 */
	public void setGenerateStatementOption(String generateStatementOption) {
		this.generateStatementOption = generateStatementOption;
	}
	/**
	 * @return Returns the instructions.
	 */
	public ArrayList getPaymentInstructions() {
		return paymentInstructions;
	}
	/**
	 * @param paymentInstructions The paymentInstructions to set.
	 */
	public void setInstructions(PaymentInstruction[] instructions) {
		this.instructions = instructions;
		populatePaymentTotals();
	}
	/**
	 * @return Returns the paymentInstructions.
	 */
	public PaymentInstruction[] getInstructions() {
		return instructions;
	}
	/**
	 * @return Returns the paymentInstructionsTotal.
	 */
	public String getPaymentInstructionsTotal() {
		BigDecimal paymentInstructionsTotal = new BigDecimal(0D);
        if(getInstructions() != null && getInstructions().length > 0)
        {
            for(int i = 0; i < getInstructions().length; i++)
            	paymentInstructionsTotal = (paymentInstructionsTotal).add(getInstructions()[i].getAmount().getAmount());
        }
		String total =
			NumberRender.formatByType(
				paymentInstructionsTotal, 
				"0.00", "c", false);		
		return total;
 	}
	/**
	 * @return Returns the receivedDate.
	 */
	public Date getReceivedDate() {
		return receivedDate;
	}
	/**
	 * @param receivedDate The receivedDate to set.
	 */
	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}
	/**
	 * @return Returns the requestedEffectiveDate.
	 */
	public Date getRequestedEffectiveDate() {
		return requestedEffectiveDate;
	}
	/**
	 * @param requestedEffectiveDate The requestedEffectiveDate to set.
	 */
	public void setRequestedEffectiveDate(Date requestedEffectiveDate) {
		this.requestedEffectiveDate = requestedEffectiveDate;
	}
	/**
	 * @return Returns the sender.
	 */
	public String getSender() {
		return sender;
	}
	/**
	 * @param sender The sender to set.
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}
	/**
	 * @return Returns the numberOfRecords.
	 */
	public String getNumberOfRecords() {
		return numberOfRecords;
	}
	/**
	 * @param numberOfRecords The numberOfRecords to set.
	 */
	public void setNumberOfRecords(String numberOfRecords) {
		this.numberOfRecords = numberOfRecords;
	}
	/**
	 * @return Returns the payrollDate.
	 */
	public Date getPayrollDate() {
		return payrollDate;
	}
	/**
	 * @param payrollDate The payrollDate to set.
	 */
	public void setPayrollDate(Date payrollDate) {
		this.payrollDate = payrollDate;
	}
	/**
	 * @return Returns the submissionType.
	 */
	public String getSubmissionType() {
		return submissionType;
	}
	/**
	 * @param submissionType The submissionType to set.
	 */
	public void setSubmissionType(String submissionType) {
		this.submissionType = submissionType;
	}
	/**
	 * @return Returns the vestingsByMoneyType.
	 */
	public Collection getVestingsByMoneyType() {
		return vestingsByMoneyType;
	}
	/**
	 * @param vestingsByMoneyType The vestingsByMoneyType to set.
	 */
	public void setVestingsByMoneyType(Collection vestingsByMoneyType) {
		this.vestingsByMoneyType = vestingsByMoneyType;
	}
	/**
	 * @return Returns the totalAllocations.
	 */
	public String getTotalAllocations() {
		return totalAllocations;
	}
	/**
	 * @param totalAllocations The totalAllocations to set.
	 */
	public void setTotalAllocations(String totalAllocations) {
		this.totalAllocations = totalAllocations;
	}
	/**
	 * @return Returns the totalVestings.
	 */
	public String getTotalVestings() {
		return totalVestings;
	}
	/**
	 * @param totalVestings The totalVestings to set.
	 */
	public void setTotalVestings(String totalVestings) {
		this.totalVestings = totalVestings;
	}
	/**
	 * @return Returns the totalLoanRepayments.
	 */
	public String getTotalLoanRepayments() {
		return totalLoanRepayments;
	}
	/**
	 * @param totalLoanRepayments The totalLoanRepayments to set.
	 */
	public void setTotalLoanRepayments(String totalLoanRepayments) {
		this.totalLoanRepayments = totalLoanRepayments;
	}
	/**
	 * @return Returns the displayDebitFootnote.
	 */
	public boolean isDisplayDebitFootnote() {
		return displayDebitFootnote;
	}
	/**
	 * @param displayDebitFootnote The displayDebitFootnote to set.
	 */
	public void setDisplayDebitFootnote(boolean displayDebitFootnote) {
		this.displayDebitFootnote = displayDebitFootnote;
	}
	/**
	 * @return Returns the statementDates.
	 */
	public Collection getStatementDates() {
		return statementDates;
	}
	/**
	 * @param statementDates The statementDates to set.
	 */
	public void setStatementDates(Collection statementDates) {
		this.statementDates = statementDates;
	}		
}
