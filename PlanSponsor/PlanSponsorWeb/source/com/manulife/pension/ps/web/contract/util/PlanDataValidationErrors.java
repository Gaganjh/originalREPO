package com.manulife.pension.ps.web.contract.util;

import java.util.List;

import com.manulife.pension.ps.web.census.util.AbstractValidationErrors;
import com.manulife.pension.validator.ValidationError;

public class PlanDataValidationErrors extends AbstractValidationErrors {
    private static final long serialVersionUID = 2297001787045950776L;

    public PlanDataValidationErrors(List<ValidationError> errors) {
        setErrors(errors);
    }    
}
