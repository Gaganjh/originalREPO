package com.manulife.pension.ps.service.report.feeSchedule.util;

import com.manulife.pension.ps.service.report.feeSchedule.valueobject.TPAFeeScheduleContract;

/**
 * 
 * invoked when sort by type is clicked
 * 
 * @author Siby Thomas
 *
 */
public class TPAFeeScheduleContractScheduleTypeComparator extends TPAFeeScheduleContractNameComparator {
	
	public TPAFeeScheduleContractScheduleTypeComparator() {
		super();
	}

	@Override
	public int compare(TPAFeeScheduleContract arg0, TPAFeeScheduleContract arg1) {

		String feeSchedule0 = arg0.getFeeSchedule();
		String feeSchedule1 = arg1.getFeeSchedule();
		
		int result = 0;
		int multiply = (isAscending() ? 1 : -1);
		
	    if(feeSchedule0 == null && feeSchedule1 == null) {
		   result = 0;
	    } else if (feeSchedule0 == null) {
	    	result = -1;
	    }else if (feeSchedule1 == null) {
	    	result = 1;
	    } else {
	    	result = feeSchedule0.compareTo(feeSchedule1);
	    }
		
		result = result *  multiply;
				
		if (result == 0) {
			result = super.compare(arg0, arg1, true);
		}
		
		return result;
		
	}
}