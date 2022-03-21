package com.manulife.pension.platform.web.process;

import javax.servlet.http.HttpServletRequest;

import com.manulife.pension.exception.SystemException;

/**
 * An interface a process action must implement It defines the ProcessState that
 * the action supports, and the method to return the ProcessContext that this
 * action is involved with.
 * 
 * @author guweigu
 * 
 */
public interface ProcessController {
	/**
	 * The state this action can deal with
	 * 
	 * @return
	 */
	ProcessState getState();

	/**
	 * Retrieve the current process context
	 * 
	 * @param request
	 * @return
	 */
	ProcessContext getProcessContext(HttpServletRequest request)
			throws SystemException;
}
