package com.manulife.pension.platform.web.validation.rules;

import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.validation.rules.generic.AtLeastOneNonZeroRule;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.NumericRule;
import com.manulife.pension.validator.ValidationRuleSet;

/**
 * @author Chris Shin
 */
public class ContractNumberRule extends ValidationRuleSet {

	private static final ContractNumberRule instance = new ContractNumberRule();

	/**
	 * Constructor.
	 */
	public ContractNumberRule() {
		super();
		/*
		 * MPR 347.2/MPR287. System must display an error message if user has not 
		 * entered Contract Number. Curser will go to the first
		 * mandatory page element that was identified as missing.
		 */
		addRule(new MandatoryRule(CommonErrorCodes.CONTRACT_NUMBER_MANDATORY),
			true);
		/*
		 * System validates that Contract Number has a size of 5 to 7 and contains
		 * all numeric values.
		 */
		addRule(new NumericRule(CommonErrorCodes.CONTRACT_NUMBER_INVALID, CommonConstants.CONTRACT_NUMBER_MIN_LENGTH, CommonConstants.CONTRACT_NUMBER_MAX_LENGTH));
		addRule(new AtLeastOneNonZeroRule(CommonErrorCodes.CONTRACT_NUMBER_INVALID));
	}

	public static final ContractNumberRule getInstance() {
		return instance;
	}
}