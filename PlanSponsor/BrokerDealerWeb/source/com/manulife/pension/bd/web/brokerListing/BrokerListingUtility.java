package com.manulife.pension.bd.web.brokerListing;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manulife.pension.bd.service.brokerListing.valueobject.BrokerListingReportData;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.bob.blockOfBusiness.util.BlockOfBusinessUtility;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.util.LabelInfoBean;
import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;
import com.manulife.pension.service.security.role.BDAdministrator;
import com.manulife.pension.service.security.role.BDContentManager;
import com.manulife.pension.service.security.role.BDFirmRep;
import com.manulife.pension.service.security.role.BDInternalBasic;
import com.manulife.pension.service.security.role.BDNationalAccounts;
import com.manulife.pension.service.security.role.BDRvp;
import com.manulife.pension.service.security.role.BDUserRole;

/**
 * This is the utility class used by the Broker Listing Report.
 * 
 * @author HArlomte
 * 
 */
public class BrokerListingUtility {

    public static ArrayList<String> columnsList;
    
    public static Map<String, String> columnsInfoMap;

    // This will hold all the Filters for Broker Listing Report.
    public static ArrayList<String> allBrokerListingFilters;
    
    // This will hold advance Filters, used for CSV, PDF.
    public static ArrayList<String> advFilters;

    // This will hold quick Filters, used for CSV, PDF.
    public static ArrayList<String> quickFilters;
    
    // This Map will hold the Filter ID, Filter Title.
    public static Map<String, String> filterDetails;
    
