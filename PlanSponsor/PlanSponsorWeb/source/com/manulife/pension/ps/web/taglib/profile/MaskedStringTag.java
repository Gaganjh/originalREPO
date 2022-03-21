package com.manulife.pension.ps.web.taglib.profile;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.manulife.pension.platform.web.taglib.util.TagUtils;
/**
 * This tag returns the display/masked version of challenge answer
 * 
 * @author:	Ludmila Stern
 * 
 */
public class MaskedStringTag extends TagSupport {

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
		StringBuffer strbf = new StringBuffer();
			int length = value.length();
			if(length >0){
				for(int i =0; i<length; i++) {
					strbf.append("*");
				}
			}
			else {
				strbf.append("");
			}
			pageContext.getOut().print(strbf.toString());
			
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
	 * Gets the valueBeanProperty
	 * @return Returns a String
	 */
	public String getVProperty() {
		return property;
	}
	/**
	 * Sets the valueBeanProperty
	 * @param valueBeanProperty The valueBeanProperty to set
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

