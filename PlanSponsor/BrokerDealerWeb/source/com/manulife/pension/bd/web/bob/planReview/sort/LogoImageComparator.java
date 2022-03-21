package com.manulife.pension.bd.web.bob.planReview.sort;

import com.manulife.pension.bd.web.bob.planReview.PlanReviewReportUIHolder;

/**
 * This is Comparator class for Logo Image Name used for Plan Review report
 * pages.
 * 
 * @author NagaRaju
 * 
 */

public class LogoImageComparator extends BaseContractReviewReportComparator {

	public LogoImageComparator(String sortDirection) {
		super(sortDirection);
	}

	@Override
	public int compare(PlanReviewReportUIHolder arg0, PlanReviewReportUIHolder arg1) {

		String logoimg0 = arg0.getLogoImageNameTitle();
		String logoimg1 = arg1.getLogoImageNameTitle();

		int multiplier = isAscending() ? 1 : -1;

		int result = multiplier * logoimg0.compareToIgnoreCase(logoimg1);
		if  (result==0){
			result = super.compare(arg0, arg1);
		}
		return result;
	}

}
