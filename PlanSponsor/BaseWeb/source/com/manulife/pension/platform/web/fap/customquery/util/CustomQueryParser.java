package com.manulife.pension.platform.web.fap.customquery.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.platform.web.fap.constants.FapConstants;
import com.manulife.pension.ps.service.report.fandp.valueobject.CustomQueryRow;
import com.manulife.pension.service.fund.fandp.valueobject.Fi360ScoreQuartiles;

/**
 * A parser to create the custom where clause from the user data.
 *  
 * @author ayyalsa
 *
 */
public class CustomQueryParser {
	
	/**
	 * Creates the Custom Where clause from the user data.
	 * 
	 * @param userDefinedQueryCriteria
	 * @param fundQueryFields
	 * @param operators
	 * 
	 * @return created custom where clause
	 * 
	 * @throws ParseException
	 */
	public String buildCustomWhereClause(
			List<CustomQueryRow> userDefinedQueryCriteria, 
			Map<String, CustomQueryField> fundQueryFields, 
			Map<String, CustomQueryOperator> operators) 
	throws ParseException {
		
		// if there is no valid data return blank
		if (CustomQueryUtility.isUserDefinedQueryCriteriaEmpty(
				userDefinedQueryCriteria)) {
			return "";
		}
		
		//build map of displayValue/sqlValue
		Map<String, String> operatorValues = new HashMap<String, String>();
		
		for (Iterator<CustomQueryOperator> iter = 
			operators.values().iterator(); iter.hasNext();) {
			
			CustomQueryOperator operator = (CustomQueryOperator) iter.next();
			operatorValues.put(operator.getLabel(), 
					operator.getEquivalentSqlOperator());
		}
		
		StringBuffer buff = new StringBuffer();
		buff.append(" AND (");
		
		/*
		 * README PY
		 * The following code checks to see if the field is of IntegerAsAlpha
		 * is for dealing with fund_morningstar.morningstar_rating column 
		 * where its datatype is char(1) but could contain values 
		 * such as ' ', '1', '2', '3', ... Technically this column should be 
		 * changed to a numeric datatype in the database.
		 */
		Set<CustomQueryField> integerAsAlphaFieldSet = 
			new HashSet<CustomQueryField>();
		
		Set<CustomQueryField> checkNullForFieldSet = 
			new HashSet<CustomQueryField>();
		
		for (Iterator<CustomQueryRow> iter = 
			userDefinedQueryCriteria.iterator(); iter.hasNext();) {
			
			CustomQueryRow queryRow = (CustomQueryRow) iter.next();
			if (!queryRow.isEmptyCriteria()){
				CustomQueryField field = 
					(CustomQueryField) fundQueryFields.get(
							String.valueOf(queryRow.getFieldId()));
				
				if (field != null) {
					
					if (field.isIntegerAsAlpha() && 
							!(StringUtils.equals(queryRow.getValue(), " ") && field.isAllowBlankValues())) {
						integerAsAlphaFieldSet.add(field);
					}
					
					if (field.isLookForNullValues() && 
						(StringUtils.equals(queryRow.getOperator(), "Not=") ||
						StringUtils.equals(queryRow.getOperator(), "Excludes"))) { 
						checkNullForFieldSet.add(field);
					}
					
					buff.append(" ");
					buff.append(StringUtils.isEmpty(
							queryRow.getLogic()) ? "": queryRow.getLogic() +" ");
					buff.append(StringUtils.isEmpty(
							queryRow.getLeftBracket()) ? "" : queryRow.getLeftBracket());
					
					if (field.isAlpha()) {
						buff.append("LOWER(");
					}
					
					if (!StringUtils.isBlank(field.getTableId())) {
						buff.append(field.getTableId());
						buff.append(".");
					}
					buff.append(field.getSqlName());
					if (field.isAlpha()) {
						buff.append(")");
					}
					buff.append(" ");
					
					String queryValue = 
						applyModifer(queryRow.getValue(), field.getModifier());
					
					buff.append(formatMatchComparatorForWhereClause(
							(CustomQueryOperator) operators.get(queryRow.getOperator()), 
							queryValue, field.getBasicDataType(), operatorValues));
					
					buff.append(StringUtils.isEmpty(
							queryRow.getRightBracket()) ? "": queryRow.getRightBracket());
				}
			}
		}
		
		for (Iterator<CustomQueryField> iter = 
			checkNullForFieldSet.iterator(); iter.hasNext();) {
			
			CustomQueryField field = (CustomQueryField) iter.next();
			buff.append(" OR ");
			buff.append(field.getTableId());
			buff.append(".");
			buff.append(field.getSqlName());
			buff.append(" is null");
		}
		
		buff.append(")");
		
		for (Iterator<CustomQueryField> iter = 
			integerAsAlphaFieldSet.iterator(); iter.hasNext();) {
			
			CustomQueryField field = (CustomQueryField) iter.next();
			buff.append(" AND RTRIM(");
			buff.append(field.getTableId());
			buff.append(".");
			buff.append(field.getSqlName());
			buff.append(") > '0' ");
			buff.append(" AND RTRIM(");
			buff.append(field.getTableId());
			buff.append(".");
			buff.append(field.getSqlName());
			buff.append(") <> '' ");
		}
		
		return buff.toString();
	}
	
