package com.manulife.pension.ps.web.util.sort;

public class QuickSorter extends Sorter
{
	final static IllegalArgumentException
		STACK_OVERFLOW_EXCEPTION = new IllegalArgumentException("stack overflow in QuickSort");

public void sortItems (Object [] list, SortTool tool, boolean descending)
{
	// create stack
	final int stackSize = 32;
	StackItem [] stack = new StackItem [stackSize];

	for (int n = 0; n < 32; ++n)
	{
		stack[n] = new StackItem();
	}	

	int stackPtr = 0;

	// determine direction of sort
	int compareMethod = descending ?
			SortTool.IS_GREATER_THAN :
			SortTool.IS_LESS_THAN;

	// size of minimum partition to median-of-three
	final int Threshold = 7;

	// sizes of left and right partitions
	int lsize, rsize;

	// create working indexes
	int l, r, mid, scanl, scanr, pivot;

	// set initial values
	l = 0;
	r = list.length - 1;

	Object temp;
	
	// main loop
	while (true)
	{
		while (r > l)
		{
			if ((r - l) > Threshold)
			{
				// "median-of-three" partitioning
				mid = (l + r) / 2;

				// three-sort left, middle, and right elements
				if (tool.compare(list[mid],list[l]) == compareMethod)
				{
					temp      = list[mid];
					list[mid] = list[l];
					list[l]   = temp;
				}

				if (tool.compare(list[r],list[l]) == compareMethod)
				{
					temp    = list[r];
					list[r] = list[l];
					list[l] = temp;
				}

				// three-sort left, middle, and right elements
				if (tool.compare(list[r],list[mid]) == compareMethod)
				{
					temp      = list[mid];
					list[mid] = list[r];
					list[r]   = temp;
				}

				// set-up for partitioning
				pivot = r - 1;
	
				temp        = list[mid];
				list[mid]   = list[pivot];
				list[pivot] = temp;
	
				scanl = l + 1;
				scanr = r - 2;
			}
			else
			{
				// set-up for partitioning
				pivot = r;
				scanl = l;
				scanr = r - 1;
			}
	
			for (;;)
			{
				// scan from left for element >= to pivot
				while ((tool.compare(list[scanl],list[pivot]) == compareMethod) && (scanl < r))
				{
					++scanl;
				}	
	
				// scan from right for element <= to pivot
				while ((tool.compare(list[pivot],list[scanr]) == compareMethod) && (scanr > l))
				{
					--scanr;
				}	
	
				// if scans have met, exit inner loop
				if (scanl >= scanr)
				{
					break;
				}	
	
				// exchange elements
				temp        = list[scanl];
				list[scanl] = list[scanr];
				list[scanr] = temp;
	
				if (scanl < r)
				{
					++scanl;
				}	
	
				if (scanr > l)
				{
					--scanr;
				}	
			}
	
			// exchange final element
			temp        = list[scanl];
			list[scanl] = list[pivot];
			list[pivot] = temp;
	
			// place largest partition on stack
			lsize = scanl - l;
			rsize = r - scanl;
	
			if (lsize > rsize)
			{
				if (lsize != 1)
				{
					++stackPtr;
	
					if (stackPtr == stackSize)
					{
						throw STACK_OVERFLOW_EXCEPTION;
					}	
	
					stack[stackPtr].left  = l;
					stack[stackPtr].right = scanl - 1;
				}
	
				if (rsize != 0)
				{
					l = scanl + 1;
				}	
				else
				{
					break;
				}	
			}
			else
			{
				if (rsize != 1)
				{
					++stackPtr;
	
					if (stackPtr == stackSize)
					{
				   	throw STACK_OVERFLOW_EXCEPTION;
				   }	
	
					stack[stackPtr].left  = scanl + 1;
					stack[stackPtr].right = r;
				}
	
				if (lsize != 0)
				{
					r = scanl - 1;
				}	
				else
				{
					break;
				}	
			}
		}
	
		// iterate with values from stack
		if (stackPtr != 0)
		{
			l = stack[stackPtr].left;
			r = stack[stackPtr].right;
	
			--stackPtr;
		}
		else
		{
			break;
		}	
	}
}
}
