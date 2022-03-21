package com.manulife.pension.platform.web.validation.rules;

import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.validator.ValidationRuleSet;

/**
 * @author Charles Chan
 */
public class EmailRule extends ValidationRuleSet {

	private static final EmailRule instance = new EmailRule();

	/**
	 * Constructor.
	 */
	private EmailRule() {
		super();
		/*
		 * MPR 34. System must display an error message if user has not entered
		 * First name, Last name, email or SSN. Curser will go to the first
		 * mandatory page element that was identified as missing.
		 */
		addRule(new MandatoryRule(CommonErrorCodes.EMAIL_MANDATORY), true);
		/*
		 * MPR 37. System validates that Email address has an @ symbol and a
		 * period after the @ symbol. If not system will display an error
		 * message and cursor will go to the email entry page element.
		 */
		 
		 /* replaced the format with the one copied from the EZK
		 */
		//addRule(new RegularExpressionRule(ErrorCodes.EMAIL_INVALID,
		//		"^[^@]+@[^\\.]+\\..+$"));
		// 2004-10-04 added apostrophe (')
		addRule(new RegularExpressionRule(CommonErrorCodes.EMAIL_INVALID,
				"^[a-zA-Z0-9-._']+@([a-zA-Z0-9-]+\\.)+[a-zA-Z0-9]+$") );
	}

	public static final EmailRule getInstance() {
		return instance;
	}
}