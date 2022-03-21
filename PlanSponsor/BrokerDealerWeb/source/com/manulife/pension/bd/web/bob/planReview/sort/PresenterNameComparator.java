package com.manulife.pension.bd.web.bob.planReview.sort;

import com.manulife.pension.bd.web.bob.planReview.PlanReviewReportUIHolder;

/**
 * This is Comparator class for Presenter Name used for Plan Review report
 * pages.
 * 
 * @author NagaRaju
 * 
 */

public class PresenterNameComparator extends BaseContractReviewReportComparator {

	public PresenterNameComparator(String sortDirection) {
		super(sortDirection);
	}

	@Override
	public int compare(PlanReviewReportUIHolder arg0, PlanReviewReportUIHolder arg1) {

		String prstrName0 = arg0.getPresenterName();
		String prstrName1 = arg1.getPresenterName();

		int multiplier = isAscending() ? 1 : -1;

		int result = multiplier * prstrName0.compareToIgnoreCase(prstrName1);
		//if Month End date is same then we can make the sorting according to ContactNumber and Name
		if(result==0){
			result = super.compare(arg0, arg1);
		}
		return result;
	}

}
