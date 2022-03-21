package com.manulife.pension.platform.web.util;

import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.validator.ValidationRuleSet;

/**
 * @author 
 */
public class MobileRule extends ValidationRuleSet {

	private static final MobileRule instance = new MobileRule();

	/**
	 * Constructor.
	 */
	private MobileRule() {
		super();
		
		addRule(new RegularExpressionRule(CommonErrorCodes.MOBILE_NOT_NUMERIC, "^[0-9]{0,10}$"),true);
		

	}	 

	public static final MobileRule getInstance() {
		return instance;
	}
}