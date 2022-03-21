package com.manulife.pension.ps.service.report.bob.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.bob.valueobject.BlockOfBusinessReportData;
import com.manulife.pension.ps.service.report.bob.valueobject.BlockOfBusinessReportVO;
import com.manulife.pension.ps.service.report.bob.valueobject.BlockOfBusinessSummaryVO;
import com.manulife.pension.ps.service.report.bob.valueobject.BrokerInfoVO;
import com.manulife.pension.service.contract.util.SmallPlanFeature;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.report.valueobject.ReportSortList;

/**
 * This class is the DAO that will retrieve the Block Of Business report information.
 * 
 * @author harlomte
 * 
 */
public class BlockOfBusinessDAO extends BaseDatabaseDAO {
    private static final Logger logger = Logger.getLogger(BlockOfBusinessDAO.class);

    private static String className = BlockOfBusinessDAO.class.getName();

    private static String GET_BOB = "{CALL " + BROKER_DEALER_SCHEMA_NAME
            + "BLOCK_OF_BUSINESS (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

    private static String SQL_SELECT_CLASS_MEDIUM_NAME = "SELECT CLASS_MEDIUM_NAME FROM PSW100.CLASS_TYPE WHERE CLASS_ID = ?";
    
    // Database column Names
    private static String CONTRACT_NAME = "CONTRACT_NAME";

    private static String CONTRACT_ID = "CONTRACT_ID";

    private static String EFFECTIVE_DATE = "EFFECTIVE_DATE";

    private static String PLAN_REPORTING_YEAR_END_DATE = "PLAN_REPORTING_YEAR_END_DATE";

    private static String INCORPORATED_STATE_CODE = "INCORPORATED_STATE_CODE";
    
    private static String NUMBER_OF_LIVES = "NUMBER_OF_LIVES";

    private static String TOTAL_ASSET = "TOTAL_ASSET";

    private static String COMMISSION_DEPOSIT_TR = "COMMISSION_DEPOSIT_TR";

    private static String COMMISSION_DEPOSIT_REG = "COMMISSION_DEPOSIT_REG";

    private static String COMMISSION_ASSET_AB = "COMMISSION_DEPOSIT_REG";

    private static String COMMISSION_PRICE_CREDIT = "COMMISSION_PRICE_CREDIT";

    private static String BROKER_DEALER_FIRM_NAME = "BROKER_DEALER_FIRM_NAME";

    private static String RVP_FULLNAME = "RVP_FULLNAME";

    private static String PRODUCT_TYPE = "PRODUCT_TYPE";

    private static String MANULIFE_COMPANY = "MANULIFE_COMPANY";

    private static String MANULIFE_COMPANY_ID = "MANULIFE_COMPANY_ID";

    private static String CLASS_ID = "CLASS_ID";

    private static String PRODUCER_CODE_LIST = "PRODUCER_CODE_LIST";

    private static String BROKER_FULLNAME_LIST = "BROKER_FULLNAME_LIST";

    private static String FINANCIAL_REP_LAST_NAME_UCASE = "UCASE(LAST_NAME)";

    private static String FINANCIAL_REP_FIRST_NAME_UCASE = "UCASE(FIRST_NAME)";

    private static String ORGANIZATION_NAME_UCASE = "UCASE(ORGANIZATION_NAME)";

    private static String CLIENT_SHORT_NAME = "CLIENT_SHORT_NAME";

    private static String PROPOSAL_NO = "PROPOSAL_NO";

    private static String PROPOSAL_PRINTED_DATE = "PROPOSAL_PRINTED_DATE";

    private static String EXPECTED_ASSET_CHARGE_PCT = "EXPECTED_ASSET_CHARGE_PCT";

    private static String EXPECTED_FIRST_YEAR_ASSETS_AMT = "EXPECTED_FIRST_YEAR_ASSETS_AMT";

    private static String TRANS_ASSET_PRIOR_TO_CHARGES = "TRANS_ASSET_PRIOR_TO_CHARGES";

    // Discontinued Date
    private static String CONTRACT_STATUS_EFFECTIVE_DATE = "CONTRACT_STATUS_EFFECTIVE_DATE";

    private static String CONTRACT_STATUS_CODE = "CONTRACT_STATUS_CODE";
    
    // CSF related database column names
    private static String FSW_COFID_CODE = "FSW_COFID_CODE";

    private static String PBA_IND = "PERSONAL_BROKERAGE_ACCOUNT_IND";

    private static String ALLOW_LOAN_IND = "ALLOW_LOAN_IND";

    private static String GIFL_IND = "GIFL_IND";

    private static String ROTH_IND = "ROTH_IND";

    private static String EZ_INCREASE_IND = "EZ_INCREASE_IND";

    private static String EZ_START_IND = "EZ_START_IND";
    
    private static String MA_SERVICE_FEATURE_CODE = "MA_SERVICE_FEATURE_CODE";
    
    private static String SMALL_PLAN_OPTION_CODE_COL = "SMALL_PLAN_OPTION_CODE";    
   
    // private static String PLAN_CHECK_IND = "PLAN_CHECK_IND";

    // private static String EZ_GOAL_IND = "EZ_GOAL_IND";
    
    private static String FUND_FAMILY_CODE = "FUND_FAMILY_IND_LIST";
    
    private static String MONEY_MARKET_FUND_IND = "MONEY_MARKET_FUND_IND";

    private static String STABLE_VALUE_FUND_IND = "STABLE_VALUE_FUND_IND";

    private static String OTHER_FUND_IND = "OTHER_FUND_IND";
    
    private static String IPS_REVIEW_IND = "IPS_REVIEW_IND";
    
    private static String JHTC_PASSIVE_TRUSTEE_CODE = "JHTC_PASSIVE_TRUSTEE_CODE";
    
    private static String BGA_CONTRACT_IND = "BGA_CONTRACT_IND";
    
    
    private static String COFID_321_IND = "COFID_321_IND";
    

    private static String SYW_IND = "SYW_IND";
    
    private static String SEND_SERVICE = "SEND_SERVICE";
    
    private static String COFID_338_IND = "COFID_338_IND";

    private static String JHI_IND = "JHI_IND";
    
    private static String SVGIF_IND = "SVGIF_IND";
    
    private static String JH_CREDIT_PROGRAM_IND = "JH_CREDIT_PROGRAM_IND";
    
    // Column Names used in Filter Clause.
    private static String CONTRACT_ISSUED_STATE_CODE = "CONTRACT_ISSUED_STATE_CODE";
    
    private static String BROKER_DEALER_FIRM_ID_LIST = "BROKER_DEALER_FIRM_ID_LIST";

    private static String GREATER_THAN_EQUAL_SYM = " >= ";

    private static String LESS_THAN_EQUAL_SYM = " <= ";

    private static String PERCENT_SYM = "%";

    private static String EQUAL_SYM = "=";
    
    private static String OPEN_BRACKET = "(";

    private static String CLOSE_BRACKET = ")";

    private static final String Y_SYM = "Y";
    
    private static String IS_SYM = " IS"; 
    
    private static String NOT_SYM = " NOT"; 
    
    private static String NULL_SYM = " NULL"; 

    private static final String HYPHON_SYMBOL = "-";

    private static final String COMMA_SYMBOL = ",";
    
    private static final String SEMICOLON_SYMBOL = ";";

    private static final String EMPTY_STRING = "";
    
    private static final String COFID_321 = "321";
    
    private static final String COFID_338 = "338";
    
    private static String OR = " OR ";
    
    private static String AND = " AND ";
    
    private static String LIKE = " LIKE ";

    private static String BETWEEN = " BETWEEN ";
    
    private static String ACTIVE_TAB = "AC";

    private static String OUTSTANDING_PROPOSALS_TAB = "PP";

    private static String PENDING_TAB = "PN";

    private static String DISCONTINUED_TAB = "DI";
    
    private static String AESTRIK = "*";

    
    // Summary section related constants.
    public static final String USER_FULLNAME = "USER_FULLNAME";

    public static final String PRODUCER_CODE = "PRODUCER_CODE";

    public static final String SUM_BROKER_DEALER_FIRM_NAME = "BROKER_DEALER_FIRM_NAME";

    public static final String BROKER_FULLNAME = "BROKER_FULLNAME";
    
    public static final String BROKER_FIRST_NAME = "FIRST_NAME";
    
    public static final String BROKER_LAST_NAME = "LAST_NAME";
    
    public static final String FR_ADDRESS_LINE1 = "FR_ADDRESS_LINE1";
    
    public static final String FR_ADDRESS_LINE2 = "FR_ADDRESS_LINE2";
    
    public static final String FR_ADDRESS_LINE3 = "FR_ADDRESS_LINE3";
    
    public static final String FR_CITY_NAME = "CITY_NAME";
    
    public static final String FR_STATE_CODE = "STATE_CODE";
    
    public static final String FR_ZIP_CODE = "ZIP_CODE";

    public static final String SUM_TOTAL_ASSET = "TOTAL_ASSET";

    public static final String ACTIVE_CONTRACT_COUNT = "ACTIVE_CONTRACT_COUNT";

    public static final String ACTIVE_PENDING_CONTRACT_LIVES = "ACTIVE_PENDING_CONTRACT_LIVES";

    public static final String ACTIVE_CONTRACT_LIVES = "ACTIVE_CONTRACT_LIVES";
    
    public static final String OUTSTANDING_PROPOSAL_TOT_ASSET = "OUTSTANDING_PROPOSAL_TOT_ASSET";

    public static final String OUTSTANDING_PROPOSAL_COUNT = "OUTSTANDING_PROPOSAL_COUNT";

    public static final String OUTSTANDING_PROPOSAL_LIVES = "OUTSTANDING_PROPOSAL_LIVES";

    public static final String PENDING_CONTRACT_TOTAL_ASSET = "PENDING_CONTRACT_TOTAL_ASSET";
    
    public static final String PENDING_CONTRACT_COUNT = "PENDING_CONTRACT_COUNT";

    public static final String PENDING_CONTRACT_LIVES = "PENDING_CONTRACT_LIVES";
    
    public static final String DI_CONTRACT_COUNT = "DI_CONTRACT_COUNT";

	//Detailed Account Report extra columns
    public static final String COMMISSION_ASSET_1YR = "COMMISSION_ASSET_1YR";
    
    public static final String COMMISSION_ASSET_REN = "COMMISSION_ASSET_REN";
    
    public static final String COMMISSION_DEPOSIT_REG_1YR = "COMMISSION_DEPOSIT_REG_1YR";
    
    public static final String COMMISSION_DEPOSIT_REG_REN = "COMMISSION_DEPOSIT_REG_REN";
    
    public static final String COMMISSION_DEPOSIT_TR_1YR = "COMMISSION_DEPOSIT_TR_1YR";
    
    public static final String COMMISSION_DEPOSIT_TR_REN = "COMMISSION_DEPOSIT_TR_REN";
    
