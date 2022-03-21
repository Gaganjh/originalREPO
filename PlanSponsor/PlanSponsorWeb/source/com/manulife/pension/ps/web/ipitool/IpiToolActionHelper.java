package com.manulife.pension.ps.web.ipitool;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;

/**
 * 
 * @author Baburaj Ramasamy
 * 
 */
public class IpiToolActionHelper {
    
	public static StringBuffer bacValidator(String[] bacValues) {

		String previousVal = "0.00";
		double nextBandStarts = 0.00;
		boolean isValidBandEnd = false;
		double maxAllowed = 999999999.99;
		StringBuffer error = new StringBuffer();
		boolean maxVal = false;
		boolean invalidVal = false;
		boolean invalidRate = false;
		boolean isExtraEntry = false;
		boolean isValidRate = false;
		boolean isRateChanged = false;
		boolean isBlank = false;
		boolean isBandChanged = false;
		boolean isZero = false;
		boolean isBlankRate = false;

		String[] bacVal = new String[100];
		String previousRate = "0.00";

		for (String value : bacValues) {

			bacVal = value.split(",");

			if (!isBandChanged) {
				isBandChanged = StringUtils.isNotBlank(bacVal[0]) ? true : false;
			}
			if (!isRateChanged) {
				isRateChanged = StringUtils.isNotBlank(bacVal[1]) ? true : false;
			}
			if (bacVal.length > 0) {
				bacVal[0] = bacVal[0] == null ? String
						.valueOf(Constants.DECIMAL_PATTERN) : bacVal[0];
				if (bacVal.length == 2) {
					bacVal[1] = bacVal[1] == null ? String
							.valueOf(Constants.DECIMAL_PATTERN) : bacVal[1];
				} else {
					bacVal[1] = String.valueOf(Constants.DECIMAL_PATTERN);
				}
				if (!isValidBandEnd && StringUtils.isNotBlank(bacVal[0])
						&& isValidDecimalBand(bacVal[0])) {
					isValidBandEnd = nextBandStarts >= Double
							.parseDouble(bacVal[0]);
					nextBandStarts = Double.parseDouble(bacVal[0]) + 0.01;

				}
				if (!maxVal) {
					maxVal = StringUtils.isNotBlank(bacVal[0])
							&& isValidDecimalBand(bacVal[0])
							&& Double.parseDouble(bacVal[0]) == maxAllowed ? true
							: false;
				}
				if (!invalidVal) {
					invalidVal = StringUtils.isNotBlank(bacVal[0])
							&& (!isValidDecimalBand(bacVal[0])
							|| (Double.parseDouble(bacVal[0]) > maxAllowed || Double
									.parseDouble(bacVal[0]) < 0)) ? true
							: false;
				}
				if (!isBlank && !maxVal) {
					isBlank = StringUtils.isBlank(previousVal)&& StringUtils.isNotBlank(bacVal[0]) ? true : false;
					isBlank = isBlank || StringUtils.isBlank(bacVal[0]);
					previousVal = bacVal[0];
				}
				if (!isZero) {
					isZero = StringUtils.isNotBlank(bacVal[0])
							&& isValidDecimalBand(bacVal[0])
							&& Double.parseDouble(bacVal[0]) == 0 ? true
							: false;
				}
				if (!invalidRate) {
					boolean isNumeric = !bacVal[1].contains(".") ? bacVal[1]
							.matches("\\d") : isValidDecimal(bacVal[1]);
					invalidRate = StringUtils.isNotBlank(bacVal[1])
							&& (!isNumeric || Double.parseDouble(bacVal[1]) < 0 || Double
									.parseDouble(bacVal[1]) > 9.999) ? true
							: false;
				}
				if (!isExtraEntry && maxVal && isBandChanged) {
					isExtraEntry = StringUtils.isBlank(bacVal[0])
							&& StringUtils.isNotBlank(bacVal[1]) ? true : false;
				}
				if (!isValidRate && isRateChanged) {
					isValidRate = StringUtils.isNotBlank(bacVal[0])
							&& StringUtils.isBlank(bacVal[1]) ? true : false;
				}
				if (!isBlankRate) {
					isBlankRate = StringUtils.isBlank(previousRate)
							&& StringUtils.isNotBlank(bacVal[1]) ? true : false;
					previousRate = bacVal[1];
				}
				
			}

		}

		String collon = " : ";
		String doubleQuotes = "\"";
		String COMMA = ",";

		if (invalidVal) {
			error.append(doubleQuotes + "INVALID_NEW_BAND_ENDS" + doubleQuotes
					+ collon + doubleQuotes
					+ ErrorCodes.INVALID_NEW_BAND_ENDS + doubleQuotes);
			error.append(COMMA);

		}
		if (isZero) {
			error.append(doubleQuotes + "NEW_BAND_ENDS_ZERO" + doubleQuotes
					+ collon + doubleQuotes
					+ ErrorCodes.NEW_BAND_ENDS_ZERO + doubleQuotes);
			error.append(COMMA);

		}
		if (!maxVal && isBandChanged) {
			error.append(doubleQuotes + "INVALID_LAST_BAND_ENDS" + doubleQuotes
					+ collon + doubleQuotes
					+ ErrorCodes.INVALID_LAST_BAND_ENDS + doubleQuotes);
			error.append(COMMA);

		}
		if (isValidBandEnd) {
			error.append(doubleQuotes + "INVALID_BAND_STARTS_AND_ENDS"
					+ doubleQuotes + collon + doubleQuotes
					+ ErrorCodes.INVALID_BAND_STARTS_AND_ENDS
					+ doubleQuotes);
			error.append(COMMA);

		}
		if (invalidRate) {
			error
					.append(doubleQuotes + "INVALID_ASSET_CHARGE_RATE"
							+ doubleQuotes + collon + doubleQuotes
							+ ErrorCodes.INVALID_ASSET_CHARGE_RATE
							+ doubleQuotes);
			error.append(COMMA);

		}
		if (isExtraEntry) {
			error.append(doubleQuotes + "EXTRA_ASSET_CHARGE_RATE_ENTRY"
					+ doubleQuotes + collon + doubleQuotes
					+ ErrorCodes.EXTRA_ASSET_CHARGE_RATE_ENTRY
					+ doubleQuotes);
			error.append(COMMA);

		}
		if (isBandChanged && isBlank ) {
			error.append(doubleQuotes + "ONE_OR_MORE_INVALID_NEW_BAND_ENDS"
					+ doubleQuotes + collon + doubleQuotes
					+ ErrorCodes.ONE_OR_MORE_INVALID_NEW_BAND_ENDS
					+ doubleQuotes);
			error.append(COMMA);

		}
		if (isValidRate && maxVal && isRateChanged) {
			error.append(doubleQuotes + "ONE_OR_MORE_INVALID_ASSET_CHARGE_RATE"
					+ doubleQuotes + collon + doubleQuotes
					+ ErrorCodes.ONE_OR_MORE_INVALID_ASSET_CHARGE_RATE
					+ doubleQuotes);
			error.append(COMMA);

		} else if (!isBandChanged && isBlankRate) {
			error.append(doubleQuotes + "ONE_OR_MORE_INVALID_ASSET_CHARGE_RATE"
					+ doubleQuotes + collon + doubleQuotes
					+ ErrorCodes.ONE_OR_MORE_INVALID_ASSET_CHARGE_RATE
					+ doubleQuotes);
			error.append(COMMA);

		}
		

		return error.length() > 0
				&& COMMA.equals(String
						.valueOf(error.charAt(error.length() - 1))) ? error
				.deleteCharAt(error.length() - 1) : error;

	}

