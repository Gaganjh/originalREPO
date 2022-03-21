package com.manulife.pension.platform.web.fap.customquery.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.platform.web.fap.FapForm;
import com.manulife.pension.platform.web.fap.constants.FapConstants;
import com.manulife.pension.platform.web.fap.customquery.util.propertymanager.PropertyManager;
import com.manulife.pension.ps.service.report.fandp.valueobject.CustomQueryRow;
import com.manulife.pension.ps.service.report.fandp.valueobject.CustomQuerySavedData;
import com.manulife.pension.ps.service.report.fandp.valueobject.FundListSavedData;
import com.manulife.pension.ps.service.report.fandp.valueobject.UserSavedData;
import com.manulife.pension.service.fund.fandp.valueobject.Fi360ScoreQuartiles;

/**
 * An utility class to support the custom query operations. 
 * Creates the Logic, Field and Value list and also methods to 
 * create the custom Where clause
 * 
 * @author ayyalsa
 *
 */
public class CustomQueryUtility {

	/**
	 *  Map to store the custom query options. this is used to populate the
	 *  values to the drop-downs 
	 */
	public static HashMap<String, List<LabelValueBean>> customQueryOptions;
	
	/**
	 *  To store the Logic list.
	 */
	public static List<LabelValueBean> logicList;
	
	/**
	 *  To store the operators for the Alpha data type
	 */
	public static List<LabelValueBean> operatorsForAlphaDataType;
	
	/**
	 *  To store the operators for the Numeric data type
	 */
	public static List<LabelValueBean> operatorsForNumericDataType;
	
	/**
	 *  To store the operators for the Date data type
	 */
	public static List<LabelValueBean> operatorsForDateDataType;
	
	/**
	 *  Map to store the operators list. Used for validations
	 */
	public static Map<String, CustomQueryOperator> operatorsMap;
	
	/**
	 * Map to store the Fields list. Used for validations
	 */
	public static Map<String, CustomQueryField> fieldsMap;
	
	/**
	 * Map to store the MorningStar FundScorecard Evaluation List. 
	 * Used for user selection
	 */
	public static List<LabelValueBean> morningStarEvaluationList;
	
	/**
	 * Map to store the Fi360 FundScorecard Evaluation List. 
	 * Used for user selection
	 */
	public static List<LabelValueBean> fi360EvaluationList;
	
	/**
	 * Map to store the RPAG FundScorecard Evaluation List. 
	 * Used for user selection
	 */
	public static List<LabelValueBean> rpagEvaluationList;
	
	/**
	 * Map to store the Morningstar rating values
	 * Used for user selection
	 */
	public static List<LabelValueBean> overallStarRatingList;
	
	/**
	 * Create the minimum number of rows that needs to be 
	 * displayed when the custom query section is enabled
	 * 
	 * @return List<CustomQueryRow> - List of rows
	 */
	public static List<CustomQueryRow> 
	createMinimumNumberOfQueryCriteriaRows() {
		
		List<CustomQueryRow> customQueryRowList = 
			new ArrayList<CustomQueryRow>();
		
		// Add three empty rows
		customQueryRowList.add(new CustomQueryRow());
		customQueryRowList.add(new CustomQueryRow());
		customQueryRowList.add(new CustomQueryRow());
	
		return customQueryRowList;
	}
	
