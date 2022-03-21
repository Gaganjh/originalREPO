package com.manulife.pension.ps.web.taglib.profile;

import javax.servlet.jsp.JspException;

import com.manulife.pension.platform.web.taglib.util.TagUtils;
import com.manulife.pension.platform.web.taglib.util.ConditionalTagBase;
import com.manulife.pension.platform.web.taglib.util.BaseBundle;

import com.manulife.pension.ps.web.profiles.AccessLevelHelper;
import com.manulife.pension.service.security.role.JhtcOfficer;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;

/**
 * public class IsJhtcOfficerUserTag extends ConditionalTagBase
 * 
 * Evaluates the body of the page if 
 * @author munusra
 */
public class  IsJhtcOfficerUserTag extends ConditionalTagBase {


	/**
	 * Evaluate the condition that is being tested by this particular tag,
	 * and return <code>true</code> if the nested body content of this tag
	 * should be evaluated, or <code>false</code> if it should be skipped.
	 * This method must be implemented by concrete subclasses.
	 *
	 * @exception JspException if a JSP exception occurs
	 */
	protected boolean condition() throws JspException {

		return (condition(true));

	}
	/**
	 * @see ConditionalTagBase#condition()
	 */
	protected boolean condition(boolean desired) throws JspException {

		if (this.name == null) {
			JspException e =
				new JspException(BaseBundle.getMessage("IsJhtcOfficerUserTag.noNameAttribute"));
			TagUtils.getInstance().saveException(pageContext, e);
			throw e;
		}
		boolean isJhtc = false;
		Object role = null;
		if (this.property == null) {
			role = TagUtils.getInstance().lookup(pageContext, name, scope);
		} else {
			role = TagUtils.getInstance().lookup(pageContext, name, property, scope);
		}

		if (role != null) {
			if (role instanceof String) {
				isJhtc = AccessLevelHelper.JHTC_OFFICER_ACCESS.equalsIgnoreCase((String)role);	
			} else {
				isJhtc = role instanceof JhtcOfficer;	
			}
		}
		return (isJhtc == desired);
	}

}
