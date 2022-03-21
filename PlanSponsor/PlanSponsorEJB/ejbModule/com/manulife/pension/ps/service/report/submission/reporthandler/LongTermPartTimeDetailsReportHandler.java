package com.manulife.pension.ps.service.report.submission.reporthandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.participant.valueobject.LongTermPartTimeParticipant;
import com.manulife.pension.ps.service.report.submission.valueobject.LongTermPartTimeDetailsReportData;
import com.manulife.pension.ps.service.submission.dao.SubmissionDAO;
import com.manulife.pension.ps.service.submission.util.SubmissionParticipantComparatorFactory;
import com.manulife.pension.ps.service.submission.valueobject.LongTermPartTimeDetailItem;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.report.valueobject.ReportSortList;

/**
 *
 * Create report for Long Term Part Time details.
 * 
 */
public class LongTermPartTimeDetailsReportHandler implements ReportHandler {  

	private static final String className = LongTermPartTimeDetailsReportHandler.class.getName();
    
	//   Labels
    private static final String TRANSACTION_LABEL = "elig.h10";
    private static final String CONTRACT_LABEL = "Cont#";
    private static final String SSN_LABEL = "SSN#";
    private static final String FIRST_NAME_LABEL = "FirstName";
    private static final String LAST_NAME_LABEL = "LastName";
    private static final String INITIAL_LABEL = "Initial";
    private static final String LTPT_ASSESS_YR_LABEL = "LTPTAssessYr";


	/**
	 * Generates Long Term Part Time details for a given contract and submission ID
	 */
	public ReportData getReportData(ReportCriteria criteria) throws SystemException {

		int contractNumber = 
				((Integer)criteria.getFilterValue(LongTermPartTimeDetailsReportData.FILTER_CONTRACT_NUMBER)).intValue();
		int submissionId = 
			((Integer)criteria.getFilterValue(LongTermPartTimeDetailsReportData.FILTER_SUBMISSION_ID)).intValue();
		LongTermPartTimeDetailsReportData reportData = null;

		LongTermPartTimeDetailItem longTermPartTimeData = 
				SubmissionDAO.getLongTermPartTimeDetails(submissionId, contractNumber, criteria);
        
        if (longTermPartTimeData == null) {
            return null;
        }
        
		reportData = setReportHeader(criteria, contractNumber, longTermPartTimeData);
		
		List details = new ArrayList();
		for (Iterator i = longTermPartTimeData.getSubmissionParticipants().iterator(); i.hasNext();) {
			LongTermPartTimeParticipant participantData = (LongTermPartTimeParticipant) i.next();
			details.add(participantData);
		}
		reportData.setDetails(page(details, criteria));
		
		longTermPartTimeData.setSubmissionParticipants(null);
		
		reportData.setLongTermPartTimeDataData(longTermPartTimeData);
		
		return 	reportData;
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
					SubmissionParticipantComparatorFactory.getInstance().getComparator(firstSort.getSortField(), firstSort.getSortDirection()));
		}
	}
	/**
	 * Return only the items for the current page and page size.
	 * To return all items (as for a csv download file),
	 * set ReportCriteria.pageSize to ReportCriteria.NOLIMIT_PAGE_SIZE
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
	 * Sets the header information for this report.
	 * 
	 * @param criteria
	 * @param contractNumber
	 * @param longTermPartTimeData
	 * @return
	 */
	private LongTermPartTimeDetailsReportData setReportHeader(ReportCriteria criteria, 
			int contractNumber, LongTermPartTimeDetailItem longTermPartTimeData) {

		int totalCount = longTermPartTimeData.getSubmissionParticipants().size();
		LongTermPartTimeDetailsReportData data = new LongTermPartTimeDetailsReportData(criteria, totalCount);
        data.setContractNumber(contractNumber);
		
        data.setColumnLabels(generateColumnLabels(longTermPartTimeData, data));

		return data;
	}

	/**
	 * Generates the list of column labels for this report.
	 * 
	 * @param longTermPartTimeData
	 * @param reportData
	 */
	private List<String> generateColumnLabels(LongTermPartTimeDetailItem longTermPartTimeData, 
			LongTermPartTimeDetailsReportData reportData) {

		List<String> columnLabels =  new ArrayList<String>();

		if (longTermPartTimeData.getTpaSystemName() == null) {
            columnLabels.add(TRANSACTION_LABEL);
        }
		columnLabels.add(CONTRACT_LABEL);
		columnLabels.add(SSN_LABEL);
        columnLabels.add(FIRST_NAME_LABEL);
        columnLabels.add(LAST_NAME_LABEL);
        columnLabels.add(INITIAL_LABEL);
        columnLabels.add(LTPT_ASSESS_YR_LABEL);
        
		return columnLabels;
	}
	
}