package com.manulife.pension.ps.service.report.feeSchedule.util;

import com.manulife.pension.ps.service.report.feeSchedule.valueobject.TPAFeeScheduleContract;

/**
 * invoked when sort by user name
 * 
 * @author Siby Thomas
 * 
 */
public class TPAFeeScheduleContractNumberComparator extends TPAFeeScheduleContractNameComparator {

	public TPAFeeScheduleContractNumberComparator() {
		super();
	}

	@Override
	public int compare(TPAFeeScheduleContract arg0, TPAFeeScheduleContract arg1) {

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