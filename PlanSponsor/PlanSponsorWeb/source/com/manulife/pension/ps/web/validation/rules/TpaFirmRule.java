package com.manulife.pension.ps.web.validation.rules;

import java.util.Comparator;

import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.NumericRule;
import com.manulife.pension.platform.web.validation.rules.generic.UniqueElementRule;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.profiles.TpaFirm;
import com.manulife.pension.util.Pair;
import com.manulife.pension.validator.Validateable;
import com.manulife.pension.validator.ValidationRuleSet;

/**
 * Validate a TPA firm ID. It contains two rules: <p/>
 * <ul>
 * <li>Manadatory
 * <li>Must not already exists in the list of TPA firms.
 * </ul>
 * <p>
 * It expects a Pair to be passed into the validate method.
 * 
 * @author Charles Chan
 */
public class TpaFirmRule extends ValidationRuleSet {

	private static final TpaFirmRule instance = new TpaFirmRule();

	private class CompareTpaFirmId implements Comparator {
		public int compare(Object o1, Object o2) {
			TpaFirm firm = (TpaFirm) o1;
			String firmId = (String) o2;
			if (!firm.isRemoved()) {
				return firm.getId().compareTo(Integer.valueOf(firmId));
			} else {
				return -1;
			}
		}
	}

	/**
	 * Constructor.
	 */
	private TpaFirmRule() {
		super();
		/*
		 * MPR 116. System must confirm that Mandatory Data has been entered.
		 * Mandatory data on the Create TPA form is First Name, Last Name, eMail ,
		 * SSN and at least one TPA firm ID.
		 */
		addRule(new MandatoryRule(ErrorCodes.TPA_FIRM_ID_MANDATORY), true);

		addRule(new NumericRule(ErrorCodes.TPA_FIRM_ID_INVALID, 5), true);

		/*
		 * MPR 247. System must validate that TPA firm has not been selected for
		 * this profile already, or display error message and move cursor to TPA
		 * firm input box.
		 */
		addRule(new UniqueElementRule(ErrorCodes.TPA_FIRM_ID_NOT_UNIQUE,
				new CompareTpaFirmId()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.validation.ValidationRuleSet#getObjectToValidate(com.manulife.pension.ps.web.validation.Validateable,
	 *      java.lang.Object)
	 */
	protected Object getObjectToValidate(Validateable validateable, Object obj) {
		if (validateable instanceof MandatoryRule
				|| validateable instanceof NumericRule) {
			return ((Pair) obj).getSecond();
		} else {
			return obj;
		}
	}

	public static final TpaFirmRule getInstance() {
		return instance;
	}
}