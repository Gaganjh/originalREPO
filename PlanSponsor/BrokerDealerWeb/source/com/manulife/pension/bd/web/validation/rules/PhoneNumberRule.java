package com.manulife.pension.bd.web.validation.rules;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.registration.util.PhoneNumber;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.validator.Validateable;
import com.manulife.pension.validator.ValidationRuleSet;

/**
 * This is a rule class for PhoneNumber.
 * 
 * @author Ilamparithi
 * 
 */
public class PhoneNumberRule extends ValidationRuleSet {

    private static final PhoneNumberRule instance = new PhoneNumberRule();

    /**
     * Constructor.
     */
    private PhoneNumberRule() {
        super();
        addRule(new MandatoryRule(BDErrorCodes.MISSING_PHONE_NUMBER), true);
        addRule(new RegularExpressionRule(BDErrorCodes.INVALID_PHONE_NUMBER,
                BDRuleConstants.PHONE_NUMBER_RE), true);
    }

    /**
     * This method validates the PhoneNumber object and populates the Collection with error if any.
     * 
     * @return boolean a boolean value to indicate whether the validation is passed or not
     */
    protected Object getObjectToValidate(Validateable validateable, Object obj) {
        /*
         * PhoneNumber is a complex object with 3 groups of digits, the MandatoryRule &
         * RegularExpressionRule cannot look inside the PhoneNumber object to determine if the
         * digits are all empty.
         */
        if (obj instanceof PhoneNumber) {
            PhoneNumber phoneNumber = (PhoneNumber) obj;
            if (StringUtils.isEmpty(phoneNumber.getValue())) {
                return null;
            } else {
                return phoneNumber.getValue();
            }
        }
        return obj;
    }

    public static final PhoneNumberRule getInstance() {
        return instance;
    }

}
