package com.manulife.pension.ps.service.withdrawal.valueobject;

import java.io.Serializable;

import com.manulife.pension.ps.service.withdrawal.reporthandler.SearchParticipantWithdrawalReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * Report data for Participant search page.
 * 
 * @author Harsh Kuthiala
 *
 */

public class SearchParticipantWithdrawalReportData extends ReportData implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	public static final String REPORT_ID = SearchParticipantWithdrawalReportHandler.class.getName();
	public static final String REPORT_NAME = "searchRequestWithdrawalReport";
	
	public static final String FILTER_LAST_NAME = "lastname";
	public static final String FILTER_SSN = "ssn";
    public static final String FILTER_CONTRACT_ID = "contract_id";
	
	public static final String SORT_PARTICIPANT_NAME = "participantName";
	public static final String SORT_SSN = "ssn";
    
    public static final String FILTER_SITE_LOCATION = "siteLocation";
    public static final String FILTER_IS_TPA = "isTpa";
    public static final String FILTER_USER_PROFILE_ID = "userProfileId";
	
	public SearchParticipantWithdrawalReportData (ReportCriteria criteria, int count) {
		super(criteria, count);
	}

}
