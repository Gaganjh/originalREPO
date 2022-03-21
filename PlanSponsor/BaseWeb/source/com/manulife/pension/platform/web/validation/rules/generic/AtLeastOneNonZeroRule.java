package com.manulife.pension.platform.web.validation.rules.generic;

import java.util.Collection;

import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationRule;

public class AtLeastOneNonZeroRule extends ValidationRule {

	public AtLeastOneNonZeroRule(int errorCode) {
		super(errorCode);
	}

	/**
	 * @see com.manulife.pension.ps.web.validation.Validateable#validate(java.lang.String,
	 *      java.util.Collection, java.lang.Object)
	 */
	public boolean validate(String[] fieldIds, Collection validationErrors,
			Object objectToValidate) {
		String str = objectToValidate.toString();
		boolean allZeros = true;

		for (int i = 0; i < str.length() && allZeros; i++) {
			if (str.charAt(i) != '0') {
				allZeros = false;
			}
		}
		if (allZeros) {
			validationErrors.add(new ValidationError(fieldIds, getErrorCode()));
		}
		return !allZeros;
	}
}