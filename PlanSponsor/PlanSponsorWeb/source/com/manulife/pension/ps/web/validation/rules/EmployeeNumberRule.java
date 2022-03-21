package com.manulife.pension.ps.web.validation.rules;

import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.NumericRule;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.validator.ValidationRuleSet;

/**
 * @author Charles Chan
 */
public class EmployeeNumberRule extends ValidationRuleSet {

	private static final EmployeeNumberRule instance = new EmployeeNumberRule();

	/**
	 * Constructor.
	 */
	private EmployeeNumberRule() {
		super();
		/*
		 * MPR 82. First Name, Last Name Employee Number and email address are
		 * mandatory
		 */
		addRule(new MandatoryRule(ErrorCodes.EMPLOYEE_NUMBER_MANDATORY), true);
		/*
		 * 9 characters numeric.
		 */
		addRule(new NumericRule(ErrorCodes.EMPLOYEE_NUMBER_INVALID, 5, 20));
	}

	public static final EmployeeNumberRule getInstance() {
		return instance;
	}
}