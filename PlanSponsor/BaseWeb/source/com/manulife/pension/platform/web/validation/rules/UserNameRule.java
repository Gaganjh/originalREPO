package com.manulife.pension.platform.web.validation.rules;

import java.util.Collection;

import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.validation.rules.generic.InvalidBeginOrEndWithPeriodRule;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.validator.ValidationRuleSet;
/**
 * @author Tony Tomasone
 * 
 */

public class UserNameRule extends ValidationRuleSet {

	private static final UserNameRule instance = new UserNameRule();
	
	private final String userNameRuleRegExp = "^(?![\\.]{6,20})[a-zA-Z0-9_\\-\\.]{6,20}$";
	
	/**
	 * Constructor for UserNameRule
	 */
	public UserNameRule() {
		super();
		/*
		 * MPR 300.1: User ID (Mandatory)
		 * MPR 302: System must display an error message if mandatory data is not 
		 * entered, and move cursor to first mandatory data field missing.
		 */
		addRule(new MandatoryRule(CommonErrorCodes.USER_NAME_MANDATORY),true);
		/*
		 * MPR 306: System must display an error message if User ID entered 
		 * does not meet User ID standards
		 * MPR 453  User ID must be 6-20 characters
		 * MPR 454 User ID must be Alphanumeric. 
		 * Numbers (0-9) or characters (A-Z, a-z) are allowed
		 * Can contain up to 20 lower case or upper case characters except for " / \ [ ] : ;  | = , + * ? < > @ '
		 * no imbedded or leading blanks
		 * cannot consist solely of periods (.)
		 * is not case sensitive
		 */
		addRule(new RegularExpressionRule(CommonErrorCodes.USER_NAME_INVALID, userNameRuleRegExp),true);

	}
	
	public static final UserNameRule getInstance() {
		return instance;
	}

	public boolean validate(String fieldId, Collection validationErrors,
			Object objectToValidate) {
		
		boolean isValid = true;
		
		isValid = super.validate(fieldId, validationErrors, objectToValidate);
		
		if (isValid) {
			// common log 50274 no period at either the beginning or the end
			InvalidBeginOrEndWithPeriodRule rule = new InvalidBeginOrEndWithPeriodRule(
					CommonErrorCodes.USER_NAME_INVALID);
			isValid = rule.validate(fieldId, validationErrors, objectToValidate);
		}
			
		return isValid;
	}
	
	public String getUserNameRuleRegExp() {
		return userNameRuleRegExp;
	}
}

