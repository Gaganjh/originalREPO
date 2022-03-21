package com.manulife.pension.platform.web.report;


/**
 * Form for complete withdrawal detail page.
 * 
 *	@author Tamilarasu krishnamoorthy
 */
public class TransactionDetailsWithdrawalForm extends
		BaseReportForm {
	
	private static final long serialVersionUID = 1L;
	
	private String transactionNumber="";
	private String pptId="";
	private boolean showLiaWithdrawalMessage = false;
	private boolean showLiaWithdrawalNotification = false;
	private int payeeCount = 0;

	/**
	 * @return the pptId
	 */
	public String getPptId() {
		return pptId;
	}


	/**
	 * @param pptId the pptId to set
	 */
	public void setPptId(String pptId) {
		this.pptId = pptId;
	}


	/**
	 * Default Constructor
	 */
	public TransactionDetailsWithdrawalForm() {
		super();
	}	
	
	
	/**
	 * get the transaction number
	 * @return transactionNumber
	 */
	public String getTransactionNumber() {
		return transactionNumber;
	}
	
	/**
	 * Set the transaction number
	 * @param transactionNumber
	 */
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}


	/**
	 * @return
	 */
	public boolean isShowLiaWithdrawalMessage() {
		return showLiaWithdrawalMessage;
	}


	/**
	 * @param showLiaWithdrawalMessage
	 */
	public void setShowLiaWithdrawalMessage(boolean showLiaWithdrawalMessage) {
		this.showLiaWithdrawalMessage = showLiaWithdrawalMessage;
	}


	public boolean isShowLiaWithdrawalNotification() {
		return showLiaWithdrawalNotification;
	}


	public void setShowLiaWithdrawalNotification(
			boolean showLiaWithdrawalNotification) {
		this.showLiaWithdrawalNotification = showLiaWithdrawalNotification;
	}


	public int getPayeeCount() {
		return payeeCount;
	}


	public void setPayeeCount(int payeeCount) {
		this.payeeCount = payeeCount;
	}
	
	
	
}