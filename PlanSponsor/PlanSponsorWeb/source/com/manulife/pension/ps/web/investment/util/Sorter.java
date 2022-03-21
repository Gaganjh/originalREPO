package com.manulife.pension.ps.web.investment.util;

/**
 * This class is used to support the sorting requirements for ContractFunds
 *
 * @author   Chris Shin
 * @version  CS1.0  (March 1, 2004)
 * @see      com.manulife.pension.ezk.common.util.sort.Sorter.java
 **/
public abstract class Sorter {
	
	public void sortItems (Object [] list, SortTool tool)
	{ // default ascending sort
		sortItems(list,tool,false);
	}
	public abstract void sortItems (Object [] list, SortTool tool, boolean descending);

}

