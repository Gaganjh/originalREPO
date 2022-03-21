package com.manulife.pension.ps.service.report.transaction.handler;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the mapping of a money source code to its description.
 * 
 * @author Charles Chan
 */
public class MoneySourceDescription {

	public static final String REGULAR_CODE 							= "REG";
	public static final String STEP_RATE_CODE 							= "SR";
	public static final String PRE_PURCHASE_EARNINGS_REGULAR_CODE 		= "PPERG";
	public static final String PRE_PURCHASE_REALLOCATION_RG_CODE 		= "PPRRG";
	public static final String QUALIFIED_DOMESTIC_RELATION_ORDER_CODE 	= "QDRO";
	public static final String INTERNAL_FORFEITURE_CODE 				= "IFORF";
	public static final String EARNINGS_DUE_TO_UNVESTED_MONEY_CODE 		= "UMERN";
	public static final String EXTERNAL_FORFEITURE_CODE 				= "XFORF";
	public static final String INTERNAL_REINSTATEMENT_CODE 				= "IREIN";
	public static final String INTERNAL_LOAN_REPAYMENT_CODE 			= "ILRPY";
	public static final String INTERNAL_TRANSFER_REGULAR_CODE 			= "ITFRR";
	public static final String INTERNAL_TRANSFER_EXTERNAL_CODE 			= "ITFRX";
	public static final String PRE_PURCHASE_EARNINGS_TRANSFER_CODE 		= "PPE";
	public static final String PRE_PURCHASE_REALLOCATION_XT_CODE 		= "PPR";
	public static final String EXTERNAL_LOAN_REPAYMENT_CODE 			= "XLRPY";
	public static final String EXTERNAL_TRANSFER_CODE 					= "XTFR";
	public static final String EXTERNAL_TRANSFER_2_CODE 				= "XTFR2";
	
	public static final String CONTRIBUTION								= "Contribution";
	public static final String REGULAR_CONTRIBUTION 					= "Regular contribution";
	public static final String FORFEITURE_CONTRIBUTION					= "Forfeiture contribution";
	public static final String REINSTATEMENT_CONTRIBUTION 				= "Reinstatement of contribution";
	public static final String TRANSFER_CONTRIBUTION			 		= "Transfer contribution";
	
	public static final String DEFAULT_DESC								= CONTRIBUTION;
	
	public static final String REGULAR_DESC 							= REGULAR_CONTRIBUTION;
	public static final String STEP_RATE_DESC 							= CONTRIBUTION;
	public static final String PRE_PURCHASE_EARNINGS_REGULAR_DESC 		= CONTRIBUTION;
	public static final String PRE_PURCHASE_REALLOCATION_RG_DESC 		= CONTRIBUTION;
	public static final String QUALIFIED_DOMESTIC_RELATION_ORDER_DESC 	= CONTRIBUTION;
	public static final String INTERNAL_FORFEITURE_DESC 				= FORFEITURE_CONTRIBUTION;
	public static final String EARNINGS_DUE_TO_UNVESTED_MONEY_DESC 		= FORFEITURE_CONTRIBUTION;
	public static final String EXTERNAL_FORFEITURE_DESC 				= FORFEITURE_CONTRIBUTION;
	public static final String INTERNAL_REINSTATEMENT_DESC 				= REINSTATEMENT_CONTRIBUTION;
	public static final String INTERNAL_LOAN_REPAYMENT_DESC 			= TRANSFER_CONTRIBUTION;
	public static final String INTERNAL_TRANSFER_REGULAR_DESC	 		= TRANSFER_CONTRIBUTION;
	public static final String INTERNAL_TRANSFER_EXTERNAL_DESC 			= TRANSFER_CONTRIBUTION;
	public static final String PRE_PURCHASE_EARNINGS_TRANSFER_DESC 		= TRANSFER_CONTRIBUTION;
	public static final String PRE_PURCHASE_REALLOCATION_XT_DESC 		= TRANSFER_CONTRIBUTION;
	public static final String EXTERNAL_LOAN_REPAYMENT_DESC 			= TRANSFER_CONTRIBUTION;
	public static final String EXTERNAL_TRANSFER_DESC 					= TRANSFER_CONTRIBUTION;
	public static final String EXTERNAL_TRANSFER_2_DESC 				= TRANSFER_CONTRIBUTION;
	
