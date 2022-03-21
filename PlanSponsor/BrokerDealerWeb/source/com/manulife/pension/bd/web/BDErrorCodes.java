package com.manulife.pension.bd.web;

import com.manulife.pension.platform.web.CommonErrorCodes;

public interface BDErrorCodes extends CommonErrorCodes {
    public static final int NO_PARTICIPANTS_INVESTED = 0; // value is 1217 in PSW. Right now, since

    // we dont know the CMA Content id, putting the value as 0.
    // Transaction errors
    public static final int INVALID_DATE_RANGE = 1104;

    // Investment charting errors
    /**
     * TODO - saravana need to replace the with new error code
     */
    public static final int CHART_REQUIRED_FUND_SELECTION1 = 1224;

    // need to remove this later
    public static final int CHART_HOUSTON_PROBLEM = 9000;

    public static final int CHART_GENERIC = 9000;

    // Validation rules for Deferral Summary and Participant Transaction History search dates

    public static final int BOTH_DATES_EMPTY = 2264;

    public static final int FROM_DATE_EMPTY = 2265;

    public static final int FROM_DATE_AFTER_TO = 2266;

    public static final int INVALID_DATE = 2268;

    public static final int BOTH_DATES_NEEDED = 8030;

    public static final int FROM_DATE_BEFORE_24_MONTHS = 2267;

    public static final int CONTRACT_EXIST_ON_THE_OTHER_SITE = 2263;

    public static final int MESSAGE_NO_RESULTS_FOR_SEARCH_CRITERIA = 2173;

    // end ....

    // Participant Account
    public static final int PARTICIPANT_ACCOUNT_MONEY_TYPE_REPORTS_NOT_AVAILABLE = 54216;

    public static final int PARTICIPANT_ACCOUNT_NO_PARTICIPANTS = 51393;

    /*------------- Login ------------------------------------*/
    //public static final int PASSWORD_INCORRECT = 1002;
    //US42589-  FRW CMA Error messages
    public static final int PASSWORD_INCORRECT = 1001;
    
    public static final int PASSWORD_FAILED_N_TIMES = 1003;
    public static final int PASSWORD_FAILED_ALMOST_N_TIMES = 1009;
    
    //public static final int PROFILE_NOT_REGISTERED = 9170;
    //US42589-  FRW CMA Error messages
    public static final int PROFILE_NOT_REGISTERED = 1001;

    public static final int NO_BDW_ACCESS = 9200;

    public static final int NO_ACTIVE_BROKER_ENTITY = 9201;

    public static final int ACCESS_LOCKED = 9202;
    
    public static final int RVP_USER_INVALID_PARTY = 9203;

    //public static final int INACTIVE_PASSWORD = 9204;
    //US42589-  FRW CMA Error message
    public static final int INACTIVE_PASSWORD = 1001;

    public static final int RESET_PASSWORD_EXPIRED = 9206;

    public static final int FIRMREP_USER_NO_PARTY = 9207;

    public static final int USER_PROFILE_DELETED = 9129;
    
    public static final int ASSISTANT_DISABLED_LOGIN = 9279;
    
    /*------------  Activation --------------------------------*/
    public static final int ACTIVATION_STATUS_EXPIRED = 9208;

    public static final int ACTIVATION_STATUS_COMPLETE = 9209;

    public static final int ACTIVATION_STATUS_FAILED = 9210;

    public static final int ACTIVATION_INVALID_REQUEST = 9211;

    public static final int ACTIVATION_NEW_PWD_EXPIRED = 9212;

    public static final int ACTIVATION_USERNAME_INVALID = 9213;

    public static final int ACTIVATION_PROFILE_DELETED = 9235;

    public static final int ACTIVATION_PASSWORD_STATUS_INVALID = 9204;
    
    public static final int ACTIVATION_USERPARTY_STATUS_INVALID = 9245;

