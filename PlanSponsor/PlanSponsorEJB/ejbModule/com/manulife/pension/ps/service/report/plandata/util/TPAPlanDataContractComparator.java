package com.manulife.pension.ps.service.report.plandata.util;

import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

import com.manulife.pension.ps.service.report.plandata.valueobject.TPAPlanDataContract;

/**
 * 
 * @author Dheepa Poongol
 *
 */
abstract public class TPAPlanDataContractComparator implements
		Comparator<TPAPlanDataContract> {

	protected static final Date MIN_DATE = new GregorianCalendar(1, 0, 1)
			.getTime();
	protected static final Integer MIN_INTEGER = new Integer(Integer.MIN_VALUE);
	protected static final String EMPTY_STRING = "";

	protected boolean ascending = true;

	public TPAPlanDataContractComparator() {
		this.ascending = true;
	}

	public TPAPlanDataContractComparator(boolean ascending) {
		this.ascending = ascending;
	}

	abstract public int compare(TPAPlanDataContract arg0,
	        TPAPlanDataContract arg1);

	/**
	 * @return Returns the ascending.
	 */
	public boolean isAscending() {
		return ascending;
	}

	/**
	 * @param ascending
	 *            The ascending to set.
	 */
	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}
}
