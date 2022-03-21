package com.manulife.pension.platform.utility.ireports.utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ireports.dao.ContractDAO;
import com.manulife.pension.ireports.dao.DAOFactory;
import com.manulife.pension.ireports.dao.ReportDataRepository;
import com.manulife.pension.ireports.dao.ReportDataRepositoryFactory;
import com.manulife.pension.ireports.model.FundOffering;
import com.manulife.pension.ireports.report.ReportFormattingConstants;
import com.manulife.pension.ireports.util.ContractFundOfferingDeterminator;
import com.manulife.pension.ireports.util.FundHelper;
import com.manulife.pension.platform.utility.fap.constants.FapConstants;
import com.manulife.pension.platform.utility.ireports.FundReportConstants;
import com.manulife.pension.platform.utility.ireports.valueobject.FundReportParamsHolderVO;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.fandp.valueobject.FandpFilterCriteria;
import com.manulife.pension.service.fund.fandp.valueobject.FundInformation;
import com.manulife.pension.service.fund.fandp.valueobject.FundsAndPerformance;
import com.manulife.pension.service.fund.standardreports.valueobject.CurrentAsOfDate;
import com.manulife.pension.service.fund.standardreports.valueobject.Fund;
import com.manulife.pension.service.fund.standardreports.valueobject.StandardReportsConstants;

/**
 * This class is a Utility class that assists in i:report Generation part.
 * 
 * @author harlomte
 * 
 */
public class FundReportUtil {

    /**
     * This method returns back the "fully qualified class name" which will be called to generate
     * the i:report PDF.
     * 
     * @param groupBy - Group by = Asset Class / Risk Return.
     * @param reportName - Report selected by the user in online F&P page.
     * @return - Fully qualified Class Name of the class which will be called to generate i:report
     *         PDF.
     */
    public static String getReportClassName(String selectedGroup, String selectedReport) {
        String selectedReportClassName = FundReportConstants.EMPTY_STRING;
        if (!StringUtils.isBlank(selectedReport)) {
            if (FundReportUtil.isBelongsToOtherReportSection(selectedReport)) {
                selectedReportClassName = FundReportConstants.otherReportIdToClassNameMap
                        .get(selectedReport);
            } else if (!StringUtils.isBlank(selectedGroup)) {
                if (FundReportConstants.groupIdToReportIdClassNameMap.get(selectedGroup) != null) {
                    selectedReportClassName = FundReportConstants.groupIdToReportIdClassNameMap
                            .get(selectedGroup).get(selectedReport);
                }
            }
        }

        return selectedReportClassName;
    }

    /**
     * This method returns true if the selected report is one of i:reports.
     * 
     * @param selectedReport - The report selected by the user in F&P page.
     * @return - true if the selected report is one of i:reports, else, returns false.
     */
    public static boolean isIreport(String selectedReport) {
        return FapConstants.EXPENSE_RATIO_REPORT.equals(selectedReport)
                || FapConstants.FUND_CHARACTERISITICS_REPORT.equals(selectedReport)
                || FapConstants.INVESTEMNT_RETURNS_AND_EXPENSE_RATIO_REPORT.equals(selectedReport)
                || FapConstants.INVESTEMNT_RETURNS_AND_STANDARD_DEVIATION_REPORT.equals(selectedReport)
                || FapConstants.MORNINGSTAR_RATINGS_AND_TICKR_SYMBOLS_REPORT.equals(selectedReport)
                || FapConstants.MARKET_INDEX_REPORT.equals(selectedReport);
    }
    
    /**
     * This method tests if a given report belongs to Other Reports section or not.
     * 
     * @param reportName
     * @return
     */
    public static Boolean isBelongsToOtherReportSection(String reportSelected) {
        return FapConstants.MARKET_INDEX_REPORT.equals(reportSelected);
    }

