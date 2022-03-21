package com.manulife.pension.ps.service.report.feeSchedule.util;


import com.manulife.pension.ps.service.report.feeSchedule.valueobject.TPAFeeScheduleContract;

/**
 * invoked when sort by user name
 * 
 * @author Siby Thomas
 * 
 */
public class TPAFeeScheduleContractLastUpdatedUserComparator extends TPAFeeScheduleContractNameComparator {

	public TPAFeeScheduleContractLastUpdatedUserComparator() {
		super();
	}

	@Override
	public int compare(TPAFeeScheduleContract arg0, TPAFeeScheduleContract arg1) {

		String name0 = arg0.getCreatedUser();
		String name1 = arg1.getCreatedUser();

		int result = 0;
		int multiply = (isAscending() ? 1 : -1);
		
	    if(name0 == null && name1 == null) {
		   result = 0;
	    } else if (name0 == null) {
	    	result = -1;
	    }else if (name1 == null) {
	    	result = 1;
	    } else {
	    	result = name0.compareTo(name1);
	    }
		
		result = result *  multiply;
				
		if (result == 0) {
			result = super.compare(arg0, arg1, true);
		}

		return result;

	}
}