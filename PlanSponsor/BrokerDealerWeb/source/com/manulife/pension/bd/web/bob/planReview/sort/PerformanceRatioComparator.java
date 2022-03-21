package com.manulife.pension.bd.web.bob.planReview.sort;

import com.manulife.pension.bd.web.bob.planReview.PlanReviewReportUIHolder;

/**
 * This is Comparator class for Performance Ratio Comparator used for Plan Review
 * report pages.
 * 
 * @author NagaRaju
 * 
 */

public class PerformanceRatioComparator extends
		BaseContractReviewReportComparator {

	public PerformanceRatioComparator(String sortDirection) {
		super(sortDirection);
	}

	@Override
	public int compare(PlanReviewReportUIHolder arg0, PlanReviewReportUIHolder arg1) {

		boolean chck0 = arg0.isSelectedperformanceAndExpenseRatio();
		boolean chck1 = arg1.isSelectedperformanceAndExpenseRatio();
		String checkedselected0 = new Boolean(chck0).toString();
		String checkedselected1 = new Boolean(chck1).toString();

		int multiplier = isAscending() ? 1 : -1;

		int result = multiplier
				* checkedselected1.compareToIgnoreCase(checkedselected0);
		
		if (result == 0) {
			result = super.compare(arg0, arg1);
		}

		return result;
	}

}
