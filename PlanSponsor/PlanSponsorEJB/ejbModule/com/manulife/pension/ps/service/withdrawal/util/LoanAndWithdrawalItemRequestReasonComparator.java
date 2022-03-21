/**
 * Created on August 29, 2006
 */
		
package com.manulife.pension.ps.service.withdrawal.util;

import com.manulife.pension.ps.service.withdrawal.valueobject.LoanAndWithdrawalItem;


/**
 * Comparator implementation by request reason
 * 
 * @author Mihai Popa
 */
public class LoanAndWithdrawalItemRequestReasonComparator extends LoanAndWithdrawalItemComparator {

	public LoanAndWithdrawalItemRequestReasonComparator() {
		super();
	}

	public int compare(Object arg0, Object arg1) {

		
		String requestReason0 = null;
		String requestReason1 = null;

		if (arg0 != null) {
			requestReason0 = ((LoanAndWithdrawalItem)(arg0)).getRequestReason();
		}
		if (arg1 != null) {
			requestReason1 = ((LoanAndWithdrawalItem)(arg1)).getRequestReason();
		}
		
		if (requestReason0 == null) {
			requestReason0 = EMPTY_STRING;
		}
		if (requestReason1 == null) {
			requestReason1 = EMPTY_STRING;
		}
		int result = (isAscending() ? 1 : -1) * requestReason0.compareTo(requestReason1);
		
		// if they are equal, goto the secondary sort by request date same order as here 
		if (result == 0) {
			result = new LoanAndWithdrawalItemRequestDateComparator(isAscending()).
							compare(arg0, arg1);
		}		
		return result;
	}
}