	public static final String EDIT_REGULAR_DESC 							= "Regular contribution";
	public static final String EDIT_STEP_RATE_DESC 							= "Contribution (SR)";
	public static final String EDIT_PRE_PURCHASE_EARNINGS_REGULAR_DESC 		= "Contribution (PPERG)";
	public static final String EDIT_PRE_PURCHASE_REALLOCATION_RG_DESC 		= "Contribution (PPRRG)";
	public static final String EDIT_QUALIFIED_DOMESTIC_RELATION_ORDER_DESC 	= "Contribution (QDRO)";
	public static final String EDIT_INTERNAL_FORFEITURE_DESC 				= "Forfeiture contribution (IFORF)";
	public static final String EDIT_EARNINGS_DUE_TO_UNVESTED_MONEY_DESC 	= "Forfeiture contribution (UMERN";
	public static final String EDIT_EXTERNAL_FORFEITURE_DESC 				= "Forfeiture contribution (XFORF)";
	public static final String EDIT_INTERNAL_REINSTATEMENT_DESC 			= "Reinstatement of contribution (IREIN)";
	public static final String EDIT_INTERNAL_LOAN_REPAYMENT_DESC 			= "Transfer contribution (ILRPY)";
	public static final String EDIT_INTERNAL_TRANSFER_REGULAR_DESC 			= "Transfer contribution (ITFRR)";
	public static final String EDIT_INTERNAL_TRANSFER_EXTERNAL_DESC 		= "Transfer contribution (ITFRX)";
	public static final String EDIT_PRE_PURCHASE_EARNINGS_TRANSFER_DESC 	= "Transfer contribution (PPE)";
	public static final String EDIT_PRE_PURCHASE_REALLOCATION_XT_DESC 		= "Transfer contribution (PPR)";
	public static final String EDIT_EXTERNAL_LOAN_REPAYMENT_DESC 			= "Transfer contribution (XLRPY)";
	public static final String EDIT_EXTERNAL_TRANSFER_DESC 					= "Transfer contribution";
	public static final String EDIT_EXTERNAL_TRANSFER_2_DESC 				= "Transfer contribution (XTFR2)";

	public static final String REGULAR_TRANSACTION_CODE 							= "00000505";
	public static final String STEP_RATE_TRANSACTION_CODE 							= "";
	public static final String PRE_PURCHASE_EARNINGS_REGULAR_TRANSACTION_CODE 		= "";
	public static final String PRE_PURCHASE_REALLOCATION_RG_TRANSACTION_CODE 		= "";
	public static final String QUALIFIED_DOMESTIC_RELATION_ORDER_TRANSACTION_CODE 	= "";
	public static final String INTERNAL_FORFEITURE_TRANSACTION_CODE 				= "00000530";
	public static final String EARNINGS_DUE_TO_UNVESTED_MONEY_TRANSACTION_CODE 		= "";
	public static final String EXTERNAL_FORFEITURE_TRANSACTION_CODE 				= "00000535";
	public static final String INTERNAL_REINSTATEMENT_TRANSACTION_CODE 				= "";
	public static final String INTERNAL_LOAN_REPAYMENT_TRANSACTION_CODE 			= "00000540";
	public static final String INTERNAL_TRANSFER_REGULAR_TRANSACTION_CODE 			= "";
	public static final String INTERNAL_TRANSFER_EXTERNAL_TRANSACTION_CODE 			= "";
	public static final String PRE_PURCHASE_EARNINGS_TRANSFER_TRANSACTION_CODE 		= "";
	public static final String PRE_PURCHASE_REALLOCATION_XT_TRANSACTION_CODE 		= "";
	public static final String EXTERNAL_LOAN_REPAYMENT_TRANSACTION_CODE 			= "00000545";
	public static final String EXTERNAL_TRANSFER_TRANSACTION_CODE 					= "00000510";
	public static final String EXTERNAL_TRANSFER_2_TRANSACTION_CODE 				= "00000515";

