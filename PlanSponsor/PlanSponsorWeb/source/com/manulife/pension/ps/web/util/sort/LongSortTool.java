package com.manulife.pension.ps.web.util.sort;

public class LongSortTool implements SortTool
{

public int compare (Object first, Object second)
{
	if ((first != null) && (second != null)
		&& (first instanceof Long) && (second instanceof Long))
	{
		long long1 = ((Long)first).longValue();
		long long2 = ((Long)second).longValue();

		if (long1 < long2)
			{return IS_LESS_THAN;}
		else if (long1 > long2)
			{return IS_GREATER_THAN;}
		else
			{return IS_EQUAL_TO;}
	}	
	else
		{throw SortTool.INVALID_ARGS;}
}
}
