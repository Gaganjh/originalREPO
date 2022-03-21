package com.manulife.pension.ps.service.report.submission.reporthandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.submission.valueobject.TPAVestingSubmissionReportData;
import com.manulife.pension.ps.service.report.submission.valueobject.VestingDetailsReportData;
import com.manulife.pension.ps.service.submission.dao.SubmissionDAO;
import com.manulife.pension.ps.service.submission.dao.TPAVestingSubmissionDAO;
import com.manulife.pension.ps.service.submission.valueobject.MoneyTypeHeader;
import com.manulife.pension.ps.service.submission.valueobject.VestingDetailItem;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.vesting.VestingConstants;

/**
 * 
 * @author Diana Macean
 *
 * Create the TPA Vesting Submission Report
 * 
 */
public class TPAVestingSubmissionReportHandler implements ReportHandler {  

	private static final String className = TPAVestingSubmissionReportHandler.class.getName();
    
	// Labels
    private static final String TPA_FIRM_ID = "TPA Firm ID";
    private static final String TPA_FIRM_NAME = "TPA Firm Name";
    private static final String CONTRACT_NUMBER = "Contract #";
    private static final String CONTRACT_NAME = "Contract Name";
    private static final String PLAN_YEAR_END = "Plan Year End";
    private static final String LAST_FILE_SUBMISSION = "Last File Submission";
    private static final String SUBMISSION_ID = "Submission ID";
    private static final String SUBMISSION_STATUS = "Submission Status";
    private static final String VESTING_SERVICE = "Vesting Service";
    private static final String EARLIEST_EFF_DATE = "Earliest Vesting Effective Date in Submission";
    private static final String LATEST_EFF_DATE = "Latest Vesting Effective Date in Submission";
    private static final String ONLINE_UPDATES = "Online Update After File Submission";
    private static final String COMMENTS = "Comments"; 

    
	/**
	 * Generates vesting details for a given contract and submission ID
	 */
	public ReportData getReportData(ReportCriteria criteria) throws SystemException {

		
        TPAVestingSubmissionReportData reportData = 
			TPAVestingSubmissionDAO.getTPAVestingSubmissions(criteria);
        
        if (reportData == null) {
            return null;
        }
        		
		reportData.setColumnLabels(generateColumnLabels());
		       
		return 	reportData;
	}


	/**
	 * Generates the list of column labels for this report.
	 * 
	 */
	private List generateColumnLabels() {

		List columnLabels =  new ArrayList();
		
		columnLabels.add(TPA_FIRM_ID);
		columnLabels.add(TPA_FIRM_NAME);
        columnLabels.add(CONTRACT_NUMBER);
        columnLabels.add(CONTRACT_NAME);
        columnLabels.add(PLAN_YEAR_END);
        columnLabels.add(LAST_FILE_SUBMISSION);
        columnLabels.add(SUBMISSION_ID);
        columnLabels.add(SUBMISSION_STATUS);
        columnLabels.add(VESTING_SERVICE);
        columnLabels.add(EARLIEST_EFF_DATE);
        columnLabels.add(LATEST_EFF_DATE);
        columnLabels.add(ONLINE_UPDATES);
        columnLabels.add(COMMENTS);
		
		return columnLabels;
	}
	
}