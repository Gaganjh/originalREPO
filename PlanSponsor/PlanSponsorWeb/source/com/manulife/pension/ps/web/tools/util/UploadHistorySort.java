package com.manulife.pension.ps.web.tools.util;

import com.manulife.pension.lp.model.gft.GFTUploadHistorySummary;
import com.manulife.pension.ps.web.util.sort.QuickSorter;

/**
 * @author drotele
 * Created on Mar 25, 2004
 *
 */
public class UploadHistorySort {

	private final QuickSorter sorter = new QuickSorter();
	/**
	 * 
	 */
	public UploadHistorySort() {
		super();
	}

	public GFTUploadHistorySummary[] sortGFTUploadHistorySummaryArray( int sortFlag,
		GFTUploadHistorySummary[] summaries, boolean direction)
		throws Throwable {
		
		if (summaries != null && summaries.length > 1) {
			UploadHistorySortTool sortTool =
				new UploadHistorySortTool(sortFlag);
//			boolean direction =
//				(sortTool
//					.compare(summaries[summaries.length - 1], summaries[0])
//					> 0);
			this.sorter.sortItems(summaries, sortTool, direction);
		}
		return summaries;
	}

}
