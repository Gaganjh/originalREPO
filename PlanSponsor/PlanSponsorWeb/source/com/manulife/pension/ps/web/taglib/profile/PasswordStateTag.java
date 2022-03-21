package com.manulife.pension.ps.web.taglib.profile;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.manulife.pension.platform.web.taglib.util.TagUtils;

import com.manulife.pension.service.security.utility.SecurityConstants;
/**
 * This tag returns the display of the password status image given the password status
 * 
 * 
 * @author:	Ludmila Stern
 * 
 */
public class PasswordStateTag  extends TagSupport {

	private String name;
	private String value;
	private String property;
	private String scope;

	public int doStartTag() throws JspException {
		
		try 
		{
			if (value == null) 
			{
				value = (String) TagUtils.getInstance().lookup(pageContext, name,
				property, scope);
			}
			if (SecurityConstants.LOCKED_PASSWORD_STATUS.equals(value)) {
				pageContext.getOut().print("Locked");
			} else if (SecurityConstants.RESET_PASSWORD_STATUS.equals(value)){
				pageContext.getOut().print("Reset");
			} else if (SecurityConstants.NOT_APPLICABLE.equals(value)){
				pageContext.getOut().print("n/a");
			} else {
				pageContext.getOut().print("Active");

			}
		
		} catch (IOException ex) {
			throw new JspException(ex.getMessage());
		}
		return SKIP_BODY;
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


}



