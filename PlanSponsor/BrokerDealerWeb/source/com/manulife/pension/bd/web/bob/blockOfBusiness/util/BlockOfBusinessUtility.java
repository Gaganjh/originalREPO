package com.manulife.pension.bd.web.bob.blockOfBusiness.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.delegate.BrokerServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.delegate.ReportServiceDelegate;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.platform.web.util.FundClassUtility;
import com.manulife.pension.ps.service.report.bob.valueobject.BlockOfBusinessReportData;
import com.manulife.pension.service.broker.exception.InvalidIdException;
import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;
import com.manulife.pension.service.broker.valueobject.RegionalVicePresident;
import com.manulife.pension.service.broker.valueobject.SalesDivision;
import com.manulife.pension.service.broker.valueobject.SalesRegion;
import com.manulife.pension.service.contract.util.SmallPlanFeature;
import com.manulife.pension.service.environment.valueobject.DeCodeVO;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.service.security.bd.handler.BDSecurityProfileHandler;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.role.BDAdministrator;
import com.manulife.pension.service.security.role.BDContentManager;
import com.manulife.pension.service.security.role.BDFinancialRepAssistant;
import com.manulife.pension.service.security.role.BDFirmRep;
import com.manulife.pension.service.security.role.BDInternalBasic;
import com.manulife.pension.service.security.role.BDNationalAccounts;
import com.manulife.pension.service.security.role.BDRvp;
import com.manulife.pension.service.security.role.BDUserRole;
import com.manulife.pension.service.security.role.RIAUserManager;
import com.manulife.pension.util.log.ServiceLogRecord;

/**
 * This class is a Helper class for Block of Business page.
 * 
 * @author HArlomte
 * 
 */
public class BlockOfBusinessUtility {

    public static Map<String, String> contractStatusMap;
    
    public static Map<String, String> contractStatusTitleMap;

    public static Map<String, Object> cacheMap;
    
    // Used in BlockOfBusinessCommonAction.
    public static ArrayList<String> columnsList;
    
    public static ArrayList<String> columnsListPDF;

    // Will hold the columns whose header has to be Center aligned.
    public static ArrayList<String> centerAlignedColumnHeaderList;

    // Will hold the columns whose values has to be right aligned.
    public static ArrayList<String> rightAlignedColumnsList;

    // Will hold the columns whose values has to be Center aligned.
    public static ArrayList<String> centerAlignedColumnsList;

    public static ArrayList<String> filterList;

    public static ArrayList<String> quickFilterList;

    public static ArrayList<String> quickFilterOrderList;

    public static final Logger logger = Logger.getLogger(BlockOfBusinessUtility.class);
    
