/*
 * Created on Aug 8, 2005
 *
 */
package com.manulife.pension.ps.service.report.transaction.handler;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDataItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryItem;

/**
 * @author Stephan Marceau
 *
 * A handler specific for Inter Account Transfers.
 * Will handle transforming to FundToFundTransfer or to a Rebalance transaction as required. 
 */
public class InterAccountTransferHandler extends GenericTransactionHandler {
	
	public static String FUND_TO_FUND_DESCRIPTION = "Fund to fund transfer";
	public static String REBALANCE_DESCRIPTION = "Rebalance";
	public static String CLASS_CONVERSION_DESCRIPTION = "Class conversion";
	
	public static String CLASS_CONVERSION_REASON = "CC";


	/**
	 * @param description
	 */
	public InterAccountTransferHandler() {
		super("");
	}

	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.service.report.transaction.handler.AbstractTransactionHandler#createTransactionHistoryItem(com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDataItem)
	 */
	protected TransactionHistoryItem createTransactionHistoryItem(
			TransactionDataItem tx) {
		TransactionHistoryItem item = super.createTransactionHistoryItem(tx);
		if (tx.getTransactionReasonCode().equals(CLASS_CONVERSION_REASON)) {
			this.txDescription1 = CLASS_CONVERSION_DESCRIPTION;
		} else if (tx.getTransferInProtocolCode().equals("R")) {
			this.txDescription1 = REBALANCE_DESCRIPTION;
		} else if (tx.getTransferInProtocolCode().equals("F") || tx.getTransferInProtocolCode().equals("S")) {
			this.txDescription1 = FUND_TO_FUND_DESCRIPTION;
		}
		return item;
}

	/**
	 * Returns the 2nd line of descriptions format: "xxx-xx-nnnn lastname,
	 * firstname"
	 */
	public String getDescriptionLine2(TransactionDataItem tx)
			throws SystemException {
		return getSsnDescription(tx);
	}
	
	/**
	 * SSE024 do not mask SSN for download full ssn permission
	 * Returns the 2nd line of descriptions forma: ssn, lastname,
	 * firstname"
	 */
	public String getDescriptionLine2Unmasked(TransactionDataItem tx)
			throws SystemException {
		return getSsnDescriptionUnmasked(tx);
	}

}

