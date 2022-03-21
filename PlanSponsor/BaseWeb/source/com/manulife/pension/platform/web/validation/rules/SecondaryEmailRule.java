package com.manulife.pension.platform.web.validation.rules;

import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.validator.ValidationRuleSet;

/**
 * @author nallaba
 */
public class SecondaryEmailRule extends ValidationRuleSet {

	private static final SecondaryEmailRule instance = new SecondaryEmailRule();

	/**
	 * Constructor.
	 */
	private SecondaryEmailRule() {
		super();
		//addRule(new MandatoryRule(CommonErrorCodes.EMAIL_MANDATORY), true);
		addRule(new RegularExpressionRule(CommonErrorCodes.SECONDARY_EMAIL_INVALID,
				"^[a-zA-Z0-9-._']+@([a-zA-Z0-9-]+\\.)+[a-zA-Z0-9]+$") );
	}

	public static final SecondaryEmailRule getInstance() {
		return instance;
	}
}