	/**
	 * Create the options that are needed to enable the 
	 * custom query section.
	 * The options will be 
	 * 		1. Logical
	 * 		2. Fields
	 * 		3. Operators 
	 * 
	 * @return HashMap<String, List<LabelValueBean>> - options map 
	 */
	public static synchronized HashMap<String, List<LabelValueBean>> 
	createCustomQueryOptions() {
		
		if (customQueryOptions == null || customQueryOptions.isEmpty()) {
			customQueryOptions = new HashMap<String, List<LabelValueBean>>();
	
			/**
			 * Load the Logic List
			 */
			loadLogicList();
			// Put the Logic List to the map 
			customQueryOptions.put(FapConstants.LOGIC_KEY, logicList);
			
			
			/**
			 * Load the operators Map
			 */
			loadOperatorsMap();
			
			// This will hold the list of operators
			List<LabelValueBean> operatorsList = new ArrayList<LabelValueBean>();
			
			// add the default option
			operatorsList.add(new LabelValueBean(
					FapConstants.DEFAULT_SELECT, FapConstants.NOT_APPLICABLE));
	
			// iterate through the list and create options as Label - Value bean 
			Iterator<String> operatorKey = operatorsMap.keySet().iterator();
			while (operatorKey.hasNext()) {
				CustomQueryOperator operator = operatorsMap.get(operatorKey.next());
				// add the operators
				operatorsList.add(new LabelValueBean(
						operator.getLabel(), operator.getId()));
			}
			// Put the operators List create above to the map 
			customQueryOptions.put(FapConstants.OPERATOR_KEY, operatorsList);
			
	
			/**
			 * Load the Fields Map
			 */
			loadFieldsMap();
			
			// This will hold the list of fields
			List<LabelValueBean> fieldsList = new ArrayList<LabelValueBean>();
			
			// add the default option
			fieldsList.add(new LabelValueBean(FapConstants.DEFAULT_SELECT, "-1"));
	
			String lastGroupName = null;
			Iterator<String> fieldKey = fieldsMap.keySet().iterator();
			while (fieldKey.hasNext()) {
				CustomQueryField field = fieldsMap.get(fieldKey.next());
				
				String currentGroupName = field.getGroupName();
				if (!StringUtils.equalsIgnoreCase(lastGroupName, currentGroupName)) {
					fieldsList.add(new LabelValueBean(currentGroupName, "-2"));
					lastGroupName = currentGroupName;
				}
				// add the Fields
				fieldsList.add(new LabelValueBean(
						field.getDisplayName(), field.getFieldId()));
			}
			
			// Put the Fields List create above to the map 
			customQueryOptions.put(FapConstants.FIELD_KEY, fieldsList);
			createOperator(customQueryOptions);
		}
		
		return customQueryOptions;
	}
	
	/**
	 *  Create the logic options for the Logic drop-down
	 */
	private static void loadLogicList() {
			logicList = new ArrayList<LabelValueBean>(2);

			logicList.add(new LabelValueBean(
					FapConstants.DEFAULT_SELECT, FapConstants.BLANK));
			logicList.add(new LabelValueBean(
					FapConstants.LOGIC_AND, FapConstants.LOGIC_AND));
			logicList.add(new LabelValueBean(
					FapConstants.LOGIC_OR, FapConstants.LOGIC_OR));
	}
	
