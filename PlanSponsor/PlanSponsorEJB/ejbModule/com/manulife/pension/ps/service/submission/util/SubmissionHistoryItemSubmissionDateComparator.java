package com.manulife.pension.ps.service.submission.util;

import java.util.Date;

import com.manulife.pension.ps.service.submission.valueobject.SubmissionHistoryItem;

/**
 * Comparator implementation by submission date where the secondary sort by tracing (submission) number descending always
 * 
 * @author Tony Tomasone
 */
public class SubmissionHistoryItemSubmissionDateComparator extends SubmissionHistoryItemComparator {

	public SubmissionHistoryItemSubmissionDateComparator() {
		super();
	}

	public int compare(Object arg0, Object arg1) {

		Date date0 = null;
		Date date1 = null;
		
		boolean isDraft1 = false;
		boolean isDraft2 = false;

		if (arg0 != null) {
			date0 = ((SubmissionHistoryItem)(arg0)).getSubmissionDate();
			isDraft1 = ((SubmissionHistoryItem)(arg0)).isDraft();
		}
		if (arg1 != null) {
			date1 = ((SubmissionHistoryItem)(arg1)).getSubmissionDate();
			isDraft2 = ((SubmissionHistoryItem)(arg1)).isDraft();
		}
		
		if (date0 == null) {
			date0 = MIN_DATE;
		}
		if (date1 == null) {
			date1 = MIN_DATE;
		}

		int multiplier = isAscending() ? 1 : -1;
		
		// first sort by draft status
		if (isDraft1 && !isDraft2) {
			return multiplier;
		} else if (!isDraft1 && isDraft2) {
			return -1 * multiplier;
		} 
		
		// at this point either both are draft or not draft so we can use the
		// normal comparison method for submission date at this point

		int result = multiplier * date0.compareTo(date1);
		
		// if they are equal, goto the secondary sort by tracing (submission) number descending always
		if (result == 0) {
			result = new SubmissionHistoryItemSubmissionNumberComparator(false).compare(arg0, arg1);
		}
		
		return result;
	}
}