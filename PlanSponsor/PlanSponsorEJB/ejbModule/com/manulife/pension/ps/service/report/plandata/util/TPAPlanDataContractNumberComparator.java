package com.manulife.pension.ps.service.report.plandata.util;

import com.manulife.pension.ps.service.report.plandata.valueobject.TPAPlanDataContract;

/**
 * invoked when sort by user name
 * 
 * @author Dheepa Poongol
 * 
 */
public class TPAPlanDataContractNumberComparator extends TPAPlanDataContractNameComparator {

	public TPAPlanDataContractNumberComparator() {
		super();
	}

	@Override
	public int compare(TPAPlanDataContract arg0, TPAPlanDataContract arg1) {

		Integer contract0 = arg0.getContractId();
		Integer contract1 = arg1.getContractId();

		int result = (isAscending() ? 1 : -1)
				* contract0.compareTo(contract1);

		if (result == 0) {
			result = super.compare(arg0, arg1, true);
		}

		return result;
	}
}