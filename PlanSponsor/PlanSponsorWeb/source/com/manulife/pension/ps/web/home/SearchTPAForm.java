package com.manulife.pension.ps.web.home;

import com.manulife.pension.ps.web.report.ReportForm;

/**
 * FormBean for Search TPA
 * 
 * @author gullara
 *
 */
public class SearchTPAForm extends ReportForm {

	
	private static final long serialVersionUID = 1L;

	public static final String FIELD_FILTER_VALUE = "filterValue";

	private String filter;

	private String filterValue;
	
	private String tpaUserName;
	
	private String tpaFirmName;
	
	private String tpaFirmId;
	
	private String filterYes;
	
	private String sortYes;
	
	private String storedProcExecute;
	
	/**
	 * Constructor.
	 */
	public SearchTPAForm() {
		super();
	}

	/**
	 * @return Returns the filterType.
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * @param filter
	 *            The filter to set.
	 */
	public void setFilter(String filter) {
		this.filter = trim(filter);
	}

	/**
	 * @return Returns the filterValue.
	 */
	public String getFilterValue() {
		return filterValue;
	}

	/**
	 * @param filterValue
	 *            The filterValue to set.
	 */
	public void setFilterValue(String filterValue) {
		this.filterValue = trim(filterValue);
	}
	/**
	 * @see ReportForm#clear()
	 */
 	public void clear() {
		super.clear();
		setFilter(null);
		setFilterValue(null);
	}

	public String getTpaFirmId() {
		return tpaFirmId;
	}

	public void setTpaFirmId(String tpaFirmId) {
		this.tpaFirmId = tpaFirmId;
	}

	public String getTpaFirmName() {
		return tpaFirmName;
	}

	public void setTpaFirmName(String tpaFirmName) {
		this.tpaFirmName = tpaFirmName;
	}

	public String getTpaUserName() {
		return tpaUserName;
	}

	public void setTpaUserName(String tpaUserName) {
		this.tpaUserName = tpaUserName;
	}

	

	public String getSortYes() {
		return sortYes;
	}

	public void setSortYes(String sortYes) {
		this.sortYes = sortYes;
	}

	public String getFilterYes() {
		return filterYes;
	}

	public void setFilterYes(String filterYes) {
		this.filterYes = filterYes;
	}

	public String getStoredProcExecute() {
		return storedProcExecute;
	}

	public void setStoredProcExecute(String storedProcExecute) {
		this.storedProcExecute = storedProcExecute;
	}
	
	/**
	 * resetting all the values
	 *
	 */
	public void reset(){
		this.tpaFirmId = null;
		this.tpaFirmName = null;
		this.tpaUserName = null;
		this.filter = null;
		this.filterValue =null;
		this.filterYes = null;
		this.sortYes = null;
	}

}
