package com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject;

import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

import com.manulife.pension.ps.service.report.participant.payrollselfservice.reporthandler.PayrollSelfServiceChangesReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class PayrollSelfServiceChangesReportData extends ReportData {

	private static final long serialVersionUID = -2214979843230731101L;

	public static final String REPORT_ID = PayrollSelfServiceChangesReportHandler.class.getName();

	public static final String REPORT_NAME = "payrollSelfServiceChangesReport";

	public PayrollSelfServiceChangesReportData() {
		// Default constructor
	}

	public PayrollSelfServiceChangesReportData(ReportCriteria criteria, int count) {
		super(criteria, count);
	}

	// for filtering	 
	public static final String FILTER_CONTRACT_ID ="contractId";
	public static final String FILTER_LAST_NAME ="lastName";
	public static final String FILTER_SSN = "ssn";
	public static final String FILTER_RECORD_TYPE = "recordType";	
	public static final String FILTER_FROM_EFFECTIVE_DATE = "fromEffectiveDate";
	public static final String FILTER_TO_EFFECTIVE_DATE = "toEffectiveDate";
	public static final String FILTER_INCLUDE_LOANS = "includeLoans"; // This is the performance measure . Loan queries should be only executed if plans supports loans

	// for Sorting
	public static final String DEFAULT_SORT = "effectiveDate";
	public static final String LAST_NAME_FIELD = "lastName";
	public static final String DESCRIPTION_FIELD = "description";    
	public static final String INITIATED_FIELD = "initiated";
	public static final String EFFECTIVE_DATE_FIELD = "effectiveDate";    
	public static final String MONEY_TYPE_FIELD = "moneyType";

	// for reading from db.
	public static final String PROFILE_ID_COLUMN = "PROFILE_ID";
	public static final String CONTRACT_ID_COLUMN = "CONTRACT_ID";
	public static final String CONTRACT_NAME_COLUMN = "CONTRACT_NAME";
	public static final String CREATED_TS_COLUMN = "CREATED_TS";
	public static final String TIME_CREATED_COLUMN = "TIME_CREATED";
	public static final String INSTRUCTION_NO_COLUMN = "INSTRUCTION_NO";
	public static final String INCREASE_AMT_COLUMN = "INCREASE_AMT";    
	public static final String INCREASE_PCT_COLUMN = "INCREASE_PCT";
	public static final String EFFECTIVE_DATE_COLUMN = "EFFECTIVE_DATE";
	public static final String MONEY_SOURCE_CATEGORY_CODE_COLUMN = "MONEY_SOURCE_CATEGORY_CODE";
	public static final String MONEY_TYPE_CATEGORY_CODE_COLUMN = "MONEY_TYPE_CATEGORY_CODE";
	public static final String CONTRIB_INSTRUCT_SRC_CODE = "CONTRIBUTION_INSTRUCT_SRC_CODE";
	public static final String CONTRIB_AMT_COLUMN = "CONTRIBUTION_AMT";
	public static final String CONTRIBUTION_FREQUENCY_CODE_COLUMN = "CONTRIBUTION_FREQUENCY_CODE";
	public static final String CONTRIB_PCT_COLUMN = "CONTRIBUTION_PCT";
	public static final String MONEY_TYPE_CODE_COLUMN = "MONEY_TYPE_CODE";
	public static final String PROCESSED_STATUS_COLUMN = "PROCESSED_STATUS_CODE";
	public static final String PROCESSED_SOURCE_CODE_COLUMN = "PROCESSED_SOURCE_CODE";
	public static final String PROCESSED_TS_COLUMN = "PROCESSED_TS";
	public static final String PROCESSED_USER_PROFILE_ID_COLUMN = "PROCESSED_USER_PROFILE_ID";
	public static final String CONTRIB_OLD_AMT_COLUMN = "CONTRIBUTION_OLD_AMT";
	public static final String CONTRIB_OLD_PCT_COLUMN = "CONTRIBUTION_OLD_PCT";
	public static final String ANV_DATE_COLUMN = "ACI_ANNIVERSARY_DATE";
	public static final String SSN_COLUMN = "SOCIAL_SECURITY_NO";
	public static final String FIRST_NAME_COLUMN = "FIRST_NAME";
	public static final String LAST_NAME_COLUMN = "LAST_NAME";
	public static final String MIDDLE_INITIAL_COLUMN = "MIDDLE_INITIAL";    
	public static final String EMPLOYEE_ID_COLUMN = "EMPLOYEE_ID";
	public static final String EMPLOYER_DIVISION_COLUMN = "EMPLOYER_DIVISION";
	public static final String PARTICIPANT_STATUS_CODE_COLUMN = "PARTICIPANT_STATUS_CODE";
	public static final String CREATED_USER_ID_TYPE_COLUMN = "CREATED_USER_ID_TYPE";
	public static final String CREATED_SOURCE_CODE_COLUMN 			= "CREATED_SOURCE_CODE";
	public static final String PROCESSED_USER_ID_TYPE_COLUMN = "PROCESSED_USER_ID_TYPE";
	public static final String TIME_PROCESSED_COLUMN 				= "TIME_PROCESSED";	
	public static final String STATUS_COLUMN 						= "STATUS_CODE";
	public static final String TIME_PAYROLL_FEEDBACK_SERVICE_ADDED_COLUMN	= "PFS_ADDED_DATE";
	public static final String LOAN_NUMBER_COLUMN 							= "LOAN_NUMBER";
	public static final String LOAN_PRINCIPAL_AMOUNT_COLUMN 				= "LOAN_PRINCIPAL_AMT";
	public static final String LOAN_TOTAL_INTEREST_AMOUNT_COLUMN 			= "TOTAL_INTEREST_AMOUNT";
	public static final String LOAN_GOAL_AMOUNT_COLUMN 						= "LOAN_GOAL_AMOUNT";
	public static final String LOAN_NUMBER_OF_PAYMENTS_COLUMN 				= "NUMBER_OF_PAYMENTS";
	public static final String LOAN_EXPECTED_REPAYMENT_AMOUNT_COLUMN 		= "EXPECTED_REPAYMENT_AMOUNT";
	public static final String TIME_LOAN_CLOSURE_CREATED_COLUMN 			= "CLOSURE_INITIATED_DATE";
	public static final String TIME_LOAN_CLOSURE_EFFECTIVE_COLUMN 			= "CLOSURE_EFFECTIVE_DATE";
	public static final String LOAN_STATUS_CODE_COLUMN 						= "LOAN_STATUS_CODE";
	public static final String LOAN_STATUS_DATE_COLUMN						= "LOAN_STATUS_DATE";
	public static final String LOAN_SUBMITTED_BY_LAST_NAME_COLUMN 			= "SUBMITTED_BY_LAST_NAME";
	public static final String LOAN_SUBMITTED_BY_FIRST_NAME_COLUMN 			= "SUBMITTED_BY_FIRST_NAME";
	public static final String ISSUE_TRANSACTION_EFFECTIVE_DATE_COLUMN 		= "ISSUE_TRANSACTION_EFFECTIVE_DATE";
	public static final String ISSUE_TRANSACTION_PROCESSING_DATE_COLUMN 	= "ISSUE_TRANSACTION_PROCESSING_DATE";
	public static final String LOAN_ORIGIN_CODE_COLUMN						= "LOAN_ORIGIN_CODE";
	
	//Enrollments 
	public static final String BEFORE_TAX_DEFER_AMT_COLUMN = "BEFORE_TAX_DEFER_AMT"; 
	public static final String BEFORE_TAX_DEFER_PCT_COLUMN = "BEFORE_TAX_DEFER_PCT"; 
	public static final String DESIG_ROTH_DEF_AMT_COLUMN = "DESIG_ROTH_DEF_AMT"; 
	public static final String DESIG_ROTH_DEF_PCT_COLUMN = "DESIG_ROTH_DEF_PCT";
	public static final String ENROLL_BEFORE_TAX_DEFER_PCT_COLUMN = "ENROLL_BEFORE_TAX_DEFER_PCT"; 
	public static final String ENROLL_BEFORE_TAX_DEFER_AMT_COLUMN = "ENROLL_BEFORE_TAX_DEFER_AMT"; 
	public static final String ENROLL_DESIG_ROTH_DEF_PCT_COLUMN = "ENROLL_DESIG_ROTH_DEF_PCT"; 
	public static final String ENROLL_DESIG_ROTH_DEF_AMT_COLUMN = "ENROLL_DESIG_ROTH_DEF_AMT";
	public static final String ENROLLMENT_EFFECTIVE_DATE_COLUMN = "ENROLLMENT_EFFECTIVE_DATE";
	public static final String MFC_CONTRACT_ENROLLMENT_DATE_COLUMN = "MFC_CONTRACT_ENROLLMENT_DATE";
	public static final String ENROLLMENT_PROCESSED_DATE = "ENROLLMENT_PROCESSED_DATE";
	public static final String ENROLLMENT_METHOD_CODE = "ENROLLMENT_METHOD_CODE";
	
	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/uuuu").withResolverStyle(ResolverStyle.STRICT);

	public static final String TRAD_MONEY_TYPE_CODE = "EEDEF";
	public static final String ROTH_MONEY_TYPE_CODE = "EEROT";

	public void setPageNumber(int pageNumber) {
		if(this.getReportCriteria() == null) {
			this.setReportCriteria(new ReportCriteria(REPORT_ID));
		}

		this.getReportCriteria().setPageNumber(pageNumber);
	}


}
