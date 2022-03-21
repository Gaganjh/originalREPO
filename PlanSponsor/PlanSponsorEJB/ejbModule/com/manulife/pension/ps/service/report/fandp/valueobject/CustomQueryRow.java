package com.manulife.pension.ps.service.report.fandp.valueobject;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * This value object is used by the CustomQuerySavedData to represent a single
 * row of a "Custom Query" that a user has saved or will save to the database.
 * Each parameter matches a field on the Funds and Performance JSP for the
 * Custom Query section. An ArrayList of these objects represents the whole
 * query.
 * 
 * @author Mark Eldridge
 * @version initial (Feb 2009)
 * @see CustomQuerySavedData
 * @see UserSavedData
 **/

public class CustomQueryRow implements Serializable {

    private static final long serialVersionUID = -5353034539758620142L;

    private String logic = "";

    private String leftBracket = "";

    private int fieldId = -1;

    private String operator = "";

    private String value = "";

    private String rightBracket = "";

    private String dataTypeForSelectedField;
    
    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }

    public String getLeftBracket() {
        return leftBracket;
    }

    public void setLeftBracket(String leftBracket) {
        this.leftBracket = leftBracket;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRightBracket() {
        return rightBracket;
    }

    public void setRightBracket(String rightBracket) {
        this.rightBracket = rightBracket;
    }

    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
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
	 * @param ignoreBlankValidation - true if the validation should allow " "
	 * 
	 * @return
	 */
	private boolean isEmpty(String value, boolean ignoreBlankValidation) {
		if ((!ignoreBlankValidation && StringUtils.isBlank(value)) 
				|| (ignoreBlankValidation && StringUtils.isEmpty(value))
				|| StringUtils.equalsIgnoreCase("N/A", value)) {
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
		return isEmpty(logic, false);	
	}
	
	/**
	 * Returns TRUE, if the fieldSelection is blank/null/'N/A'
	 * 
	 * @return boolean
	 */
	public boolean isFieldSelectionEmpty() {
		return (fieldId == -1);	
	}
	
	/**
	 * Returns TRUE, if the operator is blank/null/'N/A'
	 * 
	 * @return boolean
	 */
	public boolean isOperatorEmpty() {
		return isEmpty(operator, false);	
	}
	
	/**
	 * Returns TRUE, if the value is blank/null/'N/A'
	 * 
	 * @return boolean
	 */
	public boolean isValueEmpty() {
		if (!isFieldSelectionEmpty() && this.fieldId == 19) {
			return isEmpty(value, true);
		} else {
			return isEmpty(value, false);	
		}
	}
	
	/**
	 * Returns TRUE, if the value is blank/null/'N/A'
	 * 
	 * @return boolean
	 */
	public boolean isSelectValueInValid() {
		if (!isFieldSelectionEmpty()
				&& (this.fieldId == 20 || this.fieldId == 21 || this.fieldId == 22)) {
			if ("Not Scored".equals(value)
					&& ("<".equals(operator)
							|| "<=".equals(operator)
							|| ">".equals(operator) 
							|| ">=".equals(operator))) {
				return true;
			}
		}
		return false;

	}
	
	public String getMissingFields(int rowIndex) {
		StringBuffer missingFileds = new StringBuffer();
		
		if (rowIndex != 0) {
			if (isEmptyLogicalFunction()){
				missingFileds.append("Logic");
			}
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
		
		if (isSelectValueInValid()){
			if (missingFileds.length() > 0){
				missingFileds.append(", ");
			}
			missingFileds.append("Combination of Operator & Value");
		}
		
		return missingFileds.toString();
	}

}
