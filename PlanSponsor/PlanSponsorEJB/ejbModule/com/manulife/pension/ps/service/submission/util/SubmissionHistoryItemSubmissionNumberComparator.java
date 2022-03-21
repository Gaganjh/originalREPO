package com.manulife.pension.ps.service.submission.util;

import com.manulife.pension.ps.service.submission.valueobject.SubmissionHistoryItem;

/**
 * Comparator implementation by submission number
 * 
 * @author Tony Tomasone
 */
public class SubmissionHistoryItemSubmissionNumberComparator extends SubmissionHistoryItemComparator {
	
	public SubmissionHistoryItemSubmissionNumberComparator() {
		super();
	}

	public SubmissionHistoryItemSubmissionNumberComparator(boolean ascending) {
		super(ascending);
	}

	public int compare(Object arg0, Object arg1) {

		Integer number0 = null;
		Integer number1 = null;
		
		if (arg0 != null) {
			number0 = ((SubmissionHistoryItem)(arg0)).getSubmissionId();
		}
		if (arg1 != null) {
			number1 = ((SubmissionHistoryItem)(arg1)).getSubmissionId();
		}
		
		if (number0 == null) {
			number0 = MIN_INTEGER;
		}
		if (number1 == null) {
			number1 = MIN_INTEGER;
		}

		return (isAscending() ? 1 : -1) * number0.compareTo(number1);
	}
}