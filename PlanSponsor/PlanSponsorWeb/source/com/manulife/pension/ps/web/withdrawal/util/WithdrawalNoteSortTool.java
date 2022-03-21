package com.manulife.pension.ps.web.withdrawal.util;

import com.manulife.pension.ps.web.util.sort.LongSortTool;
import com.manulife.pension.ps.web.util.sort.SortTool;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestNote;

/**
 * The sort tool for WithdrawalNoteSort.
 * 
 * @author Kristin Kerr
 */
public class WithdrawalNoteSortTool implements SortTool {
    
    private static LongSortTool longSortTool = new LongSortTool();
    
    public int compare(Object first, Object second) {
        int result = 0;
        if (first == null && second == null) {
            result = 0;
        } else if (first == null && second != null) {
            result = -1;
        } else if (first != null && second == null) {
            result = 1;
        } else {
            result = compareWithdrawalNotes(first, second);
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
     * Compare Withdrawal notes by date.
     */
    private int compareWithdrawalNotes(Object first, Object second) {
        WithdrawalRequestNote note1 = (WithdrawalRequestNote)first;
        WithdrawalRequestNote note2 = (WithdrawalRequestNote)second;
        Long date1 = new Long(note1.getCreated().getTime());
        Long date2 = new Long(note2.getCreated().getTime());
        return longSortTool.compare(date1, date2);
    }

}
