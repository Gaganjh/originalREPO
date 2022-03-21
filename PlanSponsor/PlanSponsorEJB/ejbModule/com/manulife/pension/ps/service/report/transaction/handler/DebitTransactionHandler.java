package com.manulife.pension.ps.service.report.transaction.handler;

import com.manulife.pension.ps.service.report.transaction.valueobject.DebitTransactionReportItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDataItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryItem;

/**
 * @author Maria Lee
 * @author Charles Chan
 */
public class DebitTransactionHandler extends AbstractTransactionHandler {
	
	private static final String PENDING = "Pending ";
	private static final String NO = "N";
	
	public DebitTransactionHandler(String description) {
		super(description);
	}

	protected TransactionHistoryItem createTransactionHistoryItem(
			TransactionDataItem tx) {

		TransactionHistoryItem item = null;

		if (isForwardMode(tx.getTransactionMode())) {
			/*
			 * Real debit For debit transactions, credit amount is always zero
			 * unless it is a reversal.
			 */
			item = new DebitTransactionReportItem();
		} else {
			/*
			 * Reverse debit For debit transactions, debit amount is the
			 * transaction amount Reversals are reported as credit amount.
			 */
			item = new DebitTransactionReportItem(true);
		}
		item.setAmount(tx.getTransactionAmount());
		return item;
	}
	
	/**
	 * Overriden method to return the line 1 description
	 * 
	 * @param tx
	 *            is the transaction returned from Apollo
	 * @return String is the line 1 description 
	 * @exception Exception
	 */
 	public String getDescriptionLine1(TransactionDataItem tx) {
		
		return prefixDescriptionLine1WithPending(tx, super.txDescription1);
		
	} 
 	
 	public String getDescriptionLine1Unmasked(TransactionDataItem tx) {
		
		return getDescriptionLine1(tx);
		
	}
	
	/**
	 * Prefix the line 1 decription with "Pending" if
	 * the debit txn is not tied to credit
	 * 
	 * @param tx
	 * 				is the transaction returned from Apollo
	 * 		  description
	 * 				is the line 1 description
	 * @return String
	 * 				is the line 1 description with or without "Pending"
	 */
	protected String prefixDescriptionLine1WithPending(TransactionDataItem tx, String description) {
		
		if (tx.getDebitTiedToCreditFlag() != null) {
			if (tx.getDebitTiedToCreditFlag().equalsIgnoreCase(NO) )
				return PENDING + description;
		}
		return description;
	}
}