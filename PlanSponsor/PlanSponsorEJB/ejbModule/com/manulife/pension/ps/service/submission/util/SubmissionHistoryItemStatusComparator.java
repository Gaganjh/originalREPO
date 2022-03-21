package com.manulife.pension.ps.service.submission.util;

import com.manulife.pension.ps.service.submission.valueobject.SubmissionHistoryItem;

/**
 * Comparator implementation by status where the secondary sort by tracing (submission) number descending always
 * 
 * @author Tony Tomasone
 */
public class SubmissionHistoryItemStatusComparator extends SubmissionHistoryItemComparator {

	public SubmissionHistoryItemStatusComparator() {
		super();
	}

	public int compare(Object arg0, Object arg1) {

		StatusGroupHelper statusHelper = StatusGroupHelper.getInstance();
		
		String status0 = null;
		String status1 = null;

		if (arg0 != null) {
			status0 = statusHelper.getGroupByStatus(((SubmissionHistoryItem)(arg0)).getSystemStatus());
		}
		if (arg1 != null) {
			status1 = statusHelper.getGroupByStatus(((SubmissionHistoryItem)(arg1)).getSystemStatus());
		}
		
		if (status0 == null) {
			status0 = EMPTY_STRING;
		}
		if (status1 == null) {
			status1 = EMPTY_STRING;
		}
		int result = (isAscending() ? 1 : -1) * status0.compareTo(status1);
		
		// if they are equal, goto the secondary sort by tracing (submission) number descending always
		if (result == 0) {
			result = new SubmissionHistoryItemSubmissionNumberComparator(false).compare(arg0, arg1);
		}
		return result;
	}
}