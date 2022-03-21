package com.manulife.pension.bd.web.tools;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.manulife.pension.lp.model.gft.CashAccount;
import com.manulife.pension.lp.model.gft.DirectDebitAccount;
import com.manulife.pension.lp.model.gft.GFTUploadDetail;
import com.manulife.pension.lp.model.gft.PaymentInstruction;
import com.manulife.pension.bd.web.tools.util.SubmissionTypeTranslater;
import com.manulife.pension.service.util.FormatUtils;
import com.manulife.util.render.NumberRender;

/**
 * 
 * Utility class, intent to expose the GFTUploadDetail object to
 * the JSP Layer 
 * @author Tony Tomasone
 */
public class SubmissionUploadDetailBean implements Serializable {

	public static final String INSTRUCTIONS_BEAN_NAME = "instructions";

	private static final int MAX_WIDTH = 9999;
	private static final String STATEMENTS_REQUESTED =
		"Yes. Participant statements for the relevant quarters will be generated.";
	private static final String STATEMENTS_NOT_REQUESTED =
		"No. Participant statements will not be triggered at this time.";
	private static final String STATEMENTS_NA =
		"Not applicable.";

	private static final String FILE_NA = "n/a";

	private GFTUploadDetail data;
	private boolean displayBillPaymentSection = false;
	private boolean displayCreditPaymentSection = false;
	private boolean displayGenerateStatementSection = false;
	private boolean displayDebitFootnote = false;
	private ArrayList instructions;
	private double contributionTotal = 0d;
	private double billTotal = 0d;
	private double creditTotal = 0d;
	private String status = "";
	private String systemStatus = "";
	private Collection statementDates = new ArrayList();	
	

	/**
	 * constructor
	 */
	public SubmissionUploadDetailBean() {
	}

	/**
	 * FileUploadDetailBean constructor
	 */
	public SubmissionUploadDetailBean(GFTUploadDetail gftUploadDetail) {
		this.data = gftUploadDetail;
		populatePaymentInstructions();
	}

	/**
	 * ConfirmationNumber
	 */
	public String getConfirmationNumber() {
		return this.data.getSubmissionId();
	}

	/**
	 * FileName
	 */
	public String getFileName() {
		return this.data.getFileName();
	}

	/**
	 * FileTypeName
	 */
	public String getSubmissionTypeName() {
		if (this.data.getSubmissionTypeCode() != null) {
			return SubmissionTypeTranslater.translate(this.data.getSubmissionTypeCode());
		} else {
			return FILE_NA;
		}
	}

	/**
	 * LastPayrollForQuarterOption
	 */
	public String getLastPayrollForQuarterOption() {
		//	return this.data.isLastPayrollForQuarter()?"Yes. Statements were requested.":"n/a.";
		if (this.data.isLastPayrollForQuarter() == null) { 
			return STATEMENTS_NA;
		}
		
		return this.data.isLastPayrollForQuarter().booleanValue()
			? STATEMENTS_REQUESTED
			: STATEMENTS_NOT_REQUESTED;
	}

