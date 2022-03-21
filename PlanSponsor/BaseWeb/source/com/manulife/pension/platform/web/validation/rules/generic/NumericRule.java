package com.manulife.pension.platform.web.validation.rules.generic;



public class NumericRule extends RegularExpressionRule {

	public final static int NO_LIMIT = -1;

	public NumericRule(int errorCode) {
		super(errorCode, getPattern(NO_LIMIT, NO_LIMIT));
	}

	public NumericRule(int errorCode, int exactNumberOfDigits) {
		super(errorCode, getPattern(exactNumberOfDigits, exactNumberOfDigits));
	}

	public NumericRule(int errorCode, int minNumberOfDigits,
			int maxNumberOfDigits) {
		super(errorCode, getPattern(minNumberOfDigits, maxNumberOfDigits));
	}

	private static String getPattern(int minNumberOfDigits,
			int maxNumberOfDigits) {

		if (minNumberOfDigits == NO_LIMIT && maxNumberOfDigits == NO_LIMIT) {
			return "^[[:digit:]]+$";
		} else if (minNumberOfDigits == NO_LIMIT) {
			return "^[[:digit:]]{," + maxNumberOfDigits + "}$";
		} else if (maxNumberOfDigits == NO_LIMIT) {
			return "^[[:digit:]]{" + minNumberOfDigits + ",}$";
		} else {
			return "^[[:digit:]]{" + minNumberOfDigits + ","
					+ maxNumberOfDigits + "}$";
		}
	}
}