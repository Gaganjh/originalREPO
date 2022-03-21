package com.manulife.pension.bd.web;


import com.manulife.pension.platform.web.PdfConstants;


/**
 * This java class will have all the BD related Constants used for PDF Generation functionality of Reports. 
 * 
 * @author Ramkumar
 *
 */
public class BDPdfConstants extends PdfConstants {

	//LayoutBean Paths
    public static final String BLOCK_OF_BUSINESS_ACTIVE_TAB_PATH = "/bob/blockOfBusinessActive.jsp";
    public static final String BLOCK_OF_BUSINESS_OUTSTANDING_PROPOSALS_TAB_PATH = "/bob/blockOfBusinessOutstanding.jsp";
    public static final String BLOCK_OF_BUSINESS_PENDING_TAB_PATH = "/bob/blockOfBusinessPending.jsp";
    public static final String BLOCK_OF_BUSINESS_DISCONTINUED_TAB_PATH = "/bob/blockOfBusinessDiscontinued.jsp";
    public static final String BROKER_LISTING_PATH = "/brokerListing/brokerListing.jsp";
	public static final String TXN_DETAILS_LOAN_REPAYMENT_PATH = "/transaction/loanRepaymentTransactionReport.jsp";
	public static final String CONTRACT_CASH_ACCOUNT_PATH = "/transaction/cashAccountReport.jsp";
	public static final String PPT_TXN_DETAILS_CONTRIB_PATH = "/transaction/participantContributionDetailsReport.jsp";
	public static final String PPT_CONTRIB_ADJ_DETAILS_PATH = "/transaction/participantContribAdjDetailsReport.jsp";
	public static final String CONTRIB_TXN_DETAILS_PATH = "/transaction/contributionTransactionReport.jsp";
	public static final String FTF_DETAILS_PATH = "/transaction/fundToFundTransactionReport.jsp";
	public static final String WITHDRAWAL_DETAILS_PATH = "/transaction/withdrawalTransactionReport.jsp";
	public static final String REBALANCE_DETAILS_PATH = "/transaction/transactionDetailsRebalReport.jsp";
	public static final String PPT_TXN_HISTORY_PATH = "/transaction/participantTransactionHistory.jsp";
	public static final String DEFERRAL_CHANGE_DETAILS_PATH = "/transaction/deferralChangeDetailsReport.jsp";
	public static final String INVESTMENT_ALLOCATION_DETAILS_PATH = "/investment/investmentAllocationDetailsReport.jsp";
	public static final String INVESTMENT_ALLOCATION_PATH = "/investment/investmentAllocationReport.jsp";
	public static final String COFIDUCIARY_FUND_RECOMMENDATION_PATH = "/investment/cofiduciaryReviewScreen.jsp";
	public static final String PPT_ACCOUNT_PATH = "/participant/participantAccount.jsp";
    public static final String PPT_ACCOUNT_AFTER_TAX_MONEY_TAB_WEB_INF_PATH = "/WEB-INF/participant/participantAccountNetContribEarnings.jsp";
    public static final String PPT_ACCOUNT_MONEY_TYPE_SUMMARY_PATH = "/participant/participantAccountMoneyTypeSummary.jsp";
    public static final String PPT_ACCOUNT_MONEY_TYPE_DETAILS_PATH = "/participant/participantAccountMoneyTypeDetails.jsp";
    public static final String PPT_ACCOUNT_NET_DEFERRAL_PATH = "/participant/participantAccountNetDeferral.jsp";
    public static final String PPT_ACCOUNT_NET_CONTRIB_EARNINGS_PATH = "/participant/participantAccountNetContribEarnings.jsp";
    public static final String CONTRACT_INFORMATION_PATH = "/contract/contractInformation.jsp";
    public static final String REGULATOTY_DISCLOSURE_PATH = "/contract/regulatoryDisclosuresList.jsp";
    public static final String CONTRACT_TXN_HISTORY_PATH = "/transaction/transactionHistoryReport.jsp";
    public static final String EZINCREASE_SETTINGS_CHANGE_PATH = "/transaction/ezIncreaseSettingsChangeDetailsReport.jsp";
    public static final String DB_ACCOUNT_PATH = "/participant/definedBenefitAccount.jsp";
    public static final String DB_ACCOUNT_MONEY_TYPE_SUMMARY_PATH = "/participant/definedBenefitAccountMoneyTypeSummary.jsp";
    public static final String PPT_SUMMARY_REPORT_PATH = "/participant/participantSummaryReport.jsp";
    public static final String LOAN_REPAYMENT_DETAILS_PATH = "/transaction/loanRepaymentDetailsReport.jsp";
    public static final String CLASS_CONVERSION_PATH = "/transaction/classConversionTransactionReport.jsp";
    public static final String CURRENT_LOAN_SUMMARY_PATH = "/transaction/loanSummaryReport.jsp";
    public static final String PERFORMANCE_CHART_PATH = "/investment/performanceChartResults.jsp";
    public static final String BENEFIT_BASE_PATH = "/participant/participantBenefitBaseInformation.jsp";
    public static final String RATE_OF_RETURN_CALCULATOR_PATH = "/contract/rorCalculator.jsp";
    public static final String RATE_OF_RETURN_CALCULATOR_DB_PATH = "/contract/rorCalculator_db.jsp";
    public static final String SDU_HISTORY_PATH ="/secureDocumentUpload/sduHistoryTab.jsp";
    public static final String SDU_SUBMIT_PATH ="/secureDocumentUpload/sduSubmitTab.jsp";
  //Systematic WD report
    public static final String SWD_SUMMARY = "swdSummary";
	public static final String WD_DETAILS = "wdDetails";
	public static final String WD_DETAIL = "wdDetail";
	 public static final String WD_STATUS = "wdStatus";
	 public static final String WD_TYPE = "wdType";
	 public static final String SET_DATE = "setDate";
	 public static final String CALC_METHOD = "calcMethod";
	 public static final String WD_FREQ = "frequency";
	 public static final String LAST_PAY_DATE = "lastPayDate";
	 public static final String LAST_PAY_AMT = "lastPayAmount";
	 public static final String LAST_NAME_LABEL = "lastName";
	 public static final String WD_STATUS_LABEL = "withdrawalStatus";
	 public static final String WD_TYPE_LABEL = "withdrawalType";
	 public static final String SSN_LABEL = "SSN";
	 public static final String CONTRACT = "Contract";

	 
    public static final String SYS_WITHDRAW_PATH = "/transaction/systematicWithdrawalReport.jsp";
	//Transaction Details Loan Summary Report.
	public static final String TXN_DETAILS_LOAN_REPAYMENT = "txnDetailsLoanRepayment";
	public static final String TOTAL_REPAYMENT_AMT = "totalRepaymentAmt";
	public static final String TOTAL_PRINCIPAL = "totalPrincipal";
	public static final String TOTAL_INTEREST = "totalInterest";
	public static final String TXN_DETAILS = "txnDetails";
	public static final String CONTRACT_TXN_DETAILS = "contracTxnDetails";
	public static final String TXN_DETAIL = "txnDetail";
	public static final String CONTRACT_TXN_DETAIL = "contracTxnDetail";
	public static final String PPT_LOAN_NUMBER = "pptLoanNumber";
	public static final String PPT_REPAYMENT_AMT = "pptRepaymentAmt";
	public static final String PPT_PRINCIPAL = "pptPrincipal";
	public static final String PPT_INTEREST = "pptInterest";
	
