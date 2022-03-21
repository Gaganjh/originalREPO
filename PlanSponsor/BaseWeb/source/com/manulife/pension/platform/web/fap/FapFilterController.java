package com.manulife.pension.platform.web.fap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.content.CommonContentConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.delegate.FandpServiceDelegate;
import com.manulife.pension.platform.web.fap.constants.FapConstants;
import com.manulife.pension.platform.web.fap.tabs.FundScoreCardMetricsSelection;
import com.manulife.pension.platform.web.fap.tabs.util.FapTabUtility;
import com.manulife.pension.platform.web.fap.util.FapFilterUtility;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.exception.GenericExceptionWithContentType;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.fandp.valueobject.FandpFilterCriteria;
import com.manulife.pension.service.fund.fandp.valueobject.FundBaseInformation;
import com.manulife.pension.service.fund.fandp.valueobject.FundsAndPerformance;
import com.manulife.pension.util.content.GenericException;

/**
 * Handles the advance filter section of the Funds & Performance Page.
 * The Custom Query is not handled by this action class. Instead handled 
 * by a separate action class (CustomQueryAction)
 *  
 * @author ayyalsa
 *
 */
public abstract class FapFilterController extends FapController {

	/**
	 * Performs the basic operations required to enable the Advance Filter.
	 * This method will create the base fund array and creates the options for 
	 * the Advance filter select drop-down. 
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return actionForward
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	public String doAdvanceFilter(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) 
	throws IOException, ServletException, SystemException {
	
		FapForm fapForm = (FapForm) actionForm;

		/* get the Value Object for the FundInformation Tab and set it in 
		 * request. This object will be used to create the base fund array.
		 */ 
		 FundsAndPerformance fundsAndPerformance = null;

	    fundsAndPerformance = (FundsAndPerformance) 
	    		request.getSession().getAttribute(
	    				FapConstants.VO_FUNDS_AND_PERFORMANCE);
		
	    List<FundBaseInformation> tabValueObject = 
	    	fundsAndPerformance.getFundListToCreateBaseFundArray();
		request.setAttribute(FapConstants.VO_TAB_VALUE_OBJECT, tabValueObject);
		
		// set the results ID
		fapForm.setFilterResultsId(
				FapConstants.FUND_INFO_AND_SELECT_QUERY_OPTIONS);
		
		// create the Advance filter options and set in the form
		fapForm.setOptionalFilterList(
				FapFilterUtility.createOptionalFilterList(fapForm));

		String contractNumber = fapForm.getContractSearchText();
		if(null!=contractNumber && contractNumber.trim().length()>0) {
			fapForm.setJhiIndicatorFlg(FundServiceDelegate.getInstance().getJhiIndicatorFlg(contractNumber, FapConstants.PRODUCT_FEATURE_TYPE_CD_JHI));
			fapForm.setSvpIndicatorFlg(FundServiceDelegate.getInstance().getSvpIndicatorFlg(contractNumber, FapConstants.PRODUCT_FEATURE_TYPE_CD_SVP));
		}
		
