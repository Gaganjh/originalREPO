package com.manulife.pension.bd.web.validation.rules;

import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.validator.ValidationRuleSet;

/**
 * This is a rule class for Email Address.
 * 
 * @author Ilamparithi
 * 
 */
public class EmailAddressRule extends ValidationRuleSet {

    private static final EmailAddressRule instance = new EmailAddressRule();
    
    private static EmailAddressRule ruleInstance;

    /**
     * Constructor.
     */
    private EmailAddressRule() {
        super();
        addRule(new MandatoryRule(BDErrorCodes.EMAIL_MANDATORY), true);
        addRule(new RegularExpressionRule(BDErrorCodes.EMAIL_INVALID,
                BDRuleConstants.EMAIL_ADDRESS_RE), true);
    }

    /**
     * Constructor.
     * 
     * @param errorCodeEmailManadatory
     * @param errorCodeEmailInvalid
     */
    private EmailAddressRule(int errorCodeEmailManadatory, int errorCodeEmailInvalid) {
        super();
        addRule(new MandatoryRule(errorCodeEmailManadatory), true);
        addRule(new RegularExpressionRule(errorCodeEmailInvalid, BDRuleConstants.EMAIL_ADDRESS_RE),
                true);
    }

    /**
     * Returns a EmailAddressRule instance
     * 
     * @return EmailAddressRule
     */
    public static final EmailAddressRule getInstance() {
        return instance;
    }

    /**
     * Returns a EmailAddressRule instance
     * 
     * @param errorCodeEmailManadatory
     * @param errorCodeEmailInvalid
     * 
     * @return EmailAddressRule
     */
    public static final EmailAddressRule getInstance(int errorCodeEmailManadatory,
            int errorCodeEmailInvalid) {
        ruleInstance = new EmailAddressRule(errorCodeEmailManadatory, errorCodeEmailInvalid);
        return ruleInstance;
    }
}
