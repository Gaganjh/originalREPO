package com.manulife.pension.bd.web.estatement.sort;

import com.manulife.pension.bd.web.estatement.RiaStatementVO;

/**
 * This is Comparator class for Contract Name used for Plan Review report pages.
 * 
 * @author NagaRaju
 * 
 */

public class StatementDateComparator extends BaseRiaStatementsReportComparator {

	public StatementDateComparator(String sortDirection) {
		super(sortDirection);
	}

	@Override
	public int compare(RiaStatementVO arg0, RiaStatementVO arg1) {		
		int multiplier = isAscending() ? 1 : -1;

		int result = multiplier
				* arg0.getGenDate().compareTo(arg1.getGenDate());
		
		if(result == 0){
			result = 1 * arg0.getFirmName().compareTo(arg1.getFirmName()); 
		}
		
		return result;
	}

}
