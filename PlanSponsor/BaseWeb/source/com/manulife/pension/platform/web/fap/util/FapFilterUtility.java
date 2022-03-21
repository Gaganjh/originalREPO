package com.manulife.pension.platform.web.fap.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.delegate.FandpServiceDelegate;
import com.manulife.pension.platform.web.fap.FapForm;
import com.manulife.pension.platform.web.fap.constants.FapConstants;
import com.manulife.pension.ps.service.report.fandp.valueobject.CustomQuerySavedData;
import com.manulife.pension.ps.service.report.fandp.valueobject.FundListSavedData;
import com.manulife.pension.ps.service.report.fandp.valueobject.UserSavedData;
import com.manulife.pension.ps.service.report.fandp.valueobject.UserSavedDataLists;
import com.manulife.pension.ps.service.report.investment.valueobject.FundCategory;
import com.manulife.pension.service.contract.valueobject.ContractVO;
import com.manulife.pension.service.environment.valueobject.DeCodeVO;
import com.manulife.pension.service.fund.cache.FundInfoCache;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.fandp.valueobject.FandpFilterCriteria;
import com.manulife.pension.service.fund.valueobject.AssetClassVO;

/**
 * Utility class that supports to create the filter options
 * 
 * @author ayyalsa
 *
 */
public class FapFilterUtility {

	/**
	 * A static object Map to store the filters
	 */
	public static Map<String, Object> cacheMap = 
		new HashMap<String, Object>();
	
	/**
	 * A Map that holds the error codes mapping with the user event
	 */
	public static Map<String, Integer> userEventMap = new HashMap<String, Integer>();
	
	/**
	 * Static block to initialize the event map along with the error codes
	 */
	static {
		userEventMap.put(FapConstants.DISPLAY_QUERY_RESULTS, CommonErrorCodes.MUST_SELECT_A_QUERY_TO_DISPLAY);
		userEventMap.put(FapConstants.EDIT_CRITERIA, CommonErrorCodes.MUST_SELECT_A_QUERY_TO_EDIT);
		userEventMap.put(FapConstants.DELETE_QUERY, CommonErrorCodes.MUST_SELECT_A_QUERY_DELETE);
		userEventMap.put(FapConstants.DISPLAY_LIST, CommonErrorCodes.MUST_SELECT_A_LIST_TO_DISPLAY);
		userEventMap.put(FapConstants.DELETE_LIST, CommonErrorCodes.MUST_SELECT_A_LIST_DELETE);
	}
	/**
	 * Creates the list for the base view by filter.
	 * 
	 * @return baseFilterViewOptionList List<LabelValueBean>
	 */
	@SuppressWarnings("unchecked")
	public static List<LabelValueBean> createBaseFilterViewOptionList(
			boolean includeContractFundsOption) {
		
		List<LabelValueBean> baseFilterViewOptionList;
		
		if (includeContractFundsOption) {
			baseFilterViewOptionList = 
				(List<LabelValueBean>)cacheMap.get(FapConstants.BASE_FILTER_VIEW_OPTION_LIST);
			
			// if the list is null
			if (baseFilterViewOptionList == null || baseFilterViewOptionList.isEmpty()) {
				// initialize the List
				baseFilterViewOptionList = new ArrayList<LabelValueBean>(2);
				
				// add the options to the list
				baseFilterViewOptionList.add(new LabelValueBean(
						FapConstants.BASE_FILTER_ALL_FUNDS, 
						FapConstants.BASE_FILTER_ALL_FUNDS_KEY));
				baseFilterViewOptionList.add(new LabelValueBean(
						FapConstants.BASE_FILTER_CONTRACT_FUNDS, 
						FapConstants.BASE_FILTER_CONTRACT_FUNDS_KEY));
				
				// place the list in map
				cacheMap.put(FapConstants.BASE_FILTER_VIEW_OPTION_LIST, 
						baseFilterViewOptionList);
			}
		} else {
			baseFilterViewOptionList = 
				(List<LabelValueBean>)cacheMap.get(FapConstants.BASE_FILTER_VIEW_NO_CONTRACT_FUNDS);
			
			// if the list is null
			if (baseFilterViewOptionList == null || baseFilterViewOptionList.isEmpty()) {
				// initialize the List
				baseFilterViewOptionList = new ArrayList<LabelValueBean>(2);
				
				// add the options to the list
				baseFilterViewOptionList.add(new LabelValueBean(
						FapConstants.BASE_FILTER_ALL_FUNDS, 
						FapConstants.BASE_FILTER_ALL_FUNDS_KEY));
				
				// place the list in map
				cacheMap.put(FapConstants.BASE_FILTER_VIEW_NO_CONTRACT_FUNDS, 
						baseFilterViewOptionList);
			}
		}
				
		return baseFilterViewOptionList;
	}
	
