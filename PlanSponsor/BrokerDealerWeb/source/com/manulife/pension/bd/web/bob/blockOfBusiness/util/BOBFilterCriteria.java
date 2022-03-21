package com.manulife.pension.bd.web.bob.blockOfBusiness.util;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.util.LabelInfoBean;
import com.manulife.pension.ps.service.report.bob.valueobject.BlockOfBusinessReportData;
import com.manulife.pension.service.security.role.BDCar;
import com.manulife.pension.service.security.role.BDFirmRep;
import com.manulife.pension.service.security.role.BDInternalUser;
import com.manulife.pension.service.security.role.BDRvp;
import com.manulife.pension.service.security.role.BDUserRole;

/**
 * This class will be used by BOB action class to get the applicable filters.
 * 
 * @author harlomte
 * 
 */
public class BOBFilterCriteria {

    private static BOBFilterStore bobFilterStore;

    /**
     * This method will retrieve the applicable filters for a particular tab, user role.
     * 
     * @param tabName - the tab name.
     * @param userRole - the BDUserRole object.
     * @param isMimicMode - whether the user is in mimic mode or not.
     * @return - BOBFilterMap object containing the applicable filters info.
     * @throws SystemException
     */
    public static BOBFilterMap getApplicableFilters(String tabName, BDUserRole userRole,
            Boolean isMimicMode) throws SystemException {
        
        BOBFilterMap bobFilterList = getFilterStore().getAllFiltersForTab(tabName);
        
        if (bobFilterList == null) {
            throw new SystemException(BDConstants.UNABLE_TO_GET_FILTER_LIST);
        }
        applyUserRoleRules(bobFilterList, userRole, isMimicMode);
        
        return bobFilterList;
        
    }

    /**
     * This method retrieves the filter store retrieved from the XML file.
     * 
     * @return - BOBFilterStore object.
     * @throws SystemException
     */
    public static BOBFilterStore getFilterStore() throws SystemException {
        
        if (bobFilterStore == null) {
            URL filterFileURL = BOBFilterCriteria.class.getClassLoader().getResource(
                    BDConstants.BOB_FILTER_CONFIG_FILE);
            
            if (filterFileURL == null) {
                throw new SystemException(BDConstants.UNABLE_TO_GET_FILTER_STORE);
            }

            String filterFile = filterFileURL.getPath();

            ApplicationContext filterCtx = new FileSystemXmlApplicationContext(filterFile);

            bobFilterStore = (BOBFilterStore) filterCtx.getBean(BDConstants.ALL_FILTERS);
        }

        if (bobFilterStore == null) {
            throw new SystemException(BDConstants.UNABLE_TO_GET_FILTER_STORE);
        } else {
            return createFilterStoreClone();
        }
    }

    /**
     * This method applies the conditions related to user role, before giving a final list of
     * applicable filters.
     * 
     * @param bobFilterList - the Filters that have been retrieved from XML file, and on which the
     *            user role related conditions have not yet been applied.
     * @param userRole - BDUserRole object.
     * @param isMimicMode - whether the user is in mimic mode or not.
     */
    private static void applyUserRoleRules(BOBFilterMap bobFilterList, BDUserRole userRole,
            Boolean isMimicMode) {
        Boolean enableFinancialRepName = false;
        Boolean enableFirmName = false;
        Boolean enableRVPRegionDivisionAdvanceFilter = false;
        Boolean disableContractNumber = false;
        Boolean enableRVPRegionDivisionQuickFilter = false;
        
        if (BlockOfBusinessUtility.isInternalUserAndNotRVP(userRole)) {
            if (isMimicMode != null && !isMimicMode) {
            	// if internal user and not RVP , enable quick filter RVP
            	enableRVPRegionDivisionQuickFilter = true;
                enableFirmName = true;
                enableFinancialRepName = true;
            }
        } else if (userRole instanceof BDRvp) {
            if (isMimicMode != null && !isMimicMode) {
                enableFirmName = true;
                enableFinancialRepName = true;
            }
        } else if (userRole instanceof BDFirmRep) {
            enableFirmName = true;
            enableFinancialRepName = true;
        } 
        
        // if not RVP , enable advance filter by RVP
        if( !(userRole instanceof BDRvp)){
        	enableRVPRegionDivisionAdvanceFilter = true;
        }
        
        if (!enableFinancialRepName) {
            disableAdvanceQuickFilters(bobFilterList, BlockOfBusinessReportData.FILTER_FINANCIAL_REP_NAME);
        }
        if (!enableFirmName) {
            disableAdvanceQuickFilters(bobFilterList, BlockOfBusinessReportData.FILTER_BDFIRM_NAME);
        }
        // disable advance filter if RVP user
        if (!enableRVPRegionDivisionAdvanceFilter) {
        	disableAdvanceFilters(bobFilterList, BlockOfBusinessReportData.FILTER_RPV_NAME);
        	disableAdvanceFilters(bobFilterList, BlockOfBusinessReportData.FILTER_SALES_REGION);
        	disableAdvanceFilters(bobFilterList,
                    BlockOfBusinessReportData.FILTER_SALES_DIVISION);
        }
        // disable quick filter if external/RVP users
        if (!enableRVPRegionDivisionQuickFilter) {
        	disableQuickFilters(bobFilterList, BlockOfBusinessReportData.FILTER_RPV_NAME);
        	disableQuickFilters(bobFilterList, BlockOfBusinessReportData.FILTER_SALES_REGION);
        	disableQuickFilters(bobFilterList,
                    BlockOfBusinessReportData.FILTER_SALES_DIVISION);
        }
    }

