package com.manulife.pension.ps.service.report.transaction.handler;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDataItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryItem;

/**
 * Interface for Cash Account Transaction Handlers
 * 
 * @author Maria Lee
 * @author Charles Chan
 */
public interface TransactionHandler {

	/**
	 * Transforms a TransactionDataItem to a TransactionHistoryItem. A
	 * TransactionDataItem is typically constructed by the DAO and the
	 * TransactionHistoryItem is typically consumed by the front-end.
	 * 
	 * @param tx
	 *            The TransactionDataItem to transform.
	 * @return TransactionHistoryItem The converted history item.
	 * @throws SystemException
	 */
	TransactionHistoryItem transform(TransactionDataItem tx)
			throws SystemException;
}
