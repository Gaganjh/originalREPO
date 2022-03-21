/*
 * Created on May 12, 2005
 * Validates input field for being non-empty, numeric, greater then zero and less then hundreed
 */
package com.manulife.pension.ps.web.validation.rules.amortizationSchedule;

import java.math.BigDecimal;
import java.util.Collection;

import com.manulife.pension.validator.ValidationError;

/**
 * @author sternlu
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GreaterThanZeroLessThanHundredRule {
    private int errorCode;
	public GreaterThanZeroLessThanHundredRule(int errorCode) {
		this.errorCode = errorCode;
	}

	public BigDecimal validate(String fieldId, Collection validationErrors,
			String objectToValidate) {

		BigDecimal ZERO = new BigDecimal("0");
		BigDecimal amount = ZERO;
		BigDecimal ONE_HUNDRED = new BigDecimal("100");
		boolean validated = true;
		try {
			amount = new BigDecimal(objectToValidate);
			validated = (amount.compareTo(ZERO) > 0 && amount
					.compareTo(ONE_HUNDRED) <= 0);

		} catch (Exception e) {
			
			validated = false;
		}
		if (!validated)
			validationErrors.add(new ValidationError(fieldId, errorCode));

		return amount;
	}

	public BigDecimal validate(String fieldId, Collection validationErrors,
			String objectToValidate, int maxDecimalPlaces) {

		BigDecimal ZERO = new BigDecimal("0");
		BigDecimal amount = ZERO;
		BigDecimal ONE_HUNDRED = new BigDecimal("100");
		boolean validated = true;
		try {
			amount = new BigDecimal(objectToValidate);
			validated = (amount.compareTo(ZERO) > 0 && amount
					.compareTo(ONE_HUNDRED) <= 0);

		} catch (Exception e) {
			
			validated = false;
		}

		if(validated)
		{
		int iValue=amount.intValue();
		if (maxDecimalPlaces >0)
		// then chack the required decimal places
			validated = amount.scale()<=maxDecimalPlaces;
		// validate the whole number
		else
		{
			if (amount!=null && iValue !=0)
				validated = amount.doubleValue()%iValue==0;
			else 
				validated =false;	
		}
		}		
		
		if (!validated)
			validationErrors.add(new ValidationError(fieldId, errorCode));

		return amount;
	}	
}

