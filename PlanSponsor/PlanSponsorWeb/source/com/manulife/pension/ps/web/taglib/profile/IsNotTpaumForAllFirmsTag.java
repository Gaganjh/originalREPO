/*
 * Created on Nov 1, 2005
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
 * @author sternlu
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class IsNotTpaumForAllFirmsTag  extends ConditionalTagBase {

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
		return (condition(false));
	}
	/**
	 * @see ConditionalTagBase#condition()
	 */
	protected boolean condition(boolean desired) throws JspException {

		boolean tpaum = true;
		if (this.name == null) {
			JspException e =
				new JspException(BaseBundle.getMessage("IsNotTPAUMForAllFirmsTag.noNameAttribute"));
			TagUtils.getInstance().saveException(pageContext, e);
			throw e;
		}

		Collection tpaFirms = null;
		
		if (this.property == null)
			tpaFirms = (ArrayList)TagUtils.getInstance().lookup(pageContext, name, scope);
		else
			tpaFirms = (ArrayList)TagUtils.getInstance().lookup(pageContext, name, property, scope);
		
		//if(tpaFirms == null)
		//		return (tpaum == desired);
		/*
		for(Iterator it = tpaFirms.iterator();it.hasNext();)
		{
		  	TPAFirmInfo firmInfo = (TPAFirmInfo)it.next();
		  	if (firmInfo.getContractPermission().getRole() instanceof ThirdPartyAdministrator )  {
            	tpauam = false;
            	return (tpaum == desired);
		  }
		  }
		  */
		if (tpaFirms == null) {
		JspException e =
			new JspException("IsNotTPAUMForAllFirmsTag no tpaFirms");
		TagUtils.getInstance().saveException(pageContext, e);
		throw e;
		}
		   tpaum = TpaumHelper.isTPAUMForAllFirms(tpaFirms);
		 
		return (tpaum == desired);
	}
}
