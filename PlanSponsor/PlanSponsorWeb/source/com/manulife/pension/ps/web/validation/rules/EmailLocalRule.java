package com.manulife.pension.ps.web.validation.rules;

import java.util.Collection;

import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationRule;

/**
 * Validate MPR.590
 * Email address must "@jhancock.com" or "@manulife"
 * 
 * @author marcest
 * Created Jan 29, 2007
 */
public class EmailLocalRule extends ValidationRule {

	private static final EmailLocalRule instance = new EmailLocalRule();

	public static final EmailLocalRule getInstance() {
		return instance;
	}
	
	
	/**
	 * @param errorCode
	 */
	public EmailLocalRule (int errorCode) {
		super( errorCode );
	}
	
	public EmailLocalRule () {
		this( ErrorCodes.EMAIL_MUST_BE_INTERNAL ); 
	}
	
	/** 
	 * 
	 * @see com.manulife.pension.validation.Validateable#validate(java.lang.String[], java.util.Collection, java.lang.Object)
	 */
	public boolean validate(String[] fieldIds, Collection validationErrors, Object objectToValidate) {
		String email = objectToValidate.toString();
		if (!isEmailLocal(email)) {
			validationErrors.add(new ValidationError(fieldIds, getErrorCode()));
            return false;
		}
		return true;
	}

	/**
	 * @param email
	 * @return
	 */
	protected boolean isEmailLocal(String email) {
		if (email == null)
			return false;
		return ((email.indexOf("@jhancock") != -1) || (email.indexOf("@manulife") != -1) ||
				(email.indexOf("@jhrps") != -1));
	}
}
