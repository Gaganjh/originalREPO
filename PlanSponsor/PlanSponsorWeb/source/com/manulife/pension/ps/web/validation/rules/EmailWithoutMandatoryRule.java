package com.manulife.pension.ps.web.validation.rules;

import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.validator.ValidationRuleSet;

/**
 * @author Steven Wang
 */
public class EmailWithoutMandatoryRule extends ValidationRuleSet {

	private static final EmailWithoutMandatoryRule instance = new EmailWithoutMandatoryRule();

	/**
	 * Constructor.
	 */
	private EmailWithoutMandatoryRule() {
		super();
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
		/*
		 * CL #133727- Email Rules have been updated to restrict the 
		 * use of symbols at the beginning of the mail id, to disallow 
		 *  multiple periods (.) at a time, etc. 
		 */
		addRule(new RegularExpressionRule(ErrorCodes.EMAIL_INVALID,
				"^[a-zA-Z0-9_][a-zA-Z0-9-._']+@([a-zA-Z0-9-]+\\.)+[a-zA-Z0-9]+$") );
	}

	public static final EmailWithoutMandatoryRule getInstance() {
		return instance;
	}
}