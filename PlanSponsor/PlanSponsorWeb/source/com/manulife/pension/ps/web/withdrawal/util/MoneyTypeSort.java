package com.manulife.pension.ps.web.withdrawal.util;

import com.manulife.pension.ps.web.util.sort.QuickSorter;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;


/**
 * A sort utility for Withdrawal money types.
 * 
 * @author Kristin Kerr
 */
public class MoneyTypeSort {

    private final QuickSorter sorter = new QuickSorter();

    public MoneyTypeSort() {
        super();
    }

    public WithdrawalRequestMoneyType[] sortMoneyTypesArray(WithdrawalRequestMoneyType[] moneyTypes)
        throws Throwable {
        
        if (moneyTypes != null && moneyTypes.length > 1) {
            MoneyTypeSortTool sortTool = new MoneyTypeSortTool();

            sorter.sortItems(moneyTypes, sortTool, false);
        }
        return moneyTypes;
    }
}
