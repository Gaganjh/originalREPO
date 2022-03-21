/**
 * Created on September 12, 2006
 */
package com.manulife.pension.ps.service.withdrawal.util;

import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Harsh Kuthiala
 */
public abstract  class SearchParticipantWithdrawalRequestComparator implements Comparator {
	
	
	protected static final String EMPTY_STRING = "";
	
	protected boolean ascending = true;

	public SearchParticipantWithdrawalRequestComparator() {
		this.ascending = true;
	}

	public SearchParticipantWithdrawalRequestComparator(boolean ascending) {
		this.ascending = ascending;
	}

    public abstract  int compare(Object arg0, Object arg1);

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
