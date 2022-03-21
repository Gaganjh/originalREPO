package com.manulife.pension.bd.web.bob.planReview.sort;

import com.manulife.pension.service.planReview.valueobject.PrintDocumentPackgeVo;


/**
 * This is base Comparator class used for Plan Review report pages.
 * 
 * @author Ashok
 * 
 */

 public class PrintRequestStatusComparator extends
BaseHistoryDetailsComparator {

	public PrintRequestStatusComparator(String sortDirection) {
		super(sortDirection);
	}

	@Override
	public int compare(PrintDocumentPackgeVo arg0, PrintDocumentPackgeVo arg1) {

		String activityStatus0 = arg0.getActivityStatus();
		String activityStatus1 = arg1.getActivityStatus();

		int multiplier = isAscending() ? 1 : -1;

		int result = multiplier
				* activityStatus0.compareToIgnoreCase(activityStatus1);
		if (result == 0) {
			result = super.compare(arg0, arg1);
		}

		return result;
	}

}
