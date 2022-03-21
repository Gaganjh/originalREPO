package com.manulife.pension.bd.web.bob.planReview.sort;

import com.manulife.pension.bd.web.bob.planReview.PlanReviewReportUIHolder;

/**
 * This is Comparator class for Contract Name used for Plan Review report pages.
 * 
 * @author NagaRaju
 * 
 */

public class ContractNameComparator extends BaseContractReviewReportComparator {

	public ContractNameComparator(String sortDirection) {
		super(sortDirection);
	}

	@Override
	public int compare(PlanReviewReportUIHolder arg0, PlanReviewReportUIHolder arg1) {		
		return super.compare(arg0, arg1);
	}

}
