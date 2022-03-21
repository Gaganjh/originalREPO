package com.manulife.pension.ps.service.report.plandata.util;

import com.manulife.pension.ps.service.report.plandata.valueobject.TPAPlanDataContract;

/**
 * 
 * invoked when the Service Selected column header is clicked on
 * 
 * @author Tejas Chandrashekhar
 *
 */
public class TPAPlanDataContractServiceSelectedComparator extends TPAPlanDataContractNameComparator {
	
	public TPAPlanDataContractServiceSelectedComparator() {
		super();
	}

	@Override
	public int compare(TPAPlanDataContract arg0, TPAPlanDataContract arg1) {

		String serviceSelected0 = arg0.getServiceSelected();
		String serviceSelected1 = arg1.getServiceSelected();
		
		int result = 0;
		int multiply = (isAscending() ? 1 : -1);
		
	    if(serviceSelected0 == null && serviceSelected1 == null) {
		   result = 0;
	    } else if (serviceSelected0 == null) {
	    	result = -1;
	    }else if (serviceSelected1 == null) {
	    	result = 1;
	    } else {
	    	result = serviceSelected0.compareTo(serviceSelected1);
	    }
		
		result = result *  multiply;
				
		if (result == 0) {
			result = super.compare(arg0, arg1, true);
		}
		
		return result;		
	}
}