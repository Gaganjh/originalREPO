package com.manulife.pension.ps.service.report.plandata.util;

import java.util.Date;

import com.manulife.pension.ps.service.report.plandata.valueobject.TPAPlanDataContract;

/**
 * invoked when sort by user name
 * 
 * @author Dheepa Poongol
 * 
 */
public class TPAPlanDataContractLastUpdatedTimeComparator extends TPAPlanDataContractNameComparator {

	public TPAPlanDataContractLastUpdatedTimeComparator() {
		super();
	}

	@Override
	public int compare(TPAPlanDataContract arg0, TPAPlanDataContract arg1) {

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