    /**
     * 1. Get the Query Executed. 2. If the Query is "All Available Funds", run the query to fetch
     * the FundID's of All Available funds 3. If the Query is "Retail Funds", run the query to fetch
     * the FundID's of Retail funds 4. If the Query is "Sub-advised Funds", run the query to fetch
     * the FundID's of Sub-advised funds 5. If the Query is "ShortList Funds", Get the other 4
     * options aswell. Run the query to fetch the FundID's of Short List funds. 6. Compare the
     * available Fund ID's with those of selected funds. 7. If equal, return fund List Match = true,
     * else return false. 8. Note: No need to worry about whether NML / Closed Funds was selected or
     * not.
     * 
     * @param request
     * @param fundsAndPerformance
     * @return
     * @throws SystemException
     */
    public static boolean isFundListMatch(HttpServletRequest request,
            FundsAndPerformance fundsAndPerformance) throws SystemException {

        // If the Base Filter was used, return true.
        if (!isAdvancedFilterEnabled(request)) {
            return true;
        }

        // If the Advanced Filter other than All Available / Retail / Sub-advised / ShortList was
        // used, return false.
        FandpFilterCriteria filterCriteria = getLastExecutedFilterCriteria(request);
        if (FapConstants.OTHERS.equals(filterCriteria.getAdvanceFilterOption())) {
            return false; 
        }

        List<String> fundIdsSelected = getFundsChosen(fundsAndPerformance);
        List<String> fundIdsAvailable = getFundIdsAvailable(request);

        if (fundIdsSelected == null || fundIdsSelected.isEmpty() || fundIdsAvailable == null
                || fundIdsAvailable.isEmpty()) {
            return false;
        }

        Collections.sort(fundIdsAvailable);
        Collections.sort(fundIdsSelected);

        return fundIdsAvailable.equals(fundIdsSelected);

    }

    /**
     * This method retrieves all the Funds ID's available for a given Advanced Filter criteria in
     * F&P page. This method will work only for "All Available Funds", "Retail Funds",
     * "Sub-Advised Funds", "Short List Options used". For other Advanced Filter options / Base
     * Filter options, this method does not work. This method will not give those Fund Id's related
     * to NML, Closed Funds if these two options were chosen by the user in F&P page.
     * 
     * @param request
     * @return - All the Fund ID's for a given filter criteria.
     * @throws SystemException
     */
    public static List<String> getFundIdsAvailable(HttpServletRequest request)
            throws SystemException {
        List<String> fundsIdsAvailable = new ArrayList<String>();

        FandpFilterCriteria filterCriteria = (FandpFilterCriteria) request.getSession(false)
                .getAttribute(FapConstants.LAST_EXECUTED_FILTER_CRITERIA);

        if (filterCriteria != null) {
            fundsIdsAvailable = FundServiceDelegate.getInstance().getFundIdsForAdvanceFilter(
                    filterCriteria);
        }

        return fundsIdsAvailable;
    }

    /**
     * This method is used to retrieve the Funds chosen by the user in F&P page.
     * 
     * @param fundsAndPerformance
     * @return - A List of Funds Chosen.
     */
    public static ArrayList<String> getFundsChosen(FundsAndPerformance fundsAndPerformance) {
        // Funds List - Funds being shown in the Main Report
        // Map has Key as Group Name and value as List of Funds belonging to that group.
        ArrayList<String> fundsChosenList = new ArrayList<String>();

        Map<String, List<FundInformation>> fundInformation = fundsAndPerformance
                .getFundInformation();
        Collection<List<FundInformation>> fundInfoListColl = fundInformation.values();
        if (fundInfoListColl != null) {
            for (List<FundInformation> fundInfoList : fundInfoListColl) {
                if (fundInfoList != null) {
                    for (FundInformation fundInfoVO : fundInfoList) {
                        if (!fundInfoVO.isMarketIndexFund() && !fundInfoVO.isGuaranteedFund()) {
                            fundsChosenList.add(fundInfoVO.getFundId());
                        }
                    }
                }
            }
        }

        // Add Guaranteed Account Funds chosen.
        if (fundsAndPerformance.getGuaranteedFunds() != null
                && !fundsAndPerformance.getGuaranteedFunds().isEmpty()) {
            fundsChosenList.addAll(fundsAndPerformance.getGuaranteedFunds());
        }
        
        return fundsChosenList;
    }
    
