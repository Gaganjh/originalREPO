package com.manulife.pension.platform.web.fap.customquery;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.content.CommonContentConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.delegate.FandpServiceDelegate;
import com.manulife.pension.platform.web.fap.FapForm;
import com.manulife.pension.platform.web.fap.FapFilterController;
import com.manulife.pension.platform.web.fap.constants.FapConstants;
import com.manulife.pension.platform.web.fap.customquery.util.CustomQueryField;
import com.manulife.pension.platform.web.fap.customquery.util.CustomQueryUtility;
import com.manulife.pension.platform.web.fap.util.FapFilterUtility;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.exception.GenericExceptionWithContentType;
import com.manulife.pension.ps.service.report.fandp.valueobject.CustomQueryRow;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.fandp.valueobject.FandpFilterCriteria;
import com.manulife.pension.util.content.GenericException;

/**
 * Action class to handle the Custom Query stuffs. 
 * 		1. It loads the options for the Logic, Field Selection and Value.
 * 		2. Validates the user data and creates the Custom Where Clause.
 * 		3. Validates the query name and saves the data to the database
 * 
 * @author ayyalsa
 *
 */
public abstract class CustomQueryController extends FapFilterController {

	/**
	 * Loads the basic data require to enable the custom query.
	 * Creates the minimum number of rows and loads the options that 
	 * will be populated to the Logical, Field and Operator drop-down.
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * 
	 * @return ActionForward
	 * 
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	public String doEnableCustomQuery(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, 
			SystemException {
		
		FapForm fapForm = (FapForm) actionForm;
	
		// there is no need of any results id, just set it null.
		// no matter if it is already null
		fapForm.setFilterResultsId(null);
		
		// Set the custom query options for the Logic, Fields 
		// and operator drop-down
		fapForm.setCustomQueryOptionList(
				CustomQueryUtility.createCustomQueryOptions());
		
		// creates the minimum number of rows to display
		fapForm.setCustomQueryCriteriaList(
				CustomQueryUtility.createMinimumNumberOfQueryCriteriaRows());
		
		// clear the save name
		fapForm.setSaveQueryName(FapConstants.BLANK);
		
		// forward to custom query
		//return mapping.findForward(FapConstants.FORWARD_CUSTOM_QUERY);
		return FapConstants.FORWARD_CUSTOM_QUERY;
	}
	
	/**
	 * To create additional conditions for the custom query filter 
	 * and also to remove the conditions. 
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * 
	 * @return ActionForward
	 * 
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	public String doInsertOrRemoveRows(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			SystemException {
		
		FapForm fapForm = (FapForm) actionForm;
		
		int criteriaListSize = fapForm.getCustomQueryCriteriaList().size();
		
		/*
		 * get the row index from where the event is triggered. 
		 * the row index will be in the below format
		 * 		--	"insert<rowNumber>"
		 *		--	"insertAtLast
		 *		--	"remove<rowNumber>"
		 */
		String rowIndex = fapForm.getCustomQueryRowIndex();
		
		// If the index starts with "insert" then the user 
		// has clicked the "+" button
		if (rowIndex.startsWith(FapConstants.INSERT_KEYWORD)){
			
			// the user is not allowed to add more than 25 rows.
			if (criteriaListSize < 25) {
				// get the row number
				rowIndex = StringUtils.remove(
						rowIndex, FapConstants.INSERT_KEYWORD);
				
				// If the insert is triggered from the last "+" button,
				if (FapConstants.AT_LAST_KEYWORD.equals(rowIndex)) {
				
					rowIndex = Integer.toString(
							fapForm.getCustomQueryCriteriaList().size());
				}
	
				// Add an empty object to the ArrayList
				fapForm.getCustomQueryCriteriaList().add(
						NumberUtils.toInt(rowIndex), new CustomQueryRow());
			} else {
				// A Collection to add the error messages
				Collection<GenericException> errorMessages = 
					new ArrayList<GenericException>();
				
				errorMessages.add(new GenericException(CommonErrorCodes.CUSTOM_QUERY_EXCEEDS_25_LINES_LIMIT));
				// If there are error messages, place the collection in the request
				if (!errorMessages.isEmpty()) {
					setErrorsInRequest(request, errorMessages);
					fapForm.setMessagesExist(true);
					fapForm.setFilterResultsId(null);
					
					//return mapping.findForward(FapConstants.FORWARD_CONTINUE);
					return FapConstants.FORWARD_CONTINUE;
				}
			}
		} else {

			// get the row number
			rowIndex = StringUtils.remove(rowIndex, FapConstants.REMOVE_KEYWORD);
			
			// Remove the object from specified index of the ArrayList
			fapForm.getCustomQueryCriteriaList().remove(
					NumberUtils.toInt(rowIndex));
			
			// The remove operation will be performed, only when there are 
			// more than 3 rows
			if (fapForm.getCustomQueryCriteriaList().size() < 3){

				// Add an empty object to the ArrayList
				fapForm.getCustomQueryCriteriaList().add(new CustomQueryRow());
			}
		}
		
