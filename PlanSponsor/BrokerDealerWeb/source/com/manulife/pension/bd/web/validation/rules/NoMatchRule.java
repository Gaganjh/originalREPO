package com.manulife.pension.bd.web.validation.rules;

import java.util.Collection;

import com.manulife.pension.util.Pair;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationRule;

/**
 * This is a rule class to ensure that the two objects do not match.
 * 
 * @author Ilamparithi
 * 
 */
public class NoMatchRule extends ValidationRule {

    /**
     * Constructor
     * 
     * @param errorCode
     */
    public NoMatchRule(int errorCode) {
        super(errorCode);
    }

    /**
     * This method validates whether two objects are not same. If they are same it populates the
     * Collection with error
     * 
     * @return boolean a boolean value to indicate whether the validation is passed or not
     */
    @SuppressWarnings("unchecked")
    public boolean validate(String[] fieldIds, Collection validationErrors, Object objectToValidate) {
        boolean isValid = false;

        if (objectToValidate != null) {
            Pair pair = (Pair) objectToValidate;

            Object obj1 = pair.getFirst();
            Object obj2 = pair.getSecond();

            if (obj1 != null && obj2 != null) {
                if (!obj1.equals(obj2)) {
                    isValid = true;
                }
            }
        }

        if (!isValid) {
            validationErrors.add(new ValidationError(fieldIds, getErrorCode()));
        }
        return isValid;
    }

}
