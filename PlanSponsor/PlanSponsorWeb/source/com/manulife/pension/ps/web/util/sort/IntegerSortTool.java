package com.manulife.pension.ps.web.util.sort;

public class IntegerSortTool implements SortTool
{

public int compare (Object first, Object second)
{
	if ((first != null) && (second != null)
		&& (first instanceof Integer) && (second instanceof Integer))
	{
		int int1 = ((Integer)first).intValue();
		int int2 = ((Integer)second).intValue();

		if (int1 < int2)
			{return IS_LESS_THAN;}
		else if (int1 > int2)
			{return IS_GREATER_THAN;}
		else
			{return IS_EQUAL_TO;}
	}	
	else
		{throw SortTool.INVALID_ARGS;}
}
}