		// Create the options list
		// form-bean is in request scope. so, always need to create the options
		fapForm.setCustomQueryOptionList(
				CustomQueryUtility.createCustomQueryOptions());
		setDataType(fapForm);
		
		// set the asset class, risk category, FundCheck evaluation and 
		// Overall star rating list to the actionForm 
		fapForm.setAssetClassList(
				FapFilterUtility
				.getAssetClassListForCQ(includeGIFL(fapForm, request)));
		fapForm.setRiskCategoryList(
				FapFilterUtility
				.getRiskCategoryListForCQ(includeGIFL(fapForm, request)));
		fapForm.setMorningStarRatingEvaluationList(CustomQueryUtility
				.createMorningStarEvaluationValues());
		fapForm.setFi360EvaluationList(CustomQueryUtility
				.createFi360EvaluationValues());
		fapForm.setRpagEvaluationList(CustomQueryUtility
				.createRPAGEvaluationValues());
		fapForm.setOverallstarRatingList(
				CustomQueryUtility.createOverallStarRatingValues());
		//return mapping.findForward(FapConstants.FORWARD_CUSTOM_QUERY);
		return FapConstants.FORWARD_CUSTOM_QUERY;
	}
	
	/**
	 * Toggles the Value drop-down and value text-box based on the 
	 * user's field selection. If the user selects the Asset Class, then 
	 * the drop-down will have the list of Asset Classes. If the user selects 
	 * the Risk/return Category, then the drop-down will have the 
	 * list of Risk categories.
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * 
	 * @return ActionForward
	 * 
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	public String doToggleValue(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			SystemException {
		
		FapForm fapForm = (FapForm) actionForm;
		
		//populate the options for the custom query
		fapForm.setCustomQueryOptionList(
				CustomQueryUtility.createCustomQueryOptions());
		
		setDataType(fapForm);
		
		/*
		 * sets the values for the selected field option.
		 * If the field option is selected as "Investment Category', then	
		 * the value text-box should be replaced a drop-down which holds the 
		 * List of Investment Categories. Similarly for Asset Class and fund 
		 * evaluation options
		 */
		fapForm.setAssetClassList(
				FapFilterUtility
				.getAssetClassListForCQ(includeGIFL(fapForm, request)));
		fapForm.setRiskCategoryList(
				FapFilterUtility
				.getRiskCategoryListForCQ(includeGIFL(fapForm, request)));
		fapForm.setMorningStarRatingEvaluationList(CustomQueryUtility
				.createMorningStarEvaluationValues());
		fapForm.setFi360EvaluationList(CustomQueryUtility
				.createFi360EvaluationValues());
		fapForm.setRpagEvaluationList(CustomQueryUtility
				.createRPAGEvaluationValues());
		fapForm.setOverallstarRatingList(
				CustomQueryUtility.createOverallStarRatingValues());
		//return mapping.findForward(FapConstants.FORWARD_CUSTOM_QUERY);
		return FapConstants.FORWARD_CUSTOM_QUERY;
	}
	
	/**
	 * Validate the custom Query Name and update the values to the 
	 * database.
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * 
	 * @return actionForward
	 * 
	 * @throws IOException
	 * @throws SystemException
	 */
	public String doSaveCustomQuery(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, 
			SystemException {
		
		FapForm fapForm = (FapForm) actionForm;
		
		// Validate for errors / warnings
		validateCustomQueryName(fapForm, request);
		
		//return mapping.findForward(FapConstants.FORWARD_CONTINUE);
		return FapConstants.FORWARD_CONTINUE;
	}
	
	/**
	 * Validate the custom query values and create the custom Where clause,
	 * trigger call to the database and get the Fund Id List
	 *  
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * 
	 * @return actionForward
	 * 
	 * @throws IOException
	 * @throws SystemException
	 */
	public String doCustomQueryFilter(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			SystemException {
		
		FapForm fapForm = (FapForm) actionForm;
		
		// Validate he user inputs / selections, if there are any errors,
		// return  the forward
		if (validateCustomQuery(fapForm, request)) {
			//return mapping.findForward(FapConstants.FORWARD_CONTINUE);
			return FapConstants.FORWARD_CONTINUE;
		}
		
		// this is for the edit criteria
		setAttributesInSession(request, "customQueryFilters", 
				fapForm.getCustomQueryCriteriaList());
		
		List<String> fundIdList = new ArrayList<String>(); 
		
		// create the where clause
		String customWhereClause = FapConstants.BLANK;
		try {
			customWhereClause = CustomQueryUtility.buildCustomWhereClause(
					fapForm.getCustomQueryCriteriaList());
		} catch (ParseException e) {
			throw new SystemException(
					"Error while building WHERE clause" + e.getMessage());
		}
		
		if (!StringUtils.isBlank(customWhereClause)) {
			// create the FandpFilterCriteria object, which would be used at 
			// the back-end
			FandpFilterCriteria filterCriteria =
				FapFilterUtility.createFandpFilterCriteria(
						fapForm, 
						fapForm.getSelectedAdvanceFilterOption(), 
						request);
			
			// set the create where clause to the filter object 
			filterCriteria.setCustomWhereClause(customWhereClause);
	
			// Set the sort option selected by the user in the 
			// filter criteria object
			filterCriteria.setSloshBoxSortOption(
					fapForm.getSortPreferenceSelect());
			
			//set to include Merrill Covered Funds
			filterCriteria.setIncludeOnlyMerrillCoveredFunds(includeOnlyMerrillCoveredFunds(request, filterCriteria.getContractNumber()));
			
			try {

				if (filterCriteria.getContractNumber() != 0) {

					// make a call to and get the Fund IDs list
					fundIdList = FundServiceDelegate.getInstance()
							.executeCustomQueryByContract(filterCriteria);

				} else {

					if (!fapForm.isShowNML()) {
						filterCriteria
								.setIncludeNMLFunds(includeNMLFunds(request));
					}

					// make a call to and get the Fund IDs list

					fundIdList = FundServiceDelegate.getInstance()
							.executeCustomQuery(filterCriteria);

				}
			} catch (Exception e) {
				logger.error("Failed executing custom query. Details : " + e);
				Collection<GenericException> errorMessages = new ArrayList<GenericException>();
				errorMessages.add(new GenericException(
						CommonErrorCodes.CUSTOM_QUERY_EXECUTION_FAILED));
				setErrorsInRequest(request, errorMessages);
				fapForm.setMessagesExist(true);
				fapForm.setFilterResultsId(null);
				//return mapping.findForward(FapConstants.FORWARD_CONTINUE);
				return FapConstants.FORWARD_CONTINUE;
			}
		}
		
		if (fundIdList != null && !fundIdList.isEmpty()) {
			// set the filtered Fund IDs in the form
			fapForm.setFilteredFundIds(fundIdList);
			// set the results id as filter fund IDs.
			fapForm.setFilterResultsId(FapConstants.FILTER_FUND_IDS);
		} else {
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
		//return mapping.findForward(FapConstants.FORWARD_CONTINUE);
		return FapConstants.FORWARD_CONTINUE;
	}
	
	/* (non-Javadoc)
	 * @see com.manulife.pension.platform.web.fap.FapFilterAction#
	 * doCustomQueryEvents( 
	 * 		org.apache.struts.action.ActionMapping, 
	 * 		com.manulife.pension.platform.web.controller.AutoForm, 
	 * 		javax.servlet.http.HttpServletRequest, 
	 * 		javax.servlet.http.HttpServletResponse)
	 */
	public String doCustomQueryEvents(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		
		FapForm fapForm = (FapForm) actionForm;

		String selectedQueryName = fapForm.getMySavedQueriesQuerySelect();
		long profileId = getProfileId(request);
		
		if (StringUtils.equals(fapForm.getEventTriggered(), 
				FapConstants.DELETE_QUERY)) {
			
			FandpServiceDelegate.getInstance().deleteUserSavedDataLists(
					profileId, selectedQueryName, 
					FapConstants.TYPE_SAVED_CUSTOM_QUERY);	
			
			return doInnerFilterOption( actionForm, request, response);
			
		} else {
			
			List<CustomQueryRow> customQueryRowList = 
				FandpServiceDelegate.getInstance().retrieveCustomQuerySavedDataByName(
						profileId, FapConstants.TYPE_SAVED_CUSTOM_QUERY, selectedQueryName);
			
			if (customQueryRowList != null) {
				fapForm.setCustomQueryCriteriaList(customQueryRowList);
			} else {
				fapForm.setCustomQueryCriteriaList(
						CustomQueryUtility.createMinimumNumberOfQueryCriteriaRows());
			}
			
			if (StringUtils.equals(fapForm.getEventTriggered(), 
					FapConstants.DISPLAY_QUERY_RESULTS)) {
				
				return doCustomQueryFilter( actionForm, request, response);
				
			} else if (StringUtils.equals(fapForm.getEventTriggered(), 
					FapConstants.EDIT_CRITERIA)) {
		
				fapForm.setSaveQueryName(selectedQueryName);
				return doEditCriteria( actionForm, request, response);
			}
		}
		
		//return mapping.findForward(FapConstants.FORWARD_CONTINUE);
		return FapConstants.FORWARD_CONTINUE;
	}

	/**
	 * Validate the custom Query Name and update the values to the 
	 * database.
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * 
	 * @return actionForward
	 * 
	 * @throws IOException
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public String doEditCriteria(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, 
			SystemException {
		
		FapForm fapForm = (FapForm) actionForm;
		
		/*
		 * the previously executed custom query will be in session. 
		 * If the user clicks on the 'Edit Criteria' from the 
		 * 'My Saved Queries', then this would be null and the values
		 * will be retrieved from the database. 
		 */ 
		List<CustomQueryRow> customQueryCriteriaList = 
				(List<CustomQueryRow>) request.getSession().getAttribute(
						"customQueryFilters");
		
		if (customQueryCriteriaList != null) {
			fapForm.setCustomQueryCriteriaList(customQueryCriteriaList);
			request.getSession().removeAttribute("customQueryFilters");
		}
		
		// creates the minimum number of rows to display
		fapForm.setCustomQueryOptionList(
				CustomQueryUtility.createCustomQueryOptions());
		
		setDataType(fapForm);
		
		/*
		 * sets the values for the selected field option.
		 * If the field option is selected as "Investment Category', then	
		 * the value text-box should be replaced a drop-down which holds the 
		 * List of Investment Categories. Similarly for Asset Class and fund 
		 * evaluation options
		 */
		fapForm.setAssetClassList(
				FapFilterUtility
				.getAssetClassListForCQ(includeGIFL(fapForm, request)));
		fapForm.setRiskCategoryList(
				FapFilterUtility
				.getRiskCategoryListForCQ(includeGIFL(fapForm, request)));
		fapForm.setMorningStarRatingEvaluationList(
				CustomQueryUtility.createMorningStarEvaluationValues());
		fapForm.setFi360EvaluationList(
				CustomQueryUtility.createFi360EvaluationValues());
		fapForm.setRpagEvaluationList(
				CustomQueryUtility.createRPAGEvaluationValues());
		fapForm.setOverallstarRatingList(
				CustomQueryUtility.createOverallStarRatingValues());
		//return mapping.findForward(FapConstants.FORWARD_CUSTOM_QUERY);
		return FapConstants.FORWARD_CUSTOM_QUERY;
	}
	
	/**
	 * Validates the query values. Creates a collection of error messages, 
	 * if any and sets in the request. 
	 * 
	 * Returns TRUE, if there are errors and FALSE when there are no errors.
	 *  
	 * @param fapForm
	 * @param request	
	 * 
	 * @return true if there are any validation errors
	 */
	private boolean validateCustomQuery(
			FapForm fapForm, HttpServletRequest request) {
	
		if (CustomQueryUtility.fieldsMap == null) {
			CustomQueryUtility.createCustomQueryOptions();
		}
		
		// A Collection to add the error messages
		Collection<GenericException> errorMessages = 
			new ArrayList<GenericException>();
		
		// get the query criteria List and remove the blank/empty rows
		List<CustomQueryRow> criteriaList = 
			fapForm.getCustomQueryCriteriaList();
		//removeBlankCriteriaRows (criteriaList);
		
		if (criteriaList.size() > 0) {
			// holds the current row index
			validateMandatoryFields(criteriaList, errorMessages);
			validateParenthesis(criteriaList, errorMessages);
		}
		
		// If there are error messages, place the collection in the request
		if (!errorMessages.isEmpty()) {
			setErrorsInRequest(request, errorMessages);
			fapForm.setMessagesExist(true);
			fapForm.setFilterResultsId(null);
			
			// return true if there are error messages
			return true;
		}
		
		// no error messages
		return false;
	}
	
	/**
	 * Iterates through the Criteria list and validates whether the user has 
	 * entered on the all the Mandatory options
	 * 
	 * @param criteriaList
	 * @param errorMessages
	 */
	private void validateMandatoryFields(List<CustomQueryRow> criteriaList, 
			Collection<GenericException> errorMessages) {
		
		int rowIndex;
		String missingFields = FapConstants.BLANK;
		String[] params;
		
		// Iterate through the list 
		Iterator<CustomQueryRow> iterator = criteriaList.iterator();
		while(iterator.hasNext()) {
			CustomQueryRow queryRow = iterator.next();
			rowIndex = criteriaList.indexOf(queryRow);
			
			if (!queryRow.isEmptyCriteria()) {
				missingFields = queryRow.getMissingFields(rowIndex);
		
				if (StringUtils.isNotBlank(missingFields)) {
					params = new String[] {
							missingFields, String.valueOf(rowIndex + 1)};
					
					errorMessages.add(new GenericException(
							CommonErrorCodes.CUSTOM_QUERY_MISSING_REQUIRED_ENTRIES, 
							params));
				}
				
				validateEnteredValue(queryRow, String.valueOf(rowIndex + 1), 
						errorMessages);
			}
		}
	}
	
	/**
	 * validates whether the entered VALUE, matches the data type
	 * 
	 * @param queryRow
	 * @param rowIndex
	 * @param errorMessages
	 */
	private void validateEnteredValue(
			CustomQueryRow queryRow, 
			String rowIndex, 
			Collection<GenericException> errorMessages){
	
		String fieldId = String.valueOf(queryRow.getFieldId());
		CustomQueryField customQueryField = (CustomQueryField) 
						CustomQueryUtility.fieldsMap.get(fieldId);
		
		if (customQueryField != null) {
			String dataType = customQueryField.getBasicDataType();
			
			if (StringUtils.equals(dataType, 
					FapConstants.BASIC_DATA_TYPE_NUMERIC) &&
					StringUtils.isNotEmpty(queryRow.getValue()) &&
					!NumberUtils.isNumber(queryRow.getValue())) {
				
				// create an error message
				errorMessages.add(new GenericException(
						CommonErrorCodes.CUSTOM_QUERY_VALUE_DOES_NOT_MATCH_DATA_TYPE, 
						new String[]{rowIndex}));
			} else if (StringUtils.equals(
					dataType, FapConstants.BASIC_DATA_TYPE_DATE) &&
					StringUtils.isNotEmpty(queryRow.getValue())) {
				
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
					sdf.setLenient(false);
					sdf.parse(queryRow.getValue());
				} catch (ParseException parseException) {
					// 	create an error message
					errorMessages.add(new GenericException(
							CommonErrorCodes.CUSTOM_QUERY_VALUE_INVALID_DATE, 
							new String[]{rowIndex}));
				}
			}
		}
		
	}
	
	/**
	 * Validates, whether the brackets are used properly or not
	 * 
	 * @param criteriaList
	 * @param errorMessages
	 */
	private void validateParenthesis(
			List<CustomQueryRow> criteriaList, 
			Collection<GenericException> errorMessages) {
		
		// used for the bracket validation	
		int openBracketcount = 0;
		int closedBracketcount = 0;
		boolean improperPlaceOfBrackets = false;
		
		// Iterate through each row and perform the validations
		Iterator<CustomQueryRow> iterator = criteriaList.iterator();
		while(iterator.hasNext()) {
			
			CustomQueryRow queryCriteria = (CustomQueryRow) iterator.next();
			if (!queryCriteria.isEmptyCriteria()) {
				openBracketcount += queryCriteria.getLeftBracket().length();
				closedBracketcount -= queryCriteria.getRightBracket().length();
				
				// if the count is < 0, it means the brackets are placed in 
				// wrong sequence
				if ((closedBracketcount + openBracketcount) < 0){
					improperPlaceOfBrackets = true;
				}
			}
		}
		
		if ((closedBracketcount + openBracketcount != 0) || 
				improperPlaceOfBrackets) {
			
			String[] params = 
				new String[] {String.valueOf(openBracketcount), 
					String.valueOf(closedBracketcount * (-1))};
			errorMessages.add(new GenericException(
					CommonErrorCodes.CUSTOM_QUERY_MISMATCH_OR_INVALID_BRACKET_USAGE, 
					params));
		}
	}
	
	/**
	 * Validate's the query name
	 * 
	 * @param fapForm
	 * @param request
	 * @throws SystemException 
	 */
	private boolean validateCustomQueryName(
			FapForm fapForm, 
			HttpServletRequest request) throws SystemException {
		
		boolean hasErrors = false;
		
		String queryName = fapForm.getSaveQueryName();
		Collection<GenericException> errorMessages = 
			new ArrayList<GenericException>();
		
		if (StringUtils.isBlank(queryName)) {
			// If the name is empty
			errorMessages.add(new GenericException(
					CommonErrorCodes.CUSTOM_QUERY_SAVE_WITHOUT_NAME));
			
		} else if (FapConstants.SAVE_NAME_PATTERN.matcher(
				queryName).replaceAll(FapConstants.BLANK).length() > 0){
			
			// if the name contains chars other than alphabets, numbers,
			// blank, dashes and dots
			errorMessages.add(new GenericException(
					CommonErrorCodes.CUSTOM_QUERY_INVALID_CHAR_IN_QUERY_NAME));
		}
		
		// If there are error messages, place the collection in the request
		if (!errorMessages.isEmpty()) {
			setErrorsInRequest(request, errorMessages);
			fapForm.setMessagesExist(true);
			fapForm.setFilterResultsId(null);
			hasErrors = true;
		} else {
			
			// look for warnings
			Collection<GenericException> warningMessages = 
				new ArrayList<GenericException>();
			
			// if the name entered already exists.
			String duplicateInd = 
				FandpServiceDelegate.getInstance().insertUserData(
						getProfileId(request), 
						CustomQueryUtility.createUserSavedData(
								fapForm, 
								FapConstants.TYPE_SAVED_CUSTOM_QUERY),
						fapForm.isOverwriteExisting());
			
			if (StringUtils.isBlank(duplicateInd) || 
					StringUtils.equals(duplicateInd, FapConstants.YES)) {
				
				// If user saves a query with the same name
				try {
					
					String alertMessage = ContentHelper.getMessage(
							new GenericExceptionWithContentType(
									CommonContentConstants.CUSTOM_QUERY_NAME_ALREADY_EXISTS, 
									ContentTypeManager.instance().MISCELLANEOUS));
						
					fapForm.setAlertMessage(StringEscapeUtils.escapeJava(
							StringEscapeUtils.escapeHtml(alertMessage)));
					
				} catch (ContentException e) {
					throw new SystemException("Not able to get content for id " + 
							CommonContentConstants.LIST_NAME_ALREADY_EXISTS);
				}
				
				fapForm.setFilterResultsId("showConfirmation");
				
			} else if (validateCustomQuery(fapForm, request)) {
		
				// If user saves a query when the query has errors
				warningMessages.add(new GenericExceptionWithContentType(
						CommonContentConstants.ATTEMPT_TO_SAVE_CUSTOM_QUERY_WITH_ERRORS,
						ContentTypeManager.instance().MISCELLANEOUS));
			}
			
			// If there are error messages, place the collection in the request
			if (!warningMessages.isEmpty()) {
				setWarningsInRequest(request, warningMessages);
				if (!fapForm.isMessagesExist()) {
					fapForm.setWarningsExist(true);
				}
				fapForm.setFilterResultsId(null);
				
				// return true if there are error messages
				hasErrors = true;
			}
		}
		
		return hasErrors;
	}
	
	/**
	 * set the data type for the field selections, done on each row
	 * 
	 * @param fapForm
	 */
	private void setDataType(FapForm fapForm) {

		List<CustomQueryRow> customQueryCriteriaList = 
			fapForm.getCustomQueryCriteriaList();
		
		for (CustomQueryRow customQueryRow : customQueryCriteriaList){
		
			if (customQueryRow.getFieldId() != -1 && 
					customQueryRow.getFieldId() != -2) {
				
				String dataType = CustomQueryUtility.fieldsMap.get(
						String.valueOf(
								customQueryRow.getFieldId())).getBasicDataType();
				
				customQueryRow.setDataTypeForSelectedField(dataType);
			}
		}
	}
}
