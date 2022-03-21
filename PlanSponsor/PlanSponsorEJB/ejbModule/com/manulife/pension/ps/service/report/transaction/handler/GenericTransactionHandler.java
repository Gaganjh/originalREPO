package com.manulife.pension.ps.service.report.transaction.handler;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDataItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryItem;

/**
 * This class represents a generic transaction handler.
 * 
 * @author Charles Chan
 */
public class GenericTransactionHandler extends AbstractTransactionHandler {

	public static final String DESC_SSN_LAST_FIRST_NAME = "DESC_SSN_LAST_FIRST_NAME";

	/**
	 * Constructor.
	 * 
	 * @param typeDescription
	 * @param description
	 */
	public GenericTransactionHandler(String description) {
		super(description);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.service.report.transaction.handler.AbstractTransactionHandler#createTransactionHistoryItem(com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDataItem)
	 */
	protected TransactionHistoryItem createTransactionHistoryItem(
			TransactionDataItem tx) {
		TransactionHistoryItem item = new TransactionHistoryItem();
		item.setAmount(tx.getTransactionAmount());
		return item;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.service.report.transaction.handler.AbstractTransactionHandler#getDescriptionLine1(com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDataItem)
	 */
	public String getDescriptionLine1(TransactionDataItem tx)
			throws SystemException {
		if (txDescription1.equals(DESC_SSN_LAST_FIRST_NAME)) {
			return getSsnDescription(tx);
		} else {
			return super.getDescriptionLine1(tx);
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.service.report.transaction.handler.AbstractTransactionHandler#getDescriptionLine1(com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDataItem)
	 *
	 * SSE024 do not mask SSN for download full ssn permission
	 * Returns the 2nd line of descriptions forma: ssn, lastname,
	 * firstname"
	 */
	public String getDescriptionLine1Unmasked(TransactionDataItem tx)
			throws SystemException {
		if (txDescription1.equals(DESC_SSN_LAST_FIRST_NAME)) {
			return getSsnDescriptionUnmasked(tx);
		} else {
			return super.getDescriptionLine1Unmasked(tx);
		}
	}
}

