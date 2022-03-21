package com.manulife.pension.bd.web.estatement;

import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class EstatementReportData extends ReportData {

	int pageSize = 1;
	int pageNumber = 1;
	
	public EstatementReportData(int totalCount, int pageSize, int pageNumber) {
		super(null, totalCount);
		this.pageSize = pageSize;
		this.pageNumber = pageNumber;
	}
	
	/**
	 * calculates the totalPageCount
	 * 
	 * @return Returns an int
	 */
	public int getTotalPageCount() {
		if (totalCount == 0)
			return 0;
		if (pageSize == ReportCriteria.NOLIMIT_PAGE_SIZE)
			return 1;
		return totalCount
				/ pageSize
				+ ((totalCount % pageSize == 0)
						? 0
						: 1);
	}
	
	/**
	 * Gets the stopIndex
	 * 
	 * @return Returns a int
	 */
	public int getStopIndex() {
		if (pageSize == ReportCriteria.NOLIMIT_PAGE_SIZE) {
			return totalCount;
		}
		return Math.min(totalCount, getStartIndex() + pageSize
				- 1);
	}
	
	public int getStartIndex() {
		return (pageNumber - 1) * pageSize + 1;
	}
}
