package com.manulife.pension.ps.service.submission.util;

import com.manulife.pension.ps.service.submission.valueobject.SubmissionHistoryItem;

/**
 * Comparator implementation by submitter name where the secondary sort by tracing (submission) number descending always
 *
 * @author Tony Tomasone
 */
public class SubmissionHistoryItemSubmitterComparator extends SubmissionHistoryItemComparator {

	public SubmissionHistoryItemSubmitterComparator() {
		super();
	}

	public int compare(Object arg0, Object arg1) {

		String name0 = null;
		String name1 = null;

		if (arg0 != null) {
			name0 = ((SubmissionHistoryItem)(arg0)).getSubmitterName();
		}
		if (arg1 != null) {
			name1 = ((SubmissionHistoryItem)(arg1)).getSubmitterName();
		}
		if (name0 == null) {
			name0 = EMPTY_STRING;
		}
		if (name1 == null) {
			name1 = EMPTY_STRING;
		}
		int result = (isAscending() ? 1 : -1) * name0.compareToIgnoreCase(name1);
		
		// if they are equal, goto the secondary sort by tracing (submission) number descending always
		if (result == 0) {
			result = new SubmissionHistoryItemSubmissionNumberComparator(false).compare(arg0, arg1);
		}
		return result;
	}
}