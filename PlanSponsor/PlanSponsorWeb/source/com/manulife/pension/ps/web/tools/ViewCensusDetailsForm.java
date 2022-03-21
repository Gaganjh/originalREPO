package com.manulife.pension.ps.web.tools;

import com.manulife.pension.ps.web.report.ReportForm;


/**
 * @author parkand
 *
 */
public class ViewCensusDetailsForm extends ReportForm {
	private String subNo;
	private String mode;
	private String action;
	
	private static final String EDIT_MODE_IND = "e";
	
	/**
	 * @return Returns the subNo.
	 */
	public String getSubNo() {
		return subNo;
	}
	/**
	 * @param subNo The subNo to set.
	 */
	public void setSubNo(String subNo) {
		this.subNo = subNo;
	}
	/**
	 * @return Returns the mode.
	 */
	public String getMode() {
		return mode;
	}
	/**
	 * @param mode The mode to set.
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	public boolean isEditMode() {
		if ( EDIT_MODE_IND.equals(mode) ) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @return Returns the action.
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action The action to set.
	 */
	public void setAction(String action) {
		this.action = action;
	}
}
