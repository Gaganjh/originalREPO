package com.manulife.pension.ps.service.report.census.valueobject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manulife.pension.ps.service.report.census.reporthandler.EligibilityReportHandler;
import com.manulife.pension.service.employee.valueobject.EmployeeChangeHistoryVO;
import com.manulife.pension.service.environment.valueobject.LabelValueBean;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class EligibilityReportData extends ReportData {
	 private static final long serialVersionUID = 6410615538967753611L;

	    public static final String REPORT_ID = EligibilityReportHandler.class.getName();
	    
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
	    public static final String FILTER_EMP_STATUS = "empStatus";
	    public static final String FILTER_ENROLL_START = "enrollStart";
	    public static final String FILTER_ENROLL_END = "enrollEnd";
	    public static final String FILTER_REPORT_TYPE = "reportType";
	    public static final String FILTER_MONEY_TYPES = "moneyTypes";
	    public static final String FILTER_MONEY_TYPE_STRING = "moneyTypeString";
	    public static final String FILTER_SHOW_LTPT_INFO_INDICATOR = "isShowLTPTInfoIndicator_";
	    
	    public static final String REPORTED_FROM_DATE = "reportedFromDate";
	    public static final String REPORTED_TO_DATE = "reportedToDate";
	    
	    public static final String DEFAULT_SORT = "lastName";    
	    public static final int    PAGE_SIZE = 35; 
	    public static final String WARNING_IND_COLUMN = "WARNING_IND";
	    public static final String WARNING_COLUMN = "WARNING_TYPE";
	    public static final String ADDRESS_STATUS_COLUMN = "ADDRESS_STATUS";
	    public static final String LAST_NAME_COLUMN = "LAST_NAME";
	    public static final String NEW_EMPLOYEE_COLUMN = "NEW_EMPLOYEE";
	    public static final String FIRST_NAME_COLUMN = "FIRST_NAME";
	    public static final String MIDDLE_INITIAL_COLUMN = "MIDDLE_INITIAL";
	    public static final String SOCIAL_SECURITY_NO_COLUMN = "SOCIAL_SECURITY_NO";
	    public static final String EMPLOYER_DIVISION_COLUMN = "EMPLOYER_DIVISION";
	    public static final String EMPLOYMENT_STATUS_COLUMN = "EMPLOYMENT_STATUS_CODE";
	    public static final String EMPLOYMENT_STATUS_EFF_DATE_COLUMN = "EMPLOYMENT_STATUS_EFF_DATE";
	    public static final String EMPLOYEE_ID_COLUMN = "EMPLOYEE_ID";
	    public static final String ENROLLMENT_STATUS_COLUMN = "ENROLLMENT_STATUS";
	    public static final String ENROLLMENT_STATUS_CODE_COLUMN = "ENROLLMENT_STATUS_CODE";
	    public static final String EMPLOYMENT_STATUS_CODE = "EMPLOYMENT_STATUS_CODE";
	    public static final String ENROLLMENT_METHOD_CODE_COLUMN = "ENROLLMENT_METHOD_CODE";
	    public static final String ENROLLMENT_METHOD_COLUMN = "ENROLLMENT_METHOD";
	    public static final String PROCESSING_DATE_COLUMN = "ENROLLMENT_PROCESSING_DATE";
	    public static final String PROCESSED_DATE_COLUMN = "ENROLLMENT_PROCESSED_DATE";
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
	    public static final String CREATED_TIMESTAMP_COLUMN = "CREATED_TS";
	    public static final String CREATED_DATE_COLUMN = "CREATED_DATE";
	    public static final String AFTER_VALUE_COLUMN = "AFTER_VALUE";
	    public static final String BEFORE_VALUE_COLUMN = "BEFORE_VALUE";
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
	    public static final String SNAPSHOT_DATE = "snapShotDate";
	    public static final String REPORTED_PLAN_ENTRY_DATE = "reportedPlanEntryDate";
	    public static final String CONTRACT_DM = "isDMContract";
	    public static final String OPTOUT_DAYS = "optOutDays";
	    public static final String IS_IED = "isIED";
	    public static final String INITIAL_ENROLLMENT_DATE = "initialEnrollmentDate";
	    public static final String IS_EC_ON = "eligibilityCalcOn";
	    
	    // Issue Report
	    public static final String ELIGIBILITY_ISSUES_REPORT = "eligibilityIssuesReport";
		public static final String REPORT_TYPE = "reportType";
		public static final String DATE_PATTERN = "MM/dd/yyyy";
		public static final String JOHN_HANCOCK_REPRESENTATIVE = "John Hancock Representative";
		public static final String ELIGIBILITY_ISSUES_REPORT_NAME = "Eligibility_issues_report";
		public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(
				"MM/dd/yyyy");
		
		/**
	     * The method is to make the SimpleDateFormat synchronized in order to make it thread safe
	     * 
	     * @param value
	     * @return String
	     * @throws ParseException
	     */
		public static synchronized Date  dateParseMMDDYY(String value) throws ParseException{ 
             return SIMPLE_DATE_FORMAT.parse(value); 
        }
		
		/**
	     * The method is to make the SimpleDateFormat synchronized in order to make it thread safe
	     * 
	     * @param value
	     * @return Date
	     * @throws ParseException
	     */
		public static synchronized String  dateFormatMMDDYY(Date inputDate){ 
            return SIMPLE_DATE_FORMAT.format(inputDate); 
        } 
		
		public static final String NO_RESULT_TEXT = "Curently there are no changes to display.";
		public static final String NO_ISSUES_FOUND = "There are no issues to report for the specified period";
		public static final String ELIGIBILITY_ISSUES_REPORT_TITLE = "Eligibility Issues Report";
		public static final String CONTRACT = "Contract";
		public static final String ACTUAL_DATE_OF_DOWNLOAD = "Actual Date of Download";
		public static final String REPORT_PERIOD_FROM = "Report Period From";
		public static final String REPORT_PERIOD_TO = "Report Period To";
		public static final String EMPLOYEE_LAST_NAME = "Employee Last Name";
		public static final String EMPLOYEE_FIRST_NAME = "Employee First Name";
		public static final String EMPLOYEE_MIDDLE_NAME = "Employee Middle Initial";
		public static final String EMPLOYEE_SSN = "SSN";
		public static final String EMPLOYEE_ID = "Employee ID";
		public static final String DIVISION = "Division";
		public static final String ELIGIBILITY_TO_PARTICIPATE = "Eligible to Participate";
		public static final String MONEY_TYPE = "Money Type";
		public static final String ENROLLMENT_METHOD = "Enrollment Method";
		public static final String ENROLLMENT_EFFECTIVE_DATE = "Enrollment Effective Date";
		public static final String TRANSACTION_DATE = "Transaction Date";
		public static final String PLAN_ENTRY_DATE = "Plan Entry Date";
		public static final String TRANSACTION_TYPE = "Transaction Type";
		public static final String PAYROLL_APPLICABLE_DATE = "Payroll Applicable Date";
		public static final String SUBMISSION_NUMBER = "Submission Number";
		public static final String TRANSACTION_NUMBER = "Transaction Number";
		public static final String TRANSACTION_TYPE_ENROLLMENT = "Ineligible Enrollment";
		public static final String TRANSACTION_TYPE_CONTRIBUTION = "Ineligible Contribution";
		public static final String DEFAULT = "default";

		public static final String ENROLLMENT_METHOD_CODE_PAPER = "P";
		public static final String ENROLLMENT_METHOD_CODE_ENROLLMENT = "I";
		public static final String ENROLLMENT_METHOD_CODE_AUTO_ENROLLMENT = "A";

		public static final String ENROLLMENT_METHOD_PAPER = "Paper";
		public static final String ENROLLMENT_METHOD_ENROLLMENT = "Internet";
		public static final String ENROLLMENT_METHOD_AUTO_ENROLLMENT = "Auto Enrollment";
		// Issue report end
	    
	    private Map<String,String> eligibiltyHistoryReportMap;
	    private HashMap<String, HashMap<String, EmployeeChangeHistoryVO>> eligiblilityChangeData;
	    private Map<String,String> eligOverrideMap ;
	    private Map<String,String> eligDatesMap ;
	    private Map<String,String> eligPEDMap ;
	    private List<LabelValueBean> moneyTypes;
	    
	    // For Computation period change report
	    private Map<String,String> afterValues;
	    private Map<String,String> beforeValues;
	    private Map<String,List<Timestamp>> createdDates;
	    
	    private int numberOfChanges;
	    private int numberOfProfilesWithChanges;
	    private int totalNumberOfEmployees;
	    
	    private Date notificationPeriodStartDate;
	    private Date notificationPeriodEndDate;
	    private Map <Integer, Map <Timestamp, List<EmployeeChangeHistoryVO>>> changeHistoryMap = null;
	    private List<EligibilityIssuesReportVO> eligibilityIssuesReportVOList = null;
	    
	    public EligibilityReportData(){
	    	super();
	    }
	    
	    public EligibilityReportData(ReportCriteria criteria, int totalCount) {
	        super(criteria, totalCount);
	    }

		public Map<String, String> getEligibiltyHistoryReportMap() {
			return eligibiltyHistoryReportMap;
		}

		public void setEligibiltyHistoryReportMap(
				Map<String, String> eligibiltyHistoryReportMap) {
			this.eligibiltyHistoryReportMap = eligibiltyHistoryReportMap;
		}

		public List<LabelValueBean> getMoneyTypes() {
			return moneyTypes;
		}

		public void setMoneyTypes(List<LabelValueBean> moneyTypes) {
			this.moneyTypes = moneyTypes;
		}

		public Map<String, String> getAfterValues() {
			return afterValues;
		}

		public void setAfterValues(Map<String, String> afterValues) {
			this.afterValues = afterValues;
		}

		public Map<String, String> getBeforeValues() {
			return beforeValues;
		}

		public void setBeforeValues(Map<String, String> beforeValues) {
			this.beforeValues = beforeValues;
		}

		public Map<String, List<Timestamp>> getCreatedDates() {
			return createdDates;
		}

		public void setCreatedDates(Map<String, List<Timestamp>> createdDates) {
			this.createdDates = createdDates;
		}

		public int getNumberOfChanges() {
			return numberOfChanges;
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

		public HashMap<String, HashMap<String, EmployeeChangeHistoryVO>> getEligiblilityChangeData() {
			return eligiblilityChangeData;
		}

		public void setEligiblilityChangeData(
				HashMap<String, HashMap<String, EmployeeChangeHistoryVO>> eligiblilityChangeData) {
			this.eligiblilityChangeData = eligiblilityChangeData;
		}

		public Map<String, String> getEligOverrideMap() {
			return eligOverrideMap;
		}

		public void setEligOverrideMap(Map<String, String> eligOverrideMap) {
			this.eligOverrideMap = eligOverrideMap;
		}

		public Map<String, String> getEligDatesMap() {
		    return eligDatesMap;
		}

		public void setEligDatesMap(Map<String, String> eligDatesMap) {
		    this.eligDatesMap = eligDatesMap;
		}

		public Map<String, String> getEligPEDMap() {
		    return eligPEDMap;
		}

		public void setEligPEDMap(Map<String, String> eligPEDMap) {
		    this.eligPEDMap = eligPEDMap;
		}

	/**
	 * Get notification period start date
	 * 
	 * @return notificationPeriodStartDate
	 */
	public Date getNotificationPeriodStartDate() {
		return this.notificationPeriodStartDate;
	}

	/**
	 * Set notification period start date
	 * 
	 * @param notificationPeriodStartDate
	 */
	public void setNotificationPeriodStartDate(Date notificationPeriodStartDate) {
		this.notificationPeriodStartDate = notificationPeriodStartDate;
	}

	/**
	 * Get notification period end Date
	 * 
	 * @return notificationPeriodEndDate
	 */
	public Date getNotificationPeriodEndDate() {
		return this.notificationPeriodEndDate;
	}

	/**
	 * Set notification period end Date
	 * 
	 * @param notificationPeriodEndDate
	 */
	public void setNotificationPeriodEndDate(Date notificationPeriodEndDate) {
		this.notificationPeriodEndDate = notificationPeriodEndDate;
	}

	public Map<Integer, Map<Timestamp, List<EmployeeChangeHistoryVO>>> getChangeHistoryMap() {
		return changeHistoryMap;
	}

	public void setChangeHistoryMap(
			Map<Integer, Map<Timestamp, List<EmployeeChangeHistoryVO>>> changeHistoryMap) {
		this.changeHistoryMap = changeHistoryMap;
	}

	public List<EligibilityIssuesReportVO> getEligibilityIssuesReportVOList() {
		return eligibilityIssuesReportVOList;
	}

	public void setEligibilityIssuesReportVOList(
			List<EligibilityIssuesReportVO> eligibilityIssuesReportVOList) {
		this.eligibilityIssuesReportVOList = eligibilityIssuesReportVOList;
	}
	    
}