	private static Map moneySourceDescriptionMap;
	private static Map moneySourceEditDescriptionMap;

	private static Map moneySourceTransactionCodeMap;

	static {
		moneySourceDescriptionMap = new HashMap();
		moneySourceDescriptionMap.put(REGULAR_CODE 								, REGULAR_DESC 								);
		moneySourceDescriptionMap.put(STEP_RATE_CODE 							, STEP_RATE_DESC 							);
		moneySourceDescriptionMap.put(PRE_PURCHASE_EARNINGS_REGULAR_CODE 		, PRE_PURCHASE_EARNINGS_REGULAR_DESC 		);
		moneySourceDescriptionMap.put(PRE_PURCHASE_REALLOCATION_RG_CODE 		, PRE_PURCHASE_REALLOCATION_RG_DESC 		);
		moneySourceDescriptionMap.put(QUALIFIED_DOMESTIC_RELATION_ORDER_CODE	, QUALIFIED_DOMESTIC_RELATION_ORDER_DESC 	);
		moneySourceDescriptionMap.put(INTERNAL_FORFEITURE_CODE 					, INTERNAL_FORFEITURE_DESC 					);
		moneySourceDescriptionMap.put(EARNINGS_DUE_TO_UNVESTED_MONEY_CODE 		, EARNINGS_DUE_TO_UNVESTED_MONEY_DESC 		);
		moneySourceDescriptionMap.put(EXTERNAL_FORFEITURE_CODE 					, EXTERNAL_FORFEITURE_DESC 					);
		moneySourceDescriptionMap.put(INTERNAL_REINSTATEMENT_CODE 				, INTERNAL_REINSTATEMENT_DESC 				);
		moneySourceDescriptionMap.put(INTERNAL_LOAN_REPAYMENT_CODE 				, INTERNAL_LOAN_REPAYMENT_DESC 				);
		moneySourceDescriptionMap.put(INTERNAL_TRANSFER_REGULAR_CODE 			, INTERNAL_TRANSFER_REGULAR_DESC 			);
		moneySourceDescriptionMap.put(INTERNAL_TRANSFER_EXTERNAL_CODE 			, INTERNAL_TRANSFER_EXTERNAL_DESC 			);
		moneySourceDescriptionMap.put(PRE_PURCHASE_EARNINGS_TRANSFER_CODE 		, PRE_PURCHASE_EARNINGS_TRANSFER_DESC 		);
		moneySourceDescriptionMap.put(PRE_PURCHASE_REALLOCATION_XT_CODE 		, PRE_PURCHASE_REALLOCATION_XT_DESC 		);
		moneySourceDescriptionMap.put(EXTERNAL_LOAN_REPAYMENT_CODE 				, EXTERNAL_LOAN_REPAYMENT_DESC 				);
		moneySourceDescriptionMap.put(EXTERNAL_TRANSFER_CODE 					, EXTERNAL_TRANSFER_DESC 					);
		moneySourceDescriptionMap.put(EXTERNAL_TRANSFER_2_CODE 					, EXTERNAL_TRANSFER_2_DESC 					);
	}

