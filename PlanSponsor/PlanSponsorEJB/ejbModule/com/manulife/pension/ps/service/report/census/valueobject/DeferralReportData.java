package com.manulife.pension.ps.service.report.census.valueobject;

import com.manulife.pension.ps.service.report.census.reporthandler.DeferralReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * @author patuadr
 *
 */
public class DeferralReportData extends ReportData {
    private static final long serialVersionUID = 1662914062278648859L;

    public static final String REPORT_ID = DeferralReportHandler.class.getName();
   
    public static final String FILTER_CONTRACT_NUMBER = "contractNumber";
    public static final String FILTER_LAST_NAME = "lastName";
    public static final String FILTER_SSN = "ssn";
    public static final String FILTER_DIVISION = "division";
    public static final String FILTER_SEGMENT= "segment";
    public static final String FILTER_EMPLOYMENT_STATUS= "employmentStatus";
    public static final String FILTER_ENROLLMENT_STATUS= "enrollmentStatus";
    public static final String FILTER_AUTO_ENROLL_ON = "autoEnrollOn";
    
    public static final String DEFAULT_SORT = "default";    
    public static final int    PAGE_SIZE = 35; 
    
    public static final String WARNING_IND_COLUMN = "WARNING_IND";
    public static final String WARNING_COLUMN = "WARNING";
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
    public static final String BEFORE_TAX_DEFER_PCT_COLUMN = "BEFORE_TAX_DEFER_PCT";
    public static final String BEFORE_TAX_DEFER_AMT_COLUMN = "BEFORE_TAX_DEFER_AMT";
    public static final String DESIG_ROTH_DEF_PCT_COLUMN = "DESIG_ROTH_DEF_PCT";
    public static final String DESIG_ROTH_DEF_AMT_COLUMN = "DESIG_ROTH_DEF_AMT";
    public static final String ALERT_COLUMN = "ALERT";
    public static final String DEFER_VALUES_LAST_UPDATED_TS_COLUMN = "DEFER_VALUES_LAST_UPDATED_TS";
    
    public static final String ACI_SETTING_IND_PREVIOUS_VALUE_COLUMN = "ACI_SETTING_IND_H";
    public static final String ACI_SETTING_IND_CREATED_TS_COLUMN = "ASI_CREATED_TS_H";
    public static final String ACI_SETTING_IND_CREATED_USER_ID_COLUMN = "ASI_CREATED_USER_ID_H";
    public static final String ACI_SETTING_IND_SOURCE_CHANNEL_CODE_COLUMN = "ASI_SOURCE_CHANNEL_CODE_H";
    public static final String ACI_SETTING_IND_CREATED_USER_ID_TYPE_COLUMN = "ASI_CREATED_USER_ID_TYPE_H";
    public static final String ACI_SETTING_IND_FIRST_NAME_COLUMN = "ASI_FIRST_NAME_H";
    public static final String ACI_SETTING_IND_LAST_NAME_COLUMN = "ASI_LAST_NAME_H";
    public static final String ACI_SETTING_IND_PPT_FIRST_NAME_COLUMN = "ASI_PPT_FIRST_NAME_H";
    public static final String ACI_SETTING_IND_PPT_LAST_NAME_COLUMN = "ASI_PPT_LAST_NAME_H";
    
    public static final String DEFFER_INC_PCT_PREVIOUS_VALUE_COLUMN = "DEFER_INC_PCT_H";
    public static final String DEFFER_INC_PCT_CREATED_TS_COLUMN = "DIP_CREATED_TS_H";
    public static final String DEFFER_INC_PCT_CREATED_USER_ID_COLUMN = "DIP_CREATED_USER_ID_H";
    public static final String DEFFER_INC_PCT_SOURCE_CHANNEL_CODE_COLUMN = "DIP_SOURCE_CHANNEL_CODE_H";
    public static final String DEFFER_INC_PCT_CREATED_USER_ID_TYPE_COLUMN = "DIP_CREATED_USER_ID_TYPE_H";
    public static final String DEFFER_INC_PCT_FIRST_NAME_COLUMN = "DIP_FIRST_NAME_H";
    public static final String DEFFER_INC_PCT_LAST_NAME_COLUMN = "DIP_LAST_NAME_H";
    public static final String DEFFER_INC_PCT_PPT_FIRST_NAME_COLUMN = "DIP_PPT_FIRST_NAME_H";
    public static final String DEFFER_INC_PCT_PPT_LAST_NAME_COLUMN = "DIP_PPT_LAST_NAME_H";
    
