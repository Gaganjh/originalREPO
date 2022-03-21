package com.manulife.pension.platform.web.validation.rules.generic;

import java.util.Collection;

import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationRule;

/**
 * @author Maria Lee
 */
public class InvalidBeginOrEndWithPeriodRule extends ValidationRule {

	/**
	 * Constructor.
	 */
	public InvalidBeginOrEndWithPeriodRule(int errorCode) {
		super(errorCode);
	}	

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.validation.Validateable#validate(java.lang.String,
	 *      java.util.List, java.lang.Object)
	 */
	public boolean validate(String[] fieldIds, Collection validationErrors,
			Object objectToValidate) {		
		
		boolean isValid = true;
		String charString = null;
		
		if (objectToValidate == null) { 
			isValid = false;
		} else {	
			// no period at either the beginning or the end
			charString = (String) objectToValidate;
			if (charString.substring(0, 1).equals( ".")) 
				isValid = false;
			else if (charString.substring(charString.length() -1).equals(".")) 
				       isValid = false;
		}
			
		if (!isValid) {
			validationErrors.add(new ValidationError(fieldIds, getErrorCode()));
			return false;
		}
		return true;
	}
}