    /**
     * This method returns true if the Advanced Filter in F&P page was enabled & used. Use this
     * method to determine if the Advanced Filters was used or not.
     * 
     * @param request
     * @return - true if the Advanced Filter was used.
     */
    public static boolean isAdvancedFilterEnabled(HttpServletRequest request) {
        boolean isAdvancedFilterEnabled = false;

        if (getLastExecutedFilterCriteria(request) != null) {
            isAdvancedFilterEnabled = true;
        }

        return isAdvancedFilterEnabled;
    }

    /**
     * This method gives back a FandpFilterCriteria object that holds the last query executed in F&P
     * page.
     * 
     * @param request
     * @return - FandpFilterCriteria object that holds the last query executed in F&P page.
     */
    public static FandpFilterCriteria getLastExecutedFilterCriteria(HttpServletRequest request) {
        FandpFilterCriteria filterCriteria = (FandpFilterCriteria) request.getSession(false)
                .getAttribute(FapConstants.LAST_EXECUTED_FILTER_CRITERIA);

        return filterCriteria;
    }

    /**
     * This method retrieves a Map of lifecycle portfolio's. This is shown on the Main report.
     * 
     * @param fundOffering
     * @return - Map of lifecycle portfolio's.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getLifecyclePortfolios(FundOffering fundOffering) {
        ReportDataRepository reportData = ReportDataRepositoryFactory.getRepository();
        if (fundOffering.getFundMenu() != -1) {
            Map fundMap = reportData.getFunds(fundOffering,
                    StandardReportsConstants.ASSET_CLASS_LIFECYCLE);
            fundMap = FundHelper.sortFunds(fundMap, FundHelper.FUND_SORT_ORDER_RISKRETURN);
            Map<String, String> menuMap = new LinkedMap();
            for (Iterator iter = fundMap.entrySet().iterator(); iter.hasNext();) {
                Map.Entry entry = (Map.Entry) iter.next();
                menuMap.put((String) entry.getKey(), ((Fund) entry.getValue()).getFundname());
            }
            return menuMap;
        } else {
            return Collections.EMPTY_MAP;
        }
    }
    
    /**
     * This method retrieves a Map of lifestyle portfolio's. This is shown on the Main report.
     * 
     * @param fundOffering
     * @return - Map of lifecycle portfolio's.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getLifestylePortfolios(FundOffering fundOffering) {
        ReportDataRepository reportData = ReportDataRepositoryFactory.getRepository();
        if (fundOffering.getFundMenu() != -1) {
            Map fundMap = reportData.getFunds(fundOffering,
                    StandardReportsConstants.ASSET_CLASS_LIFESTYLE);
            fundMap = FundHelper.sortFunds(fundMap, FundHelper.FUND_SORT_ORDER_RISKRETURN);
            Map<String, String> menuMap = new LinkedMap();
            for (Iterator iter = fundMap.entrySet().iterator(); iter.hasNext();) {
                Map.Entry entry = (Map.Entry) iter.next();
                menuMap.put((String) entry.getKey(), ((Fund) entry.getValue()).getFundname());
            }
            return menuMap;
        } else {
            return Collections.EMPTY_MAP;
        }
    }

    /**
     * This method creates the FundOffering object.
     * 
     * @param fundReportParams
     * @param request
     * @return - FundOffering object.
     */
    public static FundOffering createFundOffering(FundReportParamsHolderVO fundReportParams,
            HttpServletRequest request) {
        
        FundsAndPerformance fundsAndPerformance = (FundsAndPerformance) request.getSession()
                .getAttribute(FapConstants.VO_FUNDS_AND_PERFORMANCE);

        FandpFilterCriteria fandPFilterCriteria = fundsAndPerformance.getFilterCriteriaUsed();
        
        Integer fundMenu = FundReportConstants.ALL_FUNDS_MENU_NUM;
        boolean includeNML = false;

        if (!StringUtils.isBlank(fundReportParams.getContractNumberSelected())) {
            // Contract Selected.
            com.manulife.pension.ireports.model.Contract contract = ((ContractDAO) DAOFactory
                    .create(ContractDAO.class.getName()))
                    .retrieveContractByContractNumber(fundReportParams.getContractNumberSelected());

            ContractFundOfferingDeterminator determinator = new ContractFundOfferingDeterminator(
                    fandPFilterCriteria.getCompanyName(), contract); // fapForm.getCompanyId()
            if (!determinator.isContractMixAndMatch()) {
                try {
                    fundMenu = Integer.parseInt(determinator.getFundMenuCode());
                } catch (NumberFormatException ne) {
                    fundMenu = -1;
                }
                includeNML = determinator.getIncludeNML() == FundReportConstants.Y_SYMBOL ? Boolean.TRUE
                        : Boolean.FALSE;
            }
        } else if (!isAdvancedFilterEnabled(request)) {
            if (FapConstants.BASE_FILTER_ALL_FUNDS_KEY.equals(fandPFilterCriteria.getViewBy())) { // fapForm.getBaseFilterSelect()
                fundMenu = FundReportConstants.ALL_FUNDS_MENU_NUM;
                includeNML = fundReportParams.isIncludeNml();
            }
        } else {
            Map<String, String> fundMenuMap = getFundMenuShortlistTypeGenericModified(
                    fundReportParams.isFundListMatch(), request);
            String fundMenuID = fundMenuMap.get(FundReportConstants.FUND_MENU_ID);
            if (!StringUtils.isBlank(fundMenuID)) {
                fundMenu = FundReportConstants.fundsMenuIDToFundMenuNumMap.get(fundMenuID);
            }
            includeNML = fundReportParams.isIncludeNml();
        }

        FundOffering fundOffering = new FundOffering(fandPFilterCriteria.getCompanyName(), fundMenu,
                includeNML); // fapForm.getCompanyId()

        return fundOffering;

    }

