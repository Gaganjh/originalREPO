package com.manulife.pension.ps.web.investment;

/*
  File: ContractFundsForm.java

  Version   Date         Author           Change Description
  -------   ----------   --------------   ------------------------------------------------------------------
  CS1.0     2004-01-01   Chris Shin       Initial version.
*/


import java.util.Date;

import com.manulife.pension.ps.web.controller.PsForm;


/**
 * This class is the action form for the Contract Funds form
 * 
 * @author   Chris Shin
 * @version  CS1.0  (March 1, 2004)
 **/

public class ContractFundsForm extends PsForm {

	private Date asOfDate;
	private String selectedView;
	private String selectedViewBy;
	private String currentColumnSort;
	private String currentSortDirection;
	
	public static final String VIEW_SELECTED = "selected";
	public static final String VIEW_AVAILABLE = "available";

	/**
	 * Gets the as of date
	 * @return Returns a String
	 */
	public Date getAsOfDate() {
		return this.asOfDate;
	}

	/**
	 * Sets the asOfDate
	 * @param asOfDate The asOfDate to set
	 */
	public void setAsOfDate(Date asOfDate) {
		this.asOfDate = asOfDate;
	}
	/**
	 * Gets the selected View
	 * @return Returns a String
	 */
	public String getSelectedView() {
		return this.selectedView;
	}
	/**
	 * Sets the selected View
	 * @param selectedView The selected View to set
	 */
	public void setSelectedView(String selectedView) {
		this.selectedView = selectedView;
	}
	
	/**
	 * Gets the selected ViewBy
	 * @return Returns a String
	 */
	public String getSelectedViewBy() {
		return this.selectedViewBy;
	}
	/**
	 * Sets the selected ViewBy
	 * @param selectedView The selected ViewBy to set
	 */
	public void setSelectedViewBy(String selectedViewBy) {
		this.selectedViewBy = selectedViewBy;
	}

}

