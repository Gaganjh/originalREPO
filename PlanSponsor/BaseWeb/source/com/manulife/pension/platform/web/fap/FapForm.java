package com.manulife.pension.platform.web.fap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.fap.constants.FapConstants;
import com.manulife.pension.platform.web.fap.customquery.util.CustomQueryUtility;
import com.manulife.pension.ps.service.report.fandp.valueobject.CustomQueryRow;
import com.manulife.pension.service.environment.valueobject.DeCodeVO;


/**
 * Form-bean for the Funds & Performance Page
 * 
 * @author ayyalsa
 */
public class FapForm extends AutoForm {

	/**
	 * Default Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	
	private String companyId;
	
	private String asOfDate;
	private String fundcheckAsOfDate;
	
	private boolean contractMode;
	
	/** 
	 * Base Filter Options 
	 **/
	private String baseFilterSelect = FapConstants.BASE_FILTER_ALL_FUNDS_KEY;
	private String contractSearchText;
	private String contractSelect;
	private String classSelect = "CL0";
	private String groupBySelect = FapConstants.ASSET_CLASS_FILTER_KEY;
	
	/**
	 * Attributes that supports the Base filter
	 */
	private List<LabelValueBean> baseFilterList;
	private List<LabelValueBean> contractResultsList;
	private int contractResultsSize; 
	private Collection<DeCodeVO> fundClassList;
	private List<LabelValueBean> groupByList;
	private String selectedCompanyName;
	
	/** 
	 * Advance Filter Options 
	 **/
	private String optionalFilterSelect;
	private String selectedAdvanceFilterOption;
	private String includeOptions;
	private boolean includeNML;
	private boolean includeClosedFunds;
	private boolean includeOnlySigPlusFunds;
	
	// Asset class
	private String assetClassQuerySelect;
	private String selectedAssetOrRiskValues;
	// Risk category
	private String riskCategoryQuerySelect;
	//Short list
	private String shortlistFundMenuSelect;
	private String shortlistTypeSelect;
	private String allocationGroupSelect;
	private String conservativeFundSelect;
	//Custom Query
	private List<CustomQueryRow> customQueryCriteriaList;
	private String customQueryRowIndex;
	private String saveQueryName;
	
	// Save Fund List
	private String saveListName;
	
	//Fund name search
	private String fundNameSearchText;

	//My Saved Lists
	private String mySavedListsQuerySelect;
	
	//My Saved Custom Query 
	private String mySavedQueriesQuerySelect;
	
	// Query Results box & selected funds box sorting option
	private String sortPreferenceSelect;
	private String queryResultsValues;
	private String selectedFundsValues;
	
	private List<String> sortedQueryResultsValues;
	private List<String> sortedSelectedFundsValues;
		
	private String tabSelected;
	private String cofidDistChannel;

	private boolean jhiIndicatorFlg;
	private boolean svpIndicatorFlg;
	
    /**
     * Attributes that supports the Advance filter
     */
	// Holds the options that needs to be populated to the options filter 
	// select drop-down
	private List<LabelValueBean> optionalFilterList;
	
	// Holds the options that needs to be populated to asset class list box
	private List<LabelValueBean> assetClassList;
	
	// Holds the options that needs to be populated to risk category list box
	private List<LabelValueBean> riskCategoryList;
	
	// Holds the options that needs to be populated to Custom Query Row's 
	// Value drop-down
	private List<LabelValueBean> customQueryRowValueList;
	
	// Holds the options that needs to be populated to the saved 
	// fund list drop-down
	private List<LabelValueBean> savedFundsList;
	
	// Holds the options that needs to be populated to the saved custom query 
	// drop-down
	private List<LabelValueBean> savedCustomQueryList;

	// Based on the user validation this attribute is set
	private boolean showNML;
	
	// True, if the user has enabled the advance filter.
	private boolean advanceFilterEnabled;
	
	//Included to implement merrill lynch covered funds
  	private boolean showML;
      	
	// This List will be used for the AssetClass, Risk category, Saved list, 
	// Saved queries
	private List<LabelValueBean> advanceFilterInnerOptionList;
	
	// used for the Short list option
	private HashMap<String, List<LabelValueBean>> shortListOptionList;
	
	// used for the Custom Query Filter option
	private HashMap<String, List<LabelValueBean>> customQueryOptionList;
	private boolean overwriteExisting;
	
	/**
	 * Will hold the message that should be displayed in the alert pop-up
	 */
	private String alertMessage;
	
	
	// used for the MY Custom Queries Filter option
	private String eventTriggered;
	
	// used for the custom  query Fund Check Evaluation drop-down
	private List<LabelValueBean> morningStarRatingEvaluationList;
	private List<LabelValueBean> fi360EvaluationList;
	private List<LabelValueBean> rpagEvaluationList;
	
	// used for the custom query overall star rating drop-down
	private List<LabelValueBean> overallstarRatingList;
	
	private String selectedfundIds;
	private List<String> filteredFundIds;
	private String filterResultsId;
	private String filterKey;
	private boolean displayOnlyHeaders;
	
