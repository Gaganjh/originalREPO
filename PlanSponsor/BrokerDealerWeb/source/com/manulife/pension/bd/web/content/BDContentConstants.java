package com.manulife.pension.bd.web.content;

import com.manulife.pension.platform.web.content.CommonContentConstants;

/**
 * This interface contains constants related to BD contents.
 * 
 * @author
 * 
 */
public interface BDContentConstants extends CommonContentConstants {

    public static final int MESSAGE_NO_HISTORY_TRANSACTION_FOR_DATE_SELECTED = 51097;
    
    //Withdrawal Transaction details
    public static final int MESSAGE_MULTI_PAYEE_NOTIFICATION = 73870;
    
    public static final int MESSAGE_LOAN_DEFAULT_NOTIFICATION = 73871;
    
	public static final int CORRECTION_INDICATOR_MESSAGE=73872;
	
	public static final int MESSAGE_WITHDRAWAL_PARTICPANT_APPLICABLE_TO_LIA = 83285;
	
    // TRANSACTION Text
    public static final int MESSAGE_NO_TRANSACTION_FOR_DATE_SELECTED = 1300;
    
    public static final int MESSAGE_NO_TRANSACTION_FOR_DATE_SELECTED_CMA = 50982;

    public static final int MESSAGE_TOO_MANY_TRANSACTIONS_FOR_DATE_SELECTED = 1301;
    
    public static final int MESSAGE_TOO_MANY_TRANSACTIONS_FOR_DATE_SELECTED_CMA = 50983;

    // BD News
    public static final int BD_NEWS_GROUP = 111;

    // BD What's New
    public static final int BD_WHATS_NEW_GROUP = 123;

    // BD Marketing Commentary
    public static final int BD_MARKETING_COMMENTARY_GROUP = 124;

    
    public static final int NO_ARCHIVED_NEWS_ITEMS_ERROR_MESSAGE = 61993;

    // Firm Restriction
    public static final int ATLEAST_ONE_FIRM_ERROR_MESSAGE = 62874;

    public static final int INVALID_FIRM_ERROR_MESSAGE = 62875;
    
    //Firm Restriction for RIA user
    public static final int ATLEAST_ONE_FIRM_ERROR_MESSAGE_FOR_RIA = 90578;
    public static final int NO_PERMISSION_CHECK_WARNING= 90687;
    public static final int ASSOCIATED_RIA_FIRMS_TITLE=90680;
    public static final int RIA_EMAIL_EXISTS=90599;

    // PDF related
    public static final String PAGE_NAME = "name";

    public static final String INTRO1_TEXT = "introduction1";

    public static final String INTRO2_TEXT = "introduction2";

    public static final String SUB_HEADER = "subHeader";

    public static final String TEXT = "text";

    public static final String BODY1_HEADER = "body1Header";

    public static final String BODY2_HEADER = "body2Header";

    public static final String BODY3_HEADER = "body3Header";

    // Broker Listing Related.
    public static final int BROKER_LISTING_ENTER_SEARCH_CRITERIA_TO_DISPLAY_REPORT = 63631; 

    public static final int BROKER_LISTING_NO_PRODUCERS_FOR_FILTER_ENTERED = 66197;

    public static final int BROKER_LISTING_HISTORICAL_CONTRACT_INFORMATION = 65052;

    // Block Of Business Related.
    public static final int BOB_HISTORICAL_CONTRACT_INFO_FOOTNOTE = 62674;

	public static final int BOB_PN_PP_CONTRACT_CNT_ASOFLATESTDATE_FOOTNOTE = 69946;

    public static final int BOB_ASSETS_COLUMN_IN_DI_TAB_FOOTNOTE = 62673;
    
    public static final int CONTRACT_LESS_THAN_THREE_DIGITS = 51589; // Message

    public static final int BOB_ENTER_SEARCH_CRITERIA_TO_DISPLAY_REPORT = 63631;

    public static final int BOB_NO_RESULT_TO_DISPLAY_FOR_TAB = 63633;

    public static final int BOB_NO_CONTRACTS_FOR_FILTER_ENTERED = 63632;

    public static final int BOB_RESULT_TOO_BIG = 63630; // Message

    public static final int BOB_NO_RECORDS_MEET_CONDITIONS_ENTERED = 63634;

    public static final int BOB_FILTERS_USED_DO_NOT_APPLY = 63635;
    
    public static final int BOB_INVALID_FORMAT_ASSET_RANGE_FROM_TO = 64623; // Message

    public static final int BOB_INVALID_RANGE_FOR_ASSET_RANGE_FROM_TO = 64624; // Message

    public static final int BOB_MIN_ASSET_RANGE_GT_THAN_MAX_ASSET_RANGE = 64625; // Message

    // Fund to Fund Transfer + Rebalance Details pages related
    public static final int MESSAGE_MVA_APPLIED = 54979;

    public static final int MESSAGE_REDEMPTION_FEE_APPLED = 54978;

    public static final int MISCELLANEOUS_EXCEL_IMAGE_TEXT = 62001;

    public static final int MISCELLANEOUS_CSV_ALL_IMAGE_TEXT = 65088;

    public static final int MISCELLANEOUS_PDF_IMAGE_TEXT = 62000;

    // Participant Transaction History page related
    public static final int MESSAGE_NO_RESULTS_FOR_SEARCH_CRITERIA = 51641;

    // contract information page
    public static final int CONTRACT_INFO_PAGE_CONTACT_INFO_SECTION_TITLE = 62589;

    public static final int CONTRACT_INFO__PAGE_CONTRACT_OPTIONS_SECTION_TITLE = 62590;

    public static final int CONTRACT_INFO_PAGE_CONTRACT_ASSETS_SECTION_TITLE = 62591;

    public static final int CONTRACT_INFO_PAGE_MONEY_TYPES_AND_SOURCES_SECTION_TITLE = 62596;

