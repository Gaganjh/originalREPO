/**
 * Created on August 29, 2006
 */
		
package com.manulife.pension.ps.service.withdrawal.util;

import com.manulife.pension.ps.service.withdrawal.valueobject.LoanAndWithdrawalItem;


/**
 * Comparator implementation by participant name.
 * Sorts by last name then by first name then by request date
 * 
 * @author Mihai Popa
 */
public class LoanAndWithdrawalItemParticipantNameComparator extends LoanAndWithdrawalItemComparator {

	public LoanAndWithdrawalItemParticipantNameComparator() {
		super();
	}

	public LoanAndWithdrawalItemParticipantNameComparator(boolean ascending) {
		super(ascending);
	}
	
	public int compare(Object arg0, Object arg1) {

		String lastName0 = null;
		String lastName1 = null;
		
		String firstName0 = null;
		String firstName1 = null;
		

		if (arg0 != null) {
			lastName0 = ((LoanAndWithdrawalItem)(arg0)).getLastName();
			firstName0 = ((LoanAndWithdrawalItem)(arg0)).getFirstName();
		}
		if (arg1 != null) {
			lastName1 = ((LoanAndWithdrawalItem)(arg1)).getLastName();
			firstName1 = ((LoanAndWithdrawalItem)(arg1)).getFirstName();
		}
		
		//first sort by last name
		if (lastName0 == null) {
			lastName0 = EMPTY_STRING;
		}
		if (lastName1 == null) {
			lastName1 = EMPTY_STRING;
		}
		int result = (isAscending() ? 1 : -1) * lastName0.compareTo(lastName1);

		//if equal sort by first name
		if(result == 0) {
			if (firstName0 == null) {
				firstName0 = EMPTY_STRING;
			}
			if (firstName1 == null) {
				firstName1 = EMPTY_STRING;
			}
			result = (isAscending() ? 1 : -1) * firstName0.compareTo(firstName1);
			
			//if still equal sort by  request Date (simple version)
			if (result == 0) {
				result = new LoanAndWithdrawalItemRequestDateSimpleComparator(isAscending()).
								compare(arg0, arg1);
			}					
		}
		return result;
	}
}