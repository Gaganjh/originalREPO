package com.manulife.pension.bd.web.bob.investment;


/*
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

import javax.servlet.http.HttpServletRequest;



public interface CloneableForm {
	public abstract void clear( HttpServletRequest request);

	public abstract CloneableForm getClonedForm();

	public abstract Object clone();

	public abstract void storeClonedForm();
}