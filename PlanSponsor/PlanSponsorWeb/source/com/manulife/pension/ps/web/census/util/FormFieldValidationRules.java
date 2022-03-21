package com.manulife.pension.ps.web.census.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.employee.util.EmployeeData.Property;
import com.manulife.pension.validator.ValidationError;

public abstract class FormFieldValidationRules implements
		FormFieldValidationRule {

	protected abstract List<FormFieldValidationRule> getRules();

	public boolean validate(FormFieldValidationRuleSupportForm form,
			List<ValidationError> errors) {
		boolean noError = true;
		for (FormFieldValidationRule rule : getRules()) {
			noError &= rule.validate(form, errors);
		}
		return noError;
	}

	static public class SSNRule extends AbstractFieldValidationRule {
		private RequiredFieldRule requiredRule;

		public SSNRule(String fieldName) {
			requiredRule = new RequiredFieldRule(fieldName,
					CensusErrorCodes.SSNRequired);
		}

		public SSNRule(Property field) {
			this(field.getName());
		}

		public boolean validate(FormFieldValidationRuleSupportForm form,
				List<ValidationError> errors) {
			if (!requiredRule.validate(form, errors)) {
				return false;
			}
			return true;
		}
	}

	static public class MaxFieldLengthRule extends
			AbstractFieldValueValidationRule {
		private int maxLength;

		public MaxFieldLengthRule(String fieldName, int errorCode, int maxLength) {
			super(fieldName, errorCode);
			this.maxLength = maxLength;
		}

		public boolean isValid(String value) {
			return StringUtils.isEmpty(value) || value.length() <= maxLength;
		}
	}

	static public class ValidStringFieldRule extends
			AbstractFieldValueValidationRule {
        public ValidStringFieldRule(String fieldName, int errorCode) {
            super(fieldName, errorCode);
        }

        public ValidStringFieldRule(String fieldName, int errorCode, String fieldLabel) {
			super(fieldName, errorCode, new Object[] {fieldLabel} );
		}

		public ValidStringFieldRule(Property field, int errorCode) {
			super(field, errorCode);
		}

		public boolean isValid(String value) {
			return StringUtils.isEmpty(value)
            || StringUtils.isAsciiPrintable(value);
		}
	}

	static public class RequiredFieldRule extends
			AbstractFieldValueValidationRule {
		public RequiredFieldRule(String fieldName, int errorCode) {
			super(fieldName, errorCode);
		}

		public RequiredFieldRule(Property field, int errorCode) {
			super(field, errorCode);
		}

		public boolean isValid(String value) {
			return !StringUtils.isEmpty(value);
		}
	}

	static public class FieldTypeRule extends AbstractFieldValidationRule {
		private String fieldName;

		private int errorCode;

        private Object[] params;
        
		public FieldTypeRule(Property field, int errorCode) {
			this(field.getName(), errorCode);
		}

		public FieldTypeRule(String fieldName, int errorCode) {
            this(fieldName, errorCode, null);
		}

        public FieldTypeRule(String fieldName, int errorCode, String fieldLabel) {
            this.fieldName = fieldName;
            this.errorCode = errorCode;
            this.params = fieldLabel == null ? null : new Object[] {fieldLabel};
        }

		public boolean validate(FormFieldValidationRuleSupportForm form,
				List<ValidationError> errors) {
			if (form.isFieldValueValidType(fieldName)) {
				return true;
			} else {
				addValidationError(errors, fieldName, this.errorCode, params);
				return false;
			}
		}
	}
}
