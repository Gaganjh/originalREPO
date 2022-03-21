package com.manulife.pension.ps.web.investment.sort;
/**
 * <p>
 * Tool for sorting entries defined as BigDecimal in a vector by value
 * <p>
 *
 */
import java.math.BigDecimal;
import java.util.Vector;

import com.manulife.pension.ps.web.investment.util.SortTool;
/**
 * This class is used by ContractFunds when sorting on a column that is numeric
 *
 * @author   Chris Shin
 * @version  CS1.0  (March 1, 2004)
 * @see	     com.manulife.pension.ezk.web.investments.sort.FundsByDecimalColumnSortorderTool.java
 **/

public class FundsByBigDecimalColumnSortorderTool implements SortTool {
	private int sortColumn;
   /**
 	* constructor for FundsByBigDecimalColumnSortorderTool
 	* input parameter passes the position of the column by which to sort 
 	* 
 	* @param sortColumn int
 	*/
	public FundsByBigDecimalColumnSortorderTool(int newSortColumn) {
		super();
		sortColumn = newSortColumn;

	}
	/**
 	 * Compare value of a given column in two vectors.
 	 */
	public final int compare(Object firstFundVector, Object secondFundVector) {
	
		// convert appropriate column to BigDecimal
		BigDecimal sortNumber1 = ((BigDecimal)((Vector)firstFundVector).elementAt(sortColumn));
		BigDecimal sortNumber2 = ((BigDecimal)((Vector)secondFundVector).elementAt(sortColumn));
		
		
		
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
		
		// compare numbers
		int result = sortNumber1.compareTo(sortNumber2);
		
			
		// return result
		if (result == -1) {
			return IS_LESS_THAN;
		}

		if (result == 0) {
			return IS_EQUAL_TO;
		}

		return IS_GREATER_THAN;
	}

}

