package com.manulife.pension.platform.web.util;

import com.manulife.pension.platform.web.validation.rules.generic.AtLeastOneNonZeroRule;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.validator.Validateable;
import com.manulife.pension.validator.ValidationRuleSet;

/**
 * @author Charles Chan
 */
public class SsnRule extends ValidationRuleSet {

	private static final SsnRule instance = new SsnRule();

	/**
	 * Constructor.
	 */
	private SsnRule() {
		super();
		/*
		 * MPR 34. System must display an error message if user has not entered
		 * First name, Last name, email or SSN. Curser will go to the first
		 * mandatory page element that was identified as missing.
		 */
		addRule(new MandatoryRule(CommonErrorCodes.PERSONAL_IDENTIFIER_MANDATORY),
				true);
		/*
		 * MPR 226. System validates that when personal identification type is
		 * SSN that no non-numeric values have been entered or that all “0” have
		 * not been entered, or displays error and return cursor to SSN field .
		 */
		addRule(
				new RegularExpressionRule(CommonErrorCodes.SSN_INVALID, "^[0-9]{9}$"),
				true);

		addRule(new AtLeastOneNonZeroRule(CommonErrorCodes.SSN_INVALID));
	}

	/**
	 * @see com.manulife.pension.ps.web.validation.ValidationRuleSet#getObjectToValidate(com.manulife.pension.ps.web.validation.Validateable,
	 *      java.lang.Object)
	 */
	protected Object getObjectToValidate(Validateable validateable, Object obj) {
		if (validateable instanceof MandatoryRule) {
			/*
			 * Ssn is a complex object with 3 groups of digits, the
			 * MandatoryRule cannot look inside the Ssn object to determine if
			 * the digits are all empty.
			 */
			if (obj instanceof Ssn) {
				Ssn ssn = (Ssn) obj;
				if (ssn.isEmpty()) {
					return null;
				}
			}
		}
		return obj;
	}

	public static final SsnRule getInstance() {
		return instance;
	}
}