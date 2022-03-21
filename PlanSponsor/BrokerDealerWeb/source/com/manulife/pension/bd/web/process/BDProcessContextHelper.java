package com.manulife.pension.bd.web.process;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.manulife.pension.platform.web.process.ProcessContext;

/**
 * The Helper class that deals with getting/setting ProcessContext.
 * 
 * The encapsulation here makes it possible to enforce things like in one
 * session, allow only one ProcessContext.
 * 
 * @author guweigu
 * 
 */
public class BDProcessContextHelper {
	/**
	 * Get the ProcessContext by the processName from the session
	 * 
	 * @param request
	 * @param processName
	 * @return
	 */
	public static ProcessContext getProcessContext(HttpServletRequest request,
			String processName) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			return (ProcessContext) session.getAttribute(processName);
		} else {
			return null;
		}
	}

	/**
	 * Set the ProcessContext into Session by the processName
	 * @param request
	 * @param processName
	 * @param context
	 */
	public static void setProcessContext(HttpServletRequest request,
			String processName, ProcessContext context) {
		request.getSession().setAttribute(processName, context);
	}

	/**
	 * Clear the ProcessContext from the Session
	 * 
	 * @param request
	 * @param processName
	 */
	public static void clearProcessContext(HttpServletRequest request,
			String processName) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(processName);
		}
	}
}
