package com.manulife.pension.ps.web.taglib.profile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.manulife.pension.platform.web.taglib.util.TagUtils;
import com.manulife.pension.platform.web.taglib.util.ConditionalTagBase;
import com.manulife.pension.platform.web.taglib.util.BaseBundle;

import com.manulife.pension.ps.web.controller.SecurityManager;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.security.role.UserRole;

/**
 * public class ManageUserProfileTag extends ConditionalTagBase
 * 
 * Evaluates or skips the body of the tag depending on specific role permission
 * Divides users in 4 categories depending what type of other users
 * they can manage
 * 1.manage external-  includes PUM, IUM,CAR,SCAR,TL
 * 2.manage internal -  IUM
 * 3.manage tpa  includes  IUM,CAR,SCAR,TL, does not include PUM 
 * 4. if none of the above - then it is external user who can only manage herself/himself
 * The tag looks up in SecurityManager for the specific access
 * 
 * @author Ludmila Stern
 */
public class ManageUserProfileTag extends ConditionalTagBase {

	private String name;
	private String value;
	private String property;
	private String scope;

	/**
	 * Sets the name
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the value
	 * @return Returns a String
	 */
	public String getValue() {
		return value;
	}
	/**
	 * Sets the value
	 * @param value The value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Gets the property
	 * @return Returns a String
	 */
	public String getProperty() {
		return property;
	}
	/**
	 * Sets the property
	 * @param vroperty The property to set
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * Gets the scope
	 * @return Returns a String
	 */
	public String getScope() {
		return scope;
	}
	/**
	 * Sets the scope
	 * @param scope The scope to set
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

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
				new JspException(BaseBundle.getMessage("manageProfile.noNameAttribute"));
			TagUtils.getInstance().saveException(pageContext, e);
			throw e;
		}
		boolean canManage = true;
		UserRole role = null;
		if (this.property == null) {
			role = (UserRole) TagUtils.getInstance().lookup(pageContext, name, scope);
		} else {
			role = (UserRole) TagUtils.getInstance().lookup(pageContext, name, property, scope);
		}

		if (role != null) {
			/*
					AccessLevelHelper helper = AccessLevelHelper.getInstance();
					canManage = helper.canManage(role,value);
			*/

			if (SecurityManager.MANAGE_INTERNAL.equals(value))
			{
				canManage = SecurityManager.canManageInternal(role);
			}
			else if (SecurityManager.MANAGE_TPA.equals(value))
			{
				canManage = SecurityManager.canManageTPA(role);
			}
			else if (SecurityManager.MANAGE_EXTERNAL.equals(value))
			{
				canManage = SecurityManager.canManageExternal(role);
			}
			else
			{
				UserProfile managingUserProfile = SessionHelper.getUserProfile((HttpServletRequest)pageContext.getRequest());
				canManage = SecurityManager.canManage(managingUserProfile.getPrincipal(), role);
			}
			
		}
		return (canManage == desired);
	}

}