	/**
	 * Creates the FandpFilterCriteria object based on the form-bean values
	 * 
	 * @param actionForm
	 * @param advanceFilterOption
	 * @param request 
	 * @return created FandpFilterCriteria Object
	 * @throws SystemException 
	 */
	public static FandpFilterCriteria createFandpFilterCriteria(
			FapForm actionForm, 
			String advanceFilterOption, 
			HttpServletRequest request)	throws SystemException {
		
		int contractNumber = 0;
		String contractSearchText = actionForm.getContractSearchText();
		
		// check if the search text is a contract number
		if (StringUtils.isNotBlank(contractSearchText) && 
				StringUtils.isNumeric(contractSearchText)) {
			contractNumber = Integer.parseInt(
					actionForm.getContractSearchText());
		}
		
		// create the filter criteria based on the form values
		FandpFilterCriteria filterCriteria = new FandpFilterCriteria(
				actionForm.getBaseFilterSelect(), 
				actionForm.getGroupBySelect(), 
				actionForm.getClassSelect(), 
				contractNumber, 
				actionForm.getCompanyId());
		
		if (actionForm.isContractMode()) {
			filterCriteria.setIncludeClosedFunds(true); // Should be replaced with setIncludeNMLFunds(true);??
			filterCriteria.setIncludeClosedFunds(true);
			filterCriteria.setExcludeLSPSFunds(false);
			filterCriteria.setIncludeOnlySigPlusFunds(false);
		} else {
        	filterCriteria.setIncludeNMLFunds(actionForm.isIncludeNML());
			filterCriteria.setIncludeClosedFunds(actionForm.isIncludeClosedFunds());
			filterCriteria.setExcludeLSPSFunds(true);
			filterCriteria.setIncludeOnlySigPlusFunds(actionForm.isIncludeOnlySigPlusFunds());
		}
		// If the advance filter option is selected, set the selected option 
		// to the filter criteria
		if (advanceFilterOption != null) {
			filterCriteria.setAdvanceFilterOption(advanceFilterOption);
		}
	
		return filterCriteria;
	}
	
	/**
	 * Create a contract list from the List of contractVO objects
	 * 
	 * @param contractVOList
	 * @return list of contracts as List<LabelValueBean>
	 */
	public static List<LabelValueBean> createContractResultsList(
			List<ContractVO> contractVOList) {
		
		// initialize the list
		List<LabelValueBean> contractResultsList = new ArrayList<LabelValueBean>();
		
		// if the contractVO list is not empty, create the contract options
		if (!contractVOList.isEmpty()){
			
			// default option would be 'Please select'
			contractResultsList.add(new LabelValueBean(
					FapConstants.PLEASE_SELECT, FapConstants.NOT_APPLICABLE));
			
			StringBuffer label;
			
			// Iterate the VO list and create the options
			for (ContractVO contractVO : contractVOList ){
				String companyName = contractVO.getCompanyName();
				
				if (StringUtils.equals(companyName, "US")) {
					companyName ="USA";
				}
				
				label = new StringBuffer();
				label.append(contractVO.getContractName()).append(
						FapConstants.HYPHON_SYMBOL);
				label.append(contractVO.getContractNumber()).append(
						FapConstants.BRACKET_OPEN_SYMBOL);
				label.append(companyName).append(
						FapConstants.BRACKET_CLOSE_SYMBOL);
				
				contractResultsList.add(new LabelValueBean(label.toString(), 
						contractVO.getCompanyName() +
						String.valueOf(contractVO.getContractNumber())));
			}

		}
		return contractResultsList;
	}
	
	/**
	 * Creates the FandpFilterCriteria object based on the form-bean values
	 * 
	 * @param actionForm
	 * @param advanceFilterOption
	 * @return created FandpFilterCriteria Object
	 * @throws SystemException 
	 */
	public static FandpFilterCriteria createFandpFilterCriteriaForIReports(
			FapForm actionForm, 
			String advanceFilterOption)	throws SystemException {
		
		int contractNumber = 0;
		String contractSearchText = actionForm.getContractSearchText();
		
		// check if the search text is a contract number
		if (StringUtils.isNotBlank(contractSearchText) && 
				StringUtils.isNumeric(contractSearchText)) {
			contractNumber = Integer.parseInt(
					actionForm.getContractSearchText());
		}
		
		// create the filter criteria based on the form values
		FandpFilterCriteria filterCriteria = new FandpFilterCriteria(
				actionForm.getBaseFilterSelect(), 
				actionForm.getGroupBySelect(), 
				actionForm.getClassSelect(), 
				contractNumber, 
				actionForm.getCompanyId());
		
		// If the advance filter option is selected, set the selected option 
		// to the filter criteria
		if (advanceFilterOption != null) {
			filterCriteria.setAdvanceFilterOption(advanceFilterOption);
		}
	
		return filterCriteria;
	}
	
	/**
	 * Creates the list for the fund Class for the filter.
	 * 
	 * @return fundClassList List<LabelValueBean>
	 * @throws SystemException 
	 */
	@SuppressWarnings("unchecked")
	public static Collection<DeCodeVO> getFundClassList() 
	throws SystemException {
	
		Collection<DeCodeVO> fundClassList = 
			(Collection<DeCodeVO>) cacheMap.get(FapConstants.BASE_FILTER_FUND_CLASS_LIST);
		
		// if the class list is null, get from the FundService cache
		if (fundClassList == null || fundClassList.isEmpty()) {
			fundClassList = 
				FundServiceDelegate.getInstance().getAllFundsClassList();
			
			cacheMap.put(FapConstants.BASE_FILTER_FUND_CLASS_LIST, fundClassList);
		}
		
		return fundClassList;
	}
	
