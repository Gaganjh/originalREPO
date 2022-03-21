/**
 * 
 * @ author kuthiha
 * Oct 17, 2006
 */
package com.manulife.pension.ps.web.validation.rules;

import com.manulife.pension.platform.web.validation.rules.generic.DateFormatRule;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.validator.ValidationRuleSet;

/**
 * @author kuthiha
 *
 */
public class DateRule extends ValidationRuleSet {
    
    private static final DateRule instance = new DateRule();

    /**
     * Constructor.
     */
    public DateRule() {
        super();
           addRule(new DateFormatRule(ErrorCodes.INVALID_DATE));
    }

    public static final DateRule getInstance() {
        return instance;
    }

}
