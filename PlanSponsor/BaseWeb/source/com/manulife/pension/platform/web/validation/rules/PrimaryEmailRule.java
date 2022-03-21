package com.manulife.pension.platform.web.validation.rules;

import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.validator.ValidationRuleSet;

/**
 * @author nallaba
 */
public class PrimaryEmailRule extends ValidationRuleSet {

	private static final PrimaryEmailRule instance = new PrimaryEmailRule();

	/**
	 * Constructor.
	 */
	private PrimaryEmailRule() {
		super();
		addRule(new MandatoryRule(CommonErrorCodes.PRIMARY_EMAIL_MANDATORY), true);
		addRule(new RegularExpressionRule(CommonErrorCodes.PRIMARY_EMAIL_INVALID,
				"^[a-zA-Z0-9-._']+@([a-zA-Z0-9-]+\\.)+[a-zA-Z0-9]+$") );
	}

	public static final PrimaryEmailRule getInstance() {
		return instance;
	}
}