    /**
     * This method creates a Map of Title information to be shown on the i:reports PDF.
     * 
     * @param fundReportParams
     * @param request
     * @return - Map containing the Title information
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getReportTitle(FundReportParamsHolderVO fundReportParams,
            HttpServletRequest request) {

        boolean fundListMatch = fundReportParams.isFundListMatch();

        Map<String, String> titleListMap = new LinkedMap();

        String title1 = FundReportConstants.EMPTY_STRING;
        String title2 = FundReportConstants.EMPTY_STRING;
        String title3 = FundReportConstants.EMPTY_STRING;
        String title4 = FundReportConstants.EMPTY_STRING;
        String title5 = FundReportConstants.EMPTY_STRING;
        String title6 = FundReportConstants.EMPTY_STRING;

        ReportDataRepository reportDataRepository = ReportDataRepositoryFactory.getRepository();

        String classSelected = (String) reportDataRepository.getFundClassMenu().get(
                fundReportParams.getClassSelected());

        if (!StringUtils.isBlank(fundReportParams.getContractNumberSelected())) {
            // Contract Funds view
            Contract contractVO = fundReportParams.getContractSelected();
            title1 = FundReportConstants.EMPTY_STRING;
            title2 = getReportNameAndAdditionalName(fundReportParams.getReportSelected(),
                    fundReportParams.getSubReportSelected(), classSelected,
                    FundReportConstants.CONTRACT_VIEW);

            if (contractVO != null) {
                title3 = FundReportConstants.CONTRACT_NAME_TITLE + contractVO.getCompanyName();
                title4 = FundReportConstants.CONTRACT_NUMBER_TITLE + contractVO.getContractNumber()
                        + getContractModified(fundListMatch, request);
            }
            
            String advisorName = fundReportParams.getAdvisorName();
            title5 = "Prepared By : " + advisorName;
            
            title6 = FundReportConstants.PREPARED_ON_TITLE
                    + DateFormatUtils.format(new Date(),
                            ReportFormattingConstants.DATE_PATTERN_AS_OF_DATE);
        } else {
            // Generic Funds view
            String subTitle = fundReportParams.getSubTitle();

            Map<String, String> titleInfoMap = getFundMenuShortlistTypeGenericModified(
                    fundListMatch, request);

            String fundMenuID = titleInfoMap.get(FundReportConstants.FUND_MENU_ID);
            String fundMenuTitle = FundReportConstants.EMPTY_STRING;
            if (!StringUtils.isBlank(fundMenuID)) {
                fundMenuTitle = FundReportConstants.fundsMenuIDToFundMenuTitleMap.get(fundMenuID);
            }

            // For Market Index reports, the title 1 is blank.
            if (FapConstants.MARKET_INDEX_REPORT.equals(fundReportParams.getReportSelected())) {
                title1 = FundReportConstants.EMPTY_STRING;
            } else {
                title1 = FundReportConstants.JH_SIGNATURE_TITLE + fundMenuTitle
                        + FundReportConstants.OPENING_BRACE + classSelected
                        + FundReportConstants.CLOSING_BRACE;
            }

            title2 = getReportNameAndAdditionalName(fundReportParams.getReportSelected(),
                    fundReportParams.getSubReportSelected(), classSelected,
                    FundReportConstants.ALL_FUNDS_VIEW);

            if(StringUtils.isNotEmpty(subTitle)) {
            	title3 = "Prepared For : " + subTitle;
            }
            
            String advisorName = fundReportParams.getAdvisorName();
            title4 = "Prepared By : " + advisorName;

            // For Market Index
            // reports, title 4 is displayed as a constant value.
            if (FapConstants.MARKET_INDEX_REPORT.equals(fundReportParams.getReportSelected())) {
                title5 = FundReportConstants.EMPTY_STRING;
                if (FapConstants.MARKET_INDEX_REPORT.equals(fundReportParams.getReportSelected())) {
                    title5 = FundReportConstants.MARKET_INDEX_TITLE4;
                }
            } else {
                title5 = titleInfoMap.get(FundReportConstants.SHORTLIST_TYPE_ID)
                        + FundReportConstants.SINGLE_SPACE
                        + titleInfoMap.get(FundReportConstants.GENERIC_MODIFIED_ID);
            }
            
            title6 = FundReportConstants.PREPARED_ON_TITLE
                    + DateFormatUtils.format(new Date(),
                            ReportFormattingConstants.DATE_PATTERN_AS_OF_DATE);

        }

        titleListMap.put(com.manulife.pension.ireports.StandardReportsConstants.TITLE_1, title1);
        titleListMap.put(com.manulife.pension.ireports.StandardReportsConstants.TITLE_2, title2);
        titleListMap.put(com.manulife.pension.ireports.StandardReportsConstants.TITLE_3, title3);
        titleListMap.put(com.manulife.pension.ireports.StandardReportsConstants.TITLE_4, title4);
        titleListMap.put(com.manulife.pension.ireports.StandardReportsConstants.TITLE_5, title5);
        titleListMap.put(com.manulife.pension.ireports.StandardReportsConstants.TITLE_6, title6);

        return titleListMap;
    }

    /**
     * This method retrieves the report Name and additional name (if applicable).
     * 
     * @param reportSelected
     * @param subReportSelected
     * @param classSelected
     * @param currentView
     * @return - report Name and additional name
     */
    public static String getReportNameAndAdditionalName(String reportSelected,
            String subReportSelected, String classSelected, String currentView) {
        return FundReportConstants.reportIdToReportTitleMap.get(reportSelected);
    }

