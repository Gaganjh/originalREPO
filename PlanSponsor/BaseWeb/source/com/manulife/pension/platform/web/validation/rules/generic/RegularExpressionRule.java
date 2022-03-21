package com.manulife.pension.platform.web.validation.rules.generic;

import gnu.regexp.RE;
import gnu.regexp.REException;
import gnu.regexp.RESyntax;

import java.util.Collection;

import org.apache.commons.lang.exception.NestableRuntimeException;

import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationRule;

/**
 * @author Charles Chan
 */
public class RegularExpressionRule extends ValidationRule {

	private RE re;

	/**
	 * Constructor.
	 */
	public RegularExpressionRule(int errorCode, String expression) {
		super(errorCode);
		try {
			/*
			 * Allow character class.
			 */
			RESyntax syntax = new RESyntax(RESyntax.RE_SYNTAX_PERL5);
			syntax.set(RESyntax.RE_CHAR_CLASSES);
			re = new RE(expression, 0, syntax);
		} catch (REException e) {
			throw new NestableRuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.validation.Validateable#validate(java.lang.String,
	 *      java.util.List, java.lang.Object)
	 */
	public boolean validate(String[] fieldIds, Collection validationErrors,
			Object objectToValidate) {
		if (!re.isMatch(objectToValidate)) {
			validationErrors.add(new ValidationError(fieldIds, getErrorCode()));
			return false;
		}
		return true;
	}
	
	public boolean validate(Object objectToValidate) {
        if (!re.isMatch(objectToValidate)) {
            return false;
        } 
        return true;
    }
}