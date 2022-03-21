package com.manulife.pension.ps.web.tools;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import com.manulife.pension.lp.model.gft.DirectDebitAccount;
import com.manulife.pension.lp.model.gft.GFTUploadDetail;
import com.manulife.pension.ps.web.tools.util.SubmissionTypeTranslater;
import com.manulife.pension.service.util.FormatUtils;
import com.manulife.util.render.NumberRender;

/**
 * Utility class, intent to expose the GFTUploadDetail object to
 * the JSP Layer 
 * 
 */
public class FileUploadDetailBean implements java.io.Serializable {

	public static final String INSTRUCTIONS_BEAN_NAME = "instructions";

	private static final int MAX_WIDTH = 40;
	private static final String STATEMENTS_REQUESTED =
		"Yes. Statements will be produced.";
	private static final String STATEMENTS_NOT_REQUESTED =
		"No. Statements will not be produced.";
	private static final String STATEMENTS_NA =
		"Not applicable.";

	private static final String FILE_NA = "n/a";

	private GFTUploadDetail data;

	/**
	 * constructor
	 */
	public FileUploadDetailBean() {
	}

	/**
	 * FileUploadDetailBean constructor
	 */
	public FileUploadDetailBean(GFTUploadDetail gftUploadDetail) {
		this.data = gftUploadDetail;
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
     * email
     */
    public String getEmail() {
        return this.data.getNotificationEmailAddress();
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

		if (this.data.isLastPayrollForQuarter() == null) {
			return STATEMENTS_NA;
		}
		//	return this.data.isLastPayrollForQuarter()?"Yes. Statements were requested.":"n/a.";
		return this.data.isLastPayrollForQuarter().booleanValue()
			? STATEMENTS_REQUESTED
			: STATEMENTS_NOT_REQUESTED;
	}

	/**
	 * List of the payment instructions represented as an array of 
	 * the PaymentAccountBean beans (LabelValueBean)
	 * 
	 * @return ArrayList
	 */
	public ArrayList getPaymentInstructions() {
		ArrayList instructions = new ArrayList();

		if (this.data.getPaymentInstructions() != null)
		{
			for (int i = 0; i < this.data.getPaymentInstructions().length; i++) {
				String accountName =
					FormatUtils.formatAccountName(
						this.data.getPaymentInstructions()[i].getPaymentAccount(),
						MAX_WIDTH);
				String amount =
					NumberRender.formatByType(this
							.data
							.getPaymentInstructions()[i]
							.getAmount()
							.getAmount(), "0.00", "c", false);
				// add it as a label/value pair
				instructions.add(
					new PaymentAccountBean(
						INSTRUCTIONS_BEAN_NAME,
						accountName,
						"String",
						amount));
	
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
		double DebitAcctTotal = 0.0;
		for (int i = 0; i < this.data.getPaymentInstructions().length; i++) {
			if (this.data.getPaymentInstructions()[i].getPaymentAccount()
				instanceof DirectDebitAccount) {
				DebitAcctTotal =
					(DebitAcctTotal
						+ this
							.data
							.getPaymentInstructions()[i]
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
	 * List of the payment instructions without Debit Accounts 
	 * represented as an array of the PaymentAccountBean beans (LabelValueBean)
	 * 
	 * @return ArrayList
	 */
	public ArrayList getPaymentInstructionsSuppressDebitAccounts() {
		ArrayList instructions = new ArrayList();

		for (int i = 0; i < this.data.getPaymentInstructions().length; i++) {
			if (!(this.data.getPaymentInstructions()[i].getPaymentAccount()
				instanceof DirectDebitAccount)) {
				String accountName =
					FormatUtils.formatAccountName(
						this
							.data
							.getPaymentInstructions()[i]
							.getPaymentAccount(),
						48);
				String amount =
					NumberRender.formatByType(
						this
							.data
							.getPaymentInstructions()[i]
							.getAmount()
							.getAmount(), 
						"0.00", "c", false);	
				// add it as a label/value pair
				instructions.add(
					new PaymentAccountBean(
						INSTRUCTIONS_BEAN_NAME,
						accountName,
						"String",
						amount));
			}
		}
		return instructions;
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
	 * override the sender name
	 * @param userName
	 */
	public void setSender(String userName) {
		this.data.setUserName(userName);
	}
	
	/**
	 * get the File Information
	 */
	public String getFileInformation() {
		return this.data.getFileInformation();
	}
	

}
