package com.manulife.pension.platform.web.util;

import com.manulife.pension.platform.web.validation.rules.generic.AtLeastOneNonZeroRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.validator.ValidationRuleSet;

/**
 * @author Ranjith Kumar
 */
public class ExtensionRule extends ValidationRuleSet {

	private static final ExtensionRule instance = new ExtensionRule();

	/**
	 * Constructor.
	 */
	private ExtensionRule() {
		super();
		
		addRule(new RegularExpressionRule(CommonErrorCodes.EXTENSION_NO_INVALID, "^[0-9]{0,8}$"),true);
	
	}

	public static final ExtensionRule getInstance() {
		return instance;
	}
}