	//Participant Transaction Details Contribution Report
	public static final String PPT_TXN_DETAILS_CONTRIB = "pptTxnContribution";
	public static final String PAYROLL_END_DATE = "payrollEndDate";
	public static final String CONTRIB_DATE = "contributionDate";
	public static final String CONTRIB_TYPE = "contributionType";
	public static final String CONTRIB_EE_AMT = "contributionEEAmt";
	public static final String CONTRIB_ER_AMT = "contributionERAmt";
	public static final String TOTAL_AMT = "totalAmt";
	public static final String FUND_DETAILS = "fundDetails";
	public static final String FUND_DETAIL = "fundDetail";
	public static final String FUND_GROUP = "fundGroup";
	public static final String FUND_TXN = "fundTxn";
	public static final String MONEY_TYPE = "moneyType";
	public static final String FUND_AMT = "fundAmt";
	public static final String PS_UNIT_VALUE = "psUnitValue";
	public static final String PS_NUM_OF_UNITS = "psNumberOfUnits";
	public static final String TXN_TOTAL_AMT = "txnTotalAmt";
	public static final String IS_GUARANTEED_ACCOUNT = "isGuaranteedAccount";
	
	//Contract Cash Account Report
	public static final String CONTRACT_CASH_ACCOUNT = "contractCashAccount";
	public static final String CURRENT_BALANCE_AS_OF_DATE = "currentBalanceAsOfDate";
	public static final String CURRENT_BALANCE = "currentBalance";
	public static final String OPENING_BALANCE = "openingBalance";
	public static final String CLOSING_BALANCE = "closingBalance";
	public static final String TOTAL_DEBITS = "totalDebits";
	public static final String TOTAL_CREDITS = "totalCredits";
	public static final String TXN_TYPE_DESCRIPTION1 = "txnTypeDescription1";
	public static final String TXN_TYPE_DESCRIPTION2 = "txnTypeDescription2";
	public static final String TXN_DESCRIPTIONS = "txnDescriptions";
	public static final String TXN_DESCRIPTION = "txnDescription";
	public static final String TXN_DEBITS = "txnDebits";
	public static final String TXN_CREDITS = "txnCredits";
	public static final String TXN_RUNNING_BALANCE = "txnRunningBalance";
	
	//Participant Contribution Adjustment Report
	public static final String PPT_TXN_DETAILS_CONTRIB_ADJ = "pptTxnContributionAdjustment";
	
	//Contribution Transaction Report
	public static final String CONTRIB_TXN = "contributionTransaction";
	public static final String INVESTED_DATE = "investedDate";
	public static final String MONEY_TYPES = "moneyTypes";
    public static final String MONEY_TYPE_LEFT = "moneyTypeLeft";
    public static final String MONEY_TYPE_RIGHT = "moneyTypeRight";
    public static final String MONEY_TYPE_DESC = "moneyTypeDesc";
    public static final String MONEY_AMT = "moneyAmt";
    
    //Withdrawal Detail Transaction Report
    public static final String WITHDRAWAL_DATE = "withdrawalDate";
    public static final String TRANSACTION_NUMBER = "transactionNumber";
    public static final String TRANSACTION_TYPE = "transactionType";
    public static final String TRANSACTION_TYPE_DECS = "transactionTypeDescription";
    public static final String PARTICIPANT_NAME = "name";
    public static final String PARTICIPANT_SSN = "ssn";
    public static final String PAYMENT_AMOUNT = "paymentAmount";
    public static final String PAYEE_DETAILS = "payeeDetails";
    public static final String WITHDRAWAL_TXN = "withdrawalTxn";
    public static final String COMMON_INDICATOR = "commonIndicator";
    public static final String PAYEE_TXN = "payeeTxn";
    public static final String WITHDRAWAL_CALC = "withdrawalCalc";
    
    
    public static final String AFTER_TAX_ROTH = "afterTaxRoth";
    public static final String AFTER_TAX_MTY = "afterTaXMoneyType";
    public static final String AFTER_TAX_CONTRB_AMT = "afterTaxContrAmt";
    public static final String AFTER_TAX_EARN_AMT = "afterTaxEarnAmt";
    public static final String AFTER_TAX_WD_AMT = "afterTaxWdAmt";
    
