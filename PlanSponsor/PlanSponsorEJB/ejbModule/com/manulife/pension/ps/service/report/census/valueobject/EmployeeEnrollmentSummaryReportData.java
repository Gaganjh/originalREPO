package com.manulife.pension.ps.service.report.census.valueobject;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.manulife.pension.ps.service.report.census.reporthandler.EmployeeEnrollmentSummaryReportHandler;
import com.manulife.pension.ps.service.report.census.reporthandler.EmployeeOptOutSummaryReportHandler;
import com.manulife.pension.service.environment.valueobject.LabelValueBean;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * @author patuadr
 *
 */
public class EmployeeEnrollmentSummaryReportData extends ReportData {

    private static final long serialVersionUID = 6410615538967753611L;

    public static final String REPORT_ID = EmployeeEnrollmentSummaryReportHandler.class.getName();
    
   // public static final String OPT_OUT_REPORT_ID = EmployeeOptOutSummaryReportHandler.class.getName();
    
    public static final String REPORT_NAME = "Download Eligibility Report";
    public static final String FILTER_CONTRACT_NUMBER = "contractNumber";
    public static final String FILTER_STATUS = "status";
    public static final String FILTER_LAST_NAME = "lastName";
    public static final String FILTER_SSN = "ssn";
    public static final String FILTER_DIVISION = "division";
    public static final String FILTER_SEGMENT= "segment";
    public static final String FILTER_FROM_PED= "fromPED";
    public static final String FILTER_TO_PED= "toPED";
    public static final String FILTER_AUTO_ENROLL_ON = "autoEnrollOn";
    public static final String FILTER_ELIGIBILITY_CALC_ON = "eligibilityCalcOn";
    public static final String FILTER_EMP_STATUS = "empStatus";
    public static final String FILTER_ENROLL_START = "enrollStart";
    public static final String FILTER_ENROLL_END = "enrollEnd";
    public static final String FILTER_REPORT_TYPE = "reportType";
    public static final String SOURCE_PAGE = "sourcePage";
    public static final String FILTER_MONEY_TYPES = "moneyTypes";
    public static final String FILTER_MONEY_TYPE_SELECTED = "moneyTypeSelected";
    
    public static final String DEFAULT_SORT = "lastName";    
    public static final int    PAGE_SIZE = 35; 
    public static final String WARNING_IND_COLUMN = "WARNING_IND";
    public static final String WARNING_COLUMN = "WARNING_TYPE";
    public static final String ADDRESS_STATUS_COLUMN = "ADDRESS_STATUS";//either be blank (if everything is ok) or it would provide the error code 203 which indicates warning
    public static final String LAST_NAME_COLUMN = "LAST_NAME";
    public static final String FIRST_NAME_COLUMN = "FIRST_NAME";
    public static final String MIDDLE_INITIAL_COLUMN = "MIDDLE_INITIAL";
    public static final String SOCIAL_SECURITY_NO_COLUMN = "SOCIAL_SECURITY_NO";
    public static final String EMPLOYER_DIVISION_COLUMN = "EMPLOYER_DIVISION";
    public static final String EMPLOYEE_ID_COLUMN = "EMPLOYEE_ID";
    public static final String ENROLLMENT_STATUS_COLUMN = "ENROLLMENT_STATUS";
    public static final String EMPLOYMENT_STATUS_CODE = "EMPLOYMENT_STATUS_CODE";
    public static final String ENROLLMENT_METHOD_CODE_COLUMN = "ENROLLMENT_METHOD_CODE";
    public static final String ENROLLMENT_METHOD_COLUMN = "ENROLLMENT_METHOD";
    public static final String PROCESSING_DATE_COLUMN = "ENROLLMENT_PROCESSED_DATE";
    public static final String ELIGIBLE_TO_ENROLL_COLUMN = "PLAN_ELIGIBLE_IND";
    public static final String ELIGIBILITY_DATE_COLUMN = "ELIGIBILITY_DATE";
    public static final String BIRTH_DATE_COLUMN = "BIRTH_DATE";
    public static final String HIRE_DATE_COLUMN = "HIRE_DATE";
    public static final String APPLICABLE_PLAN_ENTRY_DATE_COLUMN = "APPLICABLE_PLAN_ENTRY_DATE";
    public static final String OPT_OUT_COLUMN = "AUTO_ENROLL_OPT_OUT_IND";
    public static final String OPT_OUT_SORT_COLUMN = "OPT_OUT";
    public static final String DEFERRAL_PCT_COLUMN = "DEFERRAL_PCT";
    public static final String BEFORE_TAX_DEFER_PCT = "BEFORE_TAX_DEFER_PCT";
    public static final String BEFORE_TAX_DEFER_AMT = "BEFORE_TAX_DEFER_AMT";
    public static final String DESIG_ROTH_DEF_PCT = "DESIG_ROTH_DEF_PCT";
    public static final String DESIG_ROTH_DEF_AMT = "DESIG_ROTH_DEF_AMT";
    public static final String ENROLL_BEFORE_TAX_DEFER_PCT = "ENROLL_BEFORE_TAX_DEFER_PCT";
    public static final String ENROLL_BEFORE_TAX_DEFER_AMT = "ENROLL_BEFORE_TAX_DEFER_AMT";
    public static final String ENROLL_DESIG_ROTH_DEF_PCT = "ENROLL_DESIG_ROTH_DEF_PCT";
    public static final String ENROLL_DESIG_ROTH_DEF_AMT = "ENROLL_DESIG_ROTH_DEF_AMT";
    public static final String PROFILE_ID_COLUMN = "PROFILE_ID";
    public static final String PARTICIPANT_IND_COLUMN = "PARTICIPANT_IND";
    public static final String COLUMN_NAME = "COLUMN_NAME";
    public static final String BEFORE_VALUE = "BEFORE_VALUE";  
    public static final String SOURCE_CHANNEL_CODE_COLUMN = "SOURCE_CHANNEL_CODE";
    public static final String LAST_UPDATED_TS_COLUMN = "LAST_UPDATED_TS";
    public static final String CREATED_USER_ID_TYPE_COLUMN = "CREATED_USER_ID_TYPE";
    public static final String CREATED_FIRST_NAME_COLUMN = "CREATED_FIRST_NAME";
    public static final String CREATED_LAST_NAME_COLUMN = "CREATED_LAST_NAME";
    public static final String CREATED_USER_ID_COLUMN = "CREATED_USER_ID";
    public static final String DATA_ELEMENT_VALUE = "DATA_ELEMENT_VALUE"; 
    public static final String OPT_OUT_NOT_VESTED = "OPT_OUT_NOT_VESTED"; 
    public static final String LAST_NAME_FIELD = "lastName";
    public static final String WARNING_IND_FIELD = "warningInd";
    public static final String SOCIAL_SECURITY_NO_FIELD = "ssn";
    public static final String EMPLOYER_DIVISION_FIELD = "organizationUnitID";
    public static final String EMPLOYEE_ID_FIELD = "employerDesignatedID";
    public static final String ENROLLMENT_STATUS_FIELD = "enrollmentStatus";
    public static final String ENROLLMENT_METHOD_FIELD = "enrollmentMethod";
    public static final String PROCESSING_DATE_FIELD = "enrollmentProcessedDate";
    public static final String ELIGIBLE_TO_ENROLL_FIELD = "eligibleToEnroll";
    public static final String ELIGIBILITY_DATE_FIELD = "eligibilityDate";
    public static final String OPT_OUT_FIELD = "optOut";
    public static final String DEFERRAL_PCT_FIELD = "contributionPct";
    