    public static final String DEFFER_INC_AMT_PREVIOUS_VALUE_COLUMN = "DEFER_INC_AMT_H";
    public static final String DEFFER_INC_AMT_CREATED_TS_COLUMN = "DIA_CREATED_TS_H";
    public static final String DEFFER_INC_AMT_CREATED_USER_ID_COLUMN = "DIA_CREATED_USER_ID_H";
    public static final String DEFFER_INC_AMT_SOURCE_CHANNEL_CODE_COLUMN = "DIA_SOURCE_CHANNEL_CODE_H";
    public static final String DEFFER_INC_AMT_CREATED_USER_ID_TYPE_COLUMN = "DIA_CREATED_USER_ID_TYPE_H";
    public static final String DEFFER_INC_AMT_FIRST_NAME_COLUMN = "DIA_FIRST_NAME_H";
    public static final String DEFFER_INC_AMT_LAST_NAME_COLUMN = "DIA_LAST_NAME_H";
    public static final String DEFFER_INC_AMT_PPT_FIRST_NAME_COLUMN = "DIA_PPT_FIRST_NAME_H";
    public static final String DEFFER_INC_AMT_PPT_LAST_NAME_COLUMN = "DIA_PPT_LAST_NAME_H";
    
    public static final String DEFER_MAX_LIMIT_PCT_PREVIOUS_VALUE_COLUMN = "DEFER_MAX_LIMIT_PCT_H";
    public static final String DEFER_MAX_LIMIT_PCT_CREATED_TS_COLUMN = "DMLP_CREATED_TS_H";
    public static final String DEFER_MAX_LIMIT_PCT_CREATED_USER_ID_COLUMN = "DMLP_CREATED_USER_ID_H";
    public static final String DEFER_MAX_LIMIT_PCT_SOURCE_CHANNEL_CODE_COLUMN = "DMLP_SOURCE_CHANNEL_CODE_H";
    public static final String DEFER_MAX_LIMIT_PCT_CREATED_USER_ID_TYPE_COLUMN = "DMLP_CREATED_USER_ID_TYPE_H";
    public static final String DEFER_MAX_LIMIT_PCT_FIRST_NAME_COLUMN = "DMLP_FIRST_NAME_H";
    public static final String DEFER_MAX_LIMIT_PCT_LAST_NAME_COLUMN = "DMLP_LAST_NAME_H";
    public static final String DEFER_MAX_LIMIT_PCT_PPT_FIRST_NAME_COLUMN = "DMLP_PPT_FIRST_NAME_H";
    public static final String DEFER_MAX_LIMIT_PCT_PPT_LAST_NAME_COLUMN = "DMLP_PPT_LAST_NAME_H";
    
    public static final String DEFER_MAX_LIMIT_AMT_PREVIOUS_VALUE_COLUMN = "DEFER_MAX_LIMIT_AMT_H";
    public static final String DEFER_MAX_LIMIT_AMT_CREATED_TS_COLUMN = "DMLA_CREATED_TS_H";
    public static final String DEFER_MAX_LIMIT_AMT_CREATED_USER_ID_COLUMN = "DMLA_CREATED_USER_ID_H";
    public static final String DEFER_MAX_LIMIT_AMT_SOURCE_CHANNEL_CODE_COLUMN = "DMLA_SOURCE_CHANNEL_CODE_H";
    public static final String DEFER_MAX_LIMIT_AMT_CREATED_USER_ID_TYPE_COLUMN = "DMLA_CREATED_USER_ID_TYPE_H";
    public static final String DEFER_MAX_LIMIT_AMT_FIRST_NAME_COLUMN = "DMLA_FIRST_NAME_H";
    public static final String DEFER_MAX_LIMIT_AMT_LAST_NAME_COLUMN = "DMLA_LAST_NAME_H";
    public static final String DEFER_MAX_LIMIT_AMT_PPT_FIRST_NAME_COLUMN = "DMLA_PPT_FIRST_NAME_H";
    public static final String DEFER_MAX_LIMIT_AMT_PPT_LAST_NAME_COLUMN = "DMLA_PPT_LAST_NAME_H";
    
    public static final String ACI_REQ_WAITING_APPROVAL_ANNIVERSARY_DATE= "REQ_ANNIVERSARY_DATE";
    public static final String AD_HOC_401K_DEFERRAL_CHANGE_REQ_AMT = "REQ_CONTRIBUTION_AMT";
    public static final String AD_HOC_401K_DEFERRAL_CHANGE_REQ_PCT = "REQ_CONTRIBUTION_PCT";
    
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
    