    /*------------- Change Temp Password ----------------------*/
    public static final int CHANGE_TEMP_PWD_ACCESSCODE_NOT_MATCH = 9165;

    public static final int CHANGE_TEMP_PWD_FAIL = 9167;

    public static final int CHANGE_TEMP_PWD_ACCESS_CODE_MISSING = 9130;

    public static final int CHANGE_TEMP_PWD_ACCESS_CODE_INVALID = 9131;

    /*------------- Forget Password ---------------------------*/
    public static final int FORGET_PWD_MISSING_USER_ID = 1007;

    public static final int FORGET_PWD_INVALID_USER_ID = 2148;

    public static final int FORGET_PWD_PROFILE_NOT_FOUND = 1001;

    public static final int FORGET_PWD_NOT_ALLOWED_INTERNAL_USER = 9168;

    public static final int FORGET_PWD_FAILED_CHALLENGE = 9169;

    //public static final int FORGET_PWD_PROFILE_NOT_ACTIVE = 9170;
   //US42589-  FRW CMA Error messages
    public static final int FORGET_PWD_PROFILE_NOT_ACTIVE = 1001;
    
    public static final int FORGET_PWD_PROFILE_NO_EMAIL_ADDRESS = 9166;

    public static final int FORGET_PWD_PROFILE_DELETED = 9171;

    public static final int FORGET_PWD_BLANK_ANSWER = 2151;

    public static final int FORGET_PWD_ANSWER_NOT_MATCH = 2154;
    
    public static final int FORGET_PWD_NOT_ALLOWED_TO_CHANGED = 1561;

    /*------------- Reset Password -----------------------------*/
    public static final int RESET_PWD_ACCESS_CODE_MISSING = 9172;

    public static final int RESET_PWD_EMAIL_MISSING = 9173;

    public static final int RESET_PWD_INVALID_PROFILE_STATUS = 9197;

    /*------------- Firm Rep Creation  ------------------------*/

    public static final int FIRMREP_CREATION_MISSING_FIRMS = 9151; 

    public static final int MISSING_ACCESS_CODE = 9130;

    public static final int INVALID_ACCESS_CODE = 9131;

    public static final int PROFILE_NOT_IN_NEW_STATUS = 9128;
    
    /*------------ Firm Rep Registration ---------------------*/
    public static final int FIRMREP_REG_EXPIRED = 9237;

    public static final int FIRMREP_REG_NO_FIRMS = 9236;

    public static final int FIRMREP_REG_ACTIVATION_NOT_FOUND = 9120;

    public static final int FIRMREP_REG_REGISTERED = 9121;

    public static final int FIRMREP_REG_LOCKED = 9124;

    public static final int REG_FAIL_ALMOST_NTIMES = 9123;

    public static final int FIRMREP_REG_VALIDATION_FAIL = 9132;
    
    /*------------- Firm Rep Creation  ------------------------*/
    public static final int RIA_USER_CREATION_MISSING_FIRMS = 9803;
    
    
    /*------------- RIA Statement Viewer Creation  ------------------------*/
    public static final int RIA_USER_CREATION_FIRMS_EXCEEDED = 9084;
    
    /*------------- RIA Statements  ------------------------*/
    public static final int RIA_STATEMENT_NOT_ACCESSABLE = 6011;	
    
    public static final int RIA_STATEMENT_FILE_NOT_ACCESSABLE = 6012;	
    
    /*------------ RIA User Registration ---------------------*/
    public static final int RIAUSER_REG_ACTIVATION_NOT_FOUND = 9809;
    
    public static final int RIAUSER_REG_EXPIRED = 9237;
    
    public static final int RIAUSER_REG_REGISTERED = 9121;
    
    public static final int RIAUSER_REG_LOCKED = 9800;

    public static final int RIAUSER_REG_VALIDATION_FAIL = 9810;
    
    public static final int RIAUSER_REG_FAIL_ALMOST_NTIMES = 9807;
    