	/**
	 * Create the Operators options for the Logic drop-down.
	 * The drop-down will be loaded dynamically based on the 
	 * filed selection
	 * 
	 * @param customQueryOptions
	 */
	private static void createOperator(HashMap<String, 
			List<LabelValueBean>> customQueryOptions) {
		
		// Load the operators for the Alpha Numeric Data type
		if (operatorsForAlphaDataType == null || 
				operatorsForAlphaDataType.isEmpty()) {
			
			operatorsForAlphaDataType = new ArrayList<LabelValueBean>(5);
			operatorsForAlphaDataType.add(new LabelValueBean(
					FapConstants.DEFAULT_SELECT, FapConstants.BLANK));
			operatorsForAlphaDataType.add(
					new LabelValueBean("=", "="));
			operatorsForAlphaDataType.add(
					new LabelValueBean("Not=", "Not="));
			operatorsForAlphaDataType.add(
					new LabelValueBean("Contains", "Contains"));
			operatorsForAlphaDataType.add(
					new LabelValueBean("Excludes", "Excludes"));
		}
		customQueryOptions.put(
				CustomQueryField.BASIC_DATA_TYPE_ALPHA, 
				operatorsForAlphaDataType);
		
		// Load the operators for the Date Data type
		if (operatorsForDateDataType == null || 
				operatorsForDateDataType.isEmpty()) {
			
			operatorsForDateDataType = new ArrayList<LabelValueBean>(7);
			operatorsForDateDataType.add(new LabelValueBean(
					FapConstants.DEFAULT_SELECT, FapConstants.BLANK));
			operatorsForDateDataType.add(new LabelValueBean(">", ">"));
			operatorsForDateDataType.add(new LabelValueBean("<", "<"));
			operatorsForDateDataType.add(new LabelValueBean(">=", ">="));
			operatorsForDateDataType.add(new LabelValueBean("<=", "<="));
			operatorsForDateDataType.add(new LabelValueBean("=", "="));
			operatorsForDateDataType.add(new LabelValueBean("Not=", "Not="));
		}
		customQueryOptions.put(
				CustomQueryField.BASIC_DATA_TYPE_DATE, 
				operatorsForDateDataType);
		
		// Load the operators for the Numeric Data type
		if (operatorsForNumericDataType == null || 
				operatorsForNumericDataType.isEmpty()) {
			
			operatorsForNumericDataType = new ArrayList<LabelValueBean>(7);
			operatorsForNumericDataType.add(new LabelValueBean(
					FapConstants.DEFAULT_SELECT, FapConstants.BLANK));
			operatorsForNumericDataType.add(new LabelValueBean(">", ">"));
			operatorsForNumericDataType.add(new LabelValueBean("<", "<"));
			operatorsForNumericDataType.add(new LabelValueBean(">=", ">="));
			operatorsForNumericDataType.add(new LabelValueBean("<=", "<="));
			operatorsForNumericDataType.add(new LabelValueBean("=", "="));
			operatorsForNumericDataType.add(new LabelValueBean("Not=", "Not="));
		}
		customQueryOptions.put(
				CustomQueryField.BASIC_DATA_TYPE_NUMERIC, 
				operatorsForNumericDataType);
		
		customQueryOptions.put(
				CustomQueryField.BASIC_DATA_TYPE_MSTAR, 
				operatorsForNumericDataType);
		
		customQueryOptions.put(
				CustomQueryField.BASIC_DATA_TYPE_FI360, 
				operatorsForNumericDataType);
		
		customQueryOptions.put(
				CustomQueryField.BASIC_DATA_TYPE_RPAG, 
				operatorsForNumericDataType);
		
		customQueryOptions.put(
				CustomQueryField.BASIC_DATA_TYPE_INTEGER_AS_ALPHA, 
				operatorsForNumericDataType);
	}
	
