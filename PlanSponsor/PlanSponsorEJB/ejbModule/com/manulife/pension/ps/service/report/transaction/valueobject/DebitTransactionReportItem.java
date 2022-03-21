package com.manulife.pension.ps.service.report.transaction.valueobject;

import java.math.BigDecimal;

/**
 * @author Charles Chan
 */
public class DebitTransactionReportItem extends CashAccountItem {

	private static final BigDecimal MINUS_ONE = new BigDecimal("-1");

	/**
	 * Constructor.
	 * 
	 */
	public DebitTransactionReportItem() {
		super();
	}

	public DebitTransactionReportItem(boolean reverse) {
		super();
		this.reverse = reverse;
	}

	/**
	 * @return Returns the creditAmount.
	 */
	public BigDecimal getCreditAmount() {
		if (reverse) {
			return getAmount().multiply(MINUS_ONE);
		} else {
			return null;
		}
	}

	/**
	 * @return Returns the debitAmount.
	 */
	public BigDecimal getDebitAmount() {
		if (reverse) {
			return null;
		} else {
			return getAmount();
		} 
	}
	
	private boolean reverse;
}
