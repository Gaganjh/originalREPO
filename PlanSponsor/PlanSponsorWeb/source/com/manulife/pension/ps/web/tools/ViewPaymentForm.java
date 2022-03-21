/*
 * Created on Nov 15, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.manulife.pension.ps.web.tools;

import com.manulife.pension.platform.web.controller.AutoForm;

/**
 * @author tomasto
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ViewPaymentForm extends AutoForm {
	
	private String subNo;
	private boolean isAllowedView = false;

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
	 * @return Returns the isAllowedView.
	 */
	public boolean isAllowedView() {
		return isAllowedView;
	}
	/**
	 * @param isAllowedView The isAllowedView to set.
	 */
	public void setAllowedView(boolean isAllowedView) {
		this.isAllowedView = isAllowedView;
	}
}