	// Object that has the data for the tabs
	@SuppressWarnings("unchecked")
	private HashMap<String, List> fapSummaryInfo;
	// Object that has the columns and their specific formats to a particular tab
	@SuppressWarnings("unchecked")
	private HashMap<String, List> columnsInfo;
	
	/**
	 * This used to indicate if there are any error, warnings to information messages 
	 * to be displayed
	 */
	private boolean messagesExist;
	private boolean warningsExist;
	
	private Map<String, String> reportList;

    private String selectedReport;
    
    // says whether the user is in mimic
    private boolean inMimic;
    
   /**
    *  Used for i:reports
    */
    private String selectedFundNames;
    private String selectedContractName;
    private String advanceFilterForIReports;
    /*
    private String fundMenu;
    private String lifecyclePortfolio;
    private String[] fundsChosen;

    private String report;
    private String lifestylePortfolio;
    */

    // Extra parameters that are used by i:reports
    private boolean isfSelected;

    private boolean isClientNameEnabled;
    
    private String clientName;

    private String advisorName;
    
    private boolean isAdvisorNameDisplayed;
  
    public static final String NOT_APPLICABLE = "N/A";
    
    // This parameter is used For supress the text in i:report
    private boolean isSelectedFromWebPage;
    //This parameter holds monthEndDate to generate Historical IReport
    private Date monthEndDate;
    
    private boolean isAllFundListModified;
    private boolean isContractFundListModified;
    
    private boolean showMorningstarScorecardMetrics;
    private boolean showFi360ScorecardMetrics;
    private boolean showRpagScorecardMetrics;
    private boolean fcpContent;

    
	/**
	 * @return the monthEndDate
	 */
	public Date getMonthEndDate() {
		return monthEndDate;
	}
	/**
	 * @param monthEndDate the monthEndDate to set
	 */
	public void setMonthEndDate(Date monthEndDate) {
		this.monthEndDate = monthEndDate;
	}
	/**
	 * @return the isSelectedFormWebPage
	 */
	public boolean isSelectedFromWebPage() {
		return isSelectedFromWebPage;
	}
	/**
	 * @param isSelectedFormWebPage the isSelectedFormWebPage to set
	 */
	public void setSelectedFormWebPage(boolean isSelectedFromWebPage) {
		this.isSelectedFromWebPage = isSelectedFromWebPage;
	}
	
	/**
	 * @return the companyId
	 */
	public String getCompanyId() {
		return companyId;
	}
	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	/**
	 * @return the asOfDate
	 */
	public String getAsOfDate() {
		return asOfDate;
	}
	/**
	 * @param asOfDate the asOfDate to set
	 */
	public void setAsOfDate(String asOfDate) {
		this.asOfDate = asOfDate;
	}
	
	/**
	 * @return the fundcheckAsOfDate
	 */
	public String getFundcheckAsOfDate() {
		return fundcheckAsOfDate;
	}
	/**
	 * @param fundcheckAsOfDate the fundcheckAsOfDate to set
	 */
	public void setFundcheckAsOfDate(String fundcheckAsOfDate) {
		this.fundcheckAsOfDate = fundcheckAsOfDate;
	}
	/**
	 * @return the contractMode
	 */
	public boolean isContractMode() {
		return contractMode;
	}
	/**
	 * @param contractMode the contractMode to set
	 */
	public void setContractMode(boolean contractMode) {
		this.contractMode = contractMode;
	}
	/**
	 * @return the baseFilterSelect 
	 */
	public String getBaseFilterSelect() {
		return baseFilterSelect;
	}
	/**
	 * @param baseFilterSelect the baseFilterSelect to set
	 */
	public void setBaseFilterSelect(String baseFilterSelect) {
		this.baseFilterSelect = baseFilterSelect;
	}
	
	/**
	 * @return the contractSearchText
	 */
	public String getContractSearchText() {
		return contractSearchText;
	}
	/**
	 * @param contractSearchText the contractSearchText to set
	 */
	public void setContractSearchText(String contractSearchText) {
		this.contractSearchText = contractSearchText;
	}
	/**
	 * @return the contractSelect
	 */
	public String getContractSelect() {
		return contractSelect;
	}
	/**
	 * @param contractSelect the contractSelect to set
	 */
	public void setContractSelect(String contractSelect) {
		this.contractSelect = contractSelect;
	}
	/**
	 * @return the classEntry
	 */
	public String getClassSelect() {
		return classSelect;
	}
	/**
	 * @param classEntry the classEntry to set
	 */
	public void setClassSelect(String classSelect) {
		this.classSelect = classSelect;
	}
	/**
	 * @return the groupBySelect
	 */
	public String getGroupBySelect() {
		return groupBySelect;
	}
	/**
	 * @param groupBySelect the groupBySelect to set
	 */
	public void setGroupBySelect(String groupBySelect) {
		this.groupBySelect = groupBySelect;
	}
	
	/**
	 * @return the baseFilterList
	 */
	public List<LabelValueBean> getBaseFilterList() {
		return baseFilterList;
	}
	/**
	 * @param baseFilterList the baseFilterList to set
	 */
	public void setBaseFilterList(List<LabelValueBean> baseFilterList) {
		this.baseFilterList = baseFilterList;
	}
	