		//return mapping.findForward(FapConstants.FORWARD_CONTINUE);
		return FapConstants.FORWARD_CONTINUE;
	}
	
	/**
	 * Gets the options required for the sub-sections for the 
	 * Advance filter options.
	 * 
	 * 1. ASSET CLASS option 	- 	create options for asset class list box
	 * 2. RISK CATEGORY option 	- 	create options for risk category list box
	 * 3. SHORTLIST option 		- 	create options for 
	 * 									a. Fund Menu drop-down
	 * 									b. ShortList Type drop-down
	 * 									c. Asset Allocation Group drop-down
	 * 									d. conservative Fund drop-down
	 * 4.  MY CUSTOM QUERIES	- 	create options for the 
	 * 							  	saved custom queries drop-down
	 * 5.  MY SAVED LIST		- 	create options for the saved fund list 
	 * 								drop-down
	 *  
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return actionForward
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	public String doInnerFilterOption(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) 
	throws IOException, ServletException, SystemException {
	
		FapForm fapForm = (FapForm) actionForm;
    	
		// set the filter id as inner select options
		fapForm.setFilterResultsId(FapConstants.INNER_SELECT_OPTIONS);
		
		/*
		 * 1. If the option selected is Asset Class, then create the options 
		 * for the asset class list box
		 */
		if (FapConstants.ASSET_CLASS_FILTER_KEY.equals(
				fapForm.getOptionalFilterSelect())) {
			
			fapForm.setAdvanceFilterInnerOptionList(
					FapFilterUtility.getAssetClassList(includeGIFL(fapForm, request)));

		/*
		 * 2. If the option selected is Risk Category, then create 
		 * the options for the risk category list box
		 */
		} else if (FapConstants.RISK_CATEGORY_FILTER_KEY.equals(
				fapForm.getOptionalFilterSelect())) {
			
			fapForm.setAdvanceFilterInnerOptionList(
					FapFilterUtility.getRiskCategoryList(
							includeGIFL(fapForm, request)));
			
		/* 
		 * 3. If the option selected is ShortList, then create the options for
		 * the (a)Fund Menu, (b)ShortList Type, (c)Asset Allocation Group and
		 * (d)conservative Fund drop-downs
		 */
		} else if (FapConstants.SHORTLIST_FILTER_KEY.equals(
				fapForm.getOptionalFilterSelect())) {
			
			fapForm.setShortListOptionList(
					FapFilterUtility.createShortListOptions());
			
			// Set the results id as shortList inner options
			fapForm.setFilterResultsId(
					FapConstants.SHORT_LIST_INNER_SELECT_OPTIONS);
		/*
		 * 4. If the option selected is My Custom Queries, then get 
		 * the options for the My Custom Queries list box from the database
		 */
		} else if (FapConstants.SAVED_CUSTOM_QUERIES_FILTER_KEY.equals(
				fapForm.getOptionalFilterSelect())) {
			
			fapForm.setAdvanceFilterInnerOptionList(
					FapFilterUtility.getSavedListsAndCustomQuries(getProfileId(request), 
							FapConstants.TYPE_SAVED_CUSTOM_QUERY, 
							userAllowedToViewSavedData(request)));
			
			removeAttributesFromSession(request, "customQueryFilters");
		/*
		 * 5. If the option selected is My Saved List, then get 
		 * the options for the My Saved list box from the database
		 */
		} else if (FapConstants.SAVED_LIST_FILTER_KEY.equals(
				fapForm.getOptionalFilterSelect())) {
			
			fapForm.setAdvanceFilterInnerOptionList(
					FapFilterUtility.getSavedListsAndCustomQuries(getProfileId(request), 
							FapConstants.TYPE_SAVED_FUND_LIST, 
							userAllowedToViewSavedData(request)));
		} 
		
		// forward to the filter results page to create the JSON
		//return mapping.findForward(FapConstants.FORWARD_CONTINUE);
		return FapConstants.FORWARD_CONTINUE;
	}
	
	
	/**
	 * Validates the user data for the selected Advance Filter Option.
	 * The below are the filter options that are handled in this method
	 * 		1. Asset Class, 
	 * 		2. Risk Category, 
	 * 		3. ShortList, 
	 * 		4. Search for a Fund
	 * 		5. My Custom Queries 
	 * 		6. My Saved Lists
	 * 		7. Others
	 * Filters the Fund IDs which matches the selected Advance Filter option
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return actionForward
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	public String doFilterFundIds(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
	
		FapForm fapForm = (FapForm) actionForm;
		
		// for errors
		Collection<GenericException> errors = 
			new ArrayList<GenericException>();
		
		// for warnings
		Collection<GenericException> warnings = 
			new ArrayList<GenericException>();
		
		// set the results id as filter fund IDs.
		fapForm.setFilterResultsId(FapConstants.FILTER_FUND_IDS);
		
		// get the selected advance filter option
		String selectedFilterOption = 
			fapForm.getSelectedAdvanceFilterOption();
		
		List<String> fundIds = null;
		
		// 1. ASSET CLASS option - validate whether the user has made a 
		// selection from the asset class list box.
		if (FapConstants.ASSET_CLASS_FILTER_KEY.equals(selectedFilterOption)) {
			
			if (fapForm.getAssetClassQuerySelect() == null) {
				errors.add(new GenericException(
						CommonErrorCodes.VIEW_RESULTS_WITHOUT_SELECTION));
			} else {
				
				// create the filter criteria based on the filter option 
				// selected
				FandpFilterCriteria filterCriteria = 
					FapFilterUtility.createFandpFilterCriteria(fapForm, 
							selectedFilterOption, request);
				
				if (!fapForm.isShowNML()) {
						filterCriteria.setIncludeNMLFunds(
								includeNMLFunds(request));
			    }
				
				filterCriteria.setIncludeOnlyMerrillCoveredFunds(includeOnlyMerrillCoveredFunds(request, filterCriteria.getContractNumber()));
				
				filterCriteria.setSelectedAssetOrRiskValues(
						FapFilterUtility.convertStringToSet(
								fapForm.getSelectedAssetOrRiskValues()));
				
				// Set the sort option selected by the user in the 
				// filter criteria object
				filterCriteria.setSloshBoxSortOption(
						fapForm.getSortPreferenceSelect());
				
				// Get the list of Fund IDs from the cache, using FundService
				fundIds = 
					FundServiceDelegate.getInstance().getFundIdsForAdvanceFilter(filterCriteria);
				
				// set the filtered Fund IDs in the form
				fapForm.setFilteredFundIds(fundIds);
			}

		
		// 2. RISK CATEGORY option - validate whether the user has made a 
		// selection from the Risk category list box.
		} else if (FapConstants.RISK_CATEGORY_FILTER_KEY.equals(
				selectedFilterOption)) {
			
			if (fapForm.getRiskCategoryQuerySelect() == null) {
				errors.add(new GenericException(
						CommonErrorCodes.VIEW_RESULTS_WITHOUT_SELECTION));
			} else {
				// create the filter criteria based on the filter option selected
				FandpFilterCriteria filterCriteria = 
					FapFilterUtility.createFandpFilterCriteria(fapForm, 
							selectedFilterOption, request);
				
				if (!fapForm.isShowNML()) {
					filterCriteria.setIncludeNMLFunds(
							includeNMLFunds(request));
				}
				
				filterCriteria.setIncludeOnlyMerrillCoveredFunds(includeOnlyMerrillCoveredFunds(request, filterCriteria.getContractNumber()));
				
				filterCriteria.setSelectedAssetOrRiskValues(
						FapFilterUtility.convertStringToSet(
								fapForm.getSelectedAssetOrRiskValues()));
				
				// Set the sort option selected by the user in the 
				// filter criteria object
				filterCriteria.setSloshBoxSortOption(
						fapForm.getSortPreferenceSelect());
				
				// Get the list of Fund IDs from the cache, using FundService
				fundIds = 
					FundServiceDelegate.getInstance().getFundIdsForAdvanceFilter(filterCriteria);
				
				// set the filtered Fund IDs in the form
				fapForm.setFilteredFundIds(fundIds);
			}

		
		// 3. SHORTLIST option - validate whether the user has made a 
		// selection from all 4 (a)Fund Menu, (b)ShortList Type, 
		// (c)Asset Allocation Group and (d)conservative Fund drop-downs.
		} else if (FapConstants.SHORTLIST_FILTER_KEY.equals(
				selectedFilterOption)) {
			
			if (!fapForm.isAllShortlistOptionSelected()) {
				errors.add(new GenericException(
						CommonErrorCodes.ALL_SHORTLIST_OPTIONS_NOT_SELECTED));
			} else {
				// create the filter criteria based on the filter option 
				// selected
				FandpFilterCriteria filterCriteria = 
					FapFilterUtility.createFandpFilterCriteria(fapForm, 
							selectedFilterOption, request);
				
				if (!fapForm.isShowNML()) {
					filterCriteria.setIncludeNMLFunds(
							includeNMLFunds(request));
				}
				
				filterCriteria.setIncludeOnlyMerrillCoveredFunds(includeOnlyMerrillCoveredFunds(request, filterCriteria.getContractNumber()));
				
				// Set the sort option selected by the user in the 
				// filter criteria object
				filterCriteria.setSloshBoxSortOption(
						fapForm.getSortPreferenceSelect());
				
				filterCriteria.setFundShortListOptions(
						FapFilterUtility.createShortListParameters(request, 
								fapForm));
				
				// Get the list of Fund IDs from the cache, using FundService
				fundIds = 
					FundServiceDelegate.getInstance().getFundIdsForAdvanceFilter(filterCriteria);
				
				// set the filtered Fund IDs in the form
				fapForm.setFilteredFundIds(fundIds);
			}
		
		
		// 4. SEARCH FOR A FUND option - validates whether the user has entered
		// a valid data to perform a search by Fund Name
		} else if (FapConstants.SEARCH_FUND_FILTER_KEY.equals(
				selectedFilterOption)) {
			
			if (fapForm.getFundNameSearchText() == null || 
					fapForm.getFundNameSearchText().trim().length() == 0){
				errors.add(new GenericException(
						CommonErrorCodes.FUND_NAME_NOT_ENTERED));
			} else if (fapForm.getFundNameSearchText().trim().length() < 3) {
				errors.add(new GenericException(
						CommonErrorCodes.FUND_NAME_LESS_THAN_MINIMUM_REQUIRED_CHARS));
			} else { 
				
				// create the filter criteria based on the filter option selected
				FandpFilterCriteria filterCriteria = 
					FapFilterUtility.createFandpFilterCriteria(fapForm, 
							selectedFilterOption, request);
				
				filterCriteria.setFundNameSearchText(
						fapForm.getFundNameSearchText());
				
				if (!fapForm.isShowNML()) {
					filterCriteria.setIncludeNMLFunds(
							includeNMLFunds(request));
				}
				
				filterCriteria.setIncludeOnlyMerrillCoveredFunds(includeOnlyMerrillCoveredFunds(request, filterCriteria.getContractNumber()));
				
				// Set the sort option selected by the user in the filter criteria object
				filterCriteria.setSloshBoxSortOption(fapForm.getSortPreferenceSelect());
				
				// Get the list of Fund IDs from the cache, using FundService
				fundIds = 
					FundServiceDelegate.getInstance().getFundIdsForAdvanceFilter(filterCriteria);
				
				// set the filtered Fund IDs in the form
				fapForm.setFilteredFundIds(fundIds);
			}
			
			
		// 5. MY CUSTOM QUERIES option - validates whether the user has made a 
		// selection.
		} else if (FapConstants.SAVED_CUSTOM_QUERIES_FILTER_KEY.equals(selectedFilterOption)) {
			
			if (StringUtils.isBlank(fapForm.getMySavedQueriesQuerySelect()) ||
					StringUtils.equals(fapForm.getMySavedQueriesQuerySelect(), FapConstants.NOT_APPLICABLE)){
				
				errors.add(new GenericException(
						FapFilterUtility.getErroCodeForSavedDataFilter(fapForm)));
				
			} else {
				
				return doCustomQueryEvents( actionForm, request, response);
			}
			
			
		// 6. MY SAVED LISTS option - validates whether the user has made a 
		// selection.
		} else if (FapConstants.SAVED_LIST_FILTER_KEY.equals(selectedFilterOption)) {
				
			if (StringUtils.isBlank(fapForm.getMySavedListsQuerySelect()) ||
					StringUtils.equals(fapForm.getMySavedListsQuerySelect(), FapConstants.NOT_APPLICABLE)){
				
				errors.add(new GenericException(
						FapFilterUtility.getErroCodeForSavedDataFilter(fapForm)));
				
			} else {
				
				return doSaveListEvents( actionForm, request, response);
			}
				
		// 7. All the other option which doesn't need user validation are 
		// handled here
		} else {
			
			// create the filter criteria based on the filter option selected
			FandpFilterCriteria filterCriteria = 
				FapFilterUtility.createFandpFilterCriteria(fapForm, 
						selectedFilterOption, request);
			
			if (!fapForm.isShowNML()) {
				filterCriteria.setIncludeNMLFunds(
						includeNMLFunds(request));
			}
			
			filterCriteria.setIncludeOnlyMerrillCoveredFunds(includeOnlyMerrillCoveredFunds(request, filterCriteria.getContractNumber()));
			
			// Set the sort option selected by the user in the 
			// filter criteria object
			filterCriteria.setSloshBoxSortOption(
					fapForm.getSortPreferenceSelect());
			
			// Get the list of Fund IDs from the cache, using FundService
			fundIds = 
				FundServiceDelegate.getInstance().getFundIdsForAdvanceFilter(filterCriteria);
			
			// set the filtered Fund IDs in the form
			fapForm.setFilteredFundIds(fundIds);
		}
		
		if (fundIds == null || fundIds.isEmpty()) {
			// If user saves a query with the same name
			try {
				String alertMessage = ContentHelper.getMessage(
						new GenericExceptionWithContentType(
								CommonContentConstants.NO_MATCHING_RESULTS_FROM_QUERY,
								ContentTypeManager.instance().MISCELLANEOUS));
					
				fapForm.setAlertMessage(StringEscapeUtils.escapeJava(
						StringEscapeUtils.escapeHtml(alertMessage)));
				
			} catch (ContentException e) {
				throw new SystemException("Not able to get content for id " + 
						CommonContentConstants.LIST_NAME_ALREADY_EXISTS);
			}
			
			fapForm.setFilterResultsId("showAlert");
		}
	
		// If there are any errors set them in the request and set the 
		// messageExist attribute as TRUE in the form-bean
		if (!errors.isEmpty()) {
			setErrorsInRequest(request, errors);
			fapForm.setMessagesExist(true);
			fapForm.setFilterResultsId(null);
		}
		
		// If there are any WARNINGs set them in the request and set the 
		// warningsExist attribute as TRUE in the form-bean
		if (!warnings.isEmpty()) {
			request.setAttribute(FapConstants.BDW_INFORMATION_MESSAGES, warnings);
			fapForm.setWarningsExist(true);
		}
		
		//return mapping.findForward(FapConstants.FORWARD_CONTINUE);
		return FapConstants.FORWARD_CONTINUE;
	}
	
	/**
	 * Validates the user entered Saved List Name. If the validation
	 * is success, saves the fundList in the database
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return actionForward
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	public String doSaveFundList(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		
		FapForm fapForm = (FapForm) actionForm;
		
		if (validateSaveListName(fapForm, request)) {
			
		}
		//return mapping.findForward(FapConstants.FORWARD_CONTINUE);
		return FapConstants.FORWARD_CONTINUE;
	}
		
	/**
	 * Validate's the query name
	 * 
	 * @param fapForm
	 * @param request
	 * @throws SystemException 
	 */
	private boolean validateSaveListName(
			FapForm fapForm, 
			HttpServletRequest request) throws SystemException {
		
		boolean hasErrors = false;
		
		String listName = fapForm.getSaveListName();
		Collection<GenericException> errorMessages = 
			new ArrayList<GenericException>();
		
		if (StringUtils.isBlank(listName)) {
			// If the name is empty
			errorMessages.add(new GenericException(
					CommonErrorCodes.SAVE_LIST_WITHOUT_NAME));
			
		} else if (FapConstants.SAVE_NAME_PATTERN.matcher(
				listName).replaceAll(StringUtils.EMPTY).length() > 0){
			
			// if the name contains chars other than alphabets, numbers,
			// blank, dashes and dots
			errorMessages.add(new GenericException(
					CommonErrorCodes.SAVE_LIST_NAME_INVALID));
		}
		
		// If there are error messages, place the collection in the request
		if (!errorMessages.isEmpty()) {
			setErrorsInRequest(request, errorMessages);
			fapForm.setMessagesExist(true);
			fapForm.setFilterResultsId(null);
			hasErrors = true;
		} else {
			
			// look for warnings
			// if the name entered already exists.
			String duplicateInd = 
				FandpServiceDelegate.getInstance().insertUserData(
						getProfileId(request), 
						FapFilterUtility.createUserSavedData(
								fapForm, 
								FapConstants.TYPE_SAVED_FUND_LIST),
						fapForm.isOverwriteExisting());
			
			if (StringUtils.isBlank(duplicateInd) || 
					StringUtils.equals(duplicateInd, FapConstants.YES)) {
				// If user saves a query with the same name
				try {
					String alertMessage = ContentHelper.getMessage(
							new GenericExceptionWithContentType(
									CommonContentConstants.LIST_NAME_ALREADY_EXISTS, 
									ContentTypeManager.instance().MISCELLANEOUS));
						
					fapForm.setAlertMessage(StringEscapeUtils.escapeJava(
							StringEscapeUtils.escapeHtml(alertMessage)));
					
				} catch (ContentException e) {
					throw new SystemException("Not able to get content for id " + 
							CommonContentConstants.LIST_NAME_ALREADY_EXISTS);
				}
				
				fapForm.setFilterResultsId("showConfirmation");
			} 
		}
		
		return hasErrors;
	}	
	
	/**
	 * Handles the operations for the Saved List options like
	 * 		1. Delete a saved fund list
	 * 		2. Retrieve a saved Fund List
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return forward
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	protected String doSaveListEvents(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		
		FapForm fapForm = (FapForm) actionForm;

		String selectedListName = fapForm.getMySavedListsQuerySelect();
		long profileId = getProfileId(request);
		
		if (StringUtils.equals(fapForm.getEventTriggered(), 
				FapConstants.DELETE_LIST)) {
			
			FandpServiceDelegate.getInstance().deleteUserSavedDataLists(
					profileId, selectedListName, 
					FapConstants.TYPE_SAVED_FUND_LIST);	
			
			return doInnerFilterOption( actionForm, request, response);
			
		} else if (StringUtils.equals(fapForm.getEventTriggered(), 
				FapConstants.DISPLAY_LIST)) {
			
			List<String> retrievedFundList = 
				FandpServiceDelegate.getInstance().retrieveFundListSavedDataByName(
						profileId, FapConstants.TYPE_SAVED_FUND_LIST, selectedListName);
			
			// set the results id as filter fund IDs.
			fapForm.setFilterResultsId(FapConstants.FILTER_FUND_IDS);
			
			// set the filtered Fund IDs in the form
			fapForm.setFilteredFundIds(retrievedFundList);
		}
		
		//return mapping.findForward(FapConstants.FORWARD_CONTINUE);
		return FapConstants.FORWARD_CONTINUE;
	}
	/**
	 * Performs sorting for the Funds present in the Query Results Slosh box
	 * and Selected Funds Slosh box of the Advance Filter section.
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return actionForward
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	public String doSortForSloshBoxes( 
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		
		FapForm fapForm = (FapForm) actionForm;
		
		// create the filter criteria and 
		FandpFilterCriteria filterCriteria = 
			FapFilterUtility.createFandpFilterCriteria(fapForm, null, request);
		
		// Set the sort option selected by the user in the filter criteria object
		filterCriteria.setSloshBoxSortOption(fapForm.getSortPreferenceSelect());
		
		// Sort the Query Results Fund IDs and assign it back to the form
		fapForm.setSortedQueryResultsValues(
				sortFundIds(fapForm.getQueryResultsValues(), filterCriteria));
		
		// Sort the Selected Fund IDs and assign it back to the form		
		fapForm.setSortedSelectedFundsValues(
				sortFundIds(fapForm.getSelectedFundsValues(), filterCriteria));
		
		if (!fapForm.isShowNML()) {
				filterCriteria.setIncludeNMLFunds(includeNMLFunds(request));
		}
		
		filterCriteria.setIncludeOnlyMerrillCoveredFunds(includeOnlyMerrillCoveredFunds(request, filterCriteria.getContractNumber()));
		
		fapForm.setFilterResultsId(FapConstants.SLOSH_BOX_SORTING_RESULTS);
		
		//return mapping.findForward(FapConstants.FORWARD_CONTINUE);
		return FapConstants.FORWARD_CONTINUE;
	}
	
	/**
	 * Fetches Asset class definitions to display in pop up window
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return actionForward
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	public String doDisplayAssetClass( 
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		
		FapForm fapForm = (FapForm) actionForm;
				//TODO-Need to convert labelvaluebean which has been reffered to struts package
		// Fetches Asset Class definitions and assigns it to the form property
		// GIFL P3c 
		List<LabelValueBean> assetClassList = FapFilterUtility.getAssetClassList(true);
		List<LabelValueBean> assetClassListForDefinition = new ArrayList<LabelValueBean>();
		for (LabelValueBean labelValueBean : assetClassList) {	
			if(!"LSG".equals(labelValueBean.getValue())) {
				//GIFL asset class should not be displayed in pop up
				assetClassListForDefinition.add(labelValueBean);
			}
		}
		fapForm.setAssetClassList(assetClassListForDefinition);		
		
		//return mapping.findForward(FapConstants.FORWARD_ASSET_CLASS_DEFINITIONS);
		return FapConstants.FORWARD_ASSET_CLASS_DEFINITIONS;
	}
	
	/**
	 * Performs sorting for the Funds present in the Query Results Slosh box
	 * and Selected Funds Slosh box of the Advance Filter section.
	 * 
	 * @param fundIdsAsString
	 * @param filterCriteria
	 * @return sorted Fund IDs separated by "|"
	 * @throws SystemException
	 */
	private List<String> sortFundIds (String fundIdsAsString, 
			FandpFilterCriteria filterCriteria) throws SystemException {

		// Convert the Fund IDs String to a Set
		Set<String> queryResultsFundIds = FapFilterUtility.convertStringToSet(fundIdsAsString);
		
		// place the Fund IDs Set object to the FandpFilterCriteria object
		filterCriteria.setFundIds(queryResultsFundIds);
		
		// Get the Fund IDs in grouped order and also the grouped funds are 
		// sorted based on the Marketing sort order
		return FundServiceDelegate.getInstance().getFundIdsForAdvanceFilter(
				filterCriteria);
	}
	
	public String doFilterFundScorecardMetrics(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {

		FapForm fapForm = (FapForm) actionForm;

		if (!fapForm.isDisplayOnlyHeaders()) {
			// Retrieve the data from the session
			LinkedHashMap<String, List<? extends FundBaseInformation>> currentTabValueObject =
		        	(LinkedHashMap<String, List<? extends FundBaseInformation>>)
		        	getTabValueObject(request, fapForm, FapConstants.FUNDSCORECARD_TAB_ID, false);
			
			// do default sorting
			sortFunds(null, currentTabValueObject);
	
			// Set the default tab object to the form-bean
	    	request.setAttribute(FapConstants.VO_CURRENT_TAB, currentTabValueObject);
		}
		
		FundScoreCardMetricsSelection fundScoreCardMetricsSelection = new FundScoreCardMetricsSelection(
				fapForm.isShowMorningstarScorecardMetrics(),
				fapForm.isShowFi360ScorecardMetrics(),
				fapForm.isShowRpagScorecardMetrics());

		fapForm.setColumnsInfo(FapTabUtility
				.createFundScorecardTabColumns(fundScoreCardMetricsSelection));

		request.getSession().setAttribute(
				FapConstants.VO_FUND_SCORECARD_SELECTION,
				fundScoreCardMetricsSelection);

		//return mapping.findForward(FapConstants.FUNDSCORECARD_TAB_ID);
		return FapConstants.FUNDSCORECARD_TAB_ID;
	}
	
	/**
	 * Handles the operations like
	 * 		1. Delete a saved custom query
	 * 		2. Edit a saved or unSaved custom query
	 * 		3. Save a custom query
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return forward
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	public abstract String doCustomQueryEvents(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException;
	
	/**
	 * Validates whether the user is allowed to view the saved data
	 * 
	 * @param request
	 * @return true if the user has rights to view the saved data
	 */
	protected abstract boolean userAllowedToViewSavedData(HttpServletRequest request);
	
	/**
	 * Returns the profileId of the currently logged in user
	 * 
	 * @param request
	 * @return profile ID
	 */
	protected abstract long getProfileId(HttpServletRequest request);
	
	/**
	 * Decide whether GIFL category should be displayed or not.
	 *	
	 * @param fapForm
	 * @param request
	 * @return boolean
	 * @throws SystemException
	 */
	protected abstract boolean includeGIFL(FapForm fapForm, 
			HttpServletRequest request)throws SystemException;
	
}