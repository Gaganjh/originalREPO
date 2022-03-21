package com.manulife.pension.platform.web.taglib.transaction;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.manulife.pension.ps.service.report.transaction.handler.TransactionType;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryItem;

/**
 * This tag is used for the Transaction History page. 
 * It formats the 1st line of the transaction type line 1 with
 * the appropriate links.
 * 
 * @author: Maria Lee
 *  
 */
public abstract class BaseFormatTransactionTypeLine1Tag extends TagSupport {

    private static final long serialVersionUID = 1L;

    private static final String ANCHOR_START_PRE_URL = "<a href=\"";
    private static final String ANCHOR_START_PRE_URL_ID = "<a id=\"";
    private static final String ANCHOR_START_PRE_URL_HREF = "\" href=\"";
	private static final String ANCHOR_START_POST_URL = "\">";	
	private static final String ANCHOR_END = "</a>";
	
	private static final String PARAM_START = "?";
	private static final String PARAM_SEPARATOR = "&";
	private static final String PARAM_PARTICIPANT_ID = "participantId=";
	private static final String PARAM_ALTERNATE_PARTICIPANT_ID = "pptId="; 
	private static final String PARAM_TRANSACTION_NUMBER = "transactionNumber=";
	private static final String PARAM_TRANSACTION_DATE = "transactionDate=";
	private static final String PARAM_TASK = "task=";
	private static final String PARAM_TYPE = "type=";
	private static final String PARAM_FILTER_STRING = "filter";
	
	private String item;
	private String dbContract;
	
    public abstract int doStartTag() throws JspException;

    /**
     * generates anchor tag
     * 
     * @param action
     * @param item
     * @param useAltParam
     * @return String
     */
	protected String generateAnchor(String action, TransactionHistoryItem item, boolean useAltParam) {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(ANCHOR_START_PRE_URL_ID); 
		buffer.append(item.getTransactionNumber()); 
		buffer.append(ANCHOR_START_PRE_URL_HREF);
		buffer.append(action); 
		if (item.getType().equals(TransactionType.LOAN_ISSUE) 
				|| item.getType().equals(TransactionType.LOAN_TRANSFER) 
				|| item.getType().equals(TransactionType.LOAN_DEFAULT)) {
			buffer.append(generateLinkToLoanRepaymentParameters(item));
		} else {
			buffer.append(generateParameters(item, useAltParam));
		}
		buffer.append(ANCHOR_START_POST_URL); 				
		buffer.append(item.getTypeDescription1());
		buffer.append(ANCHOR_END);
		
		return buffer.toString();
	}
	
	/**
	 * 
	 * @param genAltParam don't want participantId referenced in any way for defined benefit version
	 */
	private String generateParameters(TransactionHistoryItem item, boolean useAltParam) {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(PARAM_START);
		if(!TransactionType.WITHDRAWAL.equals(item.getType())){
			buffer.append(PARAM_TASK).append(PARAM_FILTER_STRING);		
			buffer.append(PARAM_SEPARATOR);
		}
		buffer.append(PARAM_TRANSACTION_NUMBER).append(item.getTransactionNumber());
		buffer.append(PARAM_SEPARATOR);
		buffer.append(PARAM_TRANSACTION_DATE).append(item.getTransactionDate());
		buffer.append(PARAM_SEPARATOR);				
		if (useAltParam) {
			buffer.append(PARAM_ALTERNATE_PARTICIPANT_ID).append(String.valueOf(item.getParticipantId()));
		} else {
		buffer.append(PARAM_PARTICIPANT_ID).append(String.valueOf(item.getParticipantId()));
		}

		return buffer.toString();
	}
	
	/**
     * generates anchor tag for non parameterized URLs. 
     * 
     * @param action
     * @param item
     * @return String
     */
	protected String generateAnchor(String action, TransactionHistoryItem item) {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(ANCHOR_START_PRE_URL); 
		buffer.append(action); 
		buffer.append(ANCHOR_START_POST_URL); 				
		buffer.append(item.getTypeDescription1());
		buffer.append(ANCHOR_END);
		
		return buffer.toString();
	}
	
	private String generateLinkToLoanRepaymentParameters(TransactionHistoryItem item) {
		
		StringBuffer buffer = new StringBuffer();
		String type = item.getType();
		if (item.getType().equals(TransactionType.LOAN_ISSUE)) {
			type = com.manulife.pension.service.report.participant.transaction.handler.TransactionType.REQUEST_PS_LOAN_ISSUE;
		}else if (item.getType().equals(TransactionType.LOAN_TRANSFER)) {
			type = com.manulife.pension.service.report.participant.transaction.handler.TransactionType.REQUEST_PS_LOAN_TRANSFER;
			
		} else 	if (item.getType().equals(TransactionType.LOAN_DEFAULT)) {
			type = 	com.manulife.pension.service.report.participant.transaction.handler.TransactionType.REQUEST_PS_LOAN_CLOSURE;
		}
		buffer.append(PARAM_START);
		buffer.append(PARAM_TYPE).append(type);				
		buffer.append(PARAM_SEPARATOR);
		buffer.append(PARAM_TRANSACTION_NUMBER).append(item.getTransactionNumber());
		buffer.append(PARAM_SEPARATOR);
		buffer.append(PARAM_TRANSACTION_DATE).append(item.getTransactionDate());
		buffer.append(PARAM_SEPARATOR);				
		buffer.append(PARAM_PARTICIPANT_ID).append(String.valueOf(item.getParticipantId()));
		
		return buffer.toString();
	}

	/**
	 * Gets the item
	 * 
	 * @return Returns a String
	 */
	public String getItem() {
		return item;
	}

	/**
	 * Sets the item
	 * 
	 * @param item
	 *            The item to set
	 */
	public void setItem(String item) {
		this.item = item;
	}

	/**
     * Gets the dbContract
     * 
     * @return Returns a String
     */
	public String getDbContract() {
		return dbContract;
	}

	/**
     * Sets the dbContract
     * 
     * @param dbContract The dbContract to set
     */
	public void setDbContract(String dbContract) {
		this.dbContract = dbContract;
	}
}