	/**
	 * @return the contractResultsList
	 */
	public List<LabelValueBean> getContractResultsList() {
		return contractResultsList;
	}
	/**
	 * @param contractResultsList the contractResultsList to set
	 */
	public void setContractResultsList(List<LabelValueBean> contractResultsList) {
		this.contractResultsList = contractResultsList;
	}
	/**
	 * @return the contractResultsSize
	 */
	public int getContractResultsSize() {
		return contractResultsSize;
	}
	/**
	 * @param contractResultsSize the contractResultsSize to set
	 */
	public void setContractResultsSize(int contractResultsSize) {
		this.contractResultsSize = contractResultsSize;
	}
	/**
	 * @return the fundClassList
	 */
	public Collection<DeCodeVO> getFundClassList() {
		return fundClassList;
	}
	/**
	 * @param fundClassList the fundClassList to set
	 */
	public void setFundClassList(Collection<DeCodeVO> fundClassList) {
		this.fundClassList = fundClassList;
	}
	/**
	 * @return the groupByList
	 */
	public List<LabelValueBean> getGroupByList() {
		return groupByList;
	}
	/**
	 * @param groupByList the groupByList to set
	 */
	public void setGroupByList(List<LabelValueBean> groupByList) {
		this.groupByList = groupByList;
	}
	/**
	 * @return the optionalFilterSelect
	 */
	public String getOptionalFilterSelect() {
		return optionalFilterSelect;
	}
	/**
	 * @param optionalFilterSelect the optionalFilterSelect to set
	 */
	public void setOptionalFilterSelect(String optionalFilterSelect) {
		this.optionalFilterSelect = optionalFilterSelect;
	}
	
	/**
	 * @return the selectedAdvanceFilterOption
	 */
	public String getSelectedAdvanceFilterOption() {
		return selectedAdvanceFilterOption;
	}
	/**
	 * @param selectedAdvanceFilterOption the selectedAdvanceFilterOption to set
	 */
	public void setSelectedAdvanceFilterOption(String selectedAdvanceFilterOption) {
		this.selectedAdvanceFilterOption = selectedAdvanceFilterOption;
	}
	/**
	 * @return the includeOptions
	 */
	public String getIncludeOptions() {
		return includeOptions;
	}
	/**
	 * @param includeOptions the includeOptions to set
	 */
	public void setIncludeOptions(String includeOptions) {
		this.includeOptions = includeOptions;
	}
	
