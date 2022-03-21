package com.manulife.pension.bd.web.util;

import java.util.Comparator;

/**
 * This class represents a Comparator that compares two object first by a major
 * comparator, and if they are equal then by a minor comparator.
 * 
 * @author Ilamparithi
 * 
 */
@SuppressWarnings("unchecked")
public class CompositeComparator implements Comparator {

	private Comparator major;
	private Comparator minor;

	public CompositeComparator(Comparator major, Comparator minor) {
		this.major = major;
		this.minor = minor;
	}

	public int compare(Object o1, Object o2) {
		int result = major.compare(o1, o2);
		// if objects are not same return the result
		if (result != 0) {
			return result;
			// if objects are same do second level comparison
		} else {
			return minor.compare(o1, o2);
		}
	}

}