    // Changes for LIA(Life Income Amount)
	public static final String LIA_SELECTION_DATE_LABEL = "liaSelectionDateLabel";
	public static final String SPOUSAL_OPTION_LABEL = "spousalOptionLabel";
	public static final String LIA_PERCENTAGE_LABEL = "liaPercentageLabel";
	public static final String ANNUAL_LIA_AMOUNT_LABEL = "annualLiaAmountLabel";
	public static final String PAYMENT_FREQUENY_LABEL = "paymentFrequencyLabel";
	public static final String LIA_ANNIVERSARY_DATE_LABEL = "liaAnniversaryDateLabel";
	public static final String LIA_SELECTION_DATE_VALUE = "liaSelectionDateValue";
	public static final String SPOUSAL_OPTION_VALUE = "spousalOptionValue";
	public static final String LIA_PERCENTAGE_VALUE = "liaPercentageValue";
	public static final String ANNUAL_LIA_AMOUNT_VALUE = "annualLiaAmountValue";
	public static final String PAYMENT_FREQUENY_VALUE = "paymentFrequencyValue";
	public static final String LIA_ANNIVERSARY_DATE_VALUE = "liaAnniversaryDateValue";
	public static final String SHOW_LIA_SUMMARY_INFO = "showLiaSummaryInfo";
    
	public static final String LIA_WITHDRAWAL_MESSAGE_IND = "showLiaWithdrawalMessage";
    public static final String LIA_WITHDRAWAL_NOTIFICATION_IND = "showLiaWithdrawalNotification";
    public static final String LIA_WITHDRAWAL_MESSAGE = "liaWithdrawalMessage";
    public static final String LIA_WITHDRAWAL_NOTIFICATION = "liaWithdrawalNotification";
    public static final String MULTI_PAYEE_NOTIF = "multiPayee";
    public static final String LOAN_DEFAULT_NOTIF = "loanDefault";
    public static final String PBA_DEFAULT_NOTIF = "pbaDefault";
    
    public static final String DESGINATED_ROTH = "designRoth";
    public static final String PRE_87 = "pre87Roth";
    
    public static final String PYM_METHOD = "paymentMethod";
    public static final String PYM_TO = "paymentTo";
    public static final String PAYEE_ID = "payeeId";
    public static final String PAYEE_NAME = "payeeName";
    public static final String PAYEE_ADDR1 = "addressLine1";
    public static final String PAYEE_ADDR2 = "addressLine2";
    public static final String PAYEE_CITY = "city";
    public static final String PAYEE_STATE = "state";
    public static final String PAYEE_ZIP = "zip";
    public static final String PAYEE_COUNTRY = "country";
    public static final String ACC_TYPE = "accountType";
    public static final String BANK_BRANCH_NAME = "bankBranchName";
    public static final String ROUTINGABA_NUM = "routingABAnumber";
    public static final String ACC_NUMBER = "accountNumber";
    public static final String CREDIT_PAYEE_NAME = "creditPayeeName";
    
    public static final String WEB_VERSION = "webVersion";
    public static final String MULTI_PAYEE = "multiPayee";
    public static final String LOAN_IND = "loanInd";
    public static final String PBA_IND = "pbaInd";
    
    public static final String MVA_AMT = "mvaAmount";
    public static final String TAX_AMT = "taxableAmount";
    public static final String STATE_TAX_AMT = "serviceTaxAmount";
    public static final String FEDERAL_TAX_AMT = "federalTaxAmount";
    public static final String ROTH_TAX_AMT = "rothTaxableAmount";
    public static final String ROTH_STATE_TAX_AMT = "rothServiceTaxAmount";
    public static final String ROTH_FEDERAL_TAX_AMT = "rothFederalTaxAmount";
    public static final String TOTAL_PYMT_AMT = "totalPaymentAmount";
    public static final String FUND_DEPOSIT_INST = "fundsOnDepositInterest";
    public static final String FORFEITED_OR_UM_TEXT1 = "forfeitedOrUMText1";
    public static final String FORFEITED_OR_UM_TEXT2 = "forfeitedOrUMText2";
    public static final String FORFEITED_OR_UM_ER_AMOUNT = "forfeitedOrUnvestedERAmount";
    
    public static final String ROTH_IND = "rothMoneyTpeInd";
    public static final String MVA_IND = "mvaInd";
    
    public static final String VESTING_PERCT = "vestingPercentage";
    public static final String ACCT_BALANCE = "accountBalance";
    public static final String AVL_AMT = "availableAmount";
    
   
    
    //Fund to Fund Transfer Report 
    public static final String FTF_DETAILS = "fundToFundTransfer";
    public static final String WITHDRAWAL_DETAILS = "withdrawalDetail";
    public static final String REQUEST_DATE = "requestDate";
    public static final String SUBMISSION_METHOD = "submissionMethod";
    public static final String SOURCE_OF_TRANSFER = "sourceOfTransfer";
    public static final String IS_FROM_MONEY_TYPE_EXIST = "isFromMoneyTypeExist";
    public static final String SHOW_FROM_PERCENT = "showFromPercent";
    public static final String FUND_PERCENTAGE = "fundPercentage";
    public static final String FROM_FUND_DETAILS = "fromFundDetails";
    public static final String TO_FUND_DETAILS = "toFundDetails";
    public static final String TXN_TOTAL_PERCENT = "txnTotalPercent";
    
    // Rebalance Details
    public static final String REBALANCE_DETAILS = "rebalanceDetails";
    public static final String NOT_DB_CONTRACT = "notDBContract";
    public static final String BEFORE_CHANGE_FUND_DETAILS = "beforeChangeFundDetails";
    public static final String AFTER_CHANGE_FUND_DETAILS = "afterChangeFundDetails";
    public static final String FUND_EE_AMT = "fundEEAmt";
    public static final String FUND_ER_AMT = "fundERAmt";
    public static final String FUND_EE_PERCENT = "fundEEPercent";
    public static final String FUND_ER_PERCENT = "fundERPercent";
    public static final String TXN_TOTAL_EE_AMT = "txnTotalEEAmt";
    public static final String TXN_TOTAL_ER_AMT = "txnTotalERAmt";
    public static final String TXN_TOTAL_EE_PERCENT = "txnTotalEEPercent";
    public static final String TXN_TOTAL_ER_PERCENT = "txnTotalERPercent";
    public static final String SHOW_COMMENTS = "showComments";
    public static final String COMMENTS = "comments";
    
