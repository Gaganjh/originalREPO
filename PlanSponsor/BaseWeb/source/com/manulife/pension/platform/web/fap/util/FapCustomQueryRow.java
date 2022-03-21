package com.manulife.pension.platform.web.fap.util;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
/**
 * 
 * @deprecated - instead use 
 * com.manulife.pension.ps.service.report.fandp.valueobject.CustomQueryRow
 */
public class FapCustomQueryRow implements Serializable {

	/**
	 * a default serial version ID 
	 */
	private static final long serialVersionUID = 1L;
	
	private String logicalFunction;
	private String openParenthesis;
	private String fieldSelection;
	private String operator;
	private String value;
	private String closeParenthesis;
	
	private String dataTypeForSelectedField;
	/**
	 * An empty constructor
	 */
	public FapCustomQueryRow() {
		
	}
	
	/**
	 * Constructor
	 * 
	 * @param logicalFunction
	 * @param openParenthesis
	 * @param filedSelection
	 * @param operator
	 * @param value
	 * @param closeParenthesis
	 */
	public FapCustomQueryRow(String logicalFunction,String openParenthesis,
			String filedSelection,String operator,String value,String closeParenthesis) {
		
		this.logicalFunction = logicalFunction;
		this.openParenthesis = openParenthesis;
		this.fieldSelection = filedSelection;
		this.operator = operator;
		this.value = value;
		this.closeParenthesis = closeParenthesis;
	}

	/**
	 * @return the logicalFunction
	 */
	public String getLogicalFunction() {
		return logicalFunction;
	}

	/**
	 * @param logicalFunction the logicalFunction to set
	 */
	public void setLogicalFunction(String logicalFunction) {
		this.logicalFunction = logicalFunction;
	}

	/**
	 * @return the openParenthesis
	 */
	public String getOpenParenthesis() {
		return openParenthesis;
	}

	/**
	 * @param openParenthesis the openParenthesis to set
	 */
	public void setOpenParenthesis(String openParenthesis) {
		this.openParenthesis = openParenthesis;
	}

	/**
	 * @return the fieldSelection
	 */
	public String getFieldSelection() {
		return fieldSelection;
	}

	/**
	 * @param fieldSelection the fieldSelection to set
	 */
	public void setFieldSelection(String fieldSelection) {
		this.fieldSelection = fieldSelection;
	}

	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @param operator the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the closeParenthesis
	 */
	public String getCloseParenthesis() {
		return closeParenthesis;
	}

	/**
	 * @param closeParenthesis the closeParenthesis to set
	 */
	public void setCloseParenthesis(String closeParenthesis) {
		this.closeParenthesis = closeParenthesis;
	}
	
	/**
	 * @return the dataTypeForSelectedField
	 */
	public String getDataTypeForSelectedField() {
		return dataTypeForSelectedField;
	}

	/**
	 * @param dataTypeForSelectedField the dataTypeForSelectedField to set
	 */
	public void setDataTypeForSelectedField(String dataTypeForSelectedField) {
		this.dataTypeForSelectedField = dataTypeForSelectedField;
	}

	/**
	 * Returns TRUE, if the all query options are blank or null
	 * 
	 * @return boolean
	 */
	public boolean isEmptyCriteria() {
		return (isEmptyLogicalFunction()
				&& isFieldSelectionEmpty()
				&& isOperatorEmpty()
				&& isValueEmpty());
	}
	
	/**
	 * Returns True if the value is blank/Null/'N/A'
	 * @param value
	 * @return
	 */
	private boolean isEmpty(String value) {
		if (StringUtils.isBlank(value) || StringUtils.equalsIgnoreCase("N/A", value)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Returns TRUE, if the logicalFunction is blank or null
	 * 
	 * @return boolean
	 */
	public boolean isEmptyLogicalFunction() {
		return isEmpty(logicalFunction);	
	}
	
	/**
	 * Returns TRUE, if the fieldSelection is blank/null/'N/A'
	 * 
	 * @return boolean
	 */
	public boolean isFieldSelectionEmpty() {
		return isEmpty(fieldSelection);	
	}
	
	/**
	 * Returns TRUE, if the operator is blank/null/'N/A'
	 * 
	 * @return boolean
	 */
	public boolean isOperatorEmpty() {
		return isEmpty(operator);	
	}
	
	/**
	 * Returns TRUE, if the value is blank/null/'N/A'
	 * 
	 * @return boolean
	 */
	public boolean isValueEmpty() {
		return isEmpty(value);	
	}
	
	public String getMissingFields() {
		StringBuffer missingFileds = new StringBuffer();
		
		if (isEmptyLogicalFunction()){
			missingFileds.append("Logic");
		}
		
		if (isFieldSelectionEmpty()){
			if (missingFileds.length() > 0){
				missingFileds.append(", ");
			}
			missingFileds.append("Field");
		}
		
		if (isOperatorEmpty()){
			if (missingFileds.length() > 0){
				missingFileds.append(", ");
			}
			missingFileds.append("Operator");
		}
		
		if (isValueEmpty()){
			if (missingFileds.length() > 0){
				missingFileds.append(", ");
			}
			missingFileds.append("Value");
		}
		
		return missingFileds.toString();
	}
}
