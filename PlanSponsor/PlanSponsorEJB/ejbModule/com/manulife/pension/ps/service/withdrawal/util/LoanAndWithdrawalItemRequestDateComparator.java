/**
 * Created on August 23, 2006
 */

package com.manulife.pension.ps.service.withdrawal.util;

import java.util.Date;

import com.manulife.pension.ps.service.withdrawal.valueobject.LoanAndWithdrawalItem;

/**
 * Comparator implementation by request date.
 * Sorts by request date, then by last name then by first name
 * 
 * @author Mihai Popa
 */
public class LoanAndWithdrawalItemRequestDateComparator extends LoanAndWithdrawalItemComparator {

	public LoanAndWithdrawalItemRequestDateComparator() {
		super();
	}
	
	public LoanAndWithdrawalItemRequestDateComparator(boolean ascending) {
		super(ascending);
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
		
		if (result == 0) {
			result = new LoanAndWithdrawalItemParticipantNameComparator(isAscending()).
							compare(arg0, arg1);
		}		
		
		return result;
	}
}