	/**
	 * @return the includeNML
	 */
	public boolean isIncludeNML() {
		return includeNML;
	}
	/**
	 * @param includeNML the includeNML to set
	 */
	public void setIncludeNML(boolean includeNML) {
		this.includeNML = includeNML;
	}
	/**
	 * @return the includeClosedFunds
	 */
	public boolean isIncludeClosedFunds() {
		return includeClosedFunds;
	}
	/**
	 * @param includeClosedFunds the includeClosedFunds to set
	 */
	public void setIncludeClosedFunds(boolean includeClosedFunds) {
		this.includeClosedFunds = includeClosedFunds;
	}
	/**
	 * @return the includeOnlySigPlusFunds
	 */
	public boolean isIncludeOnlySigPlusFunds() {
		return includeOnlySigPlusFunds;
	}
	/**
	 * @param includeOnlySigPlusFunds the includeOnlySigPlusFunds to set
	 */
	public void setIncludeOnlySigPlusFunds(boolean includeOnlySigPlusFunds) {
		this.includeOnlySigPlusFunds = includeOnlySigPlusFunds;
	}
	/**
	 * @return the assetClassQuerySelect
	 */
	public String getAssetClassQuerySelect() {
		return assetClassQuerySelect;
	}
	/**
	 * @param assetClassQuerySelect the assetClassQuerySelect to set
	 */
	public void setAssetClassQuerySelect(String assetClassQuerySelect) {
		this.assetClassQuerySelect = assetClassQuerySelect;
	}
	/**
	 * @return the riskCategoryQuerySelect
	 */
	public String getRiskCategoryQuerySelect() {
		return riskCategoryQuerySelect;
	}
	/**
	 * @param riskCategoryQuerySelect the riskCategoryQuerySelect to set
	 */
	public void setRiskCategoryQuerySelect(String riskCategoryQuerySelect) {
		this.riskCategoryQuerySelect = riskCategoryQuerySelect;
	}
	/**
	 * @return the shortlistFundMenuSelect
	 */
	public String getShortlistFundMenuSelect() {
		return shortlistFundMenuSelect;
	}
	/**
	 * @param shortlistFundMenuSelect the shortlistFundMenuSelect to set
	 */
	public void setShortlistFundMenuSelect(String shortlistFundMenuSelect) {
		this.shortlistFundMenuSelect = shortlistFundMenuSelect;
	}
	/**
	 * @return the shortlistTypeSelect
	 */
	public String getShortlistTypeSelect() {
		return shortlistTypeSelect;
	}
	/**
	 * @param shortlistTypeSelect the shortlistTypeSelect to set
	 */
	public void setShortlistTypeSelect(String shortlistTypeSelect) {
		this.shortlistTypeSelect = shortlistTypeSelect;
	}
	/**
	 * @return the allocationGroupSelect
	 */
	public String getAllocationGroupSelect() {
		return allocationGroupSelect;
	}
	/**
	 * @param allocationGroupSelect the allocationGroupSelect to set
	 */
	public void setAllocationGroupSelect(String allocationGroupSelect) {
		this.allocationGroupSelect = allocationGroupSelect;
	}
	/**
	 * @return the conservativeFundSelect
	 */
	public String getConservativeFundSelect() {
		return conservativeFundSelect;
	}
	/**
	 * @param conservativeFundSelect the conservativeFundSelect to set
	 */
	public void setConservativeFundSelect(String conservativeFundSelect) {
		this.conservativeFundSelect = conservativeFundSelect;
	}
	/**
	 * @return the customQueryCriteriaList
	 */
	public List<CustomQueryRow> getCustomQueryCriteriaList() {
		return customQueryCriteriaList;
	}
	/**
	 * @param customQueryCriteriaList the customQueryCriteriaList to set
	 */
	public void setCustomQueryCriteriaList(
			List<CustomQueryRow> customQueryCriteriaList) {
		this.customQueryCriteriaList = customQueryCriteriaList;
	}
	/**
	 * @return the customQueryRowIndex
	 */
	public String getCustomQueryRowIndex() {
		return customQueryRowIndex;
	}
	/**
	 * @param customQueryRowIndex the customQueryRowIndex to set
	 */
	public void setCustomQueryRowIndex(String customQueryRowIndex) {
		this.customQueryRowIndex = customQueryRowIndex;
	}
	/**
	 * @return the fundNameSearchText
	 */
	public String getFundNameSearchText() {
		return fundNameSearchText;
	}
	/**
	 * @param fundNameSearchText the fundNameSearchText to set
	 */
	public void setFundNameSearchText(String fundNameSearchText) {
		this.fundNameSearchText = fundNameSearchText;
	}
	/**
	 * @return the mySavedListsQuerySelect
	 */
	public String getMySavedListsQuerySelect() {
		return mySavedListsQuerySelect;
	}
	/**
	 * @param mySavedListsQuerySelect the mySavedListsQuerySelect to set
	 */
	public void setMySavedListsQuerySelect(String mySavedListsQuerySelect) {
		this.mySavedListsQuerySelect = mySavedListsQuerySelect;
	}
	/**
	 * @return the mySavedQueriesQuerySelect
	 */
	public String getMySavedQueriesQuerySelect() {
		return mySavedQueriesQuerySelect;
	}
	/**
	 * @param mySavedQueriesQuerySelect the mySavedQueriesQuerySelect to set
	 */
	public void setMySavedQueriesQuerySelect(String mySavedQueriesQuerySelect) {
		this.mySavedQueriesQuerySelect = mySavedQueriesQuerySelect;
	}
	/**
	 * @return the optionalFilterList
	 */
	public List<LabelValueBean> getOptionalFilterList() {
		return optionalFilterList;
	}
	/**
	 * @param optionalFilterList the optionalFilterList to set
	 */
	public void setOptionalFilterList(List<LabelValueBean> optionalFilterList) {
		this.optionalFilterList = optionalFilterList;
	}
	/**
	 * @return the advanceFilterInnerOptionList
	 */
	public List<LabelValueBean> getAdvanceFilterInnerOptionList() {
		return advanceFilterInnerOptionList;
	}
	/**
	 * @param advanceFilterInnerOptionList the advanceFilterInnerOptionList to set
	 */
	public void setAdvanceFilterInnerOptionList(
			List<LabelValueBean> advanceFilterInnerOptionList) {
		this.advanceFilterInnerOptionList = advanceFilterInnerOptionList;
	}
	/**
	 * @return the shortListOptionList
	 */
	public HashMap<String, List<LabelValueBean>> getShortListOptionList() {
		return shortListOptionList;
	}
	/**
	 * @param shortListOptionList the shortListOptionList to set
	 */
	public void setShortListOptionList(
			HashMap<String, List<LabelValueBean>> shortListOptionList) {
		this.shortListOptionList = shortListOptionList;
	}
	/**
	 * @return the customQueryOptionList
	 */
	public HashMap<String, List<LabelValueBean>> getCustomQueryOptionList() {
		return customQueryOptionList;
	}
	/**
	 * @param customQueryOptionList the customQueryOptionList to set
	 */
	public void setCustomQueryOptionList(
			HashMap<String, List<LabelValueBean>> customQueryOptionList) {
		this.customQueryOptionList = customQueryOptionList;
	}
	/**
	 * @return the assetClassList
	 */
	public List<LabelValueBean> getAssetClassList() {
		return assetClassList;
	}
	/**
	 * @param assetClassList the assetClassList to set
	 */
	public void setAssetClassList(List<LabelValueBean> assetClassList) {
		this.assetClassList = assetClassList;
	}
	/**
	 * @return the riskCategoryList
	 */
	public List<LabelValueBean> getRiskCategoryList() {
		return riskCategoryList;
	}
	/**
	 * @param riskCategoryList the riskCategoryList to set
	 */
	public void setRiskCategoryList(List<LabelValueBean> riskCategoryList) {
		this.riskCategoryList = riskCategoryList;
	}
	/**
	 * @return the customQueryRowValueList
	 */
	public List<LabelValueBean> getCustomQueryRowValueList() {
		return customQueryRowValueList;
	}
	/**
	 * @param customQueryRowValueList the customQueryRowValueList to set
	 */
	public void setCustomQueryRowValueList(
			List<LabelValueBean> customQueryRowValueList) {
		this.customQueryRowValueList = customQueryRowValueList;
	}
	/**
	 * @return the savedFundsList
	 */
	public List<LabelValueBean> getSavedFundsList() {
		return savedFundsList;
	}
	/**
	 * @param savedFundsList the savedFundsList to set
	 */
	public void setSavedFundsList(List<LabelValueBean> savedFundsList) {
		this.savedFundsList = savedFundsList;
	}
	/**
	 * @return the savedCustomQueryList
	 */
	public List<LabelValueBean> getSavedCustomQueryList() {
		return savedCustomQueryList;
	}
	/**
	 * @param savedCustomQueryList the savedCustomQueryList to set
	 */
	public void setSavedCustomQueryList(List<LabelValueBean> savedCustomQueryList) {
		this.savedCustomQueryList = savedCustomQueryList;
	}

