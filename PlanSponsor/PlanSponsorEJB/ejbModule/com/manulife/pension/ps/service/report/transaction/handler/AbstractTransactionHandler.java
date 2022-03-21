package com.manulife.pension.ps.service.report.transaction.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDataItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryItem;
import com.manulife.util.render.SSNRender;

/**
 * Abstract base class for transaction handlers
 * 
 * @author Maria Lee
 * @author Charles Chan
 */
public abstract class AbstractTransactionHandler implements TransactionHandler {

	private static final String FORWARD_MODE = "F";
	private static final String REVERSE_MODE = "R";
	private static final String REVERSAL = " - Reversal";
	private static final String COMPLETED_STATUS[] = {"CM", "CU"};
	
	/*
	 * Dummy date representing 9999-12-31 (GregorianCalendar's MONTH is 0 based)
	 */
	private static final long DUMMY_DATE = new GregorianCalendar(9999, 11, 31)
			.getTime().getTime();

	protected String txDescription1;

	protected abstract TransactionHistoryItem createTransactionHistoryItem(
			TransactionDataItem tx);

	public AbstractTransactionHandler(String description) {
		this.txDescription1 = description;
	}

	/**
	 * Method to process the transaction retrieved from Apollo
	 * 
	 * @param tx
	 *            is the transaction retrieved from Apollo
	 * @return CashAccountItem contains the same transaction augmented with
	 *         description lines
	 * @exception SystemException
	 */
	public TransactionHistoryItem transform(TransactionDataItem tx)
			throws SystemException {

		TransactionHistoryItem item = createTransactionHistoryItem(tx);

		item.setParticipant(tx.getParticipant());

		item.setComplete(isComplete(tx.getTransactionStatusCode()));

		item.setTransactionNumber(tx.getTransactionNumber());
		
		if(TransactionType.WITHDRAWAL.equals(tx.getTransactionType())){
			item.setSubType(tx.getTransactionReasonCode());
		}else{
			item.setSubType(tx.getTransferInProtocolCode());
		}

		// return a null if the date is "9999-12-31",
		Date transactionDate = getTransactionDate(tx);

		if (isDummyDate(transactionDate)) {
			item.setTransactionDate(null);
		} else {
			item.setTransactionDate(transactionDate);
		}
		item.setType(tx.getTransactionType());

		item.setTypeDescription1(TransactionTypeDescription.getDescription(tx
				.getTransactionType(), tx.getTransactionReasonCode()));
		item.setTypeDescription2(isReverseMode(tx.getTransactionMode())
				? REVERSAL
				: "");

		List descriptions = new ArrayList();
		List descriptionsUnmasked = new ArrayList();
		String description = getDescriptionLine1(tx);
		if (description != null && description.length() > 0) {
			descriptions.add(description);
		}
		description = getDescriptionLine1Unmasked(tx);
		if (description != null && description.length() > 0) {
			descriptionsUnmasked.add(description);
		}
		description = getDescriptionLine2(tx);
		if (description != null && description.length() > 0) {
			descriptions.add(description);
		}
		description = getDescriptionLine2Unmasked(tx);
		if (description != null && description.length() > 0) {
			descriptionsUnmasked.add(description);
		}
		description = getDescriptionLine3(tx);
		if (description != null && description.length() > 0) {
			descriptions.add(description);
			descriptionsUnmasked.add(description);
		}
		item.setDescriptions(descriptions);
		item.setDescriptionsUnmasked(descriptionsUnmasked);

		return item;
	}

	/**
	 * Determines if transaction is incomplete
	 */
	private boolean isComplete(String status) {

		for (int i = 0; i < COMPLETED_STATUS.length; i++) {
			if (COMPLETED_STATUS[i].equals(status.trim()))
				return true;
		}
		return false;
	}

	/**
	 * Return the transaction effective date
	 */
	public Date getTransactionDate(TransactionDataItem tx)
			throws SystemException {

		return tx.getTransactionEffectiveDate();
	}

	/**
	 * Return the 1st description line under the Description column
	 * 
	 * @see TransactionStatusDescription
	 */
	public String getDescriptionLine1(TransactionDataItem tx)
			throws SystemException {

		return txDescription1;
	}
	/**
	 * Return the 1st description line under the Description column
	 * 
	 * @see TransactionStatusDescription
	 */
	public String getDescriptionLine1Unmasked(TransactionDataItem tx)
			throws SystemException {

		return txDescription1;
	}

	/**
	 * Return the 2nd description line under the Description column Only used
	 * for the Contribution and Withdrawal transactions
	 * 
	 * @see ContributionTransactionHandler WithdrawalTransactionHandler
	 */
	public String getDescriptionLine2(TransactionDataItem tx)
			throws SystemException {

		return "";
	}

	/**
	 * Return the 2nd description line under the Description column Only used
	 * for the Contribution and Withdrawal transactions
	 * 
	 * @see ContributionTransactionHandler WithdrawalTransactionHandler
	 */
	public String getDescriptionLine2Unmasked(TransactionDataItem tx)
			throws SystemException {

		return "";
	}

	/**
	 * Return the 3rd description line under the Description column Used for
	 * outstanding transactions to show the status description
	 * 
	 * @see CashAccountHandler
	 */
	public String getDescriptionLine3(TransactionDataItem tx) {

		return TransactionStatusDescription.getDescription(tx
				.getTransactionStatusCode());
	}

	/**
	 * Determine if mode is a forward mode
	 */
	public static boolean isForwardMode(String mode) {
		return FORWARD_MODE.equalsIgnoreCase(mode) ? true : false;
	}

	/**
	 * Determine if mode is a reversal
	 */
	public static boolean isReverseMode(String mode) {
		return REVERSE_MODE.equalsIgnoreCase(mode) ? true : false;
	}

	public String getSsnDescription(TransactionDataItem tx) {
		if (tx.isDBContract()==false) {
		String result = new StringBuffer(SSNRender.format(tx.getParticipant()
				.getSsn(), "xxx-xx-")).append(", ").append(
				tx.getParticipant().getLastName()).append(", ").append(
				tx.getParticipant().getFirstName()).toString();
		return result;
		} else {
			return null;
		}
	}
	public String getSsnDescriptionUnmasked(TransactionDataItem tx) {
		if (tx.isDBContract()==false) {
		String result = new StringBuffer(SSNRender.format(tx.getParticipant()
				.getSsn(), "", false)).append(", ").append(
				tx.getParticipant().getLastName()).append(", ").append(
				tx.getParticipant().getFirstName()).toString();
		return result;
		} else {
			return null;
		}
	}

	/**
	 * Determine if date is a dummy date
	 */
	public static boolean isDummyDate(Date date) throws SystemException {
		return date.getTime() >= DUMMY_DATE;
	}

}