/*
 * Created on May 30, 2005
 * validaets given String object against given maximum length
 */
package com.manulife.pension.platform.web.validation.rules.generic;

import java.util.Collection;

import com.manulife.pension.validator.ValidationError;

/**
 * @author sternlu
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RestrictedMaxLengthRule {
    private int errorCode;
	/**
	 * Constructor.
	 */
	public RestrictedMaxLengthRule(int errorCode) {
		this.errorCode= errorCode;

	}
	public boolean validate(String fieldId, Collection validationErrors,
			String objectToValidate, int maxSize) {

		boolean isValid = true;
		if (objectToValidate!= null)
        {	
        	if(objectToValidate.length()> maxSize)
        	{
        		isValid = false;
        		validationErrors.add(new ValidationError (new String[] { fieldId }, errorCode));
        	}
        	}
		return isValid;
		
	}

}
