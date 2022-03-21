/*
 * Created on Sep 13, 2005
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
 * TODO To change the template for this generated type comment go to Window - Preferences - Java -
 * Code Style - Code Templates
 */
public class AmountRule extends ValidationRule {

    public AmountRule(int errorCode) {
        super(errorCode);
    }

    public boolean validate(String[] fieldIds, Collection validationErrors, Object objectToValidate) {
        boolean validate = true;
        if (objectToValidate == null)
            validate = false;
        else {
            try {
                BigDecimal amount = new BigDecimal((String) objectToValidate);
                if (amount.compareTo(new BigDecimal("0")) < 0) {
                    validate = false;
                } else if (amount.compareTo(new BigDecimal("999999999.99")) > 0) {
                    // - the database can only accept 9 whole, with 2 decimal places
                    validate = false;
                } 
            } catch (Exception e) {
                validate = false;
            }
        }
        if (!validate)
            validationErrors.add(new ValidationError(fieldIds, getErrorCode()));
        return validate;
    }

}
