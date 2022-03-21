package com.manulife.pension.ps.service.report.feeSchedule.util;


import com.manulife.pension.ps.service.report.feeSchedule.valueobject.TPAFeeScheduleContract;

/**
 * 
 * Comparator to sort by date
 * 
 * @author Siby Thomas
 *
 */
public class TPAFeeScheduleContractNameComparator extends TPAFeeScheduleContractComparator {

	public TPAFeeScheduleContractNameComparator() {
		super();
	}

	@Override
	public int compare(TPAFeeScheduleContract arg0, TPAFeeScheduleContract arg1) {
		String name0 = arg0.getContractName();
		String name1 = arg1.getContractName();
		return (isAscending() ? 1 : -1) * name0.compareToIgnoreCase(name1);
	}
	
	public int compare(TPAFeeScheduleContract arg0, TPAFeeScheduleContract arg1, boolean ascending) {
		String name0 = arg0.getContractName();
		String name1 = arg1.getContractName();
		return name0.compareToIgnoreCase(name1);
	}
}