    // Participant Transaction History
    public static final String PPT_TXN_HISTORY = "pptTxnHistory";
    public static final String ASTERISK = "asterisk";
    public static final String TXN_AMT = "txnAmt";
    public static final String TXN_CHEQUE_AMT = "txnChequeAmt";
    public static final String WITHDRAWAL_AMT = "withdrawalAmt";
    public static final String DISTRIBUTION_AMT = "distributionAmt";
    public static final String WITHDRAWAL_MSG = "withdrawalMsg";
    public static final String WITHDRAWAL_TYPE = "withdrawalType";
    public static final String SHOW_MAIN_REPORT_TABLE = "showMainReportTable";
    // Participant submission  History
    public static final String SUBMISSION_NO = "submissionNo";
    public static final String SUBMISSION_DATE = "submissionDate";
    public static final String SUBMISSION_DOC_NAME = "submissionDocName";
    public static final String SUBMITTED_BY = "submittedBy";
    public static final String SUBMISSION_ROLE = "subRole";
    public static final String UPLOAD_STATUS = "uploadStatus";
     // Participant submit tab  Details
    public static final String CONTRACT_NO = "contractNo";
    public static final String SUBMISSION_NUMBER = "submissionNumber";
    public static final String SUBMISSION_TIME = "submissionTime";
    public static final String SUBMITTER_NAME = "submitterName";
    public static final String SUBMISSION_FILES = "fileNames";
    public static final String CONTRACT_NUM = "contractNum";
    
   
    
    // Deferral Change Details
    public static final String DEFERRAL_CHANGE_DETAILS = "deferralChangeDetails";
    public static final String ITEM_CHANGED = "itemChanged";
    public static final String VALUE_BEFORE = "valueBefore";
    public static final String VALUE_REQUESTED = "valueRequested";
    public static final String VALUE_UPDATED = "valueUpdated";
    public static final String STATUS = "status";
    public static final String EMPLOYMENT_STATUS = "employmentStatus";								//CL 110234
    public static final String EMPLOYMENT_STATUS_EFFECTIVE_DATE = "employmentStatusEffectiveDate";	//CL 110234
    public static final String ASOFDATECURRENTFLAG = "asOfDateCurrent";								//CL 110234
    public static final String WEB_COMMENTS = "webComments";
    public static final String GEN_SECOND_LINE = "genSecondLine";
    public static final String PROCESSED_BY_INTERNAL = "processedByInternal";
    public static final String CHANGED_BY = "changedBy";
     
    // Contract Allocation Details
    public static final String INVESTMENT_ALLOCATION_DETAILS = "investmentAllocationDetails";
    public static final String TICKER_SYMBOL = "tickerSymbol";
    public static final String FUND_CLASS = "fundClass";
    public static final String ONGOING_CONTRIB = "ongoingContrib";
    public static final String FOOTNOTE_PBA = "footnotePBA";
    public static final String NUM_OF_PPT = "noOfParticipants";
    public static final String FUND_NAME = "fundName";
    public static final String FEE_WAIVER_INDICATOR = "fwiIndicatorSymbol";
    public static final String MERRILL_RESRICTED_FUND_INDICATOR = "merrillRestrictedSymbol";
    
    public static final String PPTS_INVESTED = "participantsInvested";
    public static final String TOTAL_EE_ASSETS = "totalEEAssets";
    public static final String TOTAL_ER_ASSETS = "totalERAssets";
    public static final String TOTAL_ASSETS = "totalAssets";
    
    // Contract Investment Allocation
    public static final String INVESTMENT_ALLOCATION = "investmentAllocation";
    public static final String NO_OF_INVESTMENT_OPTION = "noOfInvestmentOption";
    public static final String ORGANIZING_OPTION = "organizingOption";
    public static final String VIEW_OPTION = "viewOption";
    public static final String FUND_SERIES_NAME = "fundSeriesName";
    public static final String REPORT_SUMMARY_DETAILS = "reportSummaryDetails";
    public static final String REPORT_SUMMARY_DETAIL = "reportSummaryDetail";
    public static final String INVESTMENT_OPTION_TEXT = "investmentOptionText";
    public static final String NO_OF_OPTIONS = "noOfOptions";
    public static final String PPT_INVESTED = "pptInvested";
    public static final String AS_OF_DATE_REPORT_CURRENT = "asOfDateReportCurrent";
    public static final String ALLOCATION_DETAILS = "allocationDetails";
    public static final String ALLOCATION_DETAIL = "allocationDetail";
    public static final String FUND_CATEGORY = "fundCategory";
    public static final String PB_FUND_CATEGORY = "pbFundCategory";
    public static final String PPTS_INVESTED_CURRENT = "pptInvestedCurrent";
    public static final String PPTS_INVESTED_FUTURE = "pptInvestedFuture";
    public static final String TOTAL_PERCENT = "totalPercent";
    public static final String GIFL_CATEGORY = "giflCategory";
   
   //FRW Regulatory disclosures
    public static final String REGULATORY_DISCLOSURES = "regulatoryDisclosures";
    public static final String REGULATORY_DISCLOSURE = "regulatoryDisclosure";
    public static final String REGULATORY_DIS_INTRO1 = "regulatoryDisIntro1";
    public static final String REGULATORY_DIS_INTRO2 = "regulatoryDisIntro2";
    
    
    // Block Of Business
    
