package com.manulife.pension.ps.service.withdrawal.reporthandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.withdrawal.dao.LoanAndWithdrawalDAO;
import com.manulife.pension.ps.service.withdrawal.dao.SearchParticipantWithdrawalDAO;
import com.manulife.pension.ps.service.withdrawal.valueobject.LoanAndWithdrawalItem;
import com.manulife.pension.ps.service.withdrawal.valueobject.LoanAndWithdrawalReportData;
import com.manulife.pension.ps.service.withdrawal.valueobject.SearchParticipantWithdrawalReportData;
import com.manulife.pension.ps.service.withdrawal.valueobject.SearchParticipantWithdrawalRequestItem;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.report.valueobject.ReportSortList;

public class SearchParticipantWithdrawalReportHandler implements ReportHandler {

	public ReportData getReportData(ReportCriteria reportCriteria) 	throws SystemException, ReportServiceException {

        SearchParticipantWithdrawalReportData reportData = null;
        reportData = SearchParticipantWithdrawalDAO.getParticipant(reportCriteria);
        List details = (List) reportData.getDetails();
     //   filter(details, reportCriteria);
        reportData.setTotalCount(details == null ? 0 : details.size());
        sort(details, reportCriteria.getSorts());
        reportData.setDetails(page(details, reportCriteria));
        return reportData;  
		
	}
    
    /**
     * Return only the items for the current page and page size.
     * 
     * @param items the list to paginate
     * @param criteria to get the paging info from
     * 
     * @return a list of page details
     */
    private static List page(List items, ReportCriteria criteria) {
        List pageDetails = new ArrayList();
        if (items != null) {
            if (criteria.getPageSize() == ReportCriteria.NOLIMIT_PAGE_SIZE) {
                pageDetails.addAll(items);
            } else {
            
                for (int i = criteria.getStartIndex() - 1; 
                i < criteria.getPageNumber() * criteria.getPageSize() && i < items.size(); i++) {
                    pageDetails.add(items.get(i));
                }
            }
        }
        return pageDetails;
    }

    /**
     * Sort the list according to the required sort order.
     * 
     * @param items the list of items to sort
     * @param sorts the sorting fields list
     */
    private static void sort(List items, ReportSortList sorts) {
        
        if (items != null && sorts != null && sorts.size() != 0) {
            ReportSort firstSort = (ReportSort) sorts.get(0);
            Collections.sort(items, 
                    SearchParticipantWithdrawalRequestItem.getComparator(firstSort.getSortField(), firstSort.getSortDirection()));
        }
    }

}