    /**
     * This method retrieves the Fund menu, ShortlistType, Generic Modified information. THis will
     * be used in determining the title.
     * 
     * @param fundListMatch
     * @param request
     * @return
     */
    public static Map<String, String> getFundMenuShortlistTypeGenericModified(
            boolean fundListMatch, HttpServletRequest request) {
        Map<String, String> titleInfoMap = new HashMap<String, String>();

        String fundMenu = FundReportConstants.ALL_FUNDS_MENU_ID;
        String shortlistType = FundReportConstants.EMPTY_STRING;
        String genericModified = FundReportConstants.EMPTY_STRING;

        if (isAdvancedFilterEnabled(request)) {

            FandpFilterCriteria filterCriteria = getLastExecutedFilterCriteria(request);

            fundMenu = FundReportConstants.ALL_FUNDS_MENU_ID;
            if (!fundListMatch) {
                genericModified = FundReportConstants.MODIFIED_LINEUP_TITLE;
            }

            if (FapConstants.ALL_FUNDS_FILTER_KEY.equals(filterCriteria.getAdvanceFilterOption())) {
                // Dont do anything. Default values hold good.
            } else if (FapConstants.RETAIL_FUNDS_FILTER_KEY.equals(filterCriteria
                    .getAdvanceFilterOption())) {
                if (fundListMatch) {
                    fundMenu = FundReportConstants.RETAIL_MENU_ID;
                }
            } else if (FapConstants.SUB_ADVISED_FUNDS_FILTER_KEY.equals(filterCriteria
                    .getAdvanceFilterOption())) {
                if (fundListMatch) {
                    fundMenu = FundReportConstants.SUB_ADVISED_MENU_ID;
                }
            } else if (FapConstants.SHORTLIST_FILTER_KEY.equals(filterCriteria
                    .getAdvanceFilterOption())) {
                Map<String, String> fundShortListOptions = filterCriteria.getFundShortListOptions();

                if (fundListMatch) {
                    if (FapConstants.FUND_MENU_RETAIL_FUNDS_FILTER_KEY.equals(fundShortListOptions
                            .get(FapConstants.FUND_MENU_PACKAGE_SERIES))) {
                        fundMenu = FundReportConstants.RETAIL_MENU_ID;
                    } else if (FapConstants.FUND_MENU_SUB_ADVISED_FUNDS_FILTER_KEY
                            .equals(fundShortListOptions.get(FapConstants.FUND_MENU_PACKAGE_SERIES))) {
                        fundMenu = FundReportConstants.SUB_ADVISED_MENU_ID;
                    }

                    shortlistType = FundReportConstants.shortlistKeyToTitleMap
                            .get(fundShortListOptions.get(FapConstants.SHORT_LIST_TYPE));
                }
            } else {
                fundMenu = FundReportConstants.ALL_FUNDS_MENU_ID;
                genericModified = FundReportConstants.MODIFIED_LINEUP_TITLE;
            }
        }

        titleInfoMap.put(FundReportConstants.FUND_MENU_ID, fundMenu);
        titleInfoMap.put(FundReportConstants.SHORTLIST_TYPE_ID, shortlistType);
        titleInfoMap.put(FundReportConstants.GENERIC_MODIFIED_ID, genericModified);

        return titleInfoMap;
    }

    /**
     * This method retrieves the Contract Modified information. Contract Modified is true if the
     * funds being shown on the Main report in F&P page differ from "Funds selected by Contract".
     * 
     * @param fundListMatch
     * @param request
     * @return
     */
    public static String getContractModified(boolean fundListMatch,
            HttpServletRequest request) {
        String contractModified = FundReportConstants.EMPTY_STRING;

        if (isAdvancedFilterEnabled(request) && !fundListMatch) {
            contractModified = FundReportConstants.HYPHON
                    + FundReportConstants.CONTRACT_MODIFIED_LINEUP_TITLE;
        }
        return contractModified;
    }
    

    /**
     * This is a helper method that returns the Date, given the context.
     * 
     * @param context
     * @return
     */
    public static Date getAsOfDateForContext(Map<String, CurrentAsOfDate> asOfDates, String context) {
        if (asOfDates == null) {
            return null;
        }

        CurrentAsOfDate currentAsOfDate = asOfDates.get(context);
        return currentAsOfDate == null ? null : currentAsOfDate.getAsofdate();
    }
}
