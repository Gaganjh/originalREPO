package com.manulife.pension.platform.utility.content;

public interface CommonContentConstants {
	// CONTENT TYPES
	public static final String TYPE_MISCELLANEOUS = "Miscellaneous";

	public static final String TYPE_MESSAGE = "Message";

	public static final String TYPE_NOTIFICATION = "Notification";

	public static final String TYPE_STANDALONE_TOOL = "StandAloneTool";

	public static final String TYPE_PAGEFOOTNOTE = "PageFootnote";

	public static final String TYPE_DISCLAIMER = "Disclaimer"; // added for G.I.F.L 1C

	// BD news
	public static final String TYPE_UPDATE = "Update";

	// BD Registration
	public static final String TYPE_LAYOUT_PAGE = "LayoutPage";

	public static final String TYPE_LAYER = "Layer";

	// fixed footnotes
	public static final int FIXED_FOOTNOTE_PBA = 53285;

	public static final int FIXED_FOOTNOTE_CONTRACT_SNAPSHOT_PBA_PDF = 53687;

	public static final int FIXED_FOOTNOTE_CONTRACT_SNAPSHOT_NOPBA_PDF = 53686;

	public static final int FIXED_FOOTNOTE_CONTRACT_SNAPSHOT_DEFINED_BENEFIT_PDF = 57249;

	public static final int IS_PBA = 51034;

	public static final int GLOBAL_DISCLOSURE = 53808;

	// Global Disclosure for NBDW website.
	public static final int BD_GLOBAL_DISCLOSURE = 64190;

	public static final int HANDLE_BAD_FILE = 55968;

	// Warning for Investment allocations details for no participants invested in the fund
	public static final int WARNING_NO_PARTICIPANTS_INVESTED_IN_THE_FUND = 50987;

	public static final int REBALANCE_DETAILS_EXPAND = 55015;

	public static final int REBALANCE_DETAILS_COLLAPSE = 55014;

	public static final int TRANSACTION_LOAN_REPAYMENT_LAYOUT_PAGE = 50877;

	public static final int TRANSACTION_DETAILS_LOAN_REPAYMENT_LAYOUT_PAGE = 61991;

	public static final int TRANSACTION_LOAN_REPAYMENT_REPORT_LAYOUT_PAGE = 62011;

	public static final int MESSAGE_MVA_APPLIED = 54979;

	public static final int MESSAGE_REDEMPTION_FEE_APPLED = 54978;

	public static final int INVESTMENT_ALLOCATION_INVALID_ASOFDATE_SELECTED = 55414;

	int WARNING_INVALID_REPORT_PAGE_SIZE = 53883;

	// MISCELLANEOUS text
	public static final int MISCELLANEOUS_ROTH_INFO = 55100;

	public static final int CONTRACT_SNAPSHOT_LAYOUT_PAGE = 50855;

	// TRANSACTIONS
	public static final int MISCELLANEOUS_TRANSACTION_HISTORY_WITHDRAWAL_MESSAGE = 55606;

	// Contract default investments message
	public static final int MESSAGE_NO_DEFAULT_INVESTMENTS = 55431;

	// participant account page
	public static final int MISCELLANEOUS_DB_PARTICIPANT_ACCOUNT_MONEY_TYPE_REPORTS_NOT_AVAILABLE = 58723;

	public static final int MESSAGE_DB_PARTICIPANT_ACCOUNT_NO_PARTICIPANTS = 58724;

	// added for ContractsOntheWeb
	public static final int MISCELLANEOUS_CONTRACT_DOCUMENTS_LINK = 60360;

	public static final int MISCELLANEOUS_CONTRACT_DOCUMENTS_DESCRIPTION = 60361;

	public static final int MISCELLANEOUS_DOWNLOAD_CONTRACT_LINK = 60366;

	public static final int MISCELLANEOUS_AMENDMENTS_NOT_AVAILABLE = 60416;

	// Contract snapshot Page
	public static final int CONTRACT_SNAPSHOT_GIFL_MESSAGE = 71047;

	// participant summary
	public static final int MESSAGE_PARTICIPANTS_VIEW_ALL = 53866;

	public static final int MESSAGE_PARTICIPANTS_NO_SEARCH_RESULTS = 53867;

	public static final int MESSAGE_SEARCH_FOR_PARTICIPANTS = 57333;

	// participant statement

	public static final int MESSAGE_PARTICIPANTS_STMT_NO_SEARCH_RESULTS = 83692;

