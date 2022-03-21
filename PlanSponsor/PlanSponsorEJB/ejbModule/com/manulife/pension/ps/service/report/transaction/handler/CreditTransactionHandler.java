package com.manulife.pension.ps.service.report.transaction.handler;

import com.manulife.pension.ps.service.report.transaction.valueobject.CreditTransactionReportItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDataItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryItem;

/**
 * Cash Account transaction handler for credit type of transactions.
 * 
 * @author Maria Lee
 * @author Charles Chan
 */
public class CreditTransactionHandler extends AbstractTransactionHandler {

	public CreditTransactionHandler(String description) {
		super(description);
	}

	protected TransactionHistoryItem createTransactionHistoryItem(
			TransactionDataItem tx) {
		TransactionHistoryItem item = new CreditTransactionReportItem();
		item.setAmount(tx.getTransactionAmount());
		return item;
	}
}