    public static final String COMMISSION_ASSET_ALL_YRS = "COMMISSION_ASSET_ALL_YRS";
    
    private static final String RIA_FLAT_PRORATA_FEE_AMT = "RIA_FLAT_PRORATA_FEE_AMT";

	private static final String RIA_FLAT_PER_HEAD_FEE_AMT = "RIA_FLAT_PER_HEAD_FEE_AMT";

	private static final String RIA_ASSET_BASED_BPS_FEE_CALC_AMT = "RIA_FEE_TOTAL";

	private static final String RIA_ASSET_BASED_BPS_MAX_FEE_AMT = "RIA_ASSET_BASED_BPS_MAX_FEE_AMT";

	private static final String RIA_ASSET_BASED_BLENDED_FEE_AMT = "RIA_ASSET_BASED_BLENDED_FEE_AMT";

	private static final String RIA_ASSET_BASED_TIERED_FEE_AMT = "RIA_ASSET_BASED_TIERED_FEE_AMT";
	
	private static final String RIA_ASSET_BASED_BPS_MIN_FEE_PCT = "RIA_ASSET_BASED_BPS_MIN_FEE_PCT";
	
	private static final String RIA_ASSET_BASED_FEE_MONTHLY_MIN_AMT = "RIA_ASSET_BASED_FEE_MONTHLY_MIN_AMT";
	
	private static final String RIA_ASSET_BASED_BPS_ANNUAL_MAX_AMT = "RIA_ASSET_BASED_BPS_ANNUAL_MAX_AMT";

	private static final String COFID_ASSET_BASED_BPS_MIN_FEE_PCT = "COFID_ASSET_BASED_BPS_MIN_FEE_PCT";
	
	private static final String COFID_ASSET_BASED_FEE_MONTHLY_MIN_AMT = "COFID_ASSET_BASED_FEE_MONTHLY_MIN_AMT";

	private static final String COFID_DOLLAR_BASED_PRORATA_FEE_AMT = "COFID_DOLLAR_BASED_PRORATA_FEE_AMT";
	
	private static final String COFIDUCIARY_FEE_FEATURE_TYPE_CODE = "COFIDUCIARY_FEE_FEATURE_TYPE_CODE";
	
	private static final String PRODUCER_ROLE_CODE = "PRODUCER_ROLE_CODE";
	
    public static final String FR_CONTRACT_SHARE_LIST = "FR_CONTRACT_SHARE_LIST"; 
    
    public static final String TRUSTEE_NAME = "TRUSTEE_NAME";

    private static String PRODUCT_ID = "PRODUCT_ID";
    
    private static String PINPOINT_IND = "PINPOINT_IND";
    
    private static String RIA_FIRM_NAME = "RIA_FIRM_NAME";
    
    private static String RIA_FEE_PAID_BY_JH = "RIA_FEE_PAID_BY_JH";
    
    private static String RIA_FEE_PAID_BY_PLAN = "RIA_FEE_PAID_BY_PLAN";
    
    private static String RIA_FEE_TOTAL = "RIA_FEE_TOTAL";

    private static Map<String, Map<String, Boolean>> applicableColumnsMap;

    private static Map<String, Boolean> applicableColumnsMapForTab;
    
    private static Map<String, String> fieldToColumnMap;
    
    private static Map<String, String> csfFieldToColumnMap;
    
    private static String SQL_COFID_SORT_CLAUSE = "(CASE WHEN COFIDUCIARY_FEE_FEATURE_TYPE_CODE = ''{0}'' AND {1} IS NOT NULL THEN {1}  ELSE 0 END )";
    
    private static String SQL_RIA_SORT_CLAUSE = "(CASE WHEN RIA_COUNT = 1 AND {0} IS NOT NULL THEN {0} ELSE 0 END )";