	/**
	 * @return the advanceFilterEnabled
	 */
	public boolean isAdvanceFilterEnabled() {
		return advanceFilterEnabled;
	}
	/**
	 * @param advanceFilterEnabled the advanceFilterEnabled to set
	 */
	public void setAdvanceFilterEnabled(boolean advanceFilterEnabled) {
		this.advanceFilterEnabled = advanceFilterEnabled;
	}
	/**
	 * @return the selectedfundIds
	 */
	public String getSelectedfundIds() {
		return selectedfundIds;
	}
	/**
	 * @param selectedfundIds the selectedfundIds to set
	 */
	public void setSelectedfundIds(String selectedfundIds) {
		this.selectedfundIds = selectedfundIds;
	}
	/**
	 * @return the filteredFundIds
	 */
	public List<String> getFilteredFundIds() {
		return filteredFundIds;
	}
	/**
	 * @param allFundsIdArray the filteredFundIds to set
	 */
	public void setFilteredFundIds(List<String> filteredFundIds) {
		this.filteredFundIds = filteredFundIds;
	}
	/**
	 * @return the filterResultsId
	 */
	public String getFilterResultsId() {
		return filterResultsId;
	}
	/**
	 * @param filterResultsId the filterResultsId to set
	 */
	public void setFilterResultsId(String filterResultsId) {
		this.filterResultsId = filterResultsId;
	}
	/**
	 * @return the filterKey
	 */
	public String getFilterKey() {
		return filterKey;
	}
	/**
	 * @param filterKey the filterKey to set
	 */
	public void setFilterKey(String filterKey) {
		this.filterKey = filterKey;
	}

	/**
	 * @return the displayOnlyHeaders
	 */
	public boolean isDisplayOnlyHeaders() {
		return displayOnlyHeaders;
	}
	/**
	 * @param displayOnlyHeaders the displayOnlyHeaders to set
	 */
	public void setDisplayOnlyHeaders(boolean displayOnlyHeaders) {
		this.displayOnlyHeaders = displayOnlyHeaders;
	}
	/**
	 * @return the fapSummaryInfo
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, List> getFapSummaryInfo() {
		return fapSummaryInfo;
	}
	/**
	 * @param fapSummaryInfo the fapSummaryInfo to set
	 */
	@SuppressWarnings("unchecked")
	public void setFapSummaryInfo(HashMap<String, List> fapSummaryInfo) {
		this.fapSummaryInfo = fapSummaryInfo;
	}
	/**
	 * @return the columnsInfo
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, List> getColumnsInfo() {
		return columnsInfo;
	}
	/**
	 * @param columnsInfo the columnsInfo to set
	 */
	@SuppressWarnings("unchecked")
	public void setColumnsInfo(HashMap<String, List> columnsInfo) {
		this.columnsInfo = columnsInfo;
	}
	
	
	/**
	 * @return the messagesExist
	 */
	public boolean isMessagesExist() {
		return messagesExist;
	}
	/**
	 * @param messagesExist the messagesExist to set
	 */
	public void setMessagesExist(boolean messagesExist) {
		this.messagesExist = messagesExist;
	}
	
