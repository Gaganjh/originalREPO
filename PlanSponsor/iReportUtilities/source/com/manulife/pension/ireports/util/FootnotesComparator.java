package com.manulife.pension.ireports.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.manulife.pension.ireports.dao.Footnote;





/**
 * Handler class for footnotes
 * 
 * @author Naresh
 * 
 */
public class FootnotesComparator implements Comparator {

	/** singleton instance */
	private static FootnotesComparator instance = new FootnotesComparator();
	
	/** default logger */
	private Logger logger = Logger.getLogger(FootnotesComparator.class);

	/**
	 * Returns singleton instance
	 */
	public static FootnotesComparator getInstance(){
		return instance;
	}
	
	/**
	 *  Private constructor for singleton
	 * 
	 */
	private FootnotesComparator(){
	}
	

	/**
	 * Implementation of Comparator interface.
	 * Compares two footnotes according to their sort sequence.
	 * @param obj1 first footnote
	 * @param obj2 second footnote to compare
	 * @return int -1|0|1 if obj1 < obj2 | obj1 == obj2 | obj1 > obj2
	 */
	public int compare( Object obj1, Object obj2 ){
		Footnote f1 = (Footnote)obj1;
		Footnote f2 = (Footnote)obj2;
		
		if ( f1.getOrderNumber() < f2.getOrderNumber() )
			return -1;
		else if ( f1.getOrderNumber() > f2.getOrderNumber() )	
			return 1;
			
		return 0;
	}
}

