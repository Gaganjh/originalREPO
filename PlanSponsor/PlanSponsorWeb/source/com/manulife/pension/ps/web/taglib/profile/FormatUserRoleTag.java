package com.manulife.pension.ps.web.taglib.profile;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.platform.web.taglib.util.TagUtils;

import com.manulife.pension.ps.web.profiles.AccessLevelHelper;
import com.manulife.pension.service.security.role.InternalUserManager;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.UserRoleFactory;

/**
 * This tag returns the display string given the string literal 
 * of the user role.
 * 
 * @author:	Maria Lee   
 * 
 */
public class FormatUserRoleTag extends TagSupport 
{

	private String name;
	private String value;
	private String property;
	private String scope;
	private String site;

	public int doStartTag() throws JspException 
	{
		String displayName = null;
		try 
		{	
			/* take the value from name & property if value is not provided */
			if (value == null) 
			{
				value = (String) TagUtils.getInstance().lookup(pageContext, name,
				property, scope);
			}
			/* if site is not provided, take plan sponsor site role as default */
			if (site == null) 
				displayName = getPlanSponsorSiteRole(value);
			else
				displayName = getEzkSiteRole(value);

			pageContext.getOut().print(displayName);
			
		} catch (IOException ex) {
			throw new JspException(ex.getMessage());
		}
		return SKIP_BODY;
	}
	
	private String getPlanSponsorSiteRole(String value) 
	{
		UserRole userRole = UserRoleFactory.getUserRole(value);
		if (userRole == null) 
		{
			return AccessLevelHelper.NO_ACCESS_STRING;
		} else {
			return userRole.getDisplayName();
		}	
	}
	
	private String getEzkSiteRole(String value) 
	{	
		if (value == null || value.length() == 0)
			return AccessLevelHelper.NO_ACCESS_STRING;

		List accessLevels = AccessLevelHelper.getInstance()
							.getEzkAccessLevels(
								new InternalUserManager());
								
		for (Iterator it=accessLevels.iterator(); it.hasNext();)
		{	
			LabelValueBean bean = (LabelValueBean) it.next();
			if (bean.getValue().equalsIgnoreCase(value))
				return bean.getLabel();
		}
		return AccessLevelHelper.NO_ACCESS_STRING;		
	}
	
	/**
	 * Gets the name
	 * @return Returns a String
	 */
	public String getName() {
		return name;
	}
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
	 * Gets the property
	 * @return Returns a String
	 */
	public String getProperty() {
		return property;
	}
	/**
	 * Sets the property
	 * @param property The property to set
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * Gets the site
	 * @return Returns a String
	 */
	public String getSite() {
		return site;
	}
	/**
	 * Sets the site
	 * @param site The site to set
	 */
	public void setSite(String site) {
		this.site = site;
	}

}

