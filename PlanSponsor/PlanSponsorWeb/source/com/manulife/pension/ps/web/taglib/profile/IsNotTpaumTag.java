/*
 * Created on Nov 4, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.web.taglib.profile;

import javax.servlet.jsp.JspException;

import com.manulife.pension.platform.web.taglib.util.TagUtils;
import com.manulife.pension.platform.web.taglib.util.ConditionalTagBase;
import com.manulife.pension.platform.web.taglib.util.BaseBundle;

import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;

/**
 * @author sternlu
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class IsNotTpaumTag extends ConditionalTagBase {

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

	/**
	 * @see ConditionalTagBase#condition()
	 */
	protected boolean condition(boolean desired) throws JspException {

		if (this.name == null) {

			JspException e = new JspException(BaseBundle.getMessage("IsTpaUserTag.noNameAttribute"));
			TagUtils.getInstance().saveException(pageContext, e);
			throw e;
		}

		boolean isTpaum = false;
		UserRole role = null;

		if (this.property == null) {

			role = (UserRole)TagUtils.getInstance().lookup(pageContext, name, scope);
		} else {

			role = (UserRole)TagUtils.getInstance().lookup(pageContext, name, property, scope);
		}

		if (role != null) {

			 isTpaum = role.isTPA() && role.hasPermission(PermissionType.MANAGE_TPA_USERS);
		}

		return (isTpaum == desired);
	}

}