    public static final int CONTRACT_INFO_PAGE_KEY_DATES_SECTION_TITLE = 62597;

    public static final int CONTRACT_INFO_PAGE_CONTRACT_ACCESS_CODE_SECTION_TITLE = 62598;

    public static final int CONTRACT_INFO_PAGE_STATEMENT_DETAILS_SECTION_TITLE = 62599;

    public static final int CONTRACT_INFO_PAGE_BD_INFO_SECTION_TITLE = 62593;

    public static final int CONTRACT_INFO_PAGE_BD_SHARE_INFO_SECTION_TITLE = 62592;

    public static final int CONTRACT_INFO_PAGE_ALLOCATION_DETAILS_SECTION_TITLE = 62595;

    public static final int CONTRACT_INFO_PAGE_PAYROLL_ALLOCATION_DETAILS_SECTION_TITLE = 62594;

    public static final int MESSAGE_CONTRACT_ACCESS_CODE = 62600;

    public static final int MISCELLANEOUS_PARTICIPANT_TOLL_FREE_NUMBER = 62602;

    public static final int MISCELLANEOUS_ENROLLMENT_FORM_FAX_NUMBER = 62604;

    public static final int MISCELLANEOUS_OTHER_FORM_FAX_NUMBER = 62605;

    public static final int MISCELLANEOUS_GENERAL_PHONE_NUMBER = 62601;
 
    public static final int CONTACT_SERVICE_REP_LABEL = 74725;
    
    public static final int CONTACT_SERVICE_REP_NUMBER_SPANISH = 74726;
    
    public static final int CONTACT_ROLLOVER_EDUCATION_SPECIALIST_LABEL = 74727;
    
    public static final int CONTACT_ROLLOVER_EDUCATION_SPECIALIST_PHONE = 74728;
    
    public static final int CONTACT_CONSOLIDATION_SPECIALIST_LABEL = 75016;
    
    public static final int CONTACT_CONSOLIDATION_SPECIALIST_PHONE = 75017;
    
    public static final int EMAIL_SUBJECT_TEXT = 93926;
    
    public static final int MISCELLANEOUS_DB_CONTRACT_PHONE_NUMBER = 57093;
    //Regulatory Disclosure section of contract information page
    public static final int CONTRACT_INFO_PAGE_REGULATORY_DISCLOSURES_SECTION_TITLE = 78275;
    
    public static final int CONTRACT_INFO_PAGE_REGULATORY_DISCLOSURES_INTRO_ONE = 78276;
    
    public static final int CONTRACT_INFO_PAGE_REGULATORY_DISCLOSURES_INTRO_TWO = 78273;
    
    public static final int CONTRACT_INFO_PAGE_REGULATORY_DISCLOSURES_LINK = 78274;
    
    public static final int CONTRACT_INFO_PAGE_REGULATORY_DISCLOSURES_TITLE = 79278;
    
    public static final int STABLE_VALUE_FUND_SUPPLEMENT_LINK = 81261; 
    public static final int STABLE_VALUE_FUND_NEW_YORK_LIFE_ANCHOR_TEXT = 89795;
    public static final int STABLE_VALUE_FUND_FEDERAL_CAPITAL_PRESERVATION_TEXT = 89794;
    public static final int STABLE_VALUE_FUND_RELIANCE_METLIFE_TEXT = 92465;
    
    // RP and R1 fund suite discloser
    public static final int RP_and_R1_VALUE_FUND_SUPPLEMENT = 98529;
    
    public static final int TRANSACTION_LOAN_REPAYMENT_LAYOUT_PAGE = 50877;

    // Registration pages
    public static final int CANCEL_POP_UP_MESSAGE = 55947;

    public static final int CANCEL_POP_UP_MESSAGE_EXTERNAL_BROKER = 62712;

    // Basic Broker Registration
    public static final int REGISTRATION_IDENTIFICATION_VALIDATION_SECTION_TITLE = 62687;

    public static final int EXTERNAL_BROKER_EMAIL_TEXT = 62688;

    public static final int EXTERNAL_BROKER_CONTRACT_TEXT = 62689;

    public static final int EXTERNAL_BROKER_SSN_TEXT = 62690;

    public static final int REGISTRATION_USERNAME_PASSWORD_SECTION_TITLE = 62734;

    public static final int REGISTRATION_USERNAME_TEXT = 64600;

    public static final int REGISTRATION_CHALLENGE_QUESTIONS_SECTION_TITLE = 62735;

    public static final int REGISTRATION_CHALLENGE_QUESTIONS_SECTION_DESCRIPTION = 62812;

    public static final int REGISTRATION_LICENSE_VERIFICATION_SECTION_TITLE = 62833;

    public static final int REGISTRATION_LICENSE_VERIFICATION_TEXT = 62834;

    public static final int REGISTRATION_PREFERENCES_SECTION_TITLE = 62736;

    public static final int REGISTRATION_PREFERENCES_TEXT = 62737;
    
    public static final int REGISTRATION_MESSAGE_LEARN_MORE = 62778;
    
    public static final int REGISTRATION_MESSAGE_CENTER_TEXT = 64118;
    
    public static final int REGISTRATION_TERMS_SECTION_TITLE = 62738;

    public static final int REGISTRATION_BASIC_BROKER_TERMS_TEXT = 62733;

    public static final int REGISTRATION_BROKER_TERMS_TEXT = 63536;
    
    public static final int REGISTRATION_RIA_TERMS_TEXT = 90564;
    
    public static final int MIGRATION_BROKER_TERMS_TEXT = 63684;
    
    public static final int REGISTRATION_FIRM_REP_TERMS_TEXT = 62799;

    public static final int REGISTRATION_ASSISTANT_TERMS_TEXT = 62775;

    public static final int FIRM_REP_ACCESS_CODE_TEXT = 62791;

    public static final int FIRM_REP_ASSISTANT_CONTACT_INFO_SECTION_TITLE = 62776;

    public static final int FIRM_REP_MESSAGE_CENTER_TEXT = 62800;

