package com.manulife.pension.ps.web.util.sort;

public class StringSortTool implements SortTool
{

public int compare (Object first, Object second)
{
	if ((first != null) && (second != null)
		&& (first instanceof String) && (second instanceof String))
	{
		int c = ((String)first).compareTo((String)second);

		if (c < 0)
			{return IS_LESS_THAN;}
		else if (c > 0)
			{return IS_GREATER_THAN;}
		else
			{return IS_EQUAL_TO;}
	}	
	else
		{throw SortTool.INVALID_ARGS;}
}
}
