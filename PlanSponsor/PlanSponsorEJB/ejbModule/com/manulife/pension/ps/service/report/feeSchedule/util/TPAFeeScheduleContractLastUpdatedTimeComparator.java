package com.manulife.pension.ps.service.report.feeSchedule.util;

import java.util.Date;

import com.manulife.pension.ps.service.report.feeSchedule.valueobject.TPAFeeScheduleContract;

/**
 * invoked when sort by user name
 * 
 * @author Siby Thomas
 * 
 */
public class TPAFeeScheduleContractLastUpdatedTimeComparator extends TPAFeeScheduleContractNameComparator {

	public TPAFeeScheduleContractLastUpdatedTimeComparator() {
		super();
	}

	@Override
	public int compare(TPAFeeScheduleContract arg0, TPAFeeScheduleContract arg1) {

		Date date0 = arg0.getCreatedTS();
		Date date1 = arg1.getCreatedTS();

		int result = 0;
		int multiply = (isAscending() ? 1 : -1);
		
	    if(date0 == null && date1 == null) {
		   result = 0;
	    } else if (date0 == null) {
	    	result = -1;
	    }else if (date1 == null) {
	    	result = 1;
	    } else {
	    	result = date0.compareTo(date1);
	    }
		
		result = result *  multiply;
				
		if (result == 0) {
			result = super.compare(arg0, arg1, true);
		}

		return result;

	}
}