    static {

        /**
         * This HashMap is used to map a given field Name to a "Database Column Name".
         */
        fieldToColumnMap = new HashMap<String, String>();
        fieldToColumnMap.put(BlockOfBusinessReportData.COL_CONTRACT_NAME_ID, CONTRACT_NAME);
        fieldToColumnMap.put(BlockOfBusinessReportData.COL_PROPOSAL_NAME_ID, CLIENT_SHORT_NAME);
        fieldToColumnMap.put(BlockOfBusinessReportData.COL_CONTRACT_NUMBER_ID, CONTRACT_ID);
        fieldToColumnMap.put(BlockOfBusinessReportData.COL_PROPOSAL_NUMBER_ID, PROPOSAL_NO);
        fieldToColumnMap.put(BlockOfBusinessReportData.COL_CONTRACT_EFF_DT_ID, EFFECTIVE_DATE);
        fieldToColumnMap.put(BlockOfBusinessReportData.COL_PROPOSAL_DT_ID, PROPOSAL_PRINTED_DATE);
        fieldToColumnMap.put(BlockOfBusinessReportData.COL_CONTRACT_PLAN_YEAR_END_MMDD_ID, PLAN_REPORTING_YEAR_END_DATE);
        fieldToColumnMap.put(BlockOfBusinessReportData.COL_CONTRACT_STATE_ID, CONTRACT_ISSUED_STATE_CODE);
        fieldToColumnMap.put(BlockOfBusinessReportData.COL_NUM_OF_LIVES, NUMBER_OF_LIVES);
        fieldToColumnMap.put(BlockOfBusinessReportData.COL_TOTAL_ASSETS_ID, TOTAL_ASSET);
        fieldToColumnMap.put(BlockOfBusinessReportData.COL_TRANSFERREDOUT_ASSETS_PRIORTO_CHARGES_ID, TRANS_ASSET_PRIOR_TO_CHARGES);
        fieldToColumnMap.put(BlockOfBusinessReportData.COL_ASSET_CHARGE_ID, EXPECTED_ASSET_CHARGE_PCT);
        fieldToColumnMap.put(BlockOfBusinessReportData.COL_EXPECTED_FIRST_YEAR_ASSETS_ID, EXPECTED_FIRST_YEAR_ASSETS_AMT);
        fieldToColumnMap.put(BlockOfBusinessReportData.COL_COMMISSIONS_DEPOSIT_TR_ID, COMMISSION_DEPOSIT_TR);
        fieldToColumnMap.put(BlockOfBusinessReportData.COL_COMMISSIONS_DEPOSIT_REG_ID, COMMISSION_DEPOSIT_REG);
        fieldToColumnMap.put(BlockOfBusinessReportData.COL_COMMISSIONS_ASSET_AB_ID, COMMISSION_ASSET_AB);
        fieldToColumnMap.put(BlockOfBusinessReportData.COL_COMMISSIONS_PRICE_CREDIT_ID, COMMISSION_PRICE_CREDIT);
        fieldToColumnMap.put(BlockOfBusinessReportData.COL_PRODUCER_CODES_OF_BROKERS_ID, PRODUCER_CODE_LIST);
        fieldToColumnMap.put(BlockOfBusinessReportData.COL_NAMES_OF_THE_BROKERS_ID, BROKER_FULLNAME_LIST);
        fieldToColumnMap.put(BlockOfBusinessReportData.COL_BDFIRM_NAME_ID, BROKER_DEALER_FIRM_NAME);
        fieldToColumnMap.put(BlockOfBusinessReportData.COL_RVP_ID, RVP_FULLNAME);
        fieldToColumnMap.put(BlockOfBusinessReportData.COL_PRODUCT_TYPE_ID, PRODUCT_TYPE);
        fieldToColumnMap.put(BlockOfBusinessReportData.COL_US_OR_NY_ID, MANULIFE_COMPANY);
        fieldToColumnMap.put(BlockOfBusinessReportData.COL_CLASS_ID, CLASS_ID);
        fieldToColumnMap.put(BlockOfBusinessReportData.COL_DISCONTINUED_DATE_ID, CONTRACT_STATUS_EFFECTIVE_DATE);// discontinued date.
        fieldToColumnMap.put(BlockOfBusinessReportData.COMMISSION_ASSET_1YR, COMMISSION_ASSET_1YR);
        fieldToColumnMap.put(BlockOfBusinessReportData.COMMISSION_ASSET_REN,COMMISSION_ASSET_REN);
        fieldToColumnMap.put(BlockOfBusinessReportData.COMMISSION_DEPOSIT_REG_1YR,COMMISSION_DEPOSIT_REG_1YR);
        fieldToColumnMap.put(BlockOfBusinessReportData.COMMISSION_DEPOSIT_REG_REN,COMMISSION_DEPOSIT_REG_REN);
        fieldToColumnMap.put(BlockOfBusinessReportData.COMMISSION_DEPOSIT_TR_1YR,COMMISSION_DEPOSIT_TR_1YR);
        fieldToColumnMap.put(BlockOfBusinessReportData.COMMISSION_DEPOSIT_TR_REN,COMMISSION_DEPOSIT_TR_REN);
        fieldToColumnMap.put(BlockOfBusinessReportData.COMMISSION_ASSET_ALL_YRS, COMMISSION_ASSET_ALL_YRS);
        fieldToColumnMap.put(BlockOfBusinessReportData.RIA_FLAT_PRORATA_FEE_AMT, RIA_FLAT_PRORATA_FEE_AMT);
        fieldToColumnMap.put(BlockOfBusinessReportData.RIA_FLAT_PER_HEAD_FEE_AMT, RIA_FLAT_PER_HEAD_FEE_AMT);
        fieldToColumnMap.put(BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_FEE_AMT, RIA_ASSET_BASED_BPS_FEE_CALC_AMT);
        fieldToColumnMap.put(BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_MAX_FEE_AMT, RIA_ASSET_BASED_BPS_MAX_FEE_AMT);
        fieldToColumnMap.put(BlockOfBusinessReportData.RIA_ASSET_BASED_BLENDED_FEE_AMT, RIA_ASSET_BASED_BLENDED_FEE_AMT);
        fieldToColumnMap.put(BlockOfBusinessReportData.RIA_ASSET_BASED_TIERED_FEE_AMT, RIA_ASSET_BASED_TIERED_FEE_AMT);
        fieldToColumnMap.put(BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_MIN_FEE_AMT, RIA_ASSET_BASED_BPS_MIN_FEE_PCT);
        fieldToColumnMap.put(BlockOfBusinessReportData.COFID_321_ASSET_BASED_BPS_FEE, COFID_ASSET_BASED_BPS_MIN_FEE_PCT);
        fieldToColumnMap.put(BlockOfBusinessReportData.COFID_321_DOLLAR_BASED_FEE_AMT, COFID_DOLLAR_BASED_PRORATA_FEE_AMT);
        fieldToColumnMap.put(BlockOfBusinessReportData.COFID_338_ASSET_BASED_BPS_FEE, COFID_ASSET_BASED_BPS_MIN_FEE_PCT);
        fieldToColumnMap.put(BlockOfBusinessReportData.COFID_338_DOLLAR_BASED_FEE_AMT, COFID_DOLLAR_BASED_PRORATA_FEE_AMT);
        fieldToColumnMap.put(BlockOfBusinessReportData.RIA_338_DESIGNATION_INDICATOR, PRODUCER_ROLE_CODE);
        fieldToColumnMap.put(BROKER_FULLNAME, BROKER_FULLNAME);
        fieldToColumnMap.put(BlockOfBusinessReportData.FR_CONTRACT_SHARE_LIST, FR_CONTRACT_SHARE_LIST);
        fieldToColumnMap.put(BlockOfBusinessReportData.COL_COMMISSIONS_RIA_FEES_ID, RIA_FEE_TOTAL);
        
        /**
         * This Map is used to map the CSF values to corresponding "Database Column Name".
         */
        csfFieldToColumnMap = new HashMap<String, String>();
        csfFieldToColumnMap.put(BlockOfBusinessReportData.FIDUCIARY_WARRANTY_VALUE, FSW_COFID_CODE);
        csfFieldToColumnMap.put(BlockOfBusinessReportData.PBA_VALUE, PBA_IND);
        csfFieldToColumnMap.put(BlockOfBusinessReportData.LOANS_VALUE, ALLOW_LOAN_IND);
        csfFieldToColumnMap.put(BlockOfBusinessReportData.GIFL_VALUE, GIFL_IND);
        csfFieldToColumnMap.put(BlockOfBusinessReportData.ROTH_VALUE, ROTH_IND);
        csfFieldToColumnMap.put(BlockOfBusinessReportData.EZINCREASE_VALUE, EZ_INCREASE_IND);
        csfFieldToColumnMap.put(BlockOfBusinessReportData.EZSTART_VALUE, EZ_START_IND);
        // csfFieldToColumnMap.put(BlockOfBusinessReportData.PLAN_CHECK_VALUE, PLAN_CHECK_IND);
        // csfFieldToColumnMap.put(BlockOfBusinessReportData.EZGOALS_VALUE, EZ_GOAL_IND);
        
        //Alignment: Added as part of new fund launch
        csfFieldToColumnMap.put(BlockOfBusinessReportData.DF_FUND_FAMILY_CODE, FUND_FAMILY_CODE);
        csfFieldToColumnMap.put(BlockOfBusinessReportData.DF_MONEY_MARKET_FUNDS_VALUE, MONEY_MARKET_FUND_IND);
        csfFieldToColumnMap.put(BlockOfBusinessReportData.DF_STABLE_VALUE_FUNDS_VALUE, STABLE_VALUE_FUND_IND);
        csfFieldToColumnMap.put(BlockOfBusinessReportData.DF_OTHERS_VALUE, OTHER_FUND_IND);
        csfFieldToColumnMap.put(BlockOfBusinessReportData.IPSM_VALUE, IPS_REVIEW_IND);
        csfFieldToColumnMap.put(BlockOfBusinessReportData.JH_PASSIVE_TRUSTEE_SERVICE_VALUE, JHTC_PASSIVE_TRUSTEE_CODE);
        csfFieldToColumnMap.put(BlockOfBusinessReportData.TOTAL_CARE_VALUE, BGA_CONTRACT_IND);
        csfFieldToColumnMap.put(BlockOfBusinessReportData.WILSHIRE321_SERVICE, COFID_321_IND);
        
        csfFieldToColumnMap.put(BlockOfBusinessReportData.SYSTEMATIC_WTTHDRAWAL_SERVICE, SYW_IND);
        csfFieldToColumnMap.put(BlockOfBusinessReportData.SEND_SERVICE, SEND_SERVICE);
        csfFieldToColumnMap.put(BlockOfBusinessReportData.WILSHIRE338_SERVICE, COFID_338_IND);
        csfFieldToColumnMap.put(BlockOfBusinessReportData.MA_VALUE, MA_SERVICE_FEATURE_CODE);
        csfFieldToColumnMap.put(BlockOfBusinessReportData.SFC_VALUE, SMALL_PLAN_OPTION_CODE_COL);
        csfFieldToColumnMap.put(BlockOfBusinessReportData.PEP_VALUE, SMALL_PLAN_OPTION_CODE_COL);
        csfFieldToColumnMap.put(BlockOfBusinessReportData.UBS321_SERVICE, COFID_321_IND);
        csfFieldToColumnMap.put(BlockOfBusinessReportData.RJ_338_SERVICE, COFID_338_IND);
        //csfFieldToColumnMap.put(BlockOfBusinessReportData.JHI_INVESTMENT_SELECTION_VALUE, JHI_IND);
        //csfFieldToColumnMap.put(BlockOfBusinessReportData.JH_SVGIF_CREDIT_VALUE, SVGIF_IND);
        csfFieldToColumnMap.put(BlockOfBusinessReportData.JH_CREDITS_PROGRAM_VALUE, JH_CREDIT_PROGRAM_IND);
        /**
         * This Map is used to give maintain a list of applicable columns for a given tab.
         */
        applicableColumnsMap = new HashMap<String, Map<String, Boolean>>();

        // Applicable columns for Active tab
        applicableColumnsMapForTab = createDefaultColumnsMap();
        applicableColumnsMapForTab.put(CLIENT_SHORT_NAME, Boolean.FALSE);
        applicableColumnsMapForTab.put(PROPOSAL_NO, Boolean.FALSE);
        applicableColumnsMapForTab.put(PROPOSAL_PRINTED_DATE, Boolean.FALSE);
        applicableColumnsMapForTab.put(EXPECTED_ASSET_CHARGE_PCT, Boolean.FALSE);
        applicableColumnsMapForTab.put(EXPECTED_FIRST_YEAR_ASSETS_AMT, Boolean.FALSE);
        applicableColumnsMapForTab.put(TRANS_ASSET_PRIOR_TO_CHARGES, Boolean.FALSE);
        applicableColumnsMapForTab.put(CONTRACT_STATUS_EFFECTIVE_DATE, Boolean.FALSE);
        applicableColumnsMapForTab.put(COMMISSION_ASSET_1YR, Boolean.TRUE);
        applicableColumnsMapForTab.put(COMMISSION_ASSET_REN, Boolean.TRUE);
        applicableColumnsMapForTab.put(COMMISSION_DEPOSIT_REG_1YR, Boolean.TRUE);
        applicableColumnsMapForTab.put(COMMISSION_DEPOSIT_REG_REN, Boolean.TRUE);
        applicableColumnsMapForTab.put(COMMISSION_DEPOSIT_TR_1YR, Boolean.TRUE);
        applicableColumnsMapForTab.put(COMMISSION_ASSET_ALL_YRS, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_FLAT_PRORATA_FEE_AMT, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_FLAT_PER_HEAD_FEE_AMT, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_ASSET_BASED_BPS_FEE_CALC_AMT, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_ASSET_BASED_BPS_MAX_FEE_AMT, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_ASSET_BASED_BLENDED_FEE_AMT, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_ASSET_BASED_TIERED_FEE_AMT, Boolean.TRUE);
        applicableColumnsMapForTab.put(PRODUCER_ROLE_CODE, Boolean.TRUE);
        applicableColumnsMapForTab.put(FR_CONTRACT_SHARE_LIST, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_FIRM_NAME, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_FEE_PAID_BY_JH, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_FEE_PAID_BY_PLAN, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_FEE_TOTAL, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_ASSET_BASED_BPS_MIN_FEE_PCT, Boolean.TRUE);
        applicableColumnsMapForTab.put(COFID_ASSET_BASED_BPS_MIN_FEE_PCT, Boolean.TRUE);
        applicableColumnsMapForTab.put(COFID_DOLLAR_BASED_PRORATA_FEE_AMT, Boolean.TRUE);
        applicableColumnsMapForTab.put(COFIDUCIARY_FEE_FEATURE_TYPE_CODE, Boolean.TRUE);
        applicableColumnsMapForTab.put(SMALL_PLAN_OPTION_CODE_COL, Boolean.TRUE);
        applicableColumnsMap.put(ACTIVE_TAB, applicableColumnsMapForTab);

        // Applicable columns for Outstanding Proposal tab
        applicableColumnsMapForTab = createDefaultColumnsMap();
        applicableColumnsMapForTab.put(CONTRACT_NAME, Boolean.FALSE);
        applicableColumnsMapForTab.put(CONTRACT_ID, Boolean.FALSE);
        applicableColumnsMapForTab.put(EFFECTIVE_DATE, Boolean.FALSE);
        applicableColumnsMapForTab.put(PLAN_REPORTING_YEAR_END_DATE, Boolean.FALSE);
        applicableColumnsMapForTab.put(INCORPORATED_STATE_CODE, Boolean.FALSE);
        applicableColumnsMapForTab.put(CONTRACT_ISSUED_STATE_CODE, Boolean.FALSE);
        applicableColumnsMapForTab.put(TOTAL_ASSET, Boolean.FALSE);
        applicableColumnsMapForTab.put(TRANS_ASSET_PRIOR_TO_CHARGES, Boolean.FALSE);
        applicableColumnsMapForTab.put(CONTRACT_STATUS_EFFECTIVE_DATE, Boolean.FALSE);
        applicableColumnsMapForTab.put(CONTRACT_STATUS_CODE, Boolean.FALSE);
        applicableColumnsMapForTab.put(COMMISSION_ASSET_1YR, Boolean.TRUE);
        applicableColumnsMapForTab.put(COMMISSION_ASSET_REN, Boolean.TRUE);
        applicableColumnsMapForTab.put(COMMISSION_DEPOSIT_REG_1YR, Boolean.TRUE);
        applicableColumnsMapForTab.put(COMMISSION_DEPOSIT_REG_REN, Boolean.TRUE);
        applicableColumnsMapForTab.put(COMMISSION_DEPOSIT_TR_1YR, Boolean.TRUE);
        applicableColumnsMapForTab.put(COMMISSION_ASSET_ALL_YRS, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_FLAT_PRORATA_FEE_AMT, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_FLAT_PER_HEAD_FEE_AMT, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_ASSET_BASED_BPS_FEE_CALC_AMT, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_ASSET_BASED_BPS_MAX_FEE_AMT, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_ASSET_BASED_BLENDED_FEE_AMT, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_ASSET_BASED_TIERED_FEE_AMT, Boolean.TRUE);
        applicableColumnsMapForTab.put(PRODUCER_ROLE_CODE, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_FIRM_NAME, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_FEE_PAID_BY_JH, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_FEE_PAID_BY_PLAN, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_FEE_TOTAL, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_ASSET_BASED_BPS_MIN_FEE_PCT, Boolean.TRUE);
        applicableColumnsMapForTab.put(COFID_ASSET_BASED_BPS_MIN_FEE_PCT, Boolean.TRUE);
        applicableColumnsMapForTab.put(COFID_DOLLAR_BASED_PRORATA_FEE_AMT, Boolean.TRUE);
        applicableColumnsMapForTab.put(COFIDUCIARY_FEE_FEATURE_TYPE_CODE, Boolean.TRUE);
        applicableColumnsMapForTab.put(SMALL_PLAN_OPTION_CODE_COL, Boolean.FALSE);
        applicableColumnsMap.put(OUTSTANDING_PROPOSALS_TAB, applicableColumnsMapForTab);

        // Applicable columns for Pending tab
        applicableColumnsMapForTab = createDefaultColumnsMap();
        applicableColumnsMapForTab.put(CLIENT_SHORT_NAME, Boolean.FALSE);
        applicableColumnsMapForTab.put(EFFECTIVE_DATE, Boolean.FALSE);
        applicableColumnsMapForTab.put(PROPOSAL_NO, Boolean.FALSE);
        applicableColumnsMapForTab.put(PROPOSAL_PRINTED_DATE, Boolean.FALSE);
        applicableColumnsMapForTab.put(EXPECTED_ASSET_CHARGE_PCT, Boolean.FALSE);
        applicableColumnsMapForTab.put(EXPECTED_FIRST_YEAR_ASSETS_AMT, Boolean.FALSE);
        applicableColumnsMapForTab.put(TRANS_ASSET_PRIOR_TO_CHARGES, Boolean.FALSE);
        applicableColumnsMapForTab.put(CONTRACT_STATUS_EFFECTIVE_DATE, Boolean.FALSE);
        applicableColumnsMapForTab.put(COMMISSION_ASSET_1YR, Boolean.TRUE);
        applicableColumnsMapForTab.put(COMMISSION_ASSET_REN, Boolean.TRUE);
        applicableColumnsMapForTab.put(COMMISSION_DEPOSIT_REG_1YR, Boolean.TRUE);
        applicableColumnsMapForTab.put(COMMISSION_DEPOSIT_REG_REN, Boolean.TRUE);
        applicableColumnsMapForTab.put(COMMISSION_DEPOSIT_TR_1YR, Boolean.TRUE);
        applicableColumnsMapForTab.put(COMMISSION_ASSET_ALL_YRS, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_FLAT_PRORATA_FEE_AMT, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_FLAT_PER_HEAD_FEE_AMT, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_ASSET_BASED_BPS_FEE_CALC_AMT, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_ASSET_BASED_BPS_MAX_FEE_AMT, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_ASSET_BASED_BLENDED_FEE_AMT, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_ASSET_BASED_TIERED_FEE_AMT, Boolean.TRUE);
        applicableColumnsMapForTab.put(PRODUCER_ROLE_CODE, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_FIRM_NAME, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_FEE_PAID_BY_JH, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_FEE_PAID_BY_PLAN, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_FEE_TOTAL, Boolean.TRUE);
        applicableColumnsMapForTab.put(RIA_ASSET_BASED_BPS_MIN_FEE_PCT, Boolean.TRUE);
        applicableColumnsMapForTab.put(COFID_ASSET_BASED_BPS_MIN_FEE_PCT, Boolean.TRUE);
        applicableColumnsMapForTab.put(COFID_DOLLAR_BASED_PRORATA_FEE_AMT, Boolean.TRUE);
        applicableColumnsMapForTab.put(COFIDUCIARY_FEE_FEATURE_TYPE_CODE, Boolean.TRUE);
        applicableColumnsMapForTab.put(SMALL_PLAN_OPTION_CODE_COL, Boolean.FALSE);
        applicableColumnsMap.put(PENDING_TAB, applicableColumnsMapForTab);
        
        // Applicable columns for Discontinued tab
        applicableColumnsMapForTab = createDefaultColumnsMap();
        applicableColumnsMapForTab.put(NUMBER_OF_LIVES, Boolean.FALSE);
        applicableColumnsMapForTab.put(TOTAL_ASSET, Boolean.FALSE);
        applicableColumnsMapForTab.put(CLIENT_SHORT_NAME, Boolean.FALSE);
        applicableColumnsMapForTab.put(PROPOSAL_NO, Boolean.FALSE);
        applicableColumnsMapForTab.put(PROPOSAL_PRINTED_DATE, Boolean.FALSE);
        applicableColumnsMapForTab.put(EXPECTED_ASSET_CHARGE_PCT, Boolean.FALSE);
        applicableColumnsMapForTab.put(EXPECTED_FIRST_YEAR_ASSETS_AMT, Boolean.FALSE);
        applicableColumnsMapForTab.put(SMALL_PLAN_OPTION_CODE_COL, Boolean.FALSE);
        applicableColumnsMap.put(DISCONTINUED_TAB, applicableColumnsMapForTab);

    }

