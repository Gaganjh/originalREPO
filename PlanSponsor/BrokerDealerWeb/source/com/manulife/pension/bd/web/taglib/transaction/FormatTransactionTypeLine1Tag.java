package com.manulife.pension.bd.web.taglib.transaction;

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
 *  
 */
public class FormatTransactionTypeLine1Tag extends BaseFormatTransactionTypeLine1Tag {
    
    private static final long serialVersionUID = 1L;

    private static final String DB_CONTRIBUTION_ACTION_PATH = "/do/bob/transaction/pptContributionDetailsReport/";
    private static final String ALLOCATION_ACTION_PATH = "/do/bob/transaction/contributionTransactionReport/";
    private static final String LOAN_REPAYMENT_ACTION_PATH = "/do/bob/transaction/loanRepaymentTransactionReport/";
    private static final String LINK_TO_LOAN_REPAYMENT_DETAILS_ACTION_PATH = "/do/bob/transaction/linkToLoanRepaymentDetailsHistoryReport/";
    private static final String REBAL_ACTION_PATH = "/do/bob/transaction/transactionDetailsRebalReport/";
    private static final String FTF_ACTION_PATH = "/do/bob/transaction/fundToFundTransactionReport/";
    private static final String ADJUSTMENT_ACTION_PATH = "/do/bob/transaction/pptContribAdjDetailsReport/";
    private static final String WITHDRAW_DETAILS_ACTION_PATH = "/do/bob/transaction/withdrawalDetailsReport/";
	private static final String CASH_ACCOUNT_ACTION_PATH = "/do/bob/transaction/cashAccountReport/";
	
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
					if ("R".equals(historyItem.getSubType())) 
						buffer.append(generateAnchor(REBAL_ACTION_PATH, historyItem, true));
					else if ("F".equals(historyItem.getSubType())
                        || "S".equals(historyItem.getSubType())) 
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