	/**
	 *  Create the operators that would be used for validations. 
	 *  this Map is grouped along with the data type list
	 */
	private static void loadOperatorsMap() {
			
			operatorsMap = new HashMap<String, CustomQueryOperator>();
			
			List<String> applicableDataTypes = new ArrayList<String>();
			applicableDataTypes.add(
					FapConstants.BASIC_DATA_TYPE_DATE);
			applicableDataTypes.add(
					FapConstants.BASIC_DATA_TYPE_NUMERIC);
				
			/*
			 * The below operators will be applicable to the 
			 * Date, Numeric and Alpha Numeric data types 
			 */
			operatorsMap.put(">", new CustomQueryOperator(
					"1", ">", ">", applicableDataTypes));
			operatorsMap.put("<", new CustomQueryOperator(
					"2", "<", "<", applicableDataTypes));
			operatorsMap.put("<=", new CustomQueryOperator(
					"3", "<=", "<=", applicableDataTypes));
			operatorsMap.put(">=", new CustomQueryOperator(
					"4", ">=", ">=", applicableDataTypes));
			
			applicableDataTypes = new ArrayList<String>();
			applicableDataTypes.add(
					FapConstants.BASIC_DATA_TYPE_DATE);
			applicableDataTypes.add(
					FapConstants.BASIC_DATA_TYPE_NUMERIC);
			applicableDataTypes.add(
					FapConstants.BASIC_DATA_TYPE_INTEGER_AS_ALPHA);
			applicableDataTypes.add(
					FapConstants.BASIC_DATA_TYPE_ALPHA);
			
			/*
			 * The below operators will be applicable to the 
			 * Date, Numeric, Alpha and Alpha Numeric data types 
			 */
			operatorsMap.put("=", new CustomQueryOperator(
					"5", "=", "=", applicableDataTypes));
			operatorsMap.put("Not=", new CustomQueryOperator(
					"6", "(=)", "<>", applicableDataTypes));
			
			applicableDataTypes = new ArrayList<String>();
			applicableDataTypes.add(
					FapConstants.BASIC_DATA_TYPE_ALPHA);
			applicableDataTypes.add(
					FapConstants.BASIC_DATA_TYPE_INTEGER_AS_ALPHA);
			/*
			 * The below operators will be applicable to the 
			 * Alpha and Alpha Numeric data types 
			 */
			operatorsMap.put("Contains", new CustomQueryOperator(
					"7", "app=", "LIKE", applicableDataTypes));
			operatorsMap.put("Excludes", new CustomQueryOperator(
					"8", "(app=)", "NOT LIKE", applicableDataTypes));
	}
	
	/**
	 *  Create the fields map that would be populated to the filed
	 *  selection drop-down and also used for validations
	 */
	@SuppressWarnings("unchecked")
	private static void loadFieldsMap() {
			
		fieldsMap = new LinkedHashMap<String, CustomQueryField>();
		
		// Initialize the PropertyManager 
		PropertyManager.getInstance();
	
		
		// get the total group count
		int queryFieldGroupCount = PropertyManager.getInt(
				FapConstants.QUERY_FIELD_GROUP_PROPERTY_KEY, 0);
		
		// Iterate through the groups
		for (int groupIndex = 1; groupIndex < queryFieldGroupCount + 1; groupIndex++) {
			
			// get the Group Name for the properties
			String queryFieldGroupName = PropertyManager.getString(
					"query.field.group.name." + groupIndex);
			String queryFieldGroupPropertyPrefix = 
				StringUtils.deleteWhitespace(queryFieldGroupName);
			
			int fieldIndex = 1;
			
			// frame the property to get the values 
			String queryFieldIndexedPropertyPrefix = 
				"query.field." + queryFieldGroupPropertyPrefix + "." + 
						fieldIndex + ".";
			
			// get all properties matching the above framed String
			Properties properties = PropertyManager.getProperties(
					queryFieldIndexedPropertyPrefix);
			
			while (properties != null && !properties.isEmpty()) {
				// build a CustomQueryField object from properties set 
				CustomQueryField field = new CustomQueryField();
				field.setGroupName(queryFieldGroupName);
				
				for (Iterator iter = 
					properties.entrySet().iterator(); iter.hasNext();) {
					
					Entry element = (Entry) iter.next();
					String key = (String) element.getKey();
					String value = (String) element.getValue();
					String attribute = StringUtils.removeStart(
							key, queryFieldIndexedPropertyPrefix);
					
					if(attribute.equals("fieldId")){
						field.setFieldId(value);
					}else if (attribute.equals("sqlName")) {
						field.setSqlName(value);
					} else if (attribute.equals("displayName")) {
						field.setDisplayName(value);
					} else if (attribute.equals("tableId")) {
						field.setTableId(value);
					} else if (attribute.equals("basicDataType")) {
						field.setBasicDataType(value);
					} else if (attribute.equals("modifier")) {
						field.setModifier(value);
					} else if (attribute.equals("lookForNullValues")) {
						if (StringUtils.equalsIgnoreCase(value, "Y")) {
							field.setLookForNullValues(true);
						} else {
							field.setLookForNullValues(false);
						}
					} else if (attribute.equals("allowBlankValues")) {
						if (StringUtils.equalsIgnoreCase(value, "Y")) {
							field.setAllowBlankValues(true);
						} else {
							field.setAllowBlankValues(false);
						}
					}
						
				}
				
				// once the CustomQueryField is created, put in the MAP,
				// with key as the filed id.
				fieldsMap.put(field.getFieldId(), field);
				fieldIndex++;
				
				// get next set of properties for the current group
				queryFieldIndexedPropertyPrefix = "query.field." + 
					queryFieldGroupPropertyPrefix +"."+ fieldIndex + ".";
				
				properties = PropertyManager.getProperties(
						queryFieldIndexedPropertyPrefix);
			}
		}
	}
	
