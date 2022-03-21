package com.manulife.pension.platform.web.validation.rules;

import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.validation.rules.generic.AtLeastOneNonZeroRule;
import com.manulife.pension.platform.web.validation.rules.generic.NumericRule;
import com.manulife.pension.validator.ValidationRuleSet;
/**
 * @author Chris Shin
 */
public class ContractNumberNoMandatoryRule extends ValidationRuleSet {

	private static final ContractNumberNoMandatoryRule instance = new ContractNumberNoMandatoryRule();

	/**
	 * Constructor.
	 */
	public ContractNumberNoMandatoryRule() {
		super();
		/*
		 * System validates that Contract Number has a size of 5 to 7 and contains
		 * all numeric values.
		 */
		addRule(new NumericRule(CommonErrorCodes.CONTRACT_NUMBER_INVALID,
				CommonConstants.CONTRACT_NUMBER_MIN_LENGTH,
				CommonConstants.CONTRACT_NUMBER_MAX_LENGTH));
		addRule(new AtLeastOneNonZeroRule(CommonErrorCodes.CONTRACT_NUMBER_INVALID));
	}

	public static final ContractNumberNoMandatoryRule getInstance() {
		return instance;
	}
}