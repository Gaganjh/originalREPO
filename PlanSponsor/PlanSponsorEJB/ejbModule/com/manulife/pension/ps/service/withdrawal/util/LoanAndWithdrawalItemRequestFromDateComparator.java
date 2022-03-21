/**
 * Created on August 23, 2006
 */

package com.manulife.pension.ps.service.withdrawal.util;

import java.util.Date;

import com.manulife.pension.ps.service.withdrawal.valueobject.LoanAndWithdrawalItem;

/**
 * Comparator implementation by request date.
 * 
 * @author Mihai Popa
 */
public class LoanAndWithdrawalItemRequestFromDateComparator extends LoanAndWithdrawalItemComparator {

	public LoanAndWithdrawalItemRequestFromDateComparator() {
		super();
	}

	public int compare(Object arg0, Object arg1) {

		Date date0 = null;
		Date date1 = null;
		
		if (arg0 != null) {
			date0 = ((LoanAndWithdrawalItem)(arg0)).getRequestFromDate();
		}
		if (arg1 != null) {
			date1 = ((LoanAndWithdrawalItem)(arg1)).getRequestFromDate();
		}
		
		if (date0 == null) {
			date0 = MIN_DATE;
		}
		if (date1 == null) {
			date1 = MIN_DATE;
		}

		int multiplier = isAscending() ? 1 : -1;
		int result = multiplier * date0.compareTo(date1);
		return result;
	}
}