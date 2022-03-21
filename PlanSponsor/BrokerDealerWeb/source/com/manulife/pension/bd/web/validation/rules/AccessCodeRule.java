package com.manulife.pension.bd.web.validation.rules;

import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.validator.ValidationRuleSet;

/**
 * This is a rule class for Access Code.
 * 
 * @author Ilamparithi
 * 
 */
public class AccessCodeRule extends ValidationRuleSet {

    private static final AccessCodeRule instance = new AccessCodeRule();

    /**
     * Constructor.
     */
    private AccessCodeRule() {
        super();
        addRule(new MandatoryRule(BDErrorCodes.MISSING_ACCESS_CODE), true);
        addRule(new RegularExpressionRule(BDErrorCodes.INVALID_ACCESS_CODE,
                BDRuleConstants.ACCESS_CODE_RE), true);
    }

    /**
     * Returns an AccessCodeRule instance
     * 
     * @return AccessCodeRule
     */
    public static final AccessCodeRule getInstance() {
        return instance;
    }

}
