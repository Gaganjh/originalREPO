package com.manulife.pension.ps.service.report.transaction.handler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Chan
 */
public class TransactionStatusDescription {

	private static final String IN_PROGRESS = "Transaction in progress";

	private static Map statusDescriptionMap;

	static {
		statusDescriptionMap = new HashMap();
		statusDescriptionMap.put("CM", "");
		statusDescriptionMap.put("CU", "");
		statusDescriptionMap.put("FL", IN_PROGRESS);
		statusDescriptionMap.put("LG", IN_PROGRESS);
		statusDescriptionMap.put("LR", IN_PROGRESS);
		statusDescriptionMap.put("PU", IN_PROGRESS);
		statusDescriptionMap.put("SI", IN_PROGRESS);
		statusDescriptionMap.put("SM", IN_PROGRESS);
		statusDescriptionMap.put("WB", IN_PROGRESS);
		statusDescriptionMap.put("WP", IN_PROGRESS);
		statusDescriptionMap.put("WR", IN_PROGRESS);
	}

	private TransactionStatusDescription() {
	}

	/**
	 * Method to get the texts for the status
	 * 
	 * @param status
	 *            is the status as stored in Apollo
	 * @return String the text for the status description
	 * @exception Exception
	 */
	public static String getDescription(String status) {
		String description = (String) statusDescriptionMap.get(status);
		if (description == null) {
			return "";
		}
		return description;
	}
}
