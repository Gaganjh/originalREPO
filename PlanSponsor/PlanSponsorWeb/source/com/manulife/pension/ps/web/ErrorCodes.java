package com.manulife.pension.ps.web;

import com.manulife.pension.platform.web.CommonErrorCodes;

/**
 * ErrorCodes contains a list of constants that are used as keys for CMA error codes.
 */
public final class ErrorCodes implements CommonErrorCodes {

    // Bookmark error
    public static final int INVALID_PAGE = 2373;

    // Auto payroll errors
    public static final int AUTOPAYROLL_PS_USER_EDIT = 5010;

    public static final int AUTOPAYROLL_PS_USER_DELETE = 5011;

    public static final int AUTOPAYROLL_TPA_USER_EDIT = 5012;

    public static final int AUTOPAYROLL_TPA_USER_DELETE = 5013;

    // File upload errors
    public static final int UPLOAD_FILE_EXCEEDS_MAX_SIZE = 4000;

    public static final int UPLOAD_FILE_EMPTY = 4001;

    public static final int UPLOAD_FILE_PROBLEM = 4002;

    public static final int FILE_INFORMATION_EXCEEDS_MAX_SIZE = 2939;

    // Submission History errors
    public static final int SUBMISSION_DATES_OVERLAP = 2189;

    public static final int SUBMISSION_PAYROLL_DATES_NOT_VALID_FOR_TYPE = 2190;

    public static final int SUBMISSION_INVALID_PERMISSION = 2363;

    public static final int SUBMISSION_CASE_LOCKED = 2351;

    public static final int SUBMISSION_CANCELLED = 2364;

    public static final int NO_CONTRIBUTION_TO_COPY = 2348;

    // This submission contains no valid data
    public static final int SUBMISSION_HAS_NO_VALID_DATA = 2357;

    // Your contract is not set up for electronic submissions. Please contact your client
    // account representative for more information
    public static final int SUBMISSION_NOT_SETUP_FOR_ELECTRONIC_SUBMISSION = 2358;

    // Submission Upload errors
    public static final int SUBMISSION_FILE_NAME_TOO_LONG = 4003;

    public static final int SUBMISSION_SYSTEM_EXCEPTION = 4004;

    public static final int SUBMISSION_CANNOT_CREATE_PAYMENT = 4005;

    public static final int SUBMISSION_NEGATIVE_AMOUNT = 2193;

    public static final int SUBMISSION_MAX_AMOUNT = 2194;

    public static final int SUBMISSION_VALID_EFFECTIVE_DATE = 2195;

    public static final int SUBMISSION_PAYMENT_INFO = 2196;

    public static final int SUBMISSION_MAX_CASH_VALUE = 2197;

    public static final int SUBMISSION_CASH_CONT_ONLY = 2198;

    public static final int SUBMISSION_CASH_FUTURE_DATED = 2199;

    public static final int SUBMISSION_MAX_BILL_AMOUNT = 2200;

    public static final int SUBMISSION_MAX_TEMP_CREDIT_AMOUNT = 2201;

    public static final int SUBMISSION_VALID_FILE_TYPE = 2202;

    public static final int SUBMISSION_USE_PAYMENT_ONLY = 2203;

    public static final int SUBMISSION_VALID_GENERATE_STMT = 2204;

    public static final int SUBMISSION_VALID_PAYMENT_FILE_INFO = 2205;

    public static final int SUBMISSION_VALID_FILE_INFO = 2206;

    public static final int SUBMISSION_VALID_ADDRESS_SUBM = 2207;

    public static final int SUBMISSION_UPLOAD_IN_PROGRESS = 2208;

    public static final int SUBMISSION_DELETE_FAILED_LOCKED = 2367;

    public static final int SUBMISSION_MAX_TOTAL_AMOUNTS = 2343;

    public static final int CONTRIBUTION_ABOVE_SIZE_LIMIT = 2355;

    public static final int VESTING_ABOVE_SIZE_LIMIT = 2355;

    public static final int UNABLE_TO_ACCESS_DATA_CHECKER = 2376;

    public static final int UNABLE_TO_ACCESS_DATA_CHECKER_FOR_DISCARD = 2378;

    public static final int UNABLE_TO_ACCESS_IFILE = 2377;

    public static final int NEGATIVE_LOAN_REPAYMENT_AMOUNTS = 3028;
    
    public static final int INVALID_FILE = 3207;

    // Edit Address errors
    public static final int ADDRESS_APOLLO_UNAVAILABLE = 2365;

    public static final int ADDRESS_INVALID_PERMISSION = 2366;

    public static final int ADDRESS_LOCKED = 2351;

    public static final int ADDRESS_INVALID_LINE_1 = 2272;

    public static final int ADDRESS_INVALID_CITY = 2273;

    public static final int ADDRESS_INVALID_STATE_CODE = 2274;

    public static final int ADDRESS_MISSING_ZIP_CODE = 2275;

    public static final int ADDRESS_INVALID_ZIP_CODE = 2276;

    public static final int ADDRESS_INVALID_COUNTRY = 2277;

    public static final int ADDRESS_COUNTRY_CHANGED_TO_USA_WARNING = 2362;

    public static final int ADDRESS_INVALID_SSN = 2368;

    public static final int ADDRESS_NOT_FOUND = 2370;

    public static final int ADDRESS_MULTIPLE_ADDRESSES_ON_APOLLO = 2372;

    public static final int ADDRESS_DISCARDED_MESSAGE = 2379;

    public static final int ADDRESS_INVALID_ZIP_CODE_STATE_RELATION = 2382;

    // Edit Employee errors
    public static final int EMPLOYEE_INVALID_PERMISSION = 2366;

    public static final int EMPLOYEE_DISCARDED_MESSAGE = 2379;

    // Investment charting errors copied from the participant site
    public static final int CHART_GENERIC = 9000;

    public static final int CHART_REQUIRED_FUND_SELECTION1 = 1200;

    public static final int CHART_HOUSTON_PROBLEM = 9000;

    public static final int NO_PARTICIPANTS_INVESTED = 1217;

    // Transaction errors
    public static final int INVALID_DATE_RANGE = 1104;

    // Forgot password errors

    public static final int PERSONAL_IDENTIFIER_DOES_NOT_EXIST = 2003;

    public static final int PERSONAL_IDENTIFIER_DOES_NOT_MATCH = 2004;

    public static final int EMAIL_ADDRESS_DOES_NOT_MATCH = 2005;

    public static final int CHALLENGE_ANSWER_LOCKED = 2006;

    public static final int CHALLENGE_QUESTION_NOT_MATCHED = 2007;

    public static final int SEARCH_FIELD_MANDATORY = 2125;

    // Validation rules for Add/Edit User Profile
    public static final int EMAIL_MUST_BE_INTERNAL = 2133; // TODO: change to proper contentid

    public static final int EMAIL_MUST_BE_EXTERNAL = 1059;
    
    public static final int PRIMARY_EMAIL_MUST_BE_EXTERNAL = 2120;
    
    public static final int SECONDARY_EMAIL_MUST_BE_EXTERNAL = 2121;

    public static final int TPA_FIRM_ID_MANDATORY = 2139;

    public static final int TPA_FIRM_ID_INVALID = 1044;

    public static final int TPA_FIRM_ID_NOT_UNIQUE = 2141;

    public static final int CONTRACT_NUMBER_NOT_UNIQUE = 2146;

    public static final int EMPLOYEE_NUMBER_MANDATORY = 2143;

    public static final int EMPLOYEE_NUMBER_INVALID = 2145;

    public static final int INTERNAL_USER_NAME_MANDATORY = 2543;

    public static final int BDW_RVP_USED = 9164;

    public static final int BDW_RVP_NOT_SELETED = 9163;

    public static final int INTERNAL_USER_NAME_INVALID = 9250;

    public static final int PIN_MANDATORY = 2149;

    public static final int PIN_INVALID = 2150;

    public static final int QUESTION_MANDATORY = 1079;

    public static final int QUESTION_INVALID = 2152;

    public static final int ANSWER_MANDATORY = 2153;

    public static final int ANSWER_INVALID = 2154;

    public static final int ANSWERS_DO_NOT_MATCH = 2155;

    public static final int ADD_USER_CONTRACT_ACCESS_LEVEL_MANDATORY = 2156;

    public static final int PROFILE_MUST_HAVE_ONE_CONTRACT = 1076;

    public static final int SAVING_WITH_NO_CHANGES = 2157;

    public static final int CURRENT_PASSWORD_MANDATORY = 2161;

    public static final int LAST_PSEUM_REMAINING = 1045;

    public static final int SEARCH_CONTRACT_NO_RESULT = 1046;
    
    public static final int JHTC_NON_PASSIVE_TRUSTEE_CONTRACT_ACCESS = 3620;

    public static final int DUPLICATE_PARTICIPANT_IDENTIFIER = 2319;
    
    public static final int PARTICIPANT_NAME_IS_ALPHA_NUMERIC = 3152;

    // * error codes specific to i-cont numeric pin. to - change the values
    // public static final int NUMERIC_PIN_INVALID = 2309;
    public static final int PINS_DO_NOT_MATCH = 2308;

    public static final int INVALID_CURRENT_PIN = 2309;

    public static final int INVALID_NEW_PIN = 2330;

    public static final int INVALID_CONFIRM_PIN = 2331;

    // ACI error codes, used in Task Center
    public static int APPROVE_NON_ACCOUNT_HOLDER = 1220; // UI Code used by tag

    public static int REMARKS_TOO_LONG = 1222;

    public static int REMARK_MISSING = 1221;

    public static int NO_DEFERRAL_ON_FILE_AUTO = 58576;

    public static int NO_DEFERRAL_ON_FILE_SIGNUP = 72300;

    public static int ACI_OVERDUE = 58577;

    public static int ACI_REQUEST_OVERDUE_WARN_AUTO = 58578;

    public static int ACI_REQUEST_OVERDUE_WARN_SIGNUP = 72301;

    public static int ACI_OUTSTANDING_REQUESTS_AUTO = 58579;

    public static int ACI_OUTSTANDING_REQUESTS_SIGNUP = 72302;

    // Validation rules for Deferral Summary and Participant Transaction History search dates
    public static final int BOTH_DATES_EMPTY = 2264;

    public static final int FROM_DATE_EMPTY = 2265;

    public static final int FROM_DATE_EMPTY_HISTORY = 9327;

    public static final int TO_DATE_EMPTY_HISTORY = 9328;

    public static final int FROM_DATE_AFTER_TO = 2266;

    public static final int FROM_DATE_AFTER_TO_HISTORY = 9329;

    public static final int INVALID_DATE = 2268;

    public static final int INVALID_DATE_HISTORY = 9325;

    public static final int BOTH_DATES_NEEDED = 8030;

    public static final int FROM_DATE_BEFORE_24_MONTHS = 2267;

    public static final int CONTRACT_EXIST_ON_THE_OTHER_SITE = 2263;

    // Error for i:loans
    public static final int ILOANS_MONEY_TYPE_AMOUNT_INVALID = 2278;

    public static final int ILOANS_MONEY_TYPE_PERCENTAGE_INVALID = 2279;

    public static final int ILOANS_CALCULATE_VESTED_BUTTON_NOT_CLICKED = 2283;

    public static final int ILOANS_HIGHEST_LOAN_BALANCE_LAST_12MTHS_INVALID = 2280;

