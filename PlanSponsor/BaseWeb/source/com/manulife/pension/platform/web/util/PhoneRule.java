package com.manulife.pension.platform.web.util;

import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.validator.ValidationRuleSet;

/**
 * @author 
 */
public class PhoneRule extends ValidationRuleSet {

	private static final PhoneRule instance = new PhoneRule();

	/**
	 * Constructor.
	 */
	private PhoneRule() {
		super();
		
		addRule(new RegularExpressionRule(CommonErrorCodes.PHONE_NOT_NUMERIC, "^[0-9]{0,10}$"),true);
		

	}	 

	public static final PhoneRule getInstance() {
		return instance;
	}
}