	/**
	 * Create the Fund Check Evaluation values that is to be 
	 * populated in the Fund Check drop-down
	 * 
	 * @return List of FundCheck values
	 */
	public static List<LabelValueBean> createMorningStarEvaluationValues() {
		
		if (morningStarEvaluationList == null || 
				morningStarEvaluationList.isEmpty()) {
			morningStarEvaluationList = new ArrayList<LabelValueBean>(7);
			morningStarEvaluationList.add(new LabelValueBean(
					FapConstants.DEFAULT_SELECT, FapConstants.BLANK));
			morningStarEvaluationList.add(new LabelValueBean(
					FapConstants.STAR_RATING_1, 
					FapConstants.STAR_RATING_1_KEY));
			morningStarEvaluationList.add(new LabelValueBean(
					FapConstants.STAR_RATING_2, 
					FapConstants.STAR_RATING_2_KEY));
			morningStarEvaluationList.add(new LabelValueBean(
					FapConstants.STAR_RATING_3, 
					FapConstants.STAR_RATING_3_KEY));
			morningStarEvaluationList.add(new LabelValueBean(
					FapConstants.STAR_RATING_4, 
					FapConstants.STAR_RATING_4_KEY));
			morningStarEvaluationList.add(new LabelValueBean(
					FapConstants.STAR_RATING_5, 
					FapConstants.STAR_RATING_5_KEY));
			morningStarEvaluationList.add(new LabelValueBean(FapConstants.NOT_SCORED,
					FapConstants.NOT_SCORED));
		}
		return morningStarEvaluationList;
	}
	
	/**
	 * Create the Fund Check Evaluation values that is to be 
	 * populated in the Fund Check drop-down
	 * 
	 * @return List of FundCheck values
	 */
	public static List<LabelValueBean> createFi360EvaluationValues() {
		if (fi360EvaluationList == null || fi360EvaluationList.isEmpty()) {
			fi360EvaluationList = new ArrayList<LabelValueBean>();
			fi360EvaluationList.add(new LabelValueBean(
					FapConstants.DEFAULT_SELECT, FapConstants.BLANK));
			fi360EvaluationList.add(new LabelValueBean(
					Fi360ScoreQuartiles.FirstQuartile.getDropDownName(),
					Fi360ScoreQuartiles.FirstQuartile.getDropDownName()));
			fi360EvaluationList.add(new LabelValueBean(
					Fi360ScoreQuartiles.SecondQuartile.getDropDownName(),
					Fi360ScoreQuartiles.SecondQuartile.getDropDownName()));
			fi360EvaluationList.add(new LabelValueBean(
					Fi360ScoreQuartiles.ThridQuartile.getDropDownName(),
					Fi360ScoreQuartiles.ThridQuartile.getDropDownName()));
			fi360EvaluationList.add(new LabelValueBean(
					Fi360ScoreQuartiles.FourthQuartile.getDropDownName(),
					Fi360ScoreQuartiles.FourthQuartile.getDropDownName()));
			fi360EvaluationList.add(new LabelValueBean(FapConstants.NOT_SCORED,
					FapConstants.NOT_SCORED));
		}
		return fi360EvaluationList;
	}
	
