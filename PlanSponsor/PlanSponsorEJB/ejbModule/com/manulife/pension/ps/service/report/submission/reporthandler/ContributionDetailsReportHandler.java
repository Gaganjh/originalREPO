package com.manulife.pension.ps.service.report.submission.reporthandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.contract.reporthandler.ContributionTemplateReportHandler;
import com.manulife.pension.ps.service.report.submission.valueobject.ContributionDetailsReportData;
import com.manulife.pension.ps.service.report.transaction.handler.MoneySourceDescription;
import com.manulife.pension.ps.service.submission.dao.SubmissionDAO;
import com.manulife.pension.ps.service.submission.util.SubmissionParticipantComparatorFactory;
import com.manulife.pension.ps.service.submission.valueobject.ContributionDetailItem;
import com.manulife.pension.ps.service.submission.valueobject.MoneyTypeHeader;
import com.manulife.pension.ps.service.submission.valueobject.SubmissionParticipant;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.report.valueobject.ReportSortList;
import com.manulife.pension.submission.SubmissionError;

/**
 * 
 * @author Jim Adamthwaite
 *
 * Create report for contribution details.
 * 
 */
public class ContributionDetailsReportHandler extends ContributionTemplateReportHandler {  

	private static final String className = ContributionDetailsReportHandler.class.getName();
	private static final String NAME_LABEL ="Participant Name";

