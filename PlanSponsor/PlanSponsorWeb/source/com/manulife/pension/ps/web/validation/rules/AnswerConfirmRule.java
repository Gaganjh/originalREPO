package com.manulife.pension.ps.web.validation.rules;

import com.manulife.pension.platform.web.validation.rules.generic.MatchRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.util.Pair;
import com.manulife.pension.validator.Validateable;
import com.manulife.pension.validator.ValidationRuleSet;

/**
 * A rule set to validate challenge answer and its confirmation. The fieldIDs
 * should be an arry of:
 * 
 * 	FIELD_CHALLENGE_ANSWER and
 *  FIELD_VERIFY_ANSWER
 * 
 * The objects to validate should be an array of:
 * 
 *  ChallengeAnswer and
 *  VerifyChallengeAnswer
 * 
 */
public class AnswerConfirmRule extends ValidationRuleSet {

	private static final AnswerConfirmRule instance = new AnswerConfirmRule();

	/**
	 * Constructor.
	 */
	public AnswerConfirmRule() {
		super();
		addRule(new RegularExpressionRule(ErrorCodes.ANSWER_INVALID,
				"^.{1,32}$"), false);
		addRule(new MatchRule(ErrorCodes.ANSWERS_DO_NOT_MATCH));
	}

	/**
	 * @see com.manulife.pension.ps.web.validation.ValidationRuleSet#getObjectToValidate(com.manulife.pension.ps.web.validation.Validateable,
	 *      java.lang.Object)
	 */
	protected Object getObjectToValidate(Validateable validateable, Object obj) {
		if (validateable instanceof RegularExpressionRule) {
			return ((Pair) obj).getFirst();
		}
		return obj;
	}
	
	protected String[] getFieldIds(Validateable validateable, String[] fieldIds) {
		if (validateable instanceof RegularExpressionRule) {
			return new String[] { fieldIds[0] };
		}
		return fieldIds;
	}

	public static AnswerConfirmRule getInstance() {
		return instance;
	}

}