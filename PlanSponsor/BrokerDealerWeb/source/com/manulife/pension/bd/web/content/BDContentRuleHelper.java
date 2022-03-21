package com.manulife.pension.bd.web.content;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.service.security.bd.valueobject.ContentFirmRestrictionRuleImpl.RuleType;

/**
 * Helper class for ContentRule classes
 * 
 * @author Ilamparithi
 * 
 */
public class BDContentRuleHelper {

    /**
     * This method forms a Set of firm ids from a comma separated string of firm ids.
     * 
     * @param firmListStr
     * @return
     */
    public static Set<Long> getFirmIdsSet(String firmListStr) {
        String[] idStrList = StringUtils.split(firmListStr, ',');
        Set<Long> brokerDealerFirmIds = new TreeSet<Long>();
        for (int i = 0; i < idStrList.length; i++) {
            brokerDealerFirmIds.add(Long.parseLong(idStrList[i]));
        }
        return brokerDealerFirmIds;
    }

    /**
     * This method returns the RuleType String for display
     * 
     * @param ruleType
     * @return String a String representation of RuleType
     */
    public static String getRuleTypeString(RuleType ruleType) {
        String ruleTypeStr = null;
        if (ruleType.compareTo(RuleType.Inclusion) == 0) {
            ruleTypeStr = BDConstants.INCLUDE_RULE;
        } else if (ruleType.compareTo(RuleType.Exclusion) == 0) {
            ruleTypeStr = BDConstants.EXCLUE_RULE;
        }
        return ruleTypeStr;
    }

    /**
     * This method returns the RuleType object for the given string
     * 
     * @param ruleTypeStr
     * @return RuleType
     */
    public static RuleType getRuleType(String ruleTypeStr) {
        RuleType ruleType = null;
        if (BDConstants.INCLUDE_RULE.equals(ruleTypeStr)) {
            ruleType = RuleType.Inclusion;
        } else if (BDConstants.EXCLUE_RULE.equals(ruleTypeStr)) {
            ruleType = RuleType.Exclusion;
        }
        return ruleType;
    }
}
