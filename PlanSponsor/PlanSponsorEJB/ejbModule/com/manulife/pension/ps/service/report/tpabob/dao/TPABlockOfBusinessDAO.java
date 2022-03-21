package com.manulife.pension.ps.service.report.tpabob.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.common.util.Constants;
import com.manulife.pension.ps.service.report.tpabob.valueobject.TPABlockOfBusinessReportData;
import com.manulife.pension.ps.service.report.tpabob.valueobject.TPABlockOfBusinessReportVO;
import com.manulife.pension.ps.service.report.tpabob.valueobject.TPABlockOfBusinessSummaryContractVO;
import com.manulife.pension.ps.service.report.tpabob.valueobject.TPABlockOfBusinessSummaryVO;
import com.manulife.pension.service.contract.util.SmallPlanFeature;
import com.manulife.pension.service.contract.valueobject.DefaultInvestmentFundVO;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.environment.EnvironmentServiceHelper;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.report.valueobject.ReportSortList;

/**
 * This class will call the TPA Block Of Business Stored Proc and retrieve the TPA BOB related
 * report data.
 * 
 * @author HArlomte
 * 
 */
public class TPABlockOfBusinessDAO extends BaseDatabaseDAO {

    private static final Logger logger = Logger.getLogger(TPABlockOfBusinessDAO.class);

    private static String className = TPABlockOfBusinessDAO.class.getName();

    private static String GET_BOB = "{CALL " + BROKER_DEALER_SCHEMA_NAME
            + "BLOCK_OF_BUSINESS (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

    // Database column Names
    private static String CONTRACT_NAME = "CONTRACT_NAME";

    private static String CONTRACT_ID = "CONTRACT_ID";

    private static String PLAN_REPORTING_YEAR_END_DATE = "PLAN_REPORTING_YEAR_END_DATE";

    private static String NUMBER_OF_LIVES = "NUMBER_OF_LIVES";

    private static String TOTAL_ASSET = "TOTAL_ASSET";

    private static String BROKER_FULLNAME_LIST = "BROKER_FULLNAME_LIST";

    private static String TRANS_ASSET_PRIOR_TO_CHARGES = "TRANS_ASSET_PRIOR_TO_CHARGES";

    // Discontinued Date
    private static String CONTRACT_STATUS_EFFECTIVE_DATE = "CONTRACT_STATUS_EFFECTIVE_DATE";

    private static String EXPECTED_FIRST_YEAR_ASSETS_AMT = "EXPECTED_FIRST_YEAR_ASSETS_AMT"; 

    private static String EXPECTED_NUM_OF_LIVES = "EXPECTED_LIVES_COUNT";

    private static String CAR_NAME = "CLIENT_ACCOUNT_REP_NAME";

    private static String CAR_NAME_UCASE = "UCASE(CLIENT_ACCOUNT_REP_NAME)";

    // Product Type - This is not a column that we show on the online screen. But this is needed
    // when showing the NumOfLives in the online screen.
    private static String PRODUCT_TYPE = "PRODUCT_TYPE";
    
    // Extra CSV related columns
    private static String TPA_FIRM_ID = "THIRD_PARTY_ADMIN_ID";

    private static String CONTRACT_STATUS_CODE = "CONTRACT_STATUS_CODE"; 

    private static String EFFECTIVE_DATE = "EFFECTIVE_DATE";

    private static String ALLOCATED_ASSETS_AMT = "ALLOCATED_ASSETS_AMT";

    private static String LOAN_ASSETS_AMT = "LOAN_ASSETS_AMT";

    private static String CASH_ACCOUNT_BALANCE_AMT = "CASH_ACCOUNT_BALANCE_AMT";

    private static String PBA_ASSETS_AMT = "PBA_ASSETS_AMT";

    private static String EZ_START_IND = "EZ_START_IND";

    private static String EZ_INCREASE_IND = "EZ_INCREASE_IND";

    private static String DIRECT_MAIL_IND = "DIRECT_MAIL_IND";

    private static String GIFL_IND = "GIFL_IND";
    
    private static String GIFL_VERSION = "GIFL_VERSION";
    
    private static String MA_SERVICE_CODE = "MA_SERVICE_FEATURE_CODE";
    
    private static final String SMALL_PLAN_CODE = "SMALL_PLAN_OPTION_CODE";
    
    private static String PAM_CODE = "PAM_CODE";
    
    private static String ONLINE_BENEFICIARY_DESIGNATION_IND = "ONLINE_BENEFICIARY_DESIGNATION_IND";

    private static String ONLINE_WD_IND = "ONLINE_WD_IND";
    
   
    private static String SYS_WD_IND = "SYW_IND";
    
    private static String SEND_SERVICE = "SEND_SERVICE";
    
    private static String JHTC_PASSIVE_TRUSTEE_CODE = "JHTC_PASSIVE_TRUSTEE_CODE";

    private static String VESTING_CALC_CODE = "VESTING_CALC_CODE";

    private static String VESTING_ON_STMT_IND = "VESTING_ON_STMT_IND";

    private static String PERMITTED_DISPARITY_IND = "PERMITTED_DISPARITY_IND";

    private static String FSW_IND = "FSW_COFID_CODE"; 

    private static String DEFAULT_INVESTMENT_OPTION = "DEFAULT_INVESTMENT_OPTION";
    
    private static String TPA_SIGNING_AUTHORITY_IND = "TPA_SIGNING_AUTHORITY_IND";

    private static String COW_IND = "COW_IND";
    
    public static  String PAYROL_PATH_IND = "PAYROL_PATH_IND";
    