    public static final int RIAUSER_REG_CANCEL_CLICKED = 6000;
    
    public static final int RIAUSER_USER_PROFILE_DELETED = 9806;
    
    public static final int MISSING_IARD_NUMBER = 9801;
    
    public static final int INVALID_IARD_NUMBER = 9802;
    
    public static final int RIA_EMAIL_EXISTS = 9805;

    /*------------- Broker Assistant Creation --------------------*/
    public static final int ASSISTANT_CREATION_MISSING_TERM = 9190;

    public static final int ASSISTANT_DUPLICATE = 9191;

    /*------------- Broker Assistant Registration --------------------*/

    public static final int ASSITANT_REG_ACTIVATION_NOT_FOUND = 9120;

    public static final int ASSISTANT_REG_REGISTERED = 9121;

    public static final int ASSISTANT_REG_LOCKED = 9122;

    public static final int ASSISTANT_REG_EXPIRED = 9237;

    public static final int ASSISTANT_REGISTRATION_MISSING_SUPERVISOR_LAST_NAME = 9125;

    public static final int ASSISTANT_REGISTRATION_INVALID_SUPERVISOR_LAST_NAME = 9126;

    public static final int ASSISTANT_REG_VALIDATION_FAIL = 9127;
    
    public static final int ASSISTANT_REG_NOT_ALLOWED = 9280;
    
    /*------------- Broker Registration --------------------*/

    public static final int BROKER_VERIFICATION_FAIL = 9109;

    public static final int BOTH_SSN_AND_TAX_ID_BLANK = 9108;

    public static final int BROKER_ENTITY_ATTACHED = 9111;

    public static final int BROKER_ENTITY_MISSING_EMAIL = 9116;

    public static final int BROKER_ENTITY_NO_ACTIVE_CONTRACT = 9112;

    public static final int MISSING_CONTRACT_NUMBER = 9107;

    public static final int INVALID_CONTRACT_NUMBER = 2323;

    public static final int MISSING_PROFILE_FIRST_NAME = 9101;

    public static final int INVALID_PROFILE_FIRST_NAME = 9102;

    public static final int MISSING_PROFILE_LAST_NAME = 9103;

    public static final int INVALID_PROFILE_LAST_NAME = 9104;

    public static final int INVALID_BD_FIRM_NAME = 9152;
    
    public static final int INVALID_PROFILE_FIRST_AND_LAST_NAME = 9380;
    
    /*------------- Basic Broker Registration --------------------*/

    public static final int BASIC_BROKER_NOT_FOUND_IN_PEOPLESOFT = 9110; // TODO

    public static final int MISSING_FIRST_NAME = 2126;

    public static final int INVALID_FIRST_NAME = 2127;

    public static final int MISSING_LAST_NAME = 2128;

    public static final int INVALID_LAST_NAME = 7085;

    public static final int BOTH_BD_FIRM_AND_INDEPENDENT_NOT_SELECTED = 9113;

    public static final int BD_FIRM_NOT_ENTERED = 9114;

    public static final int INVALID_BD_FIRM = 9115;

    /*------------- My profile update ---------------------*/
    public static final int CURRENT_PWD_MANDATORY = 2161;

    public static final int CURRENT_PWD_NOT_ACTIVE = 9199;

    // TODO - Valid Error codes should be added
    public static final int MISSING_PHONE_NUMBER = 9105;

    public static final int INVALID_PHONE_NUMBER = 9106;

    public static final int INVALID_TAX_ID = 9214;

    public static final int LICENSE_VERIFICATION_NOT_SELECTED = 9150;

    public static final int FUND_LISTING_NOT_SELECTED = 9118;

    public static final int TERMS_AND_CONDITIONS_NOT_CHECKED = 9119;

    // Challenge Questions Input Validation
    public static final int FIRST_CHALLENGE_QUESTION_NOT_SELECTED = 9133;

    public static final int FIRST_CREATE_CHALLENGE_QUESTION_NOT_ENTERED = 9134;