	/**
	 * Creates the list for the group by options.
	 * 
	 * @return assetClassList List<LabelValueBean>
	 */
	@SuppressWarnings("unchecked")
	public static List<LabelValueBean> createBaseFilterGroupByList() {
		
		List<LabelValueBean> baseFilterGroupByList = 
			(List<LabelValueBean>) cacheMap.get(FapConstants.BASE_FILTER_GROUP_BY_LIST);
		
		// If not in cache, create the options and place in cache Map 
		if (baseFilterGroupByList == null || baseFilterGroupByList.isEmpty()) {
			baseFilterGroupByList = new ArrayList<LabelValueBean>(2);
			
			baseFilterGroupByList.add(new LabelValueBean(
					FapConstants.ASSET_CLASS_FILTER, 
					FapConstants.ASSET_CLASS_FILTER_KEY));
			baseFilterGroupByList.add(new LabelValueBean(
					FapConstants.RISK_CATEGORY_FILTER, 
					FapConstants.RISK_CATEGORY_FILTER_KEY));
			
			cacheMap.put(FapConstants.BASE_FILTER_GROUP_BY_LIST, 
					baseFilterGroupByList);
		}
				
		return baseFilterGroupByList;
	}
	
	/**
	 * Creates the list for the optional filter drop-down.
	 * 
	 * @param fapForm
	 * @return optionalFilterList List<LabelValueBean>
	 */
	@SuppressWarnings("unchecked")
	public static List<LabelValueBean> createOptionalFilterList(
			FapForm fapForm) {
		
		List<LabelValueBean> optionalFilterList;
		
		/*
		 * If the view By option is selected as 'contract funds', then
		 * populate the options, which are applicable to contract funds view
		 */
		if (FapConstants.BASE_FILTER_CONTRACT_FUNDS_KEY.equals(
				fapForm.getBaseFilterSelect())) {
			
			optionalFilterList = (List<LabelValueBean>)cacheMap.get(
					FapConstants.OPTIONAL_FILTER_LIST_CONTRACT_FUNDS);
			
			if (optionalFilterList == null || optionalFilterList.isEmpty()) {
				optionalFilterList = new ArrayList<LabelValueBean>(10);
				
				optionalFilterList.add(new LabelValueBean(
						FapConstants.CONTRACT_AVAILABLE_FUNDS_FILTER, 
						FapConstants.CONTRACT_AVAILABLE_FUNDS_FILTER_KEY));
				optionalFilterList.add(new LabelValueBean(
						FapConstants.CONTRACT_SELECTED_FUNDS_FILTER, 
						FapConstants.CONTRACT_SELECTED_FUNDS_FILTER_KEY));
				optionalFilterList.add(new LabelValueBean(
						FapConstants.ASSET_CLASS_FILTER, 
						FapConstants.ASSET_CLASS_FILTER_KEY));
				optionalFilterList.add(new LabelValueBean(
						FapConstants.RISK_CATEGORY_FILTER, 
						FapConstants.RISK_CATEGORY_FILTER_KEY));
				optionalFilterList.add(new LabelValueBean(
						FapConstants.CUSTOM_QUERY_FILTER, 
						FapConstants.CUSTOM_QUERY_FILTER_KEY));
				optionalFilterList.add(new LabelValueBean(
						FapConstants.SEARCH_FUND_FILTER, 
						FapConstants.SEARCH_FUND_FILTER_KEY));
				optionalFilterList.add(new LabelValueBean(
						FapConstants.SAVED_CUSTOM_QUERIES_FILTER, 
						FapConstants.SAVED_CUSTOM_QUERIES_FILTER_KEY));
				
				// place the contract specific options in the cache map with
				// different key
				cacheMap.put(FapConstants.OPTIONAL_FILTER_LIST_CONTRACT_FUNDS, 
						optionalFilterList);
			}
			
		} else {
			/*
			 * If the view By option is selected as 'All funds', then
			 * populate the options, which are applicable to All funds view
			 */
			optionalFilterList = (List<LabelValueBean>)cacheMap.get(
					FapConstants.OPTIONAL_FILTER_LIST_ALL_FUNDS);
			
			if (optionalFilterList == null || optionalFilterList.isEmpty()) {
				optionalFilterList = new ArrayList<LabelValueBean>(10);
				
				optionalFilterList.add(new LabelValueBean(
						FapConstants.ALL_FUNDS_FILTER, 
						FapConstants.ALL_FUNDS_FILTER_KEY));
				optionalFilterList.add(new LabelValueBean(
						FapConstants.ASSET_CLASS_FILTER, 
						FapConstants.ASSET_CLASS_FILTER_KEY));
				optionalFilterList.add(new LabelValueBean(
						FapConstants.RISK_CATEGORY_FILTER, 
						FapConstants.RISK_CATEGORY_FILTER_KEY));
				/*
				  Temporarily disabled during alignment project - May 2014
				  Release. The future uncertain for shortlists, so we are
				  leaving all the code for the for the filter, and are simply
				  suppressing the ability to select the shortlist
				  
				  optionalFilterList.add(new LabelValueBean(
				  FapConstants.SHORTLIST_FILTER,
				  FapConstants.SHORTLIST_FILTER_KEY));
				 */
				optionalFilterList.add(new LabelValueBean(
						FapConstants.CUSTOM_QUERY_FILTER, 
						FapConstants.CUSTOM_QUERY_FILTER_KEY));
				optionalFilterList.add(new LabelValueBean(
						FapConstants.SEARCH_FUND_FILTER, 
						FapConstants.SEARCH_FUND_FILTER_KEY));
				optionalFilterList.add(new LabelValueBean(
						FapConstants.SAVED_LIST_FILTER, 
						FapConstants.SAVED_LIST_FILTER_KEY));
				optionalFilterList.add(new LabelValueBean(
						FapConstants.SAVED_CUSTOM_QUERIES_FILTER, 
						FapConstants.SAVED_CUSTOM_QUERIES_FILTER_KEY));
				
				// place the all funds specific options in the cache map with
				// different key
				cacheMap.put(FapConstants.OPTIONAL_FILTER_LIST_ALL_FUNDS, 
						optionalFilterList);
			}
		}
		
		return optionalFilterList;
	}
	
