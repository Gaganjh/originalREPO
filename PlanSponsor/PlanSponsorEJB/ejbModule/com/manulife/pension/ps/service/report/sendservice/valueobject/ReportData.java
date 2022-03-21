package com.manulife.pension.ps.service.report.sendservice.valueobject;

import java.io.Serializable;
import java.util.Collection;

import com.manulife.pension.service.common.valueobject.ReportCriteria;
import com.manulife.pension.util.StaticHelperClass;

/**
 * 
 * Base Report Data for Send Service
 * 
 * @author Dheepa
 *
 */

public class ReportData implements Serializable {

	private static final long serialVersionUID = 1L;
	protected ReportCriteria criteria;
	protected int totalCount;
	protected Collection details;
	protected boolean hasMore;

	public ReportData() {
	}

	public ReportData(ReportCriteria criteria, int totalCount) {
		super();
		this.criteria = criteria;
		this.totalCount = totalCount;
	}

	/**
	 * Gets the reportCriteria
	 * 
	 * @return Returns a ReportCriteria
	 */
	public ReportCriteria getReportCriteria() {
		return criteria;
	}
	/**
	 * Sets the reportCriteria
	 * 
	 * @param reportCriteria
	 *            The reportCriteria to set
	 */
	public void setReportCriteria(ReportCriteria criteria) {
		this.criteria = criteria;
	}

	/**
	 * Gets the startIndex
	 * 
	 * @return Returns a int
	 */
	public int getStartIndex() {
		return criteria.getStartIndex();
	}

	/**
	 * Gets the stopIndex
	 * 
	 * @return Returns a int
	 */
	public int getStopIndex() {
		if (criteria.getPageSize() == ReportCriteria.NOLIMIT_PAGE_SIZE) {
			return totalCount;
		}
		return Math.min(totalCount, getStartIndex() + criteria.getPageSize()
				- 1);
	}

	/**
	 * Gets the totalCount
	 * 
	 * @return Returns a int
	 */
	public int getTotalCount() {
		return totalCount;
	}
	/**
	 * Sets the totalCount
	 * 
	 * @param totalCount
	 *            The totalCount to set
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * Gets the details
	 * 
	 * @return Returns a Collection
	 */
	public Collection getDetails() {
		return details;
	}
	/**
	 * Sets the details
	 * 
	 * @param details
	 *            The details to set
	 */
	public void setDetails(Collection details) {
		this.details = details;
	}

	/**
	 * calculates the totalPageCount
	 * 
	 * @return Returns an int
	 */
	public int getTotalPageCount() {
		if (totalCount == 0)
			return 0;
		if (getReportCriteria().getPageSize() == ReportCriteria.NOLIMIT_PAGE_SIZE)
			return 1;
		return totalCount
				/ getReportCriteria().getPageSize()
				+ ((totalCount % getReportCriteria().getPageSize() == 0)
						? 0
						: 1);
	}

	public String toString() 
	{
		return StaticHelperClass.toString(this);
	}

	public boolean getHasMore() {
		return hasMore;
	}

	public void setHasMore(boolean hasMore) {
		this.hasMore = hasMore;
	}
}


