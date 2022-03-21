package com.manulife.pension.ps.web.util.sort;

public abstract class Sorter 
{

public void sortItems (Object [] list, SortTool tool)
{ // default ascending sort
	sortItems(list,tool,false);
}
public abstract void sortItems (Object [] list, SortTool tool, boolean descending);
}