    public static final int ILOANS_NUMBER_OF_OUTSTANDING_LOANS_INVALID = 2281;

    public static final int ILOANS_CURRENT_OUTSTANDING_LOAN_BALANCE_INVALID = 2282;

    public static final int ILOANS_CONTINUE_BUTTON_ERROR_PAGE1 = 2284;

    public static final int ILOANS_HIGHEST_OS_LOAN_BALANCE_GT_CURRENT = 2303;

    public static final int ILOANS_INVALID_AMORTIZATION_PERIOD = 2288;

    public static final int ILOANS_AMORTIZATION_PERIOD_GT_MAX = 2294;

    public static final int ILOANS_INVALID_MAX_AMORTIZATION_PERIOD = 2292;

    public static final int ILOANS_INVALID_PRIMARY_RESIDENCE_AMORTIZATION_PERIOD = 2371;

    public static final int ILOANS_INVALID_HARDSHIP_AMORTIZATION_PERIOD = 2304;

    public static final int ILOANS_INVALID_GENERAL_PURPOSE_AMORTIZATION_PERIOD = 2305;

    public static final int ILOANS_INTEREST_RATE_INVALID = 2285;

    public static final int ILOANS_PAYROLL_FREQUENCY_INVALID = 2287;

    public static final int ILOANS_LOAN_AMOUNT_LT_MIN_ALLOWED = 2296;

    public static final int ILOANS_LOAN_AMOUNT_INVALID = 2286;

    public static final int ILOANS_LOAN_AMOUNT_GT_MAX_ALLOWED = 2295;

    public static final int ILOANS_CALCULATE_REPAYMENT_NOT_CLICKED = 2291;

    public static final int ILOANS_MISSING_COMMENT_FOR_DENIED_REQUEST = 2301;

    public static final int ILOANS_MISSING_DEFAULT_PROVISION_FOR_REQUEST = 2302;

    public static final int ILOANS_LOAN_SETUP_FEE_INVALID = 2289;

    public static final int ILOANS_LOAN_SETUP_FEE_GT_MAX = 2297;

    public static final int ILOANS_PLAN_INFO_EXPIRY_DATE_INVALID = 2290;

    public static final int ILOANS_SPOUSAL_CONSENT_NOT_ENTERED = 2318;

    public static final int ILOANS_TYPE_MANDATORY = 2336;

    public static final int LEGALLY_MARRIED_MANDATORY = 2375; // never used. number can be reused

    public static final int ILOANS_REASON_TEXT_TOO_LONG = 2337;

    // todo
    public static final int ILOANS_OUTSTANDING_REQUESTS_EXIST = 2354;

    /*
     * The errors below are hardcoded in IloansServiceExceptions CONTRACT_INVALID = 2323;// not
     * found, wrong TPA user, no staff plan access CONTRACT_NOT_ACTIVE = 2325;
     * CONTRACT_DOES_NOT_ALLOW_lOANS = 2326; PARTICIPANT_IS_NOT_ACTIVE = 2327; INVALID_PARTICIPANT =
     * 2324; //does not belong to this contract UNEXPIRED_LOAN_REQUEST_FOR_PROFILE = 2327;
     */
    public static final int ILOANS_MAX_LOAN_AVAILABLE_ENTERED_INVALID = 2322;

    // create loan package
    // "Invalid Loan Date";
    public static final int INVALID_LOAN_DATE = 2311;

    // Loan Date must be greater then current date
    public static final int LOAN_DATE_LT_CURRENT_DAY = 2352;

    // " Invalid date in the Next Repayment Date field. Please try again
    public static final int INVALID_NEXT_PAYMENT_DATE = 2339;

    // "Next Repayment Date must be greater then current date
    public static final int NEXT_PAYMENT_DATE_LT_CURRENT_DAY = 2340;

    // "The next repayment date must be greater than the date of loan
    public static final int NEXT_PAYMENT_DATE_LT_LOAN_DATE = 2340;

    // "The next repayment date must not be more than 31 day greater than the date of loan
    public static final int NEXT_PAYMENT_DATE_LT_LOAN_DATE_31 = 2374;

    public static final int BAD_FIRST_PAYROLL_DATE_SEMI_MONTHLY = 2347;

    public static final int ILOANS_RECALCULATE_REPAYMENT_AMT = 2369;

    // Amortization schedule
    // "Invalid Loan Date";
    public static final int AMORTIZATION_SCHEDULE_INVALID_LOAN_DATE = 2311;

    // "Invalid Loan Amount";
    public static final int AMORTIZATION_SCHEDULE_INVALID_LOAN_AMOUNT = 2314;

    // "Invalid Amortization Years";
    public static final int AMORTIZATION_SCHEDULE_INVALID_AMORTIZATION_YEARS = 2315;

    // "Invalid Nominal Annual Rate";
    public static final int AMORTIZATION_SCHEDULE_INVALID_NOMINAL_RATE = 2316;

    // "Invalid First Loan Payment Date";
    public static final int AMORTIZATION_SCHEDULE_INVALID_FIRST_LOAN_PAYMENT_DATE = 2312;

    // "Date of first payment must be greater than the date of loan";
    public static final int AMORTIZATION_SCHEDULE_FIRST_LOAN_PAYMENT_DATE_LT_LOAN_DATE = 2313;

    // "Generic Error";
    public static final int ILOANS_GENERIC_ERROR = 2317;

    // "Invalid Nominal Annual Rate";
    public static final int AMORTIZATION_SCHEDULE_INVALID_PAYROLL_FREQUENCY = 2320;

    // "Loan Scenario error. ";
    public static final int AMORTIZATION_SCHEDULE_BAD_PAYMENT_AMOUNT = 2321;

    // TPA E-Download Errors
    // "We are currently experiencing technical difficulties, and TED files are not available...."
    public static final int TED_FTP_SERVER_DOWN = 2329;

    // "No files were selected to download."
    public static final int NO_FILES_SELECTED_TO_DOWNLOAD = 2342;

    // TPA EStatements Errors
    public static final int ESTATEMENTS_CONTRACT_NUMBER_IS_NOT_VALID = 2345;

    public static final int ESTATEMENTS_TOO_MANY_RECORDS = 2346;

    public static final int ESTATEMENTS_NO_CHECK_BOX_SELECTED = 2350;

    public static final int BAD_TPA_ACCESS_LEVEL = 2380;

    // Contract Level PIN Regeneration
    public static final int PIN_GEN_ADDRESS_MANDATORY = 3004;

    public static final int PIN_GEN_PIN_STATUS_MANDATORY = 3005;

    public static final int PIN_GEN_DEFAUL_ENROLLED_MANDATORY = 3006;

    public static final int PIN_GEN_NO_PARTICIPANT_SELECTED = 3009;

    public static final int PIN_GEN_ENROLLMENT_DATE_FORMAT_INVLAID = 3010;

    public static final int PIN_GEN_ENROLLMENT_DATE_INVALID = 3011;

    public static final int PIN_GEN_INDIV_PARTICIPANT_SELECTED = 3012;

    public static final int PIN_GEN_INDIV_PASS_LIMIT = 3013;

    public static final int PIN_GEN_GROUP_PASS_LIMIT = 3014;

    // Withdrawals Errors 2400
    public static final int CONTRACT_STATUS_NOT_ALLOWED = 2400;

    public static final int USER_ROLE_NOT_ALLOWED = 2401;

    public static final int HAS_ROTH_MONEY = 2391;

    public static final int PARTICIPANT_HAS_TOTAL_STATUS = 2388;

    public static final int PARTICIPANT_HAS_MULTIPLE_WITHDRAWAL_REQUESTS = 2390;

    public static final int WITHDRAWAL_REQUEST_STATUS_IS_DRAFT = 2389;

    public static final int WITHDRAWAL_REQUEST_STATUS_SET_WITHIN_ONE_DAY = 2390;

    public static final int STEP_1_DRIVER_FIELDS_CHANGED_SINCE_SAVE = 2413;

    public static final int STEP_1_DRIVER_FIELDS_CHANGED_SINCE_SAVE_POST_DRAFT = 2436;

    public static final int PARTICIPANT_SEARCH_REQUIRES_A_CONTRACT = 2408;

    public static final int WITHDRAWAL_AMOUNT_TYPE_REQUIRED = 2495;

    public static final int WITHDRAWAL_AMOUNT_NOT_VALID = 2417;

    public static final int OPTION_FOR_UNVESTED_AMOUNT_INVALID = 2496;

    public static final int TOTAL_REQUESTED_NOT_EQUAL_TO_SPECIFIC = 2445;

    public static final int REQUESTED_AMOUNT_INVALID = 2446;

    public static final int REQUESTED_AMOUNT_NOT_GREATER_THAN_ZERO = 2470;

    public static final int REQUESTED_PERCENTAGE_INVALID = 2447;

    public static final int VESTED_PERCENTAGE_INVALID = 2448;

    public static final int VESTING_ENGINE_ALLOWS_VALUE_UPDATES = 2012;

    public static final int TOTAL_REQUESTED_NOT_GREATER_THAN_ZERO = 2449;

    public static final int SPECIFIC_AMOUNT_WITHIN_THRESHOLD = 2450;

    public static final int REQUESTED_AMOUNT_WITHIN_THRESHOLD = 2451;

    public static final int REQUESTED_AMOUNT_EXCEEDS_AVAILABLE_AMOUNT = 2452;

    public static final int REQUESTED_AMOUNT_EXCEEDS_ZERO = 2453; // to be tested

    public static final int SPECIFIC_AMOUNT_GREATER_THAN_TOTAL_BALANCE = 2454;

    public static final int REQUESTED_AMOUNT_EXCEEDS_BALANCE = 2455;

    public static final int TOTAL_REQUESTED_AMOUNT_RESTRICTED_FOR_MANDATORY_DISTRIBUTION_TERMINATION = 2456;

    public static final int WMSI_UNDER_THRESHHOLD = 2457;

    public static final int TAXES_OVER_ONE_HUNDRED_PERCENT = 2463;

    public static final int STATE_TAX_NOT_ZERO_WHEN_FEDERAL_IS = 2519;

    public static final int STATE_TAX_INVALID = 2472;

    public static final int STATE_TAX_EXCEEDS_MAXIMUM = 2538;

    public static final int STATE_TAX_CHANGED = 2412;

    public static final int FEDERAL_TAX_INVALID = 1103;

    public static final int FI_NAME_INVALID = 2480;

    public static final int FI_BANK_NAME_INVALID = 2483;

    public static final int FI_ACCOUNT_NUMBER_INVALID = 2484;

    public static final int FI_CREDIT_PARTY_NAME_INVALID = 2485;

    public static final int FI_ABA_NUMBER_INVALID = 2465;

    public static final int FI_ABA_NUMBER_NOT_GREATER_THAN_ZERO = 2473;

    public static final int CHECK_PAYEE_NAME_INVALID = 2486;

    public static final int CONTRACT_STATUS_FROZEN_ERROR = 2437;
    
       
/*	Security Enhancements - reuse obsolete error codes for Organization name validations
    public static final int FI_CREDIT_PARTY_NAME_LENGTH_CHECK_ACH = 2956;
    public static final int FI_CREDIT_PARTY_NAME_LENGTH_CHECK_WIRE = 2957;
*/
    // Security Enhancements - reuse obsolete credit party name error codes for organization name checks
    public static final int ORGANIZATION_NAME_LENGTH_CHECK_ACH = 2956;
    public static final int ORGANIZATION_NAME_LENGTH_CHECK_WIRE = 2957;
    