	/**
	 * @return the warningsExist
	 */
	public boolean isWarningsExist() {
		return warningsExist;
	}
	/**
	 * @param warningsExist the warningsExist to set
	 */
	public void setWarningsExist(boolean warningsExist) {
		this.warningsExist = warningsExist;
	}
	/**
	 * Returns TRUE, 
	 * 		if all(Fund Menu, Short-list Type, Allocation Group, Conservative Fund) 4 options for the 
	 * short-list filter is selected
	 * 		else 
	 * 			Returns FALSE
	 *   
	 * @return boolean 
	 */
	public boolean isAllShortlistOptionSelected() {
		
		if (this.getShortlistFundMenuSelect() == null || NOT_APPLICABLE.equals(this.getShortlistFundMenuSelect()) 
			|| this.getShortlistTypeSelect() == null || NOT_APPLICABLE.equals(this.getShortlistTypeSelect()) 
			|| this.getAllocationGroupSelect() == null || NOT_APPLICABLE.equals(this.getAllocationGroupSelect())
			|| this.getConservativeFundSelect() == null || NOT_APPLICABLE.equals(this.getConservativeFundSelect())) {
			
			return false;
		} 
		
		return true;
		
	}
	
	public List<String> getShortlistOptionAsList() {
		
		List<String> shortlistOption = new ArrayList<String>(4);
		shortlistOption.add(this.getShortlistTypeSelect());
		shortlistOption.add(this.getShortlistFundMenuSelect());
		shortlistOption.add(this.getAllocationGroupSelect());
		shortlistOption.add(this.getConservativeFundSelect());
		
		return shortlistOption ;
	}
	
	/**
	 * This is used for the struts to populate the custom query values to the form  
	 * 
	 * @param index	- row index
	 * @return CustomQueryRow - current row's object
	 */
	public CustomQueryRow getCustomQueryCriteria(int index) {
		
		if (this.customQueryCriteriaList == null) {
			this.setCustomQueryCriteriaList(CustomQueryUtility.createMinimumNumberOfQueryCriteriaRows());
		}
		
		int listSize = this.customQueryCriteriaList.size();
		
		if (index >= listSize) {
			
			for (int i=0; i<= (index - listSize); i++) {
				this.customQueryCriteriaList.add(new CustomQueryRow());
			}
		}
		
        return (CustomQueryRow) customQueryCriteriaList.get(index);
    }

    /**
     * Gets the selected report name
     * 
     * @return String
     */
    public String getSelectedReport() {
        return selectedReport;
    }

    /**
     * Sets the selected report name
     * 
     * @param selectedReport
     */
    public void setSelectedReport(String selectedReport) {
        this.selectedReport = selectedReport;
    }
    
    public Map<String, String> getReportList() {
        return reportList;
    }

    public void setReportList(Map<String, String> reportList) {
        this.reportList = reportList;
    }
    
    public String getTabSelected() {
        return tabSelected;
    }

    public void setTabSelected(String tabSelected) {
        this.tabSelected = tabSelected;
    }
	/**
	 * @return the sortPreferenceSelect
	 */
	public String getSortPreferenceSelect() {
		return sortPreferenceSelect;
	}
	/**
	 * @param sortPreferenceSelect the sortPreferenceSelect to set
	 */
	public void setSortPreferenceSelect(String sortPreferenceSelect) {
		this.sortPreferenceSelect = sortPreferenceSelect;
	}
	/**
	 * @return the queryResultsValues
	 */
	public String getQueryResultsValues() {
		return queryResultsValues;
	}
	/**
	 * @param queryResultsValues the queryResultsValues to set
	 */
	public void setQueryResultsValues(String queryResultsValues) {
		this.queryResultsValues = queryResultsValues;
	}
	/**
	 * @return the selectedFundsValues
	 */
	public String getSelectedFundsValues() {
		return selectedFundsValues;
	}
	/**
	 * @param selectedFundsValues the selectedFundsValues to set
	 */
	public void setSelectedFundsValues(String selectedFundsValues) {
		this.selectedFundsValues = selectedFundsValues;
	}
	/**
	 * @return the sortedQueryResultsValues
	 */
	public List<String> getSortedQueryResultsValues() {
		return sortedQueryResultsValues;
	}
	/**
	 * @param sortedQueryResultsValues the sortedQueryResultsValues to set
	 */
	public void setSortedQueryResultsValues(List<String> sortedQueryResultsValues) {
		this.sortedQueryResultsValues = sortedQueryResultsValues;
	}
	/**
	 * @return the sortedSelectedFundsValues
	 */
	public List<String> getSortedSelectedFundsValues() {
		return sortedSelectedFundsValues;
	}
	/**
	 * @param sortedSelectedFundsValues the sortedSelectedFundsValues to set
	 */
	public void setSortedSelectedFundsValues(List<String> sortedSelectedFundsValues) {
		this.sortedSelectedFundsValues = sortedSelectedFundsValues;
	}
	/**
	 * @return the selectedAssetOrRiskValues
	 */
	public String getSelectedAssetOrRiskValues() {
		return selectedAssetOrRiskValues;
	}
	/**
	 * @param selectedAssetOrRiskValues the selectedAssetOrRiskValues to set
	 */
	public void setSelectedAssetOrRiskValues(String selectedAssetOrRiskValues) {
		this.selectedAssetOrRiskValues = selectedAssetOrRiskValues;
	}
	
	public List<LabelValueBean> getMorningStarRatingEvaluationList() {
		return morningStarRatingEvaluationList;
	}
	public void setMorningStarRatingEvaluationList(
			List<LabelValueBean> morningStarRatingEvaluationList) {
		this.morningStarRatingEvaluationList = morningStarRatingEvaluationList;
	}
	
