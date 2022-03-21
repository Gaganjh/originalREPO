/*
 * Created on May 13, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.web.validation.rules.amortizationSchedule;

import java.util.Collection;
import java.util.Date;

import com.manulife.pension.ps.web.iloans.util.DateFormatter;
import com.manulife.pension.validator.ValidationError;

/**
 * @author sternlu
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DateRule {
	int errorCode;

	public DateRule(int errorCode)
	{
		this.errorCode = errorCode;
				
	}

	public Date validate(String fieldId, Collection validationErrors,
			String objectToValidate) 
	{
		Date date = null;
		boolean validated = true;
		try
		{
			date =DateFormatter.parse((String) objectToValidate);
		}
		catch(Exception exception)
		{
			validated = false;
		}
		if(!validated)
			validationErrors.add(new ValidationError(fieldId, errorCode));
		return date;
	}
}