	static {
		moneySourceEditDescriptionMap = new HashMap();
		moneySourceEditDescriptionMap.put(REGULAR_CODE 								, EDIT_REGULAR_DESC 								);
		moneySourceEditDescriptionMap.put(STEP_RATE_CODE 							, EDIT_STEP_RATE_DESC 							);
		moneySourceEditDescriptionMap.put(PRE_PURCHASE_EARNINGS_REGULAR_CODE 		, EDIT_PRE_PURCHASE_EARNINGS_REGULAR_DESC 		);
		moneySourceEditDescriptionMap.put(PRE_PURCHASE_REALLOCATION_RG_CODE 		, EDIT_PRE_PURCHASE_REALLOCATION_RG_DESC 		);
		moneySourceEditDescriptionMap.put(QUALIFIED_DOMESTIC_RELATION_ORDER_CODE	, EDIT_QUALIFIED_DOMESTIC_RELATION_ORDER_DESC 	);
		moneySourceEditDescriptionMap.put(INTERNAL_FORFEITURE_CODE 					, EDIT_INTERNAL_FORFEITURE_DESC 					);
		moneySourceEditDescriptionMap.put(EARNINGS_DUE_TO_UNVESTED_MONEY_CODE 		, EDIT_EARNINGS_DUE_TO_UNVESTED_MONEY_DESC 		);
		moneySourceEditDescriptionMap.put(EXTERNAL_FORFEITURE_CODE 					, EDIT_EXTERNAL_FORFEITURE_DESC 					);
		moneySourceEditDescriptionMap.put(INTERNAL_REINSTATEMENT_CODE 				, EDIT_INTERNAL_REINSTATEMENT_DESC 				);
		moneySourceEditDescriptionMap.put(INTERNAL_LOAN_REPAYMENT_CODE 				, EDIT_INTERNAL_LOAN_REPAYMENT_DESC 				);
		moneySourceEditDescriptionMap.put(INTERNAL_TRANSFER_REGULAR_CODE 			, EDIT_INTERNAL_TRANSFER_REGULAR_DESC 			);
		moneySourceEditDescriptionMap.put(INTERNAL_TRANSFER_EXTERNAL_CODE 			, EDIT_INTERNAL_TRANSFER_EXTERNAL_DESC 			);
		moneySourceEditDescriptionMap.put(PRE_PURCHASE_EARNINGS_TRANSFER_CODE 		, EDIT_PRE_PURCHASE_EARNINGS_TRANSFER_DESC 		);
		moneySourceEditDescriptionMap.put(PRE_PURCHASE_REALLOCATION_XT_CODE 		, EDIT_PRE_PURCHASE_REALLOCATION_XT_DESC 		);
		moneySourceEditDescriptionMap.put(EXTERNAL_LOAN_REPAYMENT_CODE 				, EDIT_EXTERNAL_LOAN_REPAYMENT_DESC 				);
		moneySourceEditDescriptionMap.put(EXTERNAL_TRANSFER_CODE 					, EDIT_EXTERNAL_TRANSFER_DESC 					);
		moneySourceEditDescriptionMap.put(EXTERNAL_TRANSFER_2_CODE 					, EDIT_EXTERNAL_TRANSFER_2_DESC 					);
	}