	public List<LabelValueBean> getFi360EvaluationList() {
		return fi360EvaluationList;
	}
	public void setFi360EvaluationList(List<LabelValueBean> fi360EvaluationList) {
		this.fi360EvaluationList = fi360EvaluationList;
	}
	
	public List<LabelValueBean> getRpagEvaluationList() {
		return rpagEvaluationList;
	}
	public void setRpagEvaluationList(List<LabelValueBean> rpagEvaluationList) {
		this.rpagEvaluationList = rpagEvaluationList;
	}
	
	/**
	 * @return the overallstarRatingList
	 */
	public List<LabelValueBean> getOverallstarRatingList() {
		return overallstarRatingList;
	}
	/**
	 * @param overallstarRatingList the overallstarRatingList to set
	 */
	public void setOverallstarRatingList(List<LabelValueBean> overallstarRatingList) {
		this.overallstarRatingList = overallstarRatingList;
	}
	/**
	 * @return the selectedCompanyName
	 */
	public String getSelectedCompanyName() {
		return selectedCompanyName;
	}
	/**
	 * @param selectedCompanyName the selectedCompanyName to set
	 */
	public void setSelectedCompanyName(String selectedCompanyName) {
		this.selectedCompanyName = selectedCompanyName;
	}
	/**
	 * @return the showNML
	 */
	public boolean isShowNML() {
		return showNML;
	}
	/**
	 * @param showNML the showNML to set
	 */
	public void setShowNML(boolean showNML) {
		this.showNML = showNML;
	}
	/**
	 * @return the showML
	 */
	public boolean isShowML() {
		return showML;
	}
	/**
	 * @param showML the showML to set
	 */
	public void setShowML(boolean showML) {
		this.showML = showML;
	}
	/**
	 * @return the saveQueryName
	 */
	public String getSaveQueryName() {
		return saveQueryName;
	}
	/**
	 * @param saveQueryName the saveQueryName to set
	 */
	public void setSaveQueryName(String saveQueryName) {
		this.saveQueryName = saveQueryName;
	}
	/**
	 * @return the saveListName
	 */
	public String getSaveListName() {
		return saveListName;
	}
	/**
	 * @param saveListName the saveListName to set
	 */
	public void setSaveListName(String saveListName) {
		this.saveListName = saveListName;
	}
	/**
	 * @return the overwriteExisting
	 */
	public boolean isOverwriteExisting() {
		return overwriteExisting;
	}
	/**
	 * @param overwriteExisting the overwriteExisting to set
	 */
	public void setOverwriteExisting(boolean overwriteExisting) {
		this.overwriteExisting = overwriteExisting;
	}
	
	/**
	 * @return the alertMessage
	 */
	public String getAlertMessage() {
		return alertMessage;
	}
	/**
	 * @param alertMessage the alertMessage to set
	 */
	public void setAlertMessage(String alertMessage) {
		this.alertMessage = alertMessage;
	}
	/**
	 * @return the eventTriggered
	 */
	public String getEventTriggered() {
		return eventTriggered;
	}
	/**
	 * @param eventTriggered the eventTriggered to set
	 */
	public void setEventTriggered(String eventTriggered) {
		this.eventTriggered = eventTriggered;
	}
	
	/**
	 * Gets the site location
	 * 
	 * @return String
	 */
	public String getSiteLocation() {
	  return GlobalConstants.MANULIFE_CONTRACT_ID_FOR_USA.equals(companyId) ? CommonConstants.SITEMODE_USA
                : CommonConstants.SITEMODE_NY;
	}
	/**
	 * @return the inMimic
	 */
	public boolean isInMimic() {
		return inMimic;
	}
	/**
	 * @param inMimic the inMimic to set
	 */
	public void setInMimic(boolean inMimic) {
		this.inMimic = inMimic;
	}

    /**
     * Get selected Names
     * 
     * @return String
     */
    public String getSelectedFundNames() {
        return selectedFundNames;
    }

    /**
     * sets selected Fund Names
     * 
     * @param getSelectedFundNames
     */
    public void setSelectedFundNames(String selectedFundNames) {
        this.selectedFundNames = selectedFundNames;
    }
    
    
    /**
     * Get selected Contract Name
     * 
     * @return String
     */
    public String getSelectedContractName() {
        return selectedContractName;
    }

    /**
     * sets selected Contract Name
     * 
     * @param selectedContractName
     */
    public void setSelectedContractName(String selectedContractName) {
        this.selectedContractName = selectedContractName;
    }

    /**
     * @return the advanceFilterForIReports
     */
    public String getAdvanceFilterForIReports() {
        return advanceFilterForIReports;
    }

    /**
     * @param advanceFilterForIReports the advanceFilterForIReports to set
     */
    public void setAdvanceFilterForIReports(String advanceFilterForIReports) {
        this.advanceFilterForIReports = advanceFilterForIReports;
    }

    public boolean getIsfSelected() {
        return isfSelected;
    }

