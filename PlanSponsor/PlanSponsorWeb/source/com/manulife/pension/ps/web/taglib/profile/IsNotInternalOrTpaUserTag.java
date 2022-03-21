package com.manulife.pension.ps.web.taglib.profile;

import javax.servlet.jsp.JspException;

public class IsNotInternalOrTpaUserTag extends IsInternalOrTpaUserTag{
	
	
	
	
		/**
	 * Evaluate the condition that is being tested by this particular tag,
	 * and return <code>true</code> if the nested body content of this tag
	 * should be evaluated, or <code>false</code> if it should be skipped.
	 * This method must be implemented by concrete subclasses.
	 *
	 * @exception JspException if a JSP exception occurs
	 */
	protected boolean condition() throws JspException {

		return (condition(false));

	}

}

