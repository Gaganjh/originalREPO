package com.manulife.pension.ps.service.report.transaction.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


public class TransactionHistoryHelper {

	public static final String REDEMPTION_FEE = "RF";
	public static final String REDEMPTION_FEE_COMMENT = "Redemption Fee";
	
	public static final String MVA = "MV";
	public static final String MVA_COMMENT = "Market Value Adjustment";
	private static final String IPSM_PAPER_MEDIUM_TEXT = "IPSM - Paper";
	private static final String IPSM_ONLINE_MEDIUM_TEXT = "IPSM – Online";
	
	private static Map mediumDescriptionMap;

	static {
		mediumDescriptionMap = new HashMap();
		mediumDescriptionMap.put("PA", "Paper form");
		mediumDescriptionMap.put("IV", "Toll-free service line");
		mediumDescriptionMap.put("IN", "Participant Web site");
		mediumDescriptionMap.put("VR", "Call to Client Representative");
		mediumDescriptionMap.put("AB", "Participant Web site"); // via mPower
		mediumDescriptionMap.put("AR", "Automated rebalance");
		mediumDescriptionMap.put("AUTOCC", "Automatic Class Conversion");
	}

	private TransactionHistoryHelper() {
	}

	/**
	 * Method to get the texts for the medium code
	 * 
	 * @param status
	 *            is the status as stored in Apollo
	 * @return String the text for the status description
	 * @exception Exception
	 */
	public static String getMediumDescription(String mediumCode,String procUid, String transType, String reasonCode) {
		
		String description = (String) mediumDescriptionMap.get(mediumCode);
		
		if (description == null) {
			return "";
		}
		
		if (procUid != null) {
			if ("AUTOCC".equals(procUid) && "PA".equals(mediumCode)) 
				description = (String) mediumDescriptionMap.get(procUid);
		} else if (StringUtils.equals("IT", transType)
				&& StringUtils.equals("IS", reasonCode)) {
			if (StringUtils.equals("IN", mediumCode)) {
				description = IPSM_ONLINE_MEDIUM_TEXT;
			} else if (StringUtils.equals("PA", mediumCode)) {
				description = IPSM_PAPER_MEDIUM_TEXT;
			}
		}
		
		return description;
	}
	
	public static String getComments(String transactionSubType) {
		
		if (isMva(transactionSubType))
			return MVA_COMMENT;
		else if (isRedemptionFees(transactionSubType))
			return REDEMPTION_FEE_COMMENT;
		else
			return "";
	}
	
	public static boolean isMva(String transactionSubType) {
		
		 return transactionSubType.equals(MVA) ? true : false;
	}
	
	public static boolean isRedemptionFees(String transactionSubType) {
		
		 return transactionSubType.equals(REDEMPTION_FEE) ? true : false;
	}
}