    public static final int INVALID_INSTALMENT_STATUS_CHANGE_ERROR = 9063;
    public static final int INVALID_APOLLO_INSTALMENT_STATUS_CHANGE_ERROR = 3631;

    //Added new error messages as part of ME March 2016 CL 131784 changes
  	public static final int WD_STATE_TAX_INVALID_FOR_PR_STATE_ROLLOVER = 3052;
  	public static final int WD_STATE_TAX_INVALID_FOR_PR_STATE_NONROLLOVER = 3053;
  
  	public static final int MISSING_LEGALLY_MARRIED_IND = 2624;

    // FI Address
    public static final int ADDRESS_FI_LINE_ONE_INVALID = 2481;

    public static final int ADDRESS_FI_CITY_INVALID = 2482;

    public static final int ADDRESS_FI_STATE_INVALID = 2444;

    public static final int ADDRESS_FI_ZIP_ONE_INVALID = 2439;

    public static final int ADDRESS_FI_ZIP_TWO_INVALID = 2518;

    public static final int ADDRESS_FI_ZIP_CODE_INVALID_FOR_STATE = 2441;

    public static final int ADDRESS_FI_COUNTRY_INVALID = 2526;

    // Check Address
    public static final int ADDRESS_CHECK_PAYEE_LINE_ONE_INVALID = 2487;

    public static final int ADDRESS_CHECK_PAYEE_CITY_INVALID = 2488;

    public static final int ADDRESS_CHECK_PAYEE_STATE_INVALID = 2498;

    public static final int ADDRESS_CHECK_PAYEE_ZIP_ONE_INVALID = 2508;

    public static final int ADDRESS_CHECK_PAYEE_ZIP_TWO_INVALID = 2510;

    public static final int ADDRESS_CHECK_PAYEE_ZIP_CODE_INVALID_FOR_STATE = 2511;

    public static final int ADDRESS_CHECK_PAYEE_COUNTRY_INVALID = 2527;

    // 1099R Address
    public static final int ADDRESS_1099R_LINE_ONE_INVALID = 2490;

    public static final int ADDRESS_1099R_CITY_INVALID = 2491;

    public static final int ADDRESS_1099R_STATE_INVALID = 2499;

    public static final int ADDRESS_1099R_ZIP_ONE_INVALID = 2509;

    public static final int ADDRESS_1099R_ZIP_TWO_INVALID = 2440;

    public static final int ADDRESS_1099R_ZIP_CODE_INVALID_FOR_STATE = 2512;

    public static final int ADDRESS_1099R_COUNTRY_INVALID = 2528;

    public static final int TPA_FEE_AMOUNT_EXCEEDS_TOTAL_BALANCE = 2458;

    public static final int TPA_FEE_AMOUNT_INVALID = 2459;

    public static final int TPA_FEE_PERCENTAGE_INVALID = 2513;

    public static final int TPA_FEE_TYPE_INVALID = 2514;

    public static final int TPA_DOLLAR_THRESHOLD = 2460;

    public static final int TPA_PRECENT_THRESHOLD = 2461;

    public static final int PAYMENT_METHOD_INVALID = 2500;

    public static final int ACCOUNT_TYPE_INVALID = 2501;

    public static final int PARTICIPANT_US_CITIZEN_INVALID = 2502;

    public static final int PARTICIPANT_NOT_US_CITIZEN = 2466;

    public static final int NOTE_TO_PARTICIPANT_INVALID = 2420;

    public static final int ACCOUNT_NUMBER_FOR_ROLLOVER_INVALID = 2478;

    public static final int NAME_OF_PLAN_INVALID = 2479;

    public static final int IRS_CODE_FOR_WITHDRAWAL_ERROR = 2497;

    public static final int IRS_CODE_FOR_WITHDRAWAL_WARNING = 2497;

    public static final int IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL = 2515;

    public static final int IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION = 2516;

    public static final int IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER = 2517;

    public static final int DECLARATION_TAX_NOTICE_INVALID_ERROR = 2425;

    public static final int DECLARATION_TAX_NOTICE_INVALID_WARNING = 2426;

    public static final int DECLARATION_WAITING_PERIOD_INVALID_ERROR = 2467;

    public static final int DECLARATION_WAITING_PERIOD_INVALID_WARNING = 2503;

    public static final int DECLARATION_IRA_PROVIDER_INVALID_ERROR = 2504;

    public static final int DECLARATION_IRA_PROVIDER_INVALID_WARNING = 2505;

    public static final int PAYMENT_TO_INVALID = 2493;

    public static final int WITHDRAWAL_REASON_INVALID = 2492;

    public static final int STATE_OF_RESIDENCE_INVALID = 2421;

    public static final int TO_DATE_EMPTY = 2383;

    public static final int TO_DATE_AFTER_TODAY = 2384;

    public static final int LAST_NAME_OR_SSN_MANDATORY = 2386;

    public static final int CONTRACT_NUMBER_MANDATORY_SEARCH = 2387;

    public static final int PARTICIPANT_HAS_PBA_MONEY = 2535;

    public static final int PARTICIPANT_HAS_ROTH_MONEY = 2391;

    public static final int CONTRACT_ONLINE_WITHDRAWALS = 2531;

    // public static final int USER_INVALID_PERMISSIONS = 2552;
    public static final int TWO_STEP_REQUEST_APPROVAL = 2532;

    // public static final int USER_MATCHES_THE_PARTICIPANT = 2442;

    public static final int LOAN_REPAYMENT = 2434;

    // Add/Edit profile Errors
    public static final int CHANGE_PERMISSION_WITHOUT_ROLE = 1061;

    public static final int REMOVE_LAST_CONTRACT = 1062;

    public static final int NAME_ALREADY_EXIST = 1063;

    public static final int NEW_CONTRACT_WITHOUT_PERMISSIONS = 1064;

    public static final int EMAIL_CONTAIN_COMPANY_NAME = 1059;

    // Security role conversion errors
    public static final int BUSINESS_CONVERTED_CONTRACT = 1071;

    // manage tpa firm permissions errors
    public static final int NO_TPA_USERS_SELECTED = 2536;

    public static final int NO_WEB_ACCESS = 1080;

    public static final int USER_ALREADY_DELETED = 1081;

    // FIXME -- replace with its own id once available
    public static final int BIRTH_DATE_INVALID = 2427;

    public static final int IRS_CODE_FOR_LOAN_ERROR = 2494;

    public static final int IRS_CODE_FOR_LOAN_WARNING = 2494;

    public static final int IRS_CODE_FOR_LOAN_SHOULD_BE_NORMAL = 2506;

    public static final int IRS_CODE_FOR_LOAN_SHOULD_BE_EARLY_DISTRIBUTION = 2507;

    public static final int IRS_CODE_FOR_LOAN_SHOULD_BE_ROLLOVER = 2435;

    public static final int HardshipReasonExplanationMissingError = 2403;

    public static final int HardshipReasonCodeMissingError = 2403;

    public static final int DISABILITY_DATE_MISSING_ERROR = 2477;

    public static final int RETIREMENT_DATE_MISSING_ERROR = 2476;
    
    public static final int IRS_DISTRIBUTION_CODE_FOR_LOANS_ERROR = 3211;

    public static final int RETIREMENT_DATE_BEFORE_CONTRACT_EFFECTIVE = 2430;

    public static final int TERMINATION_DATE_MISSING_ERROR = 2475;

    public static final int TERMINATION_DATE_BEFORE_CONTRACT_EFFECTIVE = 2429;

    public static final int FINAL_CONTRIBUTION_DATE_MISSING_ERROR = 2548;

    public static final int FINAL_CONTRIBUTION_DATE_MISSING_WARNING = 2548;

    public static final int FINAL_CONTRIBUTION_DATE_BEFORE_CONTRACT_EFFECTIVE_ERROR = 2431;

    public static final int FINAL_CONTRIBUTION_DATE_OVER_SIX_MONTHS_IN_FUTURE_ERROR = 2443;

    public static final int LOAN_OPTION_REPAY_SELECTED_ERROR = 2434;

    public static final int EXPIRATION_DATE_INVALID = 2474;

    public static final int EXPIRATION_DATE_WITHIN_WARNING_THRESHOLD = 2520;

    public static final int WITHDRAWAL_REQUEST_HAS_EXPIRED_LIST_PAGE = 2530;

    public static final int WITHDRAWAL_REQUEST_HAS_EXPIRED_VIEW_PAGE = 2529;

    public static final int WITHDRAWAL_REQUEST_HAS_EXPIRED_INITIATE_REVIEW_PAGE = 2534;

    public static final int EXPIRATION_DATE_BEFORE_SAVED = 2521;

    public static final int EXPIRATION_DATE_GREATER_THAN_MAXIMUM = 3051;

    public static final int BIRTH_DATE_EMPTY_OR_BLANK = 2419;

    public static final int BIRTH_DATE_GREATER_THAN_ENROLLMENT_DATE = 2428;

    public static final int INVALID_REASON_CODE_RETIREMENT = 2432;

    public static final int WITHDRAWAL_REASON_DOES_NOT_MATCH_PARTICIPANT_STATUS = 2433;

    // Withdrawals Vesting Messages
    public static final int VESTING_CREDITING_METHOD_IS_UNSPECIFIED = 2545;

    public static final int VESTING_SCHEDULE_HAS_NOT_BEEN_SET_UP = 2546;

    public static final int VESTING_MISSING_EMPLOYEE_DATA = 2547;

    public static final int VESTING_MORE_RECENT_DATA_USED_FOR_CALCULATION_CODE = 2560;

    public static final int VESTING_GENERAL_NON_CRITICAL_MESSAGE = 2524;

    public static final int VESTING_GENERAL_CRITICAL_MESSAGE = 2525;

    public static final int VESTING_ROBUST_DATE_CHANGED_AFTER_VESTING_CALLED = 2438;

    // Initial Message codes
    public static final int PARTICIPANT_HAS_ZERO_ACCOUNT_BALANCE = 2409;

    public static final int PARTICIPANT_HAS_I_LOANS = 2410;

    public static final int PARTICIPANT_HAS_ONLINE_LOANS = 1262;

    public static final int PARTICIPANT_HAS_ANOTHER_WITHDRAWAL = 2411;

    public static final int REQUEST_CAN_NOT_BE_PROCESSED = 2412;

    public static final int MISSING_VESTING_SCHEDULE = 2550;

    public static final int UNSPECIFIED_VESTING_CREDITING_METHOD = 2549;

    public static final int INVALID_PARTICIPANT_ADDRESS_COUNTRY = 2424;

    public static final int INVALID_TRUSTEE_ADDRESS_COUNTRY = 2418;

    public static final int IRA_PROVIDER_MISSING = 2423;

    public static final int WITHDRAWAL_LOCKED = 2385;

    public static final int VESTING_SERVICE_CALLED_INDICATOR_NO = 2561;

    public static final int NOTE_TO_PARTICIPANT_INVALID_POW = 2393;

    public static final int AT_RISK_REQUEST_DECLARATION_ERROR = 8095;

    public static final int PLAN_NAME_NOT_ENTERED = 9001;

