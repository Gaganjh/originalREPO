package com.manulife.pension.ps.web.util.sort;

public class ShortSortTool implements SortTool
{

public int compare (Object first, Object second)
{
	if ((first != null) && (second != null)
		&& (first instanceof Short) && (second instanceof Short))
	{
		short short1 = ((Short)first).shortValue();
		short short2 = ((Short)second).shortValue();

		if (short1 < short2)
			{return IS_LESS_THAN;}
		else if (short1 > short2)
			{return IS_GREATER_THAN;}
		else
			{return IS_EQUAL_TO;}
	}	
	else
		{throw SortTool.INVALID_ARGS;}
}
}
