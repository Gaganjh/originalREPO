package com.manulife.pension.platform.web.validation.rules.generic;

import java.util.Collection;

import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationRule;

/**
 * A mandatory validation rule.
 * 
 * @author Charles Chan
 */
public class MandatoryRule extends ValidationRule {

	/**
	 * Constructor.
	 */
	public MandatoryRule(int errorCode) {
		super(errorCode);
	}

	/**
	 * @see com.manulife.pension.ps.web.validation.Validateable#validate(java.lang.String,
	 *      java.util.Collection, java.lang.Object)
	 */
	public boolean validate(String[] fieldIds, Collection validationErrors,
			Object objectToValidate) {

		boolean isValid = true;
		if (objectToValidate == null) {
			isValid = false;
		} else {
			if (objectToValidate instanceof String) {
				String stringToValidate = (String) objectToValidate;
				if (stringToValidate.length() == 0) {
					isValid = false;
				}
			}
		}
		if (!isValid) {
			validationErrors.add(new ValidationError(fieldIds, getErrorCode()));
		}
		return isValid;
	}
}