	private static final String ZERO_CONTRIBUTION_STATUS = "22";
    private static final String ZERO_CONTRIBUTION_STATUS_GROUP = "CM";
    private static final int ZERO_CONTRIBUTION_STATUS_ERROR_LEVEL = 10;
	private static final String ZERO_CONTRIBUTION_STATUS_ERROR_CODE = "1999";
	private static final int ZERO_CONTRIBUTION_STATUS_ERROR_CONTENT_ID = 75187;
	
	
	/**
	 * Generates contribution details for a given contract and submission ID
	 */
	public ReportData getReportData(ReportCriteria criteria) throws SystemException {
		
		int contractNumber = 
				((Integer)criteria.getFilterValue(ContributionDetailsReportData.FILTER_FIELD_1)).intValue();
		int submissionId = 
			((Integer)criteria.getFilterValue(ContributionDetailsReportData.FILTER_FIELD_2)).intValue();
		ContributionDetailsReportData reportData = null;

		ContributionDetailItem contributionData = 
				SubmissionDAO.getContributionDetails(submissionId, contractNumber);
        
        if (contributionData == null) {
            return null;
        }
        
        // $0 contribution file error message
       if(StringUtils.equals(contributionData.getSystemStatus(), ZERO_CONTRIBUTION_STATUS)) {
    	   
    	   // set as zero contribution file
    	   contributionData.setZeroContributionFile(true);
    	   
    	   // add the error code 1999
		   SubmissionError error = new SubmissionError(
					ZERO_CONTRIBUTION_STATUS_GROUP, null, null,
					ZERO_CONTRIBUTION_STATUS_ERROR_CODE,
					ZERO_CONTRIBUTION_STATUS_ERROR_LEVEL,
					ZERO_CONTRIBUTION_STATUS_ERROR_CONTENT_ID, 0, false, null, null);
        	List<SubmissionError> errorList = new ArrayList<SubmissionError>();
        	errorList.add(error);
        	contributionData.getReportDataErrors().addErrors(errorList);
        }
        
		sort(contributionData.getSubmissionParticipants(), criteria.getSorts());
			
		reportData = setReportHeader(criteria, contractNumber, contributionData);
		
		List details = new ArrayList();
		for (Iterator i = contributionData.getSubmissionParticipants().iterator(); i.hasNext();) {
			SubmissionParticipant participantData = (SubmissionParticipant) i.next();
			details.add(participantData);
		}
		reportData.setDetails(page(details, criteria));
		
		contributionData.setSubmissionParticipants(null);
		
		reportData.setContributionData(contributionData);
		
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
	 * @param contributionData
	 * @return
	 */
	private ContributionDetailsReportData setReportHeader(ReportCriteria criteria, 
			int contractNumber, ContributionDetailItem contributionData) {

		int totalCount = contributionData.getSubmissionParticipants().size();
		ContributionDetailsReportData reportData = new ContributionDetailsReportData(criteria, totalCount);
		reportData.setContractNumber(contractNumber);
		reportData.setTransactionNumber(MoneySourceDescription.getTransactionCode(contributionData.getMoneySourceID()));
		reportData.setDate(contributionData.getPayrollDate());
		
		int maxLoanCount = contributionData.getMaximumNumberOfLoans();
		// if there are no loans, but the contract does have the loan feature, create one column for loans
		if (maxLoanCount == 0 && contributionData.isContractHasLoanFeature()) {
			maxLoanCount = 1;
		}
		reportData.setColumnLabels(generateColumnLabels(contributionData, reportData, maxLoanCount));
		
		
		// calculate the allocationTotalValues
		HashMap allocationTotalValues = new HashMap();
		if (contributionData != null) {
			Iterator moneyTypes = contributionData.getAllocationMoneyTypes().iterator();
			while (moneyTypes.hasNext()) {
				MoneyTypeHeader moneyType = (MoneyTypeHeader) moneyTypes.next();
				String key = moneyType.getKey();
				Iterator participants = contributionData.getSubmissionParticipants().iterator();
				BigDecimal typeTotal = new BigDecimal(0d);
				while (participants.hasNext()) {
					SubmissionParticipant participant = (SubmissionParticipant) participants.next();
					BigDecimal amount = (BigDecimal)participant.getMoneyTypeAmounts().get(key);
					if (amount != null) typeTotal = typeTotal.add(amount);
				}
				allocationTotalValues.put(key,typeTotal);
			}
		}
		contributionData.setAllocationTotalValues(allocationTotalValues);
		
		
		// calculate the loanTotalValues
		ArrayList loanTotalValues = new ArrayList();
		if (
				contributionData != null
		) {
			for (int i = 0; i < contributionData.getMaximumNumberOfLoans(); i++) {
				Iterator participants = contributionData.getSubmissionParticipants().iterator();
				BigDecimal loanTotal = new BigDecimal(0d);
				while (participants.hasNext()) {
					SubmissionParticipant participant = (SubmissionParticipant) participants.next();
					
					// get the loan values
					if (participant.getLoanAmounts() == null || participant.getLoanAmounts().values() == null) continue;
					BigDecimal [] loanAmounts = (BigDecimal []) participant.getLoanAmounts().values().toArray(new BigDecimal [] {new BigDecimal(0d)});
					BigDecimal amount = null;
					if (i < loanAmounts.length)
						amount = loanAmounts[i];
					else 
						amount = new BigDecimal(0d);
					
					if (amount != null)
						loanTotal = loanTotal.add(amount);
				}
				loanTotalValues.add(loanTotal);
			}
		}
		
		contributionData.setLoanTotalValues(loanTotalValues);
		
		
		// calculate the participantTotal
		BigDecimal participantTotal = new BigDecimal(0d);
		if (contributionData != null) {
			Iterator participants = contributionData.getSubmissionParticipants().iterator();
			while (participants.hasNext()) {
				SubmissionParticipant participant = (SubmissionParticipant) participants.next();
				participantTotal = participantTotal.add(participant.getParticipantTotal());
			}
		}
		contributionData.setParticipantTotal(participantTotal);

		return reportData;
	}

	/**
	 * Generates the list of column labels for this report.
	 * 
	 * @param contributionData
	 * @param reportData
	 */
	private List generateColumnLabels(ContributionDetailItem contributionData, 
			ContributionDetailsReportData reportData, int maxLoanCount) {

		List columnLabels =  new ArrayList();
		columnLabels.add(TRANSACTION_LABEL);
		columnLabels.add(CONTRACT_LABEL);
		if (EMPLOYEE_ID_INDICATOR.equals(contributionData.getParticipantSortOption())) {
			columnLabels.add(EMPLOYEE_ID_LABEL);
		} else {
			columnLabels.add(SSN_LABEL);
		}
		columnLabels.add(NAME_LABEL);
		columnLabels.add(DATE_LABEL);
		
		List contractMoneyTypes = contributionData.getContractMoneyTypes();
		// sort according to the rule defined in MoneyTypeVO, which implements Comparable
		for (Iterator itr = contributionData.getAllocationMoneyTypes().iterator(); itr.hasNext(); ) {
			MoneyTypeHeader moneyType = (MoneyTypeHeader)itr.next();
			boolean moneyTypeExists = false;
			for (Iterator cmtitr = contributionData.getContractMoneyTypes().iterator(); 
					cmtitr.hasNext() && !moneyTypeExists; ) {
				String contractMoneyTypeId = ((MoneyTypeVO)cmtitr.next()).getId().trim();
				if (moneyType.getMoneyTypeId().trim().equals(contractMoneyTypeId)) {
					moneyTypeExists = true;
				}
			}
			// include only money types that are valid for the contract
			if (moneyTypeExists) {
				columnLabels.add(moneyType.getMoneyType().getContributionName()); // does implicit substitutions
			}	
		}
		
		for (int i=0; i<maxLoanCount; i++) {
			columnLabels.add(LOAN_ID_LABEL);
			columnLabels.add(LOAN_AMOUNT_LABEL);
		}
		return columnLabels;
	}
	
}