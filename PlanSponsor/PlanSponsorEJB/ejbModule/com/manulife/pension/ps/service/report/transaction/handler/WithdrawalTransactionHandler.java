package com.manulife.pension.ps.service.report.transaction.handler;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDataItem;

/**
 * @author Maria Lee
 * @author Charles Chan
 */
public class WithdrawalTransactionHandler extends CreditTransactionHandler {

	public WithdrawalTransactionHandler(String description) {
		super(description);
	}

	/**
	 * Returns the 2nd line of descriptions format: "xxx-xx-nnnn lastname,
	 * firstname"
	 */
	public String getDescriptionLine2(TransactionDataItem tx) throws SystemException {
		// Need the description only when there is value for the SSN
		if (tx.getParticipant() != null && StringUtils.isNotBlank(tx.getParticipant().getSsn())) {
			return getSsnDescription(tx);
		}
		return null;
	}
	/**
	 * SSE024 do not mask SSN for download full ssn permission
	 * Returns the 2nd line of descriptions forma: ssn, lastname,
	 * firstname"
	 */
	public String getDescriptionLine2Unmasked(TransactionDataItem tx) throws SystemException {
		// Need the description only when there is value for the SSN
		if (tx.getParticipant() != null && StringUtils.isNotBlank(tx.getParticipant().getSsn())) {
			return getSsnDescriptionUnmasked(tx);
		}
		return null;
	}
}