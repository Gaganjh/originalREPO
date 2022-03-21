package com.manulife.pension.service.withdrawal;

public interface SQLConstants {
    public static final String SQL_INSERT_DUMMY_WITHDRAWAL_1 = 
        "INSERT INTO STP100.SUBMISSION (" +
        "  SUBMISSION_ID," +
        "  SUBMISSION_TS," +
        "  USER_ID," +
        "  CREATED_USER_ID," +
        "  CREATED_TS," +
        "  LAST_UPDATED_USER_ID," +
        "  LAST_UPDATED_TS," +
        "  PAYMENT_INFO_ONLY_IND)" + 
        " VALUES (" +
        "  ?," +
        "  CURRENT TIMESTAMP," +
        "  ?," +
        "  ?," +
        "  CURRENT TIMESTAMP," +
        "  ?," +
        "  CURRENT TIMESTAMP," +
        "  'N')";
    
    public static final String SQL_INSERT_DUMMY_WITHDRAWAL_2 = 
        "INSERT INTO STP100.SUBMISSION_CASE (" +
        "  SUBMISSION_ID," +
        "  CONTRACT_ID," +
        "  SUBMISSION_CASE_TYPE_CODE," +
        "  SYNTAX_ERROR_IND," +
        "  PROCESSED_TS," +
        "  PROCESS_STATUS_CODE," +
        "  CREATED_USER_ID," +
        "  CREATED_TS," +
        "  LAST_UPDATED_USER_ID," +
        "  LAST_UPDATED_TS," +
        "  SUBMIT_COUNT )" +
        " VALUES (" +
        "  ?," +
        "  ?," +
        "  'W'," +
        "  'N'," +
        "  CURRENT TIMESTAMP," +
        "  ?," +
        "  ?," +
        "  CURRENT TIMESTAMP," +
        "  ?," +
        "  CURRENT TIMESTAMP," +
        "  1)";

    public static final String SQL_INSERT_DUMMY_WITHDRAWAL_3 = 
        "INSERT INTO STP100.SUBMISSION_WITHDRAWAL (" +
        "  SUBMISSION_ID," +
        "  CONTRACT_ID," +
        "  SUBMISSION_CASE_TYPE_CODE," +
        "  CREATED_BY_ROLE_CODE," +
        "  PROFILE_ID," +
        "  PARTICIPANT_ID," +
        "  RESIDENCE_STATE_CODE," +
        "  BIRTH_DATE," +
        "  WITHDRAWAL_REASON_CODE," +
        "  WITHDRAWAL_REASON_DETAIL_CODE," +
        "  PARTICIPANT_LEAVING_PLAN_IND," +
        "  PAYMENT_TO_CODE," +
        "  REQUEST_DATE," +
        "  EXPIRATION_DATE," +
        "  WITHDRAWAL_AMOUNT_TYPE_CODE," +
        "  WITHDRAWAL_AMOUNT," +
        "  UNVESTED_AMOUNT_OPTION_CODE," +
        "  IRS_DIST_CODE_LOAN_CLOSURE," +
        "  LOAN_OPTION_CODE," +
        "  EFFECTIVE_DATE," +
        "  CREATED_USER_PROFILE_ID," +
        "  CREATED_TS," +
        "  LAST_UPDATED_USER_PROFILE_ID," +
        "  LAST_UPDATED_TS," +
        "  LAST_FEE_CHNG_WAS_PS_USER_IND)" +     
        " VALUES (" +
        "  ?," +
        "  ?," +
        "  'W'," +
        "  'TP'," +
        "  ?," +
        "  ?," +
        "  'AK'," +
        "  '09/13/1963'," +
        "  'HA'," +
        "  'ME'," +
        "  'N'," +
        "  'PA'," +
        "  '02/27/2007'," +
        "  '02/27/2007'," +
        "  'SA'," +
        "  500.00," +
        "  'CA'," +
        "  '1'," +
        "  'CL'," +
        "  (CURRENT DATE - 7 days)," +
        "  ?," +
        "  CURRENT TIMESTAMP," +
        "  ?," +
        "  CURRENT TIMESTAMP," +
        "  ?)";

    public static final String SQL_INSERT_DUMMY_WITHDRAWAL_4 = 
        "INSERT INTO STP100.WITHDRAWAL_LOAN_DETAIL (" +
        "  SUBMISSION_ID," +
        "  LOAN_ID," +
        "  OUTSTANDING_LOAN_AMT," +
        "  CREATED_USER_PROFILE_ID," +
        "  CREATED_TS," +
        "  LAST_UPDATED_USER_PROFILE_ID," +
        "  LAST_UPDATED_TS )" +
        " VALUES (" +
        "  ?," +
        "  12," +
        "  11592.33," +
        "  ?," +
        "  CURRENT TIMESTAMP," +
        "  ?," +
        "  CURRENT TIMESTAMP)";

