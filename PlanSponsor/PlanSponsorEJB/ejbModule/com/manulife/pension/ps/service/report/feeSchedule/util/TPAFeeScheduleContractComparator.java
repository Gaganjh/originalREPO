package com.manulife.pension.ps.service.report.feeSchedule.util;

import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

import com.manulife.pension.ps.service.report.feeSchedule.valueobject.TPAFeeScheduleContract;

/**
 * 
 * @author Siby Thomas
 *
 */
abstract public class TPAFeeScheduleContractComparator implements
		Comparator<TPAFeeScheduleContract> {

	protected static final Date MIN_DATE = new GregorianCalendar(1, 0, 1)
			.getTime();
	protected static final Integer MIN_INTEGER = new Integer(Integer.MIN_VALUE);
	protected static final String EMPTY_STRING = "";

	protected boolean ascending = true;

	public TPAFeeScheduleContractComparator() {
		this.ascending = true;
	}

	public TPAFeeScheduleContractComparator(boolean ascending) {
		this.ascending = ascending;
	}

	abstract public int compare(TPAFeeScheduleContract arg0,
			TPAFeeScheduleContract arg1);

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
