package com.manulife.pension.platform.web.taglib.transaction;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.manulife.pension.service.report.participant.transaction.handler.TransactionType;
import com.manulife.pension.service.report.participant.transaction.valueobject.TransactionHistoryItem;


/**
 * This tag is used for the Participant Transaction History page. 
 * It formats the transaction type line with the appropriate links.
 * 
 * @author: Kristin Kerr
 *  
 */
public abstract class BaseFormatParticipantTransactionTypeTag extends TagSupport {
	
    private static final long serialVersionUID = 1L;

    private static Logger logger = Logger.getLogger(BaseFormatParticipantTransactionTypeTag.class);
	
	private static final String ANCHOR_START_PRE_URL = "<a href=\"";
	private static final String ANCHOR_START_POST_URL = "\">";	
	private static final String ANCHOR_START_PRE_URL_ID = "<a id=\"";
	private static final String ANCHOR_START_PRE_URL_HREF = "\" href=\"";
	private static final String ANCHOR_END = "</a>";
    
	private static final String PARAM_START = "?";
	private static final String PARAM_SEPARATOR = "&";
	private static final String PARAM_PARTICIPANT_ID = "participantId=";
	private static final String PARAM_ALTERNATE_PARTICIPANT_ID = "pptId="; // alternate used for defined benefit case
	private static final String PARAM_PROFILE_ID = "profileId=";
	private static final String PARAM_TRANSACTION_NUMBER = "transactionNumber=";
	private static final String PARAM_TRANSACTION_DATE = "transactionDate=";
	private static final String PARAM_TASK = "task=";
	private static final String PARAM_FILTER_STRING = "filter";
	private static final String PARAM_LOAN_NUMBER = "loanNumber=";

	private String item;
	protected String participantId;
    protected String profileId;
	
	public abstract int doStartTag() throws JspException;

	 /**
     * generates anchor tag
     * 
     * @param action
     * @param item
     * @param useAltParam
     * @return String
     */
	protected String generateAnchor(String action, TransactionHistoryItem item, int paramSet) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(ANCHOR_START_PRE_URL_ID); 
		buffer.append(item.getType()+item.getTransactionNumber()); 
		buffer.append(ANCHOR_START_PRE_URL_HREF);
		buffer.append(action);
		if (item.getType().equals(TransactionType.LOAN_REPAYMENT) ||
			item.getType().equals(TransactionType.LOAN_ISSUE) ||
			item.getType().equals(TransactionType.LOAN_DEFAULT)) {
			buffer.append(generateLoanParameters(item));
		} else {
			buffer.append(generateParameters(item, paramSet));
		}
		buffer.append(ANCHOR_START_POST_URL); 				
		buffer.append(item.getTypeDescription1());
		buffer.append(ANCHOR_END);
		
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
	
	/**
	 * @param useAlternateParam for defined benefit, we don't want to see participantId anywhere.
	 */
	private String generateParameters(TransactionHistoryItem item, int paramSet) {
		
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
		if (paramSet == 1) {
			buffer.append(PARAM_ALTERNATE_PARTICIPANT_ID).append(participantId);
		} else if (paramSet == 2) {
			buffer.append(PARAM_PROFILE_ID).append(profileId);			
		} else {
		    buffer.append(PARAM_PARTICIPANT_ID).append(participantId);
		}

		return buffer.toString();
	}
	
	private String generateLoanParameters(TransactionHistoryItem item) {
			
		StringBuffer buffer = new StringBuffer();
		String loanNumber = retrieveLoanNumber(item);
		buffer.append(PARAM_START);
		buffer.append(PARAM_TASK).append(PARAM_FILTER_STRING);				
		buffer.append(PARAM_SEPARATOR);
		buffer.append(PARAM_LOAN_NUMBER).append(loanNumber);
		buffer.append(PARAM_SEPARATOR);			
		buffer.append(PARAM_PARTICIPANT_ID).append(participantId);

		return buffer.toString();
	}
	
	private String retrieveLoanNumber(TransactionHistoryItem item) {
		String loanNumber = null;
		if (item.getType().equals(TransactionType.LOAN_REPAYMENT)) {
			loanNumber = item.getTypeDescription1().substring(23);
		} else if (item.getType().equals(TransactionType.LOAN_ISSUE)) {
			loanNumber = item.getTypeDescription1().substring(19);
		} else if (item.getType().equals(TransactionType.LOAN_DEFAULT)) {
			loanNumber = item.getTypeDescription1().substring(21);
		}
		try {
			Integer.parseInt(loanNumber);
		} catch (NumberFormatException e) {
			logger.error("Unable to parse loan number from loan description: " + item.getTypeDescription1());
		}
		return loanNumber;
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

}

