package com.manulife.pension.ps.web.census.util;

import java.util.List;

import com.manulife.pension.validator.ValidationError;

public interface FormFieldValidationRule {
    boolean validate(FormFieldValidationRuleSupportForm form, List<ValidationError> errors);
}
