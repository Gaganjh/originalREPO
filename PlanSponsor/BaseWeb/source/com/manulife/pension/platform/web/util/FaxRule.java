package com.manulife.pension.platform.web.util;

import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.validator.ValidationRuleSet;

/**
 * @author Ranjith Kumar
 */
public class FaxRule extends ValidationRuleSet {

	private static final FaxRule instance = new FaxRule();

	/**
	 * Constructor.
	 */
	private FaxRule() {
		super();
		
		addRule(new RegularExpressionRule(CommonErrorCodes.FAX_NOT_NUMERIC, "^[0-9]{0,10}$"),true);

	}	

	public static final FaxRule getInstance() {
		return instance;
	}
}