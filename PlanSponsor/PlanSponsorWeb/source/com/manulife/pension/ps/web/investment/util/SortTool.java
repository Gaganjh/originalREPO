package com.manulife.pension.ps.web.investment.util;

/*
  File: SortTool.java

  Version   Date         Author           Change Description
  -------   ----------   --------------   ------------------------------------------------------------------
  CS1.0     2004-01-01   Chris Shin       Initial version.
*/

/**
 * This class is used to support the sorting requirements for ContractFunds
 *
 * @author   Chris Shin
 * @version  CS1.0  (March 1, 2004)
 * @see      com.manulife.pension.ezk.common.util.sort.SortTool.java
 **/
public interface SortTool {
	
	// comparison values
	final int IS_LESS_THAN  = -1;
	final int IS_EQUAL_TO =  0;
	final int IS_GREATER_THAN  =  1;

	final IllegalArgumentException INVALID_ARGS =
		new IllegalArgumentException("Incompatible sort objects");

	final RuntimeException CLIENT_SHOULD_CATCH =
		new RuntimeException("SortTool error");

	int compare	(Object first, Object second);

}