    public static final String SQL_INSERT_DUMMY_WITHDRAWAL_5_1 = 
        "INSERT INTO STP100.WITHDRAWAL_MONEY_TYPE (" +
        "  SUBMISSION_ID," +
        "  MONEY_TYPE_ID," +
        "  TOTAL_BAL_EXCL_LOAN_PBA_AMT," +
        "  VESTING_PCT," +
        "  VESTING_PCT_UPDATABLE_IND," +
        "  WITHDRAWAL_AMT," +
        "  WITHDRAWAL_PCT," +
        "  CREATED_USER_PROFILE_ID," +
        "  CREATED_TS," +
        "  LAST_UPDATED_USER_PROFILE_ID," +
        "  LAST_UPDATED_TS)" +
        " VALUES (" +
        "  ?," +
        "  'EEDEF'," +
        "  283.38," +
        "  100.000," +
        "  'N'," +
        "  0.00," +
        "  0.00," +
        "  ?," +
        "  CURRENT TIMESTAMP," +
        "  ?," +
        "  CURRENT TIMESTAMP)";

    public static final String SQL_INSERT_DUMMY_WITHDRAWAL_5_2 = 
        "INSERT INTO STP100.WITHDRAWAL_MONEY_TYPE (" +
        "  SUBMISSION_ID," +
        "  MONEY_TYPE_ID," +
        "  TOTAL_BAL_EXCL_LOAN_PBA_AMT," +
        "  VESTING_PCT," +
        "  VESTING_PCT_UPDATABLE_IND," +
        "  WITHDRAWAL_AMT," +
        "  WITHDRAWAL_PCT," +
        "  CREATED_USER_PROFILE_ID," +
        "  CREATED_TS," +
        "  LAST_UPDATED_USER_PROFILE_ID," +
        "  LAST_UPDATED_TS)" +
        " VALUES (" +
        "  ?," +
        "  'EERC'," +
        "  4508.57," +
        "  100.000," +
        "  'N'," +
        "  500.00," +
        "  0.11," +
        "  ?," +
        "  CURRENT TIMESTAMP," +
        "  ?," +
        "  CURRENT TIMESTAMP)";


    public static final String SQL_INSERT_DUMMY_WITHDRAWAL_5_3 = 
        "INSERT INTO STP100.WITHDRAWAL_MONEY_TYPE (" +
        "  SUBMISSION_ID," +
        "  MONEY_TYPE_ID," +
        "  TOTAL_BAL_EXCL_LOAN_PBA_AMT," +
        "  VESTING_PCT," +
        "  VESTING_PCT_UPDATABLE_IND," +
        "  WITHDRAWAL_AMT," +
        "  WITHDRAWAL_PCT," +
        "  CREATED_USER_PROFILE_ID," +
        "  CREATED_TS," +
        "  LAST_UPDATED_USER_PROFILE_ID," +
        "  LAST_UPDATED_TS)" +
        " VALUES (" +
        "  ?," +
        "  'ERMC1'," +
        "  4833.40," +
        "  0.000," +
        "  'N'," +
        "  500.00," +
        "  0.00," +
        "  ?," +
        "  CURRENT TIMESTAMP," +
        "  ?," +
        "  CURRENT TIMESTAMP)";

    public static final String SQL_INSERT_DUMMY_WITHDRAWAL_7 = 
        "INSERT INTO STP100.RECIPIENT (" +
        "  SUBMISSION_ID," +
        "  RECIPIENT_NO," +
        "  FEDERAL_TAX_PCT," +
        "  CREATED_USER_PROFILE_ID," +
        "  CREATED_TS," +
        "  LAST_UPDATED_USER_PROFILE_ID," +
        "  LAST_UPDATED_TS )" +
        " VALUES (" +
        "  ?," +
        "  1," +
        "  20," +
        "  ?," +
        "  CURRENT TIMESTAMP," +
        "  ?," +
        "  CURRENT TIMESTAMP)";