    public static final String BLOCK_OF_BUSINESS = "blockOfBusiness";
    public static final String FILTERS_USED = "filtersUsed";
    public static final String FILTER = "filter";
    public static final String FILTER_TITLE = "filterTitle";
    public static final String FILTER_VALUE = "filterValue";
    public static final String INTERNAL_USER_INFO = "internalUserInfo";
    public static final String USER_NAME = "userName";
    public static final String BDFIRM_REP_INFO = "bdFirmRepInfo";
    public static final String ASSOCIATED_FIRM_NAMES = "associatedFirmNames";
    public static final String FIRM_NAME = "firmName";
    public static final String FINANCIAL_REP_INFO = "financialRepInfo";
    public static final String PRODUCER_CODE_AND_FIRMNAME_LIST = "producerCodeFirmNameList";
    public static final String PRODUCER_CODE_AND_FIRMNAME = "producerCodeAndFirmName";
    public static final String PRODUCER_CODE = "producerCode";
    public static final String ACTIVE_CONTRACT_ASSETS = "activeContractAssets";
    public static final String NUM_ACTIVE_CONTRACTS = "numOfActiveContracts";
    public static final String NUM_OF_LIVES = "numOfLives";
    public static final String NUM_OF_OUTSTANDING_PROPOSALS = "numOfOutstandingProposals";
    public static final String NUM_OF_PENDING_CONTRACTS = "numOfPendingContracts";
    public static final String BOB_REPORT_DETAILS = "reportDetails";
    public static final String REPORT_COLUMN_HEADER = "reportColumnHeader";
    public static final String COLUMN_HEADER_INFO = "columnHeaderInfo";
    public static final String COLUMN_HEADER_NAME = "columnHeaderName";
    public static final String COLUMN_HEADER_WIDTH = "columnHeaderWidth";
    public static final String COLUMN_RIGHT_ALIGNED = "columnRightAligned";
    public static final String COLUMN_CENTER_ALIGNED = "columnCenterAligned";
    public static final String BOB_REPORT_DETAIL = "reportDetail";
    public static final String REPORT_ROW = "reportRow";
    public static final String ROW_CELL = "rowCell";
    public static final String BOB_HISTORICAL_FOOTNOTE ="historicalFootnote";
	public static final String BOB_IS_LATEST_ASOF_DATE_SELECTED = "isLatestAsOfDateSelected";
	public static final String BOB_PN_PP_CONTRACT_CNT_ASOFLATESTDATE_FOOTNOTE = "PNPPContractCntAsOfLatestDtFootnote";
    public static final String BOB_DISCONTINUED_TAB_FOOTNOTE ="diTabFootnote";
    public static final String AB_FOOTNOTE ="ABFootnote";
    public static final String DAILY_UPDATE_FOOTNOTE ="dailyUpdateFootnote";
    public static final String RATE_OF_RETURN_CALCULATOR ="rateOfReturnCalculator";
    // Newly added footnotes
    
    public static final String BOB_ASSET_BASED_FOOTNOTE ="assetBasedFootnote";
    public static final String BOB_RIA_FOOTNOTE ="riaAssoatedFootnote";

    // Broker Listing Related Constants.
    public static final String BROKER_LISTING = "brokerListing";
    public static final String TOTAL_CONTRACT_ASSETS = "totalContractAssets";
    public static final String TOTAL_NUM_OF_CONTRACTS = "totalNumOfContracts";
    public static final String TOTAL_NUM_OF_FINANCIAL_REPS = "totalNumOfFinancialReps";

    // Participant Account
    public static final String PPT_ACCOUNT = "pptAccount";
    public static final String DEFAULT_DOB = "defaultDOB";
    public static final String PPT_GIFL_SELECT = "pptGIFLSelect";
    public static final String DEFAULT_INVESTMENT_IND = "defaultInvestmentInd";
    public static final String LAST_CONTRIB_DATE = "lastContribDate";
    public static final String ROTH_FIRST_DEPOSIT_YEAR = "rothFirstDepositYear";
    public static final String AUTO_REBALANCE_IND = "autoRebalanceInd";
    public static final String ALLOCATED_ASSETS = "allocatedAssets";
    public static final String PBA_ACCOUNT = "pbaAccount";
    public static final String LOAN_ASSETS = "loanAssets";
    public static final String PIE_CHART_URL = "pieChartURL";
    public static final String TOTAL_ASSETS_LC = "totalAssetsLC";
    public static final String TOTAL_ASSETS_AG = "totalAssetsAG";
    public static final String TOTAL_ASSETS_GI = "totalAssetsGI";
    public static final String TOTAL_ASSETS_GR = "totalAssetsGR";
    public static final String TOTAL_ASSETS_IN = "totalAssetsIN";
    public static final String TOTAL_ASSETS_CN = "totalAssetsCN";
    public static final String TOTAL_ASSETS_PB = "totalAssetsPB";
    public static final String TOTAL_PERCENT_LC = "totalPercentLC";
    public static final String TOTAL_PERCENT_AG = "totalPercentAG";
    public static final String TOTAL_PERCENT_GI = "totalPercentGI";
    public static final String TOTAL_PERCENT_GR = "totalPercentGR";
    public static final String TOTAL_PERCENT_IN = "totalPercentIN";
    public static final String TOTAL_PERCENT_CN = "totalPercentCN";
    public static final String TOTAL_PERCENT_PB = "totalPercentPB";
    public static final String COLOR_LC = "colorLC";
    public static final String COLOR_GR = "colorGR";
    public static final String COLOR_IN = "colorIN";
    public static final String COLOR_CN = "colorCN";
    public static final String COLOR_PB = "colorPB";
    public static final String COLOR_GI = "colorGI";
    public static final String COLOR_AG = "colorAG";
    
    public static final String ORGANIZED_BY = "organizedBy";
    public static final String UNIT_VALUE = "unitValue";
    public static final String NO_OF_UNITS = "noOfUnits";
    public static final String EE_CONTRIB = "eeContrib";
    public static final String MAXIMUM_HARDSHIP_AMOUNT = "maximumHardshipAmount";
    public static final String ER_CONTRIB = "erContrib";
    public static final String TOTAL_BALANCE = "totalBalance";
    public static final String EE_ER_UNIT_VALUE = "eeErUnitValue";
    public static final String EE_NO_OF_UNITS = "eeNoOfUnits";
    public static final String ER_NO_OF_UNITS = "erNoOfUnits";
    public static final String COMPOSITE_RATE = "compositeRate";
    public static final String EE_COMPOSITE = "eeComposite";
    public static final String NO_EE_COMPOSITE= "noEEComposite";
    public static final String NO_ER_COMPOSITE= "noERComposite";
    public static final String ER_COMPOSITE = "erComposite";
    public static final String EE_BALANCE = "eeBalance";
    public static final String ER_BALANCE = "erBalance";
    public static final String EE_BALANCE_FUND = "eeBalanceFund";
    public static final String ER_BALANCE_FUND = "erBalanceFund";
    