    static {
        
        cacheMap = new HashMap<String, Object>();
        
        /**
         * This Map is used to get the Filter Criteria value to be sent to the BlockOfBusinessDAO.
         */
        contractStatusMap = new HashMap<String, String>();
        contractStatusMap.put(BDConstants.ACTIVE_TAB, BDConstants.ACTIVE_TAB_VAL);
        contractStatusMap.put(BDConstants.OUTSTANDING_PROPOSALS_TAB,
                BDConstants.OUTSTANDING_PROPOSALS_TAB_VAL);
        contractStatusMap.put(BDConstants.PENDING_TAB, BDConstants.PENDING_TAB_VAL);
        contractStatusMap.put(BDConstants.DISCONTINUED_TAB, BDConstants.DISCONTINUED_TAB_VAL);
        
        /**
         * This Map is used to get the Title of the Tab, to be shown in CSV, PDF.
         */
        contractStatusTitleMap = new HashMap<String, String>();
        contractStatusTitleMap.put(BDConstants.ACTIVE_TAB, BDConstants.ACTIVE_TAB_TITLE);
        contractStatusTitleMap.put(BDConstants.OUTSTANDING_PROPOSALS_TAB,
                BDConstants.OUTSTANDING_PROPOSALS_TAB_TITLE);
        contractStatusTitleMap.put(BDConstants.PENDING_TAB, BDConstants.PENDING_TAB_TITLE);
        contractStatusTitleMap
                .put(BDConstants.DISCONTINUED_TAB, BDConstants.DISCONTINUED_TAB_TITLE);
        
        /**
         * This list will be used by CSV, PDF functionality to display the applicable columns.
         */
        columnsList = new ArrayList<String>(24);
        columnsList.add(BlockOfBusinessReportData.COL_CONTRACT_NAME_ID);
        columnsList.add(BlockOfBusinessReportData.COL_CONTRACT_NUMBER_ID);
        columnsList.add(BlockOfBusinessReportData.COL_PROPOSAL_NAME_ID);
        columnsList.add(BlockOfBusinessReportData.COL_PROPOSAL_NUMBER_ID);
        columnsList.add(BlockOfBusinessReportData.COL_CONTRACT_EFF_DT_ID);
        columnsList.add(BlockOfBusinessReportData.COL_PROPOSAL_DT_ID);
        columnsList.add(BlockOfBusinessReportData.COL_CONTRACT_PLAN_YEAR_END_MMDD_ID);
        columnsList.add(BlockOfBusinessReportData.COL_CONTRACT_STATE_ID);
        columnsList.add(BlockOfBusinessReportData.COL_NUM_OF_LIVES);
        columnsList.add(BlockOfBusinessReportData.COL_TOTAL_ASSETS_ID);
        columnsList.add(BlockOfBusinessReportData.COL_TRANSFERREDOUT_ASSETS_PRIORTO_CHARGES_ID);
        columnsList.add(BlockOfBusinessReportData.COL_ASSET_CHARGE_ID);
        columnsList.add(BlockOfBusinessReportData.COL_EXPECTED_FIRST_YEAR_ASSETS_ID);
        columnsList.add(BlockOfBusinessReportData.COMMISSION_DEPOSIT_TR_1YR);
        columnsList.add(BlockOfBusinessReportData.COMMISSION_DEPOSIT_REG_1YR);
        columnsList.add(BlockOfBusinessReportData.COMMISSION_DEPOSIT_REG_REN);
        columnsList.add(BlockOfBusinessReportData.COMMISSION_ASSET_1YR);
        columnsList.add(BlockOfBusinessReportData.COMMISSION_ASSET_REN);
        columnsList.add(BlockOfBusinessReportData.COMMISSION_ASSET_ALL_YRS);
        columnsList.add(BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_FEE_AMT);
        columnsList.add(BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_MIN_FEE_AMT);
        columnsList.add(BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_MAX_FEE_AMT);
        columnsList.add(BlockOfBusinessReportData.RIA_ASSET_BASED_BLENDED_FEE_AMT);
        columnsList.add(BlockOfBusinessReportData.RIA_ASSET_BASED_TIERED_FEE_AMT);
        columnsList.add(BlockOfBusinessReportData.RIA_FLAT_PER_HEAD_FEE_AMT);
        columnsList.add(BlockOfBusinessReportData.RIA_FLAT_PRORATA_FEE_AMT);
        columnsList.add(BlockOfBusinessReportData.COFID_321_ASSET_BASED_BPS_FEE);
        columnsList.add(BlockOfBusinessReportData.COFID_321_DOLLAR_BASED_FEE_AMT);
        columnsList.add(BlockOfBusinessReportData.COFID_338_ASSET_BASED_BPS_FEE);
        columnsList.add(BlockOfBusinessReportData.COFID_338_DOLLAR_BASED_FEE_AMT);
        columnsList.add(BlockOfBusinessReportData.RIA_338_DESIGNATION_INDICATOR);
        columnsList.add(BlockOfBusinessReportData.COL_COMMISSIONS_RIA_FEES_ID);
        columnsList.add(BlockOfBusinessReportData.COL_PRODUCER_CODES_OF_BROKERS_ID);
        columnsList.add(BlockOfBusinessReportData.COL_NAMES_OF_THE_BROKERS_ID);
        columnsList.add(BlockOfBusinessReportData.COL_BDFIRM_NAME_ID);
        columnsList.add(BlockOfBusinessReportData.COL_RVP_ID);
        columnsList.add(BlockOfBusinessReportData.COL_PRODUCT_TYPE_ID);
        columnsList.add(BlockOfBusinessReportData.COL_US_OR_NY_ID);
        columnsList.add(BlockOfBusinessReportData.COL_CLASS_ID);
        columnsList.add(BlockOfBusinessReportData.COL_DISCONTINUED_DATE_ID);
        
        /**
         * This list will be used by PDF functionality to display the applicable columns.
         */
        columnsListPDF = new ArrayList<String>(24);
        columnsListPDF.add(BlockOfBusinessReportData.COL_CONTRACT_NAME_ID);
        columnsListPDF.add(BlockOfBusinessReportData.COL_CONTRACT_NUMBER_ID);
        columnsListPDF.add(BlockOfBusinessReportData.COL_PROPOSAL_NAME_ID);
        columnsListPDF.add(BlockOfBusinessReportData.COL_PROPOSAL_NUMBER_ID);
        columnsListPDF.add(BlockOfBusinessReportData.COL_CONTRACT_EFF_DT_ID);
        columnsListPDF.add(BlockOfBusinessReportData.COL_PROPOSAL_DT_ID);
        columnsListPDF.add(BlockOfBusinessReportData.COL_CONTRACT_PLAN_YEAR_END_MMDD_ID);
        columnsListPDF.add(BlockOfBusinessReportData.COL_NUM_OF_LIVES);
        columnsListPDF.add(BlockOfBusinessReportData.COL_TOTAL_ASSETS_ID);
        columnsListPDF.add(BlockOfBusinessReportData.COL_TRANSFERREDOUT_ASSETS_PRIORTO_CHARGES_ID);
        columnsListPDF.add(BlockOfBusinessReportData.COL_ASSET_CHARGE_ID);
        columnsListPDF.add(BlockOfBusinessReportData.COL_EXPECTED_FIRST_YEAR_ASSETS_ID);
        columnsListPDF.add(BlockOfBusinessReportData.COMMISSION_DEPOSIT_TR_1YR);
        columnsListPDF.add(BlockOfBusinessReportData.COMMISSION_DEPOSIT_REG_1YR);
        columnsListPDF.add(BlockOfBusinessReportData.COMMISSION_DEPOSIT_REG_REN);
        columnsListPDF.add(BlockOfBusinessReportData.COMMISSION_ASSET_1YR);
        columnsListPDF.add(BlockOfBusinessReportData.COMMISSION_ASSET_REN);
        columnsListPDF.add(BlockOfBusinessReportData.COMMISSION_ASSET_ALL_YRS);
        columnsListPDF.add(BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_FEE_AMT);
        columnsListPDF.add(BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_MIN_FEE_AMT);
        columnsListPDF.add(BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_MAX_FEE_AMT);
        columnsListPDF.add(BlockOfBusinessReportData.RIA_ASSET_BASED_BLENDED_FEE_AMT);
        columnsListPDF.add(BlockOfBusinessReportData.RIA_ASSET_BASED_TIERED_FEE_AMT);
        columnsListPDF.add(BlockOfBusinessReportData.RIA_FLAT_PER_HEAD_FEE_AMT);
        columnsListPDF.add(BlockOfBusinessReportData.RIA_FLAT_PRORATA_FEE_AMT);
        columnsListPDF.add(BlockOfBusinessReportData.COFID_321_ASSET_BASED_BPS_FEE);
        columnsListPDF.add(BlockOfBusinessReportData.COFID_321_DOLLAR_BASED_FEE_AMT);
        columnsListPDF.add(BlockOfBusinessReportData.COFID_338_ASSET_BASED_BPS_FEE);
        columnsListPDF.add(BlockOfBusinessReportData.COFID_338_DOLLAR_BASED_FEE_AMT);
        columnsListPDF.add(BlockOfBusinessReportData.RIA_338_DESIGNATION_INDICATOR);
        columnsListPDF.add(BlockOfBusinessReportData.COL_COMMISSIONS_RIA_FEES_ID);
        columnsListPDF.add(BlockOfBusinessReportData.COL_PRODUCER_CODES_OF_BROKERS_ID);
        columnsListPDF.add(BlockOfBusinessReportData.COL_NAMES_OF_THE_BROKERS_ID);
        columnsListPDF.add(BlockOfBusinessReportData.COL_BDFIRM_NAME_ID);
        columnsListPDF.add(BlockOfBusinessReportData.COL_PRODUCT_TYPE_ID);
        columnsListPDF.add(BlockOfBusinessReportData.COL_US_OR_NY_ID);
        columnsListPDF.add(BlockOfBusinessReportData.COL_CLASS_ID);
        columnsListPDF.add(BlockOfBusinessReportData.COL_DISCONTINUED_DATE_ID);

        
        /**
         * This will hold the names of those column headers which need to be center aligned. This
         * information will be passed to the PDF.
         */
        centerAlignedColumnHeaderList = new ArrayList<String>(10);
        centerAlignedColumnHeaderList.add(BlockOfBusinessReportData.COL_CLASS_ID);
        centerAlignedColumnHeaderList.add(BlockOfBusinessReportData.COL_US_OR_NY_ID);
        centerAlignedColumnHeaderList.add(BlockOfBusinessReportData.COL_TOTAL_ASSETS_ID);
        centerAlignedColumnHeaderList.add(BlockOfBusinessReportData.COL_TRANSFERREDOUT_ASSETS_PRIORTO_CHARGES_ID);
        centerAlignedColumnHeaderList.add(BlockOfBusinessReportData.COL_ASSET_CHARGE_ID);
        centerAlignedColumnHeaderList.add(BlockOfBusinessReportData.COL_EXPECTED_FIRST_YEAR_ASSETS_ID);
        centerAlignedColumnHeaderList.add(BlockOfBusinessReportData.COMMISSION_DEPOSIT_TR_1YR);
        centerAlignedColumnHeaderList.add(BlockOfBusinessReportData.COMMISSION_DEPOSIT_REG_1YR);
        centerAlignedColumnHeaderList.add(BlockOfBusinessReportData.COMMISSION_DEPOSIT_REG_REN);
        centerAlignedColumnHeaderList.add(BlockOfBusinessReportData.COMMISSION_ASSET_1YR);
        centerAlignedColumnHeaderList.add(BlockOfBusinessReportData.COMMISSION_ASSET_REN);
        centerAlignedColumnHeaderList.add(BlockOfBusinessReportData.COMMISSION_ASSET_ALL_YRS);
        centerAlignedColumnHeaderList.add(BlockOfBusinessReportData.RIA_FLAT_PRORATA_FEE_AMT);
        centerAlignedColumnHeaderList.add(BlockOfBusinessReportData.RIA_FLAT_PER_HEAD_FEE_AMT);
        centerAlignedColumnHeaderList.add(BlockOfBusinessReportData.RIA_FLAT_PER_HEAD_FEE_AMT);
        centerAlignedColumnHeaderList.add(BlockOfBusinessReportData.RIA_ASSET_BASED_TIERED_FEE_AMT);
        centerAlignedColumnHeaderList.add(BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_MIN_FEE_AMT);
        centerAlignedColumnHeaderList.add(BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_MAX_FEE_AMT);
        centerAlignedColumnHeaderList.add(BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_FEE_AMT);
        centerAlignedColumnHeaderList.add(BlockOfBusinessReportData.RIA_ASSET_BASED_BLENDED_FEE_AMT);
        centerAlignedColumnHeaderList.add(BlockOfBusinessReportData.COFID_321_ASSET_BASED_BPS_FEE);
        centerAlignedColumnHeaderList.add(BlockOfBusinessReportData.COFID_321_DOLLAR_BASED_FEE_AMT);
        centerAlignedColumnHeaderList.add(BlockOfBusinessReportData.COFID_338_ASSET_BASED_BPS_FEE);
        centerAlignedColumnHeaderList.add(BlockOfBusinessReportData.COFID_338_DOLLAR_BASED_FEE_AMT);
        centerAlignedColumnHeaderList.add(BlockOfBusinessReportData.RIA_338_DESIGNATION_INDICATOR);
        centerAlignedColumnHeaderList.add(BlockOfBusinessReportData.COL_COMMISSIONS_RIA_FEES_ID);
 
        
        /**
         * This will hold the names of those columns which need to be right aligned. This
         * information will be passed to the PDF.
         */
        rightAlignedColumnsList = new ArrayList<String>(8);
        rightAlignedColumnsList.add(BlockOfBusinessReportData.COL_TOTAL_ASSETS_ID);
        rightAlignedColumnsList.add(BlockOfBusinessReportData.COL_TRANSFERREDOUT_ASSETS_PRIORTO_CHARGES_ID);
        rightAlignedColumnsList.add(BlockOfBusinessReportData.COL_ASSET_CHARGE_ID);
        rightAlignedColumnsList.add(BlockOfBusinessReportData.COL_EXPECTED_FIRST_YEAR_ASSETS_ID);
        rightAlignedColumnsList.add(BlockOfBusinessReportData.COMMISSION_DEPOSIT_TR_1YR);
        rightAlignedColumnsList.add(BlockOfBusinessReportData.COMMISSION_DEPOSIT_REG_1YR);
        rightAlignedColumnsList.add(BlockOfBusinessReportData.COMMISSION_DEPOSIT_REG_REN);
        rightAlignedColumnsList.add(BlockOfBusinessReportData.COMMISSION_ASSET_1YR);
        rightAlignedColumnsList.add(BlockOfBusinessReportData.COMMISSION_ASSET_REN);
        rightAlignedColumnsList.add(BlockOfBusinessReportData.COMMISSION_ASSET_ALL_YRS);
        rightAlignedColumnsList.add(BlockOfBusinessReportData.RIA_FLAT_PRORATA_FEE_AMT);
        rightAlignedColumnsList.add(BlockOfBusinessReportData.RIA_FLAT_PER_HEAD_FEE_AMT);
        rightAlignedColumnsList.add(BlockOfBusinessReportData.RIA_ASSET_BASED_TIERED_FEE_AMT);
        rightAlignedColumnsList.add(BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_MIN_FEE_AMT);
        rightAlignedColumnsList.add(BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_MAX_FEE_AMT);
        rightAlignedColumnsList.add(BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_FEE_AMT);
        rightAlignedColumnsList.add(BlockOfBusinessReportData.RIA_ASSET_BASED_BLENDED_FEE_AMT);
        rightAlignedColumnsList.add(BlockOfBusinessReportData.COFID_321_ASSET_BASED_BPS_FEE);
        rightAlignedColumnsList.add(BlockOfBusinessReportData.COFID_321_DOLLAR_BASED_FEE_AMT);
        rightAlignedColumnsList.add(BlockOfBusinessReportData.COFID_338_ASSET_BASED_BPS_FEE);
        rightAlignedColumnsList.add(BlockOfBusinessReportData.COFID_338_DOLLAR_BASED_FEE_AMT);
        rightAlignedColumnsList.add(BlockOfBusinessReportData.RIA_338_DESIGNATION_INDICATOR);
        rightAlignedColumnsList.add(BlockOfBusinessReportData.COL_COMMISSIONS_RIA_FEES_ID);
        
        /**
         * This will hold the names of those columns which need to be center aligned. This
         * information will be passed to the PDF.
         */
        centerAlignedColumnsList = new ArrayList<String>(2);
        centerAlignedColumnsList.add(BlockOfBusinessReportData.COL_CLASS_ID);
        centerAlignedColumnsList.add(BlockOfBusinessReportData.COL_US_OR_NY_ID);
        
        /**
         * This list will be used to show the "Filters Used" in CSV, PDF.
         */
        filterList = new ArrayList<String>(13);
        filterList.add(BlockOfBusinessReportData.FILTER_CONTRACT_NAME);
        filterList.add(BlockOfBusinessReportData.FILTER_CONTRACT_NUMBER);
        filterList.add(BlockOfBusinessReportData.FILTER_FINANCIAL_REP_NAME);
        filterList.add(BlockOfBusinessReportData.FILTER_BDFIRM_NAME);
        filterList.add(BlockOfBusinessReportData.FILTER_ASSET_RANGE_FROM);
        filterList.add(BlockOfBusinessReportData.FILTER_ASSET_RANGE_TO);
        filterList.add(BlockOfBusinessReportData.FILTER_CONTRACT_STATE);
        filterList.add(BlockOfBusinessReportData.FILTER_FUND_CLASS);
        filterList.add(BlockOfBusinessReportData.FILTER_RPV_NAME);
        filterList.add(BlockOfBusinessReportData.FILTER_SALES_REGION);
        filterList.add(BlockOfBusinessReportData.FILTER_SALES_DIVISION);
        filterList.add(BlockOfBusinessReportData.FILTER_CSF_FEATURE);
        filterList.add(BlockOfBusinessReportData.FILTER_US_OR_NY);
        filterList.add(BlockOfBusinessReportData.FILTER_PRODUCT_TYPE);

        /**
         * This list will be used to show the "Filters Used" in CSV, PDF.
         */
        quickFilterList = new ArrayList<String>(5);
        quickFilterList.add(BlockOfBusinessReportData.FILTER_CONTRACT_NAME);
        quickFilterList.add(BlockOfBusinessReportData.FILTER_CONTRACT_NUMBER);
        quickFilterList.add(BlockOfBusinessReportData.FILTER_RPV_NAME);
        quickFilterList.add(BlockOfBusinessReportData.FILTER_BDFIRM_NAME);
        quickFilterList.add(BlockOfBusinessReportData.FILTER_PRODUCT_TYPE);
        quickFilterList.add(BlockOfBusinessReportData.FILTER_US_OR_NY);
        quickFilterList.add(BlockOfBusinessReportData.FILTER_FUND_CLASS);

        
        /**
         * This is a list of quick Filters in the order they should be displayed in JSP page.
         */
        quickFilterOrderList = new ArrayList<String>(7);
        quickFilterOrderList.add(BDConstants.FILTER_BLANK_CODE);
        quickFilterOrderList.add(BlockOfBusinessReportData.FILTER_CONTRACT_NAME);
        quickFilterOrderList.add(BlockOfBusinessReportData.FILTER_CONTRACT_NUMBER);
        quickFilterOrderList.add(BlockOfBusinessReportData.FILTER_RPV_NAME);
        quickFilterOrderList.add(BlockOfBusinessReportData.FILTER_BDFIRM_NAME);
        quickFilterOrderList.add(BlockOfBusinessReportData.FILTER_PRODUCT_TYPE);
        quickFilterOrderList.add(BlockOfBusinessReportData.FILTER_US_OR_NY);
        quickFilterOrderList.add(BlockOfBusinessReportData.FILTER_FUND_CLASS);
    }

