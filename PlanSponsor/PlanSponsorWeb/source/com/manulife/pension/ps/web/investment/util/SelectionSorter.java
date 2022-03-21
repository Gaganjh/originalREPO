package com.manulife.pension.ps.web.investment.util;

/**
 * This class is used to support the sorting requirements for ContractFunds
 *
 * @author   Chris Shin
 * @version  CS1.0  (March 1, 2004)
 * @see      com.manulife.pension.ezk.common.util.sort.SelectionSorter.java
 **/
public class SelectionSorter extends Sorter {
	
	public void sortItems (Object [] list, SortTool tool, boolean descending) {

		int compareMethod = descending ?
				SortTool.IS_GREATER_THAN :
				SortTool.IS_LESS_THAN;

		for (int i = 0; i < list.length - 1; ++i) {
			int min = i;

			for (int j = i + 1; j < list.length; ++j) {
				if (tool.compare(list[j],list[min]) == compareMethod)
					min = j;
			}

			Object t  = list[min];
			list[min] = list[i];
			list[i]   = t;
		}
	}

}

