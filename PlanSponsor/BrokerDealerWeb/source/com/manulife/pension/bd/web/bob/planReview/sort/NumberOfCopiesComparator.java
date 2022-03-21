package com.manulife.pension.bd.web.bob.planReview.sort;

import com.manulife.pension.bd.web.bob.planReview.PlanReviewReportUIHolder;

/**
 * This is Comparator class for Report MonthEnd Date used for Plan Review report pages.
 * 
 * @author NagaRaju
 * 
 */

public class NumberOfCopiesComparator extends BaseContractReviewReportComparator{



	public NumberOfCopiesComparator(String sortDirection) {
		super(sortDirection);
	}

	@Override
	public int compare(PlanReviewReportUIHolder arg0,
			PlanReviewReportUIHolder arg1) {
		String numofcopy0 = arg0.getNumberOfCopies();
		String numofcopy1 = arg1.getNumberOfCopies();	
		Integer contractNbr0 = arg0.getContractNumber();
		Integer contractNbr1 = arg1.getContractNumber();

		int multiplier = isAscending() ? 1 : -1;
		
		int result = multiplier
				* numofcopy0.compareTo(numofcopy1);
		if  (result==0){
			result =  contractNbr0.compareTo(contractNbr1);
		}
		return result;
	}





}
