package com.manulife.pension.ps.service.submission.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.manulife.pension.ps.service.submission.valueobject.AddableParticipant;


/**
 * @author adamthj
 *
 */
public class AddableParticipantComparatorFactory {

	private static AddableParticipantComparatorFactory instance = new AddableParticipantComparatorFactory(); 
	private Map comparators;

	private static final String DESCENDING_INDICATOR = "DESC";
	
	private AddableParticipantComparatorFactory() {
		super();
		comparators = new HashMap(3);
		comparators.put(AddableParticipant.SORT_NAME,
				new AddableParticipantNameComparator());
		comparators.put(AddableParticipant.SORT_IDENTIFIER,
				new AddableParticipantEmployerDesignatedIdComparator());
		comparators.put(AddableParticipant.SORT_INCLUDED,
				new AddableParticipantIncludedComparator());
	}

	public static AddableParticipantComparatorFactory getInstance() {
		return instance;
	}

	private class AddableParticipantNameComparator extends SubmissionHistoryItemComparator {

		public AddableParticipantNameComparator() {
			super();
		}
		
		public AddableParticipantNameComparator(boolean ascending) {
			super(ascending);
		}

		public int compare(Object arg0, Object arg1) {

			String name0 = null;
			String name1 = null;

			if (arg0 != null) {
				name0 = ((AddableParticipant)(arg0)).getName();
			}
			if (arg1 != null) {
				name1 = ((AddableParticipant)(arg1)).getName();
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
				result = new AddableParticipantEmployerDesignatedIdComparator(isAscending()).compare(arg0, arg1);
			}
			return result;
		}
	}

	private class AddableParticipantEmployerDesignatedIdComparator extends SubmissionHistoryItemComparator {

		public AddableParticipantEmployerDesignatedIdComparator() {
			super();
		}
		
		public AddableParticipantEmployerDesignatedIdComparator(boolean ascending) {
			super(ascending);
		}
		
		public int compare(Object arg0, Object arg1) {

			String number0 = null;
			String number1 = null;
			
			if (arg0 != null) {
				number0 = ((AddableParticipant)(arg0)).getIdentifier();
			}
			if (arg1 != null) {
				number1 = ((AddableParticipant)(arg1)).getIdentifier();
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


	private class AddableParticipantIncludedComparator extends SubmissionHistoryItemComparator {

		public AddableParticipantIncludedComparator() {
			super();
		}

		public int compare(Object arg0, Object arg1) {

			String name0 = null;
			String name1 = null;

			if (arg0 != null) {
				name0 = ((AddableParticipant)(arg0)).getIncludedInSubmission() == null ? "1" : "";
			}
			if (arg1 != null) {
				name1 = ((AddableParticipant)(arg1)).getIncludedInSubmission() == null ? "1" : "";
			}
			int result = (isAscending() ? 1 : -1) * name0.compareToIgnoreCase(name1);
			
			// if they are equal, goto the secondary sort by name
			if (result == 0) {
				result = new AddableParticipantNameComparator(isAscending()).compare(arg0, arg1);
			}
			// if they are still equal, goto the tertiary sort by identifier
			if (result == 0) {
				result = new AddableParticipantEmployerDesignatedIdComparator(isAscending()).compare(arg0, arg1);
			}
			return result;
		}
	}
	
	public Comparator getComparator(String sortField, String sortDirection) {
		SubmissionHistoryItemComparator comparator = 
			comparators.containsKey(sortField) ? (SubmissionHistoryItemComparator) comparators.get(sortField) : 
												 (SubmissionHistoryItemComparator) comparators.get(AddableParticipant.SORT_IDENTIFIER);
		comparator.setAscending(!DESCENDING_INDICATOR.equalsIgnoreCase(sortDirection)); 	
		return comparator;
	}
	

}
