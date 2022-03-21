/*
 * Created on Sep 14, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.web.validation.rules.loanRequest;

import java.math.BigDecimal;
import java.util.Collection;

import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationRule;

/**
 * @author sternlu
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MaxLoanSetupFeeRule extends ValidationRule {

	private BigDecimal loanAmount;
	public MaxLoanSetupFeeRule(int errorCode, BigDecimal loanAmount) {
		super(errorCode);
		this.loanAmount = loanAmount;
	}

	public boolean validate(String[] fieldIds, Collection validationErrors,
			Object objectToValidate) {
		boolean validate = true;
			
		if (objectToValidate == null)
			validate = false;
		
		else
		{
		try {
			BigDecimal maxContractLoanSetupFeeAmt = new BigDecimal(loanAmount.doubleValue()/100 *80);
			BigDecimal setupFeeAmount = new BigDecimal((String)objectToValidate );
			if(setupFeeAmount.compareTo(maxContractLoanSetupFeeAmt) > 0)
				validate = false;
			} catch (Exception e) {
				validate = false;
			}
		}
		if (!validate)
			validationErrors.add(new ValidationError(fieldIds, getErrorCode()));
		return validate;
	}
}
