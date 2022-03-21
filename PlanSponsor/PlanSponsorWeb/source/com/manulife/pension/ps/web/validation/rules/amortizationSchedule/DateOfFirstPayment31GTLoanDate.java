/*
 * Created on Aug 29, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.web.validation.rules.amortizationSchedule;

/**
 * @author sternlu
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import com.manulife.pension.util.Pair;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationRule;

/**
 * @author sternlu
 * User needs to enter Date of First Payment for Loan.    
 * The Date of First Payment needs to be an older date than the date of the loan
 */
public class DateOfFirstPayment31GTLoanDate extends ValidationRule {

	public DateOfFirstPayment31GTLoanDate(int errorCode) {
		super(errorCode);
	}

	public boolean validate(String[] fieldIds, Collection validationErrors,
			Object objectToValidate) {
		boolean validate = true;
		if (objectToValidate == null)
			validate = false;
		else {
			Pair pair = (Pair) objectToValidate;
			try {
				Date dateOfLoan = (Date) pair.getFirst();				
				Date firstPaymentDate = (Date) pair.getSecond();				
				Calendar testCal = Calendar.getInstance();
				testCal.setTime(dateOfLoan);
				testCal.add(Calendar.DAY_OF_MONTH,31);
				Date testDate = testCal.getTime();
				
			
				if (firstPaymentDate.after(testDate))
					validate = false;
			} catch (Exception exception) {
				validate = false;
			}
		}
		if (!validate)
			validationErrors.add(new ValidationError(fieldIds, getErrorCode()));
		return validate;
	}
}