    public static final int INVALID_PLAN_NAME = 9002;

    public static final int EMPLOYER_TAX_IDENTIFICATION_NUMBER_NOT_ENTERED = 9003;

    public static final int PLAN_NUMBER_NOT_ENTERED = 9005;

    public static final int PLAN_EFFECTIVE_DATE_NOT_ENTERED = 9011;

    public static final int INVALID_EMPLOYER_TAX_IDENTIFICATION_NUMBER = 9004;

    public static final int NO_EMPLOYER_TAX_IDENTIFICATION_NUMBER_WITH_PLAN_NUMBER = 9008;

    public static final int INVALID_PLAN_NUMBER = 9006;

    public static final int NO_PLAN_NUMBER_WITH_EMPLOYER_TAX_IDENTIFICATION_NUMBER = 9007;

    public static final int EMPLOYER_TAX_IDENTIFICATION_NUMBER_PLAN_NUMBER_NOT_UNIQUE = 9009;

    public static final int INVALID_PLAN_EFFECTIVE_DATE = 9010;

    public static final int PLAN_EFFECTIVE_DATE_AFTER_CONTRACT_EFFECTIVE_DATE = 9012;

    public static final int NO_PLAN_SERIAL_NUMBER_WHEN_PROTOTYPE_ADOPTION_YES = 9014;

    public static final int MISSING_ENTITY_TYPE_OTHER_DESCRIPTION = 9015;

    public static final int INVALID_ENTITY_TYPE_OTHER_DESCRIPTION = 9016;

    public static final int INVALID_FIRST_PLAN_ENTRY_DATE = 9025;

    public static final int EXISTING_PLAN_ENTRY_DATE_REMOVED = 9026;

    public static final int EXISTING_PLAN_ENTRY_FREQUENCY_REMOVE = 9027;

    public static final int INVALID_MONEY_TYPE_FOR_HARDSHIP = 9021;

    public static final int INVALID_NORMAL_RETIREMENT_AGE = 9017;

    public static final int INVALID_EARLY_RETIREMENT_AGE = 9019;

    public static final int INVALID_MINIMUM_HARDSHIP_AMOUNT = 9022;

    public static final int INVALID_MAXIMUM_HARDSHIP_AMOUNT = 9023;
    
    public static final int EEDEF_EARNINGS_FLAG_CHANGES_WARNING = 3210;

    public static final int MINIMUM_HARDSHIP_AMOUNT_GREATER_THAN_MAXIMUM_AMOUNT = 9024;

    public static final int MAX_OUTSTANDING_LOANS_NOT_ENTERED = 9037;

    public static final int INVALID_MINIMUM_LOAN_AMOUNT = 9030;

    public static final int MINIMUM_LOAN_AMOUNT_LESS_THAN_MINIMUM_BOUND = 9031;

    public static final int INVALID_MAXIMUM_LOAN_AMOUNT = 9032;

    public static final int MAXIMUM_LOAN_AMOUNT_GREATER_THAN_MAXIMUM_BOUND = 9033;

    public static final int MINIMUM_LOAN_AMOUNT_GREATER_THAN_MAXIMUM_AMOUNT = 9034;

    public static final int INVALID_MAXIMUM_LOAN_PERCENT = 9035;

    public static final int MAXIMUM_LOAN_PERCENT_GREATER_THAN_MAXIMUM = 9036;

    public static final int INVALID_LOAN_INTEREST_RATE_ABOVE_PRIME = 9038;

    public static final int LOAN_INTEREST_RATE_ABOVE_PRIME_NOT_IN_RANGE = 9039;

    public static final int GENERAL_PURPOSE_MAXIMUM_AMORTIZATION_NOT_IN_RANGE = 2601;

    public static final int HARDSHIP_MAXIMUM_AMORTIZATION_NOT_IN_RANGE = 2602;

    public static final int PRIMARY_RESIDENCE_MAXIMUM_AMORTIZATION_NOT_IN_RANGE = 2603;

    public static final int NO_HOURS_OF_SERVICE_WITH_HOURS_OF_SERVICE_CREDIT_METHOD = 9042;

    public static final int INVALID_HOURS_OF_SERVICE = 9040;

    public static final int HOURS_OF_SERVICE_NOT_IN_RANGE = 9041;

    public static final int VESTING_PERCENTAGE_NOT_ENTERED = 9043;

    public static final int INVALID_VESTING_PERCENTAGE = 9044;

    public static final int VESTING_PERCENTAGE_NOT_IN_RANGE = 9045;

    public static final int VESTING_PERCENT_LESS_THAN_PREVIOUS_YEAR = 9046;

    public static final int VESTING_PERCENT_CAN_NOT_BE_HUNDRAD_PERCENT_FOR_YEAR_ZERO = 2894;

    public static final int NO_VESTING_INFORMATION_WHEN_CSF_CALCULATE_VESTING_YES = 2550;

    public static final int PLAN_LOCK_FAILURE_BECAUSE_ALREADY_LOCKED = 9055;

    public static final int PLAN_YEAR_END_REQUIRED_TO_SELECT_HOS = 2731;

    public static final int EC_MIN_AGE_NOT_BETWEEEN_EC_EE_AGE_LOWER_LIMIT_AND_UPPER_LIMIT = 2737;

    public static final int EC_MIN_AGE_NOT_BETWEEEN_EC_ER_AGE_LOWER_LIMIT_AND_UPPER_LIMIT = 2868;

    public static final int EC_HOS_GREATER_THAN_EE_HOS_UPPER_LIMIT = 2742;

    public static final int EC_HOS_GREATER_THAN_ER_HOS_UPPER_LIMIT = 2869;

    public static final int EC_POS_GREATER_THAN_EE_POS_UPPER_LIMIT = 2748;

    public static final int EC_POS_GREATER_THAN_ER_POS_UPPER_LIMIT = 2870;

    public static final int EC_POS_NOT_EQUAL_TO_MONTH_OR_DAYS = 3018;

    public static final int EC_PLAN_ENTRY_FREQUENCY_NOT_SELECTED = 2750;

    public static final int EC_PLAN_EFFECTIVE_DATE_NOT_ENTERED = 2730;

    public static final int EC_ELIG_REQ_CANT_BE_REMOVED_IF_EC_CSF_ON = 2754;

    public static final int EC_MULTIPLE_ELIG_RULES_FOR_SINGLE_MT_NOT_EQUAL_NO = 3017;

    public static final int EC_ELIG_COMP_PERIOD_AFTER_INITIAL_PERIOD_NOT_VALID = 3019;

    public static final int EC_EMPLOYEE_PLAN_ENTRY_DATE_BASIS_CODE_NOT_VALID_FOR_EC_SERVICE = 2888;

    public static final int EC_EMPLOYEE_PLAN_ENTRY_DATE_BASIS_CODE_NOT_VALID_FOR_EZSTART = 2889;

    public static final int EC_EMPLOYEE_PLAN_ENTRY_BASIS_CODE_SHOULD_BE_CN_FOR_EC_SERVICE = 2890;

    public static final int EC_EMPLOYEE_PLAN_ENTRY_BASIS_CODE_SHOULD_BE_CN_FOR_EZSTART = 2891;

    // Search TPA page Error Codes

    public static final int TPA_USERNAME_GREATER_THAN_ONE_CHARACTER = 9246;

    public static final int TPA_USERNAME_VIOLATE_NAMING_STANDARD = 9248;

    public static final int TPA_FIRMNAME_GREATER_THAN_ONE_CHARACTER = 9247;

    public static final int TPA_FIRMID_NON_NUMERIC = 2414;

    // TPA - BOB related.
    public static final int CONTRACT_LESS_THAN_THREE_DIGITS = 9226;

    // Employee Status History Errors
    public static final int ERROR_DELETE_ALL_RECORDS = 64039;

    public static final int ERROR_EFF_DATE_LESS_DOB = 56162;

    public static final int ERROR_EFF_DATE_LESS_HIRE_DATE = 56163;

    public static final int SELECT_EEDEF_MONEY_TYPE = 2720;

    public static final int CONTRACT_MUST_HAVE_EEDEF_MONEY_TYPE = 2901;

    public static final int CANNOT_TURN_OFF_EEDEF_MONEY_TYPE = 2900;

    public static final int SPECIFY_PLAN_ELIGIBILITY_REQUIREMENTS = 2722;

    public static final int HOURS_OF_SERVICE_SHOULD_BE_WITHIN_UPPER_LIMIT = 2723;

    public static final int PERIOD_OF_SERVICE_SHOULD_BE_WITHIN_UPPER_LIMIT = 2724;

    public static final int PERIOD_OF_SERVICE_SHOULD_BE_MONTHS_OR_DAYS = 3022;

    public static final int INVALID_PLAN_ENTRY_FREQUENCY = 2725;

    public static final int INVALID_MINIMUM_AGE = 2749;

    public static final int MULTIPLE_ELIGIBILITY_RULE_SHOULD_BE_NO = 3020;

    public static final int INVALID_ELIGIBILITY_COMP_PERIOD = 3021;

    public static final int PLAN_EFFECTIVE_DATE_IS_REQUIRED = 2721;

    public static final int CONTRACT_MUST_HAVE_EEDEF_MONEY_TYPE_FOR_EZSTART = 2727;

    // added for Onboarding
    public static final int FIRST_PLAN_ENTRY_DATE_REQUIRED = 9028;

    public static final int PERIOD_OF_SERVICE_REQUIRED_FOR_ELAPSED_TIME_SERVICE_CREDITING_METHOD = 2732;

    public static final int HOURS_OF_SERVICE_REQUIRED_FOR_HOURS_OF_SERVICE_CREDITING_METHOD = 2733;

    public static final int PERIOD_OF_SERVICE_REQUIRED_FOR_HOURS_OF_SERVICE_CREDITING_METHOD = 2734;

    public static final int BASIS_FOR_PERIOD_OF_SERVICE_REQUIRED = 2747;

    public static final int INVALID_PLAN_FREQUENCY_FOR_EZSTART = 2751;

    public static final int PLAN_ENTRY_FREQUENCY_REQUIRED_FOR_ELIGIBILITY_REQUIREMENT = 2752;

    public static final int FIRST_PLAN_ENTRY_DATE_OR_EEDEF_PLAN_ENTRY_FREQUENCY_CHANGED_WARNING = 2753;

    public static final int CANNOT_TURN_AUTO_ENROLLMENT_OFFERED_OFF_WHEN_AE_CSF_IS_ON = 2757;

    public static final int AUTO_ENROLLMENT_MUST_BE_ON_WHEN_PLAN_INCLUDES_QACA = 2756;

    public static final int AUTO_ENROLLMENT_MUST_NOT_BE_ON_WITH_MISSING_ELIGIBILITY_REQUIREMENTS_FOR_EEDEF = 2758;

    public static final int AUTO_ENROLLMENT_DEFERRAL_PERCENTAGE_REQUIRED = 2765;

    public static final int AUTO_ENROLLMENT_DEFERRAL_PERCENTAGE_LESS_THAN_CONTRIBUTION_DEFERRAL_MIN_PERCENT = 2769;

    public static final int AUTO_ENROLLMENT_DEFERRAL_PERCENTAGE_GREATER_THAN_CONTRIBUTION_DEFERRAL_MAX_PERCENT = 2770;