    public static final String EE_MONEY_TYPE = "eeMoneyType";
    public static final String ER_MONEY_TYPE = "erMoneyType";
    public static final String TOTAL_CONTRIB = "totalContrib";
    public static final String EE_CONTRIB_LOAN = "eeContribLoan";
    public static final String ER_CONTRIB_LOAN = "erContribLoan";
    public static final String TOTAL_CONTRIB_LOAN = "totalContribLoan";
    public static final String LOAN_BALANCE = "loanBalance";
    public static final String BALANCE = "balance";
    public static final String SHOW_LOAN_FEATURE = "showLoanFeature";
    
    
    public static final String DEFERRAL_CONTRIB_TEXT = "deferralContribText";
    public static final String NEXT_INCREASE_DATE = "nextIncreaseDate";
    public static final String NEXT_INCREASE_VALUE = "nextIncreaseValue";
    public static final String PERSONAL_RATE_LIMIT = "personalRateLimit";
    
    public static final String NET_CONTRIB = "netContrib";
    public static final String EARNINGS = "earnings";
    
    public static final String CSF_ACI_ON = "csfACIOn";
    
    //Contract Information
    public static final String CONTRACT_INFORMATION = "contractInformation";
    public static final String CONTACT_SECTION_TITLE = "contactSectionTitle";
    public static final String ADDRESS_LINE1 = "addressLine1";
    public static final String ADDRESS_LINE2 = "addressLine2";
    public static final String CITY = "city";
    public static final String STATE_CODE = "stateCode";
    public static final String ZIP_CODE = "zipCode";
    public static final String COMPLETE_STATE_CODE = "completeStateCode";
    public static final String GENERAL_PHONE_NO = "generalPhoneNo";
    public static final String CAR_NAME = "carName";
    public static final String PPT_TOLL_FREE_NO = "pptTollFreeNo";
    public static final String CAR_EMAIL = "carEmail";
    public static final String PPT_TOLL_FREE_SERVICE_NO_LABEL_IN_ENGLISH = "pptTollFreeServiceLabel";//For a service representative : Label
    public static final String PPT_TOLL_FREE_SERVICE_NO = "pptTollFreeServiceNo";
    public static final String PPT_TOLL_FREE_SERVICE_NO_IN_SPANISH = "pptTollFreeServiceNoinSpanish"; //For a service representative : (Spanish)
    public static final String PPT_ROLLOVER_EDUCATION_SPECIALIST_LABEL = "pptRolloverEducationSpecialistLabel"; //For a rollover education specialist Label
    public static final String PPT_ROLLOVER_EDUCATION_SPECIALIST_NO = "pptRolloverEducationSpecialistNo"; //For a rollover education specialist
    public static final String PPT_CONSOLIDATION_SPECIALIST_PHONE_LABEL = "pptConsolidationSpecialistPhoneLabel";//For a Consolidation specialist Label
    public static final String PPT_CONSOLIDATION_SPECIALIST_PHONE_NO = "pptConsolidationSpecialistPhoneNo";//For a Consolidation specialist
    public static final String PAYROLL_ALLOCATION_TITLE = "payrollAllocationTitle";
    public static final String DELIVERY_METHOD = "deliveryMethod";
    public static final String STMT_TYPE = "stmtType";
    public static final String CAR_PHONE_NO = "carPhoneNo";
    public static final String FEE_DISCLOSURE = "feeDisclosure";
    public static final String ACCESS_CHANNEL = "accessChannel";
    public static final String TPA_CONTACT_NAME = "tpaContactName";
    public static final String TPA_FIRM_NAME = "tpaFirmName";
    public static final String TPA_EMAIL = "tpaEmail";
    public static final String TPA_PHONE = "tpaPhone";
    public static final String PS_CONTACT_NAME = "psContactName";
    public static final String PS_EMAIL = "psEmail";
    public static final String PS_PHONE = "psPhone"; 
    public static final String PS_CONTACT_PHONE_NO = "psContactPhoneNo";
    public static final String ENROLL_FORM_FAX_NO = "enrollFormFaxNo";
    public static final String OTHER_FORM_FAX_NO = "otherFormFaxNo";
    public static final String ASSETS_TITLE = "assetsTitle";
    public static final String CASH_ACCOUNT_AMT = "cashAccountAmt";
    public static final String UNINVESTED_AMT = "uninvestedAmt";
    public static final String ALLOCATED_AMT = "allocatedAmt";
    public static final String PBA_ACCOUNT_AMT = "pbaAccountAmt";
    public static final String LAST_ALLOCATION_AMT = "lastAllocationAmt";
    public static final String LAST_PAYROLL_DATE = "lastPayrollDate";
    public static final String KEY_DATES_SECTION_TITLE = "keyDatesSectionTitle";
    public static final String CONTRACT_EFFECTIVE_DATE = "contractEffectiveDate";
    public static final String CONTRACT_YEAR_END_DATE = "contractYearEndDate";
    public static final String TRANSFER_DATE = "transferDate";
    public static final String STMT_DETAILS_SECTION_TITLE = "stmtDetailsSectionTitle";
    public static final String BASIS = "basis";
    public static final String LAST_PRINT_DATE = "lastPrintDate";
    public static final String PERMIT_DISPARITY = "permitDisparity";
    public static final String VESTING_SHOWN_ON_STMT = "vestingShownOnStmt";
    public static final String CONTRACT_OPTIONS_SECTION_TITLE = "contractOptionsSectionTitle";
    public static final String FEATURE = "feature";
    public static final String MANAGED_ACCOUNT = "managedAccount";
    public static final String GIFL_FEATURES_SECTION_TITLE = "giflFeaturesSectionTitle";
    public static final String GIFL_FEATURE = "giflFeature";
    public static final String SELECTED_FUNDS = "selectedFunds";
    public static final String AVAILABLE_FUNDS = "availableFunds";
    public static final String MSG_NO_DEFAULT_INVESTMENTS = "msgNoDefaultInvestments";
    public static final String SHOW_FIDUCIARY_WARRANTY = "showFudiciaryWarranty";
    public static final String SHOW_IPS_MANAGER = "showIPSManager";
    public static final String IPS_ANNUAL_REVIEW_DATE = "ipsAnnualReviewDate";
    public static final String IPS_CRITERIA_WEIGHTING = "ipsCriteriaWeighting";
    public static final String WARRANTY_MET = "warrantyMet";
    public static final String DIRECT_DEBIT_SELECTED = "directDebitSelected";
    public static final String CONSENT_DISPLAY_RKA_CONTENT = "consentDisplayRKAContent";
    public static final String MONEY_TYPES_AND_SOURCES_SECTION_TITLE = "moneyTypesAndSourcesSectionTitle";
    public static final String SHORT_NAME = "shortName";
    public static final String LONG_NAME = "longName";
    public static final String DEFAULT_INVESTMENT = "defaultInvestment";
    public static final String INVESTMENT_INSTRUCTION_TYPE = "investmentInstructionType";
    public static final String MONEY_SOURCE = "moneySource";
    public static final String ACCESS_CODE_SECTION_TITLE = "accessCodeSectionTitle";
    public static final String ACCESS_CODE_MSG = "accessCodeMsg";
    public static final String ACCESS_CODE = "accessCode";
    public static final String BLENDED_ASSET_CHARGE = "blendedAssetCharge";
    public static final String ASSET_CHARGE_AS_OF_DATE = "assetChargeAsOfDate";
    
