package com.manulife.pension.bd.service.brokerListing.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.bd.service.brokerListing.valueobject.BrokerListingReportData;
import com.manulife.pension.bd.service.brokerListing.valueobject.BrokerListingReportVO;
import com.manulife.pension.bd.service.brokerListing.valueobject.BrokerListingSummaryVO;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.report.valueobject.ReportSortList;

/**
 * This class call the Broker Listing stored proc to retrieve the Broker Listing related report
 * data.
 * 
 * @author HArlomte
 * 
 */
public class BrokerListingDAO extends BaseDatabaseDAO {

    private static final Logger logger = Logger.getLogger(BrokerListingDAO.class);

    private static String className = BrokerListingDAO.class.getName();

    private static String GET_BOB = "{CALL " + BROKER_DEALER_SCHEMA_NAME
            + "BROKER_LISTING (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

    // Summary Section related DB Columns
    private static String USER_FULLNAME_SUMM_SECTION = "USER_FULLNAME";

    private static String TOTAL_ASSET_SUMM_SECTION = "TOTAL_ASSET";

    private static String CONTRACT_COUNT_SUMM_SECTION = "CONTRACT_COUNT";

    private static String BROKER_COUNT_SUMM_SECTION = "BROKER_COUNT";
    
    private static String ROW_COUNT_SUMM_SECTION = "ROW_COUNT";
    
    // Main Report related DB Columns
    private static String BROKER_FULLNAME = "BROKER_FULLNAME";

    private static String BROKER_FULLNAME_LIST = "BROKER_FULLNAME_LIST";

    private static String BROKER_DEALER_FIRM_NAME = "BROKER_DEALER_FIRM_NAME";

    private static String FINANCIAL_REP_LAST_NAME_UCASE = "UCASE(LAST_NAME)";

    private static String FINANCIAL_REP_FIRST_NAME_UCASE = "UCASE(FIRST_NAME)";

    private static String ORGANIZATION_NAME_UCASE = "UCASE(ORGANIZATION_NAME)";

    private static String CITY_NAME = "CITY_NAME";

    private static String STATE_CODE = "STATE_CODE";

    private static String ZIP_CODE = "ZIP_CODE";

    private static String PRODUCER_CODE = "PRODUCER_CODE";

    private static String CONTRACT_COUNT = "CONTRACT_COUNT";

    private static String TOTAL_ASSET = "TOTAL_ASSET";

    private static String PARTY_ID = "PARTY_ID";
    
    private static String PERCENT_SYM = "%";

    private static String EQUAL_SYM = "=";

    private static final String COMMA_SYMBOL = ",";

    private static final String SEMICOLON_SYMBOL = ";";

    private static String OR = " OR ";
    
    private static String AND = " AND ";

    private static String LIKE = " LIKE ";
    
    private static String OPEN_BRACKET = "(";

    private static String CLOSE_BRACKET = ")";

    private static Map<String, String> fieldToColumnMap;

    static {

        /**
         * This HashMap is used to map a given field Name to a "Database Column Name".
         */
        fieldToColumnMap = new HashMap<String, String>();
        fieldToColumnMap.put(BrokerListingReportData.COL_FINANCIAL_REP_NAME_ID, BROKER_FULLNAME_LIST);
        fieldToColumnMap.put(BrokerListingReportData.COL_FIRM_NAME_ID, BROKER_DEALER_FIRM_NAME);
        fieldToColumnMap.put(BrokerListingReportData.COL_CITY_ID, CITY_NAME);
        fieldToColumnMap.put(BrokerListingReportData.COL_STATE_ID, STATE_CODE);
        fieldToColumnMap.put(BrokerListingReportData.COL_ZIP_CODE_ID, ZIP_CODE);
        fieldToColumnMap.put(BrokerListingReportData.COL_PRODUCER_CODE_ID, PRODUCER_CODE);
        fieldToColumnMap.put(BrokerListingReportData.COL_NUM_OF_CONTRACTS_ID, CONTRACT_COUNT);
        fieldToColumnMap.put(BrokerListingReportData.COL_BL_TOTAL_ASSETS_ID, TOTAL_ASSET);

    }

    /**
     * This method retrieves the Report data specific to Broker Listing report.
     * 
     * @param criteria - Report Critieria containg criteria information such as as of date, userID,
     *            userRole, filtering information, sorting information, etc.
     * @return - Returns a ReportData object containing the information retrieved from the call to
     *         stored proc.
     * @throws SystemException
     */
    @SuppressWarnings("unchecked")
    public static ReportData getReportData(final ReportCriteria criteria) throws SystemException {
        
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getReportData");
        }
        
        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet resultSet = null;
        BrokerListingReportData brokerListingReportData = null;

