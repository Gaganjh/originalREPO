package com.manulife.pension.ps.service.report.transaction.valueobject;

import java.math.BigDecimal;

/**
 * @author Charles Chan
 */
public class CreditTransactionReportItem extends CashAccountItem {

	/**
	 * Constructor.
	 * 
	 */
	public CreditTransactionReportItem() {
		super();
	}

	/**
	 * @return Returns the creditAmount.
	 */
	public BigDecimal getCreditAmount() {
		return getAmount();
	}

	/**
	 * @return Returns the debitAmount.
	 */
	public BigDecimal getDebitAmount() {
		return null;
	}
}
