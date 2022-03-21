package com.manulife.pension.ps.service.report.submission.reporthandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.submission.valueobject.CensusSubmissionReportData;
import com.manulife.pension.ps.service.report.submission.valueobject.SubmissionHistoryReportData;
import com.manulife.pension.ps.service.submission.dao.CensusSubmissionDAO;
import com.manulife.pension.ps.service.submission.util.CensusSubmissionItemComparatorFactory;
import com.manulife.pension.service.contract.dao.ContractDAO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.report.valueobject.ReportSortList;

/**
 * @author parkand
 *
 */
public class CensusSubmissionReportHandler implements ReportHandler {


	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.service.report.handler.ReportHandler#getReportData(com.manulife.pension.service.report.valueobject.ReportCriteria)
	 */
	public ReportData getReportData(ReportCriteria criteria)
			throws SystemException, ReportServiceException {

		CensusSubmissionReportData reportData = null;
		reportData = CensusSubmissionDAO.getCensusSubmissions(criteria);
		List details = (List) reportData.getDetails();
		reportData.setTotalCount(details == null ? 0 : details.size());
		//sort(details, criteria.getSorts());
		reportData.setSortCode(ContractDAO.getSortOptionCode(((Integer)criteria.getFilterValue(SubmissionHistoryReportData.FILTER_CONTRACT_NO)).intValue()));
		reportData.setDetails(page(details, criteria));
		return reportData;	
	}
	/**
	 * Return only the items for the current page and page size.
	 * 
	 * @param items the list to paginate
	 * @param criteria to get the paging info from
	 * 
	 * @return
	 */
	private static List page(List items, ReportCriteria criteria) {
		
		if (ReportCriteria.NOLIMIT_PAGE_SIZE == criteria.getPageSize()) {
			return items;
		}
		
		List pageDetails = new ArrayList(criteria.getPageSize());
		
		if (items != null) {
			for (int i = criteria.getStartIndex() - 1; 
			i < criteria.getPageNumber() * criteria.getPageSize() && i < items.size(); i++) {
				pageDetails.add(items.get(i));
			}
		}
		return pageDetails;
	}

	/**
	 * Sort the list according to the required sort order.
	 * 
	 * @param items the list to sort
	 * @param sorts the sorting fields list
	 */
	private static void sort(List items, ReportSortList sorts) {
		if (items != null && sorts != null && sorts.size() != 0) {
			ReportSort firstSort = (ReportSort) sorts.get(0);
			Collections.sort(items, 
					CensusSubmissionItemComparatorFactory.getInstance().getComparator(firstSort.getSortField(), firstSort.getSortDirection()));
		}
	}


}