  // added AE DM
    public static final String MAILING_DATE_COLUMN ="DM_EKIT_MAILED_DATE_DISPLAY";
    public static final String MAILING_DATE_FIELD ="mailingDate";
    public static final String LANGUAGE_IND_COLUMN ="PREFERRED_LANGUAGE_CODE";
    public static final String LANGUAGE_IND_FIELD ="languageInd";
    public static final String PENDING_DATE ="12/31/8888";
    public static final String PENDING_DISPLAY ="Pending";
    public static final String NULL_DATE ="12/31/9999";
    public static final String NULL_DATE_DISPLAY ="";

    // added for AEE
    public static final String CONTRIBUTION_STATUS_COLUMN = "CONTRIBUTION_STATUS";
    public static final String CONTRIBUTION_STATUS_FIELD = "contributionStatus";
    public static final String PARTICIPANT_STATUS_COLUMN = "PARTICIPANT_STATUS_CODE";
    public static final String PARTICIPANT_STATUS_FIELD = "participantStatusCode";
    public static final String PARTICIPANT_STATUS_OPTED_OUT = "NT";
    public static final String WITHDRAWAL_ELECTION_90DAYS_INITIATED = "I";
    public static final String WITHDRAWAL_ELECTION_90DAYS_FORMS_SUBMITTED = "P";
    public static final String AE_90DAYS_OPTOUT_IND_COLUMN = "AE_90DAYS_OPTOUT_IND";
    public static final String AE_90DAYS_OPTOUT_IND_FIELD = "withdrawalElection90Days";
    
    // Added For EC project. -- START --
    private List<LabelValueBean> moneyTypes;
    private Map<String,Date> planEntryDatesMap;
    private Map<String,String> eligibiltyReportMap;
    public static final String MONEY_TYPE_PED ="moneyType";
    public static final String MONEY_TYPE_PED_COLUMN ="ELIGIBLE_PLAN_ENTRY_DATE";
    // Added For EC project. -- END --
    
    private int numberOfChanges;
    private int numberOfProfilesWithChanges;
    private int totalNumberOfEmployees;
    
    public EmployeeEnrollmentSummaryReportData(ReportCriteria criteria, int totalCount) {
        super(criteria, totalCount);
    }
    
    public int getNumberOfChanges() {
        return this.numberOfChanges;
    }

    public void setNumberOfChanges(int numberOfChanges) {
        this.numberOfChanges = numberOfChanges;
    }

    public int getNumberOfProfilesWithChanges() {
        return numberOfProfilesWithChanges;
    }

    public void setNumberOfProfilesWithChanges(int numberOfProfilesWithChanges) {
        this.numberOfProfilesWithChanges = numberOfProfilesWithChanges;
    }

    public int getTotalNumberOfEmployees() {
        return totalNumberOfEmployees;
    }
    
    public void setTotalNumberOfEmployees(int totalNumberOfEmployees) {
        this.totalNumberOfEmployees = totalNumberOfEmployees;
    }
    //  Added For EC project. -- START --
	public List<LabelValueBean> getMoneyTypes() {
		return moneyTypes;
	}

	public void setMoneyTypes(List<LabelValueBean> moneyTypes) {
		this.moneyTypes = moneyTypes;
	}

	public Map<String, Date> getPlanEntryDatesMap() {
		return planEntryDatesMap;
	}

	public void setPlanEntryDatesMap(Map<String, Date> planEntryDatesMap) {
		this.planEntryDatesMap = planEntryDatesMap;
	}
	// 	Added For EC project. -- END --

	public Map<String, String> getEligibiltyReportMap() {
		return eligibiltyReportMap;
	}

	public void setEligibiltyReportMap(Map<String, String> eligibiltyReportMap) {
		this.eligibiltyReportMap = eligibiltyReportMap;
	}
}
