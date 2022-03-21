package com.manulife.pension.ps.web.tpabob.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.delegate.ReportServiceDelegate;
import com.manulife.pension.platform.web.delegate.TpaSearchServiceDelegate;
import com.manulife.pension.ps.service.report.tpabob.valueobject.TPABlockOfBusinessReportData;
import com.manulife.pension.ps.service.report.tpabob.valueobject.TPABlockOfBusinessReportVO;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.tpabob.TPABlockOfBusinessForm;
import com.manulife.pension.service.contract.util.SmallPlanFeature;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * This class is a Helper class for TPA - Block of Business page.
 * 
 * @author HArlomte
 * 
 */
public class TPABlockOfBusinessUtility {
    
    public static Map<String, String> contractStatusMap;
    
    private static ArrayList<String> allColumnList = new ArrayList<String>(11);
    
    private static Map<String, TPABobColumnInfo> activeColumnList = new HashMap<String, TPABobColumnInfo>(11);
    private static Map<String, TPABobColumnInfo> pendingColumnList = new HashMap<String, TPABobColumnInfo>(11);
    private static Map<String, TPABobColumnInfo> discontinuedColumnList = new HashMap<String, TPABobColumnInfo>(11);
    
    private static ArrayList<String> allFilters = new ArrayList<String>(4);
    
    private static Map<String, String> filtersIdToTitleMap = new HashMap<String, String>();
    
    private static ArrayList<String> allColumnListCSV = new ArrayList<String>(25);

    private static Map<String, String> csvColumnsIdToTitleMap = new HashMap<String, String>(25);
    
    
    
