/**
 * Created on August 29, 2006
 */
		
package com.manulife.pension.ps.service.withdrawal.util;

import com.manulife.pension.ps.service.withdrawal.valueobject.LoanAndWithdrawalItem;


/**
 * Comparator implementation by contract name
 * Sorts by contract name and request date
 * 
 * @author Mihai Popa
 */
public class LoanAndWithdrawalItemContractNameComparator extends LoanAndWithdrawalItemComparator {

	public LoanAndWithdrawalItemContractNameComparator() {
		super();
	}

	public int compare(Object arg0, Object arg1) {

		
		String contractName0 = null;
		String contractName1 = null;

		if (arg0 != null) {
			contractName0 = ((LoanAndWithdrawalItem)(arg0)).getContractName();
		}
		if (arg1 != null) {
			contractName1 = ((LoanAndWithdrawalItem)(arg1)).getContractName();
		}
		
		if (contractName0 == null) {
			contractName0 = EMPTY_STRING;
		}
		if (contractName1 == null) {
			contractName1 = EMPTY_STRING;
		}
		int result = (isAscending() ? 1 : -1) * contractName0.compareTo(contractName1);
		
		if (result == 0) {
			result = new LoanAndWithdrawalItemRequestDateSimpleComparator(isAscending()).
							compare(arg0, arg1);
		}							
		return result;
	}
}