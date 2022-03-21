package com.manulife.pension.ps.web.validation.rules;

import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.validator.ValidationRuleSet;

/**
 * @author Tony Tomasone
 * 
 */

public class QuestionRule extends ValidationRuleSet {

	private static final QuestionRule instance = new QuestionRule();

	/**
	 * Constructor for QuestionRule
	 */
	public QuestionRule() {
		super();
		/*
		 * MPR 300.5: Challenge Question (Mandatory)
		 * MPR 302: System must display an error message if mandatory 
		 * data is not entered, and move cursor to first mandatory data field missing.
		 */
		addRule(new MandatoryRule(ErrorCodes.QUESTION_MANDATORY),true);
		/*
		 * MPR ???: System must display an error message if Question entered 
		 * does not meet standards
		 */
		addRule(new RegularExpressionRule(ErrorCodes.QUESTION_INVALID, "^.{1,100}$"));

	}

	public static final QuestionRule getInstance() {
		return instance;
	}
}

