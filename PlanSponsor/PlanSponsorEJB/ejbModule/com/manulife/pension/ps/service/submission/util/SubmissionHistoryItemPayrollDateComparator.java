package com.manulife.pension.ps.service.submission.util;

import java.util.Date;

import com.manulife.pension.ps.service.submission.valueobject.SubmissionHistoryItem;

/**
 * Comparator implementation by PayrollDate where the secondary sort by tracing (submission) number descending always
 * 
 * @author Tony Tomasone
 */
public class SubmissionHistoryItemPayrollDateComparator extends SubmissionHistoryItemComparator {

	public SubmissionHistoryItemPayrollDateComparator() {
		super();
	}

	public int compare(Object arg0, Object arg1) {

		Date date0 = null;
		Date date1 = null;

		if (arg0 != null) {
			date0 = ((SubmissionHistoryItem)(arg0)).getPayrollDate();
		}
		if (arg1 != null) {
			date1 = ((SubmissionHistoryItem)(arg1)).getPayrollDate();
		}
		
		if (date0 == null) {
			date0 = MIN_DATE;
		}
		if (date1 == null) {
			date1 = MIN_DATE;
		}
		int result = (isAscending() ? 1 : -1) * date0.compareTo(date1);
		
		// if they are equal, goto the secondary sort by tracing (submission) number descending always
		if (result == 0) {
			result = new SubmissionHistoryItemSubmissionNumberComparator(false).compare(arg0, arg1);
		}
		return result;
	}
}