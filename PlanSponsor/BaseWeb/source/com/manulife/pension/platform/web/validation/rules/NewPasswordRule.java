package com.manulife.pension.platform.web.validation.rules;

import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.validation.rules.generic.MatchRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.util.Pair;
import com.manulife.pension.validator.Validateable;
import com.manulife.pension.validator.ValidationRuleSet;

/**
 * Rule set to validate new password and its confirmation
 */
public class NewPasswordRule extends ValidationRuleSet {

	private static final NewPasswordRule instance = new NewPasswordRule();

	/**
	 * Constructor.
	 */
	public NewPasswordRule() {
		super();

		/*
		 * MPR.367, MPR300.2, MPR302 the system will display an error message if
		 * the new password is not entered MPR307, MPR308. MPR368 verifies the
		 * password meets standard requirements
		 */
		//addRule(new RegularExpressionRule(
				//CommonErrorCodes.PASSWORD_FAILS_STANDARDS, "^[a-zA-Z0-9]{5,32}$"),
				//false);
		/* New Password Rules Updated */
		addRule(new RegularExpressionRule(
				CommonErrorCodes.PASSWORD_FAILS_STANDARDS, "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[$&+,:;=\\\\_?@#|/'<>.^*()%!-]).{8,64}$"),
				false);


		/*
		 * MPR.303, MPR366, MPR3 the system will display an error message if the
		 * confirmation password does not meet the new password
		 */
		addRule(new MatchRule(CommonErrorCodes.PASSWORDS_DO_NOT_MATCH));
	}

	/**
	 * @see com.manulife.pension.ps.web.validation.ValidationRuleSet#getObjectToValidate(com.manulife.pension.ps.web.validation.Validateable,
	 *      java.lang.Object)
	 */
	protected Object getObjectToValidate(Validateable validateable, Object obj) {

		if (validateable instanceof RegularExpressionRule) {
			return ((Pair) obj).getFirst();
		} else {
			return obj;
		}
	}

	protected String[] getFieldIds(Validateable validateable, String[] fieldIds) {
		if (validateable instanceof RegularExpressionRule) {
			return new String[] { fieldIds[0] };
		}
		return fieldIds;
	}

	public static NewPasswordRule getInstance() {
		return instance;
	}

}