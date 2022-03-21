package com.manulife.pension.ps.web.validation.rules.loanRequest;

import java.math.BigDecimal;
import java.util.Collection;

import com.manulife.pension.validator.ValidationError;

/**
 * @author sternlu
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InterestRatePctRule {
	private String saveOnlyInd;
	private int errorCode;
	private static final String SAVE_ONLY_YES = "Y";
	private static final BigDecimal ZERO = new BigDecimal("0");
	private static final BigDecimal MIN_INTEREST_RATE = new BigDecimal("0.001");
	private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");
	
	public InterestRatePctRule(int errorCode, String saveOnlyInd) {
		this.errorCode = errorCode;
		this.saveOnlyInd = saveOnlyInd;
	}
	public boolean validate(String fieldId, Collection validationErrors,
			Object objectToValidate) {
		boolean validate = true;
		String loanInterestRatePct = (String)objectToValidate;
		if (SAVE_ONLY_YES.equalsIgnoreCase(saveOnlyInd)
				&& (loanInterestRatePct == null || loanInterestRatePct.trim().length() == 0)) {
			return true;
		}
		try {
			BigDecimal amount = new BigDecimal(loanInterestRatePct);
			// LS Aug 8 , 2005 allow zero amount for save only
			if (SAVE_ONLY_YES.equalsIgnoreCase(saveOnlyInd)&&amount.compareTo(ZERO) == 0)
				return true;
			if (amount.compareTo(MIN_INTEREST_RATE) < 0
					|| amount.compareTo(ONE_HUNDRED) > 0) {
				
				validate = false;
			}

		} catch (Exception e) {
			
			validate = false;
		}
		if (!validate)
			validationErrors.add(new ValidationError (new String[] { fieldId }, errorCode));
		return validate;
	}

}
