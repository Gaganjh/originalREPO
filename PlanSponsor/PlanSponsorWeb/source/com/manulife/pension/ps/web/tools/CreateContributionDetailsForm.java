/*
 * Created on Feb 8, 2005
 *
 */
package com.manulife.pension.ps.web.tools;

import com.manulife.pension.ps.web.controller.PsForm;

/**
 * @author Jim Adamthwaite
 *
 */
public class CreateContributionDetailsForm extends PsForm {
	
	private String subNo;

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
}
