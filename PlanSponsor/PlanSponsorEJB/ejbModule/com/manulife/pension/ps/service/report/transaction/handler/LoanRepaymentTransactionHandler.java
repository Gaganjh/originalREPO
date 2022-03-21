package com.manulife.pension.ps.service.report.transaction.handler;

import java.util.Date;

import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDataItem;

/**
 * Cash Account transaction handler for Loan Repayments.
 * 
 * @author Maria Lee
 *  
 */
public class LoanRepaymentTransactionHandler extends DebitTransactionHandler {

	public LoanRepaymentTransactionHandler(String description) {
		super(description);
	}

	/**
	 * Return the transaction effective date
	 */
	public Date getTransactionDate(TransactionDataItem tx) {

		if ((tx.getTransactionEffectiveDate() == null)
				&& (tx.getRateEffectiveDate() != null)) {
			return tx.getRateEffectiveDate();
		}
		return tx.getTransactionEffectiveDate();
	}
}