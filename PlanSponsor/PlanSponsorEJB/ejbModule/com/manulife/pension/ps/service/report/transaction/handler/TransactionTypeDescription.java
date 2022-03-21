package com.manulife.pension.ps.service.report.transaction.handler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Chan
 */
public class TransactionTypeDescription {

	private static final String WITHDRAWAL = "Withdrawal";
	
	private static final String NEGATIVE_ELECTION = "90 day withdrawal election";

	private static final String BILLED_CHARGES = "Billed charges";

	private static final String PAYMENT = "Payment";

	private static final String MATURITY_REINVESTMENT = "Maturity reinvestment";

	private static final String LOAN_REPAYMENT = "Loan repayment";

	private static final String LOAN_ISSUE = "Loan issue";
		
	private static final String LOAN_TRANSFER = "Loan transfer";

	private static final String LOAN_CLOSURE = "Loan closure";

	private static final String INTER_ACCOUNT_TRANSFER = "Inter-account transfer";

	private static final String CONTRIBUTION = "Contribution";

	private static final String ADJUSTMENT = "Adjustment";

	private static final String DISCONTINUANCE = "Discontinuance";

	private static final String CHECK_REQUEST = "Check request";
	private static final String FEES = "Fee Transaction";

	private static Map typeDescriptionMap;

	static {
		typeDescriptionMap = new HashMap();
		typeDescriptionMap.put(TransactionType.ADJUSTMENT, ADJUSTMENT);
		typeDescriptionMap.put(TransactionType.ALLOCATION, CONTRIBUTION);
		typeDescriptionMap.put(
				TransactionType.AUTOMATIC_DEDUCTION_OF_UNPAID_CHARGES,
				DISCONTINUANCE);
		typeDescriptionMap.put(TransactionType.BILLED_CHARGE_BA_BM,
				DISCONTINUANCE);
		typeDescriptionMap.put(TransactionType.BILLED_CHARGE_HA_HM_WT,
				DISCONTINUANCE);
		typeDescriptionMap.put(TransactionType.CASH_ADJUSTMENT, ADJUSTMENT);

		// CHECK_REQUEST description requires reason code.
		//typeDescriptionMap.put(TransactionType.CHECK_REQUEST, DISCONTINUANCE);

		typeDescriptionMap.put(TransactionType.DISCONTINUANCE, DISCONTINUANCE);
		typeDescriptionMap.put(TransactionType.DISCONTINUANCE_AMOUNT_BA_BM,
				DISCONTINUANCE);
		typeDescriptionMap.put(TransactionType.DISCONTINUANCE_AMOUNT_HA_HM_WT,
				DISCONTINUANCE);
		typeDescriptionMap.put(TransactionType.INTER_ACCOUNT_TRANSFER,
				INTER_ACCOUNT_TRANSFER);
		typeDescriptionMap.put(TransactionType.LOAN_DEFAULT, LOAN_CLOSURE);
		typeDescriptionMap.put(TransactionType.LOAN_ISSUE, LOAN_ISSUE);
		typeDescriptionMap.put(TransactionType.LOAN_TRANSFER, LOAN_TRANSFER);
		typeDescriptionMap.put(TransactionType.LOAN_REPAYMENT, LOAN_REPAYMENT);
		typeDescriptionMap.put(
				TransactionType.MATURITY_AMOUNT_TRANSFERRED_TO_CASH,
				DISCONTINUANCE);
		typeDescriptionMap.put(TransactionType.MATURITY_REINVESTMENT,
				MATURITY_REINVESTMENT);
		typeDescriptionMap.put(TransactionType.PAYMENT_DEPOSITS, PAYMENT);
		typeDescriptionMap.put(TransactionType.PAYMENT_OF_BILLED_CHARGES,
				BILLED_CHARGES);
		typeDescriptionMap.put(TransactionType.PAYMENT_OF_TEMPORARY_CREDIT,
				DISCONTINUANCE);
		typeDescriptionMap.put(
				TransactionType.TEMPORARY_CREDIT_PAYMENT_HA_HM_WT,
				DISCONTINUANCE);
		typeDescriptionMap.put(TransactionType.WITHDRAWAL, WITHDRAWAL);
		typeDescriptionMap.put(TransactionType.NEGATIVE_ELECTION, NEGATIVE_ELECTION);
		typeDescriptionMap.put(TransactionType.FEES_TRANSACTION, FEES);

	}

	private TransactionTypeDescription() {
	}

	/**
	 * Method to get the texts for the type
	 * 
	 * @param type
	 *            is the type as stored in Apollo
	 * @return String the text for the type description
	 * @exception Exception
	 */
	public static String getDescription(String type) {
		if (type.equals(TransactionType.CHECK_REQUEST)) {
			throw new UnsupportedOperationException(
					"CR description is retrieved with a reason code.");
		}

		String description = (String) typeDescriptionMap.get(type);
		if (description == null) {
			return "";
		}
		return description;
	}

	public static String getDescription(String type, String reasonCode) {
		if (TransactionType.CHECK_REQUEST.equals(type)) {
			if ("OP".equals(reasonCode)) {
				return CHECK_REQUEST;
			}
			return DISCONTINUANCE;
		}
		return getDescription(type);
	}
}