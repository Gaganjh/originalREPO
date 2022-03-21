/*
 * Created on May 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.web.validation.rules;

import com.manulife.pension.platform.web.validation.rules.generic.MatchRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.util.Pair;
import com.manulife.pension.validator.Validateable;
import com.manulife.pension.validator.ValidationRuleSet;

/**
 * @author sternlu
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NewPinRule extends ValidationRuleSet{
private static final NewPinRule instance = new NewPinRule();

	/**
	 * Constructor.
	 */
	public NewPinRule() {
		super();

		/*
		 * the system will display an error message if the
		 * confirm pin does not match the new pin
		 */
		addRule(new MatchRule(ErrorCodes.PINS_DO_NOT_MATCH));
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

	public static NewPinRule getInstance() {
		return instance;
	}

}
