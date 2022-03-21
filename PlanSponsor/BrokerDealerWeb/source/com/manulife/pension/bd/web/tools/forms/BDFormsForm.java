package com.manulife.pension.bd.web.tools.forms;

import com.manulife.pension.platform.web.controller.AutoForm;

/**
 * store the values for bdform page
 * 
 * @author ambroar
 * 
 */
public class BDFormsForm extends AutoForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 0L;

	private String selectedMenuId;

	/**
	 * Constructor for ParicipantSummaryReportForm
	 */
	public BDFormsForm() {
		super();
	}

	/**
	 * @return the selectedMenuId
	 */
	public String getSelectedMenuId() {
		return selectedMenuId;
	}

	/**
	 * @param selectedMenuId
	 *            the selectedMenuId to set
	 */
	public void setSelectedMenuId(String selectedMenuId) {
		this.selectedMenuId = selectedMenuId;
	}

}
