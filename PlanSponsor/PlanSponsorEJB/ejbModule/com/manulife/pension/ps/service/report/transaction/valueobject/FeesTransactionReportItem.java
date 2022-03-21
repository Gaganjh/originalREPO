/*
 * Created on Jul 5, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.service.report.transaction.valueobject;

import java.math.BigDecimal;

/**
 * @author sternlu
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FeesTransactionReportItem extends CashAccountItem {

	/**
	 * Constructor.
	 * 
	 */
	public FeesTransactionReportItem() {
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
