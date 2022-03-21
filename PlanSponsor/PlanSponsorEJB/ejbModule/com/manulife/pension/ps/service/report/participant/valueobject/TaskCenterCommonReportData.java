package com.manulife.pension.ps.service.report.participant.valueobject;


import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class TaskCenterCommonReportData  extends ReportData {
	
	// for filtering
	public static final String FILTER_CONTRACTID = "contractId";      		
	public static final String FILTER_LAST_NAME ="lastName";
	public static final String FILTER_SSN = "ssn";
	public static final String FILTER_DIVISION = "division";	
	public static final String FILTER_PROCESSED_TS = "processedTS"; // not user accessible
	public static final String FILTER_EXTERNAL_USER_VIEW = "external"; // see [5.7 of DFS]
	public static final String FILTER_PROFILE_ID = "profileId";
	public static final String FILTER_SCREEN = "screen"; // ask if data for TH(TransactionHistory-Deferral Change Details)
	
	// for Sorting
    public static final String LAST_NAME_FIELD = "lastName";
    public static final String DIVISION_FIELD = "division";
    public static final String TYPE_FIELD = "type";
    public static final String INITIATED_FIELD = "initiated";
    public static final String ANV_DATE_FIELD = "anniversary";
    public static final String ACTION_TAKEN_FIELD = "actionDate";
    public static final String EFFECTIVE_DATE_FIELD = "effectiveDate";
    
    // for reading from db.
    public static final String PROFILE_ID_COLUMN = "PROFILE_ID";
    public static final String LAST_NAME_COLUMN = "LAST_NAME";
	public static final String DIVISION_COLUMN = "EMPLOYER_DIVISION";
	public static final String INITIATED_COLUMN = "CREATED_TS"; 
	public static final String ANV_DATE_COLUMN = "ACI_ANNIVERSARY_DATE"; 
	public static final String EFFECTIVE_DATE_COLUMN = "EFFECTIVE_DATE";
	public static final String FIRST_NAME_COLUMN = "FIRST_NAME";
	public static final String MIDDLE_INITIAL_COLUMN = "MIDDLE_INITIAL";
	public static final String SSN_COLUMN = "SOCIAL_SECURITY_NO";
	public static final String COUNTER_COLUMN = "COUNTER";
	public static final String EMPLOYEE_ID_COLUMN = "EMPLOYEE_ID";
	public static final String INSTRUCTION_NO_COLUMN = "INSTRUCTION_NO";
	
	public static final String CREATED_COLUMN = "CREATED_TS";
	public static final String CONTRIB_INSTRUCT_SRC_CODE = "CONTRIBUTION_INSTRUCT_SRC_CODE";
	public static final String ADHOC_FREZZE_PERIOD = "ADHOC_FREZZE_PERIOD";
	public static final String CONTRIB_AMT_COLUMN = "CONTRIBUTION_AMT";
	public static final String CONTRIB_PCT_COLUMN = "CONTRIBUTION_PCT";
	public static final String CONTRIB_OLD_AMT_COLUMN = "CONTRIBUTION_OLD_AMT";
	public static final String CONTRIB_OLD_PCT_COLUMN = "CONTRIBUTION_OLD_PCT";
	public static final String INCREASE_AMT_COLUMN = "INCREASE_AMT";
	public static final String INCREASE_PCT_COLUMN = "INCREASE_PCT";
	public static final String MONEY_TYPE_CODE_COLUMN = "MONEY_TYPE_CODE";
	public static final String PROCESSED_TS_COLUMN = "PROCESSED_TS";
	public static final String PROCESSED_STATUS_COLUMN = "PROCESSED_STATUS_CODE";
	public static final String REMARKS_COLUMN = "PROCESSED_REMARKS";
	public static final String PARTICIPANT_STATUS_CODE_COLUMN = "PARTICIPANT_STATUS_CODE";
	public static final String PARTICIPANT_BALANCE_COLUMN = "BALANCE";
	public static final String CREATED_USER_ID_TYPE_COLUMN = "CREATED_USER_ID_TYPE";
	public static final String CREATED_FIRST_NAME_COLUMN = "CREATED_USER_FIRST_NAME";   // one of two possible sources 
	public static final String CREATED_LAST_NAME_COLUMN = "CREATED_USER_LAST_NAME";
	public static final String CREATED_FIRST_NAME_COLUMN2 = "CREATED_USER_FIRST_NAME2"; // alternate source
	public static final String CREATED_LAST_NAME_COLUMN2 = "CREATED_USER_LAST_NAME2";
	public static final String PROCESSED_USER_ID_TYPE_COLUMN = "PROCESSED_USER_ID_TYPE";
	public static final String PROCESSED_SOURCE_CODE_COLUMN = "PROCESSED_SOURCE_CODE";
	public static final String PROCESSED_FIRST_NAME_COLUMN = "PROCESSED_USER_FIRST_NAME";
	public static final String PROCESSED_LAST_NAME_COLUMN = "PROCESSED_USER_LAST_NAME";
	public static final String CREATED_NAME3 = "CREATED_USER_NAME3";
	
	public TaskCenterCommonReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
	}

}