	// Loan Summary
	public static final int MISCELLANEOUS_LOAN_SUMMARY_NO_LOANS = 50986;

	// PDF related
	public static final String PAGE_NAME = "name";

	public static final String INTRO1_TEXT = "introduction1";

	public static final String INTRO2_TEXT = "introduction2";

	public static final String SUB_HEADER = "subHeader";

	public static final String TEXT = "text";

	public static final String BODY1_HEADER = "body1Header";

	public static final String BODY2_HEADER = "body2Header";

	public static final String BODY3_HEADER = "body3Header";

	public static final String BODY1 = "body1";
	public static final String BODY2 = "body2";
	public static final String BODY3 = "body3";

	// participant account
	public static final int MESSAGE_PARTICIPANT_ACCOUNT_NO_PARTICIPANTS = 51393;
	public static final int MISCELLANEOUS_PARTICIPANT_ACCOUNT_MONEY_TYPE_REPORTS_NOT_AVAILABLE = 54216;
	public static final int PS_PARTICIPANT_ACCOUNT_EARNINGS_FOOTNOTE = 55313;

	public static final int MESSAGE_OUT_OF_SERVICE_HOURS = 51038; // ErrorCode 1010
	public static final int MESSAGE_TECHNICAL_DIFFICULTIES = 52871; // ErrorCode 1047

	public static final int PS_CONTACTS_ACCOUNT_REPRESENTATIVE_PHONE = 50829;

	/* -------------- Funds And Performance --------------- */
	public static final int MESSAGE_NOT_FUND_CHECK_INFO_IN_DOWNLOADED_CSV = 63991;
	public static final int MESSAGE_CANNOT_PRINT_USING_BROWSER_PRINT = 63992;
	public static final int NO_MATCHING_RESULTS_FROM_QUERY = 64068;

	public static final int ATTEMPT_TO_SAVE_CUSTOM_QUERY_WITH_ERRORS = 64063;
	public static final int CUSTOM_QUERY_NAME_ALREADY_EXISTS = 64065;
	public static final int LIST_NAME_ALREADY_EXISTS = 64065;
	public static final int CLOSED_FUNDS_IN_GRAY_FONT = 63993;

	public static final int FUND_CHECK_LEGEND_TITILE = 65473;
	public static final int FUND_CHECK_LEGEND_DESC_STRONG = 65465;
	public static final int FUND_CHECK_LEGEND_DESC_SATSIFACTORY = 65466;
	public static final int FUND_CHECK_LEGEND_DESC_UNSATSIFACTORY = 65467;
	public static final int FUND_CHECK_LEGEND_DESC_NA = 65468;
	public static final int FUND_CHECK_LEGEND_DESC_TO_BE_REMOVED = 66357;

	public static final int FUND_AND_PERFORMANCE_LAYOUT = 64070;
	public static final int FUND_AND_PERFORMANCE_LAYOUT_LAYER1 = 64071;

	public static final int FUND_INFORMATION_TAB = 64076;
	public static final int PRICES_AND_YTD_TAB = 64077;
	public static final int STANDARD_DEVIATION_TAB = 64079;
	public static final int FUND_CHARACTERISTICS_1_TAB = 64080;
	public static final int FUND_CHARACTERISTICS_2_TAB = 64081;
	public static final int MORNINGSTAR_TAB = 64082;
	public static final int FUND_CHECK_TAB = 64083;

	/*------------------- TPA - Funds & Performance -----------------*/
	// layouts
	public static final int TPA_FUND_AND_PERFORMANCE_LAYOUT = 54270;
	public static final int TPA_FUND_AND_PERFORMANCE_LAYOUT_LAYER1 = 64155;

	// Disclaimers for tabs
	public static final int TPA_FUND_INFORMATION_TAB = 64139;
	public static final int TPA_PRICES_AND_YTD_TAB = 64140;
	public static final int TPA_PERFORMANCE_AND_FEES_TAB = 64141;
	public static final int TPA_STANDARD_DEVIATION_TAB = 64142;
	public static final int TPA_FUND_CHARACTERISTICS_1_TAB = 64143;
	public static final int TPA_FUND_CHARACTERISTICS_2_TAB = 64144;
	public static final int TPA_MORNINGSTAR_TAB = 64145;
	public static final int TPA_FUND_CHECK_TAB = 64146;