    static {
        
        /**
         * List of All Advance Filters.
         */
        advFilters = new ArrayList<String>(9);
        advFilters.add(BrokerListingReportData.FILTER_FINANCIALREP_NAME_ID);
        advFilters.add(BrokerListingReportData.FILTER_BDFIRM_NAME_ID);
        advFilters.add(BrokerListingReportData.FILTER_CITY_NAME_ID);
        advFilters.add(BrokerListingReportData.FILTER_STATE_CODE_ID);
        advFilters.add(BrokerListingReportData.FILTER_ZIP_CODE_ID);
        advFilters.add(BrokerListingReportData.FILTER_PRODUCER_CODE_ID);
        advFilters.add(BrokerListingReportData.FILTER_RVP_ID);
		// advFilters.add(BrokerListingReportData.FILTER_REGION_ID);
		// advFilters.add(BrokerListingReportData.FILTER_DIVISION_ID);

        /**
         * List of All Quick Filters.
         */
        quickFilters = new ArrayList<String>(9);
        quickFilters.add(BrokerListingReportData.FILTER_QF_FINANCIALREP_NAME_ID);
        quickFilters.add(BrokerListingReportData.FILTER_QF_BDFIRM_NAME_ID);
        quickFilters.add(BrokerListingReportData.FILTER_QF_CITY_NAME_ID);
        quickFilters.add(BrokerListingReportData.FILTER_QF_STATE_CODE_ID);
        quickFilters.add(BrokerListingReportData.FILTER_QF_ZIP_CODE_ID);
        quickFilters.add(BrokerListingReportData.FILTER_QF_PRODUCER_CODE_ID);
        quickFilters.add(BrokerListingReportData.FILTER_QF_RVP_ID);
		// quickFilters.add(BrokerListingReportData.FILTER_QF_REGION_ID);
		// quickFilters.add(BrokerListingReportData.FILTER_QF_DIVISION_ID);

        /**
         * List of All Filters, irrespective of whether they are Advance or Quick Filters.
         */
        allBrokerListingFilters = new ArrayList<String>(9);
        allBrokerListingFilters.add(BrokerListingReportData.FILTER_FINANCIALREP_NAME_ID);
        allBrokerListingFilters.add(BrokerListingReportData.FILTER_BDFIRM_NAME_ID);
        allBrokerListingFilters.add(BrokerListingReportData.FILTER_CITY_NAME_ID);
        allBrokerListingFilters.add(BrokerListingReportData.FILTER_STATE_CODE_ID);
        allBrokerListingFilters.add(BrokerListingReportData.FILTER_ZIP_CODE_ID);
        allBrokerListingFilters.add(BrokerListingReportData.FILTER_PRODUCER_CODE_ID);
        allBrokerListingFilters.add(BrokerListingReportData.FILTER_RVP_ID);
		// allBrokerListingFilters.add(BrokerListingReportData.FILTER_REGION_ID);
		// allBrokerListingFilters.add(BrokerListingReportData.FILTER_DIVISION_ID);

        /**
         * This Map has the Information related to Filters - Filter ID, Filter Title.
         */
         // Advance Filter related Filter ID's
        filterDetails = new HashMap<String, String>();
        filterDetails.put(BrokerListingReportData.FILTER_FINANCIALREP_NAME_ID, BDConstants.FILTER_FINANCIALREP_NAME_TITLE);
        filterDetails.put(BrokerListingReportData.FILTER_BDFIRM_NAME_ID, BDConstants.FILTER_BDFIRM_NAME_TITLE);
        filterDetails.put(BrokerListingReportData.FILTER_CITY_NAME_ID, BDConstants.FILTER_CITY_NAME_TITLE);
        filterDetails.put(BrokerListingReportData.FILTER_STATE_CODE_ID, BDConstants.FILTER_STATE_CODE_TITLE);
        filterDetails.put(BrokerListingReportData.FILTER_ZIP_CODE_ID, BDConstants.FILTER_ZIP_CODE_TITLE);
        filterDetails.put(BrokerListingReportData.FILTER_PRODUCER_CODE_ID, BDConstants.FILTER_PRODUCER_CODE_TITLE);
        filterDetails.put(BrokerListingReportData.FILTER_RVP_ID, BDConstants.FILTER_RVP_TITLE);
		// filterDetails.put(BrokerListingReportData.FILTER_REGION_ID, BDConstants.FILTER_REGION_TITLE);
		// filterDetails.put(BrokerListingReportData.FILTER_DIVISION_ID, BDConstants.FILTER_DIVISION_TITLE);
         // Quick Filter related Filter ID's
        filterDetails.put(BrokerListingReportData.FILTER_QF_FINANCIALREP_NAME_ID, BDConstants.FILTER_FINANCIALREP_NAME_TITLE);
        filterDetails.put(BrokerListingReportData.FILTER_QF_BDFIRM_NAME_ID, BDConstants.FILTER_BDFIRM_NAME_TITLE);
        filterDetails.put(BrokerListingReportData.FILTER_QF_CITY_NAME_ID, BDConstants.FILTER_CITY_NAME_TITLE);
        filterDetails.put(BrokerListingReportData.FILTER_QF_STATE_CODE_ID, BDConstants.FILTER_STATE_CODE_TITLE);
        filterDetails.put(BrokerListingReportData.FILTER_QF_ZIP_CODE_ID, BDConstants.FILTER_ZIP_CODE_TITLE);
        filterDetails.put(BrokerListingReportData.FILTER_QF_PRODUCER_CODE_ID, BDConstants.FILTER_PRODUCER_CODE_TITLE);
        filterDetails.put(BrokerListingReportData.FILTER_QF_RVP_ID, BDConstants.FILTER_RVP_TITLE);
		// filterDetails.put(BrokerListingReportData.FILTER_QF_REGION_ID, BDConstants.FILTER_REGION_TITLE);
		// filterDetails.put(BrokerListingReportData.FILTER_QF_DIVISION_ID, BDConstants.FILTER_DIVISION_TITLE);
        
        /**
         * List of all Column ID's.
         */
        columnsList = new ArrayList<String>(8);
        columnsList.add(BrokerListingReportData.COL_FINANCIAL_REP_NAME_ID);
        columnsList.add(BrokerListingReportData.COL_FIRM_NAME_ID);
        columnsList.add(BrokerListingReportData.COL_CITY_ID);
        columnsList.add(BrokerListingReportData.COL_STATE_ID);
        columnsList.add(BrokerListingReportData.COL_ZIP_CODE_ID);
        columnsList.add(BrokerListingReportData.COL_PRODUCER_CODE_ID);
        columnsList.add(BrokerListingReportData.COL_NUM_OF_CONTRACTS_ID);
        columnsList.add(BrokerListingReportData.COL_BL_TOTAL_ASSETS_ID);
        
        /**
         * This will hold the information of a Column ID, Column Title.
         */
        columnsInfoMap = new HashMap<String, String>();
        columnsInfoMap.put(BrokerListingReportData.COL_FINANCIAL_REP_NAME_ID, BDConstants.COL_FINANCIAL_REP_NAME_TITLE);
        columnsInfoMap.put(BrokerListingReportData.COL_FIRM_NAME_ID, BDConstants.COL_FIRM_NAME_TITLE);
        columnsInfoMap.put(BrokerListingReportData.COL_CITY_ID, BDConstants.COL_CITY_TITLE);
        columnsInfoMap.put(BrokerListingReportData.COL_STATE_ID, BDConstants.COL_STATE_TITLE);
        columnsInfoMap.put(BrokerListingReportData.COL_ZIP_CODE_ID, BDConstants.COL_ZIP_CODE_TITLE);
        columnsInfoMap.put(BrokerListingReportData.COL_PRODUCER_CODE_ID, BDConstants.COL_PRODUCER_CODE_TITLE);
        columnsInfoMap.put(BrokerListingReportData.COL_NUM_OF_CONTRACTS_ID, BDConstants.COL_NUM_OF_CONTRACTS_TITLE);
        columnsInfoMap.put(BrokerListingReportData.COL_BL_TOTAL_ASSETS_ID, BDConstants.COL_BL_TOTAL_ASSETS_TITLE);
        
    }
    