        try {
            // setup the connection and the statement
            conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareCall(GET_BOB);

            if (logger.isDebugEnabled()) {
                logger.debug("Calling Stored Procedure: " + GET_BOB);
            }

            int idx = 1;

            Integer callSessionID = (Integer) criteria.getFilters().get(
                    BrokerListingReportData.FILTER_DB_SESSION_ID);
            stmt.setBigDecimal(idx++, intToBigDecimal(callSessionID));

            Long userProfileID = (Long) criteria.getFilters().get(
                    BrokerListingReportData.FILTER_USER_PROFILE_ID);
            stmt.setBigDecimal(idx++, new BigDecimal(userProfileID));

            String userRoleCode = (String) criteria.getFilters().get(
                    BrokerListingReportData.FILTER_USER_ROLE);
            stmt.setString(idx++, userRoleCode);

            Long mimicUserProfileID = (Long) criteria.getFilters().get(
                    BrokerListingReportData.FILTER_MIMIC_USER_PROFILE_ID);
            if (mimicUserProfileID == null) {
                stmt.setString(idx++, null);
            } else {
                stmt.setBigDecimal(idx++, new BigDecimal(mimicUserProfileID));
            }

            String mimicUserRoleCode = (String) criteria.getFilters().get(
                    BrokerListingReportData.FILTER_MIMIC_USER_ROLE);
            stmt.setString(idx++, mimicUserRoleCode);

            ArrayList<Integer> partyIDList = (ArrayList<Integer>) criteria.getFilters().get(
                    BrokerListingReportData.FILTER_PARTY_ID);

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

            String reportAsOfDate = (String) criteria.getFilters().get(
                    BrokerListingReportData.FILTER_AS_OF_DATE);
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

            stmt.registerOutParameter(idx++, Types.DECIMAL);

            stmt.registerOutParameter(idx, Types.CHAR);

            // execute the stored procedure
            stmt.execute();

            resultSet = stmt.getResultSet();

            BigDecimal callSessionID_out = stmt.getBigDecimal(idx - 1);
            Integer sessionID_out = callSessionID_out == null ? null : callSessionID_out.intValue();
            
            String resultTooBigInd = stmt.getString(idx);
            
            int totalCount = 0; 
            BrokerListingSummaryVO brokerListingSummaryVO = new BrokerListingSummaryVO();
            BigDecimal totalContractAssets = new BigDecimal("0.0");
            BigDecimal totalNumberOfContracts = new BigDecimal("0.0");
            BigDecimal totalNumberOfFinancialReps = new BigDecimal("0.0");
            
            if (resultSet != null) {
                while (resultSet.next()) {
                 // Result Set#1 has User Info, Contract Assets, Total Number of Contracts, Total
                    // Number of Financial Reps, to be shown in Summary section.
                    
                    if (isRVP(userRoleCode) && mimicUserProfileID == null) {
                        brokerListingSummaryVO.setInternalUserName(resultSet
                                .getString(USER_FULLNAME_SUMM_SECTION));
                    } else {
                        brokerListingSummaryVO.setAssociatedBrokerDealerFirmName(resultSet
                                .getString(USER_FULLNAME_SUMM_SECTION));
                    }
                    
                    if (resultSet.getBigDecimal(TOTAL_ASSET_SUMM_SECTION) != null) {
                        totalContractAssets = totalContractAssets.add(resultSet
                                .getBigDecimal(TOTAL_ASSET_SUMM_SECTION));
                    }

                    if (resultSet.getBigDecimal(CONTRACT_COUNT_SUMM_SECTION) != null) {
                        totalNumberOfContracts = totalNumberOfContracts.add(resultSet
                                .getBigDecimal(CONTRACT_COUNT_SUMM_SECTION));
                    }

                    if (resultSet.getBigDecimal(BROKER_COUNT_SUMM_SECTION) != null) {
                        totalNumberOfFinancialReps = totalNumberOfFinancialReps.add(resultSet
                                .getBigDecimal(BROKER_COUNT_SUMM_SECTION));
                    }
                    
                    if (resultSet.getBigDecimal(ROW_COUNT_SUMM_SECTION) != null) {
                        totalCount += resultSet.getInt(ROW_COUNT_SUMM_SECTION);
                    }
                }
            }
            brokerListingSummaryVO.setTotalContractAssets(totalContractAssets);
            brokerListingSummaryVO.setTotalNumberOfContracts(totalNumberOfContracts);
            brokerListingSummaryVO.setTotalNumberOfFinancialReps(totalNumberOfFinancialReps);            
            
            brokerListingReportData = new BrokerListingReportData(criteria, totalCount);
            brokerListingReportData.setDbSessionID(sessionID_out);
            
            brokerListingReportData.setResultTooBigInd("Y".equals(resultTooBigInd) ? Boolean.TRUE
                    : Boolean.FALSE); 

            brokerListingReportData.setBrokerListingSummaryVO(brokerListingSummaryVO);
            
            stmt.getMoreResults();
            resultSet = stmt.getResultSet();
            // Ignoring the result set #2. It just has the Broker Dealer Firm Name's. In the main
            // page, we show the Broker Dealer Firm Names in the SUmmary section for a Firm Rep
            // User. This info is taken from the UserProfile object itself.
            stmt.getMoreResults();
            resultSet = stmt.getResultSet();
            
            ArrayList<BrokerListingReportVO> brokerListingReport = new ArrayList<BrokerListingReportVO>();

            if (resultSet != null) {
                while (resultSet.next()) {
                    // Result Set #3 has the main report information.
                    BrokerListingReportVO brokerListingReportVO = new BrokerListingReportVO();
                    
                    brokerListingReportVO.setFinancialRepName(resultSet.getString(BROKER_FULLNAME));
                    brokerListingReportVO.setFirmName(resultSet.getString(BROKER_DEALER_FIRM_NAME));
                    brokerListingReportVO.setCity(resultSet.getString(CITY_NAME));
                    brokerListingReportVO.setState(resultSet.getString(STATE_CODE));
                    brokerListingReportVO.setZipCode(resultSet.getString(ZIP_CODE));
                    brokerListingReportVO.setProducerCode(resultSet.getString(PRODUCER_CODE));
                    brokerListingReportVO.setNumOfContracts(resultSet.getBigDecimal(CONTRACT_COUNT));
                    brokerListingReportVO.setTotalAssets(resultSet.getBigDecimal(TOTAL_ASSET));
                    brokerListingReportVO.setPartyID(resultSet.getString(PARTY_ID));
                    
                    brokerListingReport.add(brokerListingReportVO);
                }
            }
            brokerListingReportData.setDetails(brokerListingReport);

        } catch (SQLException e) {
            logger.error("Problem occurred during BROKER_LISTING stored proc call. Report criteria:"
                    + criteria);
            throw new SystemException(e, className, "getReportData",
                    "Problem occurred during BROKER_LISTING stored proc call. Report criteria:"
                            + criteria);
        } finally {
            close(stmt, conn);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> getReportData");
        }
        
