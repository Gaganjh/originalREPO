package com.manulife.pension.ps.service.report.submission.reporthandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.contract.reporthandler.ContributionTemplateReportHandler;
import com.manulife.pension.ps.service.report.submission.valueobject.AddParticipantReportData;
import com.manulife.pension.ps.service.report.submission.valueobject.ContributionDetailsReportData;
import com.manulife.pension.ps.service.submission.dao.SubmissionDAO;
import com.manulife.pension.ps.service.submission.util.AddableParticipantComparatorFactory;
import com.manulife.pension.ps.service.submission.valueobject.AddableParticipant;
import com.manulife.pension.ps.service.submission.valueobject.AddableParticipantList;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.report.valueobject.ReportSortList;

/**
 * 
 * @author Jim Adamthwaite
 *
 * Add participant report generator.
 * 
 */
public class AddParticipantReportHandler extends ContributionTemplateReportHandler {  

	private static final String className = AddParticipantReportHandler.class.getName();


	/**
	 * Generates a add participant report for a given contract and submission ID
	 */
	public ReportData getReportData(ReportCriteria criteria) throws SystemException {

		int contractNumber = 
				((Integer)criteria.getFilterValue(ContributionDetailsReportData.FILTER_FIELD_1)).intValue();
		int submissionId = 
			((Integer)criteria.getFilterValue(ContributionDetailsReportData.FILTER_FIELD_2)).intValue();

		AddableParticipantList participantData = 
				SubmissionDAO.getParticipantList(submissionId, contractNumber);
		sort(participantData.getAddableParticipants(), criteria.getSorts());
			
		int totalCount = participantData.getAddableParticipants().size();

		AddParticipantReportData reportData = new AddParticipantReportData(criteria, totalCount);
		reportData.setContractNumber(contractNumber);
		reportData.setSubmissionId(new Integer(submissionId));
		reportData.setParticipantSortOption(participantData.getParticipantSortOption());
		reportData.setSystemStatus(participantData.getSystemStatus());
				
		List details = new ArrayList();
		for (Iterator i = participantData.getAddableParticipants().iterator(); i.hasNext();) {
			AddableParticipant participant = (AddableParticipant) i.next();
			details.add(participant);
		}
		reportData.setDetails(page(details, criteria));
		
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
					AddableParticipantComparatorFactory.getInstance().getComparator(firstSort.getSortField(), firstSort.getSortDirection()));
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
	

	
}