	// FootNotes For Morningstar Tab
	public static final int PERFORMANCE_DISCLOSURE = 64078;
	public static final int MORNINGSTAR_TAB_FOOTNOTE = 77505;
	public static final String MORNINGSTAR_FOOTNOTE = "77505";
	public static final String MORNINGSTAR_3_5_10_YR_FOOTNOTE = "77502";
	public static final String MORNINGSTAR_3_5_YR_FOOTNOTE = "77503";
	public static final String MORNINGSTAR_3_YR_FOOTNOTE = "77504";
	

	/* ###### TPA - Funds & Performance -- */

	/* -------------- Fund Evaluator --------------- */
	public static final int INVESTMENT_OPTIONS = 64492;
	public static final int SUB_ADVISOR = 64493;
	public static final int INCEPTION_DATE = 64931;
	public static final int SINCE_INCEPTION = 64931;
	public static final int PERFORMANCE_ASOF_MONTHEND = 64929;
	public static final int PERFORMANCE_ASOF_YEAREND = 64929;
	public static final int EXPENSE_RATIO = 64496;
	public static final int MORNING_STAR_BENCHMARK_CATEGORY = 64497;
	public static final int GENERAL_FOOTNOTES_BY_SITE = 65827;
	public static final int RISK_DISCLOSURES_BY_SITE = 65816;
	public static final int IMPORTANT_NOTES_NOT_RANKED = 77606;
	
	//Contract Investment Administration Form
	public static final int CIA_FRW_IS_NML = 68302;
	public static final int CIA_FRW_IS_NOT_NML = 68300;
	public static final int CIA_FORM_FOR_TPA= 56896;

	/* --------------- Uncashed Checks ----------------- */
	public static final int MISCELLANEOUS_UNCASHED_CHECKS_LINK = 70922;
	public static final int MISCELLANEOUS_UNCASHED_CHECKS_DESCRIPTION = 70923;
	public static final int MISCELLANEOUS_NO_UNCASHED_CHECKS_AVAILABLE = 70911;
	public static final int MISCELLANEOUS_CONTRACT_STATUS_DISCONTINUE = 70909;
	public static final int MISCELLANEOUS_NON_DB_AND_NON_DI_CONTRACT = 70908;
	public static final int MISCELLANEOUS_DB_AND_NON_DI_CONTRACT = 70910;

	// LC Fund related content
	public static final String RETIREMENT_INTRO_DISCLOSURES_ID="64924";
	
	// Withdrawals on Web
	public static final int MISCELLANEOUS_PBA_NOTIFICATION = 77004;

	public static final String TYPE_FEEDISCLOSURE = "FeeDisclosures";

	// IPSR
	public static final int IPS_REDEMTION_FEES_CURRENT_FUND_INSTRUCTION_ICON_TEXT = 79352;
	public static final int IPS_NOT_ANALYZED_CURRENT_FUND_INSTRUCTION_ICON_TEXT = 79353;
	public static final int IPS_CLOSED_CURRENT_FUND_INSTRUCTION_ICON_TEXT = 79354;
	public static final int IPS_DESELECTED_CURRENT_FUND_INSTRUCTION_ICON_TEXT = 79355;
	public static final int IPS_DIO_CURRENT_FUND_INSTRUCTION_ICON_TEXT = 79356;
	public static final int IPS_DIFFERENT_FSW_ASSETCLS_CURRENT_FUND_INSTRUCTION_ICON_TEXT = 79357;
	public static final int IPS_DIFFERENT_NON_FSW_ASSETCLS_CURRENT_FUND_INSTRUCTION_ICON_TEXT = 79358;

	public static final int IPS_REDEMTION_FEES_TOP_RANKED_FUND_INSTRUCTION_ICON_TEXT = 79359;
	public static final int IPS_CLOSED_TOP_RANKED_FUND_INSTRUCTION_ICON_TEXT = 79360;
	public static final int IPS_CLOSED_NEW_BUSINESS_TOP_RANKED_FUND_INSTRUCTION_ICON_TEXT = 79361;
	public static final int IPS_DIO_TOP_RANKED_INSTRUCTION_ICON_TEXT = 79362;
	public static final int IPS_DIFFERENT_FSW_ASSETCLS_TOP_RANKED_FUND_INSTRUCTION_ICON_TEXT = 79363;
	public static final int IPS_DIFFERENT_NON_FSW_ASSETCLS_TOP_RANKED_FUND_INSTRUCTION_ICON_TEXT = 79364;

	// LIA Withdrawal Messages
	public static final int MISCELLANEOUS_WITHDRAWAL_AMOUNT_FOR_LIA = 83189;
	
	// FEE WAIVER DISCLOSURE TEXT
	public static final String FEE_WAIVER_DISCLOSURE_ID = "90936";

}