    /**
     * This method returns a list of all Column IDs.
     * 
     * @return - A list of all the column IDs.
     */
    public static ArrayList<String> getColumnsList() {
        return columnsList;
    }

    /**
     * This method returns a Map containing the column ID, column Title.
     * 
     * @return - A Map containing the column ID, column Title.
     */
    public static Map<String, String> getColumnsInfoMap() {
        return columnsInfoMap;
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
            cal.add(Calendar.MONTH, BDConstants.NUM_MINUS_1);
            int lastDayOfTheMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            cal.set(Calendar.DAY_OF_MONTH, lastDayOfTheMonth);
            dates.add(cal.getTime());
        }

        return dates;
    }

    /**
     * This method gives back a Map containing the Filter ID, LabelInfoBean containing Filter
     * information for all the Filters. The filters will be set with enabled="true" or "false" based
     * on if the filter is applicable for a given user or not.
     * 
     * @param userRole - User Role
     * @param isInMimicMode - true, if the user is in mimic mode, else, false.
     * @return - Map containing Filter ID as key, LableInfoBean having filter info as Value.
     */
    public static Map<String, LabelInfoBean> getApplicableFilters(BDUserRole userRole,
            BDUserProfile userProfile) {
        Map<String, LabelInfoBean> allFilters = getAllFilters();
        applyUserRulesToGetApplicableFilters(userRole, userProfile.isInMimic(), allFilters);
        
        // If the user is a BDFirmRep, and if he is associated with only one Firm, then, we are
        // disabling the Firm Filter.
        List<BrokerDealerFirm> brokerDealerFirms = BlockOfBusinessUtility
                .getAssociatedFirmsForBDFirmRep(userProfile);
        if (userRole instanceof BDFirmRep) {
            if (brokerDealerFirms != null && brokerDealerFirms.size() <= 1) {
                LabelInfoBean firmFilterInfoBean = allFilters
                        .get(BrokerListingReportData.FILTER_BDFIRM_NAME_ID);
                if (firmFilterInfoBean != null) {
                    firmFilterInfoBean.setEnabled(Boolean.FALSE);
                    allFilters.put(BrokerListingReportData.FILTER_BDFIRM_NAME_ID,
                            firmFilterInfoBean);
                }
            }
        }
        return allFilters;
    }

    /**
     * This method returns a Map of all the Filters.
     * 
     * @return
     */
    public static Map<String, LabelInfoBean> getAllFilters() {
        Map<String, LabelInfoBean> allFilters = new HashMap<String, LabelInfoBean>();

        allFilters.put(BDConstants.FILTER_BLANK_ID, new LabelInfoBean(BDConstants.FILTER_BLANK_ID,
                BDConstants.FILTER_BLANK_TITLE, Boolean.TRUE));
        for (String filterID : allBrokerListingFilters) {
            allFilters.put(filterID, new LabelInfoBean(filterID, filterDetails.get(filterID),
                    Boolean.TRUE));
        }

        return allFilters;
    }

    /**
     * This method is used to apply user Role related Filtering conditions to check which of the
     * filters are applicable for a given user role.
     * 
     * @param userRole - User Role
     * @param isInMimicMode - is in mimic mode.
     * @param allFilters - A Map of all the Filters.
     */
    public static void applyUserRulesToGetApplicableFilters(BDUserRole userRole,
            boolean isInMimicMode, 
            Map<String, LabelInfoBean> allFilters) {
        if (isInternalUserAndNotRVP(userRole) && !isInMimicMode) {
            // Show all the 9 filters...
        } else if (userRole instanceof BDRvp && !isInMimicMode) {
            // Show 6 filters...
            disableFilter(BrokerListingReportData.FILTER_RVP_ID, allFilters);
			// disableFilter(BrokerListingReportData.FILTER_REGION_ID, allFilters);
			// disableFilter(BrokerListingReportData.FILTER_DIVISION_ID, allFilters);
        } else {
            // Show 6 filters...
            disableFilter(BrokerListingReportData.FILTER_RVP_ID, allFilters);
			// disableFilter(BrokerListingReportData.FILTER_REGION_ID, allFilters);
			// disableFilter(BrokerListingReportData.FILTER_DIVISION_ID, allFilters);
        }
    }
    
    /**
     * This method is used to disable a particular filter.
     * 
     * @param filterID - the filterID of the filter to be disabled.
     * @param allFilters - A Map containing all the filters.
     */
    public static void disableFilter(String filterID, Map<String, LabelInfoBean> allFilters) {
        LabelInfoBean filterInfo = allFilters.get(filterID);
        filterInfo.setEnabled(Boolean.FALSE);
        allFilters.put(filterID, filterInfo);
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
                || userRole instanceof BDContentManager || userRole instanceof BDAdministrator)
                && !(userRole instanceof BDRvp);
    }

}
