package com.manulife.pension.ps.web.taglib.transaction;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import com.manulife.pension.platform.web.taglib.BaseTagHelper;
import com.manulife.pension.platform.web.taglib.transaction.BaseFormatTransactionTypeLine1Tag;
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
public class FormatTransactionTypeLine1Tag extends BaseFormatTransactionTypeLine1Tag {

	private static final String DB_CONTRIBUTION_ACTION_PATH="/do/transaction/pptContributionDetailsReport/";
	private static final String ALLOCATION_ACTION_PATH = "/do/transaction/contributionTransactionReport/";
	private static final String LOAN_REPAYMENT_ACTION_PATH = "/do/transaction/loanRepaymentTransactionReport/";
	private static final String LINK_TO_LOAN_REPAYMENT_DETAILS_ACTION_PATH = "/do/transaction/linkToLoanRepaymentDetailsHistoryReport/";
	private static final String REBAL_ACTION_PATH = "/do/transaction/transactionDetailsRebalReport/";
	private static final String FTF_ACTION_PATH = "/do/transaction/fundToFundTransactionReport/";
	private static final String ADJUSTMENT_ACTION_PATH = "/do/transaction/pptContribAdjDetailsReport/";
	private static final String WITHDRAW_DETAILS_ACTION_PATH = "/do/transaction/withdrawalDetailsReport/";
	private static final String CASH_ACCOUNT_ACTION_PATH = "/do/transaction/cashAccountReport/";
	
	public int doStartTag() throws JspException {
		
		try {
			TransactionHistoryItem historyItem = 
				(TransactionHistoryItem) pageContext.findAttribute(getItem());
			
			StringBuffer buffer = new StringBuffer();
			String type = historyItem.getType();

			if (BaseTagHelper.isPrintFriendly(pageContext)) { 
				buffer.append(historyItem.getTypeDescription1());
				
			} else if (type.equals(TransactionType.ALLOCATION)) {
				if ("true".equalsIgnoreCase(this.getDbContract())) {
					buffer.append(generateAnchor(DB_CONTRIBUTION_ACTION_PATH, historyItem, true));
				} else {
					buffer.append(generateAnchor(ALLOCATION_ACTION_PATH, historyItem, false));	
				}			
			} else if (type.equals(TransactionType.LOAN_REPAYMENT)) { 
				buffer.append(generateAnchor(LOAN_REPAYMENT_ACTION_PATH, historyItem, false));
			
			} else if (type.equals(TransactionType.LOAN_ISSUE)) {			
				buffer.append(generateAnchor(LINK_TO_LOAN_REPAYMENT_DETAILS_ACTION_PATH, historyItem, false));
			} else if (type.equals(TransactionType.LOAN_TRANSFER)) {			
				buffer.append(generateAnchor(LINK_TO_LOAN_REPAYMENT_DETAILS_ACTION_PATH, historyItem, false));
			
			} else if (type.equals(TransactionType.LOAN_DEFAULT)) {
				buffer.append(generateAnchor(LINK_TO_LOAN_REPAYMENT_DETAILS_ACTION_PATH, historyItem, false));
			
			} else if (type.equals(TransactionType.ADJUSTMENT)) {
				if ("true".equalsIgnoreCase(this.getDbContract())) {
					buffer.append(generateAnchor(ADJUSTMENT_ACTION_PATH, historyItem, true)); // bogus really not used by target
				} else {
					buffer.append(generateAnchor(ADJUSTMENT_ACTION_PATH, historyItem, false));
				}
			
			} else if (type.equals(TransactionType.INTER_ACCOUNT_TRANSFER)) {
					if (historyItem.getSubType().equals("R")) 
						buffer.append(generateAnchor(REBAL_ACTION_PATH, historyItem, true));
					else if (historyItem.getSubType().equals("F") || historyItem.getSubType().equals("S")) 
						buffer.append(generateAnchor(FTF_ACTION_PATH, historyItem, true));
			} else if (type.equals(TransactionType.WITHDRAWAL)) {
				buffer.append(generateAnchor(WITHDRAW_DETAILS_ACTION_PATH, historyItem, true));
			} else {
				buffer.append(historyItem.getTypeDescription1());
			}

		pageContext.getOut().print(buffer.toString());

		} catch (IOException ex) {
			throw new JspException(ex.getMessage());
		}
		return SKIP_BODY;
	}
	
}