    public static final int FIRST_CREATE_CHALLENGE_QUESTION_INVALID = 9135;

    public static final int FIRST_CHALLENGE_QUESTION_ANSWER_NOT_ENTERED = 9136;

    public static final int FIRST_CHALLENGE_QUESTION_ANSWER_INVALID = 9137;

    public static final int FIRST_CHALLENGE_QUESTION_CONFIRM_ANSWER_NOT_ENTERED = 9138;

    public static final int FIRST_CHALLENGE_QUESTION_ANSWERS_MISMATCH = 9139;

    public static final int SECOND_CHALLENGE_QUESTION_NOT_SELECTED = 9140;

    public static final int SECOND_CREATE_CHALLENGE_QUESTION_NOT_ENTERED = 9141;

    public static final int SECOND_CREATE_CHALLENGE_QUESTION_INVALID = 9142;

    public static final int SELECTED_CHALLENGE_QUESTIONS_SAME = 9143;

    public static final int CREATED_CHALLENGE_QUESTIONS_SAME = 9144;

    public static final int SECOND_CHALLENGE_QUESTION_ANSWER_NOT_ENTERED = 9145;

    public static final int SECOND_CHALLENGE_QUESTION_ANSWER_INVALID = 9146;

    public static final int CHALLENGE_QUESTIONS_ANSWERS_SAME = 9147;

    public static final int SECOND_CHALLENGE_QUESTION_CONFIRM_ANSWER_NOT_ENTERED = 9148;

    public static final int SECOND_CHALLENGE_QUESTION_ANSWERS_MISMATCH = 9149;

    // Address Validation
    public static final int ADDRESS_LINE1_NOT_ENTERED = 2249;

    public static final int ADDRESS_LINE1_INVALID = 8009;

    public static final int ADDRESS_LINE2_INVALID = 8008;

    public static final int CITY_NOT_ENTERED = 2250;

    public static final int CITY_INVALID = 8007;

    public static final int STATE_NOT_SELECTED_FOR_USA = 2670;

    public static final int ZIP_CODE1_NOT_ENTERED_FOR_USA = 2275;

    public static final int ZIP_CODE1_INVALID_FOR_USA = 2276; // Not finalized in DFS. Check later.

    public static final int ZIP_CODE2_INVALID_FOR_USA = 2276; // Not finalized in DFS. Check later.

    public static final int ZIP_CODE_NOT_IN_RANGE_FOR_USA = 2673;

    // User Profile Address Validation
    public static final int USER_PROFILE_ADDRESS_LINE1_NOT_ENTERED = 9179;

    public static final int USER_PROFILE_CITY_NOT_ENTERED = 9180;

    public static final int USER_PROFILE_STATE_NOT_ENTERED_FOR_US_ADDRESS = 9181;

    public static final int USER_PROFILE_STATE_INVALID_FOR_US_ADDRESS = 9182;

    public static final int USER_PROFILE_ZIP_CODE_NOT_ENTERED_FOR_US = 9188;

    public static final int USER_PROFILE_ZIP_CODE_INVALID_FOR_US = 9183;

    public static final int USER_PROFILE_ZIP_CODE_NOT_IN_RANGE_FOR_USA = 9184;

    // Level 2 Broker Personal Info Tab
    public static final int PERSONAL_INFO_FIRST_NAME_NOT_ENTERED = 9175;

    public static final int PERSONAL_INFO_LAST_NAME_NOT_ENTERED = 9176;

    public static final int PERSONAL_INFO_EMAIL_NOT_ENTERED = 9177;

    public static final int PERSONAL_INFO_EMAIL_INVALID = 9178;

    public static final int PERSONAL_INFO_TELEPHONE_NUMBER_INVALID = 9185;

    public static final int PERSONAL_INFO_MOBILE_NUMBER_INVALID = 9186;

