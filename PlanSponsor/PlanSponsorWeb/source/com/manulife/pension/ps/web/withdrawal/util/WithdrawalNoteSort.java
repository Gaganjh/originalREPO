package com.manulife.pension.ps.web.withdrawal.util;

import com.manulife.pension.ps.web.util.sort.QuickSorter;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestNote;


/**
 * A sort utility for Withdrawal notes.
 * 
 * @author Kristin Kerr
 */
public class WithdrawalNoteSort {

    private final QuickSorter sorter = new QuickSorter();

    public WithdrawalNoteSort() {
        super();
    }

    public WithdrawalRequestNote[] sortWithdrawalNotesArray(WithdrawalRequestNote[] withdrawalNotes)
        throws Throwable {
        
        if (withdrawalNotes != null && withdrawalNotes.length > 1) {
            WithdrawalNoteSortTool sortTool = new WithdrawalNoteSortTool();

            sorter.sortItems(withdrawalNotes, sortTool, true);
        }
        return withdrawalNotes;
    }
}