	static {
		moneySourceTransactionCodeMap = new HashMap();
		moneySourceTransactionCodeMap.put(REGULAR_CODE 								, REGULAR_TRANSACTION_CODE 								);
		moneySourceTransactionCodeMap.put(STEP_RATE_CODE 							, STEP_RATE_TRANSACTION_CODE 							);
		moneySourceTransactionCodeMap.put(PRE_PURCHASE_EARNINGS_REGULAR_CODE 		, PRE_PURCHASE_EARNINGS_REGULAR_TRANSACTION_CODE 		);
		moneySourceTransactionCodeMap.put(PRE_PURCHASE_REALLOCATION_RG_CODE 		, PRE_PURCHASE_REALLOCATION_RG_TRANSACTION_CODE 		);
		moneySourceTransactionCodeMap.put(QUALIFIED_DOMESTIC_RELATION_ORDER_CODE	, QUALIFIED_DOMESTIC_RELATION_ORDER_TRANSACTION_CODE 	);
		moneySourceTransactionCodeMap.put(INTERNAL_FORFEITURE_CODE 					, INTERNAL_FORFEITURE_TRANSACTION_CODE 					);
		moneySourceTransactionCodeMap.put(EARNINGS_DUE_TO_UNVESTED_MONEY_CODE 		, EARNINGS_DUE_TO_UNVESTED_MONEY_TRANSACTION_CODE 		);
		moneySourceTransactionCodeMap.put(EXTERNAL_FORFEITURE_CODE 					, EXTERNAL_FORFEITURE_TRANSACTION_CODE 					);
		moneySourceTransactionCodeMap.put(INTERNAL_REINSTATEMENT_CODE 				, INTERNAL_REINSTATEMENT_TRANSACTION_CODE 				);
		moneySourceTransactionCodeMap.put(INTERNAL_LOAN_REPAYMENT_CODE 				, INTERNAL_LOAN_REPAYMENT_TRANSACTION_CODE 				);
		moneySourceTransactionCodeMap.put(INTERNAL_TRANSFER_REGULAR_CODE 			, INTERNAL_TRANSFER_REGULAR_TRANSACTION_CODE 			);
		moneySourceTransactionCodeMap.put(INTERNAL_TRANSFER_EXTERNAL_CODE 			, INTERNAL_TRANSFER_EXTERNAL_TRANSACTION_CODE 			);
		moneySourceTransactionCodeMap.put(PRE_PURCHASE_EARNINGS_TRANSFER_CODE 		, PRE_PURCHASE_EARNINGS_TRANSFER_TRANSACTION_CODE 		);
		moneySourceTransactionCodeMap.put(PRE_PURCHASE_REALLOCATION_XT_CODE 		, PRE_PURCHASE_REALLOCATION_XT_TRANSACTION_CODE 		);
		moneySourceTransactionCodeMap.put(EXTERNAL_LOAN_REPAYMENT_CODE 				, EXTERNAL_LOAN_REPAYMENT_TRANSACTION_CODE 				);
		moneySourceTransactionCodeMap.put(EXTERNAL_TRANSFER_CODE 					, EXTERNAL_TRANSFER_TRANSACTION_CODE 					);
		moneySourceTransactionCodeMap.put(EXTERNAL_TRANSFER_2_CODE 					, EXTERNAL_TRANSFER_2_TRANSACTION_CODE 					);
	}

	private MoneySourceDescription() {
	}

	/**
	 * Returns the description of the given money source code.
	 * 
	 * @param moneySourceName The money source code.
	 * @return The description of the money source code.
	 */
	public static String getDescription(String moneySourceName) {
		String description = (String) moneySourceDescriptionMap
				.get(moneySourceName);
		if (description == null) {
			return REGULAR_DESC;
		}
		return description;
	}

	/**
	 * Returns the description of the given money source code for viewing on ICon2.
	 * 
	 * @param moneySourceName The money source code.
	 * @return The description of the money source code.
	 */
	public static String getViewDescription(String moneySourceName) {
		String description = (String) moneySourceDescriptionMap
				.get(moneySourceName);
		if (description == null) {
			return DEFAULT_DESC;
		}
		return description;
	}

	/**
	 * Returns the description of the given money source code for edit pages.
	 * 
	 * @param moneySourceName The money source code.
	 * @return The description of the money source code, if found; otherwise the deault description
	 */
	public static String getEditDescription(String moneySourceName) {
		String description = (String) moneySourceEditDescriptionMap.get(moneySourceName);
		if (description == null) {
			return DEFAULT_DESC;
		}
		return description;
	}

	/**
	 * Returns the transaction code of the given money source code.
	 * e.g. "REG" returns "505". Note some values will return an empty string.
	 * 
	 * @param moneySourceName The money source code.
	 * @return The transaction code of the money source code.
	 */
	public static String getTransactionCode(String moneySourceName) {
		String transactionCode = (String) moneySourceTransactionCodeMap
				.get(moneySourceName);
		if (transactionCode == null) {
			return "";
		}
		return transactionCode;
	}
}
