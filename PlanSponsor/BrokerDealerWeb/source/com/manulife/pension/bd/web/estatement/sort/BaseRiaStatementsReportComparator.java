package com.manulife.pension.bd.web.estatement.sort;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.estatement.RiaStatementVO;


/**
 * This is base Comparator class used for Plan Review report pages.
 * 
 * @author NagaRaju
 * 
 */

abstract public class BaseRiaStatementsReportComparator implements
		Comparator<RiaStatementVO> {

	protected static final String SORT_DIRECTION_ASC = "asc";

	protected boolean ascending = true;

	public BaseRiaStatementsReportComparator() {
		this.ascending = true;
	}

	public BaseRiaStatementsReportComparator(boolean ascending) {
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
	public BaseRiaStatementsReportComparator(String sortDirection) {
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
	

}
