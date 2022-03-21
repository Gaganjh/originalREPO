package com.manulife.pension.ps.service.report.feeSchedule.reporthandler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.feeSchedule.TpaFeeScheduleDetails;
import com.manulife.pension.ps.service.report.feeSchedule.dao.TPAFeeScheduleContractSearchDAO;
import com.manulife.pension.ps.service.report.feeSchedule.util.TPAFeeScheduleContractComparator;
import com.manulife.pension.ps.service.report.feeSchedule.util.TPAFeeScheduleContractLastUpdatedTimeComparator;
import com.manulife.pension.ps.service.report.feeSchedule.util.TPAFeeScheduleContractLastUpdatedUserComparator;
import com.manulife.pension.ps.service.report.feeSchedule.util.TPAFeeScheduleContractNameComparator;
import com.manulife.pension.ps.service.report.feeSchedule.util.TPAFeeScheduleContractNumberComparator;
import com.manulife.pension.ps.service.report.feeSchedule.util.TPAFeeScheduleContractScheduleTypeComparator;
import com.manulife.pension.ps.service.report.feeSchedule.valueobject.TPAFeeScheduleContract;
import com.manulife.pension.ps.service.report.feeSchedule.valueobject.TPAFeeScheduleContractSearchReportData;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.report.valueobject.ReportSortList;
import com.manulife.pension.util.Pair;

public class TPAFeeScheduleContractSearchReportHandler implements ReportHandler {
	
	private static final String DESCENDING_INDICATOR = "DESC";
	
	private static Map<String, TPAFeeScheduleContractComparator> comparators = new HashMap<String, TPAFeeScheduleContractComparator>();
	static {
		comparators.put(TPAFeeScheduleContractSearchReportData.SORT_CONTRACT_NAME,
				new TPAFeeScheduleContractNameComparator());
		comparators.put(TPAFeeScheduleContractSearchReportData.SORT_CONTRACT_NUMBER,
				new TPAFeeScheduleContractNumberComparator());
		comparators.put(TPAFeeScheduleContractSearchReportData.SORT_FEE_SCHEDULE,
				new TPAFeeScheduleContractScheduleTypeComparator());
		comparators.put(TPAFeeScheduleContractSearchReportData.SORT_CREATED_USER,
				new TPAFeeScheduleContractLastUpdatedUserComparator());
		comparators.put(TPAFeeScheduleContractSearchReportData.SORT_CREATED_TS,
				new TPAFeeScheduleContractLastUpdatedTimeComparator());
	}
	
	
	/**
 	 * @see ReportHandler#getReportData(ReportCriteria)
 	 * 
 	 */
	public ReportData getReportData(ReportCriteria criteria) throws SystemException 
	{
		TPAFeeScheduleContractSearchReportData reportData =  (TPAFeeScheduleContractSearchReportData)
				TPAFeeScheduleContractSearchDAO.getReportData(criteria);	
		
		@SuppressWarnings("unchecked")
		List<TPAFeeScheduleContract> details = (List<TPAFeeScheduleContract>) reportData.getDetails();
		
		reportData.setTotalCount(details.size());
		
		// lazy load last updated details if needed
		if(reportData.isContractCountBelowMaxLimit()) {
			setLastUpdateDetails(details);
		}
		
		sort(details, criteria.getSorts());
		reportData.setDetails(page(details, criteria));
		
		return reportData;
		
	}

	/**
	 * Return only the items for the current page and page size.
	 * 
	 * @param items
	 *            the list to paginate
	 * @param criteria
	 *            to get the paging info from
	 * 
	 * @return
	 */
	private static List<TPAFeeScheduleContract> page(List<TPAFeeScheduleContract> items, ReportCriteria criteria) {
		List<TPAFeeScheduleContract> pageDetails = new ArrayList<TPAFeeScheduleContract>();
		if (items != null) {
			if (criteria.getPageSize() == ReportCriteria.NOLIMIT_PAGE_SIZE) {
				pageDetails.addAll(items);
			} else {
				for (int i = criteria.getStartIndex() - 1; i < criteria
						.getPageNumber() * criteria.getPageSize()
						&& i < items.size(); i++) {
					pageDetails.add(items.get(i));
				}
			}
		}
		return pageDetails;
	}

	/**
	 * Sort the list according to the required sort order.
	 * 
	 * @param items
	 *            the list to sort
	 * @param sorts
	 *            the sorting fields list
	 */
	private static void sort(List<TPAFeeScheduleContract> items,
			ReportSortList sorts) {
		if (items != null && sorts != null && sorts.size() != 0) {
			ReportSort firstSort = (ReportSort) sorts.get(0);
			Collections.sort(items, getComparator(
					firstSort.getSortField(), firstSort.getSortDirection()));
		}
	}
	
	

	/**
	 * Returns a compartor for the given sort field and direction
	 * @param sortField the sort field
	 * @param sortDirection the sort direction ASC or DESC
	 * @return the Compartor
	 */
	public static TPAFeeScheduleContractComparator getComparator(String sortField, String sortDirection) {
		TPAFeeScheduleContractComparator comparator = comparators.get(sortField);
		comparator.setAscending(!DESCENDING_INDICATOR.equalsIgnoreCase(sortDirection));
		return comparator;
	}
	
	protected void setLastUpdateDetails(List<TPAFeeScheduleContract> details)
			throws SystemException {
		for(TPAFeeScheduleContract item : details) {
			Pair<String, Timestamp> lastUpdatedUserDetails = TpaFeeScheduleDetails.getLastUpdateTpaCustomScheduleDetails(
					item.getContractId(), item.getTpaFirmId());
			item.setCreatedUser(lastUpdatedUserDetails.getFirst());
			item.setCreatedTS(lastUpdatedUserDetails.getSecond());
		}
	}
}