    /**
     * This method will give the "Contract Status" value to be sent to the stored proc, for a given
     * tab.
     * 
     * @param currentTab - the tab name.
     * @return - the "Contract Status" value to be sent to stored proc.
     */
    public static String getContractStatus(String currentTab) {
        if (currentTab == null) {
            return null;
        }
        return contractStatusMap.get(currentTab);
    }
    
    /**
     * Helper method to create a list of 24 calendar month end dates. This method calls the
     * getMonthEndDates(Date), passing the asOfDate.
     * 
     * @return
     * @throws SystemException
     */
    public static List<Date> getMonthEndDates() throws SystemException {
        Date asOfDate = EnvironmentServiceDelegate.getInstance(BDConstants.BD_APPLICATION_ID)
                .getAsOfDate();
        return getMonthEndDates(asOfDate);
    }

    /**
     * Helper method to create a list of 24 calendar month end dates, based on given asOfDate.
     * 
     * @return - List of "Date" objects for the past 24 month end dates.
     * @throws SystemException
     */
    public static List<Date> getMonthEndDates(Date in_asOfDate) {
        List<Date> dates = new ArrayList<Date>();
        
        if (in_asOfDate == null) {
            return null;
        }
        
        Date asOfDate = in_asOfDate;
        dates.add(asOfDate);
        // if asOfdate is Nov 5 2008, take previous 24 month end dates starting from Oct 31 2008.
        // if asOfdate is Nov 30 2008, take previous 24 month end dates starting from Oct 31 2008.
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(asOfDate);
        // Go to previous month
        cal.add(Calendar.MONTH, BDConstants.NUM_MINUS_1);
        int lastDayOfPreviousMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, lastDayOfPreviousMonth);
        // cal currently has the previous month end date.
        dates.add(cal.getTime());
        
