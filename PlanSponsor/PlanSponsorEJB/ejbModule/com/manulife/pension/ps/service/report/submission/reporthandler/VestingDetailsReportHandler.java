package com.manulife.pension.ps.service.report.submission.reporthandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.ps.service.report.participant.valueobject.VestingParticipant;
import com.manulife.pension.ps.service.report.submission.valueobject.VestingDetailsReportData;
import com.manulife.pension.ps.service.submission.dao.SubmissionDAO;
import com.manulife.pension.ps.service.submission.util.SubmissionParticipantComparatorFactory;
import com.manulife.pension.ps.service.submission.valueobject.MoneyTypeHeader;
import com.manulife.pension.ps.service.submission.valueobject.VestingDetailItem;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.report.valueobject.ReportSortList;
import com.manulife.pension.service.vesting.VestingConstants;

/**
 * 
 * @author Diana Macean
 *
 * Create report for vesting details.
 * 
 */
public class VestingDetailsReportHandler implements ReportHandler {  

	private static final String className = VestingDetailsReportHandler.class.getName();
    
	//   Labels
    private static final String TRANSACTION_LABEL = "vest.h10";
    private static final String CONTRACT_LABEL = "Cont#";
    private static final String SSN_LABEL = "SSN#";
    private static final String EMPLOYEE_ID_LABEL = "EEID#";
    private static final String FIRST_NAME_LABEL = "FirstName";
    private static final String LAST_NAME_LABEL = "LastName";
    private static final String INITIAL_LABEL = "Initial";
    private static final String VESTING_PERC_DATE_LABEL = "VestingEffDate";
    private static final String VESTING_VYOS_DATE_LABEL = "VYOSDate";
    private static final String VYOS_LABEL = "VYOS";
    private static final String VESTING_APPLY_LTPT_CREDITING_LABEL = "ApplyLTPTCrediting";

    public static final String PRODUCT_RA457="RA457";

	/**
	 * Generates vesting details for a given contract and submission ID
	 */
	public ReportData getReportData(ReportCriteria criteria) throws SystemException {

		int contractNumber = 
				((Integer)criteria.getFilterValue(VestingDetailsReportData.FILTER_CONTRACT_NUMBER)).intValue();
		int submissionId = 
			((Integer)criteria.getFilterValue(VestingDetailsReportData.FILTER_SUBMISSION_ID)).intValue();
		
		String productId = (String)criteria.getFilterValue(VestingDetailsReportData.FILTER_PRODUCTID);
		
		VestingDetailsReportData reportData = null;

		VestingDetailItem vestingData = 
				SubmissionDAO.getVestingDetails(submissionId, contractNumber, criteria);
        
        if (vestingData == null) {
            return null;
        }
        
		//sort(vestingData.getSubmissionParticipants(), criteria.getSorts());
			
		reportData = setReportHeader(criteria, contractNumber, vestingData, productId);
		
		List details = new ArrayList();
		for (Iterator i = vestingData.getSubmissionParticipants().iterator(); i.hasNext();) {
			VestingParticipant participantData = (VestingParticipant) i.next();
			details.add(participantData);
		}
		reportData.setDetails(page(details, criteria));
		
		vestingData.setSubmissionParticipants(null);
		
		reportData.setVestingData(vestingData);
		
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
	 * @param vestingData
	 * @return
	 */
	private VestingDetailsReportData setReportHeader(ReportCriteria criteria, 
			int contractNumber, VestingDetailItem vestingData, String productId) throws SystemException{

		int totalCount = vestingData.getSubmissionParticipants().size();
		VestingDetailsReportData data = new VestingDetailsReportData(criteria, totalCount);
        data.setContractNumber(contractNumber);
		
        data.setColumnLabels(generateColumnLabels(vestingData, data,contractNumber,productId));

		return data;
	}

	/**
	 * Generates the list of column labels for this report.
	 * 
	 * @param vestingData
	 * @param reportData
	 */
	private List<String> generateColumnLabels(VestingDetailItem vestingData, 
			VestingDetailsReportData reportData,int contractNumber, String productId) throws SystemException{

		String vestingCreditingMethod=ContractServiceDelegate.getInstance().getVestingCreditingMethod(contractNumber);
		vestingData.setVestingCreditingMethod(vestingCreditingMethod);
		
		List<String> columnLabels =  new ArrayList<String>();
		// only populate transaction code, 
        // if standard vesting file was submitted
        if (vestingData.getTpaSystemName() == null) {
            columnLabels.add(TRANSACTION_LABEL);
        }
		columnLabels.add(CONTRACT_LABEL);
		columnLabels.add(SSN_LABEL);
        columnLabels.add(FIRST_NAME_LABEL);
        columnLabels.add(LAST_NAME_LABEL);
        columnLabels.add(INITIAL_LABEL);
        columnLabels.add(EMPLOYEE_ID_LABEL);
        
        if (VestingConstants.VestingServiceFeature.CALCULATION.equals(vestingData.getVestingCSF())) {
        	if (VestingConstants.CreditingMethod.HOURS_OF_SERVICE.equalsIgnoreCase(vestingCreditingMethod)) {
        		if(!PRODUCT_RA457.equalsIgnoreCase(productId)) {
        			columnLabels.add(VESTING_APPLY_LTPT_CREDITING_LABEL);
        		}
        	}
        }
        if (VestingConstants.VestingServiceFeature.CALCULATION.equals(vestingData.getVestingCSF())) {
            if (vestingData.getTpaSystemName() == null) {
                columnLabels.add(VESTING_VYOS_DATE_LABEL);
            } else {
                columnLabels.add(VESTING_PERC_DATE_LABEL);
            }
            columnLabels.add(VYOS_LABEL);
        } else if (VestingConstants.VestingServiceFeature.COLLECTION.equals(vestingData.getVestingCSF())) {
            
            columnLabels.add(VESTING_PERC_DATE_LABEL);
            int countOfValidMoneyTypes = 0;
    		// flag the valid money types
    		for (Iterator<MoneyTypeHeader> itr = vestingData.getPercentageMoneyTypes().iterator(); itr.hasNext(); ) {
    			MoneyTypeHeader moneyType = (MoneyTypeHeader)itr.next();
    			boolean moneyTypeExists = false;
    			for (Iterator<MoneyTypeVO> cmtitr = vestingData.getContractMoneyTypes().iterator(); 
    					cmtitr.hasNext() && !moneyTypeExists; ) {
                    
    				String contractMoneyType = ((MoneyTypeVO)cmtitr.next()).getVestingIdentifier(vestingData.getTpaSystemName()).trim();
                    String percentageMoneyType = moneyType.getMoneyType().getVestingIdentifier(vestingData.getTpaSystemName()).trim();
    				if (StringUtils.equals(percentageMoneyType, contractMoneyType)) {
    					moneyTypeExists = true;
    				}
    			}
    			// include only money types that are valid for the contract
    			if (moneyTypeExists) {
    				columnLabels.add(moneyType.getMoneyType().getVestingName(true));
                    countOfValidMoneyTypes++;
    			}	
    		}
    
            vestingData.setCountOfValidMoneyTypes(countOfValidMoneyTypes);
        }
        
		return columnLabels;
	}
	
}