    public static final int REGISTRATION_TAKE_THE_TOUR = 62681;

    public static final int REGISTRATION_CONTACT_US = 62682;

    public static final int CHANGE_PWD_HELPER_LAYER = 62943;

    public static final int CHANGE_PASSWORD_SUCCESS = 62953;
    
    public static final int NEW_PASSWORD_HELP_TEXT = 62777;

    public static final int FORGET_PWD_LAYER = 62943;

    public static final int RESET_PWD_SUCCESS_TEXT = 63165;
    
    public static final int RESET_PASSCODE_SUCCESS_TEXT = 95501;
    
    public static final int RESET_PWD_ACCESS_CODE_TEXT = 63162;
    
    public static final int INTERNAL_USER_PERSONAL_SECTION_TITLE = 63299;

    public static final int INTERNAL_USER_PASSWORD_SECTION_TITLE = 63264;

    public static final int INTERNAL_USER_PASSWORD_HELP_TEXT = 62777;

    public static final int INTERNAL_USER_LICENSE_SECTION_TITLE = 62833;

    public static final int INTERNAL_USER_LICENSE_TEXT = 63301;

    public static final int INTERNAL_USER_PREFERENCE_SECTION_TITLE = 62736;

    public static final int INTERNAL_USER_UPDATE_SUCCESS_TEXT = 63239;

    public static final int INTERNAL_USER_LICENSE_INFO = 63303;

    public static final int EXTERNAL_USER_PASSWORD_SECTION_TITLE = 63264;

    public static final int EXTERNAL_USER_PASSWORD_HELP_TEXT = 62777;

    public static final int EXTERNAL_USER_CHALLENGE_TITLE = 63265;

    public static final int EXTERNAL_USER_CURRENT_PWD_TITLE = 63266;

    public static final int EXTERNAL_USER_SECURITY_UPDATE_SUCCESS_TEXT = 63239;

    public static final int ADD_ASSISTANT_LINK_TEXT = 63272;

    public static final int ADD_ASSISTANT_TERM_TEXT = 63273;

    public static final int ADD_ASSISTANT_SUCCESS_TEXT = 63281;

    public static final int RESNED_INVITE_ASSISTANT_TEXT = 63282;

    public static final int NO_ASSISTANT_TEXT = 63280;

    // My Profile External User Personal Info
    public static final int MY_PROFILE_SAVE_SUCCESS_MESSAGE_TEXT = 63239;

    public static final int MY_PROFILE_SAVE_SUCCESS_AND_EMAIL_SENT_MESSAGE_TEXT = 63228;
    
    public static final int MY_PROFILE_DEFAULT_FUND_LISTING_TITLE = 63288;

    public static final int BROKER_VERIFY_PERSONAL_INFO = 65260;

    public static final int PERSONAL_INFO_SECTION_TITLE = 63196;

    public static final int PERSONAL_INFO_PRIMARY_CONTACT_INFO_TEXT = 63198;

    /*-----------------User search -----------------------------------*/
    public static final int USER_SEARCH_NO_RESULT = 56379;

    public static final int USER_SEARCH_NO_CRITERIA = 63433;

    /*------------------ Add BOB -------------------------------------*/
    public static final int ADD_BOB_SECTION_TITLE = 63442;

    public static final int ADD_BOB_LEVEL1_FINISH = 63447;

    public static final int ADD_BOB_LEVEL2_FINISH = 63448;

    /* ------------ORDER ACR PAGE NOTICE----------- */
    public static final int ORDER_ACR_PAGE_NOTICE = 62889;
    
    public static final int ORDER_ACR_CONFIRMATION = 80295;
    
    public static final int MISCELLANEOUS_ACR_CONFIRMATION_TEXT = 79191;

    /*------------- Activation contents --------------*/
    public static final int ACTIVATION_BASIC_BROKER = 63524;

    public static final int ACTIVATION_BROKER = 63525;

    public static final int ACTIVATION_ADD_BROKER_ENTITY = 63526;

    public static final int ACTIVATION_NEW_PASSWORD = 63527;

    public static final int ACTIVATION_PENDING_BROKER_WARNING = 64159;

    /*-------------- Create Firm Rep ----------------------*/
    public static final int CREATE_FIRMREP_SECTION1 = 63299;

    public static final int CREATE_FIRMREP_SECTION2 = 64044;

    public static final int CREATE_FIRMREP_SECTION3 = 64043;

    public static final int CREATE_FIRMREP_ACCESS_CODE_HELP = 63162;

    public static final int CREATE_FIRMREP_SUCCESS = 64051;

    /* -------------- Firm name invalid --------------- */
    public static final int INVALID_BD_FIRM_NAME = 62875;
    
    /* --------------  Migration --------------------*/
    public static final int MIGRATION_USER_NAME_HELP = 63688;

    // User Management - Broker
    public static final int USERMANAGEMENT_PROFILE_SECTION_TITLE = 64197;

    public static final int USERMANAGEMENT_ASSISTANTS_SECTION_TITLE = 64199;
    public static final int UPDATE_ASSISTANT_SUCCESS = 90683;

    public static final int DELETE_BROKER_WARNING = 64200;
    public static final int DELETE_BROKER_ENTITY_WARNING = 64201;
    
    public static final int BROKER_RESEND_ACTIVATION_SUCCESS = 66716;
    
    public static final int BASIC_BROKER_RESEND_ACTIVATION_SUCCESS = 66716;
    public static final int UPDATE_BROKER_SUCCESS = 63239;
    
    public static final int DELETE_BROKER_ENTITY_SUCCESS = 64202;
    
    public static final int DELETE_BASIC_BROKER_WARNING = 64210;
    public static final int UPDATE_BASIC_BROKER_SUCCESS = 63239;
    
    public static final int DELETE_ASSISTANT_WARNING = 64219;
    public static final int RESEND_INVITE_ASSISTANT_SUCCESS = 64220;

