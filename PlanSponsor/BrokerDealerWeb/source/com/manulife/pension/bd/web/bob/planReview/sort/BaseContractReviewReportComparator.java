package com.manulife.pension.bd.web.bob.planReview.sort;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.bob.planReview.PlanReviewReportUIHolder;


/**
 * This is base Comparator class used for Plan Review report pages.
 * 
 * @author NagaRaju
 * 
 */

abstract public class BaseContractReviewReportComparator implements
		Comparator<PlanReviewReportUIHolder> {

	protected static final String SORT_DIRECTION_ASC = "asc";

	protected boolean ascending = true;

	public BaseContractReviewReportComparator() {
		this.ascending = true;
	}

	public BaseContractReviewReportComparator(boolean ascending) {
		this.ascending = ascending;
	}

	/**
	 * @return Returns the ascending.
	 */
	public boolean isAscending() {
		return ascending;
	}

	/**
	 * @param sortDirection
	 */
	public BaseContractReviewReportComparator(String sortDirection) {
		this.ascending = StringUtils.isBlank(sortDirection)
				|| SORT_DIRECTION_ASC.equalsIgnoreCase(sortDirection);
	}

	/**
	 * @param ascending
	 *            The ascending to set.
	 */
	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}
	
	@Override
	public int compare(PlanReviewReportUIHolder arg0, PlanReviewReportUIHolder arg1) {

		String contractName0 = arg0.getContractName();
		String contractName1 = arg1.getContractName();
		Integer contractNumber1 = arg0.getContractNumber();
		Integer contractNumber2 = arg1.getContractNumber();

		int multiplier = isAscending() ? 1 : -1;

		int result = multiplier
				* contractName0.compareToIgnoreCase(contractName1);
		if(result == 0){
			result = contractNumber1.compareTo(contractNumber2);
		}
		return result;
	}

}