    public static final int INVALID_VESTNG_COMPUTATION_PERIOD_FOR_HOURS_OF_SERVICE = 2812;

    public static final int INVALID_VESTNG_COMPUTATION_PERIOD_FOR_ELAPSED_TIME = 2813;

    public static final int INVALID_MULTIPLE_VESTING_SCHEDULES_FOR_ONE_SINGLE_MONEY_TYPE = 2814;

    public static final int AUTOMATIC_ENROLLMENT_EFFECTIVE_DATE_REQUIRED = 2759;

    public static final int AUTOMATIC_ENROLLMENT_EFFECTIVE_DATE_NOT_BEGINNING_OF_PLAN_YEAR_WARNING = 2762;

    public static final int AUTOMATIC_ENROLLMENT_EFFECTIVE_DATE_AFTER_INITIAL_ENROLLMENT_DATE = 2763;

    public static final int AUTOMATIC_ENROLLMENT_AUTOMATIC_WITHDRAWALS_REQUIRED = 2764;

    public static final int QDIA_RESTRICTION_DETAILS_REQUIRED = 2818;

    public static final int ACI_EFFECTIVE_DATE_REQUIRED = 2776;

    public static final int ACI_EFFECTIVE_DATE_PLAN_EFFECTIVE_DATE_WARNING = 2778;

    public static final int ACI_EFFECTIVE_DATE_BEFORE_CSF_ANNIVERSARY_DATE = 2779;

    public static final int ACI_CSF_ON_USING_AUTO_SIGNUP = 2786;

    public static final int ACI_APPLIES_TO_REQUIRED = 2780;

    public static final int ACI_APPLY_DATE_REQUIRED = 2789;

    public static final int ACI_APPLY_DATE_NOT_ALLOWED = 2853;

    public static final int ACI_APPLIES_TO_EFFECTIVE_DATE_REQUIRED = 2781;

    public static final int ACI_APPLIES_TO_EFFECTIVE_DATE_BEFORE_ACI_EFFECTIVE_DATE = 2788;

    public static final int ACI_DEFUALT_INCREASE_GREATER_THAN_DEFERRAL_MAX = 2791;

    public static final int ACI_DEFUALT_INCREASE_GREATER_THAN_DEFAULT_MAX = 2792;

    public static final int ACI_DEFUALT_MAXIMUM_GREATER_THAN_DEFFERRAL_MAX = 2794;

    public static final int ACI_DEFUALT_MAXIMUM_LESS_THAN_DEFFERRAL_MIN = 2795;

    public static final int ACI_PLAN_EMPLOYEE_DEFERRAL_SELECTED_DAY_EMPTY = 2771;

    public static final int ACI_PLAN_EMPLOYEE_DEFERRAL_SELECTED_MONTHS_EMPTY = 2772;

    public static final int ACI_DEFERRAL_LIMIT_MAX_MIN_ERROR = 2774;

    public static final int ACI_DEFERRAL_LIMIT_MAX_EMPTY_ERROR = 1329;

    public static final int ACI_DEFERRAL_ANNUAL_LIMIT_EMPTY = 2783;

    public static final int ACI_DEFERRAL_ANNUAL_LIMIT_GREATER_THAN_IRS_LIMIT = 2784;

    public static final int ACI_FIRST_OF_FIELD_REQUIRES_TYPE = 2817;

    public static final int ACI_NEXT_OF_FIELD_REQUIRES_TYPE = 2803;

    public static final int ACI_MAX_MATCH_FIELD_REQUIRES_TYPE = 2807;

    public static final int ACI_NEXT_PERCENT_REQUIRED = 2802;

    public static final int ACI_MONEY_TYPE_SELECTED_BUT_NOT_RULE_TYPE = 2862;

    public static final int ACI_RULE_FIRST_PERCENT_OR_NON_MATCHING_PERCENT_REQUIRED = 2810;

    public static final int ACI_RULE_MATCH_AND_NON_MATCH_ARE_BOTH_SPECIFIED = 2855;

    public static final int ACI_RULE_PERCENTS_OR_DOLLAR_RADIO_BUTTONS_DONT_MATCH = 2866;

    public static final int ACI_RULE_NO_MONEY_TYPES = 2867;

    public static final int ACI_RULE_FIRST_PECENT_REQUIRED_WHEN_FIRST_LIMIT_ENTERED = 2798;

    public static final int PLAN_ALLOWS_LOANS_IS_NO_FOR_LRK01 = 2854;

    public static final int ACI_MUST_BE_ON_IF_QACA_AND_MAX_DEFERRAL_LIMITS_TOO_LOW = 2755;

    public static final int ACI_PPTS_CAN_CHANGE_SALARY_DEFERRAL_ELECTIONS_REQUIRED = 2861;

    public static final int ACI_DEFUALT_INCREASE_REQUIRED = 2858;

    public static final int ACI_DEFUALT_MAXIMUM_REQUIRED = 2859;

    public static final int PLAN_NAME_REQUIRED_FOR_CSF_SUMMARY_PLAN_HIGHLIGHTS = 2785;

    public static final int QDIA_RESTRICTION_DETAILS_EXCEEDED_MAX_ALLOWED = 2863;

    public static final int AUTO_ENROLLMENT_DEFERRAL_PERCENTAGE_CHANGED_WHEN_AE_CSF_IS_ON = 2915;

    public static final int ACI_DEFAULT_MAXIMUM_CHANGED_IMPACT_FUTURE_SERVICES_SETUP = 2920;

    public static final int ACI_DEFAULT_INCREASE_CHANGED_IMPACT_INCREASE_NOT_CUSTOMIZED_EMPLOYEES = 2919;

    public static final int ACI_ANNUAL_APPLY_DATE_CHANGED_IMPACT_ANNIV_DATES = 2918;

    public static final int ACI_EFFECTIVE_DATE_CHANGED_IMPACT_AFTER_ENROLLED_EMPLOYEES = 2917;

    public static final int ACI_APPLIES_TO_CHANGED_IMPACT_ALL_EMPLOYEES = 2916;

    public static final int ACI_ANNUAL_APPLY_DATE_INVALID_DUE_TO_CONTRACT_FREEZE_PERIOD = 1337;

    public static final int PPT_SALARY_DEFERRAL_ELECTION_IS_UNSPECIFIED_WHEN_PPT_ALLOW_CHANGE_DEFERRALS_ONLINE_ON = 2395;

    public static final int PPT_SALARY_DEFERRAL_ELECTION_IS_UNSPECIFIED_WHEN_PLAN_ACI_ALLOWED_ON = 2396;

    public static final int ACI_APPLIES_TO_AUTO_ENROLLED_PPTS_WHEN_ACI_CSF_IS_ON = 2397;

    public static final int AUTO_ENROLLMENT_MUST_NOT_BE_ON_WITH_EEDEF_FUTURE_END_DATE = 2914;

    public static final int MIN_LOAN_IS_EMPTY = 2895;

    // Contact management - PS contact tab
    public static final int CONTACT_COMMENT_LOCKED = 1115;

    public static final int TPA_FIRM_COMMENT_LOCKED = 1130;

    // Edit contract address page
    public static final int LEGAL_ADDRESS_MISSING = 1121;

    public static final int ADDRESS_INCOMPLETE = 1122;

    public static final int ZIP_NOT_VALID_FOR_THIS_STATE = 1123;

    public static final int LEGAL_ADDRESS_MUST_BE_COMPLETED_TO_USE_COPY_ADDRESSES = 1124;

    public static final int ZIP_CODE_NOT_NUMERIC = 1125;

	public static final int POBOX_NOT_ALLOWED_IN_COURIER = 1126;

    public static final int LEGAL_STATE_NY_CANNOT_BE_FOR_USA = 1127;

    public static final int LEGAL_STATE_MUST_BE_NY_FOR_NY = 1128;

    public static final int LEGAL_ADDRESS_REMOVED = 1129;

    public static final int InvalidAsciiCharacter = 7000;

    // added for First Client Contact CSF
    public static final int OTHER_THIRD_PARTY_AND_OR_TYPE_NOT_SELECTED = 1413;

    // CR -19 EC project
    public static final int MONEY_TYPE_HAS_FUTURE_DELETION_DATE = 2921;

    public static final int EEDEF_MONEY_TYPE_HAS_FUTURE_DELETION_DATE = 2922;

    public static final int ERROR_AE_NOT_ALLOWED_IN_PLAN = 2828;

    public static final int ERROR_AE_NOT_ALLOWED_FOR_PLAN_WITH_CONFLICTING_PLAN_ENTRY_FREQUENCY = 2829;

    public static final int ERROR_INITIAL_ENROLLMENT_DATE_DOES_NOT_MATCH_PLAN = 2838;

    public static final int ERROR_ACI_AUTO_METHOD_NOT_ALLOWED_WHEN_PLAN_DOES_NOT_HAVE_ACI_ALLOWED = 2840;

    public static final int ERROR_ACI_ANNIVERSARY_DATE_BEFORE_PLAN_ACI_EFFECTIVE_DATE = 2847;

    public static final int ERROR_CALCULATED_NOT_ALLOWED_WHEN_PLAN_HAS_MULTIPLE_VESTING_SCHEDULES = 2821;

    public static final int ERROR_CALCULATED_NOT_ALLOWED_WHEN_PLAN_HAS_HOURS_OF_SERVICE_AND_ANNIVERSARY = 2822;

    public static final int ERROR_CALCULATED_NOT_ALLOWED_WHEN_PLAN_HAS_ELAPSED_TIME_AND_PLAN_YEAR = 2823;

    public static final int ERROR_CALCULATED_NOT_ALLOWED_WHEN_PLAN_SCM_UNSPECIFIED = 2907;

    public static final int ERROR_ACI_NOT_ALLOWED_WHEN_PLAN_APPLIES_IS_AU = 2933;

    public static final int ERROR_INVALID_ENROLLMENT_DATE_FORMAT = 2928;

    public static final int ERROR_ENROLLMENT_DATE_LESS_THAN_CURRENT_DATE = 2929;

    public static final int ERROR_APOLLO_UNAVAILABLE_FOR_AE = 2710;

    public static final int ERROR_INVALID_ENROLLMENT_DATE_VALUE = 5004;

    public static final int ERROR_INVALID_DEFERRAL_TYPE_FOR_AE = 1322;

    public static final int ERROR_PIL_NOT_ALLOWED_FOR_PAYEE = 2938;

    public static final int ERROR_INVALID_DATE_TO_ANNIVERSARY = 1317;

    public static final int ERROR_INVALID_PAYROLL_CUTOFF = 5008;

    public static final int ERROR_DEFERRAL_MIN_PER_GREATER_THAN_MAX_PERCENTAGE = 1320;

    public static final int ERROR_INVALID_DEFERRAL_MAX_PERCENTAGE = 1319;

    public static final int ERROR_INVALID_DEFERRAL_PERCENTAGE = 1318;

    public static final int ERROR_DEFERRAL_MIN_AMOUNT_GREATER_THAN_MAX_AMOUNT = 1330;

    public static final int ERROR_INVALID_DEFERRAL_LIMIT_AMOUNT = 2953;

    public static final int ERROR_INVALID_DEFERRAL_INCREASE_AMOUNT = 2906;

    public static final int ERROR_DEFERRAL_MAX_AMOUNT_CAN_NOT_BE_BLANK = 2913;