    public static final String SQL_INSERT_DUMMY_WITHDRAWAL_8 = 
        "INSERT INTO STP100.PAYEE (" +
        "  SUBMISSION_ID," +
        "  RECIPIENT_NO," +
        "  PAYEE_NO," +
        "  PAYEE_TYPE_CODE," +
        "  PAYEE_REASON_CODE," +
        "  CREATED_USER_PROFILE_ID," +
        "  CREATED_TS," +
        "  LAST_UPDATED_USER_PROFILE_ID," +
        "  LAST_UPDATED_TS )" +
        " VALUES (" +
        "  ?," +
        "  1," +
        "  1," +
        "  'PA'," +
        "  'P'," +
        "  ?," +
        "  CURRENT TIMESTAMP," +
        "  ?," +
        "  CURRENT TIMESTAMP)";

    public static final String SQL_GET_WITHDRAWAL_STATUS = 
        "SELECT PROCESS_STATUS_CODE FROM STP100.SUBMISSION_CASE WHERE SUBMISSION_ID = ?";

    public static final String SQL_GET_WITHDRAWAL_EXPIRY_DATE = 
        "SELECT EXPIRATION_DATE FROM STP100.SUBMISSION_WITHDRAWAL WHERE SUBMISSION_ID = ?";
    
    public static final String SQL_GET_WITHDRAWAL_ACTIVITY_SUMMARY = 
        "SELECT COUNT(*) FROM STP100.ACTIVITY_SUMMARY WHERE SUBMISSION_ID = ?";
    
    public static final String SQL_DELETE_DUMMY_WITHDRAWAL_1 = 
        "DELETE FROM STP100.SUBMISSION WHERE SUBMISSION_ID = ?";
    public static final String SQL_DELETE_DUMMY_WITHDRAWAL_2 = 
        "DELETE FROM STP100.SUBMISSION_CASE WHERE SUBMISSION_ID = ?";
    public static final String SQL_DELETE_DUMMY_WITHDRAWAL_3 = 
        "DELETE FROM STP100.SUBMISSION_WITHDRAWAL WHERE SUBMISSION_ID = ?";
    public static final String SQL_DELETE_DUMMY_WITHDRAWAL_4 = 
        "DELETE FROM STP100.WITHDRAWAL_LOAN_DETAIL WHERE SUBMISSION_ID = ?";
    public static final String SQL_DELETE_DUMMY_WITHDRAWAL_5 = 
        "DELETE FROM STP100.WITHDRAWAL_MONEY_TYPE WHERE SUBMISSION_ID = ?";
    public static final String SQL_DELETE_DUMMY_WITHDRAWAL_6 = 
        "DELETE FROM STP100.ACTIVITY_SUMMARY WHERE SUBMISSION_ID = ?";
    public static final String SQL_DELETE_DUMMY_WITHDRAWAL_7 = 
        "DELETE FROM STP100.RECIPIENT WHERE SUBMISSION_ID = ?";
    public static final String SQL_DELETE_DUMMY_WITHDRAWAL_8 = 
        "DELETE FROM STP100.PAYEE WHERE SUBMISSION_ID = ?";
    public static final String SQL_DELETE_DUMMY_WITHDRAWAL_9 = 
        "DELETE FROM STP100.PAYEE_PAYMENT_INSTRUCTION WHERE SUBMISSION_ID = ?";
    public static final String SQL_DELETE_DUMMY_WITHDRAWAL_10 = 
        "DELETE FROM STP100.DISTRIBUTION_ADDRESS WHERE SUBMISSION_ID = ?";
    public static final String SQL_DELETE_DUMMY_WITHDRAWAL_11 = 
        "DELETE FROM STP100.WITHDRAWAL_LEGALESE WHERE SUBMISSION_ID = ?";
    public static final String SQL_DELETE_DUMMY_WITHDRAWAL_12 = 
        "DELETE FROM STP100.PAYEE_PAYMENT_INSTRUCTION WHERE SUBMISSION_ID = ?";
    public static final String SQL_DELETE_DUMMY_WITHDRAWAL_13 = 
        "DELETE FROM STP100.DECLARATION WHERE SUBMISSION_ID = ?";
    public static final String SQL_DELETE_DUMMY_WITHDRAWAL_14 = 
        "DELETE FROM STP100.DISTRIBUTION_ADDRESS WHERE SUBMISSION_ID = ?";
    public static final String SQL_DELETE_DUMMY_WITHDRAWAL_15 = 
        "DELETE FROM STP100.ACTIVITY_DETAIL WHERE SUBMISSION_ID = ?";
    public static final String SQL_DELETE_DUMMY_WITHDRAWAL_16 = 
        "DELETE FROM STP100.ACTIVITY_DYNAMIC_DETAIL WHERE SUBMISSION_ID = ?";
}