    static {
        /**
         * This Map is used to get the Filter Criteria value to be sent to the BlockOfBusinessDAO.
         */
        contractStatusMap = new HashMap<String, String>();
        contractStatusMap.put(Constants.TPA_ACTIVE_TAB_ID, Constants.ACTIVE_TAB_VAL);
        contractStatusMap.put(Constants.TPA_PENDING_TAB_ID, Constants.PENDING_TAB_VAL);
        contractStatusMap.put(Constants.TPA_DISCONTINUED_TAB_ID, Constants.DISCONTINUED_TAB_VAL);

        /**
         * This ArrayList holds all the filters of the TPA - BOB page.
         */
        allFilters.add(TPABlockOfBusinessReportData.FILTER_CONTRACT_NAME_ID);
        allFilters.add(TPABlockOfBusinessReportData.FILTER_CONTRACT_NUMBER_ID);
        allFilters.add(TPABlockOfBusinessReportData.FILTER_FINANCIAL_REP_OR_ORG_NAME_ID);
        allFilters.add(TPABlockOfBusinessReportData.FILTER_CAR_NAME_ID);

        /**
         * This Map holds the mapping from ID to Title for Filters..
         */
        filtersIdToTitleMap.put(TPABlockOfBusinessReportData.FILTER_CONTRACT_NAME_ID,
                Constants.FILTER_CONTRACT_NAME_TITLE);
        filtersIdToTitleMap.put(TPABlockOfBusinessReportData.FILTER_CONTRACT_NUMBER_ID,
                Constants.FILTER_CONTRACT_NUNBER_TITLE);
        filtersIdToTitleMap.put(TPABlockOfBusinessReportData.FILTER_FINANCIAL_REP_OR_ORG_NAME_ID,
                Constants.FILTER_FINANCIAL_REP_OR_ORG_NAME_TITLE);
        filtersIdToTitleMap.put(TPABlockOfBusinessReportData.FILTER_CAR_NAME_ID,
                Constants.FILTER_CAR_NAME_TITLE);

        
        /**
         * This ArrayList holds all the columns that can be shown on the JSP page.
         */
        allColumnList.add(TPABlockOfBusinessReportData.COL_CONTRACT_NAME_ID);
        allColumnList.add(TPABlockOfBusinessReportData.COL_CONTRACT_NUMBER_ID);
        allColumnList.add(TPABlockOfBusinessReportData.COL_CONTRACT_PLAN_YEAR_END_ID);
        allColumnList.add(TPABlockOfBusinessReportData.COL_EXPECTED_FIRST_YEAR_ASSETS_ID);
        allColumnList.add(TPABlockOfBusinessReportData.COL_EXPECTED_NUM_OF_LIVES_ID);
        allColumnList.add(TPABlockOfBusinessReportData.COL_NUM_OF_LIVES_ID);
        allColumnList.add(TPABlockOfBusinessReportData.COL_TOTAL_ASSETS_ID);
        allColumnList.add(TPABlockOfBusinessReportData.COL_TRANSFERRED_OUT_ASSETS_ID);
        allColumnList.add(TPABlockOfBusinessReportData.COL_DISCONTINUANCE_DATE_ID);
        allColumnList.add(TPABlockOfBusinessReportData.COL_FINANCIAL_REP_ID);
        allColumnList.add(TPABlockOfBusinessReportData.COL_CAR_NAME_ID);

        /**
         * This Map holds the information about the columns that can be shown on Active Tab.
         */
        activeColumnList.put(TPABlockOfBusinessReportData.COL_CONTRACT_NAME_ID,
                new TPABobColumnInfo(TPABlockOfBusinessReportData.COL_CONTRACT_NAME_ID,
                        Constants.COL_CONTRACT_NAME_TITLE, Boolean.TRUE));
        activeColumnList.put(TPABlockOfBusinessReportData.COL_CONTRACT_NUMBER_ID,
                new TPABobColumnInfo(TPABlockOfBusinessReportData.COL_CONTRACT_NUMBER_ID,
                        Constants.COL_CONTRACT_NUMBER_TITLE, Boolean.TRUE));
        activeColumnList.put(TPABlockOfBusinessReportData.COL_CONTRACT_PLAN_YEAR_END_ID,
                new TPABobColumnInfo(TPABlockOfBusinessReportData.COL_CONTRACT_PLAN_YEAR_END_ID,
                        Constants.COL_CONTRACT_PLAN_YEAR_END_TITLE, Boolean.TRUE));
        activeColumnList.put(TPABlockOfBusinessReportData.COL_EXPECTED_FIRST_YEAR_ASSETS_ID,
                new TPABobColumnInfo(
                        TPABlockOfBusinessReportData.COL_EXPECTED_FIRST_YEAR_ASSETS_ID,
                        Constants.COL_EXPECTED_FIRST_YEAR_ASSETS_TITLE, Boolean.FALSE));
        activeColumnList.put(TPABlockOfBusinessReportData.COL_EXPECTED_NUM_OF_LIVES_ID,
                new TPABobColumnInfo(TPABlockOfBusinessReportData.COL_EXPECTED_NUM_OF_LIVES_ID,
                        Constants.COL_EXPECTED_NUM_OF_LIVES_TITLE, Boolean.FALSE));
        activeColumnList.put(TPABlockOfBusinessReportData.COL_NUM_OF_LIVES_ID,
                new TPABobColumnInfo(TPABlockOfBusinessReportData.COL_NUM_OF_LIVES_ID,
                        Constants.COL_NUM_OF_LIVES_TITLE, Boolean.TRUE));
        activeColumnList.put(TPABlockOfBusinessReportData.COL_TOTAL_ASSETS_ID,
                new TPABobColumnInfo(TPABlockOfBusinessReportData.COL_TOTAL_ASSETS_ID,
                        Constants.COL_TOTAL_ASSETS_TITLE, Boolean.TRUE));
        activeColumnList.put(TPABlockOfBusinessReportData.COL_TRANSFERRED_OUT_ASSETS_ID,
                new TPABobColumnInfo(TPABlockOfBusinessReportData.COL_TRANSFERRED_OUT_ASSETS_ID,
                        Constants.COL_TRANSFERRED_OUT_ASSETS_TITLE, Boolean.FALSE));
        activeColumnList.put(TPABlockOfBusinessReportData.COL_DISCONTINUANCE_DATE_ID,
                new TPABobColumnInfo(TPABlockOfBusinessReportData.COL_DISCONTINUANCE_DATE_ID,
                        Constants.COL_DISCONTINUANCE_DATE_TITLE, Boolean.FALSE));
        activeColumnList.put(TPABlockOfBusinessReportData.COL_FINANCIAL_REP_ID,
                new TPABobColumnInfo(TPABlockOfBusinessReportData.COL_FINANCIAL_REP_ID,
                        Constants.COL_FINANCIAL_REP_TITLE, Boolean.TRUE));
        activeColumnList.put(TPABlockOfBusinessReportData.COL_CAR_NAME_ID, new TPABobColumnInfo(
                TPABlockOfBusinessReportData.COL_CAR_NAME_ID, Constants.COL_CAR_NAME_TITLE,
                Boolean.TRUE));

        /**
         * This Map holds the information about the columns that can be shown on Pending Tab.
         */
        pendingColumnList.put(TPABlockOfBusinessReportData.COL_CONTRACT_NAME_ID,
                new TPABobColumnInfo(TPABlockOfBusinessReportData.COL_CONTRACT_NAME_ID,
                        Constants.COL_CONTRACT_NAME_TITLE, Boolean.TRUE));
        pendingColumnList.put(TPABlockOfBusinessReportData.COL_CONTRACT_NUMBER_ID,
                new TPABobColumnInfo(TPABlockOfBusinessReportData.COL_CONTRACT_NUMBER_ID,
                        Constants.COL_CONTRACT_NUMBER_TITLE, Boolean.TRUE));
        pendingColumnList.put(TPABlockOfBusinessReportData.COL_CONTRACT_PLAN_YEAR_END_ID,
                new TPABobColumnInfo(TPABlockOfBusinessReportData.COL_CONTRACT_PLAN_YEAR_END_ID,
                        Constants.COL_CONTRACT_PLAN_YEAR_END_TITLE, Boolean.TRUE));
        pendingColumnList.put(TPABlockOfBusinessReportData.COL_EXPECTED_FIRST_YEAR_ASSETS_ID,
                new TPABobColumnInfo(
                        TPABlockOfBusinessReportData.COL_EXPECTED_FIRST_YEAR_ASSETS_ID,
                        Constants.COL_EXPECTED_FIRST_YEAR_ASSETS_TITLE, Boolean.FALSE));
        pendingColumnList.put(TPABlockOfBusinessReportData.COL_EXPECTED_NUM_OF_LIVES_ID,
                new TPABobColumnInfo(TPABlockOfBusinessReportData.COL_EXPECTED_NUM_OF_LIVES_ID,
                        Constants.COL_EXPECTED_NUM_OF_LIVES_TITLE, Boolean.TRUE));
        pendingColumnList.put(TPABlockOfBusinessReportData.COL_NUM_OF_LIVES_ID,
                new TPABobColumnInfo(TPABlockOfBusinessReportData.COL_NUM_OF_LIVES_ID,
                        Constants.COL_NUM_OF_LIVES_TITLE, Boolean.FALSE));
        pendingColumnList.put(TPABlockOfBusinessReportData.COL_TOTAL_ASSETS_ID,
                new TPABobColumnInfo(TPABlockOfBusinessReportData.COL_TOTAL_ASSETS_ID,
                        Constants.COL_TOTAL_ASSETS_TITLE, Boolean.TRUE));
        pendingColumnList.put(TPABlockOfBusinessReportData.COL_TRANSFERRED_OUT_ASSETS_ID,
                new TPABobColumnInfo(TPABlockOfBusinessReportData.COL_TRANSFERRED_OUT_ASSETS_ID,
                        Constants.COL_TRANSFERRED_OUT_ASSETS_TITLE, Boolean.FALSE));
        pendingColumnList.put(TPABlockOfBusinessReportData.COL_DISCONTINUANCE_DATE_ID,
                new TPABobColumnInfo(TPABlockOfBusinessReportData.COL_DISCONTINUANCE_DATE_ID,
                        Constants.COL_DISCONTINUANCE_DATE_TITLE, Boolean.FALSE));
        pendingColumnList.put(TPABlockOfBusinessReportData.COL_FINANCIAL_REP_ID,
                new TPABobColumnInfo(TPABlockOfBusinessReportData.COL_FINANCIAL_REP_ID,
                        Constants.COL_FINANCIAL_REP_TITLE, Boolean.TRUE));
        pendingColumnList.put(TPABlockOfBusinessReportData.COL_CAR_NAME_ID, new TPABobColumnInfo(
                TPABlockOfBusinessReportData.COL_CAR_NAME_ID, Constants.COL_CAR_NAME_TITLE,
                Boolean.TRUE));


        /**
         * This Map holds the information about the columns that can be shown on Discontinued Tab.
         */
        discontinuedColumnList.put(TPABlockOfBusinessReportData.COL_CONTRACT_NAME_ID,
                new TPABobColumnInfo(TPABlockOfBusinessReportData.COL_CONTRACT_NAME_ID,
                        Constants.COL_CONTRACT_NAME_TITLE, Boolean.TRUE));
        discontinuedColumnList.put(TPABlockOfBusinessReportData.COL_CONTRACT_NUMBER_ID,
                new TPABobColumnInfo(TPABlockOfBusinessReportData.COL_CONTRACT_NUMBER_ID,
                        Constants.COL_CONTRACT_NUMBER_TITLE, Boolean.TRUE));
        discontinuedColumnList.put(TPABlockOfBusinessReportData.COL_CONTRACT_PLAN_YEAR_END_ID,
                new TPABobColumnInfo(TPABlockOfBusinessReportData.COL_CONTRACT_PLAN_YEAR_END_ID,
                        Constants.COL_CONTRACT_PLAN_YEAR_END_TITLE, Boolean.TRUE));
        discontinuedColumnList.put(TPABlockOfBusinessReportData.COL_EXPECTED_FIRST_YEAR_ASSETS_ID,
                new TPABobColumnInfo(
                        TPABlockOfBusinessReportData.COL_EXPECTED_FIRST_YEAR_ASSETS_ID,
                        Constants.COL_EXPECTED_FIRST_YEAR_ASSETS_TITLE, Boolean.FALSE));
        discontinuedColumnList.put(TPABlockOfBusinessReportData.COL_EXPECTED_NUM_OF_LIVES_ID,
                new TPABobColumnInfo(TPABlockOfBusinessReportData.COL_EXPECTED_NUM_OF_LIVES_ID,
                        Constants.COL_EXPECTED_NUM_OF_LIVES_TITLE, Boolean.FALSE));
        discontinuedColumnList.put(TPABlockOfBusinessReportData.COL_NUM_OF_LIVES_ID,
                new TPABobColumnInfo(TPABlockOfBusinessReportData.COL_NUM_OF_LIVES_ID,
                        Constants.COL_NUM_OF_LIVES_TITLE, Boolean.FALSE));
        discontinuedColumnList.put(TPABlockOfBusinessReportData.COL_TOTAL_ASSETS_ID,
                new TPABobColumnInfo(TPABlockOfBusinessReportData.COL_TOTAL_ASSETS_ID,
                        Constants.COL_TOTAL_ASSETS_TITLE, Boolean.FALSE));
        discontinuedColumnList.put(TPABlockOfBusinessReportData.COL_TRANSFERRED_OUT_ASSETS_ID,
                new TPABobColumnInfo(TPABlockOfBusinessReportData.COL_TRANSFERRED_OUT_ASSETS_ID,
                        Constants.COL_TRANSFERRED_OUT_ASSETS_TITLE, Boolean.TRUE));
        discontinuedColumnList.put(TPABlockOfBusinessReportData.COL_DISCONTINUANCE_DATE_ID,
                new TPABobColumnInfo(TPABlockOfBusinessReportData.COL_DISCONTINUANCE_DATE_ID,
                        Constants.COL_DISCONTINUANCE_DATE_TITLE, Boolean.TRUE));
        discontinuedColumnList.put(TPABlockOfBusinessReportData.COL_FINANCIAL_REP_ID,
                new TPABobColumnInfo(TPABlockOfBusinessReportData.COL_FINANCIAL_REP_ID,
                        Constants.COL_FINANCIAL_REP_TITLE, Boolean.TRUE));
        discontinuedColumnList.put(TPABlockOfBusinessReportData.COL_CAR_NAME_ID,
                new TPABobColumnInfo(TPABlockOfBusinessReportData.COL_CAR_NAME_ID,
                        Constants.COL_CAR_NAME_TITLE, Boolean.TRUE));
        
        /**
         * This ArrayList holds the list of Columns that should go into CSV.
         */
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_CONTRACT_NAME_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_CONTRACT_NUMBER_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_TPA_FIRM_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_FINANCIAL_REP_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_CAR_NAME_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_CONTRACT_STATUS_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_CONTRACT_EFFECTIVE_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_NUM_OF_LIVES_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_ALLOCATED_ASSETS_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_LOAN_ASSETS_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_CASH_ACCOUNT_BALANCE_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_PBA_ASSETS_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_TOTAL_ASSETS_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_EZ_START_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_EZ_INCREASE_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_DIRECT_MAIL_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_GIFL_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_MANAGED_ACCOUNT_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_SEND_SERVICE_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_JOHNHANCOCK_PASSIVE_TRUSTEE_CODE_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_PAM_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_ONLINE_BENEFICIARY_DESIGNATION_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_ONLINE_WITHDRAWALS_ID);  
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_SYS_WITHDRAWALS_ID);
		allColumnListCSV.add(TPABlockOfBusinessReportData.COL_INSTALLMENTS_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_VESTING_PERCENTAGE_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_VESTING_ON_STATEMENTS_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_PERMITTED_DISPARITY_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_FSW_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_DIO_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_TPA_SIGNING_AUTHORITY_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_COW_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_PAYROL_PATH_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_PAYROLL_FEEDBACK_SVC_ID);        
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_PLAN_HIGHLIGHTS_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_PLAN_HIGHLIGHTS_REVIEWED_ID);
        //RPSSO-124653 Start
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_SFC_ID);
        allColumnListCSV.add(TPABlockOfBusinessReportData.COL_PEP_ID);
        //RPSSO-124653 End
        
        
        /**
         * This map holds the mapping between ID and Title for CSV related columns..
         */
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_CONTRACT_NAME_ID, Constants.COL_CONTRACT_NAME_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_CONTRACT_NUMBER_ID, Constants.COL_CONTRACT_NUMBER_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_TPA_FIRM_ID, Constants.COL_TPA_FIRM_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_FINANCIAL_REP_ID, Constants.COL_FINANCIAL_REP_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_CAR_NAME_ID, Constants.COL_CAR_NAME_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_CONTRACT_STATUS_ID, Constants.COL_CONTRACT_STATUS_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_CONTRACT_EFFECTIVE_ID, Constants.COL_CONTRACT_EFFECTIVE_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_NUM_OF_LIVES_ID, Constants.COL_NUM_OF_LIVES_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_ALLOCATED_ASSETS_ID, Constants.COL_ALLOCATED_ASSETS_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_LOAN_ASSETS_ID, Constants.COL_LOAN_ASSETS_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_CASH_ACCOUNT_BALANCE_ID, Constants.COL_CASH_ACCOUNT_BALANCE_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_PBA_ASSETS_ID, Constants.COL_PBA_ASSETS_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_TOTAL_ASSETS_ID, Constants.COL_TOTAL_ASSETS_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_EZ_START_ID, Constants.COL_EZ_START_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_EZ_INCREASE_ID, Constants.COL_EZ_INCREASE_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_DIRECT_MAIL_ID, Constants.COL_DIRECT_MAIL_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_GIFL_ID, Constants.COL_GIFL_FEATURE_TITLE_SC);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_MANAGED_ACCOUNT_ID, Constants.COL_MANAGED_ACCOUNT_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_SEND_SERVICE_ID,Constants.COL_SEND_SERVICE_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_JOHNHANCOCK_PASSIVE_TRUSTEE_CODE_ID,Constants.COL_JOHNHANCOCK_PASSIVE_TRUSTEE_CODE_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_PAM_ID, Constants.COL_PAM_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_ONLINE_BENEFICIARY_DESIGNATION_ID, Constants.COL_ONLINE_BENEFICIARY_DESIGNATION_TILTE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_ONLINE_WITHDRAWALS_ID, Constants.COL_ONLINE_WITHDRAWALS_TITLE); 
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_SYS_WITHDRAWALS_ID, Constants.COL_SYS_WITHDRAWALS_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_INSTALLMENTS_ID, Constants.COL_INSTALLMENTS_TITLE);

        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_VESTING_PERCENTAGE_ID, Constants.COL_VESTING_PERCENTAGE_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_VESTING_ON_STATEMENTS_ID, Constants.COL_VESTING_ON_STATEMENTS_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_PERMITTED_DISPARITY_ID, Constants.COL_PERMITTED_DISPARITY_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_FSW_ID, Constants.COL_FSW_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_DIO_ID, Constants.COL_DIO_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_TPA_SIGNING_AUTHORITY_ID, Constants.COL_TPA_SIGNING_AUTHORITY_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_COW_ID, Constants.COL_COW_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_PAYROL_PATH_ID, Constants.COL_PAYROLL_PATH_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_PAYROLL_FEEDBACK_SVC_ID, Constants.COL_PAYROLL_FEEDBACK_SVC_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_PLAN_HIGHLIGHTS_ID, Constants.COL_PLAN_HIGHLIGHTS_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_PLAN_HIGHLIGHTS_REVIEWED_ID, Constants.COL_PLAN_HIGHLIGHTS_REVIEWED_TITLE);
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_SFC_ID, SmallPlanFeature.SFC.getTitle());
        csvColumnsIdToTitleMap.put(TPABlockOfBusinessReportData.COL_PEP_ID, SmallPlanFeature.PEP.getTitle());
}
    
    
    /**
     * Helper method to create a list of 24 calendar month end dates. This method calls the
     * getMonthEndDates(Date), passing the asOfDate.
     * 
     * @return
     * @throws SystemException
     */
    public static List<Date> getMonthEndDates() throws SystemException {
        Date asOfDate = EnvironmentServiceDelegate.getInstance(Constants.PS_APPLICATION_ID)
                .getAsOfDate();
        return getMonthEndDates(asOfDate);
    }

    /**
     * Helper method to create a list of 24 calendar month end dates, based on given asOfDate.
     * 
     * @return - List of "Date" objects for the past 24 month end dates.
     * @throws SystemException
     */
    public static List<Date> getMonthEndDates(Date in_asOfDate) throws SystemException {
        List<Date> dates = new ArrayList<Date>();

        if (in_asOfDate == null) {
            return null;
        }

        Date asOfDate = in_asOfDate;
        dates.add(asOfDate);
        // if asOfdate is Nov 5 2008, take previous 24 month end dates starting from Oct 31 2008.
        // if asOfdate is Nov 30 2008, take previous 23 month end dates starting from Oct 31 2008.

        // is asOfDate a month end date?
        boolean isMonthEndDate = false;
        Calendar cal = Calendar.getInstance();
        cal.setTime(asOfDate);
        int lastDayOfCurrMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (lastDayOfCurrMonth == cal.get(Calendar.DAY_OF_MONTH)) {
            isMonthEndDate = true;
        }

        int numOfMonthEndDates = 24;
        if (isMonthEndDate) {
            numOfMonthEndDates = 23;
        }

        // Calendar cal = Calendar.getInstance();
        cal.setTime(asOfDate);
        // add previous month end dates.
        for (int i = 0; i < numOfMonthEndDates; i++) {
            cal.add(Calendar.MONTH, Constants.NUM_MINUS_1);
            int lastDayOfTheMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            cal.set(Calendar.DAY_OF_MONTH, lastDayOfTheMonth);
            dates.add(cal.getTime());
        }

        return dates;
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
    
    public static Map<String, String> getContractStatusMap() {
        return contractStatusMap;
    }

    public static ArrayList<String> getAllColumnList() {
        return allColumnList;
    }

    public static Map<String, TPABobColumnInfo> getActiveColumnList() {
        return activeColumnList;
    }

    public static Map<String, TPABobColumnInfo> getPendingColumnList() {
        return pendingColumnList;
    }

    public static Map<String, TPABobColumnInfo> getDiscontinuedColumnList() {
        return discontinuedColumnList;
    }

    public static ArrayList<String> getAllColumnListCSV() {
        return allColumnListCSV;
    }

    public static Map<String, String> getCsvColumnsIdToTitleMap() {
        return csvColumnsIdToTitleMap;
    }

    public static ArrayList<String> getAllFilters() {
        return allFilters;
    }

    public static Map<String, String> getFiltersIdToTitleMap() {
        return filtersIdToTitleMap;
    }

    public static Map<String, TPABobColumnInfo> getApplicableColumnsListForTab(String tabName) {
        if (Constants.TPA_ACTIVE_TAB_ID.equals(tabName)) {
            return activeColumnList;
        } else if (Constants.TPA_PENDING_TAB_ID.equals(tabName)) {
            return pendingColumnList;
        } else if (Constants.TPA_DISCONTINUED_TAB_ID.equals(tabName)) {
            return discontinuedColumnList;
        }
        return null;
    }

    public static Boolean isColumnEnabledForTab(String columnName, String tabName) {
        if (Constants.TPA_ACTIVE_TAB_ID.equals(tabName)) {
            return activeColumnList.get(columnName).getIsEnabled();
        } else if (Constants.TPA_PENDING_TAB_ID.equals(tabName)) {
            return pendingColumnList.get(columnName).getIsEnabled();
        } else if (Constants.TPA_DISCONTINUED_TAB_ID.equals(tabName)) {
            return discontinuedColumnList.get(columnName).getIsEnabled();
        }
        return null;
    }

    /**
     * This method is used to verify if the current user logged in has access to the contract number
     * or not. We first check if the contract is available in the Active Tab. If not present, we
     * call the stored proc to check if the contract is available in the Discontinued tab.
     * 
     * @param reportForm - TPABlockOfBusinessForm object.
     * @param request - HttpServletRequest object
     * @param contractNumber - contract number, to which the user is trying to access.
     * @return - true if the current user has access to the given contract number, else, returns
     *         false.
     * @throws SystemException
     */
    public static boolean verifyContract(TPABlockOfBusinessForm reportForm,
            HttpServletRequest request, Long userProfileID, String userRole,
            Long mimicUserProfileID, String mimicUserRole, String contractNumber)
            throws SystemException {
        boolean isAllowedAccessToContract = false;

        // Call TPA BOB stored proc with the contract number as the filter criteria.
        TPABlockOfBusinessReportData tpabobReportData = TPABlockOfBusinessUtility
                .callTPABOBStoredProc(createReportCriteria(reportForm, request, userProfileID,
                        userRole, mimicUserProfileID, mimicUserRole, null,
                        Constants.ACTIVE_TAB_VAL, contractNumber));
        isAllowedAccessToContract = isContractNumberPresentInReportData(Integer
                .parseInt(contractNumber), tpabobReportData);

        if (!isAllowedAccessToContract) {
            Integer dbSessionID = tpabobReportData.getDbSessionID();

            tpabobReportData = TPABlockOfBusinessUtility.callTPABOBStoredProc(createReportCriteria(
                    reportForm, request, userProfileID, userRole, mimicUserProfileID,
                    mimicUserRole, dbSessionID,
                    Constants.DISCONTINUED_TAB_VAL, contractNumber));

            isAllowedAccessToContract = isContractNumberPresentInReportData(Integer
                    .parseInt(contractNumber), tpabobReportData);
        }

        return isAllowedAccessToContract;
    }

    /**
     * This method checks if the given contractNumber is present in the TPABlockOfBusiness report
     * details.
     * 
     * @param contractNumber - The Integer number to be checked.
     * @param bobReportData - The TPABlockOfBusiness report details obtained from a call to BOB
     *            stored proc.
     * @return - true if the contractNumber is present in TPABlockOfBusiness report details, else,
     *         returns false.
     */
    @SuppressWarnings("unchecked")
    public static boolean isContractNumberPresentInReportData(Integer contractNumber,
            TPABlockOfBusinessReportData bobReportData) {

        boolean isContractNumberPresentInReportData = false;

        if (bobReportData != null && bobReportData.getDetails() != null
                && !bobReportData.getDetails().isEmpty()) {
            ArrayList<TPABlockOfBusinessReportVO> bobReportVOList = (ArrayList<TPABlockOfBusinessReportVO>) bobReportData
                    .getDetails();
            for (TPABlockOfBusinessReportVO bobReportVO : bobReportVOList) {
                if (contractNumber.equals(bobReportVO.getContractNumber())) {
                    isContractNumberPresentInReportData = true;
                    break;
                }
            }
        }

        return isContractNumberPresentInReportData;
    }

    /**
     * This method creates the Report Criteria based on the parameters provided.
     * 
     * @param reportForm - TPABlockOfBusinessForm object.
     * @param request - HttpServletRequest object
     * @param dbSessionID - sessionID received from the call to the TPA BOB Stored proc.
     * @param contractStatus - the contract status report criteria.
     * @param contractNumber - contract number report criteria.
     * @return - ReportCriteria object with all the criteria set in it.
     * @throws SystemException
     */
    public static ReportCriteria createReportCriteria(TPABlockOfBusinessForm reportForm,
            HttpServletRequest request, Long userProfileID, String userRole,
            Long mimicUserProfileID, String mimicUserRole,
            Integer dbSessionID, String contractStatus,
            String contractNumber) throws SystemException {
        ReportCriteria criteria = new ReportCriteria(TPABlockOfBusinessReportData.REPORT_ID);

        criteria.addFilter(TPABlockOfBusinessReportData.FILTER_USER_PROFILE_ID, userProfileID);

        criteria.addFilter(TPABlockOfBusinessReportData.FILTER_USER_ROLE, userRole);

        criteria.addFilter(TPABlockOfBusinessReportData.FILTER_MIMIC_USER_PROFILE_ID,
                mimicUserProfileID);

        criteria.addFilter(TPABlockOfBusinessReportData.FILTER_MIMIC_USER_ROLE, mimicUserRole);

        criteria.addFilter(TPABlockOfBusinessReportData.FILTER_DB_SESSION_ID, dbSessionID);

        criteria.addFilter(TPABlockOfBusinessReportData.FILTER_CONTRACT_STATUS_CODES,
                contractStatus);

        List<Date> monthEndDates = TPABlockOfBusinessUtility.getMonthEndDates();
        if (monthEndDates != null && !monthEndDates.isEmpty()) {
            Long reportAsOfDateL = monthEndDates.get(0).getTime();
            criteria.addFilter(TPABlockOfBusinessReportData.FILTER_AS_OF_DATE, reportAsOfDateL
                    .toString());
        }

        criteria.addFilter(TPABlockOfBusinessReportData.FILTER_CONTRACT_NUMBER_ID, contractNumber);

        return criteria;
    }
    
    public static TPABlockOfBusinessReportData callTPABOBStoredProc(ReportCriteria criteria)
            throws SystemException {
        TPABlockOfBusinessReportData tpabobReportData = null;
        try {
            tpabobReportData = (TPABlockOfBusinessReportData) ReportServiceDelegate.getInstance()
                    .getReportData(criteria);
        } catch (ReportServiceException e) {
            throw new SystemException(e,
                    "ReportServiceException occurred when calling the Block Of Business stored proc");
        }

        return tpabobReportData;
    }

    /**
     * This method gets the UserInfo details for a given UserProfileID.
     * 
     * @param profileId - UserProfileID
     * @return - UserInfo object containing the details of the user.
     * @throws SystemException
     */
    public static UserInfo getUserInfo(Long profileId) throws SystemException {
        UserInfo userInfo = SecurityServiceDelegate.getInstance().getDatabaseUserByProfileId(
                profileId);
        return userInfo;
    }
    
    /**
     * This method gets a list of car names to be shown in the TPA BOB page.
     * 
     * @param userProfileID
     * @return - a list of car names
     * @throws SystemException
     */
    public static List<LabelValueBean> getCarDropdownOptions(String userProfileID)
            throws SystemException {
        List<LabelValueBean> carList = new ArrayList<LabelValueBean>();
        
        if (!StringUtils.isBlank(userProfileID) && StringUtils.isNumeric(userProfileID)) {
            List<String> carNames = callCarStoredProc(Long.parseLong(userProfileID));
            if (carNames != null) {
                for (String carName : carNames) {
                    carList.add(new LabelValueBean(carName, carName));
                }
            }
        }
        return carList;
    }

    /**
     * This method calls the stored proc to retrieve a list of car names.
     * 
     * @param userProfileId
     * @return - a list of car names.
     * @throws SystemException
     */
    public static List<String> callCarStoredProc(Long userProfileId) throws SystemException {
        return TpaSearchServiceDelegate.getInstance().getCarForUserProfile(userProfileId);
    }
}