	/**
	 * Creates the list for the Asset Classes for the filter.
	 * 
	 * @return assetClassList List<LabelValueBean>
	 */
	@SuppressWarnings("unchecked")
	public static List<LabelValueBean> getAssetClassList(boolean isGIFLRequired) {
		List<LabelValueBean> assetClassList = null;
		
		if(isGIFLRequired){
			assetClassList = (List<LabelValueBean>) cacheMap.get(FapConstants.ASSET_CLASS_LIST);
		} else {
			assetClassList = (List<LabelValueBean>) cacheMap.get(FapConstants.ASSET_CLASS_LIST_NO_GIFL);
		}
		// if not in cache. initialize it
		if (assetClassList == null || assetClassList.isEmpty()) {
			assetClassList = new ArrayList<LabelValueBean>();
			// get the list of class that need to be excluded.
			Collection assetClassesToExclude = getAssetClassesToExclude(isGIFLRequired);		
			// iterate all the asset classes and create options for the filter
			for (AssetClassVO assetClassVO : 
				(List<AssetClassVO>) FundInfoCache
					.getAllAssetClasses()) {				
				if (!assetClassesToExclude.contains(assetClassVO
						.getAssetClass())) {
					assetClassList.add(new LabelValueBean(
							assetClassVO.getAssetClassDesc(), 
							assetClassVO.getAssetClass()));
				}
			}
			
			// place the list in cache, with asset class specific key
			if (isGIFLRequired) {
				cacheMap.put(FapConstants.ASSET_CLASS_LIST, assetClassList);
			} else {
				cacheMap.put(FapConstants.ASSET_CLASS_LIST_NO_GIFL,
						assetClassList);
			}
		}
		return assetClassList;
	}		


	/**
	 * Creates the list for the Asset Classes for the Custom Query filter.
	 * 
	 * @param isGIFLRequired
	 *            - indicator for GIFL
	 * @return assetClassListForCQ List<LabelValueBean>
	 */
	@SuppressWarnings("unchecked")
	public static List<LabelValueBean> getAssetClassListForCQ(
			boolean isGIFLRequired) {
		List<LabelValueBean> assetClassListForCQ = null;
		if (isGIFLRequired) {
			assetClassListForCQ = (List<LabelValueBean>) cacheMap
					.get(FapConstants.ASSET_CLASS_LIST_FOR_CQ);
		}else{
			assetClassListForCQ = (List<LabelValueBean>) cacheMap
			.get(FapConstants.ASSET_CLASS_LIST_FOR_CQ_NO_GIFL);
		}
			
		
		// if not in cache. initialize it
		if (assetClassListForCQ == null || assetClassListForCQ.isEmpty()) {
			assetClassListForCQ = new ArrayList<LabelValueBean>();
			
			assetClassListForCQ.add(0, new LabelValueBean(
					FapConstants.DEFAULT_SELECT, FapConstants.BLANK));
			
			// get the list of class that need to be excluded.
			Collection assetClassesToExclude = getAssetClassesToExclude(isGIFLRequired);	
			
			// iterate all the asset classes and create options for the filter
			for (AssetClassVO assetClassVO : (List<AssetClassVO>) FundInfoCache
					.getAllAssetClasses()) {
				if (!(assetClassesToExclude.contains(assetClassVO
						.getAssetClass()))) {
					assetClassListForCQ.add(new LabelValueBean(assetClassVO
							.getAssetClassDesc(), assetClassVO
							.getAssetClassDesc()));
			}
		}

		// place the list in cache, with asset class specific key
			if (isGIFLRequired) {
				cacheMap.put(FapConstants.ASSET_CLASS_LIST_FOR_CQ,
						assetClassListForCQ);
			} else {
				cacheMap.put(FapConstants.ASSET_CLASS_LIST_FOR_CQ_NO_GIFL,
						assetClassListForCQ);
			}
			
		}
		return assetClassListForCQ;
	} 

	
	/**
	 * Creates the list for the Risk/Return categories for the filter.
	 * 
	 * @return riskCategoryList List<LabelValueBean>
	 */
	@SuppressWarnings("unchecked")
	public static List<LabelValueBean> getRiskCategoryList(boolean isGIFLRequired) {
		
		List<LabelValueBean> riskCategoryList = null;
		
		// If contract does not have GIFL Select, do not include the GIFL category 
		if(isGIFLRequired){			
			riskCategoryList = (List<LabelValueBean>)cacheMap.get(
					FapConstants.RISK_CATEGORY_LIST);
		}else{
			riskCategoryList = (List<LabelValueBean>)cacheMap.get(
					FapConstants.RISK_CATEGORY_LIST_NO_GIFL);
		}
			
		// if not in cache. initialize it
		if (riskCategoryList == null || riskCategoryList.isEmpty()) {
			riskCategoryList = new ArrayList<LabelValueBean>();

			// iterate all the FundCategory and create options for the filter
			for (int i = 0; i < FundCategory.CATEGORY_DESCRIPTION_EXCLUDED.length; i++) {
				if (isGIFLRequired || i != 2) {
					riskCategoryList.add(new LabelValueBean(
							FundCategory.CATEGORY_DESCRIPTION_EXCLUDED[i],
							FundCategory.CATEGORY_DESCRIPTION_EXCLUDED[i]));
				}
				
			}
			// place the list in cache, with FundCategory specific key
			if (isGIFLRequired) {
				cacheMap.put(FapConstants.RISK_CATEGORY_LIST, riskCategoryList);
			} else {
				cacheMap.put(FapConstants.RISK_CATEGORY_LIST_NO_GIFL,
						riskCategoryList);
			}
		}
		return riskCategoryList;
	}
	