    public static final int PERSONAL_INFO_FAX_NUMBER_INVALID = 9187;

    public static final int PERSONAL_INFO_COMPANY_NAME_NOT_ENTERED = 9160;

    public static final int PERSONAL_INFO_INVALID_VALUE = 9189;

    public static final int BROKER_PERSONAL_INFO_INVALID_VALUE = 9174;

    /*------------ Funds and Performance---------------------*/
    public static final int VIEW_RESULTS_WITHOUT_SELECTION = 2258;

    public static final int ALL_SHORTLIST_OPTIONS_NOT_SELECTED = 2258;

    public static final int FUND_NAME_NOT_ENTERED = 2258;

    public static final int FUND_NAME_LESS_THAN_MINIMUM_REQUIRED_CHARS = 2258;

    public static final int NO_MATCH_FOUND_FOR_ENTERED_FUND_NAME = 2258;

    public static final int CONTRACT_NAME_SEARCH_REQUIRES_MINIMUN_3_CHARARACTERS = 2258;

    public static final int NO_MATCH_FOR_CONTRACT_NUMBER = 2258;

    public static final int NO_MATCH_FOR_CONTRACT_NAME = 2258;

    public static final int CONTRACT_RESULTS_SIZE_GREATER_THAN_35 = 2258;
    
    public static final int HISTORIC_RETURNS_NOT_AVAILABLE = 9282;

    /** -----------------User search ----------------------------------- */
    public static final int USER_SEARCH_INPUT_INVALID = 9196;

    public static final int USER_SEARCH_NO_SELECTION = 9194;

    public static final int USER_SEARCH_INSUFFICIENT_ACCESS = 9195;

    public static final int MIMIC_NOT_ALLOWED = 9253;
    /*
     * Order ACR error codes
     */
    public static final int CONTRACT_NAME_REQUIRED = 9153;

    public static final int INVALID_MONTH_AND_YEAR = 9154;

    public static final int PRESENTER_NAME_REQUIRED = 9155;

    public static final int NUMBER_OF_COPIES_REQUIRED = 9156;

    public static final int NUMBER_OF_COPIES_INVALID = 9157;

    public static final int NUMBER_OF_COPIES_NOT_GREATER_THAN_ZERO = 9158;

    public static final int RECIPIENT_NAME_REQUIRED = 9159;

    public static final int YOUR_NAME_REQUIRED = 9161;

    public static final int PHONE_EXTENSION_INVALID = 9162;

    public static final int ZIP_CODE_REQUIRED = 2674;

    /*----------------------- User Migration ----------------------------*/
    public static final int MIGRATION_PROFILE_LOCKED = 9229;

    public static final int MIGRATION_PASSWORD_ALMOST_LOCKED = 9231;

    public static final int MIGRATION_PASSWORD_LOCKED = 9230;

    public static final int MIGRATION_ALREADY_COMPLETED = 9234;

    public static final int MIGRATION_ENTITY_USED = 9232;

    public static final int MIGRATION_NO_CONTRACTS = 9233;

    /*---------------------- Fund Evaluator --------------------------------*/
    public static final int MISSING_COMPULSORY_FUNDS = 9264;
    public static final int MISSING_TOP_RANK_OPTION = 9265;
    public static final int MISSING_USA_NY_FUND = 9263;
    public static final int MISSING_SELECT_YOUR_CLIENT_OPTION = 9269;
    public static final int MISSING_FIELDS_GENEREATE_REPORT= 9259;
    public static final int CRITERIA_NOT_SELECTED= 9266;
    public static final int REMAINING_PERCENTAGE= 1206;
    public static final int NON_NUMERIC_CONTRACT_NUMBER= 2414;
    public static final int CONTRACT_NOT_IN_BOB = 9216;
    public static final int NOT_DC_CONTRACT = 9262;
    public static final int CONTRACT_STATUS_IS_NOT_VALID = 9270;
    public static final int INCOMPLETE_GENEREATE_REPORT= 9261;
    public static final int LIFECYCLE_SUITE_NOT_SELECTED = 9283;
    public static final int STATE_NOT_SELECTED = 2955;
    
    
    public static final int INVALID_TOTAL_ASSET_RANGE_FOR_FILTER = 1225;
    
