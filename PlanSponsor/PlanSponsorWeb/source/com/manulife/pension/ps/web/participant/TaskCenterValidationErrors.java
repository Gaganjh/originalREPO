package com.manulife.pension.ps.web.participant;

import java.util.List;

import com.manulife.pension.ps.web.census.util.AbstractValidationErrors;
import com.manulife.pension.validator.ValidationError;

public class TaskCenterValidationErrors extends AbstractValidationErrors {

	public TaskCenterValidationErrors(List<ValidationError> errors) {
		setErrors(errors);
	}
	
}