	public static StringBuffer diValidator(String[] dcValues, int rowCount) {
		StringBuffer error = new StringBuffer();
		boolean isBlank = false;
		String value = StringUtils.EMPTY;
		boolean isNumericVal = false;
		boolean isChanged = false;
		boolean isPreviousBlank = false;
		for (int i = 0; i < rowCount; i++) {
			value = dcValues[i];

			if (!isChanged) {
				isChanged = StringUtils.isNotBlank(value);
			}
			if (!isBlank) {
				isBlank = StringUtils.isBlank(value);
			}
			if (!isNumericVal && StringUtils.isNotBlank(value)) {

				isNumericVal = !value.contains(".") ? !value.matches("\\d\\d?\\d?")
						: !value.matches("\\d\\d?\\d?\\.\\d\\d?\\d?");
			}
			if (StringUtils.isNotBlank(value) && isBlank && !isPreviousBlank) {
				isPreviousBlank = true;
			}

		}
		String collon = " : ";
		String doubleQuotes = "\"";
		String COMMA = ",";
		if (isNumericVal) {
			error.append(doubleQuotes + "INVALID_NEW_DI_CHARGE" + doubleQuotes
					+ collon + doubleQuotes
					+ ErrorCodes.INVALID_NEW_DI_CHARGE + doubleQuotes);
			error.append(COMMA);

		}
		if (isPreviousBlank) {
			error.append(doubleQuotes + "BLANK_NEW_DI_CHARGE" + doubleQuotes
					+ collon + doubleQuotes
					+ ErrorCodes.BLANK_NEW_DI_CHARGE + doubleQuotes);
		}

		return error.length() > 0
		&& COMMA.equals(String
				.valueOf(error.charAt(error.length() - 1))) ? error
		.deleteCharAt(error.length() - 1) : error;
	}

	private static boolean isValidDecimal(String value) {

		if (!value.contains(".")) {
			return StringUtils.isNumeric(value);

		} else {
			return value.matches("[0-9]*\\d\\.\\d\\d?\\d?");

		}

	}
	
	private static boolean isValidDecimalBand(String value) {

		if (!value.contains(".")) {
			return StringUtils.isNumeric(value);

		} else {
			return value.matches("[0-9]*\\d\\.\\d\\d?");

		}

	}

}
