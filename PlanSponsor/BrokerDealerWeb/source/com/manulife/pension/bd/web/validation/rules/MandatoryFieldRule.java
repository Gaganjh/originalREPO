package com.manulife.pension.bd.web.validation.rules;

import java.util.Collection;

import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationRule;

/**
 * A mandatory validation rule.
 * 
 * @author Siby Thomas
 */
public class MandatoryFieldRule extends ValidationRule {

    /**
     * Constructor.
     */
    public MandatoryFieldRule(int errorCode) {
        super(errorCode);
    }

    /**
     * This method validates an object and populates the Collection with errors if any.
     * 
     * @return boolean a boolean value to indicate whether the validation is passed or not
     */
    @SuppressWarnings("unchecked")
    public boolean validate(String[] fieldIds, Collection validationErrors, Object objectToValidate) {

        boolean isValid = true;
        if (objectToValidate == null) {
            isValid = false;
        } else {
            if (objectToValidate instanceof String) {
                String stringToValidate = (String) objectToValidate;
                if (stringToValidate.trim().length() == 0) {
                    isValid = false;
                }
            }
        }
        if (!isValid) {
            validationErrors.add(new ValidationError(fieldIds, getErrorCode()));
        }
        return isValid;
    }
}