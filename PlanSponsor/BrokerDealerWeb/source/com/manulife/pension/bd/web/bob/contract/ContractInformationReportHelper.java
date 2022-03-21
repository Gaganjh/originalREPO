package com.manulife.pension.bd.web.bob.contract;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;

/**
 * This is a helper class for ContractInformationReportAction.
 * 
 * @author Ilamparithi
 *
 */
public class ContractInformationReportHelper {
	
	private static final String GIFL_V1_V2_INTERNAL_USER = "giflV1V2InternalUser";
	private static final String GIFL_V1_V2_EXTERNAL_USER = "giflV1V2ExternalUser";
	private static final String GIFL_V3_INTERNAL_USER = "giflV3InternalUser";
	private static final String GIFL_V3_EXTERNAL_USER = "giflV3ExternalUser";
	private static HashMap<String, Integer> GIFL_FEATURES_MAP = new HashMap<String, Integer>();
	private static final String LI_START_TAG = "<li>";
	private static final String LI_END_TAG = "</li>";

	private static String GIFL_FEE_FORMAT = "0.00#";

	static {
		GIFL_FEATURES_MAP.put(GIFL_V1_V2_INTERNAL_USER, BDContentConstants.GIFL_V1_V2_INTERNAL_USER_FEATURES);
		GIFL_FEATURES_MAP.put(GIFL_V1_V2_EXTERNAL_USER, BDContentConstants.GIFL_V1_V2_EXTERNAL_USER_FEATURES);
		GIFL_FEATURES_MAP.put(GIFL_V3_INTERNAL_USER, BDContentConstants.GIFL_V3_INTERNAL_USER_FEATURES);
		GIFL_FEATURES_MAP.put(GIFL_V3_EXTERNAL_USER, BDContentConstants.GIFL_V3_EXTERNAL_USER_FEATURES);
	}
	
	/**
	 * Returns a content id specific to the user type
	 * 
	 * @param bobContext
	 * @return int
	 */
	public static int getGiflFeaturesContentId(BobContext bobContext) {
		int contentId = 0;
		String giflVersion = bobContext.getContractProfile().getContract().getGiflVersion();
		boolean isInternalUser = bobContext.getUserProfile().isInternalUser();
		if(BDConstants.GIFL_VERSION_03.equals(giflVersion)) {
			if(isInternalUser) {
				contentId = GIFL_FEATURES_MAP.get(GIFL_V3_INTERNAL_USER);
			}
			else {
				contentId = GIFL_FEATURES_MAP.get(GIFL_V3_EXTERNAL_USER);
			}
		}
		else if(BDConstants.GIFL_VERSION_01.equals(giflVersion) || BDConstants.GIFL_VERSION_02.equals(giflVersion)) {
			if(isInternalUser) {
				contentId = GIFL_FEATURES_MAP.get(GIFL_V1_V2_INTERNAL_USER);
			}
			else {
				contentId = GIFL_FEATURES_MAP.get(GIFL_V1_V2_EXTERNAL_USER);
			}
		}
		return contentId;
	}
	
	/**
	 * The gifl features are retrieved from CMA. They will include formatting information
	 * like <ul>, <li> etc. We only want a list of features to display in CSV and PDF.
	 * This method parses the CMA text and returns a list of features.
	 * 
	 * @param cmaText
	 * @return List<String>
	 */
	public static List<String> parseGiflFeaturesFromCMAText(String cmaText) {
		List<String> featuresList = new ArrayList<String>();
		String[] featuresArr = StringUtils.substringsBetween(cmaText,
				LI_START_TAG, LI_END_TAG);
		if (featuresArr != null) {
			for (int i = 0; i < featuresArr.length; i++) {
				featuresList.add(StringUtils.trim(featuresArr[i]));
			}
		}
		return featuresList;
	}
	
	public static String getGIFLFeePercentageDisplay(int contractNum) throws SystemException {
		BigDecimal value = ContractServiceDelegate.getInstance().getContractAnnualGIFLFeePercentage(contractNum);
		if (value == null) {
			return "";
		} else {
			return new DecimalFormat(GIFL_FEE_FORMAT).format(value);
		}
	}
}