	/**
	 * Creates the list for the Risk/Return categories for the filter.
	 * 
	 * @param isGIFLRequired
	 *            - boolean indicator for GIFL feature
	 * @return riskCategoryList List<LabelValueBean>
	 */
	@SuppressWarnings("unchecked")
	public static List<LabelValueBean> getRiskCategoryListForCQ(
			boolean isGIFLRequired) {
		List<LabelValueBean> riskCategoryListForCQ = null;
		
		if (isGIFLRequired) {
			riskCategoryListForCQ = (List<LabelValueBean>) cacheMap
					.get(FapConstants.RISK_CATEGORY_LIST_FOR_CQ);
		}else{
			riskCategoryListForCQ = (List<LabelValueBean>) cacheMap
			.get(FapConstants.RISK_CATEGORY_LIST_FOR_CQ_NO_GIFL);
		}
			
		// if not in cache. initialize it
		if (riskCategoryListForCQ == null || riskCategoryListForCQ.isEmpty()) {
			riskCategoryListForCQ = new ArrayList<LabelValueBean>();

			riskCategoryListForCQ.add(0, new LabelValueBean(
					FapConstants.DEFAULT_SELECT, FapConstants.BLANK));

			riskCategoryListForCQ.addAll(getRiskCategoryList(isGIFLRequired));

			// place the list in cache, with FundCategory specific key
			if (isGIFLRequired) {
				cacheMap.put(FapConstants.RISK_CATEGORY_LIST_FOR_CQ,
						riskCategoryListForCQ);
			} else {
				cacheMap.put(FapConstants.RISK_CATEGORY_LIST_FOR_CQ_NO_GIFL,
						riskCategoryListForCQ);
			}
		}
			return riskCategoryListForCQ;
	} 
	

	/**
	 * Creates the list for the Short-list filter option. for all the 4
	 * drop-downs (fundMenu, shortListType, conservativeFund,
	 * assetAllocationGroup)
	 * 
	 * @return shortListMap HashMap<String, List<LabelValueBean>>
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, List<LabelValueBean>> 
	createShortListOptions() throws SystemException {
		
		HashMap<String, List<LabelValueBean>> shortListMap = 
			(HashMap<String, List<LabelValueBean>>) cacheMap.get(FapConstants.SHORT_LIST);
		
		// if not in cache, initialize it
		if (shortListMap == null || shortListMap.isEmpty()) {
			shortListMap = new HashMap<String, List<LabelValueBean>>();
			
			/*
			 * Add the Fund Menu drop down options
			 */
			List<LabelValueBean> filterOptions = 
				new ArrayList<LabelValueBean>(4);
			
			filterOptions.add(new LabelValueBean(
					FapConstants.PLEASE_SELECT, 
					FapConstants.NOT_APPLICABLE));
			filterOptions.add(new LabelValueBean(
					FapConstants.FUND_MENU_ALL_FUNDS_FILTER, 
					FapConstants.FUND_MENU_ALL_FUNDS_FILTER_KEY));
			filterOptions.add(new LabelValueBean(
					FapConstants.FUND_MENU_RETAIL_FUNDS_FILTER, 
					FapConstants.FUND_MENU_RETAIL_FUNDS_FILTER_KEY));
			filterOptions.add(new LabelValueBean(
					FapConstants.FUND_MENU_SUB_ADVISED_FUNDS_FILTER, 
					FapConstants.FUND_MENU_SUB_ADVISED_FUNDS_FILTER_KEY));
			
