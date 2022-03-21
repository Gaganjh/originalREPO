package com.manulife.pension.bd.web.validation.rules;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.registration.util.Ssn;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.validation.rules.generic.AtLeastOneNonZeroRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.validator.Validateable;
import com.manulife.pension.validator.ValidationRuleSet;

/**
 * This is a rule class for ssn.
 * 
 * @author Ilamparithi
 */
public class SsnRule extends ValidationRuleSet {

    private static final SsnRule instance = new SsnRule();

    /**
     * Constructor.
     */
    private SsnRule() {
        super();
        /*
         * MPR 226. System validates that when personal identification type is SSN that no
         * non-numeric values have been entered or that all “0” have not been entered, or displays
         * error and return cursor to SSN field .
         */
        addRule(new RegularExpressionRule(CommonErrorCodes.SSN_INVALID, BDRuleConstants.SSN_RE),
                true);

        addRule(new AtLeastOneNonZeroRule(CommonErrorCodes.SSN_INVALID));
    }

    /**
     * This method validates the Ssn object and populates the Collection with error if any.
     * 
     * @return boolean a boolean value to indicate whether the validation is passed or not
     */
    protected Object getObjectToValidate(Validateable validateable, Object obj) {
        /*
         * Ssn is a complex object with 3 groups of digits, the rules cannot look inside the Ssn
         * object to determine if the digits are all empty.
         */
        if (obj instanceof Ssn) {
            Ssn ssn = (Ssn) obj;
            if (StringUtils.isEmpty(ssn.getValue())) {
                return null;
            } else {
                return ssn.getValue();
            }
        }
        return obj;
    }

    /**
     * Returns a SsnRule instance
     * 
     * @return SsnRule
     */
    public static final SsnRule getInstance() {
        return instance;
    }
}