    public static final int DELETE_FIRMREP_WARNING = 64237;
    public static final int RESNED_INVITE_FIRMREP_SUCCESS = 63282;
    public static final int UPDATE_FIRM_REP_SUCCESS = 63239;
    
    public static final int DELETE_RIA_WARNING = 90681;
    
    
    public static final int PARTICIPANT_STATUS_NOT_AVAILABLE = 62624;
    
    //BD Public Home Page
    public static final int PROMOTIONAL_INFO_CONTENT = 63904;
    public static final int INVESTMENT_STORY_LAYER = 63486;
    public static final int MARKET_WATCH_CONTENT = 61778;
    public static final int NEWS_EVENTS_HEADER = 63487;
    public static final int REMEMBER_USERNAME_CHECKBOX_LABEL = 73676;
    
    /* --------------  Fund Evaluator --------------------*/
    // To display a warning message when user navigates awasy from tool
    public static final int WARNING_NAVIGATES_AWAY_FROM_FUNDEVALUATOR = 55951;
    
    //STEP 1 : CLIENT INFORMATION PAGE
    public static final int STEP1_FUND_EVALUATOR_BENEFITS = 64809;
    
    //STEP 2 : CRITERIA SELECTION PAGE    
    public static final int STEP2_FUND_EVALUATOR_BENEFITS = 64815;
    
    //STEP 3 :Narrow your list
    public static final int STEP3_FUND_EVALUATOR_BENEFITS = 64819;
    
    public static final int GA_DISCLOSURE = 83000;
        
    public static final int WARRANTY_TOPFUNDS_DESC = 64823;
    
    public static final int TOPFUNDS_DESC = 64824;
    
    public static final int NO_SELECTION_DESC = 64825;
    
    //STEP5 : GENERATE REPORT PAGE
    public static final int STEP5_FUND_EVALUATOR_BENEFITS = 64916;
    
    
    
    
    public static final int FUND_EVALUATOR_BENEFITS = 64893;
    
    public static final int EQUITY_FUNDS_LABEL = 64896;
    
    public static final int HYBRID_INDEX_SECTOR_FUNDS_LABEL = 64897;
    
    public static final int FIXED_INCOME_FUNDS_LABEL = 64898;
    
    public static final int GURANTEED_ACCOUNTS_FUNDS_LABEL = 64899;
    
    public static final int FUNDS_DESELECT_WARNING = 64805;
    
    public static final int OVERLAY_TITLE = 64900;
    
    public static final int OVERLAY_PREVIEW_TITLE = 64915;
    
    public static final int HOW_TO_ADD_MORE_FUNDS_TO_FUND_EVALUATOR = 64901;
    
    public static final int ICON_LABEL_CLOSED_FUND = 65820;
    
    public static final int ICON_LABEL_PBA_COMPETING = 89052;
    
    public static final int  COMPETING_FUNDS_LINK_LABLE = 89053;
    
    public static final int ICON_LABEL_CONTRACT_SELECTED_FUND = 65821;
    
    public static final int LABEL_NOT_APPLICABLE = 67092;
    
    public static final int PREVIEW_FUNDS_PRINT_DISCLAIMER = 67036;
    
    public static final int SELECT_PARTICIPANT_INFO_MESSAGE = 62570;
    
    // My profile help 
    public static final int MY_PROFILE_HELP_LINK = 64191;
    
    /*------------  Message Center CMA ids -----------------------------*/
    public static final int GLOBAL_MESSAGE_TEXT = 64725;
    
    public static final int MESSAGE_CENTER_PREF_SECTION_TITLE = 64722;
    
    public static final int OTHER_PREF_SECTION_TITLE = 64723;
    
    public static final int MESSAGE_CENTER_PREF_EMAIL_TITLE = 64724;

    
    //Contract snapshot page pdf uses the MRL PDF framework. 
    //To use cma text in pdf page we are using separate content ids.
    public static final int PDF_PERSONAL_BROKERAGE_ACCOUNT  = 65516;
    
    public static final int PDF_ASSETS_ALLOCATED_TO_ACTIVE_PARTICIPANT = 65518;
    
    public static final int PDF_INFORMATION_FOR_REPORTING_DATE_SELECTED = 65519;
    
    public static final int PDF_GLOBAL_FOOTNOTE_TEXT1 = 65520;
    
    public static final int PDF_GLOBAL_FOOTNOTE_TEXT2 = 65521;
    
    public static final int PDF_GLOBAL_FOOTNOTE_TEXT3 = 65522;    
    
    public static final int PDF_GLOBAL_FOOTNOTE_TEXT4 = 65528;

    public static final int PDF_GLOBAL_FOOTNOTE_TEXT5 = 65529;    
    
    public static final int MESSAGE_CENTER_PREF_SECTION_DESC = 65542;
    
    //Secured Home Page
  //BRKLV1
    public static final int BDFOCUSED_LAYER_ID_FOR_BASIC_BROKER = 90419;
    
    public static final int BDFOCUSED_LAYER_ID_FOR_FIRM_REP = 90418;
    //BRKLV2
    public static final int BDFOCUSED_LAYER_ID_FOR_BROKER = 90420;
    
    public static final int BDFOCUSED_LAYER_ID_FOR_BROKER_ASSIST = 90421;
    
    public static final int BDFOCUSED_LAYER_ID_FOR_RIA = 90912;
    
    
    public static final int LAYER_ID_FOR_BASIC_BROKER = 64484;
    
    public static final int LAYER_ID_FOR_FIRM_REP = 64485;
    
    public static final int LAYER_ID_FOR_BROKER = 64486;
    
    public static final int LAYER_ID_FOR_INTERNAL_USER_NOT_IN_MIMIC = 64487;    
    
    public static final int IEVALUATOR_LINK_TEXT = 64472;
    
    public static final int MARKET_COMMENTARY_TITLE = 64483;
    
    public static final int MY_BOB_TITLE = 64473;
    
