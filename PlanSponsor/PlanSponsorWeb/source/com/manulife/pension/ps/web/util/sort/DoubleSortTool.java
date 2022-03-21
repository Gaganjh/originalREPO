package com.manulife.pension.ps.web.util.sort;

public class DoubleSortTool implements SortTool
{

public int compare (Object first, Object second)
{
	if ((first != null) && (second != null)
		&& (first instanceof Double) && (second instanceof Double))
	{
		double double1 = ((Double)first).doubleValue();
		double double2 = ((Double)second).doubleValue();

		if (double1 < double2)
			{return IS_LESS_THAN;}
		else if (double1 > double2)
			{return IS_GREATER_THAN;}
		else
			{return IS_EQUAL_TO;}
	}	
	else
		{throw SortTool.INVALID_ARGS;}
}
}
