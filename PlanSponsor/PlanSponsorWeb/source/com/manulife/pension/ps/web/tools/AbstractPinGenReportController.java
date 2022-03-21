package com.manulife.pension.ps.web.tools;

import javax.servlet.http.HttpServletRequest;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;

abstract public class AbstractPinGenReportController extends ReportController {
	protected static final String DEFAULT_SORT_FIELD = "name";
	protected static final String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;
	protected static final int    DEFAULT_PAGE_SIZE = 35;

	protected String getDefaultSort() {
		return DEFAULT_SORT_FIELD;
	}

	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}

	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {
		throw new SystemException("Not supported");
	}

	protected void populateSortCriteria(ReportCriteria criteria,
			BaseReportForm form) {
		
		String sortField = form.getSortField();
		String sortDirection = form.getSortDirection();
		
		if (sortField == null)   sortField = DEFAULT_SORT_FIELD;
		if (sortDirection == null)   sortDirection = DEFAULT_SORT_DIRECTION;
		
		criteria.insertSort(sortField, sortDirection);
	}
	
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request)
			throws SystemException {
	}
	
	/**
	 * If there is already a processing going on, do not allow it
	 * @return
	 */
	abstract protected boolean isAllowed(HttpServletRequest request);
}