    public static final int MY_BOB_LINK = 64474;

    public static final int MESSAGE_CENTER_SECTION_TITLE = 64475;
    
    public static final int UNREAD_MESSAGES_LABEL = 64476;
    
    public static final int CHANGE_MY_PREFERENCES_TEXT = 65669;
    
    public static final int DEFINED_BENEFIT_ACCOUNT_LAYOUT = 62568;

    public static final int HOME_PAGE_IMAGE_LAYER = 67186;
    
    public static final int LAYER_ID_FOR_RIAUSER = 90659;
    
    /*-------------------Message Center --------------------------------*/
    public static final int MC_NO_MESSAGE_TEXT = 65634;
    
    public static final int MC_DELETE_MESSAGE_WARNING = 65636;   
    
    public static final int GLOBAL_MESSAGES_PUBLISH_CONFIRM = 65642;
    
    public static final int GLOBAL_MESSAGES_PUBLISH_SUCCESS = 65644;
    
    public static final int GLOBAL_MESSAGES_EXPIRE_CONFIRM = 65643;
    
    public static final int GLOBAL_MESSAGES_EXPIRE_SUCCESS = 65645;

    public static final int CHALLENGE_QUESTION_SECTION_DESCRIPTION = 65278;
    
    public static final int BDW_STANDARD_ERROR_PAGE = 65084;
    
    public static final int BD_CONTRACT_SNAPSHOT_LAYOUT_PAGE = 62623;
    
    public static final int BD_CONTRACT_SNAPSHOT_LAYOUT_PAGE_DB = 67051;
    
    public static final int MISCELLANEOUS_MESSAGE_CENTER =  66392;
    
    public static final int MISCELLANEOUS_BLOCK_OF_BUSINESS = 66219;
    
    public static final int MISCELLANEOUS_PRIME_ELEMENTS = 66220;
    
    public static final int MISCELLANEOUS_FORMS = 66221;
    
    public static final int MISCELLANEOUS_PARTNERING_WITH_US = 66222;
    
    public static final int MISCELLANEOUS_NEWS_EVENTS = 66223;
    
    public static final int MISCELLANEOUS_FIND_LITERATURE = 66224;
    
    public static final int FUND_EVALUATOR_GLOSSARY = 66156;
    
    public static final int BOB_SUMMARY_FOOTNOTE = 67301;

    public static final int DB_FAX_NUMBER = 67501;
    
    //Gifl Footnote in Participant Account Page
    public static final int BDW_PA_GIFL_FOOTNOTE = 67438;
    
    public static final int MISCELLANEOUS_BENEFIT_BASE_BATCH_OUT_OF_DATE = 61370;
    
    public static final int NO_TRANSACTIONS_MESSAGE_FOR_BENEFIT_BASE_PAGE = 60773;
    
    public static final int BENEFIT_BASE_PAGE_DYNAMIC_STEP_UP_FOOTNOTE = 59954;
    
    public static final int BENEFIT_BASE_PAGE_DYNAMIC_INCOME_ENHANCEMENT_FOOTNOTE = 67137;
    
    // Contract Summary page
	public static final int GIFL_V1_V2_INTERNAL_USER_FEATURES = 70259;
	
	public static final int GIFL_V1_V2_EXTERNAL_USER_FEATURES = 70261;
	
	public static final int GIFL_V3_INTERNAL_USER_FEATURES = 70260;
	
	public static final int GIFL_V3_EXTERNAL_USER_FEATURES = 70262;
	
	public static final int GUARANTEED_INCOME_FEATURE_FOOTNOTE = 70746;
  
    /*------------------- Plan Highlights Section -----------------*/
    public static final int PLAN_HIGHLIGHTS_SECTION_HEADER = 71692;
    
    public static final int PLAN_HIGHLIGHTS_TEXT = 71693;
    
    public static final int PLAN_HIGHLIGHTS_LINK = 71746;
    
    /*----------------------------Fund Check---------------------*/
    
    public static final int FUNDCHECK_ADDITIONAL_RESOURCES_LAYER = 72167;
 
    public static final int FUNDCHECK_NO_PDFS_EXISTS_MESSAGE = 72168;
    
    public static final int FUNDCHECK_NO_PDFS_EXISTS_MESSAGE_LEVEL2 = 75591;
    
    public static final int NO_SEARCH_RESULT_FOUND=66197;    
 
    public static final int NO_SUFFICIENT_SEARCH_CHARACTERS_PROVIDED = 63657;
    
    public static final int SEARCH_ITEM_OR_TEXT_NOT_ENTERED = 72173;
    
    public static final int PLEASE_ADD_MORE_FILTERS = 72835;
    
    //IPS
    public static final int IPS_SERVICE_BROCHURE_LINK = 79515;
    public static final int INVESTMENT_POLICY_STATEMENT_LINK = 79516;
    public static final int IPS_PARTICIPANT_NOTIFICATION_LINK = 79517;
    public static final int IPS_ASSIST_SERVICE_TEXT = 79521;
    public static final int IPS_SCHEDULE_ANNUAL_REVIEW_DATE = 79522;
    public static final int IPS_SERVICE_REVIEW_DATE_TEXT = 79523;
    public static final int IPS_ASSIST_SERVICE_TEXT_DEACTIVATED = 79525;
    public static final int IPS_REVIEW_REPORTS_SECTION_TITLE = 79526;
    public static final int IPS_VIEW_REPORT_PDF_ICON = 79527;
    public static final int IPS_CURRENT_REPORT_LABEL_LINK = 79528;
    public static final int IPS_NO_CURRENT_OR_PREVIOUS_REPORT = 79529;
    public static final int IPS_PDF_REVIEW_NOT_AVAILABLE = 79530;
    public static final int IPS_REVIEW_RESULT_REPORT_LINK = 79534;
    public static final int PARTICIPANT_NOTIFICATION_LINK = 79647;
    public static final int IPS_IAT_EFFECTIVE_DATE_DETAILS = 79648; 
    public static final int IPS_FUND_ACTION_APPROVE = 79496;
    public static final int IPS_FUND_ACTION_IGNORE = 79510;
    public static final int IPS_FUND_ACTION_NOT_SELECTED = 79511;
    public static final int IPS_CONTRACT_LEVEL_REDEMPTION_FEES_TEXT = 79431;
    public static final int IPS_PARTICIPANT_LEVEL_REDEMPTION_FEES_TEXT = 79432;
    
