package com.manulife.pension.ps.web.validation.rules;

import java.util.Comparator;

import com.manulife.pension.platform.web.validation.rules.ContractNumberRule;
import com.manulife.pension.platform.web.validation.rules.generic.UniqueElementRule;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.profiles.BaseContractAccess;
import com.manulife.pension.ps.web.profiles.ContractAccess;
import com.manulife.pension.util.Pair;
import com.manulife.pension.validator.Validateable;
import com.manulife.pension.validator.ValidationRuleSet;

/**
 * @author Charles Chan
 */
public class AddContractNumberRule extends ValidationRuleSet {

	private static final AddContractNumberRule instance = new AddContractNumberRule();

	private class CompareContractNumber implements Comparator {
		public int compare(Object o1, Object o2) {
			BaseContractAccess access = (BaseContractAccess) o1;
			String contractNumber = (String) o2;
			return access.getContractNumber().compareTo(
					Integer.valueOf(contractNumber));
		}
	}

	/**
	 * Constructor.
	 */
	public AddContractNumberRule() {
		super();

		addRule(ContractNumberRule.getInstance(), true);

		addRule(new UniqueElementRule(ErrorCodes.CONTRACT_NUMBER_NOT_UNIQUE,
				new CompareContractNumber()));
	}

	/**
	 * @see com.manulife.pension.ps.web.validation.ValidationRuleSet#getObjectToValidate(com.manulife.pension.ps.web.validation.Validateable,
	 *      java.lang.Object)
	 */
	protected Object getObjectToValidate(Validateable validateable, Object obj) {
		if (validateable instanceof ContractNumberRule) {
			return ((Pair) obj).getSecond();
		} else {
			return obj;
		}
	}

	public static final AddContractNumberRule getInstance() {
		return instance;
	}
}