    public static final String DEFER_INC_PCT_COLUMN = "DEFER_INC_PCT";
    public static final String DEFER_INC_AMT_COLUMN = "DEFER_INC_AMT";
    public static final String DEFER_MAX_LIMIT_AMT_COLUMN = "DEFER_MAX_LIMIT_AMT";
    public static final String DEFER_MAX_LIMIT_PCT_COLUMN = "DEFER_MAX_LIMIT_PCT";
    public static final String ACI_ANNIVERSARY_DATE_COLUMN = "ACI_ANNIVERSARY_DATE";
    public static final String ACI_SETTINGS_IND_COLUMN = "ACI_SETTING_IND";
    public static final String NEXT_ANNIVERSARY_DATE_COLUMN = "NEXT_ANNIVERSARY_DATE";
    
    public static final String LAST_NAME_FIELD = "lastName";
    public static final String WARNING_FIELD = "warning";
    public static final String ALERT_FIELD = "alert";
    public static final String SOCIAL_SECURITY_NO_FIELD = "ssn";
    public static final String EMPLOYER_DIVISION_FIELD = "division";
    public static final String ACI_ANNIVERSARY_DATE_FIELD = "dateNextADI";
    public static final String ACI_SETTING_IND_FIELD = "autoIncrease";
    public static final String ACI_INCREASE_TYPE_FIELD = "increaseType";
    public static final String EMPLOYEE_ID_FIELD = "employerDesignatedID";
    public static final String ENROLLMENT_STATUS_FIELD = "enrollmentStatus";
    public static final String ENROLLMENT_METHOD_FIELD = "enrollmentMethod";
    public static final String PROCESSING_DATE_FIELD = "enrollmentProcessedDate";
    public static final String ELIGIBLE_TO_ENROLL_FIELD = "eligibleToEnroll";
    public static final String ELIGIBILITY_DATE_FIELD = "eligibilityDate";
    public static final String OPT_OUT_FIELD = "optOut";
    public static final String INCREASE_FIELD = "increaseAmt";
    public static final String LIMIT_FIELD = "limitAmt";
    public static final String BEFORE_TAX_DEFERRAL_PCT_FIELD = "beforeTaxDeferralPct";
    public static final String PCI_CONTRIBUTION_INSTRUCT_SRC_CODE="CONTRIBUTION_INSTRUCT_SRC_CODE";
    
    public static final String MAX_BEFORE_TAX_DEFERRAL_PCT_COLUMN= "MAX_BEFORE_TAX_DEFER_PCT";
    public static final String MAX_BEFORE_TAX_DEFERRAL_AMT_COLUMN= "MAX_BEFORE_TAX_DEFER_AMT";
    public static final String MAX_DESIG_ROTH_DEFERRAL_PCT_COLUMN= "MAX_DESIG_ROTH_DEF_PCT";
    public static final String MAX_DESIG_ROTH_DEFERRAL_AMT_COLUMN= "MAX_DESIG_ROTH_DEF_AMT";
    public static final String MIN_BEFORE_TAX_DEFERRAL_PCT_COLUMN= "MIN_BEFORE_TAX_DEFER_PCT";
    public static final String MIN_BEFORE_TAX_DEFERRAL_AMT_COLUMN= "MIN_BEFORE_TAX_DEFER_AMT";
    public static final String MIN_DESIG_ROTH_DEFERRAL_PCT_COLUMN= "MIN_DESIG_ROTH_DEF_PCT";
    public static final String MIN_DESIG_ROTH_DEFERRAL_AMT_COLUMN= "MIN_DESIG_ROTH_DEF_AMT";
    public static final String PLAN_DEFERRAL_MAX_LIMIT_AMT_COLUMN= "PLAN_DEFER_MAX_LIMIT_AMT";
    public static final String PLAN_DEFERRAL_MAX_LIMIT_PCT_COLUMN= "PLAN_DEFER_MAX_LIMIT_PCT";
    
    public static final String ACI_SETTING_OVERRIDE_IND_COLUMN= "PS_ACI_SETTING_OVERRIDE_IND";
    public static final String ACI_SETTING_IND_COLUMN= "ACI_SETTING_IND";
    public static final String PARTICIPANT_STATUS_CODE= "PARTICIPANT_STATUS_CODE";
    
    public static final String BEFORE_TAX_EXCEEDS_PLAN_WARNING= "2551";
    public static final String DEFERRAL_TOTAL_EXCEEDS_PLAN_WARNING= "2552";
    public static final String DESIGNATED_ROTH_EXCEEDS_PLAN_WARNING= "2553";
    public static final String CONTRIB_PLUS_INCREASE_EXCEEDS_PLAN_WARNING= "2555";
        
    private int totalNumberOfEmployees;
    
    public DeferralReportData(ReportCriteria criteria, int totalCount) {
        super(criteria, totalCount);
    }

    public int getTotalNumberOfEmployees() {
        return totalNumberOfEmployees;
    }

    public void setTotalNumberOfEmployees(int totalNumberOfEmployees) {
        this.totalNumberOfEmployees = totalNumberOfEmployees;
    }
    
}