	/**
	 * List of the payment contribution instructions represented as an array of 
	 * the SubmissionAccountBean beans 
	 * 
	 * @return ArrayList
	 */
	public ArrayList getPaymentInstructions() {
		return instructions;
	}
	/**
	 * List of the payment contribution instructions represented as an array of 
	 * the SubmissionAccountBean beans 
	 * 
	 * @return ArrayList
	 */
	private ArrayList populatePaymentInstructions() {

		contributionTotal = 0d;
		billTotal = 0d;
		creditTotal = 0d;
		instructions = new ArrayList();
		
		if (this.data.getPaymentInstructions() != null)
		{
			// first determine the number of accounts
			int numAccounts = 0;
			PaymentInstruction lastPaymentInstruction = null; 

			SubmissionAccountBean submissionAccountBean = null;
            int size = this.data.getPaymentInstructions().length;
			for (int i = 0; i < size; i++) {
				PaymentInstruction paymentInstruction = this.data.getPaymentInstructions()[i];
				
				// we can have up to three different instructions tied to the same account
				if (lastPaymentInstruction == null || !belongToSameAccount(paymentInstruction,lastPaymentInstruction)) {
					submissionAccountBean = new SubmissionAccountBean();
					String accountName = FormatUtils.formatAccountName(paymentInstruction.getPaymentAccount(), MAX_WIDTH);
					submissionAccountBean.setLabel(accountName);

					// add it to the display intructions so that they can be displayed correctly
					instructions.add(submissionAccountBean);
					numAccounts++;
				}
				
				// we can have up to three different instructions tied to the same account
				double thisAmount =	paymentInstruction.getAmount().getAmount().doubleValue();
				
				if(paymentInstruction.getPurposeCode().equals(PaymentInstruction.CONTRIBUTION)) {
					submissionAccountBean.setContributionValue(thisAmount);
					contributionTotal+=thisAmount;
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
			
		return instructions;
	}

	/**
	 * Totals for the payment instructions 
	 * @return String
	 */
	public String getPaymentInstructionsTotal() {
		String totals =
			NumberRender.formatByType(
				this.data.getPaymentTotalAmount().getAmount(), 
				"0.00", "c", false);		
		return totals;
	}

	/**
	 * Totals for the debit accounts 
	 * @return String
	 */
	public String getDebitAccountsTotal() {
		double debitAcctTotal = 0.0;
		for (int i = 0; i < this.data.getPaymentInstructions().length; i++) {
			if (this.data.getPaymentInstructions()[i].getPaymentAccount()
				instanceof DirectDebitAccount) {
				debitAcctTotal =
					(debitAcctTotal
						+ this
							.data
							.getPaymentInstructions()[i]
							.getAmount()
							.getAmount().doubleValue());
			}
		}
		String totals =
			NumberRender.formatByType(
				new BigDecimal(debitAcctTotal), 
				"0.00", "c", false);		
		return totals;
	}

	/**
	 * ReceivedDate
	 */
	public Date getReceivedDate() {
		return data.getReceivedDate();
	}

	/**
	 * RequestedEffectiveDate
	 */
	public Date getRequestedEffectiveDate() {
		return data.getRequestedPaymentEffectiveDate();
	}
	
	/**
	 * Sender
	 */
	public String getSender() {
		return this.data.getUserName();
	}
	public void setSender(String userName) {
		this.data.setUserName(userName);
	}
	/**
	 * FileUploadExists
	 */
	public boolean isFileUploadExists() {
		return this.data.getFileName() != null
			&& this.data.getFileName().trim().length() > 0;
	}
	/**
	 * PaymentInstructionsExists
	 */
	public boolean isPaymentInstructionsExists() {
		return this.data.getPaymentTotalAmount().isPositive();
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
	 * @return Returns the contributionTotal.
	 */
	public double getContributionTotal() {
		return contributionTotal;
	}
	/**
	 * @param contributionTotal The contributionTotal to set.
	 */
	public void setContributionTotal(double contributionTotal) {
		this.contributionTotal = contributionTotal;
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
	 * @return Returns the displayGenerateStatementSection.
	 */
	public boolean isDisplayGenerateStatementSection() {
		return displayGenerateStatementSection;
	}
	/**
	 * @param displayGenerateStatementSection The displayGenerateStatementSection to set.
	 */
	public void setDisplayGenerateStatementSection(
			boolean displayGenerateStatementSection) {
		this.displayGenerateStatementSection = displayGenerateStatementSection;
	}
	/**
	 * @return Returns the userId.
	 */
	public String getSubmitterId() {
		return this.data.getUserSSN();
	}
	/**
	 * @return Returns the systemStatus.
	 */
	public String getSystemStatus() {
		return systemStatus;
	}
	/**
	 * @param systemStatus The systemStatus to set.
	 */
	public void setSystemStatus(String systemStatus) {
		this.systemStatus = systemStatus;
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
