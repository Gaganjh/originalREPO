package com.manulife.pension.platform.web.secureDocumentUpload;

import javax.servlet.http.HttpServletRequest;

public class SDUHistoryTabForm extends SDUReportForm {

	private static final long serialVersionUID = 1L;
	
	private static final String NO_VALUE_INDICATOR = "-1";
	private boolean justMine=false;	
	private boolean justMineFilter=false;
	private String filterStartDate;
	private String filterEndDate;
	private boolean isPendingContract = false;	
	

	public SDUHistoryTabForm() {
		super();
	}
	
	public boolean isJustMineFilter() {
		return justMineFilter;
	}


	public void setJustMineFilter(boolean justMineFilter) {
		this.justMineFilter = justMineFilter;
	}

	/**
	 * @return Returns the justMine.
	 */
	public boolean isJustMine() {
		return justMine;
	}
	/**
	 * @param justMine The justMine to set.
	 */
	public void setJustMine(boolean justMine) {
		this.justMine = justMine;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Form#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void reset( HttpServletRequest arg1) {
		// TODO Auto-generated method stub
		//super.reset(arg0, arg1);
		justMine = false;
	}

	/**
	 * @return Returns the filterEndDate.
	 */
	public String getFilterEndDate() {
		return filterEndDate;
	}

	/**
	 * @param filterEndDate The filterEndDate to set.
	 */
	public void setFilterEndDate(String filterEndDate) {
		this.filterEndDate = filterEndDate;
	}

	/**
	 * @return Returns the filterStartDate.
	 */
	public String getFilterStartDate() {
		return filterStartDate;
	}

	/**
	 * @param filterStartDate The filterStartDate to set.
	 */
	public void setFilterStartDate(String filterStartDate) {
		this.filterStartDate = filterStartDate;
	}

	/**
	 * Convenience method for testing if a certain field has been set.
	 *
	 * @param value - object to test
	 * @return true is the value passed in is not null and different from the no-value place holder
	 */
	public static boolean isFieldSet(Object value) {
		return value != null && !value.equals(NO_VALUE_INDICATOR);
	}
	public boolean isPendingContract() {
		return isPendingContract;
	}
	public void setPendingContract(boolean isPendingContract) {
		this.isPendingContract = isPendingContract;
	}	
}