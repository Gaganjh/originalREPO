package com.manulife.pension.ps.service.report.submission.reporthandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.submission.valueobject.SubmissionHistoryReportData;
import com.manulife.pension.ps.service.submission.dao.SubmissionDAO;
import com.manulife.pension.ps.service.submission.valueobject.MoneySource;
import com.manulife.pension.ps.service.submission.valueobject.SubmissionHistoryItem;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.report.valueobject.ReportSortList;

/**
 * The Submission History Report Handler.
 * 
 * @author Tony Tomasone
 */
public final class SubmissionHistoryReportHandler implements ReportHandler {  
	
	private static final String DRAFT = "14";
    private static final String CONTRIBUTION = "Contribution";
    private static final String FORFEITURE_CONTRIBUTION = "Forfeiture contribution";
    private static final String REINSTATEMENT_CONTRIBUTION = "Reinstatement of contribution";
    private static final String TRANSFER_CONTRIBUTION = "X";
    private static final String REGULAR_CONTRIBUTION = "C";
    
	/**
	 * Gets the report data.
	 * @see com.manulife.pension.service.report.handler.ReportHandler#getReportData(com.manulife.pension.service.report.valueobject.ReportCriteria)
	 */
	public ReportData getReportData(ReportCriteria criteria) throws SystemException {

		SubmissionHistoryReportData reportData = null;
		
		// make a copy of the type filter
		String typeFilter = (String)criteria.getFilterValue(SubmissionHistoryReportData.FILTER_TYPE);
		boolean typeFilterReplaced = false;
		if ( typeFilter != null && typeFilter.length() > 1) {
			/*
			 * if the type filter is defined and greater than 1 character,
			 * the user has requested a filter based on money type.
			 * need to replace the filter value with 'C' in this case so that the query will work
			 * filtering based on money type will occur in post-processing in the filter() method below 
			*/
            /**
             * Is going to return FORFEITURE_CONTRIBUTION, REINSTATEMENT_CONTRIBUTION and REGULAR_CONTRIBUTION
             */
			criteria.removeFilter(SubmissionHistoryReportData.FILTER_TYPE);
			criteria.addFilter(SubmissionHistoryReportData.FILTER_TYPE, REGULAR_CONTRIBUTION);
			typeFilterReplaced = true;
		}
		
		reportData = SubmissionDAO.getSubmissions(criteria);
		
		if ( typeFilterReplaced ) {
			// convert the type filter back to the original so we can post-filter on money source type
			criteria.removeFilter(SubmissionHistoryReportData.FILTER_TYPE);
			criteria.addFilter(SubmissionHistoryReportData.FILTER_TYPE,typeFilter);
		}
		
		List details = (List) reportData.getDetails();
        if(typeFilterReplaced) {
            filterByMoneySource(details, criteria);
        } else if(REGULAR_CONTRIBUTION.equalsIgnoreCase(typeFilter)) {
            // Filter FORFEITURE_CONTRIBUTION and REINSTATEMENT_CONTRIBUTION out of REGULAR_CONTRIBUTION
            // based on Money Type
            filterOutForfeitureOrReinstatement(details, criteria);
        }
        
        // Special processing needed for Contribution
        if(typeFilterReplaced && CONTRIBUTION.equalsIgnoreCase(typeFilter)) {
            // Need to add the Transfer contributions as well
            criteria.removeFilter(SubmissionHistoryReportData.FILTER_TYPE);
            criteria.addFilter(SubmissionHistoryReportData.FILTER_TYPE, TRANSFER_CONTRIBUTION);
            
            reportData = SubmissionDAO.getSubmissions(criteria);
            details.addAll(reportData.getDetails());
        }
        
        
		reportData.setTotalCount(details == null ? 0 : details.size());
		sort(details, criteria.getSorts());
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
	 * @param items the list to sort
	 * @param sorts the sorting fields list
	 */
	private static void sort(List items, ReportSortList sorts) {
		if (items != null && sorts != null && sorts.size() != 0) {
			ReportSort firstSort = (ReportSort) sorts.get(0);
			Collections.sort(items, 
					SubmissionHistoryItem.getComparator(firstSort.getSortField(), firstSort.getSortDirection()));
		}
	}
        
    /**
     * Apply non-DB level filters.
     * 
     * @param items the list to filter
     * @param criteria the fiter criteria
     * @return the filtered list.
     */
    private static void filterOutForfeitureOrReinstatement(List items, ReportCriteria criteria) {

        if (items != null && items.size() != 0 && 
                criteria.getFilterValue(SubmissionHistoryReportData.FILTER_STATUS) != null) {
            
            String statusFilter = (String) criteria.getFilterValue(SubmissionHistoryReportData.FILTER_STATUS);
            
            for (int i=0; i < items.size(); ) {
                SubmissionHistoryItem item = (SubmissionHistoryItem) items.get(i);
                
                // filter by submission status
                if (!statusFilter.equals(item.getStatusGroup())) {
                    items.remove(i);
                } else {
                    i++;
                }
            }
        }
        
        if (items != null && items.size() != 0 && 
                criteria.getFilterValue(SubmissionHistoryReportData.FILTER_TYPE) != null) {
            
            String typeFilter = (String) criteria.getFilterValue(SubmissionHistoryReportData.FILTER_TYPE);
            
            // Remove FORFEITURE_CONTRIBUTION and REINSTATEMENT_CONTRIBUTION out of REGULAR_CONTRIBUTION
            for (int i=0; i < items.size(); ) {
                SubmissionHistoryItem item = (SubmissionHistoryItem) items.get(i);
                
                MoneySource ms = item.getMoneySource();
                if (isForfeitureOrReinstatement(ms, typeFilter)) {
                    // if the MoneySource is not defined or if the filter does not match the display name of the MoneySource, remove it
                    items.remove(i);
                } else {
                    i++;
                }
            }
        }
        
        if (items != null && items.size() != 0 && 
                ( criteria.getFilterValue(SubmissionHistoryReportData.FILTER_START_SUBMISSION_DATE) != null ||
                  criteria.getFilterValue(SubmissionHistoryReportData.FILTER_END_SUBMISSION_DATE) != null ) ) {
            
            // filtering by submission dates - do not show draft items
            for (int i=0; i < items.size(); ) {
                SubmissionHistoryItem item = (SubmissionHistoryItem) items.get(i);
                if ( DRAFT.equals(item.getType()) ) {
                    items.remove(i);
                } else {
                    i++;
                }
            }
        }
        
    }
    

	/**
	 * Apply non-DB level filters.
	 * 
	 * @param items the list to filter
	 * @param criteria the fiter criteria
	 * @return the filtered list.
	 */
	private static void filterByMoneySource(List items, ReportCriteria criteria) {

		if (items != null && items.size() != 0 && 
				criteria.getFilterValue(SubmissionHistoryReportData.FILTER_STATUS) != null) {
			
			String statusFilter = (String) criteria.getFilterValue(SubmissionHistoryReportData.FILTER_STATUS);
			
			for (int i=0; i < items.size(); ) {
				SubmissionHistoryItem item = (SubmissionHistoryItem) items.get(i);
				
				// filter by submission status
				if (!statusFilter.equals(item.getStatusGroup())) {
					items.remove(i);
				} else {
					i++;
				}
			}
		}
		
		if (items != null && items.size() != 0 && 
				criteria.getFilterValue(SubmissionHistoryReportData.FILTER_TYPE) != null) {
			
			String typeFilter = (String) criteria.getFilterValue(SubmissionHistoryReportData.FILTER_TYPE);
			
			for (int i=0; i < items.size(); ) {
				SubmissionHistoryItem item = (SubmissionHistoryItem) items.get(i);
				
				// filter by submission type
				if (!typeFilter.equals(item.getType())) {
					// type was not selected by the user - filter it out
					// but before removing check money source
					MoneySource ms = item.getMoneySource();
					if (!isMatchingMoneySource(ms, typeFilter)) {
						// if the MoneySource is not defined or if the filter does not match the display name of the MoneySource, remove it
						items.remove(i);
					} else {
					    i++;
                    }
				} else {
					i++;
				}
			}
		}
		
		if (items != null && items.size() != 0 && 
				( criteria.getFilterValue(SubmissionHistoryReportData.FILTER_START_SUBMISSION_DATE) != null ||
				  criteria.getFilterValue(SubmissionHistoryReportData.FILTER_END_SUBMISSION_DATE) != null ) ) {
			
			// filtering by submission dates - do not show draft items
			for (int i=0; i < items.size(); ) {
				SubmissionHistoryItem item = (SubmissionHistoryItem) items.get(i);
				if ( DRAFT.equals(item.getType()) ) {
					items.remove(i);
				} else {
					i++;
				}
			}
		}
		
	}
    
    /**
     * Utility that checks if the MoneySource is not defined or if the filter does not match 
     * the display name of the MoneySource
     * Also includes the special processing for Contribution
     * Contribution is the umbrella for all following types:
     *  - Regular contribution 
     *  - Forteiture contribution
     *  - Reinstatement of contribution
     *  - Transfer contribution
     * 
     * @param ms
     * @param typeFilter
     * @return
     */
    private static boolean isMatchingMoneySource(MoneySource ms, String typeFilter) {
        if(CONTRIBUTION.equalsIgnoreCase(typeFilter)) {
            // We want all for Contribution
            return true;
        }
        
        if (ms == null) {
            return false;
        } else {
            if (typeFilter.equals(ms.getDisplayName())) {
                return true;
            } else {
                return false;
            }
        }                  
    }
     
    
    private static boolean isForfeitureOrReinstatement(MoneySource ms, String typeFilter) {
        if (ms == null) {
            return false;
        } else if(FORFEITURE_CONTRIBUTION.equalsIgnoreCase(ms.getDisplayName()) ||
                        REINSTATEMENT_CONTRIBUTION.equalsIgnoreCase(ms.getDisplayName())) {
            return true;
        }
        
        return false;
    }
}