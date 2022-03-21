/*
 * Created on May 12, 2005
 * Validates input field for being non-empty, integer, greater then zero 
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
public class GreaterThanZeroRule  {
private int errorCode;
	public GreaterThanZeroRule(int errorCode) {
		this.errorCode = errorCode;
	}
	// Validate that the number field is not null and greater then zero
	public BigDecimal validate(String fieldId, Collection validationErrors,
			String objectToValidate) {
		boolean validated = true;
		BigDecimal value =new BigDecimal("0");
		try {
			value = isGreaterThenZero(objectToValidate);
		} catch (Exception e) {
			validated = false;
		}

		if (!validated)
			validationErrors.add(new ValidationError(fieldId, errorCode));
		return value;
	}
	// insures that the number field  has correct number of decimal places
	public BigDecimal validate(String fieldId, Collection validationErrors,
			String objectToValidate, int maxDecimalPlaces) {
		boolean validated = true;
		BigDecimal value =new BigDecimal("0");
//		 first validate if the number field is not null and greater then zero
		try {
			value = isGreaterThenZero(objectToValidate);
		} catch (Exception e) {
			validated = false;
		}
		if(validated)
		{
		int iValue=value.intValue();
		if (maxDecimalPlaces >0)
		// then chack the required decimal places
			validated = value.scale()<=maxDecimalPlaces;
		// validate the whole number
		else
		{
			if (value!=null && iValue !=0)
				validated = value.doubleValue()%value.intValue()==0;
			else 
				validated =false;	
		}
		}
		if(!validated)
			validationErrors.add(new ValidationError(fieldId, errorCode));
	
		
		return value;
	}
	private BigDecimal isGreaterThenZero (String objectToValidate) throws Exception
	{
		final BigDecimal ZERO = new BigDecimal("0");
		BigDecimal value = ZERO;
		value = new BigDecimal (objectToValidate);
		if(value.compareTo(ZERO) <= 0 )
			throw new Exception ("not greater then zero");
		return value;

	}
}

