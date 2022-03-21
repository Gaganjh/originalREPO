/**
 * Created on August 29, 2006
 */
		
package com.manulife.pension.ps.service.withdrawal.util;

import com.manulife.pension.ps.service.withdrawal.valueobject.LoanAndWithdrawalItem;


/**
 * Comparator implementation by contract number
 * Sorts by contract number and request date
 * 
 * @author Mihai Popa
 */
public class LoanAndWithdrawalItemContractNumberComparator extends LoanAndWithdrawalItemComparator {

	public LoanAndWithdrawalItemContractNumberComparator() {
		super();
	}

	public int compare(Object arg0, Object arg1) {

		
		Integer contractNumber0 = null;
		Integer contractNumber1 = null;

		if (arg0 != null) {
			contractNumber0 = ((LoanAndWithdrawalItem)(arg0)).getContractNumber();
		}
		if (arg1 != null) {
			contractNumber1 = ((LoanAndWithdrawalItem)(arg1)).getContractNumber();
		}
		
		if (contractNumber0 == null) {
			contractNumber0 = new Integer(0);
		}
		if (contractNumber1 == null) {
			contractNumber1 = new Integer(0);
		}
		int result = (isAscending() ? 1 : -1) * contractNumber0.compareTo(contractNumber1);
		
		if (result == 0) {
			result = new LoanAndWithdrawalItemRequestDateSimpleComparator(isAscending()).
							compare(arg0, arg1);
		}							
		
		return result;
	}
}