    // RIA IPS assist service page
    public static final int SCHEDULED_ANNUAL_REVIEW_DATE_TEXT = 76417;
    public static final int CHANGE_CRITERIA_WEIGHTING_LINK = 76418;
    public static final int IPS_ASSIST_SERVICE_LINK_TEXT = 76389;
    public static final int IPS_ASSIST_SERVICE_DESC = 76377;
    public static final int SERVICE_REVIEW_DATE_DETAILS_TEXT = 79402;
    public static final int IPS_REVIEW_REPORTS_TITLE = 79403;
    public static final int VIEW_RESULTS_ICON_TEXT = 79476;
    public static final int EDIT_RESULTS_ICON_TEXT = 79477;
    public static final int CANCEL_RESULTS_ICON_TEXT = 79478;
    public static final int IPS_REPORT_PDF_LINK = 79408;
    public static final int MESSAGE_IPS_REVIEW_INPROCESS_WHEN_CHANGING_SERVICE_DATE = 92381;
    public static final int NO_PREVIOUS_IPS_REPORT_TEXT = 79411;
    public static final int IPS_REVIEW_LOCKED_BY_TR_TEXT = 79412;
    public static final int IPS_CURRENT_REPORT_TEXT = 79475;
    public static final int NO_IPS_REPORT_PDF_TEXT = 79422;
    public static final int IPS_INTERIM_REPORT_LINK = 79423;
    public static final int IPS_IAT_EFFEXTIVE_DATE_TEXT = 79426;
    public static final int IPS_NO_IAT_EFFEXTIVE_DATE_SELECTED_WARNING = 79429;
    public static final int IPS_NO_ACTION_TAKEN_WARNING = 79428;
    public static final int IPS_ANNUAL_REVIEW_RESULT_AS_OF_TEXT = 79682;
    public static final int IPS_PARTICIPANT_NOTIFICATION = 78387;
    public static final int IPS_NEW_ANNUAL_REVIEW_DATE_DETAILS_TEXT = 79672;
    public static final int IPS_NEW_ANNUAL_REVIEW_DATE_TEXT = 79673;
    public static final int IPS_VIEW_CURRENT_REPORT_LINK = 79424;
    public static final int IPS_APPROVAL_ACTION_TEXT = 79430;
    public static final int IPS_TERMS_ANDCONDITION_TEXT = 79433;
    public static final int IPS_APPROVE_CONFIRMATION_AS_OF_DATE_TEXT = 79466;
    public static final int IPS_PARTICIPANT_OVERLAY_PAGE_TITLE = 79468;
    public static final int IPS_CONTACT_NAME_TEXT = 79457;
    public static final int IPS_CONTACT_INFO_TEXT = 79458;
    public static final int IPS_CONTACT_COMMENT_TEXT = 79459;
    public static final int IPS_CANCEL_DETAILS1_TEXT = 92386;
    public static final int IPS_CANCEL_DETAILS2_TEXT = 79454;
    public static final int IPS_CANCEL_CONNFIRAMTION_TEXT = 92266;
    public static final int IPS_CANCEL_NYSE_NOT_AVAILABLE_TEXT = 79456;

  
    public static final int EMP_SMT_HEADER =78171;
	
	public static final int EMP_SMT_INTRO =78122;
	
    public static final int PLN_ADM_HEADER =78172;
	
	public static final int PLN_ADM_INTRO =78123;

	public static final int SCH_A_HEADER =78173;
	
	public static final int SCH_A_INTRO =78124;

    public static final int SCH_B_HEADER =78174;
	
	public static final int SCH_B_INTRO =78125;

    public static final int CLS_CON_HEADER =78175;
	
	public static final int CLS_CON_INTRO =78126;

    public static final int BNF_PMT_HEADER =78176;
	
	public static final int BNF_PMT_INTRO =78127;
	
	//Contract Statements Messages
	public static final int MESSAGE_NO_CONTRACT_STATEMENTS = 78134;
	
	//FRW DetailedAccountReport CSV link CMA content(s)
	public static final int DISCLAIMER_DETAILED_ACCOUNT_REPORT = 78388;
	
	public static final int NO_ACTIVE_CONTRACTS_TO_DISPLAY = 78513;
	
	// 404a5 elements
	public static final int ICC_TITLE = 78919 ;
	public static final int PLAN_AND_INVESTMENT_NOTICE = 82699;
    public static final int INVESTMENT_COMPARATIVE_CHART_LINK_NOTE = 80282;
    public static final int ICC_YEAR_END_WARNING_MESSAGE_KEY = 78920;
    public static final int ICC_WARNING_MESSAGE_KEY = 79038;
    public static final int PLAN_INFORMATION_NOTICE_ADDENDUM_TEMPLATE = 82670;
    public static final int INVESTMENT_COMPARATIVE_CHART = 79266;
    public static final int PARTICIPANT_FUND_CHANGE_NOTICE = 82672;
    public static final int PLAN_AND_INVESTMENT_NOTICE_GUIDE = 88839;
    public static final int INVESTMENT_COMPARATIVE_CHART_GUIDE = 88840;
    
    public static final int IPS_GUIDE_PATH = 78002;
	public static final int IPS_SERVICE_BROCHURE_PATH = 77971;

	public static final int IPS_PARTICIPANT_NOTIFICATION_PATH = 78491;
	
	public static final int IPS_CHANGE_CRIERIA_AND_WEIGHTINGS = 76418;
	
