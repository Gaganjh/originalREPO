package com.manulife.pension.ps.web.census.util;

import java.util.List;

import com.manulife.pension.validator.ValidationError;

/**
 * Utility class used to display errors/warnings in Eligibility page
 * 
 * @author patuadr
 *
 */
public class EligibilityValidationErrors extends AbstractValidationErrors {

    private static final long serialVersionUID = -2963366092536764491L;

    public EligibilityValidationErrors(List<ValidationError> errors) {
        setErrors(errors);
    }    
}
