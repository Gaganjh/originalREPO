package com.manulife.pension.ps.web.taglib.transaction;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import com.manulife.pension.platform.web.taglib.BaseTagHelper;
import com.manulife.pension.platform.web.taglib.transaction.BaseFormatParticipantTransactionTypeTag;
import com.manulife.pension.service.report.participant.transaction.handler.TransactionType;
import com.manulife.pension.service.report.participant.transaction.valueobject.TransactionHistoryItem;


/**
 * This tag is used for the Participant Transaction History page. 
 * It formats the transaction type line with the appropriate links.
 * 
 * @author: Kristin Kerr
 *  
 */
public class FormatParticipantTransactionTypeTag extends BaseFormatParticipantTransactionTypeTag {
	
    private static final long serialVersionUID = 1L;

    private static final String ALLOCATION_ACTION_PATH = "/do/transaction/pptContributionDetailsReport/";
	private static final String LOAN_REPAYMENT_DETAILS_ACTION_PATH = "/do/transaction/loanRepaymentDetailsReport/";
	private static final String REBAL_ACTION_PATH = "/do/transaction/transactionDetailsRebalReport/";
	private static final String FTF_ACTION_PATH = "/do/transaction/fundToFundTransactionReport/";
	private static final String ADJUSTMENT_ACTION_PATH = "/do/transaction/pptContribAdjDetailsReport/";
	private static final String CLASS_CONVESRION_ACTION_PATH = "/do/transaction/classConversionTransactionReport/";
    private static final String DEFERRAL_DEF_PATH = "/do/transaction/deferralChangeDetailsReport/";
	private static final String DEFERRAL_ACI_PATH = "/do/transaction/aciReport/";
	private static final String WITHDRAW_DETAILS_ACTION_PATH = "/do/transaction/withdrawalDetailsReport/";
	private static final String CASH_ACCOUNT_ACTION_PATH = "/do/transaction/cashAccountReport/";
    
	private static final String PARTICIPANT_ID = "participantId";
    private static final String PROFILE_ID = "profileId"; // 
	
	public int doStartTag() throws JspException {
		
		try {
			TransactionHistoryItem historyItem = 
				(TransactionHistoryItem) pageContext.findAttribute(getItem());
			
		    participantId = (String) pageContext.getRequest().getAttribute(PARTICIPANT_ID);
            profileId = (String) pageContext.getRequest().getAttribute(PROFILE_ID); // see
                                                                                           // ParticipantTransactionHistoryAction
			
			StringBuffer buffer = new StringBuffer();
			String type = historyItem.getType();

			if (BaseTagHelper.isPrintFriendly(pageContext)) { 
				buffer.append(historyItem.getTypeDescription1());
				
			} else if (type.equals(TransactionType.ALLOCATION)) {
				buffer.append(generateAnchor(ALLOCATION_ACTION_PATH, historyItem, 0));
			
			} else if (type.equals(TransactionType.LOAN_REPAYMENT)) { 
				buffer.append(generateAnchor(LOAN_REPAYMENT_DETAILS_ACTION_PATH, historyItem, 0));
			
			} else if (type.equals(TransactionType.LOAN_ISSUE)) {			
				buffer.append(generateAnchor(LOAN_REPAYMENT_DETAILS_ACTION_PATH, historyItem, 0));
			
			} else if (type.equals(TransactionType.LOAN_DEFAULT)) {
				buffer.append(generateAnchor(LOAN_REPAYMENT_DETAILS_ACTION_PATH, historyItem, 0));
				
			} else if (type.equals(TransactionType.LOAN_TRANSFER)) {
				buffer.append(generateAnchor(LOAN_REPAYMENT_DETAILS_ACTION_PATH, historyItem, 0));
			
			} else if (type.equals(TransactionType.ADJUSTMENT)) {
				buffer.append(generateAnchor(ADJUSTMENT_ACTION_PATH, historyItem, 0));
				
			} else if (type.equals(TransactionType.PBA_TRANSFER_IN_KIND)) {
				buffer.append(generateAnchor(ALLOCATION_ACTION_PATH, historyItem, 0));		
				
			} else if (type.equals(TransactionType.DEFERRALS_DEF)) {
				buffer.append(generateAnchor(DEFERRAL_DEF_PATH, historyItem, 2)); // use profileId param		 
				
			} else if (type.equals(TransactionType.DEFERRALS_ACI)) {
				buffer.append(generateAnchor(DEFERRAL_ACI_PATH, historyItem, 2)); // use profileId param		
				
			} else if (type.equals(TransactionType.INTER_ACCOUNT_TRANSFER)) {
					if (historyItem.getLinkToRebalPage()) 
						buffer.append(generateAnchor(REBAL_ACTION_PATH, historyItem, 1));
					else if (historyItem.getLinkToFTFPage()) 
						buffer.append(generateAnchor(FTF_ACTION_PATH, historyItem, 1));	
					else if (historyItem.getLinkToClassConversionPage()) 
						buffer.append(generateAnchor(CLASS_CONVESRION_ACTION_PATH, historyItem, 1));	
			} else if (type.equals(TransactionType.WITHDRAWAL)) {
				buffer.append(generateAnchor(WITHDRAW_DETAILS_ACTION_PATH, historyItem, 1));
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

