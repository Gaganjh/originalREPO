package com.manulife.pension.ps.web.util.sort;

public interface SortTool 
{
	// comparison values
	final int IS_LESS_THAN  = -1;
	final int IS_EQUAL_TO =  0;
	final int IS_GREATER_THAN  =  1;

	final IllegalArgumentException INVALID_ARGS =
		new IllegalArgumentException("Incompatible sort objects");

	final RuntimeException CLIENT_SHOULD_CATCH =
		new RuntimeException("SortTool error");

int compare	(Object first, Object second);
}
