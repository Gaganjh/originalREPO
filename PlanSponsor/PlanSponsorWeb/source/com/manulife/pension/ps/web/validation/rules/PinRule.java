package com.manulife.pension.ps.web.validation.rules;

import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.validator.ValidationRuleSet;

/**
 * @author Tony Tomasone
 * 
 */

public class PinRule extends ValidationRuleSet {

	private static final PinRule instance = new PinRule();

	/**
	 * Constructor for PinRule
	 */
	public PinRule() {
		super();
		/*
		 * MPR 286: System must be able to determine that user is an existing site user if the PIN entered is numeric.  All users created on new site are initially provided a alpha PIN.
		 * MPR 287: System must display an error message if mandatory data has not been entered.
		 */
		addRule(new MandatoryRule(ErrorCodes.PIN_MANDATORY),true);
		/*
		 * MPR ???: System must display an error message if PIN entered 
		 * does not meet standards
		 */
		addRule(new RegularExpressionRule(ErrorCodes.PIN_INVALID, "^[a-zA-Z0-9]{5,32}$"),true);
	}

	public static final PinRule getInstance() {
		return instance;
	}
}