    public static final int ERROR_DEFERRAL_AMOUNT_CAN_NOT_BE_BLANK = 2912;

    public static final int ERROR_DEFERRAL_MAX_PERCENTAGE_CAN_NOT_BE_BLANK = 2905;

    public static final int ERROR_DEFERRAL_PERCENTAGE_CAN_NOT_BE_BLANK = 2904;

    public static final int ERROR_CHANGE_DEFERRALS_MUST_BE_YES_WHEN_AUTO = 1086;

    public static final int ERROR_ONLINE_LOANS_NOT_SUPPORTED = 2605;

    public static final int ERROR_CALCULATED_NOT_ALLOWED_WHEN_PLAN_TWO_OR_MORE_VESTINGS_UNSPECIFIED = 2909;

    public static final int ERROR_CALCULATED_NOT_ALLOWED_WHEN_VESTING_MISSED_FOR_ANY_ER_TYPES = 2910;

    public static final int ERROR_CALCULATED_NOT_ALLOWED_WHEN_PLAN_VESTING_UNSPECIFIED = 2908;

    public static final int ERROR_CHANGE_DEFERRALS_ONLINE_CAN_NOT_BE_YES = 2903;

    public static final int ERROR_ACI_ADT_CAN_NOT_BE_IN_PAST = 2875;

    public static final int ERROR_DM_MISSING_ADDRESS = 9060;

    public static final int ERROR_DM_APOLLO_UNAVAILABLE = 9061;

    public static final int ERROR_DM_UNSUPPORTIVE_DIO = 9062;

    public static final int WARNING_WITHDRAWAL_REVIEW_STAGE_PERMISSION_FOR_TPA = 2911;

    public static final int WARNING_WITHDRAWALS_WILL_NO_LOANGER_HAVE_REVIEW_STAGE = 2540;

    public static final int WARNING_WITHDRAWALS_WILL_NO_LOANGER_BE_ALLOWED = 2541;

    public static final int WARNING_WITHDRAWAL_REVIEW_STAGE_PERMISSION_FOR_PS = 1265;

    public static final int WARNING_IWITHDRAWAL_PERMINSSION_TO_NO = 1258;

    public static final int WARNING_REVIEW_LOAN_PERMISSION_FOR_TPA = 1016;

    public static final int WARNING_OUTSTANDING_iLOAN_COUNT = 1018;

    public static final int WARNING_LOANS_ARE_NO_LONGER_SUPPORTED = 1015;

    public static final int WARNING_FOR_TURNING_ON_LOANS = 1017;

    public static final int WARNING_FOR_AE_TURNING_OFF = 2830;

    public static final int WARNING_PS_RESPONSIBLE_FOR_OPT_OUT_DAYS = 2711;

    public static final int WARNING_FOR_TURNING_OFF_DM = 1107;

    public static final int WARNING_PARTICIPANT_WILL_NO_LONGER_BE_ABLE_TO_INITIATE_LOANS = 1014;

    public static final int WARNING_PAYROLL_CUTOFF_IMPACT_CODE = 2398;

    public static final int WARNING_FOR_EC_SERVICE_OFF = 2902;

    public static final int WARNING_FOR_EC_SERVICE_ON = 2726;

    public static final int WARNING_ACI_MAY_ALREADY_HAVE_HAPPENED = 2842;

    public static final int ERROR_CSF_ACI_DATE_IS_LESSTHAN_PLAN_ACI = 1360;

    public static final int WARNING_ACI_YEAR_VALUE = 1306;

    public static final int ERROR_CSF_LOCKED_BY_JH_REPRESENTATIVE = 5001;

    // Plansponsor Reset Password error codes.
    public static final int ERROR_RESET_PWD_DOB = 1433;
    public static final int ERROR_RESET_PWD_EMAIL = 1434;
    public static final int ERROR_RESET_PIN_SUSPEND = 2874;
    public static final int ERROR_RESET_PIN_MAXIMUM_EMAIL = 2952;
    public static final int ERROR_RESET_PWD_INVALID_EMPLOYMENT_STATUS = 1430;
    public static final int ERROR_RESET_PIN_INCOMPLETE_PARTICIPANT_SETUP = 1410;
    public static final int ERROR_PSW_RESET_PWD_ACCESS_STATUS = 1431;// PAR
    public static final int ERROR_PSW_RESET_PWD_MAX_REQUESTS = 1432;// PAR
    public static final int ERROR_PSW_RESET_PWD_WEB_REGIS_STATUS = 1435;// PAR

    // OB3 Dec Release CR 21 changes
    public static final int ERROR_PLAN_HIGHLIGHTS_ELIGIBILITY_MONEY_TYPE_RULE = 1271;
    public static final int ERROR_PLAN_HIGHLIGHTS_ELIGIBILITY_REQ_NOT_SPECIFIED_RULE = 1272;
    public static final int ERROR_PLAN_HIGHLIGHTS_MUST_ROLLOVERS_BE_DELAYED_RULE = 1273;
    public static final int ERROR_PLAN_HIGHLIGHTS_AE_RULE = 1274;
    public static final int ERROR_PLAN_HIGHLIGHTS_EMPLOYEE_DEFERRAL_ELECTION_RULE = 1275;
    public static final int ERROR_PLAN_HIGHLIGHTS_CATCHUP_CONTRIBUTION_RULE = 1276;
    public static final int ERROR_PLAN_HIGHLIGHTS_DEFFERAL_MAX_PCT_RULE = 1277;
    public static final int ERROR_PLAN_HIGHLIGHTS_DEFFERAL_MAX_AMT_RULE = 1278;
    public static final int ERROR_PLAN_HIGHLIGHTS_ACI_RULE = 1279;
    public static final int ERROR_PLAN_HIGHLIGHTS_VESTING_SCHEDULE_RULE = 1280;
    public static final int ERROR_PLAN_HIGHLIGHTS_NO_VESTING_SCHEDULE_RULE = 1281;
    public static final int ERROR_PLAN_HIGHLIGHTS_SPOUSAL_CONSENT_RULE = 1282;
    public static final int ERROR_PLAN_HIGHLIGHTS_FORMS_OF_DISTRB_RULE = 1283;
    public static final int ERROR_PLAN_HIGHLIGHTS_LOAN_ALLOWED_UNSPECIFIED_RULE = 1284;
    public static final int ERROR_PLAN_HIGHLIGHTS_MIN_LOAN_AMT_RULE = 1285;
    public static final int ERROR_PLAN_HIGHLIGHTS_MAX_LOAN_AMT_RULE = 1286;
    public static final int ERROR_PLAN_HIGHLIGHTS_MAX_LOAN_PCT_RULE = 1287;
    public static final int ERROR_PLAN_HIGHLIGHTS_NUM_OUTSTANDING_LOANS_RULE = 1288;

    public static final int ERROR_DEFERRAL_MAX_PERCENT_IS_GREATER_THAN_CSF_DEFERRAL_INCREASE_PER = 1361;
    public static final int ERROR_DEFERRAL_MAX_PERCENT_IS_GREATER_THAN_CSF_DEFERRAL_INCREASE_LIMIT_PER = 1321;
    public static final int ERROR_DEFERRAL_MAX_AMOUNT_IS_GREATER_THAN_CSF_DEFERRAL_INCREASE_AMT = 1362;
    public static final int ERROR_DEFERRAL_MAX_AMOUNT_IS_GREATER_THAN_CSF_DEFERRAL_INCREASE_LIMIT_AMT = 1363;

    // WoW - Pending Withdrawal Summary page error codes
    public static final int ERROR_INVALID_DATES_RANGE = 1134;
    public static final int ERROR_INVALID_FROM_DATE_WITH_CONT_EFF_DATE = 1135;
    public static final int MESSAGE_NO_HISTORY_TRANSACTION_FOR_DATE_SELECTED = 1218;

    public static final int WARNING_IWITHDRAWALS_REVIEW_PERMISSION_COULD_NOT_BE_GRANTED = 2934;

    public static final int WARNING_LOAN_REVIEW_PERMISSION_COULD_NOT_BE_TAKEN = 2935;

    public static final int WARNING_IWITHDRAWALS_PERMISSION_COULD_NOT_BE_TAKEN = 2936;

    public static final int WARNING_LOAN_REVIEW_PERMISSION_COULD_NOT_BE_GRANTED = 2937;

    public static final int ERROR_TPA_PROFILE_LOCKED = 1057;

    public static final int ERROR_ACI_YEAR_CAN_NOT_BE_MORETHAN_2_YEARS = 8088;

    // Participant Communications tab in contract service features
    public static final int WARNING_PER_DESIGNATED_PSW_USER_DOES_NOT_EXIST = 2951;

    // Plansponsor Reset PIN error codes.
    public static final int ERROR_RESET_PIN_DOB = 2872;

    public static final int ERROR_RESET_PIN_EMAIL = 2873;

    public static final int PLAN_DEFERRAL_PCT_LESS_THAN_CSF_DEFERRAL_PCT = 3023;

    public static final int PLAN_DEFERRAL_AMT_LESS_THAN_CSF_DEFERRAL_AMT = 3025;

    public static final int DEFERRAL_ANNUAL_INCREASE_HAS_DECIMAL_PLACE = 2896;

    public static final int DEFAULT_MAX_PCT_HAS_DECIMAL_PLACE = 2897;

    // OBDS PHASE 2 Beneficiary Internal Transfer
    public static final int ERROR_BENEFICIARY_TRANSFER_FROM_CONTRACT_BLANK = 1298;

    public static final int ERROR_BENEFICIARY_TRANSFER_FROM_CONTRACT_INVALID = 1297;

    // Fund Draft
    public static final int TOTAL_WEIGHTING_PERCENT_SHOULD_BE_100 = 1289;

    public static final int DUPLICATE_CRITERIA_SELECTED = 1290;

    public static final int NON_NUMERIC_VALUE = 1291;

    public static final int MUST_ENTER_WHOLE_NUMBER = 1292;

    public static final int WEIGHTING_NOT_SELETED = 1293;

    public static final int CRITERIA_NOT_SELECTED = 1294;

    public static final int NO_ACCESS_ERROR = 1295;

    // 408b2 GIFL Report
    public static final int GIFL_VERSION_NOT_SELECTED = 1522;

    public static final int GIFL_CLASS_NOT_SELECTED = 1570;

    // PIF related Error codes
    public static final int CONTRACT_NUMBER_INVALID_FORMAT = 2960;

    public static final int NO_MATCH_FOR_CONTRACT_INFORMATION = 1046;

    public static final int CONTRACT_NUMBER_INVALID_STATUS = 2961;

    public static final int DB_CONTRACT_NUMBER = 2981;

    public static final int DRAFT_IS_CURRENTLY_BEING_EDITED = 2962;

    public static final int CONTRACT_NUMBER_NAME_INVALID = 1583;

    // IPS Error code
    public static final int IPSM_TRUSTEE_LOCK = 1549;

    public static final int PPT_NO_PARTICIPANT = 1592;

    public static final int PPT_NO_STATEMENT = 1593;

    public static final int PPT_SSN_MANDATORY = 1590;
    
    // IPS Manger Participant notification overlay
    
    public static final int ERROR_ZIP_CODE = 1400;
    
    public static final int ERROR_MANDATORY = 1477;

    // LIA Error Codes
    public static final int ERROR_PARTICPANT_APPLICABLE_TO_LIA = 3032;

