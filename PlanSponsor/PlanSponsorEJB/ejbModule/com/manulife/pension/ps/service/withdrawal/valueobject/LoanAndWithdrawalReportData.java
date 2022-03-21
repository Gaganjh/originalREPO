/**
 * Created on August 23, 2006
 */
package com.manulife.pension.ps.service.withdrawal.valueobject;

import java.io.Serializable;

import com.manulife.pension.ps.service.withdrawal.reporthandler.LoanAndWithdrawalReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * ReportData object for Loan & Withdrawal page.
 *  
 * @author Mihai Popa
 */
public class LoanAndWithdrawalReportData extends ReportData implements Serializable {

	public static final long serialVersionUID = 1L;
		
	public static final String REPORT_ID = LoanAndWithdrawalReportHandler.class.getName();
	public static final String REPORT_NAME = "loanAndWithdrawalReport";

	public static final String FILTER_REQUEST_FROM_DATE = "requestFromDate";
	public static final String FILTER_REQUEST_TO_DATE = "requestToDate";
	public static final String FILTER_REQUEST_TYPE = "requestType";
	public static final String FILTER_REQUEST_REASON = "requestReason";
	public static final String FILTER_REQUEST_STATUS = "requestStatus";
	public static final String FILTER_LAST_NAME = "lastname";
	public static final String FILTER_SSN = "ssn";
	public static final String FILTER_SEARCHABLE_CONTRACTS = "searchableContracts";
	public static final String FILTER_CONTRACT_NUMBER = "contractNumber";
	public static final String FILTER_CONTRACT_NAME = "contractName";
    public static final String FILTER_PRINCIPAL = "principal";
	
	public static final String FILTER_SITE_LOCATION = "siteLocation";
	public static final String FILTER_ROLE_CODE = "roleCode";
	public static final String FILTER_PROFILE_ID = "profileId";
	public static final String FILTER_SORT_ORDER = "sortOrder";
	public static final String FILTER_DURATION = "duration";
	
	public static final String SORT_REQUEST_FROM_DATE = "requestFromDate";
	public static final String SORT_REQUEST_TO_DATE = "requestToDate";
	public static final String SORT_REQUEST_TYPE = "requestType";
	public static final String SORT_REQUEST_REASON = "requestReason";
	public static final String SORT_CONTRACT_NAME = "contractName";
	public static final String SORT_CONTRACT_NUMBER = "contractNumber";
	public static final String SORT_PARTICIPANT_NAME = "participantName";
	public static final String SORT_SSN = "ssn";
	public static final String SORT_REQUEST_DATE = "requestDate";
	public static final String SORT_REFERENCE_NUMBER = "referenceNumber";
	public static final String SORT_STATUS = "status";
	public static final String SORT_INITIATED_BY = "initiatedBy";
	
	public LoanAndWithdrawalReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
		
		
	}
}