			shortListMap.put(
					FapConstants.SHORTLIST_FUND_MENU_SELECT, filterOptions);

			/*
			 * Add the Short list type drop down options
			 */
			filterOptions = new ArrayList<LabelValueBean>(5);
			
			filterOptions.add(new LabelValueBean(
					FapConstants.PLEASE_SELECT, 
					FapConstants.NOT_APPLICABLE));
			filterOptions.add(new LabelValueBean(
					FapConstants.SHORTLIST_TYPE_LOWEST_COST, 
					FapConstants.SHORTLIST_TYPE_LOWEST_COST_KEY));
			filterOptions.add(new LabelValueBean(
					FapConstants.SHORTLIST_TYPE_TOP_PREFORMER, 
					FapConstants.SHORTLIST_TYPE_TOP_PREFORMER_KEY));
			filterOptions.add(new LabelValueBean(
					FapConstants.SHORTLIST_TYPE_3_YR_PERFORMANCE, 
					FapConstants.SHORTLIST_TYPE_3_YR_PERFORMANCE_KEY));
			filterOptions.add(new LabelValueBean(
					FapConstants.SHORTLIST_TYPE_5_YR_PERFORMANCE, 
					FapConstants.SHORTLIST_TYPE_5_YR_PERFORMANCE_KEY));
			
			shortListMap.put(
					FapConstants.SHORT_LIST_TYPE_SELECT, filterOptions);
			
			/*
			 * Add the Allocation Group drop down options
			 */
			filterOptions = new ArrayList<LabelValueBean>(4);
			
			// add the default option
			filterOptions.add(new LabelValueBean(
					FapConstants.PLEASE_SELECT, 
					FapConstants.NOT_APPLICABLE));
			
			// add the lifecycle and lifestyle families from the lookup table
			ArrayList<com.manulife.pension.service.environment.valueobject.LabelValueBean> shortlistFamilies 
				= EnvironmentServiceDelegate.getInstance(FapConstants.PS_APPLICATION_ID).getShortlistFamilyNames();
			//Set<String> shortlistFamilyKeys = shortlistFamilies.keySet();
			for (int i = 0; i < (shortlistFamilies != null ? shortlistFamilies.size() : 0); i++ ) {
				com.manulife.pension.service.environment.valueobject.LabelValueBean bean = shortlistFamilies.get(i);
				String key = bean.getValue();
				String name = bean.getLabel();
				filterOptions.add(new LabelValueBean(name, key));
			}
			
			shortListMap.put(
					FapConstants.SHORT_LIST_ALLOCATION_GROUP_SELECT, filterOptions);
			
			/*
			 * Add the Conservative fund drop down options
			 */
			filterOptions = new ArrayList<LabelValueBean>(3);
			
			filterOptions.add(new LabelValueBean(
					FapConstants.PLEASE_SELECT, 
					FapConstants.NOT_APPLICABLE));
			filterOptions.add(new LabelValueBean(
					FapConstants.CONSERVATIVE_FUNDS_MONEY_MARKET_FUND, 
					FapConstants.CONSERVATIVE_FUNDS_MONEY_MARKET_FUND_KEY));
			filterOptions.add(new LabelValueBean(
					FapConstants.CONSERVATIVE_FUNDS_STABLE_VALUE_FUND,
					FapConstants.CONSERVATIVE_FUNDS_STABLE_VALUE_FUND_KEY));
			
			shortListMap.put(
					FapConstants.SHORT_LIST_INCOME_FUND_SELECT, filterOptions);