    //Contract Transaction History
    public static final String CONTRACT_TXN_HISTORY = "contractTxnHistory";
    public static final String SUBMIT_TXN_HISTORY = "SubmitHistoryTabDetails";
    public static final String SUBMIT_TXN_HISTORY_HEADER = "Document Submissions";
    public static final String SUBMIT_TAB_DETAILS = "SubmitTabDetails";
    
    public static final String AMT_NA = "amtNA";

    //EZIncrease Settings Change
    public static final String EZINCREASE_SETTINGS_CHANGE = "ezIncreaseSettingsChange";
    public static final String VALUE_AFTER = "valueAfter";

	//Defined Benefit Account
    public static final String DB_ACCOUNT = "dbAccount";

    
    
    
    
    //Participant Summary
    public static final String PPT_SUMMARY = "pptSummary";
    public static final String EE_ASSETS_TOTAL = "eeAssetsTotal";
    public static final String ER_ASSETS_TOTAL = "erAssetsTotal";
    public static final String EE_ASSETS_AVG = "eeAssetsAvg";
    public static final String ER_ASSETS_AVG = "erAssetsAvg";
    public static final String TOTAL_ASSETS_AVG = "totalAssetsAvg";
    public static final String OUTSTANDING_LOANS = "outstandingLoans";
    public static final String OUTSTANDING_LOANS_AVG = "outstandingLoansAvg";
    public static final String PPT_FILTER = "pptFilter";
    public static final String ELIGIBILITY_DATE = "eligibilityDate";
    public static final String ROTH_MONEY = "rothMoney";
    public static final String DEFAULT_GATEWAY = "defaultGateway";
    public static final String DEFAULT_MANAGED_ACCOUNT = "defaultManagedAccount";
    public static final String CUSTOM_NAME_PHARSE = "customNamePhrase";
    public static final String CUSTOM_FIRST_NAME = "customFirstName";
    public static final String CUSTOM_DIVISION_PHARSE = "customDivisionPhrase";
    public static final String CUSTOM_ASSETS_FROM = "customAssetsFrom";
    public static final String CUSTOM_ASSETS_TO = "customAssetsTo";
    public static final String CUSTOM_GATEWAY_CHECKED = "customGatewayChecked";
    public static final String CUSTOM_MANAGED_ACCOUNT_CHECKED = "customManagedAccountChecked";
    public static final String CUSTOM_STATUS = "customStatus";
    public static final String CUSTOM_EMP_STATUS = "customEmpStatus";				//CL 110234
    public static final String QUICK_NAME_PHARSE = "quickNamePhrase";
    public static final String QUICK_FIRST_NAME = "quickFirstName";
    public static final String QUICK_ASSETS_FROM = "quickAssetsFrom";
    public static final String QUICK_ASSETS_TO = "quickAssetsTo";
    public static final String QUICK_GATEWAY_CHECKED = "quickGatewayChecked";
    public static final String QUICK_MANAGED_ACCOUNT_CHECKED = "quickManagedAccountChecked";
    public static final String QUICK_STATUS = "quickStatus";
    public static final String QUICK_EMP_STATUS = "quickEmpStatus";					//CL 110234
    public static final String QUICK_DIVISION = "quickDivision";
    public static final String FILTERS = "filters";
    public static final String QUICK_FILTER = "quickFilter";

    //Loan Repayment Details
    public static final String LOAN_REPAYMENT_DETAILS = "loanRepaymentDetails";
    public static final String LOAN_NUMBER = "loanNumber";
    public static final String INQUIRY_DATE = "inquiryDate";
    public static final String OUTSTANDING_BALANCE = "outstandingBalance";
    public static final String LAST_REPAYMENT_AMT = "lastRepaymentAmt";
    public static final String DAYS_SINCE_LAST_REPAYMENT = "daysSinceLastPayment";
    public static final String LAST_REPAYMENT_DATE = "lastRepaymentDate";
    public static final String LOAN_TYPE = "loanType";
    public static final String LOAN_REASON = "loanReason";
    public static final String TRANSFER_AMT = "transferAmt";
    public static final String TRANSFER_RATE = "transferRate";
    public static final String MATURITY_DATE = "maturityDate";
    public static final String INTEREST = "interest";
    public static final String PRINCIPAL = "principal";
    public static final String TYPE_DESC = "typeDesc";
    public static final String TOTAL_MTY_AMT = "totalMtyAmt";
    // wilange Mar2017
    public static final String AMORT_PERIOD = "amoritizationPeriod";
    public static final String ISSUE_DATE_LABEL = "issueDateLabel";
    public static final String AMOUNT = "amount";
    public static final String MONEYTYPEINFODETAILS = "moneyTypes";
    public static final String MONEYTYPEINFO = "moneyTypeInfo";
    public static final String ASOFDATE = "asOfDate";
  