    // i:withdrawal Termination Date
    public static final int ERROR_TERMINATION_DATE_EXCEEDED = 3033;
    
    // i:withdrawal Retirement Date 
    public static final int ERROR_RETIREMENT_DATE_EXCEEDED = 3240;

    // IPI Hypothetical tool Error codes

    public static final int INVALID_NEW_BAND_ENDS = 1602;

    public static final int NEW_BAND_ENDS_ZERO = 1603;

    public static final int INVALID_LAST_BAND_ENDS = 1604;

    public static final int ONE_OR_MORE_INVALID_NEW_BAND_ENDS = 1605;

    public static final int INVALID_BAND_STARTS_AND_ENDS = 1606;

    public static final int INVALID_ASSET_CHARGE_RATE = 1607;

    public static final int EXTRA_ASSET_CHARGE_RATE_ENTRY = 1608;

    public static final int ONE_OR_MORE_INVALID_ASSET_CHARGE_RATE = 1609;

    public static final int INVALID_NEW_DI_CHARGE = 1610;

    public static final int BLANK_NEW_DI_CHARGE = 1611;

    public static final int ASSET_CHARGE_AND_OTHER_CHAGE_MUST_BE_SAME = 1612;

    public static final int INVALID_NEW_PRICING_REQUEST = 1613;

    public static final int ERROR_CALCLLATE_BAC = 1614;

    public static final int ERROR_CALCULATE_DI = 1615;

    public static final int CSV_FILE_CREATED_SUCCESSFULLY = 1617;

    public static final int PPT_CHANGE_NOTIFICATION_DOC_CREATED = 1618;

    public static final int CSV_FILE_NOT_CREATED = 1619;

    public static final int PPT_CHANGE_NOTIFICATION_DOC_NOT_CREATED = 1620;

    public static final int NONZERO_ANNUALIZED_TPA_ABF = 1621;

    // Notice Info 404a5 Errors
    public static final int MANDATORY_FIELDS_INVALID = 1392;

    public static final int PHONE_NUMBER_INVALID = 1393;

    public static final int PHONE_EXTENSION_INVALID = 1394;

    public static final int PHONE_NUMBER_INCOMPLETE_1 = 1395;

    public static final int PHONE_NUMBER_INCOMPLETE_2 = 1396;

    public static final int EMAIL_ADDRESS_INVALID = 1397;

    public static final int EMAIL_ADDRESS_DOMAIN_INVALID = 1398;

    public static final int NOTICE_INFO_CUSTOM_FEE_TYPE_AND_VALUE_INCOMPLETE = 1422;

    public static final int NOTICE_INFO_FEE_VALUE_MUST_BE_NUMERIC = 1423;

    public static final int NOTICE_INFO_CUSTOM_FEE_VALUE_INCOMPLETE = 1399;

    public static final int NOTICE_INFO_CUSTOM_FEE_TYPE_INCOMPLETE = 1421;

    public static final int NOTICE_INFO_NEGATIVE_FEE_VALUE = 1424;

    public static final int NOTICE_INFO_INVALID_FEE_AMOUNT_FORMAT = 1425;

    public static final int NOTICE_INFO_INVALID_FEE_PERCENTAGE_FORMAT = 1426;

    public static final int NOTICE_INFO_INVALID_FEE_VALUE_WITH_SPECIAL_NOTE = 9843;

    public static final int INVALID_FROM_DATE_FORMAT = 1381;

    public static final int INVALID_TO_DATE_FORMAT = 1382;

    public static final int FROM_DATE_GREATER_THAN_TO_DATE = 1383;
    
    public static final int ERROR_PBA_RESTRICTION_REQUIRED = 1428;
    
    public static final int INVALID_PBA_MIN_DEPOSIT = 1427;

    // TPA Standard Fee Schedule Error Codes
    public static final int EDIT_TPA_STANDARD_SCHEDULE_PAGE_WARNING_MESSAGE_THAT_CHANGES_WILL_BE_LOST = 2965;

    public static final int TPA_STANDARD_SCHEDULE_FEE_VALUE_MUST_BE_NUMERIC = 1387;

    public static final int TPA_STANDARD_SCHEDULE_CUSTOM_FEE_VALUE_INCOMPLETE = 1384;

    public static final int TPA_STANDARD_SCHEDULE_CUSTOM_FEE_TYPE_INCOMPLETE = 1385;

    public static final int TPA_STANDARD_SCHEDULE_FEE_TYPE_AND_VALUE_INCOMPLETE = 1386;

    public static final int TPA_STANDARD_SCHEDULE_NEGATIVE_FEE_VALUE = 1388;

    public static final int TPA_STANDARD_SCHEDULE_INVALID_FEE_AMOUNT_FORMAT = 1389;

    public static final int TPA_STANDARD_SCHEDULE_INVALID_FEE_PERCENTAGE_FORMAT = 1390;

    public static final int TPA_STANDARD_SCHEDULE_INVALID_FEE_VALUE_WITH_SPECIAL_NOTE = 9841;

    // TPA Customize Contract Fee Schedule Error Codes
    public static final int TPA_CUSTOM_SCHEDULE_FEE_VALUE_MUST_BE_NUMERIC = 1387;

    public static final int TPA_CUSTOM_SCHEDULE_CUSTOM_FEE_VALUE_INCOMPLETE = 1384;

    public static final int TPA_CUSTOM_SCHEDULE_CUSTOM_FEE_TYPE_INCOMPLETE = 1385;

    public static final int TPA_CUSTOM_SCHEDULE_FEE_TYPE_AND_VALUE_INCOMPLETE = 1386;

    public static final int TPA_CUSTOM_SCHEDULE_NEGATIVE_FEE_VALUE = 1388;

    public static final int TPA_CUSTOM_SCHEDULE_INVALID_FEE_AMOUNT_FORMAT = 1389;

    public static final int TPA_CUSTOM_SCHEDULE_INVALID_FEE_PERCENTAGE_FORMAT = 1390;

    public static final int TPA_CUSTOM_SCHEDULE_INVALID_FEE_VALUE_WITH_SPECIAL_NOTE = 9842;

    public static final int INVALID_CHARACTERS_DETECTED = 9291;

    public static final int CHANGE_HISTORY_TPA_STANDARD_SCHEDULE_PAGE_FROM_DATE_INVALID_FORMAT = 1375;

    public static final int CHANGE_HISTORY_TPA_STANDARD_SCHEDULE_PAGE_TO_DATE_INVALID_FORMAT = 1376;

    public static final int CHANGE_HISTORY_STANDARD_SCHEDULE_PAGE_FROM_DATE_GREATER_THAN_TO_DATE = 1377;

    public static final int CHANGE_HISTORY_TPA_CSTOMIZE_CONTRACT_PAGE_FROM_DATE_INVALID_FORMAT = 1378;

    public static final int CHANGE_HISTORY_TPA_CSTOMIZE_CONTRACT_PAGE_TO_DATE_INVALID_FORMAT = 1379;

    public static final int CHANGE_HISTORY_CUSTOMIZE_CONTRACT_PAGE_TO_DATE_GREATER_THAN_FROM_DATE = 1380;

    public static final int CONTRACT_UNDER_DIFFERENT_TPA_FIRM_FILTER_CONTRACT_NAME = 1370;

    public static final int INVALID_CONTRACT_NAME = 1371;

    public static final int NON_NUMERIC_CONTRACT_NUMBER = 1372;

    public static final int CONTRACT_UNDER_DIFFERENT_TPA_FIRM_FILTER_CONTRACT_NUMBER = 1373;

    public static final int INVALID_CONTRACT_NUMBER = 1374;

    public static final int FEE_SCHEDULE_EDIT_ERROR_MESSAGE = 9286;

    // TODO:Error codes to be updated after the CMA is keys are released.
    public final static int ERROR_CALCADHOCROR_END_DATE_LATER_THEN_TODAY = 9309;

    public final static int ERROR_CALCADHOCROR_START_DATE_LATER_THEN_END_DATE = 9834;

    public final static int ERROR_CALCADHOCROR_START_DATE_EQUAL_END_DATE_AND_TODAY = 9308;

    public final static int ERROR_CALCADHOCROR_TIME_PERIOD_FROM_TODAY_REQUIRED = 9830;

    public final static int ERROR_CALCADHOCROR_TIME_PERIOD_FROM_TODAY_INVALID = 9830;

    public final static int ERROR_CALCADHOCROR_START_DATE_INVALID = 9832;

    public final static int ERROR_CALCADHOCROR_START_DATE_INVALID_1 = 9833;

    public final static int ERROR_CALCADHOCROR_START_DATE_REQUIRED = 9831;

    public final static int ERROR_CALCADHOCROR_START_DATE_1950 = 9304;

    public final static int ERROR_CALCADHOCROR_END_DATE_INVALID = 9836;

    public final static int ERROR_CALCADHOCROR_END_DATE_INVALID_1 = 9837;

    public final static int ERROR_CALCADHOCROR_END_DATE_REQUIRED = 9835;

    public final static int ERROR_CALCADHOCROR_MF_ROR_FOR_ONE_DAY = 9300;

    public final static int ERROR_CALCADHOCROR_MF_ROR_FOR_ONE_DAY_WITH_CASH_FLOW_AT_END = 9303;

    public final static int ERROR_CALCADHOCROR_MF_CLOSING_BALANCE_THREE_DOLLARS = 9301;

    public final static int ERROR_CALCADHOCROR_MF_GENERAL_ERROR = 9302;

    public final static int ERROR_ACCOUNT_BALANCE_OUTSIDE_SERVICE_HOURS = 9305;

    public final static int ERROR_CALCADHOCROR_TIME_PERIOD_NOT_SELECTED = 9830;

    public final static int SYSTEM_UNAVAILABLE = 9302;
    
    //DPR Changes
    public static final int RM_NOT_ASSIGNED = 9310;

    // NMC_EDIT Error Code

    public static final int NMC_EDIT_FILE_NAME_EMPTY = 9331;
    public static final int NMC_EDIT_DOCUMENT_ALREADY_EXISTS = 9332;
    public static final int NMC_EDIT_FILE_NOT_EXISTS_IN_LOCATION = 9333;
    public static final int NMC_EDIT_NON_PRINTABLE_CHARACTERS = 9334;
    public static final int NMC_EDIT_FILE_TO_BE_PDF = 9335;
    public static final int NMC_EDIT_FILE_PASSWORD_PROTECTED = 9336;
    public static final int NMC_EDIT_REPLACE_FILE_NAME_EMPTY = 9337;
    public static final int NMC_EDIT_MAX_DOCUEMNT_LENGTH = 4003;

    // NMC_ADD ERROR CODES
    public static final int NMC_ADD_UPLOAD_FILE_EMPTY = 9330;
    public static final int NMC_ADD_DOCUMENT_NAME_EMPTY = 9331;
    public static final int NMC_ADD_NOTICE_ALREADY_EXISTS = 9332;
    public static final int NMC_ADD_FILE_NOT_EXISTS_IN_LOCATION = 9333;
    public static final int NMC_ADD_NON_PRINTABLE_CHARACTERS = 9334;
    public static final int NMC_ADD_FILE_TO_BE_PDF = 9335;
    public static final int NMC_ADD_FILE_PASSWORD_PROTECTED = 9336;