    public static  String PAYROLL_FEEDBACK_SVC_IND = "PAYROLL_FEEDBACK_SVC_IND";
    
    public static  String PLAN_HIGHLIGHTS_IND = "PLAN_HIGHLIGHTS_IND";
    
    public static  String PLAN_HIGHLIGHTS_REVIEWED_IND    = "PLAN_HIGHLIGHTS_REVIEWED_IND";
    
    public static  String PIF_INSTALLMENTS_IND    = "PIF_INSTALLMENT_IND";

    private static String FINANCIAL_REP_LAST_NAME_UCASE = "UCASE(LAST_NAME)";

    private static String FINANCIAL_REP_FIRST_NAME_UCASE = "UCASE(FIRST_NAME)";

    private static String ORGANIZATION_NAME_UCASE = "UCASE(ORGANIZATION_NAME)";

    private static String MANULIFE_COMPANY = "MANULIFE_COMPANY";

    // Summary section related column names.
    public static final String THIRD_PARTY_ADMIN_NAME = "THIRD_PARTY_ADMIN_NAME";
    
    public static final String THIRD_PARTY_ADMIN_ID = "THIRD_PARTY_ADMIN_ID";

    public static final String ACTIVE_CONTRACT_COUNT = "CONTRACT_COUNT";

    public static final String ACTIVE_CONTRACT_LIVES = "NUMBER_OF_LIVES";

    public static final String ACTIVE_CONTRACT_ASSETS = "TOTAL_ASSET";
    
    public static final String PENDING_CONTRACT_COUNT = "CONTRACT_COUNT_2";

    public static final String PENDING_CONTRACT_LIVES = "NUMBER_OF_LIVES_2";
    
    public static final String DISCONTINUED_CONTRACT_COUNT = "DI_CONTRACT_COUNT";

    // Other Miscellaneous constants.
    private static String PERCENT_SYM = "%";

    private static String EQUAL_SYM = "=";

    private static String OPEN_BRACKET = "(";

    private static String CLOSE_BRACKET = ")";

    private static final String Y_SYM = "Y";

    private static final String COMMA_SYMBOL = ",";
    
    private static final String SEMICOLON_SYMBOL = ";";

    private static String OR = " OR ";

    private static String AND = " AND ";

    private static String LIKE = " LIKE ";

    
    private static String ACTIVE_TAB = "AC";

    private static String PENDING_TAB = "PN";

    private static String DISCONTINUED_TAB = "DI";

    private static Map<String, String> fieldToColumnMap;

    private static Map<String, Map<String, Boolean>> applicableColumnsMap;

    private static Map<String, Boolean> applicableColumnsMapForTab;
    
    private static Map<String,String> giflVersionValues= new HashMap<String,String>();
    
    static {
        /**
         * This HashMap is used to map a given field Name to a "Database Column Name".
         */
        fieldToColumnMap = new HashMap<String, String>();
        fieldToColumnMap.put(TPABlockOfBusinessReportData.COL_CONTRACT_NAME_ID, CONTRACT_NAME);
        fieldToColumnMap.put(TPABlockOfBusinessReportData.COL_CONTRACT_NUMBER_ID, CONTRACT_ID);
        fieldToColumnMap.put(TPABlockOfBusinessReportData.COL_CONTRACT_PLAN_YEAR_END_ID, PLAN_REPORTING_YEAR_END_DATE);
        fieldToColumnMap.put(TPABlockOfBusinessReportData.COL_NUM_OF_LIVES_ID, NUMBER_OF_LIVES);
        fieldToColumnMap.put(TPABlockOfBusinessReportData.COL_TOTAL_ASSETS_ID, TOTAL_ASSET);
        fieldToColumnMap.put(TPABlockOfBusinessReportData.COL_TRANSFERRED_OUT_ASSETS_ID, TRANS_ASSET_PRIOR_TO_CHARGES);
        fieldToColumnMap.put(TPABlockOfBusinessReportData.COL_EXPECTED_FIRST_YEAR_ASSETS_ID, EXPECTED_FIRST_YEAR_ASSETS_AMT);
        fieldToColumnMap.put(TPABlockOfBusinessReportData.COL_EXPECTED_NUM_OF_LIVES_ID, EXPECTED_NUM_OF_LIVES);
        fieldToColumnMap.put(TPABlockOfBusinessReportData.COL_CAR_NAME_ID, CAR_NAME);
        fieldToColumnMap.put(TPABlockOfBusinessReportData.COL_FINANCIAL_REP_ID, BROKER_FULLNAME_LIST);
        fieldToColumnMap.put(TPABlockOfBusinessReportData.COL_DISCONTINUANCE_DATE_ID, CONTRACT_STATUS_EFFECTIVE_DATE);// discontinued date.

        /**
         * This Map is used to give maintain a list of applicable columns for a given tab.
         */
        applicableColumnsMap = new HashMap<String, Map<String, Boolean>>();

        // Applicable columns for Active tab
        applicableColumnsMapForTab = createDefaultColumnsMap();
        applicableColumnsMapForTab.put(TRANS_ASSET_PRIOR_TO_CHARGES, Boolean.FALSE);
        applicableColumnsMapForTab.put(EXPECTED_NUM_OF_LIVES, Boolean.FALSE);
        applicableColumnsMapForTab.put(CONTRACT_STATUS_EFFECTIVE_DATE, Boolean.FALSE);
        applicableColumnsMap.put(ACTIVE_TAB, applicableColumnsMapForTab);

        // Applicable columns for Pending tab
        applicableColumnsMapForTab = createDefaultColumnsMap();
        applicableColumnsMapForTab.put(NUMBER_OF_LIVES, Boolean.FALSE);
        applicableColumnsMapForTab.put(TRANS_ASSET_PRIOR_TO_CHARGES, Boolean.FALSE);
        applicableColumnsMapForTab.put(CONTRACT_STATUS_EFFECTIVE_DATE, Boolean.FALSE);
        applicableColumnsMap.put(PENDING_TAB, applicableColumnsMapForTab);

        // Applicable columns for Discontinued tab
        applicableColumnsMapForTab = createDefaultColumnsMap();
        applicableColumnsMapForTab.put(NUMBER_OF_LIVES, Boolean.FALSE);
        applicableColumnsMapForTab.put(TOTAL_ASSET, Boolean.FALSE);
        applicableColumnsMapForTab.put(EXPECTED_NUM_OF_LIVES, Boolean.FALSE);
        applicableColumnsMapForTab.put(MA_SERVICE_CODE, Boolean.FALSE);
        applicableColumnsMap.put(DISCONTINUED_TAB, applicableColumnsMapForTab);
        
        //This map gives the GIFL Version and its text
        giflVersionValues.put(Constants.GIFL_VERSION_03,Constants.GIFL_SELECT_TEXT);
    	giflVersionValues.put(Constants.GIFL_VERSION_02,Constants.GIFL_TEXT);
    	giflVersionValues.put(Constants.GIFL_VERSION_01,Constants.GIFL_TEXT);
    }