    /**
     * This method calls the BlockofBusiness stored proc and retrieves the Summary Information,
     * Report Information to be shown on Block of Business page.
     * 
     * @param criteria - The Filtering criteria to be sent to BOB Stored proc.
     * @return - Report Data to be shown on the JSP page.
     * @throws SystemException
     */
    @SuppressWarnings( { "unchecked", "deprecation" })
    public static ReportData getReportData(final ReportCriteria criteria) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getReportData");
            logger.debug("Report criteria -> " + criteria.toString());
        }

        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet resultSet = null;
        BlockOfBusinessReportData bobReportData = null;
        try {
            // setup the connection and the statement
            conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareCall(GET_BOB);

            if (logger.isDebugEnabled()) {
                logger.debug("Calling Stored Procedure: " + GET_BOB);
            }

            int idx = 1;

            Integer callSessionID = (Integer) criteria.getFilters().get(
                    BlockOfBusinessReportData.FILTER_DB_SESSION_ID);
            stmt.setBigDecimal(idx++, intToBigDecimal(callSessionID));

            Long userProfileID = (Long) criteria.getFilters().get(
                    BlockOfBusinessReportData.FILTER_USER_PROFILE_ID);
            stmt.setBigDecimal(idx++, new BigDecimal(userProfileID));

            String userRoleCode = (String) criteria.getFilters().get(
                    BlockOfBusinessReportData.FILTER_USER_ROLE);
            stmt.setString(idx++, userRoleCode);

            Long mimicUserProfileID = (Long) criteria.getFilters().get(
                    BlockOfBusinessReportData.FILTER_MIMIC_USER_PROFILE_ID);
            if (mimicUserProfileID == null) {
                stmt.setString(idx++, null);
            } else {
                stmt.setBigDecimal(idx++, new BigDecimal(mimicUserProfileID));
            }

            String mimicUserRoleCode = (String) criteria.getFilters().get(
                    BlockOfBusinessReportData.FILTER_MIMIC_USER_ROLE);
            stmt.setString(idx++, mimicUserRoleCode);

            ArrayList<Integer> partyIDList = (ArrayList<Integer>) criteria.getFilters().get(
                    BlockOfBusinessReportData.FILTER_PARTY_ID);

            if (partyIDList == null || partyIDList.isEmpty()) {
                stmt.setString(idx++, null);
            } else {
                StringBuffer commaSeperatedPartyIDList = new StringBuffer();
                commaSeperatedPartyIDList.append(partyIDList.get(0));
                for (Integer partyID : partyIDList.subList(1, partyIDList.size())) {
                    commaSeperatedPartyIDList.append(COMMA_SYMBOL).append(partyID);
                }
                stmt.setString(idx++, commaSeperatedPartyIDList.toString());
            }

            String contractStatusCategoryCode = (String) criteria.getFilters().get(
                    BlockOfBusinessReportData.FILTER_CONTRACT_STATUS_CODES);
            stmt.setString(idx++, contractStatusCategoryCode);

            String reportAsOfDate = (String) criteria.getFilters().get(
                    BlockOfBusinessReportData.FILTER_AS_OF_DATE);
            if (reportAsOfDate == null) {
                stmt.setDate(idx++, null);
            } else {
                stmt.setDate(idx++, new java.sql.Date(Long.valueOf(reportAsOfDate)));
            }

            if (criteria.getPageSize() == ReportCriteria.NOLIMIT_PAGE_SIZE) {
                Integer startRowNumber = 0;
                Integer endRowNumber = 0;
                stmt.setInt(idx++, startRowNumber);
                stmt.setInt(idx++, endRowNumber);
            } else {
                Integer startRowNumber = criteria.getStartIndex();
                stmt.setInt(idx++, startRowNumber);
                
                Integer endRowNumber = criteria.getStartIndex() + criteria.getPageSize() - 1;
                stmt.setInt(idx++, endRowNumber);
            }

            ReportSortList sortList = criteria.getSorts();
            if (sortList == null || sortList.size() == 0) {
                stmt.setString(idx++, null);
            } else {
                String sortPhrase = createSortPhrase(criteria);
                if (StringUtils.isEmpty(sortPhrase)) {
                    stmt.setString(idx++, null);
                } else {
                    stmt.setString(idx++, sortPhrase);
                }
                
            }
            
            String filterPhrase = createFilterPhrase(criteria);
            if (StringUtils.isEmpty(filterPhrase)) {
                stmt.setString(idx++, null);
            } else {
                stmt.setString(idx++, filterPhrase);
            }
            
            // Used by TPA BOB page.
            String csv_Report_Ind = (String) criteria.getFilterValue("csv_Report_Ind");
            if (StringUtils.isBlank(csv_Report_Ind)) {
                stmt.setString(idx++, null);
            } else {
                stmt.setString(idx++, csv_Report_Ind);
            }
            
            stmt.registerOutParameter(idx++, Types.DECIMAL);

            stmt.registerOutParameter(idx, Types.CHAR);

            // execute the stored procedure
            stmt.execute();
            
            resultSet = stmt.getResultSet();
            
            BigDecimal callSessionID_out = stmt.getBigDecimal(idx - 1);
            Integer sessionID_out = callSessionID_out == null ? null : callSessionID_out.intValue();
            
            String resultTooBigInd = stmt.getString(idx);
            
            BlockOfBusinessSummaryVO bobSummaryVO = new BlockOfBusinessSummaryVO();
            
            ArrayList<BrokerInfoVO> brokerInfoVO = new ArrayList<BrokerInfoVO>();

            if (resultSet != null) {
                while (resultSet.next()) {
                    // Result Set#1 has User Info to be shown in Summary section.

                    // If the user is a Internal User: RVP, National Accounts, BASIC, Administrator,
                    // Content Manager and not in mimic mode, then show the Internal user name.
                    // Else, show the Broker Info.
                    if (isInternalUser(userRoleCode) && mimicUserProfileID == null) {
                        bobSummaryVO.setInternalUserName(resultSet.getString(USER_FULLNAME));
                    } else {
                        BrokerInfoVO bobInfo = new BrokerInfoVO();
                        bobInfo.setProducerCode(resultSet.getString(PRODUCER_CODE));
                        bobInfo.setBdFirmName(resultSet.getString(SUM_BROKER_DEALER_FIRM_NAME));
                        bobInfo.setBrokerFullName(resultSet.getString(BROKER_FULLNAME));
                        bobInfo.setBrokerFirstName(resultSet.getString(BROKER_FIRST_NAME));
                        bobInfo.setBrokerLastName(resultSet.getString(BROKER_LAST_NAME));
                        bobInfo.setFrAddressLine1(resultSet.getString(FR_ADDRESS_LINE1));
                        bobInfo.setFrAddressLine2(resultSet.getString(FR_ADDRESS_LINE2));
                        bobInfo.setFrAddressLine3(resultSet.getString(FR_ADDRESS_LINE3));
                        bobInfo.setCityName(resultSet.getString(FR_CITY_NAME));
                        bobInfo.setStateCode(resultSet.getString(FR_STATE_CODE));
                        bobInfo.setZipCode(resultSet.getString(FR_ZIP_CODE));
                        brokerInfoVO.add(bobInfo);
                    }
                }
            }

            bobSummaryVO.setBrokerInfoVO(brokerInfoVO);

            int totalCount = 0;

            stmt.getMoreResults();
            resultSet = stmt.getResultSet();
            if (resultSet != null) {
                while (resultSet.next()) {
                    // Result Set#2 has Total Asset Count, etc to be shown in Summary section.
                    
                    // Active
                    bobSummaryVO.setActiveContractAssets(resultSet.getBigDecimal(SUM_TOTAL_ASSET));
                    bobSummaryVO.setNumOfLives(resultSet.getBigDecimal(ACTIVE_PENDING_CONTRACT_LIVES));
                    bobSummaryVO.setNumOfActiveLives(resultSet.getBigDecimal(ACTIVE_CONTRACT_LIVES));
                    BigDecimal activeContractCount = resultSet.getBigDecimal(ACTIVE_CONTRACT_COUNT);
                    bobSummaryVO.setNumOfActiveContracts(activeContractCount);
                    
                    // Outstanding
                    bobSummaryVO.setOutstandingProposalsAssets(resultSet.getBigDecimal(OUTSTANDING_PROPOSAL_TOT_ASSET));
                    bobSummaryVO.setNumOfOutstandingProposalsLives(resultSet.getBigDecimal(OUTSTANDING_PROPOSAL_LIVES));
                    BigDecimal OutstandingProposalCnt = resultSet.getBigDecimal(OUTSTANDING_PROPOSAL_COUNT);
                    bobSummaryVO.setNumOfOutstandingProposals(OutstandingProposalCnt);
                    
                    // Pending
                    bobSummaryVO.setPendingContractsAssets(resultSet.getBigDecimal(PENDING_CONTRACT_TOTAL_ASSET));
                    bobSummaryVO.setNumOfPendingContractsLives(resultSet.getBigDecimal(PENDING_CONTRACT_LIVES));
                    BigDecimal pendingContractCount = resultSet.getBigDecimal(PENDING_CONTRACT_COUNT);
                    bobSummaryVO.setNumOfPendingContracts(pendingContractCount);
                    
                    // Discontinued
                    BigDecimal discontinuedContractCount = resultSet.getBigDecimal(DI_CONTRACT_COUNT);
                    bobSummaryVO.setNumOfDiscontinuedContracts(discontinuedContractCount);
            
                    if (ACTIVE_TAB.equals(contractStatusCategoryCode)) {
                        totalCount = activeContractCount.intValue();
                    } else if (OUTSTANDING_PROPOSALS_TAB.equals(contractStatusCategoryCode)) {
                        totalCount = OutstandingProposalCnt.intValue();
                    } else if (PENDING_TAB.equals(contractStatusCategoryCode)) {
                        totalCount = pendingContractCount.intValue();
                    } else {
                        totalCount = discontinuedContractCount.intValue();
                    }
                }
            }
            
            bobReportData = new BlockOfBusinessReportData(criteria, totalCount);
            bobReportData.setDbSessionID(sessionID_out);
            
            bobReportData.setResultTooBigInd(Y_SYM.equals(resultTooBigInd) ? Boolean.TRUE
                    : Boolean.FALSE); 

            bobReportData.setBobSummaryVO(bobSummaryVO);
            
            stmt.getMoreResults();
            resultSet = stmt.getResultSet();

            List<BlockOfBusinessReportVO> bobreportVOList = new ArrayList<BlockOfBusinessReportVO>();

            if (resultSet != null) {
                while (resultSet.next()) {
                    // Result Set#3 has the actual Rows to be shown in the report.
                    BlockOfBusinessReportVO bobReportVO = new BlockOfBusinessReportVO();

                    if (getColumnStatus(contractStatusCategoryCode, CONTRACT_NAME)) {
                        bobReportVO.setContractName(resultSet.getString(CONTRACT_NAME));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, CLIENT_SHORT_NAME)) {
                        bobReportVO.setProposalName(resultSet.getString(CLIENT_SHORT_NAME));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, CONTRACT_ID)) {
                        bobReportVO.setContractNumber(resultSet.getInt(CONTRACT_ID));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, EFFECTIVE_DATE)) {
                        bobReportVO.setContractEffectiveDate(resultSet.getDate(EFFECTIVE_DATE));
                    }
                    // ContractStatusEffectiveDate is Discontinuance Date.
                    if (getColumnStatus(contractStatusCategoryCode, CONTRACT_STATUS_EFFECTIVE_DATE)) {
                        bobReportVO.setDiscontinuanceDate(resultSet
                                .getDate(CONTRACT_STATUS_EFFECTIVE_DATE));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, PLAN_REPORTING_YEAR_END_DATE)) {
                        bobReportVO.setContractPlanYearEnd(resultSet
                                .getDate(PLAN_REPORTING_YEAR_END_DATE));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, CONTRACT_ISSUED_STATE_CODE)) {
                        bobReportVO.setContractState(resultSet.getString(CONTRACT_ISSUED_STATE_CODE));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, NUMBER_OF_LIVES)) {
                        Object numOfLives = resultSet.getObject(NUMBER_OF_LIVES);
                        if (numOfLives == null) {
                            bobReportVO.setNumOfLives(null);
                        } else {
                            bobReportVO.setNumOfLives((Integer) numOfLives);
                        }
                    }
                    if (getColumnStatus(contractStatusCategoryCode, TOTAL_ASSET)) {
                        bobReportVO.setTotalAssets(resultSet.getBigDecimal(TOTAL_ASSET));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, COMMISSION_DEPOSIT_TR)) {
                        bobReportVO.setCommissionsDepositTR(resultSet
                                .getBigDecimal(COMMISSION_DEPOSIT_TR));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, COMMISSION_DEPOSIT_REG)) {
                        bobReportVO.setCommissionsDepositReg(resultSet
                                .getBigDecimal(COMMISSION_DEPOSIT_REG));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, COMMISSION_ASSET_AB)) {
                        bobReportVO.setCommissionsAssetAB(resultSet
                                .getBigDecimal(COMMISSION_ASSET_AB));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, COMMISSION_PRICE_CREDIT)) {
                        bobReportVO.setCommissionsPriceCredit(resultSet
                                .getBigDecimal(COMMISSION_PRICE_CREDIT));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, PRODUCER_CODE_LIST)) {
                        String producerCodeList = resultSet.getString(PRODUCER_CODE_LIST);
                        bobReportVO.setProducerCodes(producerCodeList);
                    }
                    if (getColumnStatus(contractStatusCategoryCode, RVP_FULLNAME)) {
                        bobReportVO.setRvpName(resultSet.getString(RVP_FULLNAME));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, CLASS_ID)) {
                    	String classMediumName = resultSet.getString(CLASS_ID);
                    	if (classMediumName != null) {
                    		classMediumName = getFundClassMedName(classMediumName.trim());
                    	}
                        bobReportVO.setFundClass(classMediumName);
                    }
                    if (getColumnStatus(contractStatusCategoryCode, PRODUCT_TYPE)) {
                        bobReportVO.setProductType(resultSet.getString(PRODUCT_TYPE));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, MANULIFE_COMPANY)) {
                        bobReportVO.setUsOrNy(resultSet.getString(MANULIFE_COMPANY));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, MANULIFE_COMPANY_ID)) {
                        bobReportVO.setManulifeCompanyID(resultSet.getString(MANULIFE_COMPANY_ID));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, BROKER_FULLNAME_LIST)) {
                        String brokerListCommaSeperated = resultSet.getString(BROKER_FULLNAME_LIST);
                        if (brokerListCommaSeperated != null) {
                            brokerListCommaSeperated = StringUtils.removeEnd(brokerListCommaSeperated.trim(), COMMA_SYMBOL);
                        }
                        bobReportVO.setFinancialRepNameAndFirmName(brokerListCommaSeperated);
                    }
                    if (getColumnStatus(contractStatusCategoryCode, PROPOSAL_NO)) {
                        bobReportVO.setProposalNumber(resultSet.getInt(PROPOSAL_NO));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, PROPOSAL_PRINTED_DATE)) {
                        bobReportVO.setProposalDate(resultSet.getDate(PROPOSAL_PRINTED_DATE));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, TRANS_ASSET_PRIOR_TO_CHARGES)) {
                        bobReportVO.setTransferredAssetsPriorToCharges(resultSet
                                .getBigDecimal(TRANS_ASSET_PRIOR_TO_CHARGES));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, EXPECTED_ASSET_CHARGE_PCT)) {
                        bobReportVO.setAssetCharge(resultSet
                                .getBigDecimal(EXPECTED_ASSET_CHARGE_PCT));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, EXPECTED_FIRST_YEAR_ASSETS_AMT)) {
                        bobReportVO.setExpectedFirstYearAssets(resultSet
                                .getBigDecimal(EXPECTED_FIRST_YEAR_ASSETS_AMT));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, CONTRACT_STATUS_CODE)) {
                        bobReportVO.setContractStatusCode(resultSet.getString(CONTRACT_STATUS_CODE));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, COMMISSION_ASSET_1YR)) {
                        bobReportVO.setCommissionAsset1Year(resultSet.getString(COMMISSION_ASSET_1YR));
                    }
                    
                    if (getColumnStatus(contractStatusCategoryCode, COMMISSION_ASSET_REN)) {
                        bobReportVO.setCommissionAssetRen(resultSet.getString(COMMISSION_ASSET_REN));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, COMMISSION_DEPOSIT_REG_1YR)) {
                        bobReportVO.setCommissionDepositReg1Yr(resultSet.getString(COMMISSION_DEPOSIT_REG_1YR));
                    } 
                    if (getColumnStatus(contractStatusCategoryCode, COMMISSION_DEPOSIT_REG_REN)) {
                        bobReportVO.setCommissionDepositRegRen(resultSet.getString(COMMISSION_DEPOSIT_REG_REN));
                    } 
                    if (getColumnStatus(contractStatusCategoryCode, COMMISSION_DEPOSIT_TR_1YR)) {
                        bobReportVO.setCommissionDepositTr1yr(resultSet.getString(COMMISSION_DEPOSIT_TR_1YR));
                    } 
                    if (getColumnStatus(contractStatusCategoryCode, COMMISSION_DEPOSIT_TR_REN)) {
                        bobReportVO.setCommissionDepositTrRen(resultSet.getString(COMMISSION_DEPOSIT_TR_REN));
                    }
                    
                    if (getColumnStatus(contractStatusCategoryCode, COMMISSION_ASSET_ALL_YRS)) {
                        bobReportVO.setCommissionAssetAllYrs(resultSet.getString(COMMISSION_ASSET_ALL_YRS));
                    }
                    
                    if (getColumnStatus(contractStatusCategoryCode, RIA_ASSET_BASED_BPS_FEE_CALC_AMT)) {
                        bobReportVO.setRiaBps(resultSet.getString(RIA_ASSET_BASED_BPS_FEE_CALC_AMT));
                    }
                    
                    if (getColumnStatus(contractStatusCategoryCode, RIA_ASSET_BASED_BPS_MAX_FEE_AMT)) {
                        bobReportVO.setRiaBpsMax(resultSet.getString(RIA_ASSET_BASED_BPS_MAX_FEE_AMT));
                        bobReportVO.setRiaBpsAnnualMaxAmount(resultSet.getBigDecimal(RIA_ASSET_BASED_BPS_ANNUAL_MAX_AMT));
                    }
                    
                    if (getColumnStatus(contractStatusCategoryCode, RIA_ASSET_BASED_BLENDED_FEE_AMT)) {
                    	bobReportVO.setRiaAcBlend(resultSet.getString(RIA_ASSET_BASED_BLENDED_FEE_AMT));
                    }
                    
                    if (getColumnStatus(contractStatusCategoryCode, RIA_ASSET_BASED_TIERED_FEE_AMT)) {
                    	bobReportVO.setRiaAcTiered(resultSet.getString(RIA_ASSET_BASED_TIERED_FEE_AMT));
                    }
                    
                    if (getColumnStatus(contractStatusCategoryCode, RIA_FLAT_PER_HEAD_FEE_AMT)) {
                        bobReportVO.setRiaFlatFeePerHead(resultSet.getString(RIA_FLAT_PER_HEAD_FEE_AMT));
                    }
                    
                    if (getColumnStatus(contractStatusCategoryCode, RIA_FLAT_PRORATA_FEE_AMT)) {
                        bobReportVO.setRiaFlatFeeProrata(resultSet.getString(RIA_FLAT_PRORATA_FEE_AMT));
                    }
                    
                    if (getColumnStatus(contractStatusCategoryCode, PRODUCER_ROLE_CODE)) {
                    	String prodRoleCode = StringUtils.trimToEmpty(resultSet.getString(PRODUCER_ROLE_CODE));
                    	if(StringUtils.equals(prodRoleCode, "338")){
                    		bobReportVO.setDes338Ind("Yes");
                    	}else{
                    		bobReportVO.setDes338Ind("-");
                    	}
                    }
                    
                    if (getColumnStatus(contractStatusCategoryCode, FR_CONTRACT_SHARE_LIST)) {
                        bobReportVO.setFrContractShareList(resultSet.getString(FR_CONTRACT_SHARE_LIST));
                    }   
                    
                    if (getColumnStatus(contractStatusCategoryCode, PRODUCT_ID)) {
                        bobReportVO.setProductId(StringUtils.trimToEmpty(resultSet.getString(PRODUCT_ID)));
                    }     
                    
                    if (getColumnStatus(contractStatusCategoryCode, PINPOINT_IND)) {
                        bobReportVO.setPinPointContract("Y".equals(resultSet.getString(PINPOINT_IND)));
                    }   
                    
                    if (getColumnStatus(contractStatusCategoryCode, RIA_FIRM_NAME)) {
                        bobReportVO.setRiaFirmName(resultSet.getString(RIA_FIRM_NAME));
                    }   
                    
                    if (getColumnStatus(contractStatusCategoryCode, RIA_FEE_PAID_BY_JH)) {
                        bobReportVO.setRiaFeePaidByJH(resultSet.getBigDecimal(RIA_FEE_PAID_BY_JH));
                    }   
                    
                    if (getColumnStatus(contractStatusCategoryCode, RIA_FEE_PAID_BY_PLAN)) {
                    	bobReportVO.setRiaFeePaidByPlan(resultSet.getBigDecimal(RIA_FEE_PAID_BY_PLAN));
                    }   
                    
                    if (getColumnStatus(contractStatusCategoryCode, RIA_FEE_TOTAL)) {
                        bobReportVO.setRiaTotalFee(resultSet.getBigDecimal(RIA_FEE_TOTAL));
                    }   
                    
					if (getColumnStatus(contractStatusCategoryCode, RIA_ASSET_BASED_BPS_MIN_FEE_PCT)) {
						bobReportVO.setRiaBpsMin(resultSet.getBigDecimal(RIA_ASSET_BASED_BPS_MIN_FEE_PCT));
						bobReportVO.setRiaBpsMonthlyMinAmount(resultSet.getBigDecimal(RIA_ASSET_BASED_FEE_MONTHLY_MIN_AMT));
					}

					if (getColumnStatus(contractStatusCategoryCode, COFIDUCIARY_FEE_FEATURE_TYPE_CODE)) {
						bobReportVO.setCofidFeeFeatureCode(resultSet.getString(COFIDUCIARY_FEE_FEATURE_TYPE_CODE));
					}
					
					if (getColumnStatus(contractStatusCategoryCode, COFID_ASSET_BASED_BPS_MIN_FEE_PCT)) {
						BigDecimal value = resultSet.getBigDecimal(COFID_ASSET_BASED_BPS_MIN_FEE_PCT);
						if (value != null && value.compareTo(BigDecimal.ZERO) > 0) {
							if (COFID_321.equals(bobReportVO.getCofidFeeFeatureCode())) {
								bobReportVO.setCofid321ABFee(value);
							} else if (COFID_338.equals(bobReportVO.getCofidFeeFeatureCode())) {
								bobReportVO.setCofid338ABFee(value);
							}
						}
						
						bobReportVO.setCofidBPSFeeMonthlyMinAmt(resultSet.getBigDecimal(COFID_ASSET_BASED_FEE_MONTHLY_MIN_AMT));
					}

					if (getColumnStatus(contractStatusCategoryCode, COFID_DOLLAR_BASED_PRORATA_FEE_AMT)) {
						BigDecimal value = resultSet.getBigDecimal(COFID_DOLLAR_BASED_PRORATA_FEE_AMT);
						if (value != null && value.compareTo(BigDecimal.ZERO) > 0) {
							if (COFID_321.equals(bobReportVO.getCofidFeeFeatureCode())) {
								bobReportVO.setCofid321DBFee(value);
							} else if (COFID_338.equals(bobReportVO.getCofidFeeFeatureCode())) {
								bobReportVO.setCofid338DBFee(value);
							}
						}
					}

					if (getColumnStatus(contractStatusCategoryCode, SMALL_PLAN_OPTION_CODE_COL)) {
						String smallPlanOptCodeValue = resultSet.getString(SMALL_PLAN_OPTION_CODE_COL);
						if (StringUtils.isNotBlank(smallPlanOptCodeValue)) {
							bobReportVO.setSmallPlanOptionCode(SmallPlanFeature.getType(smallPlanOptCodeValue));
						}
					}
					
                    updateSummaryForEachContract(bobReportVO, bobSummaryVO);
                    
                    bobreportVOList.add(bobReportVO);
                }
            }
            bobReportData.setDetails(bobreportVOList);
            
        } catch (SQLException e) {
            logger
                    .error("Problem occurred during BLOCK_OF_BUSINESS stored proc call. Report criteria:"
                            + criteria);
            throw new SystemException(e, className, "getReportData",
                    "Problem occurred during BLOCK_OF_BUSINESS stored proc call. Report criteria:"
                            + criteria);
        } finally {
            close(stmt, conn);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> getReportData");
        }
        
        return bobReportData;
        
    }

    /**
     * 
     * @param bobReportVO
     * @param bobSummaryVO
     */
    private static void updateSummaryForEachContract(BlockOfBusinessReportVO bobReportVO, BlockOfBusinessSummaryVO bobSummaryVO) {
    	if(!bobReportVO.isPinPointContract() && !bobReportVO.isPreSignatureContract()) {
    		bobSummaryVO.setHasAllPinpointOrPresigContract(false);
		} 
		if(bobReportVO.isPreSignatureContract()) {
			bobSummaryVO.setHasPresigContract(true);
		} 
		if(StringUtils.isNotEmpty(bobReportVO.getRiaFirmName())) {
			bobSummaryVO.setHasContractWithRiaAssocaited(true);
		} 
		if(StringUtils.equals(bobReportVO.getRiaFirmName() , AESTRIK)) {
			bobSummaryVO.setHasContractWithMulipleRiaAssociated(true);
		} 
		if(bobReportVO.getRiaFeePaidByJH() != null) {
			bobSummaryVO.setHasContractWithRiaPaidByJH(true);
		}
		if(bobReportVO.getCommissionAssetAllYrs() != null && Double.valueOf(bobReportVO.getCommissionAssetAllYrs()) > 0) {
			bobSummaryVO.setHasContractWithABAllYearCompensation(true);
		}
		if(bobReportVO.getRiaBps() != null || bobReportVO.getRiaBpsMin() != null || bobReportVO.getRiaBpsMax() != null || bobReportVO.getRiaAcBlend() != null || 
			bobReportVO.getRiaAcTiered() != null || bobReportVO.getRiaFlatFeePerHead() != null || bobReportVO.getRiaFlatFeeProrata() != null) {
			bobSummaryVO.setHasRiaFees(true);
		}
		if(bobReportVO.getDes338Ind() == "Yes") {
				bobSummaryVO.setHas338Designation(true);
		}
		if (StringUtils.isNotEmpty(bobReportVO.getCofidFeeFeatureCode())) {
			bobSummaryVO.setHasContractsWithCofidSelected(true);
		}
	}

	/**
     * Building the sort order.
     * 
     * @param criteria ReportCriteria
     * @return String
     */
    @SuppressWarnings("unchecked")
    private static String createSortPhrase(ReportCriteria criteria) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> createSortPhrase");
        }
        StringBuffer result = new StringBuffer();
        Iterator sorts = criteria.getSorts().iterator();
        String sortDirection = null;

        for (int i = 0; sorts.hasNext(); i++) {
            ReportSort sort = (ReportSort) sorts.next();
            sortDirection = sort.getSortDirection();
            if(sort.getSortField().equals(BlockOfBusinessReportData.COL_COMMISSIONS_RIA_FEES_ID)) {
            	result.append(fieldToColumnMap.get(sort.getSortField())).append(' ');
            	result.append(sortDirection);
            	result.append(',');
            	result.append(RIA_FIRM_NAME).append(' ');
            	result.append(sortDirection);
			} else if (BlockOfBusinessReportData.COFID_321_ASSET_BASED_BPS_FEE.equals(sort.getSortField())
					|| BlockOfBusinessReportData.COFID_321_DOLLAR_BASED_FEE_AMT.equals(sort.getSortField())) {
				result.append(createCofidSortPhrase(COFID_321, fieldToColumnMap.get(sort.getSortField()))).append(' ');
				result.append(sortDirection);
			} else if (BlockOfBusinessReportData.COFID_338_ASSET_BASED_BPS_FEE.equals(sort.getSortField())
					|| BlockOfBusinessReportData.COFID_338_DOLLAR_BASED_FEE_AMT.equals(sort.getSortField())) {
				result.append(createCofidSortPhrase(COFID_338, fieldToColumnMap.get(sort.getSortField()))).append(' ');
				result.append(sortDirection);
            }  else if (BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_MIN_FEE_AMT.equals(sort.getSortField())) {
				result.append(createRIASortPhrase(fieldToColumnMap.get(sort.getSortField()))).append(' ');
				result.append(sortDirection);
			} else {
            	result.append(fieldToColumnMap.get(sort.getSortField())).append(' ');
            	result.append(sortDirection);
            }
            
            
            result.append(',');
        }

        if (result.length() > 0) {
            result.deleteCharAt(result.length() - 1);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> createSortPhrase");
        }
        return result.toString();
    }
    
    private static String createRIASortPhrase(String sortColumn) {
    	return MessageFormat.format(SQL_RIA_SORT_CLAUSE, sortColumn);
    }
    
    private static String createCofidSortPhrase(String cofidValue, String sortColumn) {
    	return MessageFormat.format(SQL_COFID_SORT_CLAUSE, cofidValue, sortColumn);
    }

    /**
     * Build the filter phrase.
     * 
     * @param criteria - Report criteria
     * @return - String of filter phrase.
     */
    @SuppressWarnings("unchecked")
    private static String createFilterPhrase(ReportCriteria criteria) {
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> createFilterPhrase");
        }
        StringBuffer result = new StringBuffer();
        
        Map filters = criteria.getFilters();
        
        // The column Name is CLIENT_SHORT_NAME for PP tab, and is CONTRACT_NAME for other tabs.
        String contractStatusCategoryCode = (String) criteria.getFilters().get(
                BlockOfBusinessReportData.FILTER_CONTRACT_STATUS_CODES);
        String contractNameDBField = CONTRACT_NAME;
        if (OUTSTANDING_PROPOSALS_TAB.equals(contractStatusCategoryCode)) {
            contractNameDBField = CLIENT_SHORT_NAME;
        }
        String contractName = (String) filters.get(BlockOfBusinessReportData.FILTER_CONTRACT_NAME);
        if (!StringUtils.isEmpty(contractName)) {
            result.append(contractNameDBField).append(LIKE).append(
                    wrapInSingleQuotes(PERCENT_SYM + contractName.trim() + PERCENT_SYM)
                            .toUpperCase())
                    .append(SEMICOLON_SYMBOL);
        }
        
        String contractNumber = (String) filters.get(BlockOfBusinessReportData.FILTER_CONTRACT_NUMBER);
        if (!StringUtils.isEmpty(contractNumber)) {
            result.append(CONTRACT_ID).append(EQUAL_SYM).append(contractNumber.trim()).append(
                    SEMICOLON_SYMBOL);
        }
        
        String contractState = (String) filters.get(BlockOfBusinessReportData.FILTER_CONTRACT_STATE);
        if (!StringUtils.isEmpty(contractState)) {
            result.append(CONTRACT_ISSUED_STATE_CODE).append(EQUAL_SYM).append(
                    wrapInSingleQuotes(contractState).toUpperCase()).append(SEMICOLON_SYMBOL);
        }
        
        BigDecimal assetRangeFrom = (BigDecimal) filters
                .get(BlockOfBusinessReportData.FILTER_ASSET_RANGE_FROM);
        BigDecimal assetRangeTo = (BigDecimal) filters
                .get(BlockOfBusinessReportData.FILTER_ASSET_RANGE_TO);

        if (assetRangeFrom != null && assetRangeTo != null) {
            result.append(TOTAL_ASSET).append(BETWEEN).append(assetRangeFrom)
                    .append(AND).append(
                    assetRangeTo).append(SEMICOLON_SYMBOL);
        } else if (assetRangeFrom != null) {
            result.append(TOTAL_ASSET).append(GREATER_THAN_EQUAL_SYM).append(assetRangeFrom)
                    .append(SEMICOLON_SYMBOL);
        } else if (assetRangeTo != null) {
            result.append(TOTAL_ASSET).append(LESS_THAN_EQUAL_SYM).append(assetRangeTo).append(
                    SEMICOLON_SYMBOL);
        }

        String financialRepOrOrgName = (String) filters
                .get(BlockOfBusinessReportData.FILTER_FINANCIAL_REP_NAME);
        result.append(buildFilterClauseForFinancialRepName(financialRepOrOrgName));
        
        String productType = (String) filters.get(BlockOfBusinessReportData.FILTER_PRODUCT_TYPE);
        if (!StringUtils.isEmpty(productType)) {
            result.append(PRODUCT_TYPE).append(EQUAL_SYM).append(
                    wrapInSingleQuotes(productType).toUpperCase()).append(SEMICOLON_SYMBOL);
        }


        String usOrNy = (String) filters.get(BlockOfBusinessReportData.FILTER_US_OR_NY);
        if (!StringUtils.isEmpty(usOrNy)) {
            result.append(MANULIFE_COMPANY).append(EQUAL_SYM).append(
                    wrapInSingleQuotes(usOrNy).toUpperCase()).append(SEMICOLON_SYMBOL);
        }

        String fundClass = (String) filters.get(BlockOfBusinessReportData.FILTER_FUND_CLASS);
        if (!StringUtils.isEmpty(fundClass)) {
            result.append(CLASS_ID).append(EQUAL_SYM).append(wrapInSingleQuotes(fundClass.trim())).append(
                    SEMICOLON_SYMBOL);
        }

        // Note: RVP, SALES REGION, SALES DIVISION, FIRM NAME filters are taken care as part of
        // partyID List except for RVP, Firm Rep. For RVP, Firm Rep, the FIRM NAME filter is sent as
        // the Firm ID itself, instead of sending it as PartyID.
        String bdFirmID = (String) filters.get(BlockOfBusinessReportData.FILTER_FIRM_NAME);
        if (!StringUtils.isEmpty(bdFirmID)) {
            result.append(BROKER_DEALER_FIRM_ID_LIST).append(LIKE).append(
                    wrapInSingleQuotes(PERCENT_SYM + bdFirmID.trim() + PERCENT_SYM)).append(
                    SEMICOLON_SYMBOL);
        }

        String csFeatures = (String) filters.get(BlockOfBusinessReportData.FILTER_CSF_FEATURE);
        if (!StringUtils.isEmpty(csFeatures)) {
        	if (csFeatures.contains(BlockOfBusinessReportData.DF_FUND_FAMILY_CODE)) {
        		Map<String, String> fundFamilyOptionsMap = (Map<String, String>) filters.get(BlockOfBusinessReportData.DF_FUND_FAMILY_CODE);
        		String fundFamilyCode = fundFamilyOptionsMap.get(csFeatures);
            	result.append(FUND_FAMILY_CODE).append(LIKE).append(
                        wrapInSingleQuotes(PERCENT_SYM + fundFamilyCode.trim() + PERCENT_SYM)).append(
                        SEMICOLON_SYMBOL);
            } else {
            	 String columnName = csfFieldToColumnMap.get(csFeatures);
                 if (columnName != null) {
                	 if(csFeatures.equals(BlockOfBusinessReportData.WILSHIRE321_SERVICE) 
                			 || csFeatures.equals(BlockOfBusinessReportData.WILSHIRE338_SERVICE)) {
                     result.append(columnName).append(EQUAL_SYM).append(wrapInSingleQuotes(Y_SYM))
                             .append(SEMICOLON_SYMBOL).append(BlockOfBusinessReportData.SERVICE_PROVIDER_CODE).append(EQUAL_SYM).append(wrapInSingleQuotes("WILSHIRE"))
                             .append(SEMICOLON_SYMBOL);
                	 }else if(csFeatures.equals(BlockOfBusinessReportData.UBS321_SERVICE)){
                	 result.append(columnName).append(EQUAL_SYM).append(wrapInSingleQuotes(Y_SYM))
                         .append(SEMICOLON_SYMBOL).append(BlockOfBusinessReportData.SERVICE_PROVIDER_CODE).append(EQUAL_SYM).append(wrapInSingleQuotes("UBS"))
                         .append(SEMICOLON_SYMBOL);
                	 }else if(csFeatures.equals(BlockOfBusinessReportData.RJ_338_SERVICE)){
                	 result.append(columnName).append(EQUAL_SYM).append(wrapInSingleQuotes(Y_SYM))
                         .append(SEMICOLON_SYMBOL).append(BlockOfBusinessReportData.SERVICE_PROVIDER_CODE).append(EQUAL_SYM).append(wrapInSingleQuotes("RJAMES"))
                         .append(SEMICOLON_SYMBOL);
                 	}else if(columnName.equals(JH_CREDIT_PROGRAM_IND)){
                		 result.append(JH_CREDIT_PROGRAM_IND).append(EQUAL_SYM).append(wrapInSingleQuotes(Y_SYM))
                         .append(SEMICOLON_SYMBOL);
					} else if (csFeatures.equals(BlockOfBusinessReportData.MA_VALUE)) {
						result.append(columnName).append(IS_SYM).append(NOT_SYM).append(NULL_SYM)
								.append(SEMICOLON_SYMBOL);
					} else if (csFeatures.equals(BlockOfBusinessReportData.SFC_VALUE)) {
						// RPSSO-124648: SMALL_PLAN_OPTION_CODE='SFC' will be added in filter
						result.append(columnName).append(EQUAL_SYM)
								.append(wrapInSingleQuotes(SmallPlanFeature.SFC.getCode())).append(SEMICOLON_SYMBOL);
					} else if (csFeatures.equals(BlockOfBusinessReportData.PEP_VALUE)) {
						// RPSSO-124648: SMALL_PLAN_OPTION_CODE='PEP' will be added in filter
						result.append(columnName).append(EQUAL_SYM)
								.append(wrapInSingleQuotes(SmallPlanFeature.PEP.getCode())).append(SEMICOLON_SYMBOL);
					} else {
                		 result.append(columnName).append(EQUAL_SYM).append(wrapInSingleQuotes(Y_SYM))
                         .append(SEMICOLON_SYMBOL);
                	 }
                 } 
            }
        }
        
        String rvpName = (String) filters.get(BlockOfBusinessReportData.FILTER_RPV_NAME);
        if (!StringUtils.isEmpty(rvpName)) {
        	result.append(RVP_FULLNAME).append(LIKE).append(
                    wrapInSingleQuotes(PERCENT_SYM + rvpName.trim() + PERCENT_SYM)).append(
                    SEMICOLON_SYMBOL);
        }
        
        String filterClause = StringUtils.removeEnd(result.toString(), SEMICOLON_SYMBOL);

        if (logger.isDebugEnabled()) {
            logger.debug("exit -> createFilterPhrase");
        }
        return filterClause;
    }

    /**
     * This method will build the filter clause for Financial Rep Name. The filter clause for
     * Financial Rep Name has to built in such a way that we identify the last_name, first_name or
     * organization_name in it.
     * 
     * The Financial Rep filter criteria can contain zero or more commas.. For example: - abc or -
     * abc, def or - abc, def, ghi, xyz etc..
     * 
     * The Financial Rep filter criteria could be an - Individual Financial Rep's Name
     * (<lastName><comma><firstName> format) or - Organization name.
     * 
     * In case if the filter criteria contains more than 1 comma (for ex: abc, def, ghi), it is not
     * possible to know which is the last_name, which is the first_name, or is it a Organization
     * Name. So, in this case, we will divide the filter criteria at every comma and send it as a
     * last_name, first_name. The whole filter criteria will also be sent as organization_name.
     * 
     * So, in the above case, we will send filter clause as: ((LAST_NAME LIKE 'abc%' AND FIRST_NAME
     * LIKE 'def, ghi%') OR (LAST_NAME LIKE 'abc, def%' AND FIRST_NAME LIKE 'ghi%') OR (LAST_NAME
     * LIKE 'abc, def, ghi%') OR (ORGANIZATION_NAME LIKE 'abc, def, ghi%'))
     */
    private static String buildFilterClauseForFinancialRepName(String financialRepOrOrgName) {

        StringBuffer result = new StringBuffer(300);
        
        if (!StringUtils.isBlank(financialRepOrOrgName)) {
            
            result.append(OPEN_BRACKET);
            
            // Find if the string contains a comma
            if (StringUtils.contains(financialRepOrOrgName, COMMA_SYMBOL)) {
                int numOfCommas = StringUtils.countMatches(financialRepOrOrgName, COMMA_SYMBOL);

                int commaIndex = 0;
                boolean firstIndex = true;
                for (; numOfCommas > 0; numOfCommas--) {
                    // Find the location of comma.
                    commaIndex = StringUtils.indexOf(financialRepOrOrgName, COMMA_SYMBOL, commaIndex);

                    String stringBeforeComma = StringUtils.left(financialRepOrOrgName, commaIndex);
                    String stringAfterComma = StringUtils.right(financialRepOrOrgName,
                            financialRepOrOrgName.length() - commaIndex - 1);

                    if (firstIndex) {
                        firstIndex = false;
                    } else {
                        result.append(OR);
                    }

                    if (!StringUtils.isBlank(stringBeforeComma) || !StringUtils.isBlank(stringAfterComma)) {
                        result.append(OPEN_BRACKET);
                    }

                    if (!StringUtils.isBlank(stringBeforeComma)) {
                        result.append(FINANCIAL_REP_LAST_NAME_UCASE).append(LIKE).append(
                                wrapInSingleQuotes(
                                        PERCENT_SYM + stringBeforeComma.trim() + PERCENT_SYM)
                                        .toUpperCase());
                    }
                    
                    if (!StringUtils.isBlank(stringAfterComma)) {
                        if (!StringUtils.isBlank(stringBeforeComma)) {
                            result.append(AND);
                        }
                        result.append(FINANCIAL_REP_FIRST_NAME_UCASE).append(LIKE).append(
                                wrapInSingleQuotes(
                                        PERCENT_SYM + stringAfterComma.trim() + PERCENT_SYM)
                                        .toUpperCase());
                    }
                    
                    if (!StringUtils.isBlank(stringBeforeComma) || !StringUtils.isBlank(stringAfterComma)) {
                        result.append(CLOSE_BRACKET);
                    }
                    
                    commaIndex++;
                }
                result.append(OR);
            }
            // Adding the filter criteria as last name because, it might be possible that the
            // user has entered only the last name which has commas as well.
            result.append(OPEN_BRACKET).append(FINANCIAL_REP_LAST_NAME_UCASE).append(LIKE).append(
                    wrapInSingleQuotes(PERCENT_SYM + financialRepOrOrgName.trim() + PERCENT_SYM)
                            .toUpperCase())
                    .append(CLOSE_BRACKET);

            result.append(OR).append(OPEN_BRACKET).append(ORGANIZATION_NAME_UCASE).append(LIKE)
                    .append(
                    wrapInSingleQuotes(
                            PERCENT_SYM + StringUtils.trim(financialRepOrOrgName) + PERCENT_SYM)
                            .toUpperCase()).append(CLOSE_BRACKET);

            result.append(CLOSE_BRACKET);
            result.append(SEMICOLON_SYMBOL);
        }
        return result.toString();
    }

    /**
     * This method will check if the userRole belongs to a internal user or not.
     * 
     * @param userRoleCode
     * @return
     */
    private static Boolean isInternalUser(String userRoleCode) {
        return BlockOfBusinessReportData.NATIONAL_ACCT_USERROLE.equals(userRoleCode)
                || BlockOfBusinessReportData.ADMINISTRATOR_USERROLE.equals(userRoleCode)
                || BlockOfBusinessReportData.CONTENT_MGR_USERROLE.equals(userRoleCode)
                || BlockOfBusinessReportData.FIELD_USERROLE.equals(userRoleCode)
                || BlockOfBusinessReportData.RVP_USERROLE.equals(userRoleCode)
                || BlockOfBusinessReportData.RUM_USERROLE.equals(userRoleCode);
    }

    /**
     * This method checks if a given columnName is applicable for a given tab or not.
     * 
     * @param tabName - the tab name
     * @param columnName - the column name
     * @return - returns "true" if the column is applicable for that tab, else, returns "false".
     */
    private static Boolean getColumnStatus(String tabName, String columnName) {
        if (applicableColumnsMap.get(tabName) == null
                || applicableColumnsMap.get(tabName).get(columnName) == null) {
            return Boolean.FALSE;
        }
        return (Boolean) applicableColumnsMap.get(tabName).get(columnName);
    }

    /**
     * This method creates a Map with all the columns set enabled. (i.e., all columns are
     * applicable)
     * 
     * @return - Map of columns.
     */
    private static Map<String, Boolean> createDefaultColumnsMap() {
        Map<String, Boolean> defaultColumnsMapForTab = new HashMap<String, Boolean>();

        defaultColumnsMapForTab.put(CONTRACT_NAME, Boolean.TRUE);
        defaultColumnsMapForTab.put(CONTRACT_ID, Boolean.TRUE);
        defaultColumnsMapForTab.put(EFFECTIVE_DATE, Boolean.TRUE);
        defaultColumnsMapForTab.put(PLAN_REPORTING_YEAR_END_DATE, Boolean.TRUE);
        defaultColumnsMapForTab.put(INCORPORATED_STATE_CODE, Boolean.TRUE);
        defaultColumnsMapForTab.put(CONTRACT_ISSUED_STATE_CODE, Boolean.TRUE);
        defaultColumnsMapForTab.put(NUMBER_OF_LIVES, Boolean.TRUE);
        defaultColumnsMapForTab.put(TOTAL_ASSET, Boolean.TRUE);
        defaultColumnsMapForTab.put(BROKER_DEALER_FIRM_NAME, Boolean.TRUE);
        defaultColumnsMapForTab.put(RVP_FULLNAME, Boolean.TRUE);
        defaultColumnsMapForTab.put(PRODUCT_TYPE, Boolean.TRUE);
        defaultColumnsMapForTab.put(MANULIFE_COMPANY, Boolean.TRUE);
        defaultColumnsMapForTab.put(MANULIFE_COMPANY_ID, Boolean.TRUE);
        defaultColumnsMapForTab.put(CLASS_ID, Boolean.TRUE);
        defaultColumnsMapForTab.put(PRODUCER_CODE_LIST, Boolean.TRUE);
        defaultColumnsMapForTab.put(BROKER_FULLNAME_LIST, Boolean.TRUE);
        defaultColumnsMapForTab.put(CLIENT_SHORT_NAME, Boolean.TRUE);
        defaultColumnsMapForTab.put(PROPOSAL_NO, Boolean.TRUE);
        defaultColumnsMapForTab.put(PROPOSAL_PRINTED_DATE, Boolean.TRUE);
        defaultColumnsMapForTab.put(EXPECTED_ASSET_CHARGE_PCT, Boolean.TRUE);
        defaultColumnsMapForTab.put(EXPECTED_FIRST_YEAR_ASSETS_AMT, Boolean.TRUE);
        defaultColumnsMapForTab.put(TRANS_ASSET_PRIOR_TO_CHARGES, Boolean.TRUE);
        defaultColumnsMapForTab.put(CONTRACT_STATUS_EFFECTIVE_DATE, Boolean.TRUE);
        defaultColumnsMapForTab.put(CONTRACT_STATUS_CODE, Boolean.TRUE);
        defaultColumnsMapForTab.put(PRODUCT_ID, Boolean.TRUE);
        defaultColumnsMapForTab.put(PINPOINT_IND, Boolean.TRUE);
        
        return defaultColumnsMapForTab;
    }

    /**
     * This method converts the Integer to BigDecimal.
     * 
     * @param parameter - Integer value
     * @return - BigDecimal value.
     */
    protected static BigDecimal intToBigDecimal(Integer parameter) {
        if (parameter == null) {
            return null;
        }
        return new BigDecimal(parameter);
    }
    
    /**
     * 
     * @param rateType
     * @return
     * @throws SQLException 
     */
    public static String getFundClassMedName(String rateType) throws SQLException{
    	 Connection conn1 = null;
         PreparedStatement statement = null;
         ResultSet rs = null;
         String classMediumName = "";

         try {
             // setup the connection and the statement
        	 conn1 = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
             statement = conn1.prepareStatement(SQL_SELECT_CLASS_MEDIUM_NAME);
             statement.setString(1, rateType);
             statement.execute();

             rs = statement.getResultSet();
             
             if (rs != null) {
            	 while(rs.next()){
            		classMediumName = rs.getString("CLASS_MEDIUM_NAME").trim();
            	 }
             }
             
         }catch (Exception e) {
			// TODO: handle exception
		}finally{
			if(statement != null && conn1 != null){
				statement.close();
				conn1.close();
			}
		}
         
         return classMediumName;
    }
}