	 // LIA Constants
	  public static final int MISCELLANEOUS_LIA_SELECTION_DATE_FIELD_LABEL = 83085;
	  public static final int MISCELLANEOUS_LIA_SPOUSAL_OPTION_FIELD_LABEL = 83086;
	  public static final int MISCELLANEOUS_LIA_PERCENTAGE_FIELD_LABEL = 83087;
	  public static final int MISCELLANEOUS_ANNUAL_LIA_AMOUNT_FIELD_LABEL = 83088;
	  public static final int MISCELLANEOUS_LIA_PAYMENT_FREQUENCY_FIELD_LABEL = 83089;
	  public static final int MISCELLANEOUS_LIA_ANNIVERSARY_DATE_FIELD_LABEL = 83090;
	  public static final int MISCELLANEOUS_BENEFIT_BASE_LIA_MESSAGE = 83287;

	// FD2013 detailed report CSV
	public static final int AB_1ST_YEAR_LEGEND = 82678;
	public static final int AB_REN_YEAR_LEGEND = 82679;
	public static final int AB_ALL_YEAR_LEGEND = 83988;
	public static final int RIA_FEE_PAID_BY_JH_LEGEND = 83989;
	public static final int MORE_THAN_ONE_RIA_LEGEND = 83990;
	public static final int AB_COLUMN_FOOTNOTE = 83974 ;                   
    public static final int DAILY_UPDATE_FOOTNOTE = 83977;  
    public static final int BOB_LEGEND_AB = 83975;  
    public static final int BOB_LEGEND_RIA = 83976;
    
    public static final int COFID321_SERVICE_BROCHURE_LINK = 84534;
    public static final int NO_COFID321_REPORTS_FOUND = 84521;

    public static final int MESSAGE_STMT_NO_PARTICIPANTS_ON_BASIC_SEARCH = 91216;
    public static final int MESSAGE_STMT_NO_PARTICIPANTS_ON_ADV_SEARCH = 91216;    
    public static final int MESSAGE_NO_STMT_FOR_PARTICIPANT = 83755;
    
    //RIA Fee detailed report CSV
    public static final int RIA_FLAT_PRORATA_FEE_AMT = 91911;
    public static final int RIA_FLAT_PER_HEAD_FEE_AMT = 91910;
    public static final int RIA_ASSET_BASED_BPS_FEE_AMT = 91906;
    public static final int RIA_ASSET_BASED_BPS_MAX_FEE_AMT = 91907;
    public static final int RIA_ASSET_BASED_BLENDED_FEE_AMT = 91908;
    public static final int RIA_ASSET_BASED_TIERED_FEE_AMT = 91909;
	
    //Accuwrite Change for Contract Document page
    public static final int MISCELLANEOUS_CONTRACT_NOT_AVAILABLE = 85112;
    //Block of Business Contract Review Reports pages
 
	
	public static final int PLAN_REVIEW_HISTORY_SUMMARY_LINK = 88475;
	public static final int PLAN_REVIEW_REQUEST_LINK = 88476;
	public static final int ACCESS_PLAN_REVIEW_HISTORY_LINK = 88859;
	public static final int SELECT_ONE_CONTRACT_MESSAGE = 88852;
	public static final int UPLOAD_COVER_INSTRUCTIONS = 88477;
	public static final int TERMS_OF_USE = 89109;
	public static final int TERMS_OF_USE_LINK = 88402;
	public static final int UPLOAD_LOGO_INSTRUCTIONS_CONTRACT_LEVEL = 88473;
	public static final int UPLOAD_LOGO_INSTRUCTIONS = 88478;
	public static final int UPLOAD_LOGO_COMPANY_NAME_MANDATORY = 88436;
	public static final int PLAN_REVIEW_DEMO_VIDEO_LINK = 89111;
	public static final int PLAN_REVIEW_SAMPLE_REPORT_LINK = 89112;
	public static final int PLAN_REVIEW_BACK_BUTTON_CLICKED = 88439;
	public static final int PLAN_REVIEW_REQUEST_BOB_LINK = 88481;
	public static final int PLAN_REVIEW_PRINT_LINK = 88480;
	public static final int PLAN_REVIEW_PRINT_LINK_CONTRACT_LEVEL = 88470;
	public static final int PLAN_REVIEW_REQUEST_LINK_CONTRACT_LEVEL  = 88471;
	public static final int PLAN_REVIEW_PRINT_REQUEST_LINK = 88988;
	public static final int PLAN_REVIEW_STEP1_PAGE_LINK = 88987;
	public static final int PLAN_REVIEW_STOCK_IMAGES_INTRODUCTION_TEXT = 89110;
	public static final int UPLOADED_COVER_IMAGE_GREATER_THAN_TWO_MB_ERROR = 88430;
	public static final int UPLOADED_COVER_IMAGE_IS_NOT_JPG_ERROR = 88431;
	public static final int PLAN_REVIEW_REQUEST_I_WONT_TO_TEXT = 88984; 
	public static final int PLAN_REVIEW_HISTORY_DISABLE_CONFIRM_TEXT = 88978;
	public static final int UPLOADED_IMAGE_COLORTYPE_IS_NOT_RGB = 88401;
	public static final int CREATE_CSV_FILE = 62001;
	public static final int UPLOADED_LOGO_IMAGE_IS_NOT_JPG_ERROR = 88434;
	public static final int UPLOADED_LOGO_IMAGE_GREATER_THAN_ONE_MB_ERROR  = 88433;
	public static final int PLAN_REVIEW_RESULTS_ERROR_MESSAGE  = 88410;
	public static final int PLAN_REVIEW_RESULTS_INCOMPLETE_ERROR_MESSAGE  = 89769;
	//CL - DR 332240 - Plan Review download reports page - Limit # of PDF downloads
	public static final int MORE_THAN_30_CONTRACT_ROWS_ERROR_MESSAGE  = 94042;
	
	
	
