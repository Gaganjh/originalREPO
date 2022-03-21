package com.manulife.pension.ps.service.report.feeSchedule.reporthandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.feeSchedule.dao.FeeScheduleChangeHistoryDAO;
import com.manulife.pension.ps.service.report.feeSchedule.valueobject.ContractFeeScheduleChangeHistoryReportData;
import com.manulife.pension.ps.service.report.feeSchedule.valueobject.ContractFeeScheduleChangeHistoryReportData.FilterSections;
import com.manulife.pension.ps.service.report.feeSchedule.valueobject.FeeScheduleChangeItem;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.report.valueobject.ReportSortList;

/**
 * 
 * Report Handler for 404a5 Change History Page
 * 
 * @author Siby Thomas
 *
 */
public final class ContractFeeScheduleChangeHistoryReportHandler implements
		ReportHandler {

	/**
	 * Gets the report data.
	 * 
	 * @see com.manulife.pension.service.report.handler.ReportHandler#getReportData(com.manulife.pension.service.report.valueobject.ReportCriteria)
	 */
	public ReportData getReportData(ReportCriteria criteria)
			throws SystemException {

		ContractFeeScheduleChangeHistoryReportData reportData = new ContractFeeScheduleChangeHistoryReportData(criteria, 0);
		List<FeeScheduleChangeItem> details = new ArrayList<FeeScheduleChangeItem>();
		reportData.setDetails(details);
		
		List<FilterSections> sections = getFilterSections(criteria);
		
		for(FilterSections section : sections) {
			switch(section) {
			case DimSection :
				 details.addAll(getDimSectionData(criteria));
				 continue;
			case PbaDetailsSection:
				 details.addAll(getPbaDetailsSectionData(criteria));
				 continue;
			case FeeSection :
				 details.addAll(getFeeSectionData(criteria));
				 continue;	 
			}
		}

		reportData.setTotalCount(details.size());
		sort(details, criteria.getSorts());
		reportData.setDetails(page(details, criteria));

		return reportData;
	}
	
	/**
	 * 
	 * @param criteria
	 * @return List<FeeScheduleChangeItem> 
	 * @throws SystemException
	 */
	private List<FeeScheduleChangeItem> getDimSectionData(ReportCriteria criteria) throws SystemException {
		List<FeeScheduleChangeItem> dimHistory = FeeScheduleChangeHistoryDAO.getDimHistory(criteria);
		Map<String, String>  users = getFilteredUserProfiles(criteria);
		if(!users.isEmpty()) {
			dimHistory = filterByUserNames(dimHistory, users);
		}
		return dimHistory;
	}
	
	
	/**
	 * 
	 * @param criteria
	 * @return List<FeeScheduleChangeItem> 
	 * @throws SystemException
	 */
	private List<FeeScheduleChangeItem> getPbaDetailsSectionData(ReportCriteria criteria) throws SystemException {
		List<FeeScheduleChangeItem> pbaHistory = FeeScheduleChangeHistoryDAO.getPBADetailsHistory(criteria);
		Map<String, String>  users = getFilteredUserProfiles(criteria);
		if(!users.isEmpty()) {
			pbaHistory = filterByUserNames(pbaHistory, users);
		}
		return pbaHistory;
	}
	
	/**
	 * 
	 * @param criteria
	 * @return List<FeeScheduleChangeItem> 
	 * @throws SystemException
	 */
	private List<FeeScheduleChangeItem> getFeeSectionData(ReportCriteria criteria)
			throws SystemException {
		List<FeeScheduleChangeItem> feeHistory = FeeScheduleChangeHistoryDAO.getFeeHistory(criteria);
		Map<String, String> users = getFilteredUserProfiles(criteria);
		if (!users.isEmpty()) {
			feeHistory = filterByUserNames(feeHistory, users);
		}
		String stdSchdeuleAppliedInd = getFilteredStandardScheduleInd(criteria);
		if(stdSchdeuleAppliedInd != null) {
			feeHistory = filterByStandardScheduleAppliedInd(feeHistory, stdSchdeuleAppliedInd);
		}
		return feeHistory;
	}
	
	@SuppressWarnings("unchecked")
	private static Map<String, String> getFilteredUserProfiles(ReportCriteria criteria) {
		if(criteria.getFilterValue(ContractFeeScheduleChangeHistoryReportData.FILTER_USER_NAME) != null) {
			return (Map<String, String>)criteria.getFilterValue(ContractFeeScheduleChangeHistoryReportData.FILTER_USER_NAME);
		}
		return  new HashMap<String, String>();
	}
	
	private static String getFilteredStandardScheduleInd(ReportCriteria criteria) {
		if(criteria.getFilterValue(ContractFeeScheduleChangeHistoryReportData.FILTER_STD_SCHEDULE_APPLIED_IND) != null) {
			return (String) criteria.getFilterValue(ContractFeeScheduleChangeHistoryReportData.FILTER_STD_SCHEDULE_APPLIED_IND);
		}
		return  null;
	}
	
	@SuppressWarnings("unchecked")
	private static List<FilterSections> getFilterSections(ReportCriteria criteria) {
		if(criteria.getFilterValue(ContractFeeScheduleChangeHistoryReportData.FILTER_SECTION) != null) {
			return (List<FilterSections>)criteria.getFilterValue(ContractFeeScheduleChangeHistoryReportData.FILTER_SECTION);
		}
		return  new ArrayList<FilterSections>();
	}
	
	/**
	 * 
	 * @param items
	 * @param searchedUsers
	 * @return List<FeeScheduleChangeItem>
	 */
	private List<FeeScheduleChangeItem> filterByUserNames( List<FeeScheduleChangeItem> items , Map<String, String> searchedUsers) {
		List<FeeScheduleChangeItem> filteredList = new ArrayList<FeeScheduleChangeItem>();
		for(FeeScheduleChangeItem changeItem : items) {
			String userName = searchedUsers.get(changeItem.getUserId());
			if(userName != null){
				changeItem.setUserName(userName);
				filteredList.add(changeItem);
			}
		}
		return filteredList;
	}
	
	/**
	 * 
	 * @param items
	 * @param searchedUsers
	 * @return List<FeeScheduleChangeItem>
	 */
	private List<FeeScheduleChangeItem> filterByStandardScheduleAppliedInd( List<FeeScheduleChangeItem> items , String filterCriteria) {
		List<FeeScheduleChangeItem> filteredList = new ArrayList<FeeScheduleChangeItem>();
		for(FeeScheduleChangeItem changeItem : items) {
			if(StringUtils.equals(filterCriteria, changeItem.getStandardScheduleAppliedValue())){
				filteredList.add(changeItem);
			}
		}
		return filteredList;
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
	private static List<FeeScheduleChangeItem> page(List<FeeScheduleChangeItem> items, ReportCriteria criteria) {
		List<FeeScheduleChangeItem> pageDetails = new ArrayList<FeeScheduleChangeItem>();
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
	private static void sort(List<FeeScheduleChangeItem> items,
			ReportSortList sorts) {
		if (items != null && sorts != null && sorts.size() != 0) {
			ReportSort firstSort = (ReportSort) sorts.get(0);
			Collections.sort(items, FeeScheduleChangeItem.getComparator(
					firstSort.getSortField(), firstSort.getSortDirection()));
		}
	}
}