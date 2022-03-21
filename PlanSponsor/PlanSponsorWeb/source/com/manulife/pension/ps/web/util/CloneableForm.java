/*
 * Created on Feb 15, 2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.manulife.pension.ps.web.util;

import javax.servlet.http.HttpServletRequest;



/**
 * @author Tony Tomasone
 *
 */
public interface CloneableForm {
	public  void clear(HttpServletRequest request);

	public abstract CloneableForm getClonedForm();

	public abstract Object clone();

	public abstract void storeClonedForm();
}