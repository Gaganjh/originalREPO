/**
 * Created on August 29, 2006
 */
		
package com.manulife.pension.ps.service.withdrawal.util;

import com.manulife.pension.ps.service.withdrawal.valueobject.LoanAndWithdrawalItem;


/**
 * Comparator implementation by reference number
 * 
 * @author Mihai Popa
 */
public class LoanAndWithdrawalItemReferenceNumberComparator extends LoanAndWithdrawalItemComparator {

	public LoanAndWithdrawalItemReferenceNumberComparator() {
		super();
	}

	public int compare(Object arg0, Object arg1) {

		
		Integer referenceNumber0 = null;
		Integer referenceNumber1 = null;

		if (arg0 != null) {
			referenceNumber0 = ((LoanAndWithdrawalItem)(arg0)).getReferenceNumber();
		}
		if (arg1 != null) {
			referenceNumber1 = ((LoanAndWithdrawalItem)(arg1)).getReferenceNumber();
		}
		
		if (referenceNumber0 == null) {
			referenceNumber0 = new Integer(0);
		}
		if (referenceNumber1 == null) {
			referenceNumber1 = new Integer(0);
		}
		int result = (isAscending() ? 1 : -1) * referenceNumber0.compareTo(referenceNumber1);
		
		return result;
	}
}