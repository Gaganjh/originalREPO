package com.manulife.pension.ps.web.investment.sort;

import java.util.Vector;

import com.manulife.pension.ps.web.investment.util.SortTool;

/**
 * This class is used by ContractFunds when sorting on a column that is alphabetic
 *
 * @author   Chris Shin
 * @version  CS1.0  (March 1, 2004)
 * @see	     com.manulife.pension.ezk.web.investments.sort.FundsByStringColumnSortorderTool.java
 **/
public final class FundsByStringColumnSortorderTool implements SortTool {
	
	private int sortColumn;
	/**
 	 * constructor for FundsByStringColumnSortorderTool
 	 * input parameter passes the position of the column by which to sort 
 	 * 
 	 * @param sortColumn int
 	 */
	public FundsByStringColumnSortorderTool(int newSortColumn) {
		super();
		sortColumn = newSortColumn;

	}
	/**
 	* Compare value of a given column in two vectors.
 	*/
	public final int compare(Object firstFundVector, Object secondFundVector) {
		
		// convert appropriate column to String
		String sortNumber1 = ((String)((Vector)firstFundVector).elementAt(sortColumn));
		String sortNumber2 = ((String)((Vector)secondFundVector).elementAt(sortColumn));
	
		// check if either item is null
		if (sortNumber1 == null) { 
			return IS_LESS_THAN;
		}
	
		if (sortNumber1 == null && sortNumber2 == null) {
			return IS_EQUAL_TO;
		}
	
		if (sortNumber2 == null) {
			return IS_GREATER_THAN;
		}
		
		// compare strings
		int result = sortNumber1.compareTo(sortNumber2);
		// return result
		if (result < 0) {
			return IS_LESS_THAN;
		}

		if (result == 0) {
			return IS_EQUAL_TO;
		}

		return IS_GREATER_THAN;
	}

}

