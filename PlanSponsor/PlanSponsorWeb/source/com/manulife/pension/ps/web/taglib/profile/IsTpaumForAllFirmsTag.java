/*
 * Created on Oct 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.web.taglib.profile;


import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.jsp.JspException;

import com.manulife.pension.platform.web.taglib.util.TagUtils;
import com.manulife.pension.platform.web.taglib.util.ConditionalTagBase;
import com.manulife.pension.platform.web.taglib.util.BaseBundle;

import com.manulife.pension.ps.web.profiles.TpaumHelper;

/**
 * public class IsExternalUserTag extends ConditionalTagBase
 * 
 * Evaluates the body of the page 
 * 
 * @author Ludmila Stern
 */
public class IsTpaumForAllFirmsTag extends ConditionalTagBase {

	/**
	 * Evaluate the condition that is being tested by this particular tag,
	 * and return <code>true</code> if the nested body content of this tag
	 * should be evaluated, or <code>false</code> if it should be skipped.
	 * This method must be implemented by concrete subclasses.
	 *
	 * @exception JspException if a JSP exception occurs
	 */
	

	
	protected boolean condition() throws JspException {
		
		//System.out.println ("value = "+ value);
		//return (condition("true".equalsIgnoreCase(value)));
		return (condition(true));
	}
	/**
	 * @see ConditionalTagBase#condition()
	 */
	protected boolean condition(boolean desired) throws JspException {

		boolean tpaum = true;
		if (this.name == null) {
			JspException e =
				new JspException(BaseBundle.getMessage("IsExternalUser.noNameAttribute"));
			TagUtils.getInstance().saveException(pageContext, e);
			throw e;
		}

		Collection tpaFirms = null;
		
		if (this.property == null)
			tpaFirms = (ArrayList)TagUtils.getInstance().lookup(pageContext, name, scope);
		else
		 tpaFirms = (ArrayList)TagUtils.getInstance().lookup(pageContext, name, property, scope);
		
		if (tpaFirms == null) {
			JspException e =
				new JspException(BaseBundle.getMessage("IsTPAUMForAllFirmsTag no tpaFirms"));
			TagUtils.getInstance().saveException(pageContext, e);
			throw e;
			}
			   tpaum = TpaumHelper.isTPAUMForAllFirms(tpaFirms);
			 
			return (tpaum == desired);

	}


}