    // Notice Manager Upload And Share
    public static final int VIEW_BUTTON_ERROR_MESSAGE = 9362;
    public static final int JH_VIEW_BUTTON_ERROR_MESSAGE = 9369;
    public static final int EDIT_BUTTON_ERROR_MESSAGE = 9364;
    public static final int EDIT_BUTTON_ACTIVE_LOCK_ERROR_MESSAGE = 9365;
    public static final int DELETE_BUTTON_ERROR_MESSAGE = 9366;
    public static final int DELETE_BUTTON_ACTIVE_LOCK_ERROR_MESSAGE = 9367;
    public static final int VIEW_DOCUMENT_NAME_ERROR_MESSAGE = 9362;
    public static final int JH_DOCUMENT_NAME_ERROR_MESSAGE = 9369;

    // Notice Manager Alerts
    public static final int ALERT_NAME_EXISTS = 9298;
    public static final int INVALID_NOTICE_DUE_DATE = 9297;
    public static final int INVALID_NOTICE_DUE_DATE_FORMAT = 9295;
    public static final int NON_PRINTABLE_CHARACTERS = 9291;
    public static final int INVALID_ALERT_TIMING = 9344;
    public static final int ALERT_NAME_MANDATORY = 9293;
    public static final int ALERT_DATE_MANDATORY = 9294;
    public static final int ALERT_FREQUENCYCODE_MANDATORY = 9296;

    // NMC Build your package
    public static final int NMC_BUILD_CENSUS_UPLOAD_FILE_EMPTY = 9350;
    public static final int NMC_BUILD_CENSUS_UPLOAD_FILE_FORMAT = 9351;
    public static final int NMC_BUILD_CENSUS_MAILING_NAME_EMPTY = 9349;
    public static final int NMC_BUILD_CENSUS_DOCUMENTS_SELECTED_EMPTY = 9348;
    public static final int NMC_BUILD_SELECTED_CUSTOM_DOCUMENT_DELETED_DOWNLOAD = 9347;
    public static final int NMC_BUILD_SELECTED_JH_DOCUMENT_DELETED_ORDER = 9353;
    public static final int NMC_BUILD_PLEASE_SELECT_DOCUMENT_TO_DOWNLOAD = 9346;
    public static final int NMC_BUILD_NO_CONTACT_INFO_PRESENT = 9352;
    public static final int NMC_BUILD_CENSUS_UPLOAD_FILE_NOT_SELECTED = 9354;
    public static final int NMC_BUILD_TRANSFERRED_PACKAGE_IS_NOT_ACCEPTED_TO_MERILL_AND_GOT_ERROR_CODE= 9356;
    public static final int NMC_BUILD_TRANSFERRED_PACKAGE_IS_NOT_ACCEPTED_TO_MERILL_AND_GOT_NO_ERROR_CODE = 9370;
    public static final int NMC_BUILD__MERILL_SYSTEM_DOWN = 9371;

    public static final int USERNAME = 51421;

    // NMC Control Reports error codes
    public static final int NMC_CONTROL_REPORTS_FROM_DATE_INVALID = 9327;
    public static final int NMC_CONTROL_REPORTS_TO_DATE_INVALID = 9328;
    public static final int NMC_CONTROL_REPORTS_FROM_DATE_AFTER_TO_DATE = 9329;

    //Notices Plan data track   
    public static final int INDICATE_ATLEAST_ONE_MONEY_TYPE_FOR_VESTING_SCHEDULE = 3084;//Need to confirm Manju
    public static final int PENDING_PIF_COMPLETION = 3060;
    public static final int MISSING_VALUES = 3063;
    public static final int DATA_SAVED_SUCCESSFULLY = 9813;
    public static final int DATA_SAVE_FAILED = 2367;//Need to get new error code
    public static final int ROLEPLAY_TEMP_SESSION = 3160;
    public static final int NO_CHANGE_ON_SAVE = 3161;
    public static final int SH_ADDN_CONTR_YES = 3168;
    public static final int SH_MATCH_ANOTHER_PLAN_NAME = 3169;
    public static final int SH_NE_ANOTHER_PLAN_NAME = 3170;
    public static final int INCOMPLETE_VESTING_SCHEDULE = 3167;
    public static final int QDIA_NOTICE_SELECTED = 3192;
    public static final int AUTO_NOTICE_SELECTED = 3190;
    
    //Contributions and Distributions Tab2
    public static final int EMPLOYEE_DEFERRAL_MAXIMUM_NOT_VALID_VALUE = 3061;
    public static final int EMPLOYEE_DEFERRAL_MAXIMUM_NON_NUMERIC = 3062;
    public static final int PLAN_ALLOW_ROTH_CONTRIBUTIONS_NOT_SELECTED = 3063;
    public static final int EMPLOYEE_ROTH_DEFERRAL_MAXIMUM_NOT_VALID_VALUE = 3064;
    public static final int EMPLOYEE_ROTH_DEFERRAL_MAXIMUM_NON_NUMERIC = 3065;
    public static final int SUMMARY_PLAN_DESCRIPTION_IS_EMPTY = 3067;
    public static final int IN_SERVICE_WITHDRAWL_IS_EMPTY = 3068;
    public static final int HARDSHIP_WITHDRAWAL_TAKEN_NOT_VALID_VALUE = 3070;
    public static final int HARDSHIP_WITHDRAWAL_TAKEN_NON_NUMERIC = 3071;
    
    
    //Automatic contribution Tab 4
    public static final int AUTOMATIC_CONTRIBUTION_PROVISION_TYPE_NOT_SELECTED = 3085;
    public static final int EACA_CONTRIBUTION_DAYS_EMPTY = 3095;
    public static final int EACA_CONTRIBUTION_DAYS_NOT_VALID = 3096;
    public static final int QACA_MATCH_PCT1_AND_PCT2_NOT_SAME = 3103;
    public static final int QACA_CONTRIBUTION_DAYS_NOT_VALID = 3118;
    public static final int QACA_SH_MATCH_VESTING_EMPTY = 3109;
    public static final int QACA_CONTRIBUTION_DAYS_EMPTY = 3117;
    public static final int EACA_EMP_CONTRIB = 3172;
    public static final int QACA_EMP_CONTRIB = 3168;
    public static final int ANNUAL_INCREASE_UNSELECTED = 3173;
    public static final int ANNUAL_INCREASE = 3174;
    public static final int MAX_AUTO_INCREASE = 3174;
    public static final int QACA_MATCH_CONTRIB_ROW2 = 3175;
    public static final int QACA_MATCH_ANOTHER_PLAN_NAME = 3176;
    public static final int QACA_NE_ANOTHER_PLAN_NAME = 3177;
	   //TODO replace with new cma key
    public static final int QACA_ACI_NO = 3118;
    public static final int ACI_UNSPECIFIED = 3193;
    public static final int UNSPECIFIED = 3193;
    
    //Plan Sponsor Contact Information - Tab 6
    public static final int CONTACT_INFORMATION_PENDING_COMPLETION = 3123;
    
    //SEND Service
    public static final int ERROR_SEND_SERVICE_DESELECTION = 3125;
    public static final int ERROR_404A5_NOTICE_NOT_AVAILABLE = 3126;
    public static final int ERROR_DIO_MORE_THAN_ONE = 3127;
    public static final int ERROR_CONTACT_INFO_MISSING = 3128;
    public static final int ERROR_INV_INFO_DATA_INCOMPLETE = 3129;
    public static final int ERROR_INV_INFO_TAB_INCOMPLETE = 3130;
    public static final int WARNING_QDIA_NOT_SELECTED = 3131;
    public static final int ERROR_AUTO_CONTRIB_DATA_INCOMPLETE = 3132;
    public static final int ERROR_CONTRIB_DISTRI_DATA_INCOMPLETE = 3133;
    public static final int ERROR_SH_DATA_INCOMPLETE = 3134;
    public static final int ERROR_NOTICE_TYPE_NOT_SELECTED = 3135;
    public static final int ERROR_SUMMARY_DATA_MISSING = 3136;
    public static final int ERROR_DATA_ISSUE=3140;
    public static final int ERROR_TECHNICAL_ISSUE=3141;
    public static final int MAXIMUM_ALLOWED_RECORDS_EXCEEDED=3196;
    public static final int ERROR_DM_SELECTED=3137;
    public static final int WARNING_NOTICE_TYPE_CHANGE = 3139;
    
    public static final int ERROR_SEND_SERVICE_SELECTED = 3138;
    public static final int ERROR_SEND_SERVICE_DESGNATE= 3155;
    
    //Passcode Override
    public static final int ERROR_EXEMPTION_REASON_MANDATORY= 3561;
    public static final int ERROR_EXEMPTION_REQUESTED_BY_MANDATORY= 3562;
    public static final int ERROR_PPM_TICKET_NUMBER_MANDATORY= 3563;
    public static final int ERROR_ALREADY_EXEMPTED= 3564;
    public static final int ERROR_ALREADY_REMOVED_EXEMPTION= 3565;
    public static final int DENY = 8140;
    
    public static final int SMS_SWITCH_ON = 8140;
    public static final int VOICE_SWITCH_ON = 8140;
    
    public static final int ERROR_PHONE_COLLECTION_OERLAY = 3577;
    public static final int ERROR_PHONE_COLLECTION_EDIT_MY_PROFILE = 3576; 
    
    public static final int WARNING_PAYROLL_FEEDBACK_SERVICE_FEATURE_IMPACT		= 2999;
    public static final int WARNING_PAYROLL_FEEDBACK_360_SERVICE_FEATURE_IMPACT	= 2998;

    public static final int ERROR_PARTICIPANT_ONLINE_DEFERRAL_MGMT_MUST_BE_ON	= 3220; 
    public static final int ERROR_PARTICIPANT_ONLINE_ENROLLMENT_MUST_BE_ON		= 3221;
    
    
    //ManagedAccount
    public static final int WARNING_CODE_MANAGED_ACCOUNT_DATE_CHANGED = 3231;
    public static final int ERROR_CODE_MANAGED_ACCOUNT_DATE_LESS_THAN_DB_DATE = 3233;
    
    
    public static final int ERROR_PAYROLL_FREQUENCY_MUST_BE_SPECIFIED			= 1150;
    public static final int ERROR_MISSING_ROLLOVER_TYPE			= 3645;
    public static final int ERROR_ROLLOVER_TYPE_MUST_BE_ROTH_IRA			= 3646;
    public static final int PAY_DIRECT_TO_ME_OPTIONLA_SEC_USER_INPUT            = 3640;
	public static final int MISSING_MULTIPLE_DESTINATION_SELECTION               = 3641;
    public static final int SELECTED_ONE_PAYEE                                   = 3642;
    public static final int ROLLOVER_REMAINING_BALANCE_MANDATORY                 = 3643;
    
    public static final int HARDSHIP_INVALID_MONEY_TYPE = 3649;
    public static final int MAXIMUM_HARDSHIP_AMOUNT = 3648;
    public static final int MINIMUM_HARDSHIP_AMOUNT = 3647;
    public static final int REQUESTED_AMOUNT_WITHIN_THRESHOLD_FOR_HA = 9866;
    public static final int AVAILABLE_HARDSHIP_AMOUNT = 9867;
    // i:withdrawal Disability Date furture date
    public static final int DISABILITY_DATE_EXCEEDED = 3650;
}