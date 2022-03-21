package com.manulife.pension.platform.web.validation.rules.generic;

import java.util.Collection;

import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.validator.ValidationRule;

/**
 * @author Chris Shin
 */
public class PasswordRule extends ValidationRule {

	private int index;
	/**
	 * Constructor.
	 */
	public PasswordRule(int errorCode, int index) {
		super(errorCode);
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	/**
	 * @see com.manulife.pension.ps.web.validation.Validateable#validate(java.lang.String,
	 *      java.util.Collection, java.lang.Object)
	 */
	public boolean validate(String[] fieldIds, Collection validationErrors,
			Object objectToValidate) {

		boolean isValid = true;

		MandatoryRule rule = new MandatoryRule(getErrorCode());
		isValid = rule.validate(fieldIds, validationErrors, objectToValidate);

		if (isValid) {
			RegularExpressionRule RErule = new RegularExpressionRule(
					CommonErrorCodes.PASSWORD_FAILS_STANDARDS, "^[\\w]{5,32}$");

			isValid = RErule.validate(fieldIds, validationErrors,
					objectToValidate);

		}

		return isValid;
	}

}