package com.manulife.pension.ps.web.taglib.profile;

import javax.servlet.jsp.JspException;

import com.manulife.pension.platform.web.taglib.util.TagUtils;
import com.manulife.pension.platform.web.taglib.util.ConditionalTagBase;
import com.manulife.pension.platform.web.taglib.util.BaseBundle;
import com.manulife.pension.service.security.role.InternalUser;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.role.UserRole;

/**
 * public class IsExternalUserTag extends ConditionalTagBase
 * 
 * Evaluates the body of the page if
 * 
 * @author Ludmila Stern
 */
public class IsInternalOrTpaUserTag extends ConditionalTagBase {

	/**
	 * Evaluate the condition that is being tested by this particular tag, and
	 * return <code>true</code> if the nested body content of this tag should
	 * be evaluated, or <code>false</code> if it should be skipped. This
	 * method must be implemented by concrete subclasses.
	 * 
	 * @exception JspException
	 *                if a JSP exception occurs
	 */
	protected boolean condition() throws JspException {

		return (condition(true));

	}

	/**
	 * @see ConditionalTagBase#condition()
	 */
	protected boolean condition(boolean desired) throws JspException {

		if (this.name == null) {
			JspException e = new JspException(BaseBundle.getMessage("IsInternalOrTpaUserTag.noNameAttribute"));
			TagUtils.getInstance().saveException(pageContext, e);
			throw e;
		}
		boolean isInternalOrTpa = false;
		UserRole role = null;
		if (this.property == null) {
			role = (UserRole) TagUtils.getInstance().lookup(pageContext, name, scope);
		} else {
			role = (UserRole) TagUtils.getInstance().lookup(pageContext, name, property,
					scope);
		}

		/*
		 * Only internal user can have null role (No Access).
		 */
		if (role == null) {
			isInternalOrTpa = true;
		} else {
			isInternalOrTpa = role instanceof InternalUser
					|| role instanceof ThirdPartyAdministrator;
		}
		return (isInternalOrTpa == desired);
	}

}

