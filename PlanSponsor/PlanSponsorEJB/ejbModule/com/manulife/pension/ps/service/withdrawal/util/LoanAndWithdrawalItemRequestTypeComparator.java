/**
 * Created on August 29, 2006
 */
		
package com.manulife.pension.ps.service.withdrawal.util;

import com.manulife.pension.ps.service.withdrawal.valueobject.LoanAndWithdrawalItem;


/**
 * Comparator implementation by request type
 * 
 * @author Mihai Popa
 */
public class LoanAndWithdrawalItemRequestTypeComparator extends LoanAndWithdrawalItemComparator {

	public LoanAndWithdrawalItemRequestTypeComparator() {
		super();
	}

	public int compare(Object arg0, Object arg1) {

		
		String requestType0 = null;
		String requestType1 = null;

		if (arg0 != null) {
			requestType0 = ((LoanAndWithdrawalItem)(arg0)).getRequestType();
		}
		if (arg1 != null) {
			requestType1 = ((LoanAndWithdrawalItem)(arg1)).getRequestType();
		}
		
		if (requestType0 == null) {
			requestType0 = EMPTY_STRING;
		}
		if (requestType1 == null) {
			requestType1 = EMPTY_STRING;
		}
		int result = (isAscending() ? 1 : -1) * requestType0.compareTo(requestType1);
		
		return result;
	}
}