    /**
     * This method would disable a given filter.
     * 
     * @param bobFilterList - the filter list from which a particular filter has to be disabled.
     * @param filterKey - the filter id which needs to be disabled.
     */
    private static void disableAdvanceQuickFilters(BOBFilterMap bobFilterList, String filterKey) {
        disableFilter(bobFilterList.getApplicableAdvFilters(), filterKey);
        disableFilter(bobFilterList.getApplicableQuickFilters(), filterKey);
    }
    
    /**
     * This method would disable a given filter.
     * 
     * @param bobFilterList - the filter list from which a particular filter has to be disabled.
     * @param filterKey - the filter id which needs to be disabled.
     */
    private static void disableAdvanceFilters(BOBFilterMap bobFilterList, String filterKey) {
        disableFilter(bobFilterList.getApplicableAdvFilters(), filterKey);
    }
    
    /**
     * This method would disable a given quick filter.
     * 
     * @param bobFilterList - the filter list from which a particular filter has to be disabled.
     * @param filterKey - the filter id which needs to be disabled.
     */
    private static void disableQuickFilters(BOBFilterMap bobFilterList, String filterKey) {
        disableFilter(bobFilterList.getApplicableQuickFilters(), filterKey);
    }
    
    /**
     * This method disables a given filter.
     * 
     * @param bobFilterMap
     * @param filterKey
     */
    private static void disableFilter(Map<String, LabelInfoBean> bobFilterMap, String filterKey) {
        LabelInfoBean bobFilter = bobFilterMap.get(filterKey);
        if (bobFilter == null) {
            return;
        }
        bobFilter.setEnabled(Boolean.FALSE);
        
        bobFilterMap.put(filterKey, bobFilter);
    }
    
    /**
     * This method creates a clone of the bobFilterStore.
     * 
     * @return
     */
    private static BOBFilterStore createFilterStoreClone() {
        BOBFilterStore bobFilterStoreCloned = new BOBFilterStore();
        Map<String, BOBFilterMap> filtersApplicableToTabClone = new HashMap<String, BOBFilterMap>();

        Map<String, BOBFilterMap> filtersApplicableToTab = bobFilterStore.getFiltersApplicableToTab();
        for (String key : filtersApplicableToTab.keySet()) {
            BOBFilterMap bobFilterMap = filtersApplicableToTab.get(key);
            
            BOBFilterMap bobFilterMapClone = new BOBFilterMap();
            if (bobFilterMap != null) {
                Map<String, LabelInfoBean> advFilters = bobFilterMap.getApplicableAdvFilters();
                Map<String, LabelInfoBean> quickFilters = bobFilterMap.getApplicableQuickFilters();
                
                Map<String, LabelInfoBean> advFiltersClone = new HashMap<String, LabelInfoBean>();
                if (advFilters != null) {
                    for (String advFilterKey : advFilters.keySet()) {
                        LabelInfoBean advFilterInfoBean = advFilters.get(advFilterKey);
                        LabelInfoBean advFilterInfoBeanClone = new LabelInfoBean(
                                advFilterInfoBean.getId(), advFilterInfoBean.getTitle(),
                                advFilterInfoBean.getEnabled());
                        advFiltersClone.put(advFilterKey, advFilterInfoBeanClone);
                    }
                }

                Map<String, LabelInfoBean> quickFiltersClone = new HashMap<String, LabelInfoBean>();
                if (quickFilters != null) {
                    for (String quickFilterKey : quickFilters.keySet()) {
                        LabelInfoBean quickFilterInfoBean = quickFilters.get(quickFilterKey);
                        LabelInfoBean quickFilterInfoBeanClone = new LabelInfoBean(
                                quickFilterInfoBean.getId(), quickFilterInfoBean.getTitle(),
                                quickFilterInfoBean.getEnabled());
                        quickFiltersClone.put(quickFilterKey, quickFilterInfoBeanClone);
                    }
                }
                
                bobFilterMapClone.setApplicableAdvFilters(advFiltersClone);
                bobFilterMapClone.setApplicableQuickFilters(quickFiltersClone);
            }
            filtersApplicableToTabClone.put(key, bobFilterMapClone);
        }
        bobFilterStoreCloned.setFiltersApplicableToTab(filtersApplicableToTabClone);
        return bobFilterStoreCloned;
    }
}