    //My Profile Personal Info - Internal User
    
    public static final int ALL_PASSWORDS_MANDATORY = 9193;
    
    public static final int MUST_ENTER_ALL_CHALLENGE_QA = 9198;
    
    //User Management - Basic Broker
    
    public static final int PEOPLESOFT_NOT_AVAILABLE = 9275;
    
    public static final int USER_MANAGEMENT_INVALID_BD_FIRM = 9249;
    
    // Block Of Business Error Codes
    
    public static final int BOB_RESULT_TOO_BIG = 9215;
    
    public static final int BOB_INVALID_FORMAT_ASSET_RANGE_FROM_TO = 9255;
    
    public static final int BOB_INVALID_RANGE_FOR_ASSET_RANGE_FROM_TO = 9256;
    
    public static final int BOB_MIN_ASSET_RANGE_GT_THAN_MAX_ASSET_RANGE = 9257;
    
    public static final int CONTRACT_LESS_THAN_THREE_DIGITS = 2159;
    
    public static final int FINANCIALREP_NAME_LESS_THAN_THREE_DIGITS = 9281;
    
    public static final int BOB_SEARCH_CRITERIA_NOT_APPLICABLE = 9285;
    

    public static final int USER_MANAGEMENT_MISSING_FIRMS = 9251;
    
    public static final int USER_MANAGEMENT_MISSING_RIA_FIRMS = 9815;
    
    public static final int USER_MANAGEMENT_VIEW_RIA_FIRM_PERMISSION_EXCEEDED = 9816;
    
    public static final int USER_MANAGEMENT_VIEW_RIA_FIRM_PERMISSION_EXCEEDED_BROKER = 9804;
    
    public static final int USER_MANAGEMENT_CONFLICT_ERROR = 9051;
    
    public static final int MY_PROFILE_CONFLICT_ERROR = 9205;
    
    /*--------------------------- Message Center -------------------------*/
    public static final int MC_NO_SELECTED_MESSAGE = 9271;

    public static final int GLOBAL_MESSAGES_EXPIRE_NOT_ALLOWED = 9273;
    
    public static final int TO_DATE_EMPTY = 2383;
    
    public static final int TO_DATE_AFTER_TODAY = 2384;
    
    // Fundcheck
 
    public static final int REFINE_YOUR_SEARCH=9215;
    public static final int ENTER_ATLEAST_THREE_CHARS=9226;
    public static final int NO_SEARCH_TEXT_ENTERED=2925;    

    // Statements
    public static final int STMT_FIRST_NAME_INVALID = 3043;
    public static final int STMT_QUICK_FIRST_NAME_INVALID = 3046;
    public static final int STMT_SSN_INVALID = 3044;
    public static final int STMT_QUICK_SSN_INVALID = 3047;
    
    //error constants for Acr_Rewrite.
    //need to change the appropriate error code here
    public static final int HISTORY_DATE_RANGE_EXCEED_MESSAGE = 3536;
    public static final int HISTORY_FROMDATE_MORE_THAN_TODATE = 3537;
    public static final int HISTORY_CONTRACT_NAME_LESS_CHAR = 3535;
    public static final int HISTORY_EMPTY_CONTRACT_NUMBER = 3534;
    public static final int HISTORY_CONTRACT_NOT_FOUND_IN_BOB_CONTEXT = 9216;
    public static final int VIEW_DELETE_PR_DOC_DIALOGUE_MESSAGE = 3538;
    public static final int ENTER_SEARCH_CRITERIA = 3542;
    
    
    
