package com.manulife.pension.bd.web.process;

import com.manulife.pension.ezk.web.ActionForm;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.process.ProcessContext;

/**
 * The wizard style process context
 * @author guweigu
 *
 */
abstract public class BDWizardProcessContext extends ProcessContext {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String ACTION_CONTINUE = "continue";
	public static final String ACTION_CANCEL = "cancel";
	
	/**
	 * Update the context from the submitted form
	 * @param form
	 * @throws SystemException
	 */
	abstract public void updateContext(ActionForm form) throws SystemException;
	
	/**
	 * Populate the form from the context object
	 * @param form
	 * @throws SystemException
	 */
	abstract public void populateForm(ActionForm form) throws SystemException;
}