        // add previous 23 month end dates.
        for (int i = 0; i < BDConstants.NUM_23; i++) {
            cal.add(Calendar.MONTH, BDConstants.NUM_MINUS_1);
            int lastDayOfTheMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            cal.set(Calendar.DAY_OF_MONTH, lastDayOfTheMonth);
            dates.add(cal.getTime());
        }

        return dates;
    }

    /**
     * This method checks if the user has selected the Default Date or not.
     * 
     * @param asOfDateSelected - the Date selected by the user in Long Format, converted to String.
     * @return - True if the asOfDateSelected is equal to default date, else, false.
     * @throws SystemException
     */
    public static Boolean isDefaultDateSelected(String asOfDateSelected) throws SystemException {
        Boolean isDefaultDateSelected = Boolean.FALSE;

        Date asOfDate = BlockOfBusinessUtility.getMonthEndDates() == null ? null
                : BlockOfBusinessUtility.getMonthEndDates().get(0);
        String asOfDateLongFormat = asOfDate == null ? null : Long.valueOf(asOfDate.getTime())
                .toString();

        if (StringUtils.isEmpty(asOfDateSelected) || asOfDateSelected.equals(asOfDateLongFormat)) {
            isDefaultDateSelected = Boolean.TRUE;
        }

        return isDefaultDateSelected;
    }
    /**
     * This method gets a List which has two values "US", "NY".
     * 
     * @return - List having two values "US", "NY"
     */
    @SuppressWarnings("unchecked")
    public static List<LabelValueBean> getUSNYList() {
        
        List<LabelValueBean> usNyList = (List<LabelValueBean>) cacheMap
                .get(BDConstants.US_NY_LIST);
        
        if (usNyList == null || usNyList.isEmpty()) {
            // us, ny List
            usNyList = new ArrayList<LabelValueBean>(2);
            usNyList.add(new LabelValueBean(BDConstants.US, BDConstants.US));
            usNyList.add(new LabelValueBean(BDConstants.NY, BDConstants.NY));
            
            cacheMap.put(BDConstants.US_NY_LIST, usNyList);
        }

        return usNyList;
    }
    
    /**
     * This method gets fund family options map
     * 
     * @return -  Map<String, String>
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getFundFamilyOptionsMap() {
    	Map<String, String> fundFamilyCodeMap = (Map<String, String>) cacheMap.get(BDConstants.FUND_FAMILY_LOOKUP_MAP);
        return fundFamilyCodeMap;
    }
    
    /**
     * This method gets a List which has two values "DB", "DC".
     * 
     * @return - List having two values "DB", "DC"
     */
    @SuppressWarnings("unchecked")
    public static List<LabelValueBean> getProductTypeList() {
        
        List<LabelValueBean> productTypeList = (List<LabelValueBean>) cacheMap
                .get(BDConstants.PRODUCT_TYPE_LIST);
        
        if (productTypeList == null || productTypeList.isEmpty()) {
            // DB, DC List
        	productTypeList = new ArrayList<LabelValueBean>(2);
        	productTypeList.add(new LabelValueBean(BDConstants.DEFINED_BENEFIT, BDConstants.DEFINED_BENEFIT));
        	productTypeList.add(new LabelValueBean(BDConstants.DC, BDConstants.DC));
            
            cacheMap.put(BDConstants.PRODUCT_TYPE_LIST, productTypeList);
        }

        return productTypeList;
    }


    /**
     * This method gets a list of Fund Classes available in Database.
     * 
     * @return - List of Fund Classes.
     * @throws SystemException
     */
    public static Collection<DeCodeVO> getFundClassesList() throws SystemException {
        
        Collection<DeCodeVO> classList = new ArrayList<DeCodeVO>();
        // DeCodeVO has CLASS_ID, CLASS_NAME of each class, ordered by CLASS_ORDER_NO.
        Collection<DeCodeVO> tempClassList = FundServiceDelegate.getInstance()
                .getAllFundsClassList();
        for (DeCodeVO classInfo : tempClassList) {
            String className = FundClassUtility.getInstance().getFundClassName(
                    classInfo.getCode());
            classList.add(new DeCodeVO(classInfo.getCode(), className));
        }
            
        return classList;
    }

    /**
     * This method gets a list of States.
     * 
     * @return - List of States.
     * @throws SystemException
     */
    public static Collection<DeCodeVO> getStatesList() throws SystemException {
        
        Collection<DeCodeVO> statesList = EnvironmentServiceDelegate.getInstance(
                BDConstants.BD_APPLICATION_ID).getUSAStatesCollectionWithoutMilitary();
            
        return statesList;
    }

    /**
     * This method gives back a list of BDFirms associated to a BDFirmRep user.
     * 
     * @param userProfile
     * @return
     * @throws SystemException
     */
    public static List<BrokerDealerFirm> getAssociatedFirmsForBDFirmRep(BDUserProfile userProfile) {
        List<BrokerDealerFirm> bdFirms = null;
        if (userProfile.getRole() instanceof BDFirmRep) {
            bdFirms = ((BDFirmRep) userProfile.getRole()).getBrokerDealerFirmEntities();    
        }
        return bdFirms;
    }

    /**
     * Get the Firm Name for a BDFirmRep User given the firm ID.
     * 
     * @param userProfile - BDFirm Rep user Profile object.
     * @param firmIdSelected - firmID for which we need to get the corresponding firm Name.
     * @return - firm Name.
     */
    public static String getFirmNameForAssociatedFirmID(BDUserProfile userProfile,
            String firmIdSelected) {
        String firmName = BDConstants.SPACE_SYMBOL;

        if (!StringUtils.isEmpty(firmIdSelected)) {
            List<BrokerDealerFirm> bdFirms = BlockOfBusinessUtility
                    .getAssociatedFirmsForBDFirmRep(userProfile);
            if (bdFirms != null && !bdFirms.isEmpty()) {
                for (BrokerDealerFirm bdFirm : bdFirms) {
                    if (firmIdSelected.equals(String.valueOf(bdFirm.getId()))) {
                        firmName = bdFirm.getFirmName();
                    }
                }
            }
        }
        return firmName;
    }
    /**
     * This method gives back a list of all RVPs.
     * 
     * @return - List of all BDFirms.
     * @throws SystemException
     */
    public static List<RegionalVicePresident> getAllRVPs() throws SystemException {
        List<RegionalVicePresident> rvps = BrokerServiceDelegate.getInstance(
                BDConstants.BD_APPLICATION_ID).getAllRVPs();
            
        return rvps;
    }

    /**
     * This method gives back a list of all Sales Regions.
     * 
     * @return - List of all BDFirms.
     * @throws SystemException
     */
    public static List<SalesRegion> getAllSalesRegions() throws SystemException {
        List<SalesRegion> salesRegions = BrokerServiceDelegate.getInstance(
                BDConstants.BD_APPLICATION_ID).getAllSalesRegions();
            
        return salesRegions;
    }

    /**
     * This method gives back a list of all Sales Divisions.
     * 
     * @return - List of all BDFirms.
     * @throws SystemException
     */
    public static List<SalesDivision> getAllSalesDivisions() throws SystemException {
        List<SalesDivision> salesDivisions = BrokerServiceDelegate.getInstance(
                BDConstants.BD_APPLICATION_ID).getAllSalesDivisions();
            
        return salesDivisions;
    }

    /**
     * This method gives back the RVP Name in lastName, firstName pattern, given the RvpID as input.
     * 
     * @param rvpId - rvpID
     * @return - rvpName for the corresponding rvpName in lastName, firstName pattern.
     * @throws SystemException
     */
    public static String getRvpNameForIDSelected(String rvpId) throws SystemException {
        String rvpName = BDConstants.SPACE_SYMBOL;
        if (rvpId != null) {
            List<RegionalVicePresident> allRvps = getAllRVPs();
            if (allRvps != null && !allRvps.isEmpty()) {
                for (RegionalVicePresident rvpInfo : allRvps) {
                    if (rvpId.equals(String.valueOf(rvpInfo.getId()))) {
                        rvpName = rvpInfo.getLastName() + BDConstants.SINGLE_SPACE_SYMBOL
                                + BDConstants.COMMA_SYMBOL + rvpInfo.getFirstName();
                        break;
                    }
                }
            }
        }
        
        return rvpName;
    }
    
    /**
     * This method gives back the RVP Name in lastName, firstName pattern, given the RvpID as input.
     * 
     * @param rvpId - rvpID
     * @return - rvpName for the corresponding rvpName in lastName, firstName pattern.
     * @throws SystemException
     */
    public static String getRvpNameSelected(String rvpId) throws SystemException {
        String rvpName = BDConstants.SPACE_SYMBOL;
        if (rvpId != null) {
            List<RegionalVicePresident> allRvps = getAllRVPs();
            if (allRvps != null && !allRvps.isEmpty()) {
                for (RegionalVicePresident rvpInfo : allRvps) {
                    if (rvpId.equals(String.valueOf(rvpInfo.getId()))) {
                        rvpName = rvpInfo.getLastName() + ", " + rvpInfo.getFirstName();
                        break;
                    }
                }
            }
        }
        
        return rvpName;
    }
    
    /**
     * This method gives back the Region Name, given the RegionID as input.
     * 
     * @param regionId
     * @return - region Name
     * @throws SystemException
     */
    public static String getRegionNameForIDSelected(String regionId) throws SystemException {
        String regionName = BDConstants.SPACE_SYMBOL;
        if (regionId != null) {
            List<SalesRegion> allRegions = getAllSalesRegions();
            if (allRegions != null && !allRegions.isEmpty()) {
                for (SalesRegion regionInfo : allRegions) {
                    if (regionId.equals(String.valueOf(regionInfo.getId()))) {
                        regionName = regionInfo.getName();
                        break;
                    }
                }
            }
        }
        
        return regionName;
    }

    /**
     * This method gives back the Division Name, given the DivisionID as input.
     * 
     * @param divisionId
     * @return - division Name.
     * @throws SystemException
     */
    public static String getDivisionNameForIDSelected(String divisionId) throws SystemException {
        String divisionName = BDConstants.SPACE_SYMBOL;
        if (divisionId != null) {
            List<SalesDivision> allDivisions = getAllSalesDivisions();
            if (allDivisions != null && !allDivisions.isEmpty()) {
                for (SalesDivision divisionInfo : allDivisions) {
                    if (divisionId.equals(String.valueOf(divisionInfo.getId()))) {
                        divisionName = divisionInfo.getName();
                        break;
                    }
                }
            }
        }
        
        return divisionName;
    }
    
    
    /**
     * This method gives a list of Features.
     * 
     * @return - List of featues.
     */ 
    @SuppressWarnings("unchecked")
    public static List<LabelValueBean> getCSFFeatures() throws SystemException {
        List<LabelValueBean> csfList = (List<LabelValueBean>) cacheMap.get(BDConstants.CSF_LIST);
       
        if (csfList == null || csfList.isEmpty()) {
            csfList = new ArrayList<LabelValueBean>(2);
            
			final Map<String, String> fundFamilyNamesWithCode = FundServiceDelegate.getInstance().getAllFundFamilyNamesWithCode();
			final Map<String, String> fundFamilyCodeMap = new HashMap<String, String>();
			int index = 0;
			for (Map.Entry<String, String> mapFundName : fundFamilyNamesWithCode.entrySet()) {
				String displayName = BDConstants.DF_PREFIX + mapFundName.getValue();
				String displayCode = BlockOfBusinessReportData.DF_FUND_FAMILY_CODE + index++;
				String familyCode = mapFundName.getKey();
				csfList.add(new LabelValueBean(displayName, displayCode));
				fundFamilyCodeMap.put(displayCode, familyCode);
			}
                      
            csfList.add(new LabelValueBean(BDConstants.DF_MONEY_MARKET_FUNDS_TITLE, BlockOfBusinessReportData.DF_MONEY_MARKET_FUNDS_VALUE));
            csfList.add(new LabelValueBean(BDConstants.DF_OTHERS_TITLE, BlockOfBusinessReportData.DF_OTHERS_VALUE));
            csfList.add(new LabelValueBean(BDConstants.DF_STABLE_VALUE_FUNDS_TITLE, BlockOfBusinessReportData.DF_STABLE_VALUE_FUNDS_VALUE));
            //csfList.add(new LabelValueBean(BDConstants.EZGOALS_TITLE, BlockOfBusinessReportData.EZGOALS_VALUE));
            csfList.add(new LabelValueBean(BDConstants.EZSTART_TITLE, BlockOfBusinessReportData.EZSTART_VALUE));
            csfList.add(new LabelValueBean(BDConstants.FIDUCIARY_WARRANTY_TITLE, BlockOfBusinessReportData.FIDUCIARY_WARRANTY_VALUE));
            csfList.add(new LabelValueBean(BDConstants.GIFL_TITLE, BlockOfBusinessReportData.GIFL_VALUE));
            csfList.add(new LabelValueBean(BDConstants.IPSM_TITLE, BlockOfBusinessReportData.IPSM_VALUE));
            csfList.add(new LabelValueBean(BDConstants.JH_PASSIVE_TRUSTEE_SERVICE_TITLE, BlockOfBusinessReportData.JH_PASSIVE_TRUSTEE_SERVICE_VALUE));
            //csfList.add(new LabelValueBean(BDConstants.JHI_INVESTMENT_SELECTION_TITLE , BlockOfBusinessReportData.JHI_INVESTMENT_SELECTION_VALUE));
            //csfList.add(new LabelValueBean(BDConstants.JH_SVGIF_CREDIT_TITLE , BlockOfBusinessReportData.JH_SVGIF_CREDIT_VALUE));        
            csfList.add(new LabelValueBean(BDConstants.JH_CREDITS_PROGRAM_TITLE , BlockOfBusinessReportData.JH_CREDITS_PROGRAM_VALUE));
            csfList.add(new LabelValueBean(BDConstants.LOANS_TITLE, BlockOfBusinessReportData.LOANS_VALUE));
            csfList.add(new LabelValueBean(BDConstants.MA_TITLE, BlockOfBusinessReportData.MA_VALUE));
            csfList.add(new LabelValueBean(BDConstants.PBA_TITLE, BlockOfBusinessReportData.PBA_VALUE));
            csfList.add(new LabelValueBean(SmallPlanFeature.PEP.getTitle(), BlockOfBusinessReportData.PEP_VALUE));
            //csfList.add(new LabelValueBean(BDConstants.PLAN_CHECK_TITLE, BlockOfBusinessReportData.PLAN_CHECK_VALUE));
            csfList.add(new LabelValueBean(BDConstants.ROTH_TITLE, BlockOfBusinessReportData.ROTH_VALUE));
            csfList.add(new LabelValueBean(BDConstants.EZINCREASE_TITLE, BlockOfBusinessReportData.EZINCREASE_VALUE));
            csfList.add(new LabelValueBean(BDConstants.SEND_SERVICE_TITLE , BlockOfBusinessReportData.SEND_SERVICE));
            csfList.add(new LabelValueBean(SmallPlanFeature.SFC.getTitle(), BlockOfBusinessReportData.SFC_VALUE));          
            csfList.add(new LabelValueBean(BDConstants.SYSTEMATIC_WITHDRAWAL_TITLE , BlockOfBusinessReportData.SYSTEMATIC_WTTHDRAWAL_SERVICE));
            csfList.add(new LabelValueBean(BDConstants.TOTAL_CARE_TITLE , BlockOfBusinessReportData.TOTAL_CARE_VALUE));
            csfList.add(new LabelValueBean(BDConstants.WILSHIRE321_TITLE , BlockOfBusinessReportData.WILSHIRE321_SERVICE));        
            csfList.add(new LabelValueBean(BDConstants.WILSHIRE338_TITLE , BlockOfBusinessReportData.WILSHIRE338_SERVICE));
            cacheMap.put(BDConstants.CSF_LIST, csfList);
            cacheMap.put(BDConstants.FUND_FAMILY_LOOKUP_MAP, fundFamilyCodeMap);
        }
            
        return csfList;
    }


    /**
     * This method would return the UserProfile object of the mimicking user.
     * 
     * @return - BDUserProfile object of the mimicking user.
     */
    @SuppressWarnings("unchecked")
    public static BDUserProfile getMimckingUserProfile(HttpServletRequest request) {
        Map<String, Object> mimickingUserSession = (Map<String, Object>) request.getSession(false)
                .getAttribute(BDConstants.ATTR_MIMICKING_SESSION);
        if (mimickingUserSession == null) {
            return null;
        }
        
        BDUserProfile mimickingInternalUserProfile = (BDUserProfile) mimickingUserSession
                .get(BDConstants.USERPROFILE_KEY);

        return mimickingInternalUserProfile;
    }

    /**
     * This method helps in retrieving the Party ID for the rvp user.
     * 
     * @param userProfile - BDUserProfile object
     * @return - returns the partyId in string format.
     */
    public static String getRVPPartyId(BDUserProfile userProfile) {
        String partyId = BDConstants.SPACE_SYMBOL;
        if (userProfile != null) {
            BDUserRole userRole = userProfile.getRole();
            if (userRole != null && userRole instanceof BDRvp) {
                Long partyIdL = ((BDRvp) userRole).getRvpEntity().getId();
                partyId = String.valueOf(partyIdL);
            }
        }
        return partyId;
    }

    /**
     * This method gives back a list of uniquely named Firm Names for a given User. The user should
     * be a BDFirmRep user.
     * 
     * @param userProfile - BDFirmRepUserProfile.
     * @return - Arraylist of Firm Names associated to the user profile.
     */
    public static ArrayList<String> getAssociatedFirmNamesForFirmRep(BDUserProfile userProfile) {
        List<BrokerDealerFirm> bdFirms = ((BDFirmRep) userProfile.getRole())
                .getBrokerDealerFirmEntities();
        ArrayList<String> firmNames = new ArrayList<String>();
        if (bdFirms != null) {
            for (BrokerDealerFirm bdFirm : bdFirms) {
                if (!firmNames.contains(bdFirm.getFirmName())) {
                    firmNames.add(bdFirm.getFirmName());
                }
            }
        }

        return firmNames;
    }

    /**
     * This method retrieves the "Firm ID" given the "Firm Party ID".
     * 
     * @param partyID - Firm Party ID
     * @return - Firm ID.
     * @throws SystemException
     * @throws InvalidIdException
     */
    public static String getFirmIDForFirmPartyID(Long partyID) throws SystemException {
        BrokerDealerFirm bdFirm;
        try {
            bdFirm = BrokerServiceDelegate.getInstance(BDConstants.BD_APPLICATION_ID)
                    .getBrokerDealerFirmById(partyID);
        } catch (InvalidIdException e) {
            throw new SystemException(e, "InvalidIdException thrown for partyID: " + partyID);
        }
        return bdFirm.getBrokerDealerFirmId();
    }
    
    /**
     * This method is used to get the Principal object for a Financial Rep/Financial Rep Assistant
     * user.
     * 
     * @param userProfile - UserProfile object for a financial rep/financial rep assistant.
     * @return - the BDPrincipal object for a Financial rep or the BDPrincipal object for the
     *         Financial Rep Assistant's Boss. This method will return null, if
     *         SecurityServiceException or SystemException exception happens.
     * @throws SystemException
     * @throws SecurityServiceException
     */
    public static BDPrincipal getPrincipalForFinancialRepOrAsst(BDUserProfile userProfile)
            throws SecurityServiceException, SystemException {
        BDPrincipal principal = userProfile.getBDPrincipal();
        
        if (principal.getBDUserRole() instanceof BDFinancialRepAssistant) {
            principal = BDSecurityProfileHandler.getInstance().getPrincipal(
                        principal.getParentProfileId());
        }
        return principal;
    }

    /**
     * This method gets the Db SessionID for the default as of date. If the DBsessionID for the default
     * as of date does not exist, it returns null.
     * @param request
     * @return
     * @throws SystemException
     */
    @SuppressWarnings("unchecked")
	public static Integer getDBSessionIDForDefaultAsOfDate(HttpServletRequest request) throws SystemException {
    	Integer dbSessionID = null;
    	
        List<Date> monthEndDates = BlockOfBusinessUtility.getMonthEndDates();
    	if (monthEndDates != null && !monthEndDates.isEmpty()) {
            Date asOfDate = monthEndDates.get(0);

            Map<Date, Integer> storedProcSessionIDMap = (Map<Date, Integer>) request.getSession(false)
            	.getAttribute(BDConstants.DB_SESSION_ID);
    
            if (storedProcSessionIDMap != null) {
                dbSessionID = storedProcSessionIDMap.get(asOfDate);
            }
    	}
    	
    	return dbSessionID;
    }

	/**
	 * This method puts the Db SessionID for the given as of date into the
	 * session attribute.
	 * 
	 * @param asOfDate
	 * @param dbSessionID
	 * @param request
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public static void setDBSessionIDForDefaultAsOfDate(Date asOfDate,
			Integer dbSessionID, HttpServletRequest request)
			throws SystemException {

		Map<Date, Integer> storedProcSessionIDMap = (Map<Date, Integer>) request
				.getSession(false).getAttribute(BDConstants.DB_SESSION_ID);

		if (storedProcSessionIDMap == null) {
			storedProcSessionIDMap = new HashMap<Date, Integer>();
		}

		if (asOfDate != null) {
			storedProcSessionIDMap.put(asOfDate, dbSessionID);
		}

		request.getSession(false).setAttribute(BDConstants.DB_SESSION_ID,
				storedProcSessionIDMap);
	}

	/**
	 * Populate the Report Criteria. This Report Criteria will be later used to
	 * call the Block of Business stored proc.
	 * 
	 * @param contractNumber
	 *            - Contract Number
	 * @param contractStatus
	 *            - Contract Status AC or DI.
	 * @return - Report Criteria object containing criteria to be sent to stored
	 *         proc.
	 * @throws SystemException
	 */
    public static ReportCriteria populateReportCriteria(BDUserProfile userProfile,
            BDUserProfile mimickingUserProfile, Integer contractNumber, String contractStatus,
            Integer dbSessionID) throws SystemException {
        ReportCriteria criteria = new ReportCriteria(BlockOfBusinessReportData.REPORT_ID);
        Long userProfileID = userProfile.getBDPrincipal().getProfileId();

        if (mimickingUserProfile == null) {
			criteria.addFilter(BlockOfBusinessReportData.FILTER_USER_PROFILE_ID, userProfileID);
			criteria.addFilter(BlockOfBusinessReportData.FILTER_USER_ROLE,
					userProfile.getRole().getRoleType().getUserRoleCode());
		} else {
			criteria.addFilter(BlockOfBusinessReportData.FILTER_USER_PROFILE_ID,
					mimickingUserProfile.getBDPrincipal().getProfileId());
			criteria.addFilter(BlockOfBusinessReportData.FILTER_USER_ROLE,
					mimickingUserProfile.getRole().getRoleType().getUserRoleCode());
			criteria.addFilter(BlockOfBusinessReportData.FILTER_MIMIC_USER_PROFILE_ID,
					userProfileID);
			criteria.addFilter(BlockOfBusinessReportData.FILTER_MIMIC_USER_ROLE,
					userProfile.getRole().getRoleType().getUserRoleCode());
		}

        criteria.addFilter(BlockOfBusinessReportData.FILTER_CONTRACT_STATUS_CODES, contractStatus);

        List<Date> monthEndDates = BlockOfBusinessUtility.getMonthEndDates();
        if (monthEndDates != null && !monthEndDates.isEmpty()) {
            Long reportAsOfDateL = monthEndDates.get(0).getTime();
            criteria.addFilter(BlockOfBusinessReportData.FILTER_AS_OF_DATE, reportAsOfDateL
                    .toString());
        }
        
        if(contractNumber != null) {
            criteria.addFilter(BlockOfBusinessReportData.FILTER_CONTRACT_NUMBER, contractNumber
                    .toString());
        }

        if (dbSessionID != null) {
            criteria.addFilter(BlockOfBusinessReportData.FILTER_DB_SESSION_ID, dbSessionID);
        }
        
        return criteria;
    }

    /**
     * This method calls the BOB Stored proc with the given criteria and gives back the results.
     * 
     * @param criteria - Report Criteria having all the criteria.
     * @return - The BlockOfBusinessReportData object having the output information of stored proc
     *         call.
     * @throws SystemException
     */
	public static BlockOfBusinessReportData callBOBStoredProc(
			ReportCriteria criteria, HttpServletRequest request)
			throws SystemException {
        BlockOfBusinessReportData bobReportData = null;

        try {
            bobReportData = (BlockOfBusinessReportData) ReportServiceDelegate.getInstance()
                    .getReportData(criteria);

			// Get the DBSessionID from the result obtained from stored proc
			// call and store it in session.
			if (bobReportData != null) {
				Date asOfDate = null;
				String asOfDateL = (String) criteria
						.getFilterValue(BlockOfBusinessReportData.FILTER_AS_OF_DATE);
				if (asOfDateL != null) {
					asOfDate = new Date(Long.parseLong(asOfDateL));
				}
				Integer dbSessionID = bobReportData.getDbSessionID();

				// Store the retrieved dbSessionID into the session.
				setDBSessionIDForDefaultAsOfDate(asOfDate, dbSessionID, request);
			}
        } catch (ReportServiceException e) {
            throw new SystemException(e,
                    "ReportServiceException occurred when calling the Block Of Business stored proc");
        }

        return bobReportData;
    }

    /**
     * This method determines if the userRole passed-in is one of: NATIONAL ACCOUNTS, BASIC, CONTENT
     * MANAGER, ADMINISTRATOR, but not a RVP.
     * 
     * @param userRole
     * @return
     */
    public static Boolean isInternalUserAndNotRVP(BDUserRole userRole) {
        return (userRole instanceof BDNationalAccounts || userRole instanceof BDInternalBasic
                || userRole instanceof BDContentManager || userRole instanceof BDAdministrator || userRole instanceof RIAUserManager)
                && !(userRole instanceof BDRvp);
    }

    /**
     * Logs the web activities
     * 
     * @param action
     * @param profile
     * @param currentPage
     * @param nextPage
     * @param form
     */
    public static void logWebActivity(String action, String logData, BDUserProfile profile,
            Logger logger, Category interactionLog, ServiceLogRecord logRecord) {
        try {
            ServiceLogRecord record = (ServiceLogRecord) logRecord.clone();
            record.setMethodName(action);
            record.setApplicationId(Environment.getInstance().getApplicationId());
            record.setData(logData);
            record.setDate(new Date());
            record.setPrincipalName(profile.getBDPrincipal().getUserName());
            record.setUserIdentity(String.valueOf(profile.getBDPrincipal().getProfileId()));

            interactionLog.error(record);
        } catch (CloneNotSupportedException e) {
            // log the error, but don't interrupt regular processing
            logger.error("error when trying to log into MRL the data:" + logData
                    + ". Exception caught= " + e);
        }
    }


}
