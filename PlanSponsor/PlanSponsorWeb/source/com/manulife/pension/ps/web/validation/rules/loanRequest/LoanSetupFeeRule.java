package com.manulife.pension.ps.web.validation.rules.loanRequest;

import java.math.BigDecimal;

import com.manulife.pension.validator.ValidationRuleSet;

/**
 * @author sternlu
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LoanSetupFeeRule extends ValidationRuleSet{
	 
	//private int []errorCodes;
	
	/**
	 * Constructor for PinRule
	 */
	public LoanSetupFeeRule( int [] errorCodes, BigDecimal loanAmt, String saveOnlyInd) {
		super();

		addRule(new AmountRule(errorCodes[0]),true);
		if(!"Y".equalsIgnoreCase(saveOnlyInd))
			addRule(new MaxLoanSetupFeeRule(errorCodes[1],loanAmt),true);

	}
}
