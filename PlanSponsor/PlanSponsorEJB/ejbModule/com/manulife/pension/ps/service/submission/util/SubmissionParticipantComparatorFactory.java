package com.manulife.pension.ps.service.submission.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.manulife.pension.ps.service.submission.valueobject.SubmissionParticipant;


/**
 * @author adamthj
 *
 */
public class SubmissionParticipantComparatorFactory {

	private static SubmissionParticipantComparatorFactory instance = new SubmissionParticipantComparatorFactory(); 
	private Map comparators;

	private static final String DESCENDING_INDICATOR = "DESC";
	
	private SubmissionParticipantComparatorFactory() {
		super();
		comparators = new HashMap(6);
		comparators.put(SubmissionParticipant.SORT_RECORD_NUMBER, 
				new SubmissionParticipantRecordNumberComparator());
		comparators.put(SubmissionParticipant.SORT_NAME,
				new SubmissionParticipantNameComparator());
		comparators.put(SubmissionParticipant.SORT_IDENTIFIER,
				new SubmissionParticipantEmployeeDesignatedIdComparator());
	}

	public static SubmissionParticipantComparatorFactory getInstance() {
		return instance;
	}

	private class SubmissionParticipantRecordNumberComparator extends SubmissionHistoryItemComparator {

		public SubmissionParticipantRecordNumberComparator() {
			super();
		}
		
		public SubmissionParticipantRecordNumberComparator(boolean ascending) {
			super(ascending);
		}
		
		public int compare(Object arg0, Object arg1) {

			Integer number0 = null;
			Integer number1 = null;
			
			if (arg0 != null) {
				number0 = new Integer(((SubmissionParticipant)(arg0)).getRecordNumber());
			}
			if (arg1 != null) {
				number1 = new Integer(((SubmissionParticipant)(arg1)).getRecordNumber());
			}
			
			if (number0 == null) {
				number0 = MIN_INTEGER;
			}
			if (number1 == null) {
				number1 = MIN_INTEGER;
			}

			int result = (isAscending() ? 1 : -1) * number0.compareTo(number1);
			
			// if they are equal, goto the secondary sort by tracing (submission) number descending always
			if (result == 0) {
				result = new SubmissionParticipantNameComparator().compare(arg0, arg1);
			}
			return result;
		}
	}

	private class SubmissionParticipantNameComparator extends SubmissionHistoryItemComparator {

		public SubmissionParticipantNameComparator() {
			super();
		}

		public int compare(Object arg0, Object arg1) {

			String name0 = null;
			String name1 = null;

			if (arg0 != null) {
				name0 = ((SubmissionParticipant)(arg0)).getName();
			}
			if (arg1 != null) {
				name1 = ((SubmissionParticipant)(arg1)).getName();
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
				result = new SubmissionParticipantRecordNumberComparator(true).compare(arg0, arg1);
			}
			return result;
		}
	}

	private class SubmissionParticipantEmployeeDesignatedIdComparator extends SubmissionHistoryItemComparator {

		public SubmissionParticipantEmployeeDesignatedIdComparator() {
			super();
		}
		
		public SubmissionParticipantEmployeeDesignatedIdComparator(boolean ascending) {
			super(ascending);
		}
		
		public int compare(Object arg0, Object arg1) {

			String number0 = null;
			String number1 = null;
			
			if (arg0 != null) {
				number0 = ((SubmissionParticipant)(arg0)).getIdentifier();
			}
			if (arg1 != null) {
				number1 = ((SubmissionParticipant)(arg1)).getIdentifier();
			}
			
			if (number0 == null) {
				number0 = EMPTY_STRING;
			}
			if (number1 == null) {
				number1 = EMPTY_STRING;
			}

			return (isAscending() ? 1 : -1) * number0.compareTo(number1);
		}
	}

	
	public Comparator getComparator(String sortField, String sortDirection) {
		SubmissionHistoryItemComparator comparator = 
			comparators.containsKey(sortField) ? (SubmissionHistoryItemComparator) comparators.get(sortField) : 
												 (SubmissionHistoryItemComparator) comparators.get(SubmissionParticipant.SORT_IDENTIFIER);
		comparator.setAscending(!DESCENDING_INDICATOR.equalsIgnoreCase(sortDirection)); 	
		return comparator;
	}
	

}
