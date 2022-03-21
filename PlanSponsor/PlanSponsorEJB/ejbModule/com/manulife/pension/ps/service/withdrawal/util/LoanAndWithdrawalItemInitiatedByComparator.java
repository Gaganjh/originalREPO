/**
 * Created on August 29, 2006
 */
		
package com.manulife.pension.ps.service.withdrawal.util;

import com.manulife.pension.ps.service.withdrawal.valueobject.LoanAndWithdrawalItem;


/**
 * Comparator implementation by initiated by field
 * Sorts by initiated by and request date
 * 
 * @author Mihai Popa
 */
public class LoanAndWithdrawalItemInitiatedByComparator extends LoanAndWithdrawalItemComparator {

	public LoanAndWithdrawalItemInitiatedByComparator() {
		super();
	}

	public int compare(Object arg0, Object arg1) {

		
		String initiatedBy0 = null;
		String initiatedBy1 = null;

		if (arg0 != null) {
			initiatedBy0 = ((LoanAndWithdrawalItem)(arg0)).getInitiatedBy();
		}
		if (arg1 != null) {
			initiatedBy1 = ((LoanAndWithdrawalItem)(arg1)).getInitiatedBy();
		}
		
		if (initiatedBy0 == null) {
			initiatedBy0 = EMPTY_STRING;
		}
		if (initiatedBy1 == null) {
			initiatedBy1 = EMPTY_STRING;
		}
		int result = (isAscending() ? 1 : -1) * initiatedBy0.compareTo(initiatedBy1);
		
		if (result == 0) {
			result = new LoanAndWithdrawalItemRequestDateSimpleComparator(isAscending()).
							compare(arg0, arg1);
		}							
		
		return result;
	}
}