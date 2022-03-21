package com.manulife.pension.platform.web.validation.rules;

import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.validator.ValidationRuleSet;

/**
 * @author Charles Chan
 */
public class NameRule extends ValidationRuleSet {

	private static final NameRule firstNameInstance = new NameRule(
			CommonErrorCodes.FIRST_NAME_MANDATORY,
			CommonErrorCodes.FIRST_NAME_INVALID);
	
	private static final NameRule stmtFirstNameInstance = new NameRule(
			CommonErrorCodes.FIRST_NAME_MANDATORY,
			CommonErrorCodes.STMT_FIRST_NAME_INVALID);

	private static final NameRule lastNameInstance = new NameRule(
			CommonErrorCodes.LAST_NAME_MANDATORY,
			CommonErrorCodes.LAST_NAME_INVALID);

	/**
	 * Constructor.
	 */
	private NameRule(int mandatoryErrorCode, int invalidErrorCode) {
		/*
		 * MPR 34. System must display an error message if user has not entered
		 * First name, Last name, email or SSN. Curser will go to the first
		 * mandatory page element that was identified as missing.
		 */
		addRule(new MandatoryRule(mandatoryErrorCode), true);
		/*
		 * Max. 30 characters, no special characters except apostrophe,no  numbers, dash and
		 * period. No leading space.
		 * NOTE: [:alnum:] accepts underscore '_' which we want to avoid.
		 */
		addRule(new RegularExpressionRule(invalidErrorCode,
				"^[[:alpha:] \\-\\.']{1,30}$"));
	}

	public static NameRule getFirstNameInstance() {
		return firstNameInstance;
	}

	public static NameRule getStmtFirstNameInstance() {
		return stmtFirstNameInstance;
	}
	
	public static NameRule getLastNameInstance() {
		return lastNameInstance;
	}
}