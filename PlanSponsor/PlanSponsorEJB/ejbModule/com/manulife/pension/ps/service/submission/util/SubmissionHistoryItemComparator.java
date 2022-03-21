package com.manulife.pension.ps.service.submission.util;

import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Adrian Robitu
 */
abstract public class SubmissionHistoryItemComparator implements Comparator {
	
	protected static final Date MIN_DATE = new GregorianCalendar(1, 0, 1).getTime();
	protected static final Integer MIN_INTEGER = new Integer(Integer.MIN_VALUE);
	protected static final String EMPTY_STRING = "";
	
	protected boolean ascending = true;

	public SubmissionHistoryItemComparator() {
		this.ascending = true;
	}

	public SubmissionHistoryItemComparator(boolean ascending) {
		this.ascending = ascending;
	}

	abstract public int compare(Object arg0, Object arg1);

	/**
	 * @return Returns the ascending.
	 */
	public boolean isAscending() {
		return ascending;
	}

	/**
	 * @param ascending The ascending to set.
	 */
	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}
}