	/**
	 * This method calls the TPA BlockofBusiness stored proc and retrieves the
	 * Summary Information, Report Information to be shown on TPA Block of
	 * Business page.
	 * 
	 * @param criteria
	 *            - The Filtering criteria to be sent to TPA BOB Stored proc.
	 * @return - Report Data to be shown on the JSP page.
	 * @throws SystemException
	 */
    @SuppressWarnings( { "unchecked", "deprecation" })
    public static ReportData getReportData(final ReportCriteria criteria) throws SystemException {

        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet resultSet = null;
        TPABlockOfBusinessReportData reportData = null;
        
        Map<String, String> maServiceFeatureType = ContractServiceDelegate.getInstance().getManagedAccountServiceCodes();
        try {
            // setup the connection and the statement
            conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareCall(GET_BOB);

            if (logger.isDebugEnabled()) {
                logger.debug("Calling Stored Procedure: " + GET_BOB);
            }

            int idx = 1;

            Integer callSessionID = (Integer) criteria.getFilters().get(
                    TPABlockOfBusinessReportData.FILTER_DB_SESSION_ID);
            stmt.setBigDecimal(idx++, intToBigDecimal(callSessionID));

            Long userProfileID = (Long) criteria.getFilters().get(
                    TPABlockOfBusinessReportData.FILTER_USER_PROFILE_ID);
            stmt.setBigDecimal(idx++, new BigDecimal(userProfileID));

            String userRoleCode = (String) criteria.getFilters().get(
                    TPABlockOfBusinessReportData.FILTER_USER_ROLE);
            stmt.setString(idx++, userRoleCode);

            Long mimicUserProfileID = (Long) criteria.getFilters().get(
                    TPABlockOfBusinessReportData.FILTER_MIMIC_USER_PROFILE_ID);
            if (mimicUserProfileID == null) {
                stmt.setString(idx++, null);
            } else {
                stmt.setBigDecimal(idx++, new BigDecimal(mimicUserProfileID));
            }

            String mimicUserRoleCode = (String) criteria.getFilters().get(
                    TPABlockOfBusinessReportData.FILTER_MIMIC_USER_ROLE);
            stmt.setString(idx++, mimicUserRoleCode);

            ArrayList<Integer> partyIDList = (ArrayList<Integer>) criteria.getFilters().get(
                    TPABlockOfBusinessReportData.FILTER_PARTY_ID);

            if (partyIDList == null || partyIDList.isEmpty()) {
                stmt.setString(idx++, null);
            } else {
                StringBuilder commaSeperatedPartyIDList = new StringBuilder();
                commaSeperatedPartyIDList.append(partyIDList.get(0));
                for (Integer partyID : partyIDList.subList(1, partyIDList.size())) {
                    commaSeperatedPartyIDList.append(COMMA_SYMBOL).append(partyID);
                }
                stmt.setString(idx++, commaSeperatedPartyIDList.toString());
            }

            String contractStatusCategoryCode = (String) criteria.getFilters().get(
                    TPABlockOfBusinessReportData.FILTER_CONTRACT_STATUS_CODES);
            stmt.setString(idx++, contractStatusCategoryCode);

            String reportAsOfDate = (String) criteria.getFilters().get(
                    TPABlockOfBusinessReportData.FILTER_AS_OF_DATE);
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
            
            // CSV_Report_Ind is set to true when the user wants to download the CSV report.
            String csvReportInd = (String) criteria
                    .getFilterValue(TPABlockOfBusinessReportData.CSV_DOWNLOAD_IND);
            if (StringUtils.isBlank(csvReportInd)) {
                stmt.setString(idx++, null);
            } else {
                stmt.setString(idx++, csvReportInd);
            }
            
            stmt.registerOutParameter(idx++, Types.DECIMAL);

            stmt.registerOutParameter(idx, Types.CHAR);

            // execute the stored procedure
            stmt.execute();
            
            resultSet = stmt.getResultSet();
            
            BigDecimal callSessionIdOut = stmt.getBigDecimal(idx - 1);
            Integer sessionIdOut = callSessionIdOut == null ? null : callSessionIdOut.intValue();
            
            String resultTooBigInd = stmt.getString(idx);
            
            int totalCount = 0;

            TPABlockOfBusinessSummaryVO tpaBlockOfBusinessSummaryVO = new TPABlockOfBusinessSummaryVO();

            ArrayList<TPABlockOfBusinessSummaryContractVO> tpaBobSummaryContractList = new ArrayList<TPABlockOfBusinessSummaryContractVO>();
            
            BigDecimal totalActiveContractCount = new BigDecimal("0.0");
            BigDecimal totalActiveContractLives = new BigDecimal("0.0");
            BigDecimal totalActiveContractAssets = new BigDecimal("0.0");
            BigDecimal totalPendingContractCount = new BigDecimal("0.0");
            BigDecimal totalPendingContractLives = new BigDecimal("0.0");
            BigDecimal totalDiscontinuedContractCount = new BigDecimal("0.0");
            if (resultSet != null) {
                while (resultSet.next()) {
                    // Result Set#1 has User Info to be shown in Summary section.
                    TPABlockOfBusinessSummaryContractVO tpaBobSummaryContractVO = new TPABlockOfBusinessSummaryContractVO();

                    String firmName = resultSet.getString(THIRD_PARTY_ADMIN_NAME);
                    // If we get a Row with Firm Name as "n/a", then ignore this row.
                    if (firmName != null && "n/a".equalsIgnoreCase(firmName.trim())) {
                        continue;
                    }
                    
                    tpaBobSummaryContractVO.setFirmName(firmName);
                    tpaBobSummaryContractVO.setFirmNumber(resultSet
                            .getString(THIRD_PARTY_ADMIN_ID));

                    BigDecimal activeContractCount = resultSet.getBigDecimal(ACTIVE_CONTRACT_COUNT);
                    BigDecimal activeContractLives = resultSet.getBigDecimal(ACTIVE_CONTRACT_LIVES);
                    BigDecimal activeContractAssets = resultSet.getBigDecimal(ACTIVE_CONTRACT_ASSETS);
                    BigDecimal pendingContractCount = resultSet.getBigDecimal(PENDING_CONTRACT_COUNT);
                    BigDecimal pendingContractLives = resultSet.getBigDecimal(PENDING_CONTRACT_LIVES);
                    BigDecimal discontinuedContractCount = resultSet
                            .getBigDecimal(DISCONTINUED_CONTRACT_COUNT);

                    if (activeContractCount != null) {
                        tpaBobSummaryContractVO.setActiveContractCount(activeContractCount);
                        totalActiveContractCount = totalActiveContractCount.add(activeContractCount);
                    }
                    if (activeContractLives != null) {
                        tpaBobSummaryContractVO.setActiveContractLives(activeContractLives);
                        totalActiveContractLives = totalActiveContractLives.add(activeContractLives);
                    }
                    if (activeContractAssets != null) {
                        tpaBobSummaryContractVO.setActiveContractAssets(activeContractAssets);
                        totalActiveContractAssets = totalActiveContractAssets.add(activeContractAssets);
                    }
                    if (pendingContractCount != null) {
                        tpaBobSummaryContractVO.setPendingContractCount(pendingContractCount);
                        totalPendingContractCount = totalPendingContractCount.add(pendingContractCount);
                    }
                    if (pendingContractLives != null) {
                        tpaBobSummaryContractVO.setPendingContractLives(pendingContractLives);
                        totalPendingContractLives = totalPendingContractLives.add(pendingContractLives);
                    }
                    
                    if (discontinuedContractCount != null) {
                        tpaBobSummaryContractVO.setDiscontinuedContractCount(discontinuedContractCount);
                        totalDiscontinuedContractCount = totalDiscontinuedContractCount
                                .add(discontinuedContractCount);
                    }
                    tpaBobSummaryContractList.add(tpaBobSummaryContractVO);
                }
                
                tpaBlockOfBusinessSummaryVO.setTpaBobSummaryContractVO(tpaBobSummaryContractList);

                tpaBlockOfBusinessSummaryVO.setTotalActiveContractCount(totalActiveContractCount);
                tpaBlockOfBusinessSummaryVO.setTotalActiveContractLives(totalActiveContractLives);
                tpaBlockOfBusinessSummaryVO.setTotalActiveContractAssets(totalActiveContractAssets);
                tpaBlockOfBusinessSummaryVO.setTotalPendingContractCount(totalPendingContractCount);
                tpaBlockOfBusinessSummaryVO.setTotalPendingContractLives(totalPendingContractLives);
                
                if (ACTIVE_TAB.equals(contractStatusCategoryCode)) {
                    totalCount = totalActiveContractCount.intValue();
                } else if (PENDING_TAB.equals(contractStatusCategoryCode)) {
                    totalCount = totalPendingContractCount.intValue();
                } else {
                    totalCount = totalDiscontinuedContractCount.intValue();
                }
            }
            
            reportData = new TPABlockOfBusinessReportData(criteria, totalCount);

            reportData.setDbSessionID(sessionIdOut);
            
            reportData.setResultTooBigInd(Y_SYM.equals(resultTooBigInd) ? Boolean.TRUE
                    : Boolean.FALSE); 

            ((TPABlockOfBusinessReportData) reportData)
                    .setTpaBlockOfBusinessSummaryVO(tpaBlockOfBusinessSummaryVO);

            ArrayList<TPABlockOfBusinessReportVO> tpaBobReportList = new ArrayList<TPABlockOfBusinessReportVO>();

            stmt.getMoreResults();
            resultSet = stmt.getResultSet();
            if (resultSet != null) {
                while (resultSet.next()) {
                    TPABlockOfBusinessReportVO tpaBobReportVO = new TPABlockOfBusinessReportVO();
                    
                    if (getColumnStatus(contractStatusCategoryCode, CONTRACT_NAME)) {
                        tpaBobReportVO.setContractName(resultSet.getString(CONTRACT_NAME));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, CONTRACT_ID)) {
                        tpaBobReportVO.setContractNumber(resultSet.getInt(CONTRACT_ID));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, PLAN_REPORTING_YEAR_END_DATE)) {
                        tpaBobReportVO.setContractPlanYearEnd(resultSet.getDate(PLAN_REPORTING_YEAR_END_DATE));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, NUMBER_OF_LIVES)) {
                        tpaBobReportVO.setNumOfLives(resultSet.getBigDecimal(NUMBER_OF_LIVES));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, TOTAL_ASSET)) {
                        tpaBobReportVO.setTotalAssets(resultSet.getBigDecimal(TOTAL_ASSET));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, TRANS_ASSET_PRIOR_TO_CHARGES)) {
                        tpaBobReportVO.setTransferredAssetsPriorToCharges(resultSet
                                .getBigDecimal(TRANS_ASSET_PRIOR_TO_CHARGES));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, EXPECTED_FIRST_YEAR_ASSETS_AMT)) {
                        tpaBobReportVO.setExpectedFirstYearAssets(resultSet
                                .getBigDecimal(EXPECTED_FIRST_YEAR_ASSETS_AMT));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, EXPECTED_NUM_OF_LIVES)) {
                        tpaBobReportVO.setExpectedNumOfLives(resultSet
                                .getBigDecimal(EXPECTED_NUM_OF_LIVES));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, PRODUCT_TYPE)) {
                        tpaBobReportVO.setProductType(resultSet.getString(PRODUCT_TYPE));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, CAR_NAME)) {
                        tpaBobReportVO.setCarName(resultSet.getString(CAR_NAME));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, BROKER_FULLNAME_LIST)) {
                        String brokerListCommaSeperated = resultSet.getString(BROKER_FULLNAME_LIST);
                        if (brokerListCommaSeperated != null) {
                            brokerListCommaSeperated = StringUtils.removeEnd(
                                    brokerListCommaSeperated.trim(), COMMA_SYMBOL);
                        }
                        tpaBobReportVO.setFinancialRepName(brokerListCommaSeperated);
                    }
                    if (getColumnStatus(contractStatusCategoryCode, CONTRACT_STATUS_EFFECTIVE_DATE)) {
                        tpaBobReportVO.setDiscontinuanceDate(resultSet.getDate(CONTRACT_STATUS_EFFECTIVE_DATE));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, TPA_FIRM_ID)) {
                        tpaBobReportVO.setTpaFirmID(resultSet.getString(TPA_FIRM_ID));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, CONTRACT_STATUS_CODE)) {
                        tpaBobReportVO.setContractStatus(resultSet.getString(CONTRACT_STATUS_CODE));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, EFFECTIVE_DATE)) {
                        tpaBobReportVO.setContractEffectiveDate(resultSet.getDate(EFFECTIVE_DATE));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, ALLOCATED_ASSETS_AMT)) {
                        tpaBobReportVO.setAllocatedAssets(resultSet.getBigDecimal(ALLOCATED_ASSETS_AMT));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, LOAN_ASSETS_AMT)) {
                        tpaBobReportVO.setLoanAssets(resultSet.getBigDecimal(LOAN_ASSETS_AMT));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, CASH_ACCOUNT_BALANCE_AMT)) {
                        tpaBobReportVO.setCashAccountBalance(resultSet.getBigDecimal(CASH_ACCOUNT_BALANCE_AMT));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, PBA_ASSETS_AMT)) {
                        tpaBobReportVO.setPbaAssets(resultSet.getBigDecimal(PBA_ASSETS_AMT));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, EZ_START_IND)) {
                        tpaBobReportVO.setEzStart(resultSet.getString(EZ_START_IND));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, EZ_INCREASE_IND)) {
                        tpaBobReportVO.setEzIncrease(resultSet.getString(EZ_INCREASE_IND));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, DIRECT_MAIL_IND)) {
                        tpaBobReportVO.setDirectMail(resultSet.getString(DIRECT_MAIL_IND));
                    }
                    //If GIFL enabled contract, then value GIFL or NON GIFL will be shown in CSV file - GIFL P3 
                    if (getColumnStatus(contractStatusCategoryCode, GIFL_IND)) {
                    	if(resultSet.getString(GIFL_IND)!=null && "Y".equalsIgnoreCase(resultSet.getString(GIFL_IND))){
                    		tpaBobReportVO.setGifl(getGiflVersionProduct(resultSet.getString(GIFL_VERSION)));
                    	}
                    	else
                    		tpaBobReportVO.setGifl(Constants.NO);
                    }
					// if MA enabled contract
					if (getColumnStatus(contractStatusCategoryCode, MA_SERVICE_CODE)) {
						if (resultSet.getString(MA_SERVICE_CODE) != null) {
							tpaBobReportVO.setManagedAccountServiceTypeDesc(maServiceFeatureType.get(resultSet.getString(MA_SERVICE_CODE)));
						} else
							tpaBobReportVO.setManagedAccountServiceTypeDesc(Constants.NO);
					}
					if (getColumnStatus(contractStatusCategoryCode, SMALL_PLAN_CODE)) {
						String smallPlanOptCodeValue = resultSet.getString(SMALL_PLAN_CODE);
						if (StringUtils.isNotBlank(smallPlanOptCodeValue)) {
							tpaBobReportVO.setSmallPlanOptionCode(SmallPlanFeature.getType(smallPlanOptCodeValue));
						}
					}
                    if (getColumnStatus(contractStatusCategoryCode, PAM_CODE)) {
                        tpaBobReportVO.setPam(resultSet.getString(PAM_CODE));
                        
                    if (getColumnStatus(contractStatusCategoryCode, ONLINE_BENEFICIARY_DESIGNATION_IND)) {
                            tpaBobReportVO.setOnlineBeneficiaryDesignation(resultSet.getString(ONLINE_BENEFICIARY_DESIGNATION_IND));
                    }
                    
                    if (getColumnStatus(contractStatusCategoryCode, ONLINE_WD_IND)) {
                        tpaBobReportVO.setOnlineWithdrawals(resultSet.getString(ONLINE_WD_IND));
                    }
                  
                    if (getColumnStatus(contractStatusCategoryCode, SYS_WD_IND)) {
                        tpaBobReportVO.setSysWithdrawals(resultSet.getString(SYS_WD_IND));
                    }
                     
                    if (getColumnStatus(contractStatusCategoryCode, SEND_SERVICE)) {
                        tpaBobReportVO.setSendService(resultSet.getString(SEND_SERVICE));
                    }
                   
                    
                    if (getColumnStatus(contractStatusCategoryCode, JHTC_PASSIVE_TRUSTEE_CODE)) {
                        tpaBobReportVO.setJohnHancockPassiveTrusteeCode(resultSet.getString(JHTC_PASSIVE_TRUSTEE_CODE));
                    }
                    
                    if (getColumnStatus(contractStatusCategoryCode, VESTING_CALC_CODE)) {
                        tpaBobReportVO.setVestingPercentage(resultSet.getString(VESTING_CALC_CODE));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, VESTING_ON_STMT_IND)) {
                        tpaBobReportVO.setVestingOnStatements(resultSet.getString(VESTING_ON_STMT_IND));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, PERMITTED_DISPARITY_IND)) {
                        tpaBobReportVO.setPermittedDisparity(resultSet.getString(PERMITTED_DISPARITY_IND));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, FSW_IND)) {
                        tpaBobReportVO.setFsw(resultSet.getString(FSW_IND));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, DEFAULT_INVESTMENT_OPTION)) {
                        tpaBobReportVO.setDio(createDIOList(resultSet
                               .getString(DEFAULT_INVESTMENT_OPTION))); 
                    if (getColumnStatus(contractStatusCategoryCode, TPA_SIGNING_AUTHORITY_IND)) {
                            tpaBobReportVO.setTpaSigningAuthority(resultSet
                                    .getString(TPA_SIGNING_AUTHORITY_IND)); 
                    }
                    if (getColumnStatus(contractStatusCategoryCode, COW_IND)) {
                        tpaBobReportVO.setCow(resultSet.getString(COW_IND));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, PAYROL_PATH_IND)) {
                        tpaBobReportVO.setPayRollPath(resultSet.getString(PAYROL_PATH_IND));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, PAYROLL_FEEDBACK_SVC_IND)) {
                        tpaBobReportVO.setPayrollFeedback(resultSet.getString(PAYROLL_FEEDBACK_SVC_IND));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, PLAN_HIGHLIGHTS_IND)) {
                        tpaBobReportVO.setPlanHighlights(resultSet.getString(PLAN_HIGHLIGHTS_IND));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, PLAN_HIGHLIGHTS_REVIEWED_IND)) {
                        tpaBobReportVO.setPlanHighlightsReviewed(resultSet.getString(PLAN_HIGHLIGHTS_REVIEWED_IND));
                    }
                    if (getColumnStatus(contractStatusCategoryCode, PIF_INSTALLMENTS_IND)) {
                        tpaBobReportVO.setInstallments(resultSet.getString(PIF_INSTALLMENTS_IND));
                    }
                    tpaBobReportList.add(tpaBobReportVO);
                }
                reportData.setDetails(tpaBobReportList);
						}
					}
				}
			
		} catch (SQLException e) {
            logger
                    .error("Problem occurred during TPA - BLOCK_OF_BUSINESS stored proc call. Report criteria:"
                            + criteria);
            throw new SystemException(e, className, "getReportData",
                    "Problem occurred during TPA - BLOCK_OF_BUSINESS stored proc call. Report criteria:"
                            + criteria);
        } finally {
            close(stmt, conn);
        }
        
        return reportData;
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
        return applicableColumnsMap.get(tabName).get(columnName);
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
        StringBuilder result = new StringBuilder();
        Iterator sorts = criteria.getSorts().iterator();
        String sortDirection = null;

        for (int i = 0; sorts.hasNext(); i++) {
            ReportSort sort = (ReportSort) sorts.next();

            sortDirection = sort.getSortDirection();

            result.append(fieldToColumnMap.get(sort.getSortField())).append(' ');
            result.append(sortDirection);

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

    /**
     * Build the filter phrase.
     * 
     * @param criteria - Report criteria
     * @return - String of filter phrase.
     */
    @SuppressWarnings("unchecked")
    private static String createFilterPhrase(ReportCriteria criteria) {

    	StringBuilder result = new StringBuilder();

        Map filters = criteria.getFilters();

        String contractName = (String) filters
                .get(TPABlockOfBusinessReportData.FILTER_CONTRACT_NAME_ID);
        if (!StringUtils.isBlank(contractName)) {
            result.append(CONTRACT_NAME).append(LIKE).append(
                    wrapInSingleQuotes(contractName.trim() + PERCENT_SYM).toUpperCase()).append(
                    SEMICOLON_SYMBOL);
        }

        String contractNumber = (String) filters
                .get(TPABlockOfBusinessReportData.FILTER_CONTRACT_NUMBER_ID);
        if (!StringUtils.isBlank(contractNumber)) {
            result.append(CONTRACT_ID).append(EQUAL_SYM).append(contractNumber.trim()).append(
                    SEMICOLON_SYMBOL);
        }

        String financialRepOrOrgName = (String) filters
                .get(TPABlockOfBusinessReportData.FILTER_FINANCIAL_REP_OR_ORG_NAME_ID);
        result.append(buildFilterClauseForFinancialRepName(financialRepOrOrgName));

        String usOrNy = (String) filters.get(TPABlockOfBusinessReportData.FILTER_US_OR_NY);
        if (!StringUtils.isEmpty(usOrNy)) {
            result.append(MANULIFE_COMPANY).append(EQUAL_SYM).append(
                    wrapInSingleQuotes(usOrNy).toUpperCase()).append(SEMICOLON_SYMBOL);
        }

        String carName = (String) filters.get(TPABlockOfBusinessReportData.FILTER_CAR_NAME_ID);
        if (!StringUtils.isBlank(carName)) {
            result.append(CAR_NAME_UCASE).append(LIKE).append(
                    wrapInSingleQuotes(carName.trim() + PERCENT_SYM)).append(SEMICOLON_SYMBOL);
        }
        
        String filterClause = StringUtils.removeEnd(result.toString(), SEMICOLON_SYMBOL);

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

    	StringBuilder result = new StringBuilder(300);

        if (!StringUtils.isBlank(financialRepOrOrgName)) {

            result.append(OPEN_BRACKET);

            // Find if the string contains a comma
            if (StringUtils.contains(financialRepOrOrgName, COMMA_SYMBOL)) {
                int numOfCommas = StringUtils.countMatches(financialRepOrOrgName, COMMA_SYMBOL);

                int commaIndex = 0;
                boolean firstIndex = true;
                for (; numOfCommas > 0; numOfCommas--) {
                    // Find the location of comma.
                    commaIndex = StringUtils.indexOf(financialRepOrOrgName, COMMA_SYMBOL,
                            commaIndex);

                    String stringBeforeComma = StringUtils.left(financialRepOrOrgName, commaIndex);
                    String stringAfterComma = StringUtils.right(financialRepOrOrgName,
                            financialRepOrOrgName.length() - commaIndex - 1);

                    if (firstIndex) {
                        firstIndex = false;
                    } else {
                        result.append(OR);
                    }

                    if (!StringUtils.isBlank(stringBeforeComma)
                            || !StringUtils.isBlank(stringAfterComma)) {
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

                    if (!StringUtils.isBlank(stringBeforeComma)
                            || !StringUtils.isBlank(stringAfterComma)) {
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
                            .toUpperCase()).append(CLOSE_BRACKET);

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
	 * The DIO information retrieved from stored proc is a long string
	 * containing the DIO options. Each of the DIO options are separated by a
	 * Separator "~^". This method separates out each of the DIO options are
	 * returns a array of it.
	 * 
	 * @param dioFromStoredProc
	 *            - String containing all the DIO options.
	 * @return - An ArrayList containing each of the DIO options.
	 */
    public static ArrayList<DefaultInvestmentFundVO> createDIOList(String dioFromStoredProc) {
        ArrayList<DefaultInvestmentFundVO> defaultInvestmentFunds = new ArrayList<DefaultInvestmentFundVO>();

        String SEPERATOR = "~^";
        String[] fundNamePctLifeCycleIndList = StringUtils.split(dioFromStoredProc, SEPERATOR);

        if (!StringUtils.isBlank(dioFromStoredProc) && fundNamePctLifeCycleIndList != null) {
        	// To get LifeCycle fund family name from a lookup table
        	Map<String, String> fundFamilyNames = EnvironmentServiceHelper
			.getInstance().getLifecycleFamilyNames();
        	
        	Collection<String> fundFamilyAdded = new ArrayList<String> ();
        	
            for (int i = 0; i < fundNamePctLifeCycleIndList.length; i++) {
                String fundName = fundNamePctLifeCycleIndList[i++];
                if (i >= fundNamePctLifeCycleIndList.length) {
                    break;
                }

                Integer fundPercentage = 0;
                try {
                    String fundPercentageS = fundNamePctLifeCycleIndList[i++];
                    if (!StringUtils.isEmpty(fundPercentageS)) {
                        // Stored proc returns the number with a '.' at the end. Removing it.
                        fundPercentageS = StringUtils.removeEnd(fundPercentageS.trim(), ".");

                        fundPercentage = Integer.parseInt(fundPercentageS);
                    }
                } catch (NumberFormatException nfe) {
                    // Do nothing. fund Percentage will be shown as 0.
                }
                if (i >= fundNamePctLifeCycleIndList.length) {
                    break;
                }

                String lifecycleFundInd = fundNamePctLifeCycleIndList[i++];

                DefaultInvestmentFundVO fund = new DefaultInvestmentFundVO();
                fund.setFundName(fundName);
                fund.setPercentage(fundPercentage);
                fund.setLifeCycleFund("Y".equalsIgnoreCase(lifecycleFundInd) ? Boolean.TRUE
                        : Boolean.FALSE);
                
                // Always add non-life cycle date funds
                if (!fund.isLifeCycleFund()) {

                    defaultInvestmentFunds.add(fund);
                } else {

                    // Adds one default investment fund for each family of Life cycle funds
                    if (!fundFamilyAdded.contains(fundNamePctLifeCycleIndList[i])) {
                    	fundFamilyAdded.add(fundNamePctLifeCycleIndList[i]);
                        fund.setFundName(fundFamilyNames.get(fundNamePctLifeCycleIndList[i]));
                        defaultInvestmentFunds.add(fund);
                    }
                }
            }
        }

        return defaultInvestmentFunds;
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
        defaultColumnsMapForTab.put(PLAN_REPORTING_YEAR_END_DATE, Boolean.TRUE);
        defaultColumnsMapForTab.put(NUMBER_OF_LIVES, Boolean.TRUE);
        defaultColumnsMapForTab.put(TOTAL_ASSET, Boolean.TRUE);
        defaultColumnsMapForTab.put(TRANS_ASSET_PRIOR_TO_CHARGES, Boolean.TRUE);
        defaultColumnsMapForTab.put(EXPECTED_FIRST_YEAR_ASSETS_AMT, Boolean.FALSE);
        defaultColumnsMapForTab.put(EXPECTED_NUM_OF_LIVES, Boolean.TRUE);
        defaultColumnsMapForTab.put(CAR_NAME, Boolean.TRUE);
        defaultColumnsMapForTab.put(BROKER_FULLNAME_LIST, Boolean.TRUE);
        defaultColumnsMapForTab.put(CONTRACT_STATUS_EFFECTIVE_DATE, Boolean.TRUE);
        defaultColumnsMapForTab.put(PRODUCT_TYPE, Boolean.TRUE);

        defaultColumnsMapForTab.put(TPA_FIRM_ID, Boolean.TRUE);
        defaultColumnsMapForTab.put(CONTRACT_STATUS_CODE, Boolean.TRUE);
        defaultColumnsMapForTab.put(EFFECTIVE_DATE, Boolean.TRUE);
        defaultColumnsMapForTab.put(ALLOCATED_ASSETS_AMT, Boolean.TRUE);
        defaultColumnsMapForTab.put(LOAN_ASSETS_AMT, Boolean.TRUE);
        defaultColumnsMapForTab.put(CASH_ACCOUNT_BALANCE_AMT, Boolean.TRUE);
        defaultColumnsMapForTab.put(PBA_ASSETS_AMT, Boolean.TRUE);
        defaultColumnsMapForTab.put(EZ_START_IND, Boolean.TRUE);
        defaultColumnsMapForTab.put(EZ_INCREASE_IND, Boolean.TRUE);
        defaultColumnsMapForTab.put(DIRECT_MAIL_IND, Boolean.TRUE);
        defaultColumnsMapForTab.put(GIFL_IND, Boolean.TRUE);
        defaultColumnsMapForTab.put(MA_SERVICE_CODE, Boolean.TRUE);
        defaultColumnsMapForTab.put(SMALL_PLAN_CODE, Boolean.TRUE);
        defaultColumnsMapForTab.put(PAM_CODE, Boolean.TRUE);
        defaultColumnsMapForTab.put(ONLINE_BENEFICIARY_DESIGNATION_IND, Boolean.TRUE);
        defaultColumnsMapForTab.put(ONLINE_WD_IND, Boolean.TRUE);

        defaultColumnsMapForTab.put(SYS_WD_IND, Boolean.TRUE);
        defaultColumnsMapForTab.put(SEND_SERVICE, Boolean.TRUE);
        defaultColumnsMapForTab.put(JHTC_PASSIVE_TRUSTEE_CODE, Boolean.TRUE);
        defaultColumnsMapForTab.put(VESTING_CALC_CODE, Boolean.TRUE);
        defaultColumnsMapForTab.put(VESTING_ON_STMT_IND, Boolean.TRUE);
        defaultColumnsMapForTab.put(PERMITTED_DISPARITY_IND, Boolean.TRUE);
        defaultColumnsMapForTab.put(FSW_IND, Boolean.TRUE);
        defaultColumnsMapForTab.put(DEFAULT_INVESTMENT_OPTION, Boolean.TRUE);
        defaultColumnsMapForTab.put(TPA_SIGNING_AUTHORITY_IND, Boolean.TRUE);
        defaultColumnsMapForTab.put(COW_IND, Boolean.TRUE);
        defaultColumnsMapForTab.put(PAYROL_PATH_IND, Boolean.TRUE);
        defaultColumnsMapForTab.put(PAYROLL_FEEDBACK_SVC_IND, Boolean.TRUE);        
        defaultColumnsMapForTab.put(PLAN_HIGHLIGHTS_IND, Boolean.TRUE);
        defaultColumnsMapForTab.put(PLAN_HIGHLIGHTS_REVIEWED_IND, Boolean.TRUE);
        defaultColumnsMapForTab.put(PIF_INSTALLMENTS_IND, Boolean.TRUE);
        
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
     * @param giflVersion
     * @return the Gifl product description like G.I.F.L or G.I.F.L Select based on the GIFL version
     *
     * 
     */
    private static String getGiflVersionProduct(String giflVersion){
    	if(giflVersion!=null){
    		return giflVersionValues.get(giflVersion.trim());
    	}
    	else{
    		return "";
    	}
    }
}
