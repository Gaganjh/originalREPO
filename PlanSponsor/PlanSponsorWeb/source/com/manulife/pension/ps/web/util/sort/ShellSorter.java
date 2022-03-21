package com.manulife.pension.ps.web.util.sort;

public class ShellSorter extends Sorter
{

public void sortItems (Object [] list, SortTool tool, boolean descending)
{
	int increment;

	int compareMethod = descending ?
			SortTool.IS_LESS_THAN :
			SortTool.IS_GREATER_THAN;

	for (increment = 1; increment <= list.length / 9; increment = 3 * increment + 1) ;
	{
		for ( ; increment > 0; increment /= 3)
		{
			for (int i = increment + 1; i <= list.length; i += increment)
			{
				Object t = list[i - 1];

				int j = i;

				while ((j > increment) && (tool.compare(list[j - increment - 1],t) == compareMethod))
				{
					list[j - 1] = list[j - increment - 1];
					j -= increment;
				}

			list[j - 1] = t;
			}
		}
	}	
}
}
