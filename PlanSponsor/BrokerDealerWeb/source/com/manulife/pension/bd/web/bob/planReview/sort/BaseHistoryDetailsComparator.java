package com.manulife.pension.bd.web.bob.planReview.sort;

import java.sql.Timestamp;
import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.planReview.valueobject.PrintDocumentPackgeVo;


/**
 * This is base Comparator class used for Plan Review report pages.
 * 
 * @author Ashok
 * 
 */

abstract public class BaseHistoryDetailsComparator implements
		Comparator<PrintDocumentPackgeVo> {

	protected static final String SORT_DIRECTION_ASC = "asc";

	protected boolean ascending = true;

	public BaseHistoryDetailsComparator() {
		this.ascending = true;
	}

	public BaseHistoryDetailsComparator(boolean ascending) {
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
	public BaseHistoryDetailsComparator(String sortDirection) {
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
	public int compare(PrintDocumentPackgeVo arg0, PrintDocumentPackgeVo arg1) {

		Integer activityId0 = arg0.getActivityId();
		Integer activityId1 = arg1.getActivityId();

		int multiplier = isAscending() ? 1 : -1;

		int result = multiplier*activityId0.compareTo(activityId1);
		
		return result;
	}

}
