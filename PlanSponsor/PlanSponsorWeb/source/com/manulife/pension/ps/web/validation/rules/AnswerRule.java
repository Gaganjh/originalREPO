package com.manulife.pension.ps.web.validation.rules;

import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.MatchRule;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.util.Pair;
import com.manulife.pension.validator.Validateable;
import com.manulife.pension.validator.ValidationRuleSet;

/**
 * @author Tony Tomasone
 * 
 */

public class AnswerRule extends ValidationRuleSet {

	private static final AnswerRule instance = new AnswerRule();
	
	/**
	 * Constructor for AnswerRule
	 */
	public AnswerRule() {
		super();
		/*
		 * MPR 300.6: Challenge Question Answer (Mandatory)
		 * MPR 300.7: Challenge Question Answer Confirmation (mandatory)
		 * MPR 302: System  must display an error message if mandatory data is not 
		 * entered, and move cursor to first mandatory data field missing.
		 */
		addRule(new MandatoryRule(ErrorCodes.ANSWER_MANDATORY),true);
		/*
		 * MPR 309: System must display an error message if Question Answer is less than 1 character or Greater than 32 characters.
		 * MPR 424.	Answer must be between 1 and 32 characters
		 * MPR 425.	Answer must allow blanks
		 */
		//addRule(new RegularExpressionRule(ErrorCodes.ANSWER_INVALID, "^[\\ \\w]{1,32}$"),true);
		/*
		 * MPR 304: System must display an error message if Question Answer 
		 * Confirmation does not match Question Answer and move cursor to Question Answer input
		 */
		addRule(new MatchRule(ErrorCodes.ANSWERS_DO_NOT_MATCH),true);
	}

	public static final AnswerRule getInstance() {
		return instance;
	}
	
	protected Object getObjectToValidate(Validateable validateable, Object obj) {

		if (validateable instanceof MandatoryRule) {
			return ((Pair) obj).getFirst();
		} else {
			return obj;
		}
	}
}

