/*
 * Created on Jul 5, 2006
 *
/**
 * Cash Account transaction handler for fees type of transactions.
 * @author Ludmila Stern
 */
package com.manulife.pension.ps.service.report.transaction.handler;
import com.manulife.pension.ps.service.report.transaction.valueobject.FeesTransactionReportItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDataItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryItem;

/**
 * @author sternlu
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FeesTransactionHandler extends AbstractTransactionHandler {

	public FeesTransactionHandler(String description) {
		super(description);
	}

	protected TransactionHistoryItem createTransactionHistoryItem(
			TransactionDataItem tx) {
		TransactionHistoryItem item = new FeesTransactionReportItem();
		item.setAmount(tx.getTransactionAmount());
		return item;
	}
}
