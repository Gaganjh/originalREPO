package com.manulife.pension.ps.web.census.util;

import java.util.List;

import com.manulife.pension.validator.ValidationError;

abstract public class AbstractFieldValidationRule implements FormFieldValidationRule {
    protected void addValidationError(List<ValidationError> errors, String fieldName, int error,
            Object[] params) {
        for (ValidationError e : errors) {
            if (e.getErrorCode() == error && isSameParams(e.getParams(), params)) {
                e.getFieldIds().add(fieldName);
                return;
            }
        }
        errors.add(new ValidationError(fieldName, error, params));
    }

    private boolean isSameParams(Object[] params, Object[] params2) {
        if (params == null && params2 == null) {
            return true;
        }
        
        if (params != null && params2 != null) {
            if (params.length == params2.length) {
                for (int i = 0; i < params.length; i++) {
                    if (!params[i].equals(params2[i])) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