	/**
	 * Converts the value to the specified modifier
	 * 
	 * @param queryValue
	 * @param modifier
	 * @return value, after applying the modifier
	 */
	private String applyModifer(String queryValue, String modifier) {
		
		if (modifier != null) {
			try {
				queryValue = Double.toString(
						new Double(queryValue).doubleValue() * new Double(modifier).doubleValue());
			} catch(Exception e) {
				// leave value as is
			}
		}
		return queryValue;
	}

	/**
	 * Formats the user data to the matching SQL operator
	 * 
	 * @param operator
	 * @param queryValue
	 * @param basicDataType
	 * @param operatorValues
	 * 
	 * @return formatted data for the custom where clause
	 *  
	 * @throws ParseException
	 */
	private String formatMatchComparatorForWhereClause(
			CustomQueryOperator operator, 
			String queryValue, 
			String basicDataType, 
			Map<String, String> operatorValues) throws ParseException {
		
		StringBuffer buff = new StringBuffer();
		String operatorSqlValue = 
				(String) operatorValues.get(operator.getLabel());
		if (CustomQueryField.BASIC_DATA_TYPE_MSTAR.equals(basicDataType) || 
				CustomQueryField.BASIC_DATA_TYPE_RPAG.equals(basicDataType)) {
			if (FapConstants.NOT_SCORED.equals(queryValue)) {
				handleNotScoreValues(buff, operatorSqlValue, queryValue);
			} else {
				handleIntegerValues(buff, operatorSqlValue, queryValue);
			}
		} else if (CustomQueryField.BASIC_DATA_TYPE_FI360.equals(basicDataType)) {
			if (FapConstants.NOT_SCORED.equals(queryValue)) {
				handleNotScoreValues(buff, operatorSqlValue, queryValue);
			} else {
				handleFi360RangeValues(buff, operatorSqlValue, queryValue);
			}
		} else {
			buff.append(operatorSqlValue + " ");
			buff.append(formatQueryValueForWhereClause(
					queryValue, basicDataType, operatorSqlValue));
				
		}
		
		return buff.toString();
	}
	
	/**
	 * Formats the value, based on the Data type.1
	 * 
	 * @param queryValue
	 * @param basicDataType
	 * @param operatorSqlValue
	 * 
	 * @return formatted values for the where clause
	 * 
	 * @throws ParseException
	 */
	private String formatQueryValueForWhereClause(
			String queryValue, 
			String basicDataType, 
			String operatorSqlValue) throws ParseException {
		
		String formattedQueryValue = queryValue;
		
		if (basicDataType.equals(CustomQueryField.BASIC_DATA_TYPE_DATE)) {
			SimpleDateFormat queryValueInputDateFormat = 
				new SimpleDateFormat("MM/dd/yyyy");
			SimpleDateFormat whereClauseDateFormat = 
				new SimpleDateFormat("yyyy-MM-dd");
			Date queryValueAsDate = 
				queryValueInputDateFormat.parse(queryValue);
			formattedQueryValue = 
				"'" + whereClauseDateFormat.format(queryValueAsDate) + "'";
			
		} else if (basicDataType.equals(
				CustomQueryField.BASIC_DATA_TYPE_ALPHA)) {
			
			if (operatorSqlValue.indexOf(
					FapConstants.SQL_OPERATOR_LIKE) > -1) {
				
				formattedQueryValue = "%" + formattedQueryValue + "%";
			}
			
			formattedQueryValue = "LOWER('" + formattedQueryValue + "')";
		} else if (basicDataType.equals(
				CustomQueryField.BASIC_DATA_TYPE_INTEGER_AS_ALPHA)){
			
			formattedQueryValue = "LOWER('" + formattedQueryValue + "')";
		}
		
		return formattedQueryValue;
	}
	
	private void handleNotScoreValues(StringBuffer buff,
			String operatorSqlValue, String queryValue) throws ParseException {
		if (operatorSqlValue.equals("=")) {
			buff.append(" is null ");
		} else if (operatorSqlValue.equals("<>")) {
			buff.append(" is not null ");
		} else {
			throw new ParseException("Invalid Operator For Not Scored Value", 0);
		}
	}
	
	private void handleIntegerValues(StringBuffer buff,
			String operatorSqlValue, String queryValue) throws ParseException {
		buff.append(operatorSqlValue + " ");
		buff.append(queryValue);
	}
	
	private void handleFi360RangeValues(StringBuffer buff,
			String operatorSqlValue, String queryValue) throws ParseException {
		Fi360ScoreQuartiles quartile = Fi360ScoreQuartiles.getQuartileByDropdownName(queryValue);
		if(quartile == null) {
			throw new ParseException("Invalid Fi360 Dropdown Value - " + queryValue, 0);
		}
		if (operatorSqlValue.equals("=")) {
			buff.append("  between ").append(quartile.getMinLimit())
					.append(" and ").append(quartile.getMaxLimit());
		} else if (operatorSqlValue.equals("<>")) {
			buff.append("  not between ").append(quartile.getMinLimit())
					.append(" and ").append(quartile.getMaxLimit());
		} else if (operatorSqlValue.equals(">")) {
			buff.append("  > ").append(quartile.getMaxLimit());
		} else if (operatorSqlValue.equals("<")) {
			buff.append("  < ").append(quartile.getMinLimit());
		} else if (operatorSqlValue.equals(">=")) {
			buff.append("  >= ").append(quartile.getMinLimit());
		} else if (operatorSqlValue.equals("<=")) {
			buff.append("  <= ").append(quartile.getMaxLimit());
		}
	}
}
