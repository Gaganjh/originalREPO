package com.manulife.pension.ps.web.census.util;

import java.util.List;

import com.manulife.pension.service.employee.util.EmployeeData.Property;
import com.manulife.pension.validator.ValidationError;

abstract class AbstractFieldValueValidationRule extends AbstractFieldValidationRule {
    private String fieldName;

    private int errorCode;

    private Object[] params;
    
    public AbstractFieldValueValidationRule(String fieldName) {
        this(fieldName, 0);
    }
    
    public AbstractFieldValueValidationRule(Property field, int errorCode) {
        this(field.getName(), errorCode);
    }

    public AbstractFieldValueValidationRule(String fieldName, int errorCode) {
        this(fieldName, errorCode, null);
    }
    
    public AbstractFieldValueValidationRule(String fieldName, int errorCode, Object[] params) {
        this.fieldName = fieldName;
        this.errorCode = errorCode;
        this.params = params;
    }
    
    abstract protected boolean isValid(String value);

    public boolean validate(FormFieldValidationRuleSupportForm form,
            List<ValidationError> errors) {
        boolean result = isValid(form.getValue(fieldName));
        if (!result) {
            addValidationError(errors, this.fieldName, this.errorCode, params);
        }
        return result;
    }
}