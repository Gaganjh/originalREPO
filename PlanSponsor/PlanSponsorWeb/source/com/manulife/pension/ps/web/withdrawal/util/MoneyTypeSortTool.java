package com.manulife.pension.ps.web.withdrawal.util;

import com.manulife.pension.ps.web.util.sort.SortTool;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;

/**
 * The sort tool for MoneyTypeSort.
 * 
 * @author Kristin Kerr
 */
public class MoneyTypeSortTool implements SortTool {
    
    public int compare(Object first, Object second) {
        int result = 0;
        if (first == null && second == null) {
            result = 0;
        } else if (first == null && second != null) {
            result = -1;
        } else if (first != null && second == null) {
            result = 1;
        } else {
            result = compareMoneyTypes(first, second);
        }
        if (result == 0) {
            return SortTool.IS_EQUAL_TO;
        } else if (result > 0) {
            return SortTool.IS_GREATER_THAN;
        } else {
            return SortTool.IS_LESS_THAN;
        }       
    }
    
    /**
     * Compare money types by money type code, then by money type name.
     * Use the custom short name if available, otherwise use the Manulife short name.
     */
    private int compareMoneyTypes(Object first, Object second) {
        WithdrawalRequestMoneyType moneyType1 = (WithdrawalRequestMoneyType)first;
        WithdrawalRequestMoneyType moneyType2 = (WithdrawalRequestMoneyType)second;
        String moneyTypeName1 = moneyType1.getMoneyTypeAliasId() != null ? 
                moneyType1.getMoneyTypeAliasId() : 
                moneyType1.getMoneyTypeId();
        String moneyTypeName2 = moneyType2.getMoneyTypeAliasId() != null ? 
                moneyType2.getMoneyTypeAliasId() : 
                moneyType2.getMoneyTypeId();
        String nameToCompare1 = moneyType1.getMoneyTypeCategoryCode() + moneyTypeName1;
        String nameToCompare2 = moneyType2.getMoneyTypeCategoryCode() + moneyTypeName2;
        return compareString(nameToCompare1, nameToCompare2);
    }

    /**
     * Compare two strings.
     */
    public int compareString (Object first, Object second) {
        if ((first != null) && (second != null)
            && (first instanceof String) && (second instanceof String)) {
            int c = ((String)first).compareTo((String)second);

            if (c < 0)
                {return IS_LESS_THAN;}
            else if (c > 0)
                {return IS_GREATER_THAN;}
            else
                {return IS_EQUAL_TO;}
        } else {
            throw SortTool.INVALID_ARGS;
        }
    }
}
