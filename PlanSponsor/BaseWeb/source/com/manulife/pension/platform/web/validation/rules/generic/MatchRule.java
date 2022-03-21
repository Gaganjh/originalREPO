package com.manulife.pension.platform.web.validation.rules.generic;

import java.util.Collection;

import com.manulife.pension.util.Pair;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationRule;

/**
 * @author Chris Shin
 */
public class MatchRule extends ValidationRule {

	public MatchRule(int errorCode) {
		super(errorCode);
	}

	/**
	 * @see MultiFieldValidateable#validate(int, String[], Collection, Object)
	 */
	public boolean validate(String[] fieldIds, Collection validationErrors,
			Object objectToValidate) {
		boolean isValid = false;

		if (objectToValidate != null) {
			Pair pair = (Pair) objectToValidate;

			Object obj1 = pair.getFirst();
			Object obj2 = pair.getSecond();

			if (obj1 != null && obj2 != null) {
				if (obj1.equals(obj2)) {
					isValid = true;
				}
			}
		}

		if (!isValid) {
			validationErrors.add(new ValidationError(fieldIds, getErrorCode()));
		}
		return isValid;
	}

}