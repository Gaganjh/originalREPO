package com.manulife.pension.platform.web.fap.customquery.util;

import java.util.List;

/**
 * A bean to create operators for the Custom Query
 * 
 * @author ayyalsa
 *
 */
public class CustomQueryOperator {
	
	private String id;
	private String label;
	private List<String> applicableDataTypes;
	private String equivalentSqlOperator;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param label
	 * @param equivalentSqlOperator
	 * @param applicableDataTypes
	 */
	public CustomQueryOperator(String id, String label,String equivalentSqlOperator,
			List<String> applicableDataTypes) {
		this.id = id;
		this.label = label;
		this.applicableDataTypes = applicableDataTypes;
		this.equivalentSqlOperator = equivalentSqlOperator;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return the applicableDataTypes
	 */
	public List<String> getApplicableDataTypes() {
		return applicableDataTypes;
	}
	/**
	 * @param applicableDataTypes the applicableDataTypes to set
	 */
	public void setApplicableDataTypes(List<String> applicableDataTypes) {
		this.applicableDataTypes = applicableDataTypes;
	}
	/**
	 * @return the equivalentSqlOperator
	 */
	public String getEquivalentSqlOperator() {
		return equivalentSqlOperator;
	}
	/**
	 * @param equivalentSqlOperator the equivalentSqlOperator to set
	 */
	public void setEquivalentSqlOperator(String equivalentSqlOperator) {
		this.equivalentSqlOperator = equivalentSqlOperator;
	}
}
