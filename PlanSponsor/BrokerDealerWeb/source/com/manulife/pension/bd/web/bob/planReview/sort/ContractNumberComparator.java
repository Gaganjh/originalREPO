package com.manulife.pension.bd.web.bob.planReview.sort;

import com.manulife.pension.bd.web.bob.planReview.PlanReviewReportUIHolder;

/**
 * This is Comparator class for Contract Number used for Plan Review report pages.
 * 
 * @author NagaRaju
 * 
 */

public class ContractNumberComparator extends
		BaseContractReviewReportComparator {

	public ContractNumberComparator(String sortDirection) {
		super(sortDirection);
	}

	@Override
	public int compare(PlanReviewReportUIHolder arg0, PlanReviewReportUIHolder arg1) {

		Integer contractNbr0 = arg0.getContractNumber();
		Integer contractNbr1 = arg1.getContractNumber();

		int multiplier = isAscending() ? 1 : -1;

		int result = multiplier * contractNbr0.compareTo(contractNbr1);

		return result;
	}

}
