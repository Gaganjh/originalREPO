package com.manulife.pension.bd.web.bob.planReview.sort;

import com.manulife.pension.bd.web.bob.planReview.PlanReviewReportUIHolder;

/**
 * This is Comparator class for Cover Image Name used for Plan Review report pages.
 * 
 * @author NagaRaju
 * 
 */

public class CoverImageComparator extends
BaseContractReviewReportComparator{

	public CoverImageComparator(String sortDirection) {
		super(sortDirection);
	}

	@Override
	public int compare(PlanReviewReportUIHolder arg0,
			PlanReviewReportUIHolder arg1) {

		String rptcvgName0 = arg0.getCoverImageNameTitle();
		String rptcvgName1 = arg1.getCoverImageNameTitle();

		int multiplier = isAscending() ? 1 : -1;

		int result = multiplier
				* rptcvgName0.compareToIgnoreCase(rptcvgName1);
		if  (result==0){
			result = super.compare(arg0, arg1);
		}
		return result;
	}

}