	/**
	 * Create the Fund Check Evaluation values that is to be 
	 * populated in the Fund Check drop-down
	 * 
	 * @return List of FundCheck values
	 */
	public static List<LabelValueBean> createRPAGEvaluationValues() {

		if (rpagEvaluationList == null || rpagEvaluationList.isEmpty()) {
			rpagEvaluationList = new ArrayList<LabelValueBean>();
			rpagEvaluationList.add(new LabelValueBean(
					FapConstants.DEFAULT_SELECT, FapConstants.BLANK));
			rpagEvaluationList.add(new LabelValueBean(
					FapConstants.RPAG_RATING_0, FapConstants.RPAG_RATING_0));
			rpagEvaluationList.add(new LabelValueBean(
					FapConstants.RPAG_RATING_1, FapConstants.RPAG_RATING_1));
			rpagEvaluationList.add(new LabelValueBean(
					FapConstants.RPAG_RATING_2, FapConstants.RPAG_RATING_2));
			rpagEvaluationList.add(new LabelValueBean(
					FapConstants.RPAG_RATING_3, FapConstants.RPAG_RATING_3));
			rpagEvaluationList.add(new LabelValueBean(
					FapConstants.RPAG_RATING_4, FapConstants.RPAG_RATING_4));
			rpagEvaluationList.add(new LabelValueBean(
					FapConstants.RPAG_RATING_5, FapConstants.RPAG_RATING_5));
			rpagEvaluationList.add(new LabelValueBean(
					FapConstants.RPAG_RATING_6, FapConstants.RPAG_RATING_6));
			rpagEvaluationList.add(new LabelValueBean(
					FapConstants.RPAG_RATING_7, FapConstants.RPAG_RATING_7));
			rpagEvaluationList.add(new LabelValueBean(
					FapConstants.RPAG_RATING_8, FapConstants.RPAG_RATING_8));
			rpagEvaluationList.add(new LabelValueBean(
					FapConstants.RPAG_RATING_9, FapConstants.RPAG_RATING_9));
			rpagEvaluationList.add(new LabelValueBean(
					FapConstants.RPAG_RATING_10, FapConstants.RPAG_RATING_10));
			rpagEvaluationList.add(new LabelValueBean(FapConstants.NOT_SCORED,
					FapConstants.NOT_SCORED));
		}

		return rpagEvaluationList;
	}
	
	/**
	 * Creates the Morningstar Rating values that is to be 
	 * populated in the Fund Check drop-down
	 * 
	 * @return List of Overall star Rating values
	 */
	public static List<LabelValueBean> createOverallStarRatingValues() {
		
		if (overallStarRatingList == null ||
				overallStarRatingList.isEmpty()) {
			overallStarRatingList = new ArrayList<LabelValueBean>(7);
			overallStarRatingList.add(new LabelValueBean(
					FapConstants.DEFAULT_SELECT, FapConstants.BLANK));
			overallStarRatingList.add(new LabelValueBean(
					FapConstants.STAR_RATING_NOT_RATED, 
					FapConstants.STAR_RATING_NOT_RATED_KEY));
			overallStarRatingList.add(new LabelValueBean(
					FapConstants.STAR_RATING_1, 
					FapConstants.STAR_RATING_1_KEY));
			overallStarRatingList.add(new LabelValueBean(
					FapConstants.STAR_RATING_2, 
					FapConstants.STAR_RATING_2_KEY));
			overallStarRatingList.add(new LabelValueBean(
					FapConstants.STAR_RATING_3, 
					FapConstants.STAR_RATING_3_KEY));
			overallStarRatingList.add(new LabelValueBean(
					FapConstants.STAR_RATING_4, 
					FapConstants.STAR_RATING_4_KEY));
			overallStarRatingList.add(new LabelValueBean(
					FapConstants.STAR_RATING_5, 
					FapConstants.STAR_RATING_5_KEY));
		}
		
		return overallStarRatingList;
	}
	