    //Plan Review Print
    public static final int REQUEST_WITH_INCOMPLETE_STATUS = 3539;
    public static final int SELECT_CONTRACT_FOR_PRINT_ERROR = 3531;
    public static final int MANDATORY_FIELD = 3532;
    public static final int EMAIL_AND_ZIP_INVALID = 3533;
    public static final int INDUSTRY_SEGMENT_BLANK = 3515;
    public static final int INPROGRESS_PLAN_REVIEW_REPORT = 3516;
    public static final int PLAN_REVIEW_REPORT_PRESENTER_NAME_BLANK= 3526;
    public static final int ALLOWED_PLAN_REVIEW_REQUEST_LIMIT  = 3540;
    
    //ROR Calculation
    public final static int ERROR_CALCADHOCROR_MF_ROR_FOR_ONE_DAY = 9862;
    public final static int ERROR_CALCADHOCROR_MF_ROR_FOR_ONE_DAY_WITH_CASH_FLOW_AT_END = 9861;
    public final static int ERROR_CALCADHOCROR_MF_CLOSING_BALANCE_THREE_DOLLARS = 9863;
    public final static int ERROR_CALCADHOCROR_MF_GENERAL_ERROR = 9865;
    public final static int ERROR_ACCOUNT_BALANCE_OUTSIDE_SERVICE_HOURS = 9864;
    public final static int ERROR_CALCADHOCROR_TIME_PERIOD_NOT_SELECTED = 9850;
    public final static int SYSTEM_UNAVAILABLE = 9865;
    public final static int ERROR_CALCADHOCROR_START_DATE_REQUIRED = 9851;
    public final static int ERROR_CALCADHOCROR_START_DATE_INVALID = 9852;
    public final static int ERROR_CALCADHOCROR_START_DATE_INVALID_1 = 9853;
    public final static int ERROR_CALCADHOCROR_START_DATE_1950 = 9858;
    public final static int ERROR_CALCADHOCROR_END_DATE_INVALID = 9856;
    public final static int ERROR_CALCADHOCROR_END_DATE_INVALID_1 = 9857;
    public final static int ERROR_CALCADHOCROR_END_DATE_REQUIRED = 9855;
    public final static int ERROR_CALCADHOCROR_END_DATE_LATER_THEN_TODAY = 9860;
    public final static int ERROR_CALCADHOCROR_START_DATE_LATER_THEN_END_DATE = 9854;
    public final static int ERROR_CALCADHOCROR_START_DATE_EQUAL_END_DATE_AND_TODAY = 9859;
    public final static int ERROR_CALCADHOCROR_TIME_PERIOD_FROM_TODAY_INVALID = 9830;
    
    //IPSM Pages
    public static final int IPSM_TRUSTEE_LOCK = 1549;
    
    //IPS Manger Participant notification overlay
    public static final int ERROR_ZIP_CODE = 1400;
    public static final int ERROR_MANDATORY = 1477;
    public static final int PHONE_NOT_NUMERIC = 1118;
	public static final int PHONE_NOT_COMPLETE = 1349;

	// FRW RSA PASSCODE ERRORS
	public static final int RESET_PCD_EMAIL_MISSING = 8136;
	public static final int ERROR_EMAIL_SERVER_INACTIVE = 8137;
	public static final int PREVIOUSLY_LOCKED = 8122;
	public static final int COOLING = 8123;
	public static final int DENY = 8141;
	//Passcode Exemption
    public static final int ERROR_EXEMPTION_REASON_MANDATORY= 8129;
    public static final int ERROR_REQUESTED_BY_MUST_FULL_NAME= 8130;
    public static final int ERROR_PPM_TICKET_NUMBER_MANDATORY= 8131;
    public static final int ERROR_ALREADY_EXEMPTED_BY_INTERNAL_USER= 8132;
    public static final int ERROR_ALREADY_PASSCODE_LOCKED=8133;
    public static final int ERROR_ALREADY_REMOVED_BY_INTERNAL_USER=8134;
   
    
}
