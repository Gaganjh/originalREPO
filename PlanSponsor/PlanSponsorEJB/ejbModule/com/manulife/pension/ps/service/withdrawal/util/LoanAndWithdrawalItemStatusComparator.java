/**
 * Created on August 29, 2006
 */
		
package com.manulife.pension.ps.service.withdrawal.util;

import com.manulife.pension.ps.service.withdrawal.valueobject.LoanAndWithdrawalItem;


/**
 * Comparator implementation by status
 * Sorts by request status and request date
 * 
 * @author Mihai Popa
 */
public class LoanAndWithdrawalItemStatusComparator extends LoanAndWithdrawalItemComparator {

	public LoanAndWithdrawalItemStatusComparator() {
		super();
	}

	public int compare(Object arg0, Object arg1) {

		
		String status0 = null;
		String status1 = null;

		if (arg0 != null) {
			status0 = ((LoanAndWithdrawalItem)(arg0)).getStatus();
		}
		if (arg1 != null) {
			status1 = ((LoanAndWithdrawalItem)(arg1)).getStatus();
		}
		
		if (status0 == null) {
			status0 = EMPTY_STRING;
		}
		if (status1 == null) {
			status1 = EMPTY_STRING;
		}
		int result = (isAscending() ? 1 : -1) * status0.compareTo(status1);
		
		if (result == 0) {
			result = new LoanAndWithdrawalItemRequestDateSimpleComparator(isAscending()).
							compare(arg0, arg1);
		}							
		
		return result;
	}
}