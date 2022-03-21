package com.manulife.pension.ps.service.submission.dao;

public interface SubmissionConstants {

    String SUBMISSION_CASE_TYPE_ADDRESS = "A";

    String SUBMISSION_CASE_TYPE_CONTRIBUTION = "C";
    
    String SUBMISSION_CASE_TYPE_CENSUS = "E";

    String INDICATOR_YES = "Y";

    String INDICATOR_NO = "N";
    
    String PAYMENT_METHOD_CODE_CASH_ACCOUNT = "CA";
    
    String PAYMENT_METHOD_CODE_DIRECT_DEBIT = "DD";

    interface Contribution {
        String PROCESS_STATUS_CODE_COMPLETE = "16";

        String PROCESS_STATUS_CODE_CANCELLED = "99";
    }
    
    interface Census {

    	String PROCESS_STATUS_CODE_DATA_CHECK_ERROR = "A3";
    	
        String PROCESS_STATUS_CODE_SYNTAX_CHECK_FAILED = "01";
        
        String PROCESS_STATUS_CODE_OUTSTANDING_ERROR_1 = "05";

        String PROCESS_STATUS_CODE_OUTSTANDING_ERROR_2 = "07";

        String PROCESS_STATUS_CODE_SUBMISSION_ERROR = "09";

        String PROCESS_STATUS_CODE_PROCESSING = "08";

        String PROCESS_STATUS_CODE_COMPLETE = "16";

        String PROCESS_STATUS_CODE_CANCELLED = "99";
    }
    
    interface Vesting {
        
        String PROCESS_STATUS_CODE_WARNINGS = "19";
        
        String PROCESS_STATUS_CODE_WARNING_IGNORES = "21";
    }
    
    interface LongTermPartTime {
        
        String PROCESS_LTPT_STATUS_CODE_WARNINGS = "6";
    }
}
