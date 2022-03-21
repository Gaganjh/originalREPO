package com.manulife.pension.platform.web.controller;

import javax.servlet.http.HttpServletRequest;



/**
 * Base action form class for forms with action parameter
 * 
 * @see BaseForm
 */
public class AutoForm extends BaseForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String DEFAULT = "default";

	protected String action = DEFAULT;

	/**
	 * Default Constructor
	 */
	public AutoForm() {
	}

	/**
	 * Gets the action
	 * 
	 * @return Returns a String
	 */
	public String getAction() {
		if (action == null) {
			action = DEFAULT;
		}
		return action;
	}

	/**
	 * Tells if it is "default" action
	 */
	public boolean isDefaultAction() {
		return getAction().equals(DEFAULT);
	}

	/**
	 * Sets the action
	 * 
	 * @param action
	 *            The action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @see org.apache.struts.action.Form#reset(org.apache.struts.action.ActionMapping,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	public void reset(HttpServletRequest arg1) {
		action = DEFAULT;
	}
}