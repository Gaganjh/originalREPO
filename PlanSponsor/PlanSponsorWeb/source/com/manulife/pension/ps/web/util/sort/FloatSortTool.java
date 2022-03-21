package com.manulife.pension.ps.web.util.sort;

public class FloatSortTool implements SortTool
{

public int compare (Object first, Object second)
{
	if ((first != null) && (second != null)
		&& (first instanceof Float) && (second instanceof Float))
	{
		float Float1 = ((Float)first).floatValue();
		float Float2 = ((Float)second).floatValue();

		if (Float1 < Float2)
			{return IS_LESS_THAN;}
		else if (Float1 > Float2)
			{return IS_GREATER_THAN;}
		else
			{return IS_EQUAL_TO;}
	}	
	else
		{throw SortTool.INVALID_ARGS;}
}
}
