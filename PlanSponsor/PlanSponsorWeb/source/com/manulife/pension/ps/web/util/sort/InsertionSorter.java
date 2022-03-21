package com.manulife.pension.ps.web.util.sort;

public class InsertionSorter extends Sorter
{

public void sortItems (Object [] list, SortTool tool, boolean descending)
{
	int compareMethod = descending ?
			SortTool.IS_LESS_THAN :
			SortTool.IS_GREATER_THAN;

	for (int i = 1; i < list.length; ++i)
	{
		Object t = list[i];

		int j = i;

		while ((j > 0) && (tool.compare(list[j-1],t) == compareMethod))
		{
			list[j] = list[j-1];
			--j;
		}

		list[j] = t;
	}
}
}