	/**
	 * Iterates through all List and validates whether the Criteria list 
	 * is Empty. If Empty returns TRUE.
	 * 
	 * @param userDefinedCriteriaList
	 * @return if list is Empty returns TRUE
	 */
	public static boolean isUserDefinedQueryCriteriaEmpty(
			List<CustomQueryRow> userDefinedCriteriaList){
		
		//	check if every element in the list is empty
		if (userDefinedCriteriaList == null || 
				userDefinedCriteriaList.isEmpty()){
			return true;
		}
		
		for (Iterator<CustomQueryRow> iter = 
			userDefinedCriteriaList.iterator(); iter.hasNext();) {
			
			CustomQueryRow queryRow = (CustomQueryRow) iter.next ();
			if (!queryRow.isEmptyCriteria()){
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Makes a call to the Query Parser to create the where clause.
	 *  
	 * @param userDefinedQueryCriteria
	 * @return created where clause
	 * @throws ParseException
	 */
	public static String buildCustomWhereClause(
			List<CustomQueryRow> userDefinedQueryCriteria) 
	throws ParseException {
	
		CustomQueryParser parser = new CustomQueryParser();
		return parser.buildCustomWhereClause(
				userDefinedQueryCriteria, fieldsMap, operatorsMap);
	}
	
	/**
	 * Converts the Fund IDs String (IDs separated by '|') to a Set
	 * 
	 * @param valuesAsString
	 * @return
	 */
	public static String convertListToString(
			List<CustomQueryRow> userDataList) {
		
		StringBuffer buffer = new StringBuffer();
		
		// Convert the Fund IDs String to a Set
		for (CustomQueryRow customQueryRow : userDataList){
			if (buffer.length() > 0) {
				buffer.append(FapConstants.USER_DATA_ROW_DELIMITER);				
			}
			appendToBuffer(customQueryRow.getLogic(), buffer);
			appendToBuffer(customQueryRow.getLeftBracket(), buffer);
			appendToBuffer(String.valueOf(customQueryRow.getFieldId()), buffer);
			appendToBuffer(customQueryRow.getOperator(), buffer);
			appendToBuffer(customQueryRow.getValue(), buffer);
			buffer.append(StringUtils.trimToNull(customQueryRow.getRightBracket()));
		}
		
		return buffer.toString();
	}
	
	/**
	 * Appends the data to the StringBuffer with "|" as a delimiter.
	 * 
	 * @param data
	 * @param buffer
	 */
	private static void appendToBuffer(String data, StringBuffer buffer) {
		buffer.append(StringUtils.trimToNull(data));
		buffer.append(FapConstants.USER_DATA_FIELD_DELIMITER);
	}
	
	/**
	 * creates CustomQuerySavedData or fundListSavedData based 
	 * on the userSaved Data Type.
	 * 
	 * @param fapForm
	 * @param userDataType
	 * 
	 * @return UserSavedData Object
	 */
	public static UserSavedData createUserSavedData(
			FapForm fapForm, String userDataType) {
		
		UserSavedData userSavedData = null;
		if (StringUtils.equals(userDataType, 
				FapConstants.TYPE_SAVED_CUSTOM_QUERY)) {
			userSavedData = new CustomQuerySavedData();
		} else if (StringUtils.equals(userDataType, 
				FapConstants.TYPE_SAVED_FUND_LIST)) {
			userSavedData = new FundListSavedData();
		}
		
		userSavedData.setListType(userDataType);
		userSavedData.setName(fapForm.getSaveQueryName());
		userSavedData.setDelimtedData(convertListToString(
				fapForm.getCustomQueryCriteriaList()));
		
		return userSavedData;
	}
}