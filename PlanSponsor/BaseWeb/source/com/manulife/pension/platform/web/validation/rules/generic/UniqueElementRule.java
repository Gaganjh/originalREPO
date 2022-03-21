package com.manulife.pension.platform.web.validation.rules.generic;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import com.manulife.pension.util.Pair;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationRule;

/**
 * @author Charles Chan
 */
public class UniqueElementRule extends ValidationRule {

	private Comparator comparator;

	/**
	 * Constructor.
	 */
	public UniqueElementRule(int errorCode) {
		super(errorCode);
		comparator = new Comparator() {
			public int compare(Object o1, Object o2) {
				return o1.equals(o2) ? 0 : 1;
			}
		};
	}

	public UniqueElementRule(int errorCode, Comparator comparator) {
		super(errorCode);
		this.comparator = comparator;
	}

	/**
	 * @see com.manulife.pension.ps.web.validation.Validateable#validate(java.lang.String,
	 *      java.util.Collection, java.lang.Object)
	 */
	public boolean validate(String[] fieldIds, Collection validationErrors,
			Object objectToValidate) {
		Pair pair = (Pair) objectToValidate;

		Object collObj = pair.getFirst();
		Object obj = pair.getSecond();

		boolean isValid = true;
		if (collObj instanceof Collection) {
			Collection collection = (Collection) collObj;
			for (Iterator it = collection.iterator(); it.hasNext();) {
				Object o1 = it.next();
				if (comparator.compare(o1, obj) == 0) {
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