	//Class Conversion Details
    public static final String CLASS_CONVERSION = "classConversion";
    public static final String TOTAL_TO_AMT = "totalToAmt";
    public static final String TO_AMT = "toAmt";
    public static final String TO_UNIT_VALUE = "toUnitValue";
    public static final String TO_NO_OF_UNITS = "toNoOfUnits";
    
    //Current Loan Summary
    public static final String CURRENT_LOAN_SUMMARY = "currentLoanSummary";
    public static final String ISSUE_DATE = "issueDate";
    public static final String ORIGINAL_LOAN_AMT = "originalLoanAmt";
    public static final String ALERT = "alert";
    
    //Performance Charting
    public static final String PERFORMANCE_CHART = "performanceChart";
    public static final String CHART_LINE_COLOR = "chartLineColor";
    public static final String START_VALUE = "startValue";
    public static final String END_VALUE = "endValue";
    public static final String LINE_CHART_URL = "lineChartURL";
    public static final String PERCENT_CHANGE = "percentChange";
    public static final String FUND_FOOTNOTES = "fundFootnotes";
    public static final String FUND_FOOTNOTE = "fundFootnote";
    
    //Benefit Base
    public static final String BENEFIT_BASE = "benefitBase";
    public static final String SELECTION_DATE = "selectionDate";
	public static final String DESELECTION_DATE = "deselectionDate";
	public static final String ACTIVATION_DATE = "activationDate";
	public static final String LAST_STEP_UP_DATE = "lastStepUpDate";
	public static final String LAST_STEP_UP_AMT = "lastStepUpAmt";
	public static final String NEXT_STEP_UP_DATE = "nextStepUpDate";
	public static final String HOLDING_PERIOD_EXP_DATE = "holdingPeriodExpDate";
	public static final String TRADE_RESTRICTION_DATE = "tradeRestrictionDate";
	public static final String SHOW_FOOTNOTE = "showFootnote";
	public static final String MARKET_VALUE = "marketValue";
	public static final String LAST_INCOME_ENHANCEMENT_RATE = "lastIncomeEnhancementRate";
	public static final String LAST_INCOME_ENHANCEMENT_DATE = "lastIncomeEnhancementDate";
	public static final String BB_CHANGE_AMT= "bbChangeAmt";
	public static final String HOLDING_PERIOD_IND = "holdingPeriodInd";
	public static final String FOOTNOTE_BB = "footnoteBB";
	public static final String MSG_NO_TXN = "msgNoTxn";
	public static final String GIFL_FOOTNOTE = "contractSummaryGiflFootnote";

	// 404 legends 
	public static final String LEGENDS = "legends";
	public static final String LEGEND = "legend";

	//IPSR
	public static final String IPS_REVIEW_RESULTS_PATH="/investment/viewIPSReviewResults.jsp";
	public static final String XSL_FILE_NAME = "ipsManager.XSLFile";
	public static final String IPS_MANAGER = "IPSManager";
	public static final String IPS_MANAGER_PATH = "/investment/ipsAndReviewDetails.jsp";
	public static final String IPS_MANAGER_SETUP_DETAILS_SECTION = "ipsManagerDetailsSectionTitle";
	public static final String IPS_CRITERIA_AND_WEIGHTINGS_SECTION = "ipsCriteriaAndWeightingsSectionTitle";
	public static final String IPS_ASSIST_SERVICE_TEXT = "ipsAssistServiceText";
	public static final String IPS_SCHEDULE_ANNUAL_REVIEW_DATE_TEXT = "ipsScheduleAnnualReviewDateText";
	public static final String IPS_SERVICE_REVIEW_DATE_TEXT = "ipsServiceReviewDateText";
	public static final String IPS_ANNUAL_REVIEW_DATE_TEXT = "ipsAnnualReviewDateText";
	public static final String IPS_CRITERIA_DESCRIPTION = "ipsCriteriaDescription";
	public static final String IPS_CRITERIA_WEIGHTING_DETAILS = "ipsCriteriaWeightingDetails";
	public static final String IPS_CRITERIA_WEIGHTING_DATA = "ipsCriteriaWeightingData";
	public static final String IPS_CRITERIA_DESC = "ipsCriteriaDesc";
	
	
	//BlockOfBusiness for ACR -PlanReviewReports
     public static final String BLOCK_OF_BUSINESS_PLAN_REVIEW_TAB_PATH = "/planReview/step2BobPlanReviewReports.jsp";
     public static final String PLAN_REVIEW_REPORT = "planReviewReport";
	 public static final String PLAN_REVIEW_SELECTED_ELEMENT = "planReviewSelectedElement";
	 public static final String PRESENTER_NAME = "presenterName";
	 public static final String COMPANY_NAME = "companyName";
	 public static final String COVER_IMAGE = "coverImage";
     public static final String LOGO_IMAGE = "logoImage";
     public static final String LOGO_IMAGE_UPLOADED = "logoImageUploaded";
    //PDF related Constants --------------END
	
	public static final String FUND_ID = "fundId";
	//public static final String FUND_NAME = "fundName";
	public static final String UNDERLYING_FUND_NETCOST = "underlyingFundNetCost";
	public static final String REVENUE_FROM_UNDERLYINGFUND = "revenueFromUnderlyingFund";
	public static final String REVENUE_FROM_SUBACCOUNT = "revenueFromSubAccount";
	public static final String TOTAL_REVENUE_USED_TOWARDS_PLANCOSTS = "totalRevenueUsedTowardsPlanCosts";
	public static final String EXPENSE_RATIO = "expenseRatio";
	public static final String REDEMPTION_FEE = "redemptionFee";
	
	// SEND service
	public static final String SEND_SERVICE_IND = "sendServiceInd";
	public static final String SEND_SERVICE_FEE = "sendServiceFee";
	public static final String SEND_SERVICE_EFFECTIVE_DATE = "sendServiceEffectiveDate";
    
}