        return brokerListingReportData;
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
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> createFilterPhrase");
        }
        StringBuffer result = new StringBuffer();

        Map filters = criteria.getFilters();

        String financialRepOrOrgName = (String) filters
                .get(BrokerListingReportData.FILTER_FINANCIALREP_NAME_ID);
        result.append(buildFilterClauseForFinancialRepName(financialRepOrOrgName));

        String cityName = (String) filters.get(BrokerListingReportData.FILTER_CITY_NAME_ID);
        if (!StringUtils.isEmpty(cityName)) {
            result.append(CITY_NAME).append(LIKE).append(
                    wrapInSingleQuotes(cityName.trim() + PERCENT_SYM).toUpperCase()).append(
                    SEMICOLON_SYMBOL);
        }

        String contractState = (String) filters.get(BrokerListingReportData.FILTER_STATE_CODE_ID);
        if (!StringUtils.isEmpty(contractState)) {
            result.append(STATE_CODE).append(EQUAL_SYM).append(
                    wrapInSingleQuotes(contractState).toUpperCase()).append(SEMICOLON_SYMBOL);
        }

        // the User can see a 9 digit zip code, but he will be able to enter only the first 5 digits
        // of ZIP code as filtering criteria.
        String zipCode = (String) filters.get(BrokerListingReportData.FILTER_ZIP_CODE_ID);
        if (!StringUtils.isEmpty(zipCode)) {
            result.append(ZIP_CODE).append(LIKE).append(
                    wrapInSingleQuotes(zipCode.trim() + PERCENT_SYM).toUpperCase()).append(
                    SEMICOLON_SYMBOL);
        }

        String producerCode = (String) filters.get(BrokerListingReportData.FILTER_PRODUCER_CODE_ID);
        if (!StringUtils.isEmpty(producerCode)) {
            result.append(PRODUCER_CODE).append(EQUAL_SYM).append(producerCode.trim()).append(
                    SEMICOLON_SYMBOL);
        }

        // Note: RVP, SALES REGION, SALES DIVISION, FIRM NAME filters are taken care as part of
        // partyID List except for RVP, Firm Rep. For RVP, Firm Rep, the Firm name filter is sent as
        // the Firm Name itself, instead of sending it as PartyID.
        String bdFirmName = (String) filters.get(BrokerListingReportData.FILTER_BROKER_DEALER_FIRM_NAME);
        if (!StringUtils.isEmpty(bdFirmName)) {
            result.append(BROKER_DEALER_FIRM_NAME).append(EQUAL_SYM).append(
                    wrapInSingleQuotes(bdFirmName.trim())).append(SEMICOLON_SYMBOL);
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
     * This method will check if the user is a RVP or not.
     * 
     * @param userRoleCode
     * @return
     */
    private static Boolean isRVP(String userRoleCode) {
        return "RVP".equals(userRoleCode);
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

}
