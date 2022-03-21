package com.manulife.pension.ps.service.report.plandata.reporthandler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.common.util.Constants;
import com.manulife.pension.ps.service.report.plandata.TpaPlanDataDetails;
import com.manulife.pension.ps.service.report.plandata.dao.TPAPlanDataContractSearchDAO;
import com.manulife.pension.ps.service.report.plandata.util.TPAPlanDataContractComparator;
import com.manulife.pension.ps.service.report.plandata.util.TPAPlanDataContractLastUpdatedTimeComparator;
import com.manulife.pension.ps.service.report.plandata.util.TPAPlanDataContractLastUpdatedUserComparator;
import com.manulife.pension.ps.service.report.plandata.util.TPAPlanDataContractNameComparator;
import com.manulife.pension.ps.service.report.plandata.util.TPAPlanDataContractNumberComparator;
import com.manulife.pension.ps.service.report.plandata.util.TPAPlanDataContractServiceSelectedComparator;
import com.manulife.pension.ps.service.report.plandata.valueobject.TPAPlanDataContract;
import com.manulife.pension.ps.service.report.plandata.valueobject.TPAPlanDataContractSearchReportData;
import com.manulife.pension.service.notices.valueobject.LastUpdatedInfoObject;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.report.valueobject.ReportSortList;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.Pair;

public class TPAPlanDataContractSearchReportHandler implements ReportHandler {
	
	private static final String DESCENDING_INDICATOR = "DESC";
	
	private static Map<String, TPAPlanDataContractComparator> comparators = new HashMap<String, TPAPlanDataContractComparator>();
	static {
		comparators.put(TPAPlanDataContractSearchReportData.SORT_CONTRACT_NAME,
				new TPAPlanDataContractNameComparator());
		comparators.put(TPAPlanDataContractSearchReportData.SORT_CONTRACT_NUMBER,
				new TPAPlanDataContractNumberComparator());
		comparators.put(TPAPlanDataContractSearchReportData.SORT_CREATED_USER,
				new TPAPlanDataContractLastUpdatedUserComparator());
		comparators.put(TPAPlanDataContractSearchReportData.SORT_CREATED_TS,
				new TPAPlanDataContractLastUpdatedTimeComparator());
		comparators.put(TPAPlanDataContractSearchReportData.SERVICE_SELECTED,
				new TPAPlanDataContractServiceSelectedComparator());
	}	
	
	/**
 	 * @see ReportHandler#getReportData(ReportCriteria)
 	 * 
 	 */
	public ReportData getReportData(ReportCriteria criteria) throws SystemException 
	{
	    TPAPlanDataContractSearchReportData reportData =  (TPAPlanDataContractSearchReportData)
				TPAPlanDataContractSearchDAO.getReportData(criteria);	
		
		@SuppressWarnings("unchecked")
		List<TPAPlanDataContract> details = (List<TPAPlanDataContract>) reportData.getDetails();
		
		reportData.setTotalCount(details.size());
		
		// lazy load last updated details if needed
		//if(reportData.isContractCountBelowMaxLimit()) {
			setLastUpdateDetails(details);
		//}
		
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
	private static List<TPAPlanDataContract> page(List<TPAPlanDataContract> items, ReportCriteria criteria) {
		List<TPAPlanDataContract> pageDetails = new ArrayList<TPAPlanDataContract>();
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
	private static void sort(List<TPAPlanDataContract> items,
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
	public static TPAPlanDataContractComparator getComparator(String sortField, String sortDirection) {
	    TPAPlanDataContractComparator comparator = comparators.get(sortField);
		comparator.setAscending(!DESCENDING_INDICATOR.equalsIgnoreCase(sortDirection));
		return comparator;
	}
	
	protected void setLastUpdateDetails(List<TPAPlanDataContract> details)
			throws SystemException {
		
		HashMap<String,LastUpdatedInfoObject> lastUpdatedInfoObjectMap =  ContractServiceDelegate.getInstance().getLastUpdatedDetails();
		lastUpdatedInfoObjectMap=getUserName(lastUpdatedInfoObjectMap);
		for(TPAPlanDataContract item : details) {
//			Pair<String, Timestamp> lastUpdatedUserDetails = TpaPlanDataDetails.getLastUpdateTpaCustomScheduleDetails(
//					item.getContractId(), item.getTpaFirmId());
			if(lastUpdatedInfoObjectMap.containsKey(String.valueOf(item.getContractId()))){
				item.setCreatedUser(lastUpdatedInfoObjectMap.get(String.valueOf(item.getContractId())).getLastUpdatedBy());
				item.setCreatedTS(lastUpdatedInfoObjectMap.get(String.valueOf(item.getContractId())).getLastUpdatedTimestamp());
			}
			
		}
	}
	
	private HashMap<String,LastUpdatedInfoObject>  getUserName(HashMap<String,LastUpdatedInfoObject> lastUpdatedInfoObjectMap) throws SystemException{
		
		HashMap<String,LastUpdatedInfoObject> lastUpdatedInfoObjectUpdatedMap=  new HashMap<String,LastUpdatedInfoObject> ();
		lastUpdatedInfoObjectUpdatedMap.putAll(lastUpdatedInfoObjectMap);
		try{
			for (Entry<String,LastUpdatedInfoObject>  user : lastUpdatedInfoObjectUpdatedMap.entrySet()) {
				if (Constants.SYSTEM_USER_PROFILE_ID.equalsIgnoreCase(user.getValue().getLastUpdatedBy())) {
					user.getValue().setLastUpdatedBy( Constants.ADMINISTRATION);
				
				} else {
					UserInfo userInfo = SecurityServiceDelegate.getInstance().getUserProfileByProfileId(new Long(user.getValue().getLastUpdatedBy()));
					if(null!=userInfo){
						if (userInfo.getRole().isInternalUser()) {
							user.getValue().setLastUpdatedBy(Constants.JOHN_HANCOCK_REPRESENTATIVE);
						} else {
							user.getValue().setLastUpdatedBy(userInfo.getFirstName()+ " " + userInfo.getLastName());
						}
					}
				}
			}
		}
		catch(NumberFormatException nfe){
			throw new SystemException(nfe, "Exception thown in getUserName() method of TPAPlanDataContractSearchReportHandler");
		}
		
		return lastUpdatedInfoObjectUpdatedMap;
	}
	
}
