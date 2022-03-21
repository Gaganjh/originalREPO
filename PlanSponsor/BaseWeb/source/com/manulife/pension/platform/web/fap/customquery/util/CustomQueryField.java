package com.manulife.pension.platform.web.fap.customquery.util;

import com.manulife.pension.platform.web.fap.constants.FapConstants;

/**
 * A bean to create Field selection values for the Custom Query
 * 
 * @author ayyalsa
 *
 */
public class CustomQueryField {

	public static final String BASIC_DATA_TYPE_DATE = "D";
	public static final String BASIC_DATA_TYPE_ALPHA = "A";
	public static final String BASIC_DATA_TYPE_NUMERIC = "N";
	public static final String BASIC_DATA_TYPE_INTEGER_AS_ALPHA = "IA";
	public static final String BASIC_DATA_TYPE_MSTAR = "MSTAR";
	public static final String BASIC_DATA_TYPE_FI360 = "FI360";
	public static final String BASIC_DATA_TYPE_RPAG = "RPAG";
	
	private String fieldId;
	private String sqlName;
	private String displayName;
	private String tableId;
	private String basicDataType;
	private String groupName;
	private String modifier;
	private boolean lookForNullValues;
	private boolean allowBlankValues;

	/**
	 * Empty constructor
	 */
	public CustomQueryField() {
	}
	
	/**
	 * Constructor
	 * 
	 * @param id
	 * @param name
	 */
	public CustomQueryField(String id, String name) {
		fieldId = id;
		displayName = name;
	}
	
	/**
	 * @return the fieldId
	 */
	public String getFieldId() {
		return fieldId;
	}

	/**
	 * @param fieldId the fieldId to set
	 */
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	/**
	 * @return the sqlName
	 */
	public String getSqlName() {
		return sqlName;
	}

	/**
	 * @param sqlName the sqlName to set
	 */
	public void setSqlName(String sqlName) {
		this.sqlName = sqlName;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the tableId
	 */
	public String getTableId() {
		return tableId;
	}

	/**
	 * @param tableId the tableId to set
	 */
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	/**
	 * @return the basicDataType
	 */
	public String getBasicDataType() {
		return basicDataType;
	}

	/**
	 * @param basicDataType the basicDataType to set
	 */
	public void setBasicDataType(String basicDataType) {
		this.basicDataType = basicDataType;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return the modifier
	 */
	public String getModifier() {
		return modifier;
	}

	/**
	 * @param modifier the modifier to set
	 */
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	/**
	 * @return the lookForNullValues
	 */
	public boolean isLookForNullValues() {
		return lookForNullValues;
	}

	/**
	 * @param lookForNullValues the lookForNullValues to set
	 */
	public void setLookForNullValues(boolean lookForNullValues) {
		this.lookForNullValues = lookForNullValues;
	}

	/**
	 * @return Returns TRUE, if the data type for this field is Alpha numeric 
	 */
	public boolean isIntegerAsAlpha() {
		return FapConstants.BASIC_DATA_TYPE_INTEGER_AS_ALPHA.equals(getBasicDataType());
	}
	
	/**
	 * @return Returns TRUE, if the data type for this field is Alpha
	 */
	public boolean isAlpha() {
		return FapConstants.BASIC_DATA_TYPE_ALPHA.equals(getBasicDataType());
	}

	/**
	 * @return the allowBlankValues
	 */
	public boolean isAllowBlankValues() {
		return allowBlankValues;
	}

	/**
	 * @param allowBlankValues the allowBlankValues to set
	 */
	public void setAllowBlankValues(boolean allowBlankValues) {
		this.allowBlankValues = allowBlankValues;
	}
}
