/**
 * 
 */
package com.manulife.pension.platform.web.util;

import java.math.BigDecimal;
import java.sql.Date;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.util.NumberUtils;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * @author akarave
 *
 * This class has utility methods For Lifetime Income Amount Details
 * 	1. to convert Date Object to String (YYYY-MM-DD)
 *  2. to validate the LIA availability
 */
public class LIADisplayHelper {

	private static final String OPTION_SPOUSE = "Spouse";
	private static final String OPTION_INDIVIDUAL = "Individual";
	private static final String NON_APPLICABLE_LIA_DATE = "9999-12-31";
	private static final String BLANK = "";
	private static final String PERCENTAGE = "%";
	private static final String FREQUENCY_CODE_SEMI_ANNUAL = "Semi Annual";
	private static final String FREQUENCY_CODE_ANNUAL = "Annual";
	private static final String FREQUENCY_CODE_MONTHLY = "Monthly";
	private static final String FREQUENCY_CODE_QUARTERLY = "Quarterly";

	/**
	 * returns the date in String YYYY-MM-DD format
	 * 
	 * @return String
	 */
	public static String getFormatedDate(Date date) {
		String displayAnniversaryDate = BLANK;
		if (date != null) {
			displayAnniversaryDate = DateRender.formatByPattern(date,
					StringUtils.EMPTY, RenderConstants.MEDIUM_YMD_DASHED);
		}
		return displayAnniversaryDate;
	}

	/**
	 * returns percentage in ###.##% format
	 * 
	 * @return
	 */
	public static String getFormatedPercentage(BigDecimal share) {
		return (share == null ? BLANK : NumberUtils.formatIERate(share
				.toString())
				+ PERCENTAGE);
	}

	/**
	 * returns the display value for frequency code.
	 * 
	 * @return String
	 */
	public static String getDisplayFrequencyCode(String frequencyCode) {
		if (StringUtils.isNotBlank(frequencyCode)) {
			if (StringUtils.equals("S", frequencyCode)) {
				return FREQUENCY_CODE_SEMI_ANNUAL;
			} else if (StringUtils.equals("A", frequencyCode)) {
				return FREQUENCY_CODE_ANNUAL;
			} else if (StringUtils.equals("M", frequencyCode)) {
				return FREQUENCY_CODE_MONTHLY;
			} else if (StringUtils.equals("Q", frequencyCode)) {
				return FREQUENCY_CODE_QUARTERLY;
			}
		}
		return BLANK;
	}

	/**
	 * returns the Individual Or Spousal Option
	 * 
	 * @return String
	 */
	public static String getDisplayIndividualOrSpousalOption(String spousalOptionInd) {
		if (StringUtils.isNotBlank(spousalOptionInd)) {
			if (StringUtils.equals("Y", spousalOptionInd)) {
				return OPTION_SPOUSE;
			} else {
				return OPTION_INDIVIDUAL;
			}
		}
		return BLANK;
	}

	/**
	 * checks whether LIA details should be display or not
	 * 
	 * @return boolean
	 */
	public static boolean isShowLIADetailsSection(Date date) {
		String formatedDate = getFormatedDate(date);
		if (StringUtils.isNotBlank(formatedDate)
				&& !StringUtils.equals(NON_APPLICABLE_LIA_DATE, formatedDate)) {
			return true;
		}
		return false;
	}
}