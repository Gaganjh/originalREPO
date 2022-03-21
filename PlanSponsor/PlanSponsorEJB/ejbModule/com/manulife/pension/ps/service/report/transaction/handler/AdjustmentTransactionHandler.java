package com.manulife.pension.ps.service.report.transaction.handler;

import java.math.BigDecimal;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDataItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryItem;

/**
 * Cash Account transaction handler for Adjustments.
 * 
 * @author Charles Chan
 */
public class AdjustmentTransactionHandler extends GenericTransactionHandler {

	private static final String CORRECTION_WITHIN_ACCOUNT = "Correction within account";
	private static final String NET_MONEY_IN = "Net money in";
	private static final String NET_MONEY_OUT = "Net money out";

	/**
	 * Constructor.
	 * 
	 * @param typeDescription
	 * @param description
	 */
	public AdjustmentTransactionHandler() {
		super(DESC_SSN_LAST_FIRST_NAME);
	}

	/**
	 * Creates a transaction history item. The amount of the returned
	 * transaction history item is NOT always positive.
	 * 
	 * @param tx
	 *            The TransactionDataItem to read from.
	 */
	protected TransactionHistoryItem createTransactionHistoryItem(
			TransactionDataItem tx) {
		TransactionHistoryItem item = new TransactionHistoryItem();
		BigDecimal amount = tx.getTransactionAmount();
		if (amount != null) {
			item.setAmount(amount);
		}
		return item;
	}

	/**
	 * Gets the second description line for an adjustment transaction using the
	 * following rule:
	 * 
	 * <pre>
	 * 
	 *  If amount is &gt; 0
	 *    Net money in
	 *  If amount is &lt; 0
	 *    Net money out
	 *  If amount is = 0
	 *    Correction within account
	 *  
	 * </pre>
	 * 
	 * @param tx
	 *            The TransactionDataItem to read from.
	 */
	public String getDescriptionLine2(TransactionDataItem tx)
			throws SystemException {
		BigDecimal amount = tx.getTransactionAmount();
		if (amount != null) {
			switch (amount.signum()) {
				case -1 :
					return NET_MONEY_OUT;
				case 1 :
					return NET_MONEY_IN;
				default :
					return CORRECTION_WITHIN_ACCOUNT;
			}
		}
		return "";
	}
	public String getDescriptionLine2Unmasked(TransactionDataItem tx)
	throws SystemException {
		return getDescriptionLine2(tx);
	}
}