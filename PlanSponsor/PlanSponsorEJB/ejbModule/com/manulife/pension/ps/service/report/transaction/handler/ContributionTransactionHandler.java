package com.manulife.pension.ps.service.report.transaction.handler;

import java.util.Date;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDataItem;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * Cash Account transaction handler for Contributions.
 * 
 * @author Maria Lee
 * @author Charles Chan
 */
public class ContributionTransactionHandler extends DebitTransactionHandler {

	private static final String PAYROLL_ENDING_DATE = "Payroll ending ";
	private static final String CONTRIBUTION_DATE = "Contribution date  ";

	public ContributionTransactionHandler() {
		super(null);
	}

	/**
	 * Overriden method to return the line 1 description
	 * 
	 * @param tx
	 *            is the transaction returned from Apollo
	 * @return String is the line 1 description line 1 description is defaulted
	 *         to "Contribution" if it can't find a match in the
	 *         moneySourceDescriptionMap of the CashAccountHelper object.
	 * @exception Exception
	 */
	public String getDescriptionLine1(TransactionDataItem tx) {
		
		return prefixDescriptionLine1WithPending(tx, 
			MoneySourceDescription.getDescription(tx.getMoneySource()));
	}
	public String getDescriptionLine1Unmaksed(TransactionDataItem tx) {
		
		return getDescriptionLine1(tx);
	}
	/**
	 * Overriden method to return the line 2 description
	 * 
	 * @param tx
	 *            is the transaction returned from Apollo
	 * @return String is the line 2 description format: Payroll ending
	 *         mm/dd/yyyy
	 * @exception SystemException
	 */
	public String getDescriptionLine2(TransactionDataItem tx)
			throws SystemException {

		if (tx.getPayrollEndingDate() == null
				|| isDummyDate(tx.getPayrollEndingDate())) {
			return "";
		}

		if (tx.isDBContract()) {
			return CONTRIBUTION_DATE
			+ DateRender.format(tx.getPayrollEndingDate(),
					RenderConstants.MEDIUM_MDY_DASHED);						
		} else {
		return PAYROLL_ENDING_DATE
				+ DateRender.format(tx.getPayrollEndingDate(),
						RenderConstants.MEDIUM_MDY_DASHED);
		}

	}
	public String getDescriptionLine2Unmasked(TransactionDataItem tx)
	throws SystemException {
		return getDescriptionLine2(tx);
		
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