    public void setIsfSelected(boolean isfSelected) {
        this.isfSelected = isfSelected;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }


    /**
     * This method checks if the Investment Selection Form should be shown to the user or not.
     * 
     * @return - true if the Investment Selection Form should be shown to the user.
     */
    public boolean getIsIsfEnabled() {
        return isStandardReport();
    }

    /**
     * This method returns true if the report selected is one of Standard Reports.
     * 
     * @return - true if the report selected is one of standard reports.
     */
    public boolean isStandardReport() {
        return FapConstants.EXPENSE_RATIO_REPORT.equals(selectedReport)
                || FapConstants.FUND_CHARACTERISITICS_REPORT.equals(selectedReport)
                || FapConstants.INVESTEMNT_RETURNS_AND_EXPENSE_RATIO_REPORT.equals(selectedReport)
                || FapConstants.INVESTEMNT_RETURNS_AND_STANDARD_DEVIATION_REPORT
                        .equals(selectedReport)
                || FapConstants.MORNINGSTAR_RATINGS_AND_TICKR_SYMBOLS_REPORT.equals(selectedReport);
    }
    
    /**
     * This method returns true if the report selected is one of Standard Reports.
     * 
     * @return - true if the report selected is one of standard reports.
     */
    public boolean isOtherReport() {
        return FapConstants.MARKET_INDEX_REPORT.equals(selectedReport);
    }

    /**
     * This method returns true if the Client Name should be shown to the user or not.
     * 
     * @return - true if the Client Name should be shown to the user.
     */
    public boolean getIsClientNameEnabled() {
        return isClientNameEnabled;
    }

    public void setClientNameEnabled(boolean isClientNameEnabled) {
        this.isClientNameEnabled = isClientNameEnabled;
    }
    
    /**
     * This method returns true if the Additional Parameters section should be shown or not.
     * 
     * @return - true if the additional parameters section should be shown, else returns false.
     */
    public boolean getIsAdditionalparamsSectionEnabled() {
        return getIsIsfEnabled() || getIsClientNameEnabled(); 
    }

    /**
     * This method resets the additional parameter values to their default values.
     */
    public void resetAdditionalParams() {
        isfSelected = false;
        clientName = null;
        advisorName = null;
    }
    
    public String getAdvisorName() {
		return advisorName;
	}
	public void setAdvisorName(String advisorName) {
		this.advisorName = advisorName;
	}
	
	public boolean isAdvisorNameDisplayed() {
		return isAdvisorNameDisplayed;
	}
	public void setAdvisorNameDisplayed(boolean isAdvisorNameDisplayed) {
		this.isAdvisorNameDisplayed = isAdvisorNameDisplayed;
	}
	
	public boolean isAllFundListModified() {
		return isAllFundListModified;
	}
	public void setAllFundListModified(boolean isAllFundListModified) {
		this.isAllFundListModified = isAllFundListModified;
	}
	
	public boolean isContractFundListModified() {
		return isContractFundListModified;
	}
	public void setContractFundListModified(boolean isContractFundListModified) {
		this.isContractFundListModified = isContractFundListModified;
	}
	
	public boolean isShowMorningstarScorecardMetrics() {
		return showMorningstarScorecardMetrics;
	}
	public void setShowMorningstarScorecardMetrics(
			boolean showMorningstarScorecardMetrics) {
		this.showMorningstarScorecardMetrics = showMorningstarScorecardMetrics;
	}
	
	public boolean isShowFi360ScorecardMetrics() {
		return showFi360ScorecardMetrics;
	}
	public void setShowFi360ScorecardMetrics(boolean showFi360ScorecardMetrics) {
		this.showFi360ScorecardMetrics = showFi360ScorecardMetrics;
	}
	
	public boolean isShowRpagScorecardMetrics() {
		return showRpagScorecardMetrics;
	}
	public void setShowRpagScorecardMetrics(boolean showRpagScorecardMetrics) {
		this.showRpagScorecardMetrics = showRpagScorecardMetrics;
	}
	/**
	 * @return the cofidDistChannel
	 */
	public String getCofidDistChannel() {
		return cofidDistChannel;
	}
	/**
	 * @param cofidDistChannel the cofidDistChannel to set
	 */
	public void setCofidDistChannel(String cofidDistChannel) {
		this.cofidDistChannel = cofidDistChannel;
	}
	/**
	 * @return the fcpContent
	 */
	public boolean isFcpContent() {
		return fcpContent;
	}
	/**
	 * @param fcpContent the fcpContent to set
	 */
	public void setFcpContent(boolean fcpContent) {
		this.fcpContent = fcpContent;
	}
	public boolean isJhiIndicatorFlg() {
		return jhiIndicatorFlg;
	}
	public void setJhiIndicatorFlg(boolean jhiIndicatorFlg) {
		this.jhiIndicatorFlg = jhiIndicatorFlg;
	}
	public boolean isSvpIndicatorFlg() {
		return svpIndicatorFlg;
	}
	public void setSvpIndicatorFlg(boolean svpIndicatorFlg) {
		this.svpIndicatorFlg = svpIndicatorFlg;
	}
}
