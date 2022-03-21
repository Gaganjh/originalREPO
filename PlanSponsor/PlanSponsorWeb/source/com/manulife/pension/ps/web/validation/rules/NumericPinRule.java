/*
 * Created on May 10, 2005
 * for i-con1 pin verify page
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.web.validation.rules;

import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.validator.ValidationRuleSet;

/**
 * @author sternlu
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NumericPinRule extends ValidationRuleSet {
 
	private int errorCode;
	/**
	 * Constructor for PinRule
	 */
	public NumericPinRule( int errorCode) {
		super();
		this.errorCode = errorCode;
		/*
		 * System must display an error message if the  PIN entered is not numeric, less then 5 characters or starts with 0
		 * System must display an error message if mandatory data has not been entered.
		 */
		addRule(new MandatoryRule(errorCode),true);
		addRule(new RegularExpressionRule(errorCode, "^[1-9]{5}$"),true);

	}
	
	
}
