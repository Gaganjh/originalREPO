package com.manulife.pension.bd.web.validation.rules;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.registration.util.TaxId;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.validator.Validateable;
import com.manulife.pension.validator.ValidationRuleSet;

/**
 * This is a rule class to validate Tax Id.
 * 
 * @author Ilamparithi
 * 
 */
public class TaxIdRule extends ValidationRuleSet {

    private static final TaxIdRule instance = new TaxIdRule();

    /**
     * Constructor.
     */
    private TaxIdRule() {
        super();
        addRule(new RegularExpressionRule(BDErrorCodes.INVALID_TAX_ID, BDRuleConstants.TAX_ID_RE),
                true);
    }

    /**
     * This method validates the TaxId object and populates the Collection with error if any.
     * 
     * @return boolean a boolean value to indicate whether the validation is passed or not
     */
    protected Object getObjectToValidate(Validateable validateable, Object obj) {
        /*
         * TaxId is a complex object with 3 groups of digits, the rules cannot look inside the TaxId
         * object to determine if the digits are all empty.
         */
        if (obj instanceof TaxId) {
            TaxId taxId = (TaxId) obj;
            if (StringUtils.isEmpty(taxId.getValue())) {
                return null;
            } else {
                return taxId.getValue();
            }
        }
        return obj;
    }

    /**
     * Returns a TaxIdRule instance
     * 
     * @return TaxIdRule
     */
    public static final TaxIdRule getInstance() {
        return instance;
    }

}