	//for contract level changes
	public static final int PLAN_REVIEW_HISTORY_LINK_CONTRACT = 88468 ;
	
	
	//result page messages.
	
	public static final int PLAN_REVIEW_REQUEST_SOME_IN_INPROGRESS = 88435;
	public static final int PLAN_REVIEW_REQUEST_ALL_IN_INPROGRESS = 88437;
	public static final int PLAN_REVIEW_REQUEST_CONFIRM_MESSAGE = 88426;
	public static final int PLAN_REVIEW_PRINT_REQUEST_CONFIRM_MESSAGE = 88851;
	
	public static final int PLAN_REVIEW_NO_RECORDS_TO_DISPLAY_MESSAGE = 90056;
	public static final int PLAN_REVIEW_ENTER_SEARCH_CRITERIA = 90057;
	public static final int PLAN_REVIEW_HISTORY_NO_RECORDS_TO_DISPLAY_MESSAGE = 90058;
	public static final int PLAN_REVIEW_PRINT_NO_RECORDS_TO_DISPLAY_MESSAGE = 90059;
    
   
    //Regulatory Disclosures
    public static final int REPORT_DETAILS_SUMMARY_CONTENT = 87345;
    public static final int TITLE_UNDERSTANDING_408B2_SECTION =87431;
    public static final int INVESTMENT_SELECTION_CONTENT= 87430;
    public static final int INVESTMENT_SELECTION_CONTENT_CL0= 88501;
    public static final int INVESTMENT_SELECTION_CONTENT_NON_CL0= 88503;
    
    public static final int ICON_LABEL_CALCULATED_FUND = 92456;
    public static final int ICON_LABEL_ADDED_FUND = 92457;
    public static final int ICON_LABEL_REMOVED_FUND = 92458;
    
    //RIA Fee Calculation
    public static final int RIA_BPS_FEE_TILTLE = 91117;
    public static final int RIA_BLEND_FEE_TILTLE = 91118;
    public static final int RIA_TIERED_FEE_TILTLE = 91119;
    
    public static final String HOMEPAGE_FINDER_FORWARD = "homePageFinder";
    public static final String EMPTY_STRING = "";
    
    public static final int IPS_NEW_ANNUAL_REVIEW_DATE_SAVE_CONFIRMATION_TEXT = 79674;
    public static final int IPS_NEW_ANNUAL_REVIEW_DATE_NO_STATUS_CHANGE_SAVE_CONFIRMATION_TEXT = 79675;
    public static final int IPS_EXTERNAL_NEW_ANNUAL_REVIEW_DATE_SAVE_CONFIRMATION_TEXT = 79676;
    public static final int IPS_EXTERNAL_NEW_ANNUAL_REVIEW_DATE_NO_STATUS_CHANGE_SAVE_CONFIRMATION_TEXT = 79677;
    public static final int SAVE_HOVER_OVER_TEXT = 77002;
    public static final int IPS_ADHOC_REPORT_LINK = 79409;
    public static final int IPS_IAT_EFFECTIVE_DATE_DESC_TEXT = 79697;
    public static final int SAVE_CONFIRMATION_TEXT = 92255;
    public static final int LABEL_STREET_ADDRESS = 90286;
    public static final int LABEL_CITY_AND_STATE = 90287;
    public static final int LABEL_ZIP_CODE = 90288;
    
    public static final int PERA_IS_SELECTED = 95351;
    public static final int PERA_IS_NOT_SELECTED = 95352;
    
    public static final int LEGEND_RIA_ASSET_BASED_BPS_MIN_FEE_AMT = 95329;
    public static final int LEGEND_COFID_321_ASSET_BASED_BPS_FEE = 95293;
    public static final int LEGEND_COFID_321_DOLLAR_BASED_FEE_AMT = 95294;
    public static final int LEGEND_COFID_338_ASSET_BASED_BPS_FEE = 95295;
    public static final int LEGEND_COFID_338_DOLLAR_BASED_FEE_AMT = 95296;
    public static final int HOVER_BPS_FEE_MONTHLY_MIN_AMT = 95321;
    public static final int HOVER_BPS_FEE_ANNUAL_MAX_AMT = 95335;
    
    //Passcode Exemption
    public static final int EXEMPT_PCD_ERROR_MSG_1 = 63165;
    
    //Fund Administration tab
    public static final int FUND_ADMINISTRATION_TAB = 96717;
    
    //SecureDocumentUpload Pages
    public static final int SDU_SUBMIT_TAB_INTRO = 96781;
    public static final int SDU_HISTORY_TAB_INTRO = 96782;
    public static final int SDU_VIEW_TAB_INTRO = 96783;
    public static final int SDU_SHARE_DOCS_PAGE_INTRO = 96784;    
    public static final int SDU_SHARE_DOCS_PAGE_PRIVACY_MESSAGE = 96670;
    public static final int SDU_SHARE_DOCS_PAGE_PRIVACY_MESSAGE_BDW = 96820;
    public static final int SDU_NO_RECORDS_TO_DISPLAY_MESSAGE = 90056;
    public static final int SDU_DELETE_DOCUMENT_WARNING_MESSAGE = 96525;
    public static final int SDU_FILE_NOT_AVAILABLE_MESSAGE = 96673;
    public static final int SDU_HISTORY_NOT_AVAILABLE_MESSAGE =91214;
    public static final int SESSION_EXPIRED_TEXT = 97600;
    public static final int SESSION_EXPIRY_WARNING_MESSAGE = 97599; 
    
    //Relationship Manager Info
    public static final int RELATIONSHIP_MANAGER_STATIC_INFO_TEXT = 97930;
    
    public static final int DE_API_ERROR_MESSAGE = 97925;
    
    // LT fund suite discloser
    public static final int LT_VALUE_FUND_SUPPLEMENT = 98857;
    public static final int DISCLOUSRE_MESSAGE_TEXT_FOR_IA = 99059;

	}	