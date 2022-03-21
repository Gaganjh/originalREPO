package com.manulife.pension.bd.web.taglib.transaction;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.manulife.pension.bd.web.bob.transaction.TransactionHistoryReportForm;
import com.manulife.pension.platform.web.taglib.BaseTagHelper;
import com.manulife.pension.ps.service.report.transaction.handler.TransactionType;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryItem;
import com.manulife.util.render.DateRender;

/**
 * This tag is used for the Transaction History page. 
 * It determines if a link to the participant history summary page is required.
 * 
 *  
 */
public class ParticipantTransactionHistoryIconTag extends TagSupport {

    private static final long serialVersionUID = 1L;

    private static final String FORM_NAME = "transactionHistoryReportForm";
	private static final String ANCHOR_START_PRE_URL = "<a href=\"";
	private static final String ANCHOR_START_POST_URL = "\" class=\"transaction_history\" title=\"Transaction History\">";	
	private static final String ANCHOR_END = "</a>";
	private static final String PARTICPANT_HISTORY_ACTION = "/do/bob/transaction/participantTransactionHistory/";
	private static final String PARAM_START = "?";
	private static final String PARAM_SEPARATOR = "&";
	private static final String PARAM_PARTICIPANT_ID = "participantId=";
	private static final String PARAM_FROM_DATE = "fromDate=";
	private static final String PARAM_TO_DATE = "toDate=";
	private static final String PARAM_TYPE = "transactionType=";
	private static final String PARAM_TASK = "task=";
	private static final String PARAM_FILTER_STRING = "filter";
	private static final String FORMAT_DATE_SHORT_MDY = "MM/dd/yyyy";


	private static Map<String, String> transactionTypesMap;

	private String item;
	
	static {
		/* a map keyed by contract requested txn types 
		   and values are the participant requested txn types */		
		transactionTypesMap = new HashMap<String, String>();
		transactionTypesMap.put(TransactionType.ADJUSTMENT, 
				com.manulife.pension.service.report.participant.transaction.handler.TransactionType.REQUEST_PS_ADJUSTMENT);
		transactionTypesMap.put(TransactionType.INTER_ACCOUNT_TRANSFER,
				com.manulife.pension.service.report.participant.transaction.handler.TransactionType.REQUEST_PS_INTER_ACCOUNT_TRANSFER);
		transactionTypesMap.put(TransactionType.LOAN_DEFAULT, 
				com.manulife.pension.service.report.participant.transaction.handler.TransactionType.REQUEST_PS_LOAN_CLOSURE);
		transactionTypesMap.put(TransactionType.LOAN_ISSUE, 
				com.manulife.pension.service.report.participant.transaction.handler.TransactionType.REQUEST_PS_LOAN_ISSUE);
		transactionTypesMap.put(TransactionType.LOAN_TRANSFER, 
				com.manulife.pension.service.report.participant.transaction.handler.TransactionType.REQUEST_PS_LOAN_TRANSFER);
		transactionTypesMap.put(TransactionType.WITHDRAWAL, 
				com.manulife.pension.service.report.participant.transaction.handler.TransactionType.REQUEST_PS_WITHDRAWAL);
	}

	public int doStartTag() throws JspException {
		
		try {
			TransactionHistoryItem historyItem = 
				(TransactionHistoryItem) pageContext.findAttribute(getItem());
			
			TransactionHistoryReportForm form = 
				(TransactionHistoryReportForm) pageContext.findAttribute(FORM_NAME);

			StringBuffer buffer = new StringBuffer();
			String type = historyItem.getType();
			
			Date dtFromDate = new Date((Long.valueOf(form.getFromDate())).longValue());
			String fromDate = DateRender.formatByPattern(dtFromDate, "", FORMAT_DATE_SHORT_MDY);
			Date dtToDate = new Date((Long.valueOf(form.getToDate())).longValue());
			String toDate = DateRender.formatByPattern(dtToDate, "", FORMAT_DATE_SHORT_MDY);

			if (!BaseTagHelper.isPrintFriendly(pageContext) &&
					(type.equals(TransactionType.ADJUSTMENT)
					|| type.equals(TransactionType.INTER_ACCOUNT_TRANSFER)
					|| type.equals(TransactionType.LOAN_DEFAULT)
					|| type.equals(TransactionType.LOAN_ISSUE)
					|| type.equals(TransactionType.LOAN_TRANSFER)
					|| type.equals(TransactionType.WITHDRAWAL)) ) {
				
				String participantIdString = String.valueOf(historyItem.getParticipantId());
				buffer.append(ANCHOR_START_PRE_URL); 
				buffer.append(PARTICPANT_HISTORY_ACTION); 
				buffer.append(PARAM_START); 
				buffer.append(PARAM_TASK).append(PARAM_FILTER_STRING);				
				buffer.append(PARAM_SEPARATOR); 
				buffer.append(PARAM_PARTICIPANT_ID).append(participantIdString);
				buffer.append(PARAM_SEPARATOR);
				buffer.append(PARAM_FROM_DATE).append(fromDate);
				buffer.append(PARAM_SEPARATOR);
				buffer.append(PARAM_TO_DATE).append(toDate);
				buffer.append(PARAM_SEPARATOR);
				buffer.append(PARAM_TYPE).append(transactionTypesMap.get(form.getTransactionType()));				
				buffer.append(ANCHOR_START_POST_URL); 				
				buffer.append(ANCHOR_END);
			} else {
				buffer.append(" ");
			}
			pageContext.getOut().print(buffer.toString());

		} catch (IOException ex) {
			throw new JspException(ex.getMessage());
		}
		return SKIP_BODY;
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