			// Put the created options list in the cache map 
			cacheMap.put(FapConstants.SHORT_LIST, shortListMap);
		}

		
		return shortListMap;
	}
	
	/**
	 * Retrieves a saved list or custom query based on the profile and data type passed
	 * @param profileId
	 * @param savedDataType
	 * @param userAllowedToViewSavedData
	 * @return savedListMapMap 
	 */
	public static List<LabelValueBean> getSavedListsAndCustomQuries(
			long profileId, String savedDataType, 
			boolean userAllowedToViewSavedData) throws SystemException {
		
		List<LabelValueBean> savedListMap = new ArrayList<LabelValueBean>();

		savedListMap.add(new LabelValueBean(
				FapConstants.PLEASE_SELECT, 
				FapConstants.NOT_APPLICABLE));
		
		if (userAllowedToViewSavedData) {
			
			UserSavedDataLists savedDataLists = 
				FandpServiceDelegate.getInstance().retrieveUserSavedDataLists(
						profileId, savedDataType);
		
			if (StringUtils.equals(savedDataType, FapConstants.TYPE_SAVED_CUSTOM_QUERY)) {
				Map<String, CustomQuerySavedData> customQuerySavedList = 
					savedDataLists.getCustomQueries();
				
				if (customQuerySavedList != null && !customQuerySavedList.isEmpty()) {
					
					for (CustomQuerySavedData customQuerydata : customQuerySavedList.values()){
						savedListMap.add(new LabelValueBean(
								customQuerydata.getName(), 
								customQuerydata.getName()));
					}
				}
			} else if (StringUtils.equals(savedDataType, FapConstants.TYPE_SAVED_FUND_LIST)) {
				Map<String, FundListSavedData> fundListSavedDataMap = 
					savedDataLists.getFundLists();
				
				if (fundListSavedDataMap != null && !fundListSavedDataMap.isEmpty()) {
					
					for (FundListSavedData fundListSavedData : fundListSavedDataMap.values()){
						savedListMap.add(new LabelValueBean(
								fundListSavedData.getName(), 
								fundListSavedData.getName()));
					}
				}
			}
		}
		
		return savedListMap;
	}
	
	/**
	 * Creates the list for the ReportsAndDownload drop-down
	 * 
	 * @return ReportsAndDownloadMap HashMap<String, String>
	 */
	public static Map<String, String> getReportsAndDownloadList() {

	    Map<String, String> map = new LinkedHashMap<String, String>();
	    
	    map.put(FapConstants.DEFAULT_REPORTS_TITLE, 
	    		FapConstants.DEFAULT_REPORTS_VALUE);
        
	    map.put(FapConstants.PRINT_CURRENT_VIEW_PDF_TITLE, 
        		FapConstants.PRINT_CURRENT_VIEW_PDF);
        
	    map.put(FapConstants.DOWNLOAD_CSV_TITLE, 
        		FapConstants.DOWNLOAD_CSV);
        
	    map.put(FapConstants.DOWNLOAD_CSV_ALL_TITLE, 
        		FapConstants.DOWNLOAD_CSV_ALL);
        
	    map.put(FapConstants.STANDARD_REPORTS_TITLE, 
        		FapConstants.NON_REPORTS_VALUE);
        
	    map.put(FapConstants.EXPENSE_RATIO_REPORT_TITLE, 
        		FapConstants.EXPENSE_RATIO_REPORT);
        
	    map.put(FapConstants.FUND_CHARACTERISITICS_REPORT_TITLE,
                FapConstants.FUND_CHARACTERISITICS_REPORT);
	    
	    map.put(FapConstants.INVESTEMNT_RETURNS_AND_EXPENSE_RATIO_REPORT_TITLE,
                FapConstants.INVESTEMNT_RETURNS_AND_EXPENSE_RATIO_REPORT);
        
	    map.put(FapConstants.INVESTEMNT_RETURNS_AND_STANDARD_DEVIATION_REPORT_TITLE,
                FapConstants.INVESTEMNT_RETURNS_AND_STANDARD_DEVIATION_REPORT);
	    
	    map.put(FapConstants.MORNINGSTAR_RATINGS_AND_TICKR_SYMBOLS_REPORT_TITLE,
                FapConstants.MORNINGSTAR_RATINGS_AND_TICKR_SYMBOLS_REPORT);
        
	    map.put(FapConstants.OTHER_REPORTS_TITLE, 
        		FapConstants.NON_REPORTS_VALUE);
        
	    map.put(FapConstants.MARKET_INDEX_REPORT_TITLE, 
        		FapConstants.MARKET_INDEX_REPORT);
	    
        return map;
    }
	
	/**
	 * Creates short-list parameters, from the action form,
	 * i.e. based on the user input
	 *  
	 * @param fapForm
	 * @return fundShortListOptions Map
	 */
	public static Map<String, String> createShortListParameters(
			HttpServletRequest request,
			FapForm fapForm) {
		
		Map<String, String> fundShortListOptions = 
			new HashMap<String, String>();
		
		fundShortListOptions.put(FapConstants.FUND_MENU_PACKAGE_SERIES, 
				fapForm.getShortlistFundMenuSelect());
		fundShortListOptions.put(FapConstants.SHORT_LIST_TYPE, 
				fapForm.getShortlistTypeSelect());
		fundShortListOptions.put(FapConstants.ASSET_ALLOCATION_GROUP, 
				fapForm.getAllocationGroupSelect());
		fundShortListOptions.put(FapConstants.INCOME_FUND, 
				fapForm.getConservativeFundSelect());

		// always place the options ins session, this is required for IReports
		request.getSession(false).setAttribute(
				FapConstants.SELECTED_SHORTLIST_OPTIONS, fundShortListOptions);
		
		return fundShortListOptions;
	}
	
	/**
	 * Returns the error code matching to the user action
	 * 
	 * @param fapForm
	 * @return error code
	 */
	public static int getErroCodeForSavedDataFilter(FapForm fapForm) {
		String userEvent = fapForm.getEventTriggered();
		
		return userEventMap.get(userEvent);
		
	}
	
	/**
	 * Creates the UserSavedData object based on the userDataType
	 * 
	 * @param fapForm
	 * @param userDataType
	 * @return userSavedData
	 */
	public static UserSavedData createUserSavedData(FapForm fapForm, String userDataType) {
		
		UserSavedData userSavedData = null;
		if (StringUtils.equals(userDataType, FapConstants.TYPE_SAVED_CUSTOM_QUERY)) {
			userSavedData = new CustomQuerySavedData();
		} else if (StringUtils.equals(userDataType, FapConstants.TYPE_SAVED_FUND_LIST)) {
			userSavedData = new FundListSavedData();
		}
		
		userSavedData.setListType(userDataType);
		userSavedData.setName(fapForm.getSaveListName().trim());
		userSavedData.setDelimtedData(fapForm.getSelectedFundsValues());
		return userSavedData;
	}

	/**
	 * Converts the Fund IDs String (IDs separated by '|') to a Set
	 * 
	 * @param valuesAsString
	 * @return
	 */
	public static Set<String> convertStringToSet(String valuesAsString) {
		
		// Convert the Fund IDs String to a Set
		Set<String> valuesAsSet = new HashSet<String>();
		if (valuesAsString.length() > 0) {
			StringTokenizer stringTokenizer = 
				new StringTokenizer(valuesAsString, "|");
			
			while(stringTokenizer.hasMoreElements()) {
				valuesAsSet.add(stringTokenizer.nextToken());
			}
		}
		
		return valuesAsSet;
	}
	
	/**
     * Creates the Filter Criteria Object and places it in the session.
     * This is used for the IReports to decide the title.
     * 
     * @param request
     * @param actionForm
     * @throws SystemException
     */
    @SuppressWarnings("unchecked")
	public static void setLastFilterCriteriaInSession(HttpServletRequest request, 
    		FapForm actionForm) throws SystemException {
    	
    	//  get the advance filter option
    	String advanceFilterForIReports = 
    		actionForm.getAdvanceFilterForIReports();
    	
    	FandpFilterCriteria filterCriteria = 
			FapFilterUtility.createFandpFilterCriteriaForIReports(
				actionForm, advanceFilterForIReports);
    	
    	/*
    	 * if the contract number is not zero, then it means the "view by' option
    	 * is by "contract funds". Set the filter option always to be as 
    	 * "funds selected by the contract" 
    	 */
    	if (filterCriteria.getContractNumber() != 0) {
    		
    		filterCriteria.setAdvanceFilterOption(FapConstants.CONTRACT_SELECTED_FUNDS_FILTER_KEY);
    		
    	} else if (!StringUtils.isBlank(advanceFilterForIReports)) {

    		/* 
    		 * If the option is Short-list, get the selected short-list 
    		 * options from the session and set it to the filter criteria object
    		 */
    		if (StringUtils.equals(FapConstants.SHORTLIST_FILTER_KEY, 
    				advanceFilterForIReports)) {
    			
    			filterCriteria.setFundShortListOptions(
    					(Map<String,String>) request.getSession(false).getAttribute(
    							FapConstants.SELECTED_SHORTLIST_OPTIONS));
    		
    		/* 
    		 * if the selected option is none of the "All Available, Retail, 
    		 * Sub-Advised" funds then set the option as "others" in the 
    		 * filter criteria object
    		 */
    		} else if (!(StringUtils.equals(FapConstants.ALL_FUNDS_FILTER_KEY, advanceFilterForIReports) ||
    				StringUtils.equals(FapConstants.RETAIL_FUNDS_FILTER_KEY, advanceFilterForIReports) ||
    				StringUtils.equals(FapConstants.SUB_ADVISED_FUNDS_FILTER_KEY, advanceFilterForIReports))) {
				
				filterCriteria.setAdvanceFilterOption(FapConstants.OTHERS);
			}
		
    	} else {
    		filterCriteria = null;
    	}
    	
    	/*
    	 * place the filter criteria object in the session. IReport will use it
    	 * to decide the titles
    	 */
		request.getSession(false).setAttribute(
				FapConstants.LAST_EXECUTED_FILTER_CRITERIA, filterCriteria);
    }
    
    private static Collection<String> getAssetClassesToExclude(
			boolean isGIFLRequired) {
    	Collection<String> assetClassesToExclude = new ArrayList<String>();
		assetClassesToExclude.add(FapConstants.ASSETCLASS_EXCLUDE_STV);
		assetClassesToExclude.add(FapConstants.ASSETCLASS_EXCLUDE_PB);
		assetClassesToExclude.add(FapConstants.ASSETCLASS_EXCLUDE_MCF);
		assetClassesToExclude.add(FapConstants.ASSETCLASS_EXCLUDE_MBC);
		assetClassesToExclude.add(FapConstants.ASSETCLASS_EXCLUDE_MGC);
		assetClassesToExclude.add(FapConstants.ASSETCLASS_EXCLUDE_IDX);
		assetClassesToExclude.add(FapConstants.ASSETCLASS_EXCLUDE_FXS);
		assetClassesToExclude.add(FapConstants.ASSETCLASS_EXCLUDE_HYF);
		assetClassesToExclude.add(FapConstants.ASSETCLASS_EXCLUDE_GLB);
		if (!isGIFLRequired) {
			assetClassesToExclude.add(FapConstants.ASSETCLASS_EXCLUDE